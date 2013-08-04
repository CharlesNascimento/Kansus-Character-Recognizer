package modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Camada {

	private List<Neuronio> neuronios = new ArrayList<Neuronio>();

	private String nome;
	
	public Camada (String nome, int numeroNeuronios, int numeroEntradas) {
		System.out.print("Adicionando camada " + nome + " com " + numeroNeuronios + " neuronios e " +
				numeroEntradas + " entradas...");
		this.nome = nome;
		this.inicializaCamada(numeroNeuronios, numeroEntradas);
		System.out.println(" [OK]");
	}
	
	public abstract void inicializaCamada (int numeroNeuronios, int numeroEntradas);

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getNumeroNeuronios() {
		return this.neuronios.size();
	}

	public List<Neuronio> getNeuronios() {
		return neuronios;
	}

	public void adicionaNeuronio(Neuronio novoNeuronio, int numeroEntradas) {
		novoNeuronio.inicializaNeuronio(numeroEntradas);
		novoNeuronio.setCamada(this);
		this.neuronios.add(novoNeuronio);
		novoNeuronio.setNumero(this.getNumeroNeuronios()-1);
	}
	
	public String getListaDePesosNeuronios() {
		String listaPesos = "";
		for (Neuronio n : this.neuronios) {
			for (int i=0; i < n.getPesos().length; i++) {
				listaPesos += n.getPesos()[i] + ";";
			}
		}
		return listaPesos;
	}

}
