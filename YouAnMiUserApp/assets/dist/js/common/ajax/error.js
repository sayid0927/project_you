/**
 * Created by 程哲振 on 2016/2/25 0025 下午 03:52.
 */
define(['jquery'], function ($) {
    function error(){
        $('.loading-bg').remove();
        setTimeout(function() {
            alert("请求发生错误,管理员正在修复中");
        },500)
    }

    return error;
});