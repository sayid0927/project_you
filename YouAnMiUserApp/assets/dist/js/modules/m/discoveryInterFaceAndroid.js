/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  android首页联调
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments',"../../config/config",'../m/discoveryM'],
    function($,ajax,getArguments,config,discoveryM) {
        function discoveryInterFaceAndroidM(){
            var ANDROID = window.js_invoke;
            var fun = {
                constructors:function() {
                    window.refreshDiscoveryView = function () {
                        $(".webApp-content").html('<div class="discovery-list-wrap"></div>');
                        discoveryM();
                    };
                },

                openViewHandle:function(data) {
                    console.log(data);
                    try {
                        ANDROID.openView(data);
                    } catch (error) {
                    }
                },

                closeLoadingView:function() {
                    try {
                        ANDROID.stopLoadingView();
                    } catch (error) {
                    }
                },

                popUpWaring:function(data) {
                    try {
                        ANDROID.showToast(data);
                    } catch (error) {
                        console.info(error)
                    }
                },

                loadingFail:function() {
                    try {
                        ANDROID.loadingFail();
                    } catch (error) {
                        console.info(error)
                    }
                },
                toLogin:function() {
                    try {
                        ANDROID.toLogin();
                    } catch (error) {
                        console.info(error)
                    }
                },
                getUserId:function() {
                    try {
                        return ANDROID.getUserId();
                    } catch (error) {
                        console.info(error)
                    }
                }
            }

            return fun;
        }

        return discoveryInterFaceAndroidM;
    });