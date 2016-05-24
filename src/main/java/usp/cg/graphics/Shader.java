package usp.cg.graphics;

import usp.cg.util.Utilities;
import usp.cg.util.Vector3f;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    Shader(String vertexFile, String fragmentFile){
        vertexShaderID = Utilities.loadShader(vertexFile, GL_VERTEX_SHADER);
        fragmentShaderID = Utilities.loadShader(fragmentFile, GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);
        glValidateProgram(programID);
    }

    public void start(){
        glUseProgram(programID);
    }

    public void stop(){
        glUseProgram(0);
    }

    public int getID(){
        return this.programID;
    }

    private int getUniform(String name){
        int result = glGetUniformLocation(programID, name);
        if(result == -1)
            System.err.println("Could not find uniform variable'" + name + "'!");
        return glGetUniformLocation(programID, name);
    }

    public void setUniform3f(String name, Vector3f vector) {
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void cleanUp(){
        stop();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }
}