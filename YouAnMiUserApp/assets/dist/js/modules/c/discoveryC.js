/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:   发现页面
 */
define(['../m/discoveryM','../m/discoveryInterFaceAndroid','../../common/checkClient/checkClient','../m/discoveryInterFaceIos'],
    function (discoveryM,indexInterFaceAndroid,checkClient,indexInterFaceIos) {
    function discoveryC(){
        //调用android 或者 ios
        clients = checkClient();
        if(clients == 'android'){
            face = indexInterFaceAndroid();
            face.closeLoadingView();
        }else{
            face = indexInterFaceIos();
        }
        face.constructors();
        discoveryM();
    }
    return discoveryC;
});