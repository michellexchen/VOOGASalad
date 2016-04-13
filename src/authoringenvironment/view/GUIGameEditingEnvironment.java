package authoringenvironment.view;

import java.util.ResourceBundle;

import authoringenvironment.controller.Controller;
import authoringenvironment.model.IEditableGameElement;
import gameengine.controller.GameInfo;
import gui.view.CheckBoxesHUDOptions;
import gui.view.IGUIEditingElement;
import gui.view.IGUIElement;
import gui.view.TextAreaGameDescriptionEditor;
import gui.view.TextAreaParent;
import gui.view.TextFieldGameNameEditor;
import gui.view.TextFieldWithButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class enables the author to edit and set various attributes of the game
 * including the game's name, description, and preview image. It will be
 * displayed as the left pane of the Main Screen.
 * 
 * @author Stephen
 *
 */

public class GUIGameEditingEnvironment implements IGUIElement, IGUIEditingElement {

	private IEditableGameElement myGameInfo;
	private static final String RESOURCE_BUNDLE_KEY = "mainScreenGUI";
	private static final double DEFAULT_PADDING = 10;
	private static final double CONTAINER_PREFERRED_WIDTH = 350.0;
	private static final int TEXT_AREA_ROWS = 5;
	private static final double TEXT_FIELD_WIDTH = 100.0;
	private static final double TEXT_FIELD_CONTAINER_SPACING = 10.0;
	private static final double TEXT_FIELD_CONTAINER_PADDING = 10.0;
	private static final double SCROLLPANE_WIDTH_BINDING_OFFSET = 30.0;
	private final ResourceBundle myResources;
	private VBox editingEnvironmentContainer;
	private Label welcomeMessage;
	private HBox nameEditorContainer;
	private VBox gameDescriptionEditor;
	private VBox previewImageContainer;
	private VBox HUDOptionsDisplay;
	private Controller controller;
	private ScrollPane myScrollPane;

	public GUIGameEditingEnvironment(GameInfo gameInfo, Controller controller) {
		this.myGameInfo = gameInfo;
		this.myResources = ResourceBundle.getBundle(RESOURCE_BUNDLE_KEY);
		this.controller = controller;
	}

	@Override
	public void setEditableElement(IEditableGameElement editable) {
		myGameInfo = editable;
	}

	private void initializeContainer() {
		editingEnvironmentContainer = new VBox();
		editingEnvironmentContainer.setPrefWidth(CONTAINER_PREFERRED_WIDTH);
		editingEnvironmentContainer.setStyle(myResources.getString("defaultBorderColor"));
	}

	private void initializeWelcomeMessage() {
		welcomeMessage = new LabelMainScreenWelcome(myResources.getString("mainScreenWelcome"));
	}

	private void initializeGameNameEditor() {
		String mainPrompt = myResources.getString("gameName");
		String textFieldPrompt = myResources.getString("enterGameName");
		TextFieldWithButton nameEditor = new TextFieldGameNameEditor(mainPrompt, textFieldPrompt, TEXT_FIELD_WIDTH);
		nameEditor.setEditableElement(myGameInfo);
		nameEditorContainer = (HBox) nameEditor.createNode();
		nameEditorContainer.setSpacing(TEXT_FIELD_CONTAINER_SPACING);
		nameEditorContainer.setPadding(new Insets(TEXT_FIELD_CONTAINER_PADDING));
	}

	private void initializeGameDescriptionEditor() {
		String prompt = myResources.getString("promptForGameDescription");
		String buttonText = myResources.getString("save");
		TextAreaParent descriptionEditor = new TextAreaGameDescriptionEditor(prompt, buttonText, TEXT_AREA_ROWS);
		descriptionEditor.setEditableElement(myGameInfo);
		gameDescriptionEditor = (VBox) descriptionEditor.createNode();
	}

	// hard coded values
	private void initializePreviewImageDisplay() {
		previewImageContainer = new VBox();
		// previewImageContainer.setAlignment(Pos.CENTER);
		previewImageContainer.setPadding(new Insets(DEFAULT_PADDING));
		Label previewImageLabel = new Label("Current Game Preview Image:");
		ImageView previewImage = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("default_game.jpg")));
		previewImageContainer.getChildren().addAll(previewImageLabel, previewImage);
	}

	private void initializeHUDOptionsDisplay() {
		CheckBoxesHUDOptions HUDOptions = new CheckBoxesHUDOptions(myGameInfo, controller);
		HUDOptionsDisplay = (VBox) HUDOptions.createNode();
	}

	private void initializeScrollPane() {
		myScrollPane = new ScrollPane();
		myScrollPane.prefWidthProperty()
				.bind(editingEnvironmentContainer.prefWidthProperty().add(SCROLLPANE_WIDTH_BINDING_OFFSET));
		myScrollPane.setContent(editingEnvironmentContainer);
		myScrollPane.setPadding(new Insets(DEFAULT_PADDING));
	}

	@Override
	public Node createNode() {
		initializeContainer();
		initializeWelcomeMessage();
		initializeGameNameEditor();
		initializeGameDescriptionEditor();
		initializePreviewImageDisplay();
		initializeHUDOptionsDisplay();
		initializeScrollPane();
		editingEnvironmentContainer.getChildren().addAll(welcomeMessage, nameEditorContainer, gameDescriptionEditor,
				previewImageContainer, HUDOptionsDisplay);
		return myScrollPane;
	}

}