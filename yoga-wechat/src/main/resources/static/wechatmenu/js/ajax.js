
function ajax(options) {
  /**
   * 传入方式默认为对象
   * */
  options = options || {};
  /**
   * 默认为GET请求
   * */
  options.type = (options.type || "GET").toUpperCase();
  /**
   * 返回值类型默认为json
   * */
  options.dataType = options.dataType || 'json';
  /**
   * 默认为异步请求
   * */
  options.async = options.async || true;
  /**
   * 对需要传入的参数的处理
   * */
  var params = getParams(options.data);
  var xhr;
  /**
   * 创建一个 ajax请求
   * W3C标准和IE标准
   */
  if (window.XMLHttpRequest){
    /**
     * W3C标准
     * */
    xhr = new XMLHttpRequest();
  }else{
    /**
     * IE标准
     * @type {ActiveXObject}
     */
    xhr = new ActiveXObject('Microsoft.XMLHTTP')
  }
  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4){
      var status = xhr.status;
      if (status >= 200 && status < 300 ){
        options.success && options.success(xhr.responseText,xhr.responseXML);
      }else{
        options.fail && options.fail(status);
      }
    }
  };
  if (options.type == 'GET'){
    xhr.open("GET",options.url + '?' + params,options.async);
    xhr.send(null)
  }else if (options.type == 'POST'){
    /**
     *打开请求
     * */
    xhr.open('POST',options.url,options.async);
    /**
     * POST请求设置请求头
     * */
    xhr.setRequestHeader('Content-Type','application/json');
    /**
     * 发送请求参数
     */
    xhr.send(options.data);
  }
}
/**
 * 对象参数的处理
 * @param data
 * @returns {string}
 */
function getParams(data) {
  var arr = [];
  for (var param in data){
    //arr.push(encodeURIComponent(param) + '=' +encodeURIComponent(data[param]));
    arr.push(param + '=' +data[param]);
  }
  //arr.push(('randomNumber=' + Math.random()).replace('.'));
  return arr.join('&');
}
export { //很关键
  ajax
}
/**
 * 对封装好的ajax请求进行调用
 *
ajax({
  url:"", //请求地址
  type:'GET',   //请求方式
  data:{name:'zhangsan',age :'23',email:'2372734044@qq.com'}, //请求参数
  dataType:"json",     // 返回值类型的设定
  async:false,   //是否异步
  success:function (response,xml) {
    console.log(response);   //   此处执行请求成功后的代码
  },
  fail:function (status) {
    console.log('状态码为'+status);   // 此处为执行成功后的代码
  }

});
*/
