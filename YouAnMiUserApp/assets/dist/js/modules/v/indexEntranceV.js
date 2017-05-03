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
        var result = compare(getArguments().appVersion, appVersion);
        var u = navigator.userAgent;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        if(result){//新版本
            var html = '';//dom
            var type = '';//区别类型
            var orgBannerVos = datas.data.orgBannerVos;//后台数据
            var data = '';//传给app数据
            var length = 9;//下标
            for(var i=0; i<length; i++){
                try{//异常处理
                    switch(orgBannerVos[i].type){//类型
                        case 1 : //流量充值
                            type = 'recharge';
                            data = '';
                            break;
                        case 2 : //应用管家
                            type = 'download';
                            data = '';
                            break;
                        case 3 : //保障服务
                            type = 'insurance';
                            data = '';
                            break;
                        case 4 : //到店优惠
                            type = 'shopDiscount';
                            data = '';
                            break;
                        case 5 : //赚佣金
                            type = 'commission';
                            data = '';
                            break;
                        case 6 : //品牌圈
                            type = 'circle';
                            data  = JSON.stringify(orgBannerVos[i]);
                            break;
                        case 7 : //地区圈
                            type = 'circle';
                            data  = JSON.stringify(orgBannerVos[i]);
                            break;
                        case 9 : //自定义功能
                            type = 'custom';
                            data  = JSON.stringify(orgBannerVos[i]);
                            break;
                        default : //系统功能
                            type = 'custom';
                            data  = JSON.stringify(orgBannerVos[i]);
                            break;
                    }
                }
                catch(err) {
                    break;
                }
                if(isAndroid){//安卓
                    if(orgBannerVos[i].scope == 2){
                        length++;
                    }
                    if(orgBannerVos[i].scope == 1 || orgBannerVos[i].scope == 3){//安卓 || 全平台
                        html += '<li class="s-function-row entrance-click" type="' + type + '" data=\''+ data +'\'>' +
                            '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                            '<h1>' + orgBannerVos[i].name + '</h1> ' +
                            '</li>';
                    }
                }
                else if(isiOS){//ios
                    if(orgBannerVos[i].type == 2) {//ios不展示应用管家
                        length++;
                        continue;
                    }
                    if(orgBannerVos[i].scope == 1){
                        length++;
                    }
                    if(orgBannerVos[i].scope == 2 || orgBannerVos[i].scope == 3){//ios || 全平台
                        html += '<li class="s-function-row entrance-click" type="' + type + '" data=\''+ data +'\'>' +
                            '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                            '<h1>' + orgBannerVos[i].name + '</h1> ' +
                            '</li>';
                    }
                }
            }
            html += '<li class="s-function-row entrance-click" id="moreFunction" data="" type="total">' +
                '<img src="images/quanbu_03.png" alt=""> ' +
                '<h1>全部</h1> ' +
                '</li>';
            $(".s-function-list ul").html(html);
            $(".hot-circles").css("height", "16.6rem");
        }
        else{//老版本
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