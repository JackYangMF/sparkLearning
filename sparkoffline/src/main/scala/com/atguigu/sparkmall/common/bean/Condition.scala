package com.atguigu.sparkmall.common.bean


/**
  * 用来封装从hive中过滤的数据.
  */
class Condition (var startDate: String,
                 var endDate: String,
                 var startAge: Int,
                 var endAge: Int,
                 var professionals: String,
                 var city: String,
                 var gender: String,
                 var keywords: String,
                 var categoryIds: String,
                 var targetPageFlow: String)
