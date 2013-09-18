package com.mangocity.hotel.vrm.action;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.googlecode.jsonplugin.annotations.JSON;
import com.opensymphony.xwork2.ActionSupport;
/***
 * 
 * @author panjianping
 *
 */
/**
 * 获取需要进行二次营销的页面信息，把该信息通过http请求发送给Vizury，
 * vizury会通过我们传送的信息分析用户行为，进行二次营销
 */
public class VizuryVRMAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String urlStr;//链接字符串，用于发送请求
	private URL url;
	private HttpURLConnection url_con;
	private String param;//h100表示酒店首页，h200表示酒店查寻页面或港澳酒店首页，h300表示酒店详情页，h400订单填写也，h500订单完成页
	private String city;//cityCode
	private String indate;//入住日期
	private String outdate;//离店日期
    private String rooms;//客人所订酒店的房间数
    private String hname;//酒店名称
    private String hotelid;
    private String image;//该酒店所对应的图片路径
    private String lp; //酒店详情页的路径
    private String oid;//订单id
    private String curr;//币种
    private String op;//订单价格
    private String newp;//酒店售价
    private String rt;//酒店星级
    
	public String execute() {
		try {
			urlStr = buildURLStr();
			url = new URL(urlStr);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.connect();
			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in));
			StringBuilder tempStr = new StringBuilder();
			while (rd.read() != -1) {
				tempStr.append(rd.readLine());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			url_con.disconnect();
		}

		return SUCCESS;
	}

	public String buildURLStr() {
		StringBuffer urlBuffer = new StringBuffer(
				"http://serv1.vizury.com/analyze/analyze.php?account_id=VIZVRM128");
		if (param != null && !"".equals(param = param.trim())
				&& !"undefined".equals(param)) {
			urlBuffer.append("&param=" + param);
		}
		if (hotelid!= null && !"".equals(hotelid = hotelid.trim())
				&& !"undefined".equals(hotelid)) {
			urlBuffer.append("&hotelid=" + hotelid);
		}
		if (city != null && !"".equals(city = city.trim())
				&& !"undefined".equals(city)) {
			urlBuffer.append("&city=" + city);
			if (indate != null && !"".equals(indate = indate.trim())
					&& !"undefined".equals(indate)) {
				urlBuffer.append("&indate=" + indate);
			}
			if (outdate != null && !"".equals(outdate = outdate.trim())
					&& !"undefined".equals(outdate)) {
				urlBuffer.append("&outdate=" + outdate);
			}
		}
		if(hname != null && !"".equals(hname = hname.trim())&& !"undefined".equals(hname)){
			urlBuffer.append("&hname="+hname);
		}
		if(lp != null && !"".equals(lp = lp.trim())&& !"undefined".equals(lp)){
			urlBuffer.append("&lp="+lp);
		}
		if(image != null && !"".equals(image = image.trim())&& !"undefined".equals(image)){
			urlBuffer.append("&image="+image);
		}
		if(rooms != null && !"".equals(rooms = rooms.trim())&& !"undefined".equals(rooms)){
			urlBuffer.append("&rooms="+rooms);
		}
		if(oid != null && !"".equals(oid = oid.trim())&& !"undefined".equals(oid)){
			urlBuffer.append("&oid="+oid);
		}
		if(curr != null && !"".equals(curr = curr.trim())&& !"undefined".equals(curr)){
			urlBuffer.append("&curr="+curr);
		}
		if(op != null && !"".equals(op = op.trim())&& !"undefined".equals(op)){
			urlBuffer.append("&op="+op);
		}
		if(newp != null && !"".equals(newp = newp.trim())&& !"undefined".equals(newp)){
			urlBuffer.append("&newp="+newp);
		}
		if(rt != null && !"".equals(rt = rt.trim())&& !"undefined".equals(rt)){
			urlBuffer.append("&rt="+rt);
		}
		urlBuffer.append("&section=1&level=1");
		return urlBuffer.toString();

	}

	@JSON(serialize = false)
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@JSON(serialize = false)
	public HttpURLConnection getUrl_con() {
		return url_con;
	}

	public void setUrl_con(HttpURLConnection url_con) {
		this.url_con = url_con;
	}

	public String getUrlStr() {
		return urlStr;
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIndate() {
		return indate;
	}

	public void setIndate(String indate) {
		this.indate = indate;
	}

	public String getOutdate() {
		return outdate;
	}

	public void setOutdate(String outdate) {
		this.outdate = outdate;
	}

	public String getRooms() {
		return rooms;
	}

	public void setRooms(String rooms) {
		this.rooms = rooms;
	}

	public String getHname() {
		return hname;
	}

	public void setHname(String hname) {
		this.hname = hname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLp() {
		return lp;
	}

	public void setLp(String lp) {
		this.lp = lp;
	}

	public String getHotelid() {
		return hotelid;
	}

	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getNewp() {
		return newp;
	}

	public void setNewp(String newp) {
		this.newp = newp;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}
	
	

}
