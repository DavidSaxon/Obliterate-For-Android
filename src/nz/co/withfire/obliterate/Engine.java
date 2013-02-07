/******************************************************************************\
| The engine of obliterate. Contains the main loop which is inside onDrawFrame |
|																			   |
| @author DavidSaxon														   |
\******************************************************************************/
package nz.co.withfire.obliterate;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class Engine implements GLSurfaceView.Renderer {

	//ENUMERATORS
	//Engine states
	enum State {
		START_UP
	};
	
	//VARIABLES
	//the current state of the engine
	private State state = State.START_UP;
	//is true when the state has been changed
	private boolean stateChanged = true;
	
	//the number of layers
	private final int numLayers = 5;
	//an list of layers which contains lists of entities
	private ArrayList<ArrayList<Entity>> entities = new ArrayList<ArrayList<Entity>>();
	
	//the projection matrix
	private final float[] projectionMatrix = new float[16];
	//the view matrix
	private final float[] viewMatrix = new float[16];
	//the model view projection matrix
	private final float[] mvpMatrix = new float[16];
	
	//TODO: remove
	private Quad2d quad;
	
	//METHODS
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

    	//TODO: init openGL method
        //set the background frame colour
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        //initalise the entites list
        for (int i = 0; i < numLayers; ++i) {
        	
        	entities.add(i, new ArrayList<Entity>());
        }
        
        //create the quad (for testing)
        float quadCoord[] = {-0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f};
        float quadColour[] = {0.2f, 0.709803922f, 0.898039216f, 1.0f};
        quad = new Quad2d(quadCoord, quadColour);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

    	//TODO: fix!
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
    
    @Override
    public void onDrawFrame(GL10 unused) {
    	
    	//TODO: fps
    	
    	//check if the state has changed
    	if (stateChanged) {
    		
    		initState();
    	}
    	
    	//Draw code
        //redraw background colour //TODO: buffer depth?
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        //set the camera position
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //calculate the projection and view transformations
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        //draw the quad TODO: remove
        quad.draw(mvpMatrix);
    }
    
    /**Initialises the new state*/
    private void initState() {
    	
    	switch (state) {
    	
    	case START_UP:
    		
    		initStartUP();
    		break;
    	}
    }
}