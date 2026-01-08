package Objects.Factory;


import Objects.GameContainer;
import Objects.GameTrap;

import static utilz.Constants.ObjectConstants.BOX;
import static utilz.Constants.ObjectConstants.SLIME;

public class Factory1 extends AbstractFactory {
    @Override
    public GameContainer getContainer(int x, int y) {
        return new GameContainer(x, y, BOX);

    }

    public GameTrap getTrap(int x, int y ) {
        return new GameTrap(x, y, SLIME);
    }
}
