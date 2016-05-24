package usp.cg.engine;

import usp.cg.graphics.GameObject;
import usp.cg.graphics.VertexArrayObject;
import usp.cg.util.Vector3f;

class Reflector extends GameObject {
    float vy;
    boolean Up, Down, hold;
    float r, g, b;
    static float HEIGHT = 0.4f;
    static float WIDTH = 0.05f;
    static float SPEEDY = 0.02f;

    Reflector(float r, float g, float b) {
        this.vy = 0;
        this.Up = false;
        this.Down = false;
        this.hold = false;
        this.r = r;
        this.g = g;
        this.b = b;

        float[] vertices = {
                0.0f, HEIGHT, 0.0f,
                0.0f, 0.0f, 0.0f,
                WIDTH, 0.0f, 0.0f,
                WIDTH, HEIGHT, 0.0f,
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        this.count = indices.length;
        VertexArrayObject vao;
        vao = new VertexArrayObject(vertices, indices);
        this.vaoID = vao.getVaoID();
        this.position = new Vector3f();
    }

    void move() {
        this.position.y += vy;
        if(this.position.y <= -1.0f){
            this.position.y = -0.99f;
            vy = 0;
        }
        if(this.position.y + HEIGHT >= 1.0f){
            this.position.y = 0.99f - HEIGHT;
            vy = 0;
        }
    }
}