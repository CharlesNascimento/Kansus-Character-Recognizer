package org.kansus.ocr.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.UIManager;

/**
 * Janela "Sobre".
 * @author Charles
 */
public class JanelaSobre extends JDialog {

	private static final long serialVersionUID = 6898701815443963186L;

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public JanelaSobre() {
		initialize();
	}

	private void initialize() {
		setModal(true);
		setResizable(false);
		setTitle("Sobre o KCR");
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaSobre.class.getResource("/res/icon.png")));
		setBounds(100, 100, 450, 236);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setBounds(117, 10, 200, 50);
			lblNewLabel.setIcon(new ImageIcon(JanelaSobre.class.getResource("/res/kansus.png")));
			contentPanel.add(lblNewLabel);
		}

		JSeparator separator = new JSeparator();
		separator.setBounds(36, 71, 362, 2);
		contentPanel.add(separator);
		{
			JButton okButton = new JButton("OK");
			okButton.setBounds(370, 173, 63, 23);
			contentPanel.add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}

		JTextPane descricao_tp = new JTextPane();
		StyledDocument doc = descricao_tp.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		descricao_tp.setBackground(UIManager.getColor("Button.background"));
		descricao_tp.setEditable(false);
		descricao_tp
				.setText("Programa desenvolvido em projeto da disciplina Intelig\u00EAncia Artificial na Universidade Federal de Sergipe. O projeto \u00E9 composto pelos seguinte alunos: Camila Carvalho, Ed Charles Nascimento, Leonardo Almeida, Rodrigo Arag\u00E3o e Sthefanie Rodrigues.");
		descricao_tp.setBounds(36, 85, 362, 115);
		contentPanel.add(descricao_tp);
	}
}
