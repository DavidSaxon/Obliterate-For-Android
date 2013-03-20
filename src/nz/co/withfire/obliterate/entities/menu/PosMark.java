package nz.co.withfire.obliterate.entities.menu;

import android.opengl.Matrix;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.QuadTex2d;
import nz.co.withfire.obliterate.utilities.Vector2d;

public class PosMark extends Entity {

    //the position
    private Vector2d pos = new Vector2d();
    
    //the image of the mark
    private QuadTex2d image;
    
    //the texture of the image
    private int tex;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    public PosMark(Vector2d glDim, Vector2d pos, int tex) {
        
        this.pos.copy(pos);
        
        //set the dimensions
        Vector2d dim1 = new Vector2d(glDim.getX() / 20.0f, glDim.getX() / 20.0f);
        
        float quadCoord[] = {   -dim1.getX(),  dim1.getY(), 0.0f,
                -dim1.getX(), -dim1.getY(), 0.0f,
                 dim1.getX(), -dim1.getY(), 0.0f,
                 dim1.getX(),  dim1.getY(), 0.0f};
        final float[] texCoords = { 1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f, 
                    0.0f, 0.0f};
        image = new QuadTex2d(quadCoord, texCoords, tex);
    }
    
    public void update() {
        
        //do nothing
    }
    
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
       //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX(), pos.getY(), -0.01f);
        
        //multiply the matrixOpenMenuButton
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
    
    public void setPos(Vector2d pos) {
        
        this.pos.copy(pos);
    }
}
