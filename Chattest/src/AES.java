import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.ArrayUtils;

public class AES {
	
	private static final String ALGO = "AES";

	public static byte[] encrypt(final String data, byte[] secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		final SecretKeySpec keySpec = new SecretKeySpec(secretKey, 0, 16, ALGO);
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		SecureRandom rand = new SecureRandom();
		byte[] iv = new byte[cipher.getBlockSize()];
		rand.nextBytes(iv);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
		
		final byte[] encryptedData = cipher.doFinal(data.getBytes());
		System.out.println("\nTEST"); 
		System.out.println(iv + " length: " + iv.length);
		System.out.println(encryptedData + " length: " + encryptedData.length);
		final byte[] concat = ArrayUtils.addAll(iv, encryptedData);
		System.out.println(concat + " length: " + concat.length);
		
		return concat;
	}
	
	public static String decrypt(final byte[] data, byte[] secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		final SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGO);
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		byte[] iv = Arrays.copyOfRange(data, 0, 16);
		byte[] message = Arrays.copyOfRange(data, 16, data.length);
	
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
		
		return new String(cipher.doFinal(message));
	}
	
}
