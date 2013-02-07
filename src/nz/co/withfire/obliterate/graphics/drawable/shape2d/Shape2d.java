/****************************************************************************\
| An interface that represents any 2d shape that is drawable by the renderer |
|																			 |
| @author David Saxon 														 |
\****************************************************************************/

package nz.co.withfire.obliterate.graphics.drawable.shape2d;

import nz.co.withfire.obliterate.graphics.drawable.Drawable;

public interface Shape2d extends Drawable {
	
	//VARIABLES
    //number of coordinates per vertex
    static final int COORDS_PER_VERTEX = 3;
}