package com.iit.bin.game;

import com.iit.bin.entity.Entity;
import com.iit.bin.entity.Player;
import com.iit.bin.entity.Transform;
import com.iit.bin.io.Timer;
import com.iit.bin.io.Window;
import com.iit.bin.render.Camera;
import com.iit.bin.render.Shader;
import com.iit.bin.world.TileRenderer;
import com.iit.bin.world.World;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    public static void main(String[] args) {
        Window.setCallbacks();


//        AABB box1= new AABB(new Vector2f(0,0), new Vector2f(1,1));
//        AABB box2= new AABB(new Vector2f(1,0), new Vector2f(1,1));
//
//        if(box1.getCollision(box2).isIntersecting)
//            System.out.println("intersecting");

        if (!glfwInit()) {
            throw new RuntimeException("Cannot initialize GLFW!");
        }

        Window window = new Window();
        window.setSize(1280, 1024);
        window.setFullscreen(false);
        window.createWindow("Game");


        GL.createCapabilities();
        Camera camera = new Camera(window.getWidth(), window.getHeight());
        glEnable(GL_TEXTURE_2D);

        TileRenderer tiles = new TileRenderer();
        Entity.initAsset();

//        float[] vertices = new float[]{
//                -0.5f, 0.5f, 0, //TOP LEFT 0
//                0.5f, 0.5f, 0, //TOP RIGHT 1
//                0.5f, -0.5f, 0, //BORROM RIGHT 2
//                -0.5f, -0.5f, 0, //BORROM LEFT 3
//
//        };
//        float[] texture = new float[]{
//                0, 0,
//                1, 0,
//                1, 1,
//
//
//                0, 1
//        };
//        int[] indices = new int[]{
//                0, 1, 2,
//                2, 3, 0
//        };
//
//
//        Model model = new Model(vertices, texture, indices);
        Shader shader = new Shader("shader");
        // PLACE TO CREATE TEXTURES ( after createCapabilities)
        //
//     Texture tex = new Texture("C:\\Munka\\CheapShootemUp-master\\src\\main\\resources\\test.png");
//        Matrix4f scale = new Matrix4f()
//                .translate(new Vector3f(0,0,0))
//                .scale(16);
//
//        Matrix4f target = new Matrix4f();
       // World world = new World("test_level");
        World world = new World("test_level");

        Player player = new Player(new Transform());

//        world.setTile(Tile.test2, 5, 0);
//        world.setTile(Tile.test2, 6, 0);
//
//        world.setTile(Tile.test2, 7, 0);
//
//        world.setTile(Tile.test2, 7, 1);
//
//        world.setTile(Tile.test2, 7, 2);
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

                player.update((float) frameCap, window, camera, world);

                world.correctCamera(camera, window);
                window.update();
                if (frameTime >= 1.0) {
                    frameTime = 0;
                    System.out.println("FPS:" + frames);
                    frames = 0;
                }
            }

            if (canRender) {
                // THIS IS THE MAIN RENDER METHOD
                glClear(GL_COLOR_BUFFER_BIT);


//                shader.bind();
//                shader.setUniform("sampler", 0);
//                shader.setUniform("projection", camera.getProjection().mul(target));
//                tex.bind(0);
//

//                for (int i=0; i<8; i++){
//                    for (int j=0; j<4; j++){
//                        tiles.renderTile((byte)0,i,j,shader,scale,camera);
//
//                    }
//                }
                world.render(tiles, shader, camera, window);
                player.render(shader, camera, world);
                window.swapBuffers();
                frames++;
            }


        }
        Entity.deleteAsset();
        glfwTerminate();

    }

}


