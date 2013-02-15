/******************************\
| Interface for bounding boxes |
|                              |
| @author David Saxon          |
\******************************/
package nz.co.withfire.obliterate.bounding_box;

import nz.co.withfire.obliterate.utilities.Vector2d;

public interface BoundingBox {

    //METHODS
    /**Shifts the bounding box
    @param dis the distance vector in which to shift the bounding box*/
    public void shift(Vector2d dis);
    
    /**Scales the bounding box
    @param s the scale amount*/
    public void scale(float s);
}
