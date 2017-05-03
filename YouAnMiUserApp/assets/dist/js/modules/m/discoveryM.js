/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  发现-精选页面
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments','../v/discoveryV',"../../config/config"],
    function($,ajax,getArguments,discoveryV,config) {
        function discoveryM(){
            var getArgument = getArguments();
            var dataInt = {
                shopId : getArgument.shopId,
            };
            ajax({
                url:config.interfacesConfig.appFound.url,
                dataType:config.interfacesConfig.appFound.dataType,
                type:config.interfacesConfig.appFound.type,
                data:JSON.stringify(dataInt),
                cache:{
                    use:cacheState,
                    name:'discoveryV',
                    time:1
                },
                isOk:function(data){
                    discoveryV(data);
                },
                isError:function(){
                    setTimeout(function(){
                        face.loadingFail();
                    },500)
                }
            });
        }
        return discoveryM;
    });