package gameengine.controller;

import gameengine.model.Actor;
import gameengine.model.CollisionDetection;
import gameengine.model.ITrigger;
import gameengine.model.PhysicsEngine;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import authoringenvironment.model.IEditableGameElement;

/**
 * A Level is essentially a package of Actor objects. It is able to relay a Trigger to Actors when it receives one.
 *
 * @author blakekaplan
 */
public class Level implements Observer, ILevel, IEditableGameElement {

	private static final String DEFAULT_NAME = "Untitled";
	private static final String DEFAULT_IMAGE_NAME = "default_background.png";

    private List<Actor> myActors;
    private Map<String, List<Actor>> myTriggerMap;
    private String myName;
    private double myHeight;
    private double myWidth;
    private List<String> myHUDOptions;
    private String myScrollingDirection;
    private String myTermination;
    private String myWinningCondition;
    private String myLosingCondition;
    private String myBackgroundImgName;
	@XStreamOmitField
    private ImageView myBackground;


    /**
     * Instantiates the triggerMap and Actor list
     */
    public Level() {
        myActors = new ArrayList<>();
        myTriggerMap = new HashMap<>();
        myName = DEFAULT_NAME;
        myBackgroundImgName = DEFAULT_IMAGE_NAME;
        setImageView(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(myBackgroundImgName))));
    }

    /**
     * Calls for the appropriate response upon receiving a particular Trigger
     *
     * @param myTrigger A particular Trigger object sent from the game player
     */
    @Override
    public void handleTrigger(ITrigger myTrigger) {
        if (!myTriggerMap.containsKey(myTrigger.getTriggerName())) return;
        List<Actor> relevantActors = myTriggerMap.get(myTrigger.getTriggerName());
        for (Actor myActor : relevantActors) {
            if (myTrigger.evaluate(myActor)){
                myActor.performActionsFor(myTrigger.getTriggerName());
            }
        }
        //myCollisionDetector.detection(myActors); //Collision Detection/Resolution for each Actor
    }

    /**
     * Sets the Level's name
     *
     * @param name A name for the Level
     */
    @Override
    public void setName(String name) {
        myName = name;
    }
    
    public void setWidth(double width) {
    	myWidth = width;
    }
    
    public void setHeight(double height) {
    	myHeight = height;
    }
    
    public void setHUDOptions(List<String> options) {
    	myHUDOptions = options;
    }
    
    public void setScrollingDirection(String scrollingDirection) {
    	myScrollingDirection = scrollingDirection;
    }

    public void setTermination(String termination) {
    	myTermination = termination;
    }
    
    public void setWinningCondition(String winningCondition) {
    	myWinningCondition = winningCondition;
    }
    
    public void setLosingCondition(String losingCondition) {
    	myLosingCondition = losingCondition;
    }
    
    /**
     * Adds a new Actor to the Level and updates the triggerMap accordingly
     *
     * @param newActor The Actor to be added to the Level
     */
    @Override
    public void addActor(Actor newActor) {
        newActor.addObserver(this);
        myActors.add(newActor);
        Set<String> actorTriggers = newActor.getTriggers();
        for (String myTrigger : actorTriggers) {
            if (myTriggerMap.containsKey(myTrigger)) {
                List<Actor> levelActors = myTriggerMap.get(myTrigger);
                levelActors.add(newActor);
                myTriggerMap.put(myTrigger, levelActors);
            } else {
                List<Actor> levelActors = new ArrayList<>();
                levelActors.add(newActor);
                myTriggerMap.put(myTrigger, levelActors);
            }
        }
        
    }

    /**
     * Provides the Level's name
     *
     * @return The Level's name
     */
    @Override
    public String getName() {
        return myName;
    }

    /**
     * Provides the list of Actors that are present in the Level
     *
     * @return The List of Actors in the Level
     */
    @Override
    public List<Actor> getActors() {
        return myActors;
    }

	@Override
	public ImageView getImageView() {
		return myBackground;
	}

	@Override
	public void setImageView(ImageView imageView) {
		myBackground = imageView;
	}

	@Override
	public void setID(int ID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
	
    public String getMyBackgroundImgName() {
		return myBackgroundImgName;
	}

	public void setMyBackgroundImgName(String myBackgroundImgName) {
		this.myBackgroundImgName = myBackgroundImgName;
	}
	
	public String toString() {

	      StringBuilder stringBuilder = new StringBuilder();
	      
	      stringBuilder.append("\nLevel [ ");
	      stringBuilder.append("\nmyName: ");
	      stringBuilder.append(myName);
	      stringBuilder.append("\nbckImg: ");
	      stringBuilder.append(myBackgroundImgName);
	      stringBuilder.append("\nmyActors: ");
	      stringBuilder.append(myActors.toString());
	      stringBuilder.append("\nTriggerMap: ");
	      stringBuilder.append(myTriggerMap.toString());
	      stringBuilder.append("\nimg: ");
	      stringBuilder.append(myBackground);
	      stringBuilder.append(" ]");
	      
	      return stringBuilder.toString();
	}
	
	public void removeActor(Actor actor){
		myActors.remove(actor);
	}


    @Override
    public void update(Observable o, Object arg) {
        Actor myActor = (Actor) o;
        if (arg.equals("DESTROY")){
            myActors.remove(myActor);
        } else if (arg.equals("WINGAME")) {
        }
    }
}
