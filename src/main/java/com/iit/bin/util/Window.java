package com.iit.bin.util;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    public boolean fullscreen;
    private long window; // GLFW pointer
    private int height, width;
    private Input input;

    public Window() {
        setSize(1024, 480);
        setFullscreen(false);
    }

    public static void setCallbacks() {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Input getInput() {
        return input;
    }

    public void update() {
        input.update();
        glfwPollEvents();
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public long getWindow() {
        return window;
    }

    public void createWindow(String title) {

        window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0); // utolsó kettő: fullscreen, multiscreen supportSystem.out.println(width+ ", "+height+", "+title+ ", "+window);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }
        if (!fullscreen) {
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window,
                    (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2);

        }
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        input = new Input(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

}
