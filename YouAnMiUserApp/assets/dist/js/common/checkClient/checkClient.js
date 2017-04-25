/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/9 0009 下午 05:32.
 * Description: ....
 */
define([''], function () {
    function checkClient(){
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        var ua = window.navigator.userAgent.toLowerCase();
        if(isAndroid==true){
                //安卓打开
            client = 'android';
            return client;
        }else if(isiOS==true){
                //IOS打开
            client = 'ios';
            return client;
        }else{
            return client;
        }
    }

    return checkClient;
});