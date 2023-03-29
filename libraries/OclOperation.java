import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;

class OclOperation 
{ OclOperation() { }

  OclOperation(String nme) 
  { name = nme; }


  String name = "";
  OclType type = null;
  ArrayList<OclAttribute> parameters = new ArrayList<OclAttribute>();

  public String getName()
  { return name; }

  public void setType(OclType t)
  { type = t; } 

  public OclType getType()
  { return type; }


  public OclType getReturnType()
  { return type; }


  public ArrayList<OclAttribute> getParameters()
  { return parameters; } 

}

