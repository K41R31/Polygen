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




package Polygen.Model.ThreeDProcessing.Kinect.DwBase;

import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class DwJOGL implements GLEventListener {

  public static boolean toggleMode = true;
  public static final GLU glu = new GLU();
  public static GLUT glut;
  public static final GLCanvas canvas   = new GLCanvas();
  public static final Frame    frame    = new Frame("diwi's jogl app");
  public static final Animator animator = new Animator(canvas);

  public static final DwFrameRate FPS = new DwFrameRate();

  public static DwMouse mouse;
  public static DwKey key;

  public static int   viewport_posX   = 100;
  public static int   viewport_posY   = 100;
  public static int   viewport_width  = 100;
  public static int   viewport_height = 100;
  public static float viewport_ratio  = viewport_width/(float)viewport_height;

  public static boolean ansicht1 = true;
  public static boolean ansicht2 = false;
  public static boolean ansicht3 = false;
  public static boolean ansicht4 = false;

  public DwJOGL(){
    canvas.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {

      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()== KeyEvent.VK_S) {
          if (toggleMode) toggleMode = false;
          else toggleMode = true;
        }

        if (e.getKeyCode()== KeyEvent.VK_1) {
          ansicht1 = true;
          ansicht2 = false;
          ansicht3 = false;
          ansicht4 = false;
        }

        if (e.getKeyCode()== KeyEvent.VK_2) {
          ansicht1 = false;
          ansicht2 = true;
          ansicht3 = false;
          ansicht4 = false;
        }

        if (e.getKeyCode()== KeyEvent.VK_3) {
          ansicht1 = false;
          ansicht2 = false;
          ansicht3 = true;
          ansicht4 = false;
        }

        if (e.getKeyCode()== KeyEvent.VK_4) {
          ansicht1 = false;
          ansicht2 = false;
          ansicht3 = false;
          ansicht4 = true;
        }

      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    });
  }

  public void printFPS(){
//  glut.s

    // increasing rotation for the next iteration
//    rotateT += 0.2f;

//    System.out.println("");
//    System.out.println("");
//    FPS.updateFrameRate();
//    //FPS.forceFrameRate(forced_framerate_);
//    System.out.println(FPS.fps());
//    System.out.println("");
//    if( key.pressed && key.key_char == 'f' )
    System.out.println("FPS: "+this.animator.getTotalFPS());

//    System.out.println(this.animator.getLastFPSPeriod());
//    System.out.println(this.animator.getLastFPS());
//    System.out.println(this.animator.getTotalFPSFrames());
  }


  @Override
  public abstract void display(GLAutoDrawable arg0);

  @Override
  public void dispose(GLAutoDrawable arg0) {
  }

  @Override
  public abstract void init(GLAutoDrawable arg0);

  public abstract void reshape(GLAutoDrawable gLDrawable);

  public abstract void onExit();

  public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
    if (height <= 0) {
      height = 1;
    }
    viewport_posX = x;
    viewport_posY = y;
    viewport_width  = width;
    viewport_height = height;
    viewport_ratio  = viewport_width/(float)viewport_height;
    System.out.println("reshape: width/height: "+width+"/"+height);
    reshape(gLDrawable);
  }


  public static boolean ERROR_CHECK(GL3 gl, String debug_note, boolean print_note) {
    int c = gl.glGetError();
    if (print_note) System.out.println("--------------------------<  ERROR_CHECK >--------------------------( "+debug_note+" )");
    boolean has_error = c != GL.GL_NO_ERROR;
    if (has_error )
      System.out.println(glu.gluErrorString(c));
    if (print_note) System.out.println("--------------------------< /ERROR_CHECK >--------------------------");
    return has_error;
  }

  public void exit() {
    onExit();
    animator.stop();
    frame.dispose();
    System.exit(0);
  }


  public void getInfoOpenglGL2(GL2 gl){
//  glGetString(GL_VERSION)

//  The string returned starts with <major version>.<minor version>. Following the minor version, there can be another '.', then a vendor-specific build number. The string may have more content, which is completely vendor-specific (thus not a part of the OpenGL standard).

//  For example, the returned string may be like "2.0.6914 WinXP SSE/SSE2/SSE3/3DNow!". 2.0 is the actual version number of GL supported. 6914 is a driver build number. WinXP is the OS. SSE/SSE2/SSE3/3DNow! are CPU features that the driver can use in case it runs in software mode.

//  Sometimes glGetString(GL_VERSION) also returns also the bus type used, such as AGP or PCI or PCIEx.

//  Alternatively, you can use glGetIntegerv(GL_MAJOR_VERSION, *) and glGetIntegerv(GL_MINOR_VERSION, *). These require GL 3.0 or greater.
//  glGetString(GL_VENDOR)

//  This returns the company name of whoever wrote the GL driver. It could be "ATI Technologies", "NVIDIA Corporation", "INTEL" and so on. Note that there's no guarantee that the string for a specific vendor will remain the same in future implementations. On Windows, if it says "Microsoft" then you are using the Windows software renderer or the Windows Direct3D wrapper. You probably haven't installed the graphics drivers yet in that case.
//  glGetString(GL_RENDERER)

//  This returns the name of the renderer, which often is the name of the GPU. In the case of Mesa, the software renderer, it would be "Mesa" or "MESA". It might even say "Direct3D" if the Windows Direct3D wrapper is being used.
//  glGetString(GL_EXTENSIONS)

    //http://www.opengl.org/wiki/GlGetString


    int version_major[] = new int[1];
    int version_minor[] = new int[1];
    gl.glGetIntegerv(GL2.GL_MAJOR_VERSION, version_major, 0);
    gl.glGetIntegerv(GL2.GL_MINOR_VERSION, version_minor, 0);

    String version    = gl.glGetString(GL2.GL_VERSION);
    String vendor     = gl.glGetString(GL2.GL_VENDOR);
    String renderer   = gl.glGetString(GL2.GL_RENDERER);
    String glsl       = gl.glGetString(GL2.GL_SHADING_LANGUAGE_VERSION);
//    String extensions = gl.glGetString(GL2.GL_EXTENSIONS);

    System.out.println("-------------< OPENGL INFO GL2 >-------------");
    System.out.println("VERSION:    " + version          );
//    System.out.println("VERSION:    " + version_major[0] );
//    System.out.println("VERSION:    " + version_minor[0] );
    System.out.println("VENDOR:     " + vendor           );
    System.out.println("RENDERER:   " + renderer         );
    System.out.println("GLSL:       " + glsl             );
//    System.out.println("EXTENSIONS: " + extensions       );

    System.out.println("-------------< /OPENGLINFO GL2 >-------------");
  }

  public void getInfoOpenglGL3(GL3 gl){
    int version_major[] = new int[1];
    int version_minor[] = new int[1];
    gl.glGetIntegerv(GL3.GL_MAJOR_VERSION, version_major, 0);
    gl.glGetIntegerv(GL3.GL_MINOR_VERSION, version_minor, 0);

    String version    = gl.glGetString(GL3.GL_VERSION);
    String vendor     = gl.glGetString(GL3.GL_VENDOR);
    String renderer   = gl.glGetString(GL3.GL_RENDERER);
    String glsl       = gl.glGetString(GL3.GL_SHADING_LANGUAGE_VERSION);
//    String extensions = gl.glGetString(GL2.GL_EXTENSIONS);

    System.out.println("-------------< OPENGL INFO GL3 >-------------");
    System.out.println("VERSION:    " + version          );
//    System.out.println("VERSION:    " + version_major[0] );
//    System.out.println("VERSION:    " + version_minor[0] );
    System.out.println("VENDOR:     " + vendor           );
    System.out.println("RENDERER:   " + renderer         );
    System.out.println("GLSL:       " + glsl             );
//    System.out.println("EXTENSIONS: " + extensions       );

    System.out.println("-------------< /OPENGLINFO GL3 >-------------");
  }

  public void getInfoOpenglExtensions(GL2 gl){
    String extensions = gl.glGetString(GL2.GL_EXTENSIONS);
    String[] tokens = extensions.split(" ");
    System.out.println("-------------< OPENGL EXTENSIONS INFO >-------------");
    for(int i = 0; i < tokens.length; i++ ){
      System.out.println(i+"\t"+tokens[i]);
    }
    System.out.println("-------------< /OPENGL EXTENSIONS INFO >-------------");
  }


  public void init(String app_title, int width, int height){
    viewport_width  = width;
    viewport_height = height;
    viewport_ratio  = viewport_width/(float)viewport_height;
    System.out.println("init: width/height = "+viewport_width+"/"+viewport_height);

    mouse = new DwMouse(this);
    key   = new DwKey  (this);

    canvas.addGLEventListener(this);
    canvas.addMouseListener(mouse);
    canvas.addKeyListener(key);

    frame.add(canvas);
    frame.setUndecorated(!true);
    frame.pack();

    Insets inset = frame.getInsets ();
//    System.out.println("insets"+inset);
    int frame_size_x = viewport_width   + inset.left   + inset.right;
    int frame_size_y = viewport_height  + inset.bottom + inset.top;

    frame.setSize( frame_size_x, frame_size_y);

//    frame.setPreferredSize( new Dimension(jq.my_screen_width, jq.my_screen_height) );

    // frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        exit();
      }
    });
    frame.setTitle(app_title);
    frame.setVisible(true);
    animator.start();
    canvas.requestFocus();
    animator.setUpdateFPSFrames(1, null);
  }
}