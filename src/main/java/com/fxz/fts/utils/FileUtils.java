package com.fxz.fts.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.zip.CRC32;
import com.fxz.auth.Utils;

public class FileUtils {
	public static void writeFile(String filename, long seek, byte[] data) throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw");
		randomAccessFile.seek(seek);
		randomAccessFile.write(data);
		randomAccessFile.close();
	}

	public static byte[] readFile(String filename, long seek, int length) throws IOException {
		byte[] data = new byte[length];
		RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw");
		try {
			randomAccessFile.seek(seek);
			int actlen = randomAccessFile.read(data);
			if (actlen > 0) {
				// randomAccessFile.close();
				byte[] actbytes = new byte[actlen];
				System.arraycopy(data, 0, actbytes, 0, actlen);
				return actbytes;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			randomAccessFile.close();
		}
		return null;
	}

	public static String getMd5(String file, Long offset) throws IOException {
		InputStream is = null;
		try {
			is = createInputStream(file, offset);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(is, digest);
			byte[] buffer = new byte[8192];
			int read = dis.read(buffer);
			while (read > -1) {
				read = dis.read(buffer);
			}
			return Utils.Byte2Hex(dis.getMessageDigest().digest());
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			close(is);
		}
	}

	public static String getCRC32(String file, Long offset) throws IOException {
		InputStream fileInputStream = null;
		try {
			CRC32 crc32 = new CRC32();
			fileInputStream = createInputStream(file, offset);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				crc32.update(buffer, 0, length);
			}
			return Long.toHexString(crc32.getValue());
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			close(fileInputStream);
		}
	}

	public static InputStream createInputStream(String file, final long offset) throws IOException {
		final RandomAccessFile raf = new RandomAccessFile(file, "r");
		raf.seek(offset);
		// The IBM jre needs to have both the stream and the random access file
		// objects closed to actually close the file
		return new FileInputStream(raf.getFD()) {
			@Override
			public void close() throws IOException {
				super.close();
				raf.close();
			}
		};
	}

	public static String getFileHash(String type, String file, long offset) throws IOException {
		if (type.equalsIgnoreCase("crc32")) {
			return getCRC32(file, offset);
		}
		if (type.equalsIgnoreCase("md5")) {
			return getMd5(file, offset);
		}
		return "N/A";
	}

	public final static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (Exception ex) {
			}
		}
	}

	public static void clearFileorDir(File path) {
		if (!path.exists())
			return;
		if (path.isFile()) {
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			clearFileorDir(files[i]);
		}
		path.delete();
	}
}
