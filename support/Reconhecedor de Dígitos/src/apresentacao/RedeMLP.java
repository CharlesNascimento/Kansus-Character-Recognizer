package apresentacao;

import negocio.GerenciadorRedeNeural;
import negocio.GerenciadorRedeNeuralImpl;

public class RedeMLP {

	private GerenciadorRedeNeural gerenciadorRedeNeural = new GerenciadorRedeNeuralImpl();

	public static void main(String[] args) {
		//if (args.length > 0) {

			RedeMLP redeMLP = new RedeMLP();
			redeMLP.fazerTreinamentoDefault();
			redeMLP.testeDefault();
	}
			
			/*if (args[0].equals("/t")) {
				 se argumento for '/t', fazer treinamento default 
				redeMLP.fazerTreinamentoDefault();
				if (args.length > 1) {
					if (args[1].length() > 0) {
						redeMLP.gravarPesosArquivo(args[1]);
					}
				} else
					redeMLP.gravarPesosArquivo("pesos-redeneural.mlp");
			}

			else if (args[0].equals("/p")) {
				 se argumento for '/p', realiza teste default 
				if (args.length > 1) {
					if (args[1].length() > 0) {
						redeMLP.carregarPesosArquivo(args[1]);
					}
				} else
					redeMLP.carregarPesosArquivo("pesos-redeneural.mlp");
				redeMLP.testeDefault();*/
			//}
		/*} else {
			System.out
					.println("Rede Neural MLP - Reconhecimento de numeros de 0 a 9 - Versao 0.1");
			System.out.println();
			System.out
					.println("**************************************************************************");
			System.out.println("Modo de usar:");
			System.out.println();
			System.out
					.println("Para fazer treinamento default e guardar pesos em <nome-arquivo>:");
			System.out.println("	java -jar rede-mlp.jar /t <nome-arquivo>");
			System.out
					.println("Para realizar teste default a partir de pesos carregados de <nome-arquivo>:");
			System.out.println("	java -jar rede-mlp.jar /p <nome-arquivo>");
			System.out.println();
			System.out.println("UCSal - 2007.2 => Inteligencia Artificial");
			System.out
					.println("**************************************************************************");
		}
	}*/

	public void fazerTreinamentoDefault() {
		System.out
				.println("Iniciando treinamento default na rede com numeros de 0 a 9...");
		System.out.println(gerenciadorRedeNeural.fazerTreinamentoDefault());
		System.out.println("Treinamento default realizado.");
	}

	public void carregarPesosArquivo(String nomeArquivo) {
		gerenciadorRedeNeural.carregarPesosArquivo(nomeArquivo);
	}

	public void gravarPesosArquivo(String nomeArquivo) {
		gerenciadorRedeNeural.gravarPesosArquivo(nomeArquivo);
	}

	public void testeDefault() {
		System.out.println(gerenciadorRedeNeural.realizarTesteDefault());
	}

	public void redefinirCamadaIntermediaria(int numeroNeuronios) {
		System.out
				.println("Iniciando treinamento default na rede com numeros de 0 a 9...");
		System.out.println(gerenciadorRedeNeural
				.setNumeroNeuroniosCamadaIntermediaria(numeroNeuronios));
		System.out.println("Treinamento default realizado.");
	}

}
