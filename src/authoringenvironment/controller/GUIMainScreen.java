package authoringenvironment.controller;

import java.util.*;

import authoringenvironment.controller.*;
import authoringenvironment.model.*;
import authoringenvironment.view.HBoxDisplayHeader;
import authoringenvironment.view.HBoxDisplayHeaderActor;
import authoringenvironment.view.HBoxDisplayHeaderLevel;
import authoringenvironment.view.PreviewUnitWithEditable;
import authoringenvironment.view.PreviewUnitWithLevel;
import gameengine.controller.*;
import gui.view.IGUI;
import javafx.beans.binding.DoubleExpression;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * 
 * This class sets up all the GUI elements on the Game Authoring Environment's
 * Main Screen
 * 
 * @author Stephen
 *
 */

public class GUIMainScreen implements IGUI, Observer {

	private static final int NUM_SCROLLPANES = 2;
	private static final int SCROLLPANE_BAR_WIDTH = 30;
	private VBox actorPreviewContainer, levelPreviewContainer, createdLevelsDisplay, createdActorsDisplay;
	private ScrollPane actorScrollPane, levelScrollPane;
	private HBoxDisplayHeader actorsDisplayHeader, levelsDisplayHeader;
	private HBox centerPane;
	private BorderPane borderPane;
	private List<PreviewUnitWithEditable> allPreviewUnits;
	private List<PreviewUnitWithLevel> levelPreviewUnits;
	private List<Level> levels;
	private IEditingEnvironment levelEditor;
	private GameEditingEnvironment gameEditor;
	private Stage stage;
	private Controller controller;

	public GUIMainScreen(GameEditingEnvironment gameEditor, Controller controller, Stage stage, List<Level> levels,
			IEditingEnvironment levelEditor) {
		this.gameEditor = gameEditor;
		this.levels = levels;
		this.levelEditor = levelEditor;
		this.controller = controller;
		this.stage = stage;
		allPreviewUnits = new ArrayList<>();
		levelPreviewUnits = new ArrayList<>();
		actorScrollPane = new ScrollPane();
		levelScrollPane = new ScrollPane();
		initializeEnvironment();
	}

	/**
	 * Initializes the Main Screen's GUI Elements
	 */
	public void initializeEnvironment() {
		initializeBorderPane();
		initializeCenterPane();
		initializeCreatedEditablesDisplay();
		initializeCreatedEditablesDisplayHeaders();
		initializeEditableGameElementPreviewContainers();
		initializeScrollPanes();
		initializeGameEditingEnvironment();
		createdActorsDisplay.getChildren().addAll(actorsDisplayHeader.getHBox(), actorScrollPane);
		createdLevelsDisplay.getChildren().addAll(levelsDisplayHeader.getHBox(), levelScrollPane);
		centerPane.getChildren().addAll(createdActorsDisplay, createdLevelsDisplay);
		borderPane.setCenter(centerPane);
	}

	/**
	 * Initializes the Game Editing Environment and sets it as the BorderPane's
	 * left pane
	 */
	private void initializeGameEditingEnvironment() {
		borderPane.setLeft(gameEditor.getNode());
	}

	/**
	 * Initializes the BorderPane for the MainScreen
	 */
	private void initializeBorderPane() {
		borderPane = new BorderPane();
		bindNodeSizeToGivenSize(borderPane, stage.widthProperty(), stage.heightProperty());
	}

	/**
	 * Initializes the HBox containing the two ScrollPanes holding the labels
	 * representing the actors and levels to be edited
	 */
	private void initializeCenterPane() {
		centerPane = new HBox();
		bindNodeSizeToGivenSize(centerPane, borderPane.widthProperty(), borderPane.heightProperty());
	}

	private void initializeCreatedEditablesDisplay() {
		createdActorsDisplay = new VBox();
		createdLevelsDisplay = new VBox();
		bindNodeSizeToGivenSize(createdActorsDisplay, centerPane.widthProperty().divide(NUM_SCROLLPANES), null);
		bindNodeSizeToGivenSize(createdLevelsDisplay, centerPane.widthProperty().divide(NUM_SCROLLPANES), null);
	}

	private void initializeCreatedEditablesDisplayHeaders() {
		actorsDisplayHeader = new HBoxDisplayHeaderActor(createdActorsDisplay.prefWidthProperty());
		levelsDisplayHeader = new HBoxDisplayHeaderLevel(createdLevelsDisplay.prefWidthProperty());
		levelsDisplayHeader.addObserver(this);
	}

	/**
	 * Initializes the ScrollPanes holding the labels representing the actors
	 * and levels to be edited
	 */
	private void initializeScrollPanes() {
		initScrollPane(actorScrollPane, createdActorsDisplay.widthProperty(), createdActorsDisplay.heightProperty(),
				actorPreviewContainer, actorsDisplayHeader.getHBox().heightProperty());
		initScrollPane(levelScrollPane, createdLevelsDisplay.widthProperty(), createdLevelsDisplay.heightProperty(),
				levelPreviewContainer, levelsDisplayHeader.getHBox().heightProperty());
	}

	/**
	 * Initializes a ScrollPane and has its size bound to half of the center
	 * pane's width and sets its contents to the container holding the elements
	 * to be placed within the ScrollPane
	 * 
	 * @param scrollPane:
	 *            scrollPane whose size is to be bound and whose content is to
	 *            be set
	 * @param container:
	 *            the container holding the elements to be placed within the
	 *            ScrollPane
	 */
	private void initScrollPane(ScrollPane scrollPane, DoubleExpression bindWidth, DoubleExpression bindHeight,
			Node initialContent, DoubleExpression bindHeightOffset) {
		bindNodeSizeToGivenSize(scrollPane, bindWidth, bindHeight.subtract(bindHeightOffset));
		scrollPane.setContent(initialContent);
	}

	/**
	 * Initializes the VBox containers holding labels representing actors and
	 * levels to be edited
	 */
	private void initializeEditableGameElementPreviewContainers() {
		actorPreviewContainer = createEditableGameElementPreviewContainer(actorScrollPane);
		levelPreviewContainer = createEditableGameElementPreviewContainer(levelScrollPane);
	}

	/**
	 * Initializes a single Label container and binds its size to a ScrollPane's
	 * width and height
	 * 
	 * @param parentScrollPane
	 *            - ScrollPane to which the Label container's width and height
	 *            will be bound
	 * @return the Label container initialized
	 */
	private VBox createEditableGameElementPreviewContainer(ScrollPane parentScrollPane) {
		VBox container = new VBox();
		bindNodeSizeToGivenSize(container, parentScrollPane.widthProperty().subtract(SCROLLPANE_BAR_WIDTH),
				parentScrollPane.heightProperty());
		return container;
	}

	/**
	 * Binds a region to a given width and height
	 * 
	 * @param child
	 *            - region whose width and height are to be bound
	 * @param width
	 *            - the width the child's width will be bound to
	 * @param height
	 *            - the height the child's height will be bound to
	 */
	private void bindNodeSizeToGivenSize(Region child, DoubleExpression width, DoubleExpression height) {
		if (width != null)
			child.prefWidthProperty().bind(width);
		if (height != null)
			child.prefHeightProperty().bind(height);
	}

	/**
	 * Returns the BorderPane containing all GUI elements in the MainScreen
	 */
	public Pane getPane() {
		return borderPane;
	}

	/**
	 * Creates a LabelClickable associated with an Actor
	 * 
	 * @param actor:
	 *            actor to be linked to LabelClickable being created
	 * @param actorEditor:
	 *            IEditingEnvironment in which Actor is to be edited
	 * @return a LabelClickable associated with an Actor
	 */
	public void createActorPreviewUnit(IEditableGameElement actor, IEditingEnvironment actorEditor) {
		PreviewUnitWithEditable actorPreviewUnit = new PreviewUnitWithEditable(actor, actorEditor);
		initializePreviewUnit(actorPreviewUnit, actorPreviewContainer);
	}

	/**
	 * 
	 * @param level:
	 *            level to be linked to LabelClickable being created
	 * @param levelEditor:
	 *            IEditingEnvironment in which Level is to be edited
	 * @return a LabelClickable associated with a Level
	 */
	public void createLevelPreviewUnit(IEditableGameElement level, IEditingEnvironment levelEditor) {
		PreviewUnitWithLevel levelPreviewUnit = new PreviewUnitWithLevel(level, levelEditor);
		initializePreviewUnit(levelPreviewUnit, levelPreviewContainer);
		levelPreviewUnits.add(levelPreviewUnit);
	}

	private void initializePreviewUnit(PreviewUnitWithEditable previewUnit, VBox parent) {
		previewUnit.addObserver(controller);
		HBox previewUnitHBox = previewUnit.getHBox();
		bindNodeSizeToGivenSize(previewUnitHBox, parent.widthProperty(), null);
		parent.getChildren().add(previewUnitHBox);
		allPreviewUnits.add(previewUnit);
		updatePreviewUnits();
	}

	/**
	 * Updates all preview units to account for any changes in the name or
	 * image of Actors and Levels
	 */
	public void updatePreviewUnits() {
		allPreviewUnits.stream().forEach(unit -> unit.update());
	}

	/**
	 * Reorders Level Labels so that Labels with Levels that appear earlier in
	 * the game are displayed toward the top of the ScrollPane containing all
	 * Level Labels
	 */
	private void reorderLevels() {
		LevelPreviewUnitReorderer levelReorderer = new LevelPreviewUnitReorderer(levelPreviewUnits,
				levelPreviewContainer, levels, levelEditor, allPreviewUnits, this);
		levelReorderer.reorderLevels();
	}

	/**
	 * Reorders levels when observable Reorder Levels button is clicked
	 */
	@Override
	public void update(Observable o, Object arg) {
		reorderLevels();
	}

}