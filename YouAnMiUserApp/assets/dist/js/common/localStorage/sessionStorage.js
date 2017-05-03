/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/9 0009 下午 05:41.
 * Description: ....
 */
define([''], function () {
    function setItem(key, value) {
        if(typeof (value)!='string') {
            sessionStorage.setItem(key, JSON.stringify(value));
        }
        else{
            sessionStorage.setItem(key, value);
        }
    };
    function getItem(key) {
        if(sessionStorage.getItem(key)){
            if(sessionStorage.getItem(key).indexOf('{')>=0 && sessionStorage.getItem(key).indexOf('}')>=1){
                return JSON.parse(sessionStorage.getItem(key));
            }
            else{
                return sessionStorage.getItem(key);
            }
        }

    };
    function removeItem(key) {
        return sessionStorage.removeItem(key);
    };

    return {setItem:setItem,getItem:getItem,removeItem:removeItem};
});