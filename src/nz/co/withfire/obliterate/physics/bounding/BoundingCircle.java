package nz.co.withfire.obliterate.physics.bounding;

import android.util.Log;
import nz.co.withfire.obliterate.utilities.Vector2d;

public class BoundingCircle implements BoundingArea {

    //VARIABLES
    //the centre position of the circle
    private Vector2d pos;
    //the radius of the circle
    private float radius;
    //the original radius of the circle
    private float orgRadius;
    
    //CONSTRUCTOR
    /**Creates a new bounding circle
    @param pos the centre position of the circle
    @param radius the radius of the circle*/
    public BoundingCircle(Vector2d pos, float radius) {
        
        this.pos = new Vector2d(pos);
        this.radius = radius;
        orgRadius = radius;
    }
    
    //METHODS
    @Override
    public Vector2d getPos() {
        
        return pos;
    }
    
    /**@return the radius of the circle*/
    public float getRadius() {
        
        return radius;
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
        
        radius = orgRadius * s;
    }
}
