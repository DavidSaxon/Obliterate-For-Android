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
import nz.co.withfire.obliterate.entities.main.Debris;
import nz.co.withfire.obliterate.entities.main.ObliterateImage;
import nz.co.withfire.obliterate.entities.start_up.LoadingBar;
import nz.co.withfire.obliterate.entities.start_up.Logo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

public class Engine implements GLSurfaceView.Renderer {

	//ENUMERATORS
	//Engine states
	enum State {
		START_UP,
		MAIN
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
	
	//Entities
	//keep a reference to the loading bar
	private LoadingBar loadingBar;
	//keep a reference to the obliterate image
	private ObliterateImage obliterateImage;
	
	//progress out of 1.0 loading
	private float loadProgress = 0.0f;
	
	//Matrix
	//the projection matrix
	private final float[] projectionMatrix = new float[16];
	//the view matrix
	private final float[] viewMatrix = new float[16];
	
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
            
            //TODO: OPTIMISE REMOVE LISTS?
            //list of entities to be removed
            ArrayList<Entity> removeList = new ArrayList<Entity>();
            
            //list of entities to be added
            ArrayList<Entity> addList = new ArrayList<Entity>();
            
	        for (Entity e: entities.get(i)) {
	        	
	            //check if the entity should be removed
	            if (e.shouldRemove()) {
	                
	                removeList.add(e);
	            }
	            //else update the entity
	            else {
	                
	                ArrayList<Entity> entityAdd = e.update();
	                
	                if (entityAdd != null) {
	                    
	                    addList.addAll(entityAdd);
	                }
	            }
	        }
	        
	        //remove the entities
	        for (Entity r : removeList) {
	            
	            entities.get(i).remove(r);
	        }
	        
	        //add new entities
	        entities.get(i).addAll(addList);
        }
        
    	//DRAW
        //redraw background colour
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        //set the camera position
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        
        //iterate over the entities and draw them
        for (int i = numLayers-1; i >= 0; --i) {
	        for (Entity e: entities.get(i)) {
	        	
	        	e.draw(viewMatrix, projectionMatrix);
	        }
        }
    }
    
    /**Inputs a touch event
    @param e the motion event*/
    public void inputTouch(MotionEvent e) {
        
        float touchX = e.getX();
        float touchY = e.getY();
        
        switch (state) {
        
            case START_UP: {
                
                //do nothing
                break;
            }
            case MAIN: {
                
                //set the image to obliterate
                obliterateImage.setToObliterate();
                
                //TODO:create a force
                
                break;
            }
        }
    }
    
    //PRIVATE METHODS
    /**Initialises the new state*/
    private void initState() {
    	
    	stateChanged = false;
    	
    	switch (state) {
    	
        	case START_UP: {
        		
        		initStartUp();
        		break;
        	}
        	case MAIN: {
        		
        		initMain();
        		break;
        	}
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
    
    /**Initialises the main state*/
    private void initMain() {
    	
    	//clear the entities
    	clearEntities();
    	
    	//TODO: get from file system (need new state)
    	
    	//add an obliterate image to the 3rd layer
    	obliterateImage = new ObliterateImage();
    	entities.get(2).add(obliterateImage);
    }
    
    /**Clear all entities from the entities list*/
    private void clearEntities() {
    	
        for (int i = 0; i < numLayers; ++i) {
        	
        	entities.set(i, new ArrayList<Entity>());
        }
    }
    
    /**Loads in the needed data for obliterate*/
    private void load() {
    	
    	//update the progress if the loading bar
		loadingBar.updateProgress(loadProgress);
    	
    	//TODO: actually load images here
		if (loadProgress < 1.0f) {
			
			loadProgress += 0.01f;
		}
		else {
			
			state = State.MAIN;
			stateChanged = true;
		}
    }
    
    /**Initialises openGL*/
    private void initGL() {
    	
    	//set the clear colour
    	GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}