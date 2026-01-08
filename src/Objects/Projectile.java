package Objects;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Projectiles.*;

/**
 * Clasa care reprezinta un proiectil în joc.
 * Gestioneaza mișcarea si starea proiectilelor lansate de jucator sau inamici.
 */
public class Projectile {
    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;

    /**
     * Constructor pentru proiectil.
     * @param x Pozitia initiala x pe ecran
     * @param y Pozitia initiala y pe ecran
     * @param dir Directia de mișcare (1 pentru dreapta, -1 pentru stanga)
     */
    public Projectile(int x,int y, int dir){
        int xOffset = (int)(-3 * Game.SCALE);
        int yOffset = (int)(5 * Game.SCALE);

        if(dir == 1){
            xOffset = (int)(29 * Game.SCALE);
        }
        hitbox = new Rectangle2D.Float(x+ xOffset,y+yOffset,CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    /**
     * Actualizează poziția proiectilului cu viteza normală.
     * @param x Factorul de viteză (poate fi folosit pentru delta time)
     */
    public void updatePos(float x){
        hitbox.x += dir * SPEED * x;
    }


    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return active;
    }

}
