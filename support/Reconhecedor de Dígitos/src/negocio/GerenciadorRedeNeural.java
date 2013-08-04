package negocio;
public interface GerenciadorRedeNeural {

	public void adicionarPadrao (String elemento, int[][] matrizRepresentacao, int saida);
	
	public void adicionarPadroesDefault();

	public String reconhecer(int[][] matrizRepresentacao);
	
	public String treinar();
	
	public void removerPadroes();

	public String fazerTreinamentoDefault ();
	
	public String realizarTesteDefault();
	
	public void gravarPesosArquivo (String nomeArquivo);
	
	public void carregarPesosArquivo (String nomeArquivo);
	
	public void setTaxaDeAprendizado (double taxaDeAprendizado);
	
	public void setNumeroMaximoCiclos (int numeroMaximoCiclos);
	
	public void setErroMinimo (double erroMinimo);
	
	public void setNomeCamadaEntrada (String nomeCamada);
	
	public void setNomeCamadaIntermediaria (String nomeCamada);
	
	public void setNomeCamadaSaida (String nomeCamada);
	
	public String setNumeroNeuroniosCamadaIntermediaria (int numeroNeuronios);
	
	public String getNomeCamadaEntrada();
	
	public int getNumeroNeuroniosCamadaEntrada();
	
	public String getNomeCamadaIntermediaria();
	
	public int getNumeroNeuroniosCamadaIntermediaria();
	
	public String getNomeCamadaSaida();
	
	public int getNumeroNeuroniosCamadaSaida();
	
	public String getPesosCamadaIntermediaria();
	
	public String getPesosCamadaSaida();
	
	public String getPadroes();
	
	public double getTaxaDeAprendizado ();
	
	public int getNumeroMaximoCiclos ();
	
	public double getErroMinimo ();
	
	
}