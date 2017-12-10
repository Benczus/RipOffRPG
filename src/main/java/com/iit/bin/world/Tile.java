package com.iit.bin.world;

public class Tile {
  public static final Tile test_tile = new Tile("grass");
   public static final Tile test2 = new Tile("star").setSolid();
    public static Tile tiles[] = new Tile[255];
    public static byte tilenumber = 0
            ;

    private byte id;
    private String texture;
    private boolean solid;

    public Tile(String texture) {

        this.id = tilenumber++;
        this.texture = texture;
        this.solid = false;


        if (tiles[id] != null) {
            throw new IllegalStateException("Tiles at :" + id + " is already being used!");

        }


        tiles[id] = this;


    }

    public Tile setSolid() {
        this.solid = true;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }


}
