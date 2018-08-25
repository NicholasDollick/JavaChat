import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class DH {
	private static final String TAG = "DH";
	private static KeyPair keyPair;
	private static KeyAgreement keyAgree;
	public final static int pValue = 41;
	public final static int gValue = 83;
	
	public static byte[] generatePublicKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
		BigInteger P = BigInteger.probablePrime(512, new SecureRandom());
		BigInteger G = BigInteger.probablePrime(512, new SecureRandom());
		
		DHParameterSpec dhParamSpec = new DHParameterSpec(P, G);
		
		System.out.println("P = " + P.toString(16));
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DiffieHellman");
		keyGen.initialize(dhParamSpec); //consider securerandom arg
		keyPair = keyGen.generateKeyPair();
		
		System.out.println("Y = " + ((DHPublicKey) keyPair.getPublic()).getY().toString(16));
		
		keyAgree = KeyAgreement.getInstance("DiffieHellman");
		keyAgree.init(keyPair.getPrivate());
		
		BigInteger pubKeyBI = ((DHPublicKey) keyPair.getPublic()).getY();
		byte[] pubKey = pubKeyBI.toByteArray();
		
		System.out.println(TAG + " " + String.format("Y [%d] = %s", pubKey.length, pubKey.toString().hashCode()));
		System.out.println("Public Key = " + pubKey);
		
		return pubKey;
	}
	
	public byte[] compareSharedKey(byte[] pubKey) {
		
		
		return null;
	}
	
	
	//modify below
    private static final byte P_BYTES[] = {
            (byte)0xC4, (byte)0x88, (byte)0xFD, (byte)0x58,
            (byte)0xE9, (byte)0x2F, (byte)0x78, (byte)0xC9
    };
    
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
    	DH.generatePublicKey();
    }

}
