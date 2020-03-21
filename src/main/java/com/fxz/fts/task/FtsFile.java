/**
 * @Copyright © 2018 fuxiuzhan Fts Team All rights reserved.
 * @Package: com.fxz.fts.utils 
 * @author: Administrator   
 * @date: 2018年8月30日 下午3:55:51 
 * 
 */
package com.fxz.fts.task;

import java.io.Serializable;

/**
 * @ClassName: FtsFile
 * @Description: 文件信息实体
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午3:55:51
 */

public class FtsFile implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String filePath;
	private String filePathto;
	private String fileName;
	private String fileNameto;
	private String filesuffix = ".fxz";
	private long fileSize = 0L;
	private boolean overWrite = false;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePathto() {
		return filePathto;
	}

	public void setFilePathto(String filePathto) {
		this.filePathto = filePathto;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileNameto() {
		return fileNameto;
	}

	public void setFileNameto(String fileNameto) {
		this.fileNameto = fileNameto;
	}

	public String getFilesuffix() {
		return filesuffix;
	}

	public void setFilesuffix(String filesuffix) {
		this.filesuffix = filesuffix;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public boolean isOverWrite() {
		return overWrite;
	}

	public void setOverWrite(boolean overWrite) {
		this.overWrite = overWrite;
	}

	@Override
	public String toString() {
		return "FtsFile [filePath=" + filePath + ", filePathto=" + filePathto
				+ ", fileName=" + fileName + ", fileNameto=" + fileNameto
				+ ", filesuffix=" + filesuffix + ", fileSize=" + fileSize
				+  ", overWrite=" + overWrite + "]";
	}



}
