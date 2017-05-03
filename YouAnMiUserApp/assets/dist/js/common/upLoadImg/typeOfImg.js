/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/11 0011 下午 02:41.
 * Description: ....
 */
define(['zepto','./showPreview'], function ($,showPreview) {
    function typeOfImg(source,classNames){
        var f=source.value;
        if(f=="")
        { alert("请上传图片");return false;}
        else
        {
            if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(f))
            {
                alert("图片类型必须是.gif,jpeg,jpg,png中的一种");
                return false;
            }
            else{
                var type= f.split('.')[1];//取图片后缀
                if(type=='jpg'||type=="JPG"){
                    type='jpeg'
                }
                showPreview(source,type,classNames)
            }
        }
    }

    return typeOfImg;
});