/**
 * 创建时间：2016-03-20
 * 创建人：czz
 * 缓存机制cache(selectCityRender,cacheData,1)
 *
 * getCache(modulesName,fn,time)；
 * argument[0]:存储的模块名称，
 * argument[1]:获取数据后进行视图渲染的方法，
 * argument[2]:缓存时间单位为分钟，不填默认为10分钟
 *
 * setCache(modulesName,datas)；
 * argument[0]:存储的模块名称，
 * argument[1]:缓存的数据，
 */
define(['jquery','../localStorage/sessionStorage','../localStorage/localStorage'],
    function ($,sStorage,lStorage) {
    function getCache(modulesName,fn,time){
        if(arguments[2]){
            var times=time;
        }
        else{
           times=10;
        }
        if(lStorage.getItem('cache')!=undefined&&lStorage.getItem('cache')[modulesName]!=undefined){
            var timestamp=new Date().getTime();
            var disTime=times*60*1000;
            var nowDis=timestamp-lStorage.getItem('cache')[modulesName][1];
            // console.log(timestamp)
            if(nowDis>=disTime){
               console.log('超过缓存时间');
               return 1;
            }
            else{
                console.log('getCache');
                var datas=lStorage.getItem('cache')[modulesName][0];
                fn(datas);

            }
        }
        else{
            return 1;
        }
    }
    function setCache(modulesName,datas){
        var timestamp=new Date().getTime();
        var lStorageData=lStorage.getItem('cache');
        if(lStorageData==undefined){
           lStorageData={};
        }
        var lStorageData1=[datas,timestamp];
        lStorageData[modulesName]=lStorageData1;
        lStorage.setItem('cache',lStorageData);
        console.log('setCache');
    }
    return {getCache:getCache,setCache:setCache};
});