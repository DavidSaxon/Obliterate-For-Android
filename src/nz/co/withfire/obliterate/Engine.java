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
import nz.co.withfire.obliterate.entities.menu.Button;
import nz.co.withfire.obliterate.entities.menu.OpenMenuButton;
import nz.co.withfire.obliterate.entities.menu.PauseMenuBackground;
import nz.co.withfire.obliterate.entities.menu.ResetSceneButton;
import nz.co.withfire.obliterate.entities.menu.TouchPoint;
import nz.co.withfire.obliterate.entities.start_up.LoadingBar;
import nz.co.withfire.obliterate.entities.start_up.Logo;
import nz.co.withfire.obliterate.graphics.TextureLoader;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.Physics;
import nz.co.withfire.obliterate.utilities.CoordUtil;
import nz.co.withfire.obliterate.utilities.Vector2d;
import android.content.Context;
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
    
    //particle type
    enum ParticleType {
        DEBRIS,
        SMOKE,
        LIQUID,
        GLASS,
        MAGNETIC
    }
    
    //activity context
    private final Context activityContext;
    
    //FLAGS
    //true to show the frame rate
    private final boolean showFps = false;
    
    //VARIABLES
    //the current state of the engine
    private State state = State.START_UP;
    //is true when the state has been changed
    private boolean stateChanged = true;
    //is true when is paused
    private boolean paused = false;
    //the current type of particle being used
    private ParticleType particleType = ParticleType.DEBRIS;
    
    //the dimensions of the screen
    private Vector2d screenDim;
    //the dimensions of the screen in openGL coords
    private Vector2d screenDimGL;
    
    //fps management
    //the current time
    private long currentTime;
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
    //a list of entities that are in the pause menu
    private ArrayList<Entity> menuEntities = new ArrayList<Entity>();
    //the physics controller
    private Physics physics;
    
    //textures
    //the logo texture
    private int logoTex;
    //the open menu button texture
    private int openMenuTex;
    //the rest scene button texture
    private int resetSceneTex;
    //menu button textures
    private int[] debrisButtonTex = new int[2];
    private int[] smokeButtonTex = new int[2];
    private int[] liquidButtonTex = new int[2];
    private int[] glassButtonTex = new int[2];
    private int[] magneticButtonTex = new int[2];
    private int[] electricButtonTex = new int[2];
    private int[] noneButtonTex = new int[2];
    private int[] shockwaveButtonTex = new int[2];
    private int[] explosionButtonTex = new int[2];
    private int[] setPosButtonTex = new int[2];
    private int[] addObButtonTex = new int[2];
    private int[] exitButtonTex = new int[2];
    //the shock wave texture
    private int shockwaveTex; //TODO: make into an array and rename to shock wave
    
    //Entities
    //keep a reference to the loading bar
    private LoadingBar loadingBar;
    //We need to keep a reference to the open menu button to check if
    //it has been pressed
    private OpenMenuButton openMenuButton;
    //the reset scene button
    private ResetSceneButton resetSceneButton;
    //the pause menu background
    private PauseMenuBackground pMBG;
    //the debris button
    private Button debrisButton;
    //the smoke button
    private Button smokeButton;
    //the liquid button
    private Button liquidButton;
    //the glass button
    private Button glassButton;
    //the magnetic button
    private Button magneticButton;
    //the electric button
    private Button electricButton;
    
    //progress out of 1.0 loading
    private float loadProgress = 0.0f;
    
    //Matrix
    //the projection matrix
    private final float[] projectionMatrix = new float[16];
    //the view matrix
    private final float[] viewMatrix = new float[16];
    
    //CONSTRUCTOR
    public Engine(Context context) {
        
        activityContext = context;
    }
    
    //PUBLIC METHODS
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        
        //initialise openGL
        initGL();
        
        //initialise the entities list
        for (int i = 0; i < numLayers; ++i) {
            
            entities.add(i, new ArrayList<Entity>());
        }
        
        //get the current time
        currentTime = SystemClock.uptimeMillis();
        
        //we have to load the logo texture before we start
        logoTex = TextureLoader.loadTexture(activityContext, R.drawable.logo);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        
        //store the screen dimensions
        screenDim = new Vector2d(width, height);
        
        Log.v("Obliterate", width + " x " + height);
        
        //set the view port
        GLES20.glViewport(0, 0, width, height);

        //calculate the projection matrix
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        
        //set the camera position
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        
        //store the openGL dimensions
        screenDimGL = new Vector2d(CoordUtil.screenPosToOpenGLPos(
                screenDim, screenDim, viewMatrix, projectionMatrix));
        
        Log.v("Obliterate", screenDimGL.getX() + " x " + screenDimGL.getY());
        
        //create a new physics controller        
        physics = new Physics(screenDimGL);
        
        //get the current time
        currentTime = SystemClock.uptimeMillis();
    }
    
    @Override
    public void onDrawFrame(GL10 unused) {
        
        //FPS Management
        //get the time
        long newTime = SystemClock.uptimeMillis();
        //find the the amount of time passed since the last frame
        int frameTime = (int) (newTime - currentTime);
        //set the new current time
        currentTime = newTime;
        
        //show the fps
        if (showFps) {
            
            showFps();
        }
        
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
        
        if (!paused) {
            
            //COLLISON CHECK
            physics.collisionCheck();
            
            //UPDATE
            //iterate over the entities and update them
            for (int i = 0; i < numLayers; ++i) {
                
                //TODO: OPTIMISE REMOVE LISTS?
                //list of entities to be removed
                ArrayList<Entity> removeList = new ArrayList<Entity>();
                
                for (Entity e: entities.get(i)) {
                    
                    //check if the entity should be removed
                    if (e.shouldRemove()) {
                        
                        removeList.add(e);
                    }
                    //else update the entity
                    else {
                        
                       e.update();
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
            }
        }
        
        //TODO: physics for menu entities if paused
        //OR could do physics detection always?? riskay...
        
        //update the menu entities
        for (Entity e : menuEntities) {
            
            e.update();
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
        
        //iterate over the menu entities and draw them
        for (Entity e: menuEntities) {
            
           e.draw(viewMatrix, projectionMatrix);
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
                
                //create a touch point object
                TouchPoint touchPoint = new TouchPoint(screenDimGL, touchPos);
                
                if (paused) {
                    
                    //check if there is a collision with debris button
                    if (physics.collision(debrisButton, touchPoint)) {
                        
                        debrisButton.press();
                        smokeButton.depress();
                        liquidButton.depress();
                        glassButton.depress();
                        magneticButton.depress();
                        
                        particleType = ParticleType.DEBRIS;
                    }
                    //check if there is a collision with smoke button
                    else if (physics.collision(smokeButton, touchPoint)) {
                        
                        debrisButton.depress();
                        smokeButton.press();
                        liquidButton.depress();
                        glassButton.depress();
                        magneticButton.depress();
                        
                        particleType = ParticleType.SMOKE;
                    }
                    //check if there is a collision with liquid button
                    else if (physics.collision(liquidButton, touchPoint)) {
                        
                        debrisButton.depress();
                        smokeButton.depress();
                        liquidButton.press();
                        glassButton.depress();
                        magneticButton.depress();
                        
                        particleType = ParticleType.LIQUID;
                    }
                    //check if there is a collision with glass button
                    else if (physics.collision(glassButton, touchPoint)) {
                        
                        debrisButton.depress();
                        smokeButton.depress();
                        liquidButton.depress();
                        glassButton.press();
                        magneticButton.depress();
                        
                        particleType = ParticleType.GLASS;
                    }
                    //check if there is a collision with magnetic button
                    else if (physics.collision(magneticButton, touchPoint)) {
                        
                        debrisButton.depress();
                        smokeButton.depress();
                        liquidButton.depress();
                        glassButton.depress();
                        magneticButton.press();
                        
                        particleType = ParticleType.MAGNETIC;
                    }
                }
                else {
                    
                    //check if there is a collision with the open menu button
                    if (physics.collision(openMenuButton, touchPoint)) {
                        
                        initPause();
                    }
                    //check if the reset scene button is pressed
                    else if (physics.collision(resetSceneButton, touchPoint)) {
                        
                        stateChanged = true;
                    }
                    else {
                        
                        //add a force point
                        Force f = new Force(touchPos, shockwaveTex);
                        
                        //TODO: add to layer 0
                        entities.get(2).add(f);
                        physics.addEntity(f);
                    }
                }
                
                break;
            }
        }
        
        touchEvent = false;
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
        entities.get(1).add(new Logo(screenDimGL, logoTex));
        
        //add the loading bar to the first layer
        loadingBar = new LoadingBar(screenDimGL);
        entities.get(0).add(loadingBar);
        loadProgress = 0.0f;
    }
    
    /**Initialises the main state*/
    private void initMain() {
        
        //clear the entities
        clearEntities();
        
        //TODO: get image from file system (need new state)
        
        //TODO: work out the size of the image and the texture and pass it to debris\
        
        //add the open menu button
        openMenuButton = new OpenMenuButton(screenDimGL, openMenuTex);
        menuEntities.add(openMenuButton);
        
        //add the reset scene button
        resetSceneButton = new ResetSceneButton(screenDimGL, resetSceneTex);
        menuEntities.add(resetSceneButton);
        
        //FIXME: remove speed?
        Vector2d speed = new Vector2d(0.0f, 0.0f);
        
        for (float y = -0.5f; y < 0.5f; y += 0.1f) {
            for (float x = -0.5f; x < 0.5f; x += 0.1f) {
             
                if (particleType == ParticleType.DEBRIS) {
                    
                    //create the debris
                    Debris d = new Debris(new Vector2d(x, y), 0.1f, speed);
                    
                    //add to physics
                    physics.addEntity(d);
                    
                    //add to entities
                    entities.get(4).add(d);
                }
            }
        }
    }
    
    /**Initialises the pause menu*/
    private void initPause() {
        
        paused = true;
        
        //tell the open menu button to slide
        openMenuButton.slideForwards();
        resetSceneButton.slideForwards();
        
        //add background
        pMBG = new PauseMenuBackground(screenDimGL);
        menuEntities.add(pMBG);
        
        debrisButton = new Button(screenDimGL, pMBG.getPos(),
            new Vector2d(-(screenDimGL.getX() / 2.0f), -(screenDimGL.getY() / 1.6f)),
            debrisButtonTex);
        menuEntities.add(debrisButton);
        smokeButton = new Button(screenDimGL, pMBG.getPos(),
                new Vector2d(-(screenDimGL.getX() / 6.0f), -(screenDimGL.getY() / 1.6f)),
                smokeButtonTex);
        menuEntities.add(smokeButton);
        liquidButton = new Button(screenDimGL, pMBG.getPos(),
                new Vector2d((screenDimGL.getX() / 6.0f), -(screenDimGL.getY() / 1.6f)),
                liquidButtonTex);
        menuEntities.add(liquidButton);
        glassButton = new Button(screenDimGL, pMBG.getPos(),
                new Vector2d((screenDimGL.getX() / 2.0f), -(screenDimGL.getY() / 1.6f)),
                glassButtonTex);
        menuEntities.add(glassButton);    
        magneticButton = new Button(screenDimGL, pMBG.getPos(),
                new Vector2d((screenDimGL.getX() / 1.2f), -(screenDimGL.getY() / 1.6f)),
                magneticButtonTex);
        menuEntities.add(magneticButton);
        
    }
    
    /**Clear all entities from the entities list*/
    private void clearEntities() {
        
        for (int i = 0; i < numLayers; ++i) {
            
            entities.set(i, new ArrayList<Entity>());
        }
        
        menuEntities.clear();
        
        physics.clear();
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
        
        //if (accumTime >= frameLength) {
            
        //    Log.v("Obliterate", "fps: " + (1000.0f / accumTime));
        //}
    }
    
    /**Loads in the needed data for obliterate*/
    private void load() {
        
        //load images
        if (Math.abs(loadProgress - 0.0f) < 0.001f) {
            
            //load main buttons
            openMenuTex = TextureLoader.loadTexture(
                activityContext, R.drawable.open_menu);
            resetSceneTex = TextureLoader.loadTexture(
                activityContext, R.drawable.reset_scene);
        }
        else if (Math.abs(loadProgress - 0.05f) < 0.001f) {
            
            //load the menu buttons
            debrisButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.debris_button_unpressed);
            debrisButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.debris_button_pressed);
            smokeButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.smoke_button_unpressed);
            smokeButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.smoke_button_pressed);
            liquidButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.liquid_button_unpressed);
            liquidButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.liquid_button_pressed);
            glassButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.glass_button_unpressed);
            glassButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.glass_button_pressed);
            magneticButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.magnetic_button_unpressed);
            magneticButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.magnetic_button_pressed);
            electricButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.electric_button_unpressed);
            electricButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.electric_button_pressed);
            noneButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.none_button_unpressed);
            noneButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.none_button_pressed);
            shockwaveButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.shockwave_button_unpressed);
            shockwaveButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.shockwave_button_pressed);
            explosionButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion_button_unpressed);
            explosionButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion_button_pressed);
            setPosButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.set_position_button_unpressed);
            setPosButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.set_position_button_pressed);
            addObButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.add_obstacle_button_unpressed);
            addObButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.add_obstacle_button_pressed);
            exitButtonTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.exit_button_unpressed);
            exitButtonTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.exit_button_pressed);
        }
        else if (Math.abs(loadProgress - 0.10f) < 0.001f) {
            
            //load the titles
        }
        else if (Math.abs(loadProgress - 0.15f) < 0.001f) {
            
            //load the shockwave
            shockwaveTex = TextureLoader.loadTexture(
                activityContext, R.drawable.force);
        }
        
        //update the progress if the loading bar
        loadingBar.updateProgress(loadProgress);
        
        //set the new progress
        if (loadProgress < 1.0f) {
            
            loadProgress += 0.05f;
        }
        else {
            
            state = State.MAIN;
            stateChanged = true;
        }
    }    
}
