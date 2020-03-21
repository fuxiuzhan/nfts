/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.processor.impl 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月31日 下午2:40:50 
 * 
 */
package com.fxz.fts.processor.impl;

import java.io.IOException;
import org.apache.log4j.Logger;
import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.StringMsg;
import com.fxz.fts.processor.IDataProcessor;
import com.fxz.fts.task.FtsTask;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: FtsDataProcessor
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月31日 下午2:40:50
 */

public class FtsDataProcessor implements IDataProcessor {

	private Logger logger = Logger.getLogger(FtsDataProcessor.class);

	@Override
	public void init(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] instream(byte[] buffer) throws IOException {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public byte[] outstream(byte[] buffer) throws IOException {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public boolean onStart(FtsConfig ftsConfig, FtsTask ftsTask) {
		// TODO Auto-generated method stub
		logger.info("transfer start->"+ftsTask);
		return true;
	}

	@Override
	public void onTaskFail(FtsTask ftsTask) {
		// TODO Auto-generated method stub
		logger.info("task fail->" + ftsTask);
	}

	@Override
	public void onTaskSucc(FtsTask ftsTask) {
		// TODO Auto-generated method stub
		logger.info("task succ->" + ftsTask);
	}

	@Override
	public void onMessage(ChannelHandlerContext ctx, StringMsg stringMsg) {
		// TODO Auto-generated method stub
		logger.info("Message->" + stringMsg);
	}

}
