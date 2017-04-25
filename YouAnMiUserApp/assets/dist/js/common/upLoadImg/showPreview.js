/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/11 0011 下午 02:39.
 * Description: ....
 */
define(['zepto','./getBase64Image','../localStorage/sessionStorage'], function ($,getBase64Image,sStorage) {

        function showPreview(source,type,classNames) {
            var file = source.files[0];

            if(window.FileReader) {
                var fr = new FileReader();
                fr.onloadend = function(e) {
                    $('.'+classNames).attr('src', e.target.result);
                    var img = document.createElement('img');
                    img.src =  e.target.result;
                    img.onload =function() {
                        var data = getBase64Image(img,type);
                        console.log(data);
                        sStorage.setItem(classNames,data)
                    }
                };
                fr.readAsDataURL(file);
            }
        }


    return showPreview;
});