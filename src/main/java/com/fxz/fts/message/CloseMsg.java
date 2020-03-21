/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.messagehandler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月31日 下午4:44:34 
 * 
 */
package com.fxz.fts.message;

import com.fxz.fts.config.Const;

/**
 * @ClassName: CloseMsg
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月31日 下午4:44:34
 */

public class CloseMsg extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	public CloseMsg() {
		setType(Const.MSG_CLOSE);
		setBody(new byte[] { 0x00, 0x00 });
	}
}
