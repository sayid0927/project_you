/**
 * Created by 程哲振 on 2016/2/25 0025 下午 03:52.
 */
define(['jquery','frozen','../getArguments/getArguments'], function ($,frozen,getArguments) {
    function beforeSendLoading(XMLHttpRequest,status,loadingSta){
       // $.showPreloader()
        var getArgument = getArguments();//获取参数函数
        XMLHttpRequest.setRequestHeader('DeviceID', getArgument.DeviceID);
        XMLHttpRequest.setRequestHeader('DeviceType', getArgument.DeviceType);
        XMLHttpRequest.setRequestHeader('Content-Type', 'application/json');


    }

    return beforeSendLoading;
});