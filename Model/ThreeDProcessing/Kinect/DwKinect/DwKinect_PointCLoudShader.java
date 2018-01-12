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




package Polygen.Model.ThreeDProcessing.Kinect.DwKinect;

import com.jogamp.opengl.GL3;

import Polygen.Model.ThreeDProcessing.Kinect.DwShader.DwShader;

public class DwKinect_PointCLoudShader {

  private GL3 gl;
  public DwShader shader;
  
  public int IN_VEC3_POSITION  ;
  public int IN_VEC3_COLOR     ;
  public int UN_MAT4_PROJECTION;
  public int UN_MAT4_MODELVIEW ;
  

  public DwKinect_PointCLoudShader(GL3 gl){
    this.gl = gl;

    String path = "Polygen/Model/ThreeDProcessing/Kinect/GLSL_SHADER/";
    System.out.println(path);
    shader = new DwShader(gl, path, "KinectPointCloud_vert.glsl", "KinectPointCloud_frag.glsl");

    gl.glUseProgram(shader.HANDLE_program);
    {
      this.IN_VEC3_POSITION   = gl.glGetAttribLocation (this.shader.HANDLE_program, "IN_VEC3_POSITION");
      this.IN_VEC3_COLOR      = gl.glGetAttribLocation (this.shader.HANDLE_program, "IN_VEC3_COLOR");
      this.UN_MAT4_PROJECTION = gl.glGetUniformLocation(this.shader.HANDLE_program, "UN_MAT4_PROJECTION");
      this.UN_MAT4_MODELVIEW  = gl.glGetUniformLocation(this.shader.HANDLE_program, "UN_MAT4_MODELVIEW");
    }
    gl.glUseProgram(0);
    
    System.out.println("attribute location: IN_VEC3_POSITION   = "+this.IN_VEC3_POSITION);
    System.out.println("attribute location: IN_VEC3_COLOR      = "+this.IN_VEC3_COLOR);
    System.out.println("uniform   location: UN_MAT4_PROJECTION = "+this.UN_MAT4_PROJECTION);
    System.out.println("uniform   location: UN_MAT4_MODELVIEW  = "+this.UN_MAT4_MODELVIEW);
  }


  public void setMat4_Projection(float[] MAT4){
    this.gl.glUniformMatrix4fv(this.UN_MAT4_PROJECTION, 1, false, MAT4, 0);
  }


  
  public void setMat4_ModelView(float[] MAT4){
    this.gl.glUniformMatrix4fv( this.UN_MAT4_MODELVIEW, 1, false, MAT4, 0);
  }



  public void bind(){
    this.gl.glUseProgram(this.shader.HANDLE_program);
  }

  public void unbind(){
    this.gl.glUseProgram(0);
  }
}

