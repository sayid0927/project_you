/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  icon 入口
 */
define(['jquery','swiper','../m/indexDiscoutM', '../../common/getArguments/getArguments', '../../common/compare/compare'],
    function ($,swiper,indexDiscountM, getArguments, compare) {
    function indexEntranceV(datas){
        var noneDiv = '';
        if(clients != 'android'){
            noneDiv = 'noneDiv';
        }
        var result = compare(getArguments().appVersion, '20424');
        var isIos = navigator.userAgent.match(/iphone|ipod/ig);
        var length = 7;
        if(result){//如果是新版本
            var html = '';
            var orgBannerVos = datas.data.orgBannerVos;
            for(var i=0; i<length; i++){
                if(isIos){
                    try{
                        if(orgBannerVos[i].type == 2) {
                            length++;
                            continue;
                        }
                    }catch (e){
                        continue;
                    }
                }
                if(orgBannerVos[i].type == 1){//流量充值
                    html += '<li class="s-function-row entrance-click" type="recharge" data="">' +
                        '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                        '<h1>' + orgBannerVos[i].name + '</h1> ' +
                        '</li>';
                }else if(orgBannerVos[i].type == 2){//应用管家
                    html += '<li class="s-function-row entrance-click" type="download" data="">' +
                        '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                        '<h1>' + orgBannerVos[i].name + '</h1> ' +
                        '</li>';
                }else if(orgBannerVos[i].type == 3){//保障服务
                    html += '<li class="s-function-row entrance-click" type="insurance" data="">' +
                        '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                        '<h1>' + orgBannerVos[i].name + '</h1> ' +
                        '</li>';
                }else if(orgBannerVos[i].type == 5){//赚佣金
                    html += '<li class="s-function-row entrance-click" type="commission" data="">' +
                        '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                        '<h1>' + orgBannerVos[i].name + '</h1> ' +
                        '</li>';
                }else if(orgBannerVos[i].type == 9){//自定义
                    html += '<li class="s-function-row entrance-click" type="custom" data=\''+JSON.stringify(orgBannerVos[i])+'\'>' +
                        '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                        '<h1>' + orgBannerVos[i].name + '</h1> ' +
                        '</li>';
                }else if(orgBannerVos[i].type == 4){//到店优惠
                    html += '<li class="s-function-row entrance-click" type="shopDiscount" data="">' +
                        '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                        '<h1>' + orgBannerVos[i].name + '</h1> ' +
                        '</li>';
                }else if(orgBannerVos[i].type == 6 || orgBannerVos[i].type == 7){//6:品牌圈；7:地区圈
                    html += '<li class="s-function-row entrance-click" type="circle" data=\''+JSON.stringify(orgBannerVos[i])+'\'>' +
                        '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                        '<h1>' + orgBannerVos[i].name + '</h1> ' +
                        '</li>';
                }
            }
            $("#moreFunction").before(html);
            $(".hot-circles").css("height", "16.6rem");
            $(".s-function-list").css("display", "block");

            //跳转全部功能页面
/*            $("#moreFunction").on("click", function () {
                var shopId = getArguments().shopId;
                var baseUrl = getArguments().baseUrl;
                var appVersion = getArguments().appVersion;
                var brandName = getArguments().brandName;
                window.location.href = 'moreFunction.html?shopId=' + shopId + '&baseUrl=' +　baseUrl + '&appVersion=' + appVersion + '&brandName=' + brandName;
            });*/
        }
        else{
            var html = '';
            html +='<div class="swiper-container">';
            html +='<div class="swiper-wrapper">';
            html +='<div class="swiper-slide entrance-click" type="recharge" data=""><div class="img-wrap"><div class="img-wrap-circle"><img src="images/liuliangchongzhi.jpg"/></div></div><span class="span-line">流量充值</span></div>';
            html +='<div class="swiper-slide entrance-click '+noneDiv+'" type="download" data=""><div class="img-wrap"><div class="img-wrap-circle"><img src="images/icon_yygj.png"></div></div><span class="span-line">应用管家</span></div>';
            html +='<div class="swiper-slide entrance-click baozhang" type="insurance" data=""><div class="img-wrap"><div class="img-wrap-circle"><img src="images/bao.png"/></div></div><span class="span-line">保障服务</span></div>';
            html +='<div class="swiper-slide entrance-click" type="commission" data=""><div class="img-wrap"><div class="img-wrap-circle"><img src="images/yongjin.png" /></div></div><span class="span-line">赚佣金</span></div>';
            var sliderHtml = '';
            if(datas.data != ''){
                for(var i = 0;i < datas.data.platformCircles.length;i++){
                    var cityId = '';
                    if(datas.data.platformCircles[i].cityId != undefined){
                        cityId = 'cityId';
                    }
                    sliderHtml += '<div class="swiper-slide entrance-click '+cityId+'" type="circle" data=\''+JSON.stringify(datas.data.platformCircles[i])+'\'>';
                    sliderHtml += '<div class="img-wrap"><div class="img-wrap-circle"><img src="'+datas.data.platformCircles[i].imageUrl+'"></div></div>';
                    sliderHtml += '<span class="span-line">'+datas.data.platformCircles[i].name+'</span>';
                    sliderHtml += '</div>';
                }
            }
            html += sliderHtml;
            html +='</div>';
            html +='<div class="swiper-paginations"></div>';
            html +='</div> ';
            $(".webApp-content .hot-circles").html(html);
            $(".s-function-list").css("display", "block");
        }

        $(".entrance-click").on('click',function(){
            var data = $(this).attr('data');
            var type = $(this).attr('type');
            var newData = '';
            if(data!=''){
                 newData = JSON.parse(data);
            }
            var _SendData = {pageId: pageIdList[type], "data": newData};
            face.openViewHandle(JSON.stringify(_SendData));
        });
        indexDiscountM();
    }

    return indexEntranceV;
});