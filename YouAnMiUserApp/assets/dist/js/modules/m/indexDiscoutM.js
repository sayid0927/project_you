/**
 * Author: 唐润林.
 * Project Name: xxx.
 * Creation Time: 2016/7/26  下午 05:41.
 * Description:    icon 入口
 */
define(['jquery','../../common/ajax/ajax','../../common/getArguments/getArguments',"../../config/config","swiper"],
    function($,ajax,getArguments,config,swiper) {
        function indexDiscoutM(){
            var getArgument = getArguments();
            var datashopId = {
                shopId : getArgument.shopId
            };
            ajax({
                url:config.interfacesConfig.isDiscounts.url,
                dataType:config.interfacesConfig.isDiscounts.dataType,
                type:config.interfacesConfig.isDiscounts.type,
                data:JSON.stringify(datashopId),
                cache:{
                    use:cacheState,
                    name:'indexDiscountV',
                    time:10
                },
                isOk:function(datas){
                    if(datas.data.isDiscount == 0){
                        $(".daodian").remove();
                        $(".baozhang").after('<div class="swiper-slide daodian" type="shopDiscount" data=""><div class="img-wrap"><div class="img-wrap-circle"><img src="images/daodianyouhui.png" /></div></div><span class="span-line">到店优惠</span></div>');
                        $(".daodian").on('click',function(){
                            var data = $(this).attr('data');
                            var type = $(this).attr('type');
                            var newData = '';
                            if(data!=''){
                                newData = JSON.parse(data);
                            }
                            var _SendData = {pageId: pageIdList[type], "data": newData};
                            face.openViewHandle(JSON.stringify(_SendData));
                        });
                        var swiper = new Swiper('.hot-circles .swiper-container', {
                            pagination: '.swiper-paginations',
                            paginationClickable: true,
                            slidesPerView: 5,
                            slidesPerGroup: 5,
                            centeredSlides: false,
                        });
                    }else{
                        var swiper = new Swiper('.hot-circles .swiper-container', {
                            pagination: '.swiper-paginations',
                            paginationClickable: true,
                            slidesPerView: 5,
                            slidesPerGroup: 5,
                            centeredSlides: false,
                        });
                    }

                },
                isError:function(){
                    var swiper = new Swiper('.hot-circles .swiper-container', {
                        pagination: '.swiper-paginations',
                        paginationClickable: true,
                        slidesPerView: 5,
                        slidesPerGroup: 5,
                        centeredSlides: false,
                    });
                    setTimeout(function(){
                        face.loadingFail();
                    },500)
                }
            });
        }
        return indexDiscoutM;
    });