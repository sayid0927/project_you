/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  首页文章
 * 店铺热文（软文）shop_article
 本店交流（平台帖子+门店帖子）   帖子：forum_topic 或 platform_topic
 店铺热文  shop_article
 平台专享（平台文章）article_store
 */
define(['jquery','../m/indexArticlesErM','../m/indexPriseM','../../common/getArguments/getArguments'],
    function ($,indexArticlesErM,indexPriseM,getArguments) {
        function indexArticlesV(datas) {
            localStorage.setItem('data1', JSON.stringify(datas.data));
            var html = '';
            for (var i = 0; i < datas.data.length; i++) {
                var _PositionDiv = '';//这个是单个小图，左边占位的样式
                var imgRight = '';
                var _AbsoluteImg = '';
                var _UserName = '';
                var _UserUrl = '';
                var _TitleColor = '';
                var _IconNeed = '';

                var _LineFour = (datas.data[i].title == '' && datas.data[i].indexArticleType < 3) ? 'item-content item-content-text lineFour' : 'item-content item-content-text';
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
                    numHtml = '<span dataId="data1" class="bigNum" articleId="' + datas.data[i].id + '" tableName="' + datas.data[i].tableName + '" positionNum="article-click'+datas.data[i].id+'"  type="' + datas.data[i].indexArticleType + '"><i class="msg-icon"><img src="images/comment.png"/></i><span class="comment-num">' + comment_num + '</span></span>';
                    _IconNeed += '<div class="action-group">';
                    _IconNeed += '<span class="bigClick" '+styleHtml+' tableName="' + datas.data[i].tableName + '" clickId="' + datas.data[i].id + '" state="'+datas.data[i].isPraise+'"><i class="good-icon disable">';
                    _IconNeed += zanHtml;
                    _IconNeed += '</i>';
                    _IconNeed += '<span class="good-num">' + good_num + '</span></span>'+numHtml;
                    _IconNeed += '</div>';
                }
                var ArticleImgComDiv = '';
                var _Type = '';
                if (typeof datas.data[i].thumImageUrlAry == 'undefined') {
                    ArticleImgComDiv = '';
                } else {
                    _Type = 1;  // _Type = 1  非店铺热文    _Type = 2  店铺热文
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
                html += '<div dataId="data1" class="' +i+ ' article-click" articleId="' + datas.data[i].id + '" tableName="' + datas.data[i].tableName + '" positionNum="article-click'+datas.data[i].id+'"  type="' + datas.data[i].indexArticleType + '">';
                html += '<div class="' + _PositionDiv + '">';
                html += '<div class="item-content item-content-title">';
                html += datas.data[i].title;
                html += '</div>';

                //首页置顶
                var zhidingHtml = '<p class="limit-p-height">';
                if(datas.data[i].isTop == 1){
                    zhidingHtml+= '<span class="label-span-zd">置顶</span>';
                }
                if(datas.data[i].labelAry != undefined && datas.data[i].labelAry.length>0){
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

                html += '</div>';
                html += ArticleImgComDiv;
                html += '</div>';
                html += '<div class="item-footer clearfix">';
                html += ' <div class="user-info" type="' + datas.data[i].tableName + '" >';
                if(datas.data[i].tableName == 'shop_article'){    //店铺热文
                    html += '<img src="images/labelPic.png" class="user-img" alt="user"/>';
                }else if(datas.data[i].tableName == 'forum_topic' || datas.data[i].tableName == 'platform_topic'){  //本店交流
                    html += '<img src="images/bdjl.png" class="user-img" alt="user"/>';
                }else if(datas.data[i].tableName == 'article_store'){  //平台专享
                    html += '<img src="images/ptzx.png" class="user-img" alt="user"/>';
                }else{
                    html += '<img src="images/labelPic.png" class="user-img" alt="user"/>';
                }
                html += '<span class="user-name">' + datas.data[i].articleFrom + '</span>';
                html += '</div>';
                html += _IconNeed;
                html += '</div>';
                html += '</li>';
            }
            $(".webApp-content .article-ul").html(html);
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

            //加载分页
            $(window).on('scroll', function () {
                if ($(window).scrollTop() + $(window).height() >= $(document).height()) {
                    if (!sFlag) {
                        return false;
                    }
                    sFlag = false;
                    counter++;
                    indexArticlesErM();
                }
            }.bind(this));



            $(".article-click,.bigNum").on('click', function () {
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

            $(".user-info").on('click', function () {
                var type = $(this).attr('type');
                var data = '';
                if (type == 'product_article_info') {
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
            $(".bigClick").on('click', function () {
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




            $(".webApp-content").append('<a href="javascript:void(0);" id="returnTop"><img src="' + imgUrlPre + 'icon/up.png"/></a>');
            //回到顶部
            $(window).scroll(function () {
                var scrollTop = $(this).scrollTop();
                if (scrollTop >= 300) {
                    $("#returnTop").show();
                } else {
                    $("#returnTop").hide();
                }
            });

            $.fn.scrollTo = function (options) {
                var defaults = {
                    toT: 0,    //滚动目标位置
                    durTime: 200,  //过渡动画时间
                    delay: 20,     //定时器时间
                    callback: null   //回调函数
                };
                var opts = $.extend(defaults, options),
                    timer = null,
                    _this = this,
                    curTop = _this.scrollTop(),//滚动条当前的位置
                    subTop = opts.toT - curTop,    //滚动条目标位置和当前位置的差值
                    index = 0,
                    dur = Math.round(opts.durTime / opts.delay),
                    smoothScroll = function (t) {
                        index++;
                        var per = Math.round(subTop / dur);
                        if (index >= dur) {
                            _this.scrollTop(t);
                            window.clearInterval(timer);
                            if (opts.callback && typeof opts.callback == 'function') {
                                opts.callback();
                            }
                            return;
                        } else {
                            _this.scrollTop(curTop + index * per);
                        }
                    };
                timer = window.setInterval(function () {
                    smoothScroll(opts.toT);
                }, opts.delay);
                return _this;
            };
            $("#returnTop").click(function(){
                $('body').scrollTo();
            });
            //回到顶部结束
        }

        return indexArticlesV;
    });