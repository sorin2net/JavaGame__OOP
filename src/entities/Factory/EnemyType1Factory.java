package entities.Factory;

import entities.Enemy;
import entities.Golem;
import entities.Skelly;
import entities.Skelly2;

import static utilz.Constants.EnemyConstants.*;

public class EnemyType1Factory implements EnemyFactory {
    @Override
    public Enemy createEnemy(float x, float y, int type)
    {
        if(type==SKELLY)
            return new Skelly(x, y);
        if(type==SKELLY2)
            return new Skelly2(x, y);
        if(type==GOLEM)
            return new Golem(x, y);
        return null;
    }

}