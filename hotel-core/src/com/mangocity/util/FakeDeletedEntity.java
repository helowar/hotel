package com.mangocity.util;

/**
 * 假删除实体
 * 
 * @author zhengxin
 * 
 */
public interface FakeDeletedEntity extends Entity {

    void setDeleted(boolean deleted);

    boolean isDeleted();

}
