define(["jquery"],function($) {
    function ajaxErrorSta(datas) {
        $('.loading-bg').remove();
        if(datas.code==20102) {
        //session过期，跳转到登录
        }
        else if(datas.code==20101){
       //session无效，跳转到登录
        }
        else{
           console.log(datas.data)
        }
    }
    return ajaxErrorSta;
});