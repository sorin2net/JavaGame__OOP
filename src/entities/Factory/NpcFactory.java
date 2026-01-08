package entities.Factory;

import entities.Boss;
import entities.Enemy;
import entities.Npc;

import static utilz.Constants.EnemyConstants.BOSS;
import static utilz.Constants.EnemyConstants.DUCKY;

public class NpcFactory implements EnemyFactory{
    @Override
    public Enemy createEnemy(float x, float y, int type)
    {
        if(type == DUCKY)
            return new Npc(x, y);
        return null;
    }
}