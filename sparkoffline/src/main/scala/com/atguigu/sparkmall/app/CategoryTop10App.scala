package com.atguigu.sparkmall.app

import com.atguigu.sparkmall.acc.MapAccumulator
import com.atguigu.sparkmall.common.bean.UserVisitAction
import com.atguigu.sparkmall.common.bean.CategoryCountInfo
import com.atguigu.sparkmall.common.util.JDBCUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.collection.mutable

object CategoryTop10App {

  // 统计热门商品 top10
  def statCategoryTop10(spark:SparkSession , userVisitActionRDD:RDD[UserVisitAction]
                       , taskId:String): Unit ={


    // 注册累加器
    val acc = new MapAccumulator
    spark.sparkContext.register(acc,"CategoryHotAcc")


    // 遍历日志进行累加
    userVisitActionRDD.foreach{
      visitAction => {

        if(visitAction.click_category_id != -1){
          acc.add(visitAction.click_category_id.toString , "click")
        }else if(visitAction.order_category_ids != null){
          visitAction.order_category_ids.split(",").foreach{
            oid => acc.add(oid , "order")
          }
        }else if(visitAction.pay_category_ids != null){
          visitAction.pay_category_ids.split(",").foreach{
            payid => acc.add(payid, "pay")
          }
        }
      }
    }

    // 遍历完成 之后 对 累加器的数据进行处理

    //按照 cateforyid 和 操作类型 分组
    val categoryAndType: Map[String, mutable.Map[(String, String), Long]] = acc.value.groupBy(_._1._1)

    // 聚合成categroyCountInfo的集合
    val categoryList:List[CategoryCountInfo] = categoryAndType.map{
      case(cid , actionMap) => CategoryCountInfo(taskId,
        cid,
        actionMap.getOrElse((cid, "click"), 0),
        actionMap.getOrElse((cid, "order"), 0),
        actionMap.getOrElse((cid, "pay"), 0))
    }.toList


    // 按照 点击  下单  支付的顺序 降序 排序
    val categroySortList:List[CategoryCountInfo] = categoryList.sortBy(info => (info.clickCount , info.orderCount , info.payCount))(Ordering.Tuple3(
      Ordering.Long.reverse , Ordering.Long.reverse , Ordering.Long.reverse
    ))

    // 取 前10的数据
    val infoes: List[CategoryCountInfo] = categroySortList.take(10)


    // 将数据写入 mysql
    val top10: List[Array[Any]] = infoes.map(info => Array(info.taskId , info.categoryId, info.clickCount, info.orderCount , info.payCount))
    JDBCUtil.executeBatchUpdate("insert into  category_top10 values (?,?,?,?,?) ", top10)





  }

}
