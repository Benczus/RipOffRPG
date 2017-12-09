package com.iit.bin.world;

import com.iit.bin.io.Window;
import com.iit.bin.render.Camera;
import com.iit.bin.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;


public class World {
    private final int view = 33;
    private byte[] tiles;
    private int width;
    private int height;
    private int scale;
    private Matrix4f world;

    public World() {
        width = 64;
        height = 64;
        scale = 16;
        tiles = new byte[width * height];
        world = new Matrix4f().setTranslation(new Vector3f(0))
        //     .scale(scale)
        ;

        world.scale(scale);

    }

    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void render(TileRenderer render, Shader shader, Camera cam, Window window) {
        int posX = (int) (cam.getPosition().x + (window.getWidth() / 2)) / (scale * 2);              //center of screen+offset of world
        int posY = (int) (cam.getPosition().y - (window.getHeight() / 2)) / (scale * 2);

        for (int i = 0; i < view * 1.3; i++) {
            for (int j = 0; j < view; j++) {
                Tile t = getTile(i - posX, j + posY);
                if (t != null)
                    render.renderTile(t, i - posX, -j - posY, shader, world, cam);
            }

        }

    }


    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int w = -width * scale * 2;
        int h = height * scale * 2;

        if (pos.x > -(window.getWidth() / 2) + scale)
            pos.x = -(window.getWidth() / 2) + scale;

        if (pos.x < w + (window.getWidth() / 2) + scale)
            pos.x = w + window.getWidth() / 2 + scale;

        if (pos.y < (window.getHeight() / 2) - scale)
            pos.y = window.getHeight() / 2 - scale;

        if (pos.y > h - (window.getHeight() / 2) - scale)
            pos.y = h - (window.getHeight() / 2) - scale;

    }

    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
    }


}
