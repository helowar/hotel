package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

/**
 */
public class ContractFile implements Serializable {

    /* properties */

    private long id = 0L;

    /*
     * 合同ID
     */
    private long contractId = 0L;

    /*
     * 文件名
     */
    private String fileName = "";

    /*
     * 上传路径
     */
    private String filePath = "";

    /* auto generate end */
    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
