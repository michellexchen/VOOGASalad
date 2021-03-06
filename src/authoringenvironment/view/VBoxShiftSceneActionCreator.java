package authoringenvironment.view;

import java.util.ResourceBundle;

import gameengine.controller.Level;
import gameengine.model.IGameElement;
import gameengine.model.Actions.Action;
import gameengine.model.Actions.ShiftScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * VBoxShiftSceneActionCreator
 * @author amyzhao
 *
 */
public class VBoxShiftSceneActionCreator extends VBox implements ILevelActionCreator {
	private static final String LABEL_TEXT = "ShiftSceneLabelText";
	private static final String DELIMITER = ",";
	private static final String DIRECTION = "ShiftSceneDirections";
	private static final String EMPTY = "";
	private IGameElement myElement;
	private ResourceBundle myResources;
	private TextField myShiftAmountTextField;
	private ComboBox myDirectionComboBox;
	private ObservableList<String> directionOptions;
	
	/**
	 * Constructor for VBoxShiftSceneActionCreator.
	 * @param resources: resource bundle to use.
	 * @param level: level to add action to.
	 */
	public VBoxShiftSceneActionCreator(ResourceBundle resources, IGameElement level) {
		myElement = level;
		myResources = resources;
		init();
	}
	
	/**
	 * Initialize action options box.
	 */
	private void init() {
		String[] labelText = myResources.getString(LABEL_TEXT).split(DELIMITER);
		makeShiftAmountBox(labelText[0]);
		makeDirectionBox(labelText[1]);
	}
	
	/**
	 * Make the hbox to select a shift amount.
	 * @param labelText: label text for the box.
	 */
	private void makeShiftAmountBox(String labelText) {
		HBox container = new HBox();
		Label label = new Label(labelText);
		myShiftAmountTextField = new TextField();
		container.getChildren().addAll(label, myShiftAmountTextField);
		this.getChildren().add(container);
	}
	
	/**
	 * Make the hbox to choose a direction.
	 * @param labelText: label text for the box.
	 */
	private void makeDirectionBox(String labelText) {
		HBox container = new HBox();
		Label label = new Label(labelText);
		directionOptions = FXCollections.observableArrayList(myResources.getString(DIRECTION).split(DELIMITER));
		myDirectionComboBox = new ComboBox<>(directionOptions);
		container.getChildren().addAll(label, myDirectionComboBox);
		this.getChildren().add(container);
	}
	
	@Override
	public Action createAction() {
		if (!myShiftAmountTextField.getText().equals(EMPTY)) {
			return new ShiftScene((Level) myElement, (String) myDirectionComboBox.getValue(), Double.parseDouble(myShiftAmountTextField.getText()));
		}
		return null;
	}

}
