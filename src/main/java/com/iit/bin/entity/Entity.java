package com.iit.bin.entity;

import com.iit.bin.collision.AABB;
import com.iit.bin.collision.Collision;
import com.iit.bin.io.Window;
import com.iit.bin.render.Animation;
import com.iit.bin.render.Camera;
import com.iit.bin.render.Model;
import com.iit.bin.render.Shader;
import com.iit.bin.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Entity {

    private static Model model;
   protected Transform transform;
   protected AABB boundingBox;
   protected int useAnimation;
    private Animation[] animations;

    public Entity(int maxAnimationCount, Transform transform) {
        this.animations = new Animation[maxAnimationCount];
        this.transform=transform;
        this.useAnimation=0;
        transform = new Transform();

        boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
    }

public static void initAsset(){
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
}

public static void deleteAsset(){
    model=null;
}

    public void move(Vector2f direction) {
        transform.pos.add(new Vector3f(direction, 0));
        boundingBox.getCenter().set(transform.pos.x, transform.pos.y);
    }

    public void collideWithTiles(World world){
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
    }

    protected void setAnimation(int index, Animation animation){
        animations[index]= animation;
    }
    public void useAnimation(int index){
        this.useAnimation=index;
    }

      public abstract void  update(float delta, Window window, Camera camera, World world);

    public void render(Shader shader, Camera camera, World world) {
        Matrix4f target= camera.getProjection();
        target.mul(world.getWorld());


        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(target));
        animations[useAnimation].bind(0);
        model.render();
    }

    public void collideWithEntity(Entity entity) {
        Collision collision= boundingBox.getCollision(entity.boundingBox);
        if (collision.isIntersecting){
            collision.distance.x/=2;
            collision.distance.y/=2;
            boundingBox.correctPosition(entity.boundingBox, collision);
            transform.pos.set(boundingBox.getCenter().x, boundingBox.getCenter().y, 0);


            entity.boundingBox.correctPosition(this.boundingBox, collision);
            entity.transform.pos.set(entity.boundingBox.getCenter().x, entity.boundingBox.getCenter().y, 0);
        }
    }
}
