/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:
 */
define(['jquery','../m/indexInterFaceAndroid','../../common/checkClient/checkClient','../m/indexInterFaceIos', '../m/moreFuncitonM'],
    function ($,indexInterFaceAndroid,checkClient,indexInterFaceIos, moreFuncitonM) {
        function moreFuncitonC(){
            //调用android 或者 ios
            clients = checkClient();
            if(clients == 'android'){
                face = indexInterFaceAndroid();
                face.closeLoadingView();
            }else{
                face = indexInterFaceIos();
            }
            face.constructors();
            moreFuncitonM();
        }
        return moreFuncitonC;
    });