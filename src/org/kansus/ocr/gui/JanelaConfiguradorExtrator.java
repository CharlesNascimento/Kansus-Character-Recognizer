package org.kansus.ocr.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.kansus.ocr.Ferramentas;
import org.kansus.ocr.Globals;

/**
 * Janela "Configurador do Extrator".
 * 
 * @author Charles
 */
public class JanelaConfiguradorExtrator extends JFrame {

	private static final long serialVersionUID = -6873394683266486554L;

	private JPanel contentPane;
	private JLabel srfLabel;
	private JLabel lpawfLabel;
	private JLabel mswaforhLabel;
	private JLabel mcwaforhLabel;
	private JLabel mcbwaforhLabel;
	private JSlider srfSlider;
	private JSlider lpawfSlider;
	private JSlider mswaforhSlider;
	private JSlider mcwaforhSlider;
	private JSlider mcbwaforhSlider;

	private JanelaPrincipal janelaPrincipal;
	private ProgressDialog progressDialog;

	/**
	 * Thread respons�vel por tra�ar os caracteres na imagem de entrada.
	 */
	private Runnable tracer = new Runnable() {

		@Override
		public void run() {
			// pegamos os valores que estavam anteriormente definidos
			float srfInitial = Globals.shortRowFraction;
			float lpawfInitial = Globals.liberalPolicyAreaWhitespaceFraction;
			float mswaforh = Globals.minSpaceWidthAsFractionOfRowHeight;
			float mcwaforhInitial = Globals.minCharWidthAsFractionOfRowHeight;
			float mcbwaforhInitial = Globals.minCharBreakWidthAsFractionOfRowHeight;

			// colocamos os valores escolhidos pelo usu�rio
			updateGlobalsValues((float) srfSlider.getValue() / 1000, (float) lpawfSlider.getValue() / 1000,
					(float) mswaforhSlider.getValue() / 1000, (float) mcwaforhSlider.getValue() / 1000,
					(float) mcbwaforhSlider.getValue() / 1000);

			// mostramos o resultado
			BufferedImage img = Ferramentas.destacarCaracteres(Globals.inputImage, progressDialog);
			janelaPrincipal.setImageViewImage(img);

			// colocamos os valores antigos de volta
			updateGlobalsValues(srfInitial, lpawfInitial, mswaforh, mcwaforhInitial, mcbwaforhInitial);
		}
	};

	/**
	 * Create the frame.
	 */
	public JanelaConfiguradorExtrator(JanelaPrincipal janelaChamadora) {
		this.janelaPrincipal = janelaChamadora;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					janelaPrincipal.setImageViewImage(ImageIO.read(Globals.inputImage));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		initialize();
		initializeSlidersValues();
	}

	private void initialize() {
		setTitle("Configurador do Extrator");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaConfiguradorExtrator.class.getResource("/res/icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 420, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		srfLabel = new JLabel("Fra��o de linha curta");
		srfLabel.setToolTipText("A fra��o m�xima que a altura de uma linha pode ter da altura da linha anterior, para que a nova linha (pequena) seja mesclada com a linha anterior (grande), afim de formar uma �nica linha.");
		srfLabel.setBounds(10, 13, 394, 14);
		contentPane.add(srfLabel);

		lpawfLabel = new JLabel("Fra��o m�nima de pixels brancos para coluna branca");
		lpawfLabel.setToolTipText("A fra��o m�nima de pixels em uma coluna que devem ser brancos, para que a coluna"
				+ " seja considerada um espa�o em branco.");
		lpawfLabel.setBounds(10, 75, 394, 14);
		contentPane.add(lpawfLabel);

		mswaforhLabel = new JLabel("Largura m�nima do espa�o");
		mswaforhLabel.setToolTipText("A largura m�nima do espa�o, expressado como uma fra��o" + " da altura de uma linha.");
		mswaforhLabel.setBounds(10, 137, 384, 14);
		contentPane.add(mswaforhLabel);

		mcwaforhLabel = new JLabel("Largura m�nima de caracter");
		mcwaforhLabel.setToolTipText("A largura m�nima de caracter, expressado como uma fra��o da altura de uma linha.");
		mcwaforhLabel.setBounds(10, 199, 394, 14);
		contentPane.add(mcwaforhLabel);

		mcbwaforhLabel = new JLabel("Largura m�nima do espa�o entre caracteres de uma palavra");
		mcbwaforhLabel.setToolTipText("A largura do espa�o entre caracteres de uma palavra, expressado como"
				+ " uma fra��o da altura de uma linha.");
		mcbwaforhLabel.setBounds(10, 261, 394, 14);
		contentPane.add(mcbwaforhLabel);

		srfSlider = new JSlider(0, 1000);
		srfSlider.setMinorTickSpacing(100);
		srfSlider.setPaintTicks(true);
		srfSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				srfLabel.setText("Fra��o de linha curta: " + (float) srfSlider.getValue() / 1000);
			}
		});
		srfSlider.setBounds(10, 38, 394, 26);
		contentPane.add(srfSlider);

		lpawfSlider = new JSlider(0, 1000);
		lpawfSlider.setPaintTicks(true);
		lpawfSlider.setMinorTickSpacing(100);
		lpawfSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				lpawfLabel.setText("Fra��o m�nima de pixels brancos para coluna branca: " + (float) lpawfSlider.getValue()
						/ 1000);
			}
		});
		lpawfSlider.setBounds(10, 100, 394, 26);
		contentPane.add(lpawfSlider);

		mswaforhSlider = new JSlider(0, 1000);
		mswaforhSlider.setMinorTickSpacing(100);
		mswaforhSlider.setPaintTicks(true);
		mswaforhSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				mswaforhLabel.setText("Largura m�nima do espa�o: " + (float) mswaforhSlider.getValue() / 1000);
			}
		});
		mswaforhSlider.setBounds(10, 162, 394, 26);
		contentPane.add(mswaforhSlider);

		mcwaforhSlider = new JSlider(0, 1000);
		mcwaforhSlider.setMinorTickSpacing(100);
		mcwaforhSlider.setPaintTicks(true);
		mcwaforhSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				mcwaforhLabel.setText("Largura m�nima de caracter: " + (float) mcwaforhSlider.getValue() / 1000);
			}
		});
		mcwaforhSlider.setBounds(10, 224, 394, 26);
		contentPane.add(mcwaforhSlider);

		mcbwaforhSlider = new JSlider(0, 1000);
		mcbwaforhSlider.setMinorTickSpacing(100);
		mcbwaforhSlider.setPaintTicks(true);
		mcbwaforhSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				mcbwaforhLabel.setText("Largura m�nima do espa�o entre caracteres de uma palavra: "
						+ (float) mcbwaforhSlider.getValue() / 1000);
			}
		});
		mcbwaforhSlider.setBounds(10, 286, 394, 26);
		contentPane.add(mcbwaforhSlider);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setMnemonic('C');
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					janelaPrincipal.setImageViewImage(ImageIO.read(Globals.inputImage));
				} catch (IOException e) {
					e.printStackTrace();
				}
				dispose();
			}
		});
		btnCancelar.setBounds(10, 337, 89, 23);
		contentPane.add(btnCancelar);

		JButton btnTestar = new JButton("Testar");
		btnTestar.setMnemonic('T');
		btnTestar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				progressDialog = new ProgressDialog("Tra�ador de Caracteres", tracer);
				progressDialog.setLocationRelativeTo(null);
				progressDialog.setVisible(true);
			}
		});
		btnTestar.setBounds(162, 337, 89, 23);
		contentPane.add(btnTestar);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setMnemonic('S');
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// colocamos os valores escolhidos pelo usu�rio
				updateGlobalsValues((float) srfSlider.getValue() / 1000, (float) lpawfSlider.getValue() / 1000,
						(float) mswaforhSlider.getValue() / 1000, (float) mcwaforhSlider.getValue() / 1000,
						(float) mcbwaforhSlider.getValue() / 1000);
				try {
					janelaPrincipal.setImageViewImage(ImageIO.read(Globals.inputImage));
				} catch (IOException e) {
					e.printStackTrace();
				}
				dispose();
			}
		});
		btnSalvar.setBounds(315, 337, 89, 23);
		contentPane.add(btnSalvar);
	}

	/**
	 * Preenche os JSliders com os valores atuais do extrator.
	 */
	public void initializeSlidersValues() {
		srfSlider.setValue((int) (Globals.shortRowFraction * 1000));
		srfLabel.setText("Fra��o de linha curta: " + (float) srfSlider.getValue() / 1000);

		lpawfSlider.setValue((int) (Globals.liberalPolicyAreaWhitespaceFraction * 1000));
		lpawfLabel.setText("Fra��o m�nima de pixels brancos para coluna branca: " + (float) lpawfSlider.getValue() / 1000);

		mswaforhSlider.setValue((int) (Globals.minSpaceWidthAsFractionOfRowHeight * 1000));
		mswaforhLabel.setText("Largura m�nima do espa�o: " + (float) mswaforhSlider.getValue() / 1000);

		mcwaforhSlider.setValue((int) (Globals.minCharWidthAsFractionOfRowHeight * 1000));
		mcwaforhLabel.setText("Largura m�nima de caracter: " + (float) mcwaforhSlider.getValue() / 1000);

		mcbwaforhSlider.setValue((int) (Globals.minCharBreakWidthAsFractionOfRowHeight * 1000));
		mcbwaforhLabel.setText("Largura m�nima do espa�o entre caracteres de uma palavra: "
				+ (float) mcbwaforhSlider.getValue() / 1000);
	}

	/**
	 * Atualiza as vari�veis do extrator.
	 * 
	 * @param srf
	 *            fra��o de linha curta.
	 * @param lpawf
	 *            fra��o m�nima de pixels brancos para coluna branca.
	 * @param mswaforh
	 *            largura m�nima do espa�o.
	 * @param mcwaforh
	 *            largura m�nima de caracter.
	 * @param mcbwaforh
	 *            largura m�nima do espa�o entre caracteres de uma palavra
	 */
	public void updateGlobalsValues(float srf, float lpawf, float mswaforh, float mcwaforh, float mcbwaforh) {
		Globals.shortRowFraction = srf;
		Globals.liberalPolicyAreaWhitespaceFraction = lpawf;
		Globals.minSpaceWidthAsFractionOfRowHeight = mswaforh;
		Globals.minCharWidthAsFractionOfRowHeight = mcwaforh;
		Globals.minCharBreakWidthAsFractionOfRowHeight = mcbwaforh;
	}
}
