package com.iit.bin.map;

import com.iit.bin.collision.AABB;
import com.iit.bin.entities.AbstractEntity;
import com.iit.bin.entities.Character;
import com.iit.bin.entities.CharacterPosTrans;
import com.iit.bin.render.Camera;
import com.iit.bin.render.Shader;
import com.iit.bin.util.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapRenderer {
    private final int view = 33;
    private byte[] tiles;
    private int width;
    private int height;
    private int scale;
    private Matrix4f world;
    private AABB[] boundingBoxes;
    private List<AbstractEntity> abstractEntityList;

    public MapRenderer() {
        width = 64;
        height = 64;
        scale = 16;
        tiles = new byte[width * height];
        world = new Matrix4f().setTranslation(new Vector3f(0))
                .scale(scale)
        ;
        boundingBoxes = new AABB[width * height];
    }

    public MapRenderer(String world, Camera camera) {
        try {
            System.out.println("levels/"+world+"_tiles.png");
            BufferedImage tileSheet = ImageIO.read(getClass().getResource("/levels/" + world + "/tiles.png"));
            BufferedImage entitySheet = ImageIO.read(getClass().getResource("/levels/" + world + "/entities.png"));
            ;
            scale=16;
            width=tileSheet.getWidth();
            height= tileSheet.getHeight();
            this.world = new Matrix4f().setTranslation(new Vector3f(0)).scale(scale);
            int [] colorTileSheet =  tileSheet.getRGB(0,0,width, height,null, 0, width);
            int [] colorEntitySheet= entitySheet.getRGB(0,0,width,height,null,0,width);
            tiles= new byte[width*height];
            boundingBoxes= new AABB[width*height];
            abstractEntityList = new ArrayList<AbstractEntity>();
            CharacterPosTrans characterPosTrans;
            for (int y = 0; y <height ; y++) {
                for (int x = 0; x <width ; x++) {
                    int red= (colorTileSheet[x+y*width] >>16) & 0xFF;
                    int blue= colorTileSheet[x+y*width] & 0xFF;
                    int green= (colorTileSheet[x+y*width] >>8) & 0xFF;
                    int entityIndex=(colorEntitySheet[x+y*width]>>16)& 0xFF;
                    int entity_alpha=(colorEntitySheet[x+y*width]>>24)& 0xFF;
                    Tile r;
                    Tile b;
                    Tile g;
                 //   System.out.println(red+" "+green+" "+blue);


                      if (red==1){
                          System.out.println("ecc");
                          try {
                              r = Tile.tiles[red];

                          } catch (ArrayIndexOutOfBoundsException e) {
                              r = null;
                          }
                          if (r != null) {

                              setTile(r, x, y);
                          }
                      }
                      if (blue==1){
                          System.out.println("pecc");
                          try {
                              b = Tile.tiles[blue+1];

                          } catch (ArrayIndexOutOfBoundsException e) {
                              b = null;
                          }
                          if (b != null) {

                              setTile(b, x, y);
                          }
                      }
                      if (green==1){
                          System.out.println("kimehecc");
                           try {
                              g = Tile.tiles[green+2];

                          } catch (ArrayIndexOutOfBoundsException e) {
                              g = null;
                          }
                          if (g != null) {
                              setTile(g, x, y);
                          }
                      }



                    if (entity_alpha > 0) {
                        characterPosTrans = new CharacterPosTrans();
                        characterPosTrans.position.x = x;
                        characterPosTrans.position.y = -y;
                        switch (entityIndex) {
                            case 1: // character
                                Character character = new Character(characterPosTrans);
                                abstractEntityList.add(character);
                                camera.getPosition().set(characterPosTrans.position.mul(-scale, new Vector3f()));
                                break;
//                            case 2: //black magic
//                               TODO: BLACK MAGIC CLASS
//                                break;
//                                case3://white magic
//                                  TODO: WHITE MAGIC CLASS
//                                break;
//                            case4:
//                            TODO ENEMY CLASS
//                            break;
                            default:
                                break;
                        }
                    }
                }
            }
            // abstractEntityList.add(new Character(new CharacterPosTrans()));

            CharacterPosTrans t = new CharacterPosTrans();
            t.position.x=0;
            t.position.y=-4;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Matrix4f getWorld() {
        return world;
    }
    public int getScale() {
        return scale;
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
        int posX = (int) (cam.getPosition().x + (window.getWidth() / 2)) / (scale * 2);              //center of screen+offset of map
        int posY = (int) (cam.getPosition().y - (window.getHeight() / 2)) / (scale * 2);

        for (int i = 0; i < view * 1.3; i++) {
            for (int j = 0; j < view; j++) {
                Tile t = getTile(i - posX, j + posY);
                if (t != null)
                    render.renderTile(t, i - posX, -j - posY, shader, world, cam);
            }
        }
        for (AbstractEntity abstractEntity : abstractEntityList
                ) {
            abstractEntity.render(shader, cam, this);
        }
    }

    public void update(float delta, Window window, Camera camera){
        for (AbstractEntity abstractEntity : abstractEntityList
                ) {
            abstractEntity.update(delta, window, camera, this);
        }
        for (int i = 0; i < abstractEntityList.size(); i++) {
            abstractEntityList.get(i).collideWithTiles(this);
            for (int j = i + 1; j < abstractEntityList.size(); j++) {
                abstractEntityList.get(i).collideWithEntity(abstractEntityList.get(j));
            }
            abstractEntityList.get(i).collideWithTiles(this);
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
        if (tile.clipping()){
            boundingBoxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
        }
        else boundingBoxes[x + y * width] = null;
    }

}

