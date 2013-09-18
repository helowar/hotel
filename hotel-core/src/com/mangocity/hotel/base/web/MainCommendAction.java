package com.mangocity.hotel.base.web;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.impl.ContractDao;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.HtlHotelScore;
import com.mangocity.hotel.base.persistence.HtlMainCommend;
import com.mangocity.hotel.base.util.SendLuceneMQ;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.Entity;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class MainCommendAction extends PersistenceAction {

    private String hotelName;

    private long hotelID;

    private int scoreNum;

    private Map map;

    private HotelManage hotelManage;

    private ContractDao contractDao;
    
    // 发送MQ功能
    private SendLuceneMQ sendLuceneMQ;

    /**
     * 查询待推荐的酒店调转名称
     */
    private static final String LISTHOTEL = "listHotel";

    protected Class getEntityClass() {
        return HtlMainCommend.class;
    }

    public String create() {
        super.create();
        hotelName = (hotelManage.findHotel(hotelID)).getChnName();
        return CREATE;
    }

    public String view() {
        super.view();
        hotelID = ((HtlMainCommend) super.getEntity()).getHotelID();
        hotelName = (hotelManage.findHotel(hotelID)).getChnName();

        HtlMainCommend mainCommend = (HtlMainCommend) super.getEntity();
        List score = mainCommend.getLstCommScore();
        String[] scores = new String[score.size()];
        String[] ids = new String[score.size()];
        for (int i = 0; i < score.size(); i++) {
            scores[i] = ((HtlHotelScore) score.get(i)).getScore();
            ids[i] = ((HtlHotelScore) score.get(i)).getID().toString();
        }

        request.setAttribute("scores", scores);
        request.setAttribute("ids", ids);
        return VIEW;
    }
    
    @Override
    public String delete() {
        if (null == super.getEntityID())
            return super.forwardError("entityID不能为空!,请传入entityID参数!");

        super.getEntityManager().remove(getEntityClass(), super.getEntityID());
        
        // 增加发送mq信息 add by chenkeming
        sendLuceneMQ.send("hotelInfo#" + hotelID);
        
        return DELETED;
    }
    

    

    public String save() {
        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlHotelScore.class,
            scoreNum);

        HtlMainCommend commend = (HtlMainCommend) super.populateEntity();
        if (null != super.getOnlineRoleUser()) {
            commend = (HtlMainCommend) CEntityEvent.setCEntity(commend, super.getOnlineRoleUser()
                .getName(), super.getOnlineRoleUser().getLoginName());
        }
        if (null == super.getEntityID() || 0 == super.getEntityID()) {
            for (int m = 0; m < ls.size(); m++) {
                HtlHotelScore score = (HtlHotelScore) ls.get(m);
                score.setMainCommend(commend);
            }
            commend.setLstCommScore(ls);
            super.setEntity(commend);
            super.getEntityManager().save(super.getEntity());

        } else {
            super.setEntity(commend);
            super.getEntityManager().update(super.getEntity());
            for (int m = 0; m < ls.size(); m++) {
                HtlHotelScore score = (HtlHotelScore) ls.get(m);
                // 根据ID导出分数记录
                score.getScore();
                if (null != score.getID()) {
                    HtlHotelScore scoreOrgin = hotelManage.findScore(score.getID());
                    scoreOrgin.setScore(score.getScore());
                    // 循环保存
                    hotelManage.saveOrUpdateScore(scoreOrgin);
                }
            }
        }

        // commend.setLstCommScore(ls);

        super.setEntityID(((Entity) super.getEntity()).getID());

        super.setEntityForm(populateFormBean(super.getEntity()));
        
        // 增加发送mq信息 add by chenkeming
        sendLuceneMQ.send("hotelInfo#" + hotelID);

        // 把list里面的对象赋值给map,
        map = super.getParams();
        return this.view();
        // return SAVE_SUCCESS;

    }

    /**
     * 查询待推荐的酒店,
     * 
     * @return
     */
    public String listHotel() {

        return LISTHOTEL;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(int scoreNum) {
        this.scoreNum = scoreNum;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public ContractDao getContractDao() {
        return contractDao;
    }

    public void setContractDao(ContractDao contractDao) {
        this.contractDao = contractDao;
    }

	public void setSendLuceneMQ(SendLuceneMQ sendLuceneMQ) {
		this.sendLuceneMQ = sendLuceneMQ;
	}


}
