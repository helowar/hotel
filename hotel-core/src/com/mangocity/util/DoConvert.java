
package com.mangocity.util;

import com.mangocity.util.log.MyLog;

/**
 * 数据
 */
public class DoConvert {
    
  private static final MyLog log = MyLog.getLogger(DoConvert.class);
  public DoConvert() {
  }
  //转换大小写
  public String ConvertAb(String Str){
   String objVal  = Str ;
   String charCode = "";
//   String returnObj = "";
   int i = 0;
   for (i = 0; i < objVal.length(); i++) {
        //charCode = objVal.charAt(i);
    }
   return charCode  ;
  }

  //对个位数进行四舍五入
  public int IntVar(int Str){
    int IntVar = 0 ;
    int IntStr = Str ;
    String StrVar = Integer.toString(IntStr) ;
    String StrVarRight = "" ;
    int StrLen = StrVar.length() ;
    StrVarRight = StrVar.substring(StrLen-1);
    if(StrVarRight.equals("0"))
       IntVar = IntStr ;
    else if(StrVarRight.equals("1"))
       IntVar = IntStr-1;
    else if(StrVarRight.equals("2"))
       IntVar = IntStr-2;
     else if(StrVarRight.equals("3"))
       IntVar = IntStr-3;
     else if(StrVarRight.equals("4"))
       IntVar = IntStr-4;
     else if(StrVarRight.equals("5"))
       IntVar = IntStr+5;
     else if(StrVarRight.equals("6"))
       IntVar = IntStr+4;
     else if(StrVarRight.equals("7"))
       IntVar = IntStr+3;
     else if(StrVarRight.equals("8"))
       IntVar = IntStr+2;
     else if(StrVarRight.equals("9"))
       IntVar = IntStr+1;
    return IntVar ;

  }
  
  /**
   * 将整型转换为字符型
   * @param d
   * @return
   */
  public static char trunIntToChar(int d) {
        if (d >= Character.MIN_VALUE && d <= Character.MAX_VALUE) {  // FIXED
        return (char)d;
      }
      else {
        log.info("Error: "+ d + " out of character bounds");
        return '0';
      }
  }
  
  /**
   * 将双精度转换为长整型
   * @param d
   * @return
   */
  public static long truncDoubToLong(double d) {
      if (d >= Long.MIN_VALUE && d <= Long.MAX_VALUE) {  // FIXED
      return (long)d;
    }
    else {
      log.info("Error: "+ d + " out of long bounds");
      return 0L;
    }
}
 
  /**
   * 将双精度转换为整型
   * @param d
   * @return
   */
  public static int truncDoubToInt(double d) {
      if (d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE) {  // FIXED
      return (int)d;
    }
    else {
      log.info("Error: "+ d + " out of integer bounds");
      return 0;
    }
}
 
  /**
   * 将浮点型数据转换为整型
   * @param d
   * @return
   */
  public static int truncFloatToInt(float d) {
      if (d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE) {  // FIXED
      return (int)d;
    }
    else {
      log.info("Error: "+ d + " out of integer bounds");
      return 0;
    }
}
  /**
   * 将长整型转换为整型
   * @param d
   * @return
   */
  public static int truncLongToInt(long d) {
      if (d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE) {  // FIXED
      return (int)d;
    }
    else {
      log.info("Error: "+ d + " out of integer bounds");
      return 0;
    }
}
  /**
   * 将整形转换为字节型
   * @param d
   * @return
   */
  public static byte truncIntToByte(int d) {
      if (d >= Byte.MIN_VALUE && d <= Byte.MAX_VALUE) {  // FIXED
      return (byte)d;
    }
    else {
      log.info("Error: "+ d + " out of byte bounds");
      return (byte)0;
    }
}

}