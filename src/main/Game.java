package main;

import audio.AudioPlayer;
import gamestates.*;
import gamestates.Menu;
import ui.AudioOptions;
import utilz.LoadSave;

import javax.swing.*;
import java.awt.*;

/**
 * Gestioneaza starile jocului, datele jucatorului si bucla principala
 */

public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
	private Scores scores;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.75f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	private String playerName;
	private ScoreDatabase scoreDatabase;

	public float pentrux ;
	public float pentruy ;
	public int pentrulevel;
	public int pot;

	public int skelly1_m;
	public int skelly2_m;
	public int golem_m;
	public int boss_m;
	public int potiuni_l;
	public int viata;

	public static int joc_terminat=0;

	/**
	 * constructorul clasei Game. Initializeaza baza de date, game window-ul si restul claselor
	 */

	public Game() {
		this.scoreDatabase = new ScoreDatabase();
		playerName=askForPlayerName();
		if(playerName==null)
			System.exit(0);
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		System.out.println(Game.GAME_WIDTH + " , " + Game.GAME_HEIGHT);
		startGameLoop();
	}

	//C

	/**
	 * execute bucla principala de joc cu FPS si UPS fixe.
	 */
	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}

			if (Gamestate.state == Gamestate.MENU) {
			}
		}
	}

	//C
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	//C
	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

	/**
	 * initializeaza toate componentele si starile de joc
	 */

	private void initClasses() {
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);
		playing = new Playing(this);
		gameOptions = new GameOptions(this);
		scores = new Scores(this);
	}

	//C+T  load save
	private String askForPlayerName() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(new Color(27, 195, 207, 255));

		ImageIcon icon = new ImageIcon(LoadSave.GetSpriteAtlas(LoadSave.WIZARD_DUCK));
		Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

		JLabel textLabel = new JLabel("<html><div style='width:200px; text-align:center;'>Introduceti numele vrajitorului:</div></html>");
		textLabel.setFont(new Font("Arial", Font.BOLD, 14));
		textLabel.setHorizontalAlignment(JLabel.CENTER);

		JTextField nameField = new JTextField(20);
		nameField.setFont(new Font("Arial", Font.PLAIN, 14));

		panel.add(imageLabel, BorderLayout.NORTH);
		panel.add(textLabel, BorderLayout.CENTER);
		panel.add(nameField, BorderLayout.SOUTH);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		int result;
		String name = null;

		while (true) {
			result = JOptionPane.showOptionDialog(
					null,
					panel,
					"",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					new Object[]{"PLAY"},
					null
			);

			if (result != JOptionPane.OK_OPTION && result != 0) {
				return null;
			}

			name = nameField.getText().trim();

			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
			} else if (name.length() > 15) {
				JOptionPane.showMessageDialog(null, "Name must be 15 characters or less!", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}

			ScoreDatabase.PlayerData playerData = scoreDatabase.getPlayerData(name);

			if (playerData != null) {
				if (playerData.isCompletedGame()) {
					JOptionPane.showMessageDialog(null,
							"Acest joc a fost deja finalizat!\nTe rugăm să alegi un alt nume pentru un joc nou.",
							"Joc finalizat", JOptionPane.INFORMATION_MESSAGE);
					continue;
				} else {
					this.setPlayerName(name);
					this.setPotions(playerData.getHighestPotions());
					int pentrulevel = playerData.getHighestLevel();
					this.setPlayerStartingPosition(pentrulevel, playerData.getHighestLevelX(),
							playerData.getHighestLevelY());
					this.setInamici(playerData.getSkelly1(), playerData.getSkelly2(), playerData.getGolem(), playerData.getBoss());
					this.setPotiuni_l(playerData.getPotiuniLuate(), playerData.getHighestHealth());
					System.out.println("viataaaaa " + playerData.getHighestHealth());
					scoreDatabase.deleteHighestLevelRecord(name);
					break;
				}
			} else {
				// Nume nou – începem un joc nou de la 0
				this.setPlayerName(name);
				this.setPlayerStartingPosition(0, 10, 400);
				this.setInamici(0,0,0,0);
				this.setPotiuni_l(0, 10);
				System.out.println("Joc nou pentru: " + name);
				break;
			}
		}

		return name;
	}

	public void setPotions(int a) {
		pot = a;
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerStartingPosition(int level, float x, float y) {
		this.pentrux = x;
		this.pentruy = y;
		this.pentrulevel = level;
		System.out.println("s a setat nivelul" + pentrulevel);
	}

	public void resetPlayerStartingPosition() {
		this.pentrux = 10;
		this.pentruy = 10;
	}

	/**
	 * Returneaza baza de date cu scoruri.
	 * @return instanta ScoreDatabase
	 */
	public ScoreDatabase getScoreDatabase() {
		return scoreDatabase;
	}

	/**
	 * Seteaza starea inamicilor invinsi.
	 * @param skelly1_m schelet tip 1
	 * @param skelly2_m schelet tip 2
	 * @param golem_m golem
	 * @param boss_m boss
	 */
	public void setInamici(int skelly1_m, int skelly2_m, int golem_m, int boss_m) {
		this.skelly1_m = skelly1_m;
		this.skelly2_m = skelly2_m;
		this.golem_m = golem_m;
		this.boss_m = boss_m;
	}

	/**
	 * Seteaza numarul de potiuni si viata.
	 * @param potiuni_l potiuni colectate
	 * @param viata nivelul de viata
	 */
	public void setPotiuni_l(int potiuni_l, int viata) {
		this.potiuni_l = potiuni_l;
		this.viata = viata;
	}


	/**
	 * Actualizeaza starea curenta a jocului.
	 */
	//stari
	public void update() {
		switch (Gamestate.state) {
			case MENU:
				menu.update();
				break;
			case PLAYING:
				playing.update();
				break;
			case SCORES:
				scores.update();
				break;
			case OPTIONS:
				gameOptions.update();
				break;
			case QUIT:
			default:
				System.exit(0);
				break;
		}
	}


/**
 * Deseneaza starea curenta a jocului.
 * @param g contextul grafic
 */
	public void render(Graphics g) {
		switch (Gamestate.state) {
			case MENU:
				menu.draw(g);
				break;
			case PLAYING:
				playing.draw(g);
				break;
			case SCORES:
				scores.draw(g);
				break;
			case OPTIONS:
				gameOptions.draw(g);
				break;
			default:
				break;
		}
	}

	public Menu getMenu() {
		return menu;
	}

	public Scores getScores() {
		return scores;
	}

	public Playing getPlaying() {
		return playing;
	}

	public GameOptions getGameOptions() {
		return gameOptions;
	}



	//audio
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public AudioOptions getAudioOptions() {
		return audioOptions;
	}



}
