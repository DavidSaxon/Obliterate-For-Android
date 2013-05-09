/******************************************************************************\
| The engine of obliterate. Contains the main loop which is inside onDrawFrame |
|                                                                              |
| @author DavidSaxon                                                           |
\******************************************************************************/
package nz.co.withfire.obliterate;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.revmob.ads.link.RevMobLink;

import nz.co.withfire.obliterate.fps_manager.FpsManager;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.entities.main.Debris;
import nz.co.withfire.obliterate.entities.main.Force;
import nz.co.withfire.obliterate.entities.menu.OpenMenuButton;
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
import android.content.Intent;
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
    
    //the current angle of gravity
    private float gravityAngle = 0.0f;
    
    //random number generator
    private Random rand = new Random();
    private int lastRand = 3;
    
    //the dimensions of the screen
    private Vector2d screenDim;
    //the dimensions of the screen in openGL coords
    private Vector2d screenDimGL;
    
    //the position of the obliterate obstacle
    private Vector2d objPos = new Vector2d(0.0f, 0.0f);
    
    //fps management
    private FpsManager fps = new FpsManager();
    
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
    
    //textures
    //the logo texture
    private int logoTex;
    //the open menu button texture
    private int openMenuTex;
    //the rest scene button texture
    private int resetSceneTex;
    //the obliterate image textures
    private int obliterateTex1;
    private int obliterateTex2;
    private int obliterateTex3;
    private int obliterateTex4;
    //the explosion texture
    private int[] explosionTex = new int[38];
    
    //Entities
    //keep a reference to the loading bar
    private LoadingBar loadingBar;
    //We need to keep a reference to the open menu button to check if
    //it has been pressed
    private OpenMenuButton openMenuButton;
    //the reset scene button
    private ResetSceneButton resetSceneButton;
    
    //progress out of 1.0 loading
    private float loadProgress = 0.0f;
    
    private RevMobLink link;
    
    //Matrix
    //the projection matrix
    private final float[] projectionMatrix = new float[16];
    //the view matrix
    private final float[] viewMatrix = new float[16];
    
    //CONSTRUCTOR
    public Engine(Context context, RevMobLink link) {
        
        activityContext = context;
        
        this.link = link;
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
        
        //we have to load the logo texture before we start
        logoTex = TextureLoader.loadTexture(activityContext, R.drawable.logo);
        
        fps.zero();
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
        
        //set the camera position
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        
        //store the openGL dimensions
        screenDimGL = new Vector2d(CoordUtil.screenPosToOpenGLPos(
                screenDim, screenDim, viewMatrix, projectionMatrix));
        
        //create a new physics controller        
        physics = new Physics(screenDimGL);
        
        fps.zero();
    }
    
    @Override
    public void onDrawFrame(GL10 unused) {
        
        //the amount of times we need to update
        int updateAmount = fps.update();
        
        //update as many times as we need to
        for (int u = 0; u < updateAmount; ++u) {
        
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
        
        //get the position and convert to openGL point
        touchPos = CoordUtil.screenPosToOpenGLPos(
            new Vector2d(e.getX(), e.getY()), screenDim,
            viewMatrix, projectionMatrix);
        
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            
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
            
                //check if there is a collision with the open menu button
                if (openMenuButton != null &&
                    physics.collision(openMenuButton, touchPoint)) {
                    
                    link.open();
                }
                //check if the reset scene button is pressed
                else if (physics.collision(resetSceneButton, touchPoint)) {
                    
                    stateChanged = true;
                }
                else {
                    
                    //add a force point
                    Force f = new Force(touchPos, explosionTex);

                    entities.get(4).add(f);
                    physics.addEntity(f);
                }
            }
            
            break;
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
        
        if (link != null) {
            
            //add the open menu button
            openMenuButton = new OpenMenuButton(screenDimGL, openMenuTex);
            entities.get(0).add(openMenuButton);
        }
        
        //add the reset scene button
        resetSceneButton = new ResetSceneButton(screenDimGL,
            resetSceneTex, true);
        entities.get(0).add(resetSceneButton);
        
        //FIXME: remove speed?
        Vector2d speed = new Vector2d(0.0f, 0.0f);
        
        int obTex = 0;
        int rnd = rand.nextInt(4);
        
        if (rnd == lastRand) {
            
            rnd = rand.nextInt(4);
        }
        
        if (rnd == lastRand) {
            
            rnd = rand.nextInt(4);
        }
        
        lastRand = rnd;
        
        if (rnd == 0) {
            
            obTex = obliterateTex1;
        }
        else if (rnd == 1) {
            
            obTex = obliterateTex2;
        }
        else if (rnd == 2) {
            
            obTex = obliterateTex3;
        }
        else {
            
            obTex = obliterateTex4;
        }
        
        for (float y = -0.45f; y < 0.46f; y += 0.1f) {
            for (float x = -0.45f; x < 0.46f; x += 0.1f) {
             
                float tx1 = x + 0.45f;
                float tx2 = tx1 + 0.1f;
                float ty1 = y + 0.45f;
                float ty2 = ty1 + 0.1f;
                
                float[] texCoords = {   tx2, ty2,
                                        tx2, ty1,
                                        tx1, ty1, 
                                        tx1, ty2};
                
                //create the debris
                Debris d = new Debris(new Vector2d(x + objPos.getX(),
                    y + objPos.getY()), 0.1f, speed,
                    texCoords, obTex);
                
                //add to physics
                physics.addEntity(d);
                
                //add to entities
                entities.get(3).add(d);
            }
        }
    }
    
    /**Clear all entities from the entities list*/
    private void clearEntities() {
        
        for (int i = 0; i < numLayers; ++i) {
            
            entities.set(i, new ArrayList<Entity>());
        }
        
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
        if (Math.abs(loadProgress - 0.35f) < 0.001f) {
            
            //load obliterate
            obliterateTex1 = TextureLoader.loadTexture(
                activityContext, R.drawable.square_1);
            obliterateTex2 = TextureLoader.loadTexture(
                    activityContext, R.drawable.square_2);
            obliterateTex3 = TextureLoader.loadTexture(
                    activityContext, R.drawable.square_3);
            obliterateTex4 = TextureLoader.loadTexture(
                    activityContext, R.drawable.square_4);
        }
        if (Math.abs(loadProgress - 0.40f) < 0.001f) {
            
            //load main buttons
            openMenuTex = TextureLoader.loadTexture(
                activityContext, R.drawable.free_games);
            resetSceneTex = TextureLoader.loadTexture(
                activityContext, R.drawable.reset_scene);
        }
        else if (Math.abs(loadProgress - 0.45f) < 0.001f) {
            
        }
        else if (Math.abs(loadProgress - 0.50f) < 0.001f) {
            
            
        }
        else if (Math.abs(loadProgress - 0.55f) < 0.001f) {
            
        }
        else if (Math.abs(loadProgress - 0.60f) < 0.001f) {
            
            //load the explosion part 1
            explosionTex[0] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion1);
            explosionTex[1] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion2);
            explosionTex[2] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion3);
            explosionTex[3] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion4);
            explosionTex[4] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion5);
            explosionTex[5] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion6);
            explosionTex[6] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion7);
            explosionTex[7] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion8);
            explosionTex[8] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion9);
            explosionTex[9] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion10);
        }
        else if (Math.abs(loadProgress - 0.65f) < 0.001f) {
            
            //load explosion part 2
            explosionTex[10] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion11);
            explosionTex[11] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion12);
            explosionTex[12] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion13);
            explosionTex[13] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion14);
            explosionTex[14] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion15);
            explosionTex[15] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion16);
            explosionTex[16] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion17);
            explosionTex[17] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion18);
            explosionTex[18] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion19);
            explosionTex[19] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion20);
        }
        else if (Math.abs(loadProgress - 0.70f) < 0.001f) {
            
            //load explosion part 3
            explosionTex[20] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion21);
            explosionTex[21] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion22);
            explosionTex[22] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion23);
            explosionTex[23] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion24);
            explosionTex[24] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion25);
            explosionTex[25] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion26);
            explosionTex[26] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion27);
            explosionTex[27] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion28);
            explosionTex[28] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion29);
            explosionTex[29] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion30);
        }
        else if (Math.abs(loadProgress - 0.75f) < 0.001f) {
            
            //load explosion part 4
            explosionTex[30] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion31);
            explosionTex[31] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion32);
            explosionTex[32] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion33);
            explosionTex[33] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion34);
            explosionTex[34] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion35);
            explosionTex[35] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion36);
            explosionTex[36] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion37);
            explosionTex[37] = TextureLoader.loadTexture(activityContext,
                    R.drawable.explosion38);
        }
        else if (Math.abs(loadProgress - 0.80f) < 0.001f) {
            
        }
        else if (Math.abs(loadProgress - 0.85f) < 0.001f) {
            
        }
        else if (Math.abs(loadProgress - 0.90f) < 0.001f) {
            
        }
        else if (Math.abs(loadProgress - 0.95f) < 0.001f) {
            
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
