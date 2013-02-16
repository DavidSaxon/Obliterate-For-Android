/************************************************************************\
| An interface for any type of bounding area used for collision checking |
|                                                                        |
| @author David Saxon                                                    |
\************************************************************************/
package nz.co.withfire.obliterate.physics.bounding;

import nz.co.withfire.obliterate.utilities.Vector2d;

public interface BoundingArea {

    //METHODS
    /**@return the centre position of the bounding area*/
    public Vector2d getPos();
    
    /**Sets the position of the bounding area
    @param pos the new position of the bounding area*/
    public void setPos(Vector2d pos);
    
    /**Translates the bounding area
    @param dis the distance to translate*/
    public void translate(Vector2d dis);
    
    /**Scales the bounding area
    @param s the amount to scale by*/
    public void scale(float s);
}
