/**
 * 
 */
package renderer;

import components.SpriteRenderer;
import jade.Window;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL20.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

/**
 * @author adamsloan
 *
 */
public class RenderBatch {
	//Vertex
	//======
	//Position			colour
	//float, float 		float, float,float, float
	private final int POS_SIZE = 2;
	private final int COLOUR_SIZE = 2;
	private final int POS_OFFSET = 0;
	private final int COLOUR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final int VERTEX_SIZE = 6;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
	
	private SpriteRenderer[] sprites;
	private int numSprites;
	private boolean hasRoom;
	private float[] vertices;
	
	private int vaoID, vboID;
	private int maxBatchSize;
	private Shader shader;
	
	public RenderBatch(int maxBatchSize) {
		shader = new Shader("assets/shaders/default.glsl");
		shader.compile();
		this.sprites = new SpriteRenderer[maxBatchSize];
		this.maxBatchSize = maxBatchSize;
		
		//4 verices quads
		vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
		this.hasRoom = true;
	}
	
	public void start() {
		//generate and bind a Vertex Array Object
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		//allocate space for vertices
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER,vboID);
		glBufferData(GL_ARRAY_BUFFER, vertices.length*Float.BYTES, GL_DYNAMIC_DRAW);
		
		//Create and upload indices buffer
		int eboID = glGenBuffers();
		int[] indices = generateIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		//Enable buffer attribute pointers
		
		//position
		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
		glEnableVertexAttribArray(0);
		
		//size
		glVertexAttribPointer(1, COLOUR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOUR_OFFSET);
		glEnableVertexAttribArray(1);
		
	}
	
	public void addSprite(SpriteRenderer spr) {
		//get the index and add render Object
		int index = this.numSprites;
		this.sprites[index] = spr;
		this.numSprites++;
		
		//add propeties to local vertices array
		loadVertexProperties(index);
		
		if(numSprites >= this.maxBatchSize) {
			this.hasRoom = false;
		}
		
	}
	
	public void render() {
		//for now we will re-buffer all data every frame
		
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		
		//use shader
		shader.use();
		shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
		shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());
		
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, this.numSprites*6,GL_UNSIGNED_INT,0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		shader.detach();
		
		
		
	}
	
	private int[] generateIndices() {
		//6 indices per quad(3 per triangle)
		int[] elements = new int[6*maxBatchSize];
		
		for(int i = 0; i< maxBatchSize;i++) {
			loadElementIndices(elements,i);
		}
		
		return elements;
	}
	
	private void loadElementIndices(int[] elements, int index) {
		int offsetArrayIndex = 6 * index;
		int offset = 4 * index;
		
		//3,2,0,0,2,1 		7,6,4,4,6,5
		
		//triangle 1
		elements[offsetArrayIndex] = offset + 3;
		elements[offsetArrayIndex+1] = offset + 2;
		elements[offsetArrayIndex+2] = offset + 0;
		
		
		//triangle 2
		elements[offsetArrayIndex+3] = offset + 0;
		elements[offsetArrayIndex+4] = offset + 2;
		elements[offsetArrayIndex+5] = offset + 1;
		
	}
	
	
	
	

}
