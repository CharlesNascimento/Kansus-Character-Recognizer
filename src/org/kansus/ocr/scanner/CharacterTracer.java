// CharacterTracer.java
// Copyright (c) 2010 William Whitney
// All rights reserved.
// This software is released under the BSD license.
// Please see the accompanying LICENSE.txt for details.

package org.kansus.ocr.scanner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
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
public class CharacterTracer extends DocumentScannerListenerAdaptor {

	private DocumentScanner documentScanner;
	private BufferedImage bfImage;
	private Graphics2D bfImageGraphics;
	private ProgressListener progressListener;

	public BufferedImage getTracedImage(File inputImage, ProgressListener progressListener) {
		try {
			this.documentScanner = new DocumentScanner(progressListener);
			this.progressListener = progressListener;
			bfImage = PreRede.threshold(ImageIO.read(inputImage), Globals.threshold);
			bfImageGraphics = bfImage.createGraphics();

			Image img = PreRede.threshold(ImageIO.read(inputImage), Globals.threshold);
			PixelImage pixelImage = new PixelImage(img);
			pixelImage.toGrayScale(true);
			pixelImage.filter();
			documentScanner.configureScannerWithGlobalsValues();
			documentScanner.scan(pixelImage, this, 0, 0, pixelImage.width, pixelImage.height);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
		bfImageGraphics.dispose();

		return bfImage;
	}

	@Override
	public void processChar(PixelImage pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2) {
		try {
			x1--;
			y1--;
			bfImageGraphics.setStroke(new BasicStroke(1));
			bfImageGraphics.setColor(Color.BLUE);
			bfImageGraphics.drawRect(x1, y1, x2 - x1, y2 - y1);
		} catch (Exception ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void processSpace(PixelImage pixelImage, int x1, int y1, int x2, int y2) {
		try {
			bfImageGraphics.setStroke(new BasicStroke(1));
			bfImageGraphics.setColor(Color.GREEN);
			bfImageGraphics.drawRect(x1, y1, x2 - x1, y2 - y1);
		} catch (Exception ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void endDocument(PixelImage pixelImage) {
		this.progressListener.onProgressCompleted(null);
	}

	private static final Logger LOG = Logger.getLogger(CharacterTracer.class.getName());
}
