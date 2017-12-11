package com.iit.bin.render;

import com.iit.bin.io.Timer;

public class Animation {
    private Texture[] frames;
    private int pointer;
    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;

    public Animation(int amount, int fps, String filename) {
        this.currentTime = 0;
        this.elapsedTime = 0;
        this.fps = 1.0 / (double) fps;
        this.pointer = 0;
        this.lastTime = Timer.getTime();
        this.frames = new Texture[amount];
        for (int i = 0; i < amount; i++) {
            this.frames[i] = new Texture( filename + "/" + i + ".png");
        }

    }

    public void bind(int sampler) {
        this.currentTime = Timer.getTime();
        this.elapsedTime += currentTime - lastTime;

        if (elapsedTime >= fps) {
            elapsedTime = 0;
            pointer++;
        }
        if (pointer >= frames.length) pointer = 0;
        this.lastTime = currentTime;
        frames[pointer].bind(sampler);
    }

    public void bind() {
        bind(0);
    }


}
