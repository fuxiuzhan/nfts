/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.message 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午5:02:04 
 * 
 */
package com.fxz.fts.message;

import java.nio.ByteBuffer;

import com.fxz.fts.config.Const;

/**
 * @ClassName: FileBlockMsg
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:02:04
 */

public class FileBlockMsg extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String uuid;
	private long startindex;
	private byte[] data;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public long getStartindex() {
		return startindex;
	}

	public void setStartindex(long startindex) {
		this.startindex = startindex;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public FileBlockMsg(BaseMessage baseMessage) {
		super(baseMessage);
		setType(Const.MSG_FILEBLOCK);
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		int uuidlen = buffer.getInt();
		byte[] uuidarry = new byte[uuidlen];
		buffer.get(uuidarry, 0, uuidlen);
		this.uuid = new String(uuidarry);
		this.startindex = buffer.getLong();
		int datalen = buffer.getInt();
		byte[] data = new byte[datalen];
		buffer.get(data, 0, datalen);
		this.data = data;
	}

	public FileBlockMsg(String uuid, long startindex, byte[] data) {
		setType(Const.MSG_FILEBLOCK);
		this.uuid = uuid;
		this.startindex = startindex;
		this.data = data;
		ByteBuffer buffer = ByteBuffer.allocate(data.length + 4 + 8 + uuid.getBytes().length + 4);
		buffer.putInt(uuid.getBytes().length);
		buffer.put(uuid.getBytes());
		buffer.putLong(startindex);
		buffer.putInt(data.length);
		buffer.put(data);
		setBody(buffer.array());
	}

	@Override
	public String toString() {
		return "FileBlockMsg [uuid=" + uuid + ", startindex=" + startindex + ", data=" + data.length + "]";
	}
	
}
