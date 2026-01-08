package main;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

//CLAUDIU sapt 2

/**
 * Fereastra principala a jocului.
 */
public class GameWindow {
	private JFrame jframe;


	/**
	 * Construieste fereastra jocului.
	 * @param gamePanel panoul de joc de adaugat
	 */
	public GameWindow(GamePanel gamePanel) {

		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);
		gamePanel.requestFocus();
		jframe.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {

			}
		});

	}

}
