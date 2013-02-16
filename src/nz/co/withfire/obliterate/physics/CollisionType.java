/**********************************************\
| All entities that are collidable extend this |
|                                              |
| @author David Saxon                          |
\**********************************************/
package nz.co.withfire.obliterate.physics;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.utilities.bounding.BoundingArea;

public abstract class CollisionType extends Entity {

    //VARIABLES
    //the bounding area
    private BoundingArea boundingBox;
    
    //METHODS
    /**@return the bounding area*/
    public BoundingArea getBoundingArea() {
        
        return boundingBox;
    }
}
