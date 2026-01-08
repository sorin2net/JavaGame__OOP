package entities.Factory;

import entities.Enemy;

public interface EnemyFactory {

        Enemy createEnemy(float x, float y, int type);
    }
