package negocio;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import modelo.Representacao;

public class PreRede {

	/**
	 * Efetua a convolu��o de uma imagem.
	 * 
	 * @param image
	 *            imagem a ser convolu�da.
	 * @return imagem convolu�da.
	 */
	public static BufferedImage convolucao(BufferedImage bfImage) {
		BufferedImage image = new BufferedImage(32, 32,
				BufferedImage.TYPE_INT_RGB);
		int cor = 0;

		for (int x = 1; x <= 30; x++) {
			for (int y = 1; y <= 30; y++) {
				cor = (bfImage.getRGB(x - 1, y - 1) & 0xFFFFFF >> 23);
				cor += (bfImage.getRGB(x, y - 1) & 0xFFFFFF >> 23);
				cor += (bfImage.getRGB(x + 1, y - 1) & 0xFFFFFF >> 23);
				cor += (bfImage.getRGB(x - 1, y) & 0xFFFFFF >> 23);
				cor += (bfImage.getRGB(x, y) & 0xFFFFFF >> 23);
				cor += (bfImage.getRGB(x + 1, y) & 0xFFFFFF >> 23);
				cor += (bfImage.getRGB(x - 1, y + 1) & 0xFFFFFF >> 23);
				cor += (bfImage.getRGB(x, y + 1) & 0xFFFFFF >> 23);
				cor += (bfImage.getRGB(x + 1, y + 1) & 0xFFFFFF >> 23);
				cor /= 9;
				if (cor > 0)
					cor = 0xFFFFFF;
				else
					cor = 0x000000;
				image.setRGB(x, y, cor);
			}

			// remover borda preta
			for (int i = 0; i <= 31; i++) {
				image.setRGB(i, 0, 0x00FFFFFF);
				image.setRGB(i, 31, 0x00FFFFFF);
			}
			for (int i = 0; i <= 31; i++) {
				image.setRGB(0, i, 0x00FFFFFF);
				image.setRGB(31, i, 0x00FFFFFF);
			}
		}
		return image;
	}

	/**
	 * Escala uma imagem 32x32 para 16x16.
	 * 
	 * @param bfImage
	 *            imagem a ser escalada.
	 * @return imagem 16x16.
	 */
	public static BufferedImage subAmostragem(BufferedImage bfImage) {
		BufferedImage result = new BufferedImage(8, 8,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x <= 7; x++) {
			for (int y = 0; y <= 7; y++) {
				result.setRGB(x, y, bfImage.getRGB(x * 4, y * 4));
			}
		}
		return result;
	}

	/**
	 * Gera 9 Ocelos de 8x8 deslocados de 4 pixels numa imagem 16x16.
	 * 
	 * @param bfImage
	 *            imagem fonte.
	 * @return vetor de imagens com 9 ocelos extra�dos da imagem fonte.
	 */
	public static BufferedImage[] gerarOcelos(BufferedImage bfImage) {
		BufferedImage[] ocelos = new BufferedImage[9];

		// Inicializar ocelos
		for (int i = 0; i < ocelos.length; i++)
			ocelos[i] = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);

		// Ocelo (1,1)
		for (int x = 0; x <= 7; x++)
			for (int y = 0; y <= 7; y++)
				ocelos[0].setRGB(x, y, bfImage.getRGB(x, y));

		// Ocelo (2,1)
		for (int x = 4; x <= 11; x++)
			for (int y = 0; y <= 7; y++)
				ocelos[1].setRGB(x - 4, y, bfImage.getRGB(x, y));

		// Ocelo (3,1)
		for (int x = 8; x <= 15; x++)
			for (int y = 0; y <= 7; y++)
				ocelos[2].setRGB(x - 8, y, bfImage.getRGB(x, y));

		// Ocelo (1,2)
		for (int x = 0; x <= 7; x++)
			for (int y = 4; y <= 11; y++)
				ocelos[3].setRGB(x, y - 4, bfImage.getRGB(x, y));

		// Ocelo (2,2)
		for (int x = 4; x <= 11; x++)
			for (int y = 4; y <= 11; y++)
				ocelos[4].setRGB(x - 4, y - 4, bfImage.getRGB(x, y));

		// Ocelo (3,2)
		for (int x = 8; x <= 15; x++)
			for (int y = 4; y <= 11; y++)
				ocelos[5].setRGB(x - 8, y - 4, bfImage.getRGB(x, y));

		// Ocelo (1,3)
		for (int x = 0; x <= 7; x++)
			for (int y = 8; y <= 15; y++)
				ocelos[6].setRGB(x, y - 8, bfImage.getRGB(x, y));

		// Ocelo (2,3)
		for (int x = 4; x <= 11; x++)
			for (int y = 8; y <= 15; y++)
				ocelos[7].setRGB(x - 4, y - 8, bfImage.getRGB(x, y));

		// Ocelo (3,3)
		for (int x = 8; x <= 15; x++)
			for (int y = 8; y <= 15; y++)
				ocelos[8].setRGB(x - 8, y - 8, bfImage.getRGB(x, y));

		return ocelos;
	}

	/**
	 * Efetua a mesclagem de v�rios ocelos, resultando em uma �nica imagem.
	 * 
	 * @param ocelos
	 *            ocelos a serem mesclados.
	 * @return uma imagem com os ocelos mesclados.
	 */
	public static BufferedImage mesclarOcelos(BufferedImage[] ocelos) {
		BufferedImage image = new BufferedImage(24, 24,
				BufferedImage.TYPE_INT_RGB);

		// Ocelo (1,1)
		for (int x = 0; x <= 7; x++)
			for (int y = 0; y <= 7; y++)
				image.setRGB(x, y, ocelos[0].getRGB(x, y));

		// Ocelo (2,1)
		for (int x = 8; x <= 15; x++)
			for (int y = 0; y <= 7; y++)
				image.setRGB(x, y, ocelos[1].getRGB(x - 8, y));

		// Ocelo (3,1)
		for (int x = 16; x <= 23; x++)
			for (int y = 0; y <= 7; y++)
				image.setRGB(x, y, ocelos[2].getRGB(x - 16, y));

		// Ocelo (1,2)
		for (int x = 0; x <= 7; x++)
			for (int y = 8; y <= 15; y++)
				image.setRGB(x, y, ocelos[3].getRGB(x, y - 8));

		// Ocelo (2,2)
		for (int x = 8; x <= 15; x++)
			for (int y = 8; y <= 15; y++)
				image.setRGB(x, y, ocelos[4].getRGB(x - 8, y - 8));

		// Ocelo (3,2)
		for (int x = 16; x <= 23; x++)
			for (int y = 8; y <= 15; y++)
				image.setRGB(x, y, ocelos[5].getRGB(x - 16, y - 8));

		// Ocelo (1,3)
		for (int x = 0; x <= 7; x++)
			for (int y = 16; y <= 23; y++)
				image.setRGB(x, y, ocelos[6].getRGB(x, y - 16));

		// Ocelo (2,3)
		for (int x = 8; x <= 15; x++)
			for (int y = 16; y <= 23; y++)
				image.setRGB(x, y, ocelos[7].getRGB(x - 8, y - 16));

		// Ocelo (3,3)
		for (int x = 16; x <= 23; x++)
			for (int y = 16; y <= 23; y++)
				image.setRGB(x, y, ocelos[8].getRGB(x - 16, y - 16));

		return image;
	}

	/**
	 * Gera uma matriz de representa��o de uma imagem que ser� utilizada como
	 * entrada para a rede MLP.
	 * 
	 * @param image
	 *            imagem a ser convertida para matriz.
	 * @return uma matriz representando a imagem.
	 */
	public static int[][] computarMatrizRepresentacao(BufferedImage image) {
		int[][] matrizRepresentacao = new int[image.getWidth()][image.getHeight()];
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++)
				if(image.getRGB(x, y) == 0xFFFFFFFF || image.getRGB(x, y) == 0xFFFFFF)
					matrizRepresentacao[x][y] = -1;
				else
					matrizRepresentacao[x][y] = 1;

		return matrizRepresentacao;
	}

	/**
	 * Cria um objeto <code>Representacao</code> a partir de uma imagem com um
	 * caracter. Este objeto <code>Representacao</code> servir� como entrada
	 * para a rede MLP.
	 * 
	 * @param bfImage
	 *            imagem com um caracter.
	 * @return um objeto <code>Representacao</code> referente � imagem com um
	 *         caracter.
	 */
	public static Representacao getRepresentacao(BufferedImage bfImage) {
		BufferedImage img = threshold(bfImage, 200);
		//img = convolucao(bfImage);
		//img = subAmostragem(img);
		//BufferedImage[] ocelos = gerarOcelos(img);
		//img = mesclarOcelos(ocelos);
		File file;
		try {
			int x = 0;
			while(true) {
				file = new File("D:\\img_" + x + ".png");
				if(!file.exists())
					break;
				x++;
			}
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Representacao(computarMatrizRepresentacao(img));
	}
	
	public static int[][] getInputs(BufferedImage image) {
		BufferedImage img = threshold(image, 200);
		//img = convolucao(image);
		//img = subAmostragem(img);
		//BufferedImage[] ocelos = gerarOcelos(img);
		//img = mesclarOcelos(ocelos);
		int[][] matriz = computarMatrizRepresentacao(img);
		File file;
		try {
			int x = 0;
			while(true) {
				file = new File("D:\\img_" + x + ".png");
				if(!file.exists())
					break;
				x++;
			}
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matriz;
	}

	/**
	 * Efetua a binariza��o de uma imagem, ou seja, os p�xels que tiverem m�dia
	 * menor que o limiar, passam a ser pretos, os que tiverem m�dia maior que o
	 * limiar, passam a ser brancos.
	 * 
	 * @param image imagem a ser binarizada.
	 * @param limiar limiar usado para binariza��o.
	 * @return
	 */
	public static BufferedImage threshold(BufferedImage image, int limiar) {
		int width = image.getWidth();
		int height = image.getHeight();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				int r = (int) ((rgb & 0x00FF0000) >>> 16);
				int g = (int) ((rgb & 0x0000FF00) >>> 8);
				int b = (int) (rgb & 0x000000FF);
				int media = (r + g + b) / 3;

				if (media < limiar)
					image.setRGB(i, j, 0x000000);
				else
					image.setRGB(i, j, 0xFFFFFF);
			}
		}
		return image;
	}
}
