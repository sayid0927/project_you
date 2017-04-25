/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:    文章分页
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments',"../../config/config",'./indexPriseM'],
    function($,ajax,getArguments,config,indexPriseM) {
        function indexArticlesErM(){
            var getArgument = getArguments();
            var getUids = face.getUserId();
            var newUserId = '';
            if (getUids == null || getUids == '' || getUids == 'undefined') {
                newUserId = getArgument.userId;
            }else{
                newUserId = getUids;
            }
            $.ajax({
                url:config.interfacesConfig.indexArticles.url,
                dataType:config.interfacesConfig.indexArticles.dataType,
                type:config.interfacesConfig.indexArticles.type,
                contentType:"application/json",
                beforeSend: function (xhr) {
                    if($(".dropload-load").length<1){
                        $(".article-list-wrap").append('<div class="dropload-load"><span class="loading"></span>加载中...</div>');
                    }
                },
                complete : function(XMLHttpRequest,status){//请求完成后最终执行参数
                    //complete(XMLHttpRequest,status,loadingSta);
                   $(".dropload-load").remove();

                },
                data:JSON.stringify({
                    shopId : getArgument.shopId,
                    pageIndex: counter,
                    userId : newUserId
                }),
                cache:{
                    use:cacheState,
                    name:'indexArticlesErV'+counter,
                    time:1
                },
                success:function(datas){
                    sFlag = true;
                    var html = '';console.log(counter);
                    localStorage.setItem('data'+counter, JSON.stringify(datas.data));
                    for(var i = 0;i<datas.data.length;i++){
                        var _PositionDiv = '';//这个是单个小图，左边占位的样式
                        var imgRight = '';
                        var _AbsoluteImg = '';
                        var _UserName = '';
                        var _UserUrl = '';
                        var _TitleColor = '';
                        var _IconNeed = '';

                        var _LineFour = (datas.data[i].title =='' && datas.data[i].indexArticleType<3) ? 'item-content item-content-text lineFour' : 'item-content item-content-text';
                        if (typeof datas.data[i].publishName === 'undefined' || datas.data[i].tableName == 'article_store') {
                            if (datas.data[i].publishMan == 1) {
                                _UserName = "匿名";
                                _UserUrl = imgUrlPre + "/icon/defaultIcon.png";
                            } else {
                                _UserUrl = imgUrlPre + "/youanmi.png";
                                _UserName = "柚安米";
                            }
                        } else {
                            _UserUrl = typeof datas.data[i].publishHeadUrl === 'undefined' || datas.data[i].publishHeadUrl.length == 0 ?
                            imgUrlPre + "/icon/defaultIcon.png" : datas.data[i].publishHeadUrl;
                            _UserName = datas.data[i].publishName;
                        }
                        var numHtml,zanHtml,styleHtml = '';
                        var good_num = (datas.data[i].praiseAmount==0 ? '' : datas.data[i].praiseAmount);
                        if(datas.data[i].isPraise==2){ //点赞信息 1.点赞  2。没有点赞
                            zanHtml = '<img src="images/thumbsUp.png"/>';
                        }else{
                            zanHtml = '<img src="images/thumbsD.png"/>';
                            styleHtml = 'style="color:#ff5f19;"';
                        }
                        if (datas.data[i].tableName == 'article_store') {
                            _IconNeed += '<div class="action-group">';
                            _IconNeed += '<span class="bigClick" '+styleHtml+' tableName="' + datas.data[i].tableName + '" clickId="' + datas.data[i].id + '" state="'+datas.data[i].isPraise+'"><i class="good-icon disable">';
                            _IconNeed += zanHtml;
                            _IconNeed += '</i>';
                            _IconNeed += '<span class="good-num">' + good_num + '</span></span>';
                            _IconNeed += '</div>';
                        } else {
                            var comment_num = (datas.data[i].replyAmount == 0 ? '' : datas.data[i].replyAmount);
                            numHtml = '<span dataId="data'+counter+'" class="bigNum" articleId="' + datas.data[i].id + '" tableName="' + datas.data[i].tableName + '" positionNum="article-click'+datas.data[i].id+'"  type="' + datas.data[i].indexArticleType + '"><i class="msg-icon"><img src="images/comment.png"/></i><span class="comment-num">' + comment_num + '</span></span>';
                            _IconNeed += '<div class="action-group">';
                            _IconNeed += '<span class="bigClick" '+styleHtml+' tableName="' + datas.data[i].tableName + '" clickId="' + datas.data[i].id + '" state="'+datas.data[i].isPraise+'"><i class="good-icon disable">';
                            _IconNeed += zanHtml;
                            _IconNeed += '</i>';
                            _IconNeed += '<span class="good-num">' + good_num + '</span></span>'+numHtml;
                            _IconNeed += '</div>';
                        }
                        var ArticleImgComDiv = '';
                        var _Type = '';
                        if(typeof datas.data[i].thumImageUrlAry == 'undefined'){
                            ArticleImgComDiv = '';
                        }else{
                            _Type = 1;
                            if (datas.data[i].tableName == 'shop_article') {
                                _Type = 2;
                            }
                            var noneDiv = '';
                            if(datas.data[i].thumImageUrlAry.length == 0){
                                noneDiv = 'noneDiv';
                            }
                            ArticleImgComDiv += '<div class=" item-img-group clearfix '+noneDiv+'">';
                            var imgHtml = '';
                            for (var j = 0; j < datas.data[i].thumImageUrlAry.length; j++) {
                                var addPosition = '';
                                //   isCoverImage  标示是否有封面  1.是 2.否
                                if(datas.data[i].isCoverImage == 1){
                                    //single 显示大图
                                    imgHtml += '<div class="item-img-wrap single">';
                                    imgHtml += '<img src=' + datas.data[i].thumImageUrlAry[0] + '>';
                                    imgHtml += '</div>';
                                    break;
                                }else{
                                    //这里是判断少于三张图片的并且无封面图的
                                    if(datas.data[i].thumImageUrlAry.length<3 && datas.data[i].thumImageUrlAry.length>=1){
                                        _PositionDiv = 'positionDiv';
                                        addPosition = 'verticalImg';
                                        imgRight = 'imgRight';
                                    } else {
                                        _PositionDiv = '';
                                        imgRight = '';
                                    }
                                    if (datas.data[i].thumImageUrlAry.length == 2) { //小于3张的时候显示一张
                                        if (j == 1) break;
                                    }
                                    imgHtml += '<div class="'+imgRight+' item-img-wrap item-img-list '+addPosition+'">';
                                    imgHtml += '<p class="item-img-wrap-content" style="background: url(' + datas.data[i].thumImageUrlAry[j] + ') center center;background-repeat: no-repeat;background-size: cover;"></p>';
                                    imgHtml += '</div>';
                                    if (j == 2) break; //超过三张显示三张
                                }
                            }
                            ArticleImgComDiv += imgHtml + '</div>';
                        }

                        //首页&本地圈中的“本店交流”内容前需增加标题：【本店交流】   forum_topic 或 platform_topic
                        var bdjlText = '';
                        if(datas.data[i].tableName == 'forum_topic' || datas.data[i].tableName == 'platform_topic'){
                            bdjlText = '<span class="bold-size">【本店交流】</span>';
                        }
                        html += '<li class="article-item clearfix article-click'+datas.data[i].id+'" data="' + datas.data[i].replyAmount + '">';
                        html+='<div dataId="data'+counter+'" class="' +i+ ' article-click" articleId="' + datas.data[i].id + '" tableName="' + datas.data[i].tableName + '" positionNum="article-click'+datas.data[i].id+'"  type="' + datas.data[i].indexArticleType + '">';
                        html+='<div class="'+_PositionDiv+'">';
                        html+='<div class="item-content item-content-title">';
                        html+= datas.data[i].title;
                        html+='</div>';

                        //首页置顶
                        var zhidingHtml = '<p class="limit-p-height">';
                        if(datas.data[i].isTop == 1){
                            zhidingHtml+= '<span class="label-span-zd">置顶</span>';
                        }
                        if(datas.data[i].labelAry.length>0){
                            for(var h=0;h<datas.data[i].labelAry.length;h++){
                                zhidingHtml += '<span class="label-span">'+datas.data[i].labelAry[h]+'</span>';
                            }
                        }
                        zhidingHtml += '</p>';
                        if(_Type!=2){
                            //非店铺热文且图片小于三张=》即一张显示在右边的时候， 标签放在内容的下边     本店交流无标签 也同理
                            if((datas.data[i].thumImageUrlAry.length<3 && datas.data[i].thumImageUrlAry.length>=1) || datas.data[i].tableName == 'forum_topic' || datas.data[i].tableName == 'platform_topic'){
                                html += ' <div class="' + _LineFour + '">'+bdjlText+datas.data[i].content+'</div>'+zhidingHtml;
                            }else{
                                html += zhidingHtml+' <div class="' + _LineFour + '">'+bdjlText+datas.data[i].content+'</div>';
                            }
                        }else{
                            html += zhidingHtml+' <div class="' + _LineFour + '">'+bdjlText+datas.data[i].content+'</div>';
                        }

                        html+='</div>';
                        html+= ArticleImgComDiv;
                        html+='</div>';
                        html+='<div class="item-footer clearfix">';
                        html+=' <div class="user-info" type="' + datas.data[i].tableName + '" >';
                        if(datas.data[i].tableName == 'shop_article'){    //店铺热文
                            html += '<img src="images/labelPic.png" class="user-img" alt="user"/>';
                        }else if(datas.data[i].tableName == 'forum_topic' || datas.data[i].tableName == 'platform_topic'){  //本店交流
                            html += '<img src="images/bdjl.png" class="user-img" alt="user"/>';
                        }else if(datas.data[i].tableName == 'article_store'){  //平台专享
                            html += '<img src="images/ptzx.png" class="user-img" alt="user"/>';
                        }else{
                            html += '<img src="images/labelPic.png" class="user-img" alt="user"/>';
                        }
                        html+='<span class="user-name">'+datas.data[i].articleFrom+'</span>';
                        html+='</div>';
                        html+= _IconNeed;
                        html+='</div>';
                        html+='</li>';

                    }
                    $(".webApp-content .article-ul").append(html);
                    //设置等宽等高图片
                    var width_height = $(".item-img-list").width()-10;
                    $(".item-img-wrap-content").css('padding-bottom',width_height+'px');
                    //循环左边文字  右边图片的  设置等高。防止样式乱
                    $(".positionDiv").each(function(){
                        var p_height = $(this).next().find('p').height();
                        if($(this).height() < p_height){
                            $(this).parent().css('height',p_height+'px');
                            $(this).next().css('height',p_height+'px');
                        }
                    });
                    $(".article-click,.bigNum").unbind('click').on('click', function () {
                        var positionnum = $(this).attr('positionnum');//这里是缓存评论数位置
                        localStorage.setItem('numer', positionnum);
                        var type = $(this).attr('type');
                        var articleId = $(this).attr('articleId');
                        var tablename = $(this).attr('tablename');
                        var leixing = articleKeyAry[type];

                        var dataId = $(this).attr('dataId');
                        var getData = JSON.parse(localStorage.getItem(dataId));
                        console.log(getData);
                        var nowI = '';
                        for(var i = 0;i<getData.length;i++){
                            if(getData[i].tableName == tablename && articleId == getData[i].id){
                                nowI = i;
                                break;
                            }
                        }
                        //console.log(nowI);
                        console.log(getData[nowI]);
                        var _SendData = {pageId: pageIdList[leixing], "data": getData[nowI]};
                        var getArgument = getArguments();
                        //文章详情参数拼接
                        var _SendDatas = '';
                        if(tablename=='shop_article'){ //店铺热文详情
                            _SendDatas = {pageId: pageIdList['article'],newpageId: pageIdList['dppt'], 'articleType':1, "data": getData[nowI],"articleId":articleId};
                            face.openViewHandle(JSON.stringify(_SendDatas));
                        }else if(tablename=='article_store'){  //平台文章详情
                            _SendDatas = {pageId: pageIdList['article'],newpageId: pageIdList['dppt'], 'articleType':2, "data": getData[nowI],"articleId":articleId};
                            face.openViewHandle(JSON.stringify(_SendDatas));
                        }else{
                            face.openViewHandle(JSON.stringify(_SendData));
                        }
                    });
                    $(".user-info").unbind('click').on('click', function () {
                        var type = $(this).attr('type');
                        var data = '';
                        if (type == 'shop_article') {
                            data = 'dprw';
                        } else if (type == 'forum_topic' || type == 'platform_topic') {
                            data = 'bdjl';
                        } else if (type == 'shop_article') {
                            data = 'mdtj';
                        } else {
                            data = 'ptzx';
                        }
                        var cityIdData = $(".cityId").attr('data');
                        var newData = JSON.parse(cityIdData);
                        if(data == 'bdjl'){
                            var _SendData = {pageId: pageIdList[data], "data": newData};
                        }else{
                            var _SendData = {pageId: pageIdList[data], "data": ''};
                        }
                        face.openViewHandle(JSON.stringify(_SendData));
                    });
                    $(".bigClick").unbind('click').on('click', function () {
                        var tableName = $(this).attr('tableName');
                        var clickId = $(this).attr('clickId');
                        var state = $(this).attr('state');
                        var getUid = face.getUserId();
                        if (getUid == null || getUid == '' || getUid == 'undefined') {
                            face.toLogin();
                            return;
                        }
                        if (state == 2) {
                            indexPriseM(tableName, clickId, getUid, this);
                        }else{
                            if($('body').find('.tcts').length>0) return;
                            var hl = '<div class="tcts">介么喜欢呀，已赞过了哦...</div>'
                            $('body').append(hl);
                            $(".tcts").fadeIn(500);
                            setTimeout(function(){
                                $(".tcts").fadeOut(500).remove();
                            },2000);
                        }
                    });
                    if(datas.data.length == 0){
                        var appendHtml = '<div class="dropload-down"><div class="dropload-refresh">没有更多了！</div></div>';
                        if($(".dropload-down").length<1){
                            $(".article-list-wrap").append(appendHtml);
                        }
                    }
                },
                error:function(){
                    $('.loading-bg').remove();
                    console.log("网络不好，重新加载");
                    indexArticlesErM();
                }
            });




        }
        return indexArticlesErM;
    });