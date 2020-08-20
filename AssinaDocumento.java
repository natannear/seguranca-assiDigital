package trabalho;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class AssinaDocumento {
	
	private PublicKey pubKey;
	
	public PublicKey getPubKey() {
		return pubKey;
	}
	public void setPubKey(PublicKey pubKey) {
		this.pubKey = pubKey;
	}
	
	
	public byte[] geraAssinatura(String mensagem) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("DSA");
		
		GeraChaves gChaves = new GeraChaves();
		KeyPair keyP = gChaves.geraChaves();
		this.pubKey = keyP.getPublic();
		PrivateKey priKey = keyP.getPrivate();
		
		sig.initSign(priKey);
		
		sig.update(mensagem.getBytes());
		byte[] assinatura = sig.sign();
		
		return assinatura;
	}
}
