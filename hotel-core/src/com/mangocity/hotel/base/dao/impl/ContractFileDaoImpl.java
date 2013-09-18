package com.mangocity.hotel.base.dao.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.ContractFileDao;
import com.mangocity.hotel.base.persistence.ContractFile;
import com.mangocity.util.dao.DAOIbatisImpl;

/**
 */
public class ContractFileDaoImpl extends DAOIbatisImpl implements ContractFileDao {

    public Object addContractFile(String queryID, ContractFile contractFile) {
        // TODO Auto-generated method stub
        return super.save(queryID, contractFile);
    }

    public int deleteContractFile(String queryID, ContractFile contractFile) {
        // TODO Auto-generated method stub
        return super.delete(queryID, contractFile);
    }

    public int modifyContractFile(String queryID, ContractFile contractFile) {
        // TODO Auto-generated method stub
        return super.update(queryID, contractFile);
    }

    public ContractFile queryContractFileById(String queryID, ContractFile contractFile) {
        // TODO Auto-generated method stub
        return (ContractFile) super.queryForObject(queryID, contractFile);
    }

    public List queryContractFileList(String queryID, ContractFile contractFile) {
        // TODO Auto-generated method stub
        return super.queryForList(queryID, contractFile);
    }

}
