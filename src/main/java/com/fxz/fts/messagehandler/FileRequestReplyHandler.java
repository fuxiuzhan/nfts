/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:24:51 
 * 
 */
package com.fxz.fts.messagehandler;

import java.io.IOException;

import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.processor.IProcessMessage;

import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: FileRequestReplyHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:24:51
 */

public class FileRequestReplyHandler implements IProcessMessage {

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		// reply 一般为接收方向发送方返回的报文，接收端一般不会收到此报文

	}

}
