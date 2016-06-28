package usp.cg.game;

import usp.cg.engine.GameItem;
import usp.cg.engine.graph.Mesh;

class Reflector extends GameItem {
    float vy;
    boolean Down, Up, hold;
    static float HEIGHT = 0.8f;
    static float WIDTH = 0.1f;
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
        if(this.position.z <= -Game.FieldSizeZ + HEIGHT){
            this.position.z = -Game.FieldSizeZ +0.01f + HEIGHT;
            vy = 0;
        }
        if(this.position.z >= Game.FieldSizeZ - HEIGHT){
            this.position.z = Game.FieldSizeZ -0.01f - HEIGHT;
            vy = 0;
        }
    }
}