// CharacterExtractor.java
// Copyright (c) 2010 William Whitney
// All rights reserved.
// This software is released under the BSD license.
// Please see the accompanying LICENSE.txt for details.
package org.kansus.ocr.scanner;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.kansus.ocr.Globals;
import org.kansus.ocr.PreRede;
import org.kansus.ocr.ProgressListener;

/**
 * Saves all the characters in an image to an output directory individually.
 * 
 * @author William Whitney
 */
public class CharacterExtractor extends DocumentScannerListenerAdaptor {

	private int num = 0;
	private DocumentScanner documentScanner;
	private File outputDir = null;
	private File inputImage = null;
	private int std_width;
	private int std_height;
	private ProgressListener progressListener;

	public void slice(File inputImage, File outputDir, int std_width, int std_height, ProgressListener progressListener) {
		try {
			this.documentScanner = new DocumentScanner(progressListener);
			this.progressListener = progressListener;
			this.std_width = std_width;
			this.std_height = std_height;
			this.inputImage = inputImage;
			this.outputDir = outputDir;
			Image img = PreRede.threshold(ImageIO.read(inputImage), Globals.threshold);
			PixelImage pixelImage = new PixelImage(img);
			pixelImage.toGrayScale(true);
			pixelImage.filter();
			documentScanner.configureScannerWithGlobalsValues();
			documentScanner.scan(pixelImage, this, 0, 0, pixelImage.width, pixelImage.height);
		} catch (IOException ex) {
			Logger.getLogger(CharacterExtractor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void processChar(PixelImage pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2) {
		try {
			int areaW = x2 - (--x1);
			int areaH = y2 - (--y1);

			// Extract the character
			BufferedImage input = PreRede.threshold(ImageIO.read(inputImage), Globals.threshold);
			BufferedImage characterImage = input.getSubimage(x1, y1, areaW, areaH);

			boolean isLinhaCorreta = false;
			boolean isColunaCorreta = false;
			for (int i = 0; i < characterImage.getWidth(); i++)
				if (characterImage.getRGB(i, 0) == 0x000000 || characterImage.getRGB(i, 0) == 0xFF000000) {
					isColunaCorreta = true;
				}
			for (int i = 0; i < characterImage.getHeight(); i++)
				if (characterImage.getRGB(0, i) == 0x000000 || characterImage.getRGB(0, i) == 0xFF000000) {
					isLinhaCorreta = true;
				}

			if (!isColunaCorreta) {
				y1++;
				areaH--;
			}
			if (!isLinhaCorreta) {
				x1++;
				areaW--;
			}

			if (!isColunaCorreta || !isLinhaCorreta)
				characterImage = input.getSubimage(x1, y1, areaW, areaH);

			// Scale image so that both the height and width are less than std
			// size
			if (characterImage.getWidth() > std_width) {
				// Make image always std_width wide
				double scaleAmount = (double) std_width / (double) characterImage.getWidth();
				AffineTransform tx = new AffineTransform();
				tx.scale(scaleAmount, scaleAmount);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				characterImage = op.filter(characterImage, null);
			}

			if (characterImage.getHeight() > std_height) {
				// Make image always std_height tall
				double scaleAmount = (double) std_height / (double) characterImage.getHeight();
				AffineTransform tx = new AffineTransform();
				tx.scale(scaleAmount, scaleAmount);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				characterImage = op.filter(characterImage, null);
			}

			// Paint the scaled image on a white background
			BufferedImage normalizedImage = new BufferedImage(std_width, std_height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = normalizedImage.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, std_width, std_height);

			// Center scaled image on new canvas
			// int x_offset = (std_width - characterImage.getWidth()) / 2;
			// int y_offset = (std_height - characterImage.getHeight()) / 2;
			int x_offset = 0;
			int y_offset = 0;

			g.drawImage(characterImage, x_offset, y_offset, null);
			g.dispose();

			// Save new image to file
			File outputfile = null;
			if (num < 10) {
				outputfile = new File(outputDir + File.separator + "char_0" + num + ".png");
			} else {
				outputfile = new File(outputDir + File.separator + "char_" + num + ".png");
			}

			PreRede.threshold(normalizedImage, Globals.threshold);
			ImageIO.write(normalizedImage, "png", outputfile);
			num++;
		} catch (Exception ex) {
			Logger.getLogger(CharacterExtractor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void endDocument(PixelImage pixelImage) {
		this.progressListener.onProgressCompleted("Processo de extração de caracteres completado com sucesso.");
	}
}
