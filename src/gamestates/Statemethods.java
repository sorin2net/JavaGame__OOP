package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
/**
 * Interfata care defineste metodele comune pentru toate starile jocului.
 * Fiecare stare a jocului trebuie sa implementeze aceste metode.
 */
public interface Statemethods {

	/**
	 * Actualizeaza starea curenta a jocului.
	 */
	void update();

	/**
	 * Deseneaza starea curenta pe ecran.
	 * @param g Contextul grafic pentru desenare
	 */
	void draw(Graphics g);

	/**
	 * Gestioneaza evenimentul de click al mouse-ului.
	 * @param e Detaliile evenimentului de mouse
	 */
	void mouseClicked(MouseEvent e);

	/**
	 * Gestioneaza evenimentul de apasare a butonului mouse-ului.
	 * @param e Detaliile evenimentului de mouse
	 */
	void mousePressed(MouseEvent e);

	/**
	 * Gestioneaza evenimentul de eliberare a butonului mouse-ului.
	 * @param e Detaliile evenimentului de mouse
	 */
	void mouseReleased(MouseEvent e);

	/**
	 * Gestioneaza evenimentul de miscare a mouse-ului.
	 * @param e Detaliile evenimentului de mouse
	 */
	void mouseMoved(MouseEvent e);

	/**
	 * Gestioneaza evenimentul de apasare a unei taste.
	 * @param e Detaliile evenimentului de tastatura
	 */
	void keyPressed(KeyEvent e);

	/**
	 * Gestioneaza evenimentul de eliberare a unei taste.
	 * @param e Detaliile evenimentului de tastatura
	 */
	void keyReleased(KeyEvent e);
}