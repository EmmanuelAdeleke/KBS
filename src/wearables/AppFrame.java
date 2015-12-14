package wearables;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Insets;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

public class AppFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppFrame frame = new AppFrame();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppFrame() {
		final Reasoner reasoner = new Reasoner("Wearables");

		try {
			reasoner.init();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		final StringBuilder chatLog = new StringBuilder();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(45, 62, 80));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblEnter = new JLabel("Enter question");
		lblEnter.setForeground(Color.WHITE);
		lblEnter.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblEnter.setBounds(270, 684, 100, 16);
		contentPane.add(lblEnter);

		Image imgQuestion = new ImageIcon(this.getClass().getResource("/wearable.png")).getImage();
		JLabel lblQuestion = new JLabel("");
		lblQuestion.setIcon(new ImageIcon(imgQuestion));
		lblQuestion.setBounds(861, 22, 100, 100);
		getContentPane().add(lblQuestion);

		JLabel lblNewLabel = new JLabel("WearablesDirect");
		lblNewLabel.setFont(new Font("Lantinghei TC", Font.PLAIN, 25));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(82, 44, 210, 34);
		contentPane.add(lblNewLabel);

		JLabel lblC = new JLabel("Welcome to WearablesDirect");
		lblC.setForeground(Color.WHITE);
		lblC.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblC.setBounds(650, 28, 210, 28);
		contentPane.add(lblC);

		JLabel lblTypeInA = new JLabel("Type in a question and I will");
		lblTypeInA.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTypeInA.setForeground(Color.WHITE);
		lblTypeInA.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblTypeInA.setBounds(639, 47, 210, 34);
		contentPane.add(lblTypeInA);

		JLabel lblHelpYouIn = new JLabel("Help you in your search for");
		lblHelpYouIn.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHelpYouIn.setForeground(Color.WHITE);
		lblHelpYouIn.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblHelpYouIn.setBounds(639, 68, 210, 34);
		contentPane.add(lblHelpYouIn);

		JLabel lblTheTechYou = new JLabel("The tech you need");
		lblTheTechYou.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTheTechYou.setForeground(Color.WHITE);
		lblTheTechYou.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblTheTechYou.setBounds(639, 88, 210, 34);
		contentPane.add(lblTheTechYou);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 146, 594, 511);
		getContentPane().add(scrollPane);

		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setMargin(new Insets(10,10,10,10));

		textField = new JTextField();
		textField.setBounds(270, 712, 489, 34);
		contentPane.add(textField);
		textField.setColumns(10);

		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String question = textField.getText();
				textArea.setText(question);
				chatLog.append("> " + question + "\n");
				chatLog.append(reasoner.generateAnswer(question) + "\n");
				textField.setText("");
				textArea.setText(chatLog.toString() + "\n");
			}
		});

	}
}
