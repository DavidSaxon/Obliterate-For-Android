/********************************************\
| A bounding box in the shape of a rectangle |
|                                            |
| @author David Saxon                        |
\********************************************/
package nz.co.withfire.obliterate.bounding_box;

import nz.co.withfire.obliterate.utilities.Vector2d;

public class BoundingRect implements BoundingBox {

    //VARIABLES
    //the position of the rectangle
    private Vector2d pos;
    //the dimensions of the rectangle
    private Vector2d dim;
    
    //CONSTRUCTOR
    /**Creates a new bounding rectangle
    @param pos the position of the rectangle
    @param dim the dimensions of the rectangle*/
    public BoundingRect(Vector2d pos, Vector2d dim) {
        
        this.pos = new Vector2d(pos);
        this.dim = new Vector2d(dim);
    }
    
    //METHODS
    @Override
    public void shift(Vector2d dis) {
        
        //TODO:
    }

    @Override
    public void scale(float s) {

        //TODO:
    }

    /**@return the position of the rectangle*/
    public final Vector2d getPos() {
        
        return pos;
    }
    
    /**@return the dimensions of rectangle*/
    public final Vector2d getDim() {
        
        return dim;
    }
}
