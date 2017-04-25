/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/15 0015 下午 12:38.
 * Description: ....
 */
define(['jquery'], function ($) {
    function getArguments() {
        var url = location.search; //获取url中"?"符后的字串
        var theRequest = new Object();
        if(url==''){
            theRequest=null;
        }
        else {
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                strs = str.split("&");
                for (var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
                }
            }
            else {
                theRequest = null
            }
        }
        return theRequest;
    }

    return getArguments;
});