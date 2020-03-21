/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.config 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午4:17:23 
 * 
 */
package com.fxz.fts.config;

/**
 * @ClassName: Const
 * @Description: 报文类型常量表
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午4:17:23
 */

public class Const {
	/*
	 * 报文常量类型 MSG_OP 0x0~0x0f保留
	 */
	public static final byte MSG_SEND_FILEREQUEST = 0x10;
	public static final byte MSG_FILEREQUESTREPLY = 0x11;
	public static final byte MSG_FILEBLOCK = 0x12;
	public static final byte MSG_COMPELTE = 0x13;
	public static final byte MSG_STRINGMG = 0x14;
	public static final byte MSG_ABORTRANFER = 0x15;
	public static final byte MSG_REQUESTBLOCK = 0x16;
	public static final byte MSG_CLOSE = 0x17;
	public static final byte MSG_ERROR = 0x18;
	/*
	 * 认证报文常量类型
	 */

	// auth
	public static final byte MESG_TYPE_AUTH = 0x0c;
	public static final byte AUTH_PUBLIC_KEY = 0x01;
	public static final byte AUTH_PRIVATE_KEY = 0x02;
	public static final byte AUTH_DH_MESSAGE = 0x03;
	public static final byte AUTH_NEED_HD = 0x04;
	public static final byte AUTH_READY = 0x05;
	public static final byte AUTH_HEARTBEAT = 0x06;
	
	//文件片段大小
	public static final int segmentsize = 1024 * 64;

	//公用配置引用
	public static FtsConfig ftsConfig = null;
}
