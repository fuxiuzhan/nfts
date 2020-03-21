/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.message 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月1日 上午9:39:22 
 * 
 */
package com.fxz.fts.message;

import java.nio.ByteBuffer;

import com.fxz.fts.config.Const;

/**
 * @ClassName: ErrorMsg
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午9:39:22
 */

public class ErrorMsg extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String uuid;
	private String error;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public ErrorMsg(BaseMessage baseMessage) {
		super(baseMessage);
		setType(Const.MSG_ERROR);
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		int uuidlen = buffer.getInt();
		byte[] uuidarry = new byte[uuidlen];
		buffer.get(uuidarry, 0, uuidlen);
		uuid = new String(uuidarry);
		int errorlen = buffer.getInt();
		byte[] errorarry = new byte[errorlen];
		buffer.get(errorarry);
		error = new String(errorarry);
	}

	public ErrorMsg(String uuid, String errormsg) {
		setType(Const.MSG_ERROR);
		ByteBuffer buffer = ByteBuffer.allocate(uuid.getBytes().length + 4+4+errormsg.getBytes().length);
		buffer.putInt(uuid.getBytes().length);
		buffer.put(uuid.getBytes());
		buffer.putInt(errormsg.getBytes().length);
		buffer.put(errormsg.getBytes());
		setBody(buffer.array());
	}

	@Override
	public String toString() {
		return "ErrorMsg [uuid=" + uuid + ", error=" + error + "]";
	}
}
