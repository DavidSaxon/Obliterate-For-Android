/******************************************************\
| The obliterate logo displayed when the app launches. |
|													   |
| @author David Saxon								   |
\******************************************************/
package nz.co.withfire.obliterate.entities.start_up;

import nz.co.withfire.obliterate.entities.Entity;

public class Logo implements Entity {
	
	@Override
	public void collisionCheck(Entity other) {
	
		//do nothing not a collision type
	}

	@Override
	public void update() {
		
		//TODO: add the quad to fade over the top
	}

	@Override
	public void draw() {
		
		//TODO: draw the logo
	}
}
