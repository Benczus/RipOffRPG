package com.iit.bin.world;

public class Tile {

    public static final Tile test_tile = new Tile("grass");
    public static final Tile test2 = new Tile("fire");
    public static Tile tiles[] = new Tile[16];
    public static byte tilenumber = 0;
    private byte id;
    private String texture;

    public Tile(String texture) {
        this.id = tilenumber++;
        this.texture = texture;

        if (tiles[id] != null) {
            throw new IllegalStateException("Tiles at :" + id + " is already being used!");
        }
        tiles[id] = this;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }


}
