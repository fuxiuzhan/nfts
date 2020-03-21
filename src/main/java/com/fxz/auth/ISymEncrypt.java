package com.fxz.auth;

import com.fxz.auth.exceptions.EncryptExcepton;

/**
 * @ClassName: ISymEncrypt
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年1月25日 下午2:11:51
 */
public interface ISymEncrypt {
	public byte[] encrypt(byte[] buffer, String key) throws EncryptExcepton;

	public byte[] decrypt(byte[] buffer, String key) throws EncryptExcepton;
}
