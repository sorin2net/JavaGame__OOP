package gamestates;

import Objects.ObjectManager;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.*;
import ui.GameOverOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.Environment.SMALL_CLOUD_HEIGHT;
import static utilz.Constants.Environment.SMALL_CLOUD_WIDTH;

public class Playing extends State implements Statemethods {
	public static Player player=null;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private GameCompletedOverlay gameCompletedOverlay;
	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.4 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.6 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg, smallCloudImg;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver = false;
	private boolean lvlCompleted =false;

	private boolean playerDying = false;

	private float currentZoom = 1.0f;
	private float targetZoom = 1.0f;
	private float ZOOM_SPEED = 0.05f;
	private float DEATH_ZOOM_LEVEL = 4.0f;

	private BufferedImage fogOfWarImage = new BufferedImage(Game.GAME_WIDTH, Game.GAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);


	//load si save
	private int finalHealth;
	public int poz2;
	public int skelly1_m2=0;
	public int skelly2_m2=0;
	public int golem_m2=0;
	public int boss_m2=0;
	public int potiuni_l2=0;
	public int viata2=10;

	public void setTot() {
		this.poz2=game.pot;
		this.skelly1_m2=game.skelly1_m;
		this.skelly2_m2=game.skelly2_m;
		this.golem_m2=game.golem_m;
		this.boss_m2=game.boss_m;
		this.potiuni_l2=game.potiuni_l;
		this.viata2=game.viata;

	}

	public void restM() {
		this.poz2=0;
		this.skelly1_m2=0;
		this.skelly2_m2=0;
		this.golem_m2=0;
		this.boss_m2=0;
		this.potiuni_l2=0;
	}


	//CLAUDIU + alte modificari
	public Playing(Game game) {
		super(game);
		setTot();
		initClasses();
		smallCloudImg = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUD);
		smallCloudsPos = new int[8];
		for(int i=0;i<smallCloudsPos.length;i++)
			smallCloudsPos[i] =(int)(40* Game.SCALE) + rnd.nextInt((int)(100*Game.SCALE));

		calcLvlOffset();
		loadStartLevel();
	}

	public void loadNextLevel(){
		resetAll();
		restM();
		player.setPotionCounter(player.getWhatIGot());

		levelManager.loadNextLevel();
		player.setSpawn(game.pentrux, game.pentruy);
		player.setWhatIGot(player.getPotionCounter());
	}

	private void loadStartLevel() {
		System.out.println("Loading start level: " + levelManager.getLvlIndex());
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		int startX = (int) game.pentrux;
		int startY = (int) game.pentruy;

		player = new Player(startX, startY, (int) (40 * Game.SCALE), (int) (40 * Game.SCALE), this,  this.viata2);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(game.pentrux, game.pentruy);

		//player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		player.setSpawn(game.pentrux, game.pentruy);
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);

	}

	@Override
	public void update() {
		if(paused){
			pauseOverlay.update();
		}
		else if(lvlCompleted){
			if(levelManager.getLvlIndex()==4)
			{
				Game.joc_terminat =1;
				gameCompletedOverlay.update();
				System.out.println("game completed up");
			}
			else {
				levelCompletedOverlay.update();
			}
		}else if(gameOver){
			gameOverOverlay.update();
			game.resetPlayerStartingPosition();
		}
		else if(playerDying){
			targetZoom = DEATH_ZOOM_LEVEL;
			player.update();
		}
		else{
			targetZoom = 1.0f;
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			player.updateSpells();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			objectManager.updateSpells(levelManager.getCurrentLevel().getLevelData());
			checkClosetoBorder();
		}
		if (Math.abs(currentZoom - targetZoom) > 0.01f) {
			currentZoom += (targetZoom - currentZoom) * ZOOM_SPEED;
		} else {
			currentZoom = targetZoom;
		}

	}

	private void checkClosetoBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;

	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform originalTransform = g2d.getTransform();

		if (currentZoom != 1.0f) {
			int playerScreenX = (int)(player.getHitbox().x - xLvlOffset);
			int playerScreenY = (int)player.getHitbox().y;

			g2d.translate(playerScreenX, playerScreenY);
			g2d.scale(currentZoom, currentZoom);
			g2d.translate(-playerScreenX, -playerScreenY);
		}

		switch (levelManager.getLvlIndex()){
			case 0:
				backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
				break;
			case 1:
				backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
				break;
			case 2:
				backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
				break;
			case 3:
				backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.BG_LVL3);
				break;
			case 4:
				backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.BG_LVL3);
				break;
			default:
				break;
		}


		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		if(levelManager.getLvlIndex()<3)
			drawClouds(g);

		levelManager.draw(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		objectManager.draw(g,xLvlOffset);
		player.drawSpell(g,xLvlOffset);
		if(levelManager.getLvlIndex()==3) {
			drawFogOfWar(g);
		}
		player.render(g, xLvlOffset);

		g2d.setTransform(originalTransform);

		if(paused){
			g.setColor(new Color(0, 0, 0,200));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}
		else if(gameOver){
			gameOverOverlay.draw(g);
		}else if(lvlCompleted){
			if (levelManager.getLvlIndex() <4){
				levelCompletedOverlay.draw(g);

			} else {
				System.out.println("game completed draw");
				gameCompletedOverlay.draw(g);
			}
		}



		g2d.setTransform(originalTransform);
	}

	private void drawClouds(Graphics g) {
		for(int i=0;i<smallCloudsPos.length;i++)
			g.drawImage(smallCloudImg,
					SMALL_CLOUD_WIDTH * 4 * i- (int)(xLvlOffset * 0.7),
					smallCloudsPos[i],
					SMALL_CLOUD_WIDTH,
					SMALL_CLOUD_HEIGHT,
					null);
	}

	public void resetAll(){
		// reset player, enemy, lvl
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		player.resetAll();
		player.clearSpells();
		game.resetPlayerStartingPosition();
		resetJumpBoost();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();

		currentZoom = 1.0f;
		targetZoom = 1.0f;
	}

	public void setGameOver(boolean gameOver){
		this.gameOver = gameOver;
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox){
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkNpcInteract(Rectangle2D.Float attackBox) {
		enemyManager.checkNpcInteracted(attackBox);
	}

	public void clearNpcInteractions() {
		enemyManager.clearNpcInteractions();
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox){
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkSpikesTouched(Player player) {
		objectManager.checkSpikesTouched(player);
	}

	public void checkSlimesTouched(Player player) {
		objectManager.checkSlimesTouched(player);
	}
	//DENIS
	private void drawFogOfWar(Graphics g) {
		Graphics2D g2d = (Graphics2D) fogOfWarImage.getGraphics();

		g2d.setComposite(AlphaComposite.Src);
		g2d.setColor(new Color(0, 0, 0, 250));
		g2d.fillRect(0, 0, fogOfWarImage.getWidth(), fogOfWarImage.getHeight());

		int radius = 500;
		int playerScreenX = (int) (player.getHitbox().x - xLvlOffset);
		int playerScreenY = (int) player.getHitbox().y;

		g2d.setComposite(AlphaComposite.Clear);
		g2d.fillOval(playerScreenX - radius / 2, playerScreenY - radius / 2, radius, radius);

		g.drawImage(fogOfWarImage, 0, 0, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!gameOver)
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gameOver)
			gameOverOverlay.keyPressed(e);
		else
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					player.setFacingRight(false);
					player.setLeft(true);
					break;
				case KeyEvent.VK_D:
					player.setFacingRight(true);
					player.setRight(true);
					break;
				case KeyEvent.VK_W:
				case KeyEvent.VK_SPACE:
					player.setJump(true);
					break;
				case KeyEvent.VK_ESCAPE:
					paused = !paused;
					break;
				case KeyEvent.VK_Q:
					if(!player.isShooting()){
						player.setShooting(true);
						player.updateSpells();
					}
					break;
				case KeyEvent.VK_E:
					player.setInteracting(true);
					break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!gameOver)
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					player.setLeft(false);
					break;
				case KeyEvent.VK_D:
					player.setRight(false);
					break;
				case KeyEvent.VK_W:
				case KeyEvent.VK_SPACE:
					player.setJump(false);
				case KeyEvent.VK_Q:
					player.setShooting(false);
					break;
				case KeyEvent.VK_E:
					player.setInteracting(false);
					break;
			}

	}

	public void mouseDragged(MouseEvent e) {
		if(!gameOver)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!gameOver) {
			if (paused)
				pauseOverlay.mousePressed(e);
			else if(lvlCompleted) {
				if(levelManager.getLvlIndex()==4)
					gameCompletedOverlay.mousePressed(e);
				else
					levelCompletedOverlay.mousePressed(e);
			}
		} else{
			gameOverOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if (lvlCompleted){
				if(levelManager.getLvlIndex()!=4)
					levelCompletedOverlay.mouseReleased(e);
				else
					gameCompletedOverlay.mouseReleased(e);
			}
		} else{
			gameOverOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if (lvlCompleted)
				if(levelManager.getLvlIndex()!=4)
					levelCompletedOverlay.mouseMoved(e);
				else
					gameCompletedOverlay.mouseMoved(e);
		}else {
			gameOverOverlay.mouseMoved(e);
		}
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.lvlCompleted = levelCompleted;
		if(levelCompleted)
			game.getAudioPlayer().lvlCompleted();
	}

	public void completeLevel() {
		this.lvlCompleted = true;
		this.finalHealth = player.getHealth();
		int score = calculateFinalScore();
		boolean completedGame = (levelManager.getLvlIndex() == levelManager.getAmountOfLevels() - 1);

		float playerX = player.getX(); // Player's X coordinate
		float playerY = player.getY(); // Player's Y coordinate
		int potionCount = player.getWhatIGot(); // Current potion count
		System.out.println("astea sunt "+ this.skelly1_m2 + this.skelly2_m2);
		// Save score and position to database
		game.getScoreDatabase().saveFinalScore(
				game.getPlayerName(),
				finalHealth,
				score,
				potionCount, // Pass current potion count
				levelManager.getLvlIndex(),
				completedGame,
				playerX, // X coordinate
				playerY,
				this.skelly1_m2,
				this.skelly2_m2,
				this.golem_m2,
				this.potiuni_l2,
				this.boss_m2// Y coordinate
		);

		game.getAudioPlayer().lvlCompleted();
	}

	private int calculateFinalScore() {
		return player.getHealth() * 100;
	}

	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	public void unpauseGame(){
		paused = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	public EnemyManager getEnemyManager(){
		return enemyManager;
	}

	public ObjectManager getObjectManager(){
		return objectManager;
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public LevelManager getLevelManager(){
		return levelManager;
	}

	public void resetJumpBoost(){
		player.activateJumpBoost(0);
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}

}
