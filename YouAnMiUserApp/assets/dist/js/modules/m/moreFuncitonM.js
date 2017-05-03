/**
 * Author: 苏昶宇.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:    icon 入口
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments','../v/moreFunctionV',"../../config/config","swiper"],
    function($,ajax,getArguments,moreFunctionV,config,swiper) {
        function moreFuncitonM(){
            var getArgument = getArguments();
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
                    name:'moreFuncitonM',
                    time:10
                },
                isOk:function(data){
                    moreFunctionV(data);
                },
                isError:function(){
                    setTimeout(function(){
                        face.loadingFail();
                    },500)
                }
            });
        }
        return moreFuncitonM;
    });