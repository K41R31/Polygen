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

public class DwFrameRate {
  private float actual_framerate_ = 60.0f;
  private float forced_framerate_ = 60.0f;
  private long framerate_last_nanos_get_framerate_ = 1;
  private long framerate_last_nanos_set_framerate_ = 1;
  
  
  public DwFrameRate(){
    
  }
  
  public float fps(){
    return actual_framerate_;
  }
  

  public void updateFrameRate() {
    long now = System.nanoTime();
    float time_dif_millis = (now - framerate_last_nanos_get_framerate_);
    framerate_last_nanos_get_framerate_ = now;
    actual_framerate_ = (actual_framerate_ * .9f) + 1E08f / time_dif_millis;
  }

  private final static float framerate_fac = 1f / 1000000f;

  public void forceFrameRate(float set_frame_rate) {
    float time_dif_milliseconds = (System.nanoTime() - framerate_last_nanos_set_framerate_)
        * framerate_fac;
    float waiting_time = (1000 / set_frame_rate) - time_dif_milliseconds;
    float waiting_time_real = waiting_time >= 0 ? waiting_time : 0;
    try {
      Thread.sleep((int) waiting_time_real);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    framerate_last_nanos_set_framerate_ = System.nanoTime();
  }
}
