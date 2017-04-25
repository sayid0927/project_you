/**
 * Author: 苏昶宇.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  icon 入口
 */
define(['jquery'],
    function ($) {
        function moreFunctionV(datas){
            var html = '';
            var orgBannerVos = datas.data.orgBannerVos;
            var isIos = navigator.userAgent.match(/iphone|ipod/ig);
            for(var i=0; i<orgBannerVos.length; i++){
                if(isIos){
                    if(orgBannerVos[i].type == 2){
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
            $(".s-function-list ul").html(html);
            $(".s-function-list").css("display", "block");

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
        }
        return moreFunctionV;
    });