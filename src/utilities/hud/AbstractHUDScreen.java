package Utilities.HUD;

import java.util.Observable;

public abstract class AbstractHUDScreen extends Observable{
	public abstract void handleChange(ValueChange change);
	public abstract void init();
}
