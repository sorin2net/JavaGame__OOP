package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

//E meniul care apare atunci cand pierzi
//DENIS

/**
 * Meniul ce apare cand jocul este pierdut.
 */
public class GameOverOverlay {

    private Playing playing;
    private BufferedImage img;
    private int imgX, imgY, imgW, imgH;
    private UrmButton menu, play;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int) (335 * Game.SCALE);
        int playX = (int) (440 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 1);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);

    }

    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgW = (int) (img.getWidth() * Game.SCALE);
        imgH = (int) (img.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (100 * Game.SCALE);

    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);

        menu.draw(g);
        play.draw(g);
    }

    public void update() {
        menu.update();
        play.update();
    }

    public void keyPressed(KeyEvent e) {

    }

    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(play, e))
            play.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        } else if (isIn(play, e))
            if (play.isMousePressed()) {
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
            }
        menu.resetBools();
        play.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)) {
            String playerName = playing.getGame().getPlayerName();
            float playerX = 10;
            float playerY = 10;
            int health = 10;
            int score = 10;
            int potions = playing.getGame().getPlaying().getPlayer().getWhatIGot();
            int currentLevel = playing.getGame().getPlaying().getLevelManager().getLvlIndex();
            int skelly_1 = 0;
            int skelly_2 = 0;
            int golem = 0;
            int boss = 0;
            int pot_l = playing.getGame().getPlaying().potiuni_l2;
            System.out.println("astea sunt" + skelly_1 + skelly_2);
            playing.getGame().getScoreDatabase().saveFinalScore(playerName, health, score, potions, currentLevel, false, playerX, playerY, skelly_1, skelly_2, golem, pot_l, boss);
            menu.setMousePressed(true);
        } else if (isIn(play, e)) {
            play.setMousePressed(true);
            playing.getPlayer().setWhatIGot(playing.getPlayer().getPotionCounter());
            playing.getPlayer().reset2();
            playing.resetAll();
            playing.restM();

        }
    }

}