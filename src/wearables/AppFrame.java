package wearables;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.awt.Font;
import javax.swing.SwingConstants;

public class AppFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextPane txtpnAsd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppFrame frame = new AppFrame();
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
	public AppFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(45, 62, 80));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(105, 418, 489, 34);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblEnter = new JLabel("Enter question");
		lblEnter.setForeground(Color.WHITE);
		lblEnter.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblEnter.setBounds(110, 393, 100, 16);
		contentPane.add(lblEnter);
		
		Image imgQuestion = new ImageIcon(this.getClass().getResource("/wearable.png")).getImage();
		JLabel lblQuestion = new JLabel("");
		lblQuestion.setIcon(new ImageIcon(imgQuestion));
		lblQuestion.setBounds(579, 19, 100, 100);
		getContentPane().add(lblQuestion);
		
		JLabel lblNewLabel = new JLabel("WearablesDirect");
		lblNewLabel.setFont(new Font("Lantinghei TC", Font.PLAIN, 25));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(82, 44, 210, 34);
		contentPane.add(lblNewLabel);
		
		JLabel lblC = new JLabel("Welcome to WearablesDirect");
		lblC.setForeground(Color.WHITE);
		lblC.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblC.setBounds(368, 25, 210, 28);
		contentPane.add(lblC);
		
		JLabel lblTypeInA = new JLabel("Type in a question and I will");
		lblTypeInA.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTypeInA.setForeground(Color.WHITE);
		lblTypeInA.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblTypeInA.setBounds(357, 44, 210, 34);
		contentPane.add(lblTypeInA);
		
		JLabel lblHelpYouIn = new JLabel("Help you in your search for");
		lblHelpYouIn.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHelpYouIn.setForeground(Color.WHITE);
		lblHelpYouIn.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblHelpYouIn.setBounds(357, 65, 210, 34);
		contentPane.add(lblHelpYouIn);
		
		JLabel lblTheTechYou = new JLabel("The tech you need");
		lblTheTechYou.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTheTechYou.setForeground(Color.WHITE);
		lblTheTechYou.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblTheTechYou.setBounds(357, 85, 210, 34);
		contentPane.add(lblTheTechYou);
		
		
	}
}
