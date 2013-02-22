/*********************\
| Loads texture data  |
|                     |
| @author David Saxon |
\*********************/

package nz.co.withfire.obliterate.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureLoader {

    /**Loads the images at the resource id as a openGL Texture
    @param context //TODO:
    @param resourceID the resource id
    @return the openGL id of the texture*/
    public static int loadTexture(final Context context,
        final int resourceID) {
        
        //create a pointer for the texture
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        
        //if the we the hanlde to the texture
        if (textureHandle[0] != 0) {
            
            
            final BitmapFactory.Options options = new BitmapFactory.Options();
            //no pre-scaling
            options.inScaled = false;
            
            //read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), resourceID, options);
            
            //bind the texture in openGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
            
            //set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            
            //load in the texture
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            
            //recycle the bitmap since the data has been loaded into OpenGL
            bitmap.recycle();
        }
        
        //we don't have a handle to the texture :(
        if (textureHandle[0] == 0) {
            
            throw new RuntimeException("Error loading texture");
        }
        
        return textureHandle[0];
    }
    
}
