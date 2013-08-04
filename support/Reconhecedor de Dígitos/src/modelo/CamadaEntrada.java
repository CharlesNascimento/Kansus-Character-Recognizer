package modelo;


public class CamadaEntrada extends Camada {

	public CamadaEntrada(String nome, int numeroNeuronios, int numeroEntradas) {
		super(nome, numeroNeuronios, numeroEntradas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inicializaCamada(int numeroNeuronios, int numeroEntradas) {
		for (int i = 0; i < numeroNeuronios; i++) {
			this.adicionaNeuronio(new NeuronioEntrada(), numeroEntradas);
		}
	}

}
