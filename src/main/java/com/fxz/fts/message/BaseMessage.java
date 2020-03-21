package com.fxz.fts.message;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @ClassName: BaseMessage
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午4:29:09
 */
public class BaseMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final byte HEADER1 = 0x0f;
	public static final byte HEADER2 = 0x0f;
	private byte type = 0x00;
	private byte verion = 0x00;
	private byte[] body;
	private byte[] checksum = new byte[] { 0x30, 0x31 };

	public byte getVerion() {
		return verion;
	}

	public void setVerion(byte verion) {
		this.verion = verion;
	}

	public byte getType() {
		return type;
	}

	public BaseMessage() {
	}

	public BaseMessage(BaseMessage bmessage) {
		this.setType(bmessage.getType());
		this.setBody(bmessage.getBody());
		this.setChecksum(bmessage.getChecksum());
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {

		this.body = body;
	}

	public byte[] getChecksum() {
		return checksum;
	}

	public void setChecksum(byte[] checksum) {
		this.checksum = checksum;
	}

	@Override
	public String toString() {
		return "BaseMessage [type=" + type + ", verion=" + verion + ", body=" + body.length + ", checksum=" + Arrays.toString(checksum) + "]";
	}

}
