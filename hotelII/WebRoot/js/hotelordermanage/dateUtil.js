
/**
 * 对于日期的公用操作函数
 */
Date.prototype.toFormatString = function () {
    return dateUtil.toFormatString(this);
};

/**
 * 命名空间
 */
function dateUtil() {
}

/**
 * 经过格式化的当前日期
 */
dateUtil.now = function(){
    return dateUtil.toFormatString(new Date());
}
 
 //比较两个date的间隔天数
dateUtil.getInterval = function (date1, date2) {
    var difference = Date.UTC(date1.getYear(), date1.getMonth(), date1.getDate(), 0, 0, 0) - Date.UTC(date2.getYear(), date2.getMonth(), date2.getDate(), 0, 0, 0);
    var difdays = difference / (1000 * 60 * 60 * 24);
    return difdays;
};
/**
 * 将日期格式化成YYYY-MM-DD的字符串
 */
dateUtil.toFormatString = function (date) {
	var months =  date.getMonth()+1;
    var nowStr = date.getFullYear().toString() + "-" + (months < 10 ? "0" + months.toString() : months.toString()) + "-" + (date.getDate() < 10 ? "0" + date.getDate().toString() : date.getDate().toString());
    return nowStr;
};
/**
 * 格式化日期，去掉时间，便于比较
 * 如果传入参数是字符串，则必须是YYYY-MM-DD格式，不接受其它格式。
 */
dateUtil.getFormatDate = function (date) {
    if (date instanceof Date) {
        var formatDate = new Date(date.getYear(), date.getMonth(), date.getDate());
        return formatDate;
    } else {
        if (typeof (date) == "string") {
            if (date.length != 10) {
                alert("date pattern must match YYYY-MM-DD format!");
                return;
            }
            var year = date.substring(0, 4);
            var month = date.substring(5, 7);
            var day = date.substring(8, 10);
            return new Date(year, month-1, day);//modified by WUYUN：之所以要减1是因为月份是从0开始的
        }
    }
    alert("invalid date format");
};
/**
 * 得到日期的前后几天
 */
dateUtil.addDays = function (date, days) {
    return new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
};
/**
 * 得到日期的前后几天，经过格式化。
 */
dateUtil.getDateStrByInterval = function (date, days) {
    return toFormatString(addDays(date, days));
};
var daysofweek = ["\u5468\u65e5", "\u5468\u4e00", "\u5468\u4e8c", "\u5468\u4e09", "\u5468\u56db", "\u5468\u4e94", "\u5468\u516d"];
dateUtil.dayOfWeek = function (day, month, year) {
    var a = Math.floor((14 - month) / 12);
    var y = year - a;
    var m = month + 12 * a - 2;
    var d = (day + y + Math.floor(y / 4) - Math.floor(y / 100) + Math.floor(y / 400) + Math.floor((31 * m) / 12)) % 7;
    return daysofweek[d];
};

