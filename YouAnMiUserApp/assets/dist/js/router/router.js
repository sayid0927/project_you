define(["jquery","underscore","backbone","../modules/c/articleDetailC"],
    function($,_,Backbone,articleDetailC) {
        function router() {
            var route = Backbone.Router.extend({
                routes: {
                    "*articleDetail?shopId=:arg1&baseUrl=:arg2&brandName=:arg3&DeviceID=:arg4&DeviceType=:arg5&userId=:arg6": "articleDetail",
                },
                articleDetail: function (arg1,arg2,arg3,arg4,arg5,arg6) {
                    articleDetailC(arg1,arg2,arg3,arg4,arg5,arg6);
                },

            });

            var app_router=new route();
            Backbone.history.start();

        }
        return router;
    });