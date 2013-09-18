/*

==================================================================

�Ƿ�Ϊ�գ�ֻ�ж��ַ���
null��0��Ϊ�գ�����trim
IsStringNull(string)

==================================================================

*/

function IsStringNull(str) {
    if (str == null)
        return true;
    var trimStr = Trim(str);
    if (trimStr.length == 0)
        return true;
    return false;
}
/*

==================================================================

LTrim(string):ȥ����ߵĿո�

==================================================================

*/

function LTrim(str) {
    var whitespace = new String(" \t\n\r");
    var s = new String(str);

    if (whitespace.indexOf(s.charAt(0)) != -1) {
        var j = 0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1) {
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}


/*

==================================================================

RTrim(string):ȥ���ұߵĿո�

==================================================================

*/

function RTrim(str) {
    var whitespace = new String(" \t\n\r");
    var s = new String(str);
    if (whitespace.indexOf(s.charAt(s.length - 1)) != -1) {
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1) {
            i--;
        }
        s = s.substring(0, i + 1);
    }
    return s;
}


/*

==================================================================

Trim(string):ȥ��ǰ��ո�

==================================================================

*/

function Trim(str) {
    return RTrim(LTrim(str));
}

/*

==================================================================

IsOutOfLength(string,int):�ж��ַ����ǳ����Ƿ񳬳����ȣ�����Ϊ2���ַ�

==================================================================

*/

function IsOutOfLength(str, len) {
    var strLength = 0;
    for (var i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) > 256) {
            strLength++;
        }
        strLength++;
        if (strLength > len) {
            return true;
        }
    }
    return false;
}

/*
==================================================================

IsOutOfLength(string,int):�ж��ַ����ǳ����Ƿ񳬳����ȣ�����Ϊ3���ַ�

==================================================================

*/

function IsOutOfLength3(str, len) {
    var cArr = str.match(/[^\x00-\xff]/ig);
    var len_address = str.length + (cArr == null ? 0 : cArr.length * 2);
    if (len_address > len)
        return true;
    else
        return false;
}


/*

==================================================================

IsNumeric(string):�ж��ַ������Ƿ�Ϊ����

==================================================================

*/


function IsNumeric(strNumber) {
    if (strNumber.length == 0) {
        return false;
    }
    return (strNumber.search(/^(-|\+)?\d+(\.\d+)?$/) != -1);
}

/*

==================================================================

IsInt(string,string,int or string):(�����ַ���,+ or - or empty,empty or 0)

���ܣ��ж��Ƿ�Ϊ����������������������������+0��������+0

=================================================================
*/

function IsInt(objStr, sign, zero) {
    var reg;
    var bolzero;


    if (Trim(objStr) == "") {
        return false;
    }
    else {
        objStr = objStr.toString();
    }


    if ((sign == null) || (Trim(sign) == "")) {
        sign = "+-";
    }


    if ((zero == null) || (Trim(zero) == "")) {
        bolzero = false;
    }
    else {
        zero = zero.toString();
        if (zero == "0") {
            bolzero = true;
        }
        else {
            alert("����Ƿ����0������ֻ��Ϊ(�ա�0)");
        }
    }


    switch (sign) {
        case "none":
            if (!bolzero) {
                reg = /^[0-9]*[1-9][0-9]*$/;
            }
            else {
                reg = /^[0-9]*[0-9][0-9]*$/;
            }
            break;
        case "+-":
        //����
            reg = /(^-?|^\+?)\d+$/;
            break;
        case "+":
            if (!bolzero) {
                //������
                reg = /^\+?[0-9]*[1-9][0-9]*$/;
            }
            else {
                //������+0
                //reg=/^\+?\d+$/;
                reg = /^\+?[0-9]*[0-9][0-9]*$/;
            }
            break;
        case "-":
            if (!bolzero) {
                //������
                reg = /^-[0-9]*[1-9][0-9]*$/;
            }
            else {
                //������+0
                //reg=/^-\d+$/;
                reg = /^-[0-9]*[0-9][0-9]*$/;
            }
            break;
        default:
            alert("�����Ų�����ֻ��Ϊ(�ա�+��-)");
            return false;
            break;
    }


    var r = objStr.match(reg);
    if (r == null) {
        return false;
    } else {
        return true;
    }
}


/*
==================================================================

checkIsValidDate(string)

���ܣ��ж��Ƿ�Ϊ��ȷ���������͡�����Ϊyyyy-MM-dd

=================================================================
*/
function checkIsValidDate(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "")
        return true;
    var pattern = /^\d{4}\/\d{1,2}\/\d{1,2}$/g;
    if (!pattern.test(str))
        return false;
    //alert("��" +str+"��1");
    var arrDate = str.split("/");
    var date = new Date(arrDate[0], (parseInt(arrDate[1], 10) - 1) + "", parseInt(arrDate[2], 10) + "");
    //alert("a:��" +date.getFullYear()+"����" + date.getMonth() + "����" + date.getDate() + "��");
    //alert("b:��" +arrDate[0]+"����" + parseInt(arrDate[1],10) + "����" + parseInt(arrDate[2],10) + "��");
    if (date.getFullYear() == arrDate[0]
            && date.getMonth() == (parseInt(arrDate[1], 10) - 1) + ""
            && date.getDate() == parseInt(arrDate[2], 10) + "")
        return true;
    else
    //alert("��" +str+"��2");
        return false;
}
/*
==================================================================

checkIsValidTime(string)

���ܣ��ж��Ƿ�Ϊ��ȷ��ʱ�����͡�����Ϊhh:mm:ss

=================================================================
*/
function checkIsValidTime(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "")
        return true;
    var pattern = /^\d{1,2}:\d{1,2}:\d{1,2}$/g;
    if (!pattern.test(str))
        return false;
    //alert("��" +str+"��1");

    return true;
}

/*

==================================================================

CheckedCount(containForm,chkFormName):����һ��form��ѡ�������Ŀ
check������radiobox��checkbox
����������check���form,check��������
==================================================================

*/


function CheckedCount(containForm, chkFormName) {
    var chkCount = 0;
    for (i = 0; i < containForm.elements.length; i++) {
        if (containForm.elements[i].name == chkFormName) {
            if (containForm.elements[i].type == 'checkbox' || containForm.elements[i].type == 'radio') {
                if (containForm.elements[i].checked) {
                    chkCount++;
                }
            }
        }
    }
    return chkCount;

}

/**
 * �ж��ǲ�����Ч��email��ַ
 */
function IsValidateEmail(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "" || str.length == 0) {
        return false;
    }

    //������ʽ
    //var pattern = /^\w{1,}@[\.,\w]{1,}$/;
    var pattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}

/**
 * �ж��ǲ�����Ч�ĺ���
 */
function checkIsHanzi(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "" || str.length == 0) {
        return true;
    }

    //������ʽ
    var pattern = /[^\u4E00-\u9FA5]/g;
    if (pattern.test(str)) {
        return false;
    }
    return true;
}
/**
 * �ж��ǲ�����Ч��Ӣ����ĸ+(�ո�
 */
function checkIsLetter(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "" || str.length == 0) {
        return true;
    }

    //������ʽ
    var pattern = /[^a-zA-Z\s]/g;
    if (pattern.test(str)) {
        return false;
    }
    return true;
}
/**
*�ж��ǲ�����Ч��Ӣ����ĸ+(�ո��㣩
*/
function checkIsLetterOrSpaceDot(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "" || str.length == 0) {
        return true;
    }

    //������ʽ
    var pattern = /[^a-zA-Z\s\.]/g;
    if (pattern.test(str)) {
        return false;
    }
    return true;
}
/**
 * �ж��ǲ�����Ч��Ӣ����ĸ������
 */
function checkIsLetterNumber(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "" || str.length == 0) {
        return true;
    }

    //������ʽ
    var pattern = /[^a-zA-Z0-9\s]/g;
    if (pattern.test(str)) {
        return false;
    }
    return true;
}
/**
 * �ж��ǲ�����Ч�����֣����֤�����룬�����ã�
 */
function checkIsNumber(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "" || str.length == 0) {
        return true;
    }

    //������ʽ
    var pattern = /[^0-9\s]/g;
    if (pattern.test(str)) {
        return false;
    }
    return true;
}
/**
 * �ж��ǲ�����Ч�İٷֱ�����
 */
function checkIsPercent(str) {
    //���Ϊ�գ���ͨ��У��
    if (str == "" || str.length == 0) {
        return true;
    }

    //������ʽ
    var pattern = /^[1-9][0-9]*%$/g;
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}

/**
 * check is validate time
 */
function isValidateTime(str) {
    if (parseInt(str) == 0) {
        return true;
    }
    var regexp = /^(([0-9])|(0[0-9])|(1[0-9])|(2[0-3]))[0-5][0-9]$/
    if (str == "" || str.length == 0) {
        return false;
    }
    if (!regexp.test(str)) {
        return false;
    }
    return true;
}


/**
 * �ж��ǲ�����Ч���ֻ�����
 * ��ʽ��ȷ����true,����false.
 */
function IsValidateMobile(str) {
    var pattern = /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
    if (str == '' || str.length == 0) {
        return false;
    }
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}


/**
 * �ж��ǲ�����Ч�ĵ绰����;
 * �绰�����ʽ��ȷ����true,����false.
 */
function IsValidatePhone(str) {
    var pattern = /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
    if (str == '' || str.length == 0) {
        return false;
    }
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}


/**
 * �ж��ǲ�����Ч����������;
 * ��ʽ��ȷ����true,����false.
 */
function IsValidateZipcode(str) {
    var pattern = /^[1-9]\d{5}$/;
    if (str == '' || str.length == 0) {
        return false;
    }
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}

