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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DwKey implements KeyListener {
  
  private DwJOGL parent;
  KeyEvent e;
  public boolean pressed = false;
  public char key_char = 0;
  
  public DwKey(DwJOGL parent){
    this.parent = parent;
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
    this.e = e;
    key_char = e.getKeyChar();
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
    pressed = true;
    this.e = e;
    key_char = e.getKeyChar();
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      parent.exit();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    pressed = false;
    // TODO Auto-generated method stub
    this.e = e;
    key_char = e.getKeyChar();
    System.out.println("keyReleased: "+key_char);
  }

}
