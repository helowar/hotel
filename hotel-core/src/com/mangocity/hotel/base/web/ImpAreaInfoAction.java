package com.mangocity.hotel.base.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class ImpAreaInfoAction extends PersistenceAction {
    private Map map = new HashMap();

    private Map cityMap = new HashMap();

    private ContractManage contractManage;

    private String SAVE = "save";

    protected Class getEntityClass() {
        return HtlArea.class;
    }

    public String save() {
        map = InitServlet.mapAreaCityObj;
        // map(key,value) 构成:key---areaCode,areaName; value----hashmap(key1,value1)
        // 其中key1--cityName;value1---cityCode
        Map map = new HashMap();

        map = com.mangocity.hotel.base.util.RateBaseReader.rateMap;

        Object obj;
        Object cityObj;
        Set setObj = map.keySet();

        Iterator it = setObj.iterator();

        String area = "";

        while (it.hasNext()) {
            String areaName = "";
            String areaCode = "";

            obj = it.next();
            if (null != obj)
                area = obj.toString();
            if (null != obj && 0 < area.length() && 0 < area.indexOf(",")) {
                areaCode = area.substring(0, area.indexOf(","));
                areaName = area.substring(area.indexOf(",") + 1);
            }
            cityMap = (HashMap) map.get(obj);
            Set citySet = cityMap.keySet();
            Iterator cityIt = citySet.iterator();
            while (cityIt.hasNext()) {
                String cityName = "";
                String cityCode = "";
                cityObj = cityIt.next();
                if (null != cityObj) {
                    cityCode = cityObj.toString();
                    cityName = (String) cityMap.get(cityObj);
                }
                // 如果区域代码和城市代码不存在，就插入
                if (!areaCode.equals("") && !cityCode.equals("")) {
                    int i = contractManage.checkAreaExist(areaCode, cityCode);
                    if (0 == i) {
                        HtlArea htlArea = new HtlArea();
                        htlArea.setAreaCode(areaCode);
                        htlArea.setAreaName(areaName);
                        htlArea.setCityCode(cityCode);
                        htlArea.setCityName(cityName);
                        super.getEntityManager().saveOrUpdate(htlArea);

                    }
                }
            }
        }
        return SAVE;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

}
