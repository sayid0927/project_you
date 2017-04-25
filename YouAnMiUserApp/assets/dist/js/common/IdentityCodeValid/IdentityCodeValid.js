/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/3/16 0016 下午 03:02.
 * Description: ....
 */
define([''], function () {
    function IdentityCodeValid(code) {
        var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
        var tip = "";
        var pass= true;

        if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
            tip = "身份证号格式错误";
            pass = false;
        }

        else if(!city[code.substr(0,2)]){
            tip = "地址编码错误";
            pass = false;
        }
        else{
            //18位身份证需要验证最后一位校验位
            if(code.length == 18){
                code = code.split('');
                //∑(ai×Wi)(mod 11)
                //加权因子
                var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
                //校验位
                var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
                var sum = 0;
                var ai = 0;
                var wi = 0;
                for (var i = 0; i < 17; i++)
                {
                    ai = code[i];
                    wi = factor[i];
                    sum += ai * wi;
                }
                var last = parity[sum % 11];
                if(parity[sum % 11] != code[17]){
                    tip = "校验位错误";
                    pass =false;
                }
            }
        }
 /*       if(!pass){
            alert(tip);
        } */
        return pass;
    }
   function isNum(s){

       var patrn = /^[0-9]\d*(\.\d+)?$/ ;
       if (!patrn.exec(s)){
           return false;
       }
       return true;
   }

    function checkCarCode(sVIN){
        var Arr = new Array();
        var Brr = new Array();
        Arr['A'] = 1;
        Arr['B'] = 2;
        Arr['C'] = 3;
        Arr['D'] = 4;
        Arr['E'] = 5;
        Arr['F'] = 6;
        Arr['G'] = 7;
        Arr['H'] = 8;
        Arr['J'] = 1;
        Arr['K'] = 2;
        Arr['L'] = 3;
        Arr['M'] = 4;
        Arr['N'] = 5;
        Arr['P'] = 7;
        Arr['R'] = 9;
        Arr['S'] = 2;
        Arr['T'] = 3;
        Arr['U'] = 4;
        Arr['V'] = 5;
        Arr['W'] = 6;
        Arr['X'] = 7;
        Arr['Y'] = 8;
        Arr['Z'] = 9;
        Arr['1'] = 1;
        Arr['2'] = 2;
        Arr['3'] = 3;
        Arr['4'] = 4;
        Arr['5'] = 5;
        Arr['6'] = 6;
        Arr['7'] = 7;
        Arr['8'] = 8;
        Arr['9'] = 9;
        Arr['0'] = 0;
        Brr[1]=8;
        Brr[2]=7;
        Brr[3]=6;
        Brr[4]=5;
        Brr[5]=4;
        Brr[6]=3;
        Brr[7]=2;
        Brr[8]=10;
        Brr[9]=0;
        Brr[10]=9;
        Brr[11]=8;
        Brr[12]=7;
        Brr[13]=6;
        Brr[14]=5;
        Brr[15]=4;
        Brr[16]=3;
        Brr[17]=2;
        var sKYZF="ABCDEFGHJKLMNPRSTUVWXYZ1234567890";
        var sJYW ='';
        var bl = false;
        var blKYZF = false;
        if (sVIN.length == 17)
        {
            var iJQS=0,intTemp=0;
            ht = Arr;
            htZM = Brr;
            try
            {
                for (var i = 0; i <sVIN.length; i++)
                {
                    if (sKYZF.indexOf(sVIN.substr(i, 1)) != -1)
                    {
                        blKYZF = true;
                        iJQS = iJQS + parseInt(ht[sVIN.substr(i, 1)]) * parseInt(htZM[(i + 1)]);
                    }
                    else
                    {
                        blKYZF = false;
                        break;
                    }
                }
                if (blKYZF)
                {
                    intTemp = iJQS%11;
                    if (intTemp == 10)
                    {
                        sJYW = "X";
                    }
                    else
                    {
                        sJYW = intTemp.toString();
                    }
                    if (sJYW == sVIN.substr(8, 1)) bl = true;
                }
                else
                {
                    bl = false;
                }
            }
            catch(err)
            {
                bl = false;
            }
        }
        return bl;
    }

    return {
        IdentityCodeValid:IdentityCodeValid,
        isNum:isNum,
        checkCarCode:checkCarCode
    };
});