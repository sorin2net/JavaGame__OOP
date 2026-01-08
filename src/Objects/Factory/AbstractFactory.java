package Objects.Factory;

import Objects.GameContainer;
import Objects.GameTrap;

abstract class AbstractFactory {
   abstract GameContainer getContainer(int x, int y);
    abstract GameTrap getTrap(int x, int y);

}
