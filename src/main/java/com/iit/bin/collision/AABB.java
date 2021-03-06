package com.iit.bin.collision;

import org.joml.Vector2f;

public class AABB {
    private Vector2f center, halfExtent;

    public AABB(Vector2f center, Vector2f halfExtent) {

        this.center = center;
        this.halfExtent = halfExtent;
    }

    public Vector2f getCenter() {
        return center;
    }

    public Collision collides(AABB box2) {
        Vector2f distance = box2.center.sub(center, new Vector2f());
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);
        distance.sub(halfExtent.add(box2.halfExtent, new Vector2f()));
        return new Collision(distance, distance.x < 0 && distance.y < 0);
    }

    public void correctPosition(AABB box2, Collision collision) {
        Vector2f correction = box2.center.sub(center, new Vector2f());
        if (collision.distance.x > collision.distance.y) {
            if (correction.x > 0) {
                center.add(collision.distance.x, 0);
            } else {
                center.add(-collision.distance.x, 0);
            }

        } else {
            if (correction.y > 0) {

                center.add(0, collision.distance.y);

            } else {
                center.add(0, -collision.distance.y);
            }
        }
    }
}
