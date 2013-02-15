/*****************************************************************************\
| An entity in obliterate, provides methods collision checking, updating, and |
| drawing. Not all of these methods need to be used.                          |
|                                                                             |
| @author David Saxon                                                         |
\*****************************************************************************/
package nz.co.withfire.obliterate.entities;

import java.util.ArrayList;

import nz.co.withfire.obliterate.bounding_box.BoundingBox;

public abstract class Entity {

    //VARIABLES
    //if the entity is a physics entity; default false
    protected boolean collisionType = false;
    //if the entity should be removed; default false
    protected boolean remove = false;
    
    
    //METHODS
    /**Checks if this entity is colliding with the other entity.
    If the entities are colliding this entity will take the proper responses.
    @param other the other entity to check collision with*/
    public void collisionCheck(Entity other) {
        
        //do nothing
    }
    
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
    
    /**@return the bounding box of the entity
    (returns null if not a collision type entity*/
    public BoundingBox getBoundingBox() {
        
        return null;
    }
    
    /**@return if this entity is a collision type*/
    public boolean isCollisionType() {
        
        return collisionType;
    }

    /**@return if this entity should be removed*/
    public boolean shouldRemove() {
        
        return remove;
    }
}
