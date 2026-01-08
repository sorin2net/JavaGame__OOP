package utilz;

import Objects.*;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;

/**
 * Utilitare pentru manipularea nivelului si a entitatilor.
 */
public class HelpMethods {

	/**
	 * lista tile-uri care sunt considerate solide
	 */
	private static final Set<Integer> SOLID_TILES = new HashSet<>();
	static {
		SOLID_TILES.add(1);
		SOLID_TILES.add(2);
		SOLID_TILES.add(3);
		SOLID_TILES.add(4);
		SOLID_TILES.add(5);
		SOLID_TILES.add(6);
		SOLID_TILES.add(7);
		SOLID_TILES.add(8);
		SOLID_TILES.add(9);
		SOLID_TILES.add(13);
		SOLID_TILES.add(14);
		SOLID_TILES.add(15);
		SOLID_TILES.add(16);
		SOLID_TILES.add(17);
		SOLID_TILES.add(18);
		SOLID_TILES.add(19);
		SOLID_TILES.add(20);
		SOLID_TILES.add(21);
		SOLID_TILES.add(24);
		SOLID_TILES.add(25);
		SOLID_TILES.add(26);
		SOLID_TILES.add(48);
		SOLID_TILES.add(49);
		SOLID_TILES.add(50);
		SOLID_TILES.add(77);
		SOLID_TILES.add(78);
		SOLID_TILES.add(79);
		SOLID_TILES.add(80);
		SOLID_TILES.add(81);
		SOLID_TILES.add(82);
		SOLID_TILES.add(75);
		SOLID_TILES.add(87);
	}


	//CLAUDIU saptamana 3
	/**
	 * Verifica daca o entitate se poate muta in pozitia data.
	 * @param x coordonata x
	 * @param y coordonata y
	 * @param width latime entitate
	 * @param height inaltime entitate
	 * @param lvlData date nivel
	 * @return true daca miscarea e posibila
	 */
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}


	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}


	/**
	 * Verifica daca un tile este solid.
	 * @param xTile coordonata x tile
	 * @param yTile coordonata y tile
	 * @param lvlData date nivel
	 * @return true daca tile-ul e solid
	 */
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData){
		int value = lvlData[yTile][xTile];

		if (value >= 96 || value < 0 || SOLID_TILES.contains(value))
			return true;
		return false;
	}

	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) { // la dreapta
			int tileXPos = currentTile * Game.TILES_SIZE; //la dreapta ultimului tile nonsolid
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width); //cat mai are de mers
			return tileXPos + xOffset - 1;
		} else
			//stanga
			return currentTile * Game.TILES_SIZE;
	}

	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}

	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;

		return true;

	}

	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if(xSpeed > 0)
			return IsSolid(hitbox.x + xSpeed + hitbox.width, hitbox.y + hitbox.height + 1 , lvlData);
		return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1 , lvlData);

	}

	public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
	}

	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData){
		for(int i=0;i<xEnd-xStart;i++)
			if(IsTileSolid(xStart + i, y,lvlData))
				return false;
		return true;
	}

	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		if (IsAllTilesClear(xStart, xEnd, y, lvlData))
			for (int i = 0; i < xEnd - xStart; i++) {
				if (!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
			}
		return true;
	}

	public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
		int firstXTile = (int) (enemyBox.x / Game.TILES_SIZE);

		int secondXTile;
		if (IsSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData))
			secondXTile = (int) (playerBox.x / Game.TILES_SIZE);
		else
			secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
	}



//DENIS
	/**
	 * Extrage datele nivelului dintr-o imagine.
	 * @param img imaginea nivelului
	 * @return matrice cu datele nivelului
	 */
	public static int[][] GetLevelData(BufferedImage img) {

		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 96) {
					value = 0;
				}
				lvlData[j][i] = value;
			}


		}
		return lvlData;
	}

	//functia asta e pentru boss, sa nu isi dea desh cand e aproape de margine
	public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}

	//TOTI
	/**
	 * Gaseste pozitiile scheletilor in nivel.
	 * @param img imaginea nivelului
	 * @param a numar de scheleti
	 * @return lista de coordonate
	 */
	public static ArrayList<int[]> GetSkellys(BufferedImage img, int a){
		int b=a;
		ArrayList<int[]> list = new ArrayList<>();
		for (int j = 0; j < img.getWidth(); j++) {
			for (int i = 0; i < img.getHeight(); i++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getGreen();
				if(value==SKELLY && b!=0)
				{
					b--;
				}
				else if(value==SKELLY && b==0) {
					list.add(new int[]{j * Game.TILES_SIZE, i * Game.TILES_SIZE});
				}
			}

		}
		return list;
	}


	/**
	 * Gaseste pozitiile scheletilor tip 2 in nivel.
	 * @param img imaginea nivelului
	 * @param a numar de scheleti
	 * @return lista de coordonate
	 */
	public static ArrayList<int[]> GetSkellys_2(BufferedImage img, int a) {
		int b = a;
		ArrayList<int[]> list = new ArrayList<>();
		for (int j = 0; j < img.getWidth(); j++) {
			for (int i = 0; i < img.getHeight(); i++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getGreen();
				if (value == SKELLY2 && b != 0) {
					b--;
				} else if (value == SKELLY2 && b == 0) {
					list.add(new int[]{j * Game.TILES_SIZE, i * Game.TILES_SIZE});
				}
			}
		}
		return list;
	}

	/**
	 * Gaseste pozitiile golemilor in nivel.
	 * @param img imaginea nivelului
	 * @param c numar de golemi
	 * @return lista de coordonate
	 */
	public static ArrayList<int[]> GetGolem(BufferedImage img, int c) {
		int b = c;
		ArrayList<int[]> list = new ArrayList<>();
		for (int j = 0; j < img.getWidth(); j++) {
			for (int i = 0; i < img.getHeight(); i++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getGreen();
				if (value == GOLEM && b != 0) {
					b--;
				} else if (value == GOLEM && b == 0) {
					list.add(new int[]{j * Game.TILES_SIZE, i * Game.TILES_SIZE});
				}
			}
		}
		return list;
	}

	/**
	 * Gaseste pozitiile boss-urilor in nivel.
	 * @param img imaginea nivelului
	 * @param d numar de boss
	 * @return lista de coordonate
	 */
	public static ArrayList<int[]> GetBoss(BufferedImage img, int d) {
		int b = d;
		ArrayList<int[]> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == BOSS && b != 0) {
					b--;
				} else if (value == BOSS && b == 0) {
					list.add(new int[]{i * Game.TILES_SIZE, j * Game.TILES_SIZE});
				}
			}
		}
		return list;
	}

	/**
	 * Gaseste pozitiile potiunilor in nivel.
	 * @param img imaginea nivelului
	 * @return lista de potiuni
	 */
	public static ArrayList<int[]> GetDuckies(BufferedImage img) {
		ArrayList<int[]> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == DUCKY) {
					list.add(new int[]{i * Game.TILES_SIZE, j * Game.TILES_SIZE});
					System.out.println(i * Game.TILES_SIZE);
				}
			}
		}
		return list;
	}



	public static ArrayList<Potion> GetPotions(BufferedImage img, int a){
	int b=a;
		ArrayList<Potion> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if(value==SPECIAL_POTION && b!=0){
					b--;
				}
				else if(value==SPECIAL_POTION && b==0) {
					list.add(new Potion(i * Game.TILES_SIZE, j* Game.TILES_SIZE,value));
				}
				if (value == RED_POTION || value == BLUE_POTION || value==GREEN_POTION) {
					list.add(new Potion(i * Game.TILES_SIZE, j* Game.TILES_SIZE,value));
				}
			}

		}
		return list;
	}

	public static ArrayList<ArrayList<int[]>> GetContainers(BufferedImage img) {
		ArrayList<ArrayList<int[]>> list = new ArrayList<>();

		ArrayList<int[]> barrelList = new ArrayList<>();
		ArrayList<int[]> boxList = new ArrayList<>();

		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();

				if (value == BARREL) {
					barrelList.add(new int[]{i * Game.TILES_SIZE, j * Game.TILES_SIZE, value});
				} else if (value == BOX) {
					boxList.add(new int[]{i * Game.TILES_SIZE, j * Game.TILES_SIZE, value});
				}
			}
		}

		list.add(barrelList);
		list.add(boxList);

		return list;
	}

	public static ArrayList<ArrayList<int[]>> GetTraps(BufferedImage img) {
		ArrayList<ArrayList<int[]>> list = new ArrayList<>();

		ArrayList<int[]> spikeList = new ArrayList<>();
		ArrayList<int[]> slimeList = new ArrayList<>();

		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();

				if (value == SPIKE) {
					spikeList.add(new int[]{i * Game.TILES_SIZE, j * Game.TILES_SIZE, value});
				} else if (value == SLIME) {
					slimeList.add(new int[]{i * Game.TILES_SIZE, j * Game.TILES_SIZE, value});
				}
			}
		}

		list.add(spikeList); // index 0 - SPIKES
		list.add(slimeList); // index 1 - SLIMES

		return list;
	}


	public static ArrayList<Cannon> GetCannons(BufferedImage img) {
		ArrayList<Cannon> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == CANNON_LEFT || value == CANNON_RIGHT) {
					list.add(new Cannon(i * Game.TILES_SIZE, j* Game.TILES_SIZE,value));
				}
			}
		}
		return list;
	}

	public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData){
		return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
	}

}