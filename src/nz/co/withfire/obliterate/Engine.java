/******************************************************************************\
| The engine of obliterate. Contains the main loop which is inside onDrawFrame |
|                                                                              |
| @author DavidSaxon                                                           |
\******************************************************************************/
package nz.co.withfire.obliterate;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.entities.main.Debris;
import nz.co.withfire.obliterate.entities.main.Force;
import nz.co.withfire.obliterate.entities.main.ObliterateImage;
import nz.co.withfire.obliterate.entities.start_up.LoadingBar;
import nz.co.withfire.obliterate.entities.start_up.Logo;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.Physics;
import nz.co.withfire.obliterate.utilities.CoordUtil;
import nz.co.withfire.obliterate.utilities.Vector2d;
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
    
    //FLAGS
    //true to show the frame rate
    private final boolean showFps = true;
    
    //VARIABLES
    //the current state of the engine
    private State state = State.MAIN;
    //is true when the state has been changed
    private boolean stateChanged = true;
    
    //the dimensions of the screen
    private Vector2d screenDim;
    
    //fps management
    //the current time
    private long currentTime;
    //the time accumulated from the last frame
    private int accumTime = 0;
    //the length of a frame
    private final int frameLength = 33;
    
    //input
    //is true if there has been a touch event
    private boolean touchEvent = false;
    private Vector2d touchPos;
    
    //the number of layers
    private final int numLayers = 5;
    //an list of layers which contains lists of entities
    private ArrayList<ArrayList<Entity>> entities = new ArrayList<ArrayList<Entity>>();
    //the physics controller
    private Physics physics;
    
    //Entities
    //keep a reference to the loading bar
    private LoadingBar loadingBar;
    //keep a reference to the obliterate image
    //private ObliterateImage obliterateImage;
    
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
        
        //create a new physics object
        physics = new Physics();
        
        //get the current time
        currentTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        
        //store the screen dimensions
        screenDim = new Vector2d(width, height);
        
        //set the view port
        GLES20.glViewport(0, 0, width, height);

        //calculate the projection matrix
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        
        //get the current time
        currentTime = System.currentTimeMillis();
    }
    
    @Override
    public void onDrawFrame(GL10 unused) {
        
        //FPS Management
        //get the time
        long newTime = System.currentTimeMillis();
        //find the the amount of time passed since the last frame
        int frameTime = (int) (newTime - currentTime);
        //set the new current time
        currentTime = newTime;
        //accumulate the time
        accumTime += frameTime;
        
        //show the fps
        if (showFps) {
            
            showFps();
        }
        
        //avoid downwards spiral of doom
        if (accumTime >= frameLength * 3) {
            
            accumTime = frameLength * 3;
        }
        
        //loop through all frames that have passed (if any) and update
        while (accumTime >= frameLength) {
        
            //check if the state has changed
            if (stateChanged) {
                
                initState();
            }
            
            //apply state specific updates
            switch (state) {
            
                case START_UP: {
                
                    load();
                    break;
                }
            }
            
            //check for touch event
            if (touchEvent) {
                
                processTouchEvent();
            }
            
            //COLLISON CHECK
            physics.collisionCheck();
            
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
                    
                    //remove from physics if collision type
                    if (r instanceof CollisionType) {
                        
                        physics.removeEntity((CollisionType) r);
                    }
                }
                
                //add new entities
                for (Entity a : addList) {
                    
                    entities.get(i).add(a);
                    
                    //if a collision type add to the physics controller
                    if (a instanceof CollisionType) {
                        
                        physics.addEntity((CollisionType) a);
                    }
                }
            }
            
            //deplete the accumulated time
            accumTime -= frameLength;
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
        
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            
            //get the position and convert to openGL point
            touchPos = CoordUtil.screenPosToOpenGLPos(
                    new Vector2d(e.getX(), e.getY()), screenDim,
                    viewMatrix, projectionMatrix);
            
            switch (state) {
            
                case START_UP: {
                    
                    //do nothing
                    break;
                }
                case MAIN: {
                    
                    touchEvent = true;
                    break;
                }
            }
        }
    }
    
    //PRIVATE METHODS
    /**Processes any touch events*/
    private void processTouchEvent() {
        
        switch(state) {
            
            case START_UP: {
                
                //do nothing
                break;
            }
            case MAIN: {
                
                //add a force point
                Force f = new Force(touchPos);
                
                //TODO: add to layer 0
                entities.get(4).add(f);
                physics.addEntity(f);
                
                //tell the image to obliterate
                //obliterateImage.setToObliterate();
                
                break;
            }
        }
        
        touchEvent = false;
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
        
        //TODO: get image from file system (need new state)
        
        //TODO: work out the size of the image and the texture and pass it to debris\
        
        //FIXME: remove speed?
        Vector2d speed = new Vector2d(0.0f, 0.0f);
        
        for (float y = -0.5f; y < 0.5f; y += 0.1f) {
            for (float x = -0.5f; x < 0.5f; x += 0.1f) {
             
                //create the debris
                Debris d = new Debris(new Vector2d(x, y), 0.1f, speed);
                
                //add to physics
                physics.addEntity(d);
                
                //add to entities
                entities.get(2).add(d);
            }
        }
        
        //add an obliterate image to the 3rd layer
        //obliterateImage = new ObliterateImage();
        //entities.get(2).add(obliterateImage);
    }
    
    /**Clear all entities from the entities list*/
    private void clearEntities() {
        
        for (int i = 0; i < numLayers; ++i) {
            
            entities.set(i, new ArrayList<Entity>());
        }
    }
    
    /**Initialises openGL*/
    private void initGL() {
        
        //set the clear colour
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        //enable transparency
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc (GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    /**Display the frame rate in the log*/
    private void showFps() {
        
        if (accumTime >= frameLength) {
            
            
            Log.v("Obliterate", "accum: " + (accumTime));
            Log.v("Obliterate", "fps: " + (1000.0f / accumTime));
        }
    }
}