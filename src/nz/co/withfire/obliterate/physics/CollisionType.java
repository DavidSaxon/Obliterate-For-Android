/**********************************************\
| All entities that are collidable extend this |
|                                              |
| @author David Saxon                          |
\**********************************************/
package nz.co.withfire.obliterate.physics;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.physics.bounding.BoundingArea;
import nz.co.withfire.obliterate.utilities.Vector2d;

public abstract class CollisionType extends Entity {

    //VARIABLES
    //is true if this entity is immovable
    protected boolean immovable = false;
    //the speed of the entity
    protected Vector2d speed = new Vector2d();
    //the bounding area
    protected BoundingArea boundingBox;
    
    //METHODS
    /**@return the dimensions of this entity*/
    public Vector2d getDim() {
        
        return new Vector2d(0.0f, 0.0f);
    }
    
    /**@return the speed of the entity*/
    public Vector2d getSpeed() {
        
        return new Vector2d(0.0f, 0.0f);
    }
    
    /**@return the bounding area*/
    public BoundingArea getBoundingArea() {
        
        return boundingBox;
    }
    
    /**Set the speed of the entity
    @param speed the new speed*/
    public void setSpeed(Vector2d speed) {
        
        if (!immovable) {
            
            this.speed = new Vector2d(speed);
        }
    }
}
