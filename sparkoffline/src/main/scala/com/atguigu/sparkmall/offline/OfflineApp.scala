package com.atguigu.sparkmall.offline


import java.util.UUID

import com.atguigu.sparkmall._
import com.alibaba.fastjson.JSON
import com.atguigu.sparkmall.common.bean.{Condition, UserVisitAction}
import com.atguigu.sparkmall.common.util.ConfigurationUtil
import org.apache.spark.sql.SparkSession
/**
  * 离线模块的入口类
  */
object OfflineApp {
  def main(args: Array[String]): Unit = {


    val spark:SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("OfflineApp")
      .enableHiveSupport()
      .config("spark.sql.warehouse.dir", "hdfs://hadoop201:9000/user/hive/warehouse")
      .getOrCreate()






  }



  /**
    * 读取指定条件的 UserVisitActionRDD
    *
    * @param spark
    * @param condition
    */
  def readUserVisitActionRDD(spark: SparkSession, condition: Condition) = {
    var sql = s"select v.* from user_visit_action v join user_info u on v.user_id=u.user_id where 1=1"
    if (isNotEmpty(condition.startDate)) {
      sql += s" and v.date>='${condition.startDate}'"
    }
    if (isNotEmpty(condition.endDate)) {
      sql += s" and v.date<='${condition.endDate}'"
    }

    if (condition.startAge != 0) {
      sql += s" and u.age>=${condition.startAge}"
    }
    if (condition.endAge != 0) {
      sql += s" and u.age<=${condition.endAge}"
    }

    import spark.implicits._
    spark.sql("use sparkmall")
    spark.sql(sql).as[UserVisitAction].rdd
  }


  /**
    * 读取过滤条件
    *
    * @return
    */
  def readConditions: Condition = {
    // 读取配置文件
    val config = ConfigurationUtil("conditions.properties")
    // 读取到其中的 JSON 字符串
    val conditionString = config.getString("condition.params.json")
    // 解析成 Condition 对象
    JSON.parseObject(conditionString, classOf[Condition])
  }

}
