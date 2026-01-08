package levels;

import Objects.*;
import entities.*;
import main.Game;
import utilz.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;



//DENIS

/**
 * Clasa corespunzatoare oricarui nivel din joc
 */
public class Level {

	private BufferedImage img;
	private int[][] lvlData;

	private ArrayList<int[]> skellies;
	private ArrayList<int[]> skellies_2;
	private ArrayList<int[]> golem;
	private ArrayList<int[]> duckies;
	private ArrayList<Potion> potions;
	private ArrayList<int[]> containers;
	private ArrayList<int[]> containers2;
	private ArrayList<int[]> spikes;
	private ArrayList<int[]> slimes;
	private ArrayList<Cannon> cannons;
	private ArrayList<int[]> bossies;


	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;

	public Level(BufferedImage img, int a, int b, int c, int d, int e) {
		this.img = img;

		createLevelData();
		createEnemies(a, b, c, d);
		createPotions(e);
		createContainers();
		createTraps();
		createCannons();
		calcLvlOffsets();

	}


	private void createCannons() {
		cannons = HelpMethods.GetCannons(img);
	}

	private void createTraps() {
		ArrayList<ArrayList<int[]>> allTraps = HelpMethods.GetTraps(img);
		spikes = allTraps.get(0);
		slimes=allTraps.get(1);
	}


	private void createContainers() {
		ArrayList<ArrayList<int[]>> allContainers = HelpMethods.GetContainers(img);
		containers = allContainers.get(0);  // BARREL
		containers2 = allContainers.get(1); // BOX
	}


	private void createPotions(int e) {
		potions = HelpMethods.GetPotions(img, e);
	}

	//private void calcPlayerSpawn() {
	//	playerSpawn = GetPlayerSpawn(img);
	//}

	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth(); //latimea nivelului TOT
		maxTilesOffset = lvlTilesWide- Game.TILES_IN_WIDTH; //cate tile-uri sunt in afara ecranului
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset; //^ dar in pixeli
	}

	private void createEnemies(int a, int b, int c, int d) {
		skellies = GetSkellys(img, a);
		skellies_2 = GetSkellys_2(img, b);   ///AICIIIIIIIIIIIII
		golem = GetGolem(img, c);
		bossies = GetBoss(img, d);
		duckies = GetDuckies(img);
	}

	private void createLevelData() {
		lvlData = GetLevelData(img);
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}

	public int getLvlOffset(){
		return maxLvlOffsetX;
	}

	public ArrayList<int[]> getSkellies(){
		return skellies;
	}

	public ArrayList<int[]> getSkellies_2(){
		return skellies_2;
	}

	public ArrayList<int[]> getGolem(){
		return golem;
	}

	public ArrayList<int[]> getBossies() {
		return bossies;
	}

	public ArrayList<int[]> getDuckies(){
		return duckies;
	}

	public Point getPlayerSpawn(){
		return playerSpawn;
	}

	public ArrayList<Potion> getPotions() {
		return potions;
	}

	public ArrayList<int[]> getContainers()
	{
		return containers;
	}
	public ArrayList<int[]> getContainers2()
	{
		return containers2;
	}


	public ArrayList<int[]> getSpikes()
	{
		return spikes;
	}

	public ArrayList<int[]> getSlimes()
	{
		return slimes;
	}


	public ArrayList<Cannon> getCannons()
	{
		return cannons;
	}


}