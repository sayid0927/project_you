/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  首页bannner
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments','../v/indexBannerV',"../../config/config"],
    function($,ajax,getArguments,indexBannerV,config) {
        function indexBannerM(){
            var getArgument = getArguments();
            var dataInt = {
                shopId : getArgument.shopId,
                userId : getArgument.userId  //getArgument.userId        face.getUserId()
            };
            ajax({
                url:config.interfacesConfig.banner.url,
                dataType:config.interfacesConfig.banner.dataType,
                type:config.interfacesConfig.banner.type,
                data:JSON.stringify(dataInt),
                cache:{
                    use:cacheState,
                    name:'indexBannerV',
                    time:1
                },
                isOk:function(data){
                    indexBannerV(data);
                },
                isError:function(){
                    setTimeout(function(){
                        face.loadingFail();
                    },500)
                }
            });
        }
        return indexBannerM;
    });