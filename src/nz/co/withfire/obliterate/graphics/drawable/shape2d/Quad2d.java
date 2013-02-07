/**********************\
| A 2 dimensional quad |
| 					   |
| @author David Saxon  |
\**********************/
package nz.co.withfire.obliterate.graphics.drawable.shape2d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import nz.co.withfire.obliterate.graphics.ShaderUtil;
import android.opengl.GLES20;

public class Quad2d implements Shape2d {

	//VARIABLES
	//4 bytes per vertex
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    //the order to draw the vertices
    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
	//the vertex buffer
    private final FloatBuffer vertexBuffer;
    //the draw list buffer
    private final ShortBuffer drawListBuffer;
    //the opengl program
    private final int program;
    //the position handle
    private int positionHandle;
    //the colour handle
    private int colourHandle;
    //the model, view, projection matrix handle
    private int mvpMatrixHandle;
    
    //the coords of the quad
    private float coords[];
    //the colour of the quad
    private float colour[];
    
    //shaders
    private final String vertexShaderCode =
    		
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        		
        "void main() {" +
        
        "	gl_Position = vPosition * uMVPMatrix;" +
        "}";

    private final String fragmentShaderCode =
    		
        "precision mediump float;" +
        "uniform vec4 vColour;" +
        		
        "void main() {" +
        
        "	gl_FragColor = vColour;" +
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
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        //enable a handle to vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        //prepare the coord data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        //get a handle to the fragment shader's colour
        colourHandle = GLES20.glGetUniformLocation(program, "vColour");

        //Set the colour for drawing
        GLES20.glUniform4fv(colourHandle, 1, colour, 0);

        //get a handle to shape's transformation matrix
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        //apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        //TODO: GLES20.GL_QUADS
        //TODO: or other programs way of drawing
        //draw the quad
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                              GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        //disable the vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
	}


	@Override
	public void setVertex(int v, float x, float y, float z) {
		
		coords[v * 3] = x;
		coords[v * 3 + 1] = y;
		coords[v * 3 + 2] = z;
		
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
	}


	@Override
	public void setColour(float r, float g, float b, float a) {
		
		float clr[] = {r, g, b, a};
		
		colour = clr;
	}
}