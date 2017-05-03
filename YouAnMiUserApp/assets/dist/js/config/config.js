define(['jquery','../common/getArguments/getArguments','../common/baseDecode/baseDecode', '../common/compare/compare'],function ($,getArguments,baseDecode, compare) {
    /**
     * 配置项
     */
    var getArgument = getArguments();
    var strUrl = getArgument.baseUrl;
    var urlDecode = baseDecode(strUrl);

    var falseData = false;//1为开启假数据，0为真实数据
    var appConfig = {
        rootUrl: window.location.href.substring(0, window.location.href.lastIndexOf("/") + 1),//项目所在根目录
        hostUrl: falseData ? window.location.href.substring(0, window.location.href.lastIndexOf("/") + 1) : "http://bis.youanmi.com",//接口的主机IP
        //multiTempUrl : 'http://192.168.1.10:28005'//志学
        multiTempUrl : urlDecode
    };

    /**
     * 接口项
     * @type {{indexInit: {url: string, dataType: string, type: string}}}
     */
    var result = compare(getArguments().appVersion, appVersion);
    if(result){//如果是新版本
        var circles = {
            //url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.rootUrl + "/falseData/data.js",//本地接口url
            //dataType:falseData ? "text" : "text",//本地接口数据类型
            //type: falseData ? "get" : "get"//本地接口请求方式
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/shop/banner/all",//后台接口url
            dataType:falseData ? "json" : "json",//后台接口数据类型
            type: falseData ? "get" : "post"//后台接口请求方式
        };
    }else{
        var circles = {
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/shop/indexCircles",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "get" : "post"//接口请求方式
        };
    }
    var interfacesConfig = { //接口配置
        banner: {
            //url: falseData ? appConfig.hostUrl + "/falseData/data.js" : "http://192.168.1.70:86/uis/shop/indexBanners",//接口url
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/shop/indexBanners",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "get" : "post"//接口请求方式
        },
        circles: circles,//首页功能入口
        isDiscounts: {
            //url: falseData ? appConfig.hostUrl + "/falseData/data.js" : "http://192.168.1.70:86/uis/shop/isDiscount",//接口url
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/shop/isDiscount",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "get" : "post"//接口请求方式
        },
        indexArticles: {
            //url: falseData ? appConfig.hostUrl + "/falseData/data.js" : "http://192.168.1.70:86/uis/shop/indexArticles",//接口url
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/shop/indexArticles",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "get" : "post"//接口请求方式
        },
        appFound: {
            //url: falseData ? appConfig.hostUrl + "/falseData/data.js" : "http://uis.gouwugongyuan.com//appFound/articles",//接口url
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/appFound/articles",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "get" : "post"//接口请求方式
        },
        priseArticle:{
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/shop/priseArticle",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "get" : "post"//接口请求方式
        },
        articleShopInfo:{  //店铺热文详情
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/article/new/shop/info",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "post" : "post"//接口请求方式
        },
        articleStoreInfo:{  //平台文章详情
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/article/new/store/info",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "post" : "post"//接口请求方式
        },
        relateProduct:{  //查看文章关联的商品信息
            url: falseData ? appConfig.hostUrl + "/falseData/data.js" : appConfig.multiTempUrl + "/article/new/relate/product",//接口url
            dataType:falseData ? "json" : "json",//接口数据类型
            type: falseData ? "post" : "post"//接口请求方式
        }
    };
    return {appConfig:appConfig,interfacesConfig:interfacesConfig,falseData:falseData}
});