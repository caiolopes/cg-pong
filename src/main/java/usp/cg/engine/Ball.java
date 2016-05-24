package usp.cg.engine;

import usp.cg.graphics.GameObject;
import usp.cg.graphics.VertexArrayObject;
import usp.cg.util.Vector3f;

import static java.lang.Math.abs;

class Ball extends GameObject {
    float vx;
    float vy;
    float r, g, b;
    private static float HEIGHT = 0.05f;
    static float SPEEDX = 0.01f;

    Ball() {
        float WIDTH = 0.05f;

        float[] vertices = {
                0.0f, HEIGHT, 0f, // VO
                0.0f, 0.0f, 0f, // V1
                WIDTH, 0.0f, 0f, // V2
                WIDTH, HEIGHT, 0f, // V3
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        this.count = indices.length;
        this.vao = new VertexArrayObject(vertices, indices);
        this.vaoID = vao.getVaoID();
        this.position = new Vector3f();
    }

    void reflection(Reflector left, Reflector right) {
        if((this.position.y <= -Game.FieldSizeY)||(this.position.y + HEIGHT >= Game.FieldSizeY))
            vy = -vy;
        if((this.position.x <= left.position.x+Reflector.WIDTH)&&(abs((this.position.y - left.position.y)) <= Reflector.HEIGHT + abs(vy))){
            this.r = left.r;
            this.g = left.g;
            this.b = left.b;
            vx = -vx;
            vy += left.vy;
        }
        if((this.position.x >= right.position.x-Reflector.WIDTH)&&(abs((double)(this.position.y - right.position.y)) <= Reflector.HEIGHT + abs(vy))){
            this.r = right.r;
            this.g = right.g;
            this.b = right.b;
            vx = -vx;
            vy += right.vy;
        }
    }

    // Verifica se algum dos refletores esta segurando a bola
    // caso positivo, reposiciona a bola pro local correto
    void care(Reflector reflector) {
        if(reflector.hold) {
            this.vx = 0;
            if(reflector.position.x < 0)
                this.position.x = reflector.position.x + 2*Reflector.WIDTH;
            if(reflector.position.x > 0)
                this.position.x = reflector.position.x - 2*Reflector.WIDTH;

            this.vy = reflector.vy;
            this.position.y = reflector.position.y;
        }
    }

    void move() {
        this.position.x += vx;
        this.position.y += vy;
    }
};