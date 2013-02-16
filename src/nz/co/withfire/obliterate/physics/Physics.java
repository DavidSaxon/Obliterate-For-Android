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
import nz.co.withfire.obliterate.physics.bounding.BoundingRect;
import nz.co.withfire.obliterate.utilities.Vector2d;

import android.util.Log;

public class Physics {

    //VARIABLES
    //the list of all the entities that are collision type
    //TODO: some form of quadtree
    private ArrayList<CollisionType> entities;
    //TODO: REMOVE ME
    private Random rand = new Random();
    
    //CONSTRUCTOR
    /**Creates a new physics controller*/
    public Physics() {
        
        //intialise the entities list
        entities = new ArrayList<CollisionType>();
    }
    
    //PUBLIC METHODS
    /**Checks for collision between entities and pass
    the entity the collision info*/
    public void collisionCheck() {
        
        //iterate over the list and check collisions for each one
        for (CollisionType c1 : entities) {           
            for (CollisionType c2 : entities) {
                
                if (c1 != c2 && collision(c1, c2)) {
                    
                    //TODO: pass on collision information
                    
                    //TODO: REMOVE
                    if (c1 instanceof Force && c2 instanceof Debris) {
                        
                        Vector2d speed = new Vector2d(
                            (rand.nextInt(200)-100)*0.0001f,
                            (rand.nextInt(200)-100)*0.0001f);
                        
                        ((Debris) c2).applyForce(speed);
                    }
                    else if (c1 instanceof Debris && c2 instanceof Force) {
                        
                        Vector2d speed = new Vector2d(
                            (rand.nextInt(200)-100)*0.0001f,
                            (rand.nextInt(200)-100)*0.0001f);
                        
                        ((Debris) c1).applyForce(speed);
                    }
//                    else if (c1 instanceof Debris && c2 instanceof Debris) {
//                        
//                        Debris d = (Debris) c1;
//                        
//                        d.setSpeed(new Vector2d(-d.getSpeed().getX()+0.01f,
//                            -d.getSpeed().getY()+0.01f));
//                    }
                }
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
            
            //cast to a bounding rect
            BoundingRect r1 = (BoundingRect) firstBounding;
            BoundingRect r2 = (BoundingRect) secondBounding;
            
            //short hand
            float r1x1 = r1.getPos().getX();
            float r1x2 = r1.getPos().getX() + r1.getDim().getX();
            float r1y1 = r1.getPos().getY();
            float r1y2 = r1.getPos().getY() + r1.getDim().getY();
            float r2x1 = r2.getPos().getX();
            float r2x2 = r2.getPos().getX() + r2.getDim().getX();
            float r2y1 = r2.getPos().getY();
            float r2y2 = r2.getPos().getY() + r2.getDim().getY();
            
            //check if the bounding boxes are intersecting
            return  ((r1x1 > r2x1 && r1x1 < r2x2) &&
                     ((r1y1 > r2y1 && r1y1 < r2y2) ||
                      (r1y2 > r2y1 && r1y2 < r2y2))) ||
                      ((r1x2 > r2x1 && r1x2 < r2x2) &&
                       ((r1y1 > r2y1 && r1y1 < r2y2) ||
                        (r1y2 > r2y1 && r1y2 < r2y2)));
        }
        
        return false;
    }
}
