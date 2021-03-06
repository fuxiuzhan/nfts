package com.fxz.auth.impl;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.fxz.auth.ISymEncrypt;
import com.fxz.auth.Utils;
import com.fxz.auth.exceptions.EncryptExcepton;

public class DES implements ISymEncrypt {

	public byte[] encrypt(byte[] buffer, String key) throws EncryptExcepton {
		try {
			key = key + "0123456789abcdefgk";
			byte[] keybytes = Utils.subBytes(Utils.Hex2Byte(key), 0, 8);
			IvParameterSpec iv = new IvParameterSpec(keybytes);
			DESKeySpec desKey = new DESKeySpec(keybytes);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
			return cipher.doFinal(buffer);
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

	public byte[] decrypt(byte[] buffer, String key) throws EncryptExcepton {
		try {
			key = key + "0123456789abcdefgk";
			byte[] keybytes = Utils.subBytes(Utils.Hex2Byte(key), 0, 8);
			IvParameterSpec iv = new IvParameterSpec(keybytes);
			DESKeySpec desKey = new DESKeySpec(keybytes);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
			return cipher.doFinal(buffer);
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

	public static void main(String[] args) throws EncryptExcepton {
		String testStr = "0123abcd一二三";
		String key = "00000000000000000000000000000000";
		String en_str = Utils.Byte2Hex(new DES().encrypt(testStr.getBytes(), key));
		System.out.println("en_str->" + en_str);
		System.out.println("de_str->" + new String(new DES().decrypt(Utils.Hex2Byte(en_str), key)));
	}
}
