package modelo;

public class NeuronioEntrada extends Neuronio {

	@Override
	public void fazerSinapse() {
		this.setValorObtido(this.getTerminalEntrada(0));
	}

	@Override
	public void calcularErro() {
		// TODO Auto-generated method stub
		
	}


}
