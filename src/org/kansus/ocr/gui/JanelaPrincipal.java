package org.kansus.ocr.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.kansus.ocr.Globals;
import org.kansus.ocr.KansusCharacterRecognizer;
import org.kansus.ocr.PreRede;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;

/**
 * Janela principal da aplicação.
 * 
 * @author Charles
 */
public class JanelaPrincipal extends JFrame {

	private static final long serialVersionUID = -1186208458000243218L;

	private JPanel contentPane;
	private ZoomAndPanImageView imageView;
	private JSplitPane splitPane;
	private JMenu mnFerramentas;
	private JMenuItem mntmTextoInteiro;
	private JTextArea textArea;
	private ProgressDialog recognizeProgressDialog;

	/**
	 * Thread responsável pelo reconhecimento da imagem.
	 */
	private Runnable recognizer = new Runnable() {

		@Override
		public void run() {
			String textoReconhecido = KansusCharacterRecognizer.reconhecerImagem(Globals.inputImage, recognizeProgressDialog);
			setTextAreaText(textoReconhecido);
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaPrincipal frame = new JanelaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JanelaPrincipal() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				JanelaPrincipal.this.splitPane.setDividerLocation((int) (JanelaPrincipal.this.getHeight() * 0.7));
			}
		});
		initialize();
		// inicializamos a rede neural
		KansusCharacterRecognizer.inicializar();
		setTheme("Night");
	}

	private void initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaPrincipal.class.getResource("/res/icon.png")));
		setTitle("Kansus Character Recognizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 662, 512);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Menu "Arquivo"
		JMenu mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);

		// Botão "Abrir..." do menu "Arquivo"
		JMenuItem mntmAbrir = new JMenuItem("Abrir...");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "bmp", "jpg", "gif");
				chooser.setFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setDialogTitle("Abrir imagem...");
				chooser.setPreferredSize(new Dimension(700, 450));

				int returnVal = chooser.showOpenDialog(JanelaPrincipal.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						// definimos a imagem selecionada pelo usuário como a
						// imagem de entrada.
						Globals.inputImage = chooser.getSelectedFile();
						setImageViewImage(ImageIO.read(Globals.inputImage));
						int inicioFormato = Globals.inputImage.getAbsolutePath().lastIndexOf(".");
						Globals.inputImageFormat = Globals.inputImage.getAbsolutePath().substring(inicioFormato,
								Globals.inputImage.getAbsolutePath().length());

						mnFerramentas.setEnabled(true);
						mntmTextoInteiro.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					chooser.setVisible(false);
				}
			}
		});
		mntmAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnArquivo.add(mntmAbrir);

		// Botão "Salvar" do menu "Arquivo"
		JMenuItem mntmSalvar = new JMenuItem("Salvar");
		mntmSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				File f = new File("*.txt");
				chooser.setSelectedFile(f);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
				chooser.setFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setDialogTitle("Salvar texto reconhecido");
				chooser.setPreferredSize(new Dimension(700, 450));

				int returnVal = chooser.showOpenDialog(JanelaPrincipal.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						KansusCharacterRecognizer.salvarTextoEmArquivo(chooser.getSelectedFile());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					chooser.setVisible(false);
				}
			}
		});
		mntmSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnArquivo.add(mntmSalvar);

		// Botão "Sair" do menu "Arquivo"
		JMenuItem mntmSair = new JMenuItem("Sair");
		mntmSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// fechamos o programa
				System.exit(0);
			}
		});

		JSeparator separator = new JSeparator();
		mnArquivo.add(separator);
		mntmSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnArquivo.add(mntmSair);

		// Menu "MLP"
		JMenu mnMlp = new JMenu("MLP");
		menuBar.add(mnMlp);

		// Botão "Treinamento" do menu MLP
		JMenuItem mntmTreinamento = new JMenuItem("Treinamento");
		mntmTreinamento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// abrimos a janela "Treinamento"
				JanelaTreinamento jt = new JanelaTreinamento();
				jt.setLocationRelativeTo(null);
				jt.setVisible(true);
			}
		});
		mntmTreinamento.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		mnMlp.add(mntmTreinamento);

		// Botão "Remover padrões" do menu MLP
		JMenuItem mntmRemoverPadres = new JMenuItem("Remover padr\u00F5es");
		mntmRemoverPadres.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		mntmRemoverPadres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int opt = JOptionPane.showConfirmDialog(getParent(),
						"Tem certeza que deseja remover todos os padrões da rede MLP?", "Confirmação",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (opt == JOptionPane.YES_OPTION) {
					// removemos todos os padrões da rede
					KansusCharacterRecognizer.getRedeNeural().removerPadroes();
					JOptionPane.showMessageDialog(getParent(), "Todos os padrões da rede neural foram removidos com sucesso.",
							"Informação", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		JMenu mnReconhecimento = new JMenu("Reconhecimento");
		mnMlp.add(mnReconhecimento);

		mntmTextoInteiro = new JMenuItem("Texto inteiro");
		mntmTextoInteiro.setEnabled(false);
		mntmTextoInteiro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recognizeProgressDialog = new ProgressDialog("Reconhecimento", recognizer);
				recognizeProgressDialog.setLocationRelativeTo(null);
				recognizeProgressDialog.setVisible(true);
			}
		});
		mntmTextoInteiro.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mnReconhecimento.add(mntmTextoInteiro);

		JMenuItem mntmCaracterIndividual = new JMenuItem("Caracter individual");
		mntmCaracterIndividual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "bmp", "jpg", "gif", "tiff");
				chooser.setFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setDialogTitle("Abrir imagem de caractere...");
				chooser.setPreferredSize(new Dimension(700, 450));

				int returnVal = chooser.showOpenDialog(getFrames()[0]);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					int result = 0;
					try {
						result = KansusCharacterRecognizer.getRedeNeural().reconhecer(
								PreRede.getRepresentacao(ImageIO.read(chooser.getSelectedFile())));
					} catch (IOException e) {
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(getParent(), "Reconhecido como: " + (char) result);
				} else {
					chooser.setVisible(false);
				}
			}
		});
		mntmCaracterIndividual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK
				| InputEvent.SHIFT_MASK));
		mnReconhecimento.add(mntmCaracterIndividual);
		mnMlp.add(mntmRemoverPadres);

		JSeparator separator_2 = new JSeparator();
		mnMlp.add(separator_2);

		// Botão "Carregar" do menu MLP
		JMenuItem mntmCarregar = new JMenuItem("Carregar");
		mntmCarregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de conhecimento", "kcr");
				chooser.setFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setDialogTitle("Abrir arquivo de conhecimento...");
				chooser.setPreferredSize(new Dimension(700, 450));

				int returnVal = chooser.showOpenDialog(getFrames()[0]);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// carregamos os pesos do arquivo e jogamos ele na rede
					KansusCharacterRecognizer.getRedeNeural().carregarPesosArquivo(chooser.getSelectedFile().getAbsolutePath());
					System.out.println(KansusCharacterRecognizer.getRedeNeural().getPadroes());
				} else {
					chooser.setVisible(false);
				}
			}
		});
		mntmCarregar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnMlp.add(mntmCarregar);

		// Botão "Salvar" do menu "MLP"
		JMenuItem mntmGravar = new JMenuItem("Salvar");
		mntmGravar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				File f = new File("*.kcr");
				chooser.setSelectedFile(f);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de conhecimento (*.kcr)", "kcr");
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("Salvar arquivo de conhecimento");
				chooser.setPreferredSize(new Dimension(700, 450));

				int response = chooser.showSaveDialog(getFrames()[0]);
				if (response == JFileChooser.APPROVE_OPTION) {
					// salvamos o arquivo com os pesos da rede
					KansusCharacterRecognizer.getRedeNeural().salvarPesosArquivo(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		mntmGravar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnMlp.add(mntmGravar);

		JSeparator separator_1 = new JSeparator();
		mnMlp.add(separator_1);

		// Botão "Configurações" do menu "MLP"
		JMenuItem mntmConfigurao = new JMenuItem("Configura\u00E7\u00F5es");
		mntmConfigurao.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmConfigurao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JanelaConfiguracaoRede jcr = new JanelaConfiguracaoRede();
				jcr.setLocationRelativeTo(null);
				jcr.setVisible(true);
			}
		});
		mnMlp.add(mntmConfigurao);

		// Menu "Ferramentas"
		mnFerramentas = new JMenu("Ferramentas");
		mnFerramentas.setEnabled(false);
		menuBar.add(mnFerramentas);

		// Botão "Extrator de caracteres" do menu Ferramentas
		JMenuItem mntmExtratorDeCaracteres = new JMenuItem("Extrator de caracteres");
		mntmExtratorDeCaracteres.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mntmExtratorDeCaracteres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JanelaExtratorCaracteres je = new JanelaExtratorCaracteres();
				je.setLocationRelativeTo(null);
				je.setVisible(true);
			}
		});
		mnFerramentas.add(mntmExtratorDeCaracteres);

		// Botão "Configurador de Limiar" do menu Ferramentas
		JMenuItem mntmConfiguradorDeLimiar = new JMenuItem("Configurador de Limiar");
		mntmConfiguradorDeLimiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JanelaConfiguradorLimiar jcl = new JanelaConfiguradorLimiar(JanelaPrincipal.this);
				jcl.setLocationRelativeTo(null);
				jcl.setVisible(true);
			}
		});

		// Botão "Configurador do Extrator" do menu Ferramentas
		JMenuItem mntmConfiguradorDoExtrator = new JMenuItem("Configurador do Extrator");
		mntmConfiguradorDoExtrator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JanelaConfiguradorExtrator ce = new JanelaConfiguradorExtrator(JanelaPrincipal.this);
				ce.setLocationRelativeTo(null);
				ce.setVisible(true);
			}
		});
		mntmConfiguradorDoExtrator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
		mnFerramentas.add(mntmConfiguradorDoExtrator);
		mntmConfiguradorDeLimiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mnFerramentas.add(mntmConfiguradorDeLimiar);

		// Menu "Janela"
		JMenu mnJanela = new JMenu("Janela");
		menuBar.add(mnJanela);

		// Menu "Tema" do menu "Janela"
		JMenu mnTema = new JMenu("Tema");
		mnJanela.add(mnTema);

		// Botão "Dark Grey - Green" do menu "Tema" do menu "Janela"
		JMenuItem mntmDarkGreyGreen = new JMenuItem("Dark Grey - Green");
		mntmDarkGreyGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setTheme("Dark Grey - Green");
			}
		});

		// Botão "Default" do menu "Tema" do menu "Janela"
		JMenuItem mntmDefault = new JMenuItem("Default");
		mntmDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					SwingUtilities.updateComponentTreeUI(JanelaPrincipal.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mnTema.add(mntmDefault);
		mnTema.add(mntmDarkGreyGreen);

		// Botão "Kansus" do menu "Tema" do menu "Janela"
		JMenuItem mntmKansus = new JMenuItem("Kansus");
		mntmKansus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setTheme("Kansus");
			}
		});

		// Botão "Dark Grey - Orange" do menu "Tema" do menu "Janela"
		JMenuItem mntmDarkGreyOrange = new JMenuItem("Dark Grey - Orange");
		mntmDarkGreyOrange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setTheme("Dark Grey - Orange");
			}
		});
		mnTema.add(mntmDarkGreyOrange);
		mnTema.add(mntmKansus);

		// Botão "Night" do menu "Tema" do menu "Janela"
		JMenuItem mntmNight = new JMenuItem("Night");
		mntmNight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setTheme("Night");
			}
		});
		mnTema.add(mntmNight);

		// Botão "Pink" do menu "Tema" do menu "Janela"
		JMenuItem mntmPink = new JMenuItem("Pink");
		mntmPink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setTheme("Pink");
			}
		});
		mnTema.add(mntmPink);

		// Botão "Snow" do menu "Tema" do menu "Janela"
		JMenuItem mntmSnow = new JMenuItem("Snow");
		mntmSnow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setTheme("Snow");
			}
		});
		mnTema.add(mntmSnow);

		// Menu "Ajuda"
		JMenu mnAjuda = new JMenu("Ajuda");
		menuBar.add(mnAjuda);

		// Botão "Ajuda" do menu "Ajuda"
		JMenuItem mntmAjuda = new JMenuItem("Ajuda");
		mntmAjuda.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnAjuda.add(mntmAjuda);

		// Botão "Sobre" do menu "Ajuda"
		JMenuItem mntmSobre = new JMenuItem("Sobre");
		mntmSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// abrimos a janela "Sobre"
				JanelaSobre about = new JanelaSobre();
				about.setLocationRelativeTo(null);
				about.setVisible(true);
			}
		});

		JSeparator separator_3 = new JSeparator();
		mnAjuda.add(separator_3);
		mnAjuda.add(mntmSobre);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Split Panel
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);

		// Text Area
		textArea = new JTextArea();
		splitPane.setRightComponent(textArea);

		// Label onde a imagem é desenhada
		imageView = new ZoomAndPanImageView(null);
		imageView.setMinimumSize(new Dimension(10, 10));
		splitPane.setLeftComponent(imageView);

		// definimos o frame como maximizado
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * Muda o tema utilizado pela aplicação.
	 * 
	 * @param theme
	 *            nome do tema.
	 */
	public void setTheme(String theme) {
		NimRODTheme nt = new NimRODTheme("theme\\" + theme + ".theme");
		NimRODLookAndFeel nf = new NimRODLookAndFeel();
		NimRODLookAndFeel.setCurrentTheme(nt);
		try {
			UIManager.setLookAndFeel(nf);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Define a imagem mostrada pelo visualizador de imagem.
	 * 
	 * @param img
	 *            imagem.
	 */
	public void setImageViewImage(BufferedImage img) {
		imageView.setImage(img);
		imageView.repaint();
	}

	/**
	 * Define o texto do JTextArea.
	 * 
	 * @param text
	 */
	public void setTextAreaText(String text) {
		this.textArea.setText(text);
	}
}
