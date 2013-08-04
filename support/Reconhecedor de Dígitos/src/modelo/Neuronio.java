package modelo;
import java.util.Random;


public abstract class Neuronio {
	
	private int numero;

	private static Random gerador = new Random();

	private Camada camada;

	private double[] terminaisEntrada;

	private double[] pesos;

	private double valorDesejado;

	private double valorObtido;
	
	private double erro;
	
	public void inicializaNeuronio(int numeroEntradas) {
		this.terminaisEntrada = new double[numeroEntradas];
		this.pesos = new double[numeroEntradas];
	}

	public double getErro() {
		return this.erro;
	}

	public Camada getCamada() {
		return camada;
	}

	public void setCamada(Camada camada) {
		this.camada = camada;
	}

	public double getValorDesejado() {
		return valorDesejado;
	}

	public void setValorDesejado(double valorDesejado) {
		this.valorDesejado = valorDesejado;
	}

	public double getValorObtido() {
		return this.valorObtido;
	}

	public void setTerminaisEntrada(double[] valoresEntrada) {
		this.terminaisEntrada = valoresEntrada;
	}

	public void setPesos(double[] valoresPesos) {
		this.pesos = valoresPesos;
	}

	public void fazerSinapse() {
		/*
		 * Primeiro, faz-se o somatório do produto entre terminais de entrada e
		 * seus respectivos pesos.
		 */
		double somatorio = 0;
		for (int i = 0; i < this.terminaisEntrada.length; i++) {
			somatorio += (this.terminaisEntrada[i] * this.pesos[i]);
		}
		//System.out.println("SOMATORIO: " + somatorio);
		/*
		 * Depois, utiliza-se uma função de ativação para dar o valor obtido na
		 * sinapse. A função utilizada nesse caso é: ( 1 - e**-2x) f(x) =
		 * ------------- ( 1 + e**-2x)
		 */
		this.setValorObtido(this.funcaoAtivacao(somatorio / (this.camada.getNumeroNeuronios() * 2)));
		//System.out.println("VALOR OBTIDO: " + this.getValorObtido());
	}

	private double funcaoAtivacao(double x) {
		return ((1 - Math.exp(-2 * x)) / (1 + Math.exp(-2 * x)));
	}

	public void gerarPesosIniciais() {
		for (int j = 0; j < this.pesos.length; j++) {
			this.pesos[j] = gerador.nextDouble();
		}
	}

	protected void setValorObtido(double valorObtido) {
		this.valorObtido = valorObtido;
	}

	public double[] getTerminaisEntrada() {
		return terminaisEntrada;
	}

	public double[] getPesos() {
		return pesos;
	}

	public void setTerminalEntrada(int indice, double terminalEntrada) {
		this.terminaisEntrada[indice] = terminalEntrada;
	}

	public double getTerminalEntrada(int indice) {
		return this.terminaisEntrada[indice];
	}

	public void ajustarPesos(double taxaDeAprendizado) {
		int i;
		double[] novosPesos = new double[this.getTerminaisEntrada().length];

		for (i = 0; i < this.getTerminaisEntrada().length; i++) {
			novosPesos[i] = this.getPesos()[i]
					+ (taxaDeAprendizado * this.getErro() * this.getTerminaisEntrada()[i]);
			/*System.out.println ("Erro: " + this.getErro() + "; Terminal de Entrada: " + this.terminaisEntrada[j]);
			System.out.println ("Peso #" + j + " ===> valor anterior: " 
					+ this.pesos[j] + "; valor atual: " + novosPesos[j] + ".");*/
		}

		this.setPesos(novosPesos);
	}
	
	public abstract void calcularErro();

	public final void setErro(double erro) {
		this.erro = erro;
	}

	public final int getNumero() {
		return numero;
	}

	public final void setNumero(int numero) {
		this.numero = numero;
	}

}
