package main;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import javax.swing.*;
import java.awt.*;


import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;



//CLAUDIU sapt2
/**
 * Panou principal pentru randare si input.
 */
public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;
	/**
	 * Constructorul panoului de joc.
	 * @param game referinta la obiectul Game
	 */
	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	/**
	 * Seteaza dimensiunile panoului.
	 */
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}
	/**
	 * Randarea graficii jocului.
	 * @param g contextul grafic
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}

}