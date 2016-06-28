package usp.cg.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import usp.cg.engine.*;
import usp.cg.engine.graph.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Classe principal que impleta a interface de lógica do jogo, que inclui ler teclas do teclado, atualizar, inicializar,
 * etc.
 */
public class Game implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;

    private boolean isRunning = true;

    private Reflector left;

    private Reflector right;

    private Ball ball;

    static float FieldSizeZ = 2f;

    private int ScoreL = 0;

    private int ScoreR = 0;

    private Window mWindow;

    private boolean gameStart = false;

    private boolean gameOver = false;

    private int leftUp = GLFW_KEY_W;
    private int leftDown = GLFW_KEY_S;
    private int rightUp = GLFW_KEY_UP;
    private int rightDown = GLFW_KEY_DOWN;

    public static float blockScale = 0.2f;
    public static float skyBoxScale = 9.0f;
    public static float extension = 1.0f;


    public Game() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        mWindow = window;
        renderer.init(mWindow);

        scene = new Scene();
        
        float reflectance = 1f;
        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture = new Texture("/textures/grassblock.png");
        Material material = new Material(texture, reflectance);
        mesh.setMaterial(material);
        
        float startx = extension * (-skyBoxScale + blockScale);
        float startz = extension * (skyBoxScale - blockScale);
        float starty = -1.0f;
        float inc = blockScale * 2;
        
        float posx = startx;
        float posz = startz;
        float incy = 0.0f;
        int NUM_ROWS = (int)(extension * skyBoxScale * 2 / inc);
        int NUM_COLS = (int)(extension * skyBoxScale * 2/ inc);
        GameItem[] gameItems  = new GameItem[(NUM_ROWS * NUM_COLS) + 3];
        boolean firstLeft = false;
        for(int i=0; i<NUM_ROWS; i++) {
            for(int j=0; j<NUM_COLS; j++) {
                GameItem gameItem = new GameItem(mesh);
                gameItem.setScale(blockScale);
                //incy = Math.random() > 0.9f ? blockScale * 2 : 0f;
                incy = (posz == 1.9999996f || posz == -2.0000002f ) ? blockScale * 2 : 0f;

                gameItem.setPosition(posx, starty + incy, posz);
                gameItems[i*NUM_COLS + j] = gameItem;
                
                posx += inc;
            }
            posx = startx;
            posz -= inc;
        }

        mesh = OBJLoader.loadMesh("/models/cube.obj");
        texture = new Texture("/textures/eraser.png");
        material = new Material(texture, reflectance);
        mesh.setMaterial(material);

        ball = new Ball(mesh);
        gameItems[gameItems.length-3] = ball;

        mesh = OBJLoader.loadMesh("/models/Pencil.obj");
        texture = new Texture("/textures/pencil.png");
        material = new Material(texture, reflectance);
        mesh.setMaterial(material);

        left = new Reflector(mesh);
        right = new Reflector(mesh);

        gameItems[gameItems.length-2] = left;
        gameItems[gameItems.length-1] = right;


        scene.setGameItems(gameItems);

        initialPositions();

        SkyBox skyBox = new SkyBox("/models/skybox.obj", "/textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        setupLights();

        cameraTop();
    }

    private void cameraTop() {
        camera.setPosition(-0.040611394f, 3.1899922f, 2.9059205f);
        camera.setRotation(55.00155f, -0.4391871f, 0f);

        leftUp = GLFW_KEY_W;
        leftDown = GLFW_KEY_S;
        rightUp = GLFW_KEY_UP;
        rightDown = GLFW_KEY_DOWN;
    }

    private void cameraFirstPlayer() {
        camera.setPosition(-4.353383f, 1.289994f, -0.026490219f);
        camera.setRotation(36.7703f, 90.08346f, 0f);

        leftUp = GLFW_KEY_A;
        leftDown = GLFW_KEY_D;
        rightUp = GLFW_KEY_LEFT;
        rightDown = GLFW_KEY_RIGHT;
    }

    private void initialPositions() {
        ball.setScale(0.09f);
        ball.setPosition(0, -0.5f, 0f);
        left.setScale(0.25f);
        left.setPosition(-3f, -0.6f, 0f);
        right.setScale(0.25f);
        right.setPosition(3f, -0.6f, 0f);

        ball.vz = 0;
        ball.position.x = 0;
        ball.position.z = 0;
    }

    private boolean win() {
        if((this.ScoreL == 5)||(this.ScoreR == 5)) {
            glfwSetWindowTitle(mWindow.getWindowHandle(), "GAME OVER! Pressione enter para novo jogo");
            gameStart = false;
            gameOver = true;
            return false;
        }
        if(ball.position.x + Ball.WIDTH < left.position.x + Reflector.WIDTH - Ball.SPEEDX){
            right.hold = true;
            this.ScoreR++;
            glfwSetWindowTitle(mWindow.getWindowHandle(), "Pong - " + this.ScoreL + " x " + this.ScoreR);
        }
        if(ball.position.x - Ball.WIDTH > right.position.x - Reflector.WIDTH + Ball.SPEEDX){
            left.hold = true;
            this.ScoreL++;
            glfwSetWindowTitle(mWindow.getWindowHandle(), "Pong - " + this.ScoreL + " x " + this.ScoreR);
        }
        return true;
    }


    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Luz ambiente
        sceneLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Luz direcional
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        left.Up = window.isKeyPressed(leftUp);
        left.Down = window.isKeyPressed(leftDown);
        right.Up = window.isKeyPressed(rightUp);
        right.Down = window.isKeyPressed(rightDown);

        if(window.isKeyPressed(GLFW_KEY_SPACE)) {
            if (right.hold) {
                right.hold = false;
                ball.vx = -Ball.SPEEDX;
            } else if (left.hold) {
                left.hold = false;
                ball.vx = Ball.SPEEDX;
            }
        }

        if (gameOver) {
            if (window.isKeyPressed(GLFW_KEY_ENTER)) {
                this.ScoreL = 0;
                this.ScoreR = 0;
                glfwSetWindowTitle(mWindow.getWindowHandle(), "Pong - " + this.ScoreR + " x " + this.ScoreL);
                gameStart = false;
                initialPositions();
            }
        }

        if (!gameStart) {
            if(window.isKeyPressed(GLFW_KEY_SPACE)) {
                glfwSetWindowTitle(mWindow.getWindowHandle(), "Pong - " + this.ScoreR + " x " + this.ScoreL);
                while (ball.vx == 0) {
                    if ((new Random().nextInt(2)) == 0)
                        ball.vx = -(new Random().nextInt(2)) * Ball.SPEEDX;
                    else
                        ball.vx = (new Random().nextInt(2)) * Ball.SPEEDX;
                }
                gameStart = true;
            }
        }

        if (window.isKeyPressed(GLFW_KEY_1)) {
            cameraTop();
        }

        if (window.isKeyPressed(GLFW_KEY_2)) {
            cameraFirstPlayer();
        }

        if((left.Down)&&(!left.Up))
            left.vy = Reflector.SPEEDY;

        if((!left.Down)&&(left.Up))
            left.vy = -Reflector.SPEEDY;

        if((right.Down)&&(!right.Up))
            right.vy = Reflector.SPEEDY;

        if((!right.Down)&&(right.Up))
            right.vy = -Reflector.SPEEDY;

        if (window.isKeyPressed(GLFW_KEY_C)) {
            System.out.println("Camera pos x:" + camera.getPosition().x + ", y: "+ camera.getPosition().y + ", z: " + camera.getPosition().z);
            System.out.println("Camera rot x:" + camera.getRotation().x + ", y: "+ camera.getRotation().y + ", z: " + camera.getRotation().z);
        }

        cameraInc.set(0, 0, 0);

        if (window.isKeyPressed(GLFW_KEY_T)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_G)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_F)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_H)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_L)) {
            SceneLight sceneLight = scene.getSceneLight();

            // Update directional light direction, intensity and colour
            DirectionalLight directionalLight = sceneLight.getDirectionalLight();
            lightAngle += 1.1f;
            if (lightAngle > 90) {
                directionalLight.setIntensity(0);
                if (lightAngle >= 360) {
                    lightAngle = -90;
                }
                sceneLight.getAmbientLight().set(0.3f, 0.3f, 0.4f);
            } else if (lightAngle <= -80 || lightAngle >= 80) {
                float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
                sceneLight.getAmbientLight().set(factor, factor, factor);
                directionalLight.setIntensity(factor);
                directionalLight.getColor().y = Math.max(factor, 0.9f);
                directionalLight.getColor().z = Math.max(factor, 0.5f);
            } else {
                sceneLight.getAmbientLight().set(1, 1, 1);
                directionalLight.setIntensity(1);
                directionalLight.getColor().x = 1;
                directionalLight.getColor().y = 1;
                directionalLight.getColor().z = 1;
            }
            double angRad = Math.toRadians(lightAngle);
            directionalLight.getDirection().x = (float) Math.sin(angRad);
            directionalLight.getDirection().y = (float) Math.cos(angRad);
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        win();
        left.move();
        right.move();
        ball.move();
        left.move();
        right.move();
        ball.move();
        ball.reflection(left, right);
        ball.care(right);
        ball.care(left);
        left.vy = 0;
        right.vy = 0;


        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, scene);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        Map<Mesh, List<GameItem>> mapMeshes = scene.getGameMeshes();
        for (Mesh mesh : mapMeshes.keySet()) {
            mesh.cleanUp();
        }
    }

}
