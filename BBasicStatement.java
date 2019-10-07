import java.util.Vector; 

/* Copyright K. Lano 2003--2013
   Package: B AMN */ 

public class BBasicStatement extends BStatement
{ private String text = "skip";  // default

  public BBasicStatement(String code)
  { text = code; }

  public String toString() { return text; }

  public BStatement substituteEq(String oldE, BExpression newE)
  { return this; } 

  public BStatement simplify()
  { return this; } 

  public BStatement normalise() 
  { return this; } 

  public BStatement seq2parallel() 
  { return this; } 

  public Vector wr()
  { return new Vector(); } 

  public Vector rd()
  { return new Vector(); } 
}

