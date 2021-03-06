package com.iit.bin.render;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture {
    private int id;
    private int height;
    private int width;

    // black magic
    public Texture(String filename) {
        BufferedImage bufferedImage;
        try {
               System.out.println("textures/" + filename);
            bufferedImage = ImageIO.read(getClass().getResource("/textures/" + filename));  // NEM MŰKÖDIK A RELATÍV ELÉRÉS. MIÉRT???!
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

            int[] pixels_raw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = pixels_raw[i * width + j];
                    pixels.put((byte) ((pixel >> 16) & 0xFF)); //RED
                    pixels.put((byte) ((pixel >> 8) & 0xFF));  //GREEN
                    pixels.put((byte) (pixel & 0xFF));        //BLUE
                    pixels.put((byte) ((pixel >> 24) & 0xFF)); //ALPHA
                }
            }

            pixels.flip();
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void bind(int sampler) {
        if (sampler >= 1 || sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler); //GL_TEXTURE0 -> első samplerhez teszi a textúrát. ha hozzá adod a samplert, akkor x-1-edik samplerhez teszi!
            glBindTexture(GL_TEXTURE_2D, id);
        }

    }

    protected void finalize() throws Throwable {
        glDeleteTextures(id);
        super.finalize();
    }

}
