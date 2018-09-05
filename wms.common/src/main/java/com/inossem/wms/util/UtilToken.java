package com.inossem.wms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

public class UtilToken {

	public static final String MAGIC_KEY = "obfuscate";

	public static String createToken(UserDetails userDetails) {
		/* Expires in one hour */
		long expires = System.currentTimeMillis() + 1000L * 60 * 60;

		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(userDetails.getUsername());
		tokenBuilder.append(":");
		tokenBuilder.append(expires);
		tokenBuilder.append(":");
		tokenBuilder.append(UtilToken.computeSignature(userDetails, expires));

		return tokenBuilder.toString();
	}

	public static String createToken(UserDetails userDetails, long time) {
		/* Expires in one hour */
		long expires = System.currentTimeMillis() + time;

		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(userDetails.getUsername());
		tokenBuilder.append(":");
		tokenBuilder.append(expires);
		tokenBuilder.append(":");
		tokenBuilder.append(UtilToken.computeSignature(userDetails, expires));

		return tokenBuilder.toString();
	}

	/**
	 * 创建TOKEN,有效时间以参数传递
	 * 
	 * @param userDetails
	 * @param expires
	 * @return
	 */
	public static String computeSignature(UserDetails userDetails, long expires) {
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(userDetails.getUsername());
		signatureBuilder.append(":");
		signatureBuilder.append(expires);
		signatureBuilder.append(":");
		signatureBuilder.append(userDetails.getPassword());
		signatureBuilder.append(":");
		signatureBuilder.append(UtilToken.MAGIC_KEY);

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!");
		}

		return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
	}

	public static String getUserNameFromToken(String authToken) {
		if (null == authToken) {
			return null;
		}

		String[] parts = authToken.split(":");
		return parts[0];
	}

	public static boolean validateToken(String authToken, UserDetails userDetails) {
		if (authToken == null || "".equals(authToken) || "null".equals(authToken)) {
			return false;
		}
		String[] parts = authToken.split(":");
		long expires = Long.parseLong(parts[1]);
		String signature = parts[2];

		if (expires < System.currentTimeMillis()) {
			return false;
		}

		return signature.equals(UtilToken.computeSignature(userDetails, expires));
	}

	/**
	 * 判断是否需要创建新的token(距离结束时间小于2分钟则创建)
	 * 
	 * @param authToken
	 * @return
	 */
	public static boolean validateTokenForNewYn(String authToken) {
		String[] parts = authToken.split(":");
		long expires = Long.parseLong(parts[1]); // 获取token中有效时间
		long timeTmp = expires - 1000L * 60 * 2; // 有效时间减去2分钟

		// 当前时间超过 有效时间前2分钟, 创建新TOKEN
		if (System.currentTimeMillis() > timeTmp) {
			return true;
		} else {
			return false;
		}

	}
}