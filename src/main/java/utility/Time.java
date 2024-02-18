/**
 * 
 */
package utility;

/**
 * Creates delta time for in game time
 * 
 * @author adamsloan
 *
 */
public class Time {
	public static float timeStarted = System.nanoTime();

	public static float getTime() {
		return (float) ((System.nanoTime() - timeStarted) * 1E-9);
	}
}
