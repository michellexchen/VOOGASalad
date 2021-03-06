package authoringenvironment.view;

import java.util.ResourceBundle;

import authoringenvironment.model.ActorRuleCreator;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;

/**
 * Tab contains ListView of images
 * 
 * @author AnnieTang
 *
 */
public class TabLibraryImages extends TabLibrary {
	private static final int STANDARD_IMAGE_HEIGHT = 20;

	public TabLibraryImages(ResourceBundle myResources, String tabText, ActorRuleCreator myRuleMaker) {
		super(myResources, tabText, myRuleMaker);
		setContent();
	}

	/**
	 * Set content of tab to list of images
	 */
	@Override
	void setContent() {
		fillFileNames();
		setLabels(FXCollections.observableArrayList());
		for (String imageName : getFileNames()) {
			ImageView iv = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(imageName)));
			iv.setFitHeight(STANDARD_IMAGE_HEIGHT);
			iv.setPreserveRatio(true);
			Label imageLabel = new Label(imageName, iv);
			if (getActorRuleCreator() != null) {
				setDragEvent(imageLabel, TransferMode.COPY);
			}
			getLabels().add(imageLabel);
		}
		setListView(new ListView<>(getLabels()));
	}

	/**
	 * Return image content of this tab
	 */
	@Override
	Node getContent() {
		return getListView();
	}
}
