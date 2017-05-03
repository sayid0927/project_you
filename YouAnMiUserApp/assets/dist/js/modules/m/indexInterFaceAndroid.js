/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  android首页联调
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments',"../../config/config",'../m/indexBannerM','../m/indexEntranceM','../m/indexArticlesM'],
    function($,ajax,getArguments,config,indexBannerM,indexEntranceM,indexArticlesM) {
        function indexInterFaceAndroidM(){
            var ANDROID = window.js_invoke;
            var fun = {
                constructors:function() {
                    //下拉刷新
                    window.refreshView = function () {
                        $('.ui-loading-block').remove();
                        $(".webApp-content").html('<div class="banner"></div> ' +
                            '<div class="s-function-list hot-circles clearfix"> ' +
                            '<ul> ' +
                            '</ul> ' +
                            '</div> ' +
                            '<section class="article-list-wrap"> ' +
                            '<ul class="article-ul"> ' +
                            '</ul> ' +
                            '</section>');
                        indexBannerM();
                        indexEntranceM();
                        indexArticlesM();
                        counter = 1;
                    };
                    //评论数累加
                    window.refreshComment = function (commentNums){
                        var positionNum = localStorage.getItem('numer');
                        var re = /^[1-9]+[0-9]*]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
                        if(re.test(commentNums)){
                            var nowNum = parseInt($("."+positionNum).attr("data"));
                            var obj = $("."+positionNum).find('.comment-num');
                            var newNum = parseInt(commentNums)+nowNum;
                            $("."+positionNum).attr("data",newNum);
                            obj.html(newNum);
                        }
                    };
                    window.setLoginUserId = function (userId) {
                        window.userId=userId;
                    };
                },

                openViewHandle:function(data) {
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

        return indexInterFaceAndroidM;
    });