/*****************************************************************************\
| An entity in obliterate, provides methods collision checking, updating, and |
| drawing. Not all of these methods need to be used.						  |
|																			  |
| @author David Saxon													      |
\*****************************************************************************/
package nz.co.withfire.obliterate.entities;

public interface Entity {

	//VARIABLES
	//default false
	public final boolean collisionType = false;
	
	//METHODS
	/**Checks if this entity is colliding with the other entity.
	If the entities are colliding this entity will take the proper responses.
	@param other the other entity to check collision with*/
	public void collisionCheck(Entity other);
	
	/**Updates the entity*/
	public void update();
	
	/**Draws the entity to the GL surface*/
	public void draw();
}
