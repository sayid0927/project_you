/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/12 0012 上午 10:26.
 * Description: ....
 */
define([''], function () {
    function checkMobile(s){
        var patrn = /(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
        if (!patrn.exec(s)){
            return false;
        }
        return true;
    }

    return checkMobile;
});