/**
 * 
 */
package components;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import input.KeyListener;
import input.MouseListener;
import utility.Time;

/**
 * @author adamsloan
 *
 */
public class Window {

	private int width, height;
	private String title;
	private long glfwWindow;
	private static Scene currentScene;

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

	public static void changeScene(int newScene) {
		switch (newScene) {
		case 0:
			currentScene = new LevelEditorScene();
			currentScene.init();
			break;
		case 1:
			currentScene = new LevelScene();
			currentScene.init();
			break;
		default:
			assert false : "Unknown scene " + newScene + "";
			break;
		}
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
		
		//For mac with M1 Chip
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		//as per tutorial
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

		Window.changeScene(0);

	}

	public void loop() {

		float beginTime = Time.getTime();
		float endTime;
		float dt = -1.0f;

		while (!glfwWindowShouldClose(glfwWindow)) {

			// poll events
			glfwPollEvents();

			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			currentScene.update(dt);

			if (dt >= 0) {
				currentScene.update(dt);
			}

			glfwSwapBuffers(glfwWindow);

			// Get Delta time
			endTime = Time.getTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}

	}

}
