/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:25:48 
 * 
 */
package com.fxz.fts.messagehandler;

import java.io.IOException;
import org.apache.log4j.Logger;

import com.fxz.fts.config.Const;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.message.StringMsg;
import com.fxz.fts.processor.IProcessMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: StringHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:25:48
 */

public class StringHandler implements IProcessMessage {
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		logger.info("get StringMsg->" + new StringMsg(baseMessage));
		Const.ftsConfig.getDataProcessor().onMessage(ctx, new StringMsg(baseMessage));
	}

}
