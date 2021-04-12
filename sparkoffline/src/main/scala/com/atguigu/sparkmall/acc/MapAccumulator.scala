package com.atguigu.sparkmall.acc

import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable


/**
  * 定义的累加器类。
  */
class MapAccumulator extends AccumulatorV2[(String,String) , mutable.Map[(String , String),Long]]{

  val map = mutable.Map[(String , String) , Long]()


  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[(String, String), mutable.Map[(String, String), Long]] = {
    val accumulator = new MapAccumulator
    map.synchronized{
      accumulator.map ++= map
    }
    accumulator
  }

  override def reset(): Unit = map.clear()

  override def add(v: (String, String)): Unit = {
    map(v) = map.getOrElseUpdate(v , 0) + 1
  }

  override def merge(other: AccumulatorV2[(String, String), mutable.Map[(String, String), Long]]): Unit = {
    //将other的数据 和当前 map的数据进行合并
    other.value.foreach{
      kv => map.put(kv._1 , map.getOrElse(kv._1 ,0) + kv._2)
    }
  }

  override def value: mutable.Map[(String, String), Long] = {
    map
  }

}
