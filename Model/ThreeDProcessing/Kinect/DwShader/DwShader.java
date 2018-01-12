/**
 * 
 *   author: (c)thomas diewald, http://thomasdiewald.com/
 *   date: 23.04.2012
 *   
 *
 * This source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is available on the World
 * Wide Web at <http://www.gnu.org/copyleft/gpl.html>. You can also
 * obtain it by writing to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */




package Polygen.Model.ThreeDProcessing.Kinect.DwShader;


import com.jogamp.opengl.GL3;
import Polygen.Model.ThreeDProcessing.Kinect.DwUtils.HELPER;


public class DwShader {

  private GL3 gl;
  private String path = "";
  private String vert_shader = "";
  private String frag_shader = "";

  
  public int HANDLE_program;
  public int HANDLE_vert;
  public int HANDLE_frag;
  public DwShader(GL3 gl, String path, String vertex_shader, String fragment_shader) {
    this.gl = gl;
    this.path = path;
    this.vert_shader = path + vertex_shader;
    this.frag_shader = path + fragment_shader;
    loadShaders();
  }
  
  public void begin(){
    gl.glUseProgram(HANDLE_program);
  }
  public void end(){
    gl.glUseProgram(0);
  }

  
  boolean loadShaders() {
    HANDLE_vert = createShader(vert_shader, GL3.GL_VERTEX_SHADER, !true);
    HANDLE_frag = createShader(frag_shader, GL3.GL_FRAGMENT_SHADER, !true);

    HANDLE_program = createShaderProgram(HANDLE_vert, HANDLE_frag);
    // gl.glValidateProgram(shader_program);

    SHADER_INFO.getProgramInfoLog(gl, HANDLE_program);
//    SHADER_INFO.getShaderInfoLog (gl, HANDLE_frag);
    
//    checkStatus("(vertex) CompileStatus:", GL2.GL_COMPILE_STATUS, vert_id);
    // checkStatus( "(vertex) CompileStatus:", GL2.GL_COMPILE_STATUS, frag_id );
    // checkStatus( "(vertex) CompileStatus:", GL2.GL_VALIDATE_STATUS, shader_program );
    return true;
  }
  
  
  
  private int createShaderProgram(int vert_id, int frag_id) {
    int shader_program_id = gl.glCreateProgram();
    gl.glAttachShader(shader_program_id, vert_id);
    gl.glAttachShader(shader_program_id, frag_id);
    gl.glLinkProgram(shader_program_id);
    return shader_program_id;
  }

  private int createShader(String path, int shader_type, boolean print_shader_content) {
    StringBuffer content_tmp = HELPER.readASCIIfile(path);
    if (content_tmp == null)
      return -1;

    String content = content_tmp.toString();
    
    if (print_shader_content)
      System.out.println(content);

    int id = gl.glCreateShader(shader_type);
    
    gl.glShaderSource(id, 1, new String[] { content }, (int[]) null, 0);
    gl.glCompileShader(id);
    return id;
  }



//  private void checkStatus(String title, int status, int obj) {
//    IntBuffer iVal = IntBuffer.allocate(1);
//    gl.glGetObjectParameterivARB(obj, status, iVal);
//
//    int length = iVal.get();
//    System.out.println(length);
//    if (length < 1) {
//      System.err.println("(ShaderGLSL)  Error occured with object parameter: "+ length);
//      // return;
//    }
//
//    if (status == GL2.GL_COMPILE_STATUS) {
//      if (length == 0) {
//        System.err.println("(ShaderGLSL)  Info Log of Shader Object ID: " + obj);
//        System.out.println("(ShaderGLSL)  --------------------------");
//        System.err.println("(ShaderGLSL)  There was a problem compiling the shader");
//        System.out.println("(ShaderGLSL)  --------------------------");
//        return;
//      } else {
//        System.out.println("(ShaderGLSL)  Info Log of Shader Object ID: " + obj);
//        System.out.println("(ShaderGLSL)  --------------------------");
//        System.out.println("(ShaderGLSL)  Shader was compiled sucessfully!");
//        System.out.println("(ShaderGLSL)  --------------------------");
//        return;
//      }
//    }
//
//    ByteBuffer infoLog = ByteBuffer.allocate(length);
//    iVal.flip();
//    gl.glGetInfoLogARB(obj, length, iVal, infoLog);
//    byte[] infoBytes = new byte[length];
//    infoLog.get(infoBytes);
//    System.out.println("(ShaderGLSL)  Info Log of Shader Object ID: " + obj);
//    System.out.println("(ShaderGLSL)  --------------------------");
//    System.out.println("(ShaderGLSL)  " + title + " : " + new String(infoBytes));
//    System.out.println("(ShaderGLSL)  --------------------------");
//  }

}
