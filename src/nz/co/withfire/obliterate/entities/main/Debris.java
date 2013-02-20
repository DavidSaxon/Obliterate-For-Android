package nz.co.withfire.obliterate.entities.main;

import java.util.ArrayList;
import java.util.Random;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.physics.CollisionData;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.CollisionData.EntityType;
import nz.co.withfire.obliterate.physics.bounding.BoundingRect;
import nz.co.withfire.obliterate.utilities.*;

public class Debris extends CollisionType {

    //VARIABLES
    //the position of the debris
    private Vector2d pos;
    //the side length of the vector
    private float sideLength;
    //the speed of the debris
    private Vector2d speed;
    
    //the debris is only affected by the force once
    private boolean forceApplied = false;
    
    //random number generator
    private Random rand = new Random();
    
    //the image of the debris
    private Quad2d image;
    
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
        
        //set the bounding box
        boundingBox = new BoundingRect(pos,
                new Vector2d(sideLength, sideLength));
    }
    
    //METHODS
    @Override
    public ArrayList<Entity> update() {

        //work through the collisions
        for (CollisionData data : collisions) {
            
            //collision with the force
            if (data.getCollideWith() == EntityType.FORCE &&
                !forceApplied) {
                
                //calculate the direction and speed
                double direction = data.getPos().angleBetween(pos);
                
                //add some noise to the angle
                direction += ((Math.PI / 3.0) * rand.nextFloat()) - (Math.PI / 6.0);
                
                speed = new Vector2d((float) -(0.04*Math.cos(direction)),
                    (float) (0.04*Math.sin(direction)));
                
                forceApplied = true;
            }
            //FIXME: ignore gravity for now
//			//apply gravity
//			if (data.getCollideWith() == EntityType.GRAVITY) {
//				
//				if (speed.getX() > data.getSpeed().getX()) {
//					
//					speed.setX(speed.getX() - data.getSpeed().getX() * 0.01f);
//				}
//				if (speed.getY() > data.getSpeed().getY()) {
//					
//					speed.setY(speed.getY() - Math.abs(data.getSpeed().getY()*0.005f));
//				}
//			}
			//FIXME: ignore entity for now
//            if (data.getCollideWith() == EntityType.DEBRIS) {
//                
//                speed = new Vector2d(data.getSpeed());
//            }
        }
        //clear the collisions
        collisions.clear();
        
        //TODO: vector addition!
		//move the debris
        pos.setX(pos.getX() + speed.getX());
        pos.setY(pos.getY() + speed.getY());
        
        //translate the bounding box
        boundingBox.setPos(pos);
        
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
    public CollisionData.EntityType getType() {
        
        return EntityType.DEBRIS;
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

}
