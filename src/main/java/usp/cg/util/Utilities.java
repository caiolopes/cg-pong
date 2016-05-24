package usp.cg.util;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Classe de metódos utéis para lidar com a OpenGL
 */
public class Utilities {

    public static int loadShader(String file, int type){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer = "";
            while((buffer = reader.readLine()) != null){
                result.append(buffer);
                result.append("\n");
            }
            reader.close();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, result.toString());
        glCompileShader(shaderID);
        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println(glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader.");
            System.err.println(-1);
        }
        return shaderID;
    }

    public static IntBuffer createIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer createByteBuffer(byte[] data){
        ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}