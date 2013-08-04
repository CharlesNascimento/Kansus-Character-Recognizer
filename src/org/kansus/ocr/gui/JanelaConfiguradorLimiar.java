package org.kansus.ocr.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.kansus.ocr.Globals;
import org.kansus.ocr.PreRede;

import javax.swing.JCheckBox;

/**
 * Janela "Configurador de Limiar".
 * 
 * @author Charles
 */
public class JanelaConfiguradorLimiar extends JDialog {

	private static final long serialVersionUID = 3678793283449379294L;
	private JPanel contentPane;

	private BufferedImage input;

	private JLabel valor_lb;
	private JSlider slider;

	private JanelaPrincipal janelaPrincipal;

	/**
	 * Create the frame.
	 */
	public JanelaConfiguradorLimiar(JanelaPrincipal janelaChamadora) {
		this.janelaPrincipal = janelaChamadora;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				janelaPrincipal.setImageViewImage(input);
			}
		});
		initialize();
		try {
			input = ImageIO.read(Globals.inputImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		valor_lb.setText(String.valueOf(Globals.threshold));
		slider.setValue(Globals.threshold);

		atualizarImagemJanelaPrincipal();
	}

	private void initialize() {
		setAlwaysOnTop(true);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaConfiguradorLimiar.class.getResource("/res/icon.png")));
		setTitle("Configurador de Limiar");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 164);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		valor_lb = new JLabel("Valor");
		valor_lb.setHorizontalAlignment(SwingConstants.CENTER);
		valor_lb.setBounds(174, 102, 46, 14);
		contentPane.add(valor_lb);

		slider = new JSlider();
		slider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				valor_lb.setText(String.valueOf(slider.getValue()));
			};
		});
		slider.setBounds(10, 29, 374, 31);
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				atualizarImagemJanelaPrincipal();
			}
		});
		contentPane.setLayout(null);

		JLabel lblDeslizeABarra = new JLabel("Deslize a barra para selecionar o limiar desejado.");
		lblDeslizeABarra.setBounds(10, 11, 374, 14);
		contentPane.add(lblDeslizeABarra);
		slider.setValue(200);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setMaximum(255);
		contentPane.add(slider);

		final JCheckBox chckbxAplicarNaImagem = new JCheckBox("Aplicar diretamente na imagem");
		chckbxAplicarNaImagem.setToolTipText("Aplica o limiar diretamente no arquivo da imagem de entrada.");
		chckbxAplicarNaImagem.setBounds(10, 67, 374, 23);
		contentPane.add(chckbxAplicarNaImagem);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Globals.threshold = slider.getValue();
				if (chckbxAplicarNaImagem.isSelected()) {
					// antes de salvar a imagem com limiar, fazemos um backup da
					// original
					String orinalPath = Globals.inputImage.getAbsolutePath();
					File rename = new File(Globals.inputImage.getAbsolutePath().replace(Globals.inputImageFormat, "")
							+ "[backup]" + Globals.inputImageFormat);
					Globals.inputImage.renameTo(rename);
					try {
						ImageIO.write(PreRede.threshold(input, Globals.threshold), "png", new File(orinalPath));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				janelaPrincipal.setImageViewImage(input);
				dispose();
			}
		});
		btnSalvar.setBounds(294, 94, 90, 30);
		contentPane.add(btnSalvar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				janelaPrincipal.setImageViewImage(input);
				dispose();
			}
		});
		btnCancelar.setBounds(10, 97, 90, 30);
		contentPane.add(btnCancelar);
	}

	/**
	 * Mostra a imagem com limiar no visualizador de imagem da janela principal.
	 */
	public void atualizarImagemJanelaPrincipal() {
		BufferedImage limiarImg = PreRede.threshold(input, slider.getValue());
		janelaPrincipal.setImageViewImage(limiarImg);
		try {
			input = ImageIO.read(Globals.inputImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
