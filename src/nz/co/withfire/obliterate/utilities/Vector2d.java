/************************\
| A 2 dimensional vector |
|                        |
| @author David Saxon    |
\************************/

package nz.co.withfire.obliterate.utilities;

public class Vector2d {

    //VARIABLES
    private float x = 0.0f;
    private float y = 0.0f;
    
    //CONSTRUCTORS
    /**Creates a zero vector*/
    public Vector2d() {
        
        //do nothing
    }
    
    /**Creates a vector with the given points
    @param x the x position
    @param y the y position*/
    public Vector2d(float x, float y) {
        
        this.x = x;
        this.y = y;
    }
    
    /**Creates a new vector from deep copying the given vector
    @param other the other vector to copy from*/
    public Vector2d(Vector2d other) {
        
        this.x = other.x;
        this.y = other.y;
    }
    
    //PUBLIC METHODS
    /**@return the x value of the vector*/
    public float getX() {
        
        return x;
    }
    
    /**@return the y value of the vector*/
    public float getY() {
        
        return y;
    }
    
    /**Set the x value
    @param x the new x value*/
    public void setX(float x) {
        
        this.x = x;
    }
    
    /**Set the y value
    @param y the new y value*/
    public void setY(float y) {
        
        this.y = y;
    }
}
