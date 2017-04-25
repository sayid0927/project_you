/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/5/25 0015 上午 09:14.
 * Description:
 * interfaceUrl->接口地址,
 * dataType->数据类型,
 * typeAndData->为json具体为请求类型和请求数据，例如{type:"post",data:{a:1,b:2}} ps:键值要匹配
 * successFn->请求成功即200状态码的时候的回调方法
 */
define(["jquery",'./ajaxErrorSta','./complete','./beforeSendLoading','./error','../cache/cache',"../../config/config"],
    function($,ajaxErrorSta,complete,beforeSendLoading,error,cache,config) {
    function ajax(dataJson,loadingSta) {
        var ajaxJson={};
        if(dataJson.type=="post"){
            ajaxJson={
                url: dataJson.url,
                dataType: dataJson.dataType,
                contentType:"application/json",
                type: dataJson.type,
                data:dataJson.data,
                timeout : 120000, //超时时间设置，单位毫秒
                beforeSend:function(XMLHttpRequest,status){ beforeSendLoading(XMLHttpRequest,status,loadingSta)},
                complete : function(XMLHttpRequest,status){//请求完成后最终执行参数
                    complete(XMLHttpRequest,status,loadingSta);
                },
                success: function (data) {
                   if(dataJson.cache.use==true){
                       cache.setCache(dataJson.cache.name,data)
                   }
                    //伪造数据
                    if(config.falseData){
                        dataJson.isOk(eval(data));
                    }
                    else {
                        if (data.code == '00000') {
                            dataJson.isOk(data);
                        }
                        else {
                            ajaxErrorSta(data);
                        }
                    }
                },
                error:function(error){
                    setTimeout(function(){
                        dataJson.isError();
                    },500);
                }
            }
        }
        else if(dataJson.type=="get"){
            ajaxJson={
                url: dataJson.url,
                dataType: dataJson.dataType,
                contentType:"application/json",
                type: dataJson.type,
                timeout : 120000, //超时时间设置，单位毫秒
                beforeSend:function(XMLHttpRequest,status){
                    beforeSendLoading(XMLHttpRequest,status,loadingSta)
                },
                complete : function(XMLHttpRequest,status){//请求完成后最终执行参数
                    complete(XMLHttpRequest,status,loadingSta);
                },
                success: function (data) {
                    if(dataJson.cache.use==true){
                        cache.setCache(dataJson.cache.name,data)
                    }
                    if(!config.falseData){
                        eval(data);
                        dataJson.isOk(data);
                    }
                    else {
                        if (data.code == '00000') {
                            dataJson.isOk(data);
                        }
                        else {
                            ajaxErrorSta(data);
                        }
                    }
                },
                error:function(err){
                    setTimeout(function(){
                        dataJson.isError();
                    },500);
                }
            }
        }
        /**
         * getCache(modulesName,fn,time)；
         * argument[0]:存储的模块名称，
         * argument[1]:获取数据后进行视图渲染的方法，
         * argument[2]:缓存时间单位为分钟，不填默认为10分钟
         */
        if(dataJson.cache.use==true) {
            if(cache.getCache(dataJson.cache.name,dataJson.isOk,dataJson.cache.time)==1){
                $.ajax(ajaxJson);
            };
        }
        else{
            $.ajax(ajaxJson);
        }

    }
    return ajax;
});