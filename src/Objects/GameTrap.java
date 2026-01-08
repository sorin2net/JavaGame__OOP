package Objects;

import main.Game;

import static utilz.Constants.ObjectConstants.BOX;
import static utilz.Constants.ObjectConstants.SPIKE;

/**
 * Capcanele din joc
 */
public class GameTrap extends GameObject {
    public GameTrap(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {
        if(objType == SPIKE){
            initHitbox(32,16);
            xDrawOffset = 0;
            yDrawOffset = (int)(Game.SCALE * 16);
            hitbox.y +=yDrawOffset;
        }else{
            initHitbox(32,80);
            xDrawOffset = 0;
            yDrawOffset = (int)(Game.SCALE * 16);
            hitbox.y +=yDrawOffset;
        }
    }

    public void update(){
        if(doAnimation)
            updateAniTick();
    }
}
