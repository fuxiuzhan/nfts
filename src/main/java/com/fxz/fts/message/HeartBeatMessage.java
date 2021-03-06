/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.message 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月18日 上午9:22:17 
 * 
 */
package com.fxz.fts.message;

import com.fxz.fts.config.Const;

/**
 * @ClassName: HeartBeatMessage
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月18日 上午9:22:17
 */

public class HeartBeatMessage extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	public HeartBeatMessage() {
		setType(Const.AUTH_HEARTBEAT);
		setBody(new byte[] { 0x00, 0x00 });
	}

}
