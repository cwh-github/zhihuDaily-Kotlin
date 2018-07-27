package com.example.cwh_pc.dailynewsstudy.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * 如20180711
 */
fun getCurrentDate():String{
    val calendar=Calendar.getInstance()
    val date=Date(System.currentTimeMillis())
    calendar.time=date
    val yeay=calendar.get(Calendar.YEAR)
    val moth=calendar.get(Calendar.MONTH)+1
    val day=calendar.get(Calendar.DAY_OF_MONTH)
    return "$yeay${formatTimeUnit(moth)}${formatTimeUnit(day)}"
}


/**
 * @param date 如20180710
 */
fun getMonthAndWeek(date:Long?):String{
    if(date==null){
        return ""
    }else{
        val strDate=date.toString()
        val yeay=strDate.substring(0,4)
        val moth=strDate.substring(4,6)
        val day=strDate.substring(6)
        val formatDateStr="$yeay-$moth-$day"
        val simpleDate=SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val formateDate=simpleDate.parse(formatDateStr)
        val calendar=Calendar.getInstance()
        calendar.time=formateDate
        val week=calendar.get(Calendar.DAY_OF_WEEK)
        return "${moth}月${day}日 ${getWeek(week)}"
    }
}

/**
 * 获取前n天
 * @param date 如 20180720
 */
fun getLastDay(date: Long?,n:Int=1):Long?{
    if(date==null){
        return null
    }else{
        val strDate=date.toString()
        val yeay=strDate.substring(0,4)
        val moth=strDate.substring(4,6)
        val day=strDate.substring(6)
        val formatDateStr="$yeay-$moth-$day"
        val simpleDate=SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val formateDate=simpleDate.parse(formatDateStr)
        val calendar=Calendar.getInstance()
        calendar.time=formateDate
        calendar.add(Calendar.DATE,-n)
        val lastYeay=calendar.get(Calendar.YEAR)
        val lastMoth=calendar.get(Calendar.MONTH)+1
        val lastDay=calendar.get(Calendar.DAY_OF_MONTH)
        return "$lastYeay${formatTimeUnit(lastMoth)}${formatTimeUnit(lastDay)}".toLong()
    }
}

/**
 * 获取距离现在n天的时间
 */
fun getLessNDayDate(n:Int):Long{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -n)
    val year=calendar.get(Calendar.YEAR)
    val moth=calendar.get(Calendar.MONTH)+1
    val day=calendar.get(Calendar.DAY_OF_MONTH)
    return "$year${formatTimeUnit(moth)}${formatTimeUnit(day)}".toLong()
}


fun getWeek(week:Int):String{
    return when(week){
        1-> "星期日"
        2-> "星期一"
        3-> "星期二"
        4-> "星期三"
        5-> "星期四"
        6-> "星期五"
        7-> "星期六"
        else -> {
            "星期"
        }
    }
}

/**
 * 将0-9转化为00-09
 */
fun formatTimeUnit(unit:Int):String{
    return if (unit<10)
        "0$unit"
    else
        "$unit"
}

/**
 * 将时间戳转化为时间如 129039900 转为 2018-07-16 15:08
 */
fun formatDate(unit:Long):String{
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
    return sdf.format(Date((unit * 1000)))
}