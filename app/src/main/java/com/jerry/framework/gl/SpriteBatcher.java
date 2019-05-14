package com.jerry.framework.gl;

import android.util.FloatMath;

import com.jerry.framework.impl.GLGraphics;
import com.jerry.framework.math.Vector2;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pavel on 10. 12. 2014.
 */
public class SpriteBatcher {
    //temporary float array in which we store the vertices of the sprites of the current batch
    final float[] verticesBuffer;
    //bufferIndex indicates where in the float array we should start to write the next vertices
    int bufferIndex;
    //Vertices instance used to render the batch
    final Vertices vertices;
    //the number drawn so far in the current batch
    int numSprites;

    public SpriteBatcher(GLGraphics glGraphics, int maxSprites) {
        /*
        we have four vertices per sprite, and each vertex takes up four floats
        (two for the x and y coordinates and another two for the texture coordinates)
         */
        this.verticesBuffer = new float[maxSprites*4*4];
        //We need it to store maxSprites × 4 vertices and maxSprites × 6 indices
        this.vertices = new Vertices(glGraphics, maxSprites*4, maxSprites*6, false, true);
        this.bufferIndex = 0;
        this.numSprites = 0;

        /*
        The first sprite in a batch will always have the indices 0, 1, 2,
        2, 3, 0; the next sprite will have 4, 5, 6, 6, 7, 4; and so on
         */
        short[] indices = new short[maxSprites*6];
        int len = indices.length;
        short j = 0;
        for (int i = 0; i < len; i += 6, j += 4) {
            indices[i + 0] = (short)(j + 0);
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = (short)(j + 0);
        }
        vertices.setIndices(indices, 0, indices.length);
    }

    /*
    binds the texture and resets the numSprites and
    bufferIndex members so that the first sprite’s vertices will get inserted at the front of the
    verticesBuffer float array
     */
    public void beginBatch(Texture texture){
        texture.bind();
        numSprites = 0;
        bufferIndex = 0;
    }

    public void endBatch(){
        vertices.setVertices(verticesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
        vertices.unbind();
    }

    public void drawSprite(float x, float y, float width, float height, TextureRegion region){
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        float x1 = x - halfWidth;
        float y1 = y - halfHeight;
        float x2 = x + halfWidth;
        float y2 = y + halfHeight;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }

    public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion region) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float rad = angle * Vector2.TO_RADIANS;
        float cos = FloatMath.cos(rad);
        float sin = FloatMath.sin(rad);

        float x1 = -halfWidth * cos - (-halfHeight) * sin;
        float y1 = -halfWidth * sin + (-halfHeight) * cos;
        float x2 = halfWidth * cos - (-halfHeight) * sin;
        float y2 = halfWidth * sin + (-halfHeight) * cos;
        float x3 = halfWidth * cos - halfHeight * sin;
        float y3 = halfWidth * sin + halfHeight * cos;
        float x4 = -halfWidth * cos - halfHeight * sin;
        float y4 = -halfWidth * sin + halfHeight * cos;

        x1 += x;
        y1 += y;
        x2 += x;
        y2 += y;
        x3 += x;
        y3 += y;
        x4 += x;
        y4 += y;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }

}
