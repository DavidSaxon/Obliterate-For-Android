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
    /**Copies the values of the other vector to this vector
    @param other the other vector to copy from*/
    public void copy(final Vector2d other) {
        
        this.x = other.x;
        this.y = other.y;
    }
    
    /**@return the x value of the vector*/
    public float getX() {
        
        return x;
    }
    
    /**@return the y value of the vector*/
    public float getY() {
        
        return y;
    }
    
    /**Sets the new values of the vector
    @param x the new x value
    @param y the new y value*/
    public void set(float x, float y) {
        
        this.x  = x;
        this.y = y;
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
    
    /**Adds the 2 points to the vector
    @param x the x point to add
    @param y the y point to add*/
    public void add(float x, float y) {
        
        this.x += x;
        this.y += y;
    }
    
    /**Adds the other vector to this vector
    @pararm other the other vector to add*/
    public void add(Vector2d other) {
        
        this.x += other.x;
        this.y += other.y;
    }
    
    /**Multiplies the vector by the given scalar
    @param scalar the scalar to multiply by*/
    public void multiply(float scalar) {
        
        x *= scalar;
        y *= scalar;
    }
    
    /**Find the angle between this vector and the other
    along the x axis
    @param other the other vector*/
    public double angleBetween(Vector2d other) {
                
        return -1.0*(Math.atan2(y-other.y, x-other.x));
    }
}
