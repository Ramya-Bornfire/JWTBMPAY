package com.bornfire.configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {
	private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

	public static String encrypt(String data, String key, String iv) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));

		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

		byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String decrypt(String encryptedData, String key, String iv) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));

		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

}
