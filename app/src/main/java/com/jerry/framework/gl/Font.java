package com.jerry.framework.gl;

/**
 * Created by Pavel on 13. 12. 2014.
 */
public class Font {
    //texture containing the font’s glyph
    public final Texture texture;
    //width and height of a single glyph
    public final int glyphWidth;
    public final int glyphHeight;
    //array of TextureRegions—one for each glyph
    public final TextureRegion[] glyphs = new TextureRegion[96];

    //loads fonts into our glyphs array
    public Font(Texture texture, int offsetX, int offsetY, int glyphsPerRow, int glyphWidth, int glyphHeight){
        this.texture = texture;
        this.glyphHeight = glyphHeight;
        this.glyphWidth = glyphWidth;
        int x = offsetX;
        int y = offsetY;
        for(int i = 0; i < 96; i++){
            glyphs[i] = new TextureRegion(texture, x,y, glyphWidth, glyphHeight);
            x+=glyphWidth;
            if(x == offsetX + glyphsPerRow * glyphWidth){
                x = offsetX;
                y += glyphHeight;
            }
        }
    }

    public void drawText(SpriteBatcher batcher, String text, float x, float y){
        int len = text.length();
        for(int i = 0; i < len; i++){
            /*
            the first element corresponding to the ASCII character has code 32. Decrement to get
            the right character
             */
            int c = text.charAt(i) - ' ';
            if(c < 0 || c > glyphs.length-1){
                continue;
            }
            TextureRegion glyph = glyphs[c];
            batcher.drawSprite(x,y,glyphWidth,glyphHeight,glyph);
            x+=glyphWidth;
        }
    }
}
