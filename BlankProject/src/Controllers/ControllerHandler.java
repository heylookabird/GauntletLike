package Controllers;

import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

public class ControllerHandler  {
	private Array<XboxListener> controllerHandlers;

	public static ControllerHandler controllerManager;
	
	public static void init(){
		controllerManager = new ControllerHandler();
		controllerManager.controllerHandlers = new Array<XboxListener>();
		
		for(int i = 0; i < Controllers.getControllers().size; i++){
			controllerManager.controllerHandlers.add(new XboxListener(i));
			Controllers.getControllers().get(i).addListener(controllerManager.controllerHandlers.get(i));
		}
	}
	private ControllerHandler() {

	}
	
	public void update(){
		for(XboxListener obj : controllerHandlers){
			obj.update();
		}
	
	}
	

}
