/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  发现-精选页面-----分页
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments',"../../config/config","dropload"],
    function($,ajax,getArguments,config,dropload) {
        function discoveryErM(){
            var getArgument = getArguments();
            var dataInt = {
                shopId : getArgument.shopId,
            };
            var counter = 2;
            // 每页展示10个
            var num = 10;
            var pageStart = 0,pageEnd = 0;

            $('.article-list-wrap').dropload({
                scrollArea : window,
                loadDownFn : function(me){
                    ajax({
                        url:config.interfacesConfig.appFound.url,
                        dataType:config.interfacesConfig.appFound.dataType,
                        type:config.interfacesConfig.appFound.type,
                        data:JSON.stringify({
                            shopId : getArgument.shopId,
                            pageIndex: counter
                        }),
                        cache:{
                            use:cacheState,
                            name:'discoveryErV'+counter,
                            time:1
                        },
                        isOk:function(datas){
                            counter++;
                            var discoveryHtml = '';
                            for(var i = 0;i<datas.data.productArticle.length;i++){
                                discoveryHtml+='<div class="discover-item">';
                                discoveryHtml+='<div class="img-bg">';
                                discoveryHtml+='<img src="'+datas.data.productArticle[i].imageUrl+'">';
                                discoveryHtml+='</div>';
                                discoveryHtml+='<div class="item-title">'+datas.data.productArticle[i].title+'</div>';
                                //discoveryHtml+='<div class="good-icon-wrap">';
                                //discoveryHtml+='<div class="opacity-bg"></div>';
                                //discoveryHtml+='<i class="good-icon"><img src="'+imgUrlPre+'icon/discoveryGood.png"></i><span class="good-num">'+datas.data.productArticle[i].praiseAmount+'</span>';
                                //discoveryHtml+='</div>';
                                discoveryHtml+='</div>';

                            }
                            $(".webApp-content .discovery-list-wrap").append(discoveryHtml);
                            me.resetload();
                            $(".discover-item").unbind('click').on('click',function(){
                                var data = $(this).attr('data');
                                var newData = JSON.parse(data);
                                var _SendData = {pageId: pageIdList['articleInfo'], "data": newData};
                                face.openViewHandle(JSON.stringify(_SendData));
                            });
                        },
                        isError:function(){
                            $('.loading-bg').remove();
                            console.log("网络不好，重新加载");
                            discoveryErM();
                        }
                    });
                }
            });
        }
        return discoveryErM;
    });