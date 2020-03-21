/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.message 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午4:09:05 
 * 
 */
package com.fxz.fts.message;

import java.util.HashMap;
import com.fxz.auth.Utils;
import com.fxz.fts.config.Const;
import com.fxz.fts.task.FtsFile;
import com.fxz.fts.task.FtsTask;

/**
 * @ClassName: SendFileRequestMsg
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午4:09:05
 */
public class SendFileRequestMsg extends BaseMessage {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	public SendFileRequestMsg(FtsTask ftsFile) {
		setType(Const.MSG_SEND_FILEREQUEST);
		String json = Utils.object2Json(ftsFile);
		setBody(json.getBytes());
	}

	public SendFileRequestMsg(BaseMessage baseMessage) {
		super(baseMessage);
		setType(Const.MSG_SEND_FILEREQUEST);
	}

	public FtsTask getFtsTask() {
		@SuppressWarnings("rawtypes")
		HashMap<String, Class> mapper = new HashMap<>();
		mapper.put("ftsFile", FtsFile.class);
		FtsTask fileInfo = (FtsTask) Utils.json2Object(new String(getBody()), FtsTask.class, mapper);
		return fileInfo;
	}

}
