/*****************************************************************\
| An interface for anything that is drawable by the GLES renderer |
|																  |
| @author David Saxon 											  |
\*****************************************************************/
package nz.co.withfire.obliterate.graphics.drawable;

public interface Drawable {

	//METHODS
	/*Draws the drawable*/
	public void draw(float[] mvpMatrix);
}