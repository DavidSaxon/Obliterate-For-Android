/******************************************************\
| The obliterate logo displayed when the app launches. |
|													   |
| @author David Saxon								   |
\******************************************************/
package nz.co.withfire.obliterate.entities.start_up;

import java.util.ArrayList;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;

public class Logo implements Entity {
	
	//VARIABLES
	//the quad displaying the logo
	private Quad2d logo;
	
	//CONSTRUCTOR
	public Logo() {
		
        float quadCoord[] = {	-1.5f,  0.8f, 0.0f,
                				-1.5f, -0.6f, 0.0f,
                				 1.5f, -0.6f, 0.0f,
                				 1.5f,  0.8f, 0.0f};
        float quadColour[] = {	0.8f, 0.8f, 0.8f, 1.0f,
        						0.8f, 0.8f, 0.8f, 1.0f,
        						0.8f, 0.8f, 0.8f, 1.0f,
        						0.8f, 0.8f, 0.8f, 1.0f};
        logo = new Quad2d(quadCoord, quadColour);
	}
	
	//METHODS
	@Override
	public void collisionCheck(Entity other) {
	
		//do nothing not a collision type
	}

	@Override
	public ArrayList<Entity> update() {
		
		return null;
	}

	@Override
	public void draw(float[] mvpMatrix) {
		
		logo.draw(mvpMatrix);
	}
}
