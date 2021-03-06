/*********************\
| Methods for shaders |
|                     |
| @author David Saxon |
\*********************/

package nz.co.withfire.obliterate.graphics;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderLoader {

    /**Loads shader code
    @param type the type of shader
    @param shaderCode the shader code to load
    @return a int mapping to the loaded shader code*/
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}