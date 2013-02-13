/************************************\
| Utilities relating to co-ordinates |
|                                    |
| @author David Saxon                |
\************************************/

package nz.co.withfire.obliterate.utilities;

import android.opengl.Matrix;

public class CoordUtil {

    /**Converts screen position to an opengl position
    Credit to Erol:
    http://stackoverflow.com/questions/10985487/android-opengl-es-2-0-screen-coordinates-to-world-coordinates
    @param screenPos the screen position
    @param screenDim the dimensions of the screen
    @param viewMatrix the view matrix
    @param projectionMatrix the projection matrix
    @return the screen position converted to opengl position*/
    public static Vector2d screenPosToOpenGLPos(Vector2d screenPos,
            Vector2d screenDim, float[] viewMatrix, float[] projectionMatrix) {
        
        //intialise openGL position vector
        Vector2d openGLPos = new Vector2d();
        
        //Matrix
        float[] invertedMatrix = new float[16];
        float[] transformationMatrix = new float[16];
        
        //Points
        float[] normalisedPoint = new float[4];
        float[] outPoint = new float[4];
        
        //invert the positions
        int screenPosX = (int) (screenDim.getX() - screenPos.getX());
        int screenPosY = (int) (screenDim.getY() - screenPos.getY());
        
        //transform the screen point
        normalisedPoint[0] =
            (float) ((screenPosX) * 2.0f / screenDim.getX() - 1.0);
        normalisedPoint[1] =
                (float) ((screenPosY) * 2.0f / screenDim.getY() - 1.0);
        normalisedPoint[2] =  -1.0f;
        normalisedPoint[3] =   1.0f;

        //get the matrix
        Matrix.multiplyMM(transformationMatrix, 0, projectionMatrix, 0,
                viewMatrix, 0);
        Matrix.invertM(invertedMatrix, 0, transformationMatrix, 0);
        
        //apply the inverse to the point
        Matrix.multiplyMV(outPoint, 0, invertedMatrix, 0, normalisedPoint, 0);
        
        //avoid dividing by zero
        if (outPoint[3] == 0.0f) {
            
            return openGLPos;
        }
        
        //Divide by the 3rd component to find the real world position
        openGLPos.setX(outPoint[0] / outPoint[3]);
        openGLPos.setY(outPoint[1] / outPoint[3]);
        
        return openGLPos;
    }
    
}
