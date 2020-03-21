/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:22:40 
 * 
 */
package com.fxz.fts.messagehandler;

import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.processor.IProcessMessage;

import io.netty.channel.ChannelHandlerContext;

/** 
 * @ClassName: AborTransferHandler 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:22:40  
 */

public class AborTransferHandler implements IProcessMessage {

	/* (non Javadoc) 
	 * @Title: process
	 * @Description: TODO
	 * @@param ctx
	 * @@param baseMessage 
	 * @@see com.fxz.fts.processor.IProcessMessage#process(io.netty.channel.ChannelHandlerContext, com.fxz.fts.message.BaseMessage) 
	 */ 
	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		
	}}

