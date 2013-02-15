/****************************************************************************\
| An interface that represents any 2d shape that is drawable by the renderer |
|                                                                            |
| @author David Saxon                                                        |
\****************************************************************************/

package nz.co.withfire.obliterate.graphics.drawable.shape2d;

import nz.co.withfire.obliterate.graphics.drawable.Drawable;
import nz.co.withfire.obliterate.utilities.*;

public interface Shape2d extends Drawable {
       
    //METHODS
    /**Sets the position of one of the shapes vertex
    @param v the vertex to change
    @param pos the new position*/
    public void setPosition(int v, Vector3d pos);
    
    /**Sets the colour of one of the shape's vertex
    @param v the vertex to change
    @param c the new colour*/
    public void setColour(int v, Vector4d c);
    
    /**Sets colour of all vertices of the quad
    @param c the new colour*/
    public void setColour(Vector4d c);
}
