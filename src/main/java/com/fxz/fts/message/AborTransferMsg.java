package com.fxz.fts.message;

import java.nio.ByteBuffer;

import com.fxz.fts.config.Const;


/** 
 * @ClassName: AborTransferMsg 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:19:00  
 */
public class AborTransferMsg extends BaseMessage {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String uuid;
	private String desc;

	public AborTransferMsg(BaseMessage baseMessage) {
		super(baseMessage);
		setType(Const.MSG_ABORTRANFER);
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		int uuidlen = buffer.getInt();
		byte[] uuidarry = new byte[uuidlen];
		buffer.get(uuidarry, 0, uuidlen);
		this.uuid = new String(uuidarry);
		int desclen = buffer.getInt();
		byte[] descarry = new byte[desclen];
		buffer.get(descarry, 0, desclen);
		this.desc = new String(descarry);
	}

	public String getUUID() {
		return uuid;
	}

	public String getDesc() {
		return desc;
	}

	public AborTransferMsg(String uuid, String desc) {
		this.uuid = uuid;
		this.desc = desc;
		setType(Const.MSG_ABORTRANFER);
		ByteBuffer buffer = ByteBuffer.allocate(uuid.getBytes().length + desc.getBytes().length + 2 * 4);
		buffer.putInt(uuid.getBytes().length);
		buffer.put(uuid.getBytes());
		buffer.putInt(desc.getBytes().length);
		buffer.put(desc.getBytes());
		setBody(buffer.array());
	}

	@Override
	public String toString() {
		return "AborTransferMsg [uuid=" + uuid + ", desc=" + desc + "]";
	}
	
}
