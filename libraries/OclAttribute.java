import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;

class OclAttribute {

  OclAttribute() { } 
 
  OclAttribute(String nme) 
  { name = nme; }

  static OclAttribute createOclAttribute() 
  { OclAttribute result = new OclAttribute();
    return result; 
  }

  public void setType(OclType t)
  { type = t; } 

  String name = "";
  OclType type = null;

  public String getName()
  { return name; }

  public OclType getType()
  { return type; }

}

