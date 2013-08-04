package org.kansus.ocr.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.kansus.ocr.Globals;
import org.kansus.ocr.PreRede;

/**
 * Classe para testes da Pré rede.
 */
public class PreRedeTest {

	public static void main(String[] args) throws IOException {
		BufferedImage in = ImageIO.read(new File("c:\\char1.png"));

		// Binarização
		BufferedImage bin = PreRede.threshold(in, Globals.threshold);
		File out = new File("c:\\bin.png");
		out.createNewFile();
		ImageIO.write(bin, "png", out);

		/*// Gerar ocelos
		BufferedImage[] ocelos = PreRede.gerarOcelos(bin);
		for (int i = 0; i < ocelos.length; i++) {
			System.out.println("Criando ocelo " + i);
			File f = new File("C:\\ocelo_" + i + ".png");
			ImageIO.write(ocelos[i], "png", f);
		}

		// Mesclar ocelos
		BufferedImage mesclagem = PreRede.mesclarOcelos(ocelos);
		File mesc = new File("c:\\mesc.png");
		ImageIO.write(mesclagem, "png", mesc);*/

		// Computar matriz de representação
		int[][] matrizRepresentacao = PreRede
				.computarMatrizRepresentacao(bin);
		String matrix = "";
		for (int x = 0; x <= 23; x++) {
			matrix += "\n";
			for (int y = 0; y <= 23; y++)
				matrix += matrizRepresentacao[y][x] + " ";
		}
		System.out.println(matrix + "\n");

		//Gerar a representação de uma imagem inicial.
		System.out.println(PreRede.getRepresentacao(in).toString());
	}
}
