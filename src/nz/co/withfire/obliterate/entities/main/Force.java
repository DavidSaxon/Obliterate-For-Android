/***********************************************************\
| Expanding point of force that causes images to obliterate |
|                                                           |
| @author David Saxon                                       |
\***********************************************************/

package nz.co.withfire.obliterate.entities.main;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.bounding.BoundingCircle;
import nz.co.withfire.obliterate.utilities.Vector2d;
import nz.co.withfire.obliterate.utilities.Vector4d;

public class Force extends CollisionType {
    
    //VARIABLES
    //the position of the force
    private Vector2d pos;
    //the speed of the force
    private Vector2d speed;
    //the scale of the force
    private float scale = 0.0f;
    //the fade of the force
    private float fade = 1.0f;
    
    //the image of the force
    Quad2d image;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the transformation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    /**Creates a new force point
    @param pos the position of the force*/
    public Force(Vector2d pos) {
        
        //copy the position
        this.pos = new Vector2d(pos);
        //set the speed
        speed = new Vector2d(0.045f, 0.045f);
        
        //TODO: add a texture to the quad
        
        float[] coord = {   -1.0f,  1.0f, 0.0f,
                            -1.0f, -1.0f, 0.0f,
                             1.0f, -1.0f, 0.0f,
                             1.0f,  1.0f, 0.0f
                        };
        float[] colour = {  0.7f, 0.8f, 0.9f, 1.0f,
                            0.7f, 0.8f, 0.9f, 1.0f,
                            0.7f, 0.8f, 0.9f, 1.0f,
                            0.7f, 0.8f, 0.9f, 1.0f
                         };
        image = new Quad2d(coord, colour);
        
        //force is immovable
        immovable = true;
        //set the bounding box
        boundingBox = new BoundingCircle(pos, 1.0f);
        //scale the bounding box
        boundingBox.scale(scale);
    }
    
    //PUBLIC METHODS
    @Override
    public void update() {
        
        //increase the scale of the image
        scale += 0.05f;
        //decrease the fade
        fade -= 0.04f;
        if (fade <= 0.0f) {
            
            remove = true;
        }
        
        image.setColour(new Vector4d(0.7f, 0.8f, 0.9f, fade));
        
        //scale the bounding box
        boundingBox.scale(scale);
    }
    
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
        //shift into visible range and move
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX(), pos.getY(), -0.01f);        
        Matrix.scaleM(tMatrix, 0, scale, scale, 1.0f);
        
        //Multiply matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
    
    @Override
    public Vector2d getPos() {
        
        return pos;
    }
    
    @Override
    public Vector2d getDim() {
        
        float r = ((BoundingCircle) boundingBox).getRadius();
        
        return new Vector2d(r * 2.0f, r * 2.0f);
    }
    
    @Override
    public Vector2d getSpeed() {
        
        return speed;
    }
}
