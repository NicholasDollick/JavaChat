import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

/*
 * Contains procedure adapted from oracle documentation
 * https://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#DH2Ex
 */

public class DH {
	private static final String TAG = "DH";
	private static KeyPair keyPair;
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	private byte[] secretKey;
	
	//public final static int pValue = 41;
	//public final static int gValue = 83;
	
	private static final BigInteger P = BigInteger.probablePrime(512, new SecureRandom());
	private static final BigInteger G = BigInteger.probablePrime(512, new SecureRandom());
	
	public byte[] generatePublicKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {

		
		System.out.println("[+] Generating Key...");
		
		DHParameterSpec dhParamSpec = new DHParameterSpec(P, G);
		
		System.out.println("P = " + P.toString(16));
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DiffieHellman");
		keyGen.initialize(dhParamSpec); //consider securerandom arg
		keyPair = keyGen.generateKeyPair();
		
		System.out.println("Y = " + ((DHPublicKey) keyPair.getPublic()).getY().toString(16));
		
		KeyAgreement keyAgree = KeyAgreement.getInstance("DiffieHellman");
		keyAgree.init(keyPair.getPrivate());
		
		BigInteger pubKeyBI = ((DHPublicKey) keyPair.getPublic()).getY();
		byte[] pubKey = pubKeyBI.toByteArray();
		
		//X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
		
		System.out.println(TAG + " " + String.format("Y [%d] = %s", pubKey.length, pubKey.toString().hashCode()));
		System.out.println("Public Key = " + pubKey);
		//System.out.println(x509KeySpec);
		
		return pubKey;
	}
	
	public void generatePublicKey2() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {

		
		System.out.println("[+] Generating Key...");
		
		DHParameterSpec dhParamSpec = new DHParameterSpec(P, G);
		
		System.out.println("P = " + P.toString(16));
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(TAG);
		keyGen.initialize(dhParamSpec); //consider securerandom arg
		keyPair = keyGen.generateKeyPair();
		
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
		System.out.println("Public Key = " + keyPair.getPublic());
		System.out.println("Private Key = " + keyPair.getPrivate());
	}
	
	public void getShared(PublicKey recievedKey) throws NoSuchAlgorithmException, InvalidKeyException {
		final KeyAgreement keyAgree = KeyAgreement.getInstance(TAG);
		keyAgree.init(privateKey);
		keyAgree.doPhase(recievedKey, true);
		
		secretKey = keyAgree.generateSecret();
	}
	
	public void printKeys() {
		System.out.println("[+] Listing Keys");
		System.out.println("Public Key = " + publicKey);
		System.out.println("Private Key = " + privateKey);
		System.out.println("Shared Key = " + secretKey);
	}
	
	public byte[] computeSharedKey(byte[] pubKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
			BigInteger pubKeyBI = new BigInteger(1, pubKey); 
			
			System.out.println("\n[+] Computing Key...");
			System.out.println("Y = " + pubKeyBI.toString(16));
			
			PublicKey pubKey2 = keyFactory.generatePublic(new DHPublicKeySpec(pubKeyBI, P, G));
			//keyAgree.doPhase(pubKey2, true);
			//byte[] sharedKey = keyAgree.generateSecret();
			
			System.out.println("PubKey2 = " + pubKey2.getEncoded());
			//System.out.println("SHARED KEY = " + sharedKey.toString());
			
			//return sharedKey;
			return null;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public void test(byte[] passedKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {
		KeyFactory keyFactory = KeyFactory.getInstance("DH");
	    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(passedKey);
	    PublicKey recievedPubKey = keyFactory.generatePublic(x509KeySpec);
	    DHParameterSpec dhParamFromRecieved = ((DHPublicKey)recievedPubKey).getParams();
	    
	    System.out.println("[+] Generating DH pair...");
	    
	    KeyPairGenerator pairGen = KeyPairGenerator.getInstance("DH");
	    pairGen.initialize(dhParamFromRecieved);
	    KeyPair kPair = pairGen.generateKeyPair();
	    
	    System.out.println("[+] Initializing...");
	    
	    KeyAgreement newKeyAgree = KeyAgreement.getInstance("DH");
	    newKeyAgree.init(kPair.getPrivate());
	   byte[] newPubKey = kPair.getPublic().getEncoded();
	   
	   System.out.println(newPubKey);
	    
	    
	}
	
    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuffer buf) {
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
    private static String toHexString(byte[] block) {
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
	
	
	//modify below
    private static final byte P_BYTES[] = {
            (byte)0xC4, (byte)0x88, (byte)0xFD, (byte)0x58,
            (byte)0xE9, (byte)0x2F, (byte)0x78, (byte)0xC9
    };
    
    // main used for testing implementation
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException {
    	/*
    	 * create two clients for testing
    	 */
    	DH bob = new DH();
    	DH alice = new DH();
    	/*
    	 * Alice creates key
    	 */
    	//byte[] alicePubKey = alice.generatePublicKey();
    	
    	/*
    	 * bob recieves key
    	 */
    	//bob.generatePublicKey2();
    	//alice.generatePublicKey2();
    	
    	
    	//byte[] aliceShareKey = alice.computeSharedKey(alicePubKey);
    	//System.out.println("Alice key = " + aliceShareKey);
    	//byte[] bobKey = bob.computeSharedKey(alicePubKey);
    	//System.out.println("Bobs key = " + bobKey);
    }

}
