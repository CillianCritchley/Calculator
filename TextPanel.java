/*
Cillian O Criothaile
C00139896
 */
import java.awt.BorderLayout;
import java.awt.*;
import javax.swing.*;


class TextPanel extends JPanel {
	
	private JTextField textField;
	
	TextPanel() {
		textField = new JTextField();
		Font biggerfont = new Font(Font.MONOSPACED, Font.BOLD, 30);
		textField.setFont(biggerfont);
		textField.setEditable(false);
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		setLayout(new BorderLayout());
		
		add(textField, BorderLayout.CENTER);
	}

	/*
	method to allow other components to send text to the textfield
	 */
	void appendText(String text) {textField.setText(text);}



}
