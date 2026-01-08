package Objects.Factory;

import Objects.GameContainer;
import Objects.GameTrap;

import static utilz.Constants.ObjectConstants.*;

public class Factory2 extends AbstractFactory {
    @Override
    public GameContainer getContainer(int x, int y) {
        return new GameContainer(x, y, BARREL);

    }

    public GameTrap getTrap(int x, int y ) {
        return new GameTrap(x, y, SPIKE);
    }
}
