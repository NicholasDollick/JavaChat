import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * Contains procedure adapted from oracle documentation
 * https://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#DH2Ex
 */

public class DH {
	private final String TAG = "DH";
	private PublicKey publicKey;
	private PublicKey recievedPublicKey;
	private PrivateKey privateKey;
	private byte[] secretKey;
	private byte[] pubKeyEnc;
	
	private static final byte P_BYTES[] = {
    		(byte)0xF4, (byte)0x88, (byte)0xFD, (byte)0x58,
            (byte)0x4E, (byte)0x49, (byte)0xDB, (byte)0xCD,
            (byte)0x20, (byte)0xB4, (byte)0x9D, (byte)0xE4,
            (byte)0x91, (byte)0x07, (byte)0x36, (byte)0x6B,
            (byte)0x33, (byte)0x6C, (byte)0x38, (byte)0x0D,
            (byte)0x45, (byte)0x1D, (byte)0x0F, (byte)0x7C,
            (byte)0x88, (byte)0xB3, (byte)0x1C, (byte)0x7C,
            (byte)0x5B, (byte)0x2D, (byte)0x8E, (byte)0xF6,
            (byte)0xF3, (byte)0xC9, (byte)0x23, (byte)0xC0,
            (byte)0x43, (byte)0xF0, (byte)0xA5, (byte)0x5B,
            (byte)0x18, (byte)0x8D, (byte)0x8E, (byte)0xBB,
            (byte)0x55, (byte)0x8C, (byte)0xB8, (byte)0x5D,
            (byte)0x38, (byte)0xD3, (byte)0x34, (byte)0xFD,
            (byte)0x7C, (byte)0x17, (byte)0x57, (byte)0x43,
            (byte)0xA3, (byte)0x1D, (byte)0x18, (byte)0x6C,
            (byte)0xDE, (byte)0x33, (byte)0x21, (byte)0x2C,
            (byte)0xB5, (byte)0x2A, (byte)0xFF, (byte)0x3C,
            (byte)0xE1, (byte)0xB1, (byte)0x29, (byte)0x40,
            (byte)0x18, (byte)0x11, (byte)0x8D, (byte)0x7C,
            (byte)0x84, (byte)0xA7, (byte)0x0A, (byte)0x72,
            (byte)0xD6, (byte)0x86, (byte)0xC4, (byte)0x03,
            (byte)0x19, (byte)0xC8, (byte)0x07, (byte)0x29,
            (byte)0x7A, (byte)0xCA, (byte)0x95, (byte)0x0C,
            (byte)0xD9, (byte)0x96, (byte)0x9F, (byte)0xAB,
            (byte)0xD0, (byte)0x0A, (byte)0x50, (byte)0x9B,
            (byte)0x02, (byte)0x46, (byte)0xD3, (byte)0x08,
            (byte)0x3D, (byte)0x66, (byte)0xA4, (byte)0x5D,
            (byte)0x41, (byte)0x9F, (byte)0x9C, (byte)0x7C,
            (byte)0xBD, (byte)0x89, (byte)0x4B, (byte)0x22,
            (byte)0x19, (byte)0x26, (byte)0xBA, (byte)0xAB,
            (byte)0xA2, (byte)0x5E, (byte)0xC3, (byte)0x55,
            (byte)0xE9, (byte)0x2F, (byte)0x78, (byte)0xC7
    };
	
	private static final BigInteger P = new BigInteger(1, P_BYTES);
	private static final BigInteger G = BigInteger.valueOf(2);

	
	//keys need to be serialized before transport through wire
	//http://www.javased.com/?api=java.security.spec.X509EncodedKeySpec
	//https://github.com/firatkucuk/diffie-hellman-helloworld
	//https://www.programcreek.com/java-api-examples/java.security.spec.X509EncodedKeySpec
	
	/*
	 * Creates key pair for the client
	 */
	public void generateKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
		System.out.println("[+] Generating Key...");
		DHParameterSpec dhParamSpec = new DHParameterSpec(P, G);
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(TAG);
		keyGen.initialize(dhParamSpec);
		final KeyPair keyPair = keyGen.generateKeyPair();
		
		KeyAgreement keyAgree = KeyAgreement.getInstance(TAG);
		keyAgree.init(keyPair.getPrivate());
		
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
		pubKeyEnc = keyPair.getPublic().getEncoded();
	}
	
	/*
	 * Generates shared secret key
	 */
	public void getShared() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, IllegalStateException {
		final KeyAgreement keyAgree = KeyAgreement.getInstance(TAG);
		keyAgree.init(privateKey);
		keyAgree.doPhase(recievedPublicKey, true);
		
		secretKey = secureSecretKey(keyAgree.generateSecret());
	}
	
	/*
	 * Converts secret key to usable state for AES
	 */
	private byte[] secureSecretKey(final byte[] longKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
		byte[] shortKey = Arrays.copyOf(longKey, 16);
		final SecretKeySpec keySpec = new SecretKeySpec(shortKey, "AES");
		
		return keySpec.getEncoded();
	}
    
	/*
	 * Fetches serialized public key from client
	 */
    public void receivePublicKeyFrom(final DH person) throws InvalidKeySpecException, NoSuchAlgorithmException {

    	recievedPublicKey = person.getPublicKey();
    }
    
    public byte[] getSecret() {
    	return this.secretKey;
    }
    
    /*
     * Generates public key from passed serialized key
     */
    public PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
    	KeyFactory keyFac = KeyFactory.getInstance("DH");
    	X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(this.pubKeyEnc);
    	return keyFac.generatePublic(x509KeySpec);
    }
	
    /*
     * Formats and prints for debugging aid
     */
	public void printKeys() {
		System.out.println("\n[+] Listing Key");
		System.out.println("Shared Key = " + this.secretKey + ", length = " + this.secretKey.length);
		System.out.println(toHexString(this.secretKey));
	}
	
    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                			'9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
     * Converts a byte array to hex string
     */
    private String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len-1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }
    
    /*
     * main used for testing implementation
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
    	
    	
    	/*
    	 * create two clients for testing
    	 */
    	DH bob = new DH();
    	DH alice = new DH();
    	
    	/*
    	 * clients create key pairs
    	 */
    	alice.generateKeys();
    	bob.generateKeys();
    	
    	/*
    	 * clients swap keys
    	 */
    	alice.receivePublicKeyFrom(bob);
    	bob.receivePublicKeyFrom(alice);
    	
    	/*
    	 *  clients generate shared secret
    	 */
    	alice.getShared();
    	bob.getShared();
    	
    	/*
    	 *  display for debugging
    	 */
    	bob.printKeys();
    	alice.printKeys();
    	
    	String message = "hello world";
    	byte[] test = AES.encrypt(message, bob.getSecret());
    	System.out.println("");
    	System.out.println("Clear text: " + message + " \nEncrypted: " + test);
    	System.out.println("Decrypted: " +  AES.decrypt(test, alice.getSecret()));
    }

}
