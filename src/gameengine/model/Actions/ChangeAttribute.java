package gameengine.model.Actions;

import gameengine.model.AttributeType;
import gameengine.model.IGameElement;

/**
 * @author blakekaplan
 */
public class ChangeAttribute extends Action {

	private AttributeType myType;
	private int myChange;

    public ChangeAttribute(IGameElement actor1, AttributeType type, Integer change){
        super(actor1);
        myType = type;
        myChange = change;
    }
    
    @Override
    public Object[] getParameters(){
    	return new Object[]{getGameElement(),myType,myChange};
    }

    @Override
    public void perform() {
        getGameElement().changeAttribute(myType,myChange);
    }

	public String getMyAttributeType() {
		return myType.toString();
	}
	
	public AttributeType getMyType() {
		return myType;
	}
	
	public int getMyValue() {
		return myChange;
	}
}
