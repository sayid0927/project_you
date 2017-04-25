/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:
 */
define(['jquery','../m/indexBannerM','../m/indexEntranceM','../m/indexArticlesM','../m/indexInterFaceAndroid','../../common/checkClient/checkClient','../m/indexInterFaceIos'],
    function ($,indexBannerM,indexEntranceM,indexArticlesM,indexInterFaceAndroid,checkClient,indexInterFaceIos) {
    function indexC(){
        //调用android 或者 ios
        clients = checkClient();
        if(clients == 'android'){
            face = indexInterFaceAndroid();
            face.closeLoadingView();
        }else{
            face = indexInterFaceIos();
        }
        face.constructors();
        indexBannerM();//首页banner
        indexEntranceM();//首页icon入口
        indexArticlesM();//首页文章入口
    }
    return indexC;
});