package nz.co.withfire.obliterate.entities.menu;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.utilities.Vector2d;

public class Divider extends Entity {

    //VARIABLES
    //the position of the divider
    private Vector2d pos = new Vector2d();
    //the dimensions of the divider
    private Vector2d dim = new Vector2d();
    //the position of the menu background
    private Vector2d bgPos;
    
    //the image of the divider
    private Quad2d image;
    
    //CONSTUCTOR
    public Divider(Vector2d pos, Vector2d dim, Vector2d bgPos) {
        
        this.pos.copy(pos);
        this.dim.copy(dim);
        this.bgPos = bgPos;
        
        //create the divider quad
    }
    
    //METHODS
    
}
