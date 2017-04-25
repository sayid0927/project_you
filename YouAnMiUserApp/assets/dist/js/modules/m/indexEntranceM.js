/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:    icon 入口
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments','../v/indexEntranceV',"../../config/config","swiper", '../../common/compare/compare'],
    function($,ajax,getArguments,indexEntranceV,config,swiper, compare) {
        function indexEntranceM(){
            var getArgument = getArguments();
            var datashopId = {
                shopId : getArgument.shopId
            };
            var result = compare(getArguments().appVersion, '20424');
            if(result){//如果是新版本
                var dataInt = {
                    shopId : getArgument.shopId,
                    brandName : getArgument.brandName
                };
                ajax({
                    url:config.interfacesConfig.circles.url,
                    dataType:config.interfacesConfig.circles.dataType,
                    type:config.interfacesConfig.circles.type,
                    data:JSON.stringify(dataInt),
                    cache:{
                        use:cacheState,
                        name:'indexEntranceV',
                        time:10
                    },
                    isOk:function(data){
                        indexEntranceV(data);
                    },
                    isError:function(){
                        setTimeout(function(){
                            face.loadingFail();
                        },500)
                    }
                });
            }else{
                var dataInt = {
                    shopId : getArgument.shopId,
                    brandName : getArgument.brandName
                };
                ajax({
                    url:config.interfacesConfig.circles.url,
                    dataType:config.interfacesConfig.circles.dataType,
                    type:config.interfacesConfig.circles.type,
                    data:JSON.stringify(dataInt),
                    cache:{
                        use:cacheState,
                        name:'indexEntranceV',
                        time:10
                    },
                    isOk:function(data){
                        indexEntranceV(data);
                    },
                    isError:function(){
                        setTimeout(function(){
                            face.loadingFail();
                        },500)

                    }
                });
            }
        }
        return indexEntranceM;
    });