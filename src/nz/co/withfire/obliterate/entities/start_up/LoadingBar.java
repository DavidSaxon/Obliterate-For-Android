/*********************\
| Just a loading bar. |
|                     |
| @author David Saxon |
\*********************/
package nz.co.withfire.obliterate.entities.start_up;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.utilities.*;

public class LoadingBar extends Entity {

    //VARIABLES
    //the progress of the loading bar
    private float progress = 0.0f;
    //the loading bar shape
    Quad2d bar;
    
    private float width;
    private float height;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    public LoadingBar(Vector2d GLdim) {
        
        width = Math.abs(GLdim.getX() / 1.25f);
        height = width / 59.0f;
        
        //create the coords of the quad
        float quadCoord[] = {-width,  height,   0.0f,
                             -width, -height, 0.0f,
                              width, -height, 0.0f,
                              width,  height,  0.0f};
        float quadColour[] = {  1.0f, 1.0f, 1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f, 1.0f};
        bar = new Quad2d(quadCoord, quadColour);
    }
    
    @Override
    public void update() {
        
        //stretch the bar across the screen
        bar.setPosition(0, new Vector3d(
            width - (width * 2 * progress), height,  0.0f));
        bar.setPosition(1, new Vector3d(
            width - (width * 2 * progress), -height, 0.0f));
        
        //fade the bar into black
        float col = 1.0f - (1.0f * progress);
        //cap the colour
        if (col < 0.0f) {
            
            col = 0.0f;
        }
        bar.setColour(0, new Vector4d(col, col, col, 1.0f));
        bar.setColour(1, new Vector4d(col, col, col, 1.0f));
    }

    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, 0, -0.25f, -0.01f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        bar.draw(mvpMatrix);
    }
    
    /**Updates the progress of the loading bar
    @param progress the new loading bar progress*/
    public void updateProgress(float progress) {
        
        this.progress = progress;
    }
}
