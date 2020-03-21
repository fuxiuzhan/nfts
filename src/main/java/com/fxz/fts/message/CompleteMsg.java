/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.message 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午5:10:39 
 * 
 */
package com.fxz.fts.message;

import java.nio.ByteBuffer;

import com.fxz.fts.config.Const;

/**
 * @ClassName: CompleteMsg
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:10:39
 */

public class CompleteMsg extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	String uuid;
	boolean succ = false;
	String desc = "N/A";

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isSucc() {
		return succ;
	}

	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public CompleteMsg(BaseMessage baseMessage) {
		super(baseMessage);
		setType(Const.MSG_COMPELTE);
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		this.succ = false;
		if (buffer.get() == 0x01) {
			this.succ = true;
		}
		int uuidlen = buffer.getInt();
		byte[] uuidbyte = new byte[uuidlen];
		buffer.get(uuidbyte);
		this.uuid = new String(uuidbyte);
		int desclen = buffer.getInt();
		byte[] descbyte = new byte[desclen];
		buffer.get(descbyte);
		this.desc = new String(descbyte);
	}

	public CompleteMsg(String uuid, boolean succ, String desc) {
		setType(Const.MSG_COMPELTE);
		this.desc = desc;
		this.uuid = uuid;
		this.succ = succ;
		byte[] uuidbyte = uuid.getBytes();
		byte[] descbyte = desc.getBytes();
		byte isaccept = 0x00;
		if (succ) {
			isaccept = 0x01;
		}
		ByteBuffer buffer = ByteBuffer.allocate(4 + uuidbyte.length + 4 + descbyte.length + 1);
		buffer.put(isaccept);
		buffer.putInt(uuidbyte.length);
		buffer.put(uuidbyte);
		buffer.putInt(descbyte.length);
		buffer.put(descbyte);
		setBody(buffer.array());
	}

	@Override
	public String toString() {
		return "CompleteMsg [uuid=" + uuid + ", succ=" + succ + ", desc=" + desc + "]";
	}
	
}
