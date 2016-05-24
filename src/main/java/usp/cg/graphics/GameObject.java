package usp.cg.graphics;

import usp.cg.util.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GameObject {

    protected int vaoID;
    protected int count;

    public Vector3f position;

    protected VertexArrayObject vao;

    protected GameObject(){
    }

    public void draw() {
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
