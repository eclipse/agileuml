import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.*;
import java.util.StringTokenizer;

public class GUI extends JFrame implements ActionListener
{ JPanel panel = new JPanel();
  JPanel tPanel = new JPanel();
  JPanel cPanel = new JPanel();
  Controller cont = Controller.inst();
  JButton loadModelButton = new JButton("loadModel");
  JButton saveModelButton = new JButton("saveModel");
  JButton loadXmiButton = new JButton("loadXmi");
  JButton loadCSVButton = new JButton("loadCSVs");
  JButton saveCSVButton = new JButton("saveCSVs");
  JButton testprimesButton = new JButton("testprimes");

 public GUI()
  { super("Select use case to execute");
    panel.setLayout(new BorderLayout());
    panel.add(tPanel, BorderLayout.NORTH);
    panel.add(cPanel, BorderLayout.CENTER);
    setContentPane(panel);
    addWindowListener(new WindowAdapter() 
    { public void windowClosing(WindowEvent e)
      { System.exit(0); } });
  tPanel.add(loadModelButton);
  loadModelButton.addActionListener(this);
  tPanel.add(saveModelButton);
  saveModelButton.addActionListener(this);
  tPanel.add(loadXmiButton);
  loadXmiButton.addActionListener(this);
  tPanel.add(loadCSVButton);
  loadCSVButton.addActionListener(this);
  tPanel.add(saveCSVButton);
  saveCSVButton.addActionListener(this);
  cPanel.add(testprimesButton);
  testprimesButton.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e)
  { if (e == null) { return; }
    String cmd = e.getActionCommand();
    if ("loadModel".equals(cmd))
    { Controller.loadModel("in.txt");
      cont.checkCompleteness();
      System.err.println("Model loaded");
      return; } 
    if ("saveModel".equals(cmd))
    { cont.saveModel("out.txt");  
      cont.saveXSI("xsi.txt"); 
      return; } 
    if ("loadXmi".equals(cmd))
    { cont.loadXSI();  
      cont.checkCompleteness();
      System.err.println("Model loaded");
      return; } 
    if ("loadCSVs".equals(cmd))
    { Controller.loadCSVModel();
      System.err.println("Model loaded");
      return; } 
    if ("saveCSVs".equals(cmd))
    { cont.saveCSVModel();  
      return; } 
    if ("testprimes".equals(cmd))
    {  cont.testprimes() ;  return; } 
  }

  public static void main(String[] args)
  {  GUI gui = new GUI();
    gui.setSize(400,400);
    gui.setVisible(true);
  }
 }
