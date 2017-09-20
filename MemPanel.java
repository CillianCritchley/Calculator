/*
Cillian O Criothaile
C00139896
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;


public class MemPanel extends JPanel implements ActionListener {
	private static final String buttonText[][] = {
		{"MC"},
		{"MR",},
		{"MS",},
		{"M+",},
		{"M-",}
	};
	private JButton btns[][] = new JButton[buttonText.length][buttonText[0].length];
	private StringListener textListener;
	
	 MemPanel() {
		setBorder(BorderFactory.createEtchedBorder());
		Font buttonFont = new Font(Font.MONOSPACED, Font.PLAIN,17);
		setLayout(new GridLayout(5,1,1,1));

		for(int i=0;i<btns.length;i++)
		{
			for(int j=0;j<btns[i].length;j++)
			{
				btns[i][j] = new JButton(buttonText[i][j]);
				btns[i][j].setFont(buttonFont);
				add(btns[i][j]);
				btns[i][j].addActionListener(this);
			}
		}


	}

	 void setStringListener(StringListener listener) {
		this.textListener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton)e.getSource();

		for(int i=0;i<btns.length;i++)
		{
			for(int j=0;j<btns[i].length;j++)
			{
					if(clicked == btns[i][j]) {
					if(textListener != null) {
						textListener.textEmitted(buttonText[i][j]);
						}
					}
			}
		}



		
	}
}
