Date.prototype.pattern=function(fmt) {
    var o = {
        "M+" : this.getMonth()+1, //月份
        "d+" : this.getDate(), //日
        "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
        "H+" : this.getHours(), //小时
        "m+" : this.getMinutes(), //分
        "s+" : this.getSeconds(), //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S" : this.getMilliseconds() //毫秒
    };
    var week = {
        "0" : "/u65e5",
        "1" : "/u4e00",
        "2" : "/u4e8c",
        "3" : "/u4e09",
        "4" : "/u56db",
        "5" : "/u4e94",
        "6" : "/u516d"
    };
    if(/(y+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    if(/(E+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);
    }
    for(var k in o){
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}

Number.prototype.toFixed = function(len){
    if(len>20 || len<0){
        throw new RangeError('toFixed() digits argument must be between 0 and 20');
    }
    // .123转为0.123
    var number = Number(this);
    if (isNaN(number) || number >= Math.pow(10, 21)) {
        return number.toString();
    }
    if (typeof (len) == 'undefined' || len == 0) {
        return (Math.round(number)).toString();
    }
    var result = number.toString(),
        numberArr = result.split('.');

    if(numberArr.length<2){
        //整数的情况
        return padNum(result);
    }
    var intNum = numberArr[0], //整数部分
        deciNum = numberArr[1],//小数部分
        lastNum = deciNum.substr(len, 1);//最后一个数字

    if(deciNum.length == len){
        //需要截取的长度等于当前长度
        return result;
    }
    if(deciNum.length < len){
        //需要截取的长度大于当前长度 1.3.toFixed(2)
        return padNum(result)
    }
    //需要截取的长度小于当前长度，需要判断最后一位数字
    result = intNum + '.' + deciNum.substr(0, len);
    if(parseInt(lastNum, 10)>=5){
        //最后一位数字大于5，要进位
        var times = Math.pow(10, len); //需要放大的倍数
        var changedInt = Number(result.replace('.',''));//截取后转为整数
        changedInt++;//整数进位
        changedInt /= times;//整数转为小数，注：有可能还是整数
        result = padNum(changedInt+'');
    }
    return result;
    //对数字末尾加0
    function padNum(num){
        var dotPos = num.indexOf('.');
        if(dotPos === -1){
            //整数的情况
            num += '.';
            for(var i = 0;i<len;i++){
                num += '0';
            }
            return num;
        } else {
            //小数的情况
            var need = len - (num.length - dotPos - 1);
            for(var j = 0;j<need;j++){
                num += '0';
            }
            return num;
        }
    }
};

function serializeNotNull(serStr){
    return serStr.split("&").filter(str => !str.endsWith("=")).join("&");
}