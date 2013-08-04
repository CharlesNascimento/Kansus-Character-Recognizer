package org.kansus.ocr.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.kansus.ocr.KansusCharacterRecognizer;

/**
 * Janela "Configuração da Rede".
 * @author Charles
 */
public class JanelaConfiguracaoRede extends JFrame {

	private static final long serialVersionUID = -3216325879554649157L;
	private final JPanel contentPanel = new JPanel();
	private JSpinner spinner_ne;
	private JSpinner spinner_em;
	private JSpinner spinner_nco;
	private JSpinner spinner_ta;

	private int neuroniosIntermediarios;

	/**
	 * Create the dialog.
	 */
	public JanelaConfiguracaoRede() {
		initialize();
		this.neuroniosIntermediarios = KansusCharacterRecognizer.getRedeNeural().getCamadaIntermediaria().getNumeroNeuronios();
		// quando a janela é aberta, preenchemos os comboboxes com os dados da
		// rede neural.
		spinner_ne.setValue(KansusCharacterRecognizer.getRedeNeural().getNumeroMaximoCiclos());
		spinner_ta.setValue(KansusCharacterRecognizer.getRedeNeural().getTaxaDeAprendizado() * 100);
		spinner_em.setValue(KansusCharacterRecognizer.getRedeNeural().getErroMinimo() * 100);
		spinner_nco.setValue(this.neuroniosIntermediarios);
	}

	private void initialize() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaConfiguracaoRede.class.getResource("/res/icon.png")));
		setTitle("Configura\u00E7\u00E3o da Rede");
		setBounds(100, 100, 300, 215);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// Botão "OK"
		JButton btnSalvar = new JButton("OK");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// atualizamos as configurações da rede
				KansusCharacterRecognizer.getRedeNeural().setErroMinimo((Double.valueOf(spinner_em.getValue().toString())) / 100);
				KansusCharacterRecognizer.getRedeNeural().setTaxaDeAprendizado(
						(Double.valueOf(spinner_ta.getValue().toString())) / 100);
				KansusCharacterRecognizer.getRedeNeural().setNumeroMaximoCiclos(Integer.valueOf(spinner_ne.getValue().toString()));
				int novoNeuroniosIntermediarios = Integer.valueOf(spinner_nco.getValue().toString());
				// só redefinimos os neurônios da camada oculta se houve
				// alteração no valor.
				if (neuroniosIntermediarios != novoNeuroniosIntermediarios)
					KansusCharacterRecognizer.getRedeNeural().redefinirNeuroniosCamadaIntermediaria(novoNeuroniosIntermediarios);
				dispose();
			}
		});
		btnSalvar.setBounds(152, 153, 90, 28);
		contentPanel.add(btnSalvar);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Rede MLP", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(6, 6, 282, 146);
		contentPanel.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("Erro m\u00EDnimo");
		label.setBounds(6, 85, 174, 16);
		panel.add(label);

		JLabel lblNeurniosCamadaOculta = new JLabel("Neur\u00F4nios camada oculta");
		lblNeurniosCamadaOculta.setBounds(6, 57, 174, 16);
		panel.add(lblNeurniosCamadaOculta);

		JLabel label_2 = new JLabel("M\u00E1ximo de \u00E9pocas");
		label_2.setBounds(6, 29, 174, 16);
		panel.add(label_2);

		JLabel label_3 = new JLabel("Taxa de aprendizado");
		label_3.setBounds(6, 113, 174, 16);
		panel.add(label_3);

		spinner_ne = new JSpinner();
		spinner_ne.setModel(new SpinnerNumberModel(1, 1, 100000, 500));
		spinner_ne.setBounds(192, 23, 84, 28);
		panel.add(spinner_ne);

		spinner_em = new JSpinner();
		spinner_em.setModel(new SpinnerNumberModel(1.0, 1.0, 90.0, 1.0));
		spinner_em.setBounds(192, 79, 84, 28);
		panel.add(spinner_em);

		spinner_nco = new JSpinner();
		spinner_nco.setModel(new SpinnerNumberModel(1, 1, 576, 1));
		spinner_nco.setBounds(192, 51, 84, 28);
		panel.add(spinner_nco);

		spinner_ta = new JSpinner();
		spinner_ta.setModel(new SpinnerNumberModel(90, null, 100, 1));
		spinner_ta.setBounds(192, 107, 84, 28);
		panel.add(spinner_ta);

		// Botão "Cancelar"
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(50, 153, 90, 28);
		contentPanel.add(btnCancelar);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
}
