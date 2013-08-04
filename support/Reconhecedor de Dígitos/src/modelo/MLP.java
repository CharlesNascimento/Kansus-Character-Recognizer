package modelo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MLP implements TreinamentoPadrao {
	
	private double taxaDeAprendizado = 0.05;

	private double erroMinimo = 0.01;

	private int numeroMaximoCiclos = 100000;

	private ArrayList<Padrao> padroes = new ArrayList<Padrao>();
	
	private Camada camadaEntrada = new CamadaEntrada ("C0", 576, 1);
	
	private Camada camadaIntermediaria = new CamadaIntermediaria ("C1", 256, this.camadaEntrada.getNumeroNeuronios());
	
	private Camada camadaSaida = new CamadaSaida ("C2", 10, this.camadaIntermediaria.getNumeroNeuronios());
	
	private Map<String, Camada> camadas = new HashMap<String, Camada>();
	
	public MLP() {
		this.inicializarMCP();
	}

	private void inicializarMCP() {
		
		this.camadas.put(this.camadaEntrada.getNome(), this.camadaEntrada);
		this.camadas.put(this.camadaIntermediaria.getNome(), this.camadaIntermediaria);
		this.camadas.put(this.camadaSaida.getNome(), this.camadaSaida);
		
		((CamadaIntermediaria)this.camadaIntermediaria).setCamadaSaida(camadaSaida);
		
	}

	public String treinar() {
		return this.backpropagation();
	}

	public String fazerTreinamentoDefault() {

		this.removerPadroes();

		this.adicionarPadroesDefault();

		return this.treinar();
	}
	
	public void adicionarPadroesDefault() {
		int e = 0;
		for (Representacao r : MLP.REPRESENTACOES) {
			this.adicionarPadrao(MLP.ELEMENTOS[e], r, MLP.SAIDAS[e]);
			e++;
		}
	}

	private String backpropagation() {
		/* Algoritmo de Back-Propagation */

		/*
		 * 1. Inicializar pesos e parâmetros;
		 */
		String resultado = "";
		int numeroCiclos = 1;
		this.inicializarPesos();
		double somaErroMedioQuadratico;
		double mediaErroMedioQuadratico;
		DecimalFormat formatoPercentagem = new DecimalFormat ("###0.00%");

		/*
		 * 2. Repita até o erro ser mínimo ou a realização de um dado número de
		 * ciclos: 2.1. Para cada padrão de treinamento X: 2.1.1. Definir saída
		 * da rede através de fase forward; 2.1.2. Comparar saídas produzidas
		 * com as saídas desejadas; 2.1.3. Atualizar pesos dos nodos através da
		 * fase backward.
		 */
		long inicio = System.currentTimeMillis();
		do {
			somaErroMedioQuadratico = 0;
			for (Padrao p : this.padroes) {
				this.configuraValoresDesejados(p);
				this.forward(p.getRepresentacao());
				this.calcularErros();
				this.backward();
				somaErroMedioQuadratico += this.getErroMedioQuadratico();
			}
			mediaErroMedioQuadratico = somaErroMedioQuadratico / this.padroes.size();
			numeroCiclos++;
			if ((numeroCiclos % 1000) == 0) {
				resultado += "Ciclo #" + numeroCiclos 
						+ "; Media EMQ: " + formatoPercentagem.format(mediaErroMedioQuadratico) + "\n";
				System.out.println("Erro: " + mediaErroMedioQuadratico + " | " + "Época atual: " + numeroCiclos);
			}
		} while ((this.erroMinimo < mediaErroMedioQuadratico)
				&& (numeroCiclos < this.numeroMaximoCiclos));
		long tempoPercorrido = System.currentTimeMillis() - inicio;
		resultado += "Ciclo #" + numeroCiclos 
				+ "; Media EMQ: " + formatoPercentagem.format(mediaErroMedioQuadratico) +
				"; Duracao: " + (tempoPercorrido/1000F) + " segundos.";
		return resultado;
	}

	private void configuraValoresDesejados(Padrao p) {
		Camada c = this.camadaSaida;
		int i = 0;
		for (Neuronio n : c.getNeuronios()) {
			n.setValorDesejado(p.getSaida()[i]);
			i++;
		}
	}

	private void forward(Representacao r) {
		/* fase forward */

		/*
		 * 1. A entrada é apresentada à camada de rede (camada C0); 2. Para cada
		 * camada Ci a partir da camada de entrada: 2.1. Após os nodos da camada
		 * Ci (i>0) calcularem seus sinais de saída, estes servem como entrada
		 * para a definição das saídas produzidas pelos nodos da camada C(i+1);
		 * 3. As saídas produzidas pelos nodos da última camada são comparadas
		 * às saídas desejadas.
		 */

		int[] entradas = r.getValoresEmSerie();

		int i;
		Camada camadaAtual;
		Camada camadaAnterior = null;

		for (String nomeCamada : this.camadas.keySet()) {
			camadaAtual = this.camadas.get(nomeCamada);
			i = 0;
			if (camadaAtual instanceof CamadaEntrada) {
				for (Neuronio n : camadaAtual.getNeuronios()) {
					n.setTerminalEntrada(0, entradas[i++]);
					n.fazerSinapse();
				}
				
			} else {
				for (Neuronio nAtual : camadaAtual.getNeuronios()) {
					for (Neuronio nAnterior : camadaAnterior.getNeuronios()) {
						nAtual.setTerminalEntrada(i++, nAnterior.getValorObtido());
					}
					i = 0;
					nAtual.fazerSinapse();
				}
			}
			camadaAnterior = camadaAtual;
		}
	}
	
	private void calcularErros() {
		Object[] chaves = this.camadas.keySet().toArray();
		int tamanhoChaves = chaves.length;
		Camada camadaAtual;
		for (int i = (tamanhoChaves - 1); i > 0; i--) {
			camadaAtual = this.camadas.get(chaves[i]);
			for (Neuronio n : camadaAtual.getNeuronios()) {
				n.calcularErro();
			}
		}
	}

	private void backward() {
		/* fase backward */

		/*
		 * 1. A partir da última camada, até chegar na camada de entrada: 1.1.
		 * Os nodos da camada atual ajustam seus pesos de forma a reduzir seus
		 * erros; 1.2. O erro de um nodo das camadas intermediárias é calculado
		 * utilizando os erros dos nodos da camada seguinte conectadas a ele,
		 * ponderados pelos pesos das conexões entre eles.
		 */

		Object[] chaves = this.camadas.keySet().toArray();
		int tamanhoChaves = chaves.length;

		Camada camadaAtual;
		for (int i = (tamanhoChaves - 1); i > 0; i--) {
			camadaAtual = this.camadas.get(chaves[i]);
			for (Neuronio n : camadaAtual.getNeuronios()) {
				n.ajustarPesos(this.taxaDeAprendizado);
			}
		}

	}

	/*
	 * Esse método inicializa pesos nas camadas intermediárias e de saída da
	 * rede com valores randômicos.
	 */
	private void inicializarPesos() {
		for (String elemento : this.camadas.keySet()) {
			Camada c = this.camadas.get(elemento);
			if (!(c instanceof CamadaEntrada)) {
				for (Neuronio n : c.getNeuronios()) {
					n.gerarPesosIniciais();
				}
			}
		}
	}

	public String reconhecer(Representacao representacao) {
		int[] resultadoObtido = null;
		this.forward(representacao);
		Camada c = this.camadaSaida;
		resultadoObtido = ((CamadaSaida) c).getResultadoObtido();
		String resultadoFracionario = ((CamadaSaida) c).getResultadoFracionario();
		String resultado = "";
		
		int j = 0;

		for (int i = 0; i < resultadoObtido.length; i++) {
			if (resultadoObtido[i] == 1) {
				j++;
				if (j == 1)
					resultado += "Reconhecido como ";
				else
					resultado += " ou ";
				resultado += i;
			}
		}
		switch (j) {
		case 0:
			return ("Nenhum numero foi reconhecido " + resultadoFracionario);
		case 1:
			return (resultado + resultadoFracionario + "." );
		default:
			return (resultado + " [Ambiguidade] " + resultadoFracionario);
		}
	}

	public void adicionarPadrao(String elemento, Representacao representacao, int[] saida) {
		Padrao p = new Padrao(elemento, representacao, saida);
		this.padroes.add(p);
	}

	public void removerPadroes() {
		this.padroes.clear();
	}

	private double getErroMedioQuadratico () {
		return ((CamadaSaida)this.camadaSaida).getErroMedioQuadratico();
	}

	public final double getTaxaDeAprendizado() {
		return taxaDeAprendizado;
	}
	
	public void gravarPesos (String nomeArquivo) {
		FileOutputStream fstream;
		DataOutputStream saida;

		try {
			fstream = new FileOutputStream(nomeArquivo);

			saida = new DataOutputStream(fstream);

			for (String elemento : this.camadas.keySet()) {
				Camada c = this.camadas.get(elemento);
				if (!(c instanceof CamadaEntrada)) {
					for (Neuronio n : c.getNeuronios()) {
						for (int i=0; i < n.getPesos().length; i++) {
							saida.writeDouble(n.getPesos()[i]);
							saida.writeChar(';');
						}
						saida.writeChar('\n');
					}
				}
			}
			saida.close();
		} catch (Exception e) {
			System.err.println("Erro escrevendo para arquivo.");
		}
	}
	
	public void inicializarPesos (String nomeArquivo) {
		FileInputStream fstream;
		DataInputStream entrada;
		
		try {
			fstream = new FileInputStream(nomeArquivo);

			entrada = new DataInputStream(fstream);

			while (entrada.available() != 0) {
				for (String elemento : this.camadas.keySet()) {
					Camada c = this.camadas.get(elemento);
					if (!(c instanceof CamadaEntrada)) {
						for (Neuronio n : c.getNeuronios()) {
							double novosPesos[] = new double[n.getPesos().length];
							for (int i=0; i < n.getPesos().length; i++) {
								novosPesos[i] = entrada.readDouble();					
								entrada.readChar();
							}
							entrada.readChar();
							n.setPesos(novosPesos);
						}
					}
				}
			}
			entrada.close();
		} catch (Exception e) {
			System.err.println("Erro lendo do arquivo.");
		}
	}

	public final void setErroMinimo(double erroMinimo) {
		this.erroMinimo = erroMinimo;
	}

	public final void setNumeroMaximoCiclos(int numeroMaximoCiclos) {
		this.numeroMaximoCiclos = numeroMaximoCiclos;
	}

	public final void setTaxaDeAprendizado(double taxaDeAprendizado) {
		this.taxaDeAprendizado = taxaDeAprendizado;
	}

	public final Camada getCamadaEntrada() {
		return camadaEntrada;
	}

	public final Camada getCamadaIntermediaria() {
		return camadaIntermediaria;
	}

	public final Camada getCamadaSaida() {
		return camadaSaida;
	}
	
	public String redefinirNeuroniosCamadaIntermediaria(int novoNumeroNeuronios) {
		this.camadaIntermediaria.inicializaCamada(novoNumeroNeuronios, this.camadaEntrada.getNumeroNeuronios());
		this.camadaSaida.inicializaCamada(this.getCamadaSaida().getNumeroNeuronios(), novoNumeroNeuronios);
		return this.fazerTreinamentoDefault();
	}
	
	public String getPadroes() {
		String padroes = "";
		for (Padrao p : this.padroes) {
			padroes += p.getElemento() + '\n';
		}
		return padroes;
	}

	public final double getErroMinimo() {
		return erroMinimo;
	}

	public final int getNumeroMaximoCiclos() {
		return numeroMaximoCiclos;
	}
	
}
