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

import java.io.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import Polygen.Model.ThreeDProcessing.Kinect.DwMath.DwMat4;
import dLibs.freenect.Kinect;
import dLibs.freenect.Kinect3D;
import dLibs.freenect.KinectFrameDepth;
import dLibs.freenect.KinectFrameVideo;
import dLibs.freenect.KinectTilt;
import dLibs.freenect.constants.DEPTH_FORMAT;
import dLibs.freenect.constants.VIDEO_FORMAT;
import dLibs.freenect.toolbox.KinectCalibration;
import dLibs.freenect.toolbox.KinectLogger;
import dLibs.freenect.toolbox.KinectPoint3D;
import dLibs.freenect.toolbox.KinectTransformation;

import static Polygen.Model.ThreeDProcessing.Kinect.DwBase.DwJOGL.*;

public class DwKinect {
  
  private GL3 gl;
  
  private Kinect kinect;
  private KinectFrameVideo kinect_video;
  private KinectFrameDepth kinect_depth;
  private Kinect3D k3d;
  private KinectTilt kinect_tilt;
  private KinectCalibration calibration_data; 
  private KinectTransformation kinect_transformation;

  private int kinectFrame_size_x = VIDEO_FORMAT._RGB_.getWidth();   
  private int kinectFrame_size_y = VIDEO_FORMAT._RGB_.getHeight();
  
  
  // pointcloud
  public DwKinect_PointCLoudShader shader_pointcloud;
  private int HANDLE_vbo_pointcloud = 0;
  private int pointcloud_elements = 0;
  private FloatBuffer vbo_points = FloatBuffer.allocate(kinectFrame_size_x*kinectFrame_size_y*3 * 2);


  float scale_col = 1/255f;// map color to range: 0-1
  int step = 10;            // only take every nth 3d point.
  float depth_min = -0.f; // negative values, meters
  float depth_max = -1.5f; // negative values, meters


  public DwKinect(GL3 gl) throws FileNotFoundException {
    this.gl = gl;
    
    KinectLogger.TYPE.INFO   .active(!true);
    KinectLogger.TYPE.DEBUG  .active(!true);
    KinectLogger.TYPE.WARNING.active(true);
    KinectLogger.TYPE.ERROR  .active(true);
    KinectLogger.log(KinectLogger.TYPE.INFO, null, "available devices: "+Kinect.count() );
    
    kinect = new Kinect(0);

    kinect_video = new KinectFrameVideo(VIDEO_FORMAT._RGB_);
    kinect_depth = new KinectFrameDepth(DEPTH_FORMAT._11BIT_);
    kinect_tilt  = new KinectTilt(); 
    k3d          = new Kinect3D();
    

    kinect_video.connect(kinect);  
    kinect_depth.connect(kinect); 
    k3d         .connect(kinect);
    kinect_tilt .connect(kinect);
    
    
    k3d.setFrameRate(30);
    kinect_video.setFrameRate(30);
    kinect_depth.setFrameRate(30);
    k3d.mapVideoFrame(true);
    kinect_tilt.setTiltDegrees(10);
    
    calibration_data = new KinectCalibration();
    calibration_data.fromFile("kinect_calibration_red.yml", null); 
    k3d.setCalibration(calibration_data);
    
//    kinect_transformation = new KinectTransformation();
//    kinect_transformation.setS(1,1,1); // scale the untis (meters to centimeters)
//  kinect_transformation.setR(0, (float)Math.PI/2f, 0);  // Z, X, Z - http://en.wikipedia.org/wiki/Euler_angles
//kinect_transformation.setR((float)Math.PI, 0, 0);  // Z, X, Z - http://en.wikipedia.org/wiki/Euler_angles
  
//    kinect_transformation.setT(0, 0, 200);      // X, X, Z - in world space
//    k3d.setTransformation(kinect_transformation);
    
    
    int[] handle = new int[1];
    
    // POINTCLOUD VBO
    gl.glGenBuffers(1, handle, 0);  
    HANDLE_vbo_pointcloud = handle[0];
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, HANDLE_vbo_pointcloud); 
    //gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo_points.capacity()*4, null, GL3.GL_STREAM_DRAW);

    // POINTCLOUD SHADER
    shader_pointcloud = new DwKinect_PointCLoudShader(gl);
  }

  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------
  //            POINTCLOUD
  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ArrayList<float[]> xyzPos;

  public void updatePointCloud(){
    vbo_points.rewind();

    KinectPoint3D[] points = k3d.get3D();

    pointcloud_elements = 0;

    xyzPos = new ArrayList<>();

    for(int y = 0; y < kinectFrame_size_y; y+=step){
      for(int x = 0; x < kinectFrame_size_x; x+=step){
        int idx = y*kinectFrame_size_x+x;
        KinectPoint3D point = points[idx];
        int col = point.getColor();

        if(point.z > depth_min || point.z < depth_max)continue;
        
        vbo_points.put(point.x);
        vbo_points.put(point.y);
        vbo_points.put(point.z);

          float[] xyzPoint = new float[3];
          xyzPoint[0] = point.x*100;
          xyzPoint[1] = point.y*100;
          xyzPoint[2] = point.z*100;
          xyzPos.add(xyzPoint);

        vbo_points.put( ((col >> 16)& 0xFF) *scale_col );
        vbo_points.put( ((col >>  8)& 0xFF) *scale_col );
        vbo_points.put( ((col >>  0)& 0xFF) *scale_col );
      }
    }

    pointcloud_elements = vbo_points.position()/6;
    vbo_points.rewind();
  }

  public ArrayList<float[]> getXYZPos() {
    return xyzPos;
  }

  public void drawPointCloud(float[] Mat4_ModelView, float[] Mat4_Projection ) {
      if (toggleMode) updatePointCloud();

    //  DwMat4.translate_ref_slf(Mat4_ModelView, new float[]{0, 0, 10});
    DwMat4.rotateZ_ref_slf(Mat4_ModelView, (float) Math.PI);
    if (ansicht1)  DwMat4.rotateY_ref_slf(Mat4_ModelView, (float) 0);

    if (ansicht2)  DwMat4.rotateY_ref_slf(Mat4_ModelView, (float) Math.PI/2f);

    if (ansicht3)  DwMat4.rotateY_ref_slf(Mat4_ModelView, (float) Math.PI);

    if (ansicht4) DwMat4.rotateY_ref_slf(Mat4_ModelView, (float) Math.PI * 3 / 2f);

    DwMat4.translate_ref_slf(Mat4_ModelView, new float[]{0,0,100});                      //Pointcloud manuell in den Mittelpunkt verschoben
//  DwMat4.rotateY_ref_slf(Mat4_ModelView, (float) 90);
    DwMat4.scale_ref_slf(Mat4_ModelView, new float[]{100, 100, 100});                    //Skalierung der Pointcloud
//  DwMat4.rotateX_ref_slf(Mat4_ModelView, (float) -Math.PI/2f);



//    DwMat4.translate_ref_slf(Mat4_ModelView, new float[]{0, 0, 10});
//    DwMat4.rotateZ_ref_slf(Mat4_ModelView, (float) Math.PI);
//    DwMat4.scale_ref_slf(Mat4_ModelView, new float[]{100, 100, 100});
//   DwMat4.rotateX_ref_slf(Mat4_ModelView, (float) -Math.PI/2f);

    shader_pointcloud.bind();
    {
      shader_pointcloud.setMat4_ModelView(Mat4_ModelView);
      shader_pointcloud.setMat4_Projection(Mat4_Projection);
      gl.glBindBuffer(GL.GL_ARRAY_BUFFER, HANDLE_vbo_pointcloud);
      gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo_points.capacity() * 4, vbo_points, GL.GL_DYNAMIC_DRAW);
      gl.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, pointcloud_elements * 3 * 4 * 2, vbo_points);

      gl.glVertexAttribPointer(shader_pointcloud.IN_VEC3_POSITION, 3, GL.GL_FLOAT, false, 24, 0);
      gl.glVertexAttribPointer(shader_pointcloud.IN_VEC3_COLOR, 3, GL.GL_FLOAT, false, 24, 12);

      gl.glEnableVertexAttribArray(shader_pointcloud.IN_VEC3_POSITION);
      gl.glEnableVertexAttribArray(shader_pointcloud.IN_VEC3_COLOR );

      gl.glDrawArrays(GL3.GL_POINTS, 0, pointcloud_elements);
      gl.glFlush();
    }

    shader_pointcloud.unbind();
  }


  public int getpointcounter() {
    return pointcloud_elements;
  }


  public void exit(){
    Kinect.shutDown();
  }

}
