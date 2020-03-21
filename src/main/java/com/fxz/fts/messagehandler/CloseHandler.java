/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月31日 下午4:49:31 
 * 
 */
package com.fxz.fts.messagehandler;

import java.io.IOException;
import org.apache.log4j.Logger;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.processor.IProcessMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: CloseHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月31日 下午4:49:31
 */

public class CloseHandler implements IProcessMessage {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		logger.info("close client");
		ctx.close();
	}

}
