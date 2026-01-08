package Objects;

import Objects.Factory.Factory1;
import Objects.Factory.Factory2;
import entities.*;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.CANNON_BALL_HEIGHT;
import static utilz.Constants.Projectiles.CANNON_BALL_WIDTH;
import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.IsProjectileHittingLevel;

/**
 * Se ocupa cu gestiunea tuturor obiectelor din joc
 * le incarca, actualizeaza, randeaza
 */

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage spikeImgs,cannonBallImg,slimeImgs;
    private BufferedImage[] cannonImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers=new ArrayList<>();
    private ArrayList<GameContainer> containers2=new ArrayList<>();
    private ArrayList<GameTrap> spikes=new ArrayList<>();
    private ArrayList<GameTrap> slimes=new ArrayList<>();
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private int tick = 0;
    public static Graphics g;


    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImgs();

    }

    public void checkSpikesTouched(Player player){
        for( GameTrap s: spikes){
            if(s.getHitbox().intersects(player.getHitbox()))
                player.kill();
        }
    }

    public void checkSlimesTouched(Player player) {
        for (GameTrap s : slimes) {

            if( (int) Math.abs(player.getHitbox().x - s.getHitbox().x)<=Game.TILES_SIZE){
                        tick++;
                }
                if (tick >= 400) {
                    player.changeHealth(-1);
                    tick = 0;
                }
            }

    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == SPECIAL_POTION)
            playing.getPlayer().addMilestone();
        else
            if(p.getObjType() == BLUE_POTION)
                playing.getPlayer().activateJumpBoost(10);
            else
                if(p.getObjType() == RED_POTION)
                    playing.getPlayer().changeHealth(RED_POTION_VALUE);
                else if(p.getObjType()== GREEN_POTION)
                    playing.getPlayer().changeHealth(-1);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitbox().intersects(attackbox)) {
                    gc.setAnimation(true);
                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 2), BLUE_POTION));
                    return;
                }
            }
        for (GameContainer gc : containers2)
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitbox().intersects(attackbox)) {
                    gc.setAnimation(true);
                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 2), RED_POTION));
                    return;
                }
            }
    }

    public void loadObjects(Level newLevel) {
        containers2.clear();
        containers.clear();
        spikes.clear();
        slimes.clear();
        Factory1 factory = new Factory1();
        Factory2 factory2 = new Factory2();
        potions = new ArrayList<>(newLevel.getPotions());
        for(int[] k:newLevel.getContainers()) {
            containers.add( factory.getContainer(k[0], k[1]));
        }
        for(int[] k:newLevel.getContainers2()) {
            containers2.add( factory2.getContainer(k[0], k[1]));
        }

        for(int[] k:newLevel.getSlimes()) {
            slimes.add(factory.getTrap(k[0], k[1]));
        }

        for(int[] k:newLevel.getSpikes()) {
            spikes.add(factory2.getTrap(k[0], k[1]));
        }

        cannons = newLevel.getCannons();
        projectiles.clear();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[4][7];
        for(int j= 0; j< potionImgs.length; j++)
            for(int i=0; i< potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12*i, 16*j, 12,16);

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

        spikeImgs = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
        slimeImgs = LoadSave.GetSpriteAtlas(LoadSave.SLIME);
        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

        for(int i=0; i<cannonImgs.length;i++){
            cannonImgs[i] = temp.getSubimage(i*40,0,40,26);
        }

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
    }

    public void addPotion(Potion potion) {
        potions.add(potion);
    }

    public void update(int[][] lvlData, Player player){
        for(Potion p : potions)
            if(p.isActive())
                p.update();
        for(GameContainer gc: containers)
            if(gc.isActive())
                gc.update();
        for(GameContainer gc: containers2)
            if(gc.isActive())
                gc.update();
        updateCannons(lvlData, player);

        updateProjectiles(lvlData,player);

    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for(Projectile p : projectiles) {
            if (p.isActive()) {
                p.updatePos(1);
                if(p.getHitbox().intersects(player.getHitbox())){
                    player.changeHealth(-2);
                    p.setActive(false);
                }
                else if(IsProjectileHittingLevel(p,lvlData)){
                    p.setActive(false);
                }
            }
        }
    }

    public void updateSpells(int[][] lvlData) {
        ArrayList<Projectile> spells = playing.getPlayer().getSpells();
        ArrayList<Skelly> enemies = playing.getEnemyManager().getSkellies();
        ArrayList<Skelly2> enemies2 = playing.getEnemyManager().getSkellies_2();
        ArrayList<Golem> enemies3 = playing.getEnemyManager().getGolem();
        ArrayList<Boss> enemies4 = playing.getEnemyManager().getBossies();

        Iterator<Projectile> spellIterator = spells.iterator();
        while (spellIterator.hasNext()) {
            Projectile p = spellIterator.next();

            if (!p.isActive()) {
                spellIterator.remove();
                continue;
            }
            p.updatePos(1);

            boolean hitEnemy = false;
            for (Skelly enemy : enemies) {
                if (enemy.isActive() && p.getHitbox().intersects(enemy.getHitbox())) {
                    enemy.hurt(25);
                    hitEnemy = true;
                    break;
                }
            }

            for (Skelly2 enemy : enemies2) {
                if (enemy.isActive() && p.getHitbox().intersects(enemy.getHitbox())) {
                    enemy.hurt(25);
                    hitEnemy = true;
                    break;
                }
            }

            for (Golem enemy3 : enemies3) {
                if (enemy3.isActive() && p.getHitbox().intersects(enemy3.getHitbox())) {
                    enemy3.hurt(25);
                    hitEnemy = true;
                    break;
                }
            }

            for (Boss enemy4 : enemies4) {
                if (enemy4.isActive() && p.getHitbox().intersects(enemy4.getHitbox())) {
                    enemy4.hurt(10);
                    hitEnemy = true;
                    break;
                }
            }



            if (hitEnemy || IsProjectileHittingLevel(p, lvlData)) {
                spellIterator.remove();

            }
        }
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x)
                return true;

        } else if (c.getHitbox().x < player.getHitbox().x)
            return true;
        return false;
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInfrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
                                c.setAnimation(true);


            c.update();
            if(c.getAniIndex() == 4 && c.getAniTick() == 0)
                shootCannon(c);
        }
    }

    private void shootCannon(Cannon c) {

        int dir =1;
        if(c.getObjType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int)c.getHitbox().x, (int)c.getHitbox().y, dir));
    }

    /**
     * deseneaza obiectele
     */
    public void draw(Graphics g, int xLvlOffset){
        drawPotions(g,xLvlOffset);
        drawContainers(g,xLvlOffset);
        drawTraps(g,xLvlOffset);
        drawCannons(g,xLvlOffset);
        drawProjectiles(g,xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for(Projectile p :projectiles){
            if(p.isActive())
                g.drawImage(cannonBallImg,(int)(p.getHitbox().x- xLvlOffset), (int)(p.getHitbox().y), CANNON_BALL_WIDTH,CANNON_BALL_HEIGHT,null);
        }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for(Cannon c :cannons) {
            int x = (int)(c.getHitbox().x - xLvlOffset);
            int width= CANNON_WIDTH;
            if(c.getObjType() == CANNON_RIGHT){
                x+=width;
                width *=-1;
            }
            g.drawImage(cannonImgs[c.getAniIndex()],x,(int)(c.getHitbox().y),width,CANNON_HEIGHT,null);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for(GameTrap s : spikes)
            g.drawImage(spikeImgs,(int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y-s.getyDrawOffset()), SPIKE_WIDTH,SPIKE_HEIGHT,null);
        for(GameTrap s : slimes)
            g.drawImage(slimeImgs,(int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y-s.getyDrawOffset()), SLIME_WIDTH,SLIME_HEIGHT,null);


    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                g.drawImage(containerImgs[1][gc.getAniIndex()],
                        (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                        (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
        for (GameContainer gc : containers2)
            if (gc.isActive()) {
                g.drawImage(containerImgs[0][gc.getAniIndex()],
                        (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                        (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                else
                    if(p.getObjType()== SPECIAL_POTION)
                        type =2;
                    else
                        if(p.getObjType()== GREEN_POTION)
                            type = 3;
                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset + 20),
                        (int) (p.getHitbox().y - p.getyDrawOffset()+20),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion p : potions)
            p.reset();

        for (GameContainer gc : containers)
            gc.reset();

        for(Cannon c: cannons)
            c.reset();
    }



}