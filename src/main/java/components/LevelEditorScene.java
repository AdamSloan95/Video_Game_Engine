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
public class LevelEditorScene extends Scene {
	private boolean changingScene = false;
	private float timeToChangeScene = 2.0f;
	
	public LevelEditorScene() {
		System.out.println("Inside level editor scene");
	}

	@Override
	public void update(float dt) {
		//test FPS
		//System.out.println(""+ (1.0f/dt) + "FPS");
		
		if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
			changingScene = true;
			
		}
		
		if(changingScene && timeToChangeScene < 0) {
			timeToChangeScene -= dt;
			
		}else if(changingScene) {
			Window.changeScene(1);
		}
		
	}

}
