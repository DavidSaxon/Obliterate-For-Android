/**********************\
| A 2 dimensional quad |
|                      |
| @author David Saxon  |
\**********************/
package nz.co.withfire.obliterate.graphics.drawable.shape2d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import nz.co.withfire.obliterate.graphics.ShaderUtil;
import android.opengl.GLES20;
import nz.co.withfire.obliterate.utilities.*;

public class Quad2d implements Shape2d {

    //VARIABLES
    //the size of a float in bytes
    private final int sizeOfFloat = 4;
    //the number of position coords per vertex
    private final int coordsPerVertex = 3;
    //the number of colour values per vertex
    private final int colValPerVertex = 4;
    //the stride of a vertex
    private final int vertexStride = coordsPerVertex * sizeOfFloat;
    //the stride of a colour
    private final int colourStride = colValPerVertex * sizeOfFloat;
    //the order to draw the vertices
    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    //the vertex buffer
    private final FloatBuffer vertexBuffer;
    //the colour buffer
    private final FloatBuffer colourBuffer;
    //the draw list buffer
    private final ShortBuffer drawListBuffer;
    //the opengl program
    private final int program;

    
    //the coords of the quad
    private float coords[];
    //the colour of the quad
    private float colour[];
    
    //shaders
    private final String vertexShaderCode =

        //the model view projection matrix
        "uniform mat4 uMVPMatrix;" +
        //vertex information that will be passed in
        "attribute vec4 vPosition;" +
        //colour information that will be passed in
        "attribute vec4 a_Color;\n" +
        //this will be passed to the fragment shader
        "varying vec4 v_Color;\n" +
                
        "void main() {" +
        
            //pass the colour through to the fragment shader
            "v_Color = a_Color\n;" +
            //set the position
        "   gl_Position = uMVPMatrix * vPosition;" +
        "}";

    private final String fragmentShaderCode =
        
        //use medium precision
        "precision mediump float;" +
        //the colour
        "varying vec4 v_Color;" +
                
        "void main() {" +
        
            //set the colour
        "   gl_FragColor = v_Color;" +
        "}";

    //CONSTRUCTOR
    /**Constructs a new 2d quad
    @param crd the co-ordinates of the quad
    @param clr the colour of the quad*/
    public Quad2d(float crd[], float clr[]) {

        //initialise variables
        coords = crd;
        colour = clr;

        //initialise the bye buffer for the vertex buffer (4 = bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        
        //initialise the vertex buffer and insert the coords
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
        
        //initialise the bye buffer for the colour buffer (4 = bytes per float)
        ByteBuffer cb = ByteBuffer.allocateDirect(colour.length * 4);
        cb.order(ByteOrder.nativeOrder());
        
        //initialise the vertex buffer and insert the coords
        colourBuffer = cb.asFloatBuffer();
        colourBuffer.put(colour);
        colourBuffer.position(0);

        //initialise the byte buffer for the draw list (2 = bytes per short)
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        
        //initialise the draw list buffer and insert the draw list
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        //prepare the shaders and the openGL program
        int vertexShader = ShaderUtil.loadShader(GLES20.GL_VERTEX_SHADER,
                                                   vertexShaderCode);
        int fragmentShader = ShaderUtil.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                     fragmentShaderCode);

        //create the openGL program
        program = GLES20.glCreateProgram();
        //attach the vertex shader to the program
        GLES20.glAttachShader(program, vertexShader);
        //attach the fragment shader to the program
        GLES20.glAttachShader(program, fragmentShader);
        //create openGL program executables
        GLES20.glLinkProgram(program);
    }


    @Override
    public void draw(float[] mvpMatrix) {

        //add the program to openGL environment
        GLES20.glUseProgram(program);

        //get handle to vertex shader's position
        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        //enable a handle to vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        //prepare the coord data
        GLES20.glVertexAttribPointer(positionHandle, coordsPerVertex,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        //get a handle to the fragment shader's colour
        int colourHandle = GLES20.glGetAttribLocation(program, "a_Color");
        
        //enable a handle to vertices
        GLES20.glEnableVertexAttribArray(colourHandle);

        //prepare the colour data
        GLES20.glVertexAttribPointer(colourHandle, colValPerVertex,
                                     GLES20.GL_FLOAT, false,
                                     colourStride, colourBuffer);

        //get a handle to shape's transformation matrix
        int mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        //apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        //draw the quad
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                              GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        //disable the vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }


    @Override
    public void setPosition(int v, Vector3d pos) {

        //todo set constant
        coords[v * coordsPerVertex] = pos.getX();
        coords[v * coordsPerVertex + 1] = pos.getY();
        coords[v * coordsPerVertex + 2] = pos.getZ();

        vertexBuffer.put(coords);
        vertexBuffer.position(0);
    }


    @Override
    public void setColour(int v, Vector4d c) {

        //TODO: set constant
        colour[v * colValPerVertex] = c.getX();
        colour[v * colValPerVertex + 1] = c.getY();
        colour[v * colValPerVertex + 2] = c.getZ();
        colour[v * colValPerVertex + 3] = c.getW();

        colourBuffer.put(colour);
        colourBuffer.position(0);
    }

    @Override
    public void setColour(Vector4d c) {

        setColour(0, c);
        setColour(1, c);
        setColour(2, c);
        setColour(3, c);
    }
}
