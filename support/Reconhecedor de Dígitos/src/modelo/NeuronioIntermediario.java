package modelo;


public class NeuronioIntermediario extends Neuronio {

	@Override
	public void calcularErro() {
		double erro;
		CamadaIntermediaria camadaIntermediaria = (CamadaIntermediaria) this.getCamada();
		CamadaSaida camadaSaida = (CamadaSaida) camadaIntermediaria.getCamadaSaida();
		erro = camadaSaida.getSomatorioErroPonderado(this.getNumero()) * (1 - (Math.pow(this.getValorObtido(), 2)));
		this.setErro(erro);
		/*System.out.print ("Neur�nio #" + this.getNumero() + " da camada intermedi�ria ===> ");
		System.out.print ("VO: " + this.getValorObtido() + "; ");
		System.out.println ("E: " + this.getErro());*/
	}

}
