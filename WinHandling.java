/*
      * Classname : WinHandling
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class describes the creation of a new window 
      * (statechart diagram). It consists of methods that are responsible for
      * closing and other functions that can be executed on the new window 
      * (Frame).
      */ 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import javax.swing.JOptionPane;

// import java.awt.*;
import java.awt.event.*;

public class WinHandling extends WindowAdapter 
{
  // Counts the number of windows created
  public int numWindows = 0; 	       
  
  //calculates the last location where the window was create
  private java.awt.Point lastLocation = null;    
  
  //Maximum coordinates (x,y) where the windows created must be at locations < this (x,y)	 
  private int maxX = 500;			
  private int maxY = 500;

  StateWin state_window; 
  
  public WinHandling() 
  {
    //Initialise the maximum coordinate depending on the screen size
    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    maxX = screenSize.width - 50; 
    maxY = screenSize.height - 50; 
    
    //Create a new window
    //   makeNewWindow(); only create when chosen !
  } 
  
  public void makeNewWindow() 
  {
    state_window = new StateWin("Statechart" + numWindows,null);
    
    state_window.setTitle("Statechart" + numWindows);
    state_window.setSize(300, 300);
    
    numWindows++;
    System.out.println("Number of windows: " + numWindows);
    
    if (lastLocation != null) {
      //Move the window over and down 40 pixels.
      lastLocation.translate(40, 40);
      if ((lastLocation.x > maxX) || (lastLocation.y > maxY)) {
	lastLocation.setLocation(0, 0);
      }
      state_window.setLocation(lastLocation);
    } else {
      lastLocation = state_window.getLocation();
    }
    
    System.out.println("Frame location: " + lastLocation);
    state_window.setVisible(true);
  }

  public Statemachine makeNewWindow(String s, SmBuilder builder) 
  { state_window = new StateWin(s,builder);

    state_window.setTitle(s);
    state_window.setSize(300, 300);

    numWindows++;
    System.out.println("Number of windows: " + numWindows);

    if (lastLocation != null) {
      //Move the window over and down 40 pixels.
      lastLocation.translate(40, 40);
      if ((lastLocation.x > maxX) || (lastLocation.y > maxY)) {
        lastLocation.setLocation(0, 0);
      }
      state_window.setLocation(lastLocation);
    } else {
      lastLocation = state_window.getLocation();
    }

    System.out.println("Frame location: " + lastLocation);
    state_window.setVisible(true);

    return state_window.getDrawArea().module; 
  }

    
  
  //This method must be evoked from the event-dispatching thread.
  /*  public void quit(JFrame frame) {
      if (quitConfirmed(frame)) {
      System.out.println("Quitting.");
      System.exit(0);
      }
      System.out.println("Quit operation not confirmed; staying alive.");
      }
      
      private boolean quitConfirmed(JFrame frame) {
      String s1 = "Quit";
      String s2 = "Cancel";
      Object[] options = {s1, s2};
      int n = JOptionPane.showOptionDialog(frame,
      "Windows are still open.\nDo you really want to quit?",
                "Quit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                s1);
		if (n == JOptionPane.YES_OPTION) {
		return true;
		} else {
		return false;
		}
    }*/
  
    public void windowClosed(WindowEvent e) {
      numWindows--;
      System.out.println("Number of windows = " + numWindows);
      if (numWindows <= 0) 
	{
	System.out.println("All windows gone.  Bye bye!");
	System.exit(0);
	}
    }
  
}

