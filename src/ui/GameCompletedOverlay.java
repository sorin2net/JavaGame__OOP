package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

/**
 * Meniul ce apare cand jucatorul termina tot jocul
 */
public class GameCompletedOverlay {
    private Playing playing;
    private UrmButton menu;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    public GameCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (387 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.GAME_COMPLETED_IMG);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        menu.draw(g);
    }

    public void update() {
        menu.update();
    }

    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.getPlayer().resetWhatIGot();
                playing.setGameState(Gamestate.MENU);
                playing.getGame().getAudioPlayer().playPressEffect();

            }
        }

        menu.resetBools();

    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)){
            menu.setMousePressed(true);

        }

    }

}
