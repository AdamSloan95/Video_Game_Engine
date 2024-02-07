/**
 * 
 */
package utility;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

/**
 * @author adamsloan
 *
 */
public class Window {

	private int width, height;
	private String title;
	private long glfwWindow;

	/*
	 * Singleton method: Only one window will ever be needed and this along with the
	 * get method ensures there can only ever be one window.
	 */
	private static Window window = null;

	private Window() {
		this.width = 1920;
		this.height = 1080;
		this.title = "Buí";
	}

	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window();
		}
		return Window.window;
	}

	public void run() {
		System.out.println("Hello LWJGL" + Version.getVersion() + "!");

		init();
		loop();

		// free the memory
		Callbacks.glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();

	}

	/**
	 * Uses multiple static imports
	 * 
	 * Initially threw an error regarding creation of window having to be on first
	 * thread setting sending VM argument at runtime "XstartOnFirstThread "
	 */
	public void init() {

		// set up error callback
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialise GLFW
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize glfw");
		}

		// config GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

		// Create Window
		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create the GLFW window");
		}

		/*
		 * setting callbacks from Mouselistener using Lambda expressions
		 */
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

		// Make open gl contect current
		glfwMakeContextCurrent(glfwWindow);

		// Enable v-sync
		// Swap every frame
		glfwSwapInterval(1);

		// make window visible
		glfwShowWindow(glfwWindow);

		// Copied from website source:
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.

		GL.createCapabilities();

	}

	public void loop() {
		while (!glfwWindowShouldClose(glfwWindow)) {
			// poll events
			glfwPollEvents();

			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

			glClear(GL_COLOR_BUFFER_BIT);
			
			
			/*
			 * REMOVE
			if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
				System.out.println("Space");
			}
			
			
			if(MouseListener.isDragging()) {
				System.out.println("Dragging");
			}
			
			if(MouseListener.mouseButtonDown(0)) {
				System.out.println("click");
			}
			
			if(MouseListener.mouseButtonDown(1)) {
				System.out.println("click 2");
			}
			
			
			System.out.println("x: " + MouseListener.getScrollX() + "y: " + MouseListener.getScrollY());
			
			System.out.println("x: " + MouseListener.getX() + "y: " + MouseListener.getY());
			
			
			*/
			System.out.println("dx: " + MouseListener.getDx() + "dy: " + MouseListener.getDy());
			
			
			

			glfwSwapBuffers(glfwWindow);
		}

	}

}
