package com.iit.bin.map;

public class Tile {
    public static Tile tiles[] = new Tile[255];
    public static final Tile grass = new Tile("grass");
    public static final Tile castlewall = new Tile("stone").setClipping();
    public static final Tile castletile = new Tile("tile");
    public static final Tile castletop= new Tile("top").setClipping();


    public static byte tilenumber = 0;


    private byte id;
    private String texture;
    private boolean clippable;

    public Tile(String texture) {
        this.id = tilenumber++;
        this.texture = texture;
        this.clippable = false;
        if (tiles[id] != null) {
            throw new IllegalStateException(id + " is already being used!");
        }
        tiles[id] = this;
        System.out.println(id+" "+ texture);
    }

    public Tile setClipping() {
        this.clippable = true;
        return this;
    }

    public boolean clipping() {
        return clippable;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }


}
