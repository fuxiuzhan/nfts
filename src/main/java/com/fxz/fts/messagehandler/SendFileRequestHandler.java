/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:25:33 
 * 
 */
package com.fxz.fts.messagehandler;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import com.fxz.fts.config.Const;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.message.FileRequestReplyMsg;
import com.fxz.fts.message.SendFileRequestMsg;
import com.fxz.fts.processor.IProcessMessage;
import com.fxz.fts.task.FtsFile;
import com.fxz.fts.task.TaskMgr;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: SendFileRequestHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:25:33
 */

public class SendFileRequestHandler implements IProcessMessage {

	Logger logger = Logger.getLogger(SendFileRequestHandler.class);

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		SendFileRequestMsg sendFileRequestMsg = new SendFileRequestMsg(baseMessage);
		if (Const.ftsConfig.getDataProcessor().onStart(Const.ftsConfig, sendFileRequestMsg.getFtsTask())) {
			logger.info("File->" + sendFileRequestMsg.getFtsTask());
			FtsFile ftsFile = sendFileRequestMsg.getFtsTask().getFtsFile();
			if (!new File(ftsFile.getFilePathto()).exists() && !new File(ftsFile.getFilePathto()).mkdirs()) {
				ctx.writeAndFlush(new FileRequestReplyMsg(sendFileRequestMsg.getFtsTask().getUuid(), false, 0L, "mkdir failed!"));
				ctx.close();
				return;
			}
			File file = new File(ftsFile.getFilePathto() + "\\" + ftsFile.getFileNameto() + ftsFile.getFilesuffix());
			if (file.exists()) {
				long filesize = file.length();
				if (filesize > ftsFile.getFileSize()) {
					ctx.writeAndFlush(new FileRequestReplyMsg(sendFileRequestMsg.getFtsTask().getUuid(), false, 0L, "File Already Exists and Bigger"));
					ctx.close();
					Const.ftsConfig.getDataProcessor().onTaskFail(sendFileRequestMsg.getFtsTask());
					return;
				} else {
					TaskMgr.addTask(sendFileRequestMsg.getFtsTask().getUuid(), sendFileRequestMsg.getFtsTask());
					ctx.writeAndFlush(new FileRequestReplyMsg(sendFileRequestMsg.getFtsTask().getUuid(), true, filesize, " ready to recv"));
					Const.ftsConfig.getDataProcessor().onStart(Const.ftsConfig, sendFileRequestMsg.getFtsTask());
					return;
				}
			} else {
				// 创建文件
				file.createNewFile();
				TaskMgr.addTask(sendFileRequestMsg.getFtsTask().getUuid(), sendFileRequestMsg.getFtsTask());
				ctx.writeAndFlush(new FileRequestReplyMsg(sendFileRequestMsg.getFtsTask().getUuid(), true, 0L, " ready to recv"));
				Const.ftsConfig.getDataProcessor().onStart(Const.ftsConfig, sendFileRequestMsg.getFtsTask());
				return;
			}
		} else {
			// 拒绝接收此任务
			ctx.writeAndFlush(new FileRequestReplyMsg(sendFileRequestMsg.getFtsTask().getUuid(), false, 0L, " refuse to recv"));
			Const.ftsConfig.getDataProcessor().onTaskFail(sendFileRequestMsg.getFtsTask());
			return;
		}

	}

}
