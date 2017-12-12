package com.iit.bin.entities;

import com.iit.bin.collision.AABB;
import com.iit.bin.collision.Collision;
import com.iit.bin.map.MapRenderer;
import com.iit.bin.render.Animation;
import com.iit.bin.render.Camera;
import com.iit.bin.render.Model;
import com.iit.bin.render.Shader;
import com.iit.bin.util.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class AbstractEntity {

    private static Model model;
    protected CharacterPosTrans characterPosTrans;
   protected AABB boundingBox;
   protected int useAnimation;
    private Animation[] animations;

    public AbstractEntity(int maxAnimationCount, CharacterPosTrans characterPosTrans) {
        this.animations = new Animation[maxAnimationCount];
        this.characterPosTrans = characterPosTrans;
        this.useAnimation=0;
        characterPosTrans = new CharacterPosTrans();
        boundingBox = new AABB(new Vector2f(characterPosTrans.position.x, characterPosTrans.position.y), new Vector2f(characterPosTrans.scale.x, characterPosTrans.scale.y));
    }

public static void initializeEntity(){
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

public static void deleteEntity(){
    model=null;
}

    protected void setAnimation(int index, Animation animation){
        animations[index]= animation;
    }

    public void useAnimation(int index){
        this.useAnimation=index;
    }

    public abstract void update(float delta, Window window, Camera camera, MapRenderer mapRenderer);

    public void render(Shader shader, Camera camera, MapRenderer mapRenderer) {
        Matrix4f target= camera.getProjection();
        target.mul(mapRenderer.getWorld());


        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", characterPosTrans.getProjection(target));
        animations[useAnimation].bind(0);
        model.render();
    }

    public void moveEntity(Vector2f direction) {
        characterPosTrans.position.add(new Vector3f(direction, 0));
        boundingBox.getCenter().set(characterPosTrans.position.x, characterPosTrans.position.y);
    }

    public void collideWithEntity(AbstractEntity abstractEntity) {
        Collision collision = boundingBox.collides(abstractEntity.boundingBox);
        if (collision.isIntersecting){
            collision.distance.x/=2;
            collision.distance.y/=2;
            boundingBox.correctPosition(abstractEntity.boundingBox, collision);
            characterPosTrans.position.set(boundingBox.getCenter().x, boundingBox.getCenter().y, 0);


            abstractEntity.boundingBox.correctPosition(this.boundingBox, collision);
            abstractEntity.characterPosTrans.position.set(abstractEntity.boundingBox.getCenter().x, abstractEntity.boundingBox.getCenter().y, 0);
        }
    }

    public void collideWithTiles(MapRenderer mapRenderer) {
        AABB[] boxes = new AABB[25];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i + j * 5] = mapRenderer.getTileBoundingBox(
                        (int) (((characterPosTrans.position.x / 2) + 0.5f) - (5 / 2)) + i,
                        (int) (((-characterPosTrans.position.y / 2) + 0.5f) - (5 / 2)) + j
                );
            }
        }

        AABB box = null;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                if (box == null) box = boxes[i];

                Vector2f lenght1 = box.getCenter().sub(characterPosTrans.position.x, characterPosTrans.position.y, new Vector2f());
                Vector2f lenght2 = boxes[i].getCenter().sub(characterPosTrans.position.x, characterPosTrans.position.y, new Vector2f());

                if (lenght1.lengthSquared() > lenght2.lengthSquared()) {
                    box = boxes[i];
                }
            }
        }
        if (box != null) {
            Collision collision = boundingBox.collides(box);
            if (collision.isIntersecting) {
                boundingBox.correctPosition(box, collision);
                characterPosTrans.position.set(boundingBox.getCenter(), 0);
            }
            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i] != null) {
                    if (box == null) box = boxes[i];

                    Vector2f lenght1 = box.getCenter().sub(characterPosTrans.position.x, characterPosTrans.position.y, new Vector2f());
                    Vector2f lenght2 = boxes[i].getCenter().sub(characterPosTrans.position.x, characterPosTrans.position.y, new Vector2f());

                    if (lenght1.lengthSquared() > lenght2.lengthSquared()) {
                        box = boxes[i];
                    }
                }
            }
            collision = boundingBox.collides(box);
            if (collision.isIntersecting) {
                boundingBox.correctPosition(box, collision);
                characterPosTrans.position.set(boundingBox.getCenter(), 0);
            }
        }
    }




}
