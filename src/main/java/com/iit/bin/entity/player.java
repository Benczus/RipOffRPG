package com.iit.bin.entity;

import com.iit.bin.collision.AABB;
import com.iit.bin.collision.Collision;
import com.iit.bin.io.Window;
import com.iit.bin.render.Animation;
import com.iit.bin.render.Camera;
import com.iit.bin.render.Model;
import com.iit.bin.render.Shader;
import com.iit.bin.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player {

    private Model model;
    //  private Texture texture;
    private Animation texture;
    private Transform transform;
    private AABB boundingBox;

    public Player() {
        float[] vertices = new float[]{
                -1f, 1f, 0, //TOP LEFT 0
                1f, 1, 0, //TOP RIGHT 1
                1f, -1, 0, //BORROM RIGHT 2
                -1f, -1f, 0, //BORROM LEFT 3

        };
        float[] texture = new float[]{
                0, 0,
                1, 0,
                1, 1,


                0, 1
        };
        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };


        model = new Model(vertices, texture, indices);
        // this.texture= new Texture("test.png");
        this.texture = new Animation(5, 15, "an");
        transform = new Transform();
        transform.scale = new Vector3f(16, 16, 1);
        boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1, 1));
    }

    public void update(float delta, Window window, Camera camera, World world) {

        if (window.getInput().isKeyDown(GLFW_KEY_A)) {
            transform.pos.add(new Vector3f(-10 * delta, 0, 0));
        }
        if (window.getInput().isKeyDown(GLFW_KEY_D)) {
            transform.pos.add(new Vector3f(10 * delta, 0, 0));
        }
        if (window.getInput().isKeyDown(GLFW_KEY_W)) {
            transform.pos.add(new Vector3f(0, 10 * delta, 0));
        }
        if (window.getInput().isKeyDown(GLFW_KEY_S)) {
            transform.pos.add(new Vector3f(0, -10 * delta, 0));
        }

        boundingBox.getCenter().set(transform.pos.x, transform.pos.y);

        AABB[] boxes = new AABB[25];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i + j * 5] = world.getTileBoundingBox(
                        (int) (((transform.pos.x / 2) + 0.5f) - (5 / 2)) + i,
                        (int) (((-transform.pos.y / 2) + 0.5f) - (5 / 2)) + j
                );
            }
        }

        AABB box = null;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                if (box == null) box = boxes[i];

                Vector2f lenght1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f lenght2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                if (lenght1.lengthSquared() > lenght2.lengthSquared()) {
                    box = boxes[i];
                }
            }
        }
        if (box != null) {
            Collision collision = boundingBox.getCollision(box);
            if (collision.isIntersecting) {
                boundingBox.correctPosition(box, collision);
                transform.pos.set(boundingBox.getCenter(), 0);
            }
            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i] != null) {
                    if (box == null) box = boxes[i];

                    Vector2f lenght1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f lenght2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                    if (lenght1.lengthSquared() > lenght2.lengthSquared()) {
                        box = boxes[i];
                    }
                }
            }
            collision = boundingBox.getCollision(box);
            if (collision.isIntersecting) {
                boundingBox.correctPosition(box, collision);
                transform.pos.set(boundingBox.getCenter(), 0);
            }
        }
        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
        camera.setPosition(transform.pos.mul(-world.getScale(), new Vector3f()));

    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

}
