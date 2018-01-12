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




package Polygen.Model.ThreeDProcessing.Kinect.DwGeometryTemplates;



import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;


public class DwXYZaxis{
  GL3 gl;
  int HANDLE_vbo;
 
  public DwXYZaxis(GL3 gl, int size){
    this.gl = gl;
    int s = size;
    float[] vertices =
      {
        // vertex 0
        0.0f, 0.0f, 0.0f, // x-axis - start
        1.0f, 1.0f, 1.0f, // x-axis - start - color

        // vertex 1
           s, 0.0f, 0.0f, // x-axis - end
        1.0f, 0.0f, 0.0f, // x-axis - end - color

        // vertex 2
        0.0f, 0.0f, 0.0f, // y-axis - start
        1.0f, 1.0f, 1.0f, // y-axis - start - color

        // vertex 3
        0.0f,    s, 0.0f, // y-axis - end
        0.0f, 1.0f, 0.0f, // y-axis - end - color

        // vertex 4
        0.0f, 0.0f, 0.0f, // z-axis - start
        1.0f, 1.0f, 1.0f, // z-axis - start - color

        // vertex 5
        0.0f, 0.0f,    s, // z-axis - end
        0.0f, 0.0f, 1.0f  // z-axis - end - color
      };
    
    FloatBuffer vbo_vertices = FloatBuffer.wrap(vertices);


    int[] handle = new int[1];
    gl.glGenBuffers(1, handle, 0);  
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, handle[0]); 
    gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo_vertices.capacity()*4, vbo_vertices, GL.GL_STATIC_DRAW);
    HANDLE_vbo = handle[0];
  }


  public void draw(DwXYZaxis_Shader shader){
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, this.HANDLE_vbo);
  
    gl.glVertexAttribPointer(shader.IN_VEC3_POSITION, 3, GL.GL_FLOAT, false, 24,  0 );
    gl.glVertexAttribPointer(shader.IN_VEC3_COLOR,    3, GL.GL_FLOAT, false, 24, 12 );
 
    gl.glEnableVertexAttribArray(shader.IN_VEC3_POSITION );
    gl.glEnableVertexAttribArray(shader.IN_VEC3_COLOR );
  
    gl.glDrawArrays(GL.GL_LINES, 0, 6 );
    gl.glFlush();
  }


}

