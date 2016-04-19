package authoringenvironment.view;

import authoringenvironment.model.IAuthoringActor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ImageView that serves as an icon for an actor.
 * @author amyzhao
 *
 */
public class ImageviewActorIcon extends ImageView {
	private IAuthoringActor myActor;
	private int myID;
	private ImageView myImageView;
	private boolean onLevel;
	
	/**
	 * Construct an icon for a given actor.
	 * @param actor: actor to construct an icon for.
	 */
	public ImageviewActorIcon(IAuthoringActor actor, double height) {
		setImage(actor.getMyImageView().getImage());
		this.setFitHeight(height);
		this.setPreserveRatio(true);
		myActor = actor;
		myID = actor.getMyID();
		onLevel = false;
		updateImageView();
	}
	
	/**
	 * Gets the actor associated with this icon.
	 * @return my actor.
	 */
	public IAuthoringActor getActor() {
		return myActor;
	}
	
	/**
	 * Gets the ID of the actor associated with this icon. (ID of actor and ID of its icon are the same).
	 * @return my ID.
	 */
	public int getID() {
		return myID;
	}
	
	/**
	 * Gets the icon's imageview.
	 * @return copy of actor's imageview.
	 */
/*	public ImageView getImageView() {
		return myImageView;
	}*/
	
	/**
	 * Update the imageview based on the actor's current image.
	 */
	public void updateImageView() {
		setImage(myActor.getMyImageView().getImage());
		setPreserveRatio(true);
		if (onLevel) {
			setFitHeight(myActor.getMyImageView().getFitHeight());
		}
	}
	
	public void setOnLevel(boolean bool) {
		onLevel = bool;
	}
}
