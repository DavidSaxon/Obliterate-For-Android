/************************************************************************\
| An interface for any type of bounding area used for collision checking |
|                                                                        |
| @author David Saxon                                                    |
\************************************************************************/
package nz.co.withfire.obliterate.utilities.bounding;

import nz.co.withfire.obliterate.utilities.Vector2d;

public interface BoundingArea {

    //METHODS
    /**@return the centre position of the bounding area*/
    public Vector2d getPos();
    
    /**Translates the bounding box
    @param dis the distance to translate*/
    public void translate(Vector2d dis);
    
    /**Scales the bounding box
    @param s the amount to scale by*/
    public void scale(float s);
}
