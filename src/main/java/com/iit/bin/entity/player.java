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

public class Player  extends Entity{
public static final int ANIM_IDLE=0;
    public static final int ANIM_WALK_DOWN=1;
    public static final int ANIM_WALK_UP=2;
    public static final int ANIM_WALK_LEFT=3;
    public static final int ANIM_WALK_RIGHT=4;
    public static int animLast=0;

public static final int ANIM_SIZE=5;

    public Player( Transform transform) {
        super(ANIM_SIZE, transform);
        setAnimation(ANIM_IDLE, new Animation(1, 2, "player/idle"));
        setAnimation(ANIM_WALK_DOWN, new Animation(2,2, "player/walking/down"));
     setAnimation(ANIM_WALK_UP, new Animation(2,2, "player/walking/up"));
        setAnimation(ANIM_WALK_RIGHT, new Animation(2,2, "player/walking/right"));
        setAnimation(ANIM_WALK_LEFT, new Animation(2,2, "player/walking/left"));
    }

    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        Vector2f movement= new Vector2f();
        if (window.getInput().isKeyDown(GLFW_KEY_A)) {
            movement.add(-10*delta,0);
        }
        if (window.getInput().isKeyDown(GLFW_KEY_D)) {
          movement.add(10*delta,0);
        }
        if (window.getInput().isKeyDown(GLFW_KEY_W)) {
            movement.add(0,10*delta);
        }
        if (window.getInput().isKeyDown(GLFW_KEY_S)) {
            movement.add(0,-10*delta);
        }
        move(movement);


        //COMBAT GOES HERE

        if (window.getInput().isKeyPressed(GLFW_KEY_K)){
//            TODO BLACK MAGIC
//            use last anim, stop movement and create entity heading to direction
//
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_H)){
//            TODO WHITE MAGIC
//            use last anim, stop movement, and create sparkling effect on character
//
        }

      //  System.out.println("x= "+ movement.x + "y="+movement.y);
        if(movement.x==0 && movement.y<0){
         //   System.out.println("moving down");
            useAnimation(ANIM_WALK_DOWN);
            animLast=ANIM_WALK_DOWN;
      }
      else if(movement.x==0 && movement.y>0){
     //       System.out.println("moving up");
            useAnimation(ANIM_WALK_UP);
            animLast=ANIM_WALK_UP;
        }
        else if(movement.x<0 && movement.y==0){
   //         System.out.println("moving right");
            useAnimation(ANIM_WALK_RIGHT);
            animLast=ANIM_WALK_RIGHT;
        }
        else if(movement.x> 0&& movement.y==0){
       //     System.out.println("moving left");
            useAnimation(ANIM_WALK_LEFT);
            animLast=ANIM_WALK_LEFT;
        }

//
        else{
            useAnimation(ANIM_IDLE);
            animLast=ANIM_IDLE;
        }

        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
    }
}
