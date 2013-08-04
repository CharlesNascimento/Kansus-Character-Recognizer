package org.kansus.ocr;

import java.awt.image.BufferedImage;
import java.io.File;

import org.kansus.ocr.scanner.CharacterExtractor;
import org.kansus.ocr.scanner.CharacterTracer;
import org.kansus.ocr.scanner.TextExtractor;

/**
 * Classe com métodos auxiliares da aplicação.
 * 
 * @author Charles
 */
public class Ferramentas {

	/**
	 * Extrai os caracteres de uma imagem e salva cada um deles em um arquivo
	 * diferente.
	 * 
	 * @param path
	 *            diretório onde os caracteres vão ser salvos.
	 * @param inputImage
	 *            imagem a ser escaneada.
	 */
	public static void extrairCaracteres(File inputImage, File path, ProgressListener progressListener) {
		CharacterExtractor slicer = new CharacterExtractor();
		slicer.slice(inputImage, path, 24, 24, progressListener);
	}

	/**
	 * Extrai os caracteres de uma imagem para a memória.
	 * 
	 * @param inputImage
	 *            imagem a ser escaneada.
	 */
	public static void extrairCaracteres(File inputImage, ProgressListener progressListener) {
		TextExtractor slicer = new TextExtractor();
		slicer.slice(inputImage, 24, 24, progressListener);
	}

	/**
	 * Destaca os caracteres numa imagem.
	 * 
	 * @param inputImage
	 *            imagem.
	 * @param progressListener
	 *            listener de progresso.
	 * @return imagem traçada.
	 */
	public static BufferedImage destacarCaracteres(File inputImage, ProgressListener progressListener) {
		CharacterTracer slicer = new CharacterTracer();
		return slicer.getTracedImage(inputImage, progressListener);
	}
}
