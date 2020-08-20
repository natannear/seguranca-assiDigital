package trabalho;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

public class AssinaturaDigital {
	
	public static void main(String args[]) throws NoSuchAlgorithmException,
	InvalidKeyException, SignatureException {
		
		AssinaDocumento assinaDocumento = new AssinaDocumento();
		String mensagem = "Testando o novo algoritmo.";
		byte[] assinatura = assinaDocumento.geraAssinatura(mensagem);
		PublicKey pubKey = assinaDocumento.getPubKey();
		
		
		VerificaAssinatura verificaAssinatura = new VerificaAssinatura();
		verificaAssinatura.recebeMensagem(pubKey, mensagem, assinatura);
		
		
		String msgAlterada = "Mensagem diferente da primeira, testando novo algoritmo.";
		verificaAssinatura.recebeMensagem(pubKey, msgAlterada, assinatura);
		
		
		verificaAssinatura.recebeMensagem(pubKey, mensagem, assinatura);
	}
}
