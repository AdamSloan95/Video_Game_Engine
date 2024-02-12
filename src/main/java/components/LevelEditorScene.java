/**
 * 
 */
package components;

/**
 * @author adamsloan
 *
 */
import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import renderer.Shader;

public class LevelEditorScene extends Scene {

	private float[] vertexArray = {
			// position 			//colour
			0.5f, -0.5f, 0.0f, 		1.0f, 0.0f, 0.0f, 1.0f, // bottom right 0
			-0.5f, 0.5f, 0.0f, 		0.0f, 1.0f, 0.0f, 1.0f, // top left 1
			0.5f, 0.5f, 0.0f, 		0.0f, 0.0f, 1.0f, 1.0f, // top right 2
			-0.5f, -0.5f, 0.0f, 	1.0f, 1.0f, 0.0f, 0.0f // bottom right 3

	};

	// IMPORTANT : MUST BE IN COUNTER-CLOCKWISE ORDER
	private int[] elementArray = {
			/*
			 * X	 	X
			 * 
			 * 
			 * X 		X
			 * 
			 */
			2, 1, 0, // Top right triangle
			0, 1, 3 // Bottom left triangle
	};

	private int vaoID, vboID, eboID;
	private Shader defaultShader;

	public LevelEditorScene() {
		

	}



	@Override
	public void init() {
		
		defaultShader = new Shader("./assets/shaders/default.glsl");
		defaultShader.compile();
		defaultShader.link();
		
		

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
		int floatSizeBytes = 4;
		int vertexSizeBytes = (positionsSize + colourSize) * floatSizeBytes;
		glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, colourSize, GL_FLOAT, false, vertexSizeBytes, positionsSize*floatSizeBytes);
		glEnableVertexAttribArray(1);
		
		

	}
	
	@Override
	public void update(float dt) {
		defaultShader.use();
		
		//bind the vao that we're using 
		glBindVertexArray(vaoID);
		
		//Enable the vertex Attribute Pointers
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, elementArray.length,GL_UNSIGNED_INT,0);
		
		//unbind
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		defaultShader.detach();

	}

}
