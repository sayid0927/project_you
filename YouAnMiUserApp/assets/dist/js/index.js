//require的配置文件
require.config({
    baseUrl:window.location.href.substring(0,window.location.href.lastIndexOf("/")+1)+"js",
    waitSeconds: 30,
    paths : {
        "jquery" : "../common/js/lib/zepto.min",
        "frozen" : "../common/js/lib/frozen",
        "swiper":"../common/js/lib/swiper-3.3.1.jquery.min",
        "dropload":"../common/js/lib/dropload.min",
        "deferred" : "../common/js/lib/deferred"
    },
    shim:{
        "jquery":{
            exports: '$'
        },
        "frozen":{
            exports: ''
        },
        "swiper":{
            exports:"swiper"
        },
        "dropload":{
            exports:"dropload"
        },
        "deferred":{
            exports:"deferred"
        }
    }
});

var appVersion = '20499';
var imgUrlPre = 'http://h5app.youanmi.com/multiTemp/multiTemp/images/';
var client = ''; //判断ios android 的全局变量
var clients = '';//联调android ios 的全局变量
var face = '';//联调android ios 的全局引用变量
var cacheState = false; //缓存是否开启  true开启  false 关闭
var sFlag = true;
var counter = 1;
var pageIdList = {
    'topic': 10,
    'article': 11,  //平台详情
    'articleInfo': 12,
    'banner': 20,   //首页bannnr
    'commission': 21,  //赚佣金
    'shop': 31,
    'recharge': 22,  //流量充值
    'download': 23, //应用管家
    'product': 24,  //优惠商品
    'circle': 25,	//点击进入品牌圈
    'shopDiscount': 26, //到店优惠
    'insurance' : 40, //保障服务
    'dprw': 41, //店铺热文
    'bdjl': 42, //本店交流
    'mdtj': 43, //店铺热文
    'ptzx': 44,  //平台专享
    'dppt': 45, //店铺热文---平台专享   统一给app
    'total' : 100, //全部
    'custom' : 9 //自定义功能&&系统功能
};

var articleKeyAry = [null, 'topic', 'article', 'articleInfo'];
/**
 * 主要入口
 */
require(["./modules/c/indexC"],function(indexC){
    indexC();
});


