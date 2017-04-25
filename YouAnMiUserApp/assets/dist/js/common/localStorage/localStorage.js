/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/9 0009 下午 05:41.
 * Description: ....
 */
define([''], function () {
    function setItem(key, value) {
        if(typeof (value)!='string') {
            localStorage.setItem(key, JSON.stringify(value));
        }
        else{
            localStorage.setItem(key, value);
        }
    };
    function getItem(key) {
        if(localStorage.getItem(key)){
            if(localStorage.getItem(key).indexOf('{')>=0 && localStorage.getItem(key).indexOf('}')>=1){
                return JSON.parse(localStorage.getItem(key));
            }
            else{
                return localStorage.getItem(key);
            }
        }

    };
    function removeItem(key) {
        return localStorage.removeItem(key);
    };

    return {setItem:setItem,getItem:getItem,removeItem:removeItem};
});