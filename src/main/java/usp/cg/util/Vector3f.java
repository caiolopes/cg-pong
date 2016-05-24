package usp.cg.util;

/**
 * Vetor de 3 floats
 */
public class Vector3f {

    public float x,y,z;

    public Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(){
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public void translate(Vector3f vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }
}