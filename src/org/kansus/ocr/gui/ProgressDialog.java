package org.kansus.ocr.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import org.kansus.ocr.ProgressListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Diálogo que mostra o progresso de uma determinada tarefa.
 * 
 * @author Charles
 */
public class ProgressDialog extends JDialog implements ProgressListener {

	private static final long serialVersionUID = -8389779952219060977L;

	private String title;

	private JPanel contentPane;
	private JLabel lblProgress;
	private JProgressBar progressBar;

	private Thread worker;

	/**
	 * Create the frame.
	 */
	public ProgressDialog(String title, Runnable work) {
		addWindowListener(new WindowAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void windowClosing(WindowEvent arg0) {
				worker.stop();
			}
		});
		this.title = title;
		initialize();

		// iniciamos a thread de trabalho.
		worker = new Thread(work);
		worker.start();
	}

	private void initialize() {
		setResizable(false);
		setModal(true);
		setTitle(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProgressDialog.class.getResource("/res/icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		progressBar = new JProgressBar();
		progressBar.setBounds(6, 6, 432, 19);
		contentPane.add(progressBar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				worker.stop();
				dispose();
			}
		});
		btnCancelar.setBounds(348, 37, 90, 28);
		contentPane.add(btnCancelar);

		lblProgress = new JLabel("Progresso");
		lblProgress.setBounds(6, 43, 330, 16);
		contentPane.add(lblProgress);
	}

	@Override
	public void onProgressChanged(String message, int progress) {
		this.progressBar.setValue(progress);
		this.lblProgress.setText(message);
	}

	@Override
	public void onProgressCompleted(String message) {
		progressBar.setValue(100);
		if (message != null) {
			if (message.startsWith("Erro"))
				JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
			else
				JOptionPane.showMessageDialog(null, message, "Informação", JOptionPane.INFORMATION_MESSAGE);
		}
		dispose();
	}
}
