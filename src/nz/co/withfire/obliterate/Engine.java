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
import nz.co.withfire.obliterate.entities.start_up.LoadingBar;
import nz.co.withfire.obliterate.entities.start_up.Logo;
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
	//keep a reference to the loading bar
	private LoadingBar loadingBar;
	
	//progress out of 1.0 loading
	private float loadProgress = 0.0f;
	
	//the projection matrix
	private final float[] projectionMatrix = new float[16];
	//the view matrix
	private final float[] viewMatrix = new float[16];
	//the model view projection matrix
	private final float[] mvpMatrix = new float[16];
	
	//PUBLIC METHODS
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    	
    	//initialise openGL
    	initGL();
        
        //initialise the entities list
        for (int i = 0; i < numLayers; ++i) {
        	
        	entities.add(i, new ArrayList<Entity>());
        }
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
    	
    	//COLLISON CHECK
    	//TODO: collision loop (quad tree)
    	
    	//apply state specific updates
    	switch (state) {
    	
    	case START_UP:
    		
    		load();
    		break;
    	}
    	
    	//UPDATE
    	//iterate over the entities and update them
        for (int i = 0; i < numLayers; ++i) {
	        for (Entity e: entities.get(i)) {
	        	
	        	e.update();
	        }
        }
    	
    	//DRAW
        //redraw background colour
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        //set the camera position
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //calculate the projection and view transformations
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        
        //iterate over the entities and draw them
        for (int i = numLayers-1; i >= 0; --i) {
	        for (Entity e: entities.get(i)) {
	        	
	        	e.draw(mvpMatrix);
	        }
        }
    }
    
    //PRIVATE METHODS
    /**Initialises the new state*/
    private void initState() {
    	
    	stateChanged = false;
    	
    	switch (state) {
    	
    	case START_UP:
    		
    		initStartUp();
    		break;
    	}
    }
    
    /**Initialises the start up state*/
    private void initStartUp() {
    	
    	//clear the entities
    	clearEntities();
    	
    	//add the logo to the second layer
    	entities.get(1).add(new Logo());
    	
    	//add the loading bar to the first layer
    	loadingBar = new LoadingBar();
    	entities.get(0).add(loadingBar);
    	loadProgress = 0.0f;
    }
    
    /**Clear all entities from the entities list*/
    private void clearEntities() {
    	
        for (int i = 0; i < numLayers; ++i) {
        	
        	entities.set(i, new ArrayList<Entity>());
        }
    }
    
    /**Loads in the needed data for obliterate*/
    private void load() {
    	
    	//TODO: actually load images here
		if (loadProgress < 1.0f) {
			
			loadProgress += 0.01f;
		}
		
		//update the progress if the loading bar
		loadingBar.updateProgress(loadProgress);
    }
    
    /**Initialises openGL*/
    private void initGL() {
    	
    	//set the clear colour
    	GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}