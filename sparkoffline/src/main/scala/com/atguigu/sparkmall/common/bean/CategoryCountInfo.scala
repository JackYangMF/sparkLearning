package com.atguigu.sparkmall.common.bean


/**
  * 用来封装写入mysql的数据.
  */
case class CategoryCountInfo(taskId: String,
                        categoryId: String,
                        clickCount: Long,
                        orderCount: Long,
                        payCount: Long)

