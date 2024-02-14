/**
 * 
 */
package renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

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
	private boolean beingUsed = false;

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

			// System.out.println(vertexSource);
			// System.out.println(fragmentSource);

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
		if (!beingUsed) {
			glUseProgram(shaderProgramID);
			beingUsed = true;
		}

	}

	public void detach() {
		glUseProgram(0);
		beingUsed = false;

	}

	/*
	 * Matrix uploads
	 */

	public void uploadMat4f(String varName, Matrix4f mat4) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);

		glUniformMatrix4fv(varLocation, false, matBuffer);
	}

	public void uploadMat3f(String varName, Matrix3f mat3) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer);

		glUniformMatrix3fv(varLocation, false, matBuffer);
	}

	public void uploadMat2f(String varName, Matrix2f mat2) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
		mat2.get(matBuffer);

		glUniformMatrix2fv(varLocation, false, matBuffer);
	}

	/*
	 * Vector uploads
	 */

	public void uploadVec4f(String varName, Vector4f vec4) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform4f(varLocation, vec4.x, vec4.y, vec4.z, vec4.w);
	}

	public void uploadVec3f(String varName, Vector3f vec3) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform3f(varLocation, vec3.x, vec3.y, vec3.z);
	}

	public void uploadVec2f(String varName, Vector2f vec2) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform2f(varLocation, vec2.x, vec2.y);
	}

	/*
	 * Float and int uploads
	 */

	public void uploadFloat(String varName, float val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1f(varLocation, val);
	}

	public void uploadInt(String varName, int val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, val);

	}
	
	public void uploadTexture(String varName, int slot) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, slot);

	}
	
	

}
