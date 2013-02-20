/**********************************************\
| All entities that are collidable extend this |
|                                              |
| @author David Saxon                          |
\**********************************************/
package nz.co.withfire.obliterate.physics;

import java.util.ArrayList;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.physics.CollisionData.EntityType;
import nz.co.withfire.obliterate.physics.bounding.BoundingArea;
import nz.co.withfire.obliterate.utilities.Vector2d;

public abstract class CollisionType extends Entity {

    //VARIABLES
    //the bounding area
    protected BoundingArea boundingBox;
    //a list of the collision data
    protected ArrayList<CollisionData> collisions =
        new ArrayList<CollisionData>();
    
    //METHODS
    /**@return the type of entity this is*/
    public EntityType getType() {
        
        return null;
    }
    
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
    
    /**Pass collision data to the entity*/
    public void passCollisionData(CollisionData data) {
        
        collisions.add(data);
    }
}
