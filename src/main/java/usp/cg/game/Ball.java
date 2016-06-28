package usp.cg.game;

import usp.cg.engine.GameItem;
import usp.cg.engine.graphics.Mesh;

import static java.lang.Math.abs;

class Ball extends GameItem {
    float vx;
    float vz;
    static float SPEEDX = 0.025f;
    static float WIDTH = 0.03f;

    Ball(Mesh mesh) {
        super(mesh);
    }

    void reflection(Pencil left, Pencil right) {
        if((this.position.z <= -Game.FieldSizeZ + Game.blockScale*2)||(this.position.z >= Game.FieldSizeZ - Game.blockScale*2))
            vz = -vz;
        if((this.position.x - WIDTH <= left.getPosition().x+ Pencil.WIDTH)&&(abs((this.position.z - left.getPosition().z)) <= Pencil.HEIGHT + abs(vz))){
            vx = -vx;
            vz += left.vy;
        }
        if((this.position.x + WIDTH >= + right.getPosition().x- Pencil.WIDTH)&&(abs((double)(this.position.z - right.getPosition().z)) <= Pencil.HEIGHT + abs(vz))){
            vx = -vx;
            vz += right.vy;
        }
    }

    // Verifica se algum dos refletores esta segurando a bola
    // caso positivo, reposiciona a bola pro local correto
    void care(Pencil pencil) {
        if(pencil.hold) {
            this.vx = 0;
            if(pencil.getPosition().x < 0)
                this.position.x = pencil.getPosition().x + 2* Pencil.WIDTH;
            if(pencil.getPosition().x > 0)
                this.position.x = pencil.getPosition().x - 2* Pencil.WIDTH;

            this.vz = pencil.vy;
            this.position.z = pencil.getPosition().z;
        }
    }

    void move() {
        this.position.x += vx;
        this.position.z += vz;
    }
};