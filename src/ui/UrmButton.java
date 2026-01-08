package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.URM_DEFAULT_SIZE;
import static utilz.Constants.UI.URMButtons.URM_SIZE;

//DENIS

/**
 * Butoane URM (Unpause/Replay/Menu)
 */
public class UrmButton extends PauseButton {
    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    /**
     * Initializeaza butonul URM.
     * @param x pozitia x pe ecran
     * @param y pozitia y pe ecran
     * @param width latime buton
     * @param height inaltime buton
     * @param rowIndex randul din atlas pentru tipul butonului
     */
    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        //iau imaginile din spritesheet pentru retry,unpause,menu
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;

    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public void setMouseOver(boolean mouseOver) {

        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {

        this.mousePressed = mousePressed;
    }

}