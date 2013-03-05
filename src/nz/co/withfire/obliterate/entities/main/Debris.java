package nz.co.withfire.obliterate.entities.main;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.QuadTex2d;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.bounding.BoundingRect;
import nz.co.withfire.obliterate.utilities.*;

public class Debris extends CollisionType {

    //VARIABLES
    //the position of the debris
    private Vector2d pos;
    //the side length of the vector
    private float sideLength;
    
    //the debris is only affected by the force once
    private boolean forceApplied = false;
    //is true if gravity should be applied
    private boolean applyGravity = false;
    
    //the texture
    private int tex;
    
    //the image of the debris
    private QuadTex2d image;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the transformation matrix
    private float[] tMatrix = new float[16];
    
    
    //CONSTRUCTOR
    /**Creates a new debris object
    @param pos the position of the debris
	@param sideLength the the length of a side of the debris
	@param speed the x and y speed of the the debris*/
    public Debris(Vector2d pos, float sideLength, Vector2d speed,
        float[] texCoords, int tex) {
        
        //initialise the variables
        this.pos = new Vector2d(pos);
		this.sideLength = sideLength;
		this.speed = new Vector2d(speed);
		this.tex = tex;
        
		//Create the debris image
        float d = sideLength/2.0f;
        //TODO: texture with part of the texture
        float[] coord = {
            -d,  d, 0.0f,
            -d, -d, 0.0f,
             d, -d, 0.0f,
             d,  d, 0.0f,
        };
        
        image = new QuadTex2d(coord, texCoords, tex);
        
        //set the bounding box
        boundingBox = new BoundingRect(pos,
                new Vector2d(sideLength, sideLength));
    }
    
    //METHODS
    @Override
    public void update() {
        
		//move the debris
        pos.add(speed);
        
        //translate the bounding box 
        boundingBox.setPos(pos);
    }

    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
        //shift into visible range and move
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX(), pos.getY(), -0.01f);
        
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
        
        return new Vector2d(sideLength, sideLength);
    }
    
    @Override
    public Vector2d getSpeed() {
        
        return speed;
    }
    
    /**@return true if the force has been applied to this debris*/
    public boolean getForceApplied() {
        
        return forceApplied;
    }
    
    public boolean getApplyGravity() {
        
        return applyGravity;
    }
    
    /**Sets if the force is applied
    @param forceApplied whether the force has been applied*/
    public void setForceApplied(boolean forceApplied) {
        
        this.forceApplied = forceApplied;
    }
    
    /**Sets gravity to be applied*/
    public void applyGravity() {
        
        applyGravity = true;
    }

}
