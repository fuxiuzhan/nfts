/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.compress 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月1日 上午9:00:12 
 * 
 */
package com.fxz.compress;

import java.util.HashMap;

import com.fxz.compress.impl.DeflaterCompress;
import com.fxz.compress.impl.GzipCompress;
import com.fxz.compress.impl.Lz4Compress;
import com.fxz.compress.impl.LzoCompress;
import com.fxz.compress.impl.SnappyCompress;

/**
 * @ClassName: CompressFactory
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午9:00:12
 */

public class CompressFactory {
	private static HashMap<String, ICompress> compressMap = new HashMap<>();
	static {
		compressMap.put("deflater", new DeflaterCompress());
		compressMap.put("gzip", new GzipCompress());
		compressMap.put("lz4", new Lz4Compress());
		compressMap.put("lzo", new LzoCompress());
		compressMap.put("snappy", new SnappyCompress());
	}

	public static ICompress getCompressor(String type) {
		return compressMap.get(type);
	}
}
