/*********************\
| A textured 2d quad  |
|                     |
| @author David Saxon |
\*********************/

package nz.co.withfire.obliterate.graphics.drawable.shape2d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import nz.co.withfire.obliterate.R;
import nz.co.withfire.obliterate.graphics.ShaderLoader;
import nz.co.withfire.obliterate.graphics.TextureLoader;
import android.content.Context;
import android.opengl.GLES20;
import nz.co.withfire.obliterate.utilities.*;

public class QuadTex2d implements Shape2d {

    //VARIABLES
    //the size of a float in bytes
    private final int sizeOfFloat = 4;
    //the number of position coords per vertex
    private final int coordsPerVertex = 3;
    //the number of colour values per vertex
    private final int colValPerVertex = 4;
    //the number of values per texture coord
    private final int valPerTex = 2;
    //the stride of a vertex
    private final int vertexStride = coordsPerVertex * sizeOfFloat;
    //the stride of a colour
    private final int colourStride = colValPerVertex * sizeOfFloat;
    //the stride of a texture
    private final int textStride = valPerTex * sizeOfFloat;
    //the order to draw the vertices
    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    //the vertex buffer
    private final FloatBuffer vertexBuffer;
    //the colour buffer
    private final FloatBuffer colourBuffer;
    //the texture buffer
    private final FloatBuffer texBuffer;
    //the draw list buffer
    private final ShortBuffer drawListBuffer;
    
    //the opengl program
    private final int program;
    //a handle to the texture data
    private int textureHandle;
    
    //the coords of the quad
    private float coords[];
    //the colour of the quad
    private float colour[];
    
    //shaders
    private final String vertexShaderCode =

        //the model view projection matrix
        "uniform mat4 uMVPMatrix;" +
        //vertex information that will be passed in
        "attribute vec4 a_Position;" +
        //colour information that will be passed in
        "attribute vec4 a_Colour;" +
        //texture information that will be passed in
        "attribute vec2 a_texCoord;" +
        
        //colour data that will be passed to the fragment shader
        "varying vec4 v_Colour;\n" +
        //texture data that will be passed to the fragment shader
        "varying vec2 v_texCoord;" +
                
        "void main() {" +
        
            //pass the colour through to the fragment shader
            "v_Colour = a_Colour;" +
            //pass the tex coords through to the fragment shader
            "v_texCoord = a_texCoord;" +
            
            //set the position
        "   gl_Position = uMVPMatrix * a_Position;" +
        "}";

    private final String fragmentShaderCode =
        
        //use medium precision
        "precision mediump float;" +
        
        //the input texture
        "uniform sampler2D u_Texture;" +
        
        //the colour
        "varying vec4 v_Colour;" +
        //the texture
        "varying vec2 v_texCoord;" +
                
        "void main() {" +
        
            //set the colour to the texture
        "   gl_FragColor = texture2D(u_Texture, v_texCoord);" +
        "}";

    //CONSTRUCTOR
    /**Constructs a new 2d quad
    @param crd the co-ordinates of the quad
    @param clr the colour of the quad*/
    public QuadTex2d(float crd[], float clr[], int tex) {

        //initialise variables
        coords = crd;
        colour = clr;
        textureHandle = tex;

        //initialise the bye buffer for the vertex buffer (4 = bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * sizeOfFloat);
        bb.order(ByteOrder.nativeOrder());
        
        //initialise the vertex buffer and insert the coords
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
        
        //initialise the byte buffer for the colour buffer (4 = bytes per float)
        ByteBuffer cb = ByteBuffer.allocateDirect(colour.length * sizeOfFloat);
        cb.order(ByteOrder.nativeOrder());
        
        //initialise the colour buffer and insert the values
        colourBuffer = cb.asFloatBuffer();
        colourBuffer.put(colour);
        colourBuffer.position(0);
        
        //the texture coords
        final float[] texCoords = { 1.0f, 0.0f,
                                    1.0f, 1.0f,
                                    0.0f, 1.0f, 
                                    0.0f, 0.0f,
                                  };
        
        //Initialise the byte buffer for the coords
        ByteBuffer tb = ByteBuffer.allocateDirect(
            texCoords.length * sizeOfFloat);
        tb.order(ByteOrder.nativeOrder());
        
        //initialise the texture buffer and insert the coords
        texBuffer = tb.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);

        //initialise the byte buffer for the draw list (2 = bytes per short)
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        
        //initialise the draw list buffer and insert the draw list
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        //prepare the shaders and the openGL program
        int vertexShader = ShaderLoader.loadShader(GLES20.GL_VERTEX_SHADER,
                                                   vertexShaderCode);
        int fragmentShader = ShaderLoader.loadShader(GLES20.GL_FRAGMENT_SHADER,
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

        //get a handle to the texture
        int textureUniformHandle =
            GLES20.glGetUniformLocation(program, "u_Texture");
        
        //set the active texture unit to texture unit 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        //bind this texture to this unit
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
        
        //tell the uniform shader to use this texture
        GLES20.glUniform1i(textureHandle, 0);
        
        //get handle to vertex shader's position
        int positionHandle = GLES20.glGetAttribLocation(program, "a_Position");

        //enable a handle to vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        //prepare the coord data
        GLES20.glVertexAttribPointer(positionHandle, coordsPerVertex,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        //get a handle to the fragment shader's colour
        int colourHandle = GLES20.glGetAttribLocation(program, "a_Colour");
        
        //enable a handle to vertices
        GLES20.glEnableVertexAttribArray(colourHandle);

        //prepare the colour data
        GLES20.glVertexAttribPointer(colourHandle, colValPerVertex,
                                     GLES20.GL_FLOAT, false,
                                     colourStride, colourBuffer);
        
        //get a handle to the texture data
        int texCoordHandle = GLES20.glGetAttribLocation(program, "a_texCoord");
        
        //pass in the texture information    
        texBuffer.position(0);
        GLES20.glVertexAttribPointer(texCoordHandle, valPerTex, GLES20.GL_FLOAT,
            false, 0, texBuffer);
        GLES20.glEnableVertexAttribArray(texCoordHandle);

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

//        //TODO: set constant
//        colour[v * colValPerVertex] = c.getX();
//        colour[v * colValPerVertex + 1] = c.getY();
//        colour[v * colValPerVertex + 2] = c.getZ();
//        colour[v * colValPerVertex + 3] = c.getW();
//
//        colourBuffer.put(colour);
//        colourBuffer.position(0);
    }

    @Override
    public void setColour(Vector4d c) {

//        setColour(0, c);
//        setColour(1, c);
//        setColour(2, c);
//        setColour(3, c);
    }
}
