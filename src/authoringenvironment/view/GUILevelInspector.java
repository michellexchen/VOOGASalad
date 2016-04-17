package authoringenvironment.view;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import authoringenvironment.controller.Controller;
import authoringenvironment.model.IAuthoringActor;
import authoringenvironment.model.IEditableGameElement;
import gameengine.controller.Level;
import gameengine.model.Actor;
import gui.view.ButtonFileChooserBackgroundImage;
import gui.view.IGUI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class GUILevelInspector implements IGUI {
	private static final String BUTTON_LABEL = "Choose a new background image";
	private static final int BUTTON_HEIGHT = 30;
	private static final int BUTTON_WIDTH = 300;
	private static final String LEVEL_OPTIONS_RESOURCE = "levelEditorOptions";
	private static final String ACTORS = "Actors";
	private static final String LEVEL_ATTRIBUTES = "Level Attributes";
	private static final int SPACING = 5;
	private Level myLevel;
	private Controller myController;
	private Pane myPane;
	private TabActors myActorsTab;
	private TabAttributes myAttributesTab;
	private VBox myContainer;
	private LevelEditingEnvironment myLevelEditor;
	
	/**
	 * Constructor for Level Inspector.
	 * @param controller: authoring environment controller.
	 * @param myResources: resource bundle for authoring environment.
	 * @param availActors: list of currently available actors.
	 * @param level: level that is being edited.
	 */
	public GUILevelInspector(Controller controller, ResourceBundle myResources, List<IAuthoringActor> availActors, Level level, LevelEditingEnvironment editor) {
		myLevelEditor = editor;
		myController = controller;
		init(controller, myResources, availActors, level);
	}
	
	private void init(Controller controller, ResourceBundle myResources, List<IAuthoringActor> availActors, Level level) {
		myPane = new StackPane();		
		myContainer = new VBox(SPACING);
		myContainer.setAlignment(Pos.CENTER);
		myActorsTab = new TabActors(myResources, ACTORS, availActors);
		myAttributesTab = new TabAttributes(controller, myResources, LEVEL_ATTRIBUTES,LEVEL_OPTIONS_RESOURCE, level);
		addTabToContainer(myAttributesTab, false);
		ButtonFileChooserBackgroundImage button = new ButtonFileChooserBackgroundImage(myController, BUTTON_LABEL, null, BUTTON_WIDTH, BUTTON_HEIGHT, myController.getStage(), myLevelEditor);
		myContainer.getChildren().add(button.createNode());
		addTabToContainer(myActorsTab, true);
		myPane.getChildren().addAll(myContainer);
	}

	/**
	 * Adds a tab to the Level Inspector's container.
	 * @param tab: tab to add.
	 * @param bindToContainer: true if you want tab to bind to its individual container's height; false o.w.
	 */
	private void addTabToContainer(TabParent tab, boolean bindToContainer) {
		VBox container = new VBox();
		TabPane tabPane = new TabPane();
		container.getChildren().add(tabPane);
		tabPane.getTabs().add(tab.getTab());
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		if (bindToContainer) {
			VBox.setVgrow(container, Priority.ALWAYS);
			tabPane.prefHeightProperty().bind(container.heightProperty());
		}
		myContainer.getChildren().add(container);
	}
	
	/**
	 * Returns the pane that the level inspector is built on.
	 */
	@Override
	public Pane getPane() {
		return myPane;
	}
	
	/**
	 * Returns the tab of available actors.
	 * @return tab of available actors.
	 */
	public TabActors getActorsTab() {
		return myActorsTab;
	}
	
	/**
	 * Returns the tab of level attributes.
	 * @return tab of level attributes.
	 */
	public TabAttributes getAttributesTab() {
		return myAttributesTab;
	}
}
