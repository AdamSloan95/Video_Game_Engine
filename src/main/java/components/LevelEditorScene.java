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

public class LevelEditorScene extends Scene {
	private String vertexShaderSRC = "	#version 330 core \n" + "	\n" + "	layout (location = 0) in vec3 aPos;\n"
			+ "	layout (location = 1) in vec4 aColour;\n" + "	\n" + "	out vec4 fColour;\n" + "	\n"
			+ "	void main()\n" + "	{\n" + "		fColour = aColour;\n" + "		gl_Position = vec4(aPos,1.0);\n"
			+ "	}\n" + "";
	private String fragmentShaderSRC = "	#version 330 core\n" + "\n" + "	in vec4 fColour;\n" + "	out vec4 colour;\n"
			+ "\n" + "	void main(){\n" + "		colour = fColour;\n" + "	}";

	private int vertexID, fragmentID, shaderProgram;
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

	public LevelEditorScene() {

	}



	@Override
	public void init() {
		// compile and link shaders

		// load and compile the vertex shader
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		// pass shader source code
		glShaderSource(vertexID, vertexShaderSRC);
		// compile shader
		glCompileShader(vertexID);

		// check for errors
		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: 'defaultShader.glsl'\n\tVertex shader comilation failed.");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false : "";
		}

		// load and compile the fragment shader
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		// pass shader source code
		glShaderSource(fragmentID, fragmentShaderSRC);
		// compile shader
		glCompileShader(fragmentID);

		// check for errors
		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: 'defaultShader.glsl'\n\tfragment shader comilation failed.");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}

		// link shaders and check errors
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragmentID);
		glLinkProgram(shaderProgram);

		// check link errors
		success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: 'defaultShader.glsl'\n\tLinking of shaders Failed.");
			System.out.println(glGetProgramInfoLog(shaderProgram, len));
			assert false : "";

		}

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
		//bind shader program
		glUseProgram(shaderProgram);
		
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
		
		glUseProgram(0);

	}

}
