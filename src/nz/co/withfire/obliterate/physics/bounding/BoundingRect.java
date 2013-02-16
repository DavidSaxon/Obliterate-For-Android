/*****************************************************\
| A bounding area that is in the shape of a rectangle |
|                                                     |
| @author David Saxon                                 |
\*****************************************************/
package nz.co.withfire.obliterate.physics.bounding;

import nz.co.withfire.obliterate.utilities.Vector2d;

public class BoundingRect implements BoundingArea {

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
    public Vector2d getPos() {
        
        return pos;
    }
    
    /**@return the dimensions of the rectangle*/
    public Vector2d getDim() {
        
        return dim;
    }

    @Override
    public void setPos(Vector2d pos) {
        
        this.pos = new Vector2d(pos);
    }
    
    @Override
    public void translate(Vector2d dis) {
        
        //TODO: vector addition
        pos = new Vector2d(pos.getX() + dis.getX(),
            pos.getY() + dis.getY());
    }

    @Override
    public void scale(float s) {
        
        //TODO:
    }

}
