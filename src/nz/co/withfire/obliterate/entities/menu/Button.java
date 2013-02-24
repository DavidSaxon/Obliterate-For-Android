package nz.co.withfire.obliterate.entities.menu;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.QuadTex2d;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.bounding.BoundingRect;
import nz.co.withfire.obliterate.utilities.Vector2d;

public class Button extends CollisionType {

    //VARIABLES
    //the position of the button
    private Vector2d pos = new Vector2d();
    //the dimensions of the button
    private Vector2d dim = new Vector2d();
    //reference to the position of the background
    private Vector2d backPos;
    //the offset from the back position
    private Vector2d offset = new Vector2d();
    
    //the array of textures for the button
    private int[] texs = new int[2];
    
    //the curent texture of the button
    private int tex;
    
    //the image of the button
    private QuadTex2d image;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    public Button(Vector2d GLdim, Vector2d backPos,
            Vector2d offset, int[] texs) {
        
        //copy the background pos
        this.backPos = backPos;
        this.pos.copy(offset);
        
        //set the tex
        this.texs = texs;
        this.tex = texs[0];
        
        //find the dimensions of the button
        float width = GLdim.getX() / 8.0f;
        
        dim.set(width, width / 2.33333f);
        
        float quadCoord[] = {   -dim.getX(),  dim.getY(), 0.0f,
                                -dim.getX(), -dim.getY(), 0.0f,
                                 dim.getX(), -dim.getY(), 0.0f,
                                 dim.getX(),  dim.getY(), 0.0f};
        final float[] texCoords = { 1.0f, 0.0f,
                                    1.0f, 1.0f,
                                    0.0f, 1.0f, 
                                    0.0f, 0.0f};
        image = new QuadTex2d(quadCoord, texCoords, tex);
        
        //set the bounding box
        boundingBox = new BoundingRect(pos, dim);
    }
    
    //METHODS
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX() + backPos.getX(),
            pos.getY() + backPos.getY(), -0.01f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
    
    //press the button
    public void press() {
        
        tex = texs[1];
        image.setTex(tex);
    }
    
    //depress the button
    public void depress() {
        
        tex = texs[0];
        image.setTex(tex);
    }
}
