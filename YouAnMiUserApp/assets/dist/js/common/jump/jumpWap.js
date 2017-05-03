/**
 * Author: ZheZhen.Cheng.
 * Project Name: webapp.
 * Created on 2016/5/30.
 * Description:jumpMedth.jump在main文件配置
 */
define(["zepto",'../checkClient/checkUserAgent'], function ($,checkUserAgent) {
    function jumpWap() {

        if(jumpMedth.jump==1){
            sessionStorage['noAnimationClient']='noAnimation';
        }
        else if(jumpMedth.jump==2){
            if(checkUserAgent.checkClient()==1||checkUserAgent.checkClient()==5){
                //跳转的时候没有动画效果
                sessionStorage['noAnimationClient']='noAnimation';
            }
            else{
                sessionStorage['noAnimationClient']='Animation';
            }
        }
       else{
            //需要进行页面的普通跳转需在a标签上增加一个类.no-animate
            $('a').addClass('external');
        }
    }

    return jumpWap;
});