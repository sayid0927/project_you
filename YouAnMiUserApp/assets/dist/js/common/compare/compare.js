/**
 * Author: 苏昶宇.
 * Project Name: xxx.
 * Creation Time: 2017/3/29 0009 下午 05:41.
 * Description: 当前版本号：curV；比较版本号：reqV
 */
define([''], function () {
    function compare(curV,reqV){
        if(curV && reqV){
            //将两个版本号拆成数字
            var arr1 = curV.split('.'),
                arr2 = reqV.split('.');
            var minLength=Math.min(arr1.length,arr2.length),
                position=0,
                diff=0;
            //依次比较版本号每一位大小，当对比得出结果后跳出循环（后文有简单介绍）
            while(position<minLength && ((diff=parseInt(arr1[position])-parseInt(arr2[position]))==0)){
                position++;
            }
            diff=(diff!=0)?diff:(arr1.length-arr2.length);
            //若curV大于reqV，则返回true
            return diff>0;
        }else{
            //输入为空
            console.log("版本号不能为空");
            return null;
        }
    }
    return compare;
});