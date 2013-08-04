package org.kansus.ocr.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.kansus.ocr.KansusCharacterRecognizer;

/**
 * Janela "Treinamento".
 * 
 * @author Charles
 */
public class JanelaTreinamento extends JFrame {

	private static final long serialVersionUID = 5756239638420445603L;

	private JPanel contentPane;
	private JTextField dir_tf;
	private JTextField ci_tf;
	private JTextField cf_tf;
	private JTextField seq_tf;
	private JRadioButton rdbtnRange;
	private JRadioButton rdbtnSequncia;
	private JLabel lblCaracterDeInicio;
	private JLabel lblCaracterFinal;
	private JLabel lblSequenciacaracteres;
	private ButtonGroup btgrp = new ButtonGroup();

	private ProgressDialog progressDialog;

	/**
	 * Thread responsável por fazer o treinamento range.
	 */
	private Runnable treinamentoRange = new Runnable() {

		@Override
		public void run() {
			try {
				KansusCharacterRecognizer.treinamentoRange(dir_tf.getText(), ci_tf.getText().charAt(0),
						cf_tf.getText().charAt(0), progressDialog);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * thread responsável por fazer o treinamento sequência.
	 */
	private Runnable treinamentoSequencia = new Runnable() {

		@Override
		public void run() {
			try {
				KansusCharacterRecognizer.treinamentoSequencia(dir_tf.getText(), seq_tf.getText(), progressDialog);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * Create the frame.
	 */
	public JanelaTreinamento() {
		initialize();
	}

	/**
	 * Initialize components.
	 */
	private void initialize() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaTreinamento.class.getResource("/res/icon.png")));
		setTitle("Treinamento");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 225);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Diret\u00F3rio");
		lblNewLabel.setBounds(6, 6, 74, 16);
		contentPane.add(lblNewLabel);

		dir_tf = new JTextField();
		dir_tf.setBounds(6, 22, 386, 28);
		contentPane.add(dir_tf);
		dir_tf.setColumns(10);

		// Botão "Buscar" da Aba "Batch"
		JButton btnSelecionar = new JButton("Buscar");
		btnSelecionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Diretório de saída");
				chooser.setPreferredSize(new Dimension(700, 450));
				int returnVal = chooser.showOpenDialog(getFrames()[0]);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					dir_tf.setText(chooser.getSelectedFile().getAbsolutePath());
				} else {
					chooser.setVisible(false);
				}
			}
		});
		btnSelecionar.setBounds(404, 22, 84, 28);
		contentPane.add(btnSelecionar);

		lblCaracterDeInicio = new JLabel("Caracter de in\u00EDcio");
		lblCaracterDeInicio.setBounds(6, 70, 115, 16);
		contentPane.add(lblCaracterDeInicio);

		ci_tf = new JTextField();
		ci_tf.setText("!");
		ci_tf.setBounds(6, 86, 37, 28);
		contentPane.add(ci_tf);
		ci_tf.setColumns(10);

		lblCaracterFinal = new JLabel("Caracter final");
		lblCaracterFinal.setBounds(238, 70, 90, 16);
		contentPane.add(lblCaracterFinal);

		cf_tf = new JTextField();
		cf_tf.setText("~");
		cf_tf.setBounds(238, 86, 37, 28);
		contentPane.add(cf_tf);
		cf_tf.setColumns(10);

		lblSequenciacaracteres = new JLabel("Sequ\u00EAncia");
		lblSequenciacaracteres.setEnabled(false);
		lblSequenciacaracteres.setBounds(6, 139, 74, 16);
		contentPane.add(lblSequenciacaracteres);

		seq_tf = new JTextField();
		seq_tf.setEnabled(false);
		seq_tf.setBounds(6, 159, 386, 28);
		contentPane.add(seq_tf);
		seq_tf.setColumns(10);

		rdbtnRange = new JRadioButton("Range (Tabela ASCII)");
		rdbtnRange.setSelected(true);
		rdbtnRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnRange.isSelected()) {
					updateRadioControls(true);
				}
			}
		});
		rdbtnRange.setBounds(6, 51, 482, 18);
		contentPane.add(rdbtnRange);

		rdbtnSequncia = new JRadioButton("Sequ\u00EAncia (Caracteres separados por espa\u00E7os em branco)");
		rdbtnSequncia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnSequncia.isSelected()) {
					updateRadioControls(false);
				}
			}
		});
		rdbtnSequncia.setBounds(6, 121, 482, 18);
		contentPane.add(rdbtnSequncia);

		// Botão "Treinar" da Aba "Batch"
		JButton btnTreinar = new JButton("Treinar");
		btnTreinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnRange.isSelected()) {
					if (ci_tf.getText().length() != 0 && cf_tf.getText().length() != 0) {
						progressDialog = new ProgressDialog("Treinamento", treinamentoRange);
						progressDialog.setLocationRelativeTo(null);
						progressDialog.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(JanelaTreinamento.this, "Informe os caracteres de início e fim!",
								"Atenção", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					progressDialog = new ProgressDialog("Treinamento", treinamentoSequencia);
					progressDialog.setLocationRelativeTo(null);
					progressDialog.setVisible(true);
				}
			}
		});
		btnTreinar.setBounds(404, 159, 84, 28);
		contentPane.add(btnTreinar);

		btgrp.add(rdbtnRange);
		btgrp.add(rdbtnSequncia);
	}

	/**
	 * Habilita e desabilita os devidos componentes de acordo com o RadioButton
	 * selecionado.
	 * 
	 * @param b
	 *            valor.
	 */
	public void updateRadioControls(boolean b) {
		lblCaracterDeInicio.setEnabled(b);
		lblCaracterFinal.setEnabled(b);
		ci_tf.setEnabled(b);
		cf_tf.setEnabled(b);

		lblSequenciacaracteres.setEnabled(!b);
		seq_tf.setEnabled(!b);
	}
}
