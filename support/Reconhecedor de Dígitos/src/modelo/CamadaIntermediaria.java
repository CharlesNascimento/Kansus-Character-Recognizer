package modelo;


public class CamadaIntermediaria extends Camada {

	public CamadaIntermediaria(String nome, int numeroNeuronios, int numeroEntradas) {
		super(nome, numeroNeuronios, numeroEntradas);
	}

	private Camada camadaSaida = null;

	
	public void setCamadaSaida(Camada camadaSaida) {
		this.camadaSaida = camadaSaida;
	}

	public Camada getCamadaSaida() {
		return camadaSaida;
	}

	@Override
	public void inicializaCamada(int numeroNeuronios, int numeroEntradas) {
		this.getNeuronios().clear();
		for (int i = 0; i < numeroNeuronios; i++) {
			this.adicionaNeuronio(new NeuronioIntermediario(), numeroEntradas);
		}
		
	}

	
}
