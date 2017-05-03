/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:    icon 入口
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments','../v/indexArticlesV',"../../config/config"],
    function($,ajax,getArguments,indexArticlesV,config) {
        function indexArticlesM(){
            var getArgument = getArguments();

            var getUids = face.getUserId();
            var newUserId = '';
            if (getUids == null || getUids == '' || getUids == 'undefined') {
                newUserId = getArgument.userId;
            }else{
                newUserId = getUids;
            }

            var dataInt = {
                shopId : getArgument.shopId,
                pageIndex: 1,
                userId : newUserId
            };
            ajax({
                url:config.interfacesConfig.indexArticles.url,
                dataType:config.interfacesConfig.indexArticles.dataType,
                type:config.interfacesConfig.indexArticles.type,
                data:JSON.stringify(dataInt),
                cache:{
                    use:cacheState,
                    name:'indexArticlesV',
                    time:1
                },
                isOk:function(data){
                    indexArticlesV(data);
                },
                isError:function(){
                    setTimeout(function(){
                        face.loadingFail();
                    },500)
                }
            });
        }
        return indexArticlesM;
    });