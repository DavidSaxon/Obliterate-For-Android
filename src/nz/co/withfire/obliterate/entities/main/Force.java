/***********************************************************\
| Expanding point of force that causes images to obliterate |
|                                                           |
| @author David Saxon                                       |
\***********************************************************/

package nz.co.withfire.obliterate.entities.main;

import java.util.ArrayList;

import android.opengl.Matrix;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.utilities.Vector2d;

public class Force extends Entity {
    
    //VARIABLES
    //the position of the force
    private Vector2d pos;
    //the scale of the force
    private float scale = 0.0f;
    
    //the image of the force
    Quad2d image;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    //the scale matrix
    private float[] sMatrix = new float[16];
    
    //CONSTRUCTOR
    /**Creates a new force point
    @param pos the position of the force*/
    public Force(Vector2d pos) {
        
        //copy the position
        this.pos = new Vector2d(pos);
        
        //TODO: add a texture to the quad
        
        float[] coord = {   -1.0f,  1.0f, 0.0f,
                            -1.0f, -1.0f, 0.0f,
                             1.0f, -1.0f, 0.0f,
                             1.0f,  1.0f, 0.0f
                        };
        float[] colour = {  0.7f, 0.8f, 0.9f, 0.3f,
                            0.7f, 0.8f, 0.9f, 0.3f,
                            0.7f, 0.8f, 0.9f, 0.3f,
                            0.7f, 0.8f, 0.9f, 0.3f
                         };
        image = new Quad2d(coord, colour);
    }
    
    //PUBLIC METHODS
    @Override
    public ArrayList<Entity> update() {
        
        //increase the scale of the image
        scale += 0.05f;
        
        return null;
    }
    
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
        //shift into visible range and move
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX(), pos.getY(), -0.01f);
        Matrix.scaleM(tMatrix, 0, scale, scale, 1.0f);
        
        //scale the matrix
        //Matrix.setIdentityM(sMatrix, 0);
        //Matrix.scaleM(sMatrix, 0, scale, scale, 0.0f);
        
        //Multiply matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        //Matrix.multiplyMM(mvpMatrix, 0, sMatrix, 0, mvpMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
}
