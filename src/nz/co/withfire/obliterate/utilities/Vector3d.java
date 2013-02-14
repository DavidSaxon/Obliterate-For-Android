/************************\
| A 3 dimensional vector | 
|                        |
| @author David Saxon    |
\************************/

package nz.co.withfire.obliterate.utilities;

public class Vector3d {
    
    //VARIABLES
    private float x = 0.0f;
    private float y = 0.0f;
    private float z = 0.0f;
    
    //CONSTRUCTOR
    /**Creates a zero vector*/
    public Vector3d() {
        
        //do nothing
    }
    
    /**Creates a vector with the given points
    @param x the x value
    @param y the y value
    @param z the z value*/
    public Vector3d(float x, float y, float z) {
        
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**Creates a new vector from deep copying the given vector
    @param other the other vector to copy from*/
    public Vector3d(Vector3d other) {
        
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
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
    
    /**@return the z value of the vector*/
    public float getZ() {
        
        return z;
    }
    
    /**Set the x value
    @param the new x value*/
    public void setX(float x) {
        
        this.x = x;
    }
    
    /**Set the y value
    @param the new y value*/
    public void setY(float y) {
        
        this.y = y;
    }
    
    /**Set the z value
    @param the new z value*/
    public void setZ(float z) {
        
        this.z = z;
    }
}
