/**
 * 
 */
package renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;


/**
 * @author adamsloan
 *
 */


public class Texture {
	
	private String filepath;
	private int textureID;
	private int width;
	private int height;
	
	public Texture(String filepath) {
		this.filepath = filepath;
		
		//generate texture on GPU
		textureID = glGenTextures();
		
		
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		
		//set the texture params
		//repeat image in both directions 
		
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		//when stretching ->pixelate
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
		
		//when shrinking ->pixelate
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = stbi_load(filepath,width,height,channels,0);
		
		if(image != null) {
			this.width = width.get(0);
			this.height = height.get(0);
			
			
			if(channels.get(0) == 3) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
			} else if(channels.get(0) == 4) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			}else {
				assert false : "Error: (Texture) Unknown number of channels '"+ channels.get(0)+"'";
				
			}
			
		}else {
			assert false : "Error: (Texture) could not load image '"+filepath +"'";
		}
		
		stbi_image_free(image);
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getWidth() {
		return this.width;
	}
		
	public int getHeight() {
		return this.height;
	}

}
