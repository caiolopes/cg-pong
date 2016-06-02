package usp.cg;

import org.lwjgl.opengl.GL;
import usp.cg.engine.Game;
import usp.cg.graphics.ShaderManager;
import usp.cg.input.KeyboardInput;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


/**
 * Classe principal que tem a main que inicializa as configurações da OpenGL, o Display, e a engine do game
 */
public class Main {
    private boolean running = false;
    private long window;
    private Game game;

    /**
     * Inicializa o display e a engine
     */
    private void init() {
        int width = 800, height = 600;
        this.running = true;

        if(glfwInit() != GL_TRUE){
            System.err.println("Error initializing glfw window handler library");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        window = glfwCreateWindow(width, height, "2D Pong", NULL, NULL);
        if(window == NULL){
            System.err.println("Could not create our Window!");
        }

        glfwSetKeyCallback(window, new KeyboardInput());

        glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, 100, 100);
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);

        GL.createCapabilities();
        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        ShaderManager shaderManager;
        shaderManager = new ShaderManager();
        shaderManager.loadAll();

        game = new Game();
    }

    /**
     * Renderiza na tela
     */
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.39f, 0.58f, 0.92f, 1.0f);

        game.render();

        glfwSwapBuffers(window);
    }

    /**
     * Atualiza updates da engine (o que rolou no jogo)
     */
    private void update() {
        glfwPollEvents();
        if (game.isRunning())
            game.update();
        else
            running = false;
    }

    /**
     * Loop do jogo
     */
    private void run() {
        init();
        // Start the timer
        long lastTime = System.nanoTime();
        // measure the difference
        double delta = 0.0;
        // calculate what 1 second / 60 frames per second equals
        double ns = 1000000000.0 / 60.0;
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                update();
                delta--;
            }
            render();
            if(glfwWindowShouldClose(window) == GL_TRUE){
                running = false;
                glfwDestroyWindow(window);
            }
        }

        cleanUp();
    }

    /**
     * Limpa os shaders da memória
     */
    private void cleanUp() {
        ShaderManager.shader1.cleanUp();
        ShaderManager.shader2.cleanUp();
        ShaderManager.shader3.cleanUp();
    }

    /**
     * Main
     * @param args args
     */
    public static void main(String args[]){
        Main driver = new Main();
        driver.run();
    }
}