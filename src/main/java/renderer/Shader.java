/**
 * 
 */
package renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author adamsloan
 *
 */
public class Shader {

	private int shaderProgramID;
	private String vertexSource;
	private String fragmentSource;
	private String filepath;
	private int vertexID, fragmentID;

	public Shader(String filepath) {
		this.filepath = filepath;
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String regex = "(#type)( )+([a-zA-z]+)";
			String[] splitString = source.split(regex);

			// find first pattern after #type 'pattern'
			int index = source.indexOf("#type") + 6;
			int endOfLine = source.indexOf("\n", index);
			String firstPattern = source.substring(index, endOfLine).trim();

			// find second pattern after #type 'pattern'
			index = source.indexOf("#type", endOfLine) + 6;
			endOfLine = source.indexOf("\n", index);
			String secondPattern = source.substring(index, endOfLine).trim();

			if (firstPattern.equals("vertex")) {
				vertexSource = splitString[1];
			} else if (firstPattern.equals("fragment")) {
				fragmentSource = splitString[1];
			} else {
				throw new IOException("Unexpected token '" + firstPattern + "'");
			}

			if (secondPattern.equals("vertex")) {
				vertexSource = splitString[2];
			} else if (secondPattern.equals("fragment")) {
				fragmentSource = splitString[2];
			} else {
				throw new IOException("Unexpected token '" + secondPattern + "'");
			}

			//System.out.println(vertexSource);
			//System.out.println(fragmentSource);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * compile shaders
	 */
	public void compile() {

		// load and compile the vertex shader
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		// pass shader source code
		glShaderSource(vertexID, vertexSource);
		// compile shader
		glCompileShader(vertexID);

		// check for errors
		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + filepath + "' \n\tVertex shader comilation failed.");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false : "";
		}

		// load and compile the fragment shader
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		// pass shader source code
		glShaderSource(fragmentID, fragmentSource);
		// compile shader
		glCompileShader(fragmentID);

		// check for errors
		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + filepath + "' \n\tfragment shader comilation failed.");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}

	}

	/*
	 * link shaders
	 */
	public void link() {
		// link shaders and check errors
		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexID);
		glAttachShader(shaderProgramID, fragmentID);
		glLinkProgram(shaderProgramID);

		// check link errors
		int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + filepath + "' \n\tLinking of shaders Failed.");
			System.out.println(glGetProgramInfoLog(shaderProgramID, len));
			assert false : "";
		}
	}

	public void use() {
		// bind shader program
		glUseProgram(shaderProgramID);

	}

	public void detach() {
		glUseProgram(0);

	}

}