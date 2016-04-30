package authoringenvironment.controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;

import authoringenvironment.model.IAuthoringActor;
import authoringenvironment.model.IEditableGameElement;
import authoringenvironment.model.IEditingEnvironment;
import authoringenvironment.view.ImageviewActorIcon;
import authoringenvironment.view.LevelInspector;
import authoringenvironment.view.LevelPreview;
import authoringenvironment.view.LevelPreviewEditing;
import gameengine.controller.Level;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Level editing environment
 * 
 * @author amyzhao
 *
 */
public class LevelEditingEnvironment implements IEditingEnvironment, Observer {
	private static final String GUI_RESOURCE = "authoringGUI";
	private static final String BACKGROUND_COLOR = "-fx-background-color: darkgray";
	private static final double SUBSCENE_WIDTH = 1000;
	private static final int DOUBLE_CLICK = 2;
	private BorderPane myRoot;
	private LevelInspector myInspector;
	private ResourceBundle myResources;
	private VBox myLeftPane;
	private Level myLevel;
	private Map<IAuthoringActor, List<IAuthoringActor>> availableActors;
	private Pane myCenterPane;
	private Stage myStage;
	private Controller myController;
	private LevelPreviewEditing myLevelEditingPreview;
	private LevelPreview myLevelPreviewer;

	/**
	 * Constructor for a level editing environment.
	 * 
	 * @param controller:
	 *            authoring environment controller.
	 * @param actors:
	 *            list of currently available actors.
	 */
	public LevelEditingEnvironment(Map<IAuthoringActor, List<IAuthoringActor>> actors, Stage stage,
			Controller controller) {
		myResources = ResourceBundle.getBundle(GUI_RESOURCE);
		availableActors = actors;
		myRoot = new BorderPane();
		myStage = stage;
		myController = controller;
		myLevelPreviewer = new LevelPreview(myController);
		initializeEnvironment();
	}
	
	/**
	 * Initializes the level editing environment's center pane and left pane, as
	 * well as the ability to drag actors between the left and center pane.
	 */
	private void initializeEnvironment() {
		myRoot.setStyle(BACKGROUND_COLOR);
		initializeCenter();
		initializeLeftPane();
		updateDrag();
	}

	/**
	 * Initializes the left pane with a level inspector and an image/sound
	 * library.
	 */
	private void initializeLeftPane() {
		myLeftPane = new VBox();
		myLeftPane.prefHeightProperty().bind(myRoot.heightProperty());
		myRoot.setLeft(myLeftPane);
		myLeftPane.setMaxWidth(myStage.getWidth() - SUBSCENE_WIDTH);
		addChildrenToLeftPane();
	}

	private void addChildrenToLeftPane() {
		myInspector = new LevelInspector(myResources, availableActors.keySet(), this);
		myLeftPane.getChildren().add(myInspector.getPane());
		myInspector.getPane().prefHeightProperty().bind(myLeftPane.heightProperty());
	}

	/**
	 * Updates the drag behavior of the level editing environment to accommodate
	 * updates to currently available actors.
	 */
	private void updateDrag() {
		setCenterPaneDragOver();
		setCenterPaneDragDropped();
		for (ImageviewActorIcon icon: myInspector.getActorsTab().getIcons()) {
			setDragDetected(icon);
			setMouseClickBehavior(icon);	
		}
	}
	
	private void setMouseClickBehavior(ImageviewActorIcon icon) {
		icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == DOUBLE_CLICK) {
						myController.goToEditingEnvironment(icon.getRefActor(),
								myController.getActorEditingEnvironment());
					}
				}
			}
		});
	}

	/**
	 * Start drag and drop when drag is detected.
	 * 
	 * @param source:
	 *            node to be dragged.
	 */
	private void setDragDetected(ImageviewActorIcon source) {
		source.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = source.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(Integer.toString(source.getID()));
				db.setContent(content);
				event.consume();
			}
		});
	}

	/**
	 * When dragged over the center pane, copy the gesture source.
	 */
	private void setCenterPaneDragOver() {
		myLevelEditingPreview.getLevelPane().setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getGestureSource() != myLevelEditingPreview.getLevelPane() && event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}
		});
	}

	/**
	 * When an actor icon is dropped on the center pane, add its actor to the
	 * center pane and set the actor so that it moves in response to mouse
	 * drags.
	 */
	private void setCenterPaneDragDropped() {
		myLevelEditingPreview.getLevelPane().setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasString()) {
					ImageviewActorIcon icon = getIconById(Integer.parseInt(db.getString()));
					IAuthoringActor actor = icon.getActor();
					updateAvailableActors(actor, icon.getRefActor());
					addActorToLevel(actor);
					success = true;
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
	}
	
	private void updateAvailableActors(IAuthoringActor actor, IAuthoringActor refActor) {
		List<IAuthoringActor> val = availableActors.get(refActor);
		val.add(actor);
		availableActors.put(refActor, val);
	}
	
	private void addActorToLevel(IAuthoringActor actor) {
		myLevel.addActor(actor);
		myInspector.getGarbageCollector().updateGarbageCollectingActors(myLevel.getActors());
		myLevelEditingPreview.addActorToScene(actor);
	}

	/**
	 * Gets an actor by its ID.
	 * 
	 * @param id:
	 *            ID of actor of interest.
	 * @return actor with given ID.
	 */
	private ImageviewActorIcon getIconById(int id) {
		List<ImageviewActorIcon> icons = myInspector.getActorsTab().getIcons();
		for (int i = 0; i < icons.size(); i++) {
			if (icons.get(i).getID() == id) {
				return icons.get(i);
			}
		}
		return null;
	}

	/**
	 * Initialize the center pane.
	 */
	private void initializeCenter() {
		myLevelEditingPreview = new LevelPreviewEditing(this);
		myCenterPane = myLevelEditingPreview.getPane();
		myRoot.setCenter(myCenterPane);
	}

	/**
	 * Get LevelEditingEnvironment's root pane.
	 */
	@Override
	public Pane getPane() {
		return myRoot;
	}

	/**
	 * Update the level that is being edited and update the level inspector to
	 * reflect the current level.
	 */
	@Override
	public void setEditableElement(IEditableGameElement editable) {
		myLevel = (Level) editable;
		myLevelEditingPreview.updateLevelPreview(myLevel);
		myInspector.getGarbageCollector().setEditableElement(myLevel);
		updateActorsList();
		myInspector.getAttributesTab().updateEditable(myLevel);
	}

	public void changeBackgroundImage(Image image, File imageFile) {
		myLevelEditingPreview.changeBackgroundImage(image, imageFile);
	}

	/**
	 * Update the list of available actors and update the level inspector to
	 * reflect the currently available actors.
	 * 
	 * @param updatedActorsList:
	 *            up-to-date list of available actors.
	 */
	public void updateActorsList() {
		myInspector.getActorsTab().setAvailableActors(availableActors.keySet());
		updateDrag();
	}

	public Level getLevel() {
		return myLevel;
	}

	@Override
	public Stage getStage() {
		return myStage;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == null) {
			myLevelEditingPreview.resizeBackgroundBasedOnScrolling();
		} else {
			myController.updateRefActor((IAuthoringActor) arg);
			myLevelEditingPreview.addLevelActorsToScene();
		}
	}

	public Controller getController() {
		return myController;
	}
	
	public Set<IAuthoringActor> getAvailableActors() {
		return availableActors.keySet();
	}
	
	public void previewGame() {
		myLevelPreviewer.previewGame();
	}
}