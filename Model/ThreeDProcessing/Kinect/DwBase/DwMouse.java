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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DwMouse implements MouseListener{
  public int x, y;
  public int xp, yp;
  private Frame frame;
  
  public boolean onscreen;
  
  private DwJOGL parent;
  
  public MouseEvent e;
  
  
  public boolean pressed = false;
  public int     pressed_button = -1;
  
  public DwMouse(DwJOGL parent){
    this.parent = parent;
    this.frame = DwJOGL.frame;
    
//    this.frame.addMouseListener( this );
  }
  
  public void set(Point p){
    xp = x;
    yp = y;
    if( p == null ){
      x = xp;
      y = yp;
      onscreen = false;
      return;
    }
    x = p.x;
    y = p.y;
    onscreen = true;
  }
  
  public void update(){
    set(frame.getMousePosition() );
  }
  
  public String toString(){
    return "Mouse: "+x+","+y+" onscreen="+onscreen;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
//    System.out.println("mouseClicked");
 
  }

  @Override
  public void mousePressed(MouseEvent e) {
//    System.out.println("mousePressed");
    this.e = e;
    pressed = true;
    pressed_button = e.getButton();

  }

  @Override
  public void mouseReleased(MouseEvent e) {
//    System.out.println("mouseReleased");
    this.e = e;
    pressed = false;
    pressed_button = -1;
  }

  @Override
  public void mouseEntered(MouseEvent e) {
//    System.out.println("mouseEntered");
    this.e = e;

  }

  @Override
  public void mouseExited(MouseEvent e) {
//    System.out.println("mouseExited");
    this.e = e;
  }
}
