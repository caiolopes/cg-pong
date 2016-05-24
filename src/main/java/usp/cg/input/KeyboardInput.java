package usp.cg.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Classe auxiliar apenas para lidar com o CallBack do teclado para lidar com as interações do jogo
 */
public class KeyboardInput extends GLFWKeyCallback {

    private static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int keycode){
        return keys[keycode];
    }

}