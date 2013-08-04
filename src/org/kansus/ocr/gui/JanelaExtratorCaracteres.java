package org.kansus.ocr.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.kansus.ocr.Ferramentas;
import org.kansus.ocr.Globals;
import org.kansus.ocr.ProgressListener;

/**
 * Janela "Extrator de Caracteres".
 * 
 * @author Charles
 */
public class JanelaExtratorCaracteres extends JFrame implements ProgressListener {

	private static final long serialVersionUID = -2138704694816577360L;

	private final JPanel contentPanel = new JPanel();
	private JTextField tfImagePath;
	private JProgressBar progressBar;

	private Thread extractorThread;

	/**
	 * Thread respons�vel por extrair os caracteres de uma imagem e salv�-los em
	 * um diret�rio especificado.
	 */
	Runnable extratorCaracteres = new Runnable() {
		@Override
		public void run() {
			Ferramentas.extrairCaracteres(Globals.inputImage, new File(tfImagePath.getText()), JanelaExtratorCaracteres.this);
		}
	};

	/**
	 * Create the frame.
	 */
	public JanelaExtratorCaracteres() {
		initialize();
	}

	private void initialize() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaExtratorCaracteres.class.getResource("/res/icon.png")));
		setTitle("Extrator de caracteres");
		setBounds(100, 100, 450, 153);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblImagem = new JLabel("Diret\u00F3rio da Sa\u00EDda");
		lblImagem.setBounds(6, 6, 104, 16);
		contentPanel.add(lblImagem);

		tfImagePath = new JTextField();
		tfImagePath.setBounds(6, 24, 330, 28);
		contentPanel.add(tfImagePath);
		tfImagePath.setColumns(10);

		// Bot�o "Buscar"
		JButton btnPesquisar = new JButton("Buscar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Diret�rio de treinamento");
				chooser.setPreferredSize(new Dimension(700, 450));
				setAlwaysOnTop(false);
				int returnVal = chooser.showOpenDialog(getFrames()[0]);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					tfImagePath.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnPesquisar.setBounds(348, 24, 90, 28);
		contentPanel.add(btnPesquisar);

		// Bot�o "Extrair"
		JButton btnExtrair = new JButton("Extrair");
		btnExtrair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final File outputDir = new File(tfImagePath.getText());
				if (!outputDir.exists()) {
					JOptionPane.showMessageDialog(null, "Diret�rio inexistente.", "Aten��o", JOptionPane.WARNING_MESSAGE);
				} else {
					extractorThread = new Thread(extratorCaracteres);
					extractorThread.start();
				}
			}
		});
		btnExtrair.setBounds(275, 64, 90, 28);
		contentPanel.add(btnExtrair);

		// Bot�o "Parar"
		JButton btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if (extratorCaracteres != null)
					extractorThread.stop();
			}
		});
		btnParar.setBounds(173, 64, 90, 28);
		contentPanel.add(btnParar);

		// Bot�o "Cancelar"
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if (extratorCaracteres != null)
					extractorThread.stop();
				dispose();
			}
		});
		btnCancelar.setBounds(71, 64, 90, 28);
		contentPanel.add(btnCancelar);

		// Barra de progresso
		progressBar = new JProgressBar();
		progressBar.setBounds(6, 100, 432, 19);
		contentPanel.add(progressBar);
	}

	@Override
	public void onProgressChanged(String message, int progress) {
		progressBar.setValue(progress);
		progressBar.repaint();
	}

	@Override
	public void onProgressCompleted(String message) {
		progressBar.setValue(100);
		progressBar.repaint();
		JOptionPane.showMessageDialog(null, message, "Informa��o", JOptionPane.INFORMATION_MESSAGE);
	}
}
