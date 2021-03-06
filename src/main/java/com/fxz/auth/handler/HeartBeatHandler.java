/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.auth.handler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月18日 上午9:19:44 
 * 
 */
package com.fxz.auth.handler;

import org.apache.log4j.Logger;

import com.fxz.fts.config.Const;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.message.HeartBeatMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * @ClassName: HeartBeatHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月18日 上午9:19:44
 */

public class HeartBeatHandler extends ChannelHandlerAdapter {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				logger.info("READER_IDLE");
				ctx.writeAndFlush(new HeartBeatMessage());
				logger.info("Send HeartBeat");
			}
			if (event.state() == IdleState.WRITER_IDLE) {
				logger.info("WRITER_IDLE");
				ctx.writeAndFlush(new HeartBeatMessage());
				logger.info("Send HeartBeat");
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		if (((BaseMessage) msg).getType() == Const.AUTH_HEARTBEAT) {
			ReferenceCountUtil.release(msg);
			System.out.println("msg->" + msg);
		} else {
			super.channelRead(ctx, msg);
		}
	}

}
