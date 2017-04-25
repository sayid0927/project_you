/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:  首页bannner
 */
define(['jquery','swiper'],
    function ($,swiper) {
    function indexBannerV(datas){
        var html = '';
        html +='<div class="swiper-container">';
        html +='<div class="swiper-wrapper">';
        var sliderHtml = '';
        for(var i = 0;i < datas.data.banners.length; i++){
            sliderHtml += '<div class="swiper-slide banner-click" data=\''+JSON.stringify(datas.data.banners[i])+'\'>';
            sliderHtml += '<img src="'+datas.data.banners[i].imageUrl+'" alt="'+datas.data.banners[i].title+'" data-jumpurl="'+datas.data.banners[i].url+'"/>';
            sliderHtml += '</div>';
        }
        html += sliderHtml+'</div>';
        html +='<div class="swiper-pagination"></div>';
        html +='</div> ';
        $(".webApp-content .banner").html(html);
        var swiper = new Swiper('.banner .swiper-container', {
            autoplay: 5000,
            pagination: '.swiper-pagination',
            paginationClickable: true
        });

        $(".banner-click").on('click',function(){
            var data = $(this).attr('data');
            var newData = JSON.parse(data);
            var _SendData = {pageId: pageIdList['banner'], "data": newData};
            face.openViewHandle(JSON.stringify(_SendData));
        });
    }

    return indexBannerV;
});