package org.kansus.ocr;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.kansus.ocr.mlp.MLP;
import org.kansus.ocr.mlp.Representacao;

/**
 * Classe central da aplicação.
 * 
 * @author Charles
 */
public class KansusCharacterRecognizer {

	private static final int INICIO_CARACTERES_IMPRIMIVEIS = 33;
	private static final int FIM_CARACTERES_IMPRIMIVEIS = 127;

	private static MLP redeNeural;

	private static String textoReconhecido;

	/**
	 * Inicializa as variáveis da classe.
	 */
	public static void inicializar() {
		textoReconhecido = "";
		redeNeural = new MLP();
	}

	/**
	 * @return rede neural {@link MLP}.
	 */
	public static MLP getRedeNeural() {
		return redeNeural;
	}

	/**
	 * @return texto reconhecido.
	 */
	public static String getTextoReconhecido() {
		return textoReconhecido;
	}

	/**
	 * Adiciona um novo caracter ao texto reconhecido, de acordo com o
	 * reconhecimento da imagem do caracter.
	 * 
	 * @param charImage
	 *            imagem do caracter.
	 */
	public static void novoCaracterExtraido(BufferedImage charImage) {
		char caracter = (char) (redeNeural.reconhecer(PreRede.getRepresentacao(charImage)) + INICIO_CARACTERES_IMPRIMIVEIS);
		textoReconhecido += caracter;
	}

	/**
	 * Adiciona um novo espaço branco ao texto reconhecido.
	 */
	public static void novoEspacoBranco() {
		textoReconhecido += " ";
	}

	/**
	 * Adiciona uma nova quebra de linha ao texto reconhecido.
	 */
	public static void novaQuebraDeLinha() {
		textoReconhecido += System.getProperty("line.separator");
	}

	/**
	 * Efetua o reconhecimento de texto numa imagem.
	 * 
	 * @param inputImage
	 *            imagem a ser analizada.
	 * @param progressListener
	 *            listener de progresso.
	 * @return texto reconhecido.
	 */
	public static String reconhecerImagem(File inputImage, ProgressListener progressListener) {
		textoReconhecido = "";
		Ferramentas.extrairCaracteres(inputImage, progressListener);
		return textoReconhecido;
	}

	/**
	 * Salva um arquivo .txt com o texto reconhecido da imagem.
	 * 
	 * @param path
	 *            caminho do arquivo a ser salvo.
	 */
	public static void salvarTextoEmArquivo(File path) {
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(textoReconhecido);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Efetua o treinamento da redeNeural MLP a partir das imagens em um
	 * diretório e um range de caracteres as representando.
	 * 
	 * @param dir
	 *            diretório onde estão as imagens dos caracteres.
	 * @param start
	 *            caracter de início.
	 * @param end
	 *            caracter de fim.
	 * @param progressListener
	 *            listener de progresso.
	 */
	public static void treinamentoRange(String dir, char start, char end, ProgressListener progressListener) {
		int progresso = 0;
		// validar caracteres de início e fim
		if (start == ' ' || end == ' ') {
			progressListener.onProgressCompleted("Erro: Caracter branco não é um argumento válido.");
			return;
		}
		// validar range de caracteres
		if (start > end) {
			progressListener.onProgressCompleted("Erro: Range de caracteres inválido, o caracter de início deve ser menor que"
					+ " o caracter de fim, de acordo com a tabela ASCII");
			return;
		}
		File directory = new File(dir);
		// validar diretório
		if (!directory.exists()) {
			progressListener.onProgressCompleted("Erro: Diretório inexistente.");
			return;
		}
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				if (arg0.getAbsolutePath().toLowerCase().endsWith("bmp"))
					return true;
				if (arg0.getAbsolutePath().toLowerCase().endsWith("gif"))
					return true;
				if (arg0.getAbsolutePath().toLowerCase().endsWith("jpg"))
					return true;
				if (arg0.getAbsolutePath().toLowerCase().endsWith("png"))
					return true;
				return false;
			}
		};

		File[] imagens = directory.listFiles(filter);
		// detectar se o diretório tem imagens
		if (imagens.length == 0) {
			progressListener.onProgressCompleted("Erro: Nenhuma imagem encontrada no diretório informado.");
			return;
		} else if (end - start + 1 > imagens.length) {
			progressListener
					.onProgressCompleted("Erro: O range de caracteres é maior que a quantidade de imagens no diretório.");
			return;
		} else {
			// treinamento
			int i = 0;
			int totalCaracteres = end - start + 1;
			float relevancia = 0.2f;
			for (int c = start; c <= end; c++, i++) {
				File f = imagens[i];
				BufferedImage img = null;
				try {
					img = ImageIO.read(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Representacao rep = PreRede.getRepresentacao(img);

				int[] saida = new int[FIM_CARACTERES_IMPRIMIVEIS - INICIO_CARACTERES_IMPRIMIVEIS];
				Arrays.fill(saida, -1);
				saida[c - INICIO_CARACTERES_IMPRIMIVEIS] = 1;
				redeNeural.adicionarPadrao(String.valueOf((char) c), rep, saida);
				progresso = (int) (((float) c / (float) totalCaracteres) * 100f * relevancia);
				progressListener.onProgressChanged("Adicionando padrões na rede...", progresso);
			}
			redeNeural.treinar(progressListener, progresso);
		}
	}

	/**
	 * Efetua o treinamento da redeNeural MLP a partir das imagens em um
	 * diretório e um range de caracteres as representando.
	 * 
	 * @param dir
	 *            diretório onde estão as imagens dos caracteres.
	 * @param seqChar
	 *            sequência de caracteres descrevendo os imagens do diretório.
	 * @param progressListener
	 *            listener de progresso.
	 */
	public static void treinamentoSequencia(String dir, String seqChar, ProgressListener progressListener) {
		int progresso = 0;
		char[] chars = seqChar.trim().toCharArray();
		// validar sequência de caracteres
		if (chars.length == 0) {
			progressListener.onProgressCompleted("Erro: Sequência de caracteres vazia.");
			return;
		}
		File directory = new File(dir);
		// validar diretório
		if (!directory.exists()) {
			progressListener.onProgressCompleted("Erro: Diretório inexistente!");
			return;
		}
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				if (arg0.getAbsolutePath().toLowerCase().endsWith("bmp"))
					return true;
				if (arg0.getAbsolutePath().toLowerCase().endsWith("gif"))
					return true;
				if (arg0.getAbsolutePath().toLowerCase().endsWith("jpg"))
					return true;
				if (arg0.getAbsolutePath().toLowerCase().endsWith("png"))
					return true;
				return false;
			}
		};

		File[] imagens = directory.listFiles(filter);
		// verificar se o diretório tem imagens
		if (imagens.length == 0) {
			progressListener.onProgressCompleted("Erro: Nenhuma imagem encontrada no diretório informado.");
			return;
		}
		// verificar se a sequencia de caracteres é menor ou igual à quantidade
		// de imagens no diretório
		else if (chars.length > imagens.length) {
			progressListener
					.onProgressCompleted("Erro: Sequência de caracteres maior do que a quantidade de imagens no diretório.");
			return;
		} else {
			// treinamento.
			int totalCaracteres = chars.length;
			for (int i = 0; i < chars.length; i++) {
				File f = imagens[i];
				BufferedImage img = null;
				try {
					img = ImageIO.read(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Representacao rep = PreRede.getRepresentacao(img);
				int[] saida = new int[FIM_CARACTERES_IMPRIMIVEIS - INICIO_CARACTERES_IMPRIMIVEIS + 1];
				Arrays.fill(saida, -1);
				saida[chars[i] - INICIO_CARACTERES_IMPRIMIVEIS] = 1;
				redeNeural.adicionarPadrao(String.valueOf(chars[i]), rep, saida);
				progressListener.onProgressChanged("Adicionando padrões na rede...", totalCaracteres);
			}
			redeNeural.treinar(progressListener, progresso);
		}
	}
}
