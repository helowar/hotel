package com.mangocity.hotel.base.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.HtlPictureInfo;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 * @author yuexiaofeng
 * 
 */
public class PictureHotelAction extends PersistenceAction {

    private String forward = "success";

    private long hotelId;

    private Map<Long, String> priceType = new HashMap<Long, String>();

    private HotelManage hotelManage;

    private List hotelPictureLis = new ArrayList();

    private String fileName;

    private String serverFileName;

    private static final int BUFFER_SIZE = 16 * 1024;

    private File pictureupload;

    private File serverFile;

    private HtlPictureInfo htlPictureInfo;

    private String relaPath;

    private String smollPic;

    private String bigPic;

    private boolean hasSave = false;

    private String actionFlag;

    private boolean hasPath;

    private long id;

    private String relaPath2;

    private boolean hasPathBig = false;

    public String getActionFlag() {
        return actionFlag;
    }

    public void setActionFlag(String actionFlag) {
        this.actionFlag = actionFlag;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String forward() {// parasoft-suppress NAMING.GETA "STRUTS命名没有问题"
        // commonQueryHotelPicture(hotelPicInfo);
        return forward;
    }

    /*
     * 根据酒店ID查找
     */
    public String queryPictureInfo() {

        commonQueryHotelPicture(htlPictureInfo);
        return "query";
    }

    public String addPictureInfo() {
        // commonQueryHotelPicture(hotelPicInfo);
        return "add";
    }

    /*
     * 根据ID查找
     */
    public String updatePictureInfo() {

        queryHotelPictureById(htlPictureInfo);
        return "add";
    }

    /*
     * 根据ID删除
     */
    public String deletePictureInfo() {
        deleteHotelPictureById(htlPictureInfo);
        return "query";
    }

    public String uploadPictureInfo() {
        // commonQueryHotelPicture(hotelPicInfo);
        return "upload";
    }

    /*
     * 实现上传
     */
    private static void copy(File src, File dst) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
                out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
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

    /*
     * 取文件名
     */
    private static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    /*
     * 上传图片到指定目录
     */
    public String uploadPicture() {
        serverFileName = new Date().getTime() + getExtention(fileName);
        serverFile = new File(ServletActionContext.getServletContext().getRealPath("upload") + "/"
            + "Images" + "/" + serverFileName);
        if (pictureupload.exists()) {
            copy(pictureupload, serverFile);
            relaPath = request.getContextPath() + "/" + "upload" + "/" + "Images" + "/"
                + serverFileName;// 要取相对路径
            return "upload";
        } else {
            return "noPic";
        }
    }

    /*
     * 信息入库
     */
    public String uploadPictureIn() {
        htlPictureInfo.setHotelId(hotelId);
        htlPictureInfo.setSmollPic(smollPic);
        htlPictureInfo.setBigPic(bigPic);
        if (null != super.getOnlineRoleUser()) {
            htlPictureInfo = (HtlPictureInfo) CEntityEvent.setCEntity(htlPictureInfo, super
                .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
        }
        if (null != actionFlag && "doEdiPicInfo".equals(actionFlag)) {
            htlPictureInfo.setId(id);
            hotelManage.modifyHotelPic(htlPictureInfo);
        } else {
            hotelManage.addHotelPic(htlPictureInfo);
        }
        hasSave = true;
        return "add";
    }

    public void commonQueryHotelPicture(HtlPictureInfo hotelPicInfo) {
        hotelPictureLis = hotelManage.queryHotelPicList(hotelPicInfo);
    }

    public void queryHotelPictureById(HtlPictureInfo hotelPicInfo) {
        htlPictureInfo = hotelManage.queryHotelPicById(hotelPicInfo);
        serverFileName = htlPictureInfo.getSmollPic();
        String serverFileNameBig = htlPictureInfo.getBigPic();
        relaPath = "/" + "upload" + "/" + "Images" + "/" + serverFileName;
        relaPath2 = "/" + "upload" + "/" + "Images" + "/" + serverFileNameBig;
        if (null != serverFileName && !("".equals(serverFileName)))
            hasPath = true;
        if (null != serverFileNameBig && !("".equals(serverFileNameBig)))
            hasPathBig = true;
    }

    public void deleteHotelPictureById(HtlPictureInfo hotelPicInfo) {
        hotelManage.deleteHotelPic(hotelPicInfo);
        hotelPictureLis = hotelManage.queryHotelPicList(hotelPicInfo);
    }

    public Map<Long, String> getPriceType() {
        return priceType;
    }

    public void setPriceType(Map<Long, String> priceType) {
        this.priceType = priceType;
    }

    // public HotelManage getHotelManage() {
    // return hotelManage;
    // }
    //
    // public void setHotelManage(HotelManage hotelManage) {
    // this.hotelManage = hotelManage;
    // }

    public List getHotelPictureLis() {
        return hotelPictureLis;
    }

    public void setHotelPictureLis(List hotelPictureLis) {
        this.hotelPictureLis = hotelPictureLis;
    }

    public File getPictureupload() {
        return pictureupload;
    }

    public void setPictureupload(File pictureupload) {
        this.pictureupload = pictureupload;
    }

    public HtlPictureInfo getHtlPictureInfo() {
        return htlPictureInfo;
    }

    public void setHtlPictureInfo(HtlPictureInfo htlPictureInfo) {
        this.htlPictureInfo = htlPictureInfo;
    }

    public File getServerFile() {
        return serverFile;
    }

    public void setServerFile(File serverFile) {
        this.serverFile = serverFile;
    }

    public String getRelaPath() {
        return relaPath;
    }

    public void setRelaPath(String relaPath) {
        this.relaPath = relaPath;
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public String getSmollPic() {
        return smollPic;
    }

    public void setSmollPic(String smollPic) {
        this.smollPic = smollPic;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public boolean isHasSave() {
        return hasSave;
    }

    public void setHasSave(boolean hasSave) {
        this.hasSave = hasSave;
    }

    public boolean isHasPath() {
        return hasPath;
    }

    public void setHasPath(boolean hasPath) {
        this.hasPath = hasPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRelaPath2() {
        return relaPath2;
    }

    public void setRelaPath2(String relaPath2) {
        this.relaPath2 = relaPath2;
    }

    public boolean isHasPathBig() {
        return hasPathBig;
    }

    public void setHasPathBig(boolean hasPathBig) {
        this.hasPathBig = hasPathBig;
    }

}
