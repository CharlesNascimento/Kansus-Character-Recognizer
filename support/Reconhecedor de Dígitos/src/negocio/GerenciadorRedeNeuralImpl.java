package negocio;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import modelo.MLP;
import modelo.Representacao;

public class GerenciadorRedeNeuralImpl implements GerenciadorRedeNeural {
	
	private MLP redeNeural;
	
	public static BufferedImage zero;
	public static BufferedImage um;
	public static BufferedImage dois;
	public static BufferedImage tres;
	public static BufferedImage quatro;
	public static BufferedImage cinco;
	public static BufferedImage seis;
	public static BufferedImage sete;
	public static BufferedImage oito;
	public static BufferedImage nove;
	
	public GerenciadorRedeNeuralImpl () {
		try {
			zero = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_00.png"));
			um = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_01.png"));
			dois = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_02.png"));
			tres = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_03.png"));
			quatro = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_04.png"));
			cinco = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_05.png"));
			seis = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_06.png"));
			sete = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_07.png"));
			oito = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_08.png"));
			nove = ImageIO.read(new File("C:\\Users\\Charles\\Documents\\zChars\\char_09.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Criando uma nova rede MLP...");
		this.redeNeural = new MLP();
		System.out.println("Rede MLP criada.\n");	
	}

	/* (non-Javadoc)
	 * @see GerenciadorRedeNeural#treinar(java.lang.String, Representacao)
	 */
	public String treinar() {
		return this.redeNeural.treinar();
	}
	
	/* (non-Javadoc)
	 * @see GerenciadorRedeNeural#reconhecer(Representacao)
	 */
	public String reconhecer (int[][] matrizRepresentacao) {
		Representacao representacao = new Representacao (matrizRepresentacao);
		return this.redeNeural.reconhecer(representacao);
	}
	
	public String fazerTreinamentoDefault () {
		return this.redeNeural.fazerTreinamentoDefault();
	}

	public void carregarPesosArquivo (String nomeArquivo) {
		this.redeNeural.inicializarPesos(nomeArquivo);
	}
	
	public void gravarPesosArquivo (String nomeArquivo) {
		this.redeNeural.gravarPesos(nomeArquivo);
	}
	
	public void setTaxaDeAprendizado (double taxaDeAprendizado) {
		this.redeNeural.setTaxaDeAprendizado(taxaDeAprendizado);
	}
	
	public void setNumeroMaximoCiclos (int numeroMaximoCiclos) {
		this.redeNeural.setNumeroMaximoCiclos(numeroMaximoCiclos);
	}
	
	public void setErroMinimo (double erroMinimo) {
		this.redeNeural.setErroMinimo(erroMinimo);
	}

	public void setNomeCamadaEntrada(String nomeCamada) {
		this.redeNeural.getCamadaEntrada().setNome(nomeCamada);
	}

	public void setNomeCamadaIntermediaria(String nomeCamada) {
		this.redeNeural.getCamadaIntermediaria().setNome(nomeCamada);
	}

	public void setNomeCamadaSaida(String nomeCamada) {
		this.redeNeural.getCamadaSaida().setNome(nomeCamada);
	}

	public String setNumeroNeuroniosCamadaIntermediaria(int numeroNeuronios) {
		return this.redeNeural.redefinirNeuroniosCamadaIntermediaria(numeroNeuronios);
	}
	
	public String getNomeCamadaEntrada() {
		return this.redeNeural.getCamadaEntrada().getNome();
	}
	
	public int getNumeroNeuroniosCamadaEntrada() {
		return this.redeNeural.getCamadaEntrada().getNumeroNeuronios();
	}
	
	public String getNomeCamadaIntermediaria() {
		return this.redeNeural.getCamadaIntermediaria().getNome();
	}
	
	public int getNumeroNeuroniosCamadaIntermediaria() {
		return this.redeNeural.getCamadaIntermediaria().getNumeroNeuronios();
	}
	
	public String getNomeCamadaSaida() {
		return this.redeNeural.getCamadaSaida().getNome();
	}
	
	public int getNumeroNeuroniosCamadaSaida() {
		return this.redeNeural.getCamadaSaida().getNumeroNeuronios();
	}
	
	public String getPesosCamadaIntermediaria() {
		return this.redeNeural.getCamadaIntermediaria().getListaDePesosNeuronios();
	}
	
	public String getPesosCamadaSaida() {
		return this.redeNeural.getCamadaSaida().getListaDePesosNeuronios();
	}

	public void adicionarPadrao(String elemento, int[][] matrizRepresentacao, int saida) {
		Representacao representacao = new Representacao (matrizRepresentacao);
		int[] vetorSaida = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
		vetorSaida[saida] = 1;
		this.redeNeural.adicionarPadrao(elemento, representacao, vetorSaida);	
		this.redeNeural.treinar();
	}

	public void adicionarPadroesDefault() {
		this.redeNeural.adicionarPadroesDefault();		
	}

	/*public String realizarTesteDefault() {
		
		String resultado = "";
		
		int[][] zero = { {-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1}, 
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1, 1,-1,-1,-1, 1,-1},
				 {-1,-1, 1,-1,-1,-1, 1,-1},
				 {-1,-1, 1,-1,-1,-1, 1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1,-1,-1}};

		int[][] um = { {-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1},
				   {-1,-1,-1,-1, 1,-1,-1,-1},
				   {-1,-1,-1,-1, 1,-1,-1,-1},
				   {-1,-1,-1,-1, 1,-1,-1,-1},
				   {-1,-1,-1,-1, 1,-1,-1,-1},
				   {-1,-1,-1,-1, 1,-1,-1,-1},
				   {-1,-1,-1,-1,-1,-1,-1,-1}};

		int[][] dois = { {-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1, 1,-1}, 
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1, 1,-1,-1,-1,-1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1,-1,-1}};

		int[][] tres = { {-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1}, 
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1, 1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1, 1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1,-1,-1}};

		int[][] quatroModificado = { { 1, -1, -1, -1, 1 }, { 1, -1, -1, -1, 1 },
		 { 1, 1, 1, 1, 1 }, { -1, -1, -1, -1, 1 }, { -1, -1, -1, -1, 1 } };

		int[][] quatro = { {-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1},
				   {-1,-1,-1,-1, 1, 1,-1,-1},
				   {-1,-1,-1, 1,-1, 1,-1,-1},
				   {-1,-1, 1, 1, 1, 1, 1,-1},
				   {-1,-1,-1,-1,-1, 1,-1,-1},
				   {-1,-1,-1,-1,-1, 1,-1,-1},
				   {-1,-1,-1,-1,-1,-1,-1,-1}};

		int[][] cinco = { {-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1}, 
				  {-1,-1, 1, 1, 1, 1, 1,-1},
				  {-1,-1, 1,-1,-1,-1,-1,-1},
				  {-1,-1, 1, 1, 1, 1, 1,-1},
				  {-1,-1,-1,-1,-1,-1, 1,-1},
				  {-1,-1, 1, 1, 1, 1, 1,-1},
				  {-1,-1,-1,-1,-1,-1,-1,-1}};

		int[][] seis = { {-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1}, 
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1, 1,-1,-1,-1,-1,-1}, 
				 {-1,-1, 1, 1, 1, 1, 1,-1}, 
				 {-1,-1, 1,-1,-1,-1, 1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1,-1,-1}};

		int[][] sete = { 
				{-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1},
				 {-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1, 1,-1,-1},
				 {-1,-1,-1, 1,-1,-1,-1},
				 {-1,-1, 1,-1,-1,-1,-1},
				 {-1, 1,-1,-1,-1,-1,-1},
				 {-1,-1,-1,-1,-1,-1,-1}};

		int[][] oito = { 
				{-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1, 1,-1,-1,-1, 1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1, 1,-1,-1,-1, 1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1,-1,-1}};

		int[][] nove = { 
				 {-1,-1,-1,-1,-1,-1,-1,-1},
				 {-1,-1,-1,-1,-1,-1,-1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1, 1,-1,-1,-1, 1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1, 1,-1},
				 {-1,-1, 1, 1, 1, 1, 1,-1},
				 {-1,-1,-1,-1,-1,-1,-1,-1}};
		
		resultado += "\nIniciando teste default...\n";

		resultado += this.testeReconhecimento(zero, "zero");
		resultado += this.testeReconhecimento(um, "um");
		resultado += this.testeReconhecimento(dois, "dois");
		resultado += this.testeReconhecimento(tres, "tres");
		resultado += this.testeReconhecimento(quatro, "quatro");
		resultado += this.testeReconhecimento(cinco, "cinco");
		resultado += this.testeReconhecimento(seis, "seis");
		resultado += this.testeReconhecimento(sete, "sete");
		resultado += this.testeReconhecimento(oito, "oito");
		resultado += this.testeReconhecimento(nove, "nove");
		
		return resultado;
	}*/
	
public String realizarTesteDefault() {
		
		String resultado = "";
		
		int[][] matrizZero = PreRede.getInputs(zero);

		int[][] matrizUm = PreRede.getInputs(um);

		int[][] matrizDois = PreRede.getInputs(dois);

		int[][] matrizTres = PreRede.getInputs(tres);

		int[][] matrizQuatro = PreRede.getInputs(quatro);

		int[][] matrizCinco = PreRede.getInputs(cinco);

		int[][] matrizSeis = PreRede.getInputs(seis);

		int[][] matrizSete = PreRede.getInputs(sete);

		int[][] matrizOito = PreRede.getInputs(oito);

		int[][] matrizNove = PreRede.getInputs(nove);
		
		resultado += "\nIniciando teste default...\n";

		resultado += this.testeReconhecimento(matrizZero, "zero");
		resultado += this.testeReconhecimento(matrizUm, "um");
		resultado += this.testeReconhecimento(matrizDois, "dois");
		resultado += this.testeReconhecimento(matrizTres, "tres");
		resultado += this.testeReconhecimento(matrizQuatro, "quatro");
		resultado += this.testeReconhecimento(matrizCinco, "cinco");
		resultado += this.testeReconhecimento(matrizSeis, "seis");
		resultado += this.testeReconhecimento(matrizSete, "sete");
		resultado += this.testeReconhecimento(matrizOito, "oito");
		resultado += this.testeReconhecimento(matrizNove, "nove");
		
		return resultado;
	}
	
	private String testeReconhecimento (int[][] matriz, String nomePadrao) {
		String resultadoTeste = "";
		resultadoTeste += "Entrando padrao de numero " + nomePadrao + "... ";
		resultadoTeste += this.reconhecer(matriz) + '\n';
		return resultadoTeste;
	}

	public void removerPadroes() {
		this.redeNeural.removerPadroes();
	}

	public String getPadroes() {
		return this.redeNeural.getPadroes();
	}

	public double getErroMinimo() {
		return this.redeNeural.getErroMinimo();
	}

	public int getNumeroMaximoCiclos() {
		return this.redeNeural.getNumeroMaximoCiclos();
	}

	public double getTaxaDeAprendizado() {
		return this.redeNeural.getTaxaDeAprendizado();
	}
	
}
