package com.fxz.compress.impl;

import org.xerial.snappy.Snappy;

import com.fxz.compress.AbstractCompress;
import com.fxz.compress.CompressExceptions;
import com.fxz.compress.ICompress;

/**
 * @ClassName: SnappyCompress
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午8:59:29
 */
public class SnappyCompress extends AbstractCompress implements ICompress {

	@Override
	public byte[] compress(byte[] buffer) throws CompressExceptions {
		try {
			return Snappy.compress(buffer);
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}
	}

	@Override
	public byte[] decompress(byte[] buffer) throws CompressExceptions {
		try {
			return Snappy.uncompress(buffer);
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}
	}

}
