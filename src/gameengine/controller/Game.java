package gameengine.controller;

import java.util.*;

import gameengine.model.Actor;
import gameengine.model.CollisionDetection;
import gameengine.model.IActor;
import gameengine.model.ITrigger;
import gameengine.model.PhysicsEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableMap;
import javafx.util.Duration;

/**
 * This class is intended to represent a game containing levels with actors.
 * @author colettetorres
 *
 */
public class Game extends Observable implements Observer {
	public static final int SIZE = 400;
	public static final int FRAMES_PER_SECOND = 60;
	private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;

	private String initialGameFile;
	private List<Level> levels;
	private GameInfo info;
	private PhysicsEngine myPhysicsEngine;
	private CollisionDetection myCollisionDetector;
	private Timeline animation;
	private List<Actor> currentActors;
	private List<Actor> deadActors;
	private ObservableMap<String, Object> HUDData;
	
	
	/**
	 * A game is instantiated with a list of all levels in the game and a level to start on.
	 * Upon instantiation, the actors from all levels are collected into a list and added to a map containing references from ID to actor.
	 * In addition, a map is created mapping all the actors contained in a level to the level ID 
	 * @param levelNum the first level to play in the game
	 * @param gameLevels all the levels in the game 
	 */
	public Game(String gameFilePath, GameInfo gameInfo, List<Level> gameLevels) {
		initialGameFile = gameFilePath;
		levels = gameLevels;
		info = gameInfo;
		setCurrentActors(new ArrayList<Actor>());
		setDeadActors(new ArrayList<Actor>());
        myPhysicsEngine = new PhysicsEngine();
        myCollisionDetector = new CollisionDetection(myPhysicsEngine);
		
		initTimeline();
		
	}
	

	private void initTimeline() {
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
	}
	
	public Game(GameInfo gameInfo, List<Level> gameLevels) {
		this(null, gameInfo, gameLevels);
	}
	
	public Game(List<Level> gameLevels) {
		this(new GameInfo(), gameLevels);
	}
	
	public void startGame(){
		setCurrentActors(getCurrentLevel().getActors());
		initActors();
		animation.play();
	}
	
	private void step(){
		physicsUpdate();
		myCollisionDetector.detection(getCurrentActors());
		updateActors();
	}
	
	private void physicsUpdate(){
		for(Actor a: getCurrentActors()){
			myPhysicsEngine.tick(a);
		}
	}
	
	private void initActors(){
		for(Actor a: getCurrentActors()){
			a.addObserver(this);
			a.setEngine(myPhysicsEngine);
		}
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
		animation.stop();
		setCurrentLevel(info.getCurrentLevelNum()+1);
	}
	
	/**
	 * Lets current level handle a trigger 
	 * @param myTrigger the trigger received from the game player 
	 */
	public void handleTrigger(ITrigger myTrigger) {
		getCurrentLevel().handleTrigger(myTrigger);
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);
	}

	public List<Actor> getActors() {
		return getLevels().get(getInfo().getCurrentLevelNum()).getActors();
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
	
	public Level getCurrentLevel(){
		return levels.get(info.getCurrentLevelNum());
	}
	
	public void setCurrentLevel(int levelNum){
		info.setCurrentLevelNum(levelNum);
	}
	
	public void updateActors(){
		for(Actor a: getCurrentActors()){
			if(a.isDead()){
				deadActors.add(a);
			}
		}
		if(deadActors.size()!=0){
			removeDeadActors();
		}
		setCurrentActors(getCurrentLevel().getActors());
	}


	private void removeDeadActors() {
		setChanged();
		notifyObservers("updateActors");
		getCurrentLevel().removeActors(deadActors);	
		deadActors.clear();
	}


	public List<Actor> getDeadActors() {
		return deadActors;
	}


	public void setDeadActors(List<Actor> deadActors) {
		this.deadActors = deadActors;
	}


	public List<Actor> getCurrentActors() {
		return currentActors;
	}


	public void setCurrentActors(List<Actor> currentActors) {
		this.currentActors = currentActors;
	}

}
