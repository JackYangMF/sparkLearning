package com.atguigu.sparkmall.mock.util


import scala.collection.mutable.ListBuffer
/**
  * 该类生成一定 权重的 值。
  */
object RandomOptions {

  def apply[T](opts: (T, Int)*) ={
    val randomOptions = new RandomOptions[T]()
    randomOptions.totalWeight = (0 /: opts)(_ + _._2) // 计算出来总的比重
    opts.foreach{
      case (value, weight) => randomOptions.options ++= (1 to weight).map(_ => value)
    }
    randomOptions
  }


  def main(args: Array[String]): Unit = {
    // 测试
    val opts = RandomOptions(("张三", 10), ("李四", 30))
    (0 to 50).foreach(_ => println(opts.getRandomOption()))
  }
}


class RandomOptions[T]{
  var totalWeight: Int = _
  var options = ListBuffer[T]()

  /**
    * 获取随机的 Option 的值
    * @return
    */
  def getRandomOption() = {
    options(RandomNumUtil.randomInt(0, totalWeight - 1))
  }
}