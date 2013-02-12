package nz.co.withfire.obliterate.entities.main;

import java.util.ArrayList;

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
	
	//TESTING
	//count till obliterate
	private int countDown = 100;
	
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

	@Override
	public void collisionCheck(Entity other) {
		
		//TODO:
	}

	@Override
	public ArrayList<Entity> update() {
		
	    //count down to obliterate
		if (countDown == 0) {
		    
		    remove = true;
		}
		else {
		    
		    --countDown;
		}
		
		return null;
	}

	@Override
	public void draw(float[] mvpMatrix) {
	    
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, 0, 0, 1);
        
        Matrix.multiplyMM(this.mvpMatrix, 0, tMatrix, 0, mvpMatrix, 0);
		
		image.draw(this.mvpMatrix);
	}
}