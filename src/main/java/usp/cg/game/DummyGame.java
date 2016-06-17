package usp.cg.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import usp.cg.engine.*;
import usp.cg.engine.graph.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;

    // game
    private boolean isRunning = true;

    private Reflector left;

    private Reflector right;

    private Ball ball;

    static float FieldSizeZ = 2f;

    private int ScoreL = 0;

    private int ScoreR = 0;
    // end game

    public static float blockScale = 0.2f;
    public static float skyBoxScale = 9.0f;
    public static float extension = 1.0f;


    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();
        
        // Setup  GameItems
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

        // game
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

        ball.setScale(0.1f);
        ball.setPosition(0, -0.5f, -10f);
        left.setScale(0.35f);
        left.setPosition(-3f, -0.5f, -10f);
        right.setScale(0.35f);
        right.setPosition(3f, -0.5f, -10f);

        while (ball.vx == 0) {
            if ((new Random().nextInt(2)) == 0)
                ball.vx = -(new Random().nextInt(2)) * Ball.SPEEDX;
            else
                ball.vx = (new Random().nextInt(2)) * Ball.SPEEDX;
        }

        ball.vz = 0;
        ball.position.x = 0;
        ball.position.z = 0;
        // end game

        scene.setGameItems(gameItems);

        // Setup  SkyBox
        SkyBox skyBox = new SkyBox("/models/skybox.obj", "/textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);
        
        // Setup Lights
        setupLights();
        
        // Create HUD
        //hud = new Hud("DEMO");
        
        //camera.getPosition().x = 0.65f;
        //camera.getPosition().y = 1.15f;
        //camera.getPosition().y = 4.34f;

        camera.movePosition(-1.89f, 6.34f, 1.31f);
        camera.moveRotation(89.65f, -0.072f, 0);
    }

    private boolean win() {
        if((this.ScoreL == 8)||(this.ScoreR == 8)) {
            return false;
        }
        if(ball.position.x < left.position.x + Reflector.WIDTH - Ball.SPEEDX){
            right.hold = true;
            this.ScoreR++;
        }
        if(ball.position.x > right.position.x - Reflector.WIDTH + Ball.SPEEDX){
            left.hold = true;
            this.ScoreL++;
        }
        return true;
    }


    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        // game
        left.Down = window.isKeyPressed(GLFW_KEY_S);
        left.Up = window.isKeyPressed(GLFW_KEY_W);
        right.Down = window.isKeyPressed(GLFW_KEY_DOWN);
        right.Up = window.isKeyPressed(GLFW_KEY_UP);

        if(window.isKeyPressed(GLFW_KEY_SPACE)) {
            if (right.hold) {
                right.hold = false;
                ball.vx = -Ball.SPEEDX;
            } else if (left.hold) {
                left.hold = false;
                ball.vx = Ball.SPEEDX;
            }
        }

        if((left.Down)&&(!left.Up))
            left.vy = Reflector.SPEEDY;

        if((!left.Down)&&(left.Up))
            left.vy = -Reflector.SPEEDY;

        if((right.Down)&&(!right.Up))
            right.vy = Reflector.SPEEDY;

        if((!right.Down)&&(right.Up))
            right.vy = -Reflector.SPEEDY;
        // end game
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
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // game
        isRunning = win();
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
        // end game

        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            
            // Update HUD compass
            //hud.rotateCompass(camera.getRotation().y);
        }

        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if ((new Date()).getSeconds()%10 == 0) {
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
    public void render(Window window) {
        //hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        Map<Mesh, List<GameItem>> mapMeshes = scene.getGameMeshes();
        for (Mesh mesh : mapMeshes.keySet()) {
            mesh.cleanUp();
        }
        //hud.cleanup();
    }

}
