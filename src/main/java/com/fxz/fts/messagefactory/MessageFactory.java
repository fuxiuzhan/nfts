/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagefactory 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:17:27 
 * 
 */
package com.fxz.fts.messagefactory;

import java.util.HashMap;

import com.fxz.fts.config.Const;
import com.fxz.fts.messagehandler.AborTransferHandler;
import com.fxz.fts.messagehandler.CloseHandler;
import com.fxz.fts.messagehandler.CompleteHandler;
import com.fxz.fts.messagehandler.ErrorMsgHandler;
import com.fxz.fts.messagehandler.FileBlockHandler;
import com.fxz.fts.messagehandler.FileRequestReplyHandler;
import com.fxz.fts.messagehandler.RequestBlockHandler;
import com.fxz.fts.messagehandler.SendFileRequestHandler;
import com.fxz.fts.messagehandler.StringHandler;
import com.fxz.fts.processor.IProcessMessage;

/**
 * @ClassName: MessageFactory
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:17:27
 */

public class MessageFactory {
	private static HashMap<Byte, IProcessMessage> messageHandlerMap = new HashMap<>();
	static {
		messageHandlerMap.put(Const.MSG_ABORTRANFER, new AborTransferHandler());
		messageHandlerMap.put(Const.MSG_COMPELTE, new CompleteHandler());
		messageHandlerMap.put(Const.MSG_FILEBLOCK, new FileBlockHandler());
		messageHandlerMap.put(Const.MSG_FILEREQUESTREPLY, new FileRequestReplyHandler());
		messageHandlerMap.put(Const.MSG_REQUESTBLOCK, new RequestBlockHandler());
		messageHandlerMap.put(Const.MSG_SEND_FILEREQUEST, new SendFileRequestHandler());
		messageHandlerMap.put(Const.MSG_STRINGMG, new StringHandler());
		messageHandlerMap.put(Const.MSG_CLOSE, new CloseHandler());
		messageHandlerMap.put(Const.MSG_ERROR, new ErrorMsgHandler());
	}

	public static IProcessMessage getProcessor(byte type) {
		return messageHandlerMap.get(type);
	}
}
