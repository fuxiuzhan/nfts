/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.message 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午4:46:55 
 * 
 */
package com.fxz.fts.message;

import java.nio.ByteBuffer;

import com.fxz.fts.config.Const;

/**
 * @ClassName: FileRequestReply
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午4:46:55
 */

public class FileRequestReplyMsg extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private boolean accept = false;
	private String desc="N/A";
	private String uuid;
	private long offset;
	
	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public FileRequestReplyMsg(BaseMessage baseMessage) {
		super(baseMessage);
		setType(Const.MSG_FILEREQUESTREPLY);
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		this.accept = false;
		if (buffer.get() == 0x01) {
			this.accept = true;
		}
		this.offset=buffer.getLong();
		int uuidlen = buffer.getInt();
		byte[] uuidbyte = new byte[uuidlen];
		buffer.get(uuidbyte);
		this.uuid = new String(uuidbyte);
		int desclen = buffer.getInt();
		byte[] descbyte = new byte[desclen];
		buffer.get(descbyte);
		this.desc = new String(descbyte);
	}

	public FileRequestReplyMsg(String fileuuid, boolean accpet, long offset,String desc) {
		setType(Const.MSG_FILEREQUESTREPLY);
		this.desc = desc;
		this.uuid = fileuuid;
		this.accept = accpet;
		byte[] uuidbyte = uuid.getBytes();
		byte[] descbyte = desc.getBytes();
		byte isaccept = 0x00;
		if (accpet) {
			isaccept = 0x01;
		}
		ByteBuffer buffer = ByteBuffer.allocate(4+8+ + uuidbyte.length + 4 + descbyte.length + 1);
		buffer.put(isaccept);
		buffer.putLong(offset);
		buffer.putInt(uuidbyte.length);
		buffer.put(uuidbyte);
		buffer.putInt(descbyte.length);
		buffer.put(descbyte);
		setBody(buffer.array());

	}

	@Override
	public String toString() {
		return "FileRequestReplyMsg [accept=" + accept + ", desc=" + desc + ", uuid=" + uuid + ", offset=" + offset + "]";
	}

}
