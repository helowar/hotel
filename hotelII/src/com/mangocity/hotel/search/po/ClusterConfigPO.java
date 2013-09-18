package com.mangocity.hotel.search.po;

import java.util.Date;

/**
 * 集群配置参数对象
 * 
 * @author MT 2011.9.20
 * @version v1.0 
 */
public class ClusterConfigPO implements java.io.Serializable{

	private static final long serialVersionUID = -5166074223920546212L;
	
	/* 序列号  */
	private String repId;
	
	/* 应用名称  */
	private String appName;
	
	/* 节点名称  */
	private String nodeName;
	
	/* 节点主机  */
	private String nodeHost;
	
	/* 备用主机  */
	private String helperHosts;
	
	/* 节点目录  */
	private String repDir;
	
	/* 集群名称  */
	private String groupName;
	
	/* 序列号  */
	private Date gwtCreated;
	
	/* 创建者  */
	private String creater;
	
	/* 修改时间  */
	private Date gwtModified;
	
	/* 修改者  */
	private String modifier;
	
	public ClusterConfigPO(){
		super();
	}


	/**
	 * @return the repId
	 */
	public String getRepId() {
		return repId;
	}


	/**
	 * @param repId the repId to set
	 */
	public void setRepId(String repId) {
		this.repId = repId;
	}


	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}


	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}


	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}


	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}


	/**
	 * @return the nodeHost
	 */
	public String getNodeHost() {
		return nodeHost;
	}


	/**
	 * @param nodeHost the nodeHost to set
	 */
	public void setNodeHost(String nodeHost) {
		this.nodeHost = nodeHost;
	}


	/**
	 * @return the helperHosts
	 */
	public String getHelperHosts() {
		return helperHosts;
	}


	/**
	 * @param helperHosts the helperHosts to set
	 */
	public void setHelperHosts(String helperHosts) {
		this.helperHosts = helperHosts;
	}


	/**
	 * @return the repDir
	 */
	public String getRepDir() {
		return repDir;
	}


	/**
	 * @param repDir the repDir to set
	 */
	public void setRepDir(String repDir) {
		this.repDir = repDir;
	}


	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}


	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	/**
	 * @return the gwtCreated
	 */
	public Date getGwtCreated() {
		return gwtCreated;
	}


	/**
	 * @param gwtCreated the gwtCreated to set
	 */
	public void setGwtCreated(Date gwtCreated) {
		this.gwtCreated = gwtCreated;
	}


	/**
	 * @return the creater
	 */
	public String getCreater() {
		return creater;
	}


	/**
	 * @param creater the creater to set
	 */
	public void setCreater(String creater) {
		this.creater = creater;
	}


	/**
	 * @return the gwtModified
	 */
	public Date getGwtModified() {
		return gwtModified;
	}


	/**
	 * @param gwtModified the gwtModified to set
	 */
	public void setGwtModified(Date gwtModified) {
		this.gwtModified = gwtModified;
	}


	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}


	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}


	@Override
	public String toString() {
		return "JEClusterConfigPO [repId=" + repId + ", appName=" + appName
				+ ", nodeName=" + nodeName + ", nodeHost=" + nodeHost
				+ ", helperHosts=" + helperHosts + ", repDir=" + repDir
				+ ", groupName=" + groupName + ", gwtCreated=" + gwtCreated
				+ ", creater=" + creater + ", gwtModified=" + gwtModified
				+ ", modifier=" + modifier + "]";
	}

}
