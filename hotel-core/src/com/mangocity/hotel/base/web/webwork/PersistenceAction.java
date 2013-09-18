package com.mangocity.hotel.base.web.webwork;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mangocity.hotel.base.persistence.CEntity;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.util.ClassUtil;
import com.mangocity.util.Entity;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.exception.ClassInstantiateException;

/**
 * 持久化Action 提供增、删、改、假删除的操作服务 1.创建一个空白页面：bussiness!create.action 2.编辑并保存：bussiness!edit.action
 * 3.新增并保存:bussiness!save.action 4.查看:bussiness!view.action 5.删除：bussiness!delete.action
 * 6.假删除：bussiness!fakeDelete.action
 * 
 * @author zhengxin
 * 
 */
public abstract class PersistenceAction extends GenericAction {

    /**
     * 实体ID
     */
    private Long entityID;

    private final static String ID = "ID";

    private Long[] entityIDall;

    /**
     * 假删除调转名称
     */
    protected static final String FAKE_DELETED = "fakeDeleted";

    protected static final String DELETED = "deleted";

    protected static final String VIEW = "view";
    
    protected static final String VIEWFORROOMSTATE = "viewForroomState";

    protected static final String SAVE_SUCCESS = "save";

    protected static final String CREATE = "create";

    protected static final String LIST_ALL = "listAll";

    protected static final String EDIT = "edit";
    
    protected String modifyForView;

    /**
     * 实体类
     */
    protected Class entityClass;

    /**
     * 实体操作类
     */
    private DAO entityManager;

    /**
     * 持久化实体
     */
    private Object entity;

    /**
     * 表单
     */
    private Object entityForm;

    private List entities;

    /**
     * 操作类型，view 查看，edit修改，add 增加
     */
    private String operateType;

    /**
     * @return
     */
    public String create() {
        entity = populateEntity();
        return CREATE;
    }

    /**
     * 编辑并保存
     * 
     * @return
     */
    public String edit() {
        /**
         * 填充参数数据到实体
         */
        if (null == entity) {
            entity = populateEntity();
        }

        entityManager.saveOrUpdate(entity);

        entityForm = populateFormBean(entity);

        return SAVE_SUCCESS;
    }

    /**
     * 新增后的保存操作
     * 
     * @return
     * @throws SQLException 
     */
    public String save() throws SQLException {

        entity = populateEntity();

        if (entity instanceof CEntity && null != super.getOnlineRoleUser()) {
            entity = CEntityEvent.setCEntity(entity, super.getOnlineRoleUser().getName(), super
                .getOnlineRoleUser().getLoginName());
        }

        if (null == entityID || 0 == entityID)

            entityManager.save(entity);
        else
            entityManager.saveOrUpdate(entity);
        entityID = ((Entity) entity).getID();

        entityForm = populateFormBean(entity);

        return SAVE_SUCCESS;
    }

    protected Class getEntityClass() {
        return entityClass;
    }

    /**
     * 假删除
     * 
     * @return
     */
    public String fakeDelete() {
        entityManager.removeByFake(entityClass, entityID);
        return FAKE_DELETED;
    }

    /**
     * 真删除
     * 
     * @return
     */
    public String delete() {
        if (null == entityID)
            return super.forwardError("entityID不能为空!,请传入entityID参数!");

        entityManager.remove(getEntityClass(), entityID);
        return DELETED;
    }

    /**
     * 
     *批量删除
     * 
     */
    public String volumeDel() {
        try {
            for (int i = 0; i < entityIDall.length; i++) {
                entityManager.remove(getEntityClass(), entityIDall[i]);
            }
        } catch (Exception e) {
            log.error(e);
        }
        return DELETED;
    }

    /**
     * 查看
     * 
     * @return
     */
    public String view() {
        if (null == entityID) {
            String error = "entityID不能为空!，请传入entityID参数!";

            return super.forwardError(error);
        }

        entity = populateEntity();

        if (null == entity) {
            String error = "找不到实体对象,id:" + entityID + "; 类：" + getEntityClass();
            return super.forwardError(error);
        }

        entityForm = populateFormBean(entity);

        return VIEW;
    }
    
    /**
     * 房态用--查看
     * 
     * @return
     */
    public String viewForRoomState() {
        if (null == entityID) {
            String error = "entityID不能为空!，请传入entityID参数!";

            return super.forwardError(error);
        }

        entity = populateEntity();

        if (null == entity) {
            String error = "找不到实体对象,id:" + entityID + "; 类：" + getEntityClass();
            return super.forwardError(error);
        }

        entityForm = populateFormBean(entity);
        modifyForView ="1";
        
        return VIEWFORROOMSTATE;
    }

    public String listAll() {
        entities = entityManager.listAll(getEntityClass());

        return LIST_ALL;
    }

    /**
     * 将表单数据填充到实体对象当中
     * 
     * @return
     */
    protected Object populateEntity() {

        Object obj = null;

        if (null != entityID && 0 != entityID) {
            obj = entityManager.find(this.getEntityClass(), entityID);
        } else
            obj = ClassUtil.newInstance(getEntityClass());

        Map parameters = super.getParams();

        parameters.put("ID", entityID);

        if (null != obj)
            try {
                prepare();
                BeanUtils.copyProperties(obj, parameters);

            } catch (Exception e) {

                throw new ClassInstantiateException(e);

            }

        return obj;
    }

    /**
     * 将实体对象填充到表单当中
     * 
     * @param obj
     * @return
     */
    protected Object populateFormBean(Object obj) {

        return obj;
    }

    /**
     * 将实体对象，转化成Map的表单形式，供页面调用
     * 
     * @param obj
     * @return
     */
    protected Map simplePopulateFormBean(Object obj) {
        try {
            return BeanUtils.describe(obj);
        } catch (Exception e) {

            throw new ClassInstantiateException(e);

        }
    }

    /**
     * 保存或新增entity对象
     * 
     * @param
     * @return
     */
    protected Serializable saveOrUpdate(Entity obj) {
        if (null == obj.getID() || 0 == obj.getID()) {
            return entityManager.save(obj);
        } else {
            entityManager.saveOrUpdate(obj);
            return obj.getID();
        }
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Object getEntityForm() {
        return entityForm;
    }

    public void setEntityForm(Object entityForm) {
        this.entityForm = entityForm;
    }

    public DAO getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(DAO entityManager) {
        this.entityManager = entityManager;
    }

    public Long getEntityID() {
        return entityID;
    }

    public void setEntityID(Long entityID) {
        this.entityID = entityID;
    }

    /**
     * 用于子类的提供重写的方法，主要用于在接收参数后，对参数进行的一个处理
     * 
     */
    protected void prepare() {

    }

    public List getEntities() {
        return entities;
    }

    public void setEntities(List entities) {
        this.entities = entities;
    }

    public Long[] getEntityIDall() {
        return entityIDall;
    }

    public void setEntityIDall(Long[] entityIDall) {
        this.entityIDall = entityIDall;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

	public String getModifyForView() {
		return modifyForView;
	}

	public void setModifyForView(String modifyForView) {
		this.modifyForView = modifyForView;
	}

}
