package com.yoga.core.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

/**
 * AES 256位加密 
 * java 1.6 link:
 * http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html 
 * java 1.7 link:
 * http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html 
 * java 1.8 link:
 * http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 * 
 * @author Skysea
 *
 */
public class AesUtil {
	private static String ALGORITHM = "AES/CBC/PKCS5Padding";
	private static String ALGORITHM_128 = "AES/ECB/PKCS5Padding";//NoPadding
	private static String ENCODING = "utf-8";
	private static String MESSAGE_DIGEST = "SHA-256";
	private static String MESSAGE_DIGEST_128 = "MD5";
	private static String SECRET_KEY_SPEC = "AES";
	private static int IV_LEN = 16;

	public static String decrypt(String encrypted, String key, String iv) throws Exception {
		byte[] keyb = key.getBytes(ENCODING);
		MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST);
		byte[] thedigest = md.digest(keyb);
		SecretKeySpec skey = new SecretKeySpec(thedigest, SECRET_KEY_SPEC);
		Cipher dcipher = Cipher.getInstance(ALGORITHM);
		dcipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(complementKey(IV_LEN, iv.getBytes())));
		byte[] clearbyte = dcipher.doFinal(DatatypeConverter.parseHexBinary(encrypted));
		return new String(clearbyte);
	}

	public static String encrypt(String content, String key, String iv) throws Exception {
		byte[] input = content.getBytes(ENCODING);
		MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST);
		byte[] thedigest = md.digest(key.getBytes(ENCODING));
		SecretKeySpec skc = new SecretKeySpec(thedigest, SECRET_KEY_SPEC);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, skc, new IvParameterSpec(complementKey(IV_LEN, iv.getBytes())));
		byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
		int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
		ctLength += cipher.doFinal(cipherText, ctLength);
		return DatatypeConverter.printHexBinary(cipherText);
	}

	public static String encrypt128(String content, String key) throws Exception {
		byte[] input = content.getBytes(ENCODING);
		MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_128);
		byte[] thedigest = md.digest(key.getBytes(ENCODING));
		SecretKeySpec skc = new SecretKeySpec(thedigest, SECRET_KEY_SPEC);
		Cipher cipher = Cipher.getInstance(ALGORITHM_128);
		cipher.init(Cipher.ENCRYPT_MODE, skc);
		byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
		int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
		ctLength += cipher.doFinal(cipherText, ctLength);
		return DatatypeConverter.printHexBinary(cipherText);
	}
	
	public static String decrypt128(String encrypted, String key) throws Exception {
		byte[] keyb = key.getBytes(ENCODING);
		MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_128);
		byte[] thedigest = md.digest(keyb);
		SecretKeySpec skey = new SecretKeySpec(thedigest, SECRET_KEY_SPEC);
		Cipher dcipher = Cipher.getInstance(ALGORITHM_128);
		dcipher.init(Cipher.DECRYPT_MODE, skey);
		byte[] clearbyte = dcipher.doFinal(DatatypeConverter.parseHexBinary(encrypted));
		return new String(clearbyte);
	}

	private static byte[] complementKey(int len, byte[] src) {

		byte[] dest = new byte[len];
		for (int i = 0; i < len && i < src.length; i++) {
			dest[i] = src[i];
		}
		return dest;
	}

}