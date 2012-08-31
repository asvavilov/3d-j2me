import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

public class MIDletMain extends MIDlet {
 static MIDletMain midlet;
 MyCanvas d = new MyCanvas();
 Timer iTimer = new Timer();

 public MIDletMain() {
  this.midlet = this;
 }

 public void startApp() {
  Display.getDisplay(this).setCurrent(d);
  iTimer.schedule( new MyTimerTask(), 0, 40 );
 }

 public void pauseApp() {
 }

 public void destroyApp(boolean unconditional) {
 }


 public static void quitApp() {
  midlet.destroyApp(true);
  midlet.notifyDestroyed();
  midlet = null;
 }


 class MyTimerTask extends TimerTask {
public void run() {
   if( d != null ) {
    d.repaint();
   }
  }
 }
}