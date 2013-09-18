package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.domain.valueobject.GisInfo;
import com.mangocity.util.config.ConfigUtil;
import com.mangocity.util.log.MyLog;

/**
 * 地图查询酒店
 * 
 * @author chenkeming
 * 
 */
public class URLClient implements Serializable {
	private static final MyLog log = MyLog.getLogger(URLClient.class);
    private GisService gisService;

    private HotelManage hotelManage;

    // static String HOP_NOT_USE_MAP = ConfigUtil.getResourceByKey("HOP_NOT_USE_MAP");

    public static String HOP_VVIP = ConfigUtil.getResourceByKey("HOP_VVIP");

    public static String HOP_TMC_ORDER = ConfigUtil.getResourceByKey("HOP_TMC_ORDER");

    // public static String HOP_TMC_ORDER =
    // "http://10.10.1.140/TMC/orderFlow/ProtocolOrderAction!view.action";
    // private String getDocumentAt(InputStream is, HttpURLConnection _conn) {
    // StringBuffer document = new StringBuffer();
    // BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    // char[] _cbuf = new char[1024];
    // int nRead = 0;
    // try {
    // while (0 < (nRead=reader.read(_cbuf,0,_cbuf.length))) {
    // document.append(_cbuf, 0, nRead);
    // }
    // } catch (IOException e) {
    // System.out.println(e.getMessage());
    // return null;
    // } finally {
    // try {
    // reader.close();
    // _conn.disconnect();
    // } catch (Exception e) {
    // }
    // }
    //
    // return document.toString();
    // }

    /**
     * 根据地图接口查询酒店
     * 
     * @param cityCode
     *            城市代码
     * @param showPlace
     *            地理位置/名胜
     * @param scope
     *            范围(公里)
     * 
     * @return 返回芒果网签约的酒店id,用字符串组装,以“,”隔开。例：”100001,100002,1222222”
     */
    public String getMapHotel(String cityCode, String showPlace, double scope) {

        String res = "";
        log.info("------emap-------" + cityCode + " ::: " + showPlace + " :::"
            + "------emap-------");
        String cityName = InitServlet.cityObj.get(cityCode);

        List<GisInfo> result = null;
        try {
            /**
             * 搜索showPlace(名胜)scope(范围内的酒店)
             */
            result = gisService.searchConsultAddress(cityName, showPlace, scope, 0L, 1, 0);
        } catch (ServiceException e) {
        	log.error(e.getMessage(),e);
        }

        if (null != result) {
            /**
             * 遍历gisList
             */
            String gisids = "";
            log.info(cityCode + ":::" + showPlace + ":: gisid size()" + result.size());
            for (Iterator<GisInfo> i = result.iterator(); i.hasNext();) {
                GisInfo gisInfo = i.next();
                /**
                 * 将gisid以"100001,100002,1222222"形式分割开
                 */
                gisids += gisInfo.getGisId() + ",";
            }
            int len = gisids.length();
            gisids = gisids.substring(0, len - 1);
            List hotelList = hotelManage.queryHtlHotelList(gisids);
            for (Iterator<HtlHotel> h = hotelList.iterator(); h.hasNext();) {
                HtlHotel hotel = h.next();
                res += hotel.getID() + ",";
            }
            len = res.length();
            res = res.substring(0, len - 1);
        }
        return res;
    }

    public GisService getGisService() {
        return gisService;
    }

    public void setGisService(GisService gisService) {
        this.gisService = gisService;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

}
