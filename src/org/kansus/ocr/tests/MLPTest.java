package org.kansus.ocr.tests;

import java.io.File;

import javax.imageio.ImageIO;

import org.kansus.ocr.KansusCharacterRecognizer;
import org.kansus.ocr.PreRede;
import org.kansus.ocr.ProgressListener;

/**
 * Classe para testes da MLP.
 */
public class MLPTest implements ProgressListener {

	public static void main(String[] args) {
		try {
			MLPTest mlpTest = new MLPTest();
			KansusCharacterRecognizer.inicializar();
			KansusCharacterRecognizer.treinamentoRange("C:\\Users\\Charles\\Documents\\Testes OCR", '!', '~', mlpTest);
			int resultado = KansusCharacterRecognizer.getRedeNeural().reconhecer(
					PreRede.getRepresentacao(ImageIO.read(new File("C:\\Users\\Charles\\Documents\\Testes OCR\\char_60.png"))));
			System.out.println((char) resultado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProgressChanged(String message, int progress) {

	}

	@Override
	public void onProgressCompleted(String message) {

	}
}
