package usp.cg.engine.graphics;

import java.nio.FloatBuffer;

class UniformData {

    private final int uniformLocation;
    
    private FloatBuffer floatBuffer;
    
    UniformData(int uniformLocation) {
        this.uniformLocation = uniformLocation;
    }

    int getUniformLocation() {
        return uniformLocation;
    }

    FloatBuffer getFloatBuffer() {
        return floatBuffer;
    }

    void setFloatBuffer(FloatBuffer floatBuffer) {
        this.floatBuffer = floatBuffer;
    }
}
