package modelo;
public class NeuronioSaida extends Neuronio {

	@Override
	public void calcularErro() {
		double erro;
		erro = (this.getValorDesejado() - this.getValorObtido()) * (1 - (Math.pow(this.getValorObtido(), 2)));
		this.setErro(erro);
		/*System.out.print ("Neurônio #" + this.getNumero() + " da camada de saída ===> ");
		System.out.print ("VD: " + this.getValorDesejado() + "; ");
		System.out.print ("VO: " + this.getValorObtido() + "; ");
		System.out.println ("E: " + this.getErro());*/
	}

}
