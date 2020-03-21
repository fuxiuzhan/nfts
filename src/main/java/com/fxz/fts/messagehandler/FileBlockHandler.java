/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:24:29 
 * 
 */
package com.fxz.fts.messagehandler;

import java.io.IOException;
import com.fxz.fts.config.Const;
import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.message.FileBlockMsg;
import com.fxz.fts.processor.IProcessMessage;
import com.fxz.fts.task.TaskMgr;
import com.fxz.fts.utils.FileUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: FileBlockHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:24:29
 */

public class FileBlockHandler implements IProcessMessage {

	FtsConfig ftsConfig = Const.ftsConfig;

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		FileBlockMsg blockMsg = new FileBlockMsg(baseMessage);
		String fileName = TaskMgr.getTask(blockMsg.getUuid()).getFtsFile().getFilePathto() + "\\" + TaskMgr.getTask(blockMsg.getUuid()).getFtsFile().getFileNameto() + TaskMgr.getTask(blockMsg.getUuid()).getFtsFile().getFilesuffix();
		FileUtils.writeFile(fileName, blockMsg.getStartindex(), ftsConfig.getDataProcessor().instream(blockMsg.getData()));
	}

}
