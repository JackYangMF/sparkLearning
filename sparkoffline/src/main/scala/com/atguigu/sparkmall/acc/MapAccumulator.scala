package com.atguigu.sparkmall.acc

import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

class MapAccumulator extends AccumulatorV2[(String,String) , mutable.Map[(String , String),Long]]{

  override def isZero: Boolean = ???

  override def copy(): AccumulatorV2[(String, String), mutable.Map[(String, String), Long]] = ???

  override def reset(): Unit = ???

  override def add(v: (String, String)): Unit = ???

  override def merge(other: AccumulatorV2[(String, String), mutable.Map[(String, String), Long]]): Unit = ???

  override def value: mutable.Map[(String, String), Long] = ???
}
