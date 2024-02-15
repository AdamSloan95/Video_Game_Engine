/**
 * 
 */
package jade;

/**
 * @author adamsloan
 *
 */
import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import components.FontRenderer;
import components.SpriteRenderer;
import renderer.Shader;
import renderer.Texture;
import utility.Time;

public class LevelEditorScene extends Scene {

	private float[] vertexArray = {
			// position 					//colour 				//uv co-ordinates
			100.5f, -0.5f, 0.0f, 1.0f, 		0.0f, 0.0f, 1.0f, 		1, 1, 			// bottom right 0
			-0.5f, 100.5f, 0.0f, 0.0f, 		1.0f, 0.0f, 1.0f, 		0, 0, 			// top left 1
			100.5f, 100.5f, 0.0f, 0.0f, 	0.0f, 1.0f, 1.0f, 		1, 0, 			// top right 2
			-0.5f, -0.5f, 0.0f, 1.0f, 		1.0f, 0.0f, 0.0f, 		0, 1 			// bottom right 3

	};

	// IMPORTANT : MUST BE IN COUNTER-CLOCKWISE ORDER
	private int[] elementArray = {
			/*
			 * X X
			 * 
			 * 
			 * X X
			 * 
			 */
			2, 1, 0, // Top right triangle
			0, 1, 3 // Bottom left triangle
	};

	private int vaoID, vboID, eboID;
	private Shader defaultShader;
	private Texture testTexture;
	
	GameObject testObj;
	private boolean firstTime = false;

	public LevelEditorScene() {

	}

	@Override
	public void init() {
		System.out.println("creating test object");
		this.testObj = new GameObject("test object");
		this.testObj.addComponent(new SpriteRenderer());
		this.testObj.addComponent(new FontRenderer());
		this.addGameObjectToScene(this.testObj);
		
		
		this.camera = new Camera(new Vector2f());

		defaultShader = new Shader("./assets/shaders/default.glsl");
		defaultShader.compile();
		defaultShader.link();
		this.testTexture =new Texture("./assets/images/testImage.png");

		// Generate VAO, VBO and EBO buffer objects and send to GPU
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		// create a float buffer of vertices
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip(); // make sure to flip

		// Create VBO upload
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

		// Create the indices and upload
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();

		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

		// add vertex attribute pointers
		int positionsSize = 3;
		int colourSize = 4;
		int uvSize = 2;
		int vertexSizeBytes = (positionsSize + colourSize+ uvSize) * Float.BYTES;
		
		glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, colourSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colourSize) * Float.BYTES);
		glEnableVertexAttribArray(2);

	}

	@Override
	public void update(float dt) {
		camera.position.x -= dt * 50.0f;
		camera.position.y -= dt * 20.0f;
		
		
		//upload texture to shader
		defaultShader.uploadTexture("TEX_SAMPLER", 0);
		glActiveTexture(GL_TEXTURE0);
		testTexture.bind();
		

		defaultShader.use();
		defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		defaultShader.uploadMat4f("uView", camera.getViewMatrix());
		defaultShader.uploadFloat("uTime", Time.getTime());

		// bind the vao that we're using
		glBindVertexArray(vaoID);

		// Enable the vertex Attribute Pointers

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

		// unbind

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);

		glBindVertexArray(0);

		defaultShader.detach();
		
		
		if(!firstTime) {
			System.out.println("Creating Game Object");
			GameObject go = new GameObject("Game Test 2");
			go.addComponent(new SpriteRenderer());
			this.addGameObjectToScene(go);
			firstTime = true;
		}
		
		
		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}

	}

}