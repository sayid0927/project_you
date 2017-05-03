/**
 * Created by 程哲振 on 2016/2/25 0025 下午 04:05.
 * Describe:
 */
define(['jquery'], function ($) {
    function complete(XMLHttpRequest,status,loadingSta) {
        if(status=='timeout'){//超时,status还有success,error等值的情况
            //loading.loading('hide')
            $('.ui-loading-block').remove();
            console.log("请求超时，服务器或网络异常！")
        }
        else{
            setTimeout(function(){
                // loading.loading('hide')
                $('.ui-loading-block').remove();
            },1)
        }

    }

    return complete;
});