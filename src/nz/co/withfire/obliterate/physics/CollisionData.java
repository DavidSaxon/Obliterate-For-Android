/*********************************\
| Contains data about a collision |
|                                 |
| @author David Saxon             |
\*********************************/

package nz.co.withfire.obliterate.physics;

import nz.co.withfire.obliterate.utilities.Vector2d;

public class CollisionData {

    //ENUMERATOR
    public enum EntityType {
        FORCE,
        DEBRIS
    }
    
    //VARIABLES
    //the type of entity collided with
    private EntityType collideWith;
    //the position of the object collided with
    private Vector2d pos;
    //the speed of the object collided with
    private Vector2d speed;
    
    //CONSTRUCTOR
    /**Creates new collision data
    @param collideWith the entity collided with
    @param pos the position of the entity collided with
    @param speed the speed of the entity collided with*/
    public CollisionData(EntityType collideWith,
            Vector2d pos, Vector2d speed) {
        
        this.collideWith = collideWith;
        this.pos = new Vector2d(pos);
        this.speed = new Vector2d(speed);
    }
    
    //METHODS
    /**@return the object being collided with*/
    public EntityType getCollideWith() {
        
        return collideWith;
    }
    
    /**@return the position of the entity collided with*/
    public Vector2d getPos() {
        
        return pos;
    }
    
    /**@return the speed of the entity collided with*/
    public Vector2d getSpeed() {
        
        return speed;
    }
}
