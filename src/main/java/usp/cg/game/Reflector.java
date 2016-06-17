package usp.cg.game;

import usp.cg.engine.GameItem;
import usp.cg.engine.graph.Mesh;

class Reflector extends GameItem {
    float vy;
    boolean Down, Up, hold;
    static float HEIGHT = 1f;
    static float WIDTH = 0.2f;
    static float SPEEDY = 0.02f;

    Reflector(Mesh mesh) {
        super(mesh);
        this.vy = 0;
        this.Down = false;
        this.Up = false;
        this.hold = false;
    }

    void move() {
        this.position.z += vy;
        if(this.position.z <= -DummyGame.FieldSizeY){
            this.position.z = -DummyGame.FieldSizeY+0.01f;
            vy = 0;
        }
        if(this.position.z >= DummyGame.FieldSizeY){
            this.position.z = DummyGame.FieldSizeY-0.01f;
            vy = 0;
        }
    }
}