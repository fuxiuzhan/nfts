package com.fxz.fts.processor;

import java.io.IOException;
import java.util.Queue;

import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.AborTransferMsg;
import com.fxz.fts.message.CompleteMsg;
import com.fxz.fts.message.FileBlockMsg;
import com.fxz.fts.message.FileRequestReplyMsg;
import com.fxz.fts.message.StringMsg;
import com.fxz.fts.task.FtsTask;

import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: IProcessor
 * @Description: 传输控制接口
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:27:30
 */
public interface IProcessor {

	void transfer_init(ChannelHandlerContext ctx,Queue<FtsTask> ftsFilesqueue,FtsConfig ftsConfig) throws IOException;

	void transfer_start(ChannelHandlerContext ctx, FileRequestReplyMsg replyMsg) throws IOException;

	void transfer_processing(ChannelHandlerContext ctx, FileBlockMsg blockMsg) throws IOException;

	void transfer_exception(ChannelHandlerContext ctx,Throwable e) throws IOException;

	void transfer_abor(ChannelHandlerContext ctx, AborTransferMsg aborTransferMsg) throws IOException;

	void transfer_mesgtext(ChannelHandlerContext ctx, StringMsg stringMsg) throws IOException;

	void transfer_close(ChannelHandlerContext ctx);

	void transfer_complete(ChannelHandlerContext ctx, CompleteMsg completeMsg) throws IOException;

	void transfer_status(ChannelHandlerContext ctx, long filesize, long transfered, int speed) throws IOException;
}
