package com.mangocity.hotel.base.util;

import com.mangocity.util.StringUtil;

public abstract class HotelStrUtil {

	 /**
     * 根据每天的房态和房间数量数据，判断能否预订
     * 
     * @param roomStates
     * @param qtys
     * @return
     */
    public static int handleRoomStates(String[] roomStates, int[] qtys) {
        int result = -1;
        int[] bedStatus = { -1, -1, -1 };
        boolean[] bedUse = { false, false, false };
        for (int j = 0; j < roomStates.length; j++) {
            String roomState = roomStates[j];
            if (!StringUtil.isValidStr(roomState)) {
                continue;
            }
            // 解析房态1:0/2:-1/3:4
            String[] roomArray = roomState.split("/");
            for (int i = 0; i < roomArray.length; i++) {
                String[] testStr2 = roomArray[i].split(":");
                if(roomState.indexOf("G")>=0){
                	if(testStr2.length>=3){
                		if(StringUtil.isValidStr(testStr2[3])){
                			qtys[j] = Integer.valueOf(testStr2[3]).intValue();
                		}
                	}
                }
                int bedIndex = Integer.parseInt(testStr2[0]) - 1;
                if (testStr2[1].equals("4") || (testStr2[1].equals("3") && 0 >= qtys[j])) {
                    if (4 > bedStatus[bedIndex]) {
                        bedStatus[bedIndex] = 4;
                    }
                }
                bedUse[bedIndex] = true;
            }
        }
        if ((bedUse[0] && -1 == bedStatus[0]) || (bedUse[1] && -1 == bedStatus[1])
            || (bedUse[2] && -1 == bedStatus[2])
            || (false == bedUse[0] && false == bedUse[1] && false == bedUse[2])) {
            return result;
        }
        return 4;
    }
    
    
    /**
     * 根据客房类型和房间状态，替换room_statu状态
     * 
     * @param roomStatu
     *            , 这个是当前数据库中的room_statu;
     * @param curRoomStatus
     * @return by guojun 2008-12-19 11:12
     */
    public static String replaceRoomStatus(String roomStatu, String curRoomStatus) {
        String strRoomStatu = roomStatu;
        String strCurRoomStatus = curRoomStatus;
        String desRoomStatus = "";

        if (null == curRoomStatus || curRoomStatus.equals("")) {
            return desRoomStatus = strRoomStatu;
        }

        if (null == strRoomStatu || strRoomStatu.equals("")) {
            return desRoomStatus = strCurRoomStatus;
        } else {
            // 数据库中得到的room_state,从/分割成数组
            String[] rs = strRoomStatu.split("\\/");
            boolean replaceFlag = false;
            for (int i = 0; i < rs.length; i++) {

                if (strCurRoomStatus.charAt(0) > rs[i].charAt(0)) {
                    desRoomStatus += rs[i] + "/";
                } else if (strCurRoomStatus.charAt(0) == rs[i].charAt(0) && false == replaceFlag) {
                    desRoomStatus += strCurRoomStatus + "/";
                    replaceFlag = true;
                }
                /*
                 * else if(strCurRoomStatus.charAt(0) < rs[i].charAt(0) && replaceFlag == false){
                 * desRoomStatus = desRoomStatus + strCurRoomStatus + "/"; desRoomStatus =
                 * desRoomStatus + rs[i] + "/"; replaceFlag = true; }
                 */
                else {
                    desRoomStatus += rs[i] + "/";
                }
            }

            if (false == replaceFlag) {
                desRoomStatus += strCurRoomStatus;
            } else {
                desRoomStatus = desRoomStatus.substring(0, desRoomStatus.length() - 1);
            }
        }

        return desRoomStatus;
    }
}
