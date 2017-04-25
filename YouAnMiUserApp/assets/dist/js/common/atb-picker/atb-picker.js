/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/10 0010 上午 08:43.
 * Description: cls为触发picker的元素（.class #id），形参arr数组中的数组，为城市的数组，形参fn为回调函数，titleName为pick的title
 * */
define(['zepto','sm'], function ($,sm) {
    function atbPicker(cls,arr,fn,titleName) {
        var setValue= [];
        if(arguments[3]){
            var titleName=titleName;
        }
        else{
            titleName="选择列表";
        }

        for(var i=0;i<arr.length;i++){
            setValue.push({textAlign: 'center', values: arr[i]})
        }
        $(cls).picker({
            toolbarTemplate: '<header class="bar bar-nav">\
                              <button class="button button-link pull-right close-picker">确定</button>\
                              <h1 class="title">'+titleName+'</h1>\
                              </header>',
            cols:setValue,
            formatValue:fn
        });



    }

    return atbPicker;
});