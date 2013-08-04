package modelo;


import java.text.DecimalFormat;


public class CamadaSaida extends Camada {

	public CamadaSaida(String nome, int numeroNeuronios, int numeroEntradas) {
		super(nome, numeroNeuronios, numeroEntradas);
	}

	@Override
	public void inicializaCamada(int numeroNeuronios, int numeroEntradas) {
		this.getNeuronios().clear();
		for (int i = 0; i < numeroNeuronios; i++) {
			this.adicionaNeuronio(new NeuronioSaida(), numeroEntradas);
		}
		
	}
	
	public double getSomatorioErroPonderado (int j) {
		double somatorio = 0;
		for (Neuronio n : this.getNeuronios()) {
			somatorio += n.getErro() * n.getPesos()[j];
		}
		return somatorio;
	}
	
	public double getErroMedioQuadratico() {
		double emq = 0;
		double diferencial;
		for (Neuronio n : this.getNeuronios()) {
			diferencial = n.getValorDesejado() - n.getValorObtido();
			emq += Math.pow(diferencial, 2);
		}
		emq = 0.5 * emq;
		return emq;
	}
	
	public int[] getResultadoObtido () {
		int[] resultadoObtido = new int[this.getNumeroNeuronios()];
		int i = 0;
		for (Neuronio n : this.getNeuronios()) {
			if (n.getValorObtido() > 0)
				resultadoObtido[i++] = 1;
			else
				resultadoObtido[i++] = -1;
		}
		return resultadoObtido;
	}
	
	public String getResultadoFracionario() {
		String resultadoFracionario = " ( ";
		DecimalFormat formatoFracionario = new DecimalFormat ("#.##");
		for (Neuronio n : this.getNeuronios()) {
			resultadoFracionario += formatoFracionario.format(n.getValorObtido()) + "; ";
		}
		resultadoFracionario += ")";
		return resultadoFracionario;
	}
}
