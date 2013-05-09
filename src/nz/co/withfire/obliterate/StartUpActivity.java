/************************************************\
| Starts Obliterate, and passes on to the engine |
|                                                |
| @author David Saxon                            |
\************************************************/
package nz.co.withfire.obliterate;

import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.RevMobTestingMode;
import com.revmob.ads.link.RevMobLink;

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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;


public class StartUpActivity extends Activity {

    //VARIABLES
    //the gl surface to render onto (for displaying the title)
    private extGLSurfaceView display;
    
    //revmob
    private static final String REVMOB_APP_ID =
        "518904a3100c778cb5000006";
    private RevMob revmob;
    
    public static boolean ad = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        //super call
        super.onCreate(savedInstanceState);
        
        //set to full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        //set up revmob
        RevMob revmob = RevMob.start(this, REVMOB_APP_ID);
        RevMobAdsListener listener = new RevMobAdsListener() {
            public void onRevMobAdReceived() {}
            public void onRevMobAdNotReceived(String message) {Log.v("Obliterate", "here");Engine.ad = false;}
            public void onRevMobAdDisplayed() {Engine.waitDone = true;}
            public void onRevMobAdDismiss() {}
            public void onRevMobAdClicked() {Engine.wait = true;}
        };
        RevMobLink link = null;
        if (ad) {
        
            link = revmob.createAdLink(this, listener);
        }
        
        //set the display
        display = new extGLSurfaceView(this, link);
        
        //set the content view to the display
        setContentView(display);
    }
}

//TODO: turn this into a class
//small class that extends GLSurfaceView
class extGLSurfaceView extends GLSurfaceView {
    
    //VARIABLES
    //the engine
    public Engine engine;

    
    //CONSTRUCTOR
    public extGLSurfaceView(Context context, RevMobLink link) {
        
        //super call
        super(context);
        
        //create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        
        //create the engine
        engine = new Engine(context, link);
        
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