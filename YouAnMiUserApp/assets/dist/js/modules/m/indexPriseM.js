/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:    icon 入口
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments',"../../config/config"],
    function($,ajax,getArguments,config) {
        function indexPriseM(tableName,clickId,getUid,obj){
            var getArgument = getArguments();
            var dataInt = {
                userId : getUid,//getArgument.shopId,
                tableName: tableName,
                id:clickId
            };
            ajax({
                url:config.interfacesConfig.priseArticle.url,
                dataType:config.interfacesConfig.priseArticle.dataType,
                type:config.interfacesConfig.priseArticle.type,
                data:JSON.stringify(dataInt),
                cache:{
                    use:false,
                    name:'indexArticlesV',
                    time:10
                },
                isOk:function(data){

                    $(obj).append("<span class='numers'>+1</span>");
                    var box = $(".numers");
                    var left = $(obj).offset().left + $(obj).width() / 2;
                    var top = $(obj).offset().top - $(obj).height();
                    box.css({
                        "position": "absolute",
                        "left": left + "px",
                        "top": top + "px",
                        "z-index": 9999,
                        "font-size": '12px',
                        "line-height": '30px',
                        "color": 'ff5f19'
                    });
                    box.animate({
                        "font-size": '30px',
                        "opacity": "0",
                        "top": top - parseInt(30) + "px"
                    }, 600, function () {
                        box.remove();
                    });


                    $(obj).find('img').attr('src','images/thumbsD.png');
                    var _thisI = $(obj).find('.good-num');
                    $(obj).css('color','#ff5f19');
                    var nuberText = _thisI.text();
                    var newNuberText = Number(nuberText) + 1;
                    _thisI.text(newNuberText);
                    $(obj).attr('state','1');
                },
                isError:function(){
                    $('.loading-bg').remove();
                    alert("网络不好，重新加载");
                    indexPriseM();
                }
            });
        }
        return indexPriseM;
    });