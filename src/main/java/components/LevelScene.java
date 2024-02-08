/**
 * 
 */
package components;

import java.awt.event.KeyEvent;

import input.KeyListener;

/**
 * @author adamsloan
 *
 */
public class LevelScene  extends Scene{
	private boolean changingScene = false;
	private float timeToChangeScene = 2.0f;
	
	public LevelScene() {
		System.out.println("Inside level scene");
	}

	@Override
	public void update(float dt) {
		if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
			changingScene = true;
			
		}
		
		if(changingScene && timeToChangeScene < 0) {
			timeToChangeScene -= dt;
			
		}else if(changingScene) {
			Window.changeScene(0);
		}
	
	}

}
