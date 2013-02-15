/************************\
| A 4 dimensional vector |
|                        |
| @david Saxon           |
\************************/
package nz.co.withfire.obliterate.utilities;

public class Vector4d {

    //VARIABLES
    private float x = 0.0f;
    private float y = 0.0f;
    private float z = 0.0f;
    private float w = 0.0f;
    
    //CONSTRUCTOR
    /**Create a new zero vector*/
    public Vector4d() {
        
        //do nothing
    }
    
    /**Create a vector from the vector from the given points
    @param x the x value
    @param y the y value
    @param z the z value
    @param w the w value*/
    public Vector4d(float x, float y, float z, float w) {
        
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    /**Creates a new vector from deep copying the other vector
    @param other the other vector to copy from*/
    public Vector4d(Vector4d other) {
        
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
    }
    
    //PUBLIC METHODS
    /**@return the x value*/
    public float getX() {
        
        return x;
    }
    
    /**@return the y value*/
    public float getY() {
        
        return y;
    }
    
    /**@return the z value*/
    public float getZ() {
        
        return z;
    }
    
    /**@return the w value*/
    public float getW() {
        
        return w;
    }
    
    /**Sets the x value
    @param x the new x value*/
    public void setX(float x) {
        
        this.x = x;
    }
    
    /**Sets the y value
    @param y the new value*/
    public void setY(float y) {
        
        this.y = y;
    }
    
    /**Sets the z value
    @param z the new z value*/
    public void setZ(float z) {
        
        this.z = z;
    }
    
    /**Sets the w value
    @param w the new w value*/
    public void setW(float w) {
        
        this.w = w;
    }
}
