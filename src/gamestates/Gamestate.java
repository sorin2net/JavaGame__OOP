package gamestates;

/**
 * enumerarea tuturor starilor de joc
 */
public enum Gamestate {

	PLAYING, MENU, OPTIONS, QUIT, SCORES;

	public static Gamestate state = MENU;

}
