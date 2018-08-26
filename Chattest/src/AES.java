import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	
	private static final String ALGO = "AES";
	private byte[] keyVal;
	
	public AES(byte[] key) {
		keyVal = key;
	}
	

	public static byte[] encryptTest(final String data, byte[] secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		final SecretKeySpec keySpec = new SecretKeySpec(secretKey, 0, 16, ALGO);
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(secretKey);
		
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
		
		final byte[] encryptedData = cipher.doFinal(data.getBytes());
		
		return encryptedData;
	}
	
	public static String decryptTest(final byte[] data, byte[] secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		final SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGO);
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(secretKey);
		
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
		
		return new String(cipher.doFinal(data));
	}
	

	public String decrypt(String data) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Key key = new SecretKeySpec(keyVal, ALGO);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(cipher.DECRYPT_MODE, key);
		byte[] decodeVal = Base64.getDecoder().decode(data);
		byte[] decVal = cipher.doFinal(decodeVal);
		String decryptedValue = new String(decVal);
		
		return decryptedValue;
	}
}
