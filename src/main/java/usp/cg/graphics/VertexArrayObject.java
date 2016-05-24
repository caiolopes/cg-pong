package usp.cg.graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static usp.cg.util.Utilities.createByteBuffer;
import static usp.cg.util.Utilities.createFloatBuffer;

public class VertexArrayObject {

    private static final int VERTEX_ATTRIB = 0;
    private int vao;

    public VertexArrayObject(float[] vertices, byte[] indices){
        createArrayObject(vertices, indices);
    }

    private void createArrayObject(float[] vertices, byte[] indices){
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        createVerticesBuffer(vertices);
        createIndicesBuffer(indices);

        glBindVertexArray(0);
    }

    public int getVaoID(){
        return vao;
    }

    private void createVerticesBuffer(float[] vertices){
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void createIndicesBuffer(byte[] indices){
        int ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createByteBuffer(indices), GL_STATIC_DRAW);
    }
}
