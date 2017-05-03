/**
 * Author: 苏昶宇.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  icon 入口
 */
define(['jquery'],
    function ($) {
        function moreFunctionV(datas){
            var html = '';//dom
            var orgBannerVos = datas.data.orgBannerVos;//后台数据
            var u = navigator.userAgent;//设备信息
            var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
            var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
            var data = '';//返回app数据
            for(var i=0; i<orgBannerVos.length; i++){
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
                if(isAndroid){//安卓
                    if(orgBannerVos[i].scope == 1 || orgBannerVos[i].scope == 3){//安卓 || 全平台
                        html += '<li class="s-function-row entrance-click" type="' + type + '" data=\''+ data +'\'>' +
                            '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                            '<h1>' + orgBannerVos[i].name + '</h1> ' +
                            '</li>';
                    }
                }
                else if(isiOS){//ios
                    if(orgBannerVos[i].type == 2) {
                        continue;
                    }
                    if(orgBannerVos[i].scope == 2 || orgBannerVos[i].scope == 3){//ios || 全平台
                        html += '<li class="s-function-row entrance-click" type="' + type + '" data=\''+ data +'\'>' +
                            '<img src="' + orgBannerVos[i].iconUrl + '" alt=""> ' +
                            '<h1>' + orgBannerVos[i].name + '</h1> ' +
                            '</li>';
                    }
                }
            }
            $(".s-function-list ul").html(html);

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