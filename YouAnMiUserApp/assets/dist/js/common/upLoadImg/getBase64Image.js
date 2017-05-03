/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/11 0011 下午 02:41.
 * Description: ....
 */
define([''], function () {
    function getBase64Image(img,type) {
        var canvas = document.createElement("canvas");
        canvas.width = img.width;
        canvas.height = img.height;
        var ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0, img.width, img.height);
        var dataURL = canvas.toDataURL("image/"+type);
        return dataURL;
    }

    return getBase64Image;
});