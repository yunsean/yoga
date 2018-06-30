
var daaa=function (window) {
    var regId = /^#[\w\-]+$/,
        regCls = /^.[\w\-]+$/,
        p_silice = Array.prototype.slice,
        p_push = Array.prototype.push,
        markArr = function (obj) {
            return p_silice.call(obj, 0);
        };
    function MWood(selector) {
        return new MWood.prototype.init(selector);
    };

    function MWood(selector, context) {
        return new MWood.prototype.init(selector, context);
    }

    MWood.prototype = {
        init: function (selector) {
            if (regId.test(selector)) {
                this.length = 1;
                this[0] = document.getElementById(selector);
                return this;
            }
            else if (regCls.test(selector)) {
                if (document.querySelectorAll) {
                    p_push.apply(this, markArr(document.querySelectorAll(selector)));
                } else {
                    var len = documenet.getElementsByTagName('*').length,
                        clsArr = [],
                        el, i = 0,
                        selector = selector.replace(/\-/g, "\\-");
                    var oRegExp = new RegExp("(^|\\s)" + selector + "(\\s|$)");

                    for (; i < len; i++) {
                        el = els[i];
                        if (oRegExp.test(el[type])) {
                            clsArr.push(el);
                        }
                    }

                    pro_push.apply(this, markArr(clsArr));
                    return this;
                }
            }
        },
        length: 0
    }

    MWood.prototype.init.prototype = MWood.prototype;

    MWood.extend = function (obj) {
        for (var o in obj) {
            this[o] = obj[o];
        }
    }

    MWood.extend({
        addHandle: function (elm, type, fn) {
            if (elm.addEventListener) {
                elm.addEventListener(type, fn, false);
            }
            else if (el.attachEvent) {
                elm.attachEvent("on" + type, fn);
            } else {
                elm["on" + type] = fn;
            }
        },
        removeHandle: function (elm, type, fn) {
          if (elm.removeEventListener) {
                elm.removeEventListener(type, fn, false);
            }
            else if (el.detachEvent) {
                elm.detachEvent("on" + type, fn);
            } else {
                elm["on" + type] = null;
            }
        },
        getEvent: function (evnet) {
            return evnet ? evnet : window.evnet;
        },
        getTarget: function (event) {
            return event.target || event.srcElement;
        }
    });

    window.MWood = MWood;
};

var classLi;
var dragInfo = {};   //存放有关拖拽对象的一些信息
var sort_main_a;
var sort_main_b;
var sort_branch_a;
var sort_branch_b;
var jsonData=[];
function drag(b,c) {
    daaa(window)


  //绑定事件
  var menuList=document.getElementById('menuList');//一级菜单栏
    if(b=='MenuSorting'){
      //console.log('有效果吗')
      jsonData=JSON.parse(JSON.stringify(c))
      //console.log(jsonData)
      //console.log(jsonData[0].name)
        MWood.addHandle(document, "mousedown", handleEvent);
        MWood.addHandle(document, "mousemove", handleEvent);
        MWood.addHandle(document, "mouseup", handleEvent);
    }else {
        MWood.removeHandle(document, "mousedown", handleEvent);
        MWood.removeHandle(document, "mousemove", handleEvent);
        MWood.removeHandle(document, "mouseup", handleEvent);
    }

};
var handleEvent=function () {
  var e = MWood.getEvent(event);  //获得事件
  var target = MWood.getTarget(event); //获得事件对象
  if(target.tagName=="A"){
    target=target.parentNode;
  }
  if(target.tagName=="I"){
    target=target.parentNode.parentNode;
  }
  if(target.tagName=="SPAN"){
    target=target.parentNode.parentNode;
  }

  switch (event.type) {   //判断事件类型
    case "mousedown":
      if (target.className.indexOf("jsMenu") > -1) {
        for (var j=0;j<document.getElementsByClassName('jsMenu').length;j++){
          if (target==document.getElementsByClassName('jsMenu')[j]){
            sort_main_a=j;
          }
        };

        classLi="jsMenu";

        dragInfo.dObj = target;   //存放事件对象
        var tlwh = getObjtlwh(target); //获得对象的 offsetTop,offsetLeft,offsetWidth,offsetHeight
        //计算出鼠标的坐标与对象offsetTop,offsetLeft的差值以便于鼠标移动时实时定位拖拽对象的位置
        target.x = e.clientX - tlwh[1];
        target.y = e.clientY - tlwh[0];



        //修改对象的position以便可以设置left和top进行拖拽 从这里开始此对象已经脱离文档流
        //何为文档流 当怎么样设置position或别的设置会脱离文档流请百度
        target.style.position = "absolute";

        //设置拖拽对象的left top width height
        target.style.left = (e.clientX - target.x) + "px";
        target.style.top = (e.clientY - target.y) + "px";
        target.style.width = tlwh[2] + "px";
        target.style.height = tlwh[3] + "px";

        //建立一个新对象填补被拖拽对象的位置，这样页面就还会按现在的排版，不会有任何更改。
        //如果没创建，被拖拽对象的位置就会被文档流上的其他对象占用，文章最后提供代码，可以注释掉下面4句试下两种情况
        dragInfo.vObj = document.createElement("li");
        dragInfo.vObj.className='jsMenu pre_menu_item grid_item jslevel1 ui-sortable ui-sortable-disabled vObj';
        dragInfo.vObj.style.width =tlwh[2]-1+ "px";
        dragInfo.vObj.style.height =  tlwh[3]-1+ "px";
        target.parentNode.insertBefore(dragInfo.vObj, target);
      }
      if (target.className.indexOf("jslevel2") > -1) {
        for (var j=0;j<document.getElementsByClassName('jsMenu').length;j++){
          if (target.parentNode.parentNode.parentNode==document.getElementsByClassName('jsMenu')[j]){
            sort_main_a=j;
          }
        }
        for (var i=0;i<target.parentNode.getElementsByClassName('jslevel2 ').length;i++){
          if (target==target.parentNode.parentNode.getElementsByClassName('jslevel2')[i]){
            sort_branch_a=i;
          }
        }
        classLi="jslevel2";

        dragInfo.dObj = target;   //存放事件对象
        var tlwh = getObjtlwh(target); //获得对象的 offsetTop,offsetLeft,offsetWidth,offsetHeight
        //计算出鼠标的坐标与对象offsetTop,offsetLeft的差值以便于鼠标移动时实时定位拖拽对象的位置
        target.x = e.clientX - tlwh[1];
        target.y = e.clientY - tlwh[0];



        //修改对象的position以便可以设置left和top进行拖拽 从这里开始此对象已经脱离文档流
        //何为文档流 当怎么样设置position或别的设置会脱离文档流请百度
        target.style.position = "absolute";

        //设置拖拽对象的left top width height
        target.style.left = (e.clientX - target.x) + "px";
        target.style.top = (e.clientY - target.y) + "px";
        target.style.width = tlwh[2] + "px";
        target.style.height = tlwh[3] + "px";

        //建立一个新对象填补被拖拽对象的位置，这样页面就还会按现在的排版，不会有任何更改。
        //如果没创建，被拖拽对象的位置就会被文档流上的其他对象占用，文章最后提供代码，可以注释掉下面4句试下两种情况
        dragInfo.vObj = document.createElement("li");
        dragInfo.vObj.className='jslevel2 current';
        dragInfo.vObj.style.width = target.style.width;
        dragInfo.vObj.style.height = target.style.height;
        target.parentNode.insertBefore(dragInfo.vObj, target);
      }
      break;
    case "mousemove":
      if (dragInfo.dObj) {
        //当鼠标移动的时候判断时候已经有对象存在（对象会在mousedown的时候存放进这个变量里）
        //设置拖拽对象的left和top改变位置，因为position已经在mousedown的时候改变为absolute了（鼠标坐标-保存的差值）
        dragInfo.dObj.style.left = (e.clientX - dragInfo.dObj.x) + "px";
        dragInfo.dObj.style.top = (e.clientY - dragInfo.dObj.y) + "px";

        //获取class为droggle的一组HTMl对象.Array.prototype.slice.call是给一个伪数组转为真正的数组用的,MWood(".droggle")就是取得class为droggle的一组对象
        var arr = Array.prototype.slice.call(MWood("."+classLi), 0);

        //循环对象
        for (var i = 0; i < arr.length; i++) {
          if (arr[i] === dragInfo.dObj)  //过滤拖拽对象
            continue;
          var tlwh = getObjtlwh(arr[i]); //获得对象的 offsetTop,offsetLeft,offsetWidth,offsetHeight

          //下面就主要是判断鼠标的位置是否为引起页面上各HTML元素在页面上位置的替换
          //判断鼠标x > 对比对象offsetLeft && x < （对比对象的offsetWidth + 对象对象offsetLef）。 鼠标的坐标y同理鼠标坐标x的判断
          //判断位置变换也可以按自己的标准来，我百度的时候看到例子里是这样判断我就直接像他那样写了
          if (target.className.indexOf("jsMenu") > -1){
            if (e.x >arr[i].getBoundingClientRect().left ) {
              arr[i].parentNode.insertBefore(dragInfo.vObj, arr[i].nextSibling);
              for (var j=0;j<document.getElementsByClassName('jsMenu').length;j++){
                if (arr[i]==document.getElementsByClassName('jsMenu')[j]){
                  sort_main_b=j;
                }
              };
            }
          }
          if (target.className.indexOf("jslevel2") > -1){
            if (e.y >arr[i].getBoundingClientRect().top ) {
              arr[i].parentNode.insertBefore(dragInfo.vObj, arr[i].nextSibling);
              for (var j=0;j<document.getElementsByClassName('jslevel2').length;j++){
                if (arr[i]==document.getElementsByClassName('jslevel2')[j]){
                  sort_branch_b=j;
                }
              };
            }
          }
        }
      }
      break;
    case "mouseup":
      if (dragInfo.dObj) {
        //console.log(sort_main_b)
        //console.log(sort_main_a)
        //console.log(jsonData[0].children)
        if(dragInfo.dObj.className.indexOf("jsMenu") > -1){
          //console.log('进入主菜单拖拽')
          if(sort_main_b==0){
            sort_main_b=1;
          }
          if (sort_main_a<sort_main_b){
            sort_main_b=sort_main_b-1;
          }
          var sort_main_a_data=jsonData[sort_main_a];
          jsonData.splice(sort_main_a,1);
          jsonData.splice(sort_main_b,0,sort_main_a_data);
          for(var i=0;i<jsonData.length;i++){
            jsonData[i].sort=i;
          }

          for (let i=0;i<jsonData.length;i++) {
            console.log(jsonData[i].name)
          }
        };
        if(dragInfo.dObj.className.indexOf("jslevel2") > -1){
          //for (let i=0;i<jsonData[sort_main_a].children.length;i++) {
          //  console.log(jsonData[sort_main_a].children[i].name);
          //}
          //console.log("进入子菜单拖拽")
          if(sort_branch_a<sort_branch_b){
            sort_branch_b=sort_branch_b-1;
          }
          if(sort_branch_b==0){
            sort_branch_b=1;
          }

          var sort_branch_a_data=jsonData[sort_main_a].children[sort_branch_a];
          jsonData[sort_main_a].children.splice(sort_branch_a,1);//[1,2]
          jsonData[sort_main_a].children.splice(sort_branch_b,0,sort_branch_a_data);
          for(var i=0;i<jsonData[sort_main_a].children.length;i++){
            jsonData[sort_main_a].children[i].sort=i;
          }
          //for (let i=0;i<jsonData[sort_main_a].children.length;i++) {
          //  console.log(jsonData[sort_main_a].children[i].name);
          //}

        }
        //把拖拽对象重新弄进文档流（static就是默认的值），替换临时占位的对象，并初始化dragInfo；
        dragInfo.dObj.style.position = "relative";
        dragInfo.dObj.style.left =null;
        dragInfo.dObj.style.top =null;
        dragInfo.dObj.style.width =null;
        dragInfo.dObj.style.height =null;
        dragInfo.vObj.parentNode.insertBefore(dragInfo.dObj, dragInfo.vObj);
        dragInfo.dObj.parentNode.removeChild(dragInfo.vObj);
        dragInfo = {};
        var vObj=document.getElementsByClassName('vObj');
        if(vObj.length>0){
          for (var i=0;i<vObj.length;i++){
            vObj[i].parentNode.removeChild(vObj[i])
          }
        }
        numData(sort_main_a,sort_branch_b)
      }
      break;
  }
};
var deepCopy= function(source) {
  var result={};
  for (var key in source) {
    result[key] = typeof source[key]==='object'? deepCoyp(source[key]): source[key];
  }
  return result;
}
function getObjtlwh(o) {
    var arr = [];
    arr[0] = o.offsetTop;
    arr[1] = o.offsetLeft;
    arr[2] = o.offsetWidth;
    arr[3] = o.offsetHeight;
    return arr
}
function numData(){
  return jsonData
}
export { //很关键
  drag,
  numData
}

