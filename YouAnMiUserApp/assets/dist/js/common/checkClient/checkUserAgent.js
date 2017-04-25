/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/16 0016 上午 09:08.
 * Description: 1、openOnmobile()返回0在PC，1在移动端
 *              2、 checkClient()返回1为安卓，2为IOS，3为PC
 */
define([''], function () {
    function openOnmobile(){
        var browser = {
            versions: function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {   //移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        };
    if (browser.versions.mobile) {
         return 1;
    }
    else{
        return 0;
    }
    }

    function checkClient(){
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

        if(isAndroid==true){
            //安卓打开
            return 1;
        }
        else if(isiOS==true){
            //IOS打开
            return 2;
        }
        else{
            return 3;
        }
    }


function is_wx() {
        var browser = {
            versions: function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {   //移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        };

            var ua = navigator.userAgent.toLowerCase();//获取判断用的对象


            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                //在微信中打开
                return 1;
            }
            else if (ua.match(/WeiBo/i) == "weibo") {
                //在新浪微博客户端打开
                return 2;
            }
            else{
                return 3;
            }

    }
    var checkUserAgent={openOnmobile:openOnmobile,checkClient:checkClient,is_wx:is_wx};
    return checkUserAgent;
});