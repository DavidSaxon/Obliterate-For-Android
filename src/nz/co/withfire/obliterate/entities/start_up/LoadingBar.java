/*********************\
| Just a loading bar. |
|					  |
| @author David Saxon |
\*********************/
package nz.co.withfire.obliterate.entities.start_up;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;

public class LoadingBar implements Entity {

	//VARIABLES
	//the progress of the loading bar
	private float progress = 0.0f;
	//the loading bar shape
	Quad2d bar;
	
	//CONSTRUCTOR
	public LoadingBar() {
		
		//create the coords of the quad
        float quadCoord[] = {-1.5f, -.8f,   0.0f,
                			 -1.5f, -0.75f, 0.0f,
                			  1.5f, -0.75f, 0.0f,
                              1.5f, -0.8f,  0.0f};
        float quadColour[] = {0.0f, 0.0f, 0.0f, 1.0f};
        bar = new Quad2d(quadCoord, quadColour);
	}
	
	@Override
	public void collisionCheck(Entity other) {
		
		//do nothing
	}

	@Override
	public void update() {
		
		//stretch the bar across the screen
		bar.setVertex(0, 1.5f-(3.0f*progress), -0.8f, 0.0f);
		bar.setVertex(1, 1.5f-(3.0f*progress), -0.75f, 0.0f);
		
		//fade the bar in
		bar.setColour(1.0f-progress, 1.0f-progress, 1.0f-progress, 1.0f);
	}

	@Override
	public void draw(float[] mvpMatrix) {
		
		bar.draw(mvpMatrix);
	}
	
	/**Updates the progress of the loading bar
	@param progress the new loading bar progress*/
	public void updateProgress(float progress) {
		
		this.progress = progress;
	}
}
