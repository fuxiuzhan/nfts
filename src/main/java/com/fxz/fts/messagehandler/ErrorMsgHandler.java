/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月1日 上午9:58:43 
 * 
 */
package com.fxz.fts.messagehandler;

import java.io.IOException;
import org.apache.log4j.Logger;

import com.fxz.fts.config.Const;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.message.ErrorMsg;
import com.fxz.fts.processor.IProcessMessage;
import com.fxz.fts.task.TaskMgr;

import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: ErrorMsgHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午9:58:43
 */

public class ErrorMsgHandler implements IProcessMessage {

	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		ErrorMsg errorMsg = new ErrorMsg(baseMessage);
		logger.error("Exception->" + errorMsg);
		Const.ftsConfig.getDataProcessor().onTaskFail(TaskMgr.getTask(errorMsg.getUuid()));
		TaskMgr.removeTask(errorMsg.getUuid());
		ctx.close();
	}

}
