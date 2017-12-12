package com.iit.bin.entities;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CharacterPosTrans {
    // positioning, scaling, rotation

    public Vector3f position;
    public Vector3f scale;

    public CharacterPosTrans() {
        position = new Vector3f();
        scale = new Vector3f(1, 1, 1);
    }

    public Matrix4f getProjection(Matrix4f target) {
        target.translate(position);
        target.scale(scale);
        return target;
    }

}
