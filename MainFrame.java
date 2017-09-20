/*
Cillian O Criothaile
C00139896
 */
import java.awt.*;
import javax.swing.*;

class MainFrame  {
	
	private MemPanel memPanel;
	private Calculator Calc;
	private TextPanel textPanel;
	private BtnPanel btnPanel;
	private JFrame theFrame;



	MainFrame() {
		theFrame = new JFrame();
		btnPanel = new BtnPanel();
		textPanel = new TextPanel();
		memPanel = new MemPanel();
		Calc = new Calculator();

		theFrame.setTitle("Calculator");
		theFrame.setLayout(new BorderLayout());
		theFrame.add(memPanel,BorderLayout.WEST);
		theFrame.add(btnPanel, BorderLayout.CENTER);
		theFrame.add(textPanel, BorderLayout.NORTH);

		theFrame.setSize(380, 442);
		memPanel.setVisible(false);
		theFrame.setResizable(false);
		theFrame.setLocation(new Point(400,300));
		theFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
/*
Stringlistener attached to the button panel. Does three things. Listens for the M button press so it can hide and unhide
 the memory panel.
 Everything else gets sent over to Calculator.java via the put() method.
 */
		btnPanel.setStringListener(new StringListener() {
			@Override
			public void textEmitted(String text) {
				/*
				Hides and unhides the Memory Function panel. Attempts to position the resized panel in the same
				position it was in before the click. Almost gets it right. Almost.
				 */
				if(text.equals("M") && memPanel.isVisible())
				{
					memPanel.setVisible(false);
					Point p = theFrame.getLocationOnScreen();
					p.translate(50,0);
					theFrame.setSize(380, 442);
					theFrame.setLocation(p);

				}
				else if(text.equals("M") && !memPanel.isVisible()) {

					Point p = theFrame.getLocationOnScreen();
					p.translate(-50,0);
					theFrame.setSize(490, 442);
					theFrame.setLocation(p);
					memPanel.setVisible(true);
				}
				else {
					Calc.put(text);
				}
			}
		});

		/*
		Sends the string from the Calculator output() method to the textpanel  via the appendText method
		 */
		Calc.setStringListener(new StringListener() {
			@Override
			public void textEmitted(String text) {
				textPanel.appendText(text);
			}

		});

		/*
		sends the memory panel functions (memory plus, memory clear, etc) to the Calculator class using the put() method.
		 */
		memPanel.setStringListener(new StringListener() {
			@Override
			public void textEmitted(String text) {
				Calc.put(text);
			}


		});

		theFrame.setVisible(true);

	}


}
