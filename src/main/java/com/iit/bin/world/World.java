package com.iit.bin.world;

import com.iit.bin.collision.AABB;
import com.iit.bin.io.Window;
import com.iit.bin.render.Camera;
import com.iit.bin.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class World {
    private final int view = 33;
    private byte[] tiles;
    private int width;
    private int height;
    private int scale;
    private Matrix4f world;
    private AABB[] boundingBoxes;

    public World() {
        width = 64;
        height = 64;
        scale = 16;
        tiles = new byte[width * height];
        world = new Matrix4f().setTranslation(new Vector3f(0))
      .scale(scale)
        ;
        boundingBoxes = new AABB[width * height];


    }

    public World(String world){
        try {
            System.out.println("levels/"+world+"_tiles.png");

            // BufferedImage tileSheet= ImageIO.read(new File("C:\\Munka\\CheapShootemUp-master\\src\\main\\resources\\levels\\"+world+"_tiles.png"));
            BufferedImage tileSheet = ImageIO.read(getClass().getResource("/levels/" + world + "_tiles.png"));

            //       BufferedImage entitySheet= ImageIO.read(new File("./levels/"+world+"_entities.png"));
            scale=16;

            width=tileSheet.getWidth();
            height= tileSheet.getHeight();


            this.world = new Matrix4f().setTranslation(new Vector3f(0))
                    .scale(scale)
            ;
            int [] colorTileSheet =  tileSheet.getRGB(0,0,width, height,null, 0, width);

            tiles= new byte[width*height];
            boundingBoxes= new AABB[width*height];

            for (int y = 0; y <height ; y++) {
                for (int x = 0; x <width ; x++) {
                    int red= (colorTileSheet[x+y*width] >>16) & 0xFF;

                   Tile t;
                   try {
                       t = Tile.tiles[red];
                   }
                   catch (ArrayIndexOutOfBoundsException e){
                       t=null;
                   }
                   if (t!=null){
                       setTile(t,x,y);
                   }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Matrix4f getWorld() {
        return world;
    }

    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public AABB getTileBoundingBox(int x, int y) {
        try {
            return boundingBoxes[x + y * width];
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
        if (tile.isSolid()){
            boundingBoxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
        }

        else boundingBoxes[x + y * width] = null;
    }


    public int getScale() {
        return scale;
    }
}

