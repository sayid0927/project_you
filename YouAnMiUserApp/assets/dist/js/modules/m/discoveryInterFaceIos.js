/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  android首页联调
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments',"../../config/config",'../m/discoveryM','deferred'],
    function($,ajax,getArguments,config,discoveryM,deferred) {
        function indexInterFaceIosM(){
            var _Me = '';
            var fun = {
                constructors:function() {
                    this.launchIosInit();
                },
                launchIosInit:function() {
                    _Me = this;
                    var dtd = $.Deferred();
                    var userId = '';
                    function connectWebViewJavascriptBridge(callback) {
                        if (window.WebViewJavascriptBridge) {
                            callback(WebViewJavascriptBridge);
                        } else {
                            document.addEventListener('WebViewJavascriptBridgeReady', function () {
                                callback(WebViewJavascriptBridge);
                            }, false);
                        }
                    }

                    try {
                        connectWebViewJavascriptBridge(function (bridge) {
                            bridge.init(function (message, responseCallback) {
                                log('JS got a message', message);
                                var data = {'Javascript Responds': 'Wee!'};
                                log('JS responding with', data);
                                responseCallback(data);
                            });

                            bridge.registerHandler('getUserId', function (data) {
                                //userId =data;
                                localStorage.setItem('userId',data);
                                console.log(localStorage.getItem('userId')+'78787887');
                            });
                            //下拉刷新
                            bridge.registerHandler('refreshDiscoveryView', function () {
                                $(".webApp-content").html('<div class="discovery-list-wrap"></div>');
                                discoveryM();
                            });

                            _Me.iosHandleFn = {
                                openView: function (data) {
                                    bridge.callHandler('openView', data);
                                },
                                toLogin: function (data) {
                                    bridge.callHandler('toLogin', data);
                                },
                                loadingFail: function (data) {
                                    bridge.callHandler('loadingFail', data);
                                },
                                getUserId:function(){
                                    userId = localStorage.getItem('userId');
                                    console.log(userId+'123123123');
                                    return userId;
                                }
                            };
                            dtd.resolve();
                        });
                    } catch (error) {
                        alert(error);
                    }
                    return dtd.promise();
                },

                getUpdateData:function() {
                    return this.pageData.data;
                },

                openViewHandle:function(data) {
                    return this.iosHandleFn.openView(data);
                },

                loadingFail:function(data) {
                    return this.iosHandleFn.loadingFail(data);
                },

                getUserId:function() {
                    return this.iosHandleFn.getUserId();
                },

                toLogin:function(data) {
                    return this.iosHandleFn.toLogin(data);
                }
            }

            return fun;
        }

        return indexInterFaceIosM;
    });