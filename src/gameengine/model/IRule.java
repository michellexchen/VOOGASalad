package gameengine.model;

import gameengine.model.Actions.Action;
import gameengine.model.Triggers.ITrigger;

/**
 * This interface defines the public methods for Rule objects. For our purposes,
 * Rules will be ways of packaging Triggers with their appropriate Action
 * response.
 *
 * @author blakekaplan
 */
public interface IRule {

	/**
	 * Gets the Trigger from the Rule
	 *
	 * @return The specific Trigger from the Rule
	 */
	public ITrigger getMyTrigger();

	/**
	 * Gets the Action from the Rule
	 *
	 * @return The specific Action from the Rule
	 */
	public Action getMyAction();
}
