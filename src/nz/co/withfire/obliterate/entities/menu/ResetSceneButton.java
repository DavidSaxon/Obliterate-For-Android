/******************************\
| Button that resets the scene |
|                              |
| @author David Saxon          |
\******************************/
package nz.co.withfire.obliterate.entities.menu;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.QuadTex2d;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.bounding.BoundingRect;
import nz.co.withfire.obliterate.utilities.Vector2d;

public class ResetSceneButton extends CollisionType {

    //VARIABLES
    //the orginal position of the button
    private Vector2d orgPos = new Vector2d();
    //the position of the button
    private Vector2d pos = new Vector2d();
    //the dimensions of the bounding box
    private Vector2d dim = new Vector2d();
    //the opengl dimensions
    private Vector2d GLdim = new Vector2d();
    
    //is true if the button should be sliding forwards
    private boolean slideForwards = false;
    private boolean slideBack = false;
    
    //the rotation amount of the button
    private float rotation = 0.0f;
    private boolean rot = false;
    
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
    public ResetSceneButton(Vector2d GLdim, int tex, boolean rot) {
        
        this.GLdim.copy(GLdim);
        this.tex = tex;
        this.rot = rot;
        
        orgPos.set(GLdim.getX() / 1.1f,
            Math.abs(GLdim.getY() / 1.15f));
        pos.copy(orgPos);
        
        //find the dimensions of the button
        dim.set(GLdim.getX() / 23.5f, GLdim.getX() / 23.5f);
        
        float quadCoord[] = {   -dim.getX(),  dim.getY(), 0.0f,
                                -dim.getX(), -dim.getY(), 0.0f,
                                 dim.getX(), -dim.getY(), 0.0f,
                                 dim.getX(),  dim.getY(), 0.0f};
        final float[] texCoords = { 1.0f, 0.0f,
                                    1.0f, 1.0f,
                                    0.0f, 1.0f, 
                                    0.0f, 0.0f};
        image = new QuadTex2d(quadCoord, texCoords, tex);
        
        //set the bounding box to be slightly bigger than the size
        boundingBox = new BoundingRect(pos, dim);
    }
    
    //METHODS
    @Override
    public void update() {
        
        if (rot) {
            
            //roatate the button
            if (rotation < 360.0f) {
                
                rotation += 15.0f;
            }
            else {
                
                rotation = 360.0f;
            }
        }
        
        //slide forwards
        if (slideForwards) {
            
            //slide
            if (pos.getX() < GLdim.getX() + (2.0f * dim.getX())) {
                
                //TODO: make sure it doesn't slide too far
                
                float shiftAmount = GLdim.getX() / 15.0f;
                
                pos.setX(pos.getX() + shiftAmount);
                boundingBox.translate(new Vector2d(shiftAmount, 0.0f));
            }
            else {
                
                slideForwards = false;
            }
        }
        //slide back
        if (slideBack) {
            
            //slide
            if (pos.getX() > orgPos.getX()) {
                
                //TODO: make sure it doesn't slide too far
                
                float shiftAmount = GLdim.getX() / 15.0f;
                
                pos.setX(pos.getX() - shiftAmount);
                boundingBox.translate(new Vector2d(-shiftAmount, 0.0f));
            }
            else {
                
                pos.copy(orgPos);
                slideBack = false;
            }
        }
    }
    
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX(), pos.getY(), -0.01f);
        Matrix.rotateM(tMatrix, 0, -rotation, 0, 0, 1.0f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
    
    /**Is called to set the button sliding forwards*/
    public void slideForwards() {
        
        slideForwards = true;
    }
    
    public void slideBack() {
        
        slideBack = true;
    }
}
