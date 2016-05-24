package usp.cg.engine;

import usp.cg.graphics.ShaderManager;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static usp.cg.input.KeyboardInput.isKeyDown;

public class Game {
    static float FieldSizeY;
    private int ScoreL;
    private int ScoreR;
    private Reflector left;
    private Reflector right;
    private Ball ball;
    private boolean isRunning;

    public Game() {
        // GAME INITIAL SETTINGS
        FieldSizeY = 0.99f;
        this.ScoreL = 0;
        this.ScoreR = 0;
        this.right = new Reflector(1.0f, 0, 0);
        this.left = new Reflector(1.0f, 1.0f, 0);
        this.ball = new Ball();
        this.isRunning = true;

        left.position.x =  -0.8f;
        right.position.x = 0.8f;

        while (ball.vx == 0)
            ball.vx = (new Random().nextInt(3))*Ball.SPEEDX;

        ball.vy = 0;
        ball.position.x = 0;
        ball.position.y = 0;
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

    public boolean isRunning() {
        return isRunning;
    }

    public void update() {
        isRunning = win();
        keyboard();
        left.move();
        right.move();
        ball.move();
        ball.reflection(left, right);
        ball.care(right);
        ball.care(left);
        KeyReset();
    }

    private void KeyReset() {
        left.vy = 0;
        right.vy = 0;
    }

    private void keyboard() {
        left.Up = isKeyDown(GLFW_KEY_W);
        left.Down = isKeyDown(GLFW_KEY_S);
        right.Up = isKeyDown(GLFW_KEY_UP);
        right.Down = isKeyDown(GLFW_KEY_DOWN);

        if(isKeyDown(GLFW_KEY_SPACE)) {
            if (right.hold) {
                right.hold = false;
                ball.vx = -Ball.SPEEDX;
            } else if (left.hold) {
                left.hold = false;
                ball.vx = Ball.SPEEDX;
            }
        }

        if((left.Up)&&(!left.Down))
            left.vy = Reflector.SPEEDY;

        if((!left.Up)&&(left.Down))
            left.vy = -Reflector.SPEEDY;

        if((right.Up)&&(!right.Down))
            right.vy = Reflector.SPEEDY;

        if((!right.Up)&&(right.Down))
            right.vy = -Reflector.SPEEDY;
    }

    public void render() {
        ShaderManager.shader1.start();
        ShaderManager.shader1.setUniform3f("pos", left.position);
        left.draw();
        ShaderManager.shader1.stop();

        ShaderManager.shader2.start();
        ShaderManager.shader2.setUniform3f("pos", right.position);
        right.draw();
        ShaderManager.shader2.stop();

        ShaderManager.shader3.start();
        ShaderManager.shader3.setUniform3f("pos", ball.position);
        ball.draw();
        ShaderManager.shader3.stop();
    }

    /*
    public void DrawField() {
        // CG
        glColor3f(1,1,1);
        glVertex2f(-FieldSizeX - BorderT,-FieldSizeY - BorderT);
        glVertex2f(FieldSizeX + BorderT,-FieldSizeY - BorderT);
        glVertex2f(FieldSizeX + BorderT,-FieldSizeY);
        glVertex2f(-FieldSizeX - BorderT,-FieldSizeY);

        glVertex2f(-FieldSizeX - BorderT,FieldSizeY + BorderT);
        glVertex2f(FieldSizeX + BorderT,FieldSizeY + BorderT);
        glVertex2f(FieldSizeX + BorderT,FieldSizeY);
        glVertex2f(-FieldSizeX - BorderT,FieldSizeY);

        glVertex2f(-FieldSizeX - BorderT,-FieldSizeY - BorderT);
        glVertex2f(-FieldSizeX,-FieldSizeY - BorderT);
        glVertex2f(-FieldSizeX,FieldSizeY + BorderT);
        glVertex2f(-FieldSizeX - BorderT, FieldSizeY + BorderT);

        glVertex2f(FieldSizeX,-FieldSizeY - BorderT);
        glVertex2f(FieldSizeX + BorderT,-FieldSizeY - BorderT);
        glVertex2f(FieldSizeX + BorderT,FieldSizeY + BorderT);
        glVertex2f(FieldSizeX, FieldSizeY + BorderT);

        for(float i = -FieldSizeY; i <= FieldSizeY; i += 4*MLineT){
            glVertex2f(-MLineT,i + MLineT);
            glVertex2f(MLineT,i + MLineT);
            glVertex2f(MLineT,i - MLineT);
            glVertex2f(-MLineT,i - MLineT);
        }
    }


    void DrawScore() {
        // CG
        glRasterPos2f(TextPosX - 125, TextPosY + 60);
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'T');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'U');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'S');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'C');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'A');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, ' ');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'P');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'O');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'N');
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'G');

        glRasterPos2f(TextPosX - 50, TextPosY + 20);
        glutBitmapCharacter(GLUT_BITMAP_9_BY_15, '0' + ScoreL);
        glRasterPos2f(TextPosX + 30, TextPosY + 20);
        glutBitmapCharacter(GLUT_BITMAP_9_BY_15, '0' + ScoreR);
        if(ScoreL == 8) {
            glRasterPos2f(TextPosX - 200, TextPosY + 40);
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'W');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'I');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'N');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'N');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'E');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'R');
        }
        if(ScoreR == 8) {
            glRasterPos2f(TextPosX + 150, TextPosY + 40);
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'W');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'I');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'N');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'N');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'E');
            glutBitmapCharacter(GLUT_BITMAP_9_BY_15, 'R');
        }
    }*/

}
