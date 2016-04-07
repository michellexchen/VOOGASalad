package xmlTest;

import java.util.*;
/**
 * This class is intended to represent a game containing levels with actors.
 * @author colettetorres
 *
 */
public class Game extends Observable implements Observer {

	private String initialGameFile;
	private List<Level> levels;
	private GameInfo info;
	
	/**
	 * A game is instantiated with a list of all levels in the game and a level to start on.
	 * Upon instantiation, the actors from all levels are collected into a list and added to a map containing references from ID to actor.
	 * In addition, a map is created mapping all the actors contained in a level to the level ID 
	 * @param levelNum the first level to play in the game
	 * @param gameLevels all the levels in the game 
	 */
	
	public Game (GameInfo gameInfo, List<Level> gameLevels) {
		this(null, gameInfo, gameLevels);
	}
	
	public Game(String gameFilePath, GameInfo gameInfo, List<Level> gameLevels) {
		initialGameFile = gameFilePath;
		levels = gameLevels;
		info = gameInfo;
	}
	

	public String getInitialGameFile() {
		return initialGameFile;
	}

	public void setInitialGameFile(String initialGameFile) {
		this.initialGameFile = initialGameFile;
	}

	public GameInfo getInfo() {
		return info;
	}

	public void setInfo(GameInfo info) {
		this.info = info;
	}

	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}
	
	/**
	 * Gets a list of all levels in the game 
	 * @return a list of all levels in the game 
	 */
	public List<Level> getLevels() {
		return levels;
	}
	
	public void nextLevel(){
		info.setCurrentLevelNum(info.getCurrentLevelNum()+1);
	}
	
	/**
	 * Lets current level handle a trigger 
	 * @param myTrigger the trigger received from the game player 
	 */


	@Override
	public void update(Observable o, Object arg) {
		notifyObservers(arg);
		
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
	      
	      stringBuilder.append("Game [ ");
	      stringBuilder.append("\ninitialFile: ");
	      stringBuilder.append(initialGameFile);
	      stringBuilder.append("\ngameLevels: ");
	      stringBuilder.append(levels.toString());
	      stringBuilder.append("\nmyInfo: ");
	      stringBuilder.append(info.toString());
	      stringBuilder.append(" ]");
	      
	      return stringBuilder.toString();
	}
}
