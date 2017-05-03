/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  发现页面-精选
 */
define(['jquery','../m/discoveryErM'],
    function ($,discoveryErM) {
    function discoveryV(datas){
        var noneDiv = '';
        if(clients != 'android'){
            noneDiv = 'noneDiv';
        }
        var html = '';
        html+='<div class="menu-circles clearfix '+noneDiv+'">';
        html+='<div class="menu-item entrance-click" data="" type="commission">';
        html+='<div class="img-wrap">';
        html+='<img src="'+imgUrlPre+'/icon/icon_zyj.png">';
        html+='</div>';
        html+='<span>赚佣金</span>';
        html+='</div>';
        html+='<div class="menu-item entrance-click" type="recharge">';
        html+='<div class="img-wrap">';
        html+='<img src="'+imgUrlPre+'/icon/icon_llcz.png">';
        html+='</div>';
        html+='<span>流量充值</span>';
        html+='</div>';
        html+='<div class="menu-item entrance-click" type="download">';
        html+='<div class="img-wrap">';
        html+='<img src="'+imgUrlPre+'/icon/icon_yygj.png">';
        html+='</div>';
        html+='<span>应用管家</span>';
        html+='</div>';
        html+='<div class="menu-item entrance-click" type="product">';
        html+='<div class="img-wrap">';
        html+='<img src="'+imgUrlPre+'/icon/icon_yhsp.png">';
        html+='</div>';
        html+='<span>优惠商品</span>';
        html+='</div>';
        html+='</div>';
        $(".discovery-list-wrap").before(html);
        var discoveryHtml = '';
        for(var i = 0;i<datas.data.productArticle.length;i++){
            discoveryHtml+='<div class="discover-item" data=\''+JSON.stringify(datas.data.productArticle[i])+'\'>';
            discoveryHtml+='<div class="img-bg">';
            discoveryHtml+='<img src="'+datas.data.productArticle[i].imageUrl+'">';
            discoveryHtml+='</div>';
            discoveryHtml+='<div class="item-title">'+datas.data.productArticle[i].title+'</div>';
            /*discoveryHtml+='<div class="good-icon-wrap">';
            discoveryHtml+='<div class="opacity-bg"></div>';
            discoveryHtml+='<i class="good-icon"><img src="'+imgUrlPre+'icon/discoveryGood.png"></i><span class="good-num">'+datas.data.productArticle[i].praiseAmount+'</span>';
            discoveryHtml+='</div>';*/
            discoveryHtml+='</div>';
        }
        $(".webApp-content .discovery-list-wrap").html(discoveryHtml);
        discoveryErM();
        $(".entrance-click").on('click',function(){
            var data = $(this).attr('data');
            var type = $(this).attr('type');
            var _SendData = {pageId: pageIdList[type], "data": data};
            face.openViewHandle(JSON.stringify(_SendData));
        });
        $(".discover-item").on('click',function(){
            var data = $(this).attr('data');
            var newData = JSON.parse(data);
            var _SendData = {pageId: pageIdList['articleInfo'], "data": newData};
            face.openViewHandle(JSON.stringify(_SendData));
        });
        $(".webApp-content").append('<a href="javascript:void(0);" id="returnTop"><img src="'+imgUrlPre+'icon/up.png"/></a>');

        //回到顶部
        $(window).scroll(function () {
            var scrollTop = $(this).scrollTop();
            if (scrollTop >= 300) {
                $("#returnTop").show();
            } else {
                $("#returnTop").hide();
            }
        });

        $.fn.scrollTo = function (options) {
            var defaults = {
                toT: 0,    //滚动目标位置
                durTime: 200,  //过渡动画时间
                delay: 20,     //定时器时间
                callback: null   //回调函数
            };
            var opts = $.extend(defaults, options),
                timer = null,
                _this = this,
                curTop = _this.scrollTop(),//滚动条当前的位置
                subTop = opts.toT - curTop,    //滚动条目标位置和当前位置的差值
                index = 0,
                dur = Math.round(opts.durTime / opts.delay),
                smoothScroll = function (t) {
                    index++;
                    var per = Math.round(subTop / dur);
                    if (index >= dur) {
                        _this.scrollTop(t);
                        window.clearInterval(timer);
                        if (opts.callback && typeof opts.callback == 'function') {
                            opts.callback();
                        }
                        return;
                    } else {
                        _this.scrollTop(curTop + index * per);
                    }
                };
            timer = window.setInterval(function () {
                smoothScroll(opts.toT);
            }, opts.delay);
            return _this;
        };

        $("#returnTop").click(function(){
            $('body').scrollTo();
        });
        //回到顶部结束

    }

    return discoveryV;
});