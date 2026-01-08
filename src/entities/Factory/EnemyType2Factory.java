package entities.Factory;

import entities.Boss;
import entities.Enemy;
import entities.Npc;

import static utilz.Constants.EnemyConstants.BOSS;
import static utilz.Constants.EnemyConstants.DUCKY;

public class EnemyType2Factory implements EnemyFactory{
    @Override
    public Enemy createEnemy(float x, float y, int type)
    {
        if(type == BOSS)
            return new Boss(x, y);
        return null;
    }
}