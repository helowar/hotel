package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.ContractFile;

/**
 */
public interface ContractFileDao {

    /**
     * 查询合同信息列表
     * 
     * @param queryID
     * @param contractFile
     * @return
     */
    public List queryContractFileList(String queryID, ContractFile contractFile);

    /**
     * 查询合同片信息
     * 
     * @param queryID
     * @param contractFile
     * @return
     */
    public ContractFile queryContractFileById(String queryID, ContractFile contractFile);

    /**
     * 增加合同片信息
     * 
     * @param queryID
     * @param contractFile
     * @return
     */
    public Object addContractFile(String queryID, ContractFile contractFile);

    /**
     * 修改合同片信息
     * 
     * @param queryID
     * @param contractFile
     * @return
     */
    public int modifyContractFile(String queryID, ContractFile contractFile);

    /**
     * 删除合同信息
     * 
     * @param queryID
     * @param contractFile
     * @return
     */
    public int deleteContractFile(String queryID, ContractFile contractFile);

}
