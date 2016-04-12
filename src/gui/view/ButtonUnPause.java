package gui.view;

import gameplayer.controller.BaseScreenController;
import gameplayer.controller.SplashScreenController;
import gui.controller.IScreenController;

public class ButtonUnPause extends ButtonParent{
	
	private BaseScreenController myControl;

	public ButtonUnPause(IScreenController myController, String buttonText, String imageName) {
		super(myController, buttonText, imageName);
		this.myControl = (BaseScreenController) myController;
	}

	@Override
	protected void setButtonAction() {
		button.setOnAction(e -> myControl.getMyGameController().toggleUnPause());
		
	}

}
