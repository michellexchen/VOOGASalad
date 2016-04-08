package gui.view;

import gameplayer.controller.BaseScreenController;
import gui.controller.IScreenController;

public class ButtonPause extends ButtonParent {
	
	private BaseScreenController myControl;

	public ButtonPause(IScreenController myController, String buttonText, String imageName) {
		super(myController, buttonText, imageName);
		this.myControl = (BaseScreenController) myControl;
	}

	@Override
	protected void setButtonAction() {
		button.setOnAction(e -> myControl.togglePause());
		
	}

}