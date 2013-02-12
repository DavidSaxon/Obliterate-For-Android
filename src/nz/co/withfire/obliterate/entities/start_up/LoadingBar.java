/*********************\
| Just a loading bar. |
|					  |
| @author David Saxon |
\*********************/
package nz.co.withfire.obliterate.entities.start_up;

import java.util.ArrayList;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;

public class LoadingBar implements Entity {

	//VARIABLES
	//the progress of the loading bar
	private float progress = 0.0f;
	//the loading bar shape
	Quad2d bar;
	
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
	
	//CONSTRUCTOR
	public LoadingBar() {
		
		//create the coords of the quad
        float quadCoord[] = {-1.5f, -.8f,   0.0f,
                			 -1.5f, -0.75f, 0.0f,
                			  1.5f, -0.75f, 0.0f,
                              1.5f, -0.8f,  0.0f};
        float quadColour[] = {	1.0f, 1.0f, 1.0f, 1.0f,
        						1.0f, 1.0f, 1.0f, 1.0f,
        						1.0f, 1.0f, 1.0f, 1.0f,
        						1.0f, 1.0f, 1.0f, 1.0f};
        bar = new Quad2d(quadCoord, quadColour);
	}
	
	@Override
	public void collisionCheck(Entity other) {
		
		//do nothing
	}

	@Override
	public ArrayList<Entity> update() {
		
		//stretch the bar across the screen
		bar.setPosition(0, 1.5f-(2.98f * progress), -0.8f, 0.0f);
		bar.setPosition(1, 1.5f-(2.98f*  progress), -0.75f, 0.0f);
		
		//fade the bar into black
		float col = 1.0f - (1.0f * progress);
		bar.setColour(0, col, col, col, col);
		bar.setColour(1, col, col, col, col);
		
		return null;
	}

	@Override
	public void draw(float[] mvpMatrix) {
	    
	    //shift into visible range
	    Matrix.setIdentityM(tMatrix, 0);
	    Matrix.translateM(tMatrix, 0, 0, 0, 1);
	    
	    Matrix.multiplyMM(this.mvpMatrix, 0, tMatrix, 0, mvpMatrix, 0);
		
		bar.draw(this.mvpMatrix);
	}
	
	/**Updates the progress of the loading bar
	@param progress the new loading bar progress*/
	public void updateProgress(float progress) {
		
		this.progress = progress;
	}
}
