package nz.co.withfire.obliterate.entities.menu;

import android.opengl.Matrix;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.QuadTex2d;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.bounding.BoundingRect;
import nz.co.withfire.obliterate.utilities.Vector2d;

public class GravityArrow extends CollisionType {

    //VARIABLES
    //the position of the button
    private Vector2d pos = new Vector2d();
    //the dimensions of the button
    private Vector2d dim = new Vector2d();
    //reference to the position of the background
    private Vector2d backPos;
    //the rotation of the arrow
    private float rotation = 180.0f;
    
    //the texture
    private int tex;
    
    //the image of the arrow
    private QuadTex2d image;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    public GravityArrow(Vector2d GLdim, Vector2d backPos,
            Vector2d offset, int tex) {
        
        this.backPos = backPos;
        this.pos.copy(offset);
        this.tex = tex;
        
        float width = GLdim.getX() / 10.0f;
        
        dim.set(width, width);
        
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
    
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX() + backPos.getX(),
            pos.getY() + backPos.getY(), -0.01f);
        Matrix.rotateM(tMatrix, 0, rotation, 0.0f, 0.0f, 1.0f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
    
    public void setRotation(float rotation) {
        
        this.rotation = rotation;
    }
    
    public float getRotation() {
        
        return rotation;
    }
}
