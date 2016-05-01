package gui.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import authoringenvironment.model.IAuthoringActor;
import authoringenvironment.model.IEditableGameElement;
import authoringenvironment.model.IEditingElement;
import gameengine.controller.Level;
import gameengine.model.Actor;
import gameengine.model.IPlayActor;
import gameengine.model.Rule;
import gameengine.model.Actions.Destroy;
import gameengine.model.Triggers.ITrigger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Checkboxes for all HUD options.
 * 
 * @author amyzhao, stephen
 *
 */
public class CheckBoxesGarbageCollection extends Observable implements IGUIElement, IEditingElement {

	private static final String DELIMITER = ",";
	private static final String RESOURCE = "garbageCollectionOptions";
	private static final String OPTIONS = "Options";
	private static final String START_PROMPT = "StartPrompt";
	private static final String END_PROMPT = "EndPrompt";
	private static final String SELECT = "Select";
	private static final Double CONTAINER_SPACING = 10.0;
	private static final Double CONTAINER_PADDING = 10.0;
	private static final String LEFT = "Left";
	private static final String RIGHT = "Right";
	private static final String TOP = "Top";
	private static final String BOTTOM = "Bottom";
	private static final String COLLISION = "Collision";
	private static final String IMAGE = "Image";
	private static final String TRIGGER_DIRECTORY = "gameengine.model.Triggers.";
	private static final String X = "X";
	private static final String Y = "Y";
	private List<String> allSides;
	private Level myLevel;
	private VBox myContainer;
	private ResourceBundle myAttributesResources;
	private List<CheckBox> mySides;
	private GUIFactory myFactory;
	private Map<String, IAuthoringActor> garbageCollectors;
	private Button checkGarbageCollectorsButton;

	/**
	 * Constructs a CheckBoxesHUDOptions object for the given GameInfo object.
	 * 
	 * @param gameInfo:
	 *            GameInfo object.
	 * @param controller:
	 *            controller for the authoring environment.
	 */
	public CheckBoxesGarbageCollection() {
		this.myAttributesResources = ResourceBundle.getBundle(RESOURCE);
		this.myContainer = new VBox(CONTAINER_SPACING);
		myContainer.setPadding(new Insets(CONTAINER_PADDING));
		myFactory = new GUIFactory(myAttributesResources);
		mySides = new ArrayList<>();
		garbageCollectors = new HashMap<>();
		init();
	}

	/**
	 * Initializes the VBox containing the HUD checkboxes.
	 * 
	 * @param key:
	 *            key in resource file for the checkboxes to add.
	 * @param vbox:
	 *            vbox to add checkboxes into.
	 */
	private void init() {
		initAllSidesList();
		myContainer.getChildren().add(new Label(myAttributesResources.getString(START_PROMPT)));
		List<Node> checkboxes = addElements(OPTIONS, myContainer);
		int i = 0;
		while (i < checkboxes.size()) {
			HBox container = new HBox();
			for (int j = 0; j < 2; j++) {
				CheckBox cb = (CheckBox) checkboxes.get(i);
				cb.prefWidthProperty().bind(myContainer.widthProperty());
				mySides.add(cb);
				container.getChildren().add(cb);
				i++;
			}
			myContainer.getChildren().add(container);
		}
		checkGarbageCollectorsButton = new Button(SELECT);
		checkGarbageCollectorsButton.prefWidthProperty().bind(myContainer.widthProperty());
		checkGarbageCollectorsButton.setOnAction(e -> updateGarbageCollectingActors(myLevel.getActors()));
		myContainer.getChildren().add(new Label(myAttributesResources.getString(END_PROMPT)));
		myContainer.getChildren().add(checkGarbageCollectorsButton);
	}

	private void initAllSidesList() {
		allSides = new ArrayList<>(Arrays.asList(LEFT, RIGHT, TOP, BOTTOM));
	}
	
	private IAuthoringActor getGarbageCollector(String side) {
		if (garbageCollectors.containsKey(side)) {
			return garbageCollectors.get(side);
		} else {
			return new Actor();
		}
	}
	
	public void updateGarbageCollectingActors(List<IPlayActor> actors) {
		List<String> desiredSides = getSides();
		for (int i = 0; i < allSides.size(); i++) {
			String sideToCheck = allSides.get(i);
			IAuthoringActor garbageCollector = getGarbageCollector(sideToCheck);
			if (desiredSides.contains(sideToCheck)) {
				updateGarbageCollector(garbageCollector, sideToCheck, actors);
				garbageCollectors.put(sideToCheck, garbageCollector);
				myLevel.addGarbageCollector((IPlayActor) garbageCollector);
			} else {
				removeGarbageCollector(garbageCollector);
			}
		}
	}
	
	private void updateGarbageCollector(IAuthoringActor garbageCollector, String sideToCheck, List<IPlayActor> actors) {
		garbageCollector.setImageView(new ImageView(new Image(myAttributesResources.getString(sideToCheck + IMAGE))));
		garbageCollector.setImageViewName(myAttributesResources.getString(sideToCheck + IMAGE));
		positionGarbageCollector(garbageCollector, sideToCheck);
		updateGarbageCollectorCollisions(garbageCollector, actors, sideToCheck);
	}
	
	private void positionGarbageCollector(IAuthoringActor garbageCollector, String sideToCheck) {
		if (sideToCheck.equals(LEFT)) {
			garbageCollector.setX(Double.parseDouble(myAttributesResources.getString(sideToCheck + X)));
			garbageCollector.setY(Double.parseDouble(myAttributesResources.getString(sideToCheck + Y)));
		} else if (sideToCheck.equals(RIGHT)) {
			garbageCollector.setX(myLevel.getImageView().getFitWidth() + Double.parseDouble(myAttributesResources.getString(sideToCheck + X)));
			garbageCollector.setY(Double.parseDouble(myAttributesResources.getString(sideToCheck + Y)));
		} else if (sideToCheck.equals(TOP)){
			garbageCollector.setX(Double.parseDouble(myAttributesResources.getString(sideToCheck + X)));
			garbageCollector.setY(Double.parseDouble(myAttributesResources.getString(sideToCheck + Y)));
		}else {
			garbageCollector.setX(Double.parseDouble(myAttributesResources.getString(sideToCheck + X)));
			garbageCollector.setY(myLevel.getImageView().getFitHeight() + Double.parseDouble(myAttributesResources.getString(sideToCheck + Y)));
		}
	}
	private void updateGarbageCollectorCollisions(IAuthoringActor garbageCollector, List<IPlayActor> actors, String sideToCheck) {
		garbageCollector.getRules().clear();
		for (int j = 0; j < actors.size(); j++) {
			if (!garbageCollectors.keySet().contains(actors.get(j))) {
				ITrigger trigger = createCollisionTrigger(sideToCheck, (Actor) garbageCollector, (Actor) actors.get(j)); // use reflection from properties file
				garbageCollector.addRule(new Rule(trigger, new Destroy((Actor) actors.get(j))));
			}
		}
	}

	private void removeGarbageCollector(IAuthoringActor garbageCollector) {
		if (myLevel.getActors().contains(garbageCollector)) {
			myLevel.getGarbageCollectors().remove(garbageCollector);
			myLevel.removeActor((Actor) garbageCollector);
		}
	}
	
	private ITrigger createCollisionTrigger(String side, Actor garbageCollector, Actor actor) {
		Class<?> collision;
		try {
			String name = myAttributesResources.getString(side + COLLISION);
			collision = Class.forName(TRIGGER_DIRECTORY + name);
			Constructor<?> constructor = collision.getConstructor(Actor.class, Actor.class);
			return (ITrigger) constructor.newInstance(garbageCollector, actor);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Add checkbox elements to the vbox.
	 * 
	 * @param key:
	 *            key in resource file for elements to add.
	 * @param vbox:
	 *            container to add checkboxes to.
	 * @return list of elements added.
	 */
	private List<Node> addElements(String key, VBox vbox) {
		String[] elements = myAttributesResources.getString(key).split(DELIMITER);
		List<Node> createdElements = new ArrayList<>();
		for (int i = 0; i < elements.length; i++) {
			createdElements.add((myFactory.createNewGUIObject(elements[i]).createNode()));
		}
		return createdElements;
	}

	/**
	 * Get the HUD elements that the user has selected.
	 * 
	 * @return list of HUD elements that the user selected.
	 */
	public List<String> getSides() {
		List<String> sides = new ArrayList<String>();
		mySides.stream().filter(checkbox -> checkbox.isSelected())
		.forEach(checkbox -> sides.add(checkbox.getId()));
		return sides;
	}

	/**
	 * Sets the editable element for the CheckBoxes.
	 */
	@Override
	public void setEditableElement(IEditableGameElement element) {
		myLevel = (Level) element;
	}

	/**
	 * Returns the node for this object.
	 */
	@Override
	public Node createNode() {
		return myContainer;
	}

	@Override
	public void addNodeObserver(Observer observer) {
		this.addObserver(observer);
	}

}
