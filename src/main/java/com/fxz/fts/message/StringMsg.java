package com.fxz.fts.message;

import java.nio.ByteBuffer;
import com.fxz.fts.config.Const;

/**
 * @ClassName: StringMessage
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:17:08
 */
public class StringMsg extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String meString;

	public StringMsg(BaseMessage baseMessage) {
		super(baseMessage);
		setType(Const.MSG_STRINGMG);
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		int mesglen = buffer.getInt();
		byte[] mesgarry = new byte[mesglen];
		buffer.get(mesgarry, 0, mesglen);
		meString = new String(mesgarry);
	}

	public String getMessage() {
		return meString;
	}

	public StringMsg(String mesg) {
		setType(Const.MSG_STRINGMG);
		ByteBuffer buffer = ByteBuffer.allocate(mesg.getBytes().length + 4);
		buffer.putInt(mesg.getBytes().length);
		buffer.put(mesg.getBytes());
		setBody(buffer.array());
	}

	@Override
	public String toString() {
		return "StringMsg [meString=" + meString + "]";
	}
	

}
