package trabalho;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GeraChaves {
	
	public KeyPair geraChaves() throws NoSuchAlgorithmException, InvalidKeyException {
		// Geração das chaves públicas e privadas
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA"); // Gera as chaves
		SecureRandom secRan = new SecureRandom();					// Obter um número aleatório
		kpg.initialize(512, secRan);								// Inicializa as chaves
		KeyPair keyP = kpg.generateKeyPair();						// Váriavel criada para extrair as chaves pública e privada da outra variavel
		
		return keyP;
	}
}
