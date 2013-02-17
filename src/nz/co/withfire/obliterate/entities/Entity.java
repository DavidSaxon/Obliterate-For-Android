/*****************************************************************************\
| An entity in obliterate, provides methods collision checking, updating, and |
| drawing. Not all of these methods need to be used.                          |
|                                                                             |
| @author David Saxon                                                         |
\*****************************************************************************/
package nz.co.withfire.obliterate.entities;

import java.util.ArrayList;

import nz.co.withfire.obliterate.utilities.Vector2d;

public abstract class Entity {

    //VARIABLES
    //if the entity should be removed; default false
    protected boolean remove = false;
    
    //METHODS
    
    /**Updates the entity
    @return an array list of entities created by this entity*/
    public ArrayList<Entity> update() {
        
        //do nothing
        
        return null;
    }
    
    /**Draws the entity to the GL surface
    @param viewMatrix the view matrix
    @param projectionMatrix the projection matrix*/
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
        //do nothing
    }

    /**@return the position of the entity*/
    public Vector2d getPos() {
        
        return new Vector2d(0.0f, 0.0f);
    }
    
    /**@return if this entity should be removed*/
    public boolean shouldRemove() {
        
        return remove;
    }
}
