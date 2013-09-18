package com.mangocity.hotel.base.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlCoverPicture;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPicture;
import com.mangocity.hotel.base.persistence.HtlPictureUrl;
import com.mangocity.hotel.base.util.DownloadPicFromArtifactory;
import com.mangocity.hotel.base.util.ResourceUtil;
import com.mangocity.hotel.base.util.SendLuceneMQ;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.WebUtil;

/**
 * 
 * @author Administrator
 * 
 *         图片类型定义 酒店外观照片 0 酒店大堂照片 1 酒店房间照片2
 * 
 */

public class HotelPotoUploadAction extends PersistenceAction {
	private static final MyLog log = MyLog.getLogger(HotelPotoUploadAction.class);
    private static final long serialVersionUID = -5641351584515612351L;
    private ResourceUtil resourceUtil;
    private DownloadPicFromArtifactory downloadPicFromArtifactory;
    // private static final int BUFFER_SIZE = 16 * 1024 ;
    /**
     * 传进来的酒店ID
     */
    private long hotelID;

    /**
     * 在本地的路径
     */
    private List<File> uploads = new ArrayList<File>();

    private List<String> uploadFileNames = new ArrayList<String>();

    private List<String> uploadContentTypes = new ArrayList<String>();

    private ServletContext context;

    /**
     * 在服务器上的文件名
     */
    private String imageFileName;

    private HotelManage hotelManage;

    /**
     * 用来装图片的路径
     */
    private Map mapObj = new HashMap();
    
    // 发送MQ功能 add by chenkeming
    private SendLuceneMQ sendLuceneMQ;
    
    private List noPictureHotels = new ArrayList();
    private String hotelIdStr;
    private String localPath; 
    private  int state = 200;

 
    public String view() {

        return INPUT;
    }

    private static void copy(File src, File dst) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src), Integer.parseInt(String
                    .valueOf(src.length())));
                out = new BufferedOutputStream(new FileOutputStream(dst), Integer.parseInt(String
                    .valueOf(src.length())));
                byte[] buffer = new byte[Integer.parseInt(String.valueOf(src.length()))];
                while (0 < in.read(buffer)) {
                    out.write(buffer);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    private static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    /**
     * 上传酒店照片
     * 
     * @return
     */
    public String upload() {
        List htlPictureList = hotelManage.findHtlPicture(hotelID);
        HashMap map = new HashMap();
        if (null != htlPictureList) {
            for (int j = 0; j < htlPictureList.size(); j++) {
                HtlPicture htlPicture = (HtlPicture) htlPictureList.get(j);
                map.put(htlPicture.getPictureType(), htlPicture);
            }
        }
        for (int i = 0; i < uploadFileNames.size(); i++) {
            if (!("".equals(uploadFileNames.get(i)))) {
                imageFileName = hotelID + "_" + new Date().getTime()
                    + getExtention(uploadFileNames.get(i));
                File imageFile = new File(ServletActionContext.getServletContext().getRealPath(
                    "/upload")
                    + "/" + "Photo" + "/" + imageFileName);
                copy(uploads.get(i), imageFile);

                if (null != map.get("" + i)) {
                    HtlPicture htlPicture = (HtlPicture) map.get("" + i);
                    htlPicture.setPictureName(imageFileName);
                    hotelManage.updateHtlPicture(htlPicture);
                } else {
                    HtlPicture htlPicture = new HtlPicture();
                    htlPicture.setHotelId(hotelID);
                    htlPicture.setPictureName(imageFileName);
                    String s = String.valueOf(i);
                    htlPicture.setPictureType(s);
                    hotelManage.createHtlPicture(htlPicture);
                }

            }
        }
        
        // 增加发送mq信息 add by chenkeming
        sendLuceneMQ.send("hotelInfo#" + hotelID);
        
        return SUCCESS;
    }

    /**
     * 跳到查看酒店照片的页面
     * 
     * @return
     */
    public String edit() {
        List htlPictureList = hotelManage.findHtlPicture(hotelID);
        HtlPicture htlPicture = new HtlPicture();
        if (null != htlPictureList) {
            for (int i = 0; i < htlPictureList.size(); i++) {
                if (null != htlPictureList.get(i)) {
                    htlPicture = (HtlPicture) htlPictureList.get(i);
                    String pictureName = "";
                    if(htlPicture.getPictureName() != null && !htlPicture.getPictureName().trim().equals("")){
                    	pictureName = "/upload/Photo/" + htlPicture.getPictureName();
                    }
                    mapObj.put(htlPicture.getPictureType(), pictureName);
                }
            }
        }
        return "toEdit";
    }
    
    //查询无图酒店
    public String queryNoPicHotels(){
    	noPictureHotels = hotelManage.queryNoPicHotels();
    	for(int i = 0;i<noPictureHotels.size();i++){
    		HtlHotel htlHotel = (HtlHotel) noPictureHotels.get(i);
    		htlHotel.setCity(InitServlet.cityObj.get(htlHotel.getCity()));
    	}
    	return "autoUpload";
    }

    //复制图片至旧图片库
    public String autoUploadPictures(){
    	String htl_pic_online_url = resourceUtil.queryArtifactoryUrl();//artifactory路径
    	String[] artiInfo = resourceUtil.getArtifactoryInfo();
    	localPath =request.getParameter("localPath")+"/localImages";//临时文件存放文件夹
    	File localDirFile = new File(localPath);
		if(!localDirFile.exists()){
			localDirFile.mkdirs();
		 }
    	String hotelIdStr=request.getParameter("hotelIdStr");
    	String[] hotelIds= hotelIdStr.split(",");
    	StringBuffer resultStr = new StringBuffer(); //返回结果提示
    	for(int i = 0;i<hotelIds.length;i++){
    		log.debug("autoUploadPictures--forloop--hotelId:"+hotelIds[i]);
    		HtlHotel htlHotel = new HtlHotel();
    		htlHotel.setID(Long.valueOf(hotelIds[i]));
    		List<HtlCoverPicture> htlCoverPicList = hotelManage.queryCoverPictures(htlHotel);//查询一家酒店的相册封面
	        resultStr.append((i +1) + ".上传酒店id：" + htlHotel.getID() + ":<br>"); //返回到页面的提示
    		for(int j=0;j<htlCoverPicList.size();j++){
    			HtlCoverPicture htlCoverPicture = htlCoverPicList.get(j);
    			List<HtlPictureUrl> htlPicUrls = hotelManage.queryPictureUrls(htlCoverPicture);
    			String artUrl = null;
    			if(null != htlPicUrls && htlPicUrls.size() >4){//路径不为空
    			if(htlCoverPicture.getClassify() == 3){
    				 artUrl = htl_pic_online_url+ htlPicUrls.get(3).getPictureURL();//120*80Artifactory路径
    			}else{
    				 artUrl = htl_pic_online_url+ htlPicUrls.get(1).getPictureURL();//500*333Artifactory路径
    			}
    			String localFileName = hotelIds[i]+"_"+(int)(Math.random()*10000)+".jpg";//临时文件存放路径
    				try {
						state = DownloadPicFromArtifactory.doBusinessByHttpClient(localPath+"/"+localFileName,artUrl,artiInfo);//下载图片
						if(200 == state){
								File localFile = new File(localPath+"/"+localFileName);
				    			File serverDirFile = new File(ServletActionContext.getServletContext().getRealPath( "/upload") + "/" + "Photo" );
				    			if(!serverDirFile.exists()){
				    				serverDirFile.mkdirs();
				    			 }
				    			File serverFile = new File(ServletActionContext.getServletContext().getRealPath( "/upload") + "/" + "Photo" + "/" + localFileName);
				    			
								copy(localFile,serverFile);//上传本地图片至apache
								//上传成功则向数据库中写入信息		
							    HtlPicture htlPicture = new HtlPicture();
			                    htlPicture.setHotelId(Long.valueOf(hotelIds[i]));
			                    htlPicture.setPictureName(localFileName);
			                    String type=null;
			                    switch(htlCoverPicture.getClassify()){
			                    case 1:type="0";break;//外观
			                    case 2:type="1";break;//大堂
			                    case 3:type="3";break;//logo
			                    case 0:type="2";break;//房间图片
			                    }
			                    htlPicture.setPictureType(type);
			                    hotelManage.createHtlPicture(htlPicture);     
						        //删除临时文件
						        if(localFile.exists()){
						        	localFile.delete();
						        }
						        resultStr.append("图片上传成功：" + htlCoverPicture.getPictureId() + ":" + htlCoverPicture.getPictureName() + "/<br>"); //返回到页面的提示
							}else{
						        resultStr.append("图片上传失败：" + htlCoverPicture.getPictureId() + ":" + htlCoverPicture.getPictureName() + "/<br>");//返回到页面的提示
						}
						}catch (IOException e) {
							log.error(e.getMessage(),e);
					}	
    			}else{//路径为空
    				 resultStr.append("图片上传失败：" + htlCoverPicture.getPictureId() + ":" + htlCoverPicture.getPictureName() + "/<br>");//返回到页面的提示
    			}			
						
    	}  
    		  // 增加发送mq信息 
            sendLuceneMQ.send("hotelInfo#" + hotelIds[i]);
    		noPictureHotels.remove(htlHotel);
    		
    	}
        request.setAttribute("resultStr", resultStr.toString());
    	return "autoUpload";
    }
    
    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public Map getMapObj() {
        return mapObj;
    }

    public void setMapObj(Map mapObj) {
        this.mapObj = mapObj;
    }

	public void setSendLuceneMQ(SendLuceneMQ sendLuceneMQ) {
		this.sendLuceneMQ = sendLuceneMQ;
	}
	  public List getNoPictureHotels() {
			return noPictureHotels;
		}

		public void setNoPictureHotels(List noPictureHotels) {
			this.noPictureHotels = noPictureHotels;
		}

		public String getHotelIdStr() {
			return hotelIdStr;
		}

		public void setHotelIdStr(String hotelIdStr) {
			this.hotelIdStr = hotelIdStr;
		}

		public String getLocalPath() {
			return localPath;
		}

		public void setLocalPath(String localPath) {
			this.localPath = localPath;
		}

		public List<File> getUpload() {
	        return this.uploads;
	    }

	    public void setUpload(List<File> uploads) {
	        this.uploads = uploads;
	    }

	    public List<String> getUploadFileName() {
	        return this.uploadFileNames;
	    }

	    public void setUploadFileName(List<String> uploadFileNames) {
	        this.uploadFileNames = uploadFileNames;
	    }

	    public List<String> getUploadContentType() {
	        return this.uploadContentTypes;
	    }

	    public void setUploadContentType(List<String> contentTypes) {
	        this.uploadContentTypes = contentTypes;
	    }

	    public void setServletContext(ServletContext context) {
	        this.context = context;
	    }

	    public ServletContext getServletContext() {
	        return this.context;
	    }

	    public long getHotelID() {
	        return hotelID;
	    }

	    public void setHotelID(long hotelID) {
	        this.hotelID = hotelID;
	    }

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public ResourceUtil getResourceUtil() {
			return resourceUtil;
		}

		public void setResourceUtil(ResourceUtil resourceUtil) {
			this.resourceUtil = resourceUtil;
		}
	    
}
