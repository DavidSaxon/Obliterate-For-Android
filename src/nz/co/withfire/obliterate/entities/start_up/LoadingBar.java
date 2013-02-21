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
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    public LoadingBar() {
        
        //create the coords of the quad
        float quadCoord[] = {-1.5f, -.8f,   0.0f,
                             -1.5f, -0.75f, 0.0f,
                              1.5f, -0.75f, 0.0f,
                              1.5f, -0.8f,  0.0f};
        float quadColour[] = {  1.0f, 1.0f, 1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f, 1.0f};
        bar = new Quad2d(quadCoord, quadColour);
    }
    
    @Override
    public void update() {
        
        //stretch the bar across the screen
        bar.setPosition(0, new Vector3d(1.5f - (2.98f * progress), -0.8f,  0.0f));
        bar.setPosition(1, new Vector3d(1.5f - (2.98f * progress), -0.75f, 0.0f));
        
        //fade the bar into black
        float col = 1.0f - (1.0f * progress);
        bar.setColour(0, new Vector4d(col, col, col, col));
        bar.setColour(1, new Vector4d(col, col, col, col));
    }

    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, 0, 0, -0.01f);
        
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
