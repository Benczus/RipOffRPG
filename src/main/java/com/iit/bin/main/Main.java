package com.iit.bin.main;

import com.iit.bin.entities.AbstractEntity;
import com.iit.bin.map.MapRenderer;
import com.iit.bin.map.TileRenderer;
import com.iit.bin.render.Camera;
import com.iit.bin.render.Shader;
import com.iit.bin.util.Timer;
import com.iit.bin.util.Window;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    public static void main(String[] args) {
        Window.setCallbacks();
        if (!glfwInit()) {
            throw new RuntimeException("Cannot initializeEntity GLFW!");
        }
        Window window = new Window();
        window.setSize(640, 480);
        window.setFullscreen(false);
        window.createWindow("Game");
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Camera camera = new Camera(window.getWidth(), window.getHeight());
        glEnable(GL_TEXTURE_2D);
        TileRenderer tiles = new TileRenderer();
        AbstractEntity.initializeEntity();
        Shader shader = new Shader("shader");
        MapRenderer mapRenderer = new MapRenderer("castle", camera);
        double frameCap = 1.0 / 60.0;   //60 frames per second
        double frameTime = 0;
        int frames = 0;
        double time = Timer.getTime();
        double unprocessed = 0;
        while (!window.shouldClose()) {
            boolean canRender = false;
            double time2 = Timer.getTime();
            double passed = time2 - time;
            unprocessed += passed;
            frameTime += passed;
            time = time2;
            while (unprocessed >= frameCap) {
                unprocessed -= frameCap;
                canRender = true;

                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(window.getWindow(), true);
                }

                mapRenderer.update((float) frameCap, window, camera);

                mapRenderer.correctCamera(camera, window);
                window.update();
                if (frameTime >= 1.0) {
                    frameTime = 0;
                    System.out.println(frames);
                    frames = 0;
                }
            }

            if (canRender) {
                // THIS IS THE MAIN RENDER METHOD
                glClear(GL_COLOR_BUFFER_BIT);
                mapRenderer.render(tiles, shader, camera, window);
                window.swapBuffers();
                frames++;
            }
        }
        AbstractEntity.deleteEntity();
        glfwTerminate();
    }

}


