package com.fxz.fts.message;

import java.nio.ByteBuffer;

import com.fxz.fts.config.Const;

public class AuthMessage extends BaseMessage {

    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = 1L;
    public byte[] data;
    private byte typet;

    public AuthMessage(BaseMessage baseMessage) {
        super(baseMessage);
        setType(Const.MESG_TYPE_AUTH);
        ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
        this.typet = buffer.get();
        int datalen = buffer.getInt();
        byte[] dataarry = new byte[datalen];
        buffer.get(dataarry, 0, datalen);
        this.data = dataarry;
    }

    public AuthMessage(byte type, byte[] data) {
        this.typet = type;
        this.data = data;
        setType(Const.MESG_TYPE_AUTH);
        ByteBuffer buffer = ByteBuffer.allocate(data.length + 1 + 4);
        buffer.put(type);
        buffer.putInt(data.length);
        buffer.put(data);
        setBody(buffer.array());
    }

    public byte getSubType() {
        return typet;
    }

    public byte[] getData() {
        return data;
    }
}
