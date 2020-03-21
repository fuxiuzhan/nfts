/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.processor.impl 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午5:34:24 
 * 
 */
package com.fxz.fts.processor.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import org.apache.log4j.Logger;
import com.fxz.fts.config.Const;
import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.AborTransferMsg;
import com.fxz.fts.message.CloseMsg;
import com.fxz.fts.message.CompleteMsg;
import com.fxz.fts.message.FileBlockMsg;
import com.fxz.fts.message.FileRequestReplyMsg;
import com.fxz.fts.message.SendFileRequestMsg;
import com.fxz.fts.message.StringMsg;
import com.fxz.fts.processor.IProcessor;
import com.fxz.fts.task.FtsTask;
import com.fxz.fts.task.TaskMgr;
import com.fxz.fts.utils.FileUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: FtsProcessor
 * @Description: 实现整个传输控制逻辑，底层包的校验功能有安全模块实现
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:34:24
 */

public class FtsProcessor implements IProcessor {

	private Queue<FtsTask> qTask;
	ChannelHandlerContext tctx;
	FtsTask ftsTask;
	Logger logger = Logger.getLogger(FtsProcessor.class);
	int i = 0;
	long starttime = 0L;
	boolean process_flag = false;
	FtsConfig ftsConfig;

	@Override
	public void transfer_init(ChannelHandlerContext ctx, Queue<FtsTask> ftsFilesqueue, FtsConfig ftsConfig) throws IOException {
		// TODO Auto-generated method stub
		this.qTask = ftsFilesqueue;
		this.tctx = ctx;
		this.ftsConfig = ftsConfig;
		ftsTask = qTask.poll();
		if (ftsTask != null) {
			TaskMgr.addTask(ftsTask.getUuid(), ftsTask);
			SendFileRequestMsg sendFileRequestMsg = new SendFileRequestMsg(ftsTask);
			ctx.writeAndFlush(sendFileRequestMsg);
			ftsTask.setStartTime(new Date());
			logger.info("File Send Request Sended!");
		} else {
			logger.info("Task Queue is Empty ,Client Exit!");
		}
	}

	@Override
	public void transfer_start(ChannelHandlerContext ctx, FileRequestReplyMsg replyMsg) throws IOException {
		// TODO Auto-generated method stub
		logger.info(replyMsg);
		if (replyMsg.isAccept()) {
			logger.info("Server Accept File Sending Request!");
			if (replyMsg.getOffset() <= ftsTask.getFtsFile().getFileSize()) {
				i = 0;
				if (ftsConfig.getDataProcessor().onStart(ftsConfig, ftsTask)) {
					transfer_processing(ctx, new FileBlockMsg(ftsTask.getUuid(), replyMsg.getOffset(), new byte[] { 0x00, 0x01 }));
					logger.info("get File Offset ->" + replyMsg.getOffset());
					ftsTask.setCurrentSeek(replyMsg.getOffset());
					starttime = System.currentTimeMillis();
				} else {
					ftsConfig.getDataProcessor().onTaskFail(ftsTask);
				}
			} else {
				logger.error("offset error: server reply bigger than filesize");
			}
		} else {
			ftsTask.setEndTime(new Date());
			ftsTask.setSucc(false);
			ftsTask.setFailDesc(replyMsg.getDesc());
			logger.info("Server Refuse File  Request and Returned->" + replyMsg.getDesc());
			transfer_complete(ctx, new CompleteMsg(replyMsg.getUuid(), false, replyMsg.getDesc()));
			ftsConfig.getDataProcessor().onTaskFail(ftsTask);
		}
	}

	@Override
	public void transfer_processing(ChannelHandlerContext ctx, FileBlockMsg blockMsg) throws IOException {
		if (blockMsg.getStartindex() >= 0 && blockMsg.getStartindex() < ftsTask.getCurrentSeek()) {
			ftsTask.setCurrentSeek(blockMsg.getStartindex());
			ftsTask.setAbor(false);
			logger.info("get Request Block Offset->" + blockMsg.getStartindex());
		}
		if (process_flag) {
			return;
		}
		while (TaskMgr.isRunning(ftsTask.getUuid()) && ctx.channel().isWritable() && !ftsTask.isAbor()) {
			process_flag = true;
			byte[] data = FileUtils.readFile(ftsTask.getFtsFile().getFilePath() + "\\" + ftsTask.getFtsFile().getFileName(), ftsTask.getCurrentSeek(), Const.segmentsize);
			data = ftsConfig.getDataProcessor().outstream(data);
			if (data != null) {
				ctx.writeAndFlush(new FileBlockMsg(ftsTask.getUuid(), ftsTask.getCurrentSeek(), data));
				long timeelapse = System.currentTimeMillis() - starttime;
				if (timeelapse == 0) {
					timeelapse = 1;
				}
				if (timeelapse > 1000) {
					transfer_status(ctx, ftsTask.getFtsFile().getFileSize(), i * Const.segmentsize + data.length, (int) (i * Const.segmentsize / timeelapse));
				}
				i++;
				ftsTask.setCurrentSeek(ftsTask.getCurrentSeek() + data.length);
			} else {
				logger.info("File Read Complete!");
				ctx.writeAndFlush(new CompleteMsg(ftsTask.getUuid(), true, "send complete"));
				ftsTask.setAbor(true);
			}
		}
		process_flag = false;
	}

	@Override
	public void transfer_exception(ChannelHandlerContext ctx, Throwable e) throws IOException {
		logger.error("Exception->" + e.getMessage());
		//
		TaskMgr.removeTask(ftsTask.getUuid());
		ftsConfig.getDataProcessor().onTaskFail(ftsTask);
	}

	@Override
	public void transfer_abor(ChannelHandlerContext ctx, AborTransferMsg aborTransferMsg) throws IOException {
		// TODO Auto-generated method stub
		logger.info(aborTransferMsg);
		ftsConfig.getDataProcessor().onTaskFail(ftsTask);
	}

	@Override
	public void transfer_mesgtext(ChannelHandlerContext ctx, StringMsg stringMsg) throws IOException {
		logger.info("StringMsg->" + stringMsg);
		ftsConfig.getDataProcessor().onMessage(ctx, stringMsg);

	}

	@Override
	public void transfer_close(ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transfer_complete(ChannelHandlerContext ctx, CompleteMsg completeMsg) throws IOException {
		logger.info(completeMsg);
		ftsTask.setSucc(completeMsg.isSucc());
		ftsTask.setEndTime(new Date());
		ftsTask.setFailDesc(completeMsg.getDesc());
		ftsTask.setAbor(true);
		System.out.println("Send Complete!");
		if (completeMsg.isSucc()) {
			logger.info("file Sended");
			ftsConfig.getDataProcessor().onTaskSucc(ftsTask);
		} else {
			logger.warn("file send fail msg->" + completeMsg.getDesc());
			ftsConfig.getDataProcessor().onTaskFail(ftsTask);
		}
		ftsTask = qTask.poll();
		if (ftsTask != null) {
			TaskMgr.addTask(ftsTask.getUuid(), ftsTask);
			SendFileRequestMsg sendFileRequestMsg = new SendFileRequestMsg(ftsTask);
			ctx.writeAndFlush(sendFileRequestMsg);
			ftsTask.setStartTime(new Date());
		} else {
			logger.info("Task Queue is Empty ,Client Exit!");
			ctx.writeAndFlush(new CloseMsg());
		}
	}

	@Override
	public void transfer_status(ChannelHandlerContext ctx, long filesize, long transfered, int speed) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("FileSize->" + filesize + " Transfed->" + transfered + " Speed->" + speed + " KB/S");
	}

}
