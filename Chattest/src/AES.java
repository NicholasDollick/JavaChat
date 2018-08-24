import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
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
	
	public String encrypt(String data) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Key key = new SecretKeySpec(keyVal, ALGO);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(cipher.ENCRYPT_MODE, key);
		byte[] encodeVal = cipher.doFinal(data.getBytes());
		String encryptVal = Base64.getEncoder().encodeToString(encodeVal);
		
		return encryptVal;
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
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException, UnsupportedEncodingException, InvalidKeySpecException {
		//SecretKey secKey = key.generateKey();
		SecureRandom rand = new SecureRandom();
		byte[] salt = new byte[16];
		rand.nextBytes(salt);
		
		//use password/username combo for below
		KeySpec spec = new PBEKeySpec("theredpowerranger".toCharArray(), salt, 65536, 256);
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] key = f.generateSecret(spec).getEncoded();
		
		AES aes = new AES(key); //this needs to be a secret key
		String test = aes.encrypt("Aftermath");
		
		System.out.println("Encrypt: " + test);
		String test2 = aes.decrypt(test);
		System.out.println("Decrypt: " + test2);
		
	}
	
	public static String encrypts(String value) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(256);
		
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		String key = Base64.getEncoder().encodeToString(raw);
		String encryptKey = key;
		System.out.println("--------KEY-------");
		System.out.println(encryptKey);
		System.out.println("------END OF KEY------");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		byte[] iv = new byte[16];
		SecureRandom rand = new SecureRandom();
		rand.nextBytes(iv);
		IvParameterSpec ivParam = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		String encrypt = Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
		System.out.println(encrypt);
		
		return encrypt;
	}

}
