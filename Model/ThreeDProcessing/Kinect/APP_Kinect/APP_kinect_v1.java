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

        package Polygen.Model.ThreeDProcessing.Kinect.APP_Kinect;

        import com.jogamp.opengl.GL;

        import com.jogamp.opengl.GL3;
        import com.jogamp.opengl.GLAutoDrawable;

        import Polygen.Model.ThreeDProcessing.Kinect.DwBase.DwJOGL;
        import Polygen.Model.ThreeDProcessing.Kinect.DwCamera.DwCamera;
        import Polygen.Model.ThreeDProcessing.Kinect.DwGeometryTemplates.DwXYZaxis;
        import Polygen.Model.ThreeDProcessing.Kinect.DwGeometryTemplates.DwXYZaxis_Shader;
        import Polygen.Model.ThreeDProcessing.Kinect.DwKinect.DwKinect;
        import Polygen.Model.ThreeDProcessing.Kinect.DwMath.DwMat4;
        import com.jogamp.opengl.util.gl2.GLUT;
        import java.io.FileNotFoundException;
        import java.util.Arrays;

 public class APP_kinect_v1 extends DwJOGL {

  public GL3 gl;
  public DwCamera cam;

  public float[] mat_specular = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
  public float[] mat_shininess = new float[]{100.0f};
  public float[] mat_ambient = new float[]{.2f, .2f, .2f, 1.0f};
  public float[] mat_diffuse = new float[]{.8f, .8f, .8f, 1.0f};

  public float[] light_position0 = {0.0f, 2.0f, 0.0f, 0.0f};
  public float[] light_position1 = {2.0f, 0.0f, 0.0f, 0.0f};

  DwXYZaxis_Shader xyz_axis_shader;
  DwXYZaxis xyz_axis;

  DwKinect kinect;

  public void preDisplaySettings() {
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthMask(true);
    gl.glClearDepth(1.0f);
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
//    gl.glEnable(GL2.GL_CULL_FACE);
  }

  @Override
  public void display(GLAutoDrawable gLDrawable) {
    preDisplaySettings();
    mouse.update();
    cam.update();

    int i;

    //Ausgaben verschiedener Daten

    draw();
    if (key.pressed && key.key_char == 'f') printFPS() ; //FPS Ausgeben
    if (key.pressed && key.key_char == 'g') System.out.println("Anzahl der Punkte: " + kinect.getpointcounter());
    if (key.pressed && key.key_char == 's') {
        float[] nuller = {0.0f, 0.0f, 0.0f};
        for (i = 0; i < kinect.getXYZPos().size()-1 ; i++) {
            if (!Arrays.equals(kinect.getXYZPos().get(i), nuller))
                System.out.println("Pointcloud " +kinect.getXYZPos().size()+ " " +Arrays.toString(kinect.getXYZPos().get(i)));
        }
    }
    gl.glFlush();
    gl.glFinish();

    String name = this.getClass().getName();
    String fps = "fps: " + this.animator.getTotalFPS();
    this.frame.setTitle(name + " | " + fps);
  }




  //----------------------------------------------------------------------------
  //------------------------< DRAW >--------------------------------------------
  //----------------------------------------------------------------------------
  public void draw() {
    gl.glClearColor(0, 0, 0, 1);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    float[] Mat4_ModelView = cam.getMat4_ModelView();
    float[] Mat4_Projection = cam.getMat4_Projection();
   // DwMat4.print(Mat4_ModelView, 0);

    xyz_axis_shader.bind();
    {
      gl.glLineWidth(2.0f);
      xyz_axis_shader.setMat4_ModelView(Mat4_ModelView);
      xyz_axis_shader.setMat4_Projection(Mat4_Projection);
      xyz_axis.draw(xyz_axis_shader);
    }
    xyz_axis_shader.unbind();

        kinect.drawPointCloud(Mat4_ModelView, Mat4_Projection);

    }



  //----------------------------------------------------------------------------
  //------------------------< /DRAW >-------------------------------------------
  //----------------------------------------------------------------------------

  @Override
  public void init(GLAutoDrawable gLDrawable) {
    glut = new GLUT();
    gl = gLDrawable.getGL().getGL3();
    getInfoOpenglGL3(gl);
//    getInfoOpenglExtensions(gl);

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LEQUAL);
    gl.glDepthMask(true);
    gl.glClearDepth(1.0f);
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    System.out.println(DwJOGL.viewport_width + ", " + DwJOGL.viewport_height);

    cam = new DwCamera(this, 0, 0, 0, 100);

    xyz_axis = new DwXYZaxis(gl, 10);
    xyz_axis_shader = new DwXYZaxis_Shader(gl);

    try {
      kinect = new DwKinect(gl);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }


  @Override
  public void reshape(GLAutoDrawable gLDrawable) {
    gl.glViewport( 0 , 0 ,viewport_width, viewport_height ) ;
    gl.glDepthRange(0, 1);
    cam.setMat4_Projection(DwMat4.perspective_new(45, DwJOGL.viewport_ratio, 1, 4000));
  }

  public static void main(String[] args) {
    APP_kinect_v1 app = new APP_kinect_v1();
    app.init( app.getClass().getName(), 800, 600);
  }

  @Override
  public void onExit() {
    System.out.println("EXIT");
    kinect.exit();
  }



}