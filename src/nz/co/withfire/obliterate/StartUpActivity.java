/************************************************\
| Starts Obliterate, and passes on to the engine |
|                                                |
| @author David Saxon                            |
\************************************************/
package nz.co.withfire.obliterate;

import nz.co.withfire.obliterate.R;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StartUpActivity extends Activity implements KeyListener {

    //VARIABLES
    //the gl surface to render onto (for displaying the title)
    private extGLSurfaceView display;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        //super call
        super.onCreate(savedInstanceState);
        
        //set to full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //set the display
        display = new extGLSurfaceView(this);
        
        //set the content view to the display
        setContentView(display);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        //TODO:
        //inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_start_up, menu);
        return true;
    }

    @Override
    public void clearMetaKeyState(View view, Editable content, int states) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getInputType() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean onKeyDown(View view, Editable text, int keyCode,
            KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onBackPressed() {
        
        if (display.engine.isPaused()) {
            
            display.engine.back = true;
            return;
        }
        
        super.onBackPressed();
        return;
    }

    @Override
    public boolean onKeyOther(View view, Editable text, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }
}

//TODO: turn this into a class
//small class that extends GLSurfaceView
class extGLSurfaceView extends GLSurfaceView {
    
    //VARIABLES
    //the engine
    public Engine engine;
    
    //CONSTRUCTOR
    public extGLSurfaceView(Context context) {
        
        //super call
        super(context);
        
        //create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        
        //create the engine
        engine = new Engine(context);
        
        setEGLConfigChooser(false);
        
        //set the renderer for drawing on this surface
        setRenderer(engine);
        
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }
    
    //METHODS
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        
        //pass the event on to the engine
        engine.inputTouch(e);
        
        return true;
    }
}