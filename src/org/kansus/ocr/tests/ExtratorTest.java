package org.kansus.ocr.tests;

import java.io.File;

import org.kansus.ocr.Ferramentas;
import org.kansus.ocr.KansusCharacterRecognizer;
import org.kansus.ocr.ProgressListener;

/**
 * Classe para testes do extrator de caracteres.
 */
public class ExtratorTest implements ProgressListener {

	public static void main(String[] args) {
		ExtratorTest test = new ExtratorTest();
		File inputImage = new File("C:\\Users\\Charles\\Documents\\ascii.png");
		// extrair para a memória.
		Ferramentas.extrairCaracteres(inputImage, test);
		System.out.println(KansusCharacterRecognizer.getTextoReconhecido());

		// extrair em arquivos.
		File path = new File("C:\\Users\\Charles\\Documents\\Testes OCR");
		Ferramentas.extrairCaracteres(inputImage, path, test);

		// tracer
		Ferramentas.destacarCaracteres(inputImage, test);
	}

	@Override
	public void onProgressChanged(String message, int progress) {

	}

	@Override
	public void onProgressCompleted(String message) {

	}
}
