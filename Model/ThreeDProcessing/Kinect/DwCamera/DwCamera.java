/**
 * 
 *   author: (c)thomas diewald, http://thomasdiewald.com/
 *   date: 23.04.2012
 *   
 *   modified version of PeasyCam (Processing Library by Jonathan Feinberg)
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





package Polygen.Model.ThreeDProcessing.Kinect.DwCamera;


import java.awt.event.MouseEvent;

import Polygen.Model.ThreeDProcessing.Kinect.DwBase.DwJOGL;
import Polygen.Model.ThreeDProcessing.Kinect.DwBase.DwMouse;
import Polygen.Model.ThreeDProcessing.Kinect.DwCamera.org.apache.commons.math.geometry.Rotation;
import Polygen.Model.ThreeDProcessing.Kinect.DwCamera.org.apache.commons.math.geometry.Vector3D;
import Polygen.Model.ThreeDProcessing.Kinect.DwMath.DwMat4;



public class DwCamera {
	private static final Vector3D LOOK = Vector3D.plusK;
//	private static final Vector3D LOOK = Vector3D.minusK;
	private static final Vector3D UP = Vector3D.plusJ;

	private final double startDistance;
	private final Vector3D startCenter;

	private double minimumDistance = 1;
	private double maximumDistance = Double.MAX_VALUE;

	private final DampedAction rotateX, rotateY, rotateZ; 
	private final DampedAction dampedZoom;
	private final DampedAction dampedPanX, dampedPanY;

	private double distance;
	private Vector3D center;
	private Rotation rotation;

	
	private DwJOGL parent;
	
  private float[] Mat4_ModelView  = new float[16];
  private float[] Mat4_Projection = new float[16];


	public DwCamera(DwJOGL parent, final double lookAtX, final double lookAtY,final double lookAtZ, final double distance) {
	  this.parent = parent;
		this.startCenter = this.center = new Vector3D(lookAtX, lookAtY, lookAtZ);
		this.startDistance = this.distance = distance;
		this.rotation = new Rotation();

		final DwCamera _this = this;
		rotateX = new DampedAction() {
			@Override
			protected void behave(final double velocity) {
				rotation = rotation.applyTo(new Rotation(Vector3D.plusI, velocity));
			}
		};

		rotateY = new DampedAction() {
			@Override
			protected void behave(final double velocity) {
				rotation = rotation.applyTo(new Rotation(Vector3D.plusJ, velocity));
			}
		};

		rotateZ = new DampedAction() {
			@Override
			protected void behave(final double velocity) {
				rotation = rotation.applyTo(new Rotation(Vector3D.plusK, velocity));
			}
		};

		dampedZoom = new DampedAction() {
			@Override
			protected void behave(final double velocity) {
		    double dist = _this.distance + velocity * Math.log1p(_this.distance);
		    _this.distance = Math.min(maximumDistance, Math.max(minimumDistance, dist));
			}
		};

		dampedPanX = new DampedAction() {
			@Override
			protected void behave(final double velocity) {
		    final double panScale = Math.sqrt(distance * .005);
//		    final double panScale = distance * .005;
  	    double dx = -velocity * panScale;
  	    center = center.add(rotation.applyTo(new Vector3D(dx, 0, 0)));
			}
		};

		dampedPanY = new DampedAction() {
			@Override
			protected void behave(final double velocity) {
        final double panScale = Math.sqrt(distance * .005);
//			  final double panScale = distance * .005;
        double dy = -velocity * panScale;
        center = center.add(rotation.applyTo(new Vector3D(0, dy, 0)));
			}
		};
	}



  public void update(){
    DwMouse mouse = parent.mouse;
//    if( mouse.e == null )
//      return;

    float pmouseX = mouse.xp;
    float pmouseY = -mouse.yp;
    float mouseX = mouse.x;
    float mouseY = -mouse.y;
    float width  = parent.viewport_width;
    float height = -parent.viewport_height;
//    System.out.println(mouse.e);
    
    int mouseButton = mouse.pressed_button;
    boolean pressed = mouse.pressed;
    
    double dx = mouseX - pmouseX;
    double dy = mouseY - pmouseY;
    



    if (pressed && mouseButton == MouseEvent.BUTTON2 ) {
      dx *= .2;
      dy *= .2;
      dampedPanX.impulse(dx);
      dampedPanY.impulse(dy );
    } else if (pressed && mouseButton == MouseEvent.BUTTON1) {
      final Vector3D u = LOOK.scalarMultiply(100 + .6 * startDistance).negate();
      dx *= .3;
      dy *= .3;
      final int xSign = dx > 0 ? -1 : 1;
      final int ySign = dy < 0 ? -1 : 1;
      
      final double rho_x = Math.abs((width  / 2d) - mouseX) / (width  / 2d);
      final double rho_y = Math.abs((height / 2d) - mouseY) / (height / 2d);
      
      {
        final double adz = Math.abs(dy) * rho_x;
        final double ady = Math.abs(dy) * (1 - rho_x);
        
        final Vector3D vy = u.add(new Vector3D(0, ady, 0));
        final Vector3D vz = u.add(new Vector3D(0, adz, 0));
        rotateX.impulse(Vector3D.angle(u, vy) * ySign);
        rotateZ.impulse(Vector3D.angle(u, vz) * -ySign * (mouseX < width / 2 ? -1 : 1));
      }

      {
        final double adz = Math.abs(dx) * rho_y;
        final double adx = Math.abs(dx) * (1 - rho_y);
        
        final Vector3D vx = u.add(new Vector3D(adx, 0, 0));
        final Vector3D vz = u.add(new Vector3D(0, adz, 0));
        rotateY.impulse(Vector3D.angle(u, vx) * xSign);
        rotateZ.impulse(Vector3D.angle(u, vz) * xSign* (mouseY > height / 2 ? -1 : 1));
      }
    } else if (pressed && mouseButton == MouseEvent.BUTTON3) {
      dy *= -.1;
      dampedZoom.impulse(dy);
    }
    
    dampedZoom.draw();
    dampedPanX.draw();
    dampedPanY.draw();
    rotateX.draw();
    rotateY.draw();
    rotateZ.draw();
    
    updateModelViewMatrix();
  }
  
  public void updateModelViewMatrix(){
    final Vector3D pos = rotation.applyTo(LOOK).scalarMultiply(distance).add(center);
    final Vector3D rup = rotation.applyTo(UP);
    float[] vec_eye    = { (float)   pos.getX(), (float)   pos.getY(), (float)   pos.getZ()};
    float[] vec_center = { (float)center.getX(), (float)center.getY(), (float)center.getZ()};
    float[] vec_up     = { (float)   rup.getX(), (float)   rup.getY(), (float)   rup.getZ()};
    DwMat4.lookAt_ref(vec_eye, vec_center, vec_up, Mat4_ModelView);
//    DwMat4.scale_ref_slf(Mat4_ModelView, new float[]{1, -1, 1});
  }
   
   
//  public void UPDATE_VIEW(PApplet p) {
//    final Vector3D pos = rotation.applyTo(LOOK).scalarMultiply(distance).add(center);
//    final Vector3D rup = rotation.applyTo(UP);
//    p.camera((float)   pos.getX(), (float)   pos.getY(), (float)   pos.getZ(),
//             (float)center.getX(), (float)center.getY(), (float)center.getZ(),
//             (float)   rup.getX(), (float)   rup.getY(), (float)   rup.getZ());
//  }
  
  public float[] getMat4_ModelView(){
    return Mat4_ModelView;
  }
  public void setMat4_ModelView(float[] Mat4_ModelView){
    this.Mat4_ModelView = Mat4_ModelView;
  }
  public float[] getMat4_Projection(){
    return Mat4_Projection;
  }
  public void setMat4_Projection(float[] Mat4_Projection){
    this.Mat4_Projection = Mat4_Projection;
  }
  
  
  
  
  abstract public class DampedAction {
    private double velocity;
    final double damping;

    public DampedAction() {
      this.velocity = 0;
      this.damping = 0.74;
    }
    public void impulse(final double impulse) {
      velocity += impulse;
    }
    public void draw() {
      if (velocity == 0) {
        return;
      }
      behave(velocity);
      velocity *= damping;
      if (Math.abs(velocity) < .001) {
        velocity = 0;
      }
    }

    public void stop() {
      velocity = 0;
    }
    abstract protected void behave(final double velocity);
  }



}
