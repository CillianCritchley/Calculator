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
import javax.swing.border.EtchedBorder;
/*
2d array of strings defined first and then used in the loop to create and add the buttons to the buttonpanel.
This way allows for easier editing and changing of the calculator layout if desired.
 */
public class BtnPanel extends JPanel implements ActionListener {
	private static final String buttonText[][] = {
		{"M","x²","1/x","<html> x <sup> y</sup> </html>","+/-"},
		{"√","7","8","9","*"},
		{"%","4","5","6","/"},
		{"C","1","2","3","-"},
		{"AC",".","0","=","+"}
	};
	private JButton btns[][] = new JButton[buttonText.length][buttonText[0].length];
	private StringListener textListener;
	
	 BtnPanel() {
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		Font buttonFont = new Font(Font.DIALOG, Font.PLAIN,16);
		Font boldFont = new Font(Font.MONOSPACED, Font.BOLD,22);
		setLayout(new GridLayout(5,4,1,1));

		for(int i=0;i<btns.length;i++)
		{
			for(int j=0;j<btns[i].length;j++)
			{
				btns[i][j] = new JButton(buttonText[i][j]);

				if(i>0 && i<4  && j > 0 && j<4)
				{
					btns[i][j].setFont(boldFont);

				}
				else if(i>3 && (j == 2 || j==1 ) )
				{
					btns[i][j].setFont(boldFont);

				}

				else {
					btns[i][j].setFont(buttonFont);
				}
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
