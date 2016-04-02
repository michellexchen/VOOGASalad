package usecases;

import java.util.List;

import authoringenvironment.model.ICreatedActor;
import authoringenvironment.model.ICreatedLevel;
import authoringenvironment.model.LevelEditingEnvironment;
import gameengine.controller.ILevel;
import gameengine.model.IActor;
import javafx.scene.Group;
import javafx.scene.Scene;

public class LevelEditor implements LevelEditingEnvironment {

	private ILevel level;
	private List<IActor> actors;
	private Group root;
	private Scene scene;
	
	public LevelEditor(){
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeEnvironment() {
		root = new Group();
		scene = new Scene(root);	
	}

	@Override
	public void setLevel(ILevel level, List<IActor> actors) {
		this.level = level;
		this.actors = actors;
	}
	
	public Scene getScene() {
		return scene;
	}

}
