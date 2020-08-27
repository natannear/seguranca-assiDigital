package trabalho;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

public class Unico {
	
	public static KeyPair gerarChavePublicaPrivada() throws NoSuchAlgorithmException, InvalidKeyException {
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); 
		SecureRandom secRan = new SecureRandom();					
		kpg.initialize(512, secRan);								
		KeyPair keyP = kpg.generateKeyPair();						
		
		return keyP;
	}
	
	public static void gerarAssinatura(File arquivo) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
		
		Signature sig = Signature.getInstance("SHA256withRSA");
		
		KeyPair keyP = gerarChavePublicaPrivada();
		PublicKey pubKey = keyP.getPublic();
		PrivateKey priKey = keyP.getPrivate();
		
		sig.initSign(priKey);
		
		try {
			sig.update(Files.readAllBytes(arquivo.toPath()));
			byte[] assinaturaDigital = sig.sign();
			
			String nomeDoArquivoLimpo = tirarExtensao(arquivo.getName());
			
			byte[] key = pubKey.getEncoded();
			Files.write(Paths.get("chave_publica_"+nomeDoArquivoLimpo), key);
			Files.write(Paths.get("assinatura_digital_" + nomeDoArquivoLimpo), assinaturaDigital);
			
			System.out.println("Documento assinado com sucesso.");
			
		} catch (NoSuchFileException e) {
			System.out.println("Arquivo n�o encontrado!");
		}
		
	}
	
	public static void verificarAssinatura(PublicKey pubKey, File arquivo, byte[] assinatura) throws
	   NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
		
		Signature clientSig = Signature.getInstance("SHA256withRSA");
		
		clientSig.initVerify(pubKey);
		
		try {
			clientSig.update(Files.readAllBytes(arquivo.toPath()));
			
			if(clientSig.verify(assinatura)) { // Mensagem corretamente assinada
				System.out.println("O Arquivo recebido foi assinado corretamente.");
				//java.awt.Desktop.getDesktop().open(arquivo);
			}
			else { // Mensagem n�o pode ser validada
				System.out.println("O Arquivo recebido N�O pode ser validado.");
			}
		} catch (NullPointerException | NoSuchFileException e) {
			System.out.println("O Arquivo recebido N�O pode ser validado.");
		}
		
	}
	
	public static String tirarExtensao(String nome) {
		int indice = nome.lastIndexOf(".");
		String nomeDoArquivo = nome.substring(0,indice);
		
		return nomeDoArquivo;
	}
	
	public static PublicKey recuperaChavePublica(byte[] chave) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(chave);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		
		return pubKey;
	}
	
	public static void main(String args[]) throws 
		InvalidKeyException, SignatureException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
						
			Scanner in = new Scanner(System.in);
			System.out.printf("1 - Assinar Documento\n2 - Verificar Assinatura Digital\n0 - Sair\n> Op��o: ");
			int escolha = in.nextInt();
			
			while(escolha != 0) {
				switch(escolha) {
				case 1:
					in = new Scanner(System.in);
					System.out.println("Digite o nome do documento e sua extens�o[Arquivo precisa estar na raiz do Projeto]: ");
					String nomeDoArquivo = in.nextLine();
					File arquivo = new File(nomeDoArquivo);
					
					gerarAssinatura(arquivo);
					
					break;
				case 2:
					in = new Scanner(System.in);
					System.out.println("Digite o nome do documento e sua extens�o[Arquivo precisa estar na raiz do Projeto]: ");
					String nome = in.nextLine();
					
					arquivo = new File(nome);
					nome = tirarExtensao(nome);
					byte[] assinaturaDigital = null;
					
					try {
						assinaturaDigital = Files.readAllBytes(Paths.get("assinatura_digital_" + nome));
					} catch (NoSuchFileException e) {
						System.out.println("Arquivo n�o encontrado!");
						break;
					}
					byte[] key = Files.readAllBytes(Paths.get("chave_publica_"+nome));
					PublicKey chavePublica = recuperaChavePublica(key);
					
					verificarAssinatura(chavePublica, arquivo, assinaturaDigital);
					
					break;
				case 0:
					break;
				default:
					System.out.println("Op��o inv�lida.");
					break;
				}
				in = new Scanner(System.in);
				System.out.printf("\n1 - Assinar Documento\n2 - Verificar Assinatura Digital\n0 - Sair\n> Op��o: ");
				escolha = in.nextInt();
			}
		}
}
