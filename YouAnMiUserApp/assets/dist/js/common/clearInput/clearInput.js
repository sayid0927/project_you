/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/18 0018 下午 03:00.
 * Description: ....
 */
define(['zepto'], function ($) {
    function clearInput() {
        //清楚input内容
        $(".clearX").on('click',function(){
            $(this).prev().eq(0).val('');
        });
        $('input').on('blur',function(){
            var _this=this;
            setTimeout(function(){
                $(_this).next().eq(0).css('display','none')
            },200);
            /*$(this).next().eq(0).css('display','none')*/
        });
        $('input').on('focus',function(){
            $(this).next().eq(0).css('display','')
        });
    }

    return clearInput;
});