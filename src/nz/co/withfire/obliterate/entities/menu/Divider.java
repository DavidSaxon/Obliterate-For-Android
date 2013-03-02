package nz.co.withfire.obliterate.entities.menu;

import android.opengl.Matrix;
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
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTUCTOR
    public Divider(Vector2d pos, Vector2d dim, Vector2d bgPos) {
        
        this.pos.copy(pos);
        this.dim.copy(dim);
        this.bgPos = bgPos;
        
        //create the divider quad
        float quadCoord[] = {   -dim.getX(),  dim.getY(), 0.0f,
                -dim.getX(), -dim.getY(), 0.0f,
                 dim.getX(), -dim.getY(), 0.0f,
                 dim.getX(),  dim.getY(), 0.0f};
        float colour[] = {  0.4f, 0.4f, 0.4f, 1.0f,
                            0.4f, 0.4f, 0.4f, 1.0f,
                            0.4f, 0.4f, 0.4f, 1.0f,
                            0.4f, 0.4f, 0.4f, 1.0f };
        
        image = new Quad2d(quadCoord, colour);
    }
    
    //METHODS
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX(), pos.getY(), -0.01f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
}
