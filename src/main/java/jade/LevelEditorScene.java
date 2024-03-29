/**
 * 
 */
package jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import input.KeyListener;
import utility.AssetPool;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author adamsloan
 *
 */

public class LevelEditorScene extends Scene {

	private GameObject obj1;
	private Spritesheet sprites ;

	public LevelEditorScene() {

	}

	@Override
	public void init() {
		loadResources();
		this.camera = new Camera(new Vector2f(-250, 0));

		sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

		obj1 = new GameObject("mario", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
		obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
		this.addGameObjectToScene(obj1);

		GameObject obj2 = new GameObject("Goomba", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
		obj2.addComponent(new SpriteRenderer(sprites.getSprite(25)));
		this.addGameObjectToScene(obj2);

	}

	private void loadResources() {
		AssetPool.getShader("assets/shaders/default.glsl");
		AssetPool.addSpriteSheet("assets/images/spritesheet.png",
				new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"), 16, 16, 26, 0));
	}
	
	private int spriteIndex = 0;
	private float spriteFlipTime = 0.2f;
	private float spriteFlipTimeLeft = 0.0f;

	@Override
	public void update(float dt) {
		spriteFlipTimeLeft -=dt;
		if(spriteFlipTimeLeft <= 0) {
			spriteFlipTimeLeft = spriteFlipTime;
			spriteIndex++;
			if(spriteIndex>3) {
				spriteIndex = 0;
			}
			obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
		}
		obj1.transform.position.x += 10 * dt;

		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}

		this.renderer.render();
	}

}
