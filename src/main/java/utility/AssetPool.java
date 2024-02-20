/**
 * 
 */
package utility;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import renderer.Shader;
import renderer.Texture;

/**
 * @author adamsloan
 *
 */
public class AssetPool {
	private static Map<String, Shader> shaders = new HashMap<>();
	private static Map<String, Texture> textures = new HashMap<>();

	public static Shader getShader(String resourceName) {
		File file = new File(resourceName);

		if (shaders.containsKey(file.getAbsolutePath())) {
			return AssetPool.shaders.get(file.getAbsolutePath());
		} else {
			Shader shader = new Shader(resourceName);
			shader.compile();
			shader.link();
			AssetPool.shaders.put(file.getAbsolutePath(), shader);
			return shader;
		}
	}
	
	public static Texture getTexture(String resourceName) {
		File file = new File(resourceName);
		
		if(textures.containsKey(file.getAbsolutePath())) {
			return AssetPool.textures.get(file.getAbsolutePath());
		}else {
			Texture texture = new Texture(resourceName);
			AssetPool.textures.put(file.getAbsolutePath(), texture);
			return texture;
		}
	}
	
	

}
