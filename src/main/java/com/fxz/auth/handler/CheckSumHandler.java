/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.auth.handler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月2日 下午6:32:58 
 * 
 */
package com.fxz.auth.handler;

import org.apache.log4j.Logger;
import com.fxz.auth.IDigest;
import com.fxz.auth.encryptions.EncryptFactory;
import com.fxz.fts.message.BaseMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * @ClassName: CheckSumHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月2日 下午6:32:58
 */

public class CheckSumHandler extends ChannelHandlerAdapter {

	String digesttype = "none";
	Logger logger = Logger.getLogger(this.getClass());

	public CheckSumHandler(String checksumtype) {
		this.digesttype = checksumtype;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		if (msg instanceof BaseMessage) {
			BaseMessage baseMessage = (BaseMessage) msg;
			IDigest digest = EncryptFactory.getDigest(digesttype);
			if (digest.digest(baseMessage.getBody()).equalsIgnoreCase(new String(baseMessage.getChecksum()))) {
				super.channelRead(ctx, msg);
			} else {
				logger.error("-----------------checksumerror-------------------------");
			}
		} else {
			logger.error("Message Format Error ->" + msg.getClass().getName());
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		if (msg instanceof BaseMessage) {
			IDigest digest = EncryptFactory.getDigest(digesttype);
			BaseMessage baseMessage = (BaseMessage) msg;
			baseMessage.setChecksum(digest.digest(baseMessage.getBody()).getBytes());
			super.write(ctx, baseMessage, promise);
		} else {
			logger.error("Message Format Error ->" + msg.getClass().getName());
		}
	}

}
