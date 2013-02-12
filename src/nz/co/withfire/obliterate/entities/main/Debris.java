package nz.co.withfire.obliterate.entities.main;

import java.util.ArrayList;

import android.opengl.Matrix;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;

public class Debris implements Entity {

	//VARIABLES
	//the x and y position of the debris
	private float xPos;
	private float yPos;
	//the width and height of the debris
	private float stride;
	//the x and y move speeds of the debris
	private float xSpeed;
	private float ySpeed;
	//the image of the debris
	private Quad2d image;
	
	//Matrix
	//the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
	
	//CONSTRUCTOR
	/**Creates a new debris object
	@param xPos the x position of the debris
	@param yPos the y position of the debris
	@param stride the stride of the debris
	@param xSpeed the x speed of the debris
	@param ySpeed the y speed of the debris*/
	public Debris(float xPos, float yPos, float stride,
        float xSpeed, float ySpeed) {
		
		//initialise the variables
		this.xPos = xPos;
		this.yPos = yPos;
		this.stride = stride;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		
		float d = stride/2.0f;
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
	}
	
	//METHODS
	@Override
	public void collisionCheck(Entity other) {
	    
		//TODO:
	}

	@Override
	public ArrayList<Entity> update() {

	    //move the debris
	    xPos += xSpeed;
	    yPos += ySpeed;
	    
		return null;
	}

	@Override
	public void draw(float[] mvpMatrix) {
		
        //shift into visible range and move
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, xPos, yPos, 1);
        
        Matrix.multiplyMM(this.mvpMatrix, 0, tMatrix, 0, mvpMatrix, 0);
		
	    image.draw(this.mvpMatrix);
	}
}
