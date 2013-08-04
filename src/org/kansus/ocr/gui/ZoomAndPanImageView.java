package org.kansus.ocr.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Componente gráfico com a característica de mostrar uma imagem e permitir que
 * esta receba zoom e seja movimentada.
 * 
 * @author Charles
 */
public class ZoomAndPanImageView extends Component {

	private static final long serialVersionUID = -2501114402279185537L;
	private boolean init = true;
	private BufferedImage img;

	private ZoomAndPanListener zoomAndPanListener;

	public ZoomAndPanImageView(BufferedImage img) {
		this.img = img;
		this.zoomAndPanListener = new ZoomAndPanListener(this);
		this.addMouseListener(zoomAndPanListener);
		this.addMouseMotionListener(zoomAndPanListener);
		this.addMouseWheelListener(zoomAndPanListener);
	}

	public ZoomAndPanImageView(int minZoomLevel, int maxZoomLevel, double zoomMultiplicationFactor) {
		this.zoomAndPanListener = new ZoomAndPanListener(this, minZoomLevel, maxZoomLevel, zoomMultiplicationFactor);
		this.addMouseListener(zoomAndPanListener);
		this.addMouseMotionListener(zoomAndPanListener);
		this.addMouseWheelListener(zoomAndPanListener);
	}

	public Dimension getPreferredSize() {
		return new Dimension(600, 500);
	}

	public void setImage(BufferedImage img) {
		this.img = img;
	}

	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		if (init) {
			init = false;
			Dimension d = getSize();
			int xc = (d.width / 2) / 2;
			int yc = (d.height / 2) / 2;
			g.translate(xc, yc);
			zoomAndPanListener.setCoordTransform(g.getTransform());
		} else {
			g.setTransform(zoomAndPanListener.getCoordTransform());
		}
		g.drawImage(img, 0, 0, this);
	}
}
