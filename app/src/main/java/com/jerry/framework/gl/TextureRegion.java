package com.jerry.framework.gl;

/**
 * Created by Pavel on 10. 12. 2014.
 */
public class TextureRegion {
    public final float u1,v1;
    public final float u2,v2;
    public final Texture texture;

    public TextureRegion(Texture texture, float x, float y, float width, float height){
        this.texture = texture;
        //coords of top left corner
        this.u1 = x/texture.width;
        this.v1 = y/texture.height;
        //coords of bottom right corner
        this.u2 = this.u1 + width / texture.width;
        this.v2 = this.v1 + height / texture.height;
    }


}
