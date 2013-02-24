/****************************************************************\
| Used to see if there is a collision with a button to where the |
| user has pressed                                               |
|                                                                |
| @author David Saxon                                            |
\****************************************************************/
package nz.co.withfire.obliterate.entities.menu;

import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.bounding.BoundingCircle;
import nz.co.withfire.obliterate.utilities.Vector2d;

public class TouchPoint extends CollisionType {

    //VARIABLES
    
    //CONSTRUCTOR
    /**Creates a new touch point
    @param the screen
    @param pos the position of the touch point*/
    public TouchPoint(Vector2d GLdim, Vector2d pos) {
        
        //work out the radius of the touch point
        float radius = GLdim.getX() / 18.0f;
        
        //set the bounding box of the touch position
        boundingBox = new BoundingCircle(pos, radius);
        
        immovable = true;
    }
    
}
