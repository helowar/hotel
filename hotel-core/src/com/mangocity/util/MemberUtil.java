package com.mangocity.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 会员接口辅助类
 * 
 * @author chenkeming
 * 
 */
public class MemberUtil implements Serializable {

    /**
     * 会员的省份ID -> CDM的省份编码Map
     */
    public static Map stateMap = new HashMap();

    public static Map codeMap = new HashMap();

    public static Map chineseMap = new HashMap();
    static {
        stateMap.put(Long.valueOf(70043), "HEB"); // 河北
        stateMap.put(Long.valueOf(70044), "SDG"); // 山东
        stateMap.put(Long.valueOf(70045), "SHX"); // 山西
        stateMap.put(Long.valueOf(70046), "NMG"); // 内蒙古
        stateMap.put(Long.valueOf(70001), "HKG"); // 香港
        stateMap.put(Long.valueOf(70017), "GDN"); // 广东
        stateMap.put(Long.valueOf(70019), "HBI"); // 湖北
        stateMap.put(Long.valueOf(70020), "GXI"); // 广西
        stateMap.put(Long.valueOf(70021), "HNN"); // 湖南
        stateMap.put(Long.valueOf(70047), "HLJ"); // 黑龙江
        stateMap.put(Long.valueOf(70048), "JLN"); // 吉林
        stateMap.put(Long.valueOf(70049), "LNG"); // 辽宁
        stateMap.put(Long.valueOf(70050), "MAC"); // 澳门
        stateMap.put(Long.valueOf(70026), "SCN"); // 四川
        stateMap.put(Long.valueOf(70023), "HAN"); // 海南
        stateMap.put(Long.valueOf(70025), "GZU"); // 贵州
        stateMap.put(Long.valueOf(70027), "CKG"); // 重庆
        stateMap.put(Long.valueOf(70029), "XJG"); // 新疆
        stateMap.put(Long.valueOf(70024), "YNN"); // 云南
        stateMap.put(Long.valueOf(70028), "SXI"); // 陕西
        stateMap.put(Long.valueOf(70030), "XZG"); // 西藏
        stateMap.put(Long.valueOf(70031), "QHI"); // 青海
        stateMap.put(Long.valueOf(70032), "NXA"); // 宁夏
        stateMap.put(Long.valueOf(70033), "GSU"); // 甘肃
        stateMap.put(Long.valueOf(70034), "SHA"); // 上海
        stateMap.put(Long.valueOf(70035), "ZJG"); // 浙江
        stateMap.put(Long.valueOf(70036), "JSU"); // 江苏
        stateMap.put(Long.valueOf(70037), "AHI"); // 安徽
        stateMap.put(Long.valueOf(70038), "JXI"); // 江西
        stateMap.put(Long.valueOf(70039), "FJN"); // 福建
        stateMap.put(Long.valueOf(70040), "PEK"); // 北京
        stateMap.put(Long.valueOf(70041), "TSN"); // 天津
        stateMap.put(Long.valueOf(70042), "HEN"); // 河南

        stateMap.put(Long.valueOf(70051), "LTT"); // 联通专用
        stateMap.put(Long.valueOf(70052), "WTT"); // 网通专用
        stateMap.put(Long.valueOf(70053), "WTBJ");// 网通(北京)
        stateMap.put(Long.valueOf(70054), "NHZY");// 南航
        stateMap.put(Long.valueOf(70055), "GDLT");// 广东联通

        codeMap.put("HEB", Long.valueOf(70043)); // 河北
        codeMap.put("SDG", Long.valueOf(70044)); // 山东
        codeMap.put("SHX", Long.valueOf(70045)); // 山西
        codeMap.put("NMG", Long.valueOf(70046)); // 内蒙古
        codeMap.put("HKG", Long.valueOf(70001)); // 香港
        codeMap.put("GDN", Long.valueOf(70017)); // 广东
        codeMap.put("HBI", Long.valueOf(70019)); // 湖北
        codeMap.put("GXI", Long.valueOf(70020)); // 广西
        codeMap.put("HNN", Long.valueOf(70021)); // 湖南
        codeMap.put("HLJ", Long.valueOf(70047)); // 黑龙江
        codeMap.put("JLN", Long.valueOf(70048)); // 吉林
        codeMap.put("LNG", Long.valueOf(70049)); // 辽宁
        codeMap.put("MAC", Long.valueOf(70050)); // 澳门
        codeMap.put("SCN", Long.valueOf(70026)); // 四川
        codeMap.put("HAN", Long.valueOf(70023)); // 海南
        codeMap.put("GZU", Long.valueOf(70025)); // 贵州
        codeMap.put("CKG", Long.valueOf(70027)); // 重庆
        codeMap.put("XJG", Long.valueOf(70029)); // 新疆
        codeMap.put("YNN", Long.valueOf(70024)); // 云南
        codeMap.put("SXI", Long.valueOf(70028)); // 陕西
        codeMap.put("XZG", Long.valueOf(70030)); // 西藏
        codeMap.put("QHI", Long.valueOf(70031)); // 青海
        codeMap.put("NXA", Long.valueOf(70032)); // 宁夏
        codeMap.put("GSU", Long.valueOf(70033)); // 甘肃
        codeMap.put("SHA", Long.valueOf(70034)); // 上海
        codeMap.put("ZJG", Long.valueOf(70035)); // 浙江
        codeMap.put("JSU", Long.valueOf(70036)); // 江苏
        codeMap.put("AHI", Long.valueOf(70037)); // 安徽
        codeMap.put("JXI", Long.valueOf(70038)); // 江西
        codeMap.put("FJN", Long.valueOf(70039)); // 福建
        codeMap.put("PEK", Long.valueOf(70040)); // 北京
        codeMap.put("TSN", Long.valueOf(70041)); // 天津
        codeMap.put("HEN", Long.valueOf(70042)); // 河南

        codeMap.put("LTT", Long.valueOf(70051)); // 联通专用
        codeMap.put("WTT", Long.valueOf(70052)); // 网通专用
        codeMap.put("WTBJ", Long.valueOf(70053));// 网通(北京)
        codeMap.put("NHZY", Long.valueOf(70054));// 南航
        codeMap.put("GDLT", Long.valueOf(70055));// 广东联通

        chineseMap.put(Long.valueOf(70043), "河北"); // 河北
        chineseMap.put(Long.valueOf(70044), "山东"); // 山东
        chineseMap.put(Long.valueOf(70045), "山西"); // 山西
        chineseMap.put(Long.valueOf(70046), "内蒙古"); // 内蒙古
        chineseMap.put(Long.valueOf(70001), "香港"); // 香港
        chineseMap.put(Long.valueOf(70017), "广东"); // 广东
        chineseMap.put(Long.valueOf(70019), "湖北"); // 湖北
        chineseMap.put(Long.valueOf(70020), "广西"); // 广西
        chineseMap.put(Long.valueOf(70021), "湖南"); // 湖南
        chineseMap.put(Long.valueOf(70047), "黑龙江"); // 黑龙江
        chineseMap.put(Long.valueOf(70048), "吉林"); // 吉林
        chineseMap.put(Long.valueOf(70049), "辽宁"); // 辽宁
        chineseMap.put(Long.valueOf(70050), "澳门"); // 澳门
        chineseMap.put(Long.valueOf(70026), "四川"); // 四川
        chineseMap.put(Long.valueOf(70023), "海南"); // 海南
        chineseMap.put(Long.valueOf(70025), "贵州"); // 贵州
        chineseMap.put(Long.valueOf(70027), "重庆"); // 重庆
        chineseMap.put(Long.valueOf(70029), "新疆"); // 新疆
        chineseMap.put(Long.valueOf(70024), "云南"); // 云南
        chineseMap.put(Long.valueOf(70028), "陕西"); // 陕西
        chineseMap.put(Long.valueOf(70030), "西藏"); // 西藏
        chineseMap.put(Long.valueOf(70031), "青海"); // 青海
        chineseMap.put(Long.valueOf(70032), "宁夏"); // 宁夏
        chineseMap.put(Long.valueOf(70033), "甘肃"); // 甘肃
        chineseMap.put(Long.valueOf(70034), "上海"); // 上海
        chineseMap.put(Long.valueOf(70035), "浙江"); // 浙江
        chineseMap.put(Long.valueOf(70036), "江苏"); // 江苏
        chineseMap.put(Long.valueOf(70037), "安徽"); // 安徽
        chineseMap.put(Long.valueOf(70038), "江西"); // 江西
        chineseMap.put(Long.valueOf(70039), "福建"); // 福建
        chineseMap.put(Long.valueOf(70040), "北京"); // 北京
        chineseMap.put(Long.valueOf(70041), "天津"); // 天津
        chineseMap.put(Long.valueOf(70042), "河南"); // 河南

        chineseMap.put(Long.valueOf(70051), "联通专用"); // 联通专用
        chineseMap.put(Long.valueOf(70052), "网通专用"); // 网通专用
        chineseMap.put(Long.valueOf(70053), "网通(北京)");// 网通(北京)
        chineseMap.put(Long.valueOf(70054), "南航");// 南航
        chineseMap.put(Long.valueOf(70055), "广东联通");// 广东联通
    }
}
