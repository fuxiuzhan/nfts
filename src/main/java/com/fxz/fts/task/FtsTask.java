/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.task 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:04:09 
 * 
 */
package com.fxz.fts.task;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName: FtsTask
 * @Description: FtsFile包装类，增加文件偏移量计数
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:04:09
 */

public class FtsTask implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String uuid = UUID.randomUUID().toString();
	private long currentSeek;
	private Date startTime;
	private Date endTime;
	private boolean succ;
	private String failDesc = "N/A";
	private FtsFile ftsFile;
	private boolean abor = false;
	private String hashType = "none";
	private String hashCode = "N/A";
	private String extendMsg = "N/A";
	private int failTimes = 0;

	public String getExtendMsg() {
		return extendMsg;
	}

	public void setExtendMsg(String extendMsg) {
		this.extendMsg = extendMsg;
	}

	public int getFailTimes() {
		return failTimes;
	}

	public void setFailTimes(int failTimes) {
		this.failTimes = failTimes;
	}

	public String getHashType() {
		return hashType;
	}

	public void setHashType(String hashType) {
		this.hashType = hashType;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public boolean isAbor() {
		return abor;
	}

	public void setAbor(boolean abor) {
		this.abor = abor;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUuid() {
		return uuid;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public boolean isSucc() {
		return succ;
	}

	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	public String getFailDesc() {
		return failDesc;
	}

	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public long getCurrentSeek() {
		return currentSeek;
	}

	public void setCurrentSeek(long currentSeek) {
		this.currentSeek = currentSeek;
	}

	public FtsFile getFtsFile() {
		return ftsFile;
	}

	public void setFtsFile(FtsFile ftsFile) {
		this.ftsFile = ftsFile;
	}

	@Override
	public String toString() {
		return "FtsTask [uuid=" + uuid + ", currentSeek=" + currentSeek + ", startTime=" + startTime + ", endTime=" + endTime + ", succ=" + succ + ", failDesc=" + failDesc + ", ftsFile=" + ftsFile + ", abor=" + abor + ", hashType=" + hashType + ", hashCode=" + hashCode + ", extendMsg=" + extendMsg + ", failTimes=" + failTimes + "]";
	}
}
