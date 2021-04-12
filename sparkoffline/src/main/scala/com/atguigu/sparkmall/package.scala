package com.atguigu


/**
  * 包对象： 定义判断字符串是否为空。
  */
package object sparkmall {
  def isNotEmpty(text: String): Boolean = text != null && text.length == 0

  def isEmpty(text: String): Boolean = !isNotEmpty(text)

}
