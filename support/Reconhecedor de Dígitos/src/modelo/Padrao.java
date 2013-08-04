package modelo;
public class Padrao {

	private String elemento;

	private Representacao representacao;
	
	private int[] saida;
	
	public Padrao (String e, Representacao r, int[] s) {
		this.elemento = e;
		this.representacao = r;
		this.saida = s;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	public Representacao getRepresentacao() {
		return representacao;
	}

	public void setRepresentacao(Representacao representacao) {
		this.representacao = representacao;
	}

	public int[] getSaida() {
		return saida;
	}

	public void setSaida(int[] saida) {
		this.saida = saida;
	}

}
