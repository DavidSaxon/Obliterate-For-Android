/****************************\
| Button that opens the menu |
|                            |
| @author David Saxon        |
\****************************/

package nz.co.withfire.obliterate.entities.menu;

import android.opengl.Matrix;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.QuadTex2d;
import nz.co.withfire.obliterate.physics.CollisionType;

public class openMenuButton extends CollisionType {
    
    //VARIABLES
    //the rotation amount of the button
    private float rotation = 0.0f;
    
    //the image of the button
    private QuadTex2d image;
    
    //the texture of the image
    private int tex;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    /**Creates a new open menu button
    @param tex the texture of the button*/
    public openMenuButton(int tex) {
        
        this.tex = tex;
        
        
        float quadCoord[] = {   -0.075f,  0.075f, 0.0f,
                                -0.075f, -0.075f, 0.0f,
                                 0.075f, -0.075f, 0.0f,
                                 0.075f,  0.075f, 0.0f};
        final float[] texCoords = { 1.0f, 0.0f,
                                    1.0f, 1.0f,
                                    0.0f, 1.0f, 
                                    0.0f, 0.0f};
        image = new QuadTex2d(quadCoord, texCoords, tex);
    }
    
    //METHODS
    @Override
    public void update() {
        
        rotation += 1.0f;
    }
    
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, -1.6f, 0.85f, -0.01f);
        Matrix.rotateM(tMatrix, 0, rotation, 0, 0, 1.0f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
}
