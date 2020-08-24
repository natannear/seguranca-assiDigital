package trabalho;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Scanner;

public class AssinaturaDigital {
	
	public static void main(String args[]) throws NoSuchAlgorithmException,
	InvalidKeyException, SignatureException, IOException {
		Scanner input = new Scanner(System.in); String resp;
		AssinaDocumento assinaDocumento = new AssinaDocumento();
		String mensagem = LerArquivo();
		
		byte[] assinatura = assinaDocumento.geraAssinatura(mensagem);
		PublicKey pubKey = assinaDocumento.getPubKey();
		
		VerificaAssinatura verificaAssinatura = new VerificaAssinatura();
		System.out.println("assinatura: "+ assinatura+"\n"+"Conteudo da mensagem: "+mensagem);
		verificaAssinatura.recebeMensagem(pubKey, mensagem, assinatura);
		
		System.out.println("\nAlterar mensagem? (s/n):");
		resp = input.nextLine();
		if(resp.equalsIgnoreCase("s")) {
			mensagem = alteracaoMaliciosa();
		}
		
		System.out.println("Verificando novamente...");
		verificaAssinatura.recebeMensagem(pubKey, mensagem, assinatura);
		System.out.println("\nassinatura: "+ assinatura+"\n"+"Conteudo da mensagem: "+mensagem);
		
		input.close();
	}

	private static String LerArquivo() throws IOException {
		FileInputStream entrada = new FileInputStream("file.txt");
		InputStreamReader entradaFormatada = new InputStreamReader(entrada);
		BufferedReader entradaString = new BufferedReader(entradaFormatada);
		String mensagem = ""; String aux = "";

		while (aux != null) {
			aux = entradaString.readLine();
			if (aux != null) {
				mensagem += aux+"\n";
			} 
		}
		entradaString.close();
		return mensagem;
	}
	
	
	private static String alteracaoMaliciosa() throws IOException {
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter("file.txt"));
		String linha = "";
		Scanner in = new Scanner(System.in);
		System.out.println("Escreva algo: ");
		linha = in.nextLine();
		buffWrite.append(linha + "\n");
		buffWrite.close();
		in.close();
		return linha;
	}
}
