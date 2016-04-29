package gui.view;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import authoringenvironment.model.*;
import authoringenvironment.view.*;
import gameengine.model.*;
import gameengine.model.Actions.ApplyPhysics;
import gameengine.model.Triggers.ITrigger;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

/**
 * Checkbox object for ApplyPhysics option selection.
 * 
 * @author AnnieTang, amyzhao
 *
 */
public class CheckBoxApplyPhysics extends Observable implements IGUIElement, IEditingElement {
	private static final int PADDING = 10;
	private static final Object APPLY_PHYSICS = "ApplyPhysics";
	private static final int ZERO = 0;
	private String myPromptText;
	private int myWidth;
	private IEditableGameElement myEditableElement;
	private ActorEditingEnvironment aEE;

	/**
	 * Constructs a CheckBoxObject to edit a given element.
	 * @param promptText: option to check or un-check.
	 * @param width: width of the checkbox.
	 */
	public CheckBoxApplyPhysics(String promptText, int width, ActorEditingEnvironment aEE) {
		myPromptText = promptText;
		myWidth = width;
		this.aEE = aEE;
	}

	/**
	 * Creates the checkbox.
	 */
	@Override
	public Node createNode() {
		CheckBox checkbox = new CheckBox(myPromptText);
		checkbox.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
		checkbox.setPrefWidth(myWidth);
		checkbox.setAlignment(Pos.CENTER_LEFT);
		checkbox.setId(myPromptText);
		setEvent(checkbox);
		return checkbox;
	}
	
	private void setEvent(CheckBox checkbox){
		checkbox.setOnAction(event -> {
			if(checkbox.isSelected()){
				addApplyPhysics((IAuthoringActor) aEE.getEditable());
				System.out.println(((IAuthoringActor) aEE.getEditable()).getRules());
			}else{
				removeApplyPhysics((IAuthoringActor) aEE.getEditable());
				System.out.println(((IAuthoringActor) aEE.getEditable()).getRules());
			}
		});
	}
	
	private void addApplyPhysics(IAuthoringActor myActor){
		for(String triggerKey: myActor.getRules().keySet()){
			List<Rule> rulesForKey = myActor.getRules().get(triggerKey);
			ITrigger trigger = rulesForKey.get(ZERO).getMyTrigger();
			Rule toAdd = new Rule(trigger, new ApplyPhysics((Actor) aEE.getEditable()));
			myActor.addRule(toAdd);
		}
	}
	
	private void removeApplyPhysics(IAuthoringActor myActor){
		for(String triggerKey: myActor.getRules().keySet()){
			List<Rule> rules = myActor.getRules().get(triggerKey);
			for(int i=0; i<rules.size(); i++){
				if(rules.get(i).getMyAction().getClass().getSimpleName().equals(APPLY_PHYSICS)){
					myActor.removeRule(rules.get(i));
				}
			}
		}
	}
	
	/**
	 * Sets the element that this checkbox is editing.
	 */
	@Override
	public void setEditableElement(IEditableGameElement element) {
		myEditableElement = element;
	}

	/**
	 * Gets the element that this checkbox is editing.
	 * 
	 * @return game/level/actor that this checkbox is editing.
	 */
	protected IEditableGameElement getEditableElement() {
		return myEditableElement;
	}

	@Override
	public void addNodeObserver(Observer observer) {
		this.addObserver(observer);
	}

}