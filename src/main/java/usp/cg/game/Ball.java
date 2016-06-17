package usp.cg.game;

import usp.cg.engine.GameItem;
import usp.cg.engine.graph.Mesh;

import static java.lang.Math.abs;

class Ball extends GameItem {
    float vx;
    float vz;
    static float SPEEDX = 0.02f;

    Ball(Mesh mesh) {
        super(mesh);
    }

    void reflection(Reflector left, Reflector right) {
        if((this.position.z <= -DummyGame.FieldSizeZ + DummyGame.blockScale*2)||(this.position.z >= DummyGame.FieldSizeZ - DummyGame.blockScale*2))
            vz = -vz;
        if((this.position.x <= left.getPosition().x+Reflector.WIDTH)&&(abs((this.position.z - left.getPosition().z)) <= Reflector.HEIGHT + abs(vz))){
            vx = -vx;
            vz += left.vy;
        }
        if((this.position.x >= right.getPosition().x-Reflector.WIDTH)&&(abs((double)(this.position.z - right.getPosition().z)) <= Reflector.HEIGHT + abs(vz))){
            vx = -vx;
            vz += right.vy;
        }
    }

    // Verifica se algum dos refletores esta segurando a bola
    // caso positivo, reposiciona a bola pro local correto
    void care(Reflector reflector) {
        if(reflector.hold) {
            this.vx = 0;
            if(reflector.getPosition().x < 0)
                this.position.x = reflector.getPosition().x + 2*Reflector.WIDTH;
            if(reflector.getPosition().x > 0)
                this.position.x = reflector.getPosition().x - 2*Reflector.WIDTH;

            this.vz = reflector.vy;
            this.position.z = reflector.getPosition().z;
        }
    }

    void move() {
        this.position.x += vx;
        this.position.z += vz;
    }
};