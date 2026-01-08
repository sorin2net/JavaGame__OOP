package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

//DENIS

/**
 * Overlay-ul afisat in timpul pauzei jocului
 */
public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB, replayB, unpauseB;
    private AudioOptions audioOptions;

    /**
     * Constructor pentru overlay-ul de pauză.
     * @param playing Referință către starea de playing
     */
    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();


    }

    /**
     * Inițializează butoanele URM (Unpause, Replay, Menu).
     */
    private void createUrmButtons() {//creez butoanele cu dimensiunea necesara
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);

    }

    private void loadBackground() {//incarc imaginea meniului de pauza
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);

    }

    public void update() {

        menuB.update();
        replayB.update();
        unpauseB.update();

        audioOptions.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // UrmButtons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        audioOptions.draw(g);

    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);

    }

//si load si save
    /**
     * Gestionează evenimentul de apăsare a mouse-ului.
     * Salvează progresul jucătorului dacă se apasă butonul de meniu.
     * @param e Evenimentul mouse-ului
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
            playing.getGame().getAudioPlayer().playPressEffect();

            // Salvarea progresului jucătorului
            String playerName = playing.getGame().getPlayerName(); // Obține numele jucatorului
            float playerX = playing.getPlayer().getHitbox().x;

            float playerY = playing.getPlayer().getHitbox().y;

            int health = playing.getPlayer().getHealth();   // Viața curenta
            int score = playing.getPlayer().getHealth()*100;
            int potions = playing.getPlayer().getWhatIGot(); // Numarul de poțiuni
            int currentLevel = playing.getLevelManager().getLvlIndex(); // Nivelul curent
            int skelly= playing.skelly1_m2;
            int skelly2= playing.skelly2_m2;
            int golem = playing.golem_m2;
            int boss = playing.boss_m2;
            int pot_l= playing.potiuni_l2;

            System.out.println("astea sunt" +skelly+skelly2);
            playing.getGame().getScoreDatabase().saveFinalScore(playerName, health, score, potions, currentLevel, false, playerX, playerY, skelly, skelly2, golem, pot_l, boss);

        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
            playing.getGame().getAudioPlayer().playPressEffect();

        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
            playing.getGame().getAudioPlayer().playPressEffect();
        } else {
            audioOptions.mousePressed(e);
            playing.getGame().getAudioPlayer().playPressEffect();
        }
    }

    public void mouseReleased(MouseEvent e) {

        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                playing.setGameState(Gamestate.MENU);
                playing.getGame().getAudioPlayer().playPressEffect();
                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed())
            {
                int savedPotions = playing.getPlayer().getPotionCounter();
                playing.getPlayer().reset2();
                playing.getPlayer().setWhatIGot(playing.getPlayer().getPotionCounter());
                playing.resetAll();
                playing.restM();

                playing.getGame().getAudioPlayer().playPressEffect();

                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()){
                playing.unpauseGame();
                playing.getGame().getAudioPlayer().playPressEffect();

            }

        } else
            audioOptions.mouseReleased(e);
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();


    }

    public void mouseMoved(MouseEvent e) {

        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e, menuB)){
            menuB.setMouseOver(true);
//            playing.getGame().getAudioPlayer().playHoverEffect();
        }

        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);

    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}
