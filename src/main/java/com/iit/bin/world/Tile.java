package com.iit.bin.world;

public class Tile {
   public static final Tile test2 = new Tile("stone").setSolid();
    public static Tile tiles[] = new Tile[255];
  public static final Tile test_tile = new Tile("grass");
    public static byte tilenumber = 0;


    private byte id;
    private String texture;
    private boolean isSolid;

    public Tile(String texture) {
        this.id = tilenumber++;
        this.texture = texture;
        this.isSolid = false;
        if (tiles[id] != null) {
            throw new IllegalStateException(id + " is already being used!");
        }
        tiles[id] = this;
    }

    public Tile setSolid() {
        this.isSolid = true;
        return this;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }


}
