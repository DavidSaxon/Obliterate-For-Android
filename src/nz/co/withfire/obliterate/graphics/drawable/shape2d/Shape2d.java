/****************************************************************************\
| An interface that represents any 2d shape that is drawable by the renderer |
|                                                                            |
| @author David Saxon                                                        |
\****************************************************************************/

package nz.co.withfire.obliterate.graphics.drawable.shape2d;

import nz.co.withfire.obliterate.graphics.drawable.Drawable;

public interface Shape2d extends Drawable {
    
    //VARIABLES
    //number of coordinates per vertex
    static final int COORDS_PER_VERTEX = 3;
    
    //METHODS
    /**Sets the position of one of the shapes vertex
    @param v the vertex to change
    @param x the new x coordinate
    @param y the new y coordinate
    @param z the new z coordinate*/
    public void setPosition(int v, float x, float y, float z);
    
    /**Sets the colour of one of the shape's vertex
    @param v the vertex to change
    @param r the new red value
    @param g the new green value
    @param b the new blue value
    @param a the new alpha value*/
    public void setColour(int v, float r, float g, float b, float a);
    
    /**Sets colour of all vertices of the quad
    @param r the new red value
    @param g the new green value
    @param b the new blue value
    @param a the new alpha value*/
    public void setColour(float r, float g, float b, float a);
}