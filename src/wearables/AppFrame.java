package wearables;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
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
					// Set visible to true and resizable to false
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
		
		// Instantiate reasoner object
		final Reasoner reasoner = new Reasoner("Wearables");

		try {
			// Initialize variables (load objects from XML file into appropriate classes)
			reasoner.init();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// Create str builder object
		final StringBuilder chatLog = new StringBuilder();

		// Configure JFrame window //
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(45, 62, 80));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Image imgQuestion = new ImageIcon(this.getClass().getResource("/wearable.png")).getImage();
		JLabel lblQuestion = new JLabel("");
		lblQuestion.setIcon(new ImageIcon(imgQuestion));
		lblQuestion.setBounds(861, 22, 100, 100);
		getContentPane().add(lblQuestion);
		
		final Image imgProduct = new ImageIcon(this.getClass().getResource("/wearable.png")).getImage();
		final JLabel lblProduct = new JLabel("");
		lblProduct.setHorizontalAlignment(SwingConstants.CENTER);
		lblProduct.setBounds(667, 210, 300, 300);
		getContentPane().add(lblProduct);
		
		JLabel lblNewLabel = new JLabel("WearablesDirect");
		lblNewLabel.setFont(new Font("Lantinghei TC", Font.PLAIN, 42));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(116, 59, 409, 34);
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
		
		JLabel lblEnter = new JLabel("Enter question");
		lblEnter.setForeground(Color.WHITE);
		lblEnter.setFont(new Font("Lantinghei TC", Font.PLAIN, 14));
		lblEnter.setBounds(270, 684, 100, 16);
		contentPane.add(lblEnter);
		
		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setMargin(new Insets(10,10,10,10));
		
		textField = new JTextField();
		textField.setBounds(270, 712, 489, 34);
		contentPane.add(textField);
		textField.setColumns(10);
		
		final JLabel lblProductTitle = new JLabel("");
		lblProductTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblProductTitle.setForeground(Color.WHITE);
		lblProductTitle.setFont(new Font("Lantinghei TC", Font.PLAIN, 23));
		lblProductTitle.setBounds(628, 179, 366, 34);
		contentPane.add(lblProductTitle);
		
		final JLabel lblDescription = new JLabel();
		lblDescription.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescription.setForeground(Color.WHITE);
		lblDescription.setFont(new Font("Lantinghei TC", Font.PLAIN, 13));
		lblDescription.setBounds(661, 497, 300, 105);
		
		contentPane.add(lblDescription);
		
		// Set action listener (onEnter) for textField
		textField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Image imgProduct;
				
				// If textField is empty...
				if (textField.getText().trim().isEmpty())
					chatLog.append("Don't leave me empty... type something!\n");
				else {
					// Get text from textField and store in question
					String question = textField.getText();
					
					// Append question to the chat log
					chatLog.append("> " + question + "\n");
					
					// Append the answer to the chat log
					chatLog.append(reasoner.generateAnswer(question) + "\n");
					
					System.out.println(reasoner.analyseQuestion(question).productsFound.size());
					
					if (reasoner.analyseQuestion(question).storesFound.size() == 0) {
						System.out.println("More than one found");
					}
					else if (reasoner.analyseQuestion(question).storesFound.size() == 1) {
						lblProduct.setVisible(true);
						Store store = reasoner.getSingleStore(question);
						
						switch(store.name) {
						case "PC Galaxy":
							imgProduct = new ImageIcon(this.getClass().getResource("/king.jpg")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "Wearables for you":
							imgProduct = new ImageIcon(this.getClass().getResource("/king.jpg")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "Gadgetology":
							imgProduct = new ImageIcon(this.getClass().getResource("/king.jpg")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
						}
						lblProductTitle.setText(store.name);
						lblDescription.setText("<html> <body style='text-align: center;'>" + store.address + " </body> </html>");
					}
					else {
						lblProductTitle.setText("");
						lblProduct.setVisible(false);
						lblDescription.setText("");
					}
					
					if (reasoner.analyseQuestion(question).productsFound.size() == 0) {
						System.out.println("More than one found");
					}
					else if (reasoner.analyseQuestion(question).productsFound.size() == 1) {
						lblProduct.setVisible(true);
						Product product = reasoner.getSingleProduct(question);
						
						switch(product.name) {
						case "Pebble Time":
							imgProduct = new ImageIcon(this.getClass().getResource("/pebble.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "Moto 360 2015":
							imgProduct = new ImageIcon(this.getClass().getResource("/moto2015.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "Apple Watch Sport":
							imgProduct = new ImageIcon(this.getClass().getResource("/applewatch.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "CastAR":
							imgProduct = new ImageIcon(this.getClass().getResource("/castar.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "Laster SeeThru":
							imgProduct = new ImageIcon(this.getClass().getResource("/lasterseethru.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "Sony MW1 Smart Headset Pro":
							imgProduct = new ImageIcon(this.getClass().getResource("/sonyheadphones.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "The White Dash":
							imgProduct = new ImageIcon(this.getClass().getResource("/whitedash.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "Travel Jacket":
							imgProduct = new ImageIcon(this.getClass().getResource("/traveljacket.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
							
						case "Travel Lite":
							imgProduct = new ImageIcon(this.getClass().getResource("/travellite.png")).getImage();
							lblProduct.setIcon(new ImageIcon(imgProduct));
							break;
						
						}
						
						lblProductTitle.setText(product.name);
						lblDescription.setText("<html> <body style='text-align: center;'>" + product.description + " </body> </html>");
						System.out.println(reasoner.getSingleProduct(question));
						System.out.println("Just one found mate");
					}
					else {
						lblProductTitle.setText("");
						lblProduct.setVisible(false);
						lblDescription.setText("");
					}
		
				}
				
				// Reset the text field
				textField.setText("");
				// Display chat log to the text area
				textArea.setText(chatLog.toString() + "\n");
			}
		});

	}
}
