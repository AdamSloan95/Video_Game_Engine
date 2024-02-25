package components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import jade.Component;
import jade.Transform;
import renderer.Texture;

public class SpriteRenderer extends Component {

	private Vector4f colour;
	private Sprite sprite;
	private Transform lastTransform;
	private boolean isDirty = false;

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
		this.lastTransform = gameObject.transform.copy();

	}

	@Override
	public void update(float dt) {
		if (!this.lastTransform.equals(this.gameObject.transform)) {
			this.gameObject.transform.copy(this.lastTransform);
			isDirty = true;
		}
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

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		isDirty = true;
	}

	public void setColour(Vector4f colour) {

		if (!(this.colour == colour)) {
			this.colour.set(colour);
			isDirty = true;
		}
	}
	
	public boolean isDirty() {
		return this.isDirty;
	}
	
	public void setClean() {
		this.isDirty = false;
	}

}
