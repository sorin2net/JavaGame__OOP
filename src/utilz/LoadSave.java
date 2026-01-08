package utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

//TOTI
/**
 * Utilitar pentru incarcarea resurselor jocului.
 * constante pentru fisierele de imagini.
 */
public class LoadSave {


	public static final String PLAYER_ATLAS = "char_anim.png";
	public static final String LEVEL_ATLAS = "free.png";
	public static final String MENU_BUTTONS = "button_atlas1.png";
	public static final String MENU_BACKGROUND = "Menu_bg.png";
	public static final String PAUSE_BACKGROUND = "pause_menu1.png";
	public static final String SOUND_BUTTONS = "sound_button1.png";
	public static final String URM_BUTTONS = "urm_buttons1.png";
	public static final String VOLUME_BUTTONS = "volume_buttons1.png";
	public static final String MENU_BACKGROUND_IMG = "Background_0.png";
	public static final String PLAYING_BG_IMG = "Background_10.png";
	public static final String SMALL_CLOUD = "lilcloud.png";
	public static final String SKELLY_SPRITE = "skelly_sprite.png";
	public static final String SKELLY2_SPRITE = "skelly2_sprite.png";
	public static final String GOLEM_ATLAS = "Golem.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String STATUS_BAR2 = "potionsCheck2.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
	public static final String GAME_COMPLETED_IMG = "game_completed_sprite.png";
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String CANNON_ATLAS = "cannon_atlas.png";
	public static final String CANNON_BALL = "ball.png";
	public static final String FIREBALL = "fireball.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	public static final String OPTIONS_MENU = "pause_menu1.png";
	public static final String NPC_ATLAS = "npc.png";
	public static final String POTION_CHECK_BG = "lil_thing.png";
	public static final String BOSS_ATLAS = "boss.png";
	public static final String ORB = "glow_orb.png";
	public static final String BOSS_HEALTHBAR = "boss_health.png";
	public static final String WIZARD_DUCK = "wizard_duck.png";
	public static final String BG_LVL3 = "BgLvl3.png";
	public static final String SLIME = "slime.png";

	/**
	 * Incarca o imagine din resurse.
	 * @param fileName numele fisierului
	 * @return imaginea incarcata
	 */
	public static BufferedImage GetSpriteAtlas(String fileName) {
		try (InputStream is = LoadSave.class.getResourceAsStream("/" + fileName)) {
			if (is == null) {
				throw new IOException("fisierul nu exista: " + fileName);
			}
			return ImageIO.read(is); // incarca imaginea
		} catch (IOException e) {
			System.err.println("eroare la incarcare: " + fileName);
			e.printStackTrace(); //afisare de erori
			return null;
		}
	}

	/**
	 * Incarca toate nivelurile din directorul lvls.
	 * @return array cu imaginile nivelurilor
	 */
	public static BufferedImage[] GetAllLevels(){
		URL url = LoadSave.class.getResource("/lvls"); //getResource ->url
		File file = null;
		try {
			file = new File(url.toURI()); //toURI pentru a converti in file
		} catch (URISyntaxException e) { //daca url->uri nu merge (caractere ilegale)
			e.printStackTrace();
		}

		File[] files= null;
		File[] filesSorted = null;
		try {
			files = file.listFiles();
			if (files == null) {
				throw new IOException("nu s-a gasit fisierul: ");
			}
			filesSorted = new File[files.length];
		} catch (Exception e) {
			e.printStackTrace();
		}

		//pune in vector imaginile ordonate
		for(int i=0;i< filesSorted.length;i++)
			for(int j=0;j< files.length;j++){
				if(files[j].getName().equals((i+1) + ".png"))
					filesSorted[i] = files[j];
			}

		//citeste imaginile
		BufferedImage[] imgs =new BufferedImage[filesSorted.length];
		for(int i=0; i <imgs.length; i++){
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) { //fisier corupt
				e.printStackTrace();
			}
		}

		return imgs;
	}

}