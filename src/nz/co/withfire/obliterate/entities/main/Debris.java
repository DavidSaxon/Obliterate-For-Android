package nz.co.withfire.obliterate.entities.main;

import java.util.ArrayList;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.bounding_box.BoundingBox;
import nz.co.withfire.obliterate.bounding_box.BoundingRect;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.utilities.*;

public class Debris extends Entity {

    //VARIABLES
    //the position of the debris
    private Vector2d pos;
    //the side length of the vector
    private float sideLength;
    //the speed of the debriss
    private Vector2d speed;
    //the image of the debris
    private Quad2d image;
    //the bounding rectangle of the debris
    private BoundingRect boundingBox;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    /**Creates a new debris object
    @param pos the position of the debris
	@param sideLength the the length of a side of the debris
	@param speed the x and y speed of the the debris*/
    //TODO: NOT CALLED STRIDE
    public Debris(Vector2d pos, float sideLength, Vector2d speed) {
        
        //initialise the variables
        this.pos = new Vector2d(pos);
		this.sideLength = sideLength;
		this.speed = new Vector2d(speed);
        
		//Create the debris image
        float d = sideLength/2.0f;
        //TODO: texture with part of the texture
        float[] coord = {
            -d,  d, 0.0f,
            -d, -d, 0.0f,
             d, -d, 0.0f,
             d,  d, 0.0f,
        };
        float[] colour = {
          0.6f, 0.05f, 0.15f, 1.0f,
          0.6f, 0.05f, 0.15f, 1.0f,
          0.6f, 0.05f, 0.15f, 1.0f,
          0.6f, 0.05f, 0.15f, 1.0f
        };
        
        image = new Quad2d(coord, colour);
        
        //create the bounding box
        boundingBox = new BoundingRect(pos, new Vector2d(sideLength, sideLength));
    }
    
    //METHODS
    @Override
    public void collisionCheck(Entity other) {
        
        //TODO:
    }

    @Override
    public ArrayList<Entity> update() {

        //TODO: vector addition!
		//move the debris
        pos.setX(pos.getX() + speed.getX());
        pos.setY(pos.getY() + speed.getY());
        
        return null;
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
    public BoundingBox getBoundingBox() {
        
        return null;
    }
}
