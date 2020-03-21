package com.fxz.fts.processor;

import java.io.IOException;

import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.StringMsg;
import com.fxz.fts.task.FtsTask;

import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: IDataProcessor
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月31日 下午10:11:07
 */
public interface IDataProcessor {
	void init(Object obj);

	byte[] instream(byte[] buffer) throws IOException;

	byte[] outstream(byte[] buffer) throws IOException;

	boolean onStart(FtsConfig ftsConfig, FtsTask ftsTask);

	void onTaskFail(FtsTask ftsTask);

	void onTaskSucc(FtsTask ftsTask);

	void onMessage(ChannelHandlerContext ctx, StringMsg stringMsg);
}
