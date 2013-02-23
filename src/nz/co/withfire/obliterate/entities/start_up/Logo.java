/******************************************************\
| The obliterate logo displayed when the app launches. |
|                                                      |
| @author David Saxon                                  |
\******************************************************/
package nz.co.withfire.obliterate.entities.start_up;


import android.opengl.Matrix;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.QuadTex2d;

public class Logo extends Entity {
    
    //VARIABLES
    //the quad displaying the logo
    private QuadTex2d logo;
    
    //the texture of the logo
    private int tex;
    
   //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    public Logo(int tex) {
        
        this.tex = tex;
        
        float quadCoord[] = {   -1.5f,  0.8f, 0.0f,
                                -1.5f, -0.6f, 0.0f,
                                 1.5f, -0.6f, 0.0f,
                                 1.5f,  0.8f, 0.0f};
        float quadColour[] = {  0.8f, 0.8f, 0.8f, 1.0f,
                                0.8f, 0.8f, 0.8f, 1.0f,
                                0.8f, 0.8f, 0.8f, 1.0f,
                                0.8f, 0.8f, 0.8f, 1.0f};
        logo = new QuadTex2d(quadCoord, quadColour, tex);
    }
    
    //METHODS
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, 0, 0, -0.01f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        logo.draw(mvpMatrix);
    }
}
