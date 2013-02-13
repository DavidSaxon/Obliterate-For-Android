package nz.co.withfire.obliterate.entities.main;

import java.util.ArrayList;
import java.util.Random;

import android.opengl.Matrix;
import android.util.Log;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;

public class ObliterateImage extends Entity {
	
	//VARIABLES
    //the width and height of the image
    private float width;
    private float height;
	//the image to obliterate
	private Quad2d image;
	
	//is true if the image should be obliterated
	private boolean obliterate = false;
	
	//TESTING
	private Random rand = new Random();
	
	//Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
	
	//CONSTRUCTOR
	public ObliterateImage() {
		
	    //TODO: get the width and height from the image
	    width = 1.0f;
	    height = 1.0f;
	    
	    //calculate half the width and the height
	    float hw = width/2.0f;
	    float hh = height/2.0f;
	    
		//TODO: texture image
		float[] imageCoord = {
			-hw,  hh, 0.0f,
			-hw, -hh, 0.0f,
			 hw, -hh, 0.0f,
			 hh,  hh, 0.0f
		};
		float[] imageColour = {
			0.6f, 0.05f, 0.15f, 1.0f,
			0.6f, 0.05f, 0.15f, 1.0f,
			0.6f, 0.05f, 0.15f, 1.0f,
			0.6f, 0.05f, 0.15f, 1.0f
		};
		
		image = new Quad2d(imageCoord, imageColour);
	}

    //PUBLIC METHODS
	@Override
	public void collisionCheck(Entity other) {
		
		//TODO:
	}

	@Override
	public ArrayList<Entity> update() {
		
	    //obliterate the image
		if (obliterate) {
		    
		    remove = true;
		    return obliterate();
		}
	
	    return null;
	}

	@Override
	public void draw(float[] viewMatrix, float[] projectionMatrix) {
	    
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, 0, 0, -0.01f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
		
		image.draw(mvpMatrix);
	}
	
	/**Sets the image to be obliterate*/
	public void setToObliterate() {
	    
	    obliterate = true;
	}
	
	//PRIVATE METHODS
	/**Obliterates the image
	@return the list of entities this image has obliterated into*/
	private ArrayList<Entity> obliterate() {
	    
	    //list of new entities
	    ArrayList<Entity> remains = new ArrayList<Entity>();
	    
       //calculate the top left corner of the image
        float x = -(width/2.0f);
        float y = -(height/2.0f);
	    
        //TODO: find a proper width for the debris
	    for (float i = 0.0f; i < height; i += 0.1f) {
	        for (float j = 0.0f; j < width; j += 0.1f) {
	            
	            //TODO: real physics
	            float xSpeed = (rand.nextInt(200)-100)*0.0001f;
	            float ySpeed = (rand.nextInt(200)-100)*0.0001f;
	            
	            //TODO: add half the calculated width
	            remains.add(new Debris(x+j+0.05f, y+i+0.05f, 0.1f, xSpeed, ySpeed));
	        }
	    }
	    
	    return remains;
	}
}