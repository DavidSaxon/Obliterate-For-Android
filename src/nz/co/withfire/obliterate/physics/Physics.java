/******************************************\
| Simple physics that processes collisions |
|                                          |
| @author David Saxon                      |
\******************************************/
package nz.co.withfire.obliterate.physics;

import java.util.ArrayList;
import java.util.Random;

import nz.co.withfire.obliterate.entities.main.Debris;
import nz.co.withfire.obliterate.entities.main.Force;
import nz.co.withfire.obliterate.physics.bounding.BoundingArea;
import nz.co.withfire.obliterate.physics.bounding.BoundingCircle;
import nz.co.withfire.obliterate.physics.bounding.BoundingRect;
import nz.co.withfire.obliterate.utilities.Vector2d;
import android.util.Log;

public class Physics {

    //VARIABLES
    //the dimensions of the space to calculate physics for
    private Vector2d dim;
    
    //the list of all the entities that are collision type
    private ArrayList<CollisionType> entities;
    //A 2d list holding references to entities, organises entities by space
    //similar to a quadtree
    private ArrayList<ArrayList<CollisionType>> divisionSpaceMap;
    //the width and height of the division space map
    private Vector2d dsmDim;
    
    //a random number generator
    private Random rand = new Random();
	
    
    //CONSTRUCTOR
    /**Creates a new physics controller
    @param viewPortDim the dimensions of the view port in openGL coords*/
    public Physics(Vector2d viewPortDim) {
        
        //initialise the entities list
        entities = new ArrayList<CollisionType>();
		
        //calculate the physics dimensions
        dim = new Vector2d(Math.abs(viewPortDim.getX() * 2.0f),
            Math.abs(viewPortDim.getY() * 2.0f));
        
        dsmDim = new Vector2d((float) Math.ceil(dim.getX() * 20.0),
            (float) Math.ceil(dim.getY() * 20.0));
        
        //Initialise the division space map
        divisionSpaceMap = new ArrayList<ArrayList<CollisionType>>();
		for (int i  = 0; i < dsmDim.getX() * dsmDim.getY(); ++i) {
			
		    divisionSpaceMap.add(new ArrayList<CollisionType>());
		}
    }
    
    //PUBLIC METHODS
    /**Checks for collision between entities and pass
    the entity the collision info*/
    public void collisionCheck() {
        
		//clear the division space map
		for (int i = 0; i < dsmDim.getX() * dsmDim.getY(); ++i) {
			
		    divisionSpaceMap.get(i).clear();
		}
		
		//build the division space map
		for (CollisionType c : entities) {
		    
		  //find the locations of the entities in the division space
            Vector2d lowerBound = new Vector2d(
                (int) ((c.getPos().getX() - (c.getDim().getX() / 2.0f)
                + dim.getX()) * 5.0f),
                (int) ((c.getPos().getY() - (c.getDim().getY() / 2.0f) +
                dim.getY()) * 5.0f));
		    
            Vector2d upperBound = new Vector2d(
                (int) ((c.getPos().getX() + (c.getDim().getX() / 2.0f)
                + dim.getX()) * 5.0f),
                (int) ((c.getPos().getY() + (c.getDim().getY() / 2.0f) +
                dim.getY()) * 5.0f));
			
			//TODO: optimise
			for (int y = (int) lowerBound.getY();
			        y <= (int) upperBound.getY(); ++y) {
			    for (int x = (int) lowerBound.getY();
		            x <= (int) upperBound.getY(); ++x) {
			        
			        if (x >= 0 && x < dsmDim.getX() &&
		                y >= 0 && y < dsmDim.getY()) {
			        
			            divisionSpaceMap.get(
		                    (int) (x + (y * dsmDim.getX()))).add(c);
			        }
			    }
			}
		}
		
		//check collision
		for (ArrayList<CollisionType> a : divisionSpaceMap) {
			
		    
			for (CollisionType c1 : a) {
			    
			    //the new speed of the entity
			    Vector2d newSpeed = c1.getSpeed();
			    
				for (CollisionType c2 : a) {
					    
					if (c1 != c2 && collision(c1, c2)) {
					    
					    if (c1 instanceof Debris && c2 instanceof Force &&
				            !((Debris) c1).getForceApplied()) {
					        
				            //calculate the direction
			                double direction =
		                        c2.getPos().angleBetween(c1.getPos());
			                
			                //add some noise to the angle
			                direction += ((Math.PI / 3.0) *
		                        rand.nextFloat()) - (Math.PI / 6.0);
			                
			                newSpeed.add(
		                        (float) -(c2.getSpeed().getX() *
                                Math.cos(direction)),
		                        (float) (c2.getSpeed().getX() *
                                Math.sin(direction)));
			                
			                //set that the fore has been applied
			                ((Debris) c1).setForceApplied(true);
					    }
					}
//					if (c1 instanceof Debris && c2 instanceof Debris) {
//					    
////					    if (c1.getSpeed().getX() <= c2.getSpeed().getX()) {
////					        
////				            newSpeed.setX(newSpeed.getX() +
////		                        (c2.getSpeed().getX() / 2.0f));
////					    }
////					    else {
////					        
//					        newSpeed.setX(newSpeed.getX() / 2.0f);
////					    }
//					    
//					    //newSpeed = new Vector2d(-c2.getSpeed().getX(), -c2.getSpeed().getX()); 
//					}
				}
				
				//set the new speed of the entity
				c1.setSpeed(newSpeed);
			}
		}
    }
    
    /**Adds a new collision type entity to the physics controller
    @param e the new entity*/
    public void addEntity(CollisionType e) {
        
        entities.add(e);
    }
    
    /**Removes a collision type entity from the physics controller
    @param e the entity to remove*/
    public void removeEntity(CollisionType e) {
        
        entities.remove(e);
    }
    
    
    //PRIVATE METHODS
    /**Checks if the two objects are colliding
    @param first the first object
    @param second the second object*/
    private boolean collision(CollisionType first, CollisionType second) {
        
        //get the bounding areas
        BoundingArea firstBounding = first.getBoundingArea();
        BoundingArea secondBounding = second.getBoundingArea();
        
        //Both are rectangles
        if (firstBounding instanceof BoundingRect &&
            secondBounding instanceof BoundingRect) {
            
            return rectangleIntersectRectangle(
                (BoundingRect) firstBounding,
                (BoundingRect) secondBounding);
        }
        //rectangle and circle
        else if (firstBounding instanceof BoundingRect &&
            secondBounding instanceof BoundingCircle) {
            
            return rectangleIntersectCircle(
                (BoundingRect) firstBounding,
                (BoundingCircle) secondBounding);
        }
        //Circle and rectangle
        else if (firstBounding instanceof BoundingCircle &&
            secondBounding instanceof BoundingRect) {
            
            return rectangleIntersectCircle(
                (BoundingRect) secondBounding,
                (BoundingCircle) firstBounding);
        }
        
        
        return false;
    }
    
    /**Checks if two rectangles are intersecting
    @param r1 the first rectangle
    @param r2 the second rectangle*/
    private boolean rectangleIntersectRectangle(
        BoundingRect r1, BoundingRect r2) {
        
        //short hand
        float r1x1 = r1.getPos().getX() - (r1.getDim().getX() / 2.0f);
        float r1x2 = r1.getPos().getX() + (r1.getDim().getX() / 2.0f);
        float r1y1 = r1.getPos().getY() - (r1.getDim().getY() / 2.0f);
        float r1y2 = r1.getPos().getY() + (r1.getDim().getY() / 2.0f);
        float r2x1 = r2.getPos().getX() - (r2.getDim().getX() / 2.0f);
        float r2x2 = r2.getPos().getX() + (r2.getDim().getX() / 2.0f);
        float r2y1 = r2.getPos().getY() - (r2.getDim().getY() / 2.0f);
        float r2y2 = r2.getPos().getY() + (r2.getDim().getY() / 2.0f);
        
        //check if colliding
        return (r1x1 < r2x2) && (r1x2 > r2x1) &&
               (r1y1 < r2y2) && (r1y2 > r2y1);
    }

    /**Checks if a rectangle and a circle are intersecting
    @param r the rectangle
    @param c the circle*/
    private boolean rectangleIntersectCircle(BoundingRect r, BoundingCircle c) {
        
        //short hand
        float cx = c.getPos().getX();
        float cy = c.getPos().getY();
        float cr = c.getRadius();
        float rx = r.getPos().getX();
        float ry = r.getPos().getY();
        float rhw = r.getDim().getX() / 2.0f;
        float rhh = r.getDim().getY() / 2.0f;
        
        //check for collision
        if (Math.abs(cx - rx) > (rhw + cr)) {
            
            return false;
        }
        if (Math.abs(cy - ry) > (rhh + cr)) {
            
            return false;
        }
        
        if (Math.abs(cx - rx) <= rhw) {
            
            return true;
        }
        if (Math.abs(cy - ry) <= rhh) {
            
            return true;
        }
        
        return (Math.pow(Math.abs(cx - rx) - rhw, 2.0) +
                Math.pow(Math.abs(cy - ry) - rhh, 2.0)) <=
                Math.pow(cr, 2.0);
    }
}
