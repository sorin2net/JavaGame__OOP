package Objects;

import main.Game;

public class Slime extends GameObject {
    public Slime(int x, int y, int objType) {
        super(x, y, objType);

        initHitbox(32,80);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16);
        hitbox.y +=yDrawOffset;
    }
}
