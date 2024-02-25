package components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import jade.Component;
import renderer.Texture;

public class SpriteRenderer extends Component {
	private Vector4f colour;
	private Vector2f[] texCoords;
	private Texture texture;
	private Sprite sprite;

	public SpriteRenderer(Vector4f colour) {
		this.colour = colour;
		this.sprite = new Sprite(null);
	}

	public SpriteRenderer(Sprite sprite) {
		this.sprite = sprite;
		this.colour = new Vector4f(1, 1, 1, 1);
	}

	@Override
	public void start() {

	}

	@Override
	public void update(float dt) {

	}

	public Vector4f getColour() {
		return this.colour;
	}

	public Texture getTexture() {
		return sprite.getTexture();
	}

	public Vector2f[] getTexCoords() {
		return sprite.getTexCoords();
	}

}
