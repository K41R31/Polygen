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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

import com.jogamp.opengl.GL3;

public class SHADER_INFO {
  
  
  public static void getProgramInfoLog(GL3 gl, int program_id) {
    IntBuffer infoLogLength = IntBuffer.allocate(1);
  
    gl.glGetProgramiv(program_id, GL3.GL_INFO_LOG_LENGTH, infoLogLength);

    ByteBuffer infoLog = ByteBuffer.allocate(infoLogLength.get(0));
    gl.glGetProgramInfoLog(program_id, infoLogLength.get(0), null, infoLog);

    String infoLogString = Charset.forName("US-ASCII").decode(infoLog).toString();
    System.out.println(infoLogString);
    // throw new Error("Program compile error\n" + infoLogString);
  }

  public static void getShaderInfoLog(GL3 gl, int shader_id) {
    IntBuffer infoLogLength = IntBuffer.allocate(1);
    gl.glGetShaderiv(shader_id, GL3.GL_INFO_LOG_LENGTH, infoLogLength);

    ByteBuffer infoLog = ByteBuffer.allocate(infoLogLength.get(0));
    gl.glGetShaderInfoLog(shader_id, infoLogLength.get(0), null, infoLog);

    String infoLogString = Charset.forName("US-ASCII").decode(infoLog).toString();
    System.out.println(infoLogString);
  }
}
