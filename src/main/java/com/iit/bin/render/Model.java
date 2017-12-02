package com.iit.bin.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Model {
    int pointerCount = 3;
    private int draw_count;
    private int v_id;
    private int t_id;
    private int i_id;

    public Model(float[] vertices, float[] texCoords, int[] indices) {


        draw_count = indices.length;


        // usage= STATIC, 1szer-be adjuk, és használjuk
        //        DYNAMIC= változni fog
        /*
        FloatBuffer buffer= BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
*/
        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id); // bind
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(texCoords), GL_STATIC_DRAW);

        i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);


        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0); //elvesszük a vid-t az array bufferből! + unbind
    }

    public void render() {
        glEnableVertexAttribArray(0); // "vertices", bound in main
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, v_id); //bind
        glVertexAttribPointer(0, pointerCount, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, t_id); //bind
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, v_id); //unbind


        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    protected void finalize() throws Throwable {
        glDeleteBuffers(v_id);
        glDeleteBuffers(t_id);
        glDeleteBuffers(i_id);
        super.finalize();
    }

}
