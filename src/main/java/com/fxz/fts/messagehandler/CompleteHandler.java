/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:24:04 
 * 
 */
package com.fxz.fts.messagehandler;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.message.CompleteMsg;
import com.fxz.fts.processor.IProcessMessage;
import com.fxz.fts.task.FtsTask;
import com.fxz.fts.task.TaskMgr;
import com.fxz.fts.utils.FileUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: CompleteHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:24:04
 */

public class CompleteHandler implements IProcessMessage {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		CompleteMsg completeMsg = new CompleteMsg(baseMessage);
		FtsTask ftsTask = TaskMgr.getTask(completeMsg.getUuid());
		String filename = ftsTask.getFtsFile().getFileNameto();
		String filepath = ftsTask.getFtsFile().getFilePathto();
		String fileorgname = filename + ftsTask.getFtsFile().getFilesuffix();
		CompleteMsg completeMsg2;
		if (ftsTask != null) {
			logger.info("File Recv Complete!");
			if (ftsTask.getFtsFile().isOverWrite() && new File(filepath + "\\" + filename).exists()) {
				new File(filepath + "\\" + filename).delete();
				logger.info("Same Name File Deleted!");
			}
			if (new File(filepath + "\\" + filename).exists()) {
				completeMsg2 = new CompleteMsg(completeMsg.getUuid(), false, "file exits");
				logger.error("File Exits not OverWrite!");
			} else {
				File file = new File(filepath + "\\" + fileorgname);
				if (file.exists()) {
					if (FileUtils.getFileHash(ftsTask.getHashType(), filepath + "\\" + fileorgname, 0L).equalsIgnoreCase(ftsTask.getHashCode())) {
						file.renameTo(new File(filepath + "\\" + filename));
						logger.info("File CheckSum OK! HashCode->" + ftsTask.getHashCode());
						completeMsg2 = new CompleteMsg(completeMsg.getUuid(), true, "ok");
					} else {
						completeMsg2 = new CompleteMsg(completeMsg.getUuid(), false, "file check sum error");
						file.delete();
						logger.info("Temp File Check Sum Error ,Deleted!");
					}
				} else {
					completeMsg2 = new CompleteMsg(completeMsg.getUuid(), false, "file not found!");
					logger.error("File Not Found! FileInfo->" + ftsTask.getFtsFile());
				}
			}
			ctx.writeAndFlush(completeMsg2);
			TaskMgr.removeTask(ftsTask.getUuid());
		}

	}

}
