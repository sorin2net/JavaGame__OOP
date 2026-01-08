package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;

//DENIS saptamana 6

/**
 * Clasa pentru inamicul Golem
 */
public class Golem extends Enemy {
    private int attackBoxOffsetX;
    private boolean dying = false;
    private boolean dead = false;

    public Golem(float x, float y) {
        super(x, y, GOLEM_WIDTH, GOLEM_HEIGHT, GOLEM);
        initHitbox(31, 31);

        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(30 * Game.SCALE),(int)(31 * Game.SCALE));
        attackBoxOffsetX=(int)(Game.SCALE*30);
    }

    public void update(int[][] lvlData, Player player, EnemyManager enemyManager) {

        if (dead || !active)
            return;

        updateBehavior(lvlData, player);
        updateAniTick();
        updateAttackBox();

        if (dying && aniIndex >= GetSpriteAmount(enemyType, state) - 1) {
            enemyManager.updateM(2);
            dead = true;
            active = false;
        }
    }
    protected void mover(int[][] lvlData){
        float xSpeed = 0;

        if(walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        xSpeed *= MakeGolemFaster();

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if(IsFloor(hitbox, xSpeed, lvlData)){
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir();

    }

    private void updateAttackBox() {
        if (walkDir == LEFT) {
            attackBox.x = hitbox.x - attackBoxOffsetX;
        } else {
            attackBox.x = hitbox.x + hitbox.width;
        }
        attackBox.y = hitbox.y;
    }

    private float MakeGolemFaster() {
        if(currentHealth <GetMaxHealth(GOLEM) * 0.8)
            return 2;
        else
            return 1;

    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch(state) {
                case IDLE_G:
                    newState(RUNNING_G);
                    break;
                case RUNNING_G:
                    if (canSeePlayer(lvlData, player,5)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK_G);
                    }
                    mover(lvlData);
                    break;
                case ATTACK_G:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 7 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT_G:
                    if (aniIndex >= GetSpriteAmount(enemyType, state) - 1) {
                        if (currentHealth <= 0) {
                            dying = true;
                            newState(DEAD_G);
                        } else {
                            newState(RUNNING_G);
                        }
                    }
                    break;
                case DEAD_G:
                    break;
            }
        }
    }

    @Override
    public void hurt(int amount) {
        if (dying || dead || !active) return;

        super.hurt(amount);
        if (currentHealth <= 0) {
            newState(HIT_G);
        } else {
            newState(HIT_G);
        }
    }

    public boolean isDead() {
        return dead;
    }

    public int flipX() {
        return walkDir == LEFT ? width : 0;
    }

    public int flipW() {
        return walkDir == LEFT ? -1 : 1;
    }

    @Override
    public void resetEnemy() {
        super.resetEnemy();
        dying = false;
        dead = false;
        active = true;
    }

    @Override
    public boolean isActive() {
        return active && !dead;
    }
}