package uml2Ca;


import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import java.util.Collections;

import java.lang.*;
import java.lang.reflect.*;
import java.util.StringTokenizer;
import java.io.*;



abstract class NamedElement
  implements SystemTypes
{
  protected String name = ""; // internal

  public NamedElement()
  {
    this.name = "";

  }



  public String toString()
  { String _res_ = "(NamedElement) ";
    _res_ = _res_ + name;
    return _res_;
  }



  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List namedelements,String val)
  { for (int i = 0; i < namedelements.size(); i++)
    { NamedElement namedelementx = (NamedElement) namedelements.get(i);
      Controller.inst().setname(namedelementx,val); } }


    public String getname() { return name; }

    public static List getAllname(List namedelements)
  { List result = new Vector();
    for (int i = 0; i < namedelements.size(); i++)
    { NamedElement namedelementx = (NamedElement) namedelements.get(i);
      if (result.contains(namedelementx.getname())) { }
      else { result.add(namedelementx.getname()); } }
    return result; }

    public static List getAllOrderedname(List namedelements)
  { List result = new Vector();
    for (int i = 0; i < namedelements.size(); i++)
    { NamedElement namedelementx = (NamedElement) namedelements.get(i);
      result.add(namedelementx.getname()); } 
    return result; }


}


abstract class Relationship
  extends NamedElement
  implements SystemTypes
{

  public Relationship()
  {

  }



  public String toString()
  { String _res_ = "(Relationship) ";
    return _res_ + super.toString();
  }




}


abstract class Feature
  extends NamedElement
  implements SystemTypes
{
  protected boolean isStatic = false; // internal
  protected Type type;
  protected Type elementType;

  public Feature(Type type,Type elementType)
  {
    this.isStatic = false;
    this.type = type;
    this.elementType = elementType;

  }

  public Feature() { }



  public String toString()
  { String _res_ = "(Feature) ";
    _res_ = _res_ + isStatic;
    return _res_ + super.toString();
  }



  public void setisStatic(boolean isStatic_x) { isStatic = isStatic_x;  }


    public static void setAllisStatic(List features,boolean val)
  { for (int i = 0; i < features.size(); i++)
    { Feature featurex = (Feature) features.get(i);
      Controller.inst().setisStatic(featurex,val); } }


  public void settype(Type type_xx) { type = type_xx;
  }

  public static void setAlltype(List features, Type _val)
  { for (int _i = 0; _i < features.size(); _i++)
    { Feature featurex = (Feature) features.get(_i);
      Controller.inst().settype(featurex, _val); } }

  public void setelementType(Type elementType_xx) { elementType = elementType_xx;
  }

  public static void setAllelementType(List features, Type _val)
  { for (int _i = 0; _i < features.size(); _i++)
    { Feature featurex = (Feature) features.get(_i);
      Controller.inst().setelementType(featurex, _val); } }

    public boolean getisStatic() { return isStatic; }

    public static List getAllisStatic(List features)
  { List result = new Vector();
    for (int i = 0; i < features.size(); i++)
    { Feature featurex = (Feature) features.get(i);
      if (result.contains(new Boolean(featurex.getisStatic()))) { }
      else { result.add(new Boolean(featurex.getisStatic())); } }
    return result; }

    public static List getAllOrderedisStatic(List features)
  { List result = new Vector();
    for (int i = 0; i < features.size(); i++)
    { Feature featurex = (Feature) features.get(i);
      result.add(new Boolean(featurex.getisStatic())); } 
    return result; }

  public Type gettype() { return type; }

  public static List getAlltype(List features)
  { List result = new Vector();
    for (int _i = 0; _i < features.size(); _i++)
    { Feature featurex = (Feature) features.get(_i);
      if (result.contains(featurex.gettype())) {}
      else { result.add(featurex.gettype()); }
 }
    return result; }

  public static List getAllOrderedtype(List features)
  { List result = new Vector();
    for (int _i = 0; _i < features.size(); _i++)
    { Feature featurex = (Feature) features.get(_i);
      if (result.contains(featurex.gettype())) {}
      else { result.add(featurex.gettype()); }
 }
    return result; }

  public Type getelementType() { return elementType; }

  public static List getAllelementType(List features)
  { List result = new Vector();
    for (int _i = 0; _i < features.size(); _i++)
    { Feature featurex = (Feature) features.get(_i);
      if (result.contains(featurex.getelementType())) {}
      else { result.add(featurex.getelementType()); }
 }
    return result; }

  public static List getAllOrderedelementType(List features)
  { List result = new Vector();
    for (int _i = 0; _i < features.size(); _i++)
    { Feature featurex = (Feature) features.get(_i);
      if (result.contains(featurex.getelementType())) {}
      else { result.add(featurex.getelementType()); }
 }
    return result; }


}


abstract class Type
  extends NamedElement
  implements SystemTypes
{
  protected String typeId = ""; // internal

  public Type()
  {
    this.typeId = "";

  }



  public String toString()
  { String _res_ = "(Type) ";
    _res_ = _res_ + typeId;
    return _res_ + super.toString();
  }



  public void settypeId(String typeId_x) { typeId = typeId_x;  }


    public static void setAlltypeId(List types,String val)
  { for (int i = 0; i < types.size(); i++)
    { Type typex = (Type) types.get(i);
      Controller.inst().settypeId(typex,val); } }


    public String gettypeId() { return typeId; }

    public static List getAlltypeId(List types)
  { List result = new Vector();
    for (int i = 0; i < types.size(); i++)
    { Type typex = (Type) types.get(i);
      if (result.contains(typex.gettypeId())) { }
      else { result.add(typex.gettypeId()); } }
    return result; }

    public static List getAllOrderedtypeId(List types)
  { List result = new Vector();
    for (int i = 0; i < types.size(); i++)
    { Type typex = (Type) types.get(i);
      result.add(typex.gettypeId()); } 
    return result; }

    public boolean isValueType()
  {   boolean result = false;
 
  if (((String) name).equals("int")) 
  {   result = true;
 
  }  else
      {   if (((String) name).equals("long")) 
  {   result = true;
 
  }  else
      {   if (((String) name).equals("double")) 
  {   result = true;
 
  }  else
      {   if (((String) name).equals("boolean")) 
  {   result = true;
 
  }  else
      if (((String) name).equals("void")) 
  {   result = true;
 
  }   
   } 
   } 
   }     return result;
  }



}


abstract class Classifier
  extends Type
  implements SystemTypes
{

  public Classifier()
  {

  }



  public String toString()
  { String _res_ = "(Classifier) ";
    return _res_ + super.toString();
  }




}


abstract class StructuralFeature
  extends Feature
  implements SystemTypes
{
  protected boolean isReadOnly = false; // internal

  public StructuralFeature()
  {
    this.isReadOnly = false;

  }



  public String toString()
  { String _res_ = "(StructuralFeature) ";
    _res_ = _res_ + isReadOnly;
    return _res_ + super.toString();
  }



  public void setisReadOnly(boolean isReadOnly_x) { isReadOnly = isReadOnly_x;  }


    public static void setAllisReadOnly(List structuralfeatures,boolean val)
  { for (int i = 0; i < structuralfeatures.size(); i++)
    { StructuralFeature structuralfeaturex = (StructuralFeature) structuralfeatures.get(i);
      Controller.inst().setisReadOnly(structuralfeaturex,val); } }


    public boolean getisReadOnly() { return isReadOnly; }

    public static List getAllisReadOnly(List structuralfeatures)
  { List result = new Vector();
    for (int i = 0; i < structuralfeatures.size(); i++)
    { StructuralFeature structuralfeaturex = (StructuralFeature) structuralfeatures.get(i);
      if (result.contains(new Boolean(structuralfeaturex.getisReadOnly()))) { }
      else { result.add(new Boolean(structuralfeaturex.getisReadOnly())); } }
    return result; }

    public static List getAllOrderedisReadOnly(List structuralfeatures)
  { List result = new Vector();
    for (int i = 0; i < structuralfeatures.size(); i++)
    { StructuralFeature structuralfeaturex = (StructuralFeature) structuralfeatures.get(i);
      result.add(new Boolean(structuralfeaturex.getisReadOnly())); } 
    return result; }


}


abstract class DataType
  extends Classifier
  implements SystemTypes
{

  public DataType()
  {

  }



  public String toString()
  { String _res_ = "(DataType) ";
    return _res_ + super.toString();
  }




}


class Enumeration
  extends DataType
  implements SystemTypes
{
  private List ownedLiteral = new Vector(); // of EnumerationLiteral

  public Enumeration()
  {

  }



  public String toString()
  { String _res_ = "(Enumeration) ";
    return _res_ + super.toString();
  }

  public static Enumeration parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Enumeration enumerationx = new Enumeration();
    return enumerationx;
  }


  public void writeCSV(PrintWriter _out)
  { Enumeration enumerationx = this;
    _out.println();
  }


  public void setownedLiteral(List ownedLiteral_xx) { ownedLiteral = ownedLiteral_xx;
    }
 
  public void setownedLiteral(int ind_x, EnumerationLiteral ownedLiteral_xx) { if (ind_x >= 0 && ind_x < ownedLiteral.size()) { ownedLiteral.set(ind_x, ownedLiteral_xx); } }

 public void addownedLiteral(EnumerationLiteral ownedLiteral_xx) { ownedLiteral.add(ownedLiteral_xx);
    }
 
  public void removeownedLiteral(EnumerationLiteral ownedLiteral_xx) { Vector _removedownedLiteralownedLiteral_xx = new Vector();
  _removedownedLiteralownedLiteral_xx.add(ownedLiteral_xx);
  ownedLiteral.removeAll(_removedownedLiteralownedLiteral_xx);
    }

  public static void setAllownedLiteral(List enumerations, List _val)
  { for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerations.get(_i);
      Controller.inst().setownedLiteral(enumerationx, _val); } }

  public static void setAllownedLiteral(List enumerations, int _ind,EnumerationLiteral _val)
  { for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerations.get(_i);
      Controller.inst().setownedLiteral(enumerationx,_ind,_val); } }

  public static void addAllownedLiteral(List enumerations, EnumerationLiteral _val)
  { for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerations.get(_i);
      Controller.inst().addownedLiteral(enumerationx,  _val); } }


  public static void removeAllownedLiteral(List enumerations,EnumerationLiteral _val)
  { for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerations.get(_i);
      Controller.inst().removeownedLiteral(enumerationx, _val); } }


  public static void unionAllownedLiteral(List enumerations, List _val)
  { for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerations.get(_i);
      Controller.inst().unionownedLiteral(enumerationx, _val); } }


  public static void subtractAllownedLiteral(List enumerations,  List _val)
  { for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerations.get(_i);
      Controller.inst().subtractownedLiteral(enumerationx,  _val); } }


  public List getownedLiteral() { return (Vector) ((Vector) ownedLiteral).clone(); }

  public static List getAllownedLiteral(List enumerations)
  { List result = new Vector();
    for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerations.get(_i);
      result = Set.union(result, enumerationx.getownedLiteral()); }
    return result; }

  public static List getAllOrderedownedLiteral(List enumerations)
  { List result = new Vector();
    for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerations.get(_i);
      result.addAll(enumerationx.getownedLiteral()); }
    return result; }

    public void types2c10()
  {   if (Controller.inst().getCEnumerationByPK(this.gettypeId()) != null)
    { CEnumeration t = Controller.inst().getCEnumerationByPK(this.gettypeId());
     Controller.inst().setname(t,this.getname());
        List _enumerationliteral_list60 = new Vector();
    _enumerationliteral_list60.addAll(this.getownedLiteral());
    for (int _ind61 = 0; _ind61 < _enumerationliteral_list60.size(); _ind61++)
    { EnumerationLiteral lit = (EnumerationLiteral) _enumerationliteral_list60.get(_ind61);
      CEnumerationLiteral clit = new CEnumerationLiteral();
    Controller.inst().addCEnumerationLiteral(clit);
    Controller.inst().setname(clit,lit.getname());
    Controller.inst().addownedLiteral(t,clit);
    }

  }
    else
    { CEnumeration t = new CEnumeration();
    Controller.inst().addCEnumeration(t);
    Controller.inst().setctypeId(t,this.gettypeId());
    Controller.inst().setname(t,this.getname());
        List _enumerationliteral_list58 = new Vector();
    _enumerationliteral_list58.addAll(this.getownedLiteral());
    for (int _ind59 = 0; _ind59 < _enumerationliteral_list58.size(); _ind59++)
    { EnumerationLiteral lit = (EnumerationLiteral) _enumerationliteral_list58.get(_ind59);
      CEnumerationLiteral clit = new CEnumerationLiteral();
    Controller.inst().addCEnumerationLiteral(clit);
    Controller.inst().setname(clit,lit.getname());
    Controller.inst().addownedLiteral(t,clit);
    }
 }

  }


}


class EnumerationLiteral
  implements SystemTypes
{
  private String name = ""; // internal

  public EnumerationLiteral()
  {
    this.name = "";

  }



  public String toString()
  { String _res_ = "(EnumerationLiteral) ";
    _res_ = _res_ + name;
    return _res_;
  }

  public static EnumerationLiteral parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    EnumerationLiteral enumerationliteralx = new EnumerationLiteral();
    enumerationliteralx.name = (String) _line1vals.get(0);
    return enumerationliteralx;
  }


  public void writeCSV(PrintWriter _out)
  { EnumerationLiteral enumerationliteralx = this;
    _out.print("" + enumerationliteralx.name);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List enumerationliterals,String val)
  { for (int i = 0; i < enumerationliterals.size(); i++)
    { EnumerationLiteral enumerationliteralx = (EnumerationLiteral) enumerationliterals.get(i);
      Controller.inst().setname(enumerationliteralx,val); } }


    public String getname() { return name; }

    public static List getAllname(List enumerationliterals)
  { List result = new Vector();
    for (int i = 0; i < enumerationliterals.size(); i++)
    { EnumerationLiteral enumerationliteralx = (EnumerationLiteral) enumerationliterals.get(i);
      if (result.contains(enumerationliteralx.getname())) { }
      else { result.add(enumerationliteralx.getname()); } }
    return result; }

    public static List getAllOrderedname(List enumerationliterals)
  { List result = new Vector();
    for (int i = 0; i < enumerationliterals.size(); i++)
    { EnumerationLiteral enumerationliteralx = (EnumerationLiteral) enumerationliterals.get(i);
      result.add(enumerationliteralx.getname()); } 
    return result; }


}


class PrimitiveType
  extends DataType
  implements SystemTypes
{

  public PrimitiveType()
  {

  }



  public String toString()
  { String _res_ = "(PrimitiveType) ";
    return _res_ + super.toString();
  }

  public static PrimitiveType parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    PrimitiveType primitivetypex = new PrimitiveType();
    return primitivetypex;
  }


  public void writeCSV(PrintWriter _out)
  { PrimitiveType primitivetypex = this;
    _out.println();
  }


    public void types2c1()
  {   if (((String) this.getname()).equals("int")) 
  {   if (Controller.inst().getCPrimitiveTypeByPK(this.gettypeId()) != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK(this.gettypeId());
     Controller.inst().setname(p,"int");
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setctypeId(p,this.gettypeId());
    Controller.inst().setname(p,"int"); }
}
  }

    public void types2c2()
  {   if (((String) this.getname()).equals("long")) 
  {   if (Controller.inst().getCPrimitiveTypeByPK(this.gettypeId()) != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK(this.gettypeId());
     Controller.inst().setname(p,"long");
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setctypeId(p,this.gettypeId());
    Controller.inst().setname(p,"long"); }
}
  }

    public void types2c3()
  {   if (((String) this.getname()).equals("double")) 
  {   if (Controller.inst().getCPrimitiveTypeByPK(this.gettypeId()) != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK(this.gettypeId());
     Controller.inst().setname(p,"double");
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setctypeId(p,this.gettypeId());
    Controller.inst().setname(p,"double"); }
}
  }

    public void types2c4()
  {   if (((String) this.getname()).equals("boolean")) 
  {   if (Controller.inst().getCPrimitiveTypeByPK(this.gettypeId()) != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK(this.gettypeId());
     Controller.inst().setname(p,"unsigned char");
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setctypeId(p,this.gettypeId());
    Controller.inst().setname(p,"unsigned char"); }
}
  }

    public void types2c5()
  {   if (((String) this.getname()).equals("String")) 
  {   if (Controller.inst().getCPointerTypeByPK(this.gettypeId()) != null)
    { CPointerType t = Controller.inst().getCPointerTypeByPK(this.gettypeId());
       if (Controller.inst().getCPrimitiveTypeByPK(this.gettypeId() + "_char") != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK(this.gettypeId() + "_char");
     Controller.inst().setname(p,"char");
    Controller.inst().setpointsTo(t,p);
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setname(p,"char");
    Controller.inst().setctypeId(p,this.gettypeId() + "_char");
    Controller.inst().setpointsTo(t,p); }

  }
    else
    { CPointerType t = new CPointerType();
    Controller.inst().addCPointerType(t);
    Controller.inst().setctypeId(t,this.gettypeId());
      if (Controller.inst().getCPrimitiveTypeByPK(this.gettypeId() + "_char") != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK(this.gettypeId() + "_char");
     Controller.inst().setname(p,"char");
    Controller.inst().setpointsTo(t,p);
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setname(p,"char");
    Controller.inst().setctypeId(p,this.gettypeId() + "_char");
    Controller.inst().setpointsTo(t,p); }
 }
}
  }

    public void types2c6()
  {   if (((String) this.getname()).equals("void")) 
  {   if (Controller.inst().getCPrimitiveTypeByPK(this.gettypeId()) != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK(this.gettypeId());
     Controller.inst().setname(p,"void");
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setctypeId(p,this.gettypeId());
    Controller.inst().setname(p,"void"); }
}
  }

    public void types2c7()
  {   if (((String) this.getname()).equals("OclAny")) 
  {   if (Controller.inst().getCPointerTypeByPK(this.gettypeId()) != null)
    { CPointerType t = Controller.inst().getCPointerTypeByPK(this.gettypeId());
       if (Controller.inst().getCPrimitiveTypeByPK("void") != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK("void");
     Controller.inst().setname(p,"void");
    Controller.inst().setpointsTo(t,p);
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setname(p,"void");
    Controller.inst().setctypeId(p,"void");
    Controller.inst().setpointsTo(t,p); }

  }
    else
    { CPointerType t = new CPointerType();
    Controller.inst().addCPointerType(t);
    Controller.inst().setctypeId(t,this.gettypeId());
      if (Controller.inst().getCPrimitiveTypeByPK("void") != null)
    { CPrimitiveType p = Controller.inst().getCPrimitiveTypeByPK("void");
     Controller.inst().setname(p,"void");
    Controller.inst().setpointsTo(t,p);
  }
    else
    { CPrimitiveType p = new CPrimitiveType();
    Controller.inst().addCPrimitiveType(p);
    Controller.inst().setname(p,"void");
    Controller.inst().setctypeId(p,"void");
    Controller.inst().setpointsTo(t,p); }
 }
}
  }

    public void types2c8()
  {   if (((String) this.getname()).equals("OclType")) 
  {   if (Controller.inst().getCPointerTypeByPK(this.gettypeId()) != null)
    { CPointerType p = Controller.inst().getCPointerTypeByPK(this.gettypeId());
       if (Controller.inst().getCStructByPK(this.getname()) != null)
    { CStruct c = Controller.inst().getCStructByPK(this.getname());
     Controller.inst().setctypeId(c,this.getname() + "_struct");
    Controller.inst().setpointsTo(p,c);
  }
    else
    { CStruct c = new CStruct();
    Controller.inst().addCStruct(c);
    Controller.inst().setname(c,this.getname());
    Controller.inst().setctypeId(c,this.getname() + "_struct");
    Controller.inst().setpointsTo(p,c); }

  }
    else
    { CPointerType p = new CPointerType();
    Controller.inst().addCPointerType(p);
    Controller.inst().setctypeId(p,this.gettypeId());
      if (Controller.inst().getCStructByPK(this.getname()) != null)
    { CStruct c = Controller.inst().getCStructByPK(this.getname());
     Controller.inst().setctypeId(c,this.getname() + "_struct");
    Controller.inst().setpointsTo(p,c);
  }
    else
    { CStruct c = new CStruct();
    Controller.inst().addCStruct(c);
    Controller.inst().setname(c,this.getname());
    Controller.inst().setctypeId(c,this.getname() + "_struct");
    Controller.inst().setpointsTo(p,c); }
 }
}
  }


}


class Entity
  extends Classifier
  implements SystemTypes
{
  private boolean isAbstract = false; // internal
  private boolean isInterface = false; // internal
  private List ownedAttribute = new Vector(); // of Property
  private List superclass = new Vector(); // of Entity
  private List subclasses = new Vector(); // of Entity
  private List generalization = new Vector(); // of Generalization
  private List specialization = new Vector(); // of Generalization
  private List ownedOperation = new Vector(); // of Operation

  public Entity()
  {
    this.isAbstract = false;
    this.isInterface = false;

  }



  public String toString()
  { String _res_ = "(Entity) ";
    _res_ = _res_ + isAbstract + ",";
    _res_ = _res_ + isInterface;
    return _res_ + super.toString();
  }

  public static Entity parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Entity entityx = new Entity();
    entityx.isAbstract = Boolean.parseBoolean((String) _line1vals.get(0));
    entityx.isInterface = Boolean.parseBoolean((String) _line1vals.get(1));
    return entityx;
  }


  public void writeCSV(PrintWriter _out)
  { Entity entityx = this;
    _out.print("" + entityx.isAbstract);
    _out.print(" , ");
    _out.print("" + entityx.isInterface);
    _out.println();
  }


  public void setisAbstract(boolean isAbstract_x) { isAbstract = isAbstract_x;  }


    public static void setAllisAbstract(List entitys,boolean val)
  { for (int i = 0; i < entitys.size(); i++)
    { Entity entityx = (Entity) entitys.get(i);
      Controller.inst().setisAbstract(entityx,val); } }


  public void setisInterface(boolean isInterface_x) { isInterface = isInterface_x;  }


    public static void setAllisInterface(List entitys,boolean val)
  { for (int i = 0; i < entitys.size(); i++)
    { Entity entityx = (Entity) entitys.get(i);
      Controller.inst().setisInterface(entityx,val); } }


  public void setownedAttribute(List ownedAttribute_xx) { ownedAttribute = ownedAttribute_xx;
    }
 
  public void setownedAttribute(int ind_x, Property ownedAttribute_xx) { if (ind_x >= 0 && ind_x < ownedAttribute.size()) { ownedAttribute.set(ind_x, ownedAttribute_xx); } }

 public void addownedAttribute(Property ownedAttribute_xx) { ownedAttribute.add(ownedAttribute_xx);
    }
 
  public void removeownedAttribute(Property ownedAttribute_xx) { Vector _removedownedAttributeownedAttribute_xx = new Vector();
  _removedownedAttributeownedAttribute_xx.add(ownedAttribute_xx);
  ownedAttribute.removeAll(_removedownedAttributeownedAttribute_xx);
    }

  public static void setAllownedAttribute(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setownedAttribute(entityx, _val); } }

  public static void setAllownedAttribute(List entitys, int _ind,Property _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setownedAttribute(entityx,_ind,_val); } }

  public static void addAllownedAttribute(List entitys, Property _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().addownedAttribute(entityx,  _val); } }


  public static void removeAllownedAttribute(List entitys,Property _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().removeownedAttribute(entityx, _val); } }


  public static void unionAllownedAttribute(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().unionownedAttribute(entityx, _val); } }


  public static void subtractAllownedAttribute(List entitys,  List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().subtractownedAttribute(entityx,  _val); } }


  public void setsuperclass(List superclass_xx) { if (superclass_xx.size() > 1) { return; } 
    superclass = superclass_xx;
  }
 
  public void addsuperclass(Entity superclass_xx) { if (superclass.size() > 0) { superclass.clear(); } 
    if (superclass.contains(superclass_xx)) {} else { superclass.add(superclass_xx); }
    }
 
  public void removesuperclass(Entity superclass_xx) { Vector _removedsuperclasssuperclass_xx = new Vector();
  _removedsuperclasssuperclass_xx.add(superclass_xx);
  superclass.removeAll(_removedsuperclasssuperclass_xx);
    }

  public static void setAllsuperclass(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setsuperclass(entityx, _val); } }

  public static void addAllsuperclass(List entitys, Entity _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().addsuperclass(entityx,  _val); } }


  public static void removeAllsuperclass(List entitys,Entity _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().removesuperclass(entityx, _val); } }


  public static void unionAllsuperclass(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().unionsuperclass(entityx, _val); } }


  public static void subtractAllsuperclass(List entitys,  List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().subtractsuperclass(entityx,  _val); } }


  public void setsubclasses(List subclasses_xx) { subclasses = subclasses_xx;
    }
 
  public void addsubclasses(Entity subclasses_xx) { if (subclasses.contains(subclasses_xx)) {} else { subclasses.add(subclasses_xx); }
    }
 
  public void removesubclasses(Entity subclasses_xx) { Vector _removedsubclassessubclasses_xx = new Vector();
  _removedsubclassessubclasses_xx.add(subclasses_xx);
  subclasses.removeAll(_removedsubclassessubclasses_xx);
    }

  public static void setAllsubclasses(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setsubclasses(entityx, _val); } }

  public static void addAllsubclasses(List entitys, Entity _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().addsubclasses(entityx,  _val); } }


  public static void removeAllsubclasses(List entitys,Entity _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().removesubclasses(entityx, _val); } }


  public static void unionAllsubclasses(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().unionsubclasses(entityx, _val); } }


  public static void subtractAllsubclasses(List entitys,  List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().subtractsubclasses(entityx,  _val); } }


  public void setgeneralization(List generalization_xx) { generalization = generalization_xx;
    }
 
  public void addgeneralization(Generalization generalization_xx) { if (generalization.contains(generalization_xx)) {} else { generalization.add(generalization_xx); }
    }
 
  public void removegeneralization(Generalization generalization_xx) { Vector _removedgeneralizationgeneralization_xx = new Vector();
  _removedgeneralizationgeneralization_xx.add(generalization_xx);
  generalization.removeAll(_removedgeneralizationgeneralization_xx);
    }

  public static void setAllgeneralization(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setgeneralization(entityx, _val); } }

  public static void addAllgeneralization(List entitys, Generalization _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().addgeneralization(entityx,  _val); } }


  public static void removeAllgeneralization(List entitys,Generalization _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().removegeneralization(entityx, _val); } }


  public static void unionAllgeneralization(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().uniongeneralization(entityx, _val); } }


  public static void subtractAllgeneralization(List entitys,  List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().subtractgeneralization(entityx,  _val); } }


  public void setspecialization(List specialization_xx) { specialization = specialization_xx;
    }
 
  public void addspecialization(Generalization specialization_xx) { if (specialization.contains(specialization_xx)) {} else { specialization.add(specialization_xx); }
    }
 
  public void removespecialization(Generalization specialization_xx) { Vector _removedspecializationspecialization_xx = new Vector();
  _removedspecializationspecialization_xx.add(specialization_xx);
  specialization.removeAll(_removedspecializationspecialization_xx);
    }

  public static void setAllspecialization(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setspecialization(entityx, _val); } }

  public static void addAllspecialization(List entitys, Generalization _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().addspecialization(entityx,  _val); } }


  public static void removeAllspecialization(List entitys,Generalization _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().removespecialization(entityx, _val); } }


  public static void unionAllspecialization(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().unionspecialization(entityx, _val); } }


  public static void subtractAllspecialization(List entitys,  List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().subtractspecialization(entityx,  _val); } }


  public void setownedOperation(List ownedOperation_xx) { ownedOperation = ownedOperation_xx;
    }
 
  public void setownedOperation(int ind_x, Operation ownedOperation_xx) { if (ind_x >= 0 && ind_x < ownedOperation.size()) { ownedOperation.set(ind_x, ownedOperation_xx); } }

 public void addownedOperation(Operation ownedOperation_xx) { ownedOperation.add(ownedOperation_xx);
    }
 
  public void removeownedOperation(Operation ownedOperation_xx) { Vector _removedownedOperationownedOperation_xx = new Vector();
  _removedownedOperationownedOperation_xx.add(ownedOperation_xx);
  ownedOperation.removeAll(_removedownedOperationownedOperation_xx);
    }

  public static void setAllownedOperation(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setownedOperation(entityx, _val); } }

  public static void setAllownedOperation(List entitys, int _ind,Operation _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setownedOperation(entityx,_ind,_val); } }

  public static void addAllownedOperation(List entitys, Operation _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().addownedOperation(entityx,  _val); } }


  public static void removeAllownedOperation(List entitys,Operation _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().removeownedOperation(entityx, _val); } }


  public static void unionAllownedOperation(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().unionownedOperation(entityx, _val); } }


  public static void subtractAllownedOperation(List entitys,  List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().subtractownedOperation(entityx,  _val); } }


    public boolean getisAbstract() { return isAbstract; }

    public static List getAllisAbstract(List entitys)
  { List result = new Vector();
    for (int i = 0; i < entitys.size(); i++)
    { Entity entityx = (Entity) entitys.get(i);
      if (result.contains(new Boolean(entityx.getisAbstract()))) { }
      else { result.add(new Boolean(entityx.getisAbstract())); } }
    return result; }

    public static List getAllOrderedisAbstract(List entitys)
  { List result = new Vector();
    for (int i = 0; i < entitys.size(); i++)
    { Entity entityx = (Entity) entitys.get(i);
      result.add(new Boolean(entityx.getisAbstract())); } 
    return result; }

    public boolean getisInterface() { return isInterface; }

    public static List getAllisInterface(List entitys)
  { List result = new Vector();
    for (int i = 0; i < entitys.size(); i++)
    { Entity entityx = (Entity) entitys.get(i);
      if (result.contains(new Boolean(entityx.getisInterface()))) { }
      else { result.add(new Boolean(entityx.getisInterface())); } }
    return result; }

    public static List getAllOrderedisInterface(List entitys)
  { List result = new Vector();
    for (int i = 0; i < entitys.size(); i++)
    { Entity entityx = (Entity) entitys.get(i);
      result.add(new Boolean(entityx.getisInterface())); } 
    return result; }

  public List getownedAttribute() { return (Vector) ((Vector) ownedAttribute).clone(); }

  public static List getAllownedAttribute(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getownedAttribute()); }
    return result; }

  public static List getAllOrderedownedAttribute(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result.addAll(entityx.getownedAttribute()); }
    return result; }

  public List getsuperclass() { return (Vector) ((Vector) superclass).clone(); }

  public static List getAllsuperclass(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getsuperclass()); }
    return result; }

  public static List getAllOrderedsuperclass(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getsuperclass()); }
    return result; }

  public List getsubclasses() { return (Vector) ((Vector) subclasses).clone(); }

  public static List getAllsubclasses(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getsubclasses()); }
    return result; }

  public static List getAllOrderedsubclasses(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getsubclasses()); }
    return result; }

  public List getgeneralization() { return (Vector) ((Vector) generalization).clone(); }

  public static List getAllgeneralization(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getgeneralization()); }
    return result; }

  public static List getAllOrderedgeneralization(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getgeneralization()); }
    return result; }

  public List getspecialization() { return (Vector) ((Vector) specialization).clone(); }

  public static List getAllspecialization(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getspecialization()); }
    return result; }

  public static List getAllOrderedspecialization(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getspecialization()); }
    return result; }

  public List getownedOperation() { return (Vector) ((Vector) ownedOperation).clone(); }

  public static List getAllownedOperation(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result = Set.union(result, entityx.getownedOperation()); }
    return result; }

  public static List getAllOrderedownedOperation(List entitys)
  { List result = new Vector();
    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      result.addAll(entityx.getownedOperation()); }
    return result; }

    public List inheritedOperations()
  {   List result = new Vector();
 
  if (superclass.size() == 0) 
  {   result = (new SystemTypes.Set()).getElements();
 
  }  else
      if (superclass.size() > 0) 
  {   result = ( Set.concatenate(((Entity) Set.any(superclass)).getownedOperation(), Set.select_0(((Entity) Set.any(superclass)).inheritedOperations(),this)) );
 
  }       return result;
  }


    public boolean isApplicationClass()
  {   boolean result = false;
 
  if (((String) name).equals("OclType")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclAttribute")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclOperation")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclProcess")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclProcessGroup")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("ocltnode")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclIterator")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclFile")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("MathLib")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclDate")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclRandom")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclException")) 
  {   result = false;
 
  }  else
      if (true) 
  {   result = true;
 
  }   
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public List allSubclasses()
  {   List result = new Vector();
 
  if (subclasses.size() == 0) 
  {   result = (new SystemTypes.Set()).getElements();
 
  }  else
      if (subclasses.size() > 0) 
  {   result = ( Set.union(subclasses,Set.collect_1(subclasses)) );
 
  }       return result;
  }


    public void types2c9()
  {   if (Controller.inst().getCPointerTypeByPK(this.gettypeId()) != null)
    { CPointerType p = Controller.inst().getCPointerTypeByPK(this.gettypeId());
       if (Controller.inst().getCStructByPK(this.getname()) != null)
    { CStruct c = Controller.inst().getCStructByPK(this.getname());
     Controller.inst().setctypeId(c,this.getname());
    Controller.inst().setpointsTo(p,c);
  }
    else
    { CStruct c = new CStruct();
    Controller.inst().addCStruct(c);
    Controller.inst().setname(c,this.getname());
    Controller.inst().setctypeId(c,this.getname());
    Controller.inst().setpointsTo(p,c); }

  }
    else
    { CPointerType p = new CPointerType();
    Controller.inst().addCPointerType(p);
    Controller.inst().setctypeId(p,this.gettypeId());
      if (Controller.inst().getCStructByPK(this.getname()) != null)
    { CStruct c = Controller.inst().getCStructByPK(this.getname());
     Controller.inst().setctypeId(c,this.getname());
    Controller.inst().setpointsTo(p,c);
  }
    else
    { CStruct c = new CStruct();
    Controller.inst().addCStruct(c);
    Controller.inst().setname(c,this.getname());
    Controller.inst().setctypeId(c,this.getname());
    Controller.inst().setpointsTo(p,c); }
 }

  }

    public void program2c1(Property p)
  {     CType pre_CType62 = Controller.inst().getCTypeByPK(p.gettype().gettypeId());
  if (Controller.inst().getCStructByPK(this.getname()) != null)
    { CStruct c = Controller.inst().getCStructByPK(this.getname());
     CMember m = new CMember();
    Controller.inst().addCMember(m);
    Controller.inst().setname(m,p.getname());
    Controller.inst().setisKey(m,p.getisUnique());
    Controller.inst().setmultiplicity(m,p.mapMultiplicity());
    Controller.inst().settype(m,pre_CType62);
    Controller.inst().addmembers(c,m);
  }
    else
    { CStruct c = new CStruct();
    Controller.inst().addCStruct(c);
    Controller.inst().setname(c,this.getname());
    CMember m = new CMember();
    Controller.inst().addCMember(m);
    Controller.inst().setname(m,p.getname());
    Controller.inst().setisKey(m,p.getisUnique());
    Controller.inst().setmultiplicity(m,p.mapMultiplicity());
    Controller.inst().settype(m,pre_CType62);
    Controller.inst().addmembers(c,m); }

  }

    public void program2c1outer()
  {  Entity entityx = this;
    List _range64 = new Vector();
  _range64.addAll(entityx.getownedAttribute());
  for (int _i63 = 0; _i63 < _range64.size(); _i63++)
  { Property p = (Property) _range64.get(_i63);
        if (p.getname().length() > 0)
    {     if (p.getisStatic() == false)
    {     this.program2c1(p); }
 }

  }
  }


    public void program2c3(Operation iop)
  {   if (Controller.inst().getCOperationByPK(iop.getname() + "_" + this.getname()) != null)
    { COperation op = Controller.inst().getCOperationByPK(iop.getname() + "_" + this.getname());
     Controller.inst().setname(op,iop.getname() + "_" + this.getname());
    Controller.inst().setinheritedFrom(op,((Entity) Set.any(this.getsuperclass())).getname());
    Controller.inst().setscope(op,"entity");
    Controller.inst().setisQuery(op,iop.getisQuery());
    Controller.inst().setisStatic(op,false);
    Controller.inst().setclassname(op,this.getname());
    Controller.inst().setreturnType(op,Controller.inst().getCTypeByPK(iop.gettype().gettypeId()));
    Controller.inst().setelementType(op,Controller.inst().getCTypeByPK(iop.getelementType().gettypeId()));
    CVariable s = new CVariable();
    Controller.inst().addCVariable(s);
    Controller.inst().setname(s,"self");
    Controller.inst().settype(s,Controller.inst().getCTypeByPK(this.gettypeId()));
    Controller.inst().addparameters(op,s);
        List _property_list67 = new Vector();
    _property_list67.addAll(iop.getparameters());
    for (int _ind68 = 0; _ind68 < _property_list67.size(); _ind68++)
    { Property p = (Property) _property_list67.get(_ind68);
      CVariable v = new CVariable();
    Controller.inst().addCVariable(v);
    Controller.inst().setname(v,p.getname());
    Controller.inst().settype(v,Controller.inst().getCTypeByPK(p.gettype().gettypeId()));
    Controller.inst().addparameters(op,v);
    }

  }
    else
    { COperation op = new COperation();
    Controller.inst().addCOperation(op);
    Controller.inst().setname(op,iop.getname() + "_" + this.getname());
    Controller.inst().setinheritedFrom(op,((Entity) Set.any(this.getsuperclass())).getname());
    Controller.inst().setopId(op,iop.getname() + "_" + this.getname());
    Controller.inst().setscope(op,"entity");
    Controller.inst().setisQuery(op,iop.getisQuery());
    Controller.inst().setisStatic(op,false);
    Controller.inst().setclassname(op,this.getname());
    Controller.inst().setreturnType(op,Controller.inst().getCTypeByPK(iop.gettype().gettypeId()));
    Controller.inst().setelementType(op,Controller.inst().getCTypeByPK(iop.getelementType().gettypeId()));
    CVariable s = new CVariable();
    Controller.inst().addCVariable(s);
    Controller.inst().setname(s,"self");
    Controller.inst().settype(s,Controller.inst().getCTypeByPK(this.gettypeId()));
    Controller.inst().addparameters(op,s);
        List _property_list65 = new Vector();
    _property_list65.addAll(iop.getparameters());
    for (int _ind66 = 0; _ind66 < _property_list65.size(); _ind66++)
    { Property p = (Property) _property_list65.get(_ind66);
      CVariable v = new CVariable();
    Controller.inst().addCVariable(v);
    Controller.inst().setname(v,p.getname());
    Controller.inst().settype(v,Controller.inst().getCTypeByPK(p.gettype().gettypeId()));
    Controller.inst().addparameters(op,v);
    }
 }

  }

    public void program2c3outer()
  {  Entity entityx = this;
    List _range70 = new Vector();
  _range70.addAll(entityx.inheritedOperations());
  for (int _i69 = 0; _i69 < _range70.size(); _i69++)
  { Operation iop = (Operation) _range70.get(_i69);
        if (iop.getisStatic() == false)
    {     if (!(Operation.getAllOrderedname(entityx.getownedOperation()).contains(iop.getname())))
    {     this.program2c3(iop); }
 }

  }
  }


    public void program2c8(Property p)
  {     CType pre_CType71 = Controller.inst().getCTypeByPK(p.gettype().gettypeId());
CVariable c = new CVariable();
    Controller.inst().addCVariable(c);
    Controller.inst().setname(c,p.getname());
    Controller.inst().settype(c,pre_CType71);
    Controller.inst().addvariables(((CProgram) Set.any(Controller.inst().cprograms)),c);
  }

    public void program2c8outer()
  {  Entity entityx = this;
    List _range73 = new Vector();
  _range73.addAll(entityx.getownedAttribute());
  for (int _i72 = 0; _i72 < _range73.size(); _i72++)
  { Property p = (Property) _range73.get(_i72);
        if (p.getname().length() > 0)
    {     if (p.getisStatic() == true)
    {     if (entityx.isApplicationClass())
    {     this.program2c8(p); }
 }
 }

  }
  }



}


class Property
  extends StructuralFeature
  implements SystemTypes
{
  private int lower = 0; // internal
  private int upper = 0; // internal
  private boolean isOrdered = false; // internal
  private boolean isUnique = false; // internal
  private boolean isDerived = false; // internal
  private Entity owner;
  private Expression initialValue;

  public Property(Entity owner,Expression initialValue)
  {
    this.lower = 0;
    this.upper = 0;
    this.isOrdered = false;
    this.isUnique = false;
    this.isDerived = false;
    this.owner = owner;
    this.initialValue = initialValue;

  }

  public Property() { }



  public String toString()
  { String _res_ = "(Property) ";
    _res_ = _res_ + lower + ",";
    _res_ = _res_ + upper + ",";
    _res_ = _res_ + isOrdered + ",";
    _res_ = _res_ + isUnique + ",";
    _res_ = _res_ + isDerived;
    return _res_ + super.toString();
  }

  public static Property parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Property propertyx = new Property();
    propertyx.lower = Integer.parseInt((String) _line1vals.get(0));
    propertyx.upper = Integer.parseInt((String) _line1vals.get(1));
    propertyx.isOrdered = Boolean.parseBoolean((String) _line1vals.get(2));
    propertyx.isUnique = Boolean.parseBoolean((String) _line1vals.get(3));
    propertyx.isDerived = Boolean.parseBoolean((String) _line1vals.get(4));
    return propertyx;
  }


  public void writeCSV(PrintWriter _out)
  { Property propertyx = this;
    _out.print("" + propertyx.lower);
    _out.print(" , ");
    _out.print("" + propertyx.upper);
    _out.print(" , ");
    _out.print("" + propertyx.isOrdered);
    _out.print(" , ");
    _out.print("" + propertyx.isUnique);
    _out.print(" , ");
    _out.print("" + propertyx.isDerived);
    _out.println();
  }


  public void setlower(int lower_x) { lower = lower_x;  }


    public static void setAlllower(List propertys,int val)
  { for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      Controller.inst().setlower(propertyx,val); } }


  public void setupper(int upper_x) { upper = upper_x;  }


    public static void setAllupper(List propertys,int val)
  { for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      Controller.inst().setupper(propertyx,val); } }


  public void setisOrdered(boolean isOrdered_x) { isOrdered = isOrdered_x;  }


    public static void setAllisOrdered(List propertys,boolean val)
  { for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      Controller.inst().setisOrdered(propertyx,val); } }


  public void setisUnique(boolean isUnique_x) { isUnique = isUnique_x;  }


    public static void setAllisUnique(List propertys,boolean val)
  { for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      Controller.inst().setisUnique(propertyx,val); } }


  public void setisDerived(boolean isDerived_x) { isDerived = isDerived_x;  }


    public static void setAllisDerived(List propertys,boolean val)
  { for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      Controller.inst().setisDerived(propertyx,val); } }


  public void setowner(Entity owner_xx) { owner = owner_xx;
  }

  public static void setAllowner(List propertys, Entity _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().setowner(propertyx, _val); } }

  public void setinitialValue(Expression initialValue_xx) { initialValue = initialValue_xx;
  }

  public static void setAllinitialValue(List propertys, Expression _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().setinitialValue(propertyx, _val); } }

    public int getlower() { return lower; }

    public static List getAlllower(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      if (result.contains(new Integer(propertyx.getlower()))) { }
      else { result.add(new Integer(propertyx.getlower())); } }
    return result; }

    public static List getAllOrderedlower(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      result.add(new Integer(propertyx.getlower())); } 
    return result; }

    public int getupper() { return upper; }

    public static List getAllupper(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      if (result.contains(new Integer(propertyx.getupper()))) { }
      else { result.add(new Integer(propertyx.getupper())); } }
    return result; }

    public static List getAllOrderedupper(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      result.add(new Integer(propertyx.getupper())); } 
    return result; }

    public boolean getisOrdered() { return isOrdered; }

    public static List getAllisOrdered(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      if (result.contains(new Boolean(propertyx.getisOrdered()))) { }
      else { result.add(new Boolean(propertyx.getisOrdered())); } }
    return result; }

    public static List getAllOrderedisOrdered(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      result.add(new Boolean(propertyx.getisOrdered())); } 
    return result; }

    public boolean getisUnique() { return isUnique; }

    public static List getAllisUnique(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      if (result.contains(new Boolean(propertyx.getisUnique()))) { }
      else { result.add(new Boolean(propertyx.getisUnique())); } }
    return result; }

    public static List getAllOrderedisUnique(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      result.add(new Boolean(propertyx.getisUnique())); } 
    return result; }

    public boolean getisDerived() { return isDerived; }

    public static List getAllisDerived(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      if (result.contains(new Boolean(propertyx.getisDerived()))) { }
      else { result.add(new Boolean(propertyx.getisDerived())); } }
    return result; }

    public static List getAllOrderedisDerived(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      result.add(new Boolean(propertyx.getisDerived())); } 
    return result; }

  public Entity getowner() { return owner; }

  public static List getAllowner(List propertys)
  { List result = new Vector();
    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      if (result.contains(propertyx.getowner())) {}
      else { result.add(propertyx.getowner()); }
 }
    return result; }

  public static List getAllOrderedowner(List propertys)
  { List result = new Vector();
    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      if (result.contains(propertyx.getowner())) {}
      else { result.add(propertyx.getowner()); }
 }
    return result; }

  public Expression getinitialValue() { return initialValue; }

  public static List getAllinitialValue(List propertys)
  { List result = new Vector();
    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      if (result.contains(propertyx.getinitialValue())) {}
      else { result.add(propertyx.getinitialValue()); }
 }
    return result; }

  public static List getAllOrderedinitialValue(List propertys)
  { List result = new Vector();
    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      if (result.contains(propertyx.getinitialValue())) {}
      else { result.add(propertyx.getinitialValue()); }
 }
    return result; }

    public String mapMultiplicity()
  {   String result = "";
 
  if (upper == 1 && lower == 1) 
  {   result = "ONE";
 
  }  else
      {   if (upper == 1 && lower == 0) 
  {   result = "ZERONE";
 
  }  else
      if (true) 
  {   result = "MANY";
 
  }   
   }     return result;
  }



}


class Association
  extends Relationship
  implements SystemTypes
{
  private boolean addOnly = false; // internal
  private boolean aggregation = false; // internal
  private List memberEnd = new Vector(); // of Property

  public Association()
  {
    this.addOnly = false;
    this.aggregation = false;

  }



  public String toString()
  { String _res_ = "(Association) ";
    _res_ = _res_ + addOnly + ",";
    _res_ = _res_ + aggregation;
    return _res_ + super.toString();
  }

  public static Association parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Association associationx = new Association();
    associationx.addOnly = Boolean.parseBoolean((String) _line1vals.get(0));
    associationx.aggregation = Boolean.parseBoolean((String) _line1vals.get(1));
    return associationx;
  }


  public void writeCSV(PrintWriter _out)
  { Association associationx = this;
    _out.print("" + associationx.addOnly);
    _out.print(" , ");
    _out.print("" + associationx.aggregation);
    _out.println();
  }


  public void setaddOnly(boolean addOnly_x) { addOnly = addOnly_x;  }


    public static void setAlladdOnly(List associations,boolean val)
  { for (int i = 0; i < associations.size(); i++)
    { Association associationx = (Association) associations.get(i);
      Controller.inst().setaddOnly(associationx,val); } }


  public void setaggregation(boolean aggregation_x) { aggregation = aggregation_x;  }


    public static void setAllaggregation(List associations,boolean val)
  { for (int i = 0; i < associations.size(); i++)
    { Association associationx = (Association) associations.get(i);
      Controller.inst().setaggregation(associationx,val); } }


  public void setmemberEnd(List memberEnd_xx) { memberEnd = memberEnd_xx;
    }
 
  public void setmemberEnd(int ind_x, Property memberEnd_xx) { if (ind_x >= 0 && ind_x < memberEnd.size()) { memberEnd.set(ind_x, memberEnd_xx); } }

 public void addmemberEnd(Property memberEnd_xx) { memberEnd.add(memberEnd_xx);
    }
 
  public void removememberEnd(Property memberEnd_xx) { Vector _removedmemberEndmemberEnd_xx = new Vector();
  _removedmemberEndmemberEnd_xx.add(memberEnd_xx);
  memberEnd.removeAll(_removedmemberEndmemberEnd_xx);
    }

  public static void setAllmemberEnd(List associations, List _val)
  { for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx = (Association) associations.get(_i);
      Controller.inst().setmemberEnd(associationx, _val); } }

  public static void setAllmemberEnd(List associations, int _ind,Property _val)
  { for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx = (Association) associations.get(_i);
      Controller.inst().setmemberEnd(associationx,_ind,_val); } }

  public static void addAllmemberEnd(List associations, Property _val)
  { for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx = (Association) associations.get(_i);
      Controller.inst().addmemberEnd(associationx,  _val); } }


  public static void removeAllmemberEnd(List associations,Property _val)
  { for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx = (Association) associations.get(_i);
      Controller.inst().removememberEnd(associationx, _val); } }


  public static void unionAllmemberEnd(List associations, List _val)
  { for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx = (Association) associations.get(_i);
      Controller.inst().unionmemberEnd(associationx, _val); } }


  public static void subtractAllmemberEnd(List associations,  List _val)
  { for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx = (Association) associations.get(_i);
      Controller.inst().subtractmemberEnd(associationx,  _val); } }


    public boolean getaddOnly() { return addOnly; }

    public static List getAlladdOnly(List associations)
  { List result = new Vector();
    for (int i = 0; i < associations.size(); i++)
    { Association associationx = (Association) associations.get(i);
      if (result.contains(new Boolean(associationx.getaddOnly()))) { }
      else { result.add(new Boolean(associationx.getaddOnly())); } }
    return result; }

    public static List getAllOrderedaddOnly(List associations)
  { List result = new Vector();
    for (int i = 0; i < associations.size(); i++)
    { Association associationx = (Association) associations.get(i);
      result.add(new Boolean(associationx.getaddOnly())); } 
    return result; }

    public boolean getaggregation() { return aggregation; }

    public static List getAllaggregation(List associations)
  { List result = new Vector();
    for (int i = 0; i < associations.size(); i++)
    { Association associationx = (Association) associations.get(i);
      if (result.contains(new Boolean(associationx.getaggregation()))) { }
      else { result.add(new Boolean(associationx.getaggregation())); } }
    return result; }

    public static List getAllOrderedaggregation(List associations)
  { List result = new Vector();
    for (int i = 0; i < associations.size(); i++)
    { Association associationx = (Association) associations.get(i);
      result.add(new Boolean(associationx.getaggregation())); } 
    return result; }

  public List getmemberEnd() { return (Vector) ((Vector) memberEnd).clone(); }

  public static List getAllmemberEnd(List associations)
  { List result = new Vector();
    for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx = (Association) associations.get(_i);
      result = Set.union(result, associationx.getmemberEnd()); }
    return result; }

  public static List getAllOrderedmemberEnd(List associations)
  { List result = new Vector();
    for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx = (Association) associations.get(_i);
      result.addAll(associationx.getmemberEnd()); }
    return result; }


}


class Generalization
  extends Relationship
  implements SystemTypes
{
  private Entity specific;
  private Entity general;

  public Generalization(Entity specific,Entity general)
  {
    this.specific = specific;
    this.general = general;

  }

  public Generalization() { }



  public String toString()
  { String _res_ = "(Generalization) ";
    return _res_ + super.toString();
  }

  public static Generalization parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Generalization generalizationx = new Generalization();
    return generalizationx;
  }


  public void writeCSV(PrintWriter _out)
  { Generalization generalizationx = this;
    _out.println();
  }


  public void setspecific(Entity specific_xx) { specific = specific_xx;
  }

  public static void setAllspecific(List generalizations, Entity _val)
  { for (int _i = 0; _i < generalizations.size(); _i++)
    { Generalization generalizationx = (Generalization) generalizations.get(_i);
      Controller.inst().setspecific(generalizationx, _val); } }

  public void setgeneral(Entity general_xx) { general = general_xx;
  }

  public static void setAllgeneral(List generalizations, Entity _val)
  { for (int _i = 0; _i < generalizations.size(); _i++)
    { Generalization generalizationx = (Generalization) generalizations.get(_i);
      Controller.inst().setgeneral(generalizationx, _val); } }

  public Entity getspecific() { return specific; }

  public static List getAllspecific(List generalizations)
  { List result = new Vector();
    for (int _i = 0; _i < generalizations.size(); _i++)
    { Generalization generalizationx = (Generalization) generalizations.get(_i);
      if (result.contains(generalizationx.getspecific())) {}
      else { result.add(generalizationx.getspecific()); }
 }
    return result; }

  public static List getAllOrderedspecific(List generalizations)
  { List result = new Vector();
    for (int _i = 0; _i < generalizations.size(); _i++)
    { Generalization generalizationx = (Generalization) generalizations.get(_i);
      if (result.contains(generalizationx.getspecific())) {}
      else { result.add(generalizationx.getspecific()); }
 }
    return result; }

  public Entity getgeneral() { return general; }

  public static List getAllgeneral(List generalizations)
  { List result = new Vector();
    for (int _i = 0; _i < generalizations.size(); _i++)
    { Generalization generalizationx = (Generalization) generalizations.get(_i);
      if (result.contains(generalizationx.getgeneral())) {}
      else { result.add(generalizationx.getgeneral()); }
 }
    return result; }

  public static List getAllOrderedgeneral(List generalizations)
  { List result = new Vector();
    for (int _i = 0; _i < generalizations.size(); _i++)
    { Generalization generalizationx = (Generalization) generalizations.get(_i);
      if (result.contains(generalizationx.getgeneral())) {}
      else { result.add(generalizationx.getgeneral()); }
 }
    return result; }

    public void program2c5()
  {   if (this.getgeneral().getisInterface() == false) 
  { CMember m = new CMember();
    Controller.inst().addCMember(m);
    Controller.inst().setname(m,"super");
    Controller.inst().setmultiplicity(m,"ONE");
      if (Controller.inst().getCStructByPK(this.getspecific().getname()) != null)
    { CStruct sub = Controller.inst().getCStructByPK(this.getspecific().getname());
     Controller.inst().addmembers(sub,m);
    Controller.inst().settype(m,Controller.inst().getCPointerTypeByPK(this.getgeneral().gettypeId()));
  }
    else
    { CStruct sub = new CStruct();
    Controller.inst().addCStruct(sub);
    Controller.inst().setname(sub,this.getspecific().getname());
    Controller.inst().addmembers(sub,m);
    Controller.inst().settype(m,Controller.inst().getCPointerTypeByPK(this.getgeneral().gettypeId())); }
}
  }

    public void program2c6()
  {   if (this.getgeneral().getisInterface() == true) 
  {   if (Controller.inst().getCStructByPK(this.getgeneral().getname()) != null)
    { CStruct sup = Controller.inst().getCStructByPK(this.getgeneral().getname());
       if (Controller.inst().getCStructByPK(this.getspecific().getname()) != null)
    { CStruct sub = Controller.inst().getCStructByPK(this.getspecific().getname());
     Controller.inst().addinterfaces(sub,sup);
  }
    else
    { CStruct sub = new CStruct();
    Controller.inst().addCStruct(sub);
    Controller.inst().setname(sub,this.getspecific().getname());
    Controller.inst().addinterfaces(sub,sup); }

  }
    else
    { CStruct sup = new CStruct();
    Controller.inst().addCStruct(sup);
    Controller.inst().setname(sup,this.getgeneral().getname());
      if (Controller.inst().getCStructByPK(this.getspecific().getname()) != null)
    { CStruct sub = Controller.inst().getCStructByPK(this.getspecific().getname());
     Controller.inst().addinterfaces(sub,sup);
  }
    else
    { CStruct sub = new CStruct();
    Controller.inst().addCStruct(sub);
    Controller.inst().setname(sub,this.getspecific().getname());
    Controller.inst().addinterfaces(sub,sup); }
 }
}
  }


}


abstract class BehaviouralFeature
  extends Feature
  implements SystemTypes
{
  protected boolean isAbstract = false; // internal
  protected List parameters = new Vector(); // of Property
  protected Statement activity;

  public BehaviouralFeature(Statement activity)
  {
    this.isAbstract = false;
    this.activity = activity;

  }

  public BehaviouralFeature() { }



  public String toString()
  { String _res_ = "(BehaviouralFeature) ";
    _res_ = _res_ + isAbstract;
    return _res_ + super.toString();
  }



  public void setisAbstract(boolean isAbstract_x) { isAbstract = isAbstract_x;  }


    public static void setAllisAbstract(List behaviouralfeatures,boolean val)
  { for (int i = 0; i < behaviouralfeatures.size(); i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(i);
      Controller.inst().setisAbstract(behaviouralfeaturex,val); } }


  public void setparameters(List parameters_xx) { parameters = parameters_xx;
    }
 
  public void setparameters(int ind_x, Property parameters_xx) { if (ind_x >= 0 && ind_x < parameters.size()) { parameters.set(ind_x, parameters_xx); } }

 public void addparameters(Property parameters_xx) { parameters.add(parameters_xx);
    }
 
  public void removeparameters(Property parameters_xx) { Vector _removedparametersparameters_xx = new Vector();
  _removedparametersparameters_xx.add(parameters_xx);
  parameters.removeAll(_removedparametersparameters_xx);
    }

  public static void setAllparameters(List behaviouralfeatures, List _val)
  { for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      Controller.inst().setparameters(behaviouralfeaturex, _val); } }

  public static void setAllparameters(List behaviouralfeatures, int _ind,Property _val)
  { for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      Controller.inst().setparameters(behaviouralfeaturex,_ind,_val); } }

  public static void addAllparameters(List behaviouralfeatures, Property _val)
  { for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      Controller.inst().addparameters(behaviouralfeaturex,  _val); } }


  public static void removeAllparameters(List behaviouralfeatures,Property _val)
  { for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      Controller.inst().removeparameters(behaviouralfeaturex, _val); } }


  public static void unionAllparameters(List behaviouralfeatures, List _val)
  { for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      Controller.inst().unionparameters(behaviouralfeaturex, _val); } }


  public static void subtractAllparameters(List behaviouralfeatures,  List _val)
  { for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      Controller.inst().subtractparameters(behaviouralfeaturex,  _val); } }


  public void setactivity(Statement activity_xx) { activity = activity_xx;
  }

  public static void setAllactivity(List behaviouralfeatures, Statement _val)
  { for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      Controller.inst().setactivity(behaviouralfeaturex, _val); } }

    public boolean getisAbstract() { return isAbstract; }

    public static List getAllisAbstract(List behaviouralfeatures)
  { List result = new Vector();
    for (int i = 0; i < behaviouralfeatures.size(); i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(i);
      if (result.contains(new Boolean(behaviouralfeaturex.getisAbstract()))) { }
      else { result.add(new Boolean(behaviouralfeaturex.getisAbstract())); } }
    return result; }

    public static List getAllOrderedisAbstract(List behaviouralfeatures)
  { List result = new Vector();
    for (int i = 0; i < behaviouralfeatures.size(); i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(i);
      result.add(new Boolean(behaviouralfeaturex.getisAbstract())); } 
    return result; }

  public List getparameters() { return (Vector) ((Vector) parameters).clone(); }

  public static List getAllparameters(List behaviouralfeatures)
  { List result = new Vector();
    for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      result = Set.union(result, behaviouralfeaturex.getparameters()); }
    return result; }

  public static List getAllOrderedparameters(List behaviouralfeatures)
  { List result = new Vector();
    for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      result.addAll(behaviouralfeaturex.getparameters()); }
    return result; }

  public Statement getactivity() { return activity; }

  public static List getAllactivity(List behaviouralfeatures)
  { List result = new Vector();
    for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      if (result.contains(behaviouralfeaturex.getactivity())) {}
      else { result.add(behaviouralfeaturex.getactivity()); }
 }
    return result; }

  public static List getAllOrderedactivity(List behaviouralfeatures)
  { List result = new Vector();
    for (int _i = 0; _i < behaviouralfeatures.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(_i);
      if (result.contains(behaviouralfeaturex.getactivity())) {}
      else { result.add(behaviouralfeaturex.getactivity()); }
 }
    return result; }


}


class Operation
  extends BehaviouralFeature
  implements SystemTypes
{
  private boolean isQuery = false; // internal
  private boolean isCached = false; // internal
  private Entity owner;
  private List definers = new Vector(); // of Entity

  public Operation(Entity owner)
  {
    this.isQuery = false;
    this.isCached = false;
    this.owner = owner;

  }

  public Operation() { }



  public String toString()
  { String _res_ = "(Operation) ";
    _res_ = _res_ + isQuery + ",";
    _res_ = _res_ + isCached;
    return _res_ + super.toString();
  }

  public static Operation parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Operation operationx = new Operation();
    operationx.isQuery = Boolean.parseBoolean((String) _line1vals.get(0));
    operationx.isCached = Boolean.parseBoolean((String) _line1vals.get(1));
    return operationx;
  }


  public void writeCSV(PrintWriter _out)
  { Operation operationx = this;
    _out.print("" + operationx.isQuery);
    _out.print(" , ");
    _out.print("" + operationx.isCached);
    _out.println();
  }


  public void setisQuery(boolean isQuery_x) { isQuery = isQuery_x;  }


    public static void setAllisQuery(List operations,boolean val)
  { for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      Controller.inst().setisQuery(operationx,val); } }


  public void setisCached(boolean isCached_x) { isCached = isCached_x;  }


    public static void setAllisCached(List operations,boolean val)
  { for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      Controller.inst().setisCached(operationx,val); } }


  public void setowner(Entity owner_xx) { owner = owner_xx;
  }

  public static void setAllowner(List operations, Entity _val)
  { for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      Controller.inst().setowner(operationx, _val); } }

  public void setdefiners(List definers_xx) { definers = definers_xx;
    }
 
  public void setdefiners(int ind_x, Entity definers_xx) { if (ind_x >= 0 && ind_x < definers.size()) { definers.set(ind_x, definers_xx); } }

 public void adddefiners(Entity definers_xx) { definers.add(definers_xx);
    }
 
  public void removedefiners(Entity definers_xx) { Vector _removeddefinersdefiners_xx = new Vector();
  _removeddefinersdefiners_xx.add(definers_xx);
  definers.removeAll(_removeddefinersdefiners_xx);
    }

  public static void setAlldefiners(List operations, List _val)
  { for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      Controller.inst().setdefiners(operationx, _val); } }

  public static void setAlldefiners(List operations, int _ind,Entity _val)
  { for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      Controller.inst().setdefiners(operationx,_ind,_val); } }

  public static void addAlldefiners(List operations, Entity _val)
  { for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      Controller.inst().adddefiners(operationx,  _val); } }


  public static void removeAlldefiners(List operations,Entity _val)
  { for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      Controller.inst().removedefiners(operationx, _val); } }


  public static void unionAlldefiners(List operations, List _val)
  { for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      Controller.inst().uniondefiners(operationx, _val); } }


  public static void subtractAlldefiners(List operations,  List _val)
  { for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      Controller.inst().subtractdefiners(operationx,  _val); } }


    public boolean getisQuery() { return isQuery; }

    public static List getAllisQuery(List operations)
  { List result = new Vector();
    for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      if (result.contains(new Boolean(operationx.getisQuery()))) { }
      else { result.add(new Boolean(operationx.getisQuery())); } }
    return result; }

    public static List getAllOrderedisQuery(List operations)
  { List result = new Vector();
    for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      result.add(new Boolean(operationx.getisQuery())); } 
    return result; }

    public boolean getisCached() { return isCached; }

    public static List getAllisCached(List operations)
  { List result = new Vector();
    for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      if (result.contains(new Boolean(operationx.getisCached()))) { }
      else { result.add(new Boolean(operationx.getisCached())); } }
    return result; }

    public static List getAllOrderedisCached(List operations)
  { List result = new Vector();
    for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      result.add(new Boolean(operationx.getisCached())); } 
    return result; }

  public Entity getowner() { return owner; }

  public static List getAllowner(List operations)
  { List result = new Vector();
    for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      if (result.contains(operationx.getowner())) {}
      else { result.add(operationx.getowner()); }
 }
    return result; }

  public static List getAllOrderedowner(List operations)
  { List result = new Vector();
    for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      if (result.contains(operationx.getowner())) {}
      else { result.add(operationx.getowner()); }
 }
    return result; }

  public List getdefiners() { return (Vector) ((Vector) definers).clone(); }

  public static List getAlldefiners(List operations)
  { List result = new Vector();
    for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      result = Set.union(result, operationx.getdefiners()); }
    return result; }

  public static List getAllOrdereddefiners(List operations)
  { List result = new Vector();
    for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx = (Operation) operations.get(_i);
      result.addAll(operationx.getdefiners()); }
    return result; }

    public void program2c2()
  {   if (this.getisStatic() == false) 
  {   if (Controller.inst().getCOperationByPK(this.getname() + "_" + this.getowner().getname()) != null)
    { COperation op = Controller.inst().getCOperationByPK(this.getname() + "_" + this.getowner().getname());
     Controller.inst().setname(op,this.getname() + "_" + this.getowner().getname());
    Controller.inst().setinheritedFrom(op,"");
    Controller.inst().setscope(op,"entity");
    Controller.inst().setisQuery(op,this.getisQuery());
    Controller.inst().setisStatic(op,this.getisStatic());
    Controller.inst().setisAbstract(op,this.getisAbstract());
    Controller.inst().setclassname(op,this.getowner().getname());
    Controller.inst().setdefiners(op,Controller.inst().getCStructByPK(Entity.getAllOrderedname(this.getdefiners())));
    Controller.inst().setreturnType(op,Controller.inst().getCTypeByPK(this.gettype().gettypeId()));
    Controller.inst().setelementType(op,Controller.inst().getCTypeByPK(this.getelementType().gettypeId()));
    CVariable s = new CVariable();
    Controller.inst().addCVariable(s);
    Controller.inst().setname(s,"self");
    Controller.inst().settype(s,Controller.inst().getCTypeByPK(this.getowner().gettypeId()));
    Controller.inst().addparameters(op,s);
        List _property_list76 = new Vector();
    _property_list76.addAll(this.getparameters());
    for (int _ind77 = 0; _ind77 < _property_list76.size(); _ind77++)
    { Property p = (Property) _property_list76.get(_ind77);
      CVariable v = new CVariable();
    Controller.inst().addCVariable(v);
    Controller.inst().setname(v,p.getname());
    Controller.inst().settype(v,Controller.inst().getCTypeByPK(p.gettype().gettypeId()));
    Controller.inst().addparameters(op,v);
    }

  }
    else
    { COperation op = new COperation();
    Controller.inst().addCOperation(op);
    Controller.inst().setname(op,this.getname() + "_" + this.getowner().getname());
    Controller.inst().setopId(op,this.getname() + "_" + this.getowner().getname());
    Controller.inst().setinheritedFrom(op,"");
    Controller.inst().setscope(op,"entity");
    Controller.inst().setisQuery(op,this.getisQuery());
    Controller.inst().setisStatic(op,this.getisStatic());
    Controller.inst().setisAbstract(op,this.getisAbstract());
    Controller.inst().setclassname(op,this.getowner().getname());
    Controller.inst().setdefiners(op,Controller.inst().getCStructByPK(Entity.getAllOrderedname(this.getdefiners())));
    Controller.inst().setreturnType(op,Controller.inst().getCTypeByPK(this.gettype().gettypeId()));
    Controller.inst().setelementType(op,Controller.inst().getCTypeByPK(this.getelementType().gettypeId()));
    CVariable s = new CVariable();
    Controller.inst().addCVariable(s);
    Controller.inst().setname(s,"self");
    Controller.inst().settype(s,Controller.inst().getCTypeByPK(this.getowner().gettypeId()));
    Controller.inst().addparameters(op,s);
        List _property_list74 = new Vector();
    _property_list74.addAll(this.getparameters());
    for (int _ind75 = 0; _ind75 < _property_list74.size(); _ind75++)
    { Property p = (Property) _property_list74.get(_ind75);
      CVariable v = new CVariable();
    Controller.inst().addCVariable(v);
    Controller.inst().setname(v,p.getname());
    Controller.inst().settype(v,Controller.inst().getCTypeByPK(p.gettype().gettypeId()));
    Controller.inst().addparameters(op,v);
    }
 }
}
  }

    public void program2c4()
  {   if (this.getisStatic() == true) 
  {   if (Controller.inst().getCOperationByPK(this.getname() + "_" + this.getowner().getname()) != null)
    { COperation op = Controller.inst().getCOperationByPK(this.getname() + "_" + this.getowner().getname());
     Controller.inst().setname(op,this.getname());
    Controller.inst().setscope(op,"entity");
    Controller.inst().setisQuery(op,this.getisQuery());
    Controller.inst().setisStatic(op,this.getisStatic());
    Controller.inst().setclassname(op,this.getowner().getname());
    Controller.inst().setreturnType(op,Controller.inst().getCTypeByPK(this.gettype().gettypeId()));
    Controller.inst().setelementType(op,Controller.inst().getCTypeByPK(this.getelementType().gettypeId()));
        List _property_list80 = new Vector();
    _property_list80.addAll(this.getparameters());
    for (int _ind81 = 0; _ind81 < _property_list80.size(); _ind81++)
    { Property p = (Property) _property_list80.get(_ind81);
      CVariable v = new CVariable();
    Controller.inst().addCVariable(v);
    Controller.inst().setname(v,p.getname());
    Controller.inst().settype(v,Controller.inst().getCTypeByPK(p.gettype().gettypeId()));
    Controller.inst().addparameters(op,v);
    }

  }
    else
    { COperation op = new COperation();
    Controller.inst().addCOperation(op);
    Controller.inst().setname(op,this.getname());
    Controller.inst().setopId(op,this.getname() + "_" + this.getowner().getname());
    Controller.inst().setscope(op,"entity");
    Controller.inst().setisQuery(op,this.getisQuery());
    Controller.inst().setisStatic(op,this.getisStatic());
    Controller.inst().setclassname(op,this.getowner().getname());
    Controller.inst().setreturnType(op,Controller.inst().getCTypeByPK(this.gettype().gettypeId()));
    Controller.inst().setelementType(op,Controller.inst().getCTypeByPK(this.getelementType().gettypeId()));
        List _property_list78 = new Vector();
    _property_list78.addAll(this.getparameters());
    for (int _ind79 = 0; _ind79 < _property_list78.size(); _ind79++)
    { Property p = (Property) _property_list78.get(_ind79);
      CVariable v = new CVariable();
    Controller.inst().addCVariable(v);
    Controller.inst().setname(v,p.getname());
    Controller.inst().settype(v,Controller.inst().getCTypeByPK(p.gettype().gettypeId()));
    Controller.inst().addparameters(op,v);
    }
 }
}
  }


}


abstract class Expression
  implements SystemTypes
{
  protected boolean needsBracket = false; // internal
  protected int umlKind = value; // internal
  protected String expId = ""; // internal
  protected boolean isStatic = false; // internal
  protected Type type;
  protected Type elementType;

  public Expression(Type type,Type elementType)
  {
    this.needsBracket = false;
    this.umlKind = value;
    this.expId = "";
    this.isStatic = false;
    this.type = type;
    this.elementType = elementType;

  }

  public Expression() { }



  public String toString()
  { String _res_ = "(Expression) ";
    _res_ = _res_ + needsBracket + ",";
    _res_ = _res_ + umlKind + ",";
    _res_ = _res_ + expId + ",";
    _res_ = _res_ + isStatic;
    return _res_;
  }



  public void setneedsBracket(boolean needsBracket_x) { needsBracket = needsBracket_x;  }


    public static void setAllneedsBracket(List expressions,boolean val)
  { for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      Controller.inst().setneedsBracket(expressionx,val); } }


  public void setumlKind(int umlKind_x) { umlKind = umlKind_x;  }


    public static void setAllumlKind(List expressions,int val)
  { for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      Controller.inst().setumlKind(expressionx,val); } }


  public void setexpId(String expId_x) { expId = expId_x;  }


    public static void setAllexpId(List expressions,String val)
  { for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      Controller.inst().setexpId(expressionx,val); } }


  public void setisStatic(boolean isStatic_x) { isStatic = isStatic_x;  }


    public static void setAllisStatic(List expressions,boolean val)
  { for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      Controller.inst().setisStatic(expressionx,val); } }


  public void settype(Type type_xx) { type = type_xx;
  }

  public static void setAlltype(List expressions, Type _val)
  { for (int _i = 0; _i < expressions.size(); _i++)
    { Expression expressionx = (Expression) expressions.get(_i);
      Controller.inst().settype(expressionx, _val); } }

  public void setelementType(Type elementType_xx) { elementType = elementType_xx;
  }

  public static void setAllelementType(List expressions, Type _val)
  { for (int _i = 0; _i < expressions.size(); _i++)
    { Expression expressionx = (Expression) expressions.get(_i);
      Controller.inst().setelementType(expressionx, _val); } }

    public boolean getneedsBracket() { return needsBracket; }

    public static List getAllneedsBracket(List expressions)
  { List result = new Vector();
    for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      if (result.contains(new Boolean(expressionx.getneedsBracket()))) { }
      else { result.add(new Boolean(expressionx.getneedsBracket())); } }
    return result; }

    public static List getAllOrderedneedsBracket(List expressions)
  { List result = new Vector();
    for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      result.add(new Boolean(expressionx.getneedsBracket())); } 
    return result; }

    public int getumlKind() { return umlKind; }

    public static List getAllumlKind(List expressions)
  { List result = new Vector();
    for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      if (result.contains(new Integer(expressionx.getumlKind()))) { }
      else { result.add(new Integer(expressionx.getumlKind())); } }
    return result; }

    public static List getAllOrderedumlKind(List expressions)
  { List result = new Vector();
    for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      result.add(new Integer(expressionx.getumlKind())); } 
    return result; }

    public String getexpId() { return expId; }

    public static List getAllexpId(List expressions)
  { List result = new Vector();
    for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      if (result.contains(expressionx.getexpId())) { }
      else { result.add(expressionx.getexpId()); } }
    return result; }

    public static List getAllOrderedexpId(List expressions)
  { List result = new Vector();
    for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      result.add(expressionx.getexpId()); } 
    return result; }

    public boolean getisStatic() { return isStatic; }

    public static List getAllisStatic(List expressions)
  { List result = new Vector();
    for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      if (result.contains(new Boolean(expressionx.getisStatic()))) { }
      else { result.add(new Boolean(expressionx.getisStatic())); } }
    return result; }

    public static List getAllOrderedisStatic(List expressions)
  { List result = new Vector();
    for (int i = 0; i < expressions.size(); i++)
    { Expression expressionx = (Expression) expressions.get(i);
      result.add(new Boolean(expressionx.getisStatic())); } 
    return result; }

  public Type gettype() { return type; }

  public static List getAlltype(List expressions)
  { List result = new Vector();
    for (int _i = 0; _i < expressions.size(); _i++)
    { Expression expressionx = (Expression) expressions.get(_i);
      if (result.contains(expressionx.gettype())) {}
      else { result.add(expressionx.gettype()); }
 }
    return result; }

  public static List getAllOrderedtype(List expressions)
  { List result = new Vector();
    for (int _i = 0; _i < expressions.size(); _i++)
    { Expression expressionx = (Expression) expressions.get(_i);
      if (result.contains(expressionx.gettype())) {}
      else { result.add(expressionx.gettype()); }
 }
    return result; }

  public Type getelementType() { return elementType; }

  public static List getAllelementType(List expressions)
  { List result = new Vector();
    for (int _i = 0; _i < expressions.size(); _i++)
    { Expression expressionx = (Expression) expressions.get(_i);
      if (result.contains(expressionx.getelementType())) {}
      else { result.add(expressionx.getelementType()); }
 }
    return result; }

  public static List getAllOrderedelementType(List expressions)
  { List result = new Vector();
    for (int _i = 0; _i < expressions.size(); _i++)
    { Expression expressionx = (Expression) expressions.get(_i);
      if (result.contains(expressionx.getelementType())) {}
      else { result.add(expressionx.getelementType()); }
 }
    return result; }


}


class UseCase
  extends Classifier
  implements SystemTypes
{
  private boolean isGeneric = false; // internal
  private boolean isDerived = false; // internal
  private List parameters = new Vector(); // of Property
  private Statement classifierBehaviour;
  private Type resultType;

  public UseCase(Statement classifierBehaviour,Type resultType)
  {
    this.isGeneric = false;
    this.isDerived = false;
    this.classifierBehaviour = classifierBehaviour;
    this.resultType = resultType;

  }

  public UseCase() { }



  public String toString()
  { String _res_ = "(UseCase) ";
    _res_ = _res_ + isGeneric + ",";
    _res_ = _res_ + isDerived;
    return _res_ + super.toString();
  }

  public static UseCase parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    UseCase usecasex = new UseCase();
    usecasex.isGeneric = Boolean.parseBoolean((String) _line1vals.get(0));
    usecasex.isDerived = Boolean.parseBoolean((String) _line1vals.get(1));
    return usecasex;
  }


  public void writeCSV(PrintWriter _out)
  { UseCase usecasex = this;
    _out.print("" + usecasex.isGeneric);
    _out.print(" , ");
    _out.print("" + usecasex.isDerived);
    _out.println();
  }


  public void setisGeneric(boolean isGeneric_x) { isGeneric = isGeneric_x;  }


    public static void setAllisGeneric(List usecases,boolean val)
  { for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      Controller.inst().setisGeneric(usecasex,val); } }


  public void setisDerived(boolean isDerived_x) { isDerived = isDerived_x;  }


    public static void setAllisDerived(List usecases,boolean val)
  { for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      Controller.inst().setisDerived(usecasex,val); } }


  public void setparameters(List parameters_xx) { parameters = parameters_xx;
    }
 
  public void setparameters(int ind_x, Property parameters_xx) { if (ind_x >= 0 && ind_x < parameters.size()) { parameters.set(ind_x, parameters_xx); } }

 public void addparameters(Property parameters_xx) { parameters.add(parameters_xx);
    }
 
  public void removeparameters(Property parameters_xx) { Vector _removedparametersparameters_xx = new Vector();
  _removedparametersparameters_xx.add(parameters_xx);
  parameters.removeAll(_removedparametersparameters_xx);
    }

  public static void setAllparameters(List usecases, List _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().setparameters(usecasex, _val); } }

  public static void setAllparameters(List usecases, int _ind,Property _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().setparameters(usecasex,_ind,_val); } }

  public static void addAllparameters(List usecases, Property _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().addparameters(usecasex,  _val); } }


  public static void removeAllparameters(List usecases,Property _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().removeparameters(usecasex, _val); } }


  public static void unionAllparameters(List usecases, List _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().unionparameters(usecasex, _val); } }


  public static void subtractAllparameters(List usecases,  List _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().subtractparameters(usecasex,  _val); } }


  public void setclassifierBehaviour(Statement classifierBehaviour_xx) { classifierBehaviour = classifierBehaviour_xx;
  }

  public static void setAllclassifierBehaviour(List usecases, Statement _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().setclassifierBehaviour(usecasex, _val); } }

  public void setresultType(Type resultType_xx) { resultType = resultType_xx;
  }

  public static void setAllresultType(List usecases, Type _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().setresultType(usecasex, _val); } }

    public boolean getisGeneric() { return isGeneric; }

    public static List getAllisGeneric(List usecases)
  { List result = new Vector();
    for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      if (result.contains(new Boolean(usecasex.getisGeneric()))) { }
      else { result.add(new Boolean(usecasex.getisGeneric())); } }
    return result; }

    public static List getAllOrderedisGeneric(List usecases)
  { List result = new Vector();
    for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      result.add(new Boolean(usecasex.getisGeneric())); } 
    return result; }

    public boolean getisDerived() { return isDerived; }

    public static List getAllisDerived(List usecases)
  { List result = new Vector();
    for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      if (result.contains(new Boolean(usecasex.getisDerived()))) { }
      else { result.add(new Boolean(usecasex.getisDerived())); } }
    return result; }

    public static List getAllOrderedisDerived(List usecases)
  { List result = new Vector();
    for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      result.add(new Boolean(usecasex.getisDerived())); } 
    return result; }

  public List getparameters() { return (Vector) ((Vector) parameters).clone(); }

  public static List getAllparameters(List usecases)
  { List result = new Vector();
    for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      result = Set.union(result, usecasex.getparameters()); }
    return result; }

  public static List getAllOrderedparameters(List usecases)
  { List result = new Vector();
    for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      result.addAll(usecasex.getparameters()); }
    return result; }

  public Statement getclassifierBehaviour() { return classifierBehaviour; }

  public static List getAllclassifierBehaviour(List usecases)
  { List result = new Vector();
    for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      if (result.contains(usecasex.getclassifierBehaviour())) {}
      else { result.add(usecasex.getclassifierBehaviour()); }
 }
    return result; }

  public static List getAllOrderedclassifierBehaviour(List usecases)
  { List result = new Vector();
    for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      if (result.contains(usecasex.getclassifierBehaviour())) {}
      else { result.add(usecasex.getclassifierBehaviour()); }
 }
    return result; }

  public Type getresultType() { return resultType; }

  public static List getAllresultType(List usecases)
  { List result = new Vector();
    for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      if (result.contains(usecasex.getresultType())) {}
      else { result.add(usecasex.getresultType()); }
 }
    return result; }

  public static List getAllOrderedresultType(List usecases)
  { List result = new Vector();
    for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      if (result.contains(usecasex.getresultType())) {}
      else { result.add(usecasex.getresultType()); }
 }
    return result; }

    public void printcode34()
  {   if (((new SystemTypes.Set()).add(this.getclassifierBehaviour()).getElements()).size() > 0) 
  {   System.out.println("" + ( Controller.inst().getCTypeByPK(this.getresultType().gettypeId()) + " " + this.getname() + "(" + COperation.cParameterDeclaration(this.getparameters()) + ");\n" ));
}
  }


}


class CollectionType
  extends DataType
  implements SystemTypes
{
  private Type elementType;
  private Type keyType;

  public CollectionType(Type elementType,Type keyType)
  {
    this.elementType = elementType;
    this.keyType = keyType;

  }

  public CollectionType() { }



  public String toString()
  { String _res_ = "(CollectionType) ";
    return _res_ + super.toString();
  }

  public static CollectionType parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CollectionType collectiontypex = new CollectionType();
    return collectiontypex;
  }


  public void writeCSV(PrintWriter _out)
  { CollectionType collectiontypex = this;
    _out.println();
  }


  public void setelementType(Type elementType_xx) { elementType = elementType_xx;
  }

  public static void setAllelementType(List collectiontypes, Type _val)
  { for (int _i = 0; _i < collectiontypes.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) collectiontypes.get(_i);
      Controller.inst().setelementType(collectiontypex, _val); } }

  public void setkeyType(Type keyType_xx) { keyType = keyType_xx;
  }

  public static void setAllkeyType(List collectiontypes, Type _val)
  { for (int _i = 0; _i < collectiontypes.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) collectiontypes.get(_i);
      Controller.inst().setkeyType(collectiontypex, _val); } }

  public Type getelementType() { return elementType; }

  public static List getAllelementType(List collectiontypes)
  { List result = new Vector();
    for (int _i = 0; _i < collectiontypes.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) collectiontypes.get(_i);
      if (result.contains(collectiontypex.getelementType())) {}
      else { result.add(collectiontypex.getelementType()); }
 }
    return result; }

  public static List getAllOrderedelementType(List collectiontypes)
  { List result = new Vector();
    for (int _i = 0; _i < collectiontypes.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) collectiontypes.get(_i);
      if (result.contains(collectiontypex.getelementType())) {}
      else { result.add(collectiontypex.getelementType()); }
 }
    return result; }

  public Type getkeyType() { return keyType; }

  public static List getAllkeyType(List collectiontypes)
  { List result = new Vector();
    for (int _i = 0; _i < collectiontypes.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) collectiontypes.get(_i);
      if (result.contains(collectiontypex.getkeyType())) {}
      else { result.add(collectiontypex.getkeyType()); }
 }
    return result; }

  public static List getAllOrderedkeyType(List collectiontypes)
  { List result = new Vector();
    for (int _i = 0; _i < collectiontypes.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) collectiontypes.get(_i);
      if (result.contains(collectiontypex.getkeyType())) {}
      else { result.add(collectiontypex.getkeyType()); }
 }
    return result; }

    public void types2c11()
  {     CType pre_CType82 = Controller.inst().getCTypeByPK(elementType.gettypeId());
  if (((String) this.getname()).equals("Sequence") && !(this.getelementType().isValueType())) 
  {   if (Controller.inst().getCArrayTypeByPK(this.gettypeId()) != null)
    { CArrayType a = Controller.inst().getCArrayTypeByPK(this.gettypeId());
     Controller.inst().setduplicates(a,true);
    Controller.inst().setcomponentType(a,pre_CType82);
  }
    else
    { CArrayType a = new CArrayType();
    Controller.inst().addCArrayType(a);
    Controller.inst().setctypeId(a,this.gettypeId());
    Controller.inst().setduplicates(a,true);
    Controller.inst().setcomponentType(a,pre_CType82); }
}
  }

    public void types2c12()
  {     CType pre_CType83 = Controller.inst().getCTypeByPK(elementType.gettypeId());
  if (((String) this.getname()).equals("Sequence") && this.getelementType().isValueType()) 
  {   if (Controller.inst().getCArrayTypeByPK(this.gettypeId()) != null)
    { CArrayType a = Controller.inst().getCArrayTypeByPK(this.gettypeId());
     Controller.inst().setduplicates(a,true);
      if (Controller.inst().getCPointerTypeByPK(this.gettypeId() + "_elem") != null)
    { CPointerType cp = Controller.inst().getCPointerTypeByPK(this.gettypeId() + "_elem");
     Controller.inst().setcomponentType(a,cp);
    Controller.inst().setpointsTo(cp,pre_CType83);
  }
    else
    { CPointerType cp = new CPointerType();
    Controller.inst().addCPointerType(cp);
    Controller.inst().setctypeId(cp,this.gettypeId() + "_elem");
    Controller.inst().setcomponentType(a,cp);
    Controller.inst().setpointsTo(cp,pre_CType83); }

  }
    else
    { CArrayType a = new CArrayType();
    Controller.inst().addCArrayType(a);
    Controller.inst().setctypeId(a,this.gettypeId());
    Controller.inst().setduplicates(a,true);
      if (Controller.inst().getCPointerTypeByPK(this.gettypeId() + "_elem") != null)
    { CPointerType cp = Controller.inst().getCPointerTypeByPK(this.gettypeId() + "_elem");
     Controller.inst().setcomponentType(a,cp);
    Controller.inst().setpointsTo(cp,pre_CType83);
  }
    else
    { CPointerType cp = new CPointerType();
    Controller.inst().addCPointerType(cp);
    Controller.inst().setctypeId(cp,this.gettypeId() + "_elem");
    Controller.inst().setcomponentType(a,cp);
    Controller.inst().setpointsTo(cp,pre_CType83); }
 }
}
  }

    public void types2c13()
  {     CType pre_CType84 = Controller.inst().getCTypeByPK(elementType.gettypeId());
  if (((String) this.getname()).equals("Set") && !(this.getelementType().isValueType())) 
  {   if (Controller.inst().getCArrayTypeByPK(this.gettypeId()) != null)
    { CArrayType c = Controller.inst().getCArrayTypeByPK(this.gettypeId());
     Controller.inst().setduplicates(c,false);
    Controller.inst().setcomponentType(c,pre_CType84);
  }
    else
    { CArrayType c = new CArrayType();
    Controller.inst().addCArrayType(c);
    Controller.inst().setctypeId(c,this.gettypeId());
    Controller.inst().setduplicates(c,false);
    Controller.inst().setcomponentType(c,pre_CType84); }
}
  }

    public void types2c14()
  {     CType pre_CType85 = Controller.inst().getCTypeByPK(elementType.gettypeId());
  if (((String) this.getname()).equals("Set") && this.getelementType().isValueType()) 
  {   if (Controller.inst().getCArrayTypeByPK(this.gettypeId()) != null)
    { CArrayType c = Controller.inst().getCArrayTypeByPK(this.gettypeId());
     Controller.inst().setduplicates(c,false);
      if (Controller.inst().getCPointerTypeByPK(this.gettypeId() + "_elem") != null)
    { CPointerType cp = Controller.inst().getCPointerTypeByPK(this.gettypeId() + "_elem");
     Controller.inst().setcomponentType(c,cp);
    Controller.inst().setpointsTo(cp,pre_CType85);
  }
    else
    { CPointerType cp = new CPointerType();
    Controller.inst().addCPointerType(cp);
    Controller.inst().setctypeId(cp,this.gettypeId() + "_elem");
    Controller.inst().setcomponentType(c,cp);
    Controller.inst().setpointsTo(cp,pre_CType85); }

  }
    else
    { CArrayType c = new CArrayType();
    Controller.inst().addCArrayType(c);
    Controller.inst().setctypeId(c,this.gettypeId());
    Controller.inst().setduplicates(c,false);
      if (Controller.inst().getCPointerTypeByPK(this.gettypeId() + "_elem") != null)
    { CPointerType cp = Controller.inst().getCPointerTypeByPK(this.gettypeId() + "_elem");
     Controller.inst().setcomponentType(c,cp);
    Controller.inst().setpointsTo(cp,pre_CType85);
  }
    else
    { CPointerType cp = new CPointerType();
    Controller.inst().addCPointerType(cp);
    Controller.inst().setctypeId(cp,this.gettypeId() + "_elem");
    Controller.inst().setcomponentType(c,cp);
    Controller.inst().setpointsTo(cp,pre_CType85); }
 }
}
  }

    public void types2c15()
  {     CType pre_CType86 = Controller.inst().getCTypeByPK(elementType.gettypeId());
  if (((String) this.getname()).equals("Ref")) 
  {   if (Controller.inst().getCPointerTypeByPK(this.gettypeId()) != null)
    { CPointerType cp = Controller.inst().getCPointerTypeByPK(this.gettypeId());
     Controller.inst().setpointsTo(cp,pre_CType86);
  }
    else
    { CPointerType cp = new CPointerType();
    Controller.inst().addCPointerType(cp);
    Controller.inst().setctypeId(cp,this.gettypeId());
    Controller.inst().setpointsTo(cp,pre_CType86); }
}
  }

    public void types2c16()
  {   if (((String) this.getname()).equals("Map")) 
  {   if (Controller.inst().getCPointerTypeByPK(this.gettypeId()) != null)
    { CPointerType a = Controller.inst().getCPointerTypeByPK(this.gettypeId());
       if (Controller.inst().getCStructByPK("ocltnode") != null)
    { CStruct treeType = Controller.inst().getCStructByPK("ocltnode");
     Controller.inst().setctypeId(treeType,"ocltnode");
    Controller.inst().setpointsTo(a,treeType);
  }
    else
    { CStruct treeType = new CStruct();
    Controller.inst().addCStruct(treeType);
    Controller.inst().setname(treeType,"ocltnode");
    Controller.inst().setctypeId(treeType,"ocltnode");
    Controller.inst().setpointsTo(a,treeType); }

  }
    else
    { CPointerType a = new CPointerType();
    Controller.inst().addCPointerType(a);
    Controller.inst().setctypeId(a,this.gettypeId());
      if (Controller.inst().getCStructByPK("ocltnode") != null)
    { CStruct treeType = Controller.inst().getCStructByPK("ocltnode");
     Controller.inst().setctypeId(treeType,"ocltnode");
    Controller.inst().setpointsTo(a,treeType);
  }
    else
    { CStruct treeType = new CStruct();
    Controller.inst().addCStruct(treeType);
    Controller.inst().setname(treeType,"ocltnode");
    Controller.inst().setctypeId(treeType,"ocltnode");
    Controller.inst().setpointsTo(a,treeType); }
 }
}
  }

    public void types2c17()
  {     CType pre_CType87 = Controller.inst().getCTypeByPK(elementType.gettypeId());
    CType pre_CType88 = Controller.inst().getCTypeByPK(keyType.gettypeId());
  if (((String) this.getname()).equals("Function") && !((this.getkeyType() == null))) 
  {   if (Controller.inst().getCFunctionPointerTypeByPK(this.gettypeId()) != null)
    { CFunctionPointerType a = Controller.inst().getCFunctionPointerTypeByPK(this.gettypeId());
     Controller.inst().setrangeType(a,pre_CType87);
    Controller.inst().setdomainType(a,pre_CType88);
  }
    else
    { CFunctionPointerType a = new CFunctionPointerType();
    Controller.inst().addCFunctionPointerType(a);
    Controller.inst().setctypeId(a,this.gettypeId());
    Controller.inst().setrangeType(a,pre_CType87);
    Controller.inst().setdomainType(a,pre_CType88); }
}
  }


}


class CStruct
  extends CType
  implements SystemTypes
{
  private String name = ""; // internal
  private List interfaces = new Vector(); // of CStruct
  private List members = new Vector(); // of CMember
  private List allMembers = new Vector(); // of CMember

  public CStruct()
  {
    this.name = "";

  }



  public static CStruct parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CStruct cstructx = new CStruct();
    cstructx.name = (String) _line1vals.get(0);
      Controller.inst().cstructnameindex.put(cstructx.getname(), cstructx);
    return cstructx;
  }


  public void writeCSV(PrintWriter _out)
  { CStruct cstructx = this;
    _out.print("" + cstructx.name);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List cstructs,String val)
  { for (int i = 0; i < cstructs.size(); i++)
    { CStruct cstructx = (CStruct) cstructs.get(i);
      Controller.inst().setname(cstructx,val); } }


  public void setinterfaces(List interfaces_xx) { interfaces = interfaces_xx;
    }
 
  public void addinterfaces(CStruct interfaces_xx) { if (interfaces.contains(interfaces_xx)) {} else { interfaces.add(interfaces_xx); }
    }
 
  public void removeinterfaces(CStruct interfaces_xx) { Vector _removedinterfacesinterfaces_xx = new Vector();
  _removedinterfacesinterfaces_xx.add(interfaces_xx);
  interfaces.removeAll(_removedinterfacesinterfaces_xx);
    }

  public static void setAllinterfaces(List cstructs, List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().setinterfaces(cstructx, _val); } }

  public static void addAllinterfaces(List cstructs, CStruct _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().addinterfaces(cstructx,  _val); } }


  public static void removeAllinterfaces(List cstructs,CStruct _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().removeinterfaces(cstructx, _val); } }


  public static void unionAllinterfaces(List cstructs, List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().unioninterfaces(cstructx, _val); } }


  public static void subtractAllinterfaces(List cstructs,  List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().subtractinterfaces(cstructx,  _val); } }


  public void setmembers(List members_xx) { members = members_xx;
    }
 
  public void setmembers(int ind_x, CMember members_xx) { if (ind_x >= 0 && ind_x < members.size()) { members.set(ind_x, members_xx); } }

 public void addmembers(CMember members_xx) { members.add(members_xx);
    }
 
  public void removemembers(CMember members_xx) { Vector _removedmembersmembers_xx = new Vector();
  _removedmembersmembers_xx.add(members_xx);
  members.removeAll(_removedmembersmembers_xx);
    }

  public static void setAllmembers(List cstructs, List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().setmembers(cstructx, _val); } }

  public static void setAllmembers(List cstructs, int _ind,CMember _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().setmembers(cstructx,_ind,_val); } }

  public static void addAllmembers(List cstructs, CMember _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().addmembers(cstructx,  _val); } }


  public static void removeAllmembers(List cstructs,CMember _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().removemembers(cstructx, _val); } }


  public static void unionAllmembers(List cstructs, List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().unionmembers(cstructx, _val); } }


  public static void subtractAllmembers(List cstructs,  List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().subtractmembers(cstructx,  _val); } }


  public void setallMembers(List allMembers_xx) { allMembers = allMembers_xx;
    }
 
  public void addallMembers(CMember allMembers_xx) { if (allMembers.contains(allMembers_xx)) {} else { allMembers.add(allMembers_xx); }
    }
 
  public void removeallMembers(CMember allMembers_xx) { Vector _removedallMembersallMembers_xx = new Vector();
  _removedallMembersallMembers_xx.add(allMembers_xx);
  allMembers.removeAll(_removedallMembersallMembers_xx);
    }

  public static void setAllallMembers(List cstructs, List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().setallMembers(cstructx, _val); } }

  public static void addAllallMembers(List cstructs, CMember _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().addallMembers(cstructx,  _val); } }


  public static void removeAllallMembers(List cstructs,CMember _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().removeallMembers(cstructx, _val); } }


  public static void unionAllallMembers(List cstructs, List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().unionallMembers(cstructx, _val); } }


  public static void subtractAllallMembers(List cstructs,  List _val)
  { for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      Controller.inst().subtractallMembers(cstructx,  _val); } }


    public String getname() { return name; }

    public static List getAllname(List cstructs)
  { List result = new Vector();
    for (int i = 0; i < cstructs.size(); i++)
    { CStruct cstructx = (CStruct) cstructs.get(i);
      if (result.contains(cstructx.getname())) { }
      else { result.add(cstructx.getname()); } }
    return result; }

    public static List getAllOrderedname(List cstructs)
  { List result = new Vector();
    for (int i = 0; i < cstructs.size(); i++)
    { CStruct cstructx = (CStruct) cstructs.get(i);
      result.add(cstructx.getname()); } 
    return result; }

  public List getinterfaces() { return (Vector) ((Vector) interfaces).clone(); }

  public static List getAllinterfaces(List cstructs)
  { List result = new Vector();
    for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      result = Set.union(result, cstructx.getinterfaces()); }
    return result; }

  public static List getAllOrderedinterfaces(List cstructs)
  { List result = new Vector();
    for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      result = Set.union(result, cstructx.getinterfaces()); }
    return result; }

  public List getmembers() { return (Vector) ((Vector) members).clone(); }

  public static List getAllmembers(List cstructs)
  { List result = new Vector();
    for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      result = Set.union(result, cstructx.getmembers()); }
    return result; }

  public static List getAllOrderedmembers(List cstructs)
  { List result = new Vector();
    for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      result.addAll(cstructx.getmembers()); }
    return result; }

  public List getallMembers() { return (Vector) ((Vector) allMembers).clone(); }

  public static List getAllallMembers(List cstructs)
  { List result = new Vector();
    for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      result = Set.union(result, cstructx.getallMembers()); }
    return result; }

  public static List getAllOrderedallMembers(List cstructs)
  { List result = new Vector();
    for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructs.get(_i);
      result = Set.union(result, cstructx.getallMembers()); }
    return result; }

    public String toString()
  {   String result = "";
 
  result = "struct " + name;
    return result;
  }


    public List allCMembers()
  {   List result = new Vector();
 
  final List sups = Set.select_2(members); 
     if (sups.size() == 0) 
  {   result = members;
 
  }  else
      if (sups.size() > 0) 
  {   result = ( Set.concatenate(( Set.subtract(members,sups) ), ((CMember) Set.any(sups)).inheritedCMembers()) );
 
  }          return result;
  }


    public String createOpDec(String ent)
  {   String result = "";
 
  result = "struct " + ent + "* create" + ent + "(void);\n";
    return result;
  }


    public String createPKOpDec(String ent,String key)
  {   String result = "";
 
  result = "struct " + ent + "* create" + ent + "(char* _value);\n";
    return result;
  }


    public String concatenateOpDec()
  {   String result = "";
 
  result = "struct " + name + "** concatenate" + name + "(struct " + name + "* _col1[], struct " + name + "* _col2[]);\n";
    return result;
  }


    public String intersectionOpDec()
  {   String result = "";
 
  result = "struct " + name + "** intersection" + name + "(struct " + name + "* _col1[], struct " + name + "* _col2[]);\n";
    return result;
  }


    public String insertAtOpDec()
  {   String result = "";
 
  result = "struct " + name + "** insertAt" + name + "(struct " + name + "* _col1[], int ind, struct " + name + "* _col2[]);\n";
    return result;
  }


    public String exists1OpDec()
  {   String result = "";
 
  result = "unsigned char exists1" + name + "(struct " + name + "* _col[], unsigned char (*test)(struct " + name + "*));\n";
    return result;
  }


    public String isUniqueOpDec()
  {   String result = "";
 
  result = "unsigned char isUnique" + name + "(struct " + name + "* _col[], void* (*fe)(struct " + name + "*));\n";
    return result;
  }


    public String frontOpDec()
  {   String result = "";
 
  result = "struct " + name + "** front" + name + "(struct " + name + "* _col[]);\n";
    return result;
  }


    public String tailOpDec()
  {   String result = "";
 
  result = "struct " + name + "** tail" + name + "(struct " + name + "* _col[]);\n";
    return result;
  }


    public String removeAllOpDec()
  {   String result = "";
 
  result = "struct " + name + "** removeAll" + name + "(struct " + name + "* _col1[], struct " + name + "* _col2[]);\n";
    return result;
  }


    public String asSetOpDec()
  {   String result = "";
 
  result = "struct " + name + "** asSet" + name + "(struct " + name + "* _col[]);\n";
    return result;
  }


    public boolean isApplicationClass()
  {   boolean result = false;
 
  if (((String) name).equals("OclType")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclAttribute")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclOperation")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclProcess")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclProcessGroup")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("ocltnode")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclIterator")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclFile")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("MathLib")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclDate")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclDate")) 
  {   result = false;
 
  }  else
      {   if (((String) name).equals("OclException")) 
  {   result = false;
 
  }  else
      if (true) 
  {   result = true;
 
  }   
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public void printcode1()
  { Controller.inst().setallMembers(this,this.allCMembers());
  }

    public void printcode2()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + ( "struct " + this.getname() + " {" ));

        List _cmember_list89 = new Vector();
    _cmember_list89.addAll(this.getmembers());
    for (int _ind90 = 0; _ind90 < _cmember_list89.size(); _ind90++)
    { CMember m = (CMember) _cmember_list89.get(_ind90);
        System.out.println("" + m);

    }

      System.out.println("" + "};\n");
}
  }

    public void printcode4(CMember f)
  {   System.out.println("" + f.getterOpDec(this.getname()));

  }

    public void printcode4outer()
  {  CStruct cstructx = this;
      if (cstructx.isApplicationClass())
    {   List _range92 = new Vector();
  _range92.addAll(cstructx.getmembers());
  for (int _i91 = 0; _i91 < _range92.size(); _i91++)
  { CMember f = (CMember) _range92.get(_i91);
        if (!(f.getname().equals("super")))
    {     this.printcode4(f); }

  } }

  }


    public void printcode5(CMember f)
  {   System.out.println("" + f.inheritedGetterOpsDec(this.getname()));

  }

    public void printcode5outer()
  {  CStruct cstructx = this;
      if (cstructx.isApplicationClass())
    {   List _range94 = new Vector();
  _range94.addAll(cstructx.getmembers());
  for (int _i93 = 0; _i93 < _range94.size(); _i93++)
  { CMember f = (CMember) _range94.get(_i93);
        if (((String) f.getname()).equals("super"))
    {     this.printcode5(f); }

  } }

  }


    public void printcode6(CMember f)
  {   System.out.println("" + f.setterOpDec(this.getname()));

  }

    public void printcode6outer()
  {  CStruct cstructx = this;
      if (cstructx.isApplicationClass())
    {   List _range96 = new Vector();
  _range96.addAll(cstructx.getmembers());
  for (int _i95 = 0; _i95 < _range96.size(); _i95++)
  { CMember f = (CMember) _range96.get(_i95);
        if (!(f.getname().equals("super")))
    {     this.printcode6(f); }

  } }

  }


    public void printcode7(CMember f)
  {   System.out.println("" + f.inheritedSetterOpsDec(this.getname()));

  }

    public void printcode7outer()
  {  CStruct cstructx = this;
      if (cstructx.isApplicationClass())
    {   List _range98 = new Vector();
  _range98.addAll(cstructx.getmembers());
  for (int _i97 = 0; _i97 < _range98.size(); _i97++)
  { CMember f = (CMember) _range98.get(_i97);
        if (((String) f.getname()).equals("super"))
    {     this.printcode7(f); }

  } }

  }


    public void printcode8(CMember f)
  {   System.out.println("" + f.getAllOpDec(this.getname()));

  }

    public void printcode8outer()
  {  CStruct cstructx = this;
      if (cstructx.isApplicationClass())
    {   List _range100 = new Vector();
  _range100.addAll(cstructx.getallMembers());
  for (int _i99 = 0; _i99 < _range100.size(); _i99++)
  { CMember f = (CMember) _range100.get(_i99);
        if ((f.gettype() instanceof CPrimitiveType))
    {     this.printcode8(f); }

  } }

  }


    public void printcode9(CMember f)
  {   System.out.println("" + f.getAllOp1Dec(this.getname()));

  }

    public void printcode9outer()
  {  CStruct cstructx = this;
      if (cstructx.isApplicationClass())
    {   List _range102 = new Vector();
  _range102.addAll(cstructx.getallMembers());
  for (int _i101 = 0; _i101 < _range102.size(); _i101++)
  { CMember f = (CMember) _range102.get(_i101);
        if (!(f.getname().equals("super")))
    {     if ((f.gettype() instanceof CPointerType))
    {     this.printcode9(f); }
 }

  } }

  }


    public void printcode10(CMember key)
  {   System.out.println("" + key.getPKOpDec(this.getname()));

      System.out.println("" + key.getPKsOpDec(this.getname()));

  }

    public void printcode10outer()
  {  CStruct cstructx = this;
      if (cstructx.isApplicationClass())
    {     if (Set.exists_3(cstructx.getallMembers()))
    {   CMember key = ((CMember) Set.any(Set.select_4(cstructx.getallMembers())));
      this.printcode10(key);
 }
 }

  }


    public void printcode11()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + ( "struct " + this.getname() + "** append" + this.getname() + "(struct " + this.getname() + "* col[], struct " + this.getname() + "* ex);\n" ));
}
  }

    public void printcode12(CMember key)
  {   System.out.println("" + this.createPKOpDec(this.getname(),key.getname()));

  }

    public void printcode12outer()
  {  CStruct cstructx = this;
      if (cstructx.isApplicationClass())
    {     if (Set.exists_3(cstructx.getallMembers()))
    {   CMember key = ((CMember) Set.any(Set.select_4(cstructx.getallMembers())));
      this.printcode12(key);
 }
 }

  }


    public void printcode13()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + this.createOpDec(this.getname()));
}
  }

    public void printcode14()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + ( "struct " + this.getname() + "** insert" + this.getname() + "(struct " + this.getname() + "* col[], struct " + this.getname() + "* self);\n" ));
}
  }

    public void printcode15()
  {   if (this.isApplicationClass() && Set.exists_5(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + ( "struct " + this.getname() + "** select" + this.getname() + "(struct " + this.getname() + "* col[], unsigned char (*test)(struct " + this.getname() + "* self));\n" ));
}
  }

    public void printcode16()
  {   if (this.isApplicationClass() && Set.exists_6(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + ( "struct " + this.getname() + "** reject" + this.getname() + "(struct " + this.getname() + "* col[], unsigned char (*test)(struct " + this.getname() + "* self));\n" ));
}
  }

    public void printcode17()
  {   if (this.isApplicationClass() && Set.exists_7(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + ( "void** collect" + this.getname() + "(struct " + this.getname() + "* col[], void* (*fe)(struct " + this.getname() + "*));\n" ));

      System.out.println("" + ( "int** collect" + this.getname() + "int(struct " + this.getname() + "* col[], int (*fe)(struct " + this.getname() + "*));\n" ));

      System.out.println("" + ( "long** collect" + this.getname() + "long(struct " + this.getname() + "* col[], long (*fe)(struct " + this.getname() + "*));\n" ));

      System.out.println("" + ( "double** collect" + this.getname() + "double(struct " + this.getname() + "* col[], double (*fe)(struct " + this.getname() + "*));\n" ));

      System.out.println("" + ( "unsigned char** collect" + this.getname() + "boolean(struct " + this.getname() + "* col[], unsigned char (*fe)(struct " + this.getname() + "*));\n" ));
}
  }

    public void printcode18()
  {   if (this.isApplicationClass() && Set.exists_8(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + ( " unsigned char exists" + this.getname() + "(struct " + this.getname() + "* col[], unsigned char (*test)(struct " + this.getname() + "* ex));\n" ));
}
  }

    public void printcode19()
  {   if (this.isApplicationClass() && Set.exists_9(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + ( " unsigned char forAll" + this.getname() + "(struct " + this.getname() + "* col[], unsigned char (*test)(struct " + this.getname() + "* ex));" ));
}
  }

    public void printcode20()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + ( "  struct " + this.getname() + "** subrange" + this.getname() + "(struct " + this.getname() + "** col, int i, int j);" ));
}
  }

    public void printcode21()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + ( "  struct " + this.getname() + "** reverse" + this.getname() + "(struct " + this.getname() + "* col[]);" ));
}
  }

    public void printcode22()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + ( "struct " + this.getname() + "** remove" + this.getname() + "(struct " + this.getname() + "* col[], struct " + this.getname() + "* ex);" ));
}
  }

    public void printcode23()
  {   if (this.isApplicationClass() && Set.exists_10(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + ( "struct " + this.getname() + "** union" + this.getname() + "(struct " + this.getname() + "* col1[], struct " + this.getname() + "* col2[]);" ));
}
  }

    public void printcode24()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + this.removeAllOpDec());
}
  }

    public void printcode25()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + this.frontOpDec());
}
  }

    public void printcode26()
  {   if (this.isApplicationClass()) 
  {   System.out.println("" + this.tailOpDec());
}
  }

    public void printcode27()
  {   if (this.isApplicationClass() && Set.exists_11(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + this.concatenateOpDec());
}
  }

    public void printcode28()
  {   if (this.isApplicationClass() && Set.exists_12(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + this.intersectionOpDec());
}
  }

    public void printcode29()
  {   if (this.isApplicationClass() && Set.exists_13(Controller.inst().basicexpressions)) 
  {   System.out.println("" + this.insertAtOpDec());
}
  }

    public void printcode30()
  {   if (this.isApplicationClass() && Set.exists_14(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + this.exists1OpDec());
}
  }

    public void printcode31()
  {   if (this.isApplicationClass() && Set.exists_15(Controller.inst().binaryexpressions,this)) 
  {   System.out.println("" + this.isUniqueOpDec());
}
  }

    public void printcode32()
  {   if (this.isApplicationClass() && Set.exists_16(Controller.inst().unaryexpressions,this)) 
  {   System.out.println("" + this.asSetOpDec());
}
  }


}


class CMember
  implements SystemTypes
{
  private String name = ""; // internal
  private boolean isKey = false; // internal
  private String multiplicity = ""; // internal
  private CType type;

  public CMember(CType type)
  {
    this.name = "";
    this.isKey = false;
    this.multiplicity = "";
    this.type = type;

  }

  public CMember() { }



  public static CMember parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CMember cmemberx = new CMember();
    cmemberx.name = (String) _line1vals.get(0);
    cmemberx.isKey = Boolean.parseBoolean((String) _line1vals.get(1));
    cmemberx.multiplicity = (String) _line1vals.get(2);
    return cmemberx;
  }


  public void writeCSV(PrintWriter _out)
  { CMember cmemberx = this;
    _out.print("" + cmemberx.name);
    _out.print(" , ");
    _out.print("" + cmemberx.isKey);
    _out.print(" , ");
    _out.print("" + cmemberx.multiplicity);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List cmembers,String val)
  { for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      Controller.inst().setname(cmemberx,val); } }


  public void setisKey(boolean isKey_x) { isKey = isKey_x;  }


    public static void setAllisKey(List cmembers,boolean val)
  { for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      Controller.inst().setisKey(cmemberx,val); } }


  public void setmultiplicity(String multiplicity_x) { multiplicity = multiplicity_x;  }


    public static void setAllmultiplicity(List cmembers,String val)
  { for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      Controller.inst().setmultiplicity(cmemberx,val); } }


  public void settype(CType type_xx) { type = type_xx;
  }

  public static void setAlltype(List cmembers, CType _val)
  { for (int _i = 0; _i < cmembers.size(); _i++)
    { CMember cmemberx = (CMember) cmembers.get(_i);
      Controller.inst().settype(cmemberx, _val); } }

    public String getname() { return name; }

    public static List getAllname(List cmembers)
  { List result = new Vector();
    for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      if (result.contains(cmemberx.getname())) { }
      else { result.add(cmemberx.getname()); } }
    return result; }

    public static List getAllOrderedname(List cmembers)
  { List result = new Vector();
    for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      result.add(cmemberx.getname()); } 
    return result; }

    public boolean getisKey() { return isKey; }

    public static List getAllisKey(List cmembers)
  { List result = new Vector();
    for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      if (result.contains(new Boolean(cmemberx.getisKey()))) { }
      else { result.add(new Boolean(cmemberx.getisKey())); } }
    return result; }

    public static List getAllOrderedisKey(List cmembers)
  { List result = new Vector();
    for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      result.add(new Boolean(cmemberx.getisKey())); } 
    return result; }

    public String getmultiplicity() { return multiplicity; }

    public static List getAllmultiplicity(List cmembers)
  { List result = new Vector();
    for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      if (result.contains(cmemberx.getmultiplicity())) { }
      else { result.add(cmemberx.getmultiplicity()); } }
    return result; }

    public static List getAllOrderedmultiplicity(List cmembers)
  { List result = new Vector();
    for (int i = 0; i < cmembers.size(); i++)
    { CMember cmemberx = (CMember) cmembers.get(i);
      result.add(cmemberx.getmultiplicity()); } 
    return result; }

  public CType gettype() { return type; }

  public static List getAlltype(List cmembers)
  { List result = new Vector();
    for (int _i = 0; _i < cmembers.size(); _i++)
    { CMember cmemberx = (CMember) cmembers.get(_i);
      if (result.contains(cmemberx.gettype())) {}
      else { result.add(cmemberx.gettype()); }
 }
    return result; }

  public static List getAllOrderedtype(List cmembers)
  { List result = new Vector();
    for (int _i = 0; _i < cmembers.size(); _i++)
    { CMember cmemberx = (CMember) cmembers.get(_i);
      if (result.contains(cmemberx.gettype())) {}
      else { result.add(cmemberx.gettype()); }
 }
    return result; }

    public String toString()
  {   String result = "";
 
  if (isKey == true) 
  {   result = "  " + type + " " + name + "; /* key */";
 
  }  else
      {   if ((type instanceof CFunctionPointerType)) 
  {   result = "  " + CFunctionPointerType.declarationString(type,name) + ";";
 
  }  else
      if (true) 
  {   result = "  " + type + " " + name + ";";
 
  }   
   }     return result;
  }


    public List inheritedCMembers()
  {   List result = new Vector();
    if (!(type instanceof CPointerType) || !(((CPointerType) type).getpointsTo() instanceof CStruct)) { return result; } 
   
  final CType anc = ((CPointerType) type).getpointsTo(); 
     result = ((CStruct) anc).allCMembers();
       return result;
  }


    public String getterOpDec(String ent)
  {   String result = "";
 
    if ((type instanceof CFunctionPointerType))
    {   CType td = ((CFunctionPointerType) type).getdomainType();
    CType tr = ((CFunctionPointerType) type).getrangeType();
     result = tr + " (*get" + ent + "_" + name + "(struct " + ent + "* self))(" + td + ");\n";

 }
  else
      if (true) 
  {   result = type + " get" + ent + "_" + name + "(struct " + ent + "* self);\n";
 
  }       return result;
  }


    public String inheritedGetterOpDec(String ent,String sup)
  {   String result = "";
 
  if (!(name.equals("super"))) 
  {   result = type + " get" + ent + "_" + name + "(struct " + ent + "* self);\n";
 
  }  else
      if (((String) name).equals("super")) 
  {   result = this.ancestorGetterOpsDec(ent,sup);
 
  }       return result;
  }


    public String ancestorGetterOpsDec(String ent,String sup)
  {   String result = "";
    if (!(type instanceof CPointerType) || !(((CPointerType) type).getpointsTo() instanceof CStruct)) { return result; } 
   
  final CType anc = ((CPointerType) type).getpointsTo(); 
     result = Set.sumString(Set.collect_17(((CStruct) anc).getmembers(),ent,sup));
       return result;
  }


    public String inheritedGetterOpsDec(String ent)
  {   String result = "";
    if (!(type instanceof CPointerType) || !(((CPointerType) type).getpointsTo() instanceof CStruct)) { return result; } 
   
  final CType sup = ((CPointerType) type).getpointsTo(); 
     result = Set.sumString(Set.collect_18(((CStruct) sup).getmembers(),ent,sup));
       return result;
  }


    public String setterOpDec(String ent)
  {   String result = "";
 
    if ((type instanceof CFunctionPointerType))
    {   CType td = ((CFunctionPointerType) type).getdomainType();
    CType tr = ((CFunctionPointerType) type).getrangeType();
     result = "void set" + ent + "_" + name + "(struct " + ent + "* self, " + tr + " (*_value)(" + td + ");\n";

 }
  else
      if (true) 
  {   result = "void set" + ent + "_" + name + "(struct " + ent + "* self, " + type + " _value);\n";
 
  }       return result;
  }


    public String inheritedSetterOpDec(String ent,String sup)
  {   String result = "";
 
  if (!(name.equals("super"))) 
  {   result = "void set" + ent + "_" + name + "(struct " + ent + "* self, " + type + " _value);\n";
 
  }  else
      if (((String) name).equals("super")) 
  {   result = this.ancestorSetterOpsDec(ent,sup);
 
  }       return result;
  }


    public String ancestorSetterOpsDec(String ent,String sup)
  {   String result = "";
    if (!(type instanceof CPointerType) || !(((CPointerType) type).getpointsTo() instanceof CStruct)) { return result; } 
   
  final CType anc = ((CPointerType) type).getpointsTo(); 
     result = Set.sumString(Set.collect_19(((CStruct) anc).getmembers(),ent,sup));
       return result;
  }


    public String inheritedSetterOpsDec(String ent)
  {   String result = "";
    if (!(type instanceof CPointerType) || !(((CPointerType) type).getpointsTo() instanceof CStruct)) { return result; } 
   
  final CType sup = ((CPointerType) type).getpointsTo(); 
     result = Set.sumString(Set.collect_20(((CStruct) sup).getmembers(),ent,sup));
       return result;
  }


    public String getAllOpDec(String ent)
  {   String result = "";
 
  result = type + "** getAll" + ent + "_" + name + "(struct " + ent + "* _col[]);\n";
    return result;
  }


    public String getAllOp1Dec(String ent)
  {   String result = "";
 
  result = type + "* getAll" + ent + "_" + name + "(struct " + ent + "* _col[]);\n";
    return result;
  }


    public String inheritedAllOpDec(String ent,String sup)
  {   String result = "";
 
  if (!(name.equals("super")) && (type instanceof CPrimitiveType)) 
  {   result = this.getAllOpDec(ent);
 
  }  else
      {   if (!(name.equals("super")) && (type instanceof CPointerType)) 
  {   result = this.getAllOp1Dec(ent);
 
  }  else
      if (((String) name).equals("super")) 
  {   result = this.ancestorAllOpsDec(ent,sup);
 
  }   
   }     return result;
  }


    public String ancestorAllOpsDec(String ent,String sup)
  {   String result = "";
    if (!(type instanceof CPointerType)) { return result; } 
   
  final CType anc = ((CPointerType) type).getpointsTo(); 
     result = Set.sumString(Set.collect_21(((CStruct) anc).getmembers(),ent,sup));
       return result;
  }


    public String inheritedAllOpsDec(String ent)
  {   String result = "";
    if (!(type instanceof CPointerType)) { return result; } 
   
  final CType sup = ((CPointerType) type).getpointsTo(); 
     result = Set.sumString(Set.collect_22(((CStruct) sup).getmembers(),ent,sup));
       return result;
  }


    public String getPKOpDec(String ent)
  {   String result = "";
 
  final String e = ent.toLowerCase(); 
     result = "struct " + ent + "* get" + ent + "ByPK(char* _ex);\n";
       return result;
  }


    public String getPKsOpDec(String ent)
  {   String result = "";
 
  final String e = ent.toLowerCase(); 
     result = "struct " + ent + "** get" + ent + "ByPKs(char* _col[]);\n";
       return result;
  }


    public String initialiser()
  {   String result = "";
 
  if (isKey == true) 
  {   result = "";
 
  }  else
      {   if (((String) name).equals("super")) 
  {   result = "  result->super = create" + ((CStruct) ((CPointerType) type).getpointsTo()).getname() + "();\n";
 
  }  else
      {   if ((type instanceof CPointerType) || (type instanceof CArrayType) || (type instanceof CFunctionPointerType)) 
  {   result = "  result->" + name + " = NULL;\n";
 
  }  else
      if ((type instanceof CPrimitiveType)) 
  {   result = "  result->" + name + " = 0;\n";
 
  }   
   } 
   }     return result;
  }



}


abstract class CType
  implements SystemTypes
{
  protected String ctypeId = ""; // internal

  public CType()
  {
    this.ctypeId = "";

  }





  public void setctypeId(String ctypeId_x) { ctypeId = ctypeId_x;  }


    public static void setAllctypeId(List ctypes,String val)
  { for (int i = 0; i < ctypes.size(); i++)
    { CType ctypex = (CType) ctypes.get(i);
      Controller.inst().setctypeId(ctypex,val); } }


    public String getctypeId() { return ctypeId; }

    public static List getAllctypeId(List ctypes)
  { List result = new Vector();
    for (int i = 0; i < ctypes.size(); i++)
    { CType ctypex = (CType) ctypes.get(i);
      if (result.contains(ctypex.getctypeId())) { }
      else { result.add(ctypex.getctypeId()); } }
    return result; }

    public static List getAllOrderedctypeId(List ctypes)
  { List result = new Vector();
    for (int i = 0; i < ctypes.size(); i++)
    { CType ctypex = (CType) ctypes.get(i);
      result.add(ctypex.getctypeId()); } 
    return result; }

    public abstract String toString();




}


class COperation
  implements SystemTypes
{
  private String name = ""; // internal
  private String opId = ""; // internal
  private boolean isStatic = false; // internal
  private String scope = ""; // internal
  private boolean isQuery = false; // internal
  private String inheritedFrom = ""; // internal
  private boolean isAbstract = false; // internal
  private String classname = ""; // internal
  private List parameters = new Vector(); // of CVariable
  private List definers = new Vector(); // of CStruct
  private CType returnType;
  private CType elementType;

  public COperation(CType returnType,CType elementType)
  {
    this.name = "";
    this.opId = "";
    this.isStatic = false;
    this.scope = "";
    this.isQuery = false;
    this.inheritedFrom = "";
    this.isAbstract = false;
    this.classname = "";
    this.returnType = returnType;
    this.elementType = elementType;

  }

  public COperation() { }



  public String toString()
  { String _res_ = "(COperation) ";
    _res_ = _res_ + name + ",";
    _res_ = _res_ + opId + ",";
    _res_ = _res_ + isStatic + ",";
    _res_ = _res_ + scope + ",";
    _res_ = _res_ + isQuery + ",";
    _res_ = _res_ + inheritedFrom + ",";
    _res_ = _res_ + isAbstract + ",";
    _res_ = _res_ + classname;
    return _res_;
  }

  public static COperation parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    COperation coperationx = new COperation();
    coperationx.name = (String) _line1vals.get(0);
    coperationx.opId = (String) _line1vals.get(1);
      Controller.inst().coperationopIdindex.put(coperationx.getopId(), coperationx);
    coperationx.isStatic = Boolean.parseBoolean((String) _line1vals.get(2));
    coperationx.scope = (String) _line1vals.get(3);
    coperationx.isQuery = Boolean.parseBoolean((String) _line1vals.get(4));
    coperationx.inheritedFrom = (String) _line1vals.get(5);
    coperationx.isAbstract = Boolean.parseBoolean((String) _line1vals.get(6));
    coperationx.classname = (String) _line1vals.get(7);
    return coperationx;
  }


  public void writeCSV(PrintWriter _out)
  { COperation coperationx = this;
    _out.print("" + coperationx.name);
    _out.print(" , ");
    _out.print("" + coperationx.opId);
    _out.print(" , ");
    _out.print("" + coperationx.isStatic);
    _out.print(" , ");
    _out.print("" + coperationx.scope);
    _out.print(" , ");
    _out.print("" + coperationx.isQuery);
    _out.print(" , ");
    _out.print("" + coperationx.inheritedFrom);
    _out.print(" , ");
    _out.print("" + coperationx.isAbstract);
    _out.print(" , ");
    _out.print("" + coperationx.classname);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List coperations,String val)
  { for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      Controller.inst().setname(coperationx,val); } }


  public void setopId(String opId_x) { opId = opId_x;  }


    public static void setAllopId(List coperations,String val)
  { for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      Controller.inst().setopId(coperationx,val); } }


  public void setisStatic(boolean isStatic_x) { isStatic = isStatic_x;  }


    public static void setAllisStatic(List coperations,boolean val)
  { for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      Controller.inst().setisStatic(coperationx,val); } }


  public void setscope(String scope_x) { scope = scope_x;  }


    public static void setAllscope(List coperations,String val)
  { for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      Controller.inst().setscope(coperationx,val); } }


  public void setisQuery(boolean isQuery_x) { isQuery = isQuery_x;  }


    public static void setAllisQuery(List coperations,boolean val)
  { for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      Controller.inst().setisQuery(coperationx,val); } }


  public void setinheritedFrom(String inheritedFrom_x) { inheritedFrom = inheritedFrom_x;  }


    public static void setAllinheritedFrom(List coperations,String val)
  { for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      Controller.inst().setinheritedFrom(coperationx,val); } }


  public void setisAbstract(boolean isAbstract_x) { isAbstract = isAbstract_x;  }


    public static void setAllisAbstract(List coperations,boolean val)
  { for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      Controller.inst().setisAbstract(coperationx,val); } }


  public void setclassname(String classname_x) { classname = classname_x;  }


    public static void setAllclassname(List coperations,String val)
  { for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      Controller.inst().setclassname(coperationx,val); } }


  public void setparameters(List parameters_xx) { parameters = parameters_xx;
    }
 
  public void setparameters(int ind_x, CVariable parameters_xx) { if (ind_x >= 0 && ind_x < parameters.size()) { parameters.set(ind_x, parameters_xx); } }

 public void addparameters(CVariable parameters_xx) { parameters.add(parameters_xx);
    }
 
  public void removeparameters(CVariable parameters_xx) { Vector _removedparametersparameters_xx = new Vector();
  _removedparametersparameters_xx.add(parameters_xx);
  parameters.removeAll(_removedparametersparameters_xx);
    }

  public static void setAllparameters(List coperations, List _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().setparameters(coperationx, _val); } }

  public static void setAllparameters(List coperations, int _ind,CVariable _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().setparameters(coperationx,_ind,_val); } }

  public static void addAllparameters(List coperations, CVariable _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().addparameters(coperationx,  _val); } }


  public static void removeAllparameters(List coperations,CVariable _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().removeparameters(coperationx, _val); } }


  public static void unionAllparameters(List coperations, List _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().unionparameters(coperationx, _val); } }


  public static void subtractAllparameters(List coperations,  List _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().subtractparameters(coperationx,  _val); } }


  public void setdefiners(List definers_xx) { definers = definers_xx;
    }
 
  public void setdefiners(int ind_x, CStruct definers_xx) { if (ind_x >= 0 && ind_x < definers.size()) { definers.set(ind_x, definers_xx); } }

 public void adddefiners(CStruct definers_xx) { definers.add(definers_xx);
    }
 
  public void removedefiners(CStruct definers_xx) { Vector _removeddefinersdefiners_xx = new Vector();
  _removeddefinersdefiners_xx.add(definers_xx);
  definers.removeAll(_removeddefinersdefiners_xx);
    }

  public static void setAlldefiners(List coperations, List _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().setdefiners(coperationx, _val); } }

  public static void setAlldefiners(List coperations, int _ind,CStruct _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().setdefiners(coperationx,_ind,_val); } }

  public static void addAlldefiners(List coperations, CStruct _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().adddefiners(coperationx,  _val); } }


  public static void removeAlldefiners(List coperations,CStruct _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().removedefiners(coperationx, _val); } }


  public static void unionAlldefiners(List coperations, List _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().uniondefiners(coperationx, _val); } }


  public static void subtractAlldefiners(List coperations,  List _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().subtractdefiners(coperationx,  _val); } }


  public void setreturnType(CType returnType_xx) { returnType = returnType_xx;
  }

  public static void setAllreturnType(List coperations, CType _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().setreturnType(coperationx, _val); } }

  public void setelementType(CType elementType_xx) { elementType = elementType_xx;
  }

  public static void setAllelementType(List coperations, CType _val)
  { for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      Controller.inst().setelementType(coperationx, _val); } }

    public String getname() { return name; }

    public static List getAllname(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      if (result.contains(coperationx.getname())) { }
      else { result.add(coperationx.getname()); } }
    return result; }

    public static List getAllOrderedname(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      result.add(coperationx.getname()); } 
    return result; }

    public String getopId() { return opId; }

    public static List getAllopId(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      if (result.contains(coperationx.getopId())) { }
      else { result.add(coperationx.getopId()); } }
    return result; }

    public static List getAllOrderedopId(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      result.add(coperationx.getopId()); } 
    return result; }

    public boolean getisStatic() { return isStatic; }

    public static List getAllisStatic(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      if (result.contains(new Boolean(coperationx.getisStatic()))) { }
      else { result.add(new Boolean(coperationx.getisStatic())); } }
    return result; }

    public static List getAllOrderedisStatic(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      result.add(new Boolean(coperationx.getisStatic())); } 
    return result; }

    public String getscope() { return scope; }

    public static List getAllscope(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      if (result.contains(coperationx.getscope())) { }
      else { result.add(coperationx.getscope()); } }
    return result; }

    public static List getAllOrderedscope(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      result.add(coperationx.getscope()); } 
    return result; }

    public boolean getisQuery() { return isQuery; }

    public static List getAllisQuery(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      if (result.contains(new Boolean(coperationx.getisQuery()))) { }
      else { result.add(new Boolean(coperationx.getisQuery())); } }
    return result; }

    public static List getAllOrderedisQuery(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      result.add(new Boolean(coperationx.getisQuery())); } 
    return result; }

    public String getinheritedFrom() { return inheritedFrom; }

    public static List getAllinheritedFrom(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      if (result.contains(coperationx.getinheritedFrom())) { }
      else { result.add(coperationx.getinheritedFrom()); } }
    return result; }

    public static List getAllOrderedinheritedFrom(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      result.add(coperationx.getinheritedFrom()); } 
    return result; }

    public boolean getisAbstract() { return isAbstract; }

    public static List getAllisAbstract(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      if (result.contains(new Boolean(coperationx.getisAbstract()))) { }
      else { result.add(new Boolean(coperationx.getisAbstract())); } }
    return result; }

    public static List getAllOrderedisAbstract(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      result.add(new Boolean(coperationx.getisAbstract())); } 
    return result; }

    public String getclassname() { return classname; }

    public static List getAllclassname(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      if (result.contains(coperationx.getclassname())) { }
      else { result.add(coperationx.getclassname()); } }
    return result; }

    public static List getAllOrderedclassname(List coperations)
  { List result = new Vector();
    for (int i = 0; i < coperations.size(); i++)
    { COperation coperationx = (COperation) coperations.get(i);
      result.add(coperationx.getclassname()); } 
    return result; }

  public List getparameters() { return (Vector) ((Vector) parameters).clone(); }

  public static List getAllparameters(List coperations)
  { List result = new Vector();
    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      result = Set.union(result, coperationx.getparameters()); }
    return result; }

  public static List getAllOrderedparameters(List coperations)
  { List result = new Vector();
    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      result.addAll(coperationx.getparameters()); }
    return result; }

  public List getdefiners() { return (Vector) ((Vector) definers).clone(); }

  public static List getAlldefiners(List coperations)
  { List result = new Vector();
    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      result = Set.union(result, coperationx.getdefiners()); }
    return result; }

  public static List getAllOrdereddefiners(List coperations)
  { List result = new Vector();
    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      result.addAll(coperationx.getdefiners()); }
    return result; }

  public CType getreturnType() { return returnType; }

  public static List getAllreturnType(List coperations)
  { List result = new Vector();
    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      if (result.contains(coperationx.getreturnType())) {}
      else { result.add(coperationx.getreturnType()); }
 }
    return result; }

  public static List getAllOrderedreturnType(List coperations)
  { List result = new Vector();
    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      if (result.contains(coperationx.getreturnType())) {}
      else { result.add(coperationx.getreturnType()); }
 }
    return result; }

  public CType getelementType() { return elementType; }

  public static List getAllelementType(List coperations)
  { List result = new Vector();
    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      if (result.contains(coperationx.getelementType())) {}
      else { result.add(coperationx.getelementType()); }
 }
    return result; }

  public static List getAllOrderedelementType(List coperations)
  { List result = new Vector();
    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx = (COperation) coperations.get(_i);
      if (result.contains(coperationx.getelementType())) {}
      else { result.add(coperationx.getelementType()); }
 }
    return result; }

    public static String parameterDeclaration(List s)
  {   String result = "";
 
  if (s.size() == 0) 
  {   result = "void";
 
  }  else
      {     if (s.size() == 1)
    {   CVariable p = ((CVariable) s.get(1 - 1));
     result = p.parameterDeclaration();
 }
  else
        if (s.size() > 1)
    {   CVariable p = ((CVariable) s.get(1 - 1));
     result = p.parameterDeclaration() + ", " + COperation.parameterDeclaration(Set.tail(s));
 }
   
   }     return result;
  }


    public static String cParameterDeclaration(List pars)
  {   String result = "";
 
  if (pars.size() == 0) 
  {   result = "void";
 
  }  else
      {     if (pars.size() == 1)
    {   Property p = ((Property) pars.get(1 - 1));
     result = Controller.inst().getCTypeByPK(p.gettype().gettypeId()) + " " + p.getname();
 }
  else
        if (pars.size() > 1)
    {   Property p = ((Property) pars.get(1 - 1));
     result = Controller.inst().getCTypeByPK(p.gettype().gettypeId()) + " " + p.getname() + ", " + COperation.cParameterDeclaration(Set.tail(pars));
 }
   
   }     return result;
  }


    public String parameterNames(List s)
  {   String result = "";
 
  if (s.size() == 0) 
  {   result = "";
 
  }  else
      {     if (s.size() == 1)
    {   CVariable p = ((CVariable) s.get(1 - 1));
     result = p.getname();
 }
  else
        if (s.size() > 1)
    {   CVariable p = ((CVariable) s.get(1 - 1));
     result = p.getname() + ", " + this.parameterNames(Set.tail(s));
 }
   
   }     return result;
  }


    public String getDeclaration()
  {   String result = "";
 
  if ((new SystemTypes.Set()).add("OclType").add("OclAttribute").add("OclOperation").add("OclIterator").add("OclFile").add("OclDate").add("OclProcess").add("OclProcessGroup").add("ocltnode").add("MathLib").add("OclException").add("OclRandom").getElements().contains(classname)) 
  {   result = "";
 
  }  else
      if (true) 
  {   result = returnType + " " + name + "(" + COperation.parameterDeclaration(parameters) + ");\n";
 
  }       return result;
  }



}


class CVariable
  implements SystemTypes
{
  private String name = ""; // internal
  private String kind = ""; // internal
  private String initialisation = ""; // internal
  private CType type;

  public CVariable(CType type)
  {
    this.name = "";
    this.kind = "";
    this.initialisation = "";
    this.type = type;

  }

  public CVariable() { }



  public String toString()
  { String _res_ = "(CVariable) ";
    _res_ = _res_ + name + ",";
    _res_ = _res_ + kind + ",";
    _res_ = _res_ + initialisation;
    return _res_;
  }

  public static CVariable parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CVariable cvariablex = new CVariable();
    cvariablex.name = (String) _line1vals.get(0);
    cvariablex.kind = (String) _line1vals.get(1);
    cvariablex.initialisation = (String) _line1vals.get(2);
    return cvariablex;
  }


  public void writeCSV(PrintWriter _out)
  { CVariable cvariablex = this;
    _out.print("" + cvariablex.name);
    _out.print(" , ");
    _out.print("" + cvariablex.kind);
    _out.print(" , ");
    _out.print("" + cvariablex.initialisation);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List cvariables,String val)
  { for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      Controller.inst().setname(cvariablex,val); } }


  public void setkind(String kind_x) { kind = kind_x;  }


    public static void setAllkind(List cvariables,String val)
  { for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      Controller.inst().setkind(cvariablex,val); } }


  public void setinitialisation(String initialisation_x) { initialisation = initialisation_x;  }


    public static void setAllinitialisation(List cvariables,String val)
  { for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      Controller.inst().setinitialisation(cvariablex,val); } }


  public void settype(CType type_xx) { type = type_xx;
  }

  public static void setAlltype(List cvariables, CType _val)
  { for (int _i = 0; _i < cvariables.size(); _i++)
    { CVariable cvariablex = (CVariable) cvariables.get(_i);
      Controller.inst().settype(cvariablex, _val); } }

    public String getname() { return name; }

    public static List getAllname(List cvariables)
  { List result = new Vector();
    for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      if (result.contains(cvariablex.getname())) { }
      else { result.add(cvariablex.getname()); } }
    return result; }

    public static List getAllOrderedname(List cvariables)
  { List result = new Vector();
    for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      result.add(cvariablex.getname()); } 
    return result; }

    public String getkind() { return kind; }

    public static List getAllkind(List cvariables)
  { List result = new Vector();
    for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      if (result.contains(cvariablex.getkind())) { }
      else { result.add(cvariablex.getkind()); } }
    return result; }

    public static List getAllOrderedkind(List cvariables)
  { List result = new Vector();
    for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      result.add(cvariablex.getkind()); } 
    return result; }

    public String getinitialisation() { return initialisation; }

    public static List getAllinitialisation(List cvariables)
  { List result = new Vector();
    for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      if (result.contains(cvariablex.getinitialisation())) { }
      else { result.add(cvariablex.getinitialisation()); } }
    return result; }

    public static List getAllOrderedinitialisation(List cvariables)
  { List result = new Vector();
    for (int i = 0; i < cvariables.size(); i++)
    { CVariable cvariablex = (CVariable) cvariables.get(i);
      result.add(cvariablex.getinitialisation()); } 
    return result; }

  public CType gettype() { return type; }

  public static List getAlltype(List cvariables)
  { List result = new Vector();
    for (int _i = 0; _i < cvariables.size(); _i++)
    { CVariable cvariablex = (CVariable) cvariables.get(_i);
      if (result.contains(cvariablex.gettype())) {}
      else { result.add(cvariablex.gettype()); }
 }
    return result; }

  public static List getAllOrderedtype(List cvariables)
  { List result = new Vector();
    for (int _i = 0; _i < cvariables.size(); _i++)
    { CVariable cvariablex = (CVariable) cvariables.get(_i);
      if (result.contains(cvariablex.gettype())) {}
      else { result.add(cvariablex.gettype()); }
 }
    return result; }

    public String parameterDeclaration()
  {   String result = "";
 
  if ((type instanceof CFunctionPointerType)) 
  {   result = ((CFunctionPointerType) type).getrangeType() + " (*" + name + ")(" + ((CFunctionPointerType) type).getdomainType() + ")";
 
  }  else
      if (!(type instanceof CFunctionPointerType)) 
  {   result = type + " " + name;
 
  }       return result;
  }



}


class CProgram
  implements SystemTypes
{
  private List operations = new Vector(); // of COperation
  private List variables = new Vector(); // of CVariable
  private List structs = new Vector(); // of CStruct

  public CProgram()
  {

  }



  public String toString()
  { String _res_ = "(CProgram) ";
    return _res_;
  }

  public static CProgram parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CProgram cprogramx = new CProgram();
    return cprogramx;
  }


  public void writeCSV(PrintWriter _out)
  { CProgram cprogramx = this;
    _out.println();
  }


  public void setoperations(List operations_xx) { operations = operations_xx;
    }
 
  public void addoperations(COperation operations_xx) { if (operations.contains(operations_xx)) {} else { operations.add(operations_xx); }
    }
 
  public void removeoperations(COperation operations_xx) { Vector _removedoperationsoperations_xx = new Vector();
  _removedoperationsoperations_xx.add(operations_xx);
  operations.removeAll(_removedoperationsoperations_xx);
    }

  public static void setAlloperations(List cprograms, List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().setoperations(cprogramx, _val); } }

  public static void addAlloperations(List cprograms, COperation _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().addoperations(cprogramx,  _val); } }


  public static void removeAlloperations(List cprograms,COperation _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().removeoperations(cprogramx, _val); } }


  public static void unionAlloperations(List cprograms, List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().unionoperations(cprogramx, _val); } }


  public static void subtractAlloperations(List cprograms,  List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().subtractoperations(cprogramx,  _val); } }


  public void setvariables(List variables_xx) { variables = variables_xx;
    }
 
  public void addvariables(CVariable variables_xx) { if (variables.contains(variables_xx)) {} else { variables.add(variables_xx); }
    }
 
  public void removevariables(CVariable variables_xx) { Vector _removedvariablesvariables_xx = new Vector();
  _removedvariablesvariables_xx.add(variables_xx);
  variables.removeAll(_removedvariablesvariables_xx);
    }

  public static void setAllvariables(List cprograms, List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().setvariables(cprogramx, _val); } }

  public static void addAllvariables(List cprograms, CVariable _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().addvariables(cprogramx,  _val); } }


  public static void removeAllvariables(List cprograms,CVariable _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().removevariables(cprogramx, _val); } }


  public static void unionAllvariables(List cprograms, List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().unionvariables(cprogramx, _val); } }


  public static void subtractAllvariables(List cprograms,  List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().subtractvariables(cprogramx,  _val); } }


  public void setstructs(List structs_xx) { structs = structs_xx;
    }
 
  public void addstructs(CStruct structs_xx) { if (structs.contains(structs_xx)) {} else { structs.add(structs_xx); }
    }
 
  public void removestructs(CStruct structs_xx) { Vector _removedstructsstructs_xx = new Vector();
  _removedstructsstructs_xx.add(structs_xx);
  structs.removeAll(_removedstructsstructs_xx);
    }

  public static void setAllstructs(List cprograms, List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().setstructs(cprogramx, _val); } }

  public static void addAllstructs(List cprograms, CStruct _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().addstructs(cprogramx,  _val); } }


  public static void removeAllstructs(List cprograms,CStruct _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().removestructs(cprogramx, _val); } }


  public static void unionAllstructs(List cprograms, List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().unionstructs(cprogramx, _val); } }


  public static void subtractAllstructs(List cprograms,  List _val)
  { for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      Controller.inst().subtractstructs(cprogramx,  _val); } }


  public List getoperations() { return (Vector) ((Vector) operations).clone(); }

  public static List getAlloperations(List cprograms)
  { List result = new Vector();
    for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      result = Set.union(result, cprogramx.getoperations()); }
    return result; }

  public static List getAllOrderedoperations(List cprograms)
  { List result = new Vector();
    for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      result = Set.union(result, cprogramx.getoperations()); }
    return result; }

  public List getvariables() { return (Vector) ((Vector) variables).clone(); }

  public static List getAllvariables(List cprograms)
  { List result = new Vector();
    for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      result = Set.union(result, cprogramx.getvariables()); }
    return result; }

  public static List getAllOrderedvariables(List cprograms)
  { List result = new Vector();
    for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      result = Set.union(result, cprogramx.getvariables()); }
    return result; }

  public List getstructs() { return (Vector) ((Vector) structs).clone(); }

  public static List getAllstructs(List cprograms)
  { List result = new Vector();
    for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      result = Set.union(result, cprogramx.getstructs()); }
    return result; }

  public static List getAllOrderedstructs(List cprograms)
  { List result = new Vector();
    for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx = (CProgram) cprograms.get(_i);
      result = Set.union(result, cprogramx.getstructs()); }
    return result; }

    public void printOperationDecs()
  {     List _coperation_list103 = new Vector();
    _coperation_list103.addAll(this.getoperations());
    for (int _ind104 = 0; _ind104 < _coperation_list103.size(); _ind104++)
    { COperation op = (COperation) _coperation_list103.get(_ind104);
        System.out.println("" + op.getDeclaration());

    }

  }

    public void printLoadOpDecs()
  {   System.out.println("" + "unsigned char loadModel(void);");

  }

    public void printMainOperation()
  {   System.out.println("" + "int main() { return 0; }");

  }

    public void printDeclarations()
  { {} /* No update form for: true */
  }

    public void printcode33()
  { this.printOperationDecs();
  }


}


class CPrimitiveType
  extends CType
  implements SystemTypes
{
  private String name = ""; // internal

  public CPrimitiveType()
  {
    this.name = "";

  }



  public static CPrimitiveType parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CPrimitiveType cprimitivetypex = new CPrimitiveType();
    cprimitivetypex.name = (String) _line1vals.get(0);
    return cprimitivetypex;
  }


  public void writeCSV(PrintWriter _out)
  { CPrimitiveType cprimitivetypex = this;
    _out.print("" + cprimitivetypex.name);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List cprimitivetypes,String val)
  { for (int i = 0; i < cprimitivetypes.size(); i++)
    { CPrimitiveType cprimitivetypex = (CPrimitiveType) cprimitivetypes.get(i);
      Controller.inst().setname(cprimitivetypex,val); } }


    public String getname() { return name; }

    public static List getAllname(List cprimitivetypes)
  { List result = new Vector();
    for (int i = 0; i < cprimitivetypes.size(); i++)
    { CPrimitiveType cprimitivetypex = (CPrimitiveType) cprimitivetypes.get(i);
      if (result.contains(cprimitivetypex.getname())) { }
      else { result.add(cprimitivetypex.getname()); } }
    return result; }

    public static List getAllOrderedname(List cprimitivetypes)
  { List result = new Vector();
    for (int i = 0; i < cprimitivetypes.size(); i++)
    { CPrimitiveType cprimitivetypex = (CPrimitiveType) cprimitivetypes.get(i);
      result.add(cprimitivetypex.getname()); } 
    return result; }

    public String toString()
  {   String result = "";
 
  result = name;
    return result;
  }



}


class CArrayType
  extends CType
  implements SystemTypes
{
  private boolean duplicates = false; // internal
  private CType componentType;

  public CArrayType(CType componentType)
  {
    this.duplicates = false;
    this.componentType = componentType;

  }

  public CArrayType() { }



  public static CArrayType parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CArrayType carraytypex = new CArrayType();
    carraytypex.duplicates = Boolean.parseBoolean((String) _line1vals.get(0));
    return carraytypex;
  }


  public void writeCSV(PrintWriter _out)
  { CArrayType carraytypex = this;
    _out.print("" + carraytypex.duplicates);
    _out.println();
  }


  public void setduplicates(boolean duplicates_x) { duplicates = duplicates_x;  }


    public static void setAllduplicates(List carraytypes,boolean val)
  { for (int i = 0; i < carraytypes.size(); i++)
    { CArrayType carraytypex = (CArrayType) carraytypes.get(i);
      Controller.inst().setduplicates(carraytypex,val); } }


  public void setcomponentType(CType componentType_xx) { componentType = componentType_xx;
  }

  public static void setAllcomponentType(List carraytypes, CType _val)
  { for (int _i = 0; _i < carraytypes.size(); _i++)
    { CArrayType carraytypex = (CArrayType) carraytypes.get(_i);
      Controller.inst().setcomponentType(carraytypex, _val); } }

    public boolean getduplicates() { return duplicates; }

    public static List getAllduplicates(List carraytypes)
  { List result = new Vector();
    for (int i = 0; i < carraytypes.size(); i++)
    { CArrayType carraytypex = (CArrayType) carraytypes.get(i);
      if (result.contains(new Boolean(carraytypex.getduplicates()))) { }
      else { result.add(new Boolean(carraytypex.getduplicates())); } }
    return result; }

    public static List getAllOrderedduplicates(List carraytypes)
  { List result = new Vector();
    for (int i = 0; i < carraytypes.size(); i++)
    { CArrayType carraytypex = (CArrayType) carraytypes.get(i);
      result.add(new Boolean(carraytypex.getduplicates())); } 
    return result; }

  public CType getcomponentType() { return componentType; }

  public static List getAllcomponentType(List carraytypes)
  { List result = new Vector();
    for (int _i = 0; _i < carraytypes.size(); _i++)
    { CArrayType carraytypex = (CArrayType) carraytypes.get(_i);
      if (result.contains(carraytypex.getcomponentType())) {}
      else { result.add(carraytypex.getcomponentType()); }
 }
    return result; }

  public static List getAllOrderedcomponentType(List carraytypes)
  { List result = new Vector();
    for (int _i = 0; _i < carraytypes.size(); _i++)
    { CArrayType carraytypex = (CArrayType) carraytypes.get(_i);
      if (result.contains(carraytypex.getcomponentType())) {}
      else { result.add(carraytypex.getcomponentType()); }
 }
    return result; }

    public String toString()
  {   String result = "";
 
  result = componentType + "*";
    return result;
  }



}


class CPointerType
  extends CType
  implements SystemTypes
{
  private CType pointsTo;

  public CPointerType(CType pointsTo)
  {
    this.pointsTo = pointsTo;

  }

  public CPointerType() { }



  public static CPointerType parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CPointerType cpointertypex = new CPointerType();
    return cpointertypex;
  }


  public void writeCSV(PrintWriter _out)
  { CPointerType cpointertypex = this;
    _out.println();
  }


  public void setpointsTo(CType pointsTo_xx) { pointsTo = pointsTo_xx;
  }

  public static void setAllpointsTo(List cpointertypes, CType _val)
  { for (int _i = 0; _i < cpointertypes.size(); _i++)
    { CPointerType cpointertypex = (CPointerType) cpointertypes.get(_i);
      Controller.inst().setpointsTo(cpointertypex, _val); } }

  public CType getpointsTo() { return pointsTo; }

  public static List getAllpointsTo(List cpointertypes)
  { List result = new Vector();
    for (int _i = 0; _i < cpointertypes.size(); _i++)
    { CPointerType cpointertypex = (CPointerType) cpointertypes.get(_i);
      if (result.contains(cpointertypex.getpointsTo())) {}
      else { result.add(cpointertypex.getpointsTo()); }
 }
    return result; }

  public static List getAllOrderedpointsTo(List cpointertypes)
  { List result = new Vector();
    for (int _i = 0; _i < cpointertypes.size(); _i++)
    { CPointerType cpointertypex = (CPointerType) cpointertypes.get(_i);
      if (result.contains(cpointertypex.getpointsTo())) {}
      else { result.add(cpointertypex.getpointsTo()); }
 }
    return result; }

    public String toString()
  {   String result = "";
 
  result = pointsTo + "*";
    return result;
  }



}


class CFunctionPointerType
  extends CType
  implements SystemTypes
{
  private CType domainType;
  private CType rangeType;

  public CFunctionPointerType(CType domainType,CType rangeType)
  {
    this.domainType = domainType;
    this.rangeType = rangeType;

  }

  public CFunctionPointerType() { }



  public static CFunctionPointerType parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CFunctionPointerType cfunctionpointertypex = new CFunctionPointerType();
    return cfunctionpointertypex;
  }


  public void writeCSV(PrintWriter _out)
  { CFunctionPointerType cfunctionpointertypex = this;
    _out.println();
  }


  public void setdomainType(CType domainType_xx) { domainType = domainType_xx;
  }

  public static void setAlldomainType(List cfunctionpointertypes, CType _val)
  { for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) cfunctionpointertypes.get(_i);
      Controller.inst().setdomainType(cfunctionpointertypex, _val); } }

  public void setrangeType(CType rangeType_xx) { rangeType = rangeType_xx;
  }

  public static void setAllrangeType(List cfunctionpointertypes, CType _val)
  { for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) cfunctionpointertypes.get(_i);
      Controller.inst().setrangeType(cfunctionpointertypex, _val); } }

  public CType getdomainType() { return domainType; }

  public static List getAlldomainType(List cfunctionpointertypes)
  { List result = new Vector();
    for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) cfunctionpointertypes.get(_i);
      if (result.contains(cfunctionpointertypex.getdomainType())) {}
      else { result.add(cfunctionpointertypex.getdomainType()); }
 }
    return result; }

  public static List getAllOrdereddomainType(List cfunctionpointertypes)
  { List result = new Vector();
    for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) cfunctionpointertypes.get(_i);
      if (result.contains(cfunctionpointertypex.getdomainType())) {}
      else { result.add(cfunctionpointertypex.getdomainType()); }
 }
    return result; }

  public CType getrangeType() { return rangeType; }

  public static List getAllrangeType(List cfunctionpointertypes)
  { List result = new Vector();
    for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) cfunctionpointertypes.get(_i);
      if (result.contains(cfunctionpointertypex.getrangeType())) {}
      else { result.add(cfunctionpointertypex.getrangeType()); }
 }
    return result; }

  public static List getAllOrderedrangeType(List cfunctionpointertypes)
  { List result = new Vector();
    for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) cfunctionpointertypes.get(_i);
      if (result.contains(cfunctionpointertypex.getrangeType())) {}
      else { result.add(cfunctionpointertypex.getrangeType()); }
 }
    return result; }

    public String toString()
  {   String result = "";
 
  result = rangeType + " (*)(" + domainType + ")";
    return result;
  }


    public static String declarationString(CType t,String var)
  {   String result = "";
 
  if ((t instanceof CFunctionPointerType)) 
  {   result = ((CFunctionPointerType) t).getrangeType() + " (*" + var + ")(" + ((CFunctionPointerType) t).getdomainType() + ")";
 
  }  else
      if (true) 
  {   result = t + " " + var;
 
  }       return result;
  }



}


class CEnumeration
  extends CType
  implements SystemTypes
{
  private String name = ""; // internal
  private List ownedLiteral = new Vector(); // of CEnumerationLiteral

  public CEnumeration()
  {
    this.name = "";

  }



  public static CEnumeration parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CEnumeration cenumerationx = new CEnumeration();
    cenumerationx.name = (String) _line1vals.get(0);
    return cenumerationx;
  }


  public void writeCSV(PrintWriter _out)
  { CEnumeration cenumerationx = this;
    _out.print("" + cenumerationx.name);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List cenumerations,String val)
  { for (int i = 0; i < cenumerations.size(); i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(i);
      Controller.inst().setname(cenumerationx,val); } }


  public void setownedLiteral(List ownedLiteral_xx) { ownedLiteral = ownedLiteral_xx;
    }
 
  public void setownedLiteral(int ind_x, CEnumerationLiteral ownedLiteral_xx) { if (ind_x >= 0 && ind_x < ownedLiteral.size()) { ownedLiteral.set(ind_x, ownedLiteral_xx); } }

 public void addownedLiteral(CEnumerationLiteral ownedLiteral_xx) { ownedLiteral.add(ownedLiteral_xx);
    }
 
  public void removeownedLiteral(CEnumerationLiteral ownedLiteral_xx) { Vector _removedownedLiteralownedLiteral_xx = new Vector();
  _removedownedLiteralownedLiteral_xx.add(ownedLiteral_xx);
  ownedLiteral.removeAll(_removedownedLiteralownedLiteral_xx);
    }

  public static void setAllownedLiteral(List cenumerations, List _val)
  { for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(_i);
      Controller.inst().setownedLiteral(cenumerationx, _val); } }

  public static void setAllownedLiteral(List cenumerations, int _ind,CEnumerationLiteral _val)
  { for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(_i);
      Controller.inst().setownedLiteral(cenumerationx,_ind,_val); } }

  public static void addAllownedLiteral(List cenumerations, CEnumerationLiteral _val)
  { for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(_i);
      Controller.inst().addownedLiteral(cenumerationx,  _val); } }


  public static void removeAllownedLiteral(List cenumerations,CEnumerationLiteral _val)
  { for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(_i);
      Controller.inst().removeownedLiteral(cenumerationx, _val); } }


  public static void unionAllownedLiteral(List cenumerations, List _val)
  { for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(_i);
      Controller.inst().unionownedLiteral(cenumerationx, _val); } }


  public static void subtractAllownedLiteral(List cenumerations,  List _val)
  { for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(_i);
      Controller.inst().subtractownedLiteral(cenumerationx,  _val); } }


    public String getname() { return name; }

    public static List getAllname(List cenumerations)
  { List result = new Vector();
    for (int i = 0; i < cenumerations.size(); i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(i);
      if (result.contains(cenumerationx.getname())) { }
      else { result.add(cenumerationx.getname()); } }
    return result; }

    public static List getAllOrderedname(List cenumerations)
  { List result = new Vector();
    for (int i = 0; i < cenumerations.size(); i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(i);
      result.add(cenumerationx.getname()); } 
    return result; }

  public List getownedLiteral() { return (Vector) ((Vector) ownedLiteral).clone(); }

  public static List getAllownedLiteral(List cenumerations)
  { List result = new Vector();
    for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(_i);
      result = Set.union(result, cenumerationx.getownedLiteral()); }
    return result; }

  public static List getAllOrderedownedLiteral(List cenumerations)
  { List result = new Vector();
    for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(_i);
      result.addAll(cenumerationx.getownedLiteral()); }
    return result; }

    public String toString()
  {   String result = "";
 
  result = "enum " + name;
    return result;
  }


    public String declarationString()
  {   String result = "";
 
  result = "enum " + name + " { default" + name + Set.sumString(Set.collect_23(ownedLiteral)) + " };\n";
    return result;
  }


    public void printcode3()
  {   System.out.println("" + this.declarationString());

  }


}


class CEnumerationLiteral
  implements SystemTypes
{
  private String name = ""; // internal

  public CEnumerationLiteral()
  {
    this.name = "";

  }



  public String toString()
  { String _res_ = "(CEnumerationLiteral) ";
    _res_ = _res_ + name;
    return _res_;
  }

  public static CEnumerationLiteral parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CEnumerationLiteral cenumerationliteralx = new CEnumerationLiteral();
    cenumerationliteralx.name = (String) _line1vals.get(0);
    return cenumerationliteralx;
  }


  public void writeCSV(PrintWriter _out)
  { CEnumerationLiteral cenumerationliteralx = this;
    _out.print("" + cenumerationliteralx.name);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List cenumerationliterals,String val)
  { for (int i = 0; i < cenumerationliterals.size(); i++)
    { CEnumerationLiteral cenumerationliteralx = (CEnumerationLiteral) cenumerationliterals.get(i);
      Controller.inst().setname(cenumerationliteralx,val); } }


    public String getname() { return name; }

    public static List getAllname(List cenumerationliterals)
  { List result = new Vector();
    for (int i = 0; i < cenumerationliterals.size(); i++)
    { CEnumerationLiteral cenumerationliteralx = (CEnumerationLiteral) cenumerationliterals.get(i);
      if (result.contains(cenumerationliteralx.getname())) { }
      else { result.add(cenumerationliteralx.getname()); } }
    return result; }

    public static List getAllOrderedname(List cenumerationliterals)
  { List result = new Vector();
    for (int i = 0; i < cenumerationliterals.size(); i++)
    { CEnumerationLiteral cenumerationliteralx = (CEnumerationLiteral) cenumerationliterals.get(i);
      result.add(cenumerationliteralx.getname()); } 
    return result; }


}


class BinaryExpression
  extends Expression
  implements SystemTypes
{
  private String operator = ""; // internal
  private String variable = ""; // internal
  private Expression left;
  private Expression right;

  public BinaryExpression(Expression left,Expression right)
  {
    this.operator = "";
    this.variable = "";
    this.left = left;
    this.right = right;

  }

  public BinaryExpression() { }



  public String toString()
  { String _res_ = "(BinaryExpression) ";
    _res_ = _res_ + operator + ",";
    _res_ = _res_ + variable;
    return _res_ + super.toString();
  }

  public static BinaryExpression parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    BinaryExpression binaryexpressionx = new BinaryExpression();
    binaryexpressionx.operator = (String) _line1vals.get(0);
    binaryexpressionx.variable = (String) _line1vals.get(1);
    return binaryexpressionx;
  }


  public void writeCSV(PrintWriter _out)
  { BinaryExpression binaryexpressionx = this;
    _out.print("" + binaryexpressionx.operator);
    _out.print(" , ");
    _out.print("" + binaryexpressionx.variable);
    _out.println();
  }


  public void setoperator(String operator_x) { operator = operator_x;  }


    public static void setAlloperator(List binaryexpressions,String val)
  { for (int i = 0; i < binaryexpressions.size(); i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(i);
      Controller.inst().setoperator(binaryexpressionx,val); } }


  public void setvariable(String variable_x) { variable = variable_x;  }


    public static void setAllvariable(List binaryexpressions,String val)
  { for (int i = 0; i < binaryexpressions.size(); i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(i);
      Controller.inst().setvariable(binaryexpressionx,val); } }


  public void setleft(Expression left_xx) { left = left_xx;
  }

  public static void setAllleft(List binaryexpressions, Expression _val)
  { for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      Controller.inst().setleft(binaryexpressionx, _val); } }

  public void setright(Expression right_xx) { right = right_xx;
  }

  public static void setAllright(List binaryexpressions, Expression _val)
  { for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      Controller.inst().setright(binaryexpressionx, _val); } }

    public String getoperator() { return operator; }

    public static List getAlloperator(List binaryexpressions)
  { List result = new Vector();
    for (int i = 0; i < binaryexpressions.size(); i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(i);
      if (result.contains(binaryexpressionx.getoperator())) { }
      else { result.add(binaryexpressionx.getoperator()); } }
    return result; }

    public static List getAllOrderedoperator(List binaryexpressions)
  { List result = new Vector();
    for (int i = 0; i < binaryexpressions.size(); i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(i);
      result.add(binaryexpressionx.getoperator()); } 
    return result; }

    public String getvariable() { return variable; }

    public static List getAllvariable(List binaryexpressions)
  { List result = new Vector();
    for (int i = 0; i < binaryexpressions.size(); i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(i);
      if (result.contains(binaryexpressionx.getvariable())) { }
      else { result.add(binaryexpressionx.getvariable()); } }
    return result; }

    public static List getAllOrderedvariable(List binaryexpressions)
  { List result = new Vector();
    for (int i = 0; i < binaryexpressions.size(); i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(i);
      result.add(binaryexpressionx.getvariable()); } 
    return result; }

  public Expression getleft() { return left; }

  public static List getAllleft(List binaryexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      if (result.contains(binaryexpressionx.getleft())) {}
      else { result.add(binaryexpressionx.getleft()); }
 }
    return result; }

  public static List getAllOrderedleft(List binaryexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      if (result.contains(binaryexpressionx.getleft())) {}
      else { result.add(binaryexpressionx.getleft()); }
 }
    return result; }

  public Expression getright() { return right; }

  public static List getAllright(List binaryexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      if (result.contains(binaryexpressionx.getright())) {}
      else { result.add(binaryexpressionx.getright()); }
 }
    return result; }

  public static List getAllOrderedright(List binaryexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      if (result.contains(binaryexpressionx.getright())) {}
      else { result.add(binaryexpressionx.getright()); }
 }
    return result; }


}


class ConditionalExpression
  extends Expression
  implements SystemTypes
{
  private Expression test;
  private Expression ifExp;
  private Expression elseExp;

  public ConditionalExpression(Expression test,Expression ifExp,Expression elseExp)
  {
    this.test = test;
    this.ifExp = ifExp;
    this.elseExp = elseExp;

  }

  public ConditionalExpression() { }



  public String toString()
  { String _res_ = "(ConditionalExpression) ";
    return _res_ + super.toString();
  }

  public static ConditionalExpression parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    ConditionalExpression conditionalexpressionx = new ConditionalExpression();
    return conditionalexpressionx;
  }


  public void writeCSV(PrintWriter _out)
  { ConditionalExpression conditionalexpressionx = this;
    _out.println();
  }


  public void settest(Expression test_xx) { test = test_xx;
  }

  public static void setAlltest(List conditionalexpressions, Expression _val)
  { for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      Controller.inst().settest(conditionalexpressionx, _val); } }

  public void setifExp(Expression ifExp_xx) { ifExp = ifExp_xx;
  }

  public static void setAllifExp(List conditionalexpressions, Expression _val)
  { for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      Controller.inst().setifExp(conditionalexpressionx, _val); } }

  public void setelseExp(Expression elseExp_xx) { elseExp = elseExp_xx;
  }

  public static void setAllelseExp(List conditionalexpressions, Expression _val)
  { for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      Controller.inst().setelseExp(conditionalexpressionx, _val); } }

  public Expression gettest() { return test; }

  public static List getAlltest(List conditionalexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      if (result.contains(conditionalexpressionx.gettest())) {}
      else { result.add(conditionalexpressionx.gettest()); }
 }
    return result; }

  public static List getAllOrderedtest(List conditionalexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      if (result.contains(conditionalexpressionx.gettest())) {}
      else { result.add(conditionalexpressionx.gettest()); }
 }
    return result; }

  public Expression getifExp() { return ifExp; }

  public static List getAllifExp(List conditionalexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      if (result.contains(conditionalexpressionx.getifExp())) {}
      else { result.add(conditionalexpressionx.getifExp()); }
 }
    return result; }

  public static List getAllOrderedifExp(List conditionalexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      if (result.contains(conditionalexpressionx.getifExp())) {}
      else { result.add(conditionalexpressionx.getifExp()); }
 }
    return result; }

  public Expression getelseExp() { return elseExp; }

  public static List getAllelseExp(List conditionalexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      if (result.contains(conditionalexpressionx.getelseExp())) {}
      else { result.add(conditionalexpressionx.getelseExp()); }
 }
    return result; }

  public static List getAllOrderedelseExp(List conditionalexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(_i);
      if (result.contains(conditionalexpressionx.getelseExp())) {}
      else { result.add(conditionalexpressionx.getelseExp()); }
 }
    return result; }


}


class UnaryExpression
  extends Expression
  implements SystemTypes
{
  private String operator = ""; // internal
  private String variable = ""; // internal
  private Expression argument;

  public UnaryExpression(Expression argument)
  {
    this.operator = "";
    this.variable = "";
    this.argument = argument;

  }

  public UnaryExpression() { }



  public String toString()
  { String _res_ = "(UnaryExpression) ";
    _res_ = _res_ + operator + ",";
    _res_ = _res_ + variable;
    return _res_ + super.toString();
  }

  public static UnaryExpression parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    UnaryExpression unaryexpressionx = new UnaryExpression();
    unaryexpressionx.operator = (String) _line1vals.get(0);
    unaryexpressionx.variable = (String) _line1vals.get(1);
    return unaryexpressionx;
  }


  public void writeCSV(PrintWriter _out)
  { UnaryExpression unaryexpressionx = this;
    _out.print("" + unaryexpressionx.operator);
    _out.print(" , ");
    _out.print("" + unaryexpressionx.variable);
    _out.println();
  }


  public void setoperator(String operator_x) { operator = operator_x;  }


    public static void setAlloperator(List unaryexpressions,String val)
  { for (int i = 0; i < unaryexpressions.size(); i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(i);
      Controller.inst().setoperator(unaryexpressionx,val); } }


  public void setvariable(String variable_x) { variable = variable_x;  }


    public static void setAllvariable(List unaryexpressions,String val)
  { for (int i = 0; i < unaryexpressions.size(); i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(i);
      Controller.inst().setvariable(unaryexpressionx,val); } }


  public void setargument(Expression argument_xx) { argument = argument_xx;
  }

  public static void setAllargument(List unaryexpressions, Expression _val)
  { for (int _i = 0; _i < unaryexpressions.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(_i);
      Controller.inst().setargument(unaryexpressionx, _val); } }

    public String getoperator() { return operator; }

    public static List getAlloperator(List unaryexpressions)
  { List result = new Vector();
    for (int i = 0; i < unaryexpressions.size(); i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(i);
      if (result.contains(unaryexpressionx.getoperator())) { }
      else { result.add(unaryexpressionx.getoperator()); } }
    return result; }

    public static List getAllOrderedoperator(List unaryexpressions)
  { List result = new Vector();
    for (int i = 0; i < unaryexpressions.size(); i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(i);
      result.add(unaryexpressionx.getoperator()); } 
    return result; }

    public String getvariable() { return variable; }

    public static List getAllvariable(List unaryexpressions)
  { List result = new Vector();
    for (int i = 0; i < unaryexpressions.size(); i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(i);
      if (result.contains(unaryexpressionx.getvariable())) { }
      else { result.add(unaryexpressionx.getvariable()); } }
    return result; }

    public static List getAllOrderedvariable(List unaryexpressions)
  { List result = new Vector();
    for (int i = 0; i < unaryexpressions.size(); i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(i);
      result.add(unaryexpressionx.getvariable()); } 
    return result; }

  public Expression getargument() { return argument; }

  public static List getAllargument(List unaryexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < unaryexpressions.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(_i);
      if (result.contains(unaryexpressionx.getargument())) {}
      else { result.add(unaryexpressionx.getargument()); }
 }
    return result; }

  public static List getAllOrderedargument(List unaryexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < unaryexpressions.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(_i);
      if (result.contains(unaryexpressionx.getargument())) {}
      else { result.add(unaryexpressionx.getargument()); }
 }
    return result; }


}


class CollectionExpression
  extends Expression
  implements SystemTypes
{
  private boolean isOrdered = false; // internal
  private List elements = new Vector(); // of Expression

  public CollectionExpression()
  {
    this.isOrdered = false;

  }



  public String toString()
  { String _res_ = "(CollectionExpression) ";
    _res_ = _res_ + isOrdered;
    return _res_ + super.toString();
  }

  public static CollectionExpression parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CollectionExpression collectionexpressionx = new CollectionExpression();
    collectionexpressionx.isOrdered = Boolean.parseBoolean((String) _line1vals.get(0));
    return collectionexpressionx;
  }


  public void writeCSV(PrintWriter _out)
  { CollectionExpression collectionexpressionx = this;
    _out.print("" + collectionexpressionx.isOrdered);
    _out.println();
  }


  public void setisOrdered(boolean isOrdered_x) { isOrdered = isOrdered_x;  }


    public static void setAllisOrdered(List collectionexpressions,boolean val)
  { for (int i = 0; i < collectionexpressions.size(); i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(i);
      Controller.inst().setisOrdered(collectionexpressionx,val); } }


  public void setelements(List elements_xx) { elements = elements_xx;
    }
 
  public void addelements(Expression elements_xx) { if (elements.contains(elements_xx)) {} else { elements.add(elements_xx); }
    }
 
  public void removeelements(Expression elements_xx) { Vector _removedelementselements_xx = new Vector();
  _removedelementselements_xx.add(elements_xx);
  elements.removeAll(_removedelementselements_xx);
    }

  public static void setAllelements(List collectionexpressions, List _val)
  { for (int _i = 0; _i < collectionexpressions.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(_i);
      Controller.inst().setelements(collectionexpressionx, _val); } }

  public static void addAllelements(List collectionexpressions, Expression _val)
  { for (int _i = 0; _i < collectionexpressions.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(_i);
      Controller.inst().addelements(collectionexpressionx,  _val); } }


  public static void removeAllelements(List collectionexpressions,Expression _val)
  { for (int _i = 0; _i < collectionexpressions.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(_i);
      Controller.inst().removeelements(collectionexpressionx, _val); } }


  public static void unionAllelements(List collectionexpressions, List _val)
  { for (int _i = 0; _i < collectionexpressions.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(_i);
      Controller.inst().unionelements(collectionexpressionx, _val); } }


  public static void subtractAllelements(List collectionexpressions,  List _val)
  { for (int _i = 0; _i < collectionexpressions.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(_i);
      Controller.inst().subtractelements(collectionexpressionx,  _val); } }


    public boolean getisOrdered() { return isOrdered; }

    public static List getAllisOrdered(List collectionexpressions)
  { List result = new Vector();
    for (int i = 0; i < collectionexpressions.size(); i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(i);
      if (result.contains(new Boolean(collectionexpressionx.getisOrdered()))) { }
      else { result.add(new Boolean(collectionexpressionx.getisOrdered())); } }
    return result; }

    public static List getAllOrderedisOrdered(List collectionexpressions)
  { List result = new Vector();
    for (int i = 0; i < collectionexpressions.size(); i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(i);
      result.add(new Boolean(collectionexpressionx.getisOrdered())); } 
    return result; }

  public List getelements() { return (Vector) ((Vector) elements).clone(); }

  public static List getAllelements(List collectionexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < collectionexpressions.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(_i);
      result = Set.union(result, collectionexpressionx.getelements()); }
    return result; }

  public static List getAllOrderedelements(List collectionexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < collectionexpressions.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(_i);
      result = Set.union(result, collectionexpressionx.getelements()); }
    return result; }


}


class BasicExpression
  extends Expression
  implements SystemTypes
{
  private String data = ""; // internal
  private boolean prestate = false; // internal
  private List parameters = new Vector(); // of Expression
  private List referredProperty = new Vector(); // of Property
  private List context = new Vector(); // of Entity
  private List arrayIndex = new Vector(); // of Expression
  private List objectRef = new Vector(); // of Expression

  public BasicExpression()
  {
    this.data = "";
    this.prestate = false;

  }



  public String toString()
  { String _res_ = "(BasicExpression) ";
    _res_ = _res_ + data + ",";
    _res_ = _res_ + prestate;
    return _res_ + super.toString();
  }

  public static BasicExpression parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    BasicExpression basicexpressionx = new BasicExpression();
    basicexpressionx.data = (String) _line1vals.get(0);
    basicexpressionx.prestate = Boolean.parseBoolean((String) _line1vals.get(1));
    return basicexpressionx;
  }


  public void writeCSV(PrintWriter _out)
  { BasicExpression basicexpressionx = this;
    _out.print("" + basicexpressionx.data);
    _out.print(" , ");
    _out.print("" + basicexpressionx.prestate);
    _out.println();
  }


  public void setdata(String data_x) { data = data_x;  }


    public static void setAlldata(List basicexpressions,String val)
  { for (int i = 0; i < basicexpressions.size(); i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(i);
      Controller.inst().setdata(basicexpressionx,val); } }


  public void setprestate(boolean prestate_x) { prestate = prestate_x;  }


    public static void setAllprestate(List basicexpressions,boolean val)
  { for (int i = 0; i < basicexpressions.size(); i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(i);
      Controller.inst().setprestate(basicexpressionx,val); } }


  public void setparameters(List parameters_xx) { parameters = parameters_xx;
    }
 
  public void setparameters(int ind_x, Expression parameters_xx) { if (ind_x >= 0 && ind_x < parameters.size()) { parameters.set(ind_x, parameters_xx); } }

 public void addparameters(Expression parameters_xx) { parameters.add(parameters_xx);
    }
 
  public void removeparameters(Expression parameters_xx) { Vector _removedparametersparameters_xx = new Vector();
  _removedparametersparameters_xx.add(parameters_xx);
  parameters.removeAll(_removedparametersparameters_xx);
    }

  public static void setAllparameters(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().setparameters(basicexpressionx, _val); } }

  public static void setAllparameters(List basicexpressions, int _ind,Expression _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().setparameters(basicexpressionx,_ind,_val); } }

  public static void addAllparameters(List basicexpressions, Expression _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().addparameters(basicexpressionx,  _val); } }


  public static void removeAllparameters(List basicexpressions,Expression _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().removeparameters(basicexpressionx, _val); } }


  public static void unionAllparameters(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().unionparameters(basicexpressionx, _val); } }


  public static void subtractAllparameters(List basicexpressions,  List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().subtractparameters(basicexpressionx,  _val); } }


  public void setreferredProperty(List referredProperty_xx) { if (referredProperty_xx.size() > 1) { return; } 
    referredProperty = referredProperty_xx;
  }
 
  public void addreferredProperty(Property referredProperty_xx) { if (referredProperty.size() > 0) { referredProperty.clear(); } 
    if (referredProperty.contains(referredProperty_xx)) {} else { referredProperty.add(referredProperty_xx); }
    }
 
  public void removereferredProperty(Property referredProperty_xx) { Vector _removedreferredPropertyreferredProperty_xx = new Vector();
  _removedreferredPropertyreferredProperty_xx.add(referredProperty_xx);
  referredProperty.removeAll(_removedreferredPropertyreferredProperty_xx);
    }

  public static void setAllreferredProperty(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().setreferredProperty(basicexpressionx, _val); } }

  public static void addAllreferredProperty(List basicexpressions, Property _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().addreferredProperty(basicexpressionx,  _val); } }


  public static void removeAllreferredProperty(List basicexpressions,Property _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().removereferredProperty(basicexpressionx, _val); } }


  public static void unionAllreferredProperty(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().unionreferredProperty(basicexpressionx, _val); } }


  public static void subtractAllreferredProperty(List basicexpressions,  List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().subtractreferredProperty(basicexpressionx,  _val); } }


  public void setcontext(List context_xx) { context = context_xx;
    }
 
  public void addcontext(Entity context_xx) { if (context.contains(context_xx)) {} else { context.add(context_xx); }
    }
 
  public void removecontext(Entity context_xx) { Vector _removedcontextcontext_xx = new Vector();
  _removedcontextcontext_xx.add(context_xx);
  context.removeAll(_removedcontextcontext_xx);
    }

  public static void setAllcontext(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().setcontext(basicexpressionx, _val); } }

  public static void addAllcontext(List basicexpressions, Entity _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().addcontext(basicexpressionx,  _val); } }


  public static void removeAllcontext(List basicexpressions,Entity _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().removecontext(basicexpressionx, _val); } }


  public static void unionAllcontext(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().unioncontext(basicexpressionx, _val); } }


  public static void subtractAllcontext(List basicexpressions,  List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().subtractcontext(basicexpressionx,  _val); } }


  public void setarrayIndex(List arrayIndex_xx) { if (arrayIndex_xx.size() > 1) { return; } 
    arrayIndex = arrayIndex_xx;
  }
 
  public void addarrayIndex(Expression arrayIndex_xx) { if (arrayIndex.size() > 0) { arrayIndex.clear(); } 
    if (arrayIndex.contains(arrayIndex_xx)) {} else { arrayIndex.add(arrayIndex_xx); }
    }
 
  public void removearrayIndex(Expression arrayIndex_xx) { Vector _removedarrayIndexarrayIndex_xx = new Vector();
  _removedarrayIndexarrayIndex_xx.add(arrayIndex_xx);
  arrayIndex.removeAll(_removedarrayIndexarrayIndex_xx);
    }

  public static void setAllarrayIndex(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().setarrayIndex(basicexpressionx, _val); } }

  public static void addAllarrayIndex(List basicexpressions, Expression _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().addarrayIndex(basicexpressionx,  _val); } }


  public static void removeAllarrayIndex(List basicexpressions,Expression _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().removearrayIndex(basicexpressionx, _val); } }


  public static void unionAllarrayIndex(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().unionarrayIndex(basicexpressionx, _val); } }


  public static void subtractAllarrayIndex(List basicexpressions,  List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().subtractarrayIndex(basicexpressionx,  _val); } }


  public void setobjectRef(List objectRef_xx) { if (objectRef_xx.size() > 1) { return; } 
    objectRef = objectRef_xx;
  }
 
  public void addobjectRef(Expression objectRef_xx) { if (objectRef.size() > 0) { objectRef.clear(); } 
    if (objectRef.contains(objectRef_xx)) {} else { objectRef.add(objectRef_xx); }
    }
 
  public void removeobjectRef(Expression objectRef_xx) { Vector _removedobjectRefobjectRef_xx = new Vector();
  _removedobjectRefobjectRef_xx.add(objectRef_xx);
  objectRef.removeAll(_removedobjectRefobjectRef_xx);
    }

  public static void setAllobjectRef(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().setobjectRef(basicexpressionx, _val); } }

  public static void addAllobjectRef(List basicexpressions, Expression _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().addobjectRef(basicexpressionx,  _val); } }


  public static void removeAllobjectRef(List basicexpressions,Expression _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().removeobjectRef(basicexpressionx, _val); } }


  public static void unionAllobjectRef(List basicexpressions, List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().unionobjectRef(basicexpressionx, _val); } }


  public static void subtractAllobjectRef(List basicexpressions,  List _val)
  { for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      Controller.inst().subtractobjectRef(basicexpressionx,  _val); } }


    public String getdata() { return data; }

    public static List getAlldata(List basicexpressions)
  { List result = new Vector();
    for (int i = 0; i < basicexpressions.size(); i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(i);
      if (result.contains(basicexpressionx.getdata())) { }
      else { result.add(basicexpressionx.getdata()); } }
    return result; }

    public static List getAllOrdereddata(List basicexpressions)
  { List result = new Vector();
    for (int i = 0; i < basicexpressions.size(); i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(i);
      result.add(basicexpressionx.getdata()); } 
    return result; }

    public boolean getprestate() { return prestate; }

    public static List getAllprestate(List basicexpressions)
  { List result = new Vector();
    for (int i = 0; i < basicexpressions.size(); i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(i);
      if (result.contains(new Boolean(basicexpressionx.getprestate()))) { }
      else { result.add(new Boolean(basicexpressionx.getprestate())); } }
    return result; }

    public static List getAllOrderedprestate(List basicexpressions)
  { List result = new Vector();
    for (int i = 0; i < basicexpressions.size(); i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(i);
      result.add(new Boolean(basicexpressionx.getprestate())); } 
    return result; }

  public List getparameters() { return (Vector) ((Vector) parameters).clone(); }

  public static List getAllparameters(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getparameters()); }
    return result; }

  public static List getAllOrderedparameters(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result.addAll(basicexpressionx.getparameters()); }
    return result; }

  public List getreferredProperty() { return (Vector) ((Vector) referredProperty).clone(); }

  public static List getAllreferredProperty(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getreferredProperty()); }
    return result; }

  public static List getAllOrderedreferredProperty(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getreferredProperty()); }
    return result; }

  public List getcontext() { return (Vector) ((Vector) context).clone(); }

  public static List getAllcontext(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getcontext()); }
    return result; }

  public static List getAllOrderedcontext(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getcontext()); }
    return result; }

  public List getarrayIndex() { return (Vector) ((Vector) arrayIndex).clone(); }

  public static List getAllarrayIndex(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getarrayIndex()); }
    return result; }

  public static List getAllOrderedarrayIndex(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getarrayIndex()); }
    return result; }

  public List getobjectRef() { return (Vector) ((Vector) objectRef).clone(); }

  public static List getAllobjectRef(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getobjectRef()); }
    return result; }

  public static List getAllOrderedobjectRef(List basicexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(_i);
      result = Set.union(result, basicexpressionx.getobjectRef()); }
    return result; }


}


class Printcode
  implements SystemTypes
{

  public Printcode()
  {

  }



  public String toString()
  { String _res_ = "(Printcode) ";
    return _res_;
  }

  public static Printcode parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Printcode printcodex = new Printcode();
    return printcodex;
  }


  public void writeCSV(PrintWriter _out)
  { Printcode printcodex = this;
    _out.println();
  }


    public void printcode()
  { {} /* No update form for: true */
  }


}


class Types2C
  implements SystemTypes
{

  public Types2C()
  {

  }



  public String toString()
  { String _res_ = "(Types2C) ";
    return _res_;
  }

  public static Types2C parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Types2C types2cx = new Types2C();
    return types2cx;
  }


  public void writeCSV(PrintWriter _out)
  { Types2C types2cx = this;
    _out.println();
  }


    public void types2C()
  { {} /* No update form for: true */
  }


}


class Program2C
  implements SystemTypes
{

  public Program2C()
  {

  }



  public String toString()
  { String _res_ = "(Program2C) ";
    return _res_;
  }

  public static Program2C parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Program2C program2cx = new Program2C();
    return program2cx;
  }


  public void writeCSV(PrintWriter _out)
  { Program2C program2cx = this;
    _out.println();
  }


    public void program2C()
  { {} /* No update form for: true */
  }


}


abstract class Statement
  implements SystemTypes
{
  protected String statId = ""; // internal

  public Statement()
  {
    this.statId = "";

  }



  public String toString()
  { String _res_ = "(Statement) ";
    _res_ = _res_ + statId;
    return _res_;
  }



  public void setstatId(String statId_x) { statId = statId_x;  }


    public static void setAllstatId(List statements,String val)
  { for (int i = 0; i < statements.size(); i++)
    { Statement statementx = (Statement) statements.get(i);
      Controller.inst().setstatId(statementx,val); } }


    public String getstatId() { return statId; }

    public static List getAllstatId(List statements)
  { List result = new Vector();
    for (int i = 0; i < statements.size(); i++)
    { Statement statementx = (Statement) statements.get(i);
      if (result.contains(statementx.getstatId())) { }
      else { result.add(statementx.getstatId()); } }
    return result; }

    public static List getAllOrderedstatId(List statements)
  { List result = new Vector();
    for (int i = 0; i < statements.size(); i++)
    { Statement statementx = (Statement) statements.get(i);
      result.add(statementx.getstatId()); } 
    return result; }


}


class ReturnStatement
  extends Statement
  implements SystemTypes
{
  private List returnValue = new Vector(); // of Expression

  public ReturnStatement()
  {

  }



  public String toString()
  { String _res_ = "(ReturnStatement) ";
    return _res_ + super.toString();
  }

  public static ReturnStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    ReturnStatement returnstatementx = new ReturnStatement();
    return returnstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { ReturnStatement returnstatementx = this;
    _out.println();
  }


  public void setreturnValue(List returnValue_xx) { if (returnValue_xx.size() > 1) { return; } 
    returnValue = returnValue_xx;
  }
 
  public void addreturnValue(Expression returnValue_xx) { if (returnValue.size() > 0) { returnValue.clear(); } 
    if (returnValue.contains(returnValue_xx)) {} else { returnValue.add(returnValue_xx); }
    }
 
  public void removereturnValue(Expression returnValue_xx) { Vector _removedreturnValuereturnValue_xx = new Vector();
  _removedreturnValuereturnValue_xx.add(returnValue_xx);
  returnValue.removeAll(_removedreturnValuereturnValue_xx);
    }

  public static void setAllreturnValue(List returnstatements, List _val)
  { for (int _i = 0; _i < returnstatements.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(_i);
      Controller.inst().setreturnValue(returnstatementx, _val); } }

  public static void addAllreturnValue(List returnstatements, Expression _val)
  { for (int _i = 0; _i < returnstatements.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(_i);
      Controller.inst().addreturnValue(returnstatementx,  _val); } }


  public static void removeAllreturnValue(List returnstatements,Expression _val)
  { for (int _i = 0; _i < returnstatements.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(_i);
      Controller.inst().removereturnValue(returnstatementx, _val); } }


  public static void unionAllreturnValue(List returnstatements, List _val)
  { for (int _i = 0; _i < returnstatements.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(_i);
      Controller.inst().unionreturnValue(returnstatementx, _val); } }


  public static void subtractAllreturnValue(List returnstatements,  List _val)
  { for (int _i = 0; _i < returnstatements.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(_i);
      Controller.inst().subtractreturnValue(returnstatementx,  _val); } }


  public List getreturnValue() { return (Vector) ((Vector) returnValue).clone(); }

  public static List getAllreturnValue(List returnstatements)
  { List result = new Vector();
    for (int _i = 0; _i < returnstatements.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(_i);
      result = Set.union(result, returnstatementx.getreturnValue()); }
    return result; }

  public static List getAllOrderedreturnValue(List returnstatements)
  { List result = new Vector();
    for (int _i = 0; _i < returnstatements.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(_i);
      result = Set.union(result, returnstatementx.getreturnValue()); }
    return result; }


}


class BreakStatement
  extends Statement
  implements SystemTypes
{

  public BreakStatement()
  {

  }



  public String toString()
  { String _res_ = "(BreakStatement) ";
    return _res_ + super.toString();
  }

  public static BreakStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    BreakStatement breakstatementx = new BreakStatement();
    return breakstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { BreakStatement breakstatementx = this;
    _out.println();
  }



}


class OperationCallStatement
  extends Statement
  implements SystemTypes
{
  private String assignsTo = ""; // internal
  private Expression callExp;

  public OperationCallStatement(Expression callExp)
  {
    this.assignsTo = "";
    this.callExp = callExp;

  }

  public OperationCallStatement() { }



  public String toString()
  { String _res_ = "(OperationCallStatement) ";
    _res_ = _res_ + assignsTo;
    return _res_ + super.toString();
  }

  public static OperationCallStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    OperationCallStatement operationcallstatementx = new OperationCallStatement();
    operationcallstatementx.assignsTo = (String) _line1vals.get(0);
    return operationcallstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { OperationCallStatement operationcallstatementx = this;
    _out.print("" + operationcallstatementx.assignsTo);
    _out.println();
  }


  public void setassignsTo(String assignsTo_x) { assignsTo = assignsTo_x;  }


    public static void setAllassignsTo(List operationcallstatements,String val)
  { for (int i = 0; i < operationcallstatements.size(); i++)
    { OperationCallStatement operationcallstatementx = (OperationCallStatement) operationcallstatements.get(i);
      Controller.inst().setassignsTo(operationcallstatementx,val); } }


  public void setcallExp(Expression callExp_xx) { callExp = callExp_xx;
  }

  public static void setAllcallExp(List operationcallstatements, Expression _val)
  { for (int _i = 0; _i < operationcallstatements.size(); _i++)
    { OperationCallStatement operationcallstatementx = (OperationCallStatement) operationcallstatements.get(_i);
      Controller.inst().setcallExp(operationcallstatementx, _val); } }

    public String getassignsTo() { return assignsTo; }

    public static List getAllassignsTo(List operationcallstatements)
  { List result = new Vector();
    for (int i = 0; i < operationcallstatements.size(); i++)
    { OperationCallStatement operationcallstatementx = (OperationCallStatement) operationcallstatements.get(i);
      if (result.contains(operationcallstatementx.getassignsTo())) { }
      else { result.add(operationcallstatementx.getassignsTo()); } }
    return result; }

    public static List getAllOrderedassignsTo(List operationcallstatements)
  { List result = new Vector();
    for (int i = 0; i < operationcallstatements.size(); i++)
    { OperationCallStatement operationcallstatementx = (OperationCallStatement) operationcallstatements.get(i);
      result.add(operationcallstatementx.getassignsTo()); } 
    return result; }

  public Expression getcallExp() { return callExp; }

  public static List getAllcallExp(List operationcallstatements)
  { List result = new Vector();
    for (int _i = 0; _i < operationcallstatements.size(); _i++)
    { OperationCallStatement operationcallstatementx = (OperationCallStatement) operationcallstatements.get(_i);
      if (result.contains(operationcallstatementx.getcallExp())) {}
      else { result.add(operationcallstatementx.getcallExp()); }
 }
    return result; }

  public static List getAllOrderedcallExp(List operationcallstatements)
  { List result = new Vector();
    for (int _i = 0; _i < operationcallstatements.size(); _i++)
    { OperationCallStatement operationcallstatementx = (OperationCallStatement) operationcallstatements.get(_i);
      if (result.contains(operationcallstatementx.getcallExp())) {}
      else { result.add(operationcallstatementx.getcallExp()); }
 }
    return result; }


}


class ImplicitCallStatement
  extends Statement
  implements SystemTypes
{
  private String assignsTo = ""; // internal
  private Expression callExp;

  public ImplicitCallStatement(Expression callExp)
  {
    this.assignsTo = "";
    this.callExp = callExp;

  }

  public ImplicitCallStatement() { }



  public String toString()
  { String _res_ = "(ImplicitCallStatement) ";
    _res_ = _res_ + assignsTo;
    return _res_ + super.toString();
  }

  public static ImplicitCallStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    ImplicitCallStatement implicitcallstatementx = new ImplicitCallStatement();
    implicitcallstatementx.assignsTo = (String) _line1vals.get(0);
    return implicitcallstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { ImplicitCallStatement implicitcallstatementx = this;
    _out.print("" + implicitcallstatementx.assignsTo);
    _out.println();
  }


  public void setassignsTo(String assignsTo_x) { assignsTo = assignsTo_x;  }


    public static void setAllassignsTo(List implicitcallstatements,String val)
  { for (int i = 0; i < implicitcallstatements.size(); i++)
    { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) implicitcallstatements.get(i);
      Controller.inst().setassignsTo(implicitcallstatementx,val); } }


  public void setcallExp(Expression callExp_xx) { callExp = callExp_xx;
  }

  public static void setAllcallExp(List implicitcallstatements, Expression _val)
  { for (int _i = 0; _i < implicitcallstatements.size(); _i++)
    { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) implicitcallstatements.get(_i);
      Controller.inst().setcallExp(implicitcallstatementx, _val); } }

    public String getassignsTo() { return assignsTo; }

    public static List getAllassignsTo(List implicitcallstatements)
  { List result = new Vector();
    for (int i = 0; i < implicitcallstatements.size(); i++)
    { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) implicitcallstatements.get(i);
      if (result.contains(implicitcallstatementx.getassignsTo())) { }
      else { result.add(implicitcallstatementx.getassignsTo()); } }
    return result; }

    public static List getAllOrderedassignsTo(List implicitcallstatements)
  { List result = new Vector();
    for (int i = 0; i < implicitcallstatements.size(); i++)
    { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) implicitcallstatements.get(i);
      result.add(implicitcallstatementx.getassignsTo()); } 
    return result; }

  public Expression getcallExp() { return callExp; }

  public static List getAllcallExp(List implicitcallstatements)
  { List result = new Vector();
    for (int _i = 0; _i < implicitcallstatements.size(); _i++)
    { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) implicitcallstatements.get(_i);
      if (result.contains(implicitcallstatementx.getcallExp())) {}
      else { result.add(implicitcallstatementx.getcallExp()); }
 }
    return result; }

  public static List getAllOrderedcallExp(List implicitcallstatements)
  { List result = new Vector();
    for (int _i = 0; _i < implicitcallstatements.size(); _i++)
    { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) implicitcallstatements.get(_i);
      if (result.contains(implicitcallstatementx.getcallExp())) {}
      else { result.add(implicitcallstatementx.getcallExp()); }
 }
    return result; }


}


abstract class LoopStatement
  extends Statement
  implements SystemTypes
{
  protected Expression test;
  protected Statement body;

  public LoopStatement(Expression test,Statement body)
  {
    this.test = test;
    this.body = body;

  }

  public LoopStatement() { }



  public String toString()
  { String _res_ = "(LoopStatement) ";
    return _res_ + super.toString();
  }



  public void settest(Expression test_xx) { test = test_xx;
  }

  public static void setAlltest(List loopstatements, Expression _val)
  { for (int _i = 0; _i < loopstatements.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) loopstatements.get(_i);
      Controller.inst().settest(loopstatementx, _val); } }

  public void setbody(Statement body_xx) { body = body_xx;
  }

  public static void setAllbody(List loopstatements, Statement _val)
  { for (int _i = 0; _i < loopstatements.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) loopstatements.get(_i);
      Controller.inst().setbody(loopstatementx, _val); } }

  public Expression gettest() { return test; }

  public static List getAlltest(List loopstatements)
  { List result = new Vector();
    for (int _i = 0; _i < loopstatements.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) loopstatements.get(_i);
      if (result.contains(loopstatementx.gettest())) {}
      else { result.add(loopstatementx.gettest()); }
 }
    return result; }

  public static List getAllOrderedtest(List loopstatements)
  { List result = new Vector();
    for (int _i = 0; _i < loopstatements.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) loopstatements.get(_i);
      if (result.contains(loopstatementx.gettest())) {}
      else { result.add(loopstatementx.gettest()); }
 }
    return result; }

  public Statement getbody() { return body; }

  public static List getAllbody(List loopstatements)
  { List result = new Vector();
    for (int _i = 0; _i < loopstatements.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) loopstatements.get(_i);
      if (result.contains(loopstatementx.getbody())) {}
      else { result.add(loopstatementx.getbody()); }
 }
    return result; }

  public static List getAllOrderedbody(List loopstatements)
  { List result = new Vector();
    for (int _i = 0; _i < loopstatements.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) loopstatements.get(_i);
      if (result.contains(loopstatementx.getbody())) {}
      else { result.add(loopstatementx.getbody()); }
 }
    return result; }


}


class BoundedLoopStatement
  extends LoopStatement
  implements SystemTypes
{
  private Expression loopRange;
  private Expression loopVar;

  public BoundedLoopStatement(Expression loopRange,Expression loopVar)
  {
    this.loopRange = loopRange;
    this.loopVar = loopVar;

  }

  public BoundedLoopStatement() { }



  public String toString()
  { String _res_ = "(BoundedLoopStatement) ";
    return _res_ + super.toString();
  }

  public static BoundedLoopStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    BoundedLoopStatement boundedloopstatementx = new BoundedLoopStatement();
    return boundedloopstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { BoundedLoopStatement boundedloopstatementx = this;
    _out.println();
  }


  public void setloopRange(Expression loopRange_xx) { loopRange = loopRange_xx;
  }

  public static void setAllloopRange(List boundedloopstatements, Expression _val)
  { for (int _i = 0; _i < boundedloopstatements.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) boundedloopstatements.get(_i);
      Controller.inst().setloopRange(boundedloopstatementx, _val); } }

  public void setloopVar(Expression loopVar_xx) { loopVar = loopVar_xx;
  }

  public static void setAllloopVar(List boundedloopstatements, Expression _val)
  { for (int _i = 0; _i < boundedloopstatements.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) boundedloopstatements.get(_i);
      Controller.inst().setloopVar(boundedloopstatementx, _val); } }

  public Expression getloopRange() { return loopRange; }

  public static List getAllloopRange(List boundedloopstatements)
  { List result = new Vector();
    for (int _i = 0; _i < boundedloopstatements.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) boundedloopstatements.get(_i);
      if (result.contains(boundedloopstatementx.getloopRange())) {}
      else { result.add(boundedloopstatementx.getloopRange()); }
 }
    return result; }

  public static List getAllOrderedloopRange(List boundedloopstatements)
  { List result = new Vector();
    for (int _i = 0; _i < boundedloopstatements.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) boundedloopstatements.get(_i);
      if (result.contains(boundedloopstatementx.getloopRange())) {}
      else { result.add(boundedloopstatementx.getloopRange()); }
 }
    return result; }

  public Expression getloopVar() { return loopVar; }

  public static List getAllloopVar(List boundedloopstatements)
  { List result = new Vector();
    for (int _i = 0; _i < boundedloopstatements.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) boundedloopstatements.get(_i);
      if (result.contains(boundedloopstatementx.getloopVar())) {}
      else { result.add(boundedloopstatementx.getloopVar()); }
 }
    return result; }

  public static List getAllOrderedloopVar(List boundedloopstatements)
  { List result = new Vector();
    for (int _i = 0; _i < boundedloopstatements.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) boundedloopstatements.get(_i);
      if (result.contains(boundedloopstatementx.getloopVar())) {}
      else { result.add(boundedloopstatementx.getloopVar()); }
 }
    return result; }


}


class UnboundedLoopStatement
  extends LoopStatement
  implements SystemTypes
{

  public UnboundedLoopStatement()
  {

  }



  public String toString()
  { String _res_ = "(UnboundedLoopStatement) ";
    return _res_ + super.toString();
  }

  public static UnboundedLoopStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    UnboundedLoopStatement unboundedloopstatementx = new UnboundedLoopStatement();
    return unboundedloopstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { UnboundedLoopStatement unboundedloopstatementx = this;
    _out.println();
  }



}


class AssignStatement
  extends Statement
  implements SystemTypes
{
  private List type = new Vector(); // of Type
  private Expression left;
  private Expression right;

  public AssignStatement(Expression left,Expression right)
  {
    this.left = left;
    this.right = right;

  }

  public AssignStatement() { }



  public String toString()
  { String _res_ = "(AssignStatement) ";
    return _res_ + super.toString();
  }

  public static AssignStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    AssignStatement assignstatementx = new AssignStatement();
    return assignstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { AssignStatement assignstatementx = this;
    _out.println();
  }


  public void settype(List type_xx) { if (type_xx.size() > 1) { return; } 
    type = type_xx;
  }
 
  public void addtype(Type type_xx) { if (type.size() > 0) { type.clear(); } 
    if (type.contains(type_xx)) {} else { type.add(type_xx); }
    }
 
  public void removetype(Type type_xx) { Vector _removedtypetype_xx = new Vector();
  _removedtypetype_xx.add(type_xx);
  type.removeAll(_removedtypetype_xx);
    }

  public static void setAlltype(List assignstatements, List _val)
  { for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      Controller.inst().settype(assignstatementx, _val); } }

  public static void addAlltype(List assignstatements, Type _val)
  { for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      Controller.inst().addtype(assignstatementx,  _val); } }


  public static void removeAlltype(List assignstatements,Type _val)
  { for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      Controller.inst().removetype(assignstatementx, _val); } }


  public static void unionAlltype(List assignstatements, List _val)
  { for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      Controller.inst().uniontype(assignstatementx, _val); } }


  public static void subtractAlltype(List assignstatements,  List _val)
  { for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      Controller.inst().subtracttype(assignstatementx,  _val); } }


  public void setleft(Expression left_xx) { left = left_xx;
  }

  public static void setAllleft(List assignstatements, Expression _val)
  { for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      Controller.inst().setleft(assignstatementx, _val); } }

  public void setright(Expression right_xx) { right = right_xx;
  }

  public static void setAllright(List assignstatements, Expression _val)
  { for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      Controller.inst().setright(assignstatementx, _val); } }

  public List gettype() { return (Vector) ((Vector) type).clone(); }

  public static List getAlltype(List assignstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      result = Set.union(result, assignstatementx.gettype()); }
    return result; }

  public static List getAllOrderedtype(List assignstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      result = Set.union(result, assignstatementx.gettype()); }
    return result; }

  public Expression getleft() { return left; }

  public static List getAllleft(List assignstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      if (result.contains(assignstatementx.getleft())) {}
      else { result.add(assignstatementx.getleft()); }
 }
    return result; }

  public static List getAllOrderedleft(List assignstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      if (result.contains(assignstatementx.getleft())) {}
      else { result.add(assignstatementx.getleft()); }
 }
    return result; }

  public Expression getright() { return right; }

  public static List getAllright(List assignstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      if (result.contains(assignstatementx.getright())) {}
      else { result.add(assignstatementx.getright()); }
 }
    return result; }

  public static List getAllOrderedright(List assignstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(_i);
      if (result.contains(assignstatementx.getright())) {}
      else { result.add(assignstatementx.getright()); }
 }
    return result; }


}


class SequenceStatement
  extends Statement
  implements SystemTypes
{
  private int kind = 0; // internal
  private List statements = new Vector(); // of Statement

  public SequenceStatement()
  {
    this.kind = 0;

  }



  public String toString()
  { String _res_ = "(SequenceStatement) ";
    _res_ = _res_ + kind;
    return _res_ + super.toString();
  }

  public static SequenceStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    SequenceStatement sequencestatementx = new SequenceStatement();
    sequencestatementx.kind = Integer.parseInt((String) _line1vals.get(0));
    return sequencestatementx;
  }


  public void writeCSV(PrintWriter _out)
  { SequenceStatement sequencestatementx = this;
    _out.print("" + sequencestatementx.kind);
    _out.println();
  }


  public void setkind(int kind_x) { kind = kind_x;  }


    public static void setAllkind(List sequencestatements,int val)
  { for (int i = 0; i < sequencestatements.size(); i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(i);
      Controller.inst().setkind(sequencestatementx,val); } }


  public void setstatements(List statements_xx) { statements = statements_xx;
    }
 
  public void setstatements(int ind_x, Statement statements_xx) { if (ind_x >= 0 && ind_x < statements.size()) { statements.set(ind_x, statements_xx); } }

 public void addstatements(Statement statements_xx) { statements.add(statements_xx);
    }
 
  public void removestatements(Statement statements_xx) { Vector _removedstatementsstatements_xx = new Vector();
  _removedstatementsstatements_xx.add(statements_xx);
  statements.removeAll(_removedstatementsstatements_xx);
    }

  public static void setAllstatements(List sequencestatements, List _val)
  { for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(_i);
      Controller.inst().setstatements(sequencestatementx, _val); } }

  public static void setAllstatements(List sequencestatements, int _ind,Statement _val)
  { for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(_i);
      Controller.inst().setstatements(sequencestatementx,_ind,_val); } }

  public static void addAllstatements(List sequencestatements, Statement _val)
  { for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(_i);
      Controller.inst().addstatements(sequencestatementx,  _val); } }


  public static void removeAllstatements(List sequencestatements,Statement _val)
  { for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(_i);
      Controller.inst().removestatements(sequencestatementx, _val); } }


  public static void unionAllstatements(List sequencestatements, List _val)
  { for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(_i);
      Controller.inst().unionstatements(sequencestatementx, _val); } }


  public static void subtractAllstatements(List sequencestatements,  List _val)
  { for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(_i);
      Controller.inst().subtractstatements(sequencestatementx,  _val); } }


    public int getkind() { return kind; }

    public static List getAllkind(List sequencestatements)
  { List result = new Vector();
    for (int i = 0; i < sequencestatements.size(); i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(i);
      if (result.contains(new Integer(sequencestatementx.getkind()))) { }
      else { result.add(new Integer(sequencestatementx.getkind())); } }
    return result; }

    public static List getAllOrderedkind(List sequencestatements)
  { List result = new Vector();
    for (int i = 0; i < sequencestatements.size(); i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(i);
      result.add(new Integer(sequencestatementx.getkind())); } 
    return result; }

  public List getstatements() { return (Vector) ((Vector) statements).clone(); }

  public static List getAllstatements(List sequencestatements)
  { List result = new Vector();
    for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(_i);
      result = Set.union(result, sequencestatementx.getstatements()); }
    return result; }

  public static List getAllOrderedstatements(List sequencestatements)
  { List result = new Vector();
    for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(_i);
      result.addAll(sequencestatementx.getstatements()); }
    return result; }


}


class ConditionalStatement
  extends Statement
  implements SystemTypes
{
  private Expression test;
  private Statement ifPart;
  private List elsePart = new Vector(); // of Statement

  public ConditionalStatement(Expression test,Statement ifPart)
  {
    this.test = test;
    this.ifPart = ifPart;

  }

  public ConditionalStatement() { }



  public String toString()
  { String _res_ = "(ConditionalStatement) ";
    return _res_ + super.toString();
  }

  public static ConditionalStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    ConditionalStatement conditionalstatementx = new ConditionalStatement();
    return conditionalstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { ConditionalStatement conditionalstatementx = this;
    _out.println();
  }


  public void settest(Expression test_xx) { test = test_xx;
  }

  public static void setAlltest(List conditionalstatements, Expression _val)
  { for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      Controller.inst().settest(conditionalstatementx, _val); } }

  public void setifPart(Statement ifPart_xx) { ifPart = ifPart_xx;
  }

  public static void setAllifPart(List conditionalstatements, Statement _val)
  { for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      Controller.inst().setifPart(conditionalstatementx, _val); } }

  public void setelsePart(List elsePart_xx) { if (elsePart_xx.size() > 1) { return; } 
    elsePart = elsePart_xx;
  }
 
  public void addelsePart(Statement elsePart_xx) { if (elsePart.size() > 0) { elsePart.clear(); } 
    if (elsePart.contains(elsePart_xx)) {} else { elsePart.add(elsePart_xx); }
    }
 
  public void removeelsePart(Statement elsePart_xx) { Vector _removedelsePartelsePart_xx = new Vector();
  _removedelsePartelsePart_xx.add(elsePart_xx);
  elsePart.removeAll(_removedelsePartelsePart_xx);
    }

  public static void setAllelsePart(List conditionalstatements, List _val)
  { for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      Controller.inst().setelsePart(conditionalstatementx, _val); } }

  public static void addAllelsePart(List conditionalstatements, Statement _val)
  { for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      Controller.inst().addelsePart(conditionalstatementx,  _val); } }


  public static void removeAllelsePart(List conditionalstatements,Statement _val)
  { for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      Controller.inst().removeelsePart(conditionalstatementx, _val); } }


  public static void unionAllelsePart(List conditionalstatements, List _val)
  { for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      Controller.inst().unionelsePart(conditionalstatementx, _val); } }


  public static void subtractAllelsePart(List conditionalstatements,  List _val)
  { for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      Controller.inst().subtractelsePart(conditionalstatementx,  _val); } }


  public Expression gettest() { return test; }

  public static List getAlltest(List conditionalstatements)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      if (result.contains(conditionalstatementx.gettest())) {}
      else { result.add(conditionalstatementx.gettest()); }
 }
    return result; }

  public static List getAllOrderedtest(List conditionalstatements)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      if (result.contains(conditionalstatementx.gettest())) {}
      else { result.add(conditionalstatementx.gettest()); }
 }
    return result; }

  public Statement getifPart() { return ifPart; }

  public static List getAllifPart(List conditionalstatements)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      if (result.contains(conditionalstatementx.getifPart())) {}
      else { result.add(conditionalstatementx.getifPart()); }
 }
    return result; }

  public static List getAllOrderedifPart(List conditionalstatements)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      if (result.contains(conditionalstatementx.getifPart())) {}
      else { result.add(conditionalstatementx.getifPart()); }
 }
    return result; }

  public List getelsePart() { return (Vector) ((Vector) elsePart).clone(); }

  public static List getAllelsePart(List conditionalstatements)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      result = Set.union(result, conditionalstatementx.getelsePart()); }
    return result; }

  public static List getAllOrderedelsePart(List conditionalstatements)
  { List result = new Vector();
    for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(_i);
      result = Set.union(result, conditionalstatementx.getelsePart()); }
    return result; }


}


class CreationStatement
  extends Statement
  implements SystemTypes
{
  private String createsInstanceOf = ""; // internal
  private String assignsTo = ""; // internal
  private Type type;
  private Type elementType;
  private List initialExpression = new Vector(); // of Expression

  public CreationStatement(Type type,Type elementType)
  {
    this.createsInstanceOf = "";
    this.assignsTo = "";
    this.type = type;
    this.elementType = elementType;

  }

  public CreationStatement() { }



  public String toString()
  { String _res_ = "(CreationStatement) ";
    _res_ = _res_ + createsInstanceOf + ",";
    _res_ = _res_ + assignsTo;
    return _res_ + super.toString();
  }

  public static CreationStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CreationStatement creationstatementx = new CreationStatement();
    creationstatementx.createsInstanceOf = (String) _line1vals.get(0);
    creationstatementx.assignsTo = (String) _line1vals.get(1);
    return creationstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { CreationStatement creationstatementx = this;
    _out.print("" + creationstatementx.createsInstanceOf);
    _out.print(" , ");
    _out.print("" + creationstatementx.assignsTo);
    _out.println();
  }


  public void setcreatesInstanceOf(String createsInstanceOf_x) { createsInstanceOf = createsInstanceOf_x;  }


    public static void setAllcreatesInstanceOf(List creationstatements,String val)
  { for (int i = 0; i < creationstatements.size(); i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(i);
      Controller.inst().setcreatesInstanceOf(creationstatementx,val); } }


  public void setassignsTo(String assignsTo_x) { assignsTo = assignsTo_x;  }


    public static void setAllassignsTo(List creationstatements,String val)
  { for (int i = 0; i < creationstatements.size(); i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(i);
      Controller.inst().setassignsTo(creationstatementx,val); } }


  public void settype(Type type_xx) { type = type_xx;
  }

  public static void setAlltype(List creationstatements, Type _val)
  { for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      Controller.inst().settype(creationstatementx, _val); } }

  public void setelementType(Type elementType_xx) { elementType = elementType_xx;
  }

  public static void setAllelementType(List creationstatements, Type _val)
  { for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      Controller.inst().setelementType(creationstatementx, _val); } }

  public void setinitialExpression(List initialExpression_xx) { if (initialExpression_xx.size() > 1) { return; } 
    initialExpression = initialExpression_xx;
  }
 
  public void addinitialExpression(Expression initialExpression_xx) { if (initialExpression.size() > 0) { initialExpression.clear(); } 
    if (initialExpression.contains(initialExpression_xx)) {} else { initialExpression.add(initialExpression_xx); }
    }
 
  public void removeinitialExpression(Expression initialExpression_xx) { Vector _removedinitialExpressioninitialExpression_xx = new Vector();
  _removedinitialExpressioninitialExpression_xx.add(initialExpression_xx);
  initialExpression.removeAll(_removedinitialExpressioninitialExpression_xx);
    }

  public static void setAllinitialExpression(List creationstatements, List _val)
  { for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      Controller.inst().setinitialExpression(creationstatementx, _val); } }

  public static void addAllinitialExpression(List creationstatements, Expression _val)
  { for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      Controller.inst().addinitialExpression(creationstatementx,  _val); } }


  public static void removeAllinitialExpression(List creationstatements,Expression _val)
  { for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      Controller.inst().removeinitialExpression(creationstatementx, _val); } }


  public static void unionAllinitialExpression(List creationstatements, List _val)
  { for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      Controller.inst().unioninitialExpression(creationstatementx, _val); } }


  public static void subtractAllinitialExpression(List creationstatements,  List _val)
  { for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      Controller.inst().subtractinitialExpression(creationstatementx,  _val); } }


    public String getcreatesInstanceOf() { return createsInstanceOf; }

    public static List getAllcreatesInstanceOf(List creationstatements)
  { List result = new Vector();
    for (int i = 0; i < creationstatements.size(); i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(i);
      if (result.contains(creationstatementx.getcreatesInstanceOf())) { }
      else { result.add(creationstatementx.getcreatesInstanceOf()); } }
    return result; }

    public static List getAllOrderedcreatesInstanceOf(List creationstatements)
  { List result = new Vector();
    for (int i = 0; i < creationstatements.size(); i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(i);
      result.add(creationstatementx.getcreatesInstanceOf()); } 
    return result; }

    public String getassignsTo() { return assignsTo; }

    public static List getAllassignsTo(List creationstatements)
  { List result = new Vector();
    for (int i = 0; i < creationstatements.size(); i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(i);
      if (result.contains(creationstatementx.getassignsTo())) { }
      else { result.add(creationstatementx.getassignsTo()); } }
    return result; }

    public static List getAllOrderedassignsTo(List creationstatements)
  { List result = new Vector();
    for (int i = 0; i < creationstatements.size(); i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(i);
      result.add(creationstatementx.getassignsTo()); } 
    return result; }

  public Type gettype() { return type; }

  public static List getAlltype(List creationstatements)
  { List result = new Vector();
    for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      if (result.contains(creationstatementx.gettype())) {}
      else { result.add(creationstatementx.gettype()); }
 }
    return result; }

  public static List getAllOrderedtype(List creationstatements)
  { List result = new Vector();
    for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      if (result.contains(creationstatementx.gettype())) {}
      else { result.add(creationstatementx.gettype()); }
 }
    return result; }

  public Type getelementType() { return elementType; }

  public static List getAllelementType(List creationstatements)
  { List result = new Vector();
    for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      if (result.contains(creationstatementx.getelementType())) {}
      else { result.add(creationstatementx.getelementType()); }
 }
    return result; }

  public static List getAllOrderedelementType(List creationstatements)
  { List result = new Vector();
    for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      if (result.contains(creationstatementx.getelementType())) {}
      else { result.add(creationstatementx.getelementType()); }
 }
    return result; }

  public List getinitialExpression() { return (Vector) ((Vector) initialExpression).clone(); }

  public static List getAllinitialExpression(List creationstatements)
  { List result = new Vector();
    for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      result = Set.union(result, creationstatementx.getinitialExpression()); }
    return result; }

  public static List getAllOrderedinitialExpression(List creationstatements)
  { List result = new Vector();
    for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(_i);
      result = Set.union(result, creationstatementx.getinitialExpression()); }
    return result; }


}


class ErrorStatement
  extends Statement
  implements SystemTypes
{
  private Expression thrownObject;

  public ErrorStatement(Expression thrownObject)
  {
    this.thrownObject = thrownObject;

  }

  public ErrorStatement() { }



  public String toString()
  { String _res_ = "(ErrorStatement) ";
    return _res_ + super.toString();
  }

  public static ErrorStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    ErrorStatement errorstatementx = new ErrorStatement();
    return errorstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { ErrorStatement errorstatementx = this;
    _out.println();
  }


  public void setthrownObject(Expression thrownObject_xx) { thrownObject = thrownObject_xx;
  }

  public static void setAllthrownObject(List errorstatements, Expression _val)
  { for (int _i = 0; _i < errorstatements.size(); _i++)
    { ErrorStatement errorstatementx = (ErrorStatement) errorstatements.get(_i);
      Controller.inst().setthrownObject(errorstatementx, _val); } }

  public Expression getthrownObject() { return thrownObject; }

  public static List getAllthrownObject(List errorstatements)
  { List result = new Vector();
    for (int _i = 0; _i < errorstatements.size(); _i++)
    { ErrorStatement errorstatementx = (ErrorStatement) errorstatements.get(_i);
      if (result.contains(errorstatementx.getthrownObject())) {}
      else { result.add(errorstatementx.getthrownObject()); }
 }
    return result; }

  public static List getAllOrderedthrownObject(List errorstatements)
  { List result = new Vector();
    for (int _i = 0; _i < errorstatements.size(); _i++)
    { ErrorStatement errorstatementx = (ErrorStatement) errorstatements.get(_i);
      if (result.contains(errorstatementx.getthrownObject())) {}
      else { result.add(errorstatementx.getthrownObject()); }
 }
    return result; }


}


class CatchStatement
  extends Statement
  implements SystemTypes
{
  private Expression caughtObject;
  private Statement action;

  public CatchStatement(Expression caughtObject,Statement action)
  {
    this.caughtObject = caughtObject;
    this.action = action;

  }

  public CatchStatement() { }



  public String toString()
  { String _res_ = "(CatchStatement) ";
    return _res_ + super.toString();
  }

  public static CatchStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CatchStatement catchstatementx = new CatchStatement();
    return catchstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { CatchStatement catchstatementx = this;
    _out.println();
  }


  public void setcaughtObject(Expression caughtObject_xx) { caughtObject = caughtObject_xx;
  }

  public static void setAllcaughtObject(List catchstatements, Expression _val)
  { for (int _i = 0; _i < catchstatements.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) catchstatements.get(_i);
      Controller.inst().setcaughtObject(catchstatementx, _val); } }

  public void setaction(Statement action_xx) { action = action_xx;
  }

  public static void setAllaction(List catchstatements, Statement _val)
  { for (int _i = 0; _i < catchstatements.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) catchstatements.get(_i);
      Controller.inst().setaction(catchstatementx, _val); } }

  public Expression getcaughtObject() { return caughtObject; }

  public static List getAllcaughtObject(List catchstatements)
  { List result = new Vector();
    for (int _i = 0; _i < catchstatements.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) catchstatements.get(_i);
      if (result.contains(catchstatementx.getcaughtObject())) {}
      else { result.add(catchstatementx.getcaughtObject()); }
 }
    return result; }

  public static List getAllOrderedcaughtObject(List catchstatements)
  { List result = new Vector();
    for (int _i = 0; _i < catchstatements.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) catchstatements.get(_i);
      if (result.contains(catchstatementx.getcaughtObject())) {}
      else { result.add(catchstatementx.getcaughtObject()); }
 }
    return result; }

  public Statement getaction() { return action; }

  public static List getAllaction(List catchstatements)
  { List result = new Vector();
    for (int _i = 0; _i < catchstatements.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) catchstatements.get(_i);
      if (result.contains(catchstatementx.getaction())) {}
      else { result.add(catchstatementx.getaction()); }
 }
    return result; }

  public static List getAllOrderedaction(List catchstatements)
  { List result = new Vector();
    for (int _i = 0; _i < catchstatements.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) catchstatements.get(_i);
      if (result.contains(catchstatementx.getaction())) {}
      else { result.add(catchstatementx.getaction()); }
 }
    return result; }


}


class FinalStatement
  extends Statement
  implements SystemTypes
{
  private Statement body;

  public FinalStatement(Statement body)
  {
    this.body = body;

  }

  public FinalStatement() { }



  public String toString()
  { String _res_ = "(FinalStatement) ";
    return _res_ + super.toString();
  }

  public static FinalStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    FinalStatement finalstatementx = new FinalStatement();
    return finalstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { FinalStatement finalstatementx = this;
    _out.println();
  }


  public void setbody(Statement body_xx) { body = body_xx;
  }

  public static void setAllbody(List finalstatements, Statement _val)
  { for (int _i = 0; _i < finalstatements.size(); _i++)
    { FinalStatement finalstatementx = (FinalStatement) finalstatements.get(_i);
      Controller.inst().setbody(finalstatementx, _val); } }

  public Statement getbody() { return body; }

  public static List getAllbody(List finalstatements)
  { List result = new Vector();
    for (int _i = 0; _i < finalstatements.size(); _i++)
    { FinalStatement finalstatementx = (FinalStatement) finalstatements.get(_i);
      if (result.contains(finalstatementx.getbody())) {}
      else { result.add(finalstatementx.getbody()); }
 }
    return result; }

  public static List getAllOrderedbody(List finalstatements)
  { List result = new Vector();
    for (int _i = 0; _i < finalstatements.size(); _i++)
    { FinalStatement finalstatementx = (FinalStatement) finalstatements.get(_i);
      if (result.contains(finalstatementx.getbody())) {}
      else { result.add(finalstatementx.getbody()); }
 }
    return result; }


}


class TryStatement
  extends Statement
  implements SystemTypes
{
  private List catchClauses = new Vector(); // of Statement
  private Statement body;
  private List endStatement = new Vector(); // of Statement

  public TryStatement(Statement body)
  {
    this.body = body;

  }

  public TryStatement() { }



  public String toString()
  { String _res_ = "(TryStatement) ";
    return _res_ + super.toString();
  }

  public static TryStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    TryStatement trystatementx = new TryStatement();
    return trystatementx;
  }


  public void writeCSV(PrintWriter _out)
  { TryStatement trystatementx = this;
    _out.println();
  }


  public void setcatchClauses(List catchClauses_xx) { catchClauses = catchClauses_xx;
    }
 
  public void setcatchClauses(int ind_x, Statement catchClauses_xx) { if (ind_x >= 0 && ind_x < catchClauses.size()) { catchClauses.set(ind_x, catchClauses_xx); } }

 public void addcatchClauses(Statement catchClauses_xx) { catchClauses.add(catchClauses_xx);
    }
 
  public void removecatchClauses(Statement catchClauses_xx) { Vector _removedcatchClausescatchClauses_xx = new Vector();
  _removedcatchClausescatchClauses_xx.add(catchClauses_xx);
  catchClauses.removeAll(_removedcatchClausescatchClauses_xx);
    }

  public static void setAllcatchClauses(List trystatements, List _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().setcatchClauses(trystatementx, _val); } }

  public static void setAllcatchClauses(List trystatements, int _ind,Statement _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().setcatchClauses(trystatementx,_ind,_val); } }

  public static void addAllcatchClauses(List trystatements, Statement _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().addcatchClauses(trystatementx,  _val); } }


  public static void removeAllcatchClauses(List trystatements,Statement _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().removecatchClauses(trystatementx, _val); } }


  public static void unionAllcatchClauses(List trystatements, List _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().unioncatchClauses(trystatementx, _val); } }


  public static void subtractAllcatchClauses(List trystatements,  List _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().subtractcatchClauses(trystatementx,  _val); } }


  public void setbody(Statement body_xx) { body = body_xx;
  }

  public static void setAllbody(List trystatements, Statement _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().setbody(trystatementx, _val); } }

  public void setendStatement(List endStatement_xx) { if (endStatement_xx.size() > 1) { return; } 
    endStatement = endStatement_xx;
  }
 
  public void addendStatement(Statement endStatement_xx) { if (endStatement.size() > 0) { endStatement.clear(); } 
    if (endStatement.contains(endStatement_xx)) {} else { endStatement.add(endStatement_xx); }
    }
 
  public void removeendStatement(Statement endStatement_xx) { Vector _removedendStatementendStatement_xx = new Vector();
  _removedendStatementendStatement_xx.add(endStatement_xx);
  endStatement.removeAll(_removedendStatementendStatement_xx);
    }

  public static void setAllendStatement(List trystatements, List _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().setendStatement(trystatementx, _val); } }

  public static void addAllendStatement(List trystatements, Statement _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().addendStatement(trystatementx,  _val); } }


  public static void removeAllendStatement(List trystatements,Statement _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().removeendStatement(trystatementx, _val); } }


  public static void unionAllendStatement(List trystatements, List _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().unionendStatement(trystatementx, _val); } }


  public static void subtractAllendStatement(List trystatements,  List _val)
  { for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      Controller.inst().subtractendStatement(trystatementx,  _val); } }


  public List getcatchClauses() { return (Vector) ((Vector) catchClauses).clone(); }

  public static List getAllcatchClauses(List trystatements)
  { List result = new Vector();
    for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      result = Set.union(result, trystatementx.getcatchClauses()); }
    return result; }

  public static List getAllOrderedcatchClauses(List trystatements)
  { List result = new Vector();
    for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      result.addAll(trystatementx.getcatchClauses()); }
    return result; }

  public Statement getbody() { return body; }

  public static List getAllbody(List trystatements)
  { List result = new Vector();
    for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      if (result.contains(trystatementx.getbody())) {}
      else { result.add(trystatementx.getbody()); }
 }
    return result; }

  public static List getAllOrderedbody(List trystatements)
  { List result = new Vector();
    for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      if (result.contains(trystatementx.getbody())) {}
      else { result.add(trystatementx.getbody()); }
 }
    return result; }

  public List getendStatement() { return (Vector) ((Vector) endStatement).clone(); }

  public static List getAllendStatement(List trystatements)
  { List result = new Vector();
    for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      result = Set.union(result, trystatementx.getendStatement()); }
    return result; }

  public static List getAllOrderedendStatement(List trystatements)
  { List result = new Vector();
    for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatements.get(_i);
      result = Set.union(result, trystatementx.getendStatement()); }
    return result; }


}


class AssertStatement
  extends Statement
  implements SystemTypes
{
  private Expression condition;
  private List message = new Vector(); // of Expression

  public AssertStatement(Expression condition)
  {
    this.condition = condition;

  }

  public AssertStatement() { }



  public String toString()
  { String _res_ = "(AssertStatement) ";
    return _res_ + super.toString();
  }

  public static AssertStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    AssertStatement assertstatementx = new AssertStatement();
    return assertstatementx;
  }


  public void writeCSV(PrintWriter _out)
  { AssertStatement assertstatementx = this;
    _out.println();
  }


  public void setcondition(Expression condition_xx) { condition = condition_xx;
  }

  public static void setAllcondition(List assertstatements, Expression _val)
  { for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      Controller.inst().setcondition(assertstatementx, _val); } }

  public void setmessage(List message_xx) { if (message_xx.size() > 1) { return; } 
    message = message_xx;
  }
 
  public void addmessage(Expression message_xx) { if (message.size() > 0) { message.clear(); } 
    if (message.contains(message_xx)) {} else { message.add(message_xx); }
    }
 
  public void removemessage(Expression message_xx) { Vector _removedmessagemessage_xx = new Vector();
  _removedmessagemessage_xx.add(message_xx);
  message.removeAll(_removedmessagemessage_xx);
    }

  public static void setAllmessage(List assertstatements, List _val)
  { for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      Controller.inst().setmessage(assertstatementx, _val); } }

  public static void addAllmessage(List assertstatements, Expression _val)
  { for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      Controller.inst().addmessage(assertstatementx,  _val); } }


  public static void removeAllmessage(List assertstatements,Expression _val)
  { for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      Controller.inst().removemessage(assertstatementx, _val); } }


  public static void unionAllmessage(List assertstatements, List _val)
  { for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      Controller.inst().unionmessage(assertstatementx, _val); } }


  public static void subtractAllmessage(List assertstatements,  List _val)
  { for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      Controller.inst().subtractmessage(assertstatementx,  _val); } }


  public Expression getcondition() { return condition; }

  public static List getAllcondition(List assertstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      if (result.contains(assertstatementx.getcondition())) {}
      else { result.add(assertstatementx.getcondition()); }
 }
    return result; }

  public static List getAllOrderedcondition(List assertstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      if (result.contains(assertstatementx.getcondition())) {}
      else { result.add(assertstatementx.getcondition()); }
 }
    return result; }

  public List getmessage() { return (Vector) ((Vector) message).clone(); }

  public static List getAllmessage(List assertstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      result = Set.union(result, assertstatementx.getmessage()); }
    return result; }

  public static List getAllOrderedmessage(List assertstatements)
  { List result = new Vector();
    for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(_i);
      result = Set.union(result, assertstatementx.getmessage()); }
    return result; }


}



public class Controller implements SystemTypes, ControllerInterface
{
  Vector namedelements = new Vector();
  Vector relationships = new Vector();
  Vector features = new Vector();
  Vector types = new Vector();
  Map typetypeIdindex = new HashMap(); // String --> Type

  Vector classifiers = new Vector();
  Vector structuralfeatures = new Vector();
  Vector datatypes = new Vector();
  Vector enumerations = new Vector();
  Vector enumerationliterals = new Vector();
  Vector primitivetypes = new Vector();
  Vector entitys = new Vector();
  Vector propertys = new Vector();
  Vector associations = new Vector();
  Vector generalizations = new Vector();
  Vector behaviouralfeatures = new Vector();
  Vector operations = new Vector();
  Vector expressions = new Vector();
  Map expressionexpIdindex = new HashMap(); // String --> Expression

  Vector usecases = new Vector();
  Vector collectiontypes = new Vector();
  Vector cstructs = new Vector();
  Map cstructnameindex = new HashMap(); // String --> CStruct

  Vector cmembers = new Vector();
  Vector ctypes = new Vector();
  Map ctypectypeIdindex = new HashMap(); // String --> CType

  Vector coperations = new Vector();
  Map coperationopIdindex = new HashMap(); // String --> COperation

  Vector cvariables = new Vector();
  Vector cprograms = new Vector();
  Vector cprimitivetypes = new Vector();
  Vector carraytypes = new Vector();
  Vector cpointertypes = new Vector();
  Vector cfunctionpointertypes = new Vector();
  Vector cenumerations = new Vector();
  Vector cenumerationliterals = new Vector();
  Vector binaryexpressions = new Vector();
  Vector conditionalexpressions = new Vector();
  Vector unaryexpressions = new Vector();
  Vector collectionexpressions = new Vector();
  Vector basicexpressions = new Vector();
  Vector printcodes = new Vector();
  Vector types2cs = new Vector();
  Vector program2cs = new Vector();
  Vector statements = new Vector();
  Map statementstatIdindex = new HashMap(); // String --> Statement

  Vector returnstatements = new Vector();
  Vector breakstatements = new Vector();
  Vector operationcallstatements = new Vector();
  Vector implicitcallstatements = new Vector();
  Vector loopstatements = new Vector();
  Vector boundedloopstatements = new Vector();
  Vector unboundedloopstatements = new Vector();
  Vector assignstatements = new Vector();
  Vector sequencestatements = new Vector();
  Vector conditionalstatements = new Vector();
  Vector creationstatements = new Vector();
  Vector errorstatements = new Vector();
  Vector catchstatements = new Vector();
  Vector finalstatements = new Vector();
  Vector trystatements = new Vector();
  Vector assertstatements = new Vector();
  private static Controller uniqueInstance; 


  private Controller() { } 


  public static Controller inst() 
    { if (uniqueInstance == null) 
    { uniqueInstance = new Controller(); }
    return uniqueInstance; } 


  public static void loadModel(String file)
  {
    try
    { BufferedReader br = null;
      File f = new File(file);
      try 
      { br = new BufferedReader(new FileReader(f)); }
      catch (Exception ex) 
      { System.err.println("No file: " + file); return; }
      Class cont = Class.forName("uml2Ca.Controller");
      java.util.Map objectmap = new java.util.HashMap();
      while (true)
      { String line1;
        try { line1 = br.readLine(); }
        catch (Exception e)
        { return; }
        if (line1 == null)
        { return; }
        line1 = line1.trim();

        if (line1.length() == 0) { continue; }
        if (line1.startsWith("//")) { continue; }
        String left;
        String op;
        String right;
        if (line1.charAt(line1.length() - 1) == '"')
        { int eqind = line1.indexOf("="); 
          if (eqind == -1) { continue; }
          else 
          { left = line1.substring(0,eqind-1).trim();
            op = "="; 
            right = line1.substring(eqind+1,line1.length()).trim();
          }
        }
        else
        { StringTokenizer st1 = new StringTokenizer(line1);
          Vector vals1 = new Vector();
          while (st1.hasMoreTokens())
          { String val1 = st1.nextToken();
            vals1.add(val1);
          }
          if (vals1.size() < 3)
          { continue; }
          left = (String) vals1.get(0);
          op = (String) vals1.get(1);
          right = (String) vals1.get(2);
        }
        if (":".equals(op))
        { int i2 = right.indexOf(".");
          if (i2 == -1)
          { Class cl;
            try { cl = Class.forName("uml2Ca." + right); }
            catch (Exception _x) { System.err.println("No entity: " + right); continue; }
            Object xinst = cl.newInstance();
            objectmap.put(left,xinst);
            Class[] cargs = new Class[] { cl };
            Method addC = null;
            try { addC = cont.getMethod("add" + right,cargs); }
            catch (Exception _xx) { System.err.println("No entity: " + right); continue; }
            if (addC == null) { continue; }
            Object[] args = new Object[] { xinst };
            addC.invoke(Controller.inst(),args);
          }
          else
          { String obj = right.substring(0,i2);
            String role = right.substring(i2+1,right.length());
            Object objinst = objectmap.get(obj); 
            if (objinst == null) 
            { System.err.println("Warning: no object " + obj); continue; }
            Object val = objectmap.get(left);
            if (val == null &&
                left.length() > 1 &&
                left.startsWith("\"") &&
                left.endsWith("\""))
            { val = left.substring(1,left.length()-1); }
            else if (val == null) 
            { continue; }
            Class objC = objinst.getClass();
            Class typeclass = val.getClass(); 
            Object[] args = new Object[] { val }; 
            Class[] settypes = new Class[] { typeclass };
            Method addrole = Controller.findMethod(objC,"add" + role);
            if (addrole != null) 
            { addrole.invoke(objinst, args); }
            else { System.err.println("Error: cannot add to " + role); }
          }
        }
        else if ("=".equals(op))
        { int i1 = left.indexOf(".");
          if (i1 == -1) 
          { continue; }
          String obj = left.substring(0,i1);
          String att = left.substring(i1+1,left.length());
          Object objinst = objectmap.get(obj); 
          if (objinst == null) 
          { System.err.println("No object: " + obj); continue; }
          Class objC = objinst.getClass();
          Class typeclass; 
          Object val; 
          if (right.charAt(0) == '"' &&
              right.charAt(right.length() - 1) == '"')
          { typeclass = String.class;
            val = right.substring(1,right.length() - 1);
          } 
          else if ("true".equals(right) || "false".equals(right))
          { typeclass = boolean.class;
            if ("true".equals(right))
            { val = new Boolean(true); }
            else
            { val = new Boolean(false); }
          }
          else 
          { val = objectmap.get(right);
            if (val != null)
            { typeclass = val.getClass(); }
            else 
            { int i;
              long l; 
              double d;
              try 
              { i = Integer.parseInt(right);
                typeclass = int.class;
                val = new Integer(i); 
              }
              catch (Exception ee)
              { try 
                { l = Long.parseLong(right);
                  typeclass = long.class;
                  val = new Long(l); 
                }
                catch (Exception eee)
                { try
                  { d = Double.parseDouble(right);
                    typeclass = double.class;
                    val = new Double(d);
                  }
                  catch (Exception ff)
                  { continue; }
                }
              }
            }
          }
          Object[] args = new Object[] { val }; 
          Class[] settypes = new Class[] { typeclass };
          Method setatt = Controller.findMethod(objC,"set" + att);
          if (setatt != null) 
          { setatt.invoke(objinst, args); }
          else { System.err.println("No attribute: " + objC.getName() + "::" + att); }
        }
      }
    } catch (Exception e) { }
  }

  public static Method findMethod(Class c, String name)
  { Method[] mets = c.getMethods(); 
    for (int i = 0; i < mets.length; i++)
    { Method m = mets[i];
      if (m.getName().equals(name))
      { return m; }
    } 
    return null;
  }


  public static void loadCSVModel()
  { boolean __eof = false;
    String __s = "";
    Controller __cont = Controller.inst();
    BufferedReader __br = null;
    try
    { File _enumeration = new File("Enumeration.csv");
      __br = new BufferedReader(new FileReader(_enumeration));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Enumeration enumerationx = Enumeration.parseCSV(__s.trim());
          if (enumerationx != null)
          { __cont.addEnumeration(enumerationx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _enumerationliteral = new File("EnumerationLiteral.csv");
      __br = new BufferedReader(new FileReader(_enumerationliteral));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { EnumerationLiteral enumerationliteralx = EnumerationLiteral.parseCSV(__s.trim());
          if (enumerationliteralx != null)
          { __cont.addEnumerationLiteral(enumerationliteralx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _primitivetype = new File("PrimitiveType.csv");
      __br = new BufferedReader(new FileReader(_primitivetype));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { PrimitiveType primitivetypex = PrimitiveType.parseCSV(__s.trim());
          if (primitivetypex != null)
          { __cont.addPrimitiveType(primitivetypex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _entity = new File("Entity.csv");
      __br = new BufferedReader(new FileReader(_entity));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Entity entityx = Entity.parseCSV(__s.trim());
          if (entityx != null)
          { __cont.addEntity(entityx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _property = new File("Property.csv");
      __br = new BufferedReader(new FileReader(_property));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Property propertyx = Property.parseCSV(__s.trim());
          if (propertyx != null)
          { __cont.addProperty(propertyx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _association = new File("Association.csv");
      __br = new BufferedReader(new FileReader(_association));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Association associationx = Association.parseCSV(__s.trim());
          if (associationx != null)
          { __cont.addAssociation(associationx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _generalization = new File("Generalization.csv");
      __br = new BufferedReader(new FileReader(_generalization));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Generalization generalizationx = Generalization.parseCSV(__s.trim());
          if (generalizationx != null)
          { __cont.addGeneralization(generalizationx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _operation = new File("Operation.csv");
      __br = new BufferedReader(new FileReader(_operation));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Operation operationx = Operation.parseCSV(__s.trim());
          if (operationx != null)
          { __cont.addOperation(operationx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _usecase = new File("UseCase.csv");
      __br = new BufferedReader(new FileReader(_usecase));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { UseCase usecasex = UseCase.parseCSV(__s.trim());
          if (usecasex != null)
          { __cont.addUseCase(usecasex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _collectiontype = new File("CollectionType.csv");
      __br = new BufferedReader(new FileReader(_collectiontype));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CollectionType collectiontypex = CollectionType.parseCSV(__s.trim());
          if (collectiontypex != null)
          { __cont.addCollectionType(collectiontypex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cstruct = new File("CStruct.csv");
      __br = new BufferedReader(new FileReader(_cstruct));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CStruct cstructx = CStruct.parseCSV(__s.trim());
          if (cstructx != null)
          { __cont.addCStruct(cstructx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cmember = new File("CMember.csv");
      __br = new BufferedReader(new FileReader(_cmember));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CMember cmemberx = CMember.parseCSV(__s.trim());
          if (cmemberx != null)
          { __cont.addCMember(cmemberx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _coperation = new File("COperation.csv");
      __br = new BufferedReader(new FileReader(_coperation));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { COperation coperationx = COperation.parseCSV(__s.trim());
          if (coperationx != null)
          { __cont.addCOperation(coperationx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cvariable = new File("CVariable.csv");
      __br = new BufferedReader(new FileReader(_cvariable));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CVariable cvariablex = CVariable.parseCSV(__s.trim());
          if (cvariablex != null)
          { __cont.addCVariable(cvariablex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cprogram = new File("CProgram.csv");
      __br = new BufferedReader(new FileReader(_cprogram));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CProgram cprogramx = CProgram.parseCSV(__s.trim());
          if (cprogramx != null)
          { __cont.addCProgram(cprogramx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cprimitivetype = new File("CPrimitiveType.csv");
      __br = new BufferedReader(new FileReader(_cprimitivetype));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CPrimitiveType cprimitivetypex = CPrimitiveType.parseCSV(__s.trim());
          if (cprimitivetypex != null)
          { __cont.addCPrimitiveType(cprimitivetypex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _carraytype = new File("CArrayType.csv");
      __br = new BufferedReader(new FileReader(_carraytype));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CArrayType carraytypex = CArrayType.parseCSV(__s.trim());
          if (carraytypex != null)
          { __cont.addCArrayType(carraytypex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cpointertype = new File("CPointerType.csv");
      __br = new BufferedReader(new FileReader(_cpointertype));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CPointerType cpointertypex = CPointerType.parseCSV(__s.trim());
          if (cpointertypex != null)
          { __cont.addCPointerType(cpointertypex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cfunctionpointertype = new File("CFunctionPointerType.csv");
      __br = new BufferedReader(new FileReader(_cfunctionpointertype));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CFunctionPointerType cfunctionpointertypex = CFunctionPointerType.parseCSV(__s.trim());
          if (cfunctionpointertypex != null)
          { __cont.addCFunctionPointerType(cfunctionpointertypex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cenumeration = new File("CEnumeration.csv");
      __br = new BufferedReader(new FileReader(_cenumeration));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CEnumeration cenumerationx = CEnumeration.parseCSV(__s.trim());
          if (cenumerationx != null)
          { __cont.addCEnumeration(cenumerationx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _cenumerationliteral = new File("CEnumerationLiteral.csv");
      __br = new BufferedReader(new FileReader(_cenumerationliteral));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CEnumerationLiteral cenumerationliteralx = CEnumerationLiteral.parseCSV(__s.trim());
          if (cenumerationliteralx != null)
          { __cont.addCEnumerationLiteral(cenumerationliteralx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _binaryexpression = new File("BinaryExpression.csv");
      __br = new BufferedReader(new FileReader(_binaryexpression));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { BinaryExpression binaryexpressionx = BinaryExpression.parseCSV(__s.trim());
          if (binaryexpressionx != null)
          { __cont.addBinaryExpression(binaryexpressionx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _conditionalexpression = new File("ConditionalExpression.csv");
      __br = new BufferedReader(new FileReader(_conditionalexpression));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { ConditionalExpression conditionalexpressionx = ConditionalExpression.parseCSV(__s.trim());
          if (conditionalexpressionx != null)
          { __cont.addConditionalExpression(conditionalexpressionx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _unaryexpression = new File("UnaryExpression.csv");
      __br = new BufferedReader(new FileReader(_unaryexpression));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { UnaryExpression unaryexpressionx = UnaryExpression.parseCSV(__s.trim());
          if (unaryexpressionx != null)
          { __cont.addUnaryExpression(unaryexpressionx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _collectionexpression = new File("CollectionExpression.csv");
      __br = new BufferedReader(new FileReader(_collectionexpression));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CollectionExpression collectionexpressionx = CollectionExpression.parseCSV(__s.trim());
          if (collectionexpressionx != null)
          { __cont.addCollectionExpression(collectionexpressionx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _basicexpression = new File("BasicExpression.csv");
      __br = new BufferedReader(new FileReader(_basicexpression));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { BasicExpression basicexpressionx = BasicExpression.parseCSV(__s.trim());
          if (basicexpressionx != null)
          { __cont.addBasicExpression(basicexpressionx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _printcode = new File("Printcode.csv");
      __br = new BufferedReader(new FileReader(_printcode));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Printcode printcodex = Printcode.parseCSV(__s.trim());
          if (printcodex != null)
          { __cont.addPrintcode(printcodex); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _types2c = new File("Types2C.csv");
      __br = new BufferedReader(new FileReader(_types2c));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Types2C types2cx = Types2C.parseCSV(__s.trim());
          if (types2cx != null)
          { __cont.addTypes2C(types2cx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _program2c = new File("Program2C.csv");
      __br = new BufferedReader(new FileReader(_program2c));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Program2C program2cx = Program2C.parseCSV(__s.trim());
          if (program2cx != null)
          { __cont.addProgram2C(program2cx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _returnstatement = new File("ReturnStatement.csv");
      __br = new BufferedReader(new FileReader(_returnstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { ReturnStatement returnstatementx = ReturnStatement.parseCSV(__s.trim());
          if (returnstatementx != null)
          { __cont.addReturnStatement(returnstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _breakstatement = new File("BreakStatement.csv");
      __br = new BufferedReader(new FileReader(_breakstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { BreakStatement breakstatementx = BreakStatement.parseCSV(__s.trim());
          if (breakstatementx != null)
          { __cont.addBreakStatement(breakstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _operationcallstatement = new File("OperationCallStatement.csv");
      __br = new BufferedReader(new FileReader(_operationcallstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { OperationCallStatement operationcallstatementx = OperationCallStatement.parseCSV(__s.trim());
          if (operationcallstatementx != null)
          { __cont.addOperationCallStatement(operationcallstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _implicitcallstatement = new File("ImplicitCallStatement.csv");
      __br = new BufferedReader(new FileReader(_implicitcallstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { ImplicitCallStatement implicitcallstatementx = ImplicitCallStatement.parseCSV(__s.trim());
          if (implicitcallstatementx != null)
          { __cont.addImplicitCallStatement(implicitcallstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _boundedloopstatement = new File("BoundedLoopStatement.csv");
      __br = new BufferedReader(new FileReader(_boundedloopstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { BoundedLoopStatement boundedloopstatementx = BoundedLoopStatement.parseCSV(__s.trim());
          if (boundedloopstatementx != null)
          { __cont.addBoundedLoopStatement(boundedloopstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _unboundedloopstatement = new File("UnboundedLoopStatement.csv");
      __br = new BufferedReader(new FileReader(_unboundedloopstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { UnboundedLoopStatement unboundedloopstatementx = UnboundedLoopStatement.parseCSV(__s.trim());
          if (unboundedloopstatementx != null)
          { __cont.addUnboundedLoopStatement(unboundedloopstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _assignstatement = new File("AssignStatement.csv");
      __br = new BufferedReader(new FileReader(_assignstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { AssignStatement assignstatementx = AssignStatement.parseCSV(__s.trim());
          if (assignstatementx != null)
          { __cont.addAssignStatement(assignstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _sequencestatement = new File("SequenceStatement.csv");
      __br = new BufferedReader(new FileReader(_sequencestatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { SequenceStatement sequencestatementx = SequenceStatement.parseCSV(__s.trim());
          if (sequencestatementx != null)
          { __cont.addSequenceStatement(sequencestatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _conditionalstatement = new File("ConditionalStatement.csv");
      __br = new BufferedReader(new FileReader(_conditionalstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { ConditionalStatement conditionalstatementx = ConditionalStatement.parseCSV(__s.trim());
          if (conditionalstatementx != null)
          { __cont.addConditionalStatement(conditionalstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _creationstatement = new File("CreationStatement.csv");
      __br = new BufferedReader(new FileReader(_creationstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CreationStatement creationstatementx = CreationStatement.parseCSV(__s.trim());
          if (creationstatementx != null)
          { __cont.addCreationStatement(creationstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _errorstatement = new File("ErrorStatement.csv");
      __br = new BufferedReader(new FileReader(_errorstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { ErrorStatement errorstatementx = ErrorStatement.parseCSV(__s.trim());
          if (errorstatementx != null)
          { __cont.addErrorStatement(errorstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _catchstatement = new File("CatchStatement.csv");
      __br = new BufferedReader(new FileReader(_catchstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { CatchStatement catchstatementx = CatchStatement.parseCSV(__s.trim());
          if (catchstatementx != null)
          { __cont.addCatchStatement(catchstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _finalstatement = new File("FinalStatement.csv");
      __br = new BufferedReader(new FileReader(_finalstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { FinalStatement finalstatementx = FinalStatement.parseCSV(__s.trim());
          if (finalstatementx != null)
          { __cont.addFinalStatement(finalstatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _trystatement = new File("TryStatement.csv");
      __br = new BufferedReader(new FileReader(_trystatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { TryStatement trystatementx = TryStatement.parseCSV(__s.trim());
          if (trystatementx != null)
          { __cont.addTryStatement(trystatementx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _assertstatement = new File("AssertStatement.csv");
      __br = new BufferedReader(new FileReader(_assertstatement));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { AssertStatement assertstatementx = AssertStatement.parseCSV(__s.trim());
          if (assertstatementx != null)
          { __cont.addAssertStatement(assertstatementx); }
        }
      }
    }
    catch(Exception __e) { }
  }


  public void checkCompleteness()
  {   for (int _i = 0; _i < types.size(); _i++)
  { Type type_x = (Type) types.get(_i);
    Type type_obj = (Type) typetypeIdindex.get(type_x.gettypeId());
    if (type_obj == type_x) { }
    else if (type_obj == null)
    { typetypeIdindex.put(type_x.gettypeId(),type_x); }
    else
    { System.out.println("Error: multiple objects with typeId = " + type_x.gettypeId()); }
  }
  for (int _i = 0; _i < expressions.size(); _i++)
  { Expression expression_x = (Expression) expressions.get(_i);
    Expression expression_obj = (Expression) expressionexpIdindex.get(expression_x.getexpId());
    if (expression_obj == expression_x) { }
    else if (expression_obj == null)
    { expressionexpIdindex.put(expression_x.getexpId(),expression_x); }
    else
    { System.out.println("Error: multiple objects with expId = " + expression_x.getexpId()); }
  }
  for (int _i = 0; _i < cstructs.size(); _i++)
  { CStruct cstruct_x = (CStruct) cstructs.get(_i);
    CStruct cstruct_obj = (CStruct) cstructnameindex.get(cstruct_x.getname());
    if (cstruct_obj == cstruct_x) { }
    else if (cstruct_obj == null)
    { cstructnameindex.put(cstruct_x.getname(),cstruct_x); }
    else
    { System.out.println("Error: multiple objects with name = " + cstruct_x.getname()); }
  }
  for (int _i = 0; _i < ctypes.size(); _i++)
  { CType ctype_x = (CType) ctypes.get(_i);
    CType ctype_obj = (CType) ctypectypeIdindex.get(ctype_x.getctypeId());
    if (ctype_obj == ctype_x) { }
    else if (ctype_obj == null)
    { ctypectypeIdindex.put(ctype_x.getctypeId(),ctype_x); }
    else
    { System.out.println("Error: multiple objects with ctypeId = " + ctype_x.getctypeId()); }
  }
  for (int _i = 0; _i < coperations.size(); _i++)
  { COperation coperation_x = (COperation) coperations.get(_i);
    COperation coperation_obj = (COperation) coperationopIdindex.get(coperation_x.getopId());
    if (coperation_obj == coperation_x) { }
    else if (coperation_obj == null)
    { coperationopIdindex.put(coperation_x.getopId(),coperation_x); }
    else
    { System.out.println("Error: multiple objects with opId = " + coperation_x.getopId()); }
  }
  for (int _i = 0; _i < statements.size(); _i++)
  { Statement statement_x = (Statement) statements.get(_i);
    Statement statement_obj = (Statement) statementstatIdindex.get(statement_x.getstatId());
    if (statement_obj == statement_x) { }
    else if (statement_obj == null)
    { statementstatIdindex.put(statement_x.getstatId(),statement_x); }
    else
    { System.out.println("Error: multiple objects with statId = " + statement_x.getstatId()); }
  }
  for (int _i = 0; _i < entitys.size(); _i++)
  { Entity owner_entityx1 = (Entity) entitys.get(_i);
    for (int _j = 0; _j < propertys.size(); _j++)
    { Property ownedAttribute_propertyx2 = (Property) propertys.get(_j);
      if (owner_entityx1.getownedAttribute().contains(ownedAttribute_propertyx2))
      { if (ownedAttribute_propertyx2.getowner() == owner_entityx1) { }
        else { ownedAttribute_propertyx2.setowner(owner_entityx1); }
      }
      else if (ownedAttribute_propertyx2.getowner() == owner_entityx1)
      { owner_entityx1.addownedAttribute(ownedAttribute_propertyx2); } 
    }
  }
  for (int _i = 0; _i < entitys.size(); _i++)
  { Entity subclasses_entityx1 = (Entity) entitys.get(_i);
    for (int _j = 0; _j < entitys.size(); _j++)
    { Entity superclass_entityx2 = (Entity) entitys.get(_j);
      if (subclasses_entityx1.getsuperclass().contains(superclass_entityx2))
      { if (superclass_entityx2.getsubclasses().contains(subclasses_entityx1)) { }
        else { superclass_entityx2.addsubclasses(subclasses_entityx1); }
      }
      else if (superclass_entityx2.getsubclasses().contains(subclasses_entityx1))
      { subclasses_entityx1.addsuperclass(superclass_entityx2); } 
    }
  }
  for (int _i = 0; _i < entitys.size(); _i++)
  { Entity specific_entityx1 = (Entity) entitys.get(_i);
    for (int _j = 0; _j < generalizations.size(); _j++)
    { Generalization generalization_generalizationx2 = (Generalization) generalizations.get(_j);
      if (specific_entityx1.getgeneralization().contains(generalization_generalizationx2))
      { if (generalization_generalizationx2.getspecific() == specific_entityx1) { }
        else { generalization_generalizationx2.setspecific(specific_entityx1); }
      }
      else if (generalization_generalizationx2.getspecific() == specific_entityx1)
      { specific_entityx1.addgeneralization(generalization_generalizationx2); } 
    }
  }
  for (int _i = 0; _i < generalizations.size(); _i++)
  { Generalization specialization_generalizationx1 = (Generalization) generalizations.get(_i);
    for (int _j = 0; _j < entitys.size(); _j++)
    { Entity general_entityx2 = (Entity) entitys.get(_j);
      if (specialization_generalizationx1.getgeneral() == general_entityx2)
      { if (general_entityx2.getspecialization().contains(specialization_generalizationx1)) { }
        else { general_entityx2.addspecialization(specialization_generalizationx1); }
      }
      else if (general_entityx2.getspecialization().contains(specialization_generalizationx1))
      { specialization_generalizationx1.setgeneral(general_entityx2); } 
    }
  }
  for (int _i = 0; _i < entitys.size(); _i++)
  { Entity owner_entityx1 = (Entity) entitys.get(_i);
    for (int _j = 0; _j < operations.size(); _j++)
    { Operation ownedOperation_operationx2 = (Operation) operations.get(_j);
      if (owner_entityx1.getownedOperation().contains(ownedOperation_operationx2))
      { if (ownedOperation_operationx2.getowner() == owner_entityx1) { }
        else { ownedOperation_operationx2.setowner(owner_entityx1); }
      }
      else if (ownedOperation_operationx2.getowner() == owner_entityx1)
      { owner_entityx1.addownedOperation(ownedOperation_operationx2); } 
    }
  }
  }


  public void saveModel(String file)
  { File outfile = new File(file); 
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
  for (int _i = 0; _i < enumerations.size(); _i++)
  { Enumeration enumerationx_ = (Enumeration) enumerations.get(_i);
    out.println("enumerationx_" + _i + " : Enumeration");
    out.println("enumerationx_" + _i + ".typeId = \"" + enumerationx_.gettypeId() + "\"");
    out.println("enumerationx_" + _i + ".name = \"" + enumerationx_.getname() + "\"");
  }

  for (int _i = 0; _i < enumerationliterals.size(); _i++)
  { EnumerationLiteral enumerationliteralx_ = (EnumerationLiteral) enumerationliterals.get(_i);
    out.println("enumerationliteralx_" + _i + " : EnumerationLiteral");
    out.println("enumerationliteralx_" + _i + ".name = \"" + enumerationliteralx_.getname() + "\"");
  }

  for (int _i = 0; _i < primitivetypes.size(); _i++)
  { PrimitiveType primitivetypex_ = (PrimitiveType) primitivetypes.get(_i);
    out.println("primitivetypex_" + _i + " : PrimitiveType");
    out.println("primitivetypex_" + _i + ".typeId = \"" + primitivetypex_.gettypeId() + "\"");
    out.println("primitivetypex_" + _i + ".name = \"" + primitivetypex_.getname() + "\"");
  }

  for (int _i = 0; _i < entitys.size(); _i++)
  { Entity entityx_ = (Entity) entitys.get(_i);
    out.println("entityx_" + _i + " : Entity");
    out.println("entityx_" + _i + ".isAbstract = " + entityx_.getisAbstract());
    out.println("entityx_" + _i + ".isInterface = " + entityx_.getisInterface());
    out.println("entityx_" + _i + ".typeId = \"" + entityx_.gettypeId() + "\"");
    out.println("entityx_" + _i + ".name = \"" + entityx_.getname() + "\"");
  }

  for (int _i = 0; _i < propertys.size(); _i++)
  { Property propertyx_ = (Property) propertys.get(_i);
    out.println("propertyx_" + _i + " : Property");
    out.println("propertyx_" + _i + ".lower = " + propertyx_.getlower());
    out.println("propertyx_" + _i + ".upper = " + propertyx_.getupper());
    out.println("propertyx_" + _i + ".isOrdered = " + propertyx_.getisOrdered());
    out.println("propertyx_" + _i + ".isUnique = " + propertyx_.getisUnique());
    out.println("propertyx_" + _i + ".isDerived = " + propertyx_.getisDerived());
    out.println("propertyx_" + _i + ".isReadOnly = " + propertyx_.getisReadOnly());
    out.println("propertyx_" + _i + ".isStatic = " + propertyx_.getisStatic());
    out.println("propertyx_" + _i + ".name = \"" + propertyx_.getname() + "\"");
  }

  for (int _i = 0; _i < associations.size(); _i++)
  { Association associationx_ = (Association) associations.get(_i);
    out.println("associationx_" + _i + " : Association");
    out.println("associationx_" + _i + ".addOnly = " + associationx_.getaddOnly());
    out.println("associationx_" + _i + ".aggregation = " + associationx_.getaggregation());
    out.println("associationx_" + _i + ".name = \"" + associationx_.getname() + "\"");
  }

  for (int _i = 0; _i < generalizations.size(); _i++)
  { Generalization generalizationx_ = (Generalization) generalizations.get(_i);
    out.println("generalizationx_" + _i + " : Generalization");
    out.println("generalizationx_" + _i + ".name = \"" + generalizationx_.getname() + "\"");
  }

  for (int _i = 0; _i < operations.size(); _i++)
  { Operation operationx_ = (Operation) operations.get(_i);
    out.println("operationx_" + _i + " : Operation");
    out.println("operationx_" + _i + ".isQuery = " + operationx_.getisQuery());
    out.println("operationx_" + _i + ".isCached = " + operationx_.getisCached());
    out.println("operationx_" + _i + ".isAbstract = " + operationx_.getisAbstract());
    out.println("operationx_" + _i + ".isStatic = " + operationx_.getisStatic());
    out.println("operationx_" + _i + ".name = \"" + operationx_.getname() + "\"");
  }

  for (int _i = 0; _i < usecases.size(); _i++)
  { UseCase usecasex_ = (UseCase) usecases.get(_i);
    out.println("usecasex_" + _i + " : UseCase");
    out.println("usecasex_" + _i + ".isGeneric = " + usecasex_.getisGeneric());
    out.println("usecasex_" + _i + ".isDerived = " + usecasex_.getisDerived());
    out.println("usecasex_" + _i + ".typeId = \"" + usecasex_.gettypeId() + "\"");
    out.println("usecasex_" + _i + ".name = \"" + usecasex_.getname() + "\"");
  }

  for (int _i = 0; _i < collectiontypes.size(); _i++)
  { CollectionType collectiontypex_ = (CollectionType) collectiontypes.get(_i);
    out.println("collectiontypex_" + _i + " : CollectionType");
    out.println("collectiontypex_" + _i + ".typeId = \"" + collectiontypex_.gettypeId() + "\"");
    out.println("collectiontypex_" + _i + ".name = \"" + collectiontypex_.getname() + "\"");
  }

  for (int _i = 0; _i < cstructs.size(); _i++)
  { CStruct cstructx_ = (CStruct) cstructs.get(_i);
    out.println("cstructx_" + _i + " : CStruct");
    out.println("cstructx_" + _i + ".name = \"" + cstructx_.getname() + "\"");
    out.println("cstructx_" + _i + ".ctypeId = \"" + cstructx_.getctypeId() + "\"");
  }

  for (int _i = 0; _i < cmembers.size(); _i++)
  { CMember cmemberx_ = (CMember) cmembers.get(_i);
    out.println("cmemberx_" + _i + " : CMember");
    out.println("cmemberx_" + _i + ".name = \"" + cmemberx_.getname() + "\"");
    out.println("cmemberx_" + _i + ".isKey = " + cmemberx_.getisKey());
    out.println("cmemberx_" + _i + ".multiplicity = \"" + cmemberx_.getmultiplicity() + "\"");
  }

  for (int _i = 0; _i < coperations.size(); _i++)
  { COperation coperationx_ = (COperation) coperations.get(_i);
    out.println("coperationx_" + _i + " : COperation");
    out.println("coperationx_" + _i + ".name = \"" + coperationx_.getname() + "\"");
    out.println("coperationx_" + _i + ".opId = \"" + coperationx_.getopId() + "\"");
    out.println("coperationx_" + _i + ".isStatic = " + coperationx_.getisStatic());
    out.println("coperationx_" + _i + ".scope = \"" + coperationx_.getscope() + "\"");
    out.println("coperationx_" + _i + ".isQuery = " + coperationx_.getisQuery());
    out.println("coperationx_" + _i + ".inheritedFrom = \"" + coperationx_.getinheritedFrom() + "\"");
    out.println("coperationx_" + _i + ".isAbstract = " + coperationx_.getisAbstract());
    out.println("coperationx_" + _i + ".classname = \"" + coperationx_.getclassname() + "\"");
  }

  for (int _i = 0; _i < cvariables.size(); _i++)
  { CVariable cvariablex_ = (CVariable) cvariables.get(_i);
    out.println("cvariablex_" + _i + " : CVariable");
    out.println("cvariablex_" + _i + ".name = \"" + cvariablex_.getname() + "\"");
    out.println("cvariablex_" + _i + ".kind = \"" + cvariablex_.getkind() + "\"");
    out.println("cvariablex_" + _i + ".initialisation = \"" + cvariablex_.getinitialisation() + "\"");
  }

  for (int _i = 0; _i < cprograms.size(); _i++)
  { CProgram cprogramx_ = (CProgram) cprograms.get(_i);
    out.println("cprogramx_" + _i + " : CProgram");
  }

  for (int _i = 0; _i < cprimitivetypes.size(); _i++)
  { CPrimitiveType cprimitivetypex_ = (CPrimitiveType) cprimitivetypes.get(_i);
    out.println("cprimitivetypex_" + _i + " : CPrimitiveType");
    out.println("cprimitivetypex_" + _i + ".name = \"" + cprimitivetypex_.getname() + "\"");
    out.println("cprimitivetypex_" + _i + ".ctypeId = \"" + cprimitivetypex_.getctypeId() + "\"");
  }

  for (int _i = 0; _i < carraytypes.size(); _i++)
  { CArrayType carraytypex_ = (CArrayType) carraytypes.get(_i);
    out.println("carraytypex_" + _i + " : CArrayType");
    out.println("carraytypex_" + _i + ".duplicates = " + carraytypex_.getduplicates());
    out.println("carraytypex_" + _i + ".ctypeId = \"" + carraytypex_.getctypeId() + "\"");
  }

  for (int _i = 0; _i < cpointertypes.size(); _i++)
  { CPointerType cpointertypex_ = (CPointerType) cpointertypes.get(_i);
    out.println("cpointertypex_" + _i + " : CPointerType");
    out.println("cpointertypex_" + _i + ".ctypeId = \"" + cpointertypex_.getctypeId() + "\"");
  }

  for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
  { CFunctionPointerType cfunctionpointertypex_ = (CFunctionPointerType) cfunctionpointertypes.get(_i);
    out.println("cfunctionpointertypex_" + _i + " : CFunctionPointerType");
    out.println("cfunctionpointertypex_" + _i + ".ctypeId = \"" + cfunctionpointertypex_.getctypeId() + "\"");
  }

  for (int _i = 0; _i < cenumerations.size(); _i++)
  { CEnumeration cenumerationx_ = (CEnumeration) cenumerations.get(_i);
    out.println("cenumerationx_" + _i + " : CEnumeration");
    out.println("cenumerationx_" + _i + ".name = \"" + cenumerationx_.getname() + "\"");
    out.println("cenumerationx_" + _i + ".ctypeId = \"" + cenumerationx_.getctypeId() + "\"");
  }

  for (int _i = 0; _i < cenumerationliterals.size(); _i++)
  { CEnumerationLiteral cenumerationliteralx_ = (CEnumerationLiteral) cenumerationliterals.get(_i);
    out.println("cenumerationliteralx_" + _i + " : CEnumerationLiteral");
    out.println("cenumerationliteralx_" + _i + ".name = \"" + cenumerationliteralx_.getname() + "\"");
  }

  for (int _i = 0; _i < binaryexpressions.size(); _i++)
  { BinaryExpression binaryexpressionx_ = (BinaryExpression) binaryexpressions.get(_i);
    out.println("binaryexpressionx_" + _i + " : BinaryExpression");
    out.println("binaryexpressionx_" + _i + ".operator = \"" + binaryexpressionx_.getoperator() + "\"");
    out.println("binaryexpressionx_" + _i + ".variable = \"" + binaryexpressionx_.getvariable() + "\"");
    out.println("binaryexpressionx_" + _i + ".needsBracket = " + binaryexpressionx_.getneedsBracket());
    out.println("binaryexpressionx_" + _i + ".umlKind = " + binaryexpressionx_.getumlKind());
    out.println("binaryexpressionx_" + _i + ".expId = \"" + binaryexpressionx_.getexpId() + "\"");
    out.println("binaryexpressionx_" + _i + ".isStatic = " + binaryexpressionx_.getisStatic());
  }

  for (int _i = 0; _i < conditionalexpressions.size(); _i++)
  { ConditionalExpression conditionalexpressionx_ = (ConditionalExpression) conditionalexpressions.get(_i);
    out.println("conditionalexpressionx_" + _i + " : ConditionalExpression");
    out.println("conditionalexpressionx_" + _i + ".needsBracket = " + conditionalexpressionx_.getneedsBracket());
    out.println("conditionalexpressionx_" + _i + ".umlKind = " + conditionalexpressionx_.getumlKind());
    out.println("conditionalexpressionx_" + _i + ".expId = \"" + conditionalexpressionx_.getexpId() + "\"");
    out.println("conditionalexpressionx_" + _i + ".isStatic = " + conditionalexpressionx_.getisStatic());
  }

  for (int _i = 0; _i < unaryexpressions.size(); _i++)
  { UnaryExpression unaryexpressionx_ = (UnaryExpression) unaryexpressions.get(_i);
    out.println("unaryexpressionx_" + _i + " : UnaryExpression");
    out.println("unaryexpressionx_" + _i + ".operator = \"" + unaryexpressionx_.getoperator() + "\"");
    out.println("unaryexpressionx_" + _i + ".variable = \"" + unaryexpressionx_.getvariable() + "\"");
    out.println("unaryexpressionx_" + _i + ".needsBracket = " + unaryexpressionx_.getneedsBracket());
    out.println("unaryexpressionx_" + _i + ".umlKind = " + unaryexpressionx_.getumlKind());
    out.println("unaryexpressionx_" + _i + ".expId = \"" + unaryexpressionx_.getexpId() + "\"");
    out.println("unaryexpressionx_" + _i + ".isStatic = " + unaryexpressionx_.getisStatic());
  }

  for (int _i = 0; _i < collectionexpressions.size(); _i++)
  { CollectionExpression collectionexpressionx_ = (CollectionExpression) collectionexpressions.get(_i);
    out.println("collectionexpressionx_" + _i + " : CollectionExpression");
    out.println("collectionexpressionx_" + _i + ".isOrdered = " + collectionexpressionx_.getisOrdered());
    out.println("collectionexpressionx_" + _i + ".needsBracket = " + collectionexpressionx_.getneedsBracket());
    out.println("collectionexpressionx_" + _i + ".umlKind = " + collectionexpressionx_.getumlKind());
    out.println("collectionexpressionx_" + _i + ".expId = \"" + collectionexpressionx_.getexpId() + "\"");
    out.println("collectionexpressionx_" + _i + ".isStatic = " + collectionexpressionx_.getisStatic());
  }

  for (int _i = 0; _i < basicexpressions.size(); _i++)
  { BasicExpression basicexpressionx_ = (BasicExpression) basicexpressions.get(_i);
    out.println("basicexpressionx_" + _i + " : BasicExpression");
    out.println("basicexpressionx_" + _i + ".data = \"" + basicexpressionx_.getdata() + "\"");
    out.println("basicexpressionx_" + _i + ".prestate = " + basicexpressionx_.getprestate());
    out.println("basicexpressionx_" + _i + ".needsBracket = " + basicexpressionx_.getneedsBracket());
    out.println("basicexpressionx_" + _i + ".umlKind = " + basicexpressionx_.getumlKind());
    out.println("basicexpressionx_" + _i + ".expId = \"" + basicexpressionx_.getexpId() + "\"");
    out.println("basicexpressionx_" + _i + ".isStatic = " + basicexpressionx_.getisStatic());
  }

  for (int _i = 0; _i < printcodes.size(); _i++)
  { Printcode printcodex_ = (Printcode) printcodes.get(_i);
    out.println("printcodex_" + _i + " : Printcode");
  }

  for (int _i = 0; _i < types2cs.size(); _i++)
  { Types2C types2cx_ = (Types2C) types2cs.get(_i);
    out.println("types2cx_" + _i + " : Types2C");
  }

  for (int _i = 0; _i < program2cs.size(); _i++)
  { Program2C program2cx_ = (Program2C) program2cs.get(_i);
    out.println("program2cx_" + _i + " : Program2C");
  }

  for (int _i = 0; _i < returnstatements.size(); _i++)
  { ReturnStatement returnstatementx_ = (ReturnStatement) returnstatements.get(_i);
    out.println("returnstatementx_" + _i + " : ReturnStatement");
    out.println("returnstatementx_" + _i + ".statId = \"" + returnstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < breakstatements.size(); _i++)
  { BreakStatement breakstatementx_ = (BreakStatement) breakstatements.get(_i);
    out.println("breakstatementx_" + _i + " : BreakStatement");
    out.println("breakstatementx_" + _i + ".statId = \"" + breakstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < operationcallstatements.size(); _i++)
  { OperationCallStatement operationcallstatementx_ = (OperationCallStatement) operationcallstatements.get(_i);
    out.println("operationcallstatementx_" + _i + " : OperationCallStatement");
    out.println("operationcallstatementx_" + _i + ".assignsTo = \"" + operationcallstatementx_.getassignsTo() + "\"");
    out.println("operationcallstatementx_" + _i + ".statId = \"" + operationcallstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < implicitcallstatements.size(); _i++)
  { ImplicitCallStatement implicitcallstatementx_ = (ImplicitCallStatement) implicitcallstatements.get(_i);
    out.println("implicitcallstatementx_" + _i + " : ImplicitCallStatement");
    out.println("implicitcallstatementx_" + _i + ".assignsTo = \"" + implicitcallstatementx_.getassignsTo() + "\"");
    out.println("implicitcallstatementx_" + _i + ".statId = \"" + implicitcallstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < boundedloopstatements.size(); _i++)
  { BoundedLoopStatement boundedloopstatementx_ = (BoundedLoopStatement) boundedloopstatements.get(_i);
    out.println("boundedloopstatementx_" + _i + " : BoundedLoopStatement");
    out.println("boundedloopstatementx_" + _i + ".statId = \"" + boundedloopstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < unboundedloopstatements.size(); _i++)
  { UnboundedLoopStatement unboundedloopstatementx_ = (UnboundedLoopStatement) unboundedloopstatements.get(_i);
    out.println("unboundedloopstatementx_" + _i + " : UnboundedLoopStatement");
    out.println("unboundedloopstatementx_" + _i + ".statId = \"" + unboundedloopstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < assignstatements.size(); _i++)
  { AssignStatement assignstatementx_ = (AssignStatement) assignstatements.get(_i);
    out.println("assignstatementx_" + _i + " : AssignStatement");
    out.println("assignstatementx_" + _i + ".statId = \"" + assignstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < sequencestatements.size(); _i++)
  { SequenceStatement sequencestatementx_ = (SequenceStatement) sequencestatements.get(_i);
    out.println("sequencestatementx_" + _i + " : SequenceStatement");
    out.println("sequencestatementx_" + _i + ".kind = " + sequencestatementx_.getkind());
    out.println("sequencestatementx_" + _i + ".statId = \"" + sequencestatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < conditionalstatements.size(); _i++)
  { ConditionalStatement conditionalstatementx_ = (ConditionalStatement) conditionalstatements.get(_i);
    out.println("conditionalstatementx_" + _i + " : ConditionalStatement");
    out.println("conditionalstatementx_" + _i + ".statId = \"" + conditionalstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < creationstatements.size(); _i++)
  { CreationStatement creationstatementx_ = (CreationStatement) creationstatements.get(_i);
    out.println("creationstatementx_" + _i + " : CreationStatement");
    out.println("creationstatementx_" + _i + ".createsInstanceOf = \"" + creationstatementx_.getcreatesInstanceOf() + "\"");
    out.println("creationstatementx_" + _i + ".assignsTo = \"" + creationstatementx_.getassignsTo() + "\"");
    out.println("creationstatementx_" + _i + ".statId = \"" + creationstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < errorstatements.size(); _i++)
  { ErrorStatement errorstatementx_ = (ErrorStatement) errorstatements.get(_i);
    out.println("errorstatementx_" + _i + " : ErrorStatement");
    out.println("errorstatementx_" + _i + ".statId = \"" + errorstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < catchstatements.size(); _i++)
  { CatchStatement catchstatementx_ = (CatchStatement) catchstatements.get(_i);
    out.println("catchstatementx_" + _i + " : CatchStatement");
    out.println("catchstatementx_" + _i + ".statId = \"" + catchstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < finalstatements.size(); _i++)
  { FinalStatement finalstatementx_ = (FinalStatement) finalstatements.get(_i);
    out.println("finalstatementx_" + _i + " : FinalStatement");
    out.println("finalstatementx_" + _i + ".statId = \"" + finalstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < trystatements.size(); _i++)
  { TryStatement trystatementx_ = (TryStatement) trystatements.get(_i);
    out.println("trystatementx_" + _i + " : TryStatement");
    out.println("trystatementx_" + _i + ".statId = \"" + trystatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < assertstatements.size(); _i++)
  { AssertStatement assertstatementx_ = (AssertStatement) assertstatements.get(_i);
    out.println("assertstatementx_" + _i + " : AssertStatement");
    out.println("assertstatementx_" + _i + ".statId = \"" + assertstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < enumerations.size(); _i++)
  { Enumeration enumerationx_ = (Enumeration) enumerations.get(_i);
    List enumeration_ownedLiteral_EnumerationLiteral = enumerationx_.getownedLiteral();
    for (int _j = 0; _j < enumeration_ownedLiteral_EnumerationLiteral.size(); _j++)
    { out.println("enumerationliteralx_" + enumerationliterals.indexOf(enumeration_ownedLiteral_EnumerationLiteral.get(_j)) + " : enumerationx_" + _i + ".ownedLiteral");
    }
  }
  for (int _i = 0; _i < entitys.size(); _i++)
  { Entity entityx_ = (Entity) entitys.get(_i);
    List entity_ownedAttribute_Property = entityx_.getownedAttribute();
    for (int _j = 0; _j < entity_ownedAttribute_Property.size(); _j++)
    { out.println("propertyx_" + propertys.indexOf(entity_ownedAttribute_Property.get(_j)) + " : entityx_" + _i + ".ownedAttribute");
    }
    List entity_superclass_Entity = entityx_.getsuperclass();
    for (int _j = 0; _j < entity_superclass_Entity.size(); _j++)
    { out.println("entityx_" + entitys.indexOf(entity_superclass_Entity.get(_j)) + " : entityx_" + _i + ".superclass");
    }
    List entity_subclasses_Entity = entityx_.getsubclasses();
    for (int _j = 0; _j < entity_subclasses_Entity.size(); _j++)
    { out.println("entityx_" + entitys.indexOf(entity_subclasses_Entity.get(_j)) + " : entityx_" + _i + ".subclasses");
    }
    List entity_generalization_Generalization = entityx_.getgeneralization();
    for (int _j = 0; _j < entity_generalization_Generalization.size(); _j++)
    { out.println("generalizationx_" + generalizations.indexOf(entity_generalization_Generalization.get(_j)) + " : entityx_" + _i + ".generalization");
    }
    List entity_specialization_Generalization = entityx_.getspecialization();
    for (int _j = 0; _j < entity_specialization_Generalization.size(); _j++)
    { out.println("generalizationx_" + generalizations.indexOf(entity_specialization_Generalization.get(_j)) + " : entityx_" + _i + ".specialization");
    }
    List entity_ownedOperation_Operation = entityx_.getownedOperation();
    for (int _j = 0; _j < entity_ownedOperation_Operation.size(); _j++)
    { out.println("operationx_" + operations.indexOf(entity_ownedOperation_Operation.get(_j)) + " : entityx_" + _i + ".ownedOperation");
    }
  }
  for (int _i = 0; _i < propertys.size(); _i++)
  { Property propertyx_ = (Property) propertys.get(_i);
    out.println("propertyx_" + _i + ".owner = entityx_" + entitys.indexOf(((Property) propertys.get(_i)).getowner()));
    if (propertyx_.getinitialValue() instanceof UnaryExpression)
    { out.println("propertyx_" + _i + ".initialValue = unaryexpressionx_" + unaryexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.getinitialValue() instanceof ConditionalExpression)
    { out.println("propertyx_" + _i + ".initialValue = conditionalexpressionx_" + conditionalexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.getinitialValue() instanceof BasicExpression)
    { out.println("propertyx_" + _i + ".initialValue = basicexpressionx_" + basicexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.getinitialValue() instanceof BinaryExpression)
    { out.println("propertyx_" + _i + ".initialValue = binaryexpressionx_" + binaryexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.getinitialValue() instanceof CollectionExpression)
    { out.println("propertyx_" + _i + ".initialValue = collectionexpressionx_" + collectionexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.gettype() instanceof Entity)
    { out.println("propertyx_" + _i + ".type = entityx_" + entitys.indexOf(propertyx_.gettype())); } 
    if (propertyx_.gettype() instanceof UseCase)
    { out.println("propertyx_" + _i + ".type = usecasex_" + usecases.indexOf(propertyx_.gettype())); } 
    if (propertyx_.gettype() instanceof PrimitiveType)
    { out.println("propertyx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(propertyx_.gettype())); } 
    if (propertyx_.gettype() instanceof Enumeration)
    { out.println("propertyx_" + _i + ".type = enumerationx_" + enumerations.indexOf(propertyx_.gettype())); } 
    if (propertyx_.gettype() instanceof CollectionType)
    { out.println("propertyx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(propertyx_.gettype())); } 
    if (propertyx_.getelementType() instanceof Entity)
    { out.println("propertyx_" + _i + ".elementType = entityx_" + entitys.indexOf(propertyx_.getelementType())); } 
    if (propertyx_.getelementType() instanceof UseCase)
    { out.println("propertyx_" + _i + ".elementType = usecasex_" + usecases.indexOf(propertyx_.getelementType())); } 
    if (propertyx_.getelementType() instanceof PrimitiveType)
    { out.println("propertyx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(propertyx_.getelementType())); } 
    if (propertyx_.getelementType() instanceof Enumeration)
    { out.println("propertyx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(propertyx_.getelementType())); } 
    if (propertyx_.getelementType() instanceof CollectionType)
    { out.println("propertyx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(propertyx_.getelementType())); } 
  }
  for (int _i = 0; _i < associations.size(); _i++)
  { Association associationx_ = (Association) associations.get(_i);
    List association_memberEnd_Property = associationx_.getmemberEnd();
    for (int _j = 0; _j < association_memberEnd_Property.size(); _j++)
    { out.println("propertyx_" + propertys.indexOf(association_memberEnd_Property.get(_j)) + " : associationx_" + _i + ".memberEnd");
    }
  }
  for (int _i = 0; _i < generalizations.size(); _i++)
  { Generalization generalizationx_ = (Generalization) generalizations.get(_i);
    out.println("generalizationx_" + _i + ".specific = entityx_" + entitys.indexOf(((Generalization) generalizations.get(_i)).getspecific()));
    out.println("generalizationx_" + _i + ".general = entityx_" + entitys.indexOf(((Generalization) generalizations.get(_i)).getgeneral()));
  }
  for (int _i = 0; _i < operations.size(); _i++)
  { Operation operationx_ = (Operation) operations.get(_i);
    out.println("operationx_" + _i + ".owner = entityx_" + entitys.indexOf(((Operation) operations.get(_i)).getowner()));
    List operation_definers_Entity = operationx_.getdefiners();
    for (int _j = 0; _j < operation_definers_Entity.size(); _j++)
    { out.println("entityx_" + entitys.indexOf(operation_definers_Entity.get(_j)) + " : operationx_" + _i + ".definers");
    }
    List operation_parameters_Property = operationx_.getparameters();
    for (int _j = 0; _j < operation_parameters_Property.size(); _j++)
    { out.println("propertyx_" + propertys.indexOf(operation_parameters_Property.get(_j)) + " : operationx_" + _i + ".parameters");
    }
    if (operationx_.getactivity() instanceof ReturnStatement)
    { out.println("operationx_" + _i + ".activity = returnstatementx_" + returnstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof BreakStatement)
    { out.println("operationx_" + _i + ".activity = breakstatementx_" + breakstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof OperationCallStatement)
    { out.println("operationx_" + _i + ".activity = operationcallstatementx_" + operationcallstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof ImplicitCallStatement)
    { out.println("operationx_" + _i + ".activity = implicitcallstatementx_" + implicitcallstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof SequenceStatement)
    { out.println("operationx_" + _i + ".activity = sequencestatementx_" + sequencestatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof ConditionalStatement)
    { out.println("operationx_" + _i + ".activity = conditionalstatementx_" + conditionalstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof AssignStatement)
    { out.println("operationx_" + _i + ".activity = assignstatementx_" + assignstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof CreationStatement)
    { out.println("operationx_" + _i + ".activity = creationstatementx_" + creationstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof CatchStatement)
    { out.println("operationx_" + _i + ".activity = catchstatementx_" + catchstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof FinalStatement)
    { out.println("operationx_" + _i + ".activity = finalstatementx_" + finalstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof TryStatement)
    { out.println("operationx_" + _i + ".activity = trystatementx_" + trystatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof AssertStatement)
    { out.println("operationx_" + _i + ".activity = assertstatementx_" + assertstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof ErrorStatement)
    { out.println("operationx_" + _i + ".activity = errorstatementx_" + errorstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof BoundedLoopStatement)
    { out.println("operationx_" + _i + ".activity = boundedloopstatementx_" + boundedloopstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof UnboundedLoopStatement)
    { out.println("operationx_" + _i + ".activity = unboundedloopstatementx_" + unboundedloopstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.gettype() instanceof Entity)
    { out.println("operationx_" + _i + ".type = entityx_" + entitys.indexOf(operationx_.gettype())); } 
    if (operationx_.gettype() instanceof UseCase)
    { out.println("operationx_" + _i + ".type = usecasex_" + usecases.indexOf(operationx_.gettype())); } 
    if (operationx_.gettype() instanceof PrimitiveType)
    { out.println("operationx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(operationx_.gettype())); } 
    if (operationx_.gettype() instanceof Enumeration)
    { out.println("operationx_" + _i + ".type = enumerationx_" + enumerations.indexOf(operationx_.gettype())); } 
    if (operationx_.gettype() instanceof CollectionType)
    { out.println("operationx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(operationx_.gettype())); } 
    if (operationx_.getelementType() instanceof Entity)
    { out.println("operationx_" + _i + ".elementType = entityx_" + entitys.indexOf(operationx_.getelementType())); } 
    if (operationx_.getelementType() instanceof UseCase)
    { out.println("operationx_" + _i + ".elementType = usecasex_" + usecases.indexOf(operationx_.getelementType())); } 
    if (operationx_.getelementType() instanceof PrimitiveType)
    { out.println("operationx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(operationx_.getelementType())); } 
    if (operationx_.getelementType() instanceof Enumeration)
    { out.println("operationx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(operationx_.getelementType())); } 
    if (operationx_.getelementType() instanceof CollectionType)
    { out.println("operationx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(operationx_.getelementType())); } 
  }
  for (int _i = 0; _i < usecases.size(); _i++)
  { UseCase usecasex_ = (UseCase) usecases.get(_i);
    List usecase_parameters_Property = usecasex_.getparameters();
    for (int _j = 0; _j < usecase_parameters_Property.size(); _j++)
    { out.println("propertyx_" + propertys.indexOf(usecase_parameters_Property.get(_j)) + " : usecasex_" + _i + ".parameters");
    }
    if (usecasex_.getclassifierBehaviour() instanceof ReturnStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = returnstatementx_" + returnstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof BreakStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = breakstatementx_" + breakstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof OperationCallStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = operationcallstatementx_" + operationcallstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof ImplicitCallStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = implicitcallstatementx_" + implicitcallstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof SequenceStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = sequencestatementx_" + sequencestatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof ConditionalStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = conditionalstatementx_" + conditionalstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof AssignStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = assignstatementx_" + assignstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof CreationStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = creationstatementx_" + creationstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof CatchStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = catchstatementx_" + catchstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof FinalStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = finalstatementx_" + finalstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof TryStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = trystatementx_" + trystatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof AssertStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = assertstatementx_" + assertstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof ErrorStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = errorstatementx_" + errorstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof BoundedLoopStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = boundedloopstatementx_" + boundedloopstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof UnboundedLoopStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = unboundedloopstatementx_" + unboundedloopstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getresultType() instanceof Entity)
    { out.println("usecasex_" + _i + ".resultType = entityx_" + entitys.indexOf(usecasex_.getresultType())); } 
    if (usecasex_.getresultType() instanceof UseCase)
    { out.println("usecasex_" + _i + ".resultType = usecasex_" + usecases.indexOf(usecasex_.getresultType())); } 
    if (usecasex_.getresultType() instanceof PrimitiveType)
    { out.println("usecasex_" + _i + ".resultType = primitivetypex_" + primitivetypes.indexOf(usecasex_.getresultType())); } 
    if (usecasex_.getresultType() instanceof Enumeration)
    { out.println("usecasex_" + _i + ".resultType = enumerationx_" + enumerations.indexOf(usecasex_.getresultType())); } 
    if (usecasex_.getresultType() instanceof CollectionType)
    { out.println("usecasex_" + _i + ".resultType = collectiontypex_" + collectiontypes.indexOf(usecasex_.getresultType())); } 
  }
  for (int _i = 0; _i < collectiontypes.size(); _i++)
  { CollectionType collectiontypex_ = (CollectionType) collectiontypes.get(_i);
    if (collectiontypex_.getelementType() instanceof Entity)
    { out.println("collectiontypex_" + _i + ".elementType = entityx_" + entitys.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getelementType() instanceof UseCase)
    { out.println("collectiontypex_" + _i + ".elementType = usecasex_" + usecases.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getelementType() instanceof PrimitiveType)
    { out.println("collectiontypex_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getelementType() instanceof Enumeration)
    { out.println("collectiontypex_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getelementType() instanceof CollectionType)
    { out.println("collectiontypex_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getkeyType() instanceof Entity)
    { out.println("collectiontypex_" + _i + ".keyType = entityx_" + entitys.indexOf(collectiontypex_.getkeyType())); } 
    if (collectiontypex_.getkeyType() instanceof UseCase)
    { out.println("collectiontypex_" + _i + ".keyType = usecasex_" + usecases.indexOf(collectiontypex_.getkeyType())); } 
    if (collectiontypex_.getkeyType() instanceof PrimitiveType)
    { out.println("collectiontypex_" + _i + ".keyType = primitivetypex_" + primitivetypes.indexOf(collectiontypex_.getkeyType())); } 
    if (collectiontypex_.getkeyType() instanceof Enumeration)
    { out.println("collectiontypex_" + _i + ".keyType = enumerationx_" + enumerations.indexOf(collectiontypex_.getkeyType())); } 
    if (collectiontypex_.getkeyType() instanceof CollectionType)
    { out.println("collectiontypex_" + _i + ".keyType = collectiontypex_" + collectiontypes.indexOf(collectiontypex_.getkeyType())); } 
  }
  for (int _i = 0; _i < cstructs.size(); _i++)
  { CStruct cstructx_ = (CStruct) cstructs.get(_i);
    List cstruct_interfaces_CStruct = cstructx_.getinterfaces();
    for (int _j = 0; _j < cstruct_interfaces_CStruct.size(); _j++)
    { out.println("cstructx_" + cstructs.indexOf(cstruct_interfaces_CStruct.get(_j)) + " : cstructx_" + _i + ".interfaces");
    }
    List cstruct_members_CMember = cstructx_.getmembers();
    for (int _j = 0; _j < cstruct_members_CMember.size(); _j++)
    { out.println("cmemberx_" + cmembers.indexOf(cstruct_members_CMember.get(_j)) + " : cstructx_" + _i + ".members");
    }
    List cstruct_allMembers_CMember = cstructx_.getallMembers();
    for (int _j = 0; _j < cstruct_allMembers_CMember.size(); _j++)
    { out.println("cmemberx_" + cmembers.indexOf(cstruct_allMembers_CMember.get(_j)) + " : cstructx_" + _i + ".allMembers");
    }
  }
  for (int _i = 0; _i < cmembers.size(); _i++)
  { CMember cmemberx_ = (CMember) cmembers.get(_i);
    if (cmemberx_.gettype() instanceof CStruct)
    { out.println("cmemberx_" + _i + ".type = cstructx_" + cstructs.indexOf(cmemberx_.gettype())); } 
    if (cmemberx_.gettype() instanceof CPrimitiveType)
    { out.println("cmemberx_" + _i + ".type = cprimitivetypex_" + cprimitivetypes.indexOf(cmemberx_.gettype())); } 
    if (cmemberx_.gettype() instanceof CPointerType)
    { out.println("cmemberx_" + _i + ".type = cpointertypex_" + cpointertypes.indexOf(cmemberx_.gettype())); } 
    if (cmemberx_.gettype() instanceof CFunctionPointerType)
    { out.println("cmemberx_" + _i + ".type = cfunctionpointertypex_" + cfunctionpointertypes.indexOf(cmemberx_.gettype())); } 
    if (cmemberx_.gettype() instanceof CArrayType)
    { out.println("cmemberx_" + _i + ".type = carraytypex_" + carraytypes.indexOf(cmemberx_.gettype())); } 
    if (cmemberx_.gettype() instanceof CEnumeration)
    { out.println("cmemberx_" + _i + ".type = cenumerationx_" + cenumerations.indexOf(cmemberx_.gettype())); } 
  }
  for (int _i = 0; _i < coperations.size(); _i++)
  { COperation coperationx_ = (COperation) coperations.get(_i);
    List coperation_parameters_CVariable = coperationx_.getparameters();
    for (int _j = 0; _j < coperation_parameters_CVariable.size(); _j++)
    { out.println("cvariablex_" + cvariables.indexOf(coperation_parameters_CVariable.get(_j)) + " : coperationx_" + _i + ".parameters");
    }
    List coperation_definers_CStruct = coperationx_.getdefiners();
    for (int _j = 0; _j < coperation_definers_CStruct.size(); _j++)
    { out.println("cstructx_" + cstructs.indexOf(coperation_definers_CStruct.get(_j)) + " : coperationx_" + _i + ".definers");
    }
    if (coperationx_.getreturnType() instanceof CStruct)
    { out.println("coperationx_" + _i + ".returnType = cstructx_" + cstructs.indexOf(coperationx_.getreturnType())); } 
    if (coperationx_.getreturnType() instanceof CPrimitiveType)
    { out.println("coperationx_" + _i + ".returnType = cprimitivetypex_" + cprimitivetypes.indexOf(coperationx_.getreturnType())); } 
    if (coperationx_.getreturnType() instanceof CPointerType)
    { out.println("coperationx_" + _i + ".returnType = cpointertypex_" + cpointertypes.indexOf(coperationx_.getreturnType())); } 
    if (coperationx_.getreturnType() instanceof CFunctionPointerType)
    { out.println("coperationx_" + _i + ".returnType = cfunctionpointertypex_" + cfunctionpointertypes.indexOf(coperationx_.getreturnType())); } 
    if (coperationx_.getreturnType() instanceof CArrayType)
    { out.println("coperationx_" + _i + ".returnType = carraytypex_" + carraytypes.indexOf(coperationx_.getreturnType())); } 
    if (coperationx_.getreturnType() instanceof CEnumeration)
    { out.println("coperationx_" + _i + ".returnType = cenumerationx_" + cenumerations.indexOf(coperationx_.getreturnType())); } 
    if (coperationx_.getelementType() instanceof CStruct)
    { out.println("coperationx_" + _i + ".elementType = cstructx_" + cstructs.indexOf(coperationx_.getelementType())); } 
    if (coperationx_.getelementType() instanceof CPrimitiveType)
    { out.println("coperationx_" + _i + ".elementType = cprimitivetypex_" + cprimitivetypes.indexOf(coperationx_.getelementType())); } 
    if (coperationx_.getelementType() instanceof CPointerType)
    { out.println("coperationx_" + _i + ".elementType = cpointertypex_" + cpointertypes.indexOf(coperationx_.getelementType())); } 
    if (coperationx_.getelementType() instanceof CFunctionPointerType)
    { out.println("coperationx_" + _i + ".elementType = cfunctionpointertypex_" + cfunctionpointertypes.indexOf(coperationx_.getelementType())); } 
    if (coperationx_.getelementType() instanceof CArrayType)
    { out.println("coperationx_" + _i + ".elementType = carraytypex_" + carraytypes.indexOf(coperationx_.getelementType())); } 
    if (coperationx_.getelementType() instanceof CEnumeration)
    { out.println("coperationx_" + _i + ".elementType = cenumerationx_" + cenumerations.indexOf(coperationx_.getelementType())); } 
  }
  for (int _i = 0; _i < cvariables.size(); _i++)
  { CVariable cvariablex_ = (CVariable) cvariables.get(_i);
    if (cvariablex_.gettype() instanceof CStruct)
    { out.println("cvariablex_" + _i + ".type = cstructx_" + cstructs.indexOf(cvariablex_.gettype())); } 
    if (cvariablex_.gettype() instanceof CPrimitiveType)
    { out.println("cvariablex_" + _i + ".type = cprimitivetypex_" + cprimitivetypes.indexOf(cvariablex_.gettype())); } 
    if (cvariablex_.gettype() instanceof CPointerType)
    { out.println("cvariablex_" + _i + ".type = cpointertypex_" + cpointertypes.indexOf(cvariablex_.gettype())); } 
    if (cvariablex_.gettype() instanceof CFunctionPointerType)
    { out.println("cvariablex_" + _i + ".type = cfunctionpointertypex_" + cfunctionpointertypes.indexOf(cvariablex_.gettype())); } 
    if (cvariablex_.gettype() instanceof CArrayType)
    { out.println("cvariablex_" + _i + ".type = carraytypex_" + carraytypes.indexOf(cvariablex_.gettype())); } 
    if (cvariablex_.gettype() instanceof CEnumeration)
    { out.println("cvariablex_" + _i + ".type = cenumerationx_" + cenumerations.indexOf(cvariablex_.gettype())); } 
  }
  for (int _i = 0; _i < cprograms.size(); _i++)
  { CProgram cprogramx_ = (CProgram) cprograms.get(_i);
    List cprogram_operations_COperation = cprogramx_.getoperations();
    for (int _j = 0; _j < cprogram_operations_COperation.size(); _j++)
    { out.println("coperationx_" + coperations.indexOf(cprogram_operations_COperation.get(_j)) + " : cprogramx_" + _i + ".operations");
    }
    List cprogram_variables_CVariable = cprogramx_.getvariables();
    for (int _j = 0; _j < cprogram_variables_CVariable.size(); _j++)
    { out.println("cvariablex_" + cvariables.indexOf(cprogram_variables_CVariable.get(_j)) + " : cprogramx_" + _i + ".variables");
    }
    List cprogram_structs_CStruct = cprogramx_.getstructs();
    for (int _j = 0; _j < cprogram_structs_CStruct.size(); _j++)
    { out.println("cstructx_" + cstructs.indexOf(cprogram_structs_CStruct.get(_j)) + " : cprogramx_" + _i + ".structs");
    }
  }
  for (int _i = 0; _i < carraytypes.size(); _i++)
  { CArrayType carraytypex_ = (CArrayType) carraytypes.get(_i);
    if (carraytypex_.getcomponentType() instanceof CStruct)
    { out.println("carraytypex_" + _i + ".componentType = cstructx_" + cstructs.indexOf(carraytypex_.getcomponentType())); } 
    if (carraytypex_.getcomponentType() instanceof CPrimitiveType)
    { out.println("carraytypex_" + _i + ".componentType = cprimitivetypex_" + cprimitivetypes.indexOf(carraytypex_.getcomponentType())); } 
    if (carraytypex_.getcomponentType() instanceof CPointerType)
    { out.println("carraytypex_" + _i + ".componentType = cpointertypex_" + cpointertypes.indexOf(carraytypex_.getcomponentType())); } 
    if (carraytypex_.getcomponentType() instanceof CFunctionPointerType)
    { out.println("carraytypex_" + _i + ".componentType = cfunctionpointertypex_" + cfunctionpointertypes.indexOf(carraytypex_.getcomponentType())); } 
    if (carraytypex_.getcomponentType() instanceof CArrayType)
    { out.println("carraytypex_" + _i + ".componentType = carraytypex_" + carraytypes.indexOf(carraytypex_.getcomponentType())); } 
    if (carraytypex_.getcomponentType() instanceof CEnumeration)
    { out.println("carraytypex_" + _i + ".componentType = cenumerationx_" + cenumerations.indexOf(carraytypex_.getcomponentType())); } 
  }
  for (int _i = 0; _i < cpointertypes.size(); _i++)
  { CPointerType cpointertypex_ = (CPointerType) cpointertypes.get(_i);
    if (cpointertypex_.getpointsTo() instanceof CStruct)
    { out.println("cpointertypex_" + _i + ".pointsTo = cstructx_" + cstructs.indexOf(cpointertypex_.getpointsTo())); } 
    if (cpointertypex_.getpointsTo() instanceof CPrimitiveType)
    { out.println("cpointertypex_" + _i + ".pointsTo = cprimitivetypex_" + cprimitivetypes.indexOf(cpointertypex_.getpointsTo())); } 
    if (cpointertypex_.getpointsTo() instanceof CPointerType)
    { out.println("cpointertypex_" + _i + ".pointsTo = cpointertypex_" + cpointertypes.indexOf(cpointertypex_.getpointsTo())); } 
    if (cpointertypex_.getpointsTo() instanceof CFunctionPointerType)
    { out.println("cpointertypex_" + _i + ".pointsTo = cfunctionpointertypex_" + cfunctionpointertypes.indexOf(cpointertypex_.getpointsTo())); } 
    if (cpointertypex_.getpointsTo() instanceof CArrayType)
    { out.println("cpointertypex_" + _i + ".pointsTo = carraytypex_" + carraytypes.indexOf(cpointertypex_.getpointsTo())); } 
    if (cpointertypex_.getpointsTo() instanceof CEnumeration)
    { out.println("cpointertypex_" + _i + ".pointsTo = cenumerationx_" + cenumerations.indexOf(cpointertypex_.getpointsTo())); } 
  }
  for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
  { CFunctionPointerType cfunctionpointertypex_ = (CFunctionPointerType) cfunctionpointertypes.get(_i);
    if (cfunctionpointertypex_.getdomainType() instanceof CStruct)
    { out.println("cfunctionpointertypex_" + _i + ".domainType = cstructx_" + cstructs.indexOf(cfunctionpointertypex_.getdomainType())); } 
    if (cfunctionpointertypex_.getdomainType() instanceof CPrimitiveType)
    { out.println("cfunctionpointertypex_" + _i + ".domainType = cprimitivetypex_" + cprimitivetypes.indexOf(cfunctionpointertypex_.getdomainType())); } 
    if (cfunctionpointertypex_.getdomainType() instanceof CPointerType)
    { out.println("cfunctionpointertypex_" + _i + ".domainType = cpointertypex_" + cpointertypes.indexOf(cfunctionpointertypex_.getdomainType())); } 
    if (cfunctionpointertypex_.getdomainType() instanceof CFunctionPointerType)
    { out.println("cfunctionpointertypex_" + _i + ".domainType = cfunctionpointertypex_" + cfunctionpointertypes.indexOf(cfunctionpointertypex_.getdomainType())); } 
    if (cfunctionpointertypex_.getdomainType() instanceof CArrayType)
    { out.println("cfunctionpointertypex_" + _i + ".domainType = carraytypex_" + carraytypes.indexOf(cfunctionpointertypex_.getdomainType())); } 
    if (cfunctionpointertypex_.getdomainType() instanceof CEnumeration)
    { out.println("cfunctionpointertypex_" + _i + ".domainType = cenumerationx_" + cenumerations.indexOf(cfunctionpointertypex_.getdomainType())); } 
    if (cfunctionpointertypex_.getrangeType() instanceof CStruct)
    { out.println("cfunctionpointertypex_" + _i + ".rangeType = cstructx_" + cstructs.indexOf(cfunctionpointertypex_.getrangeType())); } 
    if (cfunctionpointertypex_.getrangeType() instanceof CPrimitiveType)
    { out.println("cfunctionpointertypex_" + _i + ".rangeType = cprimitivetypex_" + cprimitivetypes.indexOf(cfunctionpointertypex_.getrangeType())); } 
    if (cfunctionpointertypex_.getrangeType() instanceof CPointerType)
    { out.println("cfunctionpointertypex_" + _i + ".rangeType = cpointertypex_" + cpointertypes.indexOf(cfunctionpointertypex_.getrangeType())); } 
    if (cfunctionpointertypex_.getrangeType() instanceof CFunctionPointerType)
    { out.println("cfunctionpointertypex_" + _i + ".rangeType = cfunctionpointertypex_" + cfunctionpointertypes.indexOf(cfunctionpointertypex_.getrangeType())); } 
    if (cfunctionpointertypex_.getrangeType() instanceof CArrayType)
    { out.println("cfunctionpointertypex_" + _i + ".rangeType = carraytypex_" + carraytypes.indexOf(cfunctionpointertypex_.getrangeType())); } 
    if (cfunctionpointertypex_.getrangeType() instanceof CEnumeration)
    { out.println("cfunctionpointertypex_" + _i + ".rangeType = cenumerationx_" + cenumerations.indexOf(cfunctionpointertypex_.getrangeType())); } 
  }
  for (int _i = 0; _i < cenumerations.size(); _i++)
  { CEnumeration cenumerationx_ = (CEnumeration) cenumerations.get(_i);
    List cenumeration_ownedLiteral_CEnumerationLiteral = cenumerationx_.getownedLiteral();
    for (int _j = 0; _j < cenumeration_ownedLiteral_CEnumerationLiteral.size(); _j++)
    { out.println("cenumerationliteralx_" + cenumerationliterals.indexOf(cenumeration_ownedLiteral_CEnumerationLiteral.get(_j)) + " : cenumerationx_" + _i + ".ownedLiteral");
    }
  }
  for (int _i = 0; _i < binaryexpressions.size(); _i++)
  { BinaryExpression binaryexpressionx_ = (BinaryExpression) binaryexpressions.get(_i);
    if (binaryexpressionx_.getleft() instanceof UnaryExpression)
    { out.println("binaryexpressionx_" + _i + ".left = unaryexpressionx_" + unaryexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getleft() instanceof ConditionalExpression)
    { out.println("binaryexpressionx_" + _i + ".left = conditionalexpressionx_" + conditionalexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getleft() instanceof BasicExpression)
    { out.println("binaryexpressionx_" + _i + ".left = basicexpressionx_" + basicexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getleft() instanceof BinaryExpression)
    { out.println("binaryexpressionx_" + _i + ".left = binaryexpressionx_" + binaryexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getleft() instanceof CollectionExpression)
    { out.println("binaryexpressionx_" + _i + ".left = collectionexpressionx_" + collectionexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getright() instanceof UnaryExpression)
    { out.println("binaryexpressionx_" + _i + ".right = unaryexpressionx_" + unaryexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.getright() instanceof ConditionalExpression)
    { out.println("binaryexpressionx_" + _i + ".right = conditionalexpressionx_" + conditionalexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.getright() instanceof BasicExpression)
    { out.println("binaryexpressionx_" + _i + ".right = basicexpressionx_" + basicexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.getright() instanceof BinaryExpression)
    { out.println("binaryexpressionx_" + _i + ".right = binaryexpressionx_" + binaryexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.getright() instanceof CollectionExpression)
    { out.println("binaryexpressionx_" + _i + ".right = collectionexpressionx_" + collectionexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.gettype() instanceof Entity)
    { out.println("binaryexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.gettype() instanceof UseCase)
    { out.println("binaryexpressionx_" + _i + ".type = usecasex_" + usecases.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("binaryexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.gettype() instanceof Enumeration)
    { out.println("binaryexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.gettype() instanceof CollectionType)
    { out.println("binaryexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.getelementType() instanceof Entity)
    { out.println("binaryexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(binaryexpressionx_.getelementType())); } 
    if (binaryexpressionx_.getelementType() instanceof UseCase)
    { out.println("binaryexpressionx_" + _i + ".elementType = usecasex_" + usecases.indexOf(binaryexpressionx_.getelementType())); } 
    if (binaryexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("binaryexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(binaryexpressionx_.getelementType())); } 
    if (binaryexpressionx_.getelementType() instanceof Enumeration)
    { out.println("binaryexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(binaryexpressionx_.getelementType())); } 
    if (binaryexpressionx_.getelementType() instanceof CollectionType)
    { out.println("binaryexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(binaryexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < conditionalexpressions.size(); _i++)
  { ConditionalExpression conditionalexpressionx_ = (ConditionalExpression) conditionalexpressions.get(_i);
    if (conditionalexpressionx_.gettest() instanceof UnaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = unaryexpressionx_" + unaryexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.gettest() instanceof ConditionalExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = conditionalexpressionx_" + conditionalexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.gettest() instanceof BasicExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = basicexpressionx_" + basicexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.gettest() instanceof BinaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = binaryexpressionx_" + binaryexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.gettest() instanceof CollectionExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = collectionexpressionx_" + collectionexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.getifExp() instanceof UnaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = unaryexpressionx_" + unaryexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getifExp() instanceof ConditionalExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = conditionalexpressionx_" + conditionalexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getifExp() instanceof BasicExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = basicexpressionx_" + basicexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getifExp() instanceof BinaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = binaryexpressionx_" + binaryexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getifExp() instanceof CollectionExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = collectionexpressionx_" + collectionexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof UnaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = unaryexpressionx_" + unaryexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof ConditionalExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = conditionalexpressionx_" + conditionalexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof BasicExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = basicexpressionx_" + basicexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof BinaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = binaryexpressionx_" + binaryexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof CollectionExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = collectionexpressionx_" + collectionexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.gettype() instanceof Entity)
    { out.println("conditionalexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.gettype() instanceof UseCase)
    { out.println("conditionalexpressionx_" + _i + ".type = usecasex_" + usecases.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("conditionalexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.gettype() instanceof Enumeration)
    { out.println("conditionalexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.gettype() instanceof CollectionType)
    { out.println("conditionalexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.getelementType() instanceof Entity)
    { out.println("conditionalexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(conditionalexpressionx_.getelementType())); } 
    if (conditionalexpressionx_.getelementType() instanceof UseCase)
    { out.println("conditionalexpressionx_" + _i + ".elementType = usecasex_" + usecases.indexOf(conditionalexpressionx_.getelementType())); } 
    if (conditionalexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("conditionalexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(conditionalexpressionx_.getelementType())); } 
    if (conditionalexpressionx_.getelementType() instanceof Enumeration)
    { out.println("conditionalexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(conditionalexpressionx_.getelementType())); } 
    if (conditionalexpressionx_.getelementType() instanceof CollectionType)
    { out.println("conditionalexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(conditionalexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < unaryexpressions.size(); _i++)
  { UnaryExpression unaryexpressionx_ = (UnaryExpression) unaryexpressions.get(_i);
    if (unaryexpressionx_.getargument() instanceof UnaryExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = unaryexpressionx_" + unaryexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.getargument() instanceof ConditionalExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = conditionalexpressionx_" + conditionalexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.getargument() instanceof BasicExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = basicexpressionx_" + basicexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.getargument() instanceof BinaryExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = binaryexpressionx_" + binaryexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.getargument() instanceof CollectionExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = collectionexpressionx_" + collectionexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.gettype() instanceof Entity)
    { out.println("unaryexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.gettype() instanceof UseCase)
    { out.println("unaryexpressionx_" + _i + ".type = usecasex_" + usecases.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("unaryexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.gettype() instanceof Enumeration)
    { out.println("unaryexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.gettype() instanceof CollectionType)
    { out.println("unaryexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.getelementType() instanceof Entity)
    { out.println("unaryexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(unaryexpressionx_.getelementType())); } 
    if (unaryexpressionx_.getelementType() instanceof UseCase)
    { out.println("unaryexpressionx_" + _i + ".elementType = usecasex_" + usecases.indexOf(unaryexpressionx_.getelementType())); } 
    if (unaryexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("unaryexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(unaryexpressionx_.getelementType())); } 
    if (unaryexpressionx_.getelementType() instanceof Enumeration)
    { out.println("unaryexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(unaryexpressionx_.getelementType())); } 
    if (unaryexpressionx_.getelementType() instanceof CollectionType)
    { out.println("unaryexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(unaryexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < collectionexpressions.size(); _i++)
  { CollectionExpression collectionexpressionx_ = (CollectionExpression) collectionexpressions.get(_i);
    List collectionexpression_elements_Expression = collectionexpressionx_.getelements();
    for (int _k = 0; _k < collectionexpression_elements_Expression.size(); _k++)
    { if (collectionexpression_elements_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
 if (collectionexpression_elements_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
 if (collectionexpression_elements_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
 if (collectionexpression_elements_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
 if (collectionexpression_elements_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
  }
    if (collectionexpressionx_.gettype() instanceof Entity)
    { out.println("collectionexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.gettype() instanceof UseCase)
    { out.println("collectionexpressionx_" + _i + ".type = usecasex_" + usecases.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("collectionexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.gettype() instanceof Enumeration)
    { out.println("collectionexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.gettype() instanceof CollectionType)
    { out.println("collectionexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.getelementType() instanceof Entity)
    { out.println("collectionexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(collectionexpressionx_.getelementType())); } 
    if (collectionexpressionx_.getelementType() instanceof UseCase)
    { out.println("collectionexpressionx_" + _i + ".elementType = usecasex_" + usecases.indexOf(collectionexpressionx_.getelementType())); } 
    if (collectionexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("collectionexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(collectionexpressionx_.getelementType())); } 
    if (collectionexpressionx_.getelementType() instanceof Enumeration)
    { out.println("collectionexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(collectionexpressionx_.getelementType())); } 
    if (collectionexpressionx_.getelementType() instanceof CollectionType)
    { out.println("collectionexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(collectionexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < basicexpressions.size(); _i++)
  { BasicExpression basicexpressionx_ = (BasicExpression) basicexpressions.get(_i);
    List basicexpression_parameters_Expression = basicexpressionx_.getparameters();
    for (int _k = 0; _k < basicexpression_parameters_Expression.size(); _k++)
    { if (basicexpression_parameters_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
 if (basicexpression_parameters_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
 if (basicexpression_parameters_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
 if (basicexpression_parameters_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
 if (basicexpression_parameters_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
  }
    List basicexpression_referredProperty_Property = basicexpressionx_.getreferredProperty();
    for (int _j = 0; _j < basicexpression_referredProperty_Property.size(); _j++)
    { out.println("propertyx_" + propertys.indexOf(basicexpression_referredProperty_Property.get(_j)) + " : basicexpressionx_" + _i + ".referredProperty");
    }
    List basicexpression_context_Entity = basicexpressionx_.getcontext();
    for (int _j = 0; _j < basicexpression_context_Entity.size(); _j++)
    { out.println("entityx_" + entitys.indexOf(basicexpression_context_Entity.get(_j)) + " : basicexpressionx_" + _i + ".context");
    }
    List basicexpression_arrayIndex_Expression = basicexpressionx_.getarrayIndex();
    for (int _k = 0; _k < basicexpression_arrayIndex_Expression.size(); _k++)
    { if (basicexpression_arrayIndex_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
 if (basicexpression_arrayIndex_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
 if (basicexpression_arrayIndex_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
 if (basicexpression_arrayIndex_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
 if (basicexpression_arrayIndex_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
  }
    List basicexpression_objectRef_Expression = basicexpressionx_.getobjectRef();
    for (int _k = 0; _k < basicexpression_objectRef_Expression.size(); _k++)
    { if (basicexpression_objectRef_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
 if (basicexpression_objectRef_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
 if (basicexpression_objectRef_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
 if (basicexpression_objectRef_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
 if (basicexpression_objectRef_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
  }
    if (basicexpressionx_.gettype() instanceof Entity)
    { out.println("basicexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.gettype() instanceof UseCase)
    { out.println("basicexpressionx_" + _i + ".type = usecasex_" + usecases.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("basicexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.gettype() instanceof Enumeration)
    { out.println("basicexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.gettype() instanceof CollectionType)
    { out.println("basicexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.getelementType() instanceof Entity)
    { out.println("basicexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(basicexpressionx_.getelementType())); } 
    if (basicexpressionx_.getelementType() instanceof UseCase)
    { out.println("basicexpressionx_" + _i + ".elementType = usecasex_" + usecases.indexOf(basicexpressionx_.getelementType())); } 
    if (basicexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("basicexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(basicexpressionx_.getelementType())); } 
    if (basicexpressionx_.getelementType() instanceof Enumeration)
    { out.println("basicexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(basicexpressionx_.getelementType())); } 
    if (basicexpressionx_.getelementType() instanceof CollectionType)
    { out.println("basicexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(basicexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < returnstatements.size(); _i++)
  { ReturnStatement returnstatementx_ = (ReturnStatement) returnstatements.get(_i);
    List returnstatement_returnValue_Expression = returnstatementx_.getreturnValue();
    for (int _k = 0; _k < returnstatement_returnValue_Expression.size(); _k++)
    { if (returnstatement_returnValue_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
 if (returnstatement_returnValue_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
 if (returnstatement_returnValue_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
 if (returnstatement_returnValue_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
 if (returnstatement_returnValue_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
  }
  }
  for (int _i = 0; _i < operationcallstatements.size(); _i++)
  { OperationCallStatement operationcallstatementx_ = (OperationCallStatement) operationcallstatements.get(_i);
    if (operationcallstatementx_.getcallExp() instanceof UnaryExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = unaryexpressionx_" + unaryexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
    if (operationcallstatementx_.getcallExp() instanceof ConditionalExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = conditionalexpressionx_" + conditionalexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
    if (operationcallstatementx_.getcallExp() instanceof BasicExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = basicexpressionx_" + basicexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
    if (operationcallstatementx_.getcallExp() instanceof BinaryExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = binaryexpressionx_" + binaryexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
    if (operationcallstatementx_.getcallExp() instanceof CollectionExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = collectionexpressionx_" + collectionexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
  }
  for (int _i = 0; _i < implicitcallstatements.size(); _i++)
  { ImplicitCallStatement implicitcallstatementx_ = (ImplicitCallStatement) implicitcallstatements.get(_i);
    if (implicitcallstatementx_.getcallExp() instanceof UnaryExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = unaryexpressionx_" + unaryexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
    if (implicitcallstatementx_.getcallExp() instanceof ConditionalExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = conditionalexpressionx_" + conditionalexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
    if (implicitcallstatementx_.getcallExp() instanceof BasicExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = basicexpressionx_" + basicexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
    if (implicitcallstatementx_.getcallExp() instanceof BinaryExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = binaryexpressionx_" + binaryexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
    if (implicitcallstatementx_.getcallExp() instanceof CollectionExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = collectionexpressionx_" + collectionexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
  }
  for (int _i = 0; _i < boundedloopstatements.size(); _i++)
  { BoundedLoopStatement boundedloopstatementx_ = (BoundedLoopStatement) boundedloopstatements.get(_i);
    if (boundedloopstatementx_.getloopRange() instanceof UnaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = unaryexpressionx_" + unaryexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopRange() instanceof ConditionalExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = conditionalexpressionx_" + conditionalexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopRange() instanceof BasicExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = basicexpressionx_" + basicexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopRange() instanceof BinaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = binaryexpressionx_" + binaryexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopRange() instanceof CollectionExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = collectionexpressionx_" + collectionexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopVar() instanceof UnaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = unaryexpressionx_" + unaryexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.getloopVar() instanceof ConditionalExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = conditionalexpressionx_" + conditionalexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.getloopVar() instanceof BasicExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = basicexpressionx_" + basicexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.getloopVar() instanceof BinaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = binaryexpressionx_" + binaryexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.getloopVar() instanceof CollectionExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = collectionexpressionx_" + collectionexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.gettest() instanceof UnaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = unaryexpressionx_" + unaryexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.gettest() instanceof ConditionalExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = conditionalexpressionx_" + conditionalexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.gettest() instanceof BasicExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = basicexpressionx_" + basicexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.gettest() instanceof BinaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = binaryexpressionx_" + binaryexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.gettest() instanceof CollectionExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = collectionexpressionx_" + collectionexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.getbody() instanceof ReturnStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = returnstatementx_" + returnstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof BreakStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = breakstatementx_" + breakstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof OperationCallStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = operationcallstatementx_" + operationcallstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof ImplicitCallStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = implicitcallstatementx_" + implicitcallstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof SequenceStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = sequencestatementx_" + sequencestatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof ConditionalStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = conditionalstatementx_" + conditionalstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof AssignStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = assignstatementx_" + assignstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof CreationStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = creationstatementx_" + creationstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof CatchStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = catchstatementx_" + catchstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof FinalStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = finalstatementx_" + finalstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof TryStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = trystatementx_" + trystatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof AssertStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = assertstatementx_" + assertstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof ErrorStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = errorstatementx_" + errorstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof BoundedLoopStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = boundedloopstatementx_" + boundedloopstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof UnboundedLoopStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = unboundedloopstatementx_" + unboundedloopstatements.indexOf(boundedloopstatementx_.getbody())); } 
  }
  for (int _i = 0; _i < unboundedloopstatements.size(); _i++)
  { UnboundedLoopStatement unboundedloopstatementx_ = (UnboundedLoopStatement) unboundedloopstatements.get(_i);
    if (unboundedloopstatementx_.gettest() instanceof UnaryExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = unaryexpressionx_" + unaryexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.gettest() instanceof ConditionalExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = conditionalexpressionx_" + conditionalexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.gettest() instanceof BasicExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = basicexpressionx_" + basicexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.gettest() instanceof BinaryExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = binaryexpressionx_" + binaryexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.gettest() instanceof CollectionExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = collectionexpressionx_" + collectionexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.getbody() instanceof ReturnStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = returnstatementx_" + returnstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof BreakStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = breakstatementx_" + breakstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof OperationCallStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = operationcallstatementx_" + operationcallstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof ImplicitCallStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = implicitcallstatementx_" + implicitcallstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof SequenceStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = sequencestatementx_" + sequencestatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof ConditionalStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = conditionalstatementx_" + conditionalstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof AssignStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = assignstatementx_" + assignstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof CreationStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = creationstatementx_" + creationstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof CatchStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = catchstatementx_" + catchstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof FinalStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = finalstatementx_" + finalstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof TryStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = trystatementx_" + trystatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof AssertStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = assertstatementx_" + assertstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof ErrorStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = errorstatementx_" + errorstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof BoundedLoopStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = boundedloopstatementx_" + boundedloopstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof UnboundedLoopStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = unboundedloopstatementx_" + unboundedloopstatements.indexOf(unboundedloopstatementx_.getbody())); } 
  }
  for (int _i = 0; _i < assignstatements.size(); _i++)
  { AssignStatement assignstatementx_ = (AssignStatement) assignstatements.get(_i);
    List assignstatement_type_Type = assignstatementx_.gettype();
    for (int _k = 0; _k < assignstatement_type_Type.size(); _k++)
    { if (assignstatement_type_Type.get(_k) instanceof Entity)
      { out.println("entityx_" + entitys.indexOf(assignstatement_type_Type.get(_k)) + " : assignstatementx_" + _i + ".type"); }
 if (assignstatement_type_Type.get(_k) instanceof UseCase)
      { out.println("usecasex_" + usecases.indexOf(assignstatement_type_Type.get(_k)) + " : assignstatementx_" + _i + ".type"); }
 if (assignstatement_type_Type.get(_k) instanceof PrimitiveType)
      { out.println("primitivetypex_" + primitivetypes.indexOf(assignstatement_type_Type.get(_k)) + " : assignstatementx_" + _i + ".type"); }
 if (assignstatement_type_Type.get(_k) instanceof Enumeration)
      { out.println("enumerationx_" + enumerations.indexOf(assignstatement_type_Type.get(_k)) + " : assignstatementx_" + _i + ".type"); }
 if (assignstatement_type_Type.get(_k) instanceof CollectionType)
      { out.println("collectiontypex_" + collectiontypes.indexOf(assignstatement_type_Type.get(_k)) + " : assignstatementx_" + _i + ".type"); }
  }
    if (assignstatementx_.getleft() instanceof UnaryExpression)
    { out.println("assignstatementx_" + _i + ".left = unaryexpressionx_" + unaryexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getleft() instanceof ConditionalExpression)
    { out.println("assignstatementx_" + _i + ".left = conditionalexpressionx_" + conditionalexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getleft() instanceof BasicExpression)
    { out.println("assignstatementx_" + _i + ".left = basicexpressionx_" + basicexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getleft() instanceof BinaryExpression)
    { out.println("assignstatementx_" + _i + ".left = binaryexpressionx_" + binaryexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getleft() instanceof CollectionExpression)
    { out.println("assignstatementx_" + _i + ".left = collectionexpressionx_" + collectionexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getright() instanceof UnaryExpression)
    { out.println("assignstatementx_" + _i + ".right = unaryexpressionx_" + unaryexpressions.indexOf(assignstatementx_.getright())); } 
    if (assignstatementx_.getright() instanceof ConditionalExpression)
    { out.println("assignstatementx_" + _i + ".right = conditionalexpressionx_" + conditionalexpressions.indexOf(assignstatementx_.getright())); } 
    if (assignstatementx_.getright() instanceof BasicExpression)
    { out.println("assignstatementx_" + _i + ".right = basicexpressionx_" + basicexpressions.indexOf(assignstatementx_.getright())); } 
    if (assignstatementx_.getright() instanceof BinaryExpression)
    { out.println("assignstatementx_" + _i + ".right = binaryexpressionx_" + binaryexpressions.indexOf(assignstatementx_.getright())); } 
    if (assignstatementx_.getright() instanceof CollectionExpression)
    { out.println("assignstatementx_" + _i + ".right = collectionexpressionx_" + collectionexpressions.indexOf(assignstatementx_.getright())); } 
  }
  for (int _i = 0; _i < sequencestatements.size(); _i++)
  { SequenceStatement sequencestatementx_ = (SequenceStatement) sequencestatements.get(_i);
    List sequencestatement_statements_Statement = sequencestatementx_.getstatements();
    for (int _k = 0; _k < sequencestatement_statements_Statement.size(); _k++)
    { if (sequencestatement_statements_Statement.get(_k) instanceof ReturnStatement)
      { out.println("returnstatementx_" + returnstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof BreakStatement)
      { out.println("breakstatementx_" + breakstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof OperationCallStatement)
      { out.println("operationcallstatementx_" + operationcallstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof ImplicitCallStatement)
      { out.println("implicitcallstatementx_" + implicitcallstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof SequenceStatement)
      { out.println("sequencestatementx_" + sequencestatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof ConditionalStatement)
      { out.println("conditionalstatementx_" + conditionalstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof AssignStatement)
      { out.println("assignstatementx_" + assignstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof CreationStatement)
      { out.println("creationstatementx_" + creationstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof CatchStatement)
      { out.println("catchstatementx_" + catchstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof FinalStatement)
      { out.println("finalstatementx_" + finalstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof TryStatement)
      { out.println("trystatementx_" + trystatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof AssertStatement)
      { out.println("assertstatementx_" + assertstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof ErrorStatement)
      { out.println("errorstatementx_" + errorstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof BoundedLoopStatement)
      { out.println("boundedloopstatementx_" + boundedloopstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof UnboundedLoopStatement)
      { out.println("unboundedloopstatementx_" + unboundedloopstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
  }
  }
  for (int _i = 0; _i < conditionalstatements.size(); _i++)
  { ConditionalStatement conditionalstatementx_ = (ConditionalStatement) conditionalstatements.get(_i);
    if (conditionalstatementx_.gettest() instanceof UnaryExpression)
    { out.println("conditionalstatementx_" + _i + ".test = unaryexpressionx_" + unaryexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.gettest() instanceof ConditionalExpression)
    { out.println("conditionalstatementx_" + _i + ".test = conditionalexpressionx_" + conditionalexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.gettest() instanceof BasicExpression)
    { out.println("conditionalstatementx_" + _i + ".test = basicexpressionx_" + basicexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.gettest() instanceof BinaryExpression)
    { out.println("conditionalstatementx_" + _i + ".test = binaryexpressionx_" + binaryexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.gettest() instanceof CollectionExpression)
    { out.println("conditionalstatementx_" + _i + ".test = collectionexpressionx_" + collectionexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.getifPart() instanceof ReturnStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = returnstatementx_" + returnstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof BreakStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = breakstatementx_" + breakstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof OperationCallStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = operationcallstatementx_" + operationcallstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof ImplicitCallStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = implicitcallstatementx_" + implicitcallstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof SequenceStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = sequencestatementx_" + sequencestatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof ConditionalStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = conditionalstatementx_" + conditionalstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof AssignStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = assignstatementx_" + assignstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof CreationStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = creationstatementx_" + creationstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof CatchStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = catchstatementx_" + catchstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof FinalStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = finalstatementx_" + finalstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof TryStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = trystatementx_" + trystatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof AssertStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = assertstatementx_" + assertstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof ErrorStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = errorstatementx_" + errorstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof BoundedLoopStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = boundedloopstatementx_" + boundedloopstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof UnboundedLoopStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = unboundedloopstatementx_" + unboundedloopstatements.indexOf(conditionalstatementx_.getifPart())); } 
    List conditionalstatement_elsePart_Statement = conditionalstatementx_.getelsePart();
    for (int _k = 0; _k < conditionalstatement_elsePart_Statement.size(); _k++)
    { if (conditionalstatement_elsePart_Statement.get(_k) instanceof ReturnStatement)
      { out.println("returnstatementx_" + returnstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof BreakStatement)
      { out.println("breakstatementx_" + breakstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof OperationCallStatement)
      { out.println("operationcallstatementx_" + operationcallstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof ImplicitCallStatement)
      { out.println("implicitcallstatementx_" + implicitcallstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof SequenceStatement)
      { out.println("sequencestatementx_" + sequencestatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof ConditionalStatement)
      { out.println("conditionalstatementx_" + conditionalstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof AssignStatement)
      { out.println("assignstatementx_" + assignstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof CreationStatement)
      { out.println("creationstatementx_" + creationstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof CatchStatement)
      { out.println("catchstatementx_" + catchstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof FinalStatement)
      { out.println("finalstatementx_" + finalstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof TryStatement)
      { out.println("trystatementx_" + trystatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof AssertStatement)
      { out.println("assertstatementx_" + assertstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof ErrorStatement)
      { out.println("errorstatementx_" + errorstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof BoundedLoopStatement)
      { out.println("boundedloopstatementx_" + boundedloopstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof UnboundedLoopStatement)
      { out.println("unboundedloopstatementx_" + unboundedloopstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
  }
  }
  for (int _i = 0; _i < creationstatements.size(); _i++)
  { CreationStatement creationstatementx_ = (CreationStatement) creationstatements.get(_i);
    if (creationstatementx_.gettype() instanceof Entity)
    { out.println("creationstatementx_" + _i + ".type = entityx_" + entitys.indexOf(creationstatementx_.gettype())); } 
    if (creationstatementx_.gettype() instanceof UseCase)
    { out.println("creationstatementx_" + _i + ".type = usecasex_" + usecases.indexOf(creationstatementx_.gettype())); } 
    if (creationstatementx_.gettype() instanceof PrimitiveType)
    { out.println("creationstatementx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(creationstatementx_.gettype())); } 
    if (creationstatementx_.gettype() instanceof Enumeration)
    { out.println("creationstatementx_" + _i + ".type = enumerationx_" + enumerations.indexOf(creationstatementx_.gettype())); } 
    if (creationstatementx_.gettype() instanceof CollectionType)
    { out.println("creationstatementx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(creationstatementx_.gettype())); } 
    if (creationstatementx_.getelementType() instanceof Entity)
    { out.println("creationstatementx_" + _i + ".elementType = entityx_" + entitys.indexOf(creationstatementx_.getelementType())); } 
    if (creationstatementx_.getelementType() instanceof UseCase)
    { out.println("creationstatementx_" + _i + ".elementType = usecasex_" + usecases.indexOf(creationstatementx_.getelementType())); } 
    if (creationstatementx_.getelementType() instanceof PrimitiveType)
    { out.println("creationstatementx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(creationstatementx_.getelementType())); } 
    if (creationstatementx_.getelementType() instanceof Enumeration)
    { out.println("creationstatementx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(creationstatementx_.getelementType())); } 
    if (creationstatementx_.getelementType() instanceof CollectionType)
    { out.println("creationstatementx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(creationstatementx_.getelementType())); } 
    List creationstatement_initialExpression_Expression = creationstatementx_.getinitialExpression();
    for (int _k = 0; _k < creationstatement_initialExpression_Expression.size(); _k++)
    { if (creationstatement_initialExpression_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
 if (creationstatement_initialExpression_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
 if (creationstatement_initialExpression_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
 if (creationstatement_initialExpression_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
 if (creationstatement_initialExpression_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
  }
  }
  for (int _i = 0; _i < errorstatements.size(); _i++)
  { ErrorStatement errorstatementx_ = (ErrorStatement) errorstatements.get(_i);
    if (errorstatementx_.getthrownObject() instanceof UnaryExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = unaryexpressionx_" + unaryexpressions.indexOf(errorstatementx_.getthrownObject())); } 
    if (errorstatementx_.getthrownObject() instanceof ConditionalExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = conditionalexpressionx_" + conditionalexpressions.indexOf(errorstatementx_.getthrownObject())); } 
    if (errorstatementx_.getthrownObject() instanceof BasicExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = basicexpressionx_" + basicexpressions.indexOf(errorstatementx_.getthrownObject())); } 
    if (errorstatementx_.getthrownObject() instanceof BinaryExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = binaryexpressionx_" + binaryexpressions.indexOf(errorstatementx_.getthrownObject())); } 
    if (errorstatementx_.getthrownObject() instanceof CollectionExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = collectionexpressionx_" + collectionexpressions.indexOf(errorstatementx_.getthrownObject())); } 
  }
  for (int _i = 0; _i < catchstatements.size(); _i++)
  { CatchStatement catchstatementx_ = (CatchStatement) catchstatements.get(_i);
    if (catchstatementx_.getcaughtObject() instanceof UnaryExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = unaryexpressionx_" + unaryexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getcaughtObject() instanceof ConditionalExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = conditionalexpressionx_" + conditionalexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getcaughtObject() instanceof BasicExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = basicexpressionx_" + basicexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getcaughtObject() instanceof BinaryExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = binaryexpressionx_" + binaryexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getcaughtObject() instanceof CollectionExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = collectionexpressionx_" + collectionexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getaction() instanceof ReturnStatement)
    { out.println("catchstatementx_" + _i + ".action = returnstatementx_" + returnstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof BreakStatement)
    { out.println("catchstatementx_" + _i + ".action = breakstatementx_" + breakstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof OperationCallStatement)
    { out.println("catchstatementx_" + _i + ".action = operationcallstatementx_" + operationcallstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof ImplicitCallStatement)
    { out.println("catchstatementx_" + _i + ".action = implicitcallstatementx_" + implicitcallstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof SequenceStatement)
    { out.println("catchstatementx_" + _i + ".action = sequencestatementx_" + sequencestatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof ConditionalStatement)
    { out.println("catchstatementx_" + _i + ".action = conditionalstatementx_" + conditionalstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof AssignStatement)
    { out.println("catchstatementx_" + _i + ".action = assignstatementx_" + assignstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof CreationStatement)
    { out.println("catchstatementx_" + _i + ".action = creationstatementx_" + creationstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof CatchStatement)
    { out.println("catchstatementx_" + _i + ".action = catchstatementx_" + catchstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof FinalStatement)
    { out.println("catchstatementx_" + _i + ".action = finalstatementx_" + finalstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof TryStatement)
    { out.println("catchstatementx_" + _i + ".action = trystatementx_" + trystatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof AssertStatement)
    { out.println("catchstatementx_" + _i + ".action = assertstatementx_" + assertstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof ErrorStatement)
    { out.println("catchstatementx_" + _i + ".action = errorstatementx_" + errorstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof BoundedLoopStatement)
    { out.println("catchstatementx_" + _i + ".action = boundedloopstatementx_" + boundedloopstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof UnboundedLoopStatement)
    { out.println("catchstatementx_" + _i + ".action = unboundedloopstatementx_" + unboundedloopstatements.indexOf(catchstatementx_.getaction())); } 
  }
  for (int _i = 0; _i < finalstatements.size(); _i++)
  { FinalStatement finalstatementx_ = (FinalStatement) finalstatements.get(_i);
    if (finalstatementx_.getbody() instanceof ReturnStatement)
    { out.println("finalstatementx_" + _i + ".body = returnstatementx_" + returnstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof BreakStatement)
    { out.println("finalstatementx_" + _i + ".body = breakstatementx_" + breakstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof OperationCallStatement)
    { out.println("finalstatementx_" + _i + ".body = operationcallstatementx_" + operationcallstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof ImplicitCallStatement)
    { out.println("finalstatementx_" + _i + ".body = implicitcallstatementx_" + implicitcallstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof SequenceStatement)
    { out.println("finalstatementx_" + _i + ".body = sequencestatementx_" + sequencestatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof ConditionalStatement)
    { out.println("finalstatementx_" + _i + ".body = conditionalstatementx_" + conditionalstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof AssignStatement)
    { out.println("finalstatementx_" + _i + ".body = assignstatementx_" + assignstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof CreationStatement)
    { out.println("finalstatementx_" + _i + ".body = creationstatementx_" + creationstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof CatchStatement)
    { out.println("finalstatementx_" + _i + ".body = catchstatementx_" + catchstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof FinalStatement)
    { out.println("finalstatementx_" + _i + ".body = finalstatementx_" + finalstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof TryStatement)
    { out.println("finalstatementx_" + _i + ".body = trystatementx_" + trystatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof AssertStatement)
    { out.println("finalstatementx_" + _i + ".body = assertstatementx_" + assertstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof ErrorStatement)
    { out.println("finalstatementx_" + _i + ".body = errorstatementx_" + errorstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof BoundedLoopStatement)
    { out.println("finalstatementx_" + _i + ".body = boundedloopstatementx_" + boundedloopstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof UnboundedLoopStatement)
    { out.println("finalstatementx_" + _i + ".body = unboundedloopstatementx_" + unboundedloopstatements.indexOf(finalstatementx_.getbody())); } 
  }
  for (int _i = 0; _i < trystatements.size(); _i++)
  { TryStatement trystatementx_ = (TryStatement) trystatements.get(_i);
    List trystatement_catchClauses_Statement = trystatementx_.getcatchClauses();
    for (int _k = 0; _k < trystatement_catchClauses_Statement.size(); _k++)
    { if (trystatement_catchClauses_Statement.get(_k) instanceof ReturnStatement)
      { out.println("returnstatementx_" + returnstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof BreakStatement)
      { out.println("breakstatementx_" + breakstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof OperationCallStatement)
      { out.println("operationcallstatementx_" + operationcallstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof ImplicitCallStatement)
      { out.println("implicitcallstatementx_" + implicitcallstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof SequenceStatement)
      { out.println("sequencestatementx_" + sequencestatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof ConditionalStatement)
      { out.println("conditionalstatementx_" + conditionalstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof AssignStatement)
      { out.println("assignstatementx_" + assignstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof CreationStatement)
      { out.println("creationstatementx_" + creationstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof CatchStatement)
      { out.println("catchstatementx_" + catchstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof FinalStatement)
      { out.println("finalstatementx_" + finalstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof TryStatement)
      { out.println("trystatementx_" + trystatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof AssertStatement)
      { out.println("assertstatementx_" + assertstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof ErrorStatement)
      { out.println("errorstatementx_" + errorstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof BoundedLoopStatement)
      { out.println("boundedloopstatementx_" + boundedloopstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof UnboundedLoopStatement)
      { out.println("unboundedloopstatementx_" + unboundedloopstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
  }
    if (trystatementx_.getbody() instanceof ReturnStatement)
    { out.println("trystatementx_" + _i + ".body = returnstatementx_" + returnstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof BreakStatement)
    { out.println("trystatementx_" + _i + ".body = breakstatementx_" + breakstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof OperationCallStatement)
    { out.println("trystatementx_" + _i + ".body = operationcallstatementx_" + operationcallstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof ImplicitCallStatement)
    { out.println("trystatementx_" + _i + ".body = implicitcallstatementx_" + implicitcallstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof SequenceStatement)
    { out.println("trystatementx_" + _i + ".body = sequencestatementx_" + sequencestatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof ConditionalStatement)
    { out.println("trystatementx_" + _i + ".body = conditionalstatementx_" + conditionalstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof AssignStatement)
    { out.println("trystatementx_" + _i + ".body = assignstatementx_" + assignstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof CreationStatement)
    { out.println("trystatementx_" + _i + ".body = creationstatementx_" + creationstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof CatchStatement)
    { out.println("trystatementx_" + _i + ".body = catchstatementx_" + catchstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof FinalStatement)
    { out.println("trystatementx_" + _i + ".body = finalstatementx_" + finalstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof TryStatement)
    { out.println("trystatementx_" + _i + ".body = trystatementx_" + trystatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof AssertStatement)
    { out.println("trystatementx_" + _i + ".body = assertstatementx_" + assertstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof ErrorStatement)
    { out.println("trystatementx_" + _i + ".body = errorstatementx_" + errorstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof BoundedLoopStatement)
    { out.println("trystatementx_" + _i + ".body = boundedloopstatementx_" + boundedloopstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof UnboundedLoopStatement)
    { out.println("trystatementx_" + _i + ".body = unboundedloopstatementx_" + unboundedloopstatements.indexOf(trystatementx_.getbody())); } 
    List trystatement_endStatement_Statement = trystatementx_.getendStatement();
    for (int _k = 0; _k < trystatement_endStatement_Statement.size(); _k++)
    { if (trystatement_endStatement_Statement.get(_k) instanceof ReturnStatement)
      { out.println("returnstatementx_" + returnstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof BreakStatement)
      { out.println("breakstatementx_" + breakstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof OperationCallStatement)
      { out.println("operationcallstatementx_" + operationcallstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof ImplicitCallStatement)
      { out.println("implicitcallstatementx_" + implicitcallstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof SequenceStatement)
      { out.println("sequencestatementx_" + sequencestatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof ConditionalStatement)
      { out.println("conditionalstatementx_" + conditionalstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof AssignStatement)
      { out.println("assignstatementx_" + assignstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof CreationStatement)
      { out.println("creationstatementx_" + creationstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof CatchStatement)
      { out.println("catchstatementx_" + catchstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof FinalStatement)
      { out.println("finalstatementx_" + finalstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof TryStatement)
      { out.println("trystatementx_" + trystatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof AssertStatement)
      { out.println("assertstatementx_" + assertstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof ErrorStatement)
      { out.println("errorstatementx_" + errorstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof BoundedLoopStatement)
      { out.println("boundedloopstatementx_" + boundedloopstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof UnboundedLoopStatement)
      { out.println("unboundedloopstatementx_" + unboundedloopstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
  }
  }
  for (int _i = 0; _i < assertstatements.size(); _i++)
  { AssertStatement assertstatementx_ = (AssertStatement) assertstatements.get(_i);
    if (assertstatementx_.getcondition() instanceof UnaryExpression)
    { out.println("assertstatementx_" + _i + ".condition = unaryexpressionx_" + unaryexpressions.indexOf(assertstatementx_.getcondition())); } 
    if (assertstatementx_.getcondition() instanceof ConditionalExpression)
    { out.println("assertstatementx_" + _i + ".condition = conditionalexpressionx_" + conditionalexpressions.indexOf(assertstatementx_.getcondition())); } 
    if (assertstatementx_.getcondition() instanceof BasicExpression)
    { out.println("assertstatementx_" + _i + ".condition = basicexpressionx_" + basicexpressions.indexOf(assertstatementx_.getcondition())); } 
    if (assertstatementx_.getcondition() instanceof BinaryExpression)
    { out.println("assertstatementx_" + _i + ".condition = binaryexpressionx_" + binaryexpressions.indexOf(assertstatementx_.getcondition())); } 
    if (assertstatementx_.getcondition() instanceof CollectionExpression)
    { out.println("assertstatementx_" + _i + ".condition = collectionexpressionx_" + collectionexpressions.indexOf(assertstatementx_.getcondition())); } 
    List assertstatement_message_Expression = assertstatementx_.getmessage();
    for (int _k = 0; _k < assertstatement_message_Expression.size(); _k++)
    { if (assertstatement_message_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
 if (assertstatement_message_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
 if (assertstatement_message_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
 if (assertstatement_message_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
 if (assertstatement_message_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
  }
  }
    out.close(); 
  }


  public static void loadXSI()
  { boolean __eof = false;
    String __s = "";
    String xmlstring = "";
    BufferedReader __br = null;
    try
    { File _classmodel = new File("in.xmi");
      __br = new BufferedReader(new FileReader(_classmodel));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { xmlstring = xmlstring + __s; }
      } 
      __br.close();
    } 
    catch (Exception _x) { }
    Vector res = convertXsiToVector(xmlstring);
    File outfile = new File("_in.txt");
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
    for (int i = 0; i < res.size(); i++)
    { String r = (String) res.get(i); 
      out.println(r);
    } 
    out.close();
    loadModel("_in.txt");
  }

  public static Vector convertXsiToVector(String xmlstring)
  { Vector res = new Vector();
    XMLParser comp = new XMLParser();
    comp.nospacelexicalanalysisxml(xmlstring);
    XMLNode xml = comp.parseXML();
    if (xml == null) { return res; } 
    java.util.Map instancemap = new java.util.HashMap(); // String --> Vector
    java.util.Map entmap = new java.util.HashMap();       // String --> String
    Vector entcodes = new Vector(); 
    java.util.Map allattsmap = new java.util.HashMap(); // String --> Vector
    java.util.Map stringattsmap = new java.util.HashMap(); // String --> Vector
    java.util.Map onerolesmap = new java.util.HashMap(); // String --> Vector
    java.util.Map actualtype = new java.util.HashMap(); // XMLNode --> String
    Vector eallatts = new Vector();
    instancemap.put("namedelements", new Vector()); 
    instancemap.put("namedelement",new Vector()); 
    entcodes.add("namedelements");
    entcodes.add("namedelement");
    entmap.put("namedelements","NamedElement");
    entmap.put("namedelement","NamedElement");
    eallatts = new Vector();
    eallatts.add("name");
    allattsmap.put("NamedElement", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("NamedElement", eallatts);
    eallatts = new Vector();
    onerolesmap.put("NamedElement", eallatts);
    instancemap.put("relationships", new Vector()); 
    instancemap.put("relationship",new Vector()); 
    entcodes.add("relationships");
    entcodes.add("relationship");
    entmap.put("relationships","Relationship");
    entmap.put("relationship","Relationship");
    eallatts = new Vector();
    eallatts.add("name");
    allattsmap.put("Relationship", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("Relationship", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Relationship", eallatts);
    instancemap.put("features", new Vector()); 
    instancemap.put("feature",new Vector()); 
    entcodes.add("features");
    entcodes.add("feature");
    entmap.put("features","Feature");
    entmap.put("feature","Feature");
    eallatts = new Vector();
    eallatts.add("isStatic");
    eallatts.add("name");
    allattsmap.put("Feature", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("Feature", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("Feature", eallatts);
    instancemap.put("types", new Vector()); 
    instancemap.put("type",new Vector()); 
    entcodes.add("types");
    entcodes.add("type");
    entmap.put("types","Type");
    entmap.put("type","Type");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    allattsmap.put("Type", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("Type", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Type", eallatts);
    instancemap.put("classifiers", new Vector()); 
    instancemap.put("classifier",new Vector()); 
    entcodes.add("classifiers");
    entcodes.add("classifier");
    entmap.put("classifiers","Classifier");
    entmap.put("classifier","Classifier");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    allattsmap.put("Classifier", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("Classifier", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Classifier", eallatts);
    instancemap.put("structuralfeatures", new Vector()); 
    instancemap.put("structuralfeature",new Vector()); 
    entcodes.add("structuralfeatures");
    entcodes.add("structuralfeature");
    entmap.put("structuralfeatures","StructuralFeature");
    entmap.put("structuralfeature","StructuralFeature");
    eallatts = new Vector();
    eallatts.add("isReadOnly");
    eallatts.add("isStatic");
    eallatts.add("name");
    allattsmap.put("StructuralFeature", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("StructuralFeature", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("StructuralFeature", eallatts);
    instancemap.put("datatypes", new Vector()); 
    instancemap.put("datatype",new Vector()); 
    entcodes.add("datatypes");
    entcodes.add("datatype");
    entmap.put("datatypes","DataType");
    entmap.put("datatype","DataType");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    allattsmap.put("DataType", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("DataType", eallatts);
    eallatts = new Vector();
    onerolesmap.put("DataType", eallatts);
    instancemap.put("enumerations", new Vector()); 
    instancemap.put("enumeration",new Vector()); 
    entcodes.add("enumerations");
    entcodes.add("enumeration");
    entmap.put("enumerations","Enumeration");
    entmap.put("enumeration","Enumeration");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    allattsmap.put("Enumeration", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("Enumeration", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Enumeration", eallatts);
    instancemap.put("enumerationliterals", new Vector()); 
    instancemap.put("enumerationliteral",new Vector()); 
    entcodes.add("enumerationliterals");
    entcodes.add("enumerationliteral");
    entmap.put("enumerationliterals","EnumerationLiteral");
    entmap.put("enumerationliteral","EnumerationLiteral");
    eallatts = new Vector();
    eallatts.add("name");
    allattsmap.put("EnumerationLiteral", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("EnumerationLiteral", eallatts);
    eallatts = new Vector();
    onerolesmap.put("EnumerationLiteral", eallatts);
    instancemap.put("primitivetypes", new Vector()); 
    instancemap.put("primitivetype",new Vector()); 
    entcodes.add("primitivetypes");
    entcodes.add("primitivetype");
    entmap.put("primitivetypes","PrimitiveType");
    entmap.put("primitivetype","PrimitiveType");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    allattsmap.put("PrimitiveType", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("PrimitiveType", eallatts);
    eallatts = new Vector();
    onerolesmap.put("PrimitiveType", eallatts);
    instancemap.put("entitys", new Vector()); 
    instancemap.put("entity",new Vector()); 
    entcodes.add("entitys");
    entcodes.add("entity");
    entmap.put("entitys","Entity");
    entmap.put("entity","Entity");
    eallatts = new Vector();
    eallatts.add("isAbstract");
    eallatts.add("isInterface");
    eallatts.add("typeId");
    eallatts.add("name");
    allattsmap.put("Entity", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("Entity", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Entity", eallatts);
    instancemap.put("propertys", new Vector()); 
    instancemap.put("property",new Vector()); 
    entcodes.add("propertys");
    entcodes.add("property");
    entmap.put("propertys","Property");
    entmap.put("property","Property");
    eallatts = new Vector();
    eallatts.add("lower");
    eallatts.add("upper");
    eallatts.add("isOrdered");
    eallatts.add("isUnique");
    eallatts.add("isDerived");
    eallatts.add("isReadOnly");
    eallatts.add("isStatic");
    eallatts.add("name");
    allattsmap.put("Property", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("Property", eallatts);
    eallatts = new Vector();
    eallatts.add("owner");
    eallatts.add("initialValue");
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("Property", eallatts);
    instancemap.put("associations", new Vector()); 
    instancemap.put("association",new Vector()); 
    entcodes.add("associations");
    entcodes.add("association");
    entmap.put("associations","Association");
    entmap.put("association","Association");
    eallatts = new Vector();
    eallatts.add("addOnly");
    eallatts.add("aggregation");
    eallatts.add("name");
    allattsmap.put("Association", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("Association", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Association", eallatts);
    instancemap.put("generalizations", new Vector()); 
    instancemap.put("generalization",new Vector()); 
    entcodes.add("generalizations");
    entcodes.add("generalization");
    entmap.put("generalizations","Generalization");
    entmap.put("generalization","Generalization");
    eallatts = new Vector();
    eallatts.add("name");
    allattsmap.put("Generalization", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("Generalization", eallatts);
    eallatts = new Vector();
    eallatts.add("specific");
    eallatts.add("general");
    onerolesmap.put("Generalization", eallatts);
    instancemap.put("behaviouralfeatures", new Vector()); 
    instancemap.put("behaviouralfeature",new Vector()); 
    entcodes.add("behaviouralfeatures");
    entcodes.add("behaviouralfeature");
    entmap.put("behaviouralfeatures","BehaviouralFeature");
    entmap.put("behaviouralfeature","BehaviouralFeature");
    eallatts = new Vector();
    eallatts.add("isAbstract");
    eallatts.add("isStatic");
    eallatts.add("name");
    allattsmap.put("BehaviouralFeature", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("BehaviouralFeature", eallatts);
    eallatts = new Vector();
    eallatts.add("activity");
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("BehaviouralFeature", eallatts);
    instancemap.put("operations", new Vector()); 
    instancemap.put("operation",new Vector()); 
    entcodes.add("operations");
    entcodes.add("operation");
    entmap.put("operations","Operation");
    entmap.put("operation","Operation");
    eallatts = new Vector();
    eallatts.add("isQuery");
    eallatts.add("isCached");
    eallatts.add("isAbstract");
    eallatts.add("isStatic");
    eallatts.add("name");
    allattsmap.put("Operation", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("Operation", eallatts);
    eallatts = new Vector();
    eallatts.add("owner");
    eallatts.add("activity");
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("Operation", eallatts);
    instancemap.put("expressions", new Vector()); 
    instancemap.put("expression",new Vector()); 
    entcodes.add("expressions");
    entcodes.add("expression");
    entmap.put("expressions","Expression");
    entmap.put("expression","Expression");
    eallatts = new Vector();
    eallatts.add("needsBracket");
    eallatts.add("umlKind");
    eallatts.add("expId");
    eallatts.add("isStatic");
    allattsmap.put("Expression", eallatts);
    eallatts = new Vector();
    eallatts.add("expId");
    stringattsmap.put("Expression", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("Expression", eallatts);
    instancemap.put("usecases", new Vector()); 
    instancemap.put("usecase",new Vector()); 
    entcodes.add("usecases");
    entcodes.add("usecase");
    entmap.put("usecases","UseCase");
    entmap.put("usecase","UseCase");
    eallatts = new Vector();
    eallatts.add("isGeneric");
    eallatts.add("isDerived");
    eallatts.add("typeId");
    eallatts.add("name");
    allattsmap.put("UseCase", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("UseCase", eallatts);
    eallatts = new Vector();
    eallatts.add("classifierBehaviour");
    eallatts.add("resultType");
    onerolesmap.put("UseCase", eallatts);
    instancemap.put("collectiontypes", new Vector()); 
    instancemap.put("collectiontype",new Vector()); 
    entcodes.add("collectiontypes");
    entcodes.add("collectiontype");
    entmap.put("collectiontypes","CollectionType");
    entmap.put("collectiontype","CollectionType");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    allattsmap.put("CollectionType", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("CollectionType", eallatts);
    eallatts = new Vector();
    eallatts.add("elementType");
    eallatts.add("keyType");
    onerolesmap.put("CollectionType", eallatts);
    instancemap.put("cstructs", new Vector()); 
    instancemap.put("cstruct",new Vector()); 
    entcodes.add("cstructs");
    entcodes.add("cstruct");
    entmap.put("cstructs","CStruct");
    entmap.put("cstruct","CStruct");
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("ctypeId");
    allattsmap.put("CStruct", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("ctypeId");
    stringattsmap.put("CStruct", eallatts);
    eallatts = new Vector();
    onerolesmap.put("CStruct", eallatts);
    instancemap.put("cmembers", new Vector()); 
    instancemap.put("cmember",new Vector()); 
    entcodes.add("cmembers");
    entcodes.add("cmember");
    entmap.put("cmembers","CMember");
    entmap.put("cmember","CMember");
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("isKey");
    eallatts.add("multiplicity");
    allattsmap.put("CMember", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("multiplicity");
    stringattsmap.put("CMember", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    onerolesmap.put("CMember", eallatts);
    instancemap.put("ctypes", new Vector()); 
    instancemap.put("ctype",new Vector()); 
    entcodes.add("ctypes");
    entcodes.add("ctype");
    entmap.put("ctypes","CType");
    entmap.put("ctype","CType");
    eallatts = new Vector();
    eallatts.add("ctypeId");
    allattsmap.put("CType", eallatts);
    eallatts = new Vector();
    eallatts.add("ctypeId");
    stringattsmap.put("CType", eallatts);
    eallatts = new Vector();
    onerolesmap.put("CType", eallatts);
    instancemap.put("coperations", new Vector()); 
    instancemap.put("coperation",new Vector()); 
    entcodes.add("coperations");
    entcodes.add("coperation");
    entmap.put("coperations","COperation");
    entmap.put("coperation","COperation");
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("opId");
    eallatts.add("isStatic");
    eallatts.add("scope");
    eallatts.add("isQuery");
    eallatts.add("inheritedFrom");
    eallatts.add("isAbstract");
    eallatts.add("classname");
    allattsmap.put("COperation", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("opId");
    eallatts.add("scope");
    eallatts.add("inheritedFrom");
    eallatts.add("classname");
    stringattsmap.put("COperation", eallatts);
    eallatts = new Vector();
    eallatts.add("returnType");
    eallatts.add("elementType");
    onerolesmap.put("COperation", eallatts);
    instancemap.put("cvariables", new Vector()); 
    instancemap.put("cvariable",new Vector()); 
    entcodes.add("cvariables");
    entcodes.add("cvariable");
    entmap.put("cvariables","CVariable");
    entmap.put("cvariable","CVariable");
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("kind");
    eallatts.add("initialisation");
    allattsmap.put("CVariable", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("kind");
    eallatts.add("initialisation");
    stringattsmap.put("CVariable", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    onerolesmap.put("CVariable", eallatts);
    instancemap.put("cprograms", new Vector()); 
    instancemap.put("cprogram",new Vector()); 
    entcodes.add("cprograms");
    entcodes.add("cprogram");
    entmap.put("cprograms","CProgram");
    entmap.put("cprogram","CProgram");
    eallatts = new Vector();
    allattsmap.put("CProgram", eallatts);
    eallatts = new Vector();
    stringattsmap.put("CProgram", eallatts);
    eallatts = new Vector();
    onerolesmap.put("CProgram", eallatts);
    instancemap.put("cprimitivetypes", new Vector()); 
    instancemap.put("cprimitivetype",new Vector()); 
    entcodes.add("cprimitivetypes");
    entcodes.add("cprimitivetype");
    entmap.put("cprimitivetypes","CPrimitiveType");
    entmap.put("cprimitivetype","CPrimitiveType");
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("ctypeId");
    allattsmap.put("CPrimitiveType", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("ctypeId");
    stringattsmap.put("CPrimitiveType", eallatts);
    eallatts = new Vector();
    onerolesmap.put("CPrimitiveType", eallatts);
    instancemap.put("carraytypes", new Vector()); 
    instancemap.put("carraytype",new Vector()); 
    entcodes.add("carraytypes");
    entcodes.add("carraytype");
    entmap.put("carraytypes","CArrayType");
    entmap.put("carraytype","CArrayType");
    eallatts = new Vector();
    eallatts.add("duplicates");
    eallatts.add("ctypeId");
    allattsmap.put("CArrayType", eallatts);
    eallatts = new Vector();
    eallatts.add("ctypeId");
    stringattsmap.put("CArrayType", eallatts);
    eallatts = new Vector();
    eallatts.add("componentType");
    onerolesmap.put("CArrayType", eallatts);
    instancemap.put("cpointertypes", new Vector()); 
    instancemap.put("cpointertype",new Vector()); 
    entcodes.add("cpointertypes");
    entcodes.add("cpointertype");
    entmap.put("cpointertypes","CPointerType");
    entmap.put("cpointertype","CPointerType");
    eallatts = new Vector();
    eallatts.add("ctypeId");
    allattsmap.put("CPointerType", eallatts);
    eallatts = new Vector();
    eallatts.add("ctypeId");
    stringattsmap.put("CPointerType", eallatts);
    eallatts = new Vector();
    eallatts.add("pointsTo");
    onerolesmap.put("CPointerType", eallatts);
    instancemap.put("cfunctionpointertypes", new Vector()); 
    instancemap.put("cfunctionpointertype",new Vector()); 
    entcodes.add("cfunctionpointertypes");
    entcodes.add("cfunctionpointertype");
    entmap.put("cfunctionpointertypes","CFunctionPointerType");
    entmap.put("cfunctionpointertype","CFunctionPointerType");
    eallatts = new Vector();
    eallatts.add("ctypeId");
    allattsmap.put("CFunctionPointerType", eallatts);
    eallatts = new Vector();
    eallatts.add("ctypeId");
    stringattsmap.put("CFunctionPointerType", eallatts);
    eallatts = new Vector();
    eallatts.add("domainType");
    eallatts.add("rangeType");
    onerolesmap.put("CFunctionPointerType", eallatts);
    instancemap.put("cenumerations", new Vector()); 
    instancemap.put("cenumeration",new Vector()); 
    entcodes.add("cenumerations");
    entcodes.add("cenumeration");
    entmap.put("cenumerations","CEnumeration");
    entmap.put("cenumeration","CEnumeration");
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("ctypeId");
    allattsmap.put("CEnumeration", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("ctypeId");
    stringattsmap.put("CEnumeration", eallatts);
    eallatts = new Vector();
    onerolesmap.put("CEnumeration", eallatts);
    instancemap.put("cenumerationliterals", new Vector()); 
    instancemap.put("cenumerationliteral",new Vector()); 
    entcodes.add("cenumerationliterals");
    entcodes.add("cenumerationliteral");
    entmap.put("cenumerationliterals","CEnumerationLiteral");
    entmap.put("cenumerationliteral","CEnumerationLiteral");
    eallatts = new Vector();
    eallatts.add("name");
    allattsmap.put("CEnumerationLiteral", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("CEnumerationLiteral", eallatts);
    eallatts = new Vector();
    onerolesmap.put("CEnumerationLiteral", eallatts);
    instancemap.put("binaryexpressions", new Vector()); 
    instancemap.put("binaryexpression",new Vector()); 
    entcodes.add("binaryexpressions");
    entcodes.add("binaryexpression");
    entmap.put("binaryexpressions","BinaryExpression");
    entmap.put("binaryexpression","BinaryExpression");
    eallatts = new Vector();
    eallatts.add("operator");
    eallatts.add("variable");
    eallatts.add("needsBracket");
    eallatts.add("umlKind");
    eallatts.add("expId");
    eallatts.add("isStatic");
    allattsmap.put("BinaryExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("operator");
    eallatts.add("variable");
    eallatts.add("expId");
    stringattsmap.put("BinaryExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("left");
    eallatts.add("right");
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("BinaryExpression", eallatts);
    instancemap.put("conditionalexpressions", new Vector()); 
    instancemap.put("conditionalexpression",new Vector()); 
    entcodes.add("conditionalexpressions");
    entcodes.add("conditionalexpression");
    entmap.put("conditionalexpressions","ConditionalExpression");
    entmap.put("conditionalexpression","ConditionalExpression");
    eallatts = new Vector();
    eallatts.add("needsBracket");
    eallatts.add("umlKind");
    eallatts.add("expId");
    eallatts.add("isStatic");
    allattsmap.put("ConditionalExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("expId");
    stringattsmap.put("ConditionalExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("test");
    eallatts.add("ifExp");
    eallatts.add("elseExp");
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("ConditionalExpression", eallatts);
    instancemap.put("unaryexpressions", new Vector()); 
    instancemap.put("unaryexpression",new Vector()); 
    entcodes.add("unaryexpressions");
    entcodes.add("unaryexpression");
    entmap.put("unaryexpressions","UnaryExpression");
    entmap.put("unaryexpression","UnaryExpression");
    eallatts = new Vector();
    eallatts.add("operator");
    eallatts.add("variable");
    eallatts.add("needsBracket");
    eallatts.add("umlKind");
    eallatts.add("expId");
    eallatts.add("isStatic");
    allattsmap.put("UnaryExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("operator");
    eallatts.add("variable");
    eallatts.add("expId");
    stringattsmap.put("UnaryExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("argument");
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("UnaryExpression", eallatts);
    instancemap.put("collectionexpressions", new Vector()); 
    instancemap.put("collectionexpression",new Vector()); 
    entcodes.add("collectionexpressions");
    entcodes.add("collectionexpression");
    entmap.put("collectionexpressions","CollectionExpression");
    entmap.put("collectionexpression","CollectionExpression");
    eallatts = new Vector();
    eallatts.add("isOrdered");
    eallatts.add("needsBracket");
    eallatts.add("umlKind");
    eallatts.add("expId");
    eallatts.add("isStatic");
    allattsmap.put("CollectionExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("expId");
    stringattsmap.put("CollectionExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("CollectionExpression", eallatts);
    instancemap.put("basicexpressions", new Vector()); 
    instancemap.put("basicexpression",new Vector()); 
    entcodes.add("basicexpressions");
    entcodes.add("basicexpression");
    entmap.put("basicexpressions","BasicExpression");
    entmap.put("basicexpression","BasicExpression");
    eallatts = new Vector();
    eallatts.add("data");
    eallatts.add("prestate");
    eallatts.add("needsBracket");
    eallatts.add("umlKind");
    eallatts.add("expId");
    eallatts.add("isStatic");
    allattsmap.put("BasicExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("data");
    eallatts.add("expId");
    stringattsmap.put("BasicExpression", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("BasicExpression", eallatts);
    instancemap.put("printcodes", new Vector()); 
    instancemap.put("printcode",new Vector()); 
    entcodes.add("printcodes");
    entcodes.add("printcode");
    entmap.put("printcodes","Printcode");
    entmap.put("printcode","Printcode");
    eallatts = new Vector();
    allattsmap.put("Printcode", eallatts);
    eallatts = new Vector();
    stringattsmap.put("Printcode", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Printcode", eallatts);
    instancemap.put("types2cs", new Vector()); 
    instancemap.put("types2c",new Vector()); 
    entcodes.add("types2cs");
    entcodes.add("types2c");
    entmap.put("types2cs","Types2C");
    entmap.put("types2c","Types2C");
    eallatts = new Vector();
    allattsmap.put("Types2C", eallatts);
    eallatts = new Vector();
    stringattsmap.put("Types2C", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Types2C", eallatts);
    instancemap.put("program2cs", new Vector()); 
    instancemap.put("program2c",new Vector()); 
    entcodes.add("program2cs");
    entcodes.add("program2c");
    entmap.put("program2cs","Program2C");
    entmap.put("program2c","Program2C");
    eallatts = new Vector();
    allattsmap.put("Program2C", eallatts);
    eallatts = new Vector();
    stringattsmap.put("Program2C", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Program2C", eallatts);
    instancemap.put("statements", new Vector()); 
    instancemap.put("statement",new Vector()); 
    entcodes.add("statements");
    entcodes.add("statement");
    entmap.put("statements","Statement");
    entmap.put("statement","Statement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("Statement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("Statement", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Statement", eallatts);
    instancemap.put("returnstatements", new Vector()); 
    instancemap.put("returnstatement",new Vector()); 
    entcodes.add("returnstatements");
    entcodes.add("returnstatement");
    entmap.put("returnstatements","ReturnStatement");
    entmap.put("returnstatement","ReturnStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("ReturnStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("ReturnStatement", eallatts);
    eallatts = new Vector();
    onerolesmap.put("ReturnStatement", eallatts);
    instancemap.put("breakstatements", new Vector()); 
    instancemap.put("breakstatement",new Vector()); 
    entcodes.add("breakstatements");
    entcodes.add("breakstatement");
    entmap.put("breakstatements","BreakStatement");
    entmap.put("breakstatement","BreakStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("BreakStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("BreakStatement", eallatts);
    eallatts = new Vector();
    onerolesmap.put("BreakStatement", eallatts);
    instancemap.put("operationcallstatements", new Vector()); 
    instancemap.put("operationcallstatement",new Vector()); 
    entcodes.add("operationcallstatements");
    entcodes.add("operationcallstatement");
    entmap.put("operationcallstatements","OperationCallStatement");
    entmap.put("operationcallstatement","OperationCallStatement");
    eallatts = new Vector();
    eallatts.add("assignsTo");
    eallatts.add("statId");
    allattsmap.put("OperationCallStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("assignsTo");
    eallatts.add("statId");
    stringattsmap.put("OperationCallStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("callExp");
    onerolesmap.put("OperationCallStatement", eallatts);
    instancemap.put("implicitcallstatements", new Vector()); 
    instancemap.put("implicitcallstatement",new Vector()); 
    entcodes.add("implicitcallstatements");
    entcodes.add("implicitcallstatement");
    entmap.put("implicitcallstatements","ImplicitCallStatement");
    entmap.put("implicitcallstatement","ImplicitCallStatement");
    eallatts = new Vector();
    eallatts.add("assignsTo");
    eallatts.add("statId");
    allattsmap.put("ImplicitCallStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("assignsTo");
    eallatts.add("statId");
    stringattsmap.put("ImplicitCallStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("callExp");
    onerolesmap.put("ImplicitCallStatement", eallatts);
    instancemap.put("loopstatements", new Vector()); 
    instancemap.put("loopstatement",new Vector()); 
    entcodes.add("loopstatements");
    entcodes.add("loopstatement");
    entmap.put("loopstatements","LoopStatement");
    entmap.put("loopstatement","LoopStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("LoopStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("LoopStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("test");
    eallatts.add("body");
    onerolesmap.put("LoopStatement", eallatts);
    instancemap.put("boundedloopstatements", new Vector()); 
    instancemap.put("boundedloopstatement",new Vector()); 
    entcodes.add("boundedloopstatements");
    entcodes.add("boundedloopstatement");
    entmap.put("boundedloopstatements","BoundedLoopStatement");
    entmap.put("boundedloopstatement","BoundedLoopStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("BoundedLoopStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("BoundedLoopStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("loopRange");
    eallatts.add("loopVar");
    eallatts.add("test");
    eallatts.add("body");
    onerolesmap.put("BoundedLoopStatement", eallatts);
    instancemap.put("unboundedloopstatements", new Vector()); 
    instancemap.put("unboundedloopstatement",new Vector()); 
    entcodes.add("unboundedloopstatements");
    entcodes.add("unboundedloopstatement");
    entmap.put("unboundedloopstatements","UnboundedLoopStatement");
    entmap.put("unboundedloopstatement","UnboundedLoopStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("UnboundedLoopStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("UnboundedLoopStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("test");
    eallatts.add("body");
    onerolesmap.put("UnboundedLoopStatement", eallatts);
    instancemap.put("assignstatements", new Vector()); 
    instancemap.put("assignstatement",new Vector()); 
    entcodes.add("assignstatements");
    entcodes.add("assignstatement");
    entmap.put("assignstatements","AssignStatement");
    entmap.put("assignstatement","AssignStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("AssignStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("AssignStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("left");
    eallatts.add("right");
    onerolesmap.put("AssignStatement", eallatts);
    instancemap.put("sequencestatements", new Vector()); 
    instancemap.put("sequencestatement",new Vector()); 
    entcodes.add("sequencestatements");
    entcodes.add("sequencestatement");
    entmap.put("sequencestatements","SequenceStatement");
    entmap.put("sequencestatement","SequenceStatement");
    eallatts = new Vector();
    eallatts.add("kind");
    eallatts.add("statId");
    allattsmap.put("SequenceStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("SequenceStatement", eallatts);
    eallatts = new Vector();
    onerolesmap.put("SequenceStatement", eallatts);
    instancemap.put("conditionalstatements", new Vector()); 
    instancemap.put("conditionalstatement",new Vector()); 
    entcodes.add("conditionalstatements");
    entcodes.add("conditionalstatement");
    entmap.put("conditionalstatements","ConditionalStatement");
    entmap.put("conditionalstatement","ConditionalStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("ConditionalStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("ConditionalStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("test");
    eallatts.add("ifPart");
    onerolesmap.put("ConditionalStatement", eallatts);
    instancemap.put("creationstatements", new Vector()); 
    instancemap.put("creationstatement",new Vector()); 
    entcodes.add("creationstatements");
    entcodes.add("creationstatement");
    entmap.put("creationstatements","CreationStatement");
    entmap.put("creationstatement","CreationStatement");
    eallatts = new Vector();
    eallatts.add("createsInstanceOf");
    eallatts.add("assignsTo");
    eallatts.add("statId");
    allattsmap.put("CreationStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("createsInstanceOf");
    eallatts.add("assignsTo");
    eallatts.add("statId");
    stringattsmap.put("CreationStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    eallatts.add("elementType");
    onerolesmap.put("CreationStatement", eallatts);
    instancemap.put("errorstatements", new Vector()); 
    instancemap.put("errorstatement",new Vector()); 
    entcodes.add("errorstatements");
    entcodes.add("errorstatement");
    entmap.put("errorstatements","ErrorStatement");
    entmap.put("errorstatement","ErrorStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("ErrorStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("ErrorStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("thrownObject");
    onerolesmap.put("ErrorStatement", eallatts);
    instancemap.put("catchstatements", new Vector()); 
    instancemap.put("catchstatement",new Vector()); 
    entcodes.add("catchstatements");
    entcodes.add("catchstatement");
    entmap.put("catchstatements","CatchStatement");
    entmap.put("catchstatement","CatchStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("CatchStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("CatchStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("caughtObject");
    eallatts.add("action");
    onerolesmap.put("CatchStatement", eallatts);
    instancemap.put("finalstatements", new Vector()); 
    instancemap.put("finalstatement",new Vector()); 
    entcodes.add("finalstatements");
    entcodes.add("finalstatement");
    entmap.put("finalstatements","FinalStatement");
    entmap.put("finalstatement","FinalStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("FinalStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("FinalStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("body");
    onerolesmap.put("FinalStatement", eallatts);
    instancemap.put("trystatements", new Vector()); 
    instancemap.put("trystatement",new Vector()); 
    entcodes.add("trystatements");
    entcodes.add("trystatement");
    entmap.put("trystatements","TryStatement");
    entmap.put("trystatement","TryStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("TryStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("TryStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("body");
    onerolesmap.put("TryStatement", eallatts);
    instancemap.put("assertstatements", new Vector()); 
    instancemap.put("assertstatement",new Vector()); 
    entcodes.add("assertstatements");
    entcodes.add("assertstatement");
    entmap.put("assertstatements","AssertStatement");
    entmap.put("assertstatement","AssertStatement");
    eallatts = new Vector();
    eallatts.add("statId");
    allattsmap.put("AssertStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("statId");
    stringattsmap.put("AssertStatement", eallatts);
    eallatts = new Vector();
    eallatts.add("condition");
    onerolesmap.put("AssertStatement", eallatts);
    eallatts = new Vector();
  Vector enodes = xml.getSubnodes();
  for (int i = 0; i < enodes.size(); i++)
  { XMLNode enode = (XMLNode) enodes.get(i);
    String cname = enode.getTag();
    Vector einstances = (Vector) instancemap.get(cname); 
    if (einstances == null) 
    { einstances = (Vector) instancemap.get(cname + "s"); }
    if (einstances != null) 
    { einstances.add(enode); }
  }
  for (int j = 0; j < entcodes.size(); j++)
  { String ename = (String) entcodes.get(j);
    Vector elems = (Vector) instancemap.get(ename);
    for (int k = 0; k < elems.size(); k++)
    { XMLNode enode = (XMLNode) elems.get(k);
      String tname = enode.getAttributeValue("xsi:type"); 
      if (tname == null) 
      { tname = (String) entmap.get(ename); } 
      else 
      { int colonind = tname.indexOf(":"); 
        if (colonind >= 0)
        { tname = tname.substring(colonind + 1,tname.length()); }
      }
      res.add(ename + k + " : " + tname);
      actualtype.put(enode,tname);
    }   
  }
  for (int j = 0; j < entcodes.size(); j++) 
  { String ename = (String) entcodes.get(j); 
    Vector elems = (Vector) instancemap.get(ename); 
    for (int k = 0; k < elems.size(); k++)
    { XMLNode enode = (XMLNode) elems.get(k);
      String tname = (String) actualtype.get(enode);
      Vector tallatts = (Vector)  allattsmap.get(tname);
      Vector tstringatts = (Vector)  stringattsmap.get(tname);
      Vector toneroles = (Vector)  onerolesmap.get(tname);
      Vector atts = enode.getAttributes();
      for (int p = 0; p < atts.size(); p++) 
      { XMLAttribute patt = (XMLAttribute) atts.get(p); 
        if (patt.getName().equals("xsi:type") || patt.getName().equals("xmi:id")) {} 
        else 
        { patt.getDataDeclarationFromXsi(res,tallatts,tstringatts,toneroles,ename + k, (String) entmap.get(ename)); } 
      }
    } 
  }  
  return res; } 

  public void saveXSI(String file)
  { File outfile = new File(file); 
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.println("<UMLRSDS:model xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\">");
    for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx_ = (Enumeration) enumerations.get(_i);
       out.print("<enumerations xsi:type=\"My:Enumeration\"");
    out.print(" typeId=\"" + enumerationx_.gettypeId() + "\" ");
    out.print(" name=\"" + enumerationx_.getname() + "\" ");
    out.print(" ownedLiteral = \"");
    List enumeration_ownedLiteral = enumerationx_.getownedLiteral();
    for (int _j = 0; _j < enumeration_ownedLiteral.size(); _j++)
    { out.print(" //@enumerationliterals." + enumerationliterals.indexOf(enumeration_ownedLiteral.get(_j)));
    }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < enumerationliterals.size(); _i++)
    { EnumerationLiteral enumerationliteralx_ = (EnumerationLiteral) enumerationliterals.get(_i);
       out.print("<enumerationliterals xsi:type=\"My:EnumerationLiteral\"");
    out.print(" name=\"" + enumerationliteralx_.getname() + "\" ");
    out.println(" />");
  }

    for (int _i = 0; _i < primitivetypes.size(); _i++)
    { PrimitiveType primitivetypex_ = (PrimitiveType) primitivetypes.get(_i);
       out.print("<primitivetypes xsi:type=\"My:PrimitiveType\"");
    out.print(" typeId=\"" + primitivetypex_.gettypeId() + "\" ");
    out.print(" name=\"" + primitivetypex_.getname() + "\" ");
    out.println(" />");
  }

    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx_ = (Entity) entitys.get(_i);
       out.print("<entitys xsi:type=\"My:Entity\"");
    out.print(" isAbstract=\"" + entityx_.getisAbstract() + "\" ");
    out.print(" isInterface=\"" + entityx_.getisInterface() + "\" ");
    out.print(" typeId=\"" + entityx_.gettypeId() + "\" ");
    out.print(" name=\"" + entityx_.getname() + "\" ");
    out.print(" ownedAttribute = \"");
    List entity_ownedAttribute = entityx_.getownedAttribute();
    for (int _j = 0; _j < entity_ownedAttribute.size(); _j++)
    { out.print(" //@propertys." + propertys.indexOf(entity_ownedAttribute.get(_j)));
    }
    out.print("\"");
    out.print(" superclass = \"");
    List entity_superclass = entityx_.getsuperclass();
    for (int _j = 0; _j < entity_superclass.size(); _j++)
    { out.print(" //@entitys." + entitys.indexOf(entity_superclass.get(_j)));
    }
    out.print("\"");
    out.print(" subclasses = \"");
    List entity_subclasses = entityx_.getsubclasses();
    for (int _j = 0; _j < entity_subclasses.size(); _j++)
    { out.print(" //@entitys." + entitys.indexOf(entity_subclasses.get(_j)));
    }
    out.print("\"");
    out.print(" generalization = \"");
    List entity_generalization = entityx_.getgeneralization();
    for (int _j = 0; _j < entity_generalization.size(); _j++)
    { out.print(" //@generalizations." + generalizations.indexOf(entity_generalization.get(_j)));
    }
    out.print("\"");
    out.print(" specialization = \"");
    List entity_specialization = entityx_.getspecialization();
    for (int _j = 0; _j < entity_specialization.size(); _j++)
    { out.print(" //@generalizations." + generalizations.indexOf(entity_specialization.get(_j)));
    }
    out.print("\"");
    out.print(" ownedOperation = \"");
    List entity_ownedOperation = entityx_.getownedOperation();
    for (int _j = 0; _j < entity_ownedOperation.size(); _j++)
    { out.print(" //@operations." + operations.indexOf(entity_ownedOperation.get(_j)));
    }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx_ = (Property) propertys.get(_i);
       out.print("<propertys xsi:type=\"My:Property\"");
    out.print(" lower=\"" + propertyx_.getlower() + "\" ");
    out.print(" upper=\"" + propertyx_.getupper() + "\" ");
    out.print(" isOrdered=\"" + propertyx_.getisOrdered() + "\" ");
    out.print(" isUnique=\"" + propertyx_.getisUnique() + "\" ");
    out.print(" isDerived=\"" + propertyx_.getisDerived() + "\" ");
    out.print(" isReadOnly=\"" + propertyx_.getisReadOnly() + "\" ");
    out.print(" isStatic=\"" + propertyx_.getisStatic() + "\" ");
    out.print(" name=\"" + propertyx_.getname() + "\" ");
    out.print(" owner=\"");
    out.print("//@entitys." + entitys.indexOf(((Property) propertys.get(_i)).getowner()));
    out.print("\"");
    if (propertyx_.getinitialValue() instanceof UnaryExpression)
    {   out.print(" initialValue=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
 else    if (propertyx_.getinitialValue() instanceof ConditionalExpression)
    {   out.print(" initialValue=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
 else    if (propertyx_.getinitialValue() instanceof BasicExpression)
    {   out.print(" initialValue=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
 else    if (propertyx_.getinitialValue() instanceof BinaryExpression)
    {   out.print(" initialValue=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
 else    if (propertyx_.getinitialValue() instanceof CollectionExpression)
    {   out.print(" initialValue=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
    if (propertyx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
 else    if (propertyx_.gettype() instanceof UseCase)
    {   out.print(" type=\"");
    out.print("//@usecases." + usecases.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
 else    if (propertyx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
 else    if (propertyx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
 else    if (propertyx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
    if (propertyx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((Property) propertys.get(_i)).getelementType()));
    out.print("\""); }
 else    if (propertyx_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((Property) propertys.get(_i)).getelementType()));
    out.print("\""); }
 else    if (propertyx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((Property) propertys.get(_i)).getelementType()));
    out.print("\""); }
 else    if (propertyx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((Property) propertys.get(_i)).getelementType()));
    out.print("\""); }
 else    if (propertyx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((Property) propertys.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < associations.size(); _i++)
    { Association associationx_ = (Association) associations.get(_i);
       out.print("<associations xsi:type=\"My:Association\"");
    out.print(" addOnly=\"" + associationx_.getaddOnly() + "\" ");
    out.print(" aggregation=\"" + associationx_.getaggregation() + "\" ");
    out.print(" name=\"" + associationx_.getname() + "\" ");
    out.print(" memberEnd = \"");
    List association_memberEnd = associationx_.getmemberEnd();
    for (int _j = 0; _j < association_memberEnd.size(); _j++)
    { out.print(" //@propertys." + propertys.indexOf(association_memberEnd.get(_j)));
    }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < generalizations.size(); _i++)
    { Generalization generalizationx_ = (Generalization) generalizations.get(_i);
       out.print("<generalizations xsi:type=\"My:Generalization\"");
    out.print(" name=\"" + generalizationx_.getname() + "\" ");
    out.print(" specific=\"");
    out.print("//@entitys." + entitys.indexOf(((Generalization) generalizations.get(_i)).getspecific()));
    out.print("\"");
    out.print(" general=\"");
    out.print("//@entitys." + entitys.indexOf(((Generalization) generalizations.get(_i)).getgeneral()));
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx_ = (Operation) operations.get(_i);
       out.print("<operations xsi:type=\"My:Operation\"");
    out.print(" isQuery=\"" + operationx_.getisQuery() + "\" ");
    out.print(" isCached=\"" + operationx_.getisCached() + "\" ");
    out.print(" isAbstract=\"" + operationx_.getisAbstract() + "\" ");
    out.print(" isStatic=\"" + operationx_.getisStatic() + "\" ");
    out.print(" name=\"" + operationx_.getname() + "\" ");
    out.print(" owner=\"");
    out.print("//@entitys." + entitys.indexOf(((Operation) operations.get(_i)).getowner()));
    out.print("\"");
    out.print(" definers = \"");
    List operation_definers = operationx_.getdefiners();
    for (int _j = 0; _j < operation_definers.size(); _j++)
    { out.print(" //@entitys." + entitys.indexOf(operation_definers.get(_j)));
    }
    out.print("\"");
    out.print(" parameters = \"");
    List operation_parameters = operationx_.getparameters();
    for (int _j = 0; _j < operation_parameters.size(); _j++)
    { out.print(" //@propertys." + propertys.indexOf(operation_parameters.get(_j)));
    }
    out.print("\"");
    if (operationx_.getactivity() instanceof ReturnStatement)
    {   out.print(" activity=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof BreakStatement)
    {   out.print(" activity=\"");
    out.print("//@breakstatements." + breakstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof OperationCallStatement)
    {   out.print(" activity=\"");
    out.print("//@operationcallstatements." + operationcallstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof ImplicitCallStatement)
    {   out.print(" activity=\"");
    out.print("//@implicitcallstatements." + implicitcallstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof SequenceStatement)
    {   out.print(" activity=\"");
    out.print("//@sequencestatements." + sequencestatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof ConditionalStatement)
    {   out.print(" activity=\"");
    out.print("//@conditionalstatements." + conditionalstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof AssignStatement)
    {   out.print(" activity=\"");
    out.print("//@assignstatements." + assignstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof CreationStatement)
    {   out.print(" activity=\"");
    out.print("//@creationstatements." + creationstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof CatchStatement)
    {   out.print(" activity=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof FinalStatement)
    {   out.print(" activity=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof TryStatement)
    {   out.print(" activity=\"");
    out.print("//@trystatements." + trystatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof AssertStatement)
    {   out.print(" activity=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof ErrorStatement)
    {   out.print(" activity=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof BoundedLoopStatement)
    {   out.print(" activity=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof UnboundedLoopStatement)
    {   out.print(" activity=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
    if (operationx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((Operation) operations.get(_i)).gettype()));
    out.print("\""); }
 else    if (operationx_.gettype() instanceof UseCase)
    {   out.print(" type=\"");
    out.print("//@usecases." + usecases.indexOf(((Operation) operations.get(_i)).gettype()));
    out.print("\""); }
 else    if (operationx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((Operation) operations.get(_i)).gettype()));
    out.print("\""); }
 else    if (operationx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((Operation) operations.get(_i)).gettype()));
    out.print("\""); }
 else    if (operationx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((Operation) operations.get(_i)).gettype()));
    out.print("\""); }
    if (operationx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((Operation) operations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (operationx_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((Operation) operations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (operationx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((Operation) operations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (operationx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((Operation) operations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (operationx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((Operation) operations.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex_ = (UseCase) usecases.get(_i);
       out.print("<usecases xsi:type=\"My:UseCase\"");
    out.print(" isGeneric=\"" + usecasex_.getisGeneric() + "\" ");
    out.print(" isDerived=\"" + usecasex_.getisDerived() + "\" ");
    out.print(" typeId=\"" + usecasex_.gettypeId() + "\" ");
    out.print(" name=\"" + usecasex_.getname() + "\" ");
    out.print(" parameters = \"");
    List usecase_parameters = usecasex_.getparameters();
    for (int _j = 0; _j < usecase_parameters.size(); _j++)
    { out.print(" //@propertys." + propertys.indexOf(usecase_parameters.get(_j)));
    }
    out.print("\"");
    if (usecasex_.getclassifierBehaviour() instanceof ReturnStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof BreakStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@breakstatements." + breakstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof OperationCallStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@operationcallstatements." + operationcallstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof ImplicitCallStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@implicitcallstatements." + implicitcallstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof SequenceStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@sequencestatements." + sequencestatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof ConditionalStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@conditionalstatements." + conditionalstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof AssignStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@assignstatements." + assignstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof CreationStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@creationstatements." + creationstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof CatchStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof FinalStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof TryStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@trystatements." + trystatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof AssertStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof ErrorStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof BoundedLoopStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof UnboundedLoopStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
    if (usecasex_.getresultType() instanceof Entity)
    {   out.print(" resultType=\"");
    out.print("//@entitys." + entitys.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
 else    if (usecasex_.getresultType() instanceof UseCase)
    {   out.print(" resultType=\"");
    out.print("//@usecases." + usecases.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
 else    if (usecasex_.getresultType() instanceof PrimitiveType)
    {   out.print(" resultType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
 else    if (usecasex_.getresultType() instanceof Enumeration)
    {   out.print(" resultType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
 else    if (usecasex_.getresultType() instanceof CollectionType)
    {   out.print(" resultType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < collectiontypes.size(); _i++)
    { CollectionType collectiontypex_ = (CollectionType) collectiontypes.get(_i);
       out.print("<collectiontypes xsi:type=\"My:CollectionType\"");
    out.print(" typeId=\"" + collectiontypex_.gettypeId() + "\" ");
    out.print(" name=\"" + collectiontypex_.getname() + "\" ");
    if (collectiontypex_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectiontypex_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectiontypex_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectiontypex_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectiontypex_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
    if (collectiontypex_.getkeyType() instanceof Entity)
    {   out.print(" keyType=\"");
    out.print("//@entitys." + entitys.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
 else    if (collectiontypex_.getkeyType() instanceof UseCase)
    {   out.print(" keyType=\"");
    out.print("//@usecases." + usecases.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
 else    if (collectiontypex_.getkeyType() instanceof PrimitiveType)
    {   out.print(" keyType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
 else    if (collectiontypex_.getkeyType() instanceof Enumeration)
    {   out.print(" keyType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
 else    if (collectiontypex_.getkeyType() instanceof CollectionType)
    {   out.print(" keyType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < cstructs.size(); _i++)
    { CStruct cstructx_ = (CStruct) cstructs.get(_i);
       out.print("<cstructs xsi:type=\"My:CStruct\"");
    out.print(" name=\"" + cstructx_.getname() + "\" ");
    out.print(" ctypeId=\"" + cstructx_.getctypeId() + "\" ");
    out.print(" interfaces = \"");
    List cstruct_interfaces = cstructx_.getinterfaces();
    for (int _j = 0; _j < cstruct_interfaces.size(); _j++)
    { out.print(" //@cstructs." + cstructs.indexOf(cstruct_interfaces.get(_j)));
    }
    out.print("\"");
    out.print(" members = \"");
    List cstruct_members = cstructx_.getmembers();
    for (int _j = 0; _j < cstruct_members.size(); _j++)
    { out.print(" //@cmembers." + cmembers.indexOf(cstruct_members.get(_j)));
    }
    out.print("\"");
    out.print(" allMembers = \"");
    List cstruct_allMembers = cstructx_.getallMembers();
    for (int _j = 0; _j < cstruct_allMembers.size(); _j++)
    { out.print(" //@cmembers." + cmembers.indexOf(cstruct_allMembers.get(_j)));
    }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < cmembers.size(); _i++)
    { CMember cmemberx_ = (CMember) cmembers.get(_i);
       out.print("<cmembers xsi:type=\"My:CMember\"");
    out.print(" name=\"" + cmemberx_.getname() + "\" ");
    out.print(" isKey=\"" + cmemberx_.getisKey() + "\" ");
    out.print(" multiplicity=\"" + cmemberx_.getmultiplicity() + "\" ");
    if (cmemberx_.gettype() instanceof CStruct)
    {   out.print(" type=\"");
    out.print("//@cstructs." + cstructs.indexOf(((CMember) cmembers.get(_i)).gettype()));
    out.print("\""); }
 else    if (cmemberx_.gettype() instanceof CPrimitiveType)
    {   out.print(" type=\"");
    out.print("//@cprimitivetypes." + cprimitivetypes.indexOf(((CMember) cmembers.get(_i)).gettype()));
    out.print("\""); }
 else    if (cmemberx_.gettype() instanceof CPointerType)
    {   out.print(" type=\"");
    out.print("//@cpointertypes." + cpointertypes.indexOf(((CMember) cmembers.get(_i)).gettype()));
    out.print("\""); }
 else    if (cmemberx_.gettype() instanceof CFunctionPointerType)
    {   out.print(" type=\"");
    out.print("//@cfunctionpointertypes." + cfunctionpointertypes.indexOf(((CMember) cmembers.get(_i)).gettype()));
    out.print("\""); }
 else    if (cmemberx_.gettype() instanceof CArrayType)
    {   out.print(" type=\"");
    out.print("//@carraytypes." + carraytypes.indexOf(((CMember) cmembers.get(_i)).gettype()));
    out.print("\""); }
 else    if (cmemberx_.gettype() instanceof CEnumeration)
    {   out.print(" type=\"");
    out.print("//@cenumerations." + cenumerations.indexOf(((CMember) cmembers.get(_i)).gettype()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < coperations.size(); _i++)
    { COperation coperationx_ = (COperation) coperations.get(_i);
       out.print("<coperations xsi:type=\"My:COperation\"");
    out.print(" name=\"" + coperationx_.getname() + "\" ");
    out.print(" opId=\"" + coperationx_.getopId() + "\" ");
    out.print(" isStatic=\"" + coperationx_.getisStatic() + "\" ");
    out.print(" scope=\"" + coperationx_.getscope() + "\" ");
    out.print(" isQuery=\"" + coperationx_.getisQuery() + "\" ");
    out.print(" inheritedFrom=\"" + coperationx_.getinheritedFrom() + "\" ");
    out.print(" isAbstract=\"" + coperationx_.getisAbstract() + "\" ");
    out.print(" classname=\"" + coperationx_.getclassname() + "\" ");
    out.print(" parameters = \"");
    List coperation_parameters = coperationx_.getparameters();
    for (int _j = 0; _j < coperation_parameters.size(); _j++)
    { out.print(" //@cvariables." + cvariables.indexOf(coperation_parameters.get(_j)));
    }
    out.print("\"");
    out.print(" definers = \"");
    List coperation_definers = coperationx_.getdefiners();
    for (int _j = 0; _j < coperation_definers.size(); _j++)
    { out.print(" //@cstructs." + cstructs.indexOf(coperation_definers.get(_j)));
    }
    out.print("\"");
    if (coperationx_.getreturnType() instanceof CStruct)
    {   out.print(" returnType=\"");
    out.print("//@cstructs." + cstructs.indexOf(((COperation) coperations.get(_i)).getreturnType()));
    out.print("\""); }
 else    if (coperationx_.getreturnType() instanceof CPrimitiveType)
    {   out.print(" returnType=\"");
    out.print("//@cprimitivetypes." + cprimitivetypes.indexOf(((COperation) coperations.get(_i)).getreturnType()));
    out.print("\""); }
 else    if (coperationx_.getreturnType() instanceof CPointerType)
    {   out.print(" returnType=\"");
    out.print("//@cpointertypes." + cpointertypes.indexOf(((COperation) coperations.get(_i)).getreturnType()));
    out.print("\""); }
 else    if (coperationx_.getreturnType() instanceof CFunctionPointerType)
    {   out.print(" returnType=\"");
    out.print("//@cfunctionpointertypes." + cfunctionpointertypes.indexOf(((COperation) coperations.get(_i)).getreturnType()));
    out.print("\""); }
 else    if (coperationx_.getreturnType() instanceof CArrayType)
    {   out.print(" returnType=\"");
    out.print("//@carraytypes." + carraytypes.indexOf(((COperation) coperations.get(_i)).getreturnType()));
    out.print("\""); }
 else    if (coperationx_.getreturnType() instanceof CEnumeration)
    {   out.print(" returnType=\"");
    out.print("//@cenumerations." + cenumerations.indexOf(((COperation) coperations.get(_i)).getreturnType()));
    out.print("\""); }
    if (coperationx_.getelementType() instanceof CStruct)
    {   out.print(" elementType=\"");
    out.print("//@cstructs." + cstructs.indexOf(((COperation) coperations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (coperationx_.getelementType() instanceof CPrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@cprimitivetypes." + cprimitivetypes.indexOf(((COperation) coperations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (coperationx_.getelementType() instanceof CPointerType)
    {   out.print(" elementType=\"");
    out.print("//@cpointertypes." + cpointertypes.indexOf(((COperation) coperations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (coperationx_.getelementType() instanceof CFunctionPointerType)
    {   out.print(" elementType=\"");
    out.print("//@cfunctionpointertypes." + cfunctionpointertypes.indexOf(((COperation) coperations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (coperationx_.getelementType() instanceof CArrayType)
    {   out.print(" elementType=\"");
    out.print("//@carraytypes." + carraytypes.indexOf(((COperation) coperations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (coperationx_.getelementType() instanceof CEnumeration)
    {   out.print(" elementType=\"");
    out.print("//@cenumerations." + cenumerations.indexOf(((COperation) coperations.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < cvariables.size(); _i++)
    { CVariable cvariablex_ = (CVariable) cvariables.get(_i);
       out.print("<cvariables xsi:type=\"My:CVariable\"");
    out.print(" name=\"" + cvariablex_.getname() + "\" ");
    out.print(" kind=\"" + cvariablex_.getkind() + "\" ");
    out.print(" initialisation=\"" + cvariablex_.getinitialisation() + "\" ");
    if (cvariablex_.gettype() instanceof CStruct)
    {   out.print(" type=\"");
    out.print("//@cstructs." + cstructs.indexOf(((CVariable) cvariables.get(_i)).gettype()));
    out.print("\""); }
 else    if (cvariablex_.gettype() instanceof CPrimitiveType)
    {   out.print(" type=\"");
    out.print("//@cprimitivetypes." + cprimitivetypes.indexOf(((CVariable) cvariables.get(_i)).gettype()));
    out.print("\""); }
 else    if (cvariablex_.gettype() instanceof CPointerType)
    {   out.print(" type=\"");
    out.print("//@cpointertypes." + cpointertypes.indexOf(((CVariable) cvariables.get(_i)).gettype()));
    out.print("\""); }
 else    if (cvariablex_.gettype() instanceof CFunctionPointerType)
    {   out.print(" type=\"");
    out.print("//@cfunctionpointertypes." + cfunctionpointertypes.indexOf(((CVariable) cvariables.get(_i)).gettype()));
    out.print("\""); }
 else    if (cvariablex_.gettype() instanceof CArrayType)
    {   out.print(" type=\"");
    out.print("//@carraytypes." + carraytypes.indexOf(((CVariable) cvariables.get(_i)).gettype()));
    out.print("\""); }
 else    if (cvariablex_.gettype() instanceof CEnumeration)
    {   out.print(" type=\"");
    out.print("//@cenumerations." + cenumerations.indexOf(((CVariable) cvariables.get(_i)).gettype()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < cprograms.size(); _i++)
    { CProgram cprogramx_ = (CProgram) cprograms.get(_i);
       out.print("<cprograms xsi:type=\"My:CProgram\"");
    out.print(" operations = \"");
    List cprogram_operations = cprogramx_.getoperations();
    for (int _j = 0; _j < cprogram_operations.size(); _j++)
    { out.print(" //@coperations." + coperations.indexOf(cprogram_operations.get(_j)));
    }
    out.print("\"");
    out.print(" variables = \"");
    List cprogram_variables = cprogramx_.getvariables();
    for (int _j = 0; _j < cprogram_variables.size(); _j++)
    { out.print(" //@cvariables." + cvariables.indexOf(cprogram_variables.get(_j)));
    }
    out.print("\"");
    out.print(" structs = \"");
    List cprogram_structs = cprogramx_.getstructs();
    for (int _j = 0; _j < cprogram_structs.size(); _j++)
    { out.print(" //@cstructs." + cstructs.indexOf(cprogram_structs.get(_j)));
    }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < cprimitivetypes.size(); _i++)
    { CPrimitiveType cprimitivetypex_ = (CPrimitiveType) cprimitivetypes.get(_i);
       out.print("<cprimitivetypes xsi:type=\"My:CPrimitiveType\"");
    out.print(" name=\"" + cprimitivetypex_.getname() + "\" ");
    out.print(" ctypeId=\"" + cprimitivetypex_.getctypeId() + "\" ");
    out.println(" />");
  }

    for (int _i = 0; _i < carraytypes.size(); _i++)
    { CArrayType carraytypex_ = (CArrayType) carraytypes.get(_i);
       out.print("<carraytypes xsi:type=\"My:CArrayType\"");
    out.print(" duplicates=\"" + carraytypex_.getduplicates() + "\" ");
    out.print(" ctypeId=\"" + carraytypex_.getctypeId() + "\" ");
    if (carraytypex_.getcomponentType() instanceof CStruct)
    {   out.print(" componentType=\"");
    out.print("//@cstructs." + cstructs.indexOf(((CArrayType) carraytypes.get(_i)).getcomponentType()));
    out.print("\""); }
 else    if (carraytypex_.getcomponentType() instanceof CPrimitiveType)
    {   out.print(" componentType=\"");
    out.print("//@cprimitivetypes." + cprimitivetypes.indexOf(((CArrayType) carraytypes.get(_i)).getcomponentType()));
    out.print("\""); }
 else    if (carraytypex_.getcomponentType() instanceof CPointerType)
    {   out.print(" componentType=\"");
    out.print("//@cpointertypes." + cpointertypes.indexOf(((CArrayType) carraytypes.get(_i)).getcomponentType()));
    out.print("\""); }
 else    if (carraytypex_.getcomponentType() instanceof CFunctionPointerType)
    {   out.print(" componentType=\"");
    out.print("//@cfunctionpointertypes." + cfunctionpointertypes.indexOf(((CArrayType) carraytypes.get(_i)).getcomponentType()));
    out.print("\""); }
 else    if (carraytypex_.getcomponentType() instanceof CArrayType)
    {   out.print(" componentType=\"");
    out.print("//@carraytypes." + carraytypes.indexOf(((CArrayType) carraytypes.get(_i)).getcomponentType()));
    out.print("\""); }
 else    if (carraytypex_.getcomponentType() instanceof CEnumeration)
    {   out.print(" componentType=\"");
    out.print("//@cenumerations." + cenumerations.indexOf(((CArrayType) carraytypes.get(_i)).getcomponentType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < cpointertypes.size(); _i++)
    { CPointerType cpointertypex_ = (CPointerType) cpointertypes.get(_i);
       out.print("<cpointertypes xsi:type=\"My:CPointerType\"");
    out.print(" ctypeId=\"" + cpointertypex_.getctypeId() + "\" ");
    if (cpointertypex_.getpointsTo() instanceof CStruct)
    {   out.print(" pointsTo=\"");
    out.print("//@cstructs." + cstructs.indexOf(((CPointerType) cpointertypes.get(_i)).getpointsTo()));
    out.print("\""); }
 else    if (cpointertypex_.getpointsTo() instanceof CPrimitiveType)
    {   out.print(" pointsTo=\"");
    out.print("//@cprimitivetypes." + cprimitivetypes.indexOf(((CPointerType) cpointertypes.get(_i)).getpointsTo()));
    out.print("\""); }
 else    if (cpointertypex_.getpointsTo() instanceof CPointerType)
    {   out.print(" pointsTo=\"");
    out.print("//@cpointertypes." + cpointertypes.indexOf(((CPointerType) cpointertypes.get(_i)).getpointsTo()));
    out.print("\""); }
 else    if (cpointertypex_.getpointsTo() instanceof CFunctionPointerType)
    {   out.print(" pointsTo=\"");
    out.print("//@cfunctionpointertypes." + cfunctionpointertypes.indexOf(((CPointerType) cpointertypes.get(_i)).getpointsTo()));
    out.print("\""); }
 else    if (cpointertypex_.getpointsTo() instanceof CArrayType)
    {   out.print(" pointsTo=\"");
    out.print("//@carraytypes." + carraytypes.indexOf(((CPointerType) cpointertypes.get(_i)).getpointsTo()));
    out.print("\""); }
 else    if (cpointertypex_.getpointsTo() instanceof CEnumeration)
    {   out.print(" pointsTo=\"");
    out.print("//@cenumerations." + cenumerations.indexOf(((CPointerType) cpointertypes.get(_i)).getpointsTo()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < cfunctionpointertypes.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex_ = (CFunctionPointerType) cfunctionpointertypes.get(_i);
       out.print("<cfunctionpointertypes xsi:type=\"My:CFunctionPointerType\"");
    out.print(" ctypeId=\"" + cfunctionpointertypex_.getctypeId() + "\" ");
    if (cfunctionpointertypex_.getdomainType() instanceof CStruct)
    {   out.print(" domainType=\"");
    out.print("//@cstructs." + cstructs.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getdomainType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getdomainType() instanceof CPrimitiveType)
    {   out.print(" domainType=\"");
    out.print("//@cprimitivetypes." + cprimitivetypes.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getdomainType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getdomainType() instanceof CPointerType)
    {   out.print(" domainType=\"");
    out.print("//@cpointertypes." + cpointertypes.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getdomainType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getdomainType() instanceof CFunctionPointerType)
    {   out.print(" domainType=\"");
    out.print("//@cfunctionpointertypes." + cfunctionpointertypes.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getdomainType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getdomainType() instanceof CArrayType)
    {   out.print(" domainType=\"");
    out.print("//@carraytypes." + carraytypes.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getdomainType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getdomainType() instanceof CEnumeration)
    {   out.print(" domainType=\"");
    out.print("//@cenumerations." + cenumerations.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getdomainType()));
    out.print("\""); }
    if (cfunctionpointertypex_.getrangeType() instanceof CStruct)
    {   out.print(" rangeType=\"");
    out.print("//@cstructs." + cstructs.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getrangeType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getrangeType() instanceof CPrimitiveType)
    {   out.print(" rangeType=\"");
    out.print("//@cprimitivetypes." + cprimitivetypes.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getrangeType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getrangeType() instanceof CPointerType)
    {   out.print(" rangeType=\"");
    out.print("//@cpointertypes." + cpointertypes.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getrangeType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getrangeType() instanceof CFunctionPointerType)
    {   out.print(" rangeType=\"");
    out.print("//@cfunctionpointertypes." + cfunctionpointertypes.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getrangeType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getrangeType() instanceof CArrayType)
    {   out.print(" rangeType=\"");
    out.print("//@carraytypes." + carraytypes.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getrangeType()));
    out.print("\""); }
 else    if (cfunctionpointertypex_.getrangeType() instanceof CEnumeration)
    {   out.print(" rangeType=\"");
    out.print("//@cenumerations." + cenumerations.indexOf(((CFunctionPointerType) cfunctionpointertypes.get(_i)).getrangeType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < cenumerations.size(); _i++)
    { CEnumeration cenumerationx_ = (CEnumeration) cenumerations.get(_i);
       out.print("<cenumerations xsi:type=\"My:CEnumeration\"");
    out.print(" name=\"" + cenumerationx_.getname() + "\" ");
    out.print(" ctypeId=\"" + cenumerationx_.getctypeId() + "\" ");
    out.print(" ownedLiteral = \"");
    List cenumeration_ownedLiteral = cenumerationx_.getownedLiteral();
    for (int _j = 0; _j < cenumeration_ownedLiteral.size(); _j++)
    { out.print(" //@cenumerationliterals." + cenumerationliterals.indexOf(cenumeration_ownedLiteral.get(_j)));
    }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < cenumerationliterals.size(); _i++)
    { CEnumerationLiteral cenumerationliteralx_ = (CEnumerationLiteral) cenumerationliterals.get(_i);
       out.print("<cenumerationliterals xsi:type=\"My:CEnumerationLiteral\"");
    out.print(" name=\"" + cenumerationliteralx_.getname() + "\" ");
    out.println(" />");
  }

    for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx_ = (BinaryExpression) binaryexpressions.get(_i);
       out.print("<binaryexpressions xsi:type=\"My:BinaryExpression\"");
    out.print(" operator=\"" + binaryexpressionx_.getoperator() + "\" ");
    out.print(" variable=\"" + binaryexpressionx_.getvariable() + "\" ");
    out.print(" needsBracket=\"" + binaryexpressionx_.getneedsBracket() + "\" ");
    out.print(" umlKind=\"" + binaryexpressionx_.getumlKind() + "\" ");
    out.print(" expId=\"" + binaryexpressionx_.getexpId() + "\" ");
    out.print(" isStatic=\"" + binaryexpressionx_.getisStatic() + "\" ");
    if (binaryexpressionx_.getleft() instanceof UnaryExpression)
    {   out.print(" left=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
 else    if (binaryexpressionx_.getleft() instanceof ConditionalExpression)
    {   out.print(" left=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
 else    if (binaryexpressionx_.getleft() instanceof BasicExpression)
    {   out.print(" left=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
 else    if (binaryexpressionx_.getleft() instanceof BinaryExpression)
    {   out.print(" left=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
 else    if (binaryexpressionx_.getleft() instanceof CollectionExpression)
    {   out.print(" left=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
    if (binaryexpressionx_.getright() instanceof UnaryExpression)
    {   out.print(" right=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
 else    if (binaryexpressionx_.getright() instanceof ConditionalExpression)
    {   out.print(" right=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
 else    if (binaryexpressionx_.getright() instanceof BasicExpression)
    {   out.print(" right=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
 else    if (binaryexpressionx_.getright() instanceof BinaryExpression)
    {   out.print(" right=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
 else    if (binaryexpressionx_.getright() instanceof CollectionExpression)
    {   out.print(" right=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
    if (binaryexpressionx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (binaryexpressionx_.gettype() instanceof UseCase)
    {   out.print(" type=\"");
    out.print("//@usecases." + usecases.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (binaryexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (binaryexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (binaryexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (binaryexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (binaryexpressionx_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (binaryexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (binaryexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (binaryexpressionx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < conditionalexpressions.size(); _i++)
    { ConditionalExpression conditionalexpressionx_ = (ConditionalExpression) conditionalexpressions.get(_i);
       out.print("<conditionalexpressions xsi:type=\"My:ConditionalExpression\"");
    out.print(" needsBracket=\"" + conditionalexpressionx_.getneedsBracket() + "\" ");
    out.print(" umlKind=\"" + conditionalexpressionx_.getumlKind() + "\" ");
    out.print(" expId=\"" + conditionalexpressionx_.getexpId() + "\" ");
    out.print(" isStatic=\"" + conditionalexpressionx_.getisStatic() + "\" ");
    if (conditionalexpressionx_.gettest() instanceof UnaryExpression)
    {   out.print(" test=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettest() instanceof ConditionalExpression)
    {   out.print(" test=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettest() instanceof BasicExpression)
    {   out.print(" test=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettest() instanceof BinaryExpression)
    {   out.print(" test=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettest() instanceof CollectionExpression)
    {   out.print(" test=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
    if (conditionalexpressionx_.getifExp() instanceof UnaryExpression)
    {   out.print(" ifExp=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getifExp() instanceof ConditionalExpression)
    {   out.print(" ifExp=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getifExp() instanceof BasicExpression)
    {   out.print(" ifExp=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getifExp() instanceof BinaryExpression)
    {   out.print(" ifExp=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getifExp() instanceof CollectionExpression)
    {   out.print(" ifExp=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
    if (conditionalexpressionx_.getelseExp() instanceof UnaryExpression)
    {   out.print(" elseExp=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelseExp() instanceof ConditionalExpression)
    {   out.print(" elseExp=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelseExp() instanceof BasicExpression)
    {   out.print(" elseExp=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelseExp() instanceof BinaryExpression)
    {   out.print(" elseExp=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelseExp() instanceof CollectionExpression)
    {   out.print(" elseExp=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
    if (conditionalexpressionx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettype() instanceof UseCase)
    {   out.print(" type=\"");
    out.print("//@usecases." + usecases.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (conditionalexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < unaryexpressions.size(); _i++)
    { UnaryExpression unaryexpressionx_ = (UnaryExpression) unaryexpressions.get(_i);
       out.print("<unaryexpressions xsi:type=\"My:UnaryExpression\"");
    out.print(" operator=\"" + unaryexpressionx_.getoperator() + "\" ");
    out.print(" variable=\"" + unaryexpressionx_.getvariable() + "\" ");
    out.print(" needsBracket=\"" + unaryexpressionx_.getneedsBracket() + "\" ");
    out.print(" umlKind=\"" + unaryexpressionx_.getumlKind() + "\" ");
    out.print(" expId=\"" + unaryexpressionx_.getexpId() + "\" ");
    out.print(" isStatic=\"" + unaryexpressionx_.getisStatic() + "\" ");
    if (unaryexpressionx_.getargument() instanceof UnaryExpression)
    {   out.print(" argument=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
 else    if (unaryexpressionx_.getargument() instanceof ConditionalExpression)
    {   out.print(" argument=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
 else    if (unaryexpressionx_.getargument() instanceof BasicExpression)
    {   out.print(" argument=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
 else    if (unaryexpressionx_.getargument() instanceof BinaryExpression)
    {   out.print(" argument=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
 else    if (unaryexpressionx_.getargument() instanceof CollectionExpression)
    {   out.print(" argument=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
    if (unaryexpressionx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (unaryexpressionx_.gettype() instanceof UseCase)
    {   out.print(" type=\"");
    out.print("//@usecases." + usecases.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (unaryexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (unaryexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (unaryexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (unaryexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (unaryexpressionx_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (unaryexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (unaryexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (unaryexpressionx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < collectionexpressions.size(); _i++)
    { CollectionExpression collectionexpressionx_ = (CollectionExpression) collectionexpressions.get(_i);
       out.print("<collectionexpressions xsi:type=\"My:CollectionExpression\"");
    out.print(" isOrdered=\"" + collectionexpressionx_.getisOrdered() + "\" ");
    out.print(" needsBracket=\"" + collectionexpressionx_.getneedsBracket() + "\" ");
    out.print(" umlKind=\"" + collectionexpressionx_.getumlKind() + "\" ");
    out.print(" expId=\"" + collectionexpressionx_.getexpId() + "\" ");
    out.print(" isStatic=\"" + collectionexpressionx_.getisStatic() + "\" ");
    out.print(" elements = \"");
    List collectionexpression_elements = collectionexpressionx_.getelements();
    for (int _k = 0; _k < collectionexpression_elements.size(); _k++)
    {      if (collectionexpression_elements.get(_k) instanceof UnaryExpression)
      { out.print(" //@unaryexpressions." + unaryexpressions.indexOf(collectionexpression_elements.get(_k)));
    }
 else      if (collectionexpression_elements.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(collectionexpression_elements.get(_k)));
    }
 else      if (collectionexpression_elements.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(collectionexpression_elements.get(_k)));
    }
 else      if (collectionexpression_elements.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(collectionexpression_elements.get(_k)));
    }
 else      if (collectionexpression_elements.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(collectionexpression_elements.get(_k)));
    }
  }
    out.print("\"");
    if (collectionexpressionx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((CollectionExpression) collectionexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (collectionexpressionx_.gettype() instanceof UseCase)
    {   out.print(" type=\"");
    out.print("//@usecases." + usecases.indexOf(((CollectionExpression) collectionexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (collectionexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CollectionExpression) collectionexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (collectionexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CollectionExpression) collectionexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (collectionexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CollectionExpression) collectionexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (collectionexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((CollectionExpression) collectionexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectionexpressionx_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((CollectionExpression) collectionexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectionexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CollectionExpression) collectionexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectionexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CollectionExpression) collectionexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectionexpressionx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CollectionExpression) collectionexpressions.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < basicexpressions.size(); _i++)
    { BasicExpression basicexpressionx_ = (BasicExpression) basicexpressions.get(_i);
       out.print("<basicexpressions xsi:type=\"My:BasicExpression\"");
    out.print(" data=\"" + basicexpressionx_.getdata() + "\" ");
    out.print(" prestate=\"" + basicexpressionx_.getprestate() + "\" ");
    out.print(" needsBracket=\"" + basicexpressionx_.getneedsBracket() + "\" ");
    out.print(" umlKind=\"" + basicexpressionx_.getumlKind() + "\" ");
    out.print(" expId=\"" + basicexpressionx_.getexpId() + "\" ");
    out.print(" isStatic=\"" + basicexpressionx_.getisStatic() + "\" ");
    out.print(" parameters = \"");
    List basicexpression_parameters = basicexpressionx_.getparameters();
    for (int _k = 0; _k < basicexpression_parameters.size(); _k++)
    {      if (basicexpression_parameters.get(_k) instanceof UnaryExpression)
      { out.print(" //@unaryexpressions." + unaryexpressions.indexOf(basicexpression_parameters.get(_k)));
    }
 else      if (basicexpression_parameters.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(basicexpression_parameters.get(_k)));
    }
 else      if (basicexpression_parameters.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(basicexpression_parameters.get(_k)));
    }
 else      if (basicexpression_parameters.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(basicexpression_parameters.get(_k)));
    }
 else      if (basicexpression_parameters.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(basicexpression_parameters.get(_k)));
    }
  }
    out.print("\"");
    out.print(" referredProperty = \"");
    List basicexpression_referredProperty = basicexpressionx_.getreferredProperty();
    for (int _j = 0; _j < basicexpression_referredProperty.size(); _j++)
    { out.print(" //@propertys." + propertys.indexOf(basicexpression_referredProperty.get(_j)));
    }
    out.print("\"");
    out.print(" context = \"");
    List basicexpression_context = basicexpressionx_.getcontext();
    for (int _j = 0; _j < basicexpression_context.size(); _j++)
    { out.print(" //@entitys." + entitys.indexOf(basicexpression_context.get(_j)));
    }
    out.print("\"");
    out.print(" arrayIndex = \"");
    List basicexpression_arrayIndex = basicexpressionx_.getarrayIndex();
    for (int _k = 0; _k < basicexpression_arrayIndex.size(); _k++)
    {      if (basicexpression_arrayIndex.get(_k) instanceof UnaryExpression)
      { out.print(" //@unaryexpressions." + unaryexpressions.indexOf(basicexpression_arrayIndex.get(_k)));
    }
 else      if (basicexpression_arrayIndex.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(basicexpression_arrayIndex.get(_k)));
    }
 else      if (basicexpression_arrayIndex.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(basicexpression_arrayIndex.get(_k)));
    }
 else      if (basicexpression_arrayIndex.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(basicexpression_arrayIndex.get(_k)));
    }
 else      if (basicexpression_arrayIndex.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(basicexpression_arrayIndex.get(_k)));
    }
  }
    out.print("\"");
    out.print(" objectRef = \"");
    List basicexpression_objectRef = basicexpressionx_.getobjectRef();
    for (int _k = 0; _k < basicexpression_objectRef.size(); _k++)
    {      if (basicexpression_objectRef.get(_k) instanceof UnaryExpression)
      { out.print(" //@unaryexpressions." + unaryexpressions.indexOf(basicexpression_objectRef.get(_k)));
    }
 else      if (basicexpression_objectRef.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(basicexpression_objectRef.get(_k)));
    }
 else      if (basicexpression_objectRef.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(basicexpression_objectRef.get(_k)));
    }
 else      if (basicexpression_objectRef.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(basicexpression_objectRef.get(_k)));
    }
 else      if (basicexpression_objectRef.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(basicexpression_objectRef.get(_k)));
    }
  }
    out.print("\"");
    if (basicexpressionx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((BasicExpression) basicexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (basicexpressionx_.gettype() instanceof UseCase)
    {   out.print(" type=\"");
    out.print("//@usecases." + usecases.indexOf(((BasicExpression) basicexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (basicexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((BasicExpression) basicexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (basicexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((BasicExpression) basicexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (basicexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((BasicExpression) basicexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (basicexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (basicexpressionx_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (basicexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (basicexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (basicexpressionx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < printcodes.size(); _i++)
    { Printcode printcodex_ = (Printcode) printcodes.get(_i);
       out.print("<printcodes xsi:type=\"My:Printcode\"");
    out.println(" />");
  }

    for (int _i = 0; _i < types2cs.size(); _i++)
    { Types2C types2cx_ = (Types2C) types2cs.get(_i);
       out.print("<types2cs xsi:type=\"My:Types2C\"");
    out.println(" />");
  }

    for (int _i = 0; _i < program2cs.size(); _i++)
    { Program2C program2cx_ = (Program2C) program2cs.get(_i);
       out.print("<program2cs xsi:type=\"My:Program2C\"");
    out.println(" />");
  }

    for (int _i = 0; _i < returnstatements.size(); _i++)
    { ReturnStatement returnstatementx_ = (ReturnStatement) returnstatements.get(_i);
       out.print("<returnstatements xsi:type=\"My:ReturnStatement\"");
    out.print(" statId=\"" + returnstatementx_.getstatId() + "\" ");
    out.print(" returnValue = \"");
    List returnstatement_returnValue = returnstatementx_.getreturnValue();
    for (int _k = 0; _k < returnstatement_returnValue.size(); _k++)
    {      if (returnstatement_returnValue.get(_k) instanceof UnaryExpression)
      { out.print(" //@unaryexpressions." + unaryexpressions.indexOf(returnstatement_returnValue.get(_k)));
    }
 else      if (returnstatement_returnValue.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(returnstatement_returnValue.get(_k)));
    }
 else      if (returnstatement_returnValue.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(returnstatement_returnValue.get(_k)));
    }
 else      if (returnstatement_returnValue.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(returnstatement_returnValue.get(_k)));
    }
 else      if (returnstatement_returnValue.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(returnstatement_returnValue.get(_k)));
    }
  }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < breakstatements.size(); _i++)
    { BreakStatement breakstatementx_ = (BreakStatement) breakstatements.get(_i);
       out.print("<breakstatements xsi:type=\"My:BreakStatement\"");
    out.print(" statId=\"" + breakstatementx_.getstatId() + "\" ");
    out.println(" />");
  }

    for (int _i = 0; _i < operationcallstatements.size(); _i++)
    { OperationCallStatement operationcallstatementx_ = (OperationCallStatement) operationcallstatements.get(_i);
       out.print("<operationcallstatements xsi:type=\"My:OperationCallStatement\"");
    out.print(" assignsTo=\"" + operationcallstatementx_.getassignsTo() + "\" ");
    out.print(" statId=\"" + operationcallstatementx_.getstatId() + "\" ");
    if (operationcallstatementx_.getcallExp() instanceof UnaryExpression)
    {   out.print(" callExp=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((OperationCallStatement) operationcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (operationcallstatementx_.getcallExp() instanceof ConditionalExpression)
    {   out.print(" callExp=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((OperationCallStatement) operationcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (operationcallstatementx_.getcallExp() instanceof BasicExpression)
    {   out.print(" callExp=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((OperationCallStatement) operationcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (operationcallstatementx_.getcallExp() instanceof BinaryExpression)
    {   out.print(" callExp=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((OperationCallStatement) operationcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (operationcallstatementx_.getcallExp() instanceof CollectionExpression)
    {   out.print(" callExp=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((OperationCallStatement) operationcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < implicitcallstatements.size(); _i++)
    { ImplicitCallStatement implicitcallstatementx_ = (ImplicitCallStatement) implicitcallstatements.get(_i);
       out.print("<implicitcallstatements xsi:type=\"My:ImplicitCallStatement\"");
    out.print(" assignsTo=\"" + implicitcallstatementx_.getassignsTo() + "\" ");
    out.print(" statId=\"" + implicitcallstatementx_.getstatId() + "\" ");
    if (implicitcallstatementx_.getcallExp() instanceof UnaryExpression)
    {   out.print(" callExp=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ImplicitCallStatement) implicitcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (implicitcallstatementx_.getcallExp() instanceof ConditionalExpression)
    {   out.print(" callExp=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ImplicitCallStatement) implicitcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (implicitcallstatementx_.getcallExp() instanceof BasicExpression)
    {   out.print(" callExp=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ImplicitCallStatement) implicitcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (implicitcallstatementx_.getcallExp() instanceof BinaryExpression)
    {   out.print(" callExp=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ImplicitCallStatement) implicitcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (implicitcallstatementx_.getcallExp() instanceof CollectionExpression)
    {   out.print(" callExp=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ImplicitCallStatement) implicitcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < boundedloopstatements.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx_ = (BoundedLoopStatement) boundedloopstatements.get(_i);
       out.print("<boundedloopstatements xsi:type=\"My:BoundedLoopStatement\"");
    out.print(" statId=\"" + boundedloopstatementx_.getstatId() + "\" ");
    if (boundedloopstatementx_.getloopRange() instanceof UnaryExpression)
    {   out.print(" loopRange=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopRange() instanceof ConditionalExpression)
    {   out.print(" loopRange=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopRange() instanceof BasicExpression)
    {   out.print(" loopRange=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopRange() instanceof BinaryExpression)
    {   out.print(" loopRange=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopRange() instanceof CollectionExpression)
    {   out.print(" loopRange=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
    if (boundedloopstatementx_.getloopVar() instanceof UnaryExpression)
    {   out.print(" loopVar=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopVar() instanceof ConditionalExpression)
    {   out.print(" loopVar=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopVar() instanceof BasicExpression)
    {   out.print(" loopVar=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopVar() instanceof BinaryExpression)
    {   out.print(" loopVar=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopVar() instanceof CollectionExpression)
    {   out.print(" loopVar=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
    if (boundedloopstatementx_.gettest() instanceof UnaryExpression)
    {   out.print(" test=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (boundedloopstatementx_.gettest() instanceof ConditionalExpression)
    {   out.print(" test=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (boundedloopstatementx_.gettest() instanceof BasicExpression)
    {   out.print(" test=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (boundedloopstatementx_.gettest() instanceof BinaryExpression)
    {   out.print(" test=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (boundedloopstatementx_.gettest() instanceof CollectionExpression)
    {   out.print(" test=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
    if (boundedloopstatementx_.getbody() instanceof ReturnStatement)
    {   out.print(" body=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof BreakStatement)
    {   out.print(" body=\"");
    out.print("//@breakstatements." + breakstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof OperationCallStatement)
    {   out.print(" body=\"");
    out.print("//@operationcallstatements." + operationcallstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof ImplicitCallStatement)
    {   out.print(" body=\"");
    out.print("//@implicitcallstatements." + implicitcallstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof SequenceStatement)
    {   out.print(" body=\"");
    out.print("//@sequencestatements." + sequencestatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof ConditionalStatement)
    {   out.print(" body=\"");
    out.print("//@conditionalstatements." + conditionalstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof AssignStatement)
    {   out.print(" body=\"");
    out.print("//@assignstatements." + assignstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof CreationStatement)
    {   out.print(" body=\"");
    out.print("//@creationstatements." + creationstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof CatchStatement)
    {   out.print(" body=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof FinalStatement)
    {   out.print(" body=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof TryStatement)
    {   out.print(" body=\"");
    out.print("//@trystatements." + trystatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof AssertStatement)
    {   out.print(" body=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof ErrorStatement)
    {   out.print(" body=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof BoundedLoopStatement)
    {   out.print(" body=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof UnboundedLoopStatement)
    {   out.print(" body=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < unboundedloopstatements.size(); _i++)
    { UnboundedLoopStatement unboundedloopstatementx_ = (UnboundedLoopStatement) unboundedloopstatements.get(_i);
       out.print("<unboundedloopstatements xsi:type=\"My:UnboundedLoopStatement\"");
    out.print(" statId=\"" + unboundedloopstatementx_.getstatId() + "\" ");
    if (unboundedloopstatementx_.gettest() instanceof UnaryExpression)
    {   out.print(" test=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.gettest() instanceof ConditionalExpression)
    {   out.print(" test=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.gettest() instanceof BasicExpression)
    {   out.print(" test=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.gettest() instanceof BinaryExpression)
    {   out.print(" test=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.gettest() instanceof CollectionExpression)
    {   out.print(" test=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
    if (unboundedloopstatementx_.getbody() instanceof ReturnStatement)
    {   out.print(" body=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof BreakStatement)
    {   out.print(" body=\"");
    out.print("//@breakstatements." + breakstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof OperationCallStatement)
    {   out.print(" body=\"");
    out.print("//@operationcallstatements." + operationcallstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof ImplicitCallStatement)
    {   out.print(" body=\"");
    out.print("//@implicitcallstatements." + implicitcallstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof SequenceStatement)
    {   out.print(" body=\"");
    out.print("//@sequencestatements." + sequencestatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof ConditionalStatement)
    {   out.print(" body=\"");
    out.print("//@conditionalstatements." + conditionalstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof AssignStatement)
    {   out.print(" body=\"");
    out.print("//@assignstatements." + assignstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof CreationStatement)
    {   out.print(" body=\"");
    out.print("//@creationstatements." + creationstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof CatchStatement)
    {   out.print(" body=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof FinalStatement)
    {   out.print(" body=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof TryStatement)
    {   out.print(" body=\"");
    out.print("//@trystatements." + trystatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof AssertStatement)
    {   out.print(" body=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof ErrorStatement)
    {   out.print(" body=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof BoundedLoopStatement)
    {   out.print(" body=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof UnboundedLoopStatement)
    {   out.print(" body=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < assignstatements.size(); _i++)
    { AssignStatement assignstatementx_ = (AssignStatement) assignstatements.get(_i);
       out.print("<assignstatements xsi:type=\"My:AssignStatement\"");
    out.print(" statId=\"" + assignstatementx_.getstatId() + "\" ");
    out.print(" type = \"");
    List assignstatement_type = assignstatementx_.gettype();
    for (int _k = 0; _k < assignstatement_type.size(); _k++)
    {      if (assignstatement_type.get(_k) instanceof Entity)
      { out.print(" //@entitys." + entitys.indexOf(assignstatement_type.get(_k)));
    }
 else      if (assignstatement_type.get(_k) instanceof UseCase)
      { out.print(" //@usecases." + usecases.indexOf(assignstatement_type.get(_k)));
    }
 else      if (assignstatement_type.get(_k) instanceof PrimitiveType)
      { out.print(" //@primitivetypes." + primitivetypes.indexOf(assignstatement_type.get(_k)));
    }
 else      if (assignstatement_type.get(_k) instanceof Enumeration)
      { out.print(" //@enumerations." + enumerations.indexOf(assignstatement_type.get(_k)));
    }
 else      if (assignstatement_type.get(_k) instanceof CollectionType)
      { out.print(" //@collectiontypes." + collectiontypes.indexOf(assignstatement_type.get(_k)));
    }
  }
    out.print("\"");
    if (assignstatementx_.getleft() instanceof UnaryExpression)
    {   out.print(" left=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
 else    if (assignstatementx_.getleft() instanceof ConditionalExpression)
    {   out.print(" left=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
 else    if (assignstatementx_.getleft() instanceof BasicExpression)
    {   out.print(" left=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
 else    if (assignstatementx_.getleft() instanceof BinaryExpression)
    {   out.print(" left=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
 else    if (assignstatementx_.getleft() instanceof CollectionExpression)
    {   out.print(" left=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
    if (assignstatementx_.getright() instanceof UnaryExpression)
    {   out.print(" right=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
    out.print("\""); }
 else    if (assignstatementx_.getright() instanceof ConditionalExpression)
    {   out.print(" right=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
    out.print("\""); }
 else    if (assignstatementx_.getright() instanceof BasicExpression)
    {   out.print(" right=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
    out.print("\""); }
 else    if (assignstatementx_.getright() instanceof BinaryExpression)
    {   out.print(" right=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
    out.print("\""); }
 else    if (assignstatementx_.getright() instanceof CollectionExpression)
    {   out.print(" right=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < sequencestatements.size(); _i++)
    { SequenceStatement sequencestatementx_ = (SequenceStatement) sequencestatements.get(_i);
       out.print("<sequencestatements xsi:type=\"My:SequenceStatement\"");
    out.print(" kind=\"" + sequencestatementx_.getkind() + "\" ");
    out.print(" statId=\"" + sequencestatementx_.getstatId() + "\" ");
    out.print(" statements = \"");
    List sequencestatement_statements = sequencestatementx_.getstatements();
    for (int _k = 0; _k < sequencestatement_statements.size(); _k++)
    {      if (sequencestatement_statements.get(_k) instanceof ReturnStatement)
      { out.print(" //@returnstatements." + returnstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof BreakStatement)
      { out.print(" //@breakstatements." + breakstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof OperationCallStatement)
      { out.print(" //@operationcallstatements." + operationcallstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof ImplicitCallStatement)
      { out.print(" //@implicitcallstatements." + implicitcallstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof SequenceStatement)
      { out.print(" //@sequencestatements." + sequencestatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof ConditionalStatement)
      { out.print(" //@conditionalstatements." + conditionalstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof AssignStatement)
      { out.print(" //@assignstatements." + assignstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof CreationStatement)
      { out.print(" //@creationstatements." + creationstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof CatchStatement)
      { out.print(" //@catchstatements." + catchstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof FinalStatement)
      { out.print(" //@finalstatements." + finalstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof TryStatement)
      { out.print(" //@trystatements." + trystatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof AssertStatement)
      { out.print(" //@assertstatements." + assertstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof ErrorStatement)
      { out.print(" //@errorstatements." + errorstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof BoundedLoopStatement)
      { out.print(" //@boundedloopstatements." + boundedloopstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof UnboundedLoopStatement)
      { out.print(" //@unboundedloopstatements." + unboundedloopstatements.indexOf(sequencestatement_statements.get(_k)));
    }
  }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx_ = (ConditionalStatement) conditionalstatements.get(_i);
       out.print("<conditionalstatements xsi:type=\"My:ConditionalStatement\"");
    out.print(" statId=\"" + conditionalstatementx_.getstatId() + "\" ");
    if (conditionalstatementx_.gettest() instanceof UnaryExpression)
    {   out.print(" test=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalstatementx_.gettest() instanceof ConditionalExpression)
    {   out.print(" test=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalstatementx_.gettest() instanceof BasicExpression)
    {   out.print(" test=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalstatementx_.gettest() instanceof BinaryExpression)
    {   out.print(" test=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalstatementx_.gettest() instanceof CollectionExpression)
    {   out.print(" test=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
    if (conditionalstatementx_.getifPart() instanceof ReturnStatement)
    {   out.print(" ifPart=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof BreakStatement)
    {   out.print(" ifPart=\"");
    out.print("//@breakstatements." + breakstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof OperationCallStatement)
    {   out.print(" ifPart=\"");
    out.print("//@operationcallstatements." + operationcallstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof ImplicitCallStatement)
    {   out.print(" ifPart=\"");
    out.print("//@implicitcallstatements." + implicitcallstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof SequenceStatement)
    {   out.print(" ifPart=\"");
    out.print("//@sequencestatements." + sequencestatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof ConditionalStatement)
    {   out.print(" ifPart=\"");
    out.print("//@conditionalstatements." + conditionalstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof AssignStatement)
    {   out.print(" ifPart=\"");
    out.print("//@assignstatements." + assignstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof CreationStatement)
    {   out.print(" ifPart=\"");
    out.print("//@creationstatements." + creationstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof CatchStatement)
    {   out.print(" ifPart=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof FinalStatement)
    {   out.print(" ifPart=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof TryStatement)
    {   out.print(" ifPart=\"");
    out.print("//@trystatements." + trystatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof AssertStatement)
    {   out.print(" ifPart=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof ErrorStatement)
    {   out.print(" ifPart=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof BoundedLoopStatement)
    {   out.print(" ifPart=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof UnboundedLoopStatement)
    {   out.print(" ifPart=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
    out.print(" elsePart = \"");
    List conditionalstatement_elsePart = conditionalstatementx_.getelsePart();
    for (int _k = 0; _k < conditionalstatement_elsePart.size(); _k++)
    {      if (conditionalstatement_elsePart.get(_k) instanceof ReturnStatement)
      { out.print(" //@returnstatements." + returnstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof BreakStatement)
      { out.print(" //@breakstatements." + breakstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof OperationCallStatement)
      { out.print(" //@operationcallstatements." + operationcallstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof ImplicitCallStatement)
      { out.print(" //@implicitcallstatements." + implicitcallstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof SequenceStatement)
      { out.print(" //@sequencestatements." + sequencestatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof ConditionalStatement)
      { out.print(" //@conditionalstatements." + conditionalstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof AssignStatement)
      { out.print(" //@assignstatements." + assignstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof CreationStatement)
      { out.print(" //@creationstatements." + creationstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof CatchStatement)
      { out.print(" //@catchstatements." + catchstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof FinalStatement)
      { out.print(" //@finalstatements." + finalstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof TryStatement)
      { out.print(" //@trystatements." + trystatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof AssertStatement)
      { out.print(" //@assertstatements." + assertstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof ErrorStatement)
      { out.print(" //@errorstatements." + errorstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof BoundedLoopStatement)
      { out.print(" //@boundedloopstatements." + boundedloopstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof UnboundedLoopStatement)
      { out.print(" //@unboundedloopstatements." + unboundedloopstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
  }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < creationstatements.size(); _i++)
    { CreationStatement creationstatementx_ = (CreationStatement) creationstatements.get(_i);
       out.print("<creationstatements xsi:type=\"My:CreationStatement\"");
    out.print(" createsInstanceOf=\"" + creationstatementx_.getcreatesInstanceOf() + "\" ");
    out.print(" assignsTo=\"" + creationstatementx_.getassignsTo() + "\" ");
    out.print(" statId=\"" + creationstatementx_.getstatId() + "\" ");
    if (creationstatementx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((CreationStatement) creationstatements.get(_i)).gettype()));
    out.print("\""); }
 else    if (creationstatementx_.gettype() instanceof UseCase)
    {   out.print(" type=\"");
    out.print("//@usecases." + usecases.indexOf(((CreationStatement) creationstatements.get(_i)).gettype()));
    out.print("\""); }
 else    if (creationstatementx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CreationStatement) creationstatements.get(_i)).gettype()));
    out.print("\""); }
 else    if (creationstatementx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CreationStatement) creationstatements.get(_i)).gettype()));
    out.print("\""); }
 else    if (creationstatementx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CreationStatement) creationstatements.get(_i)).gettype()));
    out.print("\""); }
    if (creationstatementx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((CreationStatement) creationstatements.get(_i)).getelementType()));
    out.print("\""); }
 else    if (creationstatementx_.getelementType() instanceof UseCase)
    {   out.print(" elementType=\"");
    out.print("//@usecases." + usecases.indexOf(((CreationStatement) creationstatements.get(_i)).getelementType()));
    out.print("\""); }
 else    if (creationstatementx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CreationStatement) creationstatements.get(_i)).getelementType()));
    out.print("\""); }
 else    if (creationstatementx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CreationStatement) creationstatements.get(_i)).getelementType()));
    out.print("\""); }
 else    if (creationstatementx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CreationStatement) creationstatements.get(_i)).getelementType()));
    out.print("\""); }
    out.print(" initialExpression = \"");
    List creationstatement_initialExpression = creationstatementx_.getinitialExpression();
    for (int _k = 0; _k < creationstatement_initialExpression.size(); _k++)
    {      if (creationstatement_initialExpression.get(_k) instanceof UnaryExpression)
      { out.print(" //@unaryexpressions." + unaryexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
 else      if (creationstatement_initialExpression.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
 else      if (creationstatement_initialExpression.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
 else      if (creationstatement_initialExpression.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
 else      if (creationstatement_initialExpression.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
  }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < errorstatements.size(); _i++)
    { ErrorStatement errorstatementx_ = (ErrorStatement) errorstatements.get(_i);
       out.print("<errorstatements xsi:type=\"My:ErrorStatement\"");
    out.print(" statId=\"" + errorstatementx_.getstatId() + "\" ");
    if (errorstatementx_.getthrownObject() instanceof UnaryExpression)
    {   out.print(" thrownObject=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ErrorStatement) errorstatements.get(_i)).getthrownObject()));
    out.print("\""); }
 else    if (errorstatementx_.getthrownObject() instanceof ConditionalExpression)
    {   out.print(" thrownObject=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ErrorStatement) errorstatements.get(_i)).getthrownObject()));
    out.print("\""); }
 else    if (errorstatementx_.getthrownObject() instanceof BasicExpression)
    {   out.print(" thrownObject=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ErrorStatement) errorstatements.get(_i)).getthrownObject()));
    out.print("\""); }
 else    if (errorstatementx_.getthrownObject() instanceof BinaryExpression)
    {   out.print(" thrownObject=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ErrorStatement) errorstatements.get(_i)).getthrownObject()));
    out.print("\""); }
 else    if (errorstatementx_.getthrownObject() instanceof CollectionExpression)
    {   out.print(" thrownObject=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ErrorStatement) errorstatements.get(_i)).getthrownObject()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < catchstatements.size(); _i++)
    { CatchStatement catchstatementx_ = (CatchStatement) catchstatements.get(_i);
       out.print("<catchstatements xsi:type=\"My:CatchStatement\"");
    out.print(" statId=\"" + catchstatementx_.getstatId() + "\" ");
    if (catchstatementx_.getcaughtObject() instanceof UnaryExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
 else    if (catchstatementx_.getcaughtObject() instanceof ConditionalExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
 else    if (catchstatementx_.getcaughtObject() instanceof BasicExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
 else    if (catchstatementx_.getcaughtObject() instanceof BinaryExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
 else    if (catchstatementx_.getcaughtObject() instanceof CollectionExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
    if (catchstatementx_.getaction() instanceof ReturnStatement)
    {   out.print(" action=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof BreakStatement)
    {   out.print(" action=\"");
    out.print("//@breakstatements." + breakstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof OperationCallStatement)
    {   out.print(" action=\"");
    out.print("//@operationcallstatements." + operationcallstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof ImplicitCallStatement)
    {   out.print(" action=\"");
    out.print("//@implicitcallstatements." + implicitcallstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof SequenceStatement)
    {   out.print(" action=\"");
    out.print("//@sequencestatements." + sequencestatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof ConditionalStatement)
    {   out.print(" action=\"");
    out.print("//@conditionalstatements." + conditionalstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof AssignStatement)
    {   out.print(" action=\"");
    out.print("//@assignstatements." + assignstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof CreationStatement)
    {   out.print(" action=\"");
    out.print("//@creationstatements." + creationstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof CatchStatement)
    {   out.print(" action=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof FinalStatement)
    {   out.print(" action=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof TryStatement)
    {   out.print(" action=\"");
    out.print("//@trystatements." + trystatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof AssertStatement)
    {   out.print(" action=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof ErrorStatement)
    {   out.print(" action=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof BoundedLoopStatement)
    {   out.print(" action=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof UnboundedLoopStatement)
    {   out.print(" action=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < finalstatements.size(); _i++)
    { FinalStatement finalstatementx_ = (FinalStatement) finalstatements.get(_i);
       out.print("<finalstatements xsi:type=\"My:FinalStatement\"");
    out.print(" statId=\"" + finalstatementx_.getstatId() + "\" ");
    if (finalstatementx_.getbody() instanceof ReturnStatement)
    {   out.print(" body=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof BreakStatement)
    {   out.print(" body=\"");
    out.print("//@breakstatements." + breakstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof OperationCallStatement)
    {   out.print(" body=\"");
    out.print("//@operationcallstatements." + operationcallstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof ImplicitCallStatement)
    {   out.print(" body=\"");
    out.print("//@implicitcallstatements." + implicitcallstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof SequenceStatement)
    {   out.print(" body=\"");
    out.print("//@sequencestatements." + sequencestatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof ConditionalStatement)
    {   out.print(" body=\"");
    out.print("//@conditionalstatements." + conditionalstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof AssignStatement)
    {   out.print(" body=\"");
    out.print("//@assignstatements." + assignstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof CreationStatement)
    {   out.print(" body=\"");
    out.print("//@creationstatements." + creationstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof CatchStatement)
    {   out.print(" body=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof FinalStatement)
    {   out.print(" body=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof TryStatement)
    {   out.print(" body=\"");
    out.print("//@trystatements." + trystatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof AssertStatement)
    {   out.print(" body=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof ErrorStatement)
    {   out.print(" body=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof BoundedLoopStatement)
    {   out.print(" body=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof UnboundedLoopStatement)
    {   out.print(" body=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < trystatements.size(); _i++)
    { TryStatement trystatementx_ = (TryStatement) trystatements.get(_i);
       out.print("<trystatements xsi:type=\"My:TryStatement\"");
    out.print(" statId=\"" + trystatementx_.getstatId() + "\" ");
    out.print(" catchClauses = \"");
    List trystatement_catchClauses = trystatementx_.getcatchClauses();
    for (int _k = 0; _k < trystatement_catchClauses.size(); _k++)
    {      if (trystatement_catchClauses.get(_k) instanceof ReturnStatement)
      { out.print(" //@returnstatements." + returnstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof BreakStatement)
      { out.print(" //@breakstatements." + breakstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof OperationCallStatement)
      { out.print(" //@operationcallstatements." + operationcallstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof ImplicitCallStatement)
      { out.print(" //@implicitcallstatements." + implicitcallstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof SequenceStatement)
      { out.print(" //@sequencestatements." + sequencestatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof ConditionalStatement)
      { out.print(" //@conditionalstatements." + conditionalstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof AssignStatement)
      { out.print(" //@assignstatements." + assignstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof CreationStatement)
      { out.print(" //@creationstatements." + creationstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof CatchStatement)
      { out.print(" //@catchstatements." + catchstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof FinalStatement)
      { out.print(" //@finalstatements." + finalstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof TryStatement)
      { out.print(" //@trystatements." + trystatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof AssertStatement)
      { out.print(" //@assertstatements." + assertstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof ErrorStatement)
      { out.print(" //@errorstatements." + errorstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof BoundedLoopStatement)
      { out.print(" //@boundedloopstatements." + boundedloopstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof UnboundedLoopStatement)
      { out.print(" //@unboundedloopstatements." + unboundedloopstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
  }
    out.print("\"");
    if (trystatementx_.getbody() instanceof ReturnStatement)
    {   out.print(" body=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof BreakStatement)
    {   out.print(" body=\"");
    out.print("//@breakstatements." + breakstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof OperationCallStatement)
    {   out.print(" body=\"");
    out.print("//@operationcallstatements." + operationcallstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof ImplicitCallStatement)
    {   out.print(" body=\"");
    out.print("//@implicitcallstatements." + implicitcallstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof SequenceStatement)
    {   out.print(" body=\"");
    out.print("//@sequencestatements." + sequencestatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof ConditionalStatement)
    {   out.print(" body=\"");
    out.print("//@conditionalstatements." + conditionalstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof AssignStatement)
    {   out.print(" body=\"");
    out.print("//@assignstatements." + assignstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof CreationStatement)
    {   out.print(" body=\"");
    out.print("//@creationstatements." + creationstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof CatchStatement)
    {   out.print(" body=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof FinalStatement)
    {   out.print(" body=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof TryStatement)
    {   out.print(" body=\"");
    out.print("//@trystatements." + trystatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof AssertStatement)
    {   out.print(" body=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof ErrorStatement)
    {   out.print(" body=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof BoundedLoopStatement)
    {   out.print(" body=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof UnboundedLoopStatement)
    {   out.print(" body=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
    out.print(" endStatement = \"");
    List trystatement_endStatement = trystatementx_.getendStatement();
    for (int _k = 0; _k < trystatement_endStatement.size(); _k++)
    {      if (trystatement_endStatement.get(_k) instanceof ReturnStatement)
      { out.print(" //@returnstatements." + returnstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof BreakStatement)
      { out.print(" //@breakstatements." + breakstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof OperationCallStatement)
      { out.print(" //@operationcallstatements." + operationcallstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof ImplicitCallStatement)
      { out.print(" //@implicitcallstatements." + implicitcallstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof SequenceStatement)
      { out.print(" //@sequencestatements." + sequencestatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof ConditionalStatement)
      { out.print(" //@conditionalstatements." + conditionalstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof AssignStatement)
      { out.print(" //@assignstatements." + assignstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof CreationStatement)
      { out.print(" //@creationstatements." + creationstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof CatchStatement)
      { out.print(" //@catchstatements." + catchstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof FinalStatement)
      { out.print(" //@finalstatements." + finalstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof TryStatement)
      { out.print(" //@trystatements." + trystatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof AssertStatement)
      { out.print(" //@assertstatements." + assertstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof ErrorStatement)
      { out.print(" //@errorstatements." + errorstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof BoundedLoopStatement)
      { out.print(" //@boundedloopstatements." + boundedloopstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof UnboundedLoopStatement)
      { out.print(" //@unboundedloopstatements." + unboundedloopstatements.indexOf(trystatement_endStatement.get(_k)));
    }
  }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < assertstatements.size(); _i++)
    { AssertStatement assertstatementx_ = (AssertStatement) assertstatements.get(_i);
       out.print("<assertstatements xsi:type=\"My:AssertStatement\"");
    out.print(" statId=\"" + assertstatementx_.getstatId() + "\" ");
    if (assertstatementx_.getcondition() instanceof UnaryExpression)
    {   out.print(" condition=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((AssertStatement) assertstatements.get(_i)).getcondition()));
    out.print("\""); }
 else    if (assertstatementx_.getcondition() instanceof ConditionalExpression)
    {   out.print(" condition=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((AssertStatement) assertstatements.get(_i)).getcondition()));
    out.print("\""); }
 else    if (assertstatementx_.getcondition() instanceof BasicExpression)
    {   out.print(" condition=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((AssertStatement) assertstatements.get(_i)).getcondition()));
    out.print("\""); }
 else    if (assertstatementx_.getcondition() instanceof BinaryExpression)
    {   out.print(" condition=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((AssertStatement) assertstatements.get(_i)).getcondition()));
    out.print("\""); }
 else    if (assertstatementx_.getcondition() instanceof CollectionExpression)
    {   out.print(" condition=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((AssertStatement) assertstatements.get(_i)).getcondition()));
    out.print("\""); }
    out.print(" message = \"");
    List assertstatement_message = assertstatementx_.getmessage();
    for (int _k = 0; _k < assertstatement_message.size(); _k++)
    {      if (assertstatement_message.get(_k) instanceof UnaryExpression)
      { out.print(" //@unaryexpressions." + unaryexpressions.indexOf(assertstatement_message.get(_k)));
    }
 else      if (assertstatement_message.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(assertstatement_message.get(_k)));
    }
 else      if (assertstatement_message.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(assertstatement_message.get(_k)));
    }
 else      if (assertstatement_message.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(assertstatement_message.get(_k)));
    }
 else      if (assertstatement_message.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(assertstatement_message.get(_k)));
    }
  }
    out.print("\"");
    out.println(" />");
  }

    out.println("</UMLRSDS:model>");
    out.close(); 
  }


  public void saveCSVModel()
  { try {
      File _enumeration = new File("Enumeration.csv");
      PrintWriter _out_enumeration = new PrintWriter(new BufferedWriter(new FileWriter(_enumeration)));
      for (int __i = 0; __i < enumerations.size(); __i++)
      { Enumeration enumerationx = (Enumeration) enumerations.get(__i);
        enumerationx.writeCSV(_out_enumeration);
      }
      _out_enumeration.close();
      File _enumerationliteral = new File("EnumerationLiteral.csv");
      PrintWriter _out_enumerationliteral = new PrintWriter(new BufferedWriter(new FileWriter(_enumerationliteral)));
      for (int __i = 0; __i < enumerationliterals.size(); __i++)
      { EnumerationLiteral enumerationliteralx = (EnumerationLiteral) enumerationliterals.get(__i);
        enumerationliteralx.writeCSV(_out_enumerationliteral);
      }
      _out_enumerationliteral.close();
      File _primitivetype = new File("PrimitiveType.csv");
      PrintWriter _out_primitivetype = new PrintWriter(new BufferedWriter(new FileWriter(_primitivetype)));
      for (int __i = 0; __i < primitivetypes.size(); __i++)
      { PrimitiveType primitivetypex = (PrimitiveType) primitivetypes.get(__i);
        primitivetypex.writeCSV(_out_primitivetype);
      }
      _out_primitivetype.close();
      File _entity = new File("Entity.csv");
      PrintWriter _out_entity = new PrintWriter(new BufferedWriter(new FileWriter(_entity)));
      for (int __i = 0; __i < entitys.size(); __i++)
      { Entity entityx = (Entity) entitys.get(__i);
        entityx.writeCSV(_out_entity);
      }
      _out_entity.close();
      File _property = new File("Property.csv");
      PrintWriter _out_property = new PrintWriter(new BufferedWriter(new FileWriter(_property)));
      for (int __i = 0; __i < propertys.size(); __i++)
      { Property propertyx = (Property) propertys.get(__i);
        propertyx.writeCSV(_out_property);
      }
      _out_property.close();
      File _association = new File("Association.csv");
      PrintWriter _out_association = new PrintWriter(new BufferedWriter(new FileWriter(_association)));
      for (int __i = 0; __i < associations.size(); __i++)
      { Association associationx = (Association) associations.get(__i);
        associationx.writeCSV(_out_association);
      }
      _out_association.close();
      File _generalization = new File("Generalization.csv");
      PrintWriter _out_generalization = new PrintWriter(new BufferedWriter(new FileWriter(_generalization)));
      for (int __i = 0; __i < generalizations.size(); __i++)
      { Generalization generalizationx = (Generalization) generalizations.get(__i);
        generalizationx.writeCSV(_out_generalization);
      }
      _out_generalization.close();
      File _operation = new File("Operation.csv");
      PrintWriter _out_operation = new PrintWriter(new BufferedWriter(new FileWriter(_operation)));
      for (int __i = 0; __i < operations.size(); __i++)
      { Operation operationx = (Operation) operations.get(__i);
        operationx.writeCSV(_out_operation);
      }
      _out_operation.close();
      File _usecase = new File("UseCase.csv");
      PrintWriter _out_usecase = new PrintWriter(new BufferedWriter(new FileWriter(_usecase)));
      for (int __i = 0; __i < usecases.size(); __i++)
      { UseCase usecasex = (UseCase) usecases.get(__i);
        usecasex.writeCSV(_out_usecase);
      }
      _out_usecase.close();
      File _collectiontype = new File("CollectionType.csv");
      PrintWriter _out_collectiontype = new PrintWriter(new BufferedWriter(new FileWriter(_collectiontype)));
      for (int __i = 0; __i < collectiontypes.size(); __i++)
      { CollectionType collectiontypex = (CollectionType) collectiontypes.get(__i);
        collectiontypex.writeCSV(_out_collectiontype);
      }
      _out_collectiontype.close();
      File _cstruct = new File("CStruct.csv");
      PrintWriter _out_cstruct = new PrintWriter(new BufferedWriter(new FileWriter(_cstruct)));
      for (int __i = 0; __i < cstructs.size(); __i++)
      { CStruct cstructx = (CStruct) cstructs.get(__i);
        cstructx.writeCSV(_out_cstruct);
      }
      _out_cstruct.close();
      File _cmember = new File("CMember.csv");
      PrintWriter _out_cmember = new PrintWriter(new BufferedWriter(new FileWriter(_cmember)));
      for (int __i = 0; __i < cmembers.size(); __i++)
      { CMember cmemberx = (CMember) cmembers.get(__i);
        cmemberx.writeCSV(_out_cmember);
      }
      _out_cmember.close();
      File _coperation = new File("COperation.csv");
      PrintWriter _out_coperation = new PrintWriter(new BufferedWriter(new FileWriter(_coperation)));
      for (int __i = 0; __i < coperations.size(); __i++)
      { COperation coperationx = (COperation) coperations.get(__i);
        coperationx.writeCSV(_out_coperation);
      }
      _out_coperation.close();
      File _cvariable = new File("CVariable.csv");
      PrintWriter _out_cvariable = new PrintWriter(new BufferedWriter(new FileWriter(_cvariable)));
      for (int __i = 0; __i < cvariables.size(); __i++)
      { CVariable cvariablex = (CVariable) cvariables.get(__i);
        cvariablex.writeCSV(_out_cvariable);
      }
      _out_cvariable.close();
      File _cprogram = new File("CProgram.csv");
      PrintWriter _out_cprogram = new PrintWriter(new BufferedWriter(new FileWriter(_cprogram)));
      for (int __i = 0; __i < cprograms.size(); __i++)
      { CProgram cprogramx = (CProgram) cprograms.get(__i);
        cprogramx.writeCSV(_out_cprogram);
      }
      _out_cprogram.close();
      File _cprimitivetype = new File("CPrimitiveType.csv");
      PrintWriter _out_cprimitivetype = new PrintWriter(new BufferedWriter(new FileWriter(_cprimitivetype)));
      for (int __i = 0; __i < cprimitivetypes.size(); __i++)
      { CPrimitiveType cprimitivetypex = (CPrimitiveType) cprimitivetypes.get(__i);
        cprimitivetypex.writeCSV(_out_cprimitivetype);
      }
      _out_cprimitivetype.close();
      File _carraytype = new File("CArrayType.csv");
      PrintWriter _out_carraytype = new PrintWriter(new BufferedWriter(new FileWriter(_carraytype)));
      for (int __i = 0; __i < carraytypes.size(); __i++)
      { CArrayType carraytypex = (CArrayType) carraytypes.get(__i);
        carraytypex.writeCSV(_out_carraytype);
      }
      _out_carraytype.close();
      File _cpointertype = new File("CPointerType.csv");
      PrintWriter _out_cpointertype = new PrintWriter(new BufferedWriter(new FileWriter(_cpointertype)));
      for (int __i = 0; __i < cpointertypes.size(); __i++)
      { CPointerType cpointertypex = (CPointerType) cpointertypes.get(__i);
        cpointertypex.writeCSV(_out_cpointertype);
      }
      _out_cpointertype.close();
      File _cfunctionpointertype = new File("CFunctionPointerType.csv");
      PrintWriter _out_cfunctionpointertype = new PrintWriter(new BufferedWriter(new FileWriter(_cfunctionpointertype)));
      for (int __i = 0; __i < cfunctionpointertypes.size(); __i++)
      { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) cfunctionpointertypes.get(__i);
        cfunctionpointertypex.writeCSV(_out_cfunctionpointertype);
      }
      _out_cfunctionpointertype.close();
      File _cenumeration = new File("CEnumeration.csv");
      PrintWriter _out_cenumeration = new PrintWriter(new BufferedWriter(new FileWriter(_cenumeration)));
      for (int __i = 0; __i < cenumerations.size(); __i++)
      { CEnumeration cenumerationx = (CEnumeration) cenumerations.get(__i);
        cenumerationx.writeCSV(_out_cenumeration);
      }
      _out_cenumeration.close();
      File _cenumerationliteral = new File("CEnumerationLiteral.csv");
      PrintWriter _out_cenumerationliteral = new PrintWriter(new BufferedWriter(new FileWriter(_cenumerationliteral)));
      for (int __i = 0; __i < cenumerationliterals.size(); __i++)
      { CEnumerationLiteral cenumerationliteralx = (CEnumerationLiteral) cenumerationliterals.get(__i);
        cenumerationliteralx.writeCSV(_out_cenumerationliteral);
      }
      _out_cenumerationliteral.close();
      File _binaryexpression = new File("BinaryExpression.csv");
      PrintWriter _out_binaryexpression = new PrintWriter(new BufferedWriter(new FileWriter(_binaryexpression)));
      for (int __i = 0; __i < binaryexpressions.size(); __i++)
      { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(__i);
        binaryexpressionx.writeCSV(_out_binaryexpression);
      }
      _out_binaryexpression.close();
      File _conditionalexpression = new File("ConditionalExpression.csv");
      PrintWriter _out_conditionalexpression = new PrintWriter(new BufferedWriter(new FileWriter(_conditionalexpression)));
      for (int __i = 0; __i < conditionalexpressions.size(); __i++)
      { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressions.get(__i);
        conditionalexpressionx.writeCSV(_out_conditionalexpression);
      }
      _out_conditionalexpression.close();
      File _unaryexpression = new File("UnaryExpression.csv");
      PrintWriter _out_unaryexpression = new PrintWriter(new BufferedWriter(new FileWriter(_unaryexpression)));
      for (int __i = 0; __i < unaryexpressions.size(); __i++)
      { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressions.get(__i);
        unaryexpressionx.writeCSV(_out_unaryexpression);
      }
      _out_unaryexpression.close();
      File _collectionexpression = new File("CollectionExpression.csv");
      PrintWriter _out_collectionexpression = new PrintWriter(new BufferedWriter(new FileWriter(_collectionexpression)));
      for (int __i = 0; __i < collectionexpressions.size(); __i++)
      { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(__i);
        collectionexpressionx.writeCSV(_out_collectionexpression);
      }
      _out_collectionexpression.close();
      File _basicexpression = new File("BasicExpression.csv");
      PrintWriter _out_basicexpression = new PrintWriter(new BufferedWriter(new FileWriter(_basicexpression)));
      for (int __i = 0; __i < basicexpressions.size(); __i++)
      { BasicExpression basicexpressionx = (BasicExpression) basicexpressions.get(__i);
        basicexpressionx.writeCSV(_out_basicexpression);
      }
      _out_basicexpression.close();
      File _printcode = new File("Printcode.csv");
      PrintWriter _out_printcode = new PrintWriter(new BufferedWriter(new FileWriter(_printcode)));
      for (int __i = 0; __i < printcodes.size(); __i++)
      { Printcode printcodex = (Printcode) printcodes.get(__i);
        printcodex.writeCSV(_out_printcode);
      }
      _out_printcode.close();
      File _types2c = new File("Types2C.csv");
      PrintWriter _out_types2c = new PrintWriter(new BufferedWriter(new FileWriter(_types2c)));
      for (int __i = 0; __i < types2cs.size(); __i++)
      { Types2C types2cx = (Types2C) types2cs.get(__i);
        types2cx.writeCSV(_out_types2c);
      }
      _out_types2c.close();
      File _program2c = new File("Program2C.csv");
      PrintWriter _out_program2c = new PrintWriter(new BufferedWriter(new FileWriter(_program2c)));
      for (int __i = 0; __i < program2cs.size(); __i++)
      { Program2C program2cx = (Program2C) program2cs.get(__i);
        program2cx.writeCSV(_out_program2c);
      }
      _out_program2c.close();
      File _returnstatement = new File("ReturnStatement.csv");
      PrintWriter _out_returnstatement = new PrintWriter(new BufferedWriter(new FileWriter(_returnstatement)));
      for (int __i = 0; __i < returnstatements.size(); __i++)
      { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(__i);
        returnstatementx.writeCSV(_out_returnstatement);
      }
      _out_returnstatement.close();
      File _breakstatement = new File("BreakStatement.csv");
      PrintWriter _out_breakstatement = new PrintWriter(new BufferedWriter(new FileWriter(_breakstatement)));
      for (int __i = 0; __i < breakstatements.size(); __i++)
      { BreakStatement breakstatementx = (BreakStatement) breakstatements.get(__i);
        breakstatementx.writeCSV(_out_breakstatement);
      }
      _out_breakstatement.close();
      File _operationcallstatement = new File("OperationCallStatement.csv");
      PrintWriter _out_operationcallstatement = new PrintWriter(new BufferedWriter(new FileWriter(_operationcallstatement)));
      for (int __i = 0; __i < operationcallstatements.size(); __i++)
      { OperationCallStatement operationcallstatementx = (OperationCallStatement) operationcallstatements.get(__i);
        operationcallstatementx.writeCSV(_out_operationcallstatement);
      }
      _out_operationcallstatement.close();
      File _implicitcallstatement = new File("ImplicitCallStatement.csv");
      PrintWriter _out_implicitcallstatement = new PrintWriter(new BufferedWriter(new FileWriter(_implicitcallstatement)));
      for (int __i = 0; __i < implicitcallstatements.size(); __i++)
      { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) implicitcallstatements.get(__i);
        implicitcallstatementx.writeCSV(_out_implicitcallstatement);
      }
      _out_implicitcallstatement.close();
      File _boundedloopstatement = new File("BoundedLoopStatement.csv");
      PrintWriter _out_boundedloopstatement = new PrintWriter(new BufferedWriter(new FileWriter(_boundedloopstatement)));
      for (int __i = 0; __i < boundedloopstatements.size(); __i++)
      { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) boundedloopstatements.get(__i);
        boundedloopstatementx.writeCSV(_out_boundedloopstatement);
      }
      _out_boundedloopstatement.close();
      File _unboundedloopstatement = new File("UnboundedLoopStatement.csv");
      PrintWriter _out_unboundedloopstatement = new PrintWriter(new BufferedWriter(new FileWriter(_unboundedloopstatement)));
      for (int __i = 0; __i < unboundedloopstatements.size(); __i++)
      { UnboundedLoopStatement unboundedloopstatementx = (UnboundedLoopStatement) unboundedloopstatements.get(__i);
        unboundedloopstatementx.writeCSV(_out_unboundedloopstatement);
      }
      _out_unboundedloopstatement.close();
      File _assignstatement = new File("AssignStatement.csv");
      PrintWriter _out_assignstatement = new PrintWriter(new BufferedWriter(new FileWriter(_assignstatement)));
      for (int __i = 0; __i < assignstatements.size(); __i++)
      { AssignStatement assignstatementx = (AssignStatement) assignstatements.get(__i);
        assignstatementx.writeCSV(_out_assignstatement);
      }
      _out_assignstatement.close();
      File _sequencestatement = new File("SequenceStatement.csv");
      PrintWriter _out_sequencestatement = new PrintWriter(new BufferedWriter(new FileWriter(_sequencestatement)));
      for (int __i = 0; __i < sequencestatements.size(); __i++)
      { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatements.get(__i);
        sequencestatementx.writeCSV(_out_sequencestatement);
      }
      _out_sequencestatement.close();
      File _conditionalstatement = new File("ConditionalStatement.csv");
      PrintWriter _out_conditionalstatement = new PrintWriter(new BufferedWriter(new FileWriter(_conditionalstatement)));
      for (int __i = 0; __i < conditionalstatements.size(); __i++)
      { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatements.get(__i);
        conditionalstatementx.writeCSV(_out_conditionalstatement);
      }
      _out_conditionalstatement.close();
      File _creationstatement = new File("CreationStatement.csv");
      PrintWriter _out_creationstatement = new PrintWriter(new BufferedWriter(new FileWriter(_creationstatement)));
      for (int __i = 0; __i < creationstatements.size(); __i++)
      { CreationStatement creationstatementx = (CreationStatement) creationstatements.get(__i);
        creationstatementx.writeCSV(_out_creationstatement);
      }
      _out_creationstatement.close();
      File _errorstatement = new File("ErrorStatement.csv");
      PrintWriter _out_errorstatement = new PrintWriter(new BufferedWriter(new FileWriter(_errorstatement)));
      for (int __i = 0; __i < errorstatements.size(); __i++)
      { ErrorStatement errorstatementx = (ErrorStatement) errorstatements.get(__i);
        errorstatementx.writeCSV(_out_errorstatement);
      }
      _out_errorstatement.close();
      File _catchstatement = new File("CatchStatement.csv");
      PrintWriter _out_catchstatement = new PrintWriter(new BufferedWriter(new FileWriter(_catchstatement)));
      for (int __i = 0; __i < catchstatements.size(); __i++)
      { CatchStatement catchstatementx = (CatchStatement) catchstatements.get(__i);
        catchstatementx.writeCSV(_out_catchstatement);
      }
      _out_catchstatement.close();
      File _finalstatement = new File("FinalStatement.csv");
      PrintWriter _out_finalstatement = new PrintWriter(new BufferedWriter(new FileWriter(_finalstatement)));
      for (int __i = 0; __i < finalstatements.size(); __i++)
      { FinalStatement finalstatementx = (FinalStatement) finalstatements.get(__i);
        finalstatementx.writeCSV(_out_finalstatement);
      }
      _out_finalstatement.close();
      File _trystatement = new File("TryStatement.csv");
      PrintWriter _out_trystatement = new PrintWriter(new BufferedWriter(new FileWriter(_trystatement)));
      for (int __i = 0; __i < trystatements.size(); __i++)
      { TryStatement trystatementx = (TryStatement) trystatements.get(__i);
        trystatementx.writeCSV(_out_trystatement);
      }
      _out_trystatement.close();
      File _assertstatement = new File("AssertStatement.csv");
      PrintWriter _out_assertstatement = new PrintWriter(new BufferedWriter(new FileWriter(_assertstatement)));
      for (int __i = 0; __i < assertstatements.size(); __i++)
      { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(__i);
        assertstatementx.writeCSV(_out_assertstatement);
      }
      _out_assertstatement.close();
    }
    catch(Exception __e) { }
  }



  public void addNamedElement(NamedElement oo) { namedelements.add(oo); }

  public void addRelationship(Relationship oo) { relationships.add(oo); addNamedElement(oo); }

  public void addFeature(Feature oo) { features.add(oo); addNamedElement(oo); }

  public void addType(Type oo) { types.add(oo); addNamedElement(oo); }

  public Type getTypeByPK(String typeIdx)
  {  return (Type) typetypeIdindex.get(typeIdx); }

  public List getTypeByPK(List typeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < typeIdx.size(); _i++)
    { Type typex = getTypeByPK((String) typeIdx.get(_i));
      if (typex != null) { res.add(typex); }
    }
    return res; 
  }

  public void addClassifier(Classifier oo) { classifiers.add(oo); addType(oo); }

  public Classifier getClassifierByPK(String typeIdx)
  { if (!(typetypeIdindex.get(typeIdx) instanceof Classifier)) { return null; }
  return (Classifier) typetypeIdindex.get(typeIdx); }

  public List getClassifierByPK(List typeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < typeIdx.size(); _i++)
    { Classifier classifierx = getClassifierByPK((String) typeIdx.get(_i));
      if (classifierx != null) { res.add(classifierx); }
    }
    return res; 
  }

  public void addStructuralFeature(StructuralFeature oo) { structuralfeatures.add(oo); addFeature(oo); }

  public void addDataType(DataType oo) { datatypes.add(oo); addClassifier(oo); }

  public DataType getDataTypeByPK(String typeIdx)
  { if (!(typetypeIdindex.get(typeIdx) instanceof DataType)) { return null; }
  return (DataType) typetypeIdindex.get(typeIdx); }

  public List getDataTypeByPK(List typeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < typeIdx.size(); _i++)
    { DataType datatypex = getDataTypeByPK((String) typeIdx.get(_i));
      if (datatypex != null) { res.add(datatypex); }
    }
    return res; 
  }

  public void addEnumeration(Enumeration oo) { enumerations.add(oo); addDataType(oo); }

  public Enumeration getEnumerationByPK(String typeIdx)
  { if (!(typetypeIdindex.get(typeIdx) instanceof Enumeration)) { return null; }
  return (Enumeration) typetypeIdindex.get(typeIdx); }

  public List getEnumerationByPK(List typeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < typeIdx.size(); _i++)
    { Enumeration enumerationx = getEnumerationByPK((String) typeIdx.get(_i));
      if (enumerationx != null) { res.add(enumerationx); }
    }
    return res; 
  }

  public void addEnumerationLiteral(EnumerationLiteral oo) { enumerationliterals.add(oo); }

  public void addPrimitiveType(PrimitiveType oo) { primitivetypes.add(oo); addDataType(oo); }

  public PrimitiveType getPrimitiveTypeByPK(String typeIdx)
  { if (!(typetypeIdindex.get(typeIdx) instanceof PrimitiveType)) { return null; }
  return (PrimitiveType) typetypeIdindex.get(typeIdx); }

  public List getPrimitiveTypeByPK(List typeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < typeIdx.size(); _i++)
    { PrimitiveType primitivetypex = getPrimitiveTypeByPK((String) typeIdx.get(_i));
      if (primitivetypex != null) { res.add(primitivetypex); }
    }
    return res; 
  }

  public void addEntity(Entity oo) { entitys.add(oo); addClassifier(oo); }

  public Entity getEntityByPK(String typeIdx)
  { if (!(typetypeIdindex.get(typeIdx) instanceof Entity)) { return null; }
  return (Entity) typetypeIdindex.get(typeIdx); }

  public List getEntityByPK(List typeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < typeIdx.size(); _i++)
    { Entity entityx = getEntityByPK((String) typeIdx.get(_i));
      if (entityx != null) { res.add(entityx); }
    }
    return res; 
  }

  public void addProperty(Property oo) { propertys.add(oo); addStructuralFeature(oo); }

  public void addAssociation(Association oo) { associations.add(oo); addRelationship(oo); }

  public void addGeneralization(Generalization oo) { generalizations.add(oo); addRelationship(oo); }

  public void addBehaviouralFeature(BehaviouralFeature oo) { behaviouralfeatures.add(oo); addFeature(oo); }

  public void addOperation(Operation oo) { operations.add(oo); addBehaviouralFeature(oo); }

  public void addExpression(Expression oo) { expressions.add(oo); }

  public Expression getExpressionByPK(String expIdx)
  {  return (Expression) expressionexpIdindex.get(expIdx); }

  public List getExpressionByPK(List expIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < expIdx.size(); _i++)
    { Expression expressionx = getExpressionByPK((String) expIdx.get(_i));
      if (expressionx != null) { res.add(expressionx); }
    }
    return res; 
  }

  public void addUseCase(UseCase oo) { usecases.add(oo); addClassifier(oo); }

  public UseCase getUseCaseByPK(String typeIdx)
  { if (!(typetypeIdindex.get(typeIdx) instanceof UseCase)) { return null; }
  return (UseCase) typetypeIdindex.get(typeIdx); }

  public List getUseCaseByPK(List typeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < typeIdx.size(); _i++)
    { UseCase usecasex = getUseCaseByPK((String) typeIdx.get(_i));
      if (usecasex != null) { res.add(usecasex); }
    }
    return res; 
  }

  public void addCollectionType(CollectionType oo) { collectiontypes.add(oo); addDataType(oo); }

  public CollectionType getCollectionTypeByPK(String typeIdx)
  { if (!(typetypeIdindex.get(typeIdx) instanceof CollectionType)) { return null; }
  return (CollectionType) typetypeIdindex.get(typeIdx); }

  public List getCollectionTypeByPK(List typeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < typeIdx.size(); _i++)
    { CollectionType collectiontypex = getCollectionTypeByPK((String) typeIdx.get(_i));
      if (collectiontypex != null) { res.add(collectiontypex); }
    }
    return res; 
  }

  public void addCStruct(CStruct oo) { cstructs.add(oo); addCType(oo); }

  public CStruct getCStructByPK(String namex)
  {  return (CStruct) cstructnameindex.get(namex); }

  public List getCStructByPK(List namex)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < namex.size(); _i++)
    { CStruct cstructx = getCStructByPK((String) namex.get(_i));
      if (cstructx != null) { res.add(cstructx); }
    }
    return res; 
  }

  public void addCMember(CMember oo) { cmembers.add(oo); }

  public void addCType(CType oo) { ctypes.add(oo); }

  public CType getCTypeByPK(String ctypeIdx)
  {  return (CType) ctypectypeIdindex.get(ctypeIdx); }

  public List getCTypeByPK(List ctypeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < ctypeIdx.size(); _i++)
    { CType ctypex = getCTypeByPK((String) ctypeIdx.get(_i));
      if (ctypex != null) { res.add(ctypex); }
    }
    return res; 
  }

  public void addCOperation(COperation oo) { coperations.add(oo); }

  public COperation getCOperationByPK(String opIdx)
  {  return (COperation) coperationopIdindex.get(opIdx); }

  public List getCOperationByPK(List opIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < opIdx.size(); _i++)
    { COperation coperationx = getCOperationByPK((String) opIdx.get(_i));
      if (coperationx != null) { res.add(coperationx); }
    }
    return res; 
  }

  public void addCVariable(CVariable oo) { cvariables.add(oo); }

  public void addCProgram(CProgram oo) { cprograms.add(oo); }

  public void addCPrimitiveType(CPrimitiveType oo) { cprimitivetypes.add(oo); addCType(oo); }

  public CPrimitiveType getCPrimitiveTypeByPK(String ctypeIdx)
  { if (!(ctypectypeIdindex.get(ctypeIdx) instanceof CPrimitiveType)) { return null; }
  return (CPrimitiveType) ctypectypeIdindex.get(ctypeIdx); }

  public List getCPrimitiveTypeByPK(List ctypeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < ctypeIdx.size(); _i++)
    { CPrimitiveType cprimitivetypex = getCPrimitiveTypeByPK((String) ctypeIdx.get(_i));
      if (cprimitivetypex != null) { res.add(cprimitivetypex); }
    }
    return res; 
  }

  public void addCArrayType(CArrayType oo) { carraytypes.add(oo); addCType(oo); }

  public CArrayType getCArrayTypeByPK(String ctypeIdx)
  { if (!(ctypectypeIdindex.get(ctypeIdx) instanceof CArrayType)) { return null; }
  return (CArrayType) ctypectypeIdindex.get(ctypeIdx); }

  public List getCArrayTypeByPK(List ctypeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < ctypeIdx.size(); _i++)
    { CArrayType carraytypex = getCArrayTypeByPK((String) ctypeIdx.get(_i));
      if (carraytypex != null) { res.add(carraytypex); }
    }
    return res; 
  }

  public void addCPointerType(CPointerType oo) { cpointertypes.add(oo); addCType(oo); }

  public CPointerType getCPointerTypeByPK(String ctypeIdx)
  { if (!(ctypectypeIdindex.get(ctypeIdx) instanceof CPointerType)) { return null; }
  return (CPointerType) ctypectypeIdindex.get(ctypeIdx); }

  public List getCPointerTypeByPK(List ctypeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < ctypeIdx.size(); _i++)
    { CPointerType cpointertypex = getCPointerTypeByPK((String) ctypeIdx.get(_i));
      if (cpointertypex != null) { res.add(cpointertypex); }
    }
    return res; 
  }

  public void addCFunctionPointerType(CFunctionPointerType oo) { cfunctionpointertypes.add(oo); addCType(oo); }

  public CFunctionPointerType getCFunctionPointerTypeByPK(String ctypeIdx)
  { if (!(ctypectypeIdindex.get(ctypeIdx) instanceof CFunctionPointerType)) { return null; }
  return (CFunctionPointerType) ctypectypeIdindex.get(ctypeIdx); }

  public List getCFunctionPointerTypeByPK(List ctypeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < ctypeIdx.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = getCFunctionPointerTypeByPK((String) ctypeIdx.get(_i));
      if (cfunctionpointertypex != null) { res.add(cfunctionpointertypex); }
    }
    return res; 
  }

  public void addCEnumeration(CEnumeration oo) { cenumerations.add(oo); addCType(oo); }

  public CEnumeration getCEnumerationByPK(String ctypeIdx)
  { if (!(ctypectypeIdindex.get(ctypeIdx) instanceof CEnumeration)) { return null; }
  return (CEnumeration) ctypectypeIdindex.get(ctypeIdx); }

  public List getCEnumerationByPK(List ctypeIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < ctypeIdx.size(); _i++)
    { CEnumeration cenumerationx = getCEnumerationByPK((String) ctypeIdx.get(_i));
      if (cenumerationx != null) { res.add(cenumerationx); }
    }
    return res; 
  }

  public void addCEnumerationLiteral(CEnumerationLiteral oo) { cenumerationliterals.add(oo); }

  public void addBinaryExpression(BinaryExpression oo) { binaryexpressions.add(oo); addExpression(oo); }

  public BinaryExpression getBinaryExpressionByPK(String expIdx)
  { if (!(expressionexpIdindex.get(expIdx) instanceof BinaryExpression)) { return null; }
  return (BinaryExpression) expressionexpIdindex.get(expIdx); }

  public List getBinaryExpressionByPK(List expIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < expIdx.size(); _i++)
    { BinaryExpression binaryexpressionx = getBinaryExpressionByPK((String) expIdx.get(_i));
      if (binaryexpressionx != null) { res.add(binaryexpressionx); }
    }
    return res; 
  }

  public void addConditionalExpression(ConditionalExpression oo) { conditionalexpressions.add(oo); addExpression(oo); }

  public ConditionalExpression getConditionalExpressionByPK(String expIdx)
  { if (!(expressionexpIdindex.get(expIdx) instanceof ConditionalExpression)) { return null; }
  return (ConditionalExpression) expressionexpIdindex.get(expIdx); }

  public List getConditionalExpressionByPK(List expIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < expIdx.size(); _i++)
    { ConditionalExpression conditionalexpressionx = getConditionalExpressionByPK((String) expIdx.get(_i));
      if (conditionalexpressionx != null) { res.add(conditionalexpressionx); }
    }
    return res; 
  }

  public void addUnaryExpression(UnaryExpression oo) { unaryexpressions.add(oo); addExpression(oo); }

  public UnaryExpression getUnaryExpressionByPK(String expIdx)
  { if (!(expressionexpIdindex.get(expIdx) instanceof UnaryExpression)) { return null; }
  return (UnaryExpression) expressionexpIdindex.get(expIdx); }

  public List getUnaryExpressionByPK(List expIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < expIdx.size(); _i++)
    { UnaryExpression unaryexpressionx = getUnaryExpressionByPK((String) expIdx.get(_i));
      if (unaryexpressionx != null) { res.add(unaryexpressionx); }
    }
    return res; 
  }

  public void addCollectionExpression(CollectionExpression oo) { collectionexpressions.add(oo); addExpression(oo); }

  public CollectionExpression getCollectionExpressionByPK(String expIdx)
  { if (!(expressionexpIdindex.get(expIdx) instanceof CollectionExpression)) { return null; }
  return (CollectionExpression) expressionexpIdindex.get(expIdx); }

  public List getCollectionExpressionByPK(List expIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < expIdx.size(); _i++)
    { CollectionExpression collectionexpressionx = getCollectionExpressionByPK((String) expIdx.get(_i));
      if (collectionexpressionx != null) { res.add(collectionexpressionx); }
    }
    return res; 
  }

  public void addBasicExpression(BasicExpression oo) { basicexpressions.add(oo); addExpression(oo); }

  public BasicExpression getBasicExpressionByPK(String expIdx)
  { if (!(expressionexpIdindex.get(expIdx) instanceof BasicExpression)) { return null; }
  return (BasicExpression) expressionexpIdindex.get(expIdx); }

  public List getBasicExpressionByPK(List expIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < expIdx.size(); _i++)
    { BasicExpression basicexpressionx = getBasicExpressionByPK((String) expIdx.get(_i));
      if (basicexpressionx != null) { res.add(basicexpressionx); }
    }
    return res; 
  }

  public void addPrintcode(Printcode oo) { printcodes.add(oo); }

  public void addTypes2C(Types2C oo) { types2cs.add(oo); }

  public void addProgram2C(Program2C oo) { program2cs.add(oo); }

  public void addStatement(Statement oo) { statements.add(oo); }

  public Statement getStatementByPK(String statIdx)
  {  return (Statement) statementstatIdindex.get(statIdx); }

  public List getStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { Statement statementx = getStatementByPK((String) statIdx.get(_i));
      if (statementx != null) { res.add(statementx); }
    }
    return res; 
  }

  public void addReturnStatement(ReturnStatement oo) { returnstatements.add(oo); addStatement(oo); }

  public ReturnStatement getReturnStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof ReturnStatement)) { return null; }
  return (ReturnStatement) statementstatIdindex.get(statIdx); }

  public List getReturnStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { ReturnStatement returnstatementx = getReturnStatementByPK((String) statIdx.get(_i));
      if (returnstatementx != null) { res.add(returnstatementx); }
    }
    return res; 
  }

  public void addBreakStatement(BreakStatement oo) { breakstatements.add(oo); addStatement(oo); }

  public BreakStatement getBreakStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof BreakStatement)) { return null; }
  return (BreakStatement) statementstatIdindex.get(statIdx); }

  public List getBreakStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { BreakStatement breakstatementx = getBreakStatementByPK((String) statIdx.get(_i));
      if (breakstatementx != null) { res.add(breakstatementx); }
    }
    return res; 
  }

  public void addOperationCallStatement(OperationCallStatement oo) { operationcallstatements.add(oo); addStatement(oo); }

  public OperationCallStatement getOperationCallStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof OperationCallStatement)) { return null; }
  return (OperationCallStatement) statementstatIdindex.get(statIdx); }

  public List getOperationCallStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { OperationCallStatement operationcallstatementx = getOperationCallStatementByPK((String) statIdx.get(_i));
      if (operationcallstatementx != null) { res.add(operationcallstatementx); }
    }
    return res; 
  }

  public void addImplicitCallStatement(ImplicitCallStatement oo) { implicitcallstatements.add(oo); addStatement(oo); }

  public ImplicitCallStatement getImplicitCallStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof ImplicitCallStatement)) { return null; }
  return (ImplicitCallStatement) statementstatIdindex.get(statIdx); }

  public List getImplicitCallStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { ImplicitCallStatement implicitcallstatementx = getImplicitCallStatementByPK((String) statIdx.get(_i));
      if (implicitcallstatementx != null) { res.add(implicitcallstatementx); }
    }
    return res; 
  }

  public void addLoopStatement(LoopStatement oo) { loopstatements.add(oo); addStatement(oo); }

  public LoopStatement getLoopStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof LoopStatement)) { return null; }
  return (LoopStatement) statementstatIdindex.get(statIdx); }

  public List getLoopStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { LoopStatement loopstatementx = getLoopStatementByPK((String) statIdx.get(_i));
      if (loopstatementx != null) { res.add(loopstatementx); }
    }
    return res; 
  }

  public void addBoundedLoopStatement(BoundedLoopStatement oo) { boundedloopstatements.add(oo); addLoopStatement(oo); }

  public BoundedLoopStatement getBoundedLoopStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof BoundedLoopStatement)) { return null; }
  return (BoundedLoopStatement) statementstatIdindex.get(statIdx); }

  public List getBoundedLoopStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = getBoundedLoopStatementByPK((String) statIdx.get(_i));
      if (boundedloopstatementx != null) { res.add(boundedloopstatementx); }
    }
    return res; 
  }

  public void addUnboundedLoopStatement(UnboundedLoopStatement oo) { unboundedloopstatements.add(oo); addLoopStatement(oo); }

  public UnboundedLoopStatement getUnboundedLoopStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof UnboundedLoopStatement)) { return null; }
  return (UnboundedLoopStatement) statementstatIdindex.get(statIdx); }

  public List getUnboundedLoopStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { UnboundedLoopStatement unboundedloopstatementx = getUnboundedLoopStatementByPK((String) statIdx.get(_i));
      if (unboundedloopstatementx != null) { res.add(unboundedloopstatementx); }
    }
    return res; 
  }

  public void addAssignStatement(AssignStatement oo) { assignstatements.add(oo); addStatement(oo); }

  public AssignStatement getAssignStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof AssignStatement)) { return null; }
  return (AssignStatement) statementstatIdindex.get(statIdx); }

  public List getAssignStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { AssignStatement assignstatementx = getAssignStatementByPK((String) statIdx.get(_i));
      if (assignstatementx != null) { res.add(assignstatementx); }
    }
    return res; 
  }

  public void addSequenceStatement(SequenceStatement oo) { sequencestatements.add(oo); addStatement(oo); }

  public SequenceStatement getSequenceStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof SequenceStatement)) { return null; }
  return (SequenceStatement) statementstatIdindex.get(statIdx); }

  public List getSequenceStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { SequenceStatement sequencestatementx = getSequenceStatementByPK((String) statIdx.get(_i));
      if (sequencestatementx != null) { res.add(sequencestatementx); }
    }
    return res; 
  }

  public void addConditionalStatement(ConditionalStatement oo) { conditionalstatements.add(oo); addStatement(oo); }

  public ConditionalStatement getConditionalStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof ConditionalStatement)) { return null; }
  return (ConditionalStatement) statementstatIdindex.get(statIdx); }

  public List getConditionalStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { ConditionalStatement conditionalstatementx = getConditionalStatementByPK((String) statIdx.get(_i));
      if (conditionalstatementx != null) { res.add(conditionalstatementx); }
    }
    return res; 
  }

  public void addCreationStatement(CreationStatement oo) { creationstatements.add(oo); addStatement(oo); }

  public CreationStatement getCreationStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof CreationStatement)) { return null; }
  return (CreationStatement) statementstatIdindex.get(statIdx); }

  public List getCreationStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { CreationStatement creationstatementx = getCreationStatementByPK((String) statIdx.get(_i));
      if (creationstatementx != null) { res.add(creationstatementx); }
    }
    return res; 
  }

  public void addErrorStatement(ErrorStatement oo) { errorstatements.add(oo); addStatement(oo); }

  public ErrorStatement getErrorStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof ErrorStatement)) { return null; }
  return (ErrorStatement) statementstatIdindex.get(statIdx); }

  public List getErrorStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { ErrorStatement errorstatementx = getErrorStatementByPK((String) statIdx.get(_i));
      if (errorstatementx != null) { res.add(errorstatementx); }
    }
    return res; 
  }

  public void addCatchStatement(CatchStatement oo) { catchstatements.add(oo); addStatement(oo); }

  public CatchStatement getCatchStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof CatchStatement)) { return null; }
  return (CatchStatement) statementstatIdindex.get(statIdx); }

  public List getCatchStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { CatchStatement catchstatementx = getCatchStatementByPK((String) statIdx.get(_i));
      if (catchstatementx != null) { res.add(catchstatementx); }
    }
    return res; 
  }

  public void addFinalStatement(FinalStatement oo) { finalstatements.add(oo); addStatement(oo); }

  public FinalStatement getFinalStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof FinalStatement)) { return null; }
  return (FinalStatement) statementstatIdindex.get(statIdx); }

  public List getFinalStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { FinalStatement finalstatementx = getFinalStatementByPK((String) statIdx.get(_i));
      if (finalstatementx != null) { res.add(finalstatementx); }
    }
    return res; 
  }

  public void addTryStatement(TryStatement oo) { trystatements.add(oo); addStatement(oo); }

  public TryStatement getTryStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof TryStatement)) { return null; }
  return (TryStatement) statementstatIdindex.get(statIdx); }

  public List getTryStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { TryStatement trystatementx = getTryStatementByPK((String) statIdx.get(_i));
      if (trystatementx != null) { res.add(trystatementx); }
    }
    return res; 
  }

  public void addAssertStatement(AssertStatement oo) { assertstatements.add(oo); addStatement(oo); }

  public AssertStatement getAssertStatementByPK(String statIdx)
  { if (!(statementstatIdindex.get(statIdx) instanceof AssertStatement)) { return null; }
  return (AssertStatement) statementstatIdindex.get(statIdx); }

  public List getAssertStatementByPK(List statIdx)
  { Vector res = new Vector(); 
    for (int _i = 0; _i < statIdx.size(); _i++)
    { AssertStatement assertstatementx = getAssertStatementByPK((String) statIdx.get(_i));
      if (assertstatementx != null) { res.add(assertstatementx); }
    }
    return res; 
  }



  public Enumeration createEnumeration()
  { Enumeration enumerationx = new Enumeration();
    addEnumeration(enumerationx);
    return enumerationx;
  }

  public EnumerationLiteral createEnumerationLiteral()
  { EnumerationLiteral enumerationliteralx = new EnumerationLiteral();
    addEnumerationLiteral(enumerationliteralx);
    return enumerationliteralx;
  }

  public PrimitiveType createPrimitiveType()
  { PrimitiveType primitivetypex = new PrimitiveType();
    addPrimitiveType(primitivetypex);
    return primitivetypex;
  }

  public Entity createEntity()
  { Entity entityx = new Entity();
    addEntity(entityx);
    return entityx;
  }

  public Property createProperty()
  { Property propertyx = new Property();
    addProperty(propertyx);
    return propertyx;
  }

  public Association createAssociation()
  { Association associationx = new Association();
    addAssociation(associationx);
    return associationx;
  }

  public Generalization createGeneralization()
  { Generalization generalizationx = new Generalization();
    addGeneralization(generalizationx);
    return generalizationx;
  }

  public Operation createOperation()
  { Operation operationx = new Operation();
    addOperation(operationx);
    return operationx;
  }

  public UseCase createUseCase()
  { UseCase usecasex = new UseCase();
    addUseCase(usecasex);
    return usecasex;
  }

  public CollectionType createCollectionType()
  { CollectionType collectiontypex = new CollectionType();
    addCollectionType(collectiontypex);
    return collectiontypex;
  }

  public CStruct createCStruct()
  { CStruct cstructx = new CStruct();
    addCStruct(cstructx);
    return cstructx;
  }

  public CMember createCMember()
  { CMember cmemberx = new CMember();
    addCMember(cmemberx);
    return cmemberx;
  }

  public COperation createCOperation()
  { COperation coperationx = new COperation();
    addCOperation(coperationx);
    return coperationx;
  }

  public CVariable createCVariable()
  { CVariable cvariablex = new CVariable();
    addCVariable(cvariablex);
    return cvariablex;
  }

  public CProgram createCProgram()
  { CProgram cprogramx = new CProgram();
    addCProgram(cprogramx);
    return cprogramx;
  }

  public CPrimitiveType createCPrimitiveType()
  { CPrimitiveType cprimitivetypex = new CPrimitiveType();
    addCPrimitiveType(cprimitivetypex);
    return cprimitivetypex;
  }

  public CArrayType createCArrayType()
  { CArrayType carraytypex = new CArrayType();
    addCArrayType(carraytypex);
    return carraytypex;
  }

  public CPointerType createCPointerType()
  { CPointerType cpointertypex = new CPointerType();
    addCPointerType(cpointertypex);
    return cpointertypex;
  }

  public CFunctionPointerType createCFunctionPointerType()
  { CFunctionPointerType cfunctionpointertypex = new CFunctionPointerType();
    addCFunctionPointerType(cfunctionpointertypex);
    return cfunctionpointertypex;
  }

  public CEnumeration createCEnumeration()
  { CEnumeration cenumerationx = new CEnumeration();
    addCEnumeration(cenumerationx);
    return cenumerationx;
  }

  public CEnumerationLiteral createCEnumerationLiteral()
  { CEnumerationLiteral cenumerationliteralx = new CEnumerationLiteral();
    addCEnumerationLiteral(cenumerationliteralx);
    return cenumerationliteralx;
  }

  public BinaryExpression createBinaryExpression()
  { BinaryExpression binaryexpressionx = new BinaryExpression();
    addBinaryExpression(binaryexpressionx);
    return binaryexpressionx;
  }

  public ConditionalExpression createConditionalExpression()
  { ConditionalExpression conditionalexpressionx = new ConditionalExpression();
    addConditionalExpression(conditionalexpressionx);
    return conditionalexpressionx;
  }

  public UnaryExpression createUnaryExpression()
  { UnaryExpression unaryexpressionx = new UnaryExpression();
    addUnaryExpression(unaryexpressionx);
    return unaryexpressionx;
  }

  public CollectionExpression createCollectionExpression()
  { CollectionExpression collectionexpressionx = new CollectionExpression();
    addCollectionExpression(collectionexpressionx);
    return collectionexpressionx;
  }

  public BasicExpression createBasicExpression()
  { BasicExpression basicexpressionx = new BasicExpression();
    addBasicExpression(basicexpressionx);
    return basicexpressionx;
  }

  public Printcode createPrintcode()
  { Printcode printcodex = new Printcode();
    addPrintcode(printcodex);
    return printcodex;
  }

  public Types2C createTypes2C()
  { Types2C types2cx = new Types2C();
    addTypes2C(types2cx);
    return types2cx;
  }

  public Program2C createProgram2C()
  { Program2C program2cx = new Program2C();
    addProgram2C(program2cx);
    return program2cx;
  }

  public ReturnStatement createReturnStatement()
  { ReturnStatement returnstatementx = new ReturnStatement();
    addReturnStatement(returnstatementx);
    return returnstatementx;
  }

  public BreakStatement createBreakStatement()
  { BreakStatement breakstatementx = new BreakStatement();
    addBreakStatement(breakstatementx);
    return breakstatementx;
  }

  public OperationCallStatement createOperationCallStatement()
  { OperationCallStatement operationcallstatementx = new OperationCallStatement();
    addOperationCallStatement(operationcallstatementx);
    return operationcallstatementx;
  }

  public ImplicitCallStatement createImplicitCallStatement()
  { ImplicitCallStatement implicitcallstatementx = new ImplicitCallStatement();
    addImplicitCallStatement(implicitcallstatementx);
    return implicitcallstatementx;
  }

  public BoundedLoopStatement createBoundedLoopStatement()
  { BoundedLoopStatement boundedloopstatementx = new BoundedLoopStatement();
    addBoundedLoopStatement(boundedloopstatementx);
    return boundedloopstatementx;
  }

  public UnboundedLoopStatement createUnboundedLoopStatement()
  { UnboundedLoopStatement unboundedloopstatementx = new UnboundedLoopStatement();
    addUnboundedLoopStatement(unboundedloopstatementx);
    return unboundedloopstatementx;
  }

  public AssignStatement createAssignStatement()
  { AssignStatement assignstatementx = new AssignStatement();
    addAssignStatement(assignstatementx);
    return assignstatementx;
  }

  public SequenceStatement createSequenceStatement()
  { SequenceStatement sequencestatementx = new SequenceStatement();
    addSequenceStatement(sequencestatementx);
    return sequencestatementx;
  }

  public ConditionalStatement createConditionalStatement()
  { ConditionalStatement conditionalstatementx = new ConditionalStatement();
    addConditionalStatement(conditionalstatementx);
    return conditionalstatementx;
  }

  public CreationStatement createCreationStatement()
  { CreationStatement creationstatementx = new CreationStatement();
    addCreationStatement(creationstatementx);
    return creationstatementx;
  }

  public ErrorStatement createErrorStatement()
  { ErrorStatement errorstatementx = new ErrorStatement();
    addErrorStatement(errorstatementx);
    return errorstatementx;
  }

  public CatchStatement createCatchStatement()
  { CatchStatement catchstatementx = new CatchStatement();
    addCatchStatement(catchstatementx);
    return catchstatementx;
  }

  public FinalStatement createFinalStatement()
  { FinalStatement finalstatementx = new FinalStatement();
    addFinalStatement(finalstatementx);
    return finalstatementx;
  }

  public TryStatement createTryStatement()
  { TryStatement trystatementx = new TryStatement();
    addTryStatement(trystatementx);
    return trystatementx;
  }

  public AssertStatement createAssertStatement()
  { AssertStatement assertstatementx = new AssertStatement();
    addAssertStatement(assertstatementx);
    return assertstatementx;
  }


public void setname(NamedElement namedelementx, String name_x) 
  { namedelementx.setname(name_x);
    }


public void setisStatic(Feature featurex, boolean isStatic_x) 
  { featurex.setisStatic(isStatic_x);
    }


  public void settype(Feature featurex, Type typexx) 
  {   if (featurex.gettype() == typexx) { return; }
    featurex.settype(typexx);
      }


  public void setelementType(Feature featurex, Type elementTypexx) 
  {   if (featurex.getelementType() == elementTypexx) { return; }
    featurex.setelementType(elementTypexx);
      }


public void settypeId(Type typex, String typeId_x) 
  { if (typetypeIdindex.get(typeId_x) != null) { return; }
  typetypeIdindex.remove(typex.gettypeId());
  typex.settypeId(typeId_x);
  typetypeIdindex.put(typeId_x,typex);
    }


public void setisReadOnly(StructuralFeature structuralfeaturex, boolean isReadOnly_x) 
  { structuralfeaturex.setisReadOnly(isReadOnly_x);
    }


  public void setownedLiteral(Enumeration enumerationx, List ownedLiteralxx) 
  {   List _oldownedLiteralxx = enumerationx.getownedLiteral();
  for (int _i = 0; _i < ownedLiteralxx.size(); _i++)
  { EnumerationLiteral _xx = (EnumerationLiteral) ownedLiteralxx.get(_i);
    if (_oldownedLiteralxx.contains(_xx)) { }
    else { Enumeration.removeAllownedLiteral(enumerations, _xx); }
  }
    enumerationx.setownedLiteral(ownedLiteralxx);
      }


  public void setownedLiteral(Enumeration enumerationx, int _ind, EnumerationLiteral enumerationliteral_x) 
  { enumerationx.setownedLiteral(_ind,enumerationliteral_x); }
  
  public void addownedLiteral(Enumeration enumerationx, EnumerationLiteral ownedLiteralxx) 
  {   Enumeration.removeAllownedLiteral(enumerations,ownedLiteralxx);
    enumerationx.addownedLiteral(ownedLiteralxx);
   }


  public void removeownedLiteral(Enumeration enumerationx, EnumerationLiteral ownedLiteralxx) 
  { enumerationx.removeownedLiteral(ownedLiteralxx);
    }


 public void unionownedLiteral(Enumeration enumerationx, List ownedLiteralx)
  { for (int _i = 0; _i < ownedLiteralx.size(); _i++)
    { EnumerationLiteral enumerationliteralxownedLiteral = (EnumerationLiteral) ownedLiteralx.get(_i);
      addownedLiteral(enumerationx, enumerationliteralxownedLiteral);
     } } 


 public void subtractownedLiteral(Enumeration enumerationx, List ownedLiteralx)
  { for (int _i = 0; _i < ownedLiteralx.size(); _i++)
    { EnumerationLiteral enumerationliteralxownedLiteral = (EnumerationLiteral) ownedLiteralx.get(_i);
      removeownedLiteral(enumerationx, enumerationliteralxownedLiteral);
     } } 


public void setname(EnumerationLiteral enumerationliteralx, String name_x) 
  { enumerationliteralx.setname(name_x);
    }


public void setisAbstract(Entity entityx, boolean isAbstract_x) 
  { entityx.setisAbstract(isAbstract_x);
    }


public void setisInterface(Entity entityx, boolean isInterface_x) 
  { entityx.setisInterface(isInterface_x);
    }


  public void setownedAttribute(Entity entityx, List ownedAttributexx) 
  {   List _oldownedAttributexx = entityx.getownedAttribute();
    for (int _j = 0; _j < _oldownedAttributexx.size(); _j++)
    { Property _yy = (Property) _oldownedAttributexx.get(_j);
      if (ownedAttributexx.contains(_yy)) { }
      else { _yy.setowner(null); }
    }
  for (int _i = 0; _i < ownedAttributexx.size(); _i++)
  { Property _xx = (Property) ownedAttributexx.get(_i);
    if (_oldownedAttributexx.contains(_xx)) { }
    else { if (_xx.getowner() != null) { _xx.getowner().removeownedAttribute(_xx); }  }
    _xx.setowner(entityx);
  }
    entityx.setownedAttribute(ownedAttributexx);
      }


  public void setownedAttribute(Entity entityx, int _ind, Property property_x) 
  { entityx.setownedAttribute(_ind,property_x); }
  
  public void addownedAttribute(Entity entityx, Property ownedAttributexx) 
  {   if (ownedAttributexx.getowner() != null) { ownedAttributexx.getowner().removeownedAttribute(ownedAttributexx); }
  ownedAttributexx.setowner(entityx);
    entityx.addownedAttribute(ownedAttributexx);
   }


  public void removeownedAttribute(Entity entityx, Property ownedAttributexx) 
  { entityx.removeownedAttribute(ownedAttributexx);
      ownedAttributexx.setowner(null);
  }


 public void unionownedAttribute(Entity entityx, List ownedAttributex)
  { for (int _i = 0; _i < ownedAttributex.size(); _i++)
    { Property propertyxownedAttribute = (Property) ownedAttributex.get(_i);
      addownedAttribute(entityx, propertyxownedAttribute);
     } } 


 public void subtractownedAttribute(Entity entityx, List ownedAttributex)
  { for (int _i = 0; _i < ownedAttributex.size(); _i++)
    { Property propertyxownedAttribute = (Property) ownedAttributex.get(_i);
      removeownedAttribute(entityx, propertyxownedAttribute);
     } } 


  public void setsuperclass(Entity entityx, List superclassxx) 
  {   if (superclassxx.size() > 1) { return; }
  List _oldsuperclassxx = entityx.getsuperclass();
    for (int _j = 0; _j < _oldsuperclassxx.size(); _j++)
    { Entity _yy = (Entity) _oldsuperclassxx.get(_j);
      if (superclassxx.contains(_yy)) { }
      else { _yy.removesubclasses(entityx); }
    }
  for (int _i = 0; _i < superclassxx.size(); _i++)
  { Entity _xx = (Entity) superclassxx.get(_i);
    if (_oldsuperclassxx.contains(_xx)) { }
    else { _xx.addsubclasses(entityx); }
  }
    entityx.setsuperclass(superclassxx);
      }


  public void addsuperclass(Entity entityx, Entity superclassxx) 
  { if (entityx.getsuperclass().contains(superclassxx)) { return; }
      if (entityx.getsuperclass().size() > 0)
    { Entity entity_xx = (Entity) entityx.getsuperclass().get(0);
      entityx.removesuperclass(entity_xx);
      entity_xx.removesubclasses(entityx);
    }
    superclassxx.addsubclasses(entityx);
    entityx.addsuperclass(superclassxx);
   }


  public void removesuperclass(Entity entityx, Entity superclassxx) 
  { entityx.removesuperclass(superclassxx);
      superclassxx.removesubclasses(entityx);
  }


 public void unionsuperclass(Entity entityx, List superclassx)
  { for (int _i = 0; _i < superclassx.size(); _i++)
    { Entity entityxsuperclass = (Entity) superclassx.get(_i);
      addsuperclass(entityx, entityxsuperclass);
     } } 


 public void subtractsuperclass(Entity entityx, List superclassx)
  { for (int _i = 0; _i < superclassx.size(); _i++)
    { Entity entityxsuperclass = (Entity) superclassx.get(_i);
      removesuperclass(entityx, entityxsuperclass);
     } } 


  public void setsubclasses(Entity entityx, List subclassesxx) 
  {   List _oldsubclassesxx = entityx.getsubclasses();
    for (int _j = 0; _j < _oldsubclassesxx.size(); _j++)
    { Entity _yy = (Entity) _oldsubclassesxx.get(_j);
      if (subclassesxx.contains(_yy)) { }
      else { _yy.removesuperclass(entityx); }
    }
  for (int _i = 0; _i < subclassesxx.size(); _i++)
  { Entity _xx = (Entity) subclassesxx.get(_i);
    if (_oldsubclassesxx.contains(_xx)) { }
    else { Entity.removeAllsubclasses(_xx.getsuperclass(),_xx); }
    Vector _xxNewValue = new Vector();
    _xxNewValue.add(entityx);
    _xx.setsuperclass(_xxNewValue);
  }
    entityx.setsubclasses(subclassesxx);
      }


  public void addsubclasses(Entity entityx, Entity subclassesxx) 
  { if (entityx.getsubclasses().contains(subclassesxx)) { return; }
    Entity.removeAllsubclasses(subclassesxx.getsuperclass(),subclassesxx);
  subclassesxx.addsuperclass(entityx);
    entityx.addsubclasses(subclassesxx);
   }


  public void removesubclasses(Entity entityx, Entity subclassesxx) 
  { entityx.removesubclasses(subclassesxx);
      subclassesxx.removesuperclass(entityx);
  }


 public void unionsubclasses(Entity entityx, List subclassesx)
  { for (int _i = 0; _i < subclassesx.size(); _i++)
    { Entity entityxsubclasses = (Entity) subclassesx.get(_i);
      addsubclasses(entityx, entityxsubclasses);
     } } 


 public void subtractsubclasses(Entity entityx, List subclassesx)
  { for (int _i = 0; _i < subclassesx.size(); _i++)
    { Entity entityxsubclasses = (Entity) subclassesx.get(_i);
      removesubclasses(entityx, entityxsubclasses);
     } } 


  public void setgeneralization(Entity entityx, List generalizationxx) 
  {   List _oldgeneralizationxx = entityx.getgeneralization();
    for (int _j = 0; _j < _oldgeneralizationxx.size(); _j++)
    { Generalization _yy = (Generalization) _oldgeneralizationxx.get(_j);
      if (generalizationxx.contains(_yy)) { }
      else { _yy.setspecific(null); }
    }
  for (int _i = 0; _i < generalizationxx.size(); _i++)
  { Generalization _xx = (Generalization) generalizationxx.get(_i);
    if (_oldgeneralizationxx.contains(_xx)) { }
    else { if (_xx.getspecific() != null) { _xx.getspecific().removegeneralization(_xx); }  }
    _xx.setspecific(entityx);
  }
    entityx.setgeneralization(generalizationxx);
      }


  public void addgeneralization(Entity entityx, Generalization generalizationxx) 
  { if (entityx.getgeneralization().contains(generalizationxx)) { return; }
    if (generalizationxx.getspecific() != null) { generalizationxx.getspecific().removegeneralization(generalizationxx); }
  generalizationxx.setspecific(entityx);
    entityx.addgeneralization(generalizationxx);
   }


  public void removegeneralization(Entity entityx, Generalization generalizationxx) 
  { entityx.removegeneralization(generalizationxx);
      generalizationxx.setspecific(null);
  }


 public void uniongeneralization(Entity entityx, List generalizationx)
  { for (int _i = 0; _i < generalizationx.size(); _i++)
    { Generalization generalizationxgeneralization = (Generalization) generalizationx.get(_i);
      addgeneralization(entityx, generalizationxgeneralization);
     } } 


 public void subtractgeneralization(Entity entityx, List generalizationx)
  { for (int _i = 0; _i < generalizationx.size(); _i++)
    { Generalization generalizationxgeneralization = (Generalization) generalizationx.get(_i);
      removegeneralization(entityx, generalizationxgeneralization);
     } } 


  public void setspecialization(Entity entityx, List specializationxx) 
  {   List _oldspecializationxx = entityx.getspecialization();
    for (int _j = 0; _j < _oldspecializationxx.size(); _j++)
    { Generalization _yy = (Generalization) _oldspecializationxx.get(_j);
      if (specializationxx.contains(_yy)) { }
      else { _yy.setgeneral(null); }
    }
  for (int _i = 0; _i < specializationxx.size(); _i++)
  { Generalization _xx = (Generalization) specializationxx.get(_i);
    if (_oldspecializationxx.contains(_xx)) { }
    else { if (_xx.getgeneral() != null) { _xx.getgeneral().removespecialization(_xx); }  }
    _xx.setgeneral(entityx);
  }
    entityx.setspecialization(specializationxx);
      }


  public void addspecialization(Entity entityx, Generalization specializationxx) 
  { if (entityx.getspecialization().contains(specializationxx)) { return; }
    if (specializationxx.getgeneral() != null) { specializationxx.getgeneral().removespecialization(specializationxx); }
  specializationxx.setgeneral(entityx);
    entityx.addspecialization(specializationxx);
   }


  public void removespecialization(Entity entityx, Generalization specializationxx) 
  { entityx.removespecialization(specializationxx);
      specializationxx.setgeneral(null);
  }


 public void unionspecialization(Entity entityx, List specializationx)
  { for (int _i = 0; _i < specializationx.size(); _i++)
    { Generalization generalizationxspecialization = (Generalization) specializationx.get(_i);
      addspecialization(entityx, generalizationxspecialization);
     } } 


 public void subtractspecialization(Entity entityx, List specializationx)
  { for (int _i = 0; _i < specializationx.size(); _i++)
    { Generalization generalizationxspecialization = (Generalization) specializationx.get(_i);
      removespecialization(entityx, generalizationxspecialization);
     } } 


  public void setownedOperation(Entity entityx, List ownedOperationxx) 
  {   List _oldownedOperationxx = entityx.getownedOperation();
    for (int _j = 0; _j < _oldownedOperationxx.size(); _j++)
    { Operation _yy = (Operation) _oldownedOperationxx.get(_j);
      if (ownedOperationxx.contains(_yy)) { }
      else { _yy.setowner(null); }
    }
  for (int _i = 0; _i < ownedOperationxx.size(); _i++)
  { Operation _xx = (Operation) ownedOperationxx.get(_i);
    if (_oldownedOperationxx.contains(_xx)) { }
    else { if (_xx.getowner() != null) { _xx.getowner().removeownedOperation(_xx); }  }
    _xx.setowner(entityx);
  }
    entityx.setownedOperation(ownedOperationxx);
      }


  public void setownedOperation(Entity entityx, int _ind, Operation operation_x) 
  { entityx.setownedOperation(_ind,operation_x); }
  
  public void addownedOperation(Entity entityx, Operation ownedOperationxx) 
  {   if (ownedOperationxx.getowner() != null) { ownedOperationxx.getowner().removeownedOperation(ownedOperationxx); }
  ownedOperationxx.setowner(entityx);
    entityx.addownedOperation(ownedOperationxx);
   }


  public void removeownedOperation(Entity entityx, Operation ownedOperationxx) 
  { entityx.removeownedOperation(ownedOperationxx);
      ownedOperationxx.setowner(null);
  }


 public void unionownedOperation(Entity entityx, List ownedOperationx)
  { for (int _i = 0; _i < ownedOperationx.size(); _i++)
    { Operation operationxownedOperation = (Operation) ownedOperationx.get(_i);
      addownedOperation(entityx, operationxownedOperation);
     } } 


 public void subtractownedOperation(Entity entityx, List ownedOperationx)
  { for (int _i = 0; _i < ownedOperationx.size(); _i++)
    { Operation operationxownedOperation = (Operation) ownedOperationx.get(_i);
      removeownedOperation(entityx, operationxownedOperation);
     } } 


public void setlower(Property propertyx, int lower_x) 
  { propertyx.setlower(lower_x);
    }


public void setupper(Property propertyx, int upper_x) 
  { propertyx.setupper(upper_x);
    }


public void setisOrdered(Property propertyx, boolean isOrdered_x) 
  { propertyx.setisOrdered(isOrdered_x);
    }


public void setisUnique(Property propertyx, boolean isUnique_x) 
  { propertyx.setisUnique(isUnique_x);
    }


public void setisDerived(Property propertyx, boolean isDerived_x) 
  { propertyx.setisDerived(isDerived_x);
    }


  public void setowner(Property propertyx, Entity ownerxx) 
  {   if (propertyx.getowner() == ownerxx) { return; }
    if (propertyx.getowner() != null)
    { Entity old_value = propertyx.getowner();
      old_value.removeownedAttribute(propertyx); } 
    if (ownerxx != null) { ownerxx.addownedAttribute(propertyx); }
    propertyx.setowner(ownerxx);
      }


  public void setinitialValue(Property propertyx, Expression initialValuexx) 
  {   if (propertyx.getinitialValue() == initialValuexx) { return; }
    for (int _q = 0; _q < propertys.size(); _q++)
    { Property _q_E1x = (Property) propertys.get(_q);
      if (_q_E1x.getinitialValue() == initialValuexx)
      { _q_E1x.setinitialValue(null); }
    }
    propertyx.setinitialValue(initialValuexx);
      }


public void setaddOnly(Association associationx, boolean addOnly_x) 
  { associationx.setaddOnly(addOnly_x);
    }


public void setaggregation(Association associationx, boolean aggregation_x) 
  { associationx.setaggregation(aggregation_x);
    }


  public void setmemberEnd(Association associationx, List memberEndxx) 
  {   List _oldmemberEndxx = associationx.getmemberEnd();
  for (int _i = 0; _i < memberEndxx.size(); _i++)
  { Property _xx = (Property) memberEndxx.get(_i);
    if (_oldmemberEndxx.contains(_xx)) { }
    else { Association.removeAllmemberEnd(associations, _xx); }
  }
    associationx.setmemberEnd(memberEndxx);
      }


  public void setmemberEnd(Association associationx, int _ind, Property property_x) 
  { associationx.setmemberEnd(_ind,property_x); }
  
  public void addmemberEnd(Association associationx, Property memberEndxx) 
  {   Association.removeAllmemberEnd(associations,memberEndxx);
    associationx.addmemberEnd(memberEndxx);
   }


  public void removememberEnd(Association associationx, Property memberEndxx) 
  { associationx.removememberEnd(memberEndxx);
    }


 public void unionmemberEnd(Association associationx, List memberEndx)
  { for (int _i = 0; _i < memberEndx.size(); _i++)
    { Property propertyxmemberEnd = (Property) memberEndx.get(_i);
      addmemberEnd(associationx, propertyxmemberEnd);
     } } 


 public void subtractmemberEnd(Association associationx, List memberEndx)
  { for (int _i = 0; _i < memberEndx.size(); _i++)
    { Property propertyxmemberEnd = (Property) memberEndx.get(_i);
      removememberEnd(associationx, propertyxmemberEnd);
     } } 


  public void setspecific(Generalization generalizationx, Entity specificxx) 
  {   if (generalizationx.getspecific() == specificxx) { return; }
    if (generalizationx.getspecific() != null)
    { Entity old_value = generalizationx.getspecific();
      old_value.removegeneralization(generalizationx); } 
    if (specificxx != null) { specificxx.addgeneralization(generalizationx); }
    generalizationx.setspecific(specificxx);
      }


  public void setgeneral(Generalization generalizationx, Entity generalxx) 
  {   if (generalizationx.getgeneral() == generalxx) { return; }
    if (generalizationx.getgeneral() != null)
    { Entity old_value = generalizationx.getgeneral();
      old_value.removespecialization(generalizationx); } 
    if (generalxx != null) { generalxx.addspecialization(generalizationx); }
    generalizationx.setgeneral(generalxx);
      }


public void setisAbstract(BehaviouralFeature behaviouralfeaturex, boolean isAbstract_x) 
  { behaviouralfeaturex.setisAbstract(isAbstract_x);
    }


  public void setparameters(BehaviouralFeature behaviouralfeaturex, List parametersxx) 
  {   List _oldparametersxx = behaviouralfeaturex.getparameters();
  for (int _i = 0; _i < parametersxx.size(); _i++)
  { Property _xx = (Property) parametersxx.get(_i);
    if (_oldparametersxx.contains(_xx)) { }
    else { BehaviouralFeature.removeAllparameters(behaviouralfeatures, _xx); }
  }
    behaviouralfeaturex.setparameters(parametersxx);
      }


  public void setparameters(BehaviouralFeature behaviouralfeaturex, int _ind, Property property_x) 
  { behaviouralfeaturex.setparameters(_ind,property_x); }
  
  public void addparameters(BehaviouralFeature behaviouralfeaturex, Property parametersxx) 
  {   BehaviouralFeature.removeAllparameters(behaviouralfeatures,parametersxx);
    behaviouralfeaturex.addparameters(parametersxx);
   }


  public void removeparameters(BehaviouralFeature behaviouralfeaturex, Property parametersxx) 
  { behaviouralfeaturex.removeparameters(parametersxx);
    }


 public void unionparameters(BehaviouralFeature behaviouralfeaturex, List parametersx)
  { for (int _i = 0; _i < parametersx.size(); _i++)
    { Property propertyxparameters = (Property) parametersx.get(_i);
      addparameters(behaviouralfeaturex, propertyxparameters);
     } } 


 public void subtractparameters(BehaviouralFeature behaviouralfeaturex, List parametersx)
  { for (int _i = 0; _i < parametersx.size(); _i++)
    { Property propertyxparameters = (Property) parametersx.get(_i);
      removeparameters(behaviouralfeaturex, propertyxparameters);
     } } 


  public void setactivity(BehaviouralFeature behaviouralfeaturex, Statement activityxx) 
  {   if (behaviouralfeaturex.getactivity() == activityxx) { return; }
    for (int _q = 0; _q < behaviouralfeatures.size(); _q++)
    { BehaviouralFeature _q_E1x = (BehaviouralFeature) behaviouralfeatures.get(_q);
      if (_q_E1x.getactivity() == activityxx)
      { _q_E1x.setactivity(null); }
    }
    behaviouralfeaturex.setactivity(activityxx);
      }


public void setisQuery(Operation operationx, boolean isQuery_x) 
  { operationx.setisQuery(isQuery_x);
    }


public void setisCached(Operation operationx, boolean isCached_x) 
  { operationx.setisCached(isCached_x);
    }


  public void setowner(Operation operationx, Entity ownerxx) 
  {   if (operationx.getowner() == ownerxx) { return; }
    if (operationx.getowner() != null)
    { Entity old_value = operationx.getowner();
      old_value.removeownedOperation(operationx); } 
    if (ownerxx != null) { ownerxx.addownedOperation(operationx); }
    operationx.setowner(ownerxx);
      }


  public void setdefiners(Operation operationx, List definersxx) 
  {     operationx.setdefiners(definersxx);
      }


  public void setdefiners(Operation operationx, int _ind, Entity entity_x) 
  { operationx.setdefiners(_ind,entity_x); }
  
  public void adddefiners(Operation operationx, Entity definersxx) 
  {     operationx.adddefiners(definersxx);
   }


  public void removedefiners(Operation operationx, Entity definersxx) 
  { operationx.removedefiners(definersxx);
    }


 public void uniondefiners(Operation operationx, List definersx)
  { for (int _i = 0; _i < definersx.size(); _i++)
    { Entity entityxdefiners = (Entity) definersx.get(_i);
      adddefiners(operationx, entityxdefiners);
     } } 


 public void subtractdefiners(Operation operationx, List definersx)
  { for (int _i = 0; _i < definersx.size(); _i++)
    { Entity entityxdefiners = (Entity) definersx.get(_i);
      removedefiners(operationx, entityxdefiners);
     } } 


public void setneedsBracket(Expression expressionx, boolean needsBracket_x) 
  { expressionx.setneedsBracket(needsBracket_x);
    }


public void setumlKind(Expression expressionx, int umlKind_x) 
  { expressionx.setumlKind(umlKind_x);
    }


public void setexpId(Expression expressionx, String expId_x) 
  { if (expressionexpIdindex.get(expId_x) != null) { return; }
  expressionexpIdindex.remove(expressionx.getexpId());
  expressionx.setexpId(expId_x);
  expressionexpIdindex.put(expId_x,expressionx);
    }


public void setisStatic(Expression expressionx, boolean isStatic_x) 
  { expressionx.setisStatic(isStatic_x);
    }


  public void settype(Expression expressionx, Type typexx) 
  {   if (expressionx.gettype() == typexx) { return; }
    expressionx.settype(typexx);
      }


  public void setelementType(Expression expressionx, Type elementTypexx) 
  {   if (expressionx.getelementType() == elementTypexx) { return; }
    expressionx.setelementType(elementTypexx);
      }


public void setisGeneric(UseCase usecasex, boolean isGeneric_x) 
  { usecasex.setisGeneric(isGeneric_x);
    }


public void setisDerived(UseCase usecasex, boolean isDerived_x) 
  { usecasex.setisDerived(isDerived_x);
    }


  public void setparameters(UseCase usecasex, List parametersxx) 
  {   List _oldparametersxx = usecasex.getparameters();
  for (int _i = 0; _i < parametersxx.size(); _i++)
  { Property _xx = (Property) parametersxx.get(_i);
    if (_oldparametersxx.contains(_xx)) { }
    else { UseCase.removeAllparameters(usecases, _xx); }
  }
    usecasex.setparameters(parametersxx);
      }


  public void setparameters(UseCase usecasex, int _ind, Property property_x) 
  { usecasex.setparameters(_ind,property_x); }
  
  public void addparameters(UseCase usecasex, Property parametersxx) 
  {   UseCase.removeAllparameters(usecases,parametersxx);
    usecasex.addparameters(parametersxx);
   }


  public void removeparameters(UseCase usecasex, Property parametersxx) 
  { usecasex.removeparameters(parametersxx);
    }


 public void unionparameters(UseCase usecasex, List parametersx)
  { for (int _i = 0; _i < parametersx.size(); _i++)
    { Property propertyxparameters = (Property) parametersx.get(_i);
      addparameters(usecasex, propertyxparameters);
     } } 


 public void subtractparameters(UseCase usecasex, List parametersx)
  { for (int _i = 0; _i < parametersx.size(); _i++)
    { Property propertyxparameters = (Property) parametersx.get(_i);
      removeparameters(usecasex, propertyxparameters);
     } } 


  public void setclassifierBehaviour(UseCase usecasex, Statement classifierBehaviourxx) 
  {   if (usecasex.getclassifierBehaviour() == classifierBehaviourxx) { return; }
    for (int _q = 0; _q < usecases.size(); _q++)
    { UseCase _q_E1x = (UseCase) usecases.get(_q);
      if (_q_E1x.getclassifierBehaviour() == classifierBehaviourxx)
      { _q_E1x.setclassifierBehaviour(null); }
    }
    usecasex.setclassifierBehaviour(classifierBehaviourxx);
      }


  public void setresultType(UseCase usecasex, Type resultTypexx) 
  {   if (usecasex.getresultType() == resultTypexx) { return; }
    usecasex.setresultType(resultTypexx);
      }


  public void setelementType(CollectionType collectiontypex, Type elementTypexx) 
  {   if (collectiontypex.getelementType() == elementTypexx) { return; }
    collectiontypex.setelementType(elementTypexx);
      }


  public void setkeyType(CollectionType collectiontypex, Type keyTypexx) 
  {   if (collectiontypex.getkeyType() == keyTypexx) { return; }
    collectiontypex.setkeyType(keyTypexx);
      }


public void setname(CStruct cstructx, String name_x) 
  { if (cstructnameindex.get(name_x) != null) { return; }
  cstructnameindex.remove(cstructx.getname());
  cstructx.setname(name_x);
  cstructnameindex.put(name_x,cstructx);
    }


  public void setinterfaces(CStruct cstructx, List interfacesxx) 
  {     cstructx.setinterfaces(interfacesxx);
      }


  public void addinterfaces(CStruct cstructx, CStruct interfacesxx) 
  { if (cstructx.getinterfaces().contains(interfacesxx)) { return; }
      cstructx.addinterfaces(interfacesxx);
   }


  public void removeinterfaces(CStruct cstructx, CStruct interfacesxx) 
  { cstructx.removeinterfaces(interfacesxx);
    }


 public void unioninterfaces(CStruct cstructx, List interfacesx)
  { for (int _i = 0; _i < interfacesx.size(); _i++)
    { CStruct cstructxinterfaces = (CStruct) interfacesx.get(_i);
      addinterfaces(cstructx, cstructxinterfaces);
     } } 


 public void subtractinterfaces(CStruct cstructx, List interfacesx)
  { for (int _i = 0; _i < interfacesx.size(); _i++)
    { CStruct cstructxinterfaces = (CStruct) interfacesx.get(_i);
      removeinterfaces(cstructx, cstructxinterfaces);
     } } 


  public void setmembers(CStruct cstructx, List membersxx) 
  {   List _oldmembersxx = cstructx.getmembers();
  for (int _i = 0; _i < membersxx.size(); _i++)
  { CMember _xx = (CMember) membersxx.get(_i);
    if (_oldmembersxx.contains(_xx)) { }
    else { CStruct.removeAllmembers(cstructs, _xx); }
  }
    cstructx.setmembers(membersxx);
      }


  public void setmembers(CStruct cstructx, int _ind, CMember cmember_x) 
  { cstructx.setmembers(_ind,cmember_x); }
  
  public void addmembers(CStruct cstructx, CMember membersxx) 
  {   CStruct.removeAllmembers(cstructs,membersxx);
    cstructx.addmembers(membersxx);
   }


  public void removemembers(CStruct cstructx, CMember membersxx) 
  { cstructx.removemembers(membersxx);
    }


 public void unionmembers(CStruct cstructx, List membersx)
  { for (int _i = 0; _i < membersx.size(); _i++)
    { CMember cmemberxmembers = (CMember) membersx.get(_i);
      addmembers(cstructx, cmemberxmembers);
     } } 


 public void subtractmembers(CStruct cstructx, List membersx)
  { for (int _i = 0; _i < membersx.size(); _i++)
    { CMember cmemberxmembers = (CMember) membersx.get(_i);
      removemembers(cstructx, cmemberxmembers);
     } } 


  public void setallMembers(CStruct cstructx, List allMembersxx) 
  {     cstructx.setallMembers(allMembersxx);
      }


  public void addallMembers(CStruct cstructx, CMember allMembersxx) 
  { if (cstructx.getallMembers().contains(allMembersxx)) { return; }
      cstructx.addallMembers(allMembersxx);
   }


  public void removeallMembers(CStruct cstructx, CMember allMembersxx) 
  { cstructx.removeallMembers(allMembersxx);
    }


 public void unionallMembers(CStruct cstructx, List allMembersx)
  { for (int _i = 0; _i < allMembersx.size(); _i++)
    { CMember cmemberxallMembers = (CMember) allMembersx.get(_i);
      addallMembers(cstructx, cmemberxallMembers);
     } } 


 public void subtractallMembers(CStruct cstructx, List allMembersx)
  { for (int _i = 0; _i < allMembersx.size(); _i++)
    { CMember cmemberxallMembers = (CMember) allMembersx.get(_i);
      removeallMembers(cstructx, cmemberxallMembers);
     } } 


public void setname(CMember cmemberx, String name_x) 
  { cmemberx.setname(name_x);
    }


public void setisKey(CMember cmemberx, boolean isKey_x) 
  { cmemberx.setisKey(isKey_x);
    }


public void setmultiplicity(CMember cmemberx, String multiplicity_x) 
  { cmemberx.setmultiplicity(multiplicity_x);
    }


  public void settype(CMember cmemberx, CType typexx) 
  {   if (cmemberx.gettype() == typexx) { return; }
    cmemberx.settype(typexx);
      }


public void setctypeId(CType ctypex, String ctypeId_x) 
  { if (ctypectypeIdindex.get(ctypeId_x) != null) { return; }
  ctypectypeIdindex.remove(ctypex.getctypeId());
  ctypex.setctypeId(ctypeId_x);
  ctypectypeIdindex.put(ctypeId_x,ctypex);
    }


public void setname(COperation coperationx, String name_x) 
  { coperationx.setname(name_x);
    }


public void setopId(COperation coperationx, String opId_x) 
  { if (coperationopIdindex.get(opId_x) != null) { return; }
  coperationopIdindex.remove(coperationx.getopId());
  coperationx.setopId(opId_x);
  coperationopIdindex.put(opId_x,coperationx);
    }


public void setisStatic(COperation coperationx, boolean isStatic_x) 
  { coperationx.setisStatic(isStatic_x);
    }


public void setscope(COperation coperationx, String scope_x) 
  { coperationx.setscope(scope_x);
    }


public void setisQuery(COperation coperationx, boolean isQuery_x) 
  { coperationx.setisQuery(isQuery_x);
    }


public void setinheritedFrom(COperation coperationx, String inheritedFrom_x) 
  { coperationx.setinheritedFrom(inheritedFrom_x);
    }


public void setisAbstract(COperation coperationx, boolean isAbstract_x) 
  { coperationx.setisAbstract(isAbstract_x);
    }


public void setclassname(COperation coperationx, String classname_x) 
  { coperationx.setclassname(classname_x);
    }


  public void setparameters(COperation coperationx, List parametersxx) 
  {   List _oldparametersxx = coperationx.getparameters();
  for (int _i = 0; _i < parametersxx.size(); _i++)
  { CVariable _xx = (CVariable) parametersxx.get(_i);
    if (_oldparametersxx.contains(_xx)) { }
    else { COperation.removeAllparameters(coperations, _xx); }
  }
    coperationx.setparameters(parametersxx);
      }


  public void setparameters(COperation coperationx, int _ind, CVariable cvariable_x) 
  { coperationx.setparameters(_ind,cvariable_x); }
  
  public void addparameters(COperation coperationx, CVariable parametersxx) 
  {   COperation.removeAllparameters(coperations,parametersxx);
    coperationx.addparameters(parametersxx);
   }


  public void removeparameters(COperation coperationx, CVariable parametersxx) 
  { coperationx.removeparameters(parametersxx);
    }


 public void unionparameters(COperation coperationx, List parametersx)
  { for (int _i = 0; _i < parametersx.size(); _i++)
    { CVariable cvariablexparameters = (CVariable) parametersx.get(_i);
      addparameters(coperationx, cvariablexparameters);
     } } 


 public void subtractparameters(COperation coperationx, List parametersx)
  { for (int _i = 0; _i < parametersx.size(); _i++)
    { CVariable cvariablexparameters = (CVariable) parametersx.get(_i);
      removeparameters(coperationx, cvariablexparameters);
     } } 


  public void setdefiners(COperation coperationx, List definersxx) 
  {     coperationx.setdefiners(definersxx);
      }


  public void setdefiners(COperation coperationx, int _ind, CStruct cstruct_x) 
  { coperationx.setdefiners(_ind,cstruct_x); }
  
  public void adddefiners(COperation coperationx, CStruct definersxx) 
  {     coperationx.adddefiners(definersxx);
   }


  public void removedefiners(COperation coperationx, CStruct definersxx) 
  { coperationx.removedefiners(definersxx);
    }


 public void uniondefiners(COperation coperationx, List definersx)
  { for (int _i = 0; _i < definersx.size(); _i++)
    { CStruct cstructxdefiners = (CStruct) definersx.get(_i);
      adddefiners(coperationx, cstructxdefiners);
     } } 


 public void subtractdefiners(COperation coperationx, List definersx)
  { for (int _i = 0; _i < definersx.size(); _i++)
    { CStruct cstructxdefiners = (CStruct) definersx.get(_i);
      removedefiners(coperationx, cstructxdefiners);
     } } 


  public void setreturnType(COperation coperationx, CType returnTypexx) 
  {   if (coperationx.getreturnType() == returnTypexx) { return; }
    coperationx.setreturnType(returnTypexx);
      }


  public void setelementType(COperation coperationx, CType elementTypexx) 
  {   if (coperationx.getelementType() == elementTypexx) { return; }
    coperationx.setelementType(elementTypexx);
      }


public void setname(CVariable cvariablex, String name_x) 
  { cvariablex.setname(name_x);
    }


public void setkind(CVariable cvariablex, String kind_x) 
  { cvariablex.setkind(kind_x);
    }


public void setinitialisation(CVariable cvariablex, String initialisation_x) 
  { cvariablex.setinitialisation(initialisation_x);
    }


  public void settype(CVariable cvariablex, CType typexx) 
  {   if (cvariablex.gettype() == typexx) { return; }
    cvariablex.settype(typexx);
      }


  public void setoperations(CProgram cprogramx, List operationsxx) 
  {   List _oldoperationsxx = cprogramx.getoperations();
  for (int _i = 0; _i < operationsxx.size(); _i++)
  { COperation _xx = (COperation) operationsxx.get(_i);
    if (_oldoperationsxx.contains(_xx)) { }
    else { CProgram.removeAlloperations(cprograms, _xx); }
  }
    cprogramx.setoperations(operationsxx);
      }


  public void addoperations(CProgram cprogramx, COperation operationsxx) 
  { if (cprogramx.getoperations().contains(operationsxx)) { return; }
    CProgram.removeAlloperations(cprograms,operationsxx);
    cprogramx.addoperations(operationsxx);
   }


  public void removeoperations(CProgram cprogramx, COperation operationsxx) 
  { cprogramx.removeoperations(operationsxx);
    }


 public void unionoperations(CProgram cprogramx, List operationsx)
  { for (int _i = 0; _i < operationsx.size(); _i++)
    { COperation coperationxoperations = (COperation) operationsx.get(_i);
      addoperations(cprogramx, coperationxoperations);
     } } 


 public void subtractoperations(CProgram cprogramx, List operationsx)
  { for (int _i = 0; _i < operationsx.size(); _i++)
    { COperation coperationxoperations = (COperation) operationsx.get(_i);
      removeoperations(cprogramx, coperationxoperations);
     } } 


  public void setvariables(CProgram cprogramx, List variablesxx) 
  {   List _oldvariablesxx = cprogramx.getvariables();
  for (int _i = 0; _i < variablesxx.size(); _i++)
  { CVariable _xx = (CVariable) variablesxx.get(_i);
    if (_oldvariablesxx.contains(_xx)) { }
    else { CProgram.removeAllvariables(cprograms, _xx); }
  }
    cprogramx.setvariables(variablesxx);
      }


  public void addvariables(CProgram cprogramx, CVariable variablesxx) 
  { if (cprogramx.getvariables().contains(variablesxx)) { return; }
    CProgram.removeAllvariables(cprograms,variablesxx);
    cprogramx.addvariables(variablesxx);
   }


  public void removevariables(CProgram cprogramx, CVariable variablesxx) 
  { cprogramx.removevariables(variablesxx);
    }


 public void unionvariables(CProgram cprogramx, List variablesx)
  { for (int _i = 0; _i < variablesx.size(); _i++)
    { CVariable cvariablexvariables = (CVariable) variablesx.get(_i);
      addvariables(cprogramx, cvariablexvariables);
     } } 


 public void subtractvariables(CProgram cprogramx, List variablesx)
  { for (int _i = 0; _i < variablesx.size(); _i++)
    { CVariable cvariablexvariables = (CVariable) variablesx.get(_i);
      removevariables(cprogramx, cvariablexvariables);
     } } 


  public void setstructs(CProgram cprogramx, List structsxx) 
  {   List _oldstructsxx = cprogramx.getstructs();
  for (int _i = 0; _i < structsxx.size(); _i++)
  { CStruct _xx = (CStruct) structsxx.get(_i);
    if (_oldstructsxx.contains(_xx)) { }
    else { CProgram.removeAllstructs(cprograms, _xx); }
  }
    cprogramx.setstructs(structsxx);
      }


  public void addstructs(CProgram cprogramx, CStruct structsxx) 
  { if (cprogramx.getstructs().contains(structsxx)) { return; }
    CProgram.removeAllstructs(cprograms,structsxx);
    cprogramx.addstructs(structsxx);
   }


  public void removestructs(CProgram cprogramx, CStruct structsxx) 
  { cprogramx.removestructs(structsxx);
    }


 public void unionstructs(CProgram cprogramx, List structsx)
  { for (int _i = 0; _i < structsx.size(); _i++)
    { CStruct cstructxstructs = (CStruct) structsx.get(_i);
      addstructs(cprogramx, cstructxstructs);
     } } 


 public void subtractstructs(CProgram cprogramx, List structsx)
  { for (int _i = 0; _i < structsx.size(); _i++)
    { CStruct cstructxstructs = (CStruct) structsx.get(_i);
      removestructs(cprogramx, cstructxstructs);
     } } 


public void setname(CPrimitiveType cprimitivetypex, String name_x) 
  { cprimitivetypex.setname(name_x);
    }


public void setduplicates(CArrayType carraytypex, boolean duplicates_x) 
  { carraytypex.setduplicates(duplicates_x);
    }


  public void setcomponentType(CArrayType carraytypex, CType componentTypexx) 
  {   if (carraytypex.getcomponentType() == componentTypexx) { return; }
    carraytypex.setcomponentType(componentTypexx);
      }


  public void setpointsTo(CPointerType cpointertypex, CType pointsToxx) 
  {   if (cpointertypex.getpointsTo() == pointsToxx) { return; }
    cpointertypex.setpointsTo(pointsToxx);
      }


  public void setdomainType(CFunctionPointerType cfunctionpointertypex, CType domainTypexx) 
  {   if (cfunctionpointertypex.getdomainType() == domainTypexx) { return; }
    cfunctionpointertypex.setdomainType(domainTypexx);
      }


  public void setrangeType(CFunctionPointerType cfunctionpointertypex, CType rangeTypexx) 
  {   if (cfunctionpointertypex.getrangeType() == rangeTypexx) { return; }
    cfunctionpointertypex.setrangeType(rangeTypexx);
      }


public void setname(CEnumeration cenumerationx, String name_x) 
  { cenumerationx.setname(name_x);
    }


  public void setownedLiteral(CEnumeration cenumerationx, List ownedLiteralxx) 
  {   List _oldownedLiteralxx = cenumerationx.getownedLiteral();
  for (int _i = 0; _i < ownedLiteralxx.size(); _i++)
  { CEnumerationLiteral _xx = (CEnumerationLiteral) ownedLiteralxx.get(_i);
    if (_oldownedLiteralxx.contains(_xx)) { }
    else { CEnumeration.removeAllownedLiteral(cenumerations, _xx); }
  }
    cenumerationx.setownedLiteral(ownedLiteralxx);
      }


  public void setownedLiteral(CEnumeration cenumerationx, int _ind, CEnumerationLiteral cenumerationliteral_x) 
  { cenumerationx.setownedLiteral(_ind,cenumerationliteral_x); }
  
  public void addownedLiteral(CEnumeration cenumerationx, CEnumerationLiteral ownedLiteralxx) 
  {   CEnumeration.removeAllownedLiteral(cenumerations,ownedLiteralxx);
    cenumerationx.addownedLiteral(ownedLiteralxx);
   }


  public void removeownedLiteral(CEnumeration cenumerationx, CEnumerationLiteral ownedLiteralxx) 
  { cenumerationx.removeownedLiteral(ownedLiteralxx);
    }


 public void unionownedLiteral(CEnumeration cenumerationx, List ownedLiteralx)
  { for (int _i = 0; _i < ownedLiteralx.size(); _i++)
    { CEnumerationLiteral cenumerationliteralxownedLiteral = (CEnumerationLiteral) ownedLiteralx.get(_i);
      addownedLiteral(cenumerationx, cenumerationliteralxownedLiteral);
     } } 


 public void subtractownedLiteral(CEnumeration cenumerationx, List ownedLiteralx)
  { for (int _i = 0; _i < ownedLiteralx.size(); _i++)
    { CEnumerationLiteral cenumerationliteralxownedLiteral = (CEnumerationLiteral) ownedLiteralx.get(_i);
      removeownedLiteral(cenumerationx, cenumerationliteralxownedLiteral);
     } } 


public void setname(CEnumerationLiteral cenumerationliteralx, String name_x) 
  { cenumerationliteralx.setname(name_x);
    }


public void setoperator(BinaryExpression binaryexpressionx, String operator_x) 
  { binaryexpressionx.setoperator(operator_x);
    }


public void setvariable(BinaryExpression binaryexpressionx, String variable_x) 
  { binaryexpressionx.setvariable(variable_x);
    }


  public void setleft(BinaryExpression binaryexpressionx, Expression leftxx) 
  {   if (binaryexpressionx.getleft() == leftxx) { return; }
    binaryexpressionx.setleft(leftxx);
      }


  public void setright(BinaryExpression binaryexpressionx, Expression rightxx) 
  {   if (binaryexpressionx.getright() == rightxx) { return; }
    binaryexpressionx.setright(rightxx);
      }


  public void settest(ConditionalExpression conditionalexpressionx, Expression testxx) 
  {   if (conditionalexpressionx.gettest() == testxx) { return; }
    conditionalexpressionx.settest(testxx);
      }


  public void setifExp(ConditionalExpression conditionalexpressionx, Expression ifExpxx) 
  {   if (conditionalexpressionx.getifExp() == ifExpxx) { return; }
    conditionalexpressionx.setifExp(ifExpxx);
      }


  public void setelseExp(ConditionalExpression conditionalexpressionx, Expression elseExpxx) 
  {   if (conditionalexpressionx.getelseExp() == elseExpxx) { return; }
    conditionalexpressionx.setelseExp(elseExpxx);
      }


public void setoperator(UnaryExpression unaryexpressionx, String operator_x) 
  { unaryexpressionx.setoperator(operator_x);
    }


public void setvariable(UnaryExpression unaryexpressionx, String variable_x) 
  { unaryexpressionx.setvariable(variable_x);
    }


  public void setargument(UnaryExpression unaryexpressionx, Expression argumentxx) 
  {   if (unaryexpressionx.getargument() == argumentxx) { return; }
    for (int _q = 0; _q < unaryexpressions.size(); _q++)
    { UnaryExpression _q_E1x = (UnaryExpression) unaryexpressions.get(_q);
      if (_q_E1x.getargument() == argumentxx)
      { _q_E1x.setargument(null); }
    }
    unaryexpressionx.setargument(argumentxx);
      }


public void setisOrdered(CollectionExpression collectionexpressionx, boolean isOrdered_x) 
  { collectionexpressionx.setisOrdered(isOrdered_x);
    }


  public void setelements(CollectionExpression collectionexpressionx, List elementsxx) 
  {     collectionexpressionx.setelements(elementsxx);
      }


  public void addelements(CollectionExpression collectionexpressionx, Expression elementsxx) 
  { if (collectionexpressionx.getelements().contains(elementsxx)) { return; }
      collectionexpressionx.addelements(elementsxx);
   }


  public void removeelements(CollectionExpression collectionexpressionx, Expression elementsxx) 
  { collectionexpressionx.removeelements(elementsxx);
    }


 public void unionelements(CollectionExpression collectionexpressionx, List elementsx)
  { for (int _i = 0; _i < elementsx.size(); _i++)
    { Expression expressionxelements = (Expression) elementsx.get(_i);
      addelements(collectionexpressionx, expressionxelements);
     } } 


 public void subtractelements(CollectionExpression collectionexpressionx, List elementsx)
  { for (int _i = 0; _i < elementsx.size(); _i++)
    { Expression expressionxelements = (Expression) elementsx.get(_i);
      removeelements(collectionexpressionx, expressionxelements);
     } } 


public void setdata(BasicExpression basicexpressionx, String data_x) 
  { basicexpressionx.setdata(data_x);
    }


public void setprestate(BasicExpression basicexpressionx, boolean prestate_x) 
  { basicexpressionx.setprestate(prestate_x);
    }


  public void setparameters(BasicExpression basicexpressionx, List parametersxx) 
  {     basicexpressionx.setparameters(parametersxx);
      }


  public void setparameters(BasicExpression basicexpressionx, int _ind, Expression expression_x) 
  { basicexpressionx.setparameters(_ind,expression_x); }
  
  public void addparameters(BasicExpression basicexpressionx, Expression parametersxx) 
  {     basicexpressionx.addparameters(parametersxx);
   }


  public void removeparameters(BasicExpression basicexpressionx, Expression parametersxx) 
  { basicexpressionx.removeparameters(parametersxx);
    }


 public void unionparameters(BasicExpression basicexpressionx, List parametersx)
  { for (int _i = 0; _i < parametersx.size(); _i++)
    { Expression expressionxparameters = (Expression) parametersx.get(_i);
      addparameters(basicexpressionx, expressionxparameters);
     } } 


 public void subtractparameters(BasicExpression basicexpressionx, List parametersx)
  { for (int _i = 0; _i < parametersx.size(); _i++)
    { Expression expressionxparameters = (Expression) parametersx.get(_i);
      removeparameters(basicexpressionx, expressionxparameters);
     } } 


  public void setreferredProperty(BasicExpression basicexpressionx, List referredPropertyxx) 
  {   if (referredPropertyxx.size() > 1) { return; }
    basicexpressionx.setreferredProperty(referredPropertyxx);
      }


  public void addreferredProperty(BasicExpression basicexpressionx, Property referredPropertyxx) 
  { if (basicexpressionx.getreferredProperty().contains(referredPropertyxx)) { return; }
      if (basicexpressionx.getreferredProperty().size() > 0)
    { Property property_xx = (Property) basicexpressionx.getreferredProperty().get(0);
      basicexpressionx.removereferredProperty(property_xx);
      }
      basicexpressionx.addreferredProperty(referredPropertyxx);
   }


  public void removereferredProperty(BasicExpression basicexpressionx, Property referredPropertyxx) 
  { basicexpressionx.removereferredProperty(referredPropertyxx);
    }


 public void unionreferredProperty(BasicExpression basicexpressionx, List referredPropertyx)
  { for (int _i = 0; _i < referredPropertyx.size(); _i++)
    { Property propertyxreferredProperty = (Property) referredPropertyx.get(_i);
      addreferredProperty(basicexpressionx, propertyxreferredProperty);
     } } 


 public void subtractreferredProperty(BasicExpression basicexpressionx, List referredPropertyx)
  { for (int _i = 0; _i < referredPropertyx.size(); _i++)
    { Property propertyxreferredProperty = (Property) referredPropertyx.get(_i);
      removereferredProperty(basicexpressionx, propertyxreferredProperty);
     } } 


  public void setcontext(BasicExpression basicexpressionx, List contextxx) 
  {     basicexpressionx.setcontext(contextxx);
      }


  public void addcontext(BasicExpression basicexpressionx, Entity contextxx) 
  { if (basicexpressionx.getcontext().contains(contextxx)) { return; }
      basicexpressionx.addcontext(contextxx);
   }


  public void removecontext(BasicExpression basicexpressionx, Entity contextxx) 
  { basicexpressionx.removecontext(contextxx);
    }


 public void unioncontext(BasicExpression basicexpressionx, List contextx)
  { for (int _i = 0; _i < contextx.size(); _i++)
    { Entity entityxcontext = (Entity) contextx.get(_i);
      addcontext(basicexpressionx, entityxcontext);
     } } 


 public void subtractcontext(BasicExpression basicexpressionx, List contextx)
  { for (int _i = 0; _i < contextx.size(); _i++)
    { Entity entityxcontext = (Entity) contextx.get(_i);
      removecontext(basicexpressionx, entityxcontext);
     } } 


  public void setarrayIndex(BasicExpression basicexpressionx, List arrayIndexxx) 
  {   if (arrayIndexxx.size() > 1) { return; }
  List _oldarrayIndexxx = basicexpressionx.getarrayIndex();
  for (int _i = 0; _i < arrayIndexxx.size(); _i++)
  { Expression _xx = (Expression) arrayIndexxx.get(_i);
    if (_oldarrayIndexxx.contains(_xx)) { }
    else { BasicExpression.removeAllarrayIndex(basicexpressions, _xx); }
  }
    basicexpressionx.setarrayIndex(arrayIndexxx);
      }


  public void addarrayIndex(BasicExpression basicexpressionx, Expression arrayIndexxx) 
  { if (basicexpressionx.getarrayIndex().contains(arrayIndexxx)) { return; }
      if (basicexpressionx.getarrayIndex().size() > 0)
    { Expression expression_xx = (Expression) basicexpressionx.getarrayIndex().get(0);
      basicexpressionx.removearrayIndex(expression_xx);
      }
    BasicExpression.removeAllarrayIndex(basicexpressions,arrayIndexxx);
    basicexpressionx.addarrayIndex(arrayIndexxx);
   }


  public void removearrayIndex(BasicExpression basicexpressionx, Expression arrayIndexxx) 
  { basicexpressionx.removearrayIndex(arrayIndexxx);
    }


 public void unionarrayIndex(BasicExpression basicexpressionx, List arrayIndexx)
  { for (int _i = 0; _i < arrayIndexx.size(); _i++)
    { Expression expressionxarrayIndex = (Expression) arrayIndexx.get(_i);
      addarrayIndex(basicexpressionx, expressionxarrayIndex);
     } } 


 public void subtractarrayIndex(BasicExpression basicexpressionx, List arrayIndexx)
  { for (int _i = 0; _i < arrayIndexx.size(); _i++)
    { Expression expressionxarrayIndex = (Expression) arrayIndexx.get(_i);
      removearrayIndex(basicexpressionx, expressionxarrayIndex);
     } } 


  public void setobjectRef(BasicExpression basicexpressionx, List objectRefxx) 
  {   if (objectRefxx.size() > 1) { return; }
  List _oldobjectRefxx = basicexpressionx.getobjectRef();
  for (int _i = 0; _i < objectRefxx.size(); _i++)
  { Expression _xx = (Expression) objectRefxx.get(_i);
    if (_oldobjectRefxx.contains(_xx)) { }
    else { BasicExpression.removeAllobjectRef(basicexpressions, _xx); }
  }
    basicexpressionx.setobjectRef(objectRefxx);
      }


  public void addobjectRef(BasicExpression basicexpressionx, Expression objectRefxx) 
  { if (basicexpressionx.getobjectRef().contains(objectRefxx)) { return; }
      if (basicexpressionx.getobjectRef().size() > 0)
    { Expression expression_xx = (Expression) basicexpressionx.getobjectRef().get(0);
      basicexpressionx.removeobjectRef(expression_xx);
      }
    BasicExpression.removeAllobjectRef(basicexpressions,objectRefxx);
    basicexpressionx.addobjectRef(objectRefxx);
   }


  public void removeobjectRef(BasicExpression basicexpressionx, Expression objectRefxx) 
  { basicexpressionx.removeobjectRef(objectRefxx);
    }


 public void unionobjectRef(BasicExpression basicexpressionx, List objectRefx)
  { for (int _i = 0; _i < objectRefx.size(); _i++)
    { Expression expressionxobjectRef = (Expression) objectRefx.get(_i);
      addobjectRef(basicexpressionx, expressionxobjectRef);
     } } 


 public void subtractobjectRef(BasicExpression basicexpressionx, List objectRefx)
  { for (int _i = 0; _i < objectRefx.size(); _i++)
    { Expression expressionxobjectRef = (Expression) objectRefx.get(_i);
      removeobjectRef(basicexpressionx, expressionxobjectRef);
     } } 


public void setstatId(Statement statementx, String statId_x) 
  { if (statementstatIdindex.get(statId_x) != null) { return; }
  statementstatIdindex.remove(statementx.getstatId());
  statementx.setstatId(statId_x);
  statementstatIdindex.put(statId_x,statementx);
    }


  public void setreturnValue(ReturnStatement returnstatementx, List returnValuexx) 
  {   if (returnValuexx.size() > 1) { return; }
  List _oldreturnValuexx = returnstatementx.getreturnValue();
  for (int _i = 0; _i < returnValuexx.size(); _i++)
  { Expression _xx = (Expression) returnValuexx.get(_i);
    if (_oldreturnValuexx.contains(_xx)) { }
    else { ReturnStatement.removeAllreturnValue(returnstatements, _xx); }
  }
    returnstatementx.setreturnValue(returnValuexx);
      }


  public void addreturnValue(ReturnStatement returnstatementx, Expression returnValuexx) 
  { if (returnstatementx.getreturnValue().contains(returnValuexx)) { return; }
      if (returnstatementx.getreturnValue().size() > 0)
    { Expression expression_xx = (Expression) returnstatementx.getreturnValue().get(0);
      returnstatementx.removereturnValue(expression_xx);
      }
    ReturnStatement.removeAllreturnValue(returnstatements,returnValuexx);
    returnstatementx.addreturnValue(returnValuexx);
   }


  public void removereturnValue(ReturnStatement returnstatementx, Expression returnValuexx) 
  { returnstatementx.removereturnValue(returnValuexx);
    }


 public void unionreturnValue(ReturnStatement returnstatementx, List returnValuex)
  { for (int _i = 0; _i < returnValuex.size(); _i++)
    { Expression expressionxreturnValue = (Expression) returnValuex.get(_i);
      addreturnValue(returnstatementx, expressionxreturnValue);
     } } 


 public void subtractreturnValue(ReturnStatement returnstatementx, List returnValuex)
  { for (int _i = 0; _i < returnValuex.size(); _i++)
    { Expression expressionxreturnValue = (Expression) returnValuex.get(_i);
      removereturnValue(returnstatementx, expressionxreturnValue);
     } } 


public void setassignsTo(OperationCallStatement operationcallstatementx, String assignsTo_x) 
  { operationcallstatementx.setassignsTo(assignsTo_x);
    }


  public void setcallExp(OperationCallStatement operationcallstatementx, Expression callExpxx) 
  {   if (operationcallstatementx.getcallExp() == callExpxx) { return; }
    for (int _q = 0; _q < operationcallstatements.size(); _q++)
    { OperationCallStatement _q_E1x = (OperationCallStatement) operationcallstatements.get(_q);
      if (_q_E1x.getcallExp() == callExpxx)
      { _q_E1x.setcallExp(null); }
    }
    operationcallstatementx.setcallExp(callExpxx);
      }


public void setassignsTo(ImplicitCallStatement implicitcallstatementx, String assignsTo_x) 
  { implicitcallstatementx.setassignsTo(assignsTo_x);
    }


  public void setcallExp(ImplicitCallStatement implicitcallstatementx, Expression callExpxx) 
  {   if (implicitcallstatementx.getcallExp() == callExpxx) { return; }
    for (int _q = 0; _q < implicitcallstatements.size(); _q++)
    { ImplicitCallStatement _q_E1x = (ImplicitCallStatement) implicitcallstatements.get(_q);
      if (_q_E1x.getcallExp() == callExpxx)
      { _q_E1x.setcallExp(null); }
    }
    implicitcallstatementx.setcallExp(callExpxx);
      }


  public void settest(LoopStatement loopstatementx, Expression testxx) 
  {   if (loopstatementx.gettest() == testxx) { return; }
    for (int _q = 0; _q < loopstatements.size(); _q++)
    { LoopStatement _q_E1x = (LoopStatement) loopstatements.get(_q);
      if (_q_E1x.gettest() == testxx)
      { _q_E1x.settest(null); }
    }
    loopstatementx.settest(testxx);
      }


  public void setbody(LoopStatement loopstatementx, Statement bodyxx) 
  {   if (loopstatementx.getbody() == bodyxx) { return; }
    for (int _q = 0; _q < loopstatements.size(); _q++)
    { LoopStatement _q_E1x = (LoopStatement) loopstatements.get(_q);
      if (_q_E1x.getbody() == bodyxx)
      { _q_E1x.setbody(null); }
    }
    loopstatementx.setbody(bodyxx);
      }


  public void setloopRange(BoundedLoopStatement boundedloopstatementx, Expression loopRangexx) 
  {   if (boundedloopstatementx.getloopRange() == loopRangexx) { return; }
    for (int _q = 0; _q < boundedloopstatements.size(); _q++)
    { BoundedLoopStatement _q_E1x = (BoundedLoopStatement) boundedloopstatements.get(_q);
      if (_q_E1x.getloopRange() == loopRangexx)
      { _q_E1x.setloopRange(null); }
    }
    boundedloopstatementx.setloopRange(loopRangexx);
      }


  public void setloopVar(BoundedLoopStatement boundedloopstatementx, Expression loopVarxx) 
  {   if (boundedloopstatementx.getloopVar() == loopVarxx) { return; }
    for (int _q = 0; _q < boundedloopstatements.size(); _q++)
    { BoundedLoopStatement _q_E1x = (BoundedLoopStatement) boundedloopstatements.get(_q);
      if (_q_E1x.getloopVar() == loopVarxx)
      { _q_E1x.setloopVar(null); }
    }
    boundedloopstatementx.setloopVar(loopVarxx);
      }


  public void settype(AssignStatement assignstatementx, List typexx) 
  {   if (typexx.size() > 1) { return; }
    assignstatementx.settype(typexx);
      }


  public void addtype(AssignStatement assignstatementx, Type typexx) 
  { if (assignstatementx.gettype().contains(typexx)) { return; }
      if (assignstatementx.gettype().size() > 0)
    { Type type_xx = (Type) assignstatementx.gettype().get(0);
      assignstatementx.removetype(type_xx);
      }
      assignstatementx.addtype(typexx);
   }


  public void removetype(AssignStatement assignstatementx, Type typexx) 
  { assignstatementx.removetype(typexx);
    }


 public void uniontype(AssignStatement assignstatementx, List typex)
  { for (int _i = 0; _i < typex.size(); _i++)
    { Type typextype = (Type) typex.get(_i);
      addtype(assignstatementx, typextype);
     } } 


 public void subtracttype(AssignStatement assignstatementx, List typex)
  { for (int _i = 0; _i < typex.size(); _i++)
    { Type typextype = (Type) typex.get(_i);
      removetype(assignstatementx, typextype);
     } } 


  public void setleft(AssignStatement assignstatementx, Expression leftxx) 
  {   if (assignstatementx.getleft() == leftxx) { return; }
    for (int _q = 0; _q < assignstatements.size(); _q++)
    { AssignStatement _q_E1x = (AssignStatement) assignstatements.get(_q);
      if (_q_E1x.getleft() == leftxx)
      { _q_E1x.setleft(null); }
    }
    assignstatementx.setleft(leftxx);
      }


  public void setright(AssignStatement assignstatementx, Expression rightxx) 
  {   if (assignstatementx.getright() == rightxx) { return; }
    for (int _q = 0; _q < assignstatements.size(); _q++)
    { AssignStatement _q_E1x = (AssignStatement) assignstatements.get(_q);
      if (_q_E1x.getright() == rightxx)
      { _q_E1x.setright(null); }
    }
    assignstatementx.setright(rightxx);
      }


public void setkind(SequenceStatement sequencestatementx, int kind_x) 
  { sequencestatementx.setkind(kind_x);
    }


  public void setstatements(SequenceStatement sequencestatementx, List statementsxx) 
  {   List _oldstatementsxx = sequencestatementx.getstatements();
  for (int _i = 0; _i < statementsxx.size(); _i++)
  { Statement _xx = (Statement) statementsxx.get(_i);
    if (_oldstatementsxx.contains(_xx)) { }
    else { SequenceStatement.removeAllstatements(sequencestatements, _xx); }
  }
    sequencestatementx.setstatements(statementsxx);
      }


  public void setstatements(SequenceStatement sequencestatementx, int _ind, Statement statement_x) 
  { sequencestatementx.setstatements(_ind,statement_x); }
  
  public void addstatements(SequenceStatement sequencestatementx, Statement statementsxx) 
  {   SequenceStatement.removeAllstatements(sequencestatements,statementsxx);
    sequencestatementx.addstatements(statementsxx);
   }


  public void removestatements(SequenceStatement sequencestatementx, Statement statementsxx) 
  { sequencestatementx.removestatements(statementsxx);
    }


 public void unionstatements(SequenceStatement sequencestatementx, List statementsx)
  { for (int _i = 0; _i < statementsx.size(); _i++)
    { Statement statementxstatements = (Statement) statementsx.get(_i);
      addstatements(sequencestatementx, statementxstatements);
     } } 


 public void subtractstatements(SequenceStatement sequencestatementx, List statementsx)
  { for (int _i = 0; _i < statementsx.size(); _i++)
    { Statement statementxstatements = (Statement) statementsx.get(_i);
      removestatements(sequencestatementx, statementxstatements);
     } } 


  public void settest(ConditionalStatement conditionalstatementx, Expression testxx) 
  {   if (conditionalstatementx.gettest() == testxx) { return; }
    for (int _q = 0; _q < conditionalstatements.size(); _q++)
    { ConditionalStatement _q_E1x = (ConditionalStatement) conditionalstatements.get(_q);
      if (_q_E1x.gettest() == testxx)
      { _q_E1x.settest(null); }
    }
    conditionalstatementx.settest(testxx);
      }


  public void setifPart(ConditionalStatement conditionalstatementx, Statement ifPartxx) 
  {   if (conditionalstatementx.getifPart() == ifPartxx) { return; }
    for (int _q = 0; _q < conditionalstatements.size(); _q++)
    { ConditionalStatement _q_E1x = (ConditionalStatement) conditionalstatements.get(_q);
      if (_q_E1x.getifPart() == ifPartxx)
      { _q_E1x.setifPart(null); }
    }
    conditionalstatementx.setifPart(ifPartxx);
      }


  public void setelsePart(ConditionalStatement conditionalstatementx, List elsePartxx) 
  {   if (elsePartxx.size() > 1) { return; }
  List _oldelsePartxx = conditionalstatementx.getelsePart();
  for (int _i = 0; _i < elsePartxx.size(); _i++)
  { Statement _xx = (Statement) elsePartxx.get(_i);
    if (_oldelsePartxx.contains(_xx)) { }
    else { ConditionalStatement.removeAllelsePart(conditionalstatements, _xx); }
  }
    conditionalstatementx.setelsePart(elsePartxx);
      }


  public void addelsePart(ConditionalStatement conditionalstatementx, Statement elsePartxx) 
  { if (conditionalstatementx.getelsePart().contains(elsePartxx)) { return; }
      if (conditionalstatementx.getelsePart().size() > 0)
    { Statement statement_xx = (Statement) conditionalstatementx.getelsePart().get(0);
      conditionalstatementx.removeelsePart(statement_xx);
      }
    ConditionalStatement.removeAllelsePart(conditionalstatements,elsePartxx);
    conditionalstatementx.addelsePart(elsePartxx);
   }


  public void removeelsePart(ConditionalStatement conditionalstatementx, Statement elsePartxx) 
  { conditionalstatementx.removeelsePart(elsePartxx);
    }


 public void unionelsePart(ConditionalStatement conditionalstatementx, List elsePartx)
  { for (int _i = 0; _i < elsePartx.size(); _i++)
    { Statement statementxelsePart = (Statement) elsePartx.get(_i);
      addelsePart(conditionalstatementx, statementxelsePart);
     } } 


 public void subtractelsePart(ConditionalStatement conditionalstatementx, List elsePartx)
  { for (int _i = 0; _i < elsePartx.size(); _i++)
    { Statement statementxelsePart = (Statement) elsePartx.get(_i);
      removeelsePart(conditionalstatementx, statementxelsePart);
     } } 


public void setcreatesInstanceOf(CreationStatement creationstatementx, String createsInstanceOf_x) 
  { creationstatementx.setcreatesInstanceOf(createsInstanceOf_x);
    }


public void setassignsTo(CreationStatement creationstatementx, String assignsTo_x) 
  { creationstatementx.setassignsTo(assignsTo_x);
    }


  public void settype(CreationStatement creationstatementx, Type typexx) 
  {   if (creationstatementx.gettype() == typexx) { return; }
    creationstatementx.settype(typexx);
      }


  public void setelementType(CreationStatement creationstatementx, Type elementTypexx) 
  {   if (creationstatementx.getelementType() == elementTypexx) { return; }
    creationstatementx.setelementType(elementTypexx);
      }


  public void setinitialExpression(CreationStatement creationstatementx, List initialExpressionxx) 
  {   if (initialExpressionxx.size() > 1) { return; }
    creationstatementx.setinitialExpression(initialExpressionxx);
      }


  public void addinitialExpression(CreationStatement creationstatementx, Expression initialExpressionxx) 
  { if (creationstatementx.getinitialExpression().contains(initialExpressionxx)) { return; }
      if (creationstatementx.getinitialExpression().size() > 0)
    { Expression expression_xx = (Expression) creationstatementx.getinitialExpression().get(0);
      creationstatementx.removeinitialExpression(expression_xx);
      }
      creationstatementx.addinitialExpression(initialExpressionxx);
   }


  public void removeinitialExpression(CreationStatement creationstatementx, Expression initialExpressionxx) 
  { creationstatementx.removeinitialExpression(initialExpressionxx);
    }


 public void unioninitialExpression(CreationStatement creationstatementx, List initialExpressionx)
  { for (int _i = 0; _i < initialExpressionx.size(); _i++)
    { Expression expressionxinitialExpression = (Expression) initialExpressionx.get(_i);
      addinitialExpression(creationstatementx, expressionxinitialExpression);
     } } 


 public void subtractinitialExpression(CreationStatement creationstatementx, List initialExpressionx)
  { for (int _i = 0; _i < initialExpressionx.size(); _i++)
    { Expression expressionxinitialExpression = (Expression) initialExpressionx.get(_i);
      removeinitialExpression(creationstatementx, expressionxinitialExpression);
     } } 


  public void setthrownObject(ErrorStatement errorstatementx, Expression thrownObjectxx) 
  {   if (errorstatementx.getthrownObject() == thrownObjectxx) { return; }
    errorstatementx.setthrownObject(thrownObjectxx);
      }


  public void setcaughtObject(CatchStatement catchstatementx, Expression caughtObjectxx) 
  {   if (catchstatementx.getcaughtObject() == caughtObjectxx) { return; }
    catchstatementx.setcaughtObject(caughtObjectxx);
      }


  public void setaction(CatchStatement catchstatementx, Statement actionxx) 
  {   if (catchstatementx.getaction() == actionxx) { return; }
    catchstatementx.setaction(actionxx);
      }


  public void setbody(FinalStatement finalstatementx, Statement bodyxx) 
  {   if (finalstatementx.getbody() == bodyxx) { return; }
    finalstatementx.setbody(bodyxx);
      }


  public void setcatchClauses(TryStatement trystatementx, List catchClausesxx) 
  {   List _oldcatchClausesxx = trystatementx.getcatchClauses();
  for (int _i = 0; _i < catchClausesxx.size(); _i++)
  { Statement _xx = (Statement) catchClausesxx.get(_i);
    if (_oldcatchClausesxx.contains(_xx)) { }
    else { TryStatement.removeAllcatchClauses(trystatements, _xx); }
  }
    trystatementx.setcatchClauses(catchClausesxx);
      }


  public void setcatchClauses(TryStatement trystatementx, int _ind, Statement statement_x) 
  { trystatementx.setcatchClauses(_ind,statement_x); }
  
  public void addcatchClauses(TryStatement trystatementx, Statement catchClausesxx) 
  {   TryStatement.removeAllcatchClauses(trystatements,catchClausesxx);
    trystatementx.addcatchClauses(catchClausesxx);
   }


  public void removecatchClauses(TryStatement trystatementx, Statement catchClausesxx) 
  { trystatementx.removecatchClauses(catchClausesxx);
    }


 public void unioncatchClauses(TryStatement trystatementx, List catchClausesx)
  { for (int _i = 0; _i < catchClausesx.size(); _i++)
    { Statement statementxcatchClauses = (Statement) catchClausesx.get(_i);
      addcatchClauses(trystatementx, statementxcatchClauses);
     } } 


 public void subtractcatchClauses(TryStatement trystatementx, List catchClausesx)
  { for (int _i = 0; _i < catchClausesx.size(); _i++)
    { Statement statementxcatchClauses = (Statement) catchClausesx.get(_i);
      removecatchClauses(trystatementx, statementxcatchClauses);
     } } 


  public void setbody(TryStatement trystatementx, Statement bodyxx) 
  {   if (trystatementx.getbody() == bodyxx) { return; }
    for (int _q = 0; _q < trystatements.size(); _q++)
    { TryStatement _q_E1x = (TryStatement) trystatements.get(_q);
      if (_q_E1x.getbody() == bodyxx)
      { _q_E1x.setbody(null); }
    }
    trystatementx.setbody(bodyxx);
      }


  public void setendStatement(TryStatement trystatementx, List endStatementxx) 
  {   if (endStatementxx.size() > 1) { return; }
  List _oldendStatementxx = trystatementx.getendStatement();
  for (int _i = 0; _i < endStatementxx.size(); _i++)
  { Statement _xx = (Statement) endStatementxx.get(_i);
    if (_oldendStatementxx.contains(_xx)) { }
    else { TryStatement.removeAllendStatement(trystatements, _xx); }
  }
    trystatementx.setendStatement(endStatementxx);
      }


  public void addendStatement(TryStatement trystatementx, Statement endStatementxx) 
  { if (trystatementx.getendStatement().contains(endStatementxx)) { return; }
      if (trystatementx.getendStatement().size() > 0)
    { Statement statement_xx = (Statement) trystatementx.getendStatement().get(0);
      trystatementx.removeendStatement(statement_xx);
      }
    TryStatement.removeAllendStatement(trystatements,endStatementxx);
    trystatementx.addendStatement(endStatementxx);
   }


  public void removeendStatement(TryStatement trystatementx, Statement endStatementxx) 
  { trystatementx.removeendStatement(endStatementxx);
    }


 public void unionendStatement(TryStatement trystatementx, List endStatementx)
  { for (int _i = 0; _i < endStatementx.size(); _i++)
    { Statement statementxendStatement = (Statement) endStatementx.get(_i);
      addendStatement(trystatementx, statementxendStatement);
     } } 


 public void subtractendStatement(TryStatement trystatementx, List endStatementx)
  { for (int _i = 0; _i < endStatementx.size(); _i++)
    { Statement statementxendStatement = (Statement) endStatementx.get(_i);
      removeendStatement(trystatementx, statementxendStatement);
     } } 


  public void setcondition(AssertStatement assertstatementx, Expression conditionxx) 
  {   if (assertstatementx.getcondition() == conditionxx) { return; }
    assertstatementx.setcondition(conditionxx);
      }


  public void setmessage(AssertStatement assertstatementx, List messagexx) 
  {   if (messagexx.size() > 1) { return; }
    assertstatementx.setmessage(messagexx);
      }


  public void addmessage(AssertStatement assertstatementx, Expression messagexx) 
  { if (assertstatementx.getmessage().contains(messagexx)) { return; }
      if (assertstatementx.getmessage().size() > 0)
    { Expression expression_xx = (Expression) assertstatementx.getmessage().get(0);
      assertstatementx.removemessage(expression_xx);
      }
      assertstatementx.addmessage(messagexx);
   }


  public void removemessage(AssertStatement assertstatementx, Expression messagexx) 
  { assertstatementx.removemessage(messagexx);
    }


 public void unionmessage(AssertStatement assertstatementx, List messagex)
  { for (int _i = 0; _i < messagex.size(); _i++)
    { Expression expressionxmessage = (Expression) messagex.get(_i);
      addmessage(assertstatementx, expressionxmessage);
     } } 


 public void subtractmessage(AssertStatement assertstatementx, List messagex)
  { for (int _i = 0; _i < messagex.size(); _i++)
    { Expression expressionxmessage = (Expression) messagex.get(_i);
      removemessage(assertstatementx, expressionxmessage);
     } } 



  public  List AllTypeisValueType(List typexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < typexs.size(); _i++)
    { Type typex = (Type) typexs.get(_i);
      result.add(new Boolean(typex.isValueType()));
    }
    return result; 
  }

  public void types2c10(Enumeration enumerationx)
  {   enumerationx.types2c10();
   }

  public void types2c1(PrimitiveType primitivetypex)
  {   primitivetypex.types2c1();
   }

  public void types2c2(PrimitiveType primitivetypex)
  {   primitivetypex.types2c2();
   }

  public void types2c3(PrimitiveType primitivetypex)
  {   primitivetypex.types2c3();
   }

  public void types2c4(PrimitiveType primitivetypex)
  {   primitivetypex.types2c4();
   }

  public void types2c5(PrimitiveType primitivetypex)
  {   primitivetypex.types2c5();
   }

  public void types2c6(PrimitiveType primitivetypex)
  {   primitivetypex.types2c6();
   }

  public void types2c7(PrimitiveType primitivetypex)
  {   primitivetypex.types2c7();
   }

  public void types2c8(PrimitiveType primitivetypex)
  {   primitivetypex.types2c8();
   }

  public  List AllEntityinheritedOperations(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.addAll(entityx.inheritedOperations());
    }
    return result; 
  }

  public  List AllEntityisApplicationClass(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(new Boolean(entityx.isApplicationClass()));
    }
    return result; 
  }

  public  List AllEntityallSubclasses(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.addAll(entityx.allSubclasses());
    }
    return result; 
  }

  public void types2c9(Entity entityx)
  {   entityx.types2c9();
   }

  public void program2c1(Entity entityx,Property p)
  {   entityx.program2c1(p);
   }

  public void program2c1outer(Entity entityx)
  {   entityx.program2c1outer();
   }

  public void program2c3(Entity entityx,Operation iop)
  {   entityx.program2c3(iop);
   }

  public void program2c3outer(Entity entityx)
  {   entityx.program2c3outer();
   }

  public void program2c8(Entity entityx,Property p)
  {   entityx.program2c8(p);
   }

  public void program2c8outer(Entity entityx)
  {   entityx.program2c8outer();
   }

  public  List AllPropertymapMultiplicity(List propertyxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < propertyxs.size(); _i++)
    { Property propertyx = (Property) propertyxs.get(_i);
      result.add(propertyx.mapMultiplicity());
    }
    return result; 
  }

  public void program2c5(Generalization generalizationx)
  {   generalizationx.program2c5();
   }

  public void program2c6(Generalization generalizationx)
  {   generalizationx.program2c6();
   }

  public void program2c2(Operation operationx)
  {   operationx.program2c2();
   }

  public void program2c4(Operation operationx)
  {   operationx.program2c4();
   }

  public void printcode34(UseCase usecasex)
  {   usecasex.printcode34();
   }

  public void types2c11(CollectionType collectiontypex)
  {   collectiontypex.types2c11();
   }

  public void types2c12(CollectionType collectiontypex)
  {   collectiontypex.types2c12();
   }

  public void types2c13(CollectionType collectiontypex)
  {   collectiontypex.types2c13();
   }

  public void types2c14(CollectionType collectiontypex)
  {   collectiontypex.types2c14();
   }

  public void types2c15(CollectionType collectiontypex)
  {   collectiontypex.types2c15();
   }

  public void types2c16(CollectionType collectiontypex)
  {   collectiontypex.types2c16();
   }

  public void types2c17(CollectionType collectiontypex)
  {   collectiontypex.types2c17();
   }

  public  List AllCStructtoString(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.toString());
    }
    return result; 
  }

  public  List AllCStructallCMembers(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.addAll(cstructx.allCMembers());
    }
    return result; 
  }

  public  List AllCStructcreateOpDec(List cstructxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.createOpDec(ent));
    }
    return result; 
  }

  public  List AllCStructcreatePKOpDec(List cstructxs,String ent,String key)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.createPKOpDec(ent, key));
    }
    return result; 
  }

  public  List AllCStructconcatenateOpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.concatenateOpDec());
    }
    return result; 
  }

  public  List AllCStructintersectionOpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.intersectionOpDec());
    }
    return result; 
  }

  public  List AllCStructinsertAtOpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.insertAtOpDec());
    }
    return result; 
  }

  public  List AllCStructexists1OpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.exists1OpDec());
    }
    return result; 
  }

  public  List AllCStructisUniqueOpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.isUniqueOpDec());
    }
    return result; 
  }

  public  List AllCStructfrontOpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.frontOpDec());
    }
    return result; 
  }

  public  List AllCStructtailOpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.tailOpDec());
    }
    return result; 
  }

  public  List AllCStructremoveAllOpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.removeAllOpDec());
    }
    return result; 
  }

  public  List AllCStructasSetOpDec(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(cstructx.asSetOpDec());
    }
    return result; 
  }

  public  List AllCStructisApplicationClass(List cstructxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cstructxs.size(); _i++)
    { CStruct cstructx = (CStruct) cstructxs.get(_i);
      result.add(new Boolean(cstructx.isApplicationClass()));
    }
    return result; 
  }

  public void printcode1(CStruct cstructx)
  {   cstructx.printcode1();
   }

  public void printcode2(CStruct cstructx)
  {   cstructx.printcode2();
   }

  public void printcode4(CStruct cstructx,CMember f)
  {   cstructx.printcode4(f);
   }

  public void printcode4outer(CStruct cstructx)
  {   cstructx.printcode4outer();
   }

  public void printcode5(CStruct cstructx,CMember f)
  {   cstructx.printcode5(f);
   }

  public void printcode5outer(CStruct cstructx)
  {   cstructx.printcode5outer();
   }

  public void printcode6(CStruct cstructx,CMember f)
  {   cstructx.printcode6(f);
   }

  public void printcode6outer(CStruct cstructx)
  {   cstructx.printcode6outer();
   }

  public void printcode7(CStruct cstructx,CMember f)
  {   cstructx.printcode7(f);
   }

  public void printcode7outer(CStruct cstructx)
  {   cstructx.printcode7outer();
   }

  public void printcode8(CStruct cstructx,CMember f)
  {   cstructx.printcode8(f);
   }

  public void printcode8outer(CStruct cstructx)
  {   cstructx.printcode8outer();
   }

  public void printcode9(CStruct cstructx,CMember f)
  {   cstructx.printcode9(f);
   }

  public void printcode9outer(CStruct cstructx)
  {   cstructx.printcode9outer();
   }

  public void printcode10(CStruct cstructx,CMember key)
  {   cstructx.printcode10(key);
   }

  public void printcode10outer(CStruct cstructx)
  {   cstructx.printcode10outer();
   }

  public void printcode11(CStruct cstructx)
  {   cstructx.printcode11();
   }

  public void printcode12(CStruct cstructx,CMember key)
  {   cstructx.printcode12(key);
   }

  public void printcode12outer(CStruct cstructx)
  {   cstructx.printcode12outer();
   }

  public void printcode13(CStruct cstructx)
  {   cstructx.printcode13();
   }

  public void printcode14(CStruct cstructx)
  {   cstructx.printcode14();
   }

  public void printcode15(CStruct cstructx)
  {   cstructx.printcode15();
   }

  public void printcode16(CStruct cstructx)
  {   cstructx.printcode16();
   }

  public void printcode17(CStruct cstructx)
  {   cstructx.printcode17();
   }

  public void printcode18(CStruct cstructx)
  {   cstructx.printcode18();
   }

  public void printcode19(CStruct cstructx)
  {   cstructx.printcode19();
   }

  public void printcode20(CStruct cstructx)
  {   cstructx.printcode20();
   }

  public void printcode21(CStruct cstructx)
  {   cstructx.printcode21();
   }

  public void printcode22(CStruct cstructx)
  {   cstructx.printcode22();
   }

  public void printcode23(CStruct cstructx)
  {   cstructx.printcode23();
   }

  public void printcode24(CStruct cstructx)
  {   cstructx.printcode24();
   }

  public void printcode25(CStruct cstructx)
  {   cstructx.printcode25();
   }

  public void printcode26(CStruct cstructx)
  {   cstructx.printcode26();
   }

  public void printcode27(CStruct cstructx)
  {   cstructx.printcode27();
   }

  public void printcode28(CStruct cstructx)
  {   cstructx.printcode28();
   }

  public void printcode29(CStruct cstructx)
  {   cstructx.printcode29();
   }

  public void printcode30(CStruct cstructx)
  {   cstructx.printcode30();
   }

  public void printcode31(CStruct cstructx)
  {   cstructx.printcode31();
   }

  public void printcode32(CStruct cstructx)
  {   cstructx.printcode32();
   }

  public  List AllCMembertoString(List cmemberxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.toString());
    }
    return result; 
  }

  public  List AllCMemberinheritedCMembers(List cmemberxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.addAll(cmemberx.inheritedCMembers());
    }
    return result; 
  }

  public  List AllCMembergetterOpDec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.getterOpDec(ent));
    }
    return result; 
  }

  public  List AllCMemberinheritedGetterOpDec(List cmemberxs,String ent,String sup)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.inheritedGetterOpDec(ent, sup));
    }
    return result; 
  }

  public  List AllCMemberancestorGetterOpsDec(List cmemberxs,String ent,String sup)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.ancestorGetterOpsDec(ent, sup));
    }
    return result; 
  }

  public  List AllCMemberinheritedGetterOpsDec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.inheritedGetterOpsDec(ent));
    }
    return result; 
  }

  public  List AllCMembersetterOpDec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.setterOpDec(ent));
    }
    return result; 
  }

  public  List AllCMemberinheritedSetterOpDec(List cmemberxs,String ent,String sup)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.inheritedSetterOpDec(ent, sup));
    }
    return result; 
  }

  public  List AllCMemberancestorSetterOpsDec(List cmemberxs,String ent,String sup)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.ancestorSetterOpsDec(ent, sup));
    }
    return result; 
  }

  public  List AllCMemberinheritedSetterOpsDec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.inheritedSetterOpsDec(ent));
    }
    return result; 
  }

  public  List AllCMembergetAllOpDec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.getAllOpDec(ent));
    }
    return result; 
  }

  public  List AllCMembergetAllOp1Dec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.getAllOp1Dec(ent));
    }
    return result; 
  }

  public  List AllCMemberinheritedAllOpDec(List cmemberxs,String ent,String sup)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.inheritedAllOpDec(ent, sup));
    }
    return result; 
  }

  public  List AllCMemberancestorAllOpsDec(List cmemberxs,String ent,String sup)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.ancestorAllOpsDec(ent, sup));
    }
    return result; 
  }

  public  List AllCMemberinheritedAllOpsDec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.inheritedAllOpsDec(ent));
    }
    return result; 
  }

  public  List AllCMembergetPKOpDec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.getPKOpDec(ent));
    }
    return result; 
  }

  public  List AllCMembergetPKsOpDec(List cmemberxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.getPKsOpDec(ent));
    }
    return result; 
  }

  public  List AllCMemberinitialiser(List cmemberxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cmemberxs.size(); _i++)
    { CMember cmemberx = (CMember) cmemberxs.get(_i);
      result.add(cmemberx.initialiser());
    }
    return result; 
  }

  public  List AllCTypetoString(List ctypexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < ctypexs.size(); _i++)
    { CType ctypex = (CType) ctypexs.get(_i);
      result.add(ctypex.toString());
    }
    return result; 
  }

  public  List AllCOperationparameterNames(List coperationxs,List s)
  { 
    List result = new Vector();
    for (int _i = 0; _i < coperationxs.size(); _i++)
    { COperation coperationx = (COperation) coperationxs.get(_i);
      result.add(coperationx.parameterNames(s));
    }
    return result; 
  }

  public  List AllCOperationgetDeclaration(List coperationxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < coperationxs.size(); _i++)
    { COperation coperationx = (COperation) coperationxs.get(_i);
      result.add(coperationx.getDeclaration());
    }
    return result; 
  }

  public  List AllCVariableparameterDeclaration(List cvariablexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cvariablexs.size(); _i++)
    { CVariable cvariablex = (CVariable) cvariablexs.get(_i);
      result.add(cvariablex.parameterDeclaration());
    }
    return result; 
  }

  public void printOperationDecs(CProgram cprogramx)
  {   cprogramx.printOperationDecs();
   }

  public  List AllCProgramprintOperationDecs(List cprogramxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cprogramxs.size(); _i++)
    { CProgram cprogramx = (CProgram) cprogramxs.get(_i);
      printOperationDecs(cprogramx);
    }
    return result; 
  }

  public void printLoadOpDecs(CProgram cprogramx)
  {   cprogramx.printLoadOpDecs();
   }

  public  List AllCProgramprintLoadOpDecs(List cprogramxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cprogramxs.size(); _i++)
    { CProgram cprogramx = (CProgram) cprogramxs.get(_i);
      printLoadOpDecs(cprogramx);
    }
    return result; 
  }

  public void printMainOperation(CProgram cprogramx)
  {   cprogramx.printMainOperation();
   }

  public  List AllCProgramprintMainOperation(List cprogramxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cprogramxs.size(); _i++)
    { CProgram cprogramx = (CProgram) cprogramxs.get(_i);
      printMainOperation(cprogramx);
    }
    return result; 
  }

  public void printDeclarations(CProgram cprogramx)
  {   cprogramx.printDeclarations();
   }

  public  List AllCProgramprintDeclarations(List cprogramxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cprogramxs.size(); _i++)
    { CProgram cprogramx = (CProgram) cprogramxs.get(_i);
      printDeclarations(cprogramx);
    }
    return result; 
  }

  public void printcode33(CProgram cprogramx)
  {   cprogramx.printcode33();
   }

  public  List AllCPrimitiveTypetoString(List cprimitivetypexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cprimitivetypexs.size(); _i++)
    { CPrimitiveType cprimitivetypex = (CPrimitiveType) cprimitivetypexs.get(_i);
      result.add(cprimitivetypex.toString());
    }
    return result; 
  }

  public  List AllCArrayTypetoString(List carraytypexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < carraytypexs.size(); _i++)
    { CArrayType carraytypex = (CArrayType) carraytypexs.get(_i);
      result.add(carraytypex.toString());
    }
    return result; 
  }

  public  List AllCPointerTypetoString(List cpointertypexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cpointertypexs.size(); _i++)
    { CPointerType cpointertypex = (CPointerType) cpointertypexs.get(_i);
      result.add(cpointertypex.toString());
    }
    return result; 
  }

  public  List AllCFunctionPointerTypetoString(List cfunctionpointertypexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cfunctionpointertypexs.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) cfunctionpointertypexs.get(_i);
      result.add(cfunctionpointertypex.toString());
    }
    return result; 
  }

  public  List AllCEnumerationtoString(List cenumerationxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cenumerationxs.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerationxs.get(_i);
      result.add(cenumerationx.toString());
    }
    return result; 
  }

  public  List AllCEnumerationdeclarationString(List cenumerationxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < cenumerationxs.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) cenumerationxs.get(_i);
      result.add(cenumerationx.declarationString());
    }
    return result; 
  }

  public void printcode3(CEnumeration cenumerationx)
  {   cenumerationx.printcode3();
   }

  public void printcode(Printcode printcodex)
  {   printcodex.printcode();
   }

  public  List AllPrintcodeprintcode(List printcodexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < printcodexs.size(); _i++)
    { Printcode printcodex = (Printcode) printcodexs.get(_i);
      printcode(printcodex);
    }
    return result; 
  }

  public void types2C(Types2C types2cx)
  {   types2cx.types2C();
   }

  public  List AllTypes2Ctypes2C(List types2cxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < types2cxs.size(); _i++)
    { Types2C types2cx = (Types2C) types2cxs.get(_i);
      types2C(types2cx);
    }
    return result; 
  }

  public void program2C(Program2C program2cx)
  {   program2cx.program2C();
   }

  public  List AllProgram2Cprogram2C(List program2cxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < program2cxs.size(); _i++)
    { Program2C program2cx = (Program2C) program2cxs.get(_i);
      program2C(program2cx);
    }
    return result; 
  }



  public void killAllNamedElement(List namedelementxx)
  { for (int _i = 0; _i < namedelementxx.size(); _i++)
    { killNamedElement((NamedElement) namedelementxx.get(_i)); }
  }

  public void killNamedElement(NamedElement namedelementxx)
  { if (namedelementxx == null) { return; }
   namedelements.remove(namedelementxx);
  }

  public void killAbstractNamedElement(NamedElement namedelementxx)
  {
    if (namedelementxx instanceof Property)
    { killProperty((Property) namedelementxx); }
    if (namedelementxx instanceof Operation)
    { killOperation((Operation) namedelementxx); }
    if (namedelementxx instanceof Association)
    { killAssociation((Association) namedelementxx); }
    if (namedelementxx instanceof Generalization)
    { killGeneralization((Generalization) namedelementxx); }
    if (namedelementxx instanceof Entity)
    { killEntity((Entity) namedelementxx); }
    if (namedelementxx instanceof UseCase)
    { killUseCase((UseCase) namedelementxx); }
    if (namedelementxx instanceof PrimitiveType)
    { killPrimitiveType((PrimitiveType) namedelementxx); }
    if (namedelementxx instanceof Enumeration)
    { killEnumeration((Enumeration) namedelementxx); }
    if (namedelementxx instanceof CollectionType)
    { killCollectionType((CollectionType) namedelementxx); }
  }

  public void killAbstractNamedElement(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { NamedElement _e = (NamedElement) _l.get(_i);
      killAbstractNamedElement(_e);
    }
  }



  public void killAllRelationship(List relationshipxx)
  { for (int _i = 0; _i < relationshipxx.size(); _i++)
    { killRelationship((Relationship) relationshipxx.get(_i)); }
  }

  public void killRelationship(Relationship relationshipxx)
  { if (relationshipxx == null) { return; }
   relationships.remove(relationshipxx);
  killNamedElement(relationshipxx);
  }

  public void killAbstractRelationship(Relationship relationshipxx)
  {
    if (relationshipxx instanceof Association)
    { killAssociation((Association) relationshipxx); }
    if (relationshipxx instanceof Generalization)
    { killGeneralization((Generalization) relationshipxx); }
  }

  public void killAbstractRelationship(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Relationship _e = (Relationship) _l.get(_i);
      killAbstractRelationship(_e);
    }
  }



  public void killAllFeature(List featurexx)
  { for (int _i = 0; _i < featurexx.size(); _i++)
    { killFeature((Feature) featurexx.get(_i)); }
  }

  public void killFeature(Feature featurexx)
  { if (featurexx == null) { return; }
   features.remove(featurexx);
  killNamedElement(featurexx);
  }

  public void killAbstractFeature(Feature featurexx)
  {
    if (featurexx instanceof Property)
    { killProperty((Property) featurexx); }
    if (featurexx instanceof Operation)
    { killOperation((Operation) featurexx); }
  }

  public void killAbstractFeature(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Feature _e = (Feature) _l.get(_i);
      killAbstractFeature(_e);
    }
  }



  public void killAllType(List typexx)
  { for (int _i = 0; _i < typexx.size(); _i++)
    { killType((Type) typexx.get(_i)); }
  }

  public void killType(Type typexx)
  { if (typexx == null) { return; }
   types.remove(typexx);
    Vector _1removedtypeFeature = new Vector();
    Vector _1removedelementTypeFeature = new Vector();
    Vector _1removedelementTypeCollectionType = new Vector();
    Vector _1removedkeyTypeCollectionType = new Vector();
    Vector _1removedtypeExpression = new Vector();
    Vector _1removedelementTypeExpression = new Vector();
    Vector _1removedtypeCreationStatement = new Vector();
    Vector _1removedelementTypeCreationStatement = new Vector();
    Vector _1removedresultTypeUseCase = new Vector();
    Vector _1qrangetypeFeature = new Vector();
    _1qrangetypeFeature.addAll(features);
    for (int _i = 0; _i < _1qrangetypeFeature.size(); _i++)
    { Feature featurex = (Feature) _1qrangetypeFeature.get(_i);
      if (featurex != null && typexx.equals(featurex.gettype()))
      { _1removedtypeFeature.add(featurex);
        featurex.settype(null);
      }
    }
    Vector _1qrangeelementTypeFeature = new Vector();
    _1qrangeelementTypeFeature.addAll(features);
    for (int _i = 0; _i < _1qrangeelementTypeFeature.size(); _i++)
    { Feature featurex = (Feature) _1qrangeelementTypeFeature.get(_i);
      if (featurex != null && typexx.equals(featurex.getelementType()))
      { _1removedelementTypeFeature.add(featurex);
        featurex.setelementType(null);
      }
    }
    Vector _1qrangeelementTypeCollectionType = new Vector();
    _1qrangeelementTypeCollectionType.addAll(collectiontypes);
    for (int _i = 0; _i < _1qrangeelementTypeCollectionType.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) _1qrangeelementTypeCollectionType.get(_i);
      if (collectiontypex != null && typexx.equals(collectiontypex.getelementType()))
      { _1removedelementTypeCollectionType.add(collectiontypex);
        collectiontypex.setelementType(null);
      }
    }
    Vector _1qrangekeyTypeCollectionType = new Vector();
    _1qrangekeyTypeCollectionType.addAll(collectiontypes);
    for (int _i = 0; _i < _1qrangekeyTypeCollectionType.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) _1qrangekeyTypeCollectionType.get(_i);
      if (collectiontypex != null && typexx.equals(collectiontypex.getkeyType()))
      { _1removedkeyTypeCollectionType.add(collectiontypex);
        collectiontypex.setkeyType(null);
      }
    }
    Vector _1qrangetypeExpression = new Vector();
    _1qrangetypeExpression.addAll(expressions);
    for (int _i = 0; _i < _1qrangetypeExpression.size(); _i++)
    { Expression expressionx = (Expression) _1qrangetypeExpression.get(_i);
      if (expressionx != null && typexx.equals(expressionx.gettype()))
      { _1removedtypeExpression.add(expressionx);
        expressionx.settype(null);
      }
    }
    Vector _1qrangeelementTypeExpression = new Vector();
    _1qrangeelementTypeExpression.addAll(expressions);
    for (int _i = 0; _i < _1qrangeelementTypeExpression.size(); _i++)
    { Expression expressionx = (Expression) _1qrangeelementTypeExpression.get(_i);
      if (expressionx != null && typexx.equals(expressionx.getelementType()))
      { _1removedelementTypeExpression.add(expressionx);
        expressionx.setelementType(null);
      }
    }
    Vector _1qrangetypeCreationStatement = new Vector();
    _1qrangetypeCreationStatement.addAll(creationstatements);
    for (int _i = 0; _i < _1qrangetypeCreationStatement.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) _1qrangetypeCreationStatement.get(_i);
      if (creationstatementx != null && typexx.equals(creationstatementx.gettype()))
      { _1removedtypeCreationStatement.add(creationstatementx);
        creationstatementx.settype(null);
      }
    }
    Vector _1qrangeelementTypeCreationStatement = new Vector();
    _1qrangeelementTypeCreationStatement.addAll(creationstatements);
    for (int _i = 0; _i < _1qrangeelementTypeCreationStatement.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) _1qrangeelementTypeCreationStatement.get(_i);
      if (creationstatementx != null && typexx.equals(creationstatementx.getelementType()))
      { _1removedelementTypeCreationStatement.add(creationstatementx);
        creationstatementx.setelementType(null);
      }
    }
    Vector _1qrangetypeAssignStatement = new Vector();
    _1qrangetypeAssignStatement.addAll(assignstatements);
    for (int _i = 0; _i < _1qrangetypeAssignStatement.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) _1qrangetypeAssignStatement.get(_i);
      if (assignstatementx != null && assignstatementx.gettype().contains(typexx))
      { removetype(assignstatementx,typexx); }
    }
    Vector _1qrangeresultTypeUseCase = new Vector();
    _1qrangeresultTypeUseCase.addAll(usecases);
    for (int _i = 0; _i < _1qrangeresultTypeUseCase.size(); _i++)
    { UseCase usecasex = (UseCase) _1qrangeresultTypeUseCase.get(_i);
      if (usecasex != null && typexx.equals(usecasex.getresultType()))
      { _1removedresultTypeUseCase.add(usecasex);
        usecasex.setresultType(null);
      }
    }
    typetypeIdindex.remove(typexx.gettypeId());
    for (int _i = 0; _i < _1removedtypeFeature.size(); _i++)
    { killAbstractFeature((Feature) _1removedtypeFeature.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeFeature.size(); _i++)
    { killAbstractFeature((Feature) _1removedelementTypeFeature.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeCollectionType.size(); _i++)
    { killCollectionType((CollectionType) _1removedelementTypeCollectionType.get(_i)); }
    for (int _i = 0; _i < _1removedkeyTypeCollectionType.size(); _i++)
    { killCollectionType((CollectionType) _1removedkeyTypeCollectionType.get(_i)); }
    for (int _i = 0; _i < _1removedtypeExpression.size(); _i++)
    { killAbstractExpression((Expression) _1removedtypeExpression.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeExpression.size(); _i++)
    { killAbstractExpression((Expression) _1removedelementTypeExpression.get(_i)); }
    for (int _i = 0; _i < _1removedtypeCreationStatement.size(); _i++)
    { killCreationStatement((CreationStatement) _1removedtypeCreationStatement.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeCreationStatement.size(); _i++)
    { killCreationStatement((CreationStatement) _1removedelementTypeCreationStatement.get(_i)); }
    for (int _i = 0; _i < _1removedresultTypeUseCase.size(); _i++)
    { killUseCase((UseCase) _1removedresultTypeUseCase.get(_i)); }
  killNamedElement(typexx);
  }

  public void killAbstractType(Type typexx)
  {
    if (typexx instanceof Entity)
    { killEntity((Entity) typexx); }
    if (typexx instanceof UseCase)
    { killUseCase((UseCase) typexx); }
    if (typexx instanceof PrimitiveType)
    { killPrimitiveType((PrimitiveType) typexx); }
    if (typexx instanceof Enumeration)
    { killEnumeration((Enumeration) typexx); }
    if (typexx instanceof CollectionType)
    { killCollectionType((CollectionType) typexx); }
  }

  public void killAbstractType(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Type _e = (Type) _l.get(_i);
      killAbstractType(_e);
    }
  }



  public void killAllClassifier(List classifierxx)
  { for (int _i = 0; _i < classifierxx.size(); _i++)
    { killClassifier((Classifier) classifierxx.get(_i)); }
  }

  public void killClassifier(Classifier classifierxx)
  { if (classifierxx == null) { return; }
   classifiers.remove(classifierxx);
  killType(classifierxx);
  }

  public void killAbstractClassifier(Classifier classifierxx)
  {
    if (classifierxx instanceof Entity)
    { killEntity((Entity) classifierxx); }
    if (classifierxx instanceof UseCase)
    { killUseCase((UseCase) classifierxx); }
    if (classifierxx instanceof PrimitiveType)
    { killPrimitiveType((PrimitiveType) classifierxx); }
    if (classifierxx instanceof Enumeration)
    { killEnumeration((Enumeration) classifierxx); }
    if (classifierxx instanceof CollectionType)
    { killCollectionType((CollectionType) classifierxx); }
  }

  public void killAbstractClassifier(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Classifier _e = (Classifier) _l.get(_i);
      killAbstractClassifier(_e);
    }
  }



  public void killAllStructuralFeature(List structuralfeaturexx)
  { for (int _i = 0; _i < structuralfeaturexx.size(); _i++)
    { killStructuralFeature((StructuralFeature) structuralfeaturexx.get(_i)); }
  }

  public void killStructuralFeature(StructuralFeature structuralfeaturexx)
  { if (structuralfeaturexx == null) { return; }
   structuralfeatures.remove(structuralfeaturexx);
  killFeature(structuralfeaturexx);
  }

  public void killAbstractStructuralFeature(StructuralFeature structuralfeaturexx)
  {
    if (structuralfeaturexx instanceof Property)
    { killProperty((Property) structuralfeaturexx); }
  }

  public void killAbstractStructuralFeature(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { StructuralFeature _e = (StructuralFeature) _l.get(_i);
      killAbstractStructuralFeature(_e);
    }
  }



  public void killAllDataType(List datatypexx)
  { for (int _i = 0; _i < datatypexx.size(); _i++)
    { killDataType((DataType) datatypexx.get(_i)); }
  }

  public void killDataType(DataType datatypexx)
  { if (datatypexx == null) { return; }
   datatypes.remove(datatypexx);
  killClassifier(datatypexx);
  }

  public void killAbstractDataType(DataType datatypexx)
  {
    if (datatypexx instanceof PrimitiveType)
    { killPrimitiveType((PrimitiveType) datatypexx); }
    if (datatypexx instanceof Enumeration)
    { killEnumeration((Enumeration) datatypexx); }
    if (datatypexx instanceof CollectionType)
    { killCollectionType((CollectionType) datatypexx); }
  }

  public void killAbstractDataType(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { DataType _e = (DataType) _l.get(_i);
      killAbstractDataType(_e);
    }
  }



  public void killAllEnumeration(List enumerationxx)
  { for (int _i = 0; _i < enumerationxx.size(); _i++)
    { killEnumeration((Enumeration) enumerationxx.get(_i)); }
  }

  public void killEnumeration(Enumeration enumerationxx)
  { if (enumerationxx == null) { return; }
   enumerations.remove(enumerationxx);
  killDataType(enumerationxx);
  }



  public void killAllEnumerationLiteral(List enumerationliteralxx)
  { for (int _i = 0; _i < enumerationliteralxx.size(); _i++)
    { killEnumerationLiteral((EnumerationLiteral) enumerationliteralxx.get(_i)); }
  }

  public void killEnumerationLiteral(EnumerationLiteral enumerationliteralxx)
  { if (enumerationliteralxx == null) { return; }
   enumerationliterals.remove(enumerationliteralxx);
    Vector _1qrangeownedLiteralEnumeration = new Vector();
    _1qrangeownedLiteralEnumeration.addAll(enumerations);
    for (int _i = 0; _i < _1qrangeownedLiteralEnumeration.size(); _i++)
    { Enumeration enumerationx = (Enumeration) _1qrangeownedLiteralEnumeration.get(_i);
      if (enumerationx != null && enumerationx.getownedLiteral().contains(enumerationliteralxx))
      { removeownedLiteral(enumerationx,enumerationliteralxx); }
    }
  }



  public void killAllPrimitiveType(List primitivetypexx)
  { for (int _i = 0; _i < primitivetypexx.size(); _i++)
    { killPrimitiveType((PrimitiveType) primitivetypexx.get(_i)); }
  }

  public void killPrimitiveType(PrimitiveType primitivetypexx)
  { if (primitivetypexx == null) { return; }
   primitivetypes.remove(primitivetypexx);
  killDataType(primitivetypexx);
  }



  public void killAllEntity(List entityxx)
  { for (int _i = 0; _i < entityxx.size(); _i++)
    { killEntity((Entity) entityxx.get(_i)); }
  }

  public void killEntity(Entity entityxx)
  { if (entityxx == null) { return; }
   entitys.remove(entityxx);
    Vector _2removedownerProperty = new Vector();
    Vector _2removedspecificGeneralization = new Vector();
    Vector _1removedgeneralGeneralization = new Vector();
    Vector _2removedownerOperation = new Vector();
    Vector _2qrangeownerProperty = new Vector();
    _2qrangeownerProperty.addAll(entityxx.getownedAttribute());
    for (int _i = 0; _i < _2qrangeownerProperty.size(); _i++)
    { Property propertyx = (Property) _2qrangeownerProperty.get(_i);
      if (propertyx != null && entityxx.equals(propertyx.getowner()))
      { _2removedownerProperty.add(propertyx);
        propertyx.setowner(null);
      }
    }

    entityxx.setownedAttribute(new Vector());
    Vector _1qrangesuperclassEntity = new Vector();
    _1qrangesuperclassEntity.addAll(entityxx.getsubclasses());
    for (int _i = 0; _i < _1qrangesuperclassEntity.size(); _i++)
    { Entity entityx = (Entity) _1qrangesuperclassEntity.get(_i);
      if (entityx != null && entityx.getsuperclass().contains(entityxx))
      { removesuperclass(entityx,entityxx); }
    }
    Vector _2qrangesubclassesEntity = new Vector();
    _2qrangesubclassesEntity.addAll(entityxx.getsuperclass());
    for (int _i = 0; _i < _2qrangesubclassesEntity.size(); _i++)
    { Entity entityx = (Entity) _2qrangesubclassesEntity.get(_i);
      if (entityx != null && entityx.getsubclasses().contains(entityxx))
      { removesubclasses(entityx,entityxx); }
    }

    entityxx.setsuperclass(new Vector());
    Vector _2qrangespecificGeneralization = new Vector();
    _2qrangespecificGeneralization.addAll(entityxx.getgeneralization());
    for (int _i = 0; _i < _2qrangespecificGeneralization.size(); _i++)
    { Generalization generalizationx = (Generalization) _2qrangespecificGeneralization.get(_i);
      if (generalizationx != null && entityxx.equals(generalizationx.getspecific()))
      { _2removedspecificGeneralization.add(generalizationx);
        generalizationx.setspecific(null);
      }
    }

    entityxx.setgeneralization(new Vector());
    Vector _1qrangegeneralGeneralization = new Vector();
    _1qrangegeneralGeneralization.addAll(entityxx.getspecialization());
    for (int _i = 0; _i < _1qrangegeneralGeneralization.size(); _i++)
    { Generalization generalizationx = (Generalization) _1qrangegeneralGeneralization.get(_i);
      if (generalizationx != null && entityxx.equals(generalizationx.getgeneral()))
      { _1removedgeneralGeneralization.add(generalizationx);
        generalizationx.setgeneral(null);
      }
    }
    Vector _2qrangeownerOperation = new Vector();
    _2qrangeownerOperation.addAll(entityxx.getownedOperation());
    for (int _i = 0; _i < _2qrangeownerOperation.size(); _i++)
    { Operation operationx = (Operation) _2qrangeownerOperation.get(_i);
      if (operationx != null && entityxx.equals(operationx.getowner()))
      { _2removedownerOperation.add(operationx);
        operationx.setowner(null);
      }
    }

    entityxx.setownedOperation(new Vector());
    Vector _1qrangedefinersOperation = new Vector();
    _1qrangedefinersOperation.addAll(operations);
    for (int _i = 0; _i < _1qrangedefinersOperation.size(); _i++)
    { Operation operationx = (Operation) _1qrangedefinersOperation.get(_i);
      if (operationx != null && operationx.getdefiners().contains(entityxx))
      { removedefiners(operationx,entityxx); }
    }
    Vector _1qrangecontextBasicExpression = new Vector();
    _1qrangecontextBasicExpression.addAll(basicexpressions);
    for (int _i = 0; _i < _1qrangecontextBasicExpression.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _1qrangecontextBasicExpression.get(_i);
      if (basicexpressionx != null && basicexpressionx.getcontext().contains(entityxx))
      { removecontext(basicexpressionx,entityxx); }
    }
    for (int _i = 0; _i < _2removedownerProperty.size(); _i++)
    { killProperty((Property) _2removedownerProperty.get(_i)); }
    for (int _i = 0; _i < _2removedspecificGeneralization.size(); _i++)
    { killGeneralization((Generalization) _2removedspecificGeneralization.get(_i)); }
    for (int _i = 0; _i < _1removedgeneralGeneralization.size(); _i++)
    { killGeneralization((Generalization) _1removedgeneralGeneralization.get(_i)); }
    for (int _i = 0; _i < _2removedownerOperation.size(); _i++)
    { killOperation((Operation) _2removedownerOperation.get(_i)); }
  killClassifier(entityxx);
  }



  public void killAllProperty(List propertyxx)
  { for (int _i = 0; _i < propertyxx.size(); _i++)
    { killProperty((Property) propertyxx.get(_i)); }
  }

  public void killProperty(Property propertyxx)
  { if (propertyxx == null) { return; }
   propertys.remove(propertyxx);
    Vector _1qrangeownedAttributeEntity = new Vector();
    _1qrangeownedAttributeEntity.add(propertyxx.getowner());
    for (int _i = 0; _i < _1qrangeownedAttributeEntity.size(); _i++)
    { Entity entityx = (Entity) _1qrangeownedAttributeEntity.get(_i);
      if (entityx != null && entityx.getownedAttribute().contains(propertyxx))
      { removeownedAttribute(entityx,propertyxx); }
    }
    Vector _1qrangememberEndAssociation = new Vector();
    _1qrangememberEndAssociation.addAll(associations);
    for (int _i = 0; _i < _1qrangememberEndAssociation.size(); _i++)
    { Association associationx = (Association) _1qrangememberEndAssociation.get(_i);
      if (associationx != null && associationx.getmemberEnd().contains(propertyxx))
      { removememberEnd(associationx,propertyxx); }
    }
    Vector _1qrangeparametersBehaviouralFeature = new Vector();
    _1qrangeparametersBehaviouralFeature.addAll(behaviouralfeatures);
    for (int _i = 0; _i < _1qrangeparametersBehaviouralFeature.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) _1qrangeparametersBehaviouralFeature.get(_i);
      if (behaviouralfeaturex != null && behaviouralfeaturex.getparameters().contains(propertyxx))
      { removeparameters(behaviouralfeaturex,propertyxx); }
    }
    Vector _1qrangeparametersUseCase = new Vector();
    _1qrangeparametersUseCase.addAll(usecases);
    for (int _i = 0; _i < _1qrangeparametersUseCase.size(); _i++)
    { UseCase usecasex = (UseCase) _1qrangeparametersUseCase.get(_i);
      if (usecasex != null && usecasex.getparameters().contains(propertyxx))
      { removeparameters(usecasex,propertyxx); }
    }
    Vector _1qrangereferredPropertyBasicExpression = new Vector();
    _1qrangereferredPropertyBasicExpression.addAll(basicexpressions);
    for (int _i = 0; _i < _1qrangereferredPropertyBasicExpression.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _1qrangereferredPropertyBasicExpression.get(_i);
      if (basicexpressionx != null && basicexpressionx.getreferredProperty().contains(propertyxx))
      { removereferredProperty(basicexpressionx,propertyxx); }
    }
  killStructuralFeature(propertyxx);
  }



  public void killAllAssociation(List associationxx)
  { for (int _i = 0; _i < associationxx.size(); _i++)
    { killAssociation((Association) associationxx.get(_i)); }
  }

  public void killAssociation(Association associationxx)
  { if (associationxx == null) { return; }
   associations.remove(associationxx);
  killRelationship(associationxx);
  }



  public void killAllGeneralization(List generalizationxx)
  { for (int _i = 0; _i < generalizationxx.size(); _i++)
    { killGeneralization((Generalization) generalizationxx.get(_i)); }
  }

  public void killGeneralization(Generalization generalizationxx)
  { if (generalizationxx == null) { return; }
   generalizations.remove(generalizationxx);
    Vector _1qrangegeneralizationEntity = new Vector();
    _1qrangegeneralizationEntity.add(generalizationxx.getspecific());
    for (int _i = 0; _i < _1qrangegeneralizationEntity.size(); _i++)
    { Entity entityx = (Entity) _1qrangegeneralizationEntity.get(_i);
      if (entityx != null && entityx.getgeneralization().contains(generalizationxx))
      { removegeneralization(entityx,generalizationxx); }
    }
    Vector _2qrangespecializationEntity = new Vector();
    _2qrangespecializationEntity.add(generalizationxx.getgeneral());
    for (int _i = 0; _i < _2qrangespecializationEntity.size(); _i++)
    { Entity entityx = (Entity) _2qrangespecializationEntity.get(_i);
      if (entityx != null && entityx.getspecialization().contains(generalizationxx))
      { removespecialization(entityx,generalizationxx); }
    }
  killRelationship(generalizationxx);
  }



  public void killAllBehaviouralFeature(List behaviouralfeaturexx)
  { for (int _i = 0; _i < behaviouralfeaturexx.size(); _i++)
    { killBehaviouralFeature((BehaviouralFeature) behaviouralfeaturexx.get(_i)); }
  }

  public void killBehaviouralFeature(BehaviouralFeature behaviouralfeaturexx)
  { if (behaviouralfeaturexx == null) { return; }
   behaviouralfeatures.remove(behaviouralfeaturexx);
  killFeature(behaviouralfeaturexx);
  }

  public void killAbstractBehaviouralFeature(BehaviouralFeature behaviouralfeaturexx)
  {
    if (behaviouralfeaturexx instanceof Operation)
    { killOperation((Operation) behaviouralfeaturexx); }
  }

  public void killAbstractBehaviouralFeature(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { BehaviouralFeature _e = (BehaviouralFeature) _l.get(_i);
      killAbstractBehaviouralFeature(_e);
    }
  }



  public void killAllOperation(List operationxx)
  { for (int _i = 0; _i < operationxx.size(); _i++)
    { killOperation((Operation) operationxx.get(_i)); }
  }

  public void killOperation(Operation operationxx)
  { if (operationxx == null) { return; }
   operations.remove(operationxx);
    Vector _1qrangeownedOperationEntity = new Vector();
    _1qrangeownedOperationEntity.add(operationxx.getowner());
    for (int _i = 0; _i < _1qrangeownedOperationEntity.size(); _i++)
    { Entity entityx = (Entity) _1qrangeownedOperationEntity.get(_i);
      if (entityx != null && entityx.getownedOperation().contains(operationxx))
      { removeownedOperation(entityx,operationxx); }
    }
  killBehaviouralFeature(operationxx);
  }



  public void killAllExpression(List expressionxx)
  { for (int _i = 0; _i < expressionxx.size(); _i++)
    { killExpression((Expression) expressionxx.get(_i)); }
  }

  public void killExpression(Expression expressionxx)
  { if (expressionxx == null) { return; }
   expressions.remove(expressionxx);
    Vector _1removedleftBinaryExpression = new Vector();
    Vector _1removedrightBinaryExpression = new Vector();
    Vector _1removedtestConditionalExpression = new Vector();
    Vector _1removedifExpConditionalExpression = new Vector();
    Vector _1removedelseExpConditionalExpression = new Vector();
    Vector _1removedargumentUnaryExpression = new Vector();
    Vector _1removedinitialValueProperty = new Vector();
    Vector _1removedcallExpOperationCallStatement = new Vector();
    Vector _1removedcallExpImplicitCallStatement = new Vector();
    Vector _1removedtestLoopStatement = new Vector();
    Vector _1removedleftAssignStatement = new Vector();
    Vector _1removedrightAssignStatement = new Vector();
    Vector _1removedtestConditionalStatement = new Vector();
    Vector _1removedloopRangeBoundedLoopStatement = new Vector();
    Vector _1removedloopVarBoundedLoopStatement = new Vector();
    Vector _1removedcaughtObjectCatchStatement = new Vector();
    Vector _1removedconditionAssertStatement = new Vector();
    Vector _1removedthrownObjectErrorStatement = new Vector();
    Vector _1qrangeelementsCollectionExpression = new Vector();
    _1qrangeelementsCollectionExpression.addAll(collectionexpressions);
    for (int _i = 0; _i < _1qrangeelementsCollectionExpression.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) _1qrangeelementsCollectionExpression.get(_i);
      if (collectionexpressionx != null && collectionexpressionx.getelements().contains(expressionxx))
      { removeelements(collectionexpressionx,expressionxx); }
    }
    Vector _1qrangeleftBinaryExpression = new Vector();
    _1qrangeleftBinaryExpression.addAll(binaryexpressions);
    for (int _i = 0; _i < _1qrangeleftBinaryExpression.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) _1qrangeleftBinaryExpression.get(_i);
      if (binaryexpressionx != null && expressionxx.equals(binaryexpressionx.getleft()))
      { _1removedleftBinaryExpression.add(binaryexpressionx);
        binaryexpressionx.setleft(null);
      }
    }
    Vector _1qrangerightBinaryExpression = new Vector();
    _1qrangerightBinaryExpression.addAll(binaryexpressions);
    for (int _i = 0; _i < _1qrangerightBinaryExpression.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) _1qrangerightBinaryExpression.get(_i);
      if (binaryexpressionx != null && expressionxx.equals(binaryexpressionx.getright()))
      { _1removedrightBinaryExpression.add(binaryexpressionx);
        binaryexpressionx.setright(null);
      }
    }
    Vector _1qrangetestConditionalExpression = new Vector();
    _1qrangetestConditionalExpression.addAll(conditionalexpressions);
    for (int _i = 0; _i < _1qrangetestConditionalExpression.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) _1qrangetestConditionalExpression.get(_i);
      if (conditionalexpressionx != null && expressionxx.equals(conditionalexpressionx.gettest()))
      { _1removedtestConditionalExpression.add(conditionalexpressionx);
        conditionalexpressionx.settest(null);
      }
    }
    Vector _1qrangeifExpConditionalExpression = new Vector();
    _1qrangeifExpConditionalExpression.addAll(conditionalexpressions);
    for (int _i = 0; _i < _1qrangeifExpConditionalExpression.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) _1qrangeifExpConditionalExpression.get(_i);
      if (conditionalexpressionx != null && expressionxx.equals(conditionalexpressionx.getifExp()))
      { _1removedifExpConditionalExpression.add(conditionalexpressionx);
        conditionalexpressionx.setifExp(null);
      }
    }
    Vector _1qrangeelseExpConditionalExpression = new Vector();
    _1qrangeelseExpConditionalExpression.addAll(conditionalexpressions);
    for (int _i = 0; _i < _1qrangeelseExpConditionalExpression.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) _1qrangeelseExpConditionalExpression.get(_i);
      if (conditionalexpressionx != null && expressionxx.equals(conditionalexpressionx.getelseExp()))
      { _1removedelseExpConditionalExpression.add(conditionalexpressionx);
        conditionalexpressionx.setelseExp(null);
      }
    }
    Vector _1qrangeparametersBasicExpression = new Vector();
    _1qrangeparametersBasicExpression.addAll(basicexpressions);
    for (int _i = 0; _i < _1qrangeparametersBasicExpression.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _1qrangeparametersBasicExpression.get(_i);
      if (basicexpressionx != null && basicexpressionx.getparameters().contains(expressionxx))
      { removeparameters(basicexpressionx,expressionxx); }
    }
    Vector _1qrangeargumentUnaryExpression = new Vector();
    _1qrangeargumentUnaryExpression.addAll(unaryexpressions);
    for (int _i = 0; _i < _1qrangeargumentUnaryExpression.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) _1qrangeargumentUnaryExpression.get(_i);
      if (unaryexpressionx != null && expressionxx.equals(unaryexpressionx.getargument()))
      { _1removedargumentUnaryExpression.add(unaryexpressionx);
        unaryexpressionx.setargument(null);
      }
    }
    Vector _1qrangearrayIndexBasicExpression = new Vector();
    _1qrangearrayIndexBasicExpression.addAll(basicexpressions);
    for (int _i = 0; _i < _1qrangearrayIndexBasicExpression.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _1qrangearrayIndexBasicExpression.get(_i);
      if (basicexpressionx != null && basicexpressionx.getarrayIndex().contains(expressionxx))
      { removearrayIndex(basicexpressionx,expressionxx); }
    }
    Vector _1qrangeinitialValueProperty = new Vector();
    _1qrangeinitialValueProperty.addAll(propertys);
    for (int _i = 0; _i < _1qrangeinitialValueProperty.size(); _i++)
    { Property propertyx = (Property) _1qrangeinitialValueProperty.get(_i);
      if (propertyx != null && expressionxx.equals(propertyx.getinitialValue()))
      { _1removedinitialValueProperty.add(propertyx);
        propertyx.setinitialValue(null);
      }
    }
    Vector _1qrangeobjectRefBasicExpression = new Vector();
    _1qrangeobjectRefBasicExpression.addAll(basicexpressions);
    for (int _i = 0; _i < _1qrangeobjectRefBasicExpression.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _1qrangeobjectRefBasicExpression.get(_i);
      if (basicexpressionx != null && basicexpressionx.getobjectRef().contains(expressionxx))
      { removeobjectRef(basicexpressionx,expressionxx); }
    }
    Vector _1qrangeinitialExpressionCreationStatement = new Vector();
    _1qrangeinitialExpressionCreationStatement.addAll(creationstatements);
    for (int _i = 0; _i < _1qrangeinitialExpressionCreationStatement.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) _1qrangeinitialExpressionCreationStatement.get(_i);
      if (creationstatementx != null && creationstatementx.getinitialExpression().contains(expressionxx))
      { removeinitialExpression(creationstatementx,expressionxx); }
    }
    Vector _1qrangereturnValueReturnStatement = new Vector();
    _1qrangereturnValueReturnStatement.addAll(returnstatements);
    for (int _i = 0; _i < _1qrangereturnValueReturnStatement.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) _1qrangereturnValueReturnStatement.get(_i);
      if (returnstatementx != null && returnstatementx.getreturnValue().contains(expressionxx))
      { removereturnValue(returnstatementx,expressionxx); }
    }
    Vector _1qrangecallExpOperationCallStatement = new Vector();
    _1qrangecallExpOperationCallStatement.addAll(operationcallstatements);
    for (int _i = 0; _i < _1qrangecallExpOperationCallStatement.size(); _i++)
    { OperationCallStatement operationcallstatementx = (OperationCallStatement) _1qrangecallExpOperationCallStatement.get(_i);
      if (operationcallstatementx != null && expressionxx.equals(operationcallstatementx.getcallExp()))
      { _1removedcallExpOperationCallStatement.add(operationcallstatementx);
        operationcallstatementx.setcallExp(null);
      }
    }
    Vector _1qrangecallExpImplicitCallStatement = new Vector();
    _1qrangecallExpImplicitCallStatement.addAll(implicitcallstatements);
    for (int _i = 0; _i < _1qrangecallExpImplicitCallStatement.size(); _i++)
    { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) _1qrangecallExpImplicitCallStatement.get(_i);
      if (implicitcallstatementx != null && expressionxx.equals(implicitcallstatementx.getcallExp()))
      { _1removedcallExpImplicitCallStatement.add(implicitcallstatementx);
        implicitcallstatementx.setcallExp(null);
      }
    }
    Vector _1qrangetestLoopStatement = new Vector();
    _1qrangetestLoopStatement.addAll(loopstatements);
    for (int _i = 0; _i < _1qrangetestLoopStatement.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) _1qrangetestLoopStatement.get(_i);
      if (loopstatementx != null && expressionxx.equals(loopstatementx.gettest()))
      { _1removedtestLoopStatement.add(loopstatementx);
        loopstatementx.settest(null);
      }
    }
    Vector _1qrangeleftAssignStatement = new Vector();
    _1qrangeleftAssignStatement.addAll(assignstatements);
    for (int _i = 0; _i < _1qrangeleftAssignStatement.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) _1qrangeleftAssignStatement.get(_i);
      if (assignstatementx != null && expressionxx.equals(assignstatementx.getleft()))
      { _1removedleftAssignStatement.add(assignstatementx);
        assignstatementx.setleft(null);
      }
    }
    Vector _1qrangerightAssignStatement = new Vector();
    _1qrangerightAssignStatement.addAll(assignstatements);
    for (int _i = 0; _i < _1qrangerightAssignStatement.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) _1qrangerightAssignStatement.get(_i);
      if (assignstatementx != null && expressionxx.equals(assignstatementx.getright()))
      { _1removedrightAssignStatement.add(assignstatementx);
        assignstatementx.setright(null);
      }
    }
    Vector _1qrangetestConditionalStatement = new Vector();
    _1qrangetestConditionalStatement.addAll(conditionalstatements);
    for (int _i = 0; _i < _1qrangetestConditionalStatement.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) _1qrangetestConditionalStatement.get(_i);
      if (conditionalstatementx != null && expressionxx.equals(conditionalstatementx.gettest()))
      { _1removedtestConditionalStatement.add(conditionalstatementx);
        conditionalstatementx.settest(null);
      }
    }
    Vector _1qrangeloopRangeBoundedLoopStatement = new Vector();
    _1qrangeloopRangeBoundedLoopStatement.addAll(boundedloopstatements);
    for (int _i = 0; _i < _1qrangeloopRangeBoundedLoopStatement.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) _1qrangeloopRangeBoundedLoopStatement.get(_i);
      if (boundedloopstatementx != null && expressionxx.equals(boundedloopstatementx.getloopRange()))
      { _1removedloopRangeBoundedLoopStatement.add(boundedloopstatementx);
        boundedloopstatementx.setloopRange(null);
      }
    }
    Vector _1qrangeloopVarBoundedLoopStatement = new Vector();
    _1qrangeloopVarBoundedLoopStatement.addAll(boundedloopstatements);
    for (int _i = 0; _i < _1qrangeloopVarBoundedLoopStatement.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) _1qrangeloopVarBoundedLoopStatement.get(_i);
      if (boundedloopstatementx != null && expressionxx.equals(boundedloopstatementx.getloopVar()))
      { _1removedloopVarBoundedLoopStatement.add(boundedloopstatementx);
        boundedloopstatementx.setloopVar(null);
      }
    }
    Vector _1qrangecaughtObjectCatchStatement = new Vector();
    _1qrangecaughtObjectCatchStatement.addAll(catchstatements);
    for (int _i = 0; _i < _1qrangecaughtObjectCatchStatement.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) _1qrangecaughtObjectCatchStatement.get(_i);
      if (catchstatementx != null && expressionxx.equals(catchstatementx.getcaughtObject()))
      { _1removedcaughtObjectCatchStatement.add(catchstatementx);
        catchstatementx.setcaughtObject(null);
      }
    }
    Vector _1qrangeconditionAssertStatement = new Vector();
    _1qrangeconditionAssertStatement.addAll(assertstatements);
    for (int _i = 0; _i < _1qrangeconditionAssertStatement.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) _1qrangeconditionAssertStatement.get(_i);
      if (assertstatementx != null && expressionxx.equals(assertstatementx.getcondition()))
      { _1removedconditionAssertStatement.add(assertstatementx);
        assertstatementx.setcondition(null);
      }
    }
    Vector _1qrangemessageAssertStatement = new Vector();
    _1qrangemessageAssertStatement.addAll(assertstatements);
    for (int _i = 0; _i < _1qrangemessageAssertStatement.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) _1qrangemessageAssertStatement.get(_i);
      if (assertstatementx != null && assertstatementx.getmessage().contains(expressionxx))
      { removemessage(assertstatementx,expressionxx); }
    }
    Vector _1qrangethrownObjectErrorStatement = new Vector();
    _1qrangethrownObjectErrorStatement.addAll(errorstatements);
    for (int _i = 0; _i < _1qrangethrownObjectErrorStatement.size(); _i++)
    { ErrorStatement errorstatementx = (ErrorStatement) _1qrangethrownObjectErrorStatement.get(_i);
      if (errorstatementx != null && expressionxx.equals(errorstatementx.getthrownObject()))
      { _1removedthrownObjectErrorStatement.add(errorstatementx);
        errorstatementx.setthrownObject(null);
      }
    }
    expressionexpIdindex.remove(expressionxx.getexpId());
    for (int _i = 0; _i < _1removedleftBinaryExpression.size(); _i++)
    { killBinaryExpression((BinaryExpression) _1removedleftBinaryExpression.get(_i)); }
    for (int _i = 0; _i < _1removedrightBinaryExpression.size(); _i++)
    { killBinaryExpression((BinaryExpression) _1removedrightBinaryExpression.get(_i)); }
    for (int _i = 0; _i < _1removedtestConditionalExpression.size(); _i++)
    { killConditionalExpression((ConditionalExpression) _1removedtestConditionalExpression.get(_i)); }
    for (int _i = 0; _i < _1removedifExpConditionalExpression.size(); _i++)
    { killConditionalExpression((ConditionalExpression) _1removedifExpConditionalExpression.get(_i)); }
    for (int _i = 0; _i < _1removedelseExpConditionalExpression.size(); _i++)
    { killConditionalExpression((ConditionalExpression) _1removedelseExpConditionalExpression.get(_i)); }
    for (int _i = 0; _i < _1removedargumentUnaryExpression.size(); _i++)
    { killUnaryExpression((UnaryExpression) _1removedargumentUnaryExpression.get(_i)); }
    for (int _i = 0; _i < _1removedinitialValueProperty.size(); _i++)
    { killProperty((Property) _1removedinitialValueProperty.get(_i)); }
    for (int _i = 0; _i < _1removedcallExpOperationCallStatement.size(); _i++)
    { killOperationCallStatement((OperationCallStatement) _1removedcallExpOperationCallStatement.get(_i)); }
    for (int _i = 0; _i < _1removedcallExpImplicitCallStatement.size(); _i++)
    { killImplicitCallStatement((ImplicitCallStatement) _1removedcallExpImplicitCallStatement.get(_i)); }
    for (int _i = 0; _i < _1removedtestLoopStatement.size(); _i++)
    { killAbstractLoopStatement((LoopStatement) _1removedtestLoopStatement.get(_i)); }
    for (int _i = 0; _i < _1removedleftAssignStatement.size(); _i++)
    { killAssignStatement((AssignStatement) _1removedleftAssignStatement.get(_i)); }
    for (int _i = 0; _i < _1removedrightAssignStatement.size(); _i++)
    { killAssignStatement((AssignStatement) _1removedrightAssignStatement.get(_i)); }
    for (int _i = 0; _i < _1removedtestConditionalStatement.size(); _i++)
    { killConditionalStatement((ConditionalStatement) _1removedtestConditionalStatement.get(_i)); }
    for (int _i = 0; _i < _1removedloopRangeBoundedLoopStatement.size(); _i++)
    { killBoundedLoopStatement((BoundedLoopStatement) _1removedloopRangeBoundedLoopStatement.get(_i)); }
    for (int _i = 0; _i < _1removedloopVarBoundedLoopStatement.size(); _i++)
    { killBoundedLoopStatement((BoundedLoopStatement) _1removedloopVarBoundedLoopStatement.get(_i)); }
    for (int _i = 0; _i < _1removedcaughtObjectCatchStatement.size(); _i++)
    { killCatchStatement((CatchStatement) _1removedcaughtObjectCatchStatement.get(_i)); }
    for (int _i = 0; _i < _1removedconditionAssertStatement.size(); _i++)
    { killAssertStatement((AssertStatement) _1removedconditionAssertStatement.get(_i)); }
    for (int _i = 0; _i < _1removedthrownObjectErrorStatement.size(); _i++)
    { killErrorStatement((ErrorStatement) _1removedthrownObjectErrorStatement.get(_i)); }
  }

  public void killAbstractExpression(Expression expressionxx)
  {
    if (expressionxx instanceof UnaryExpression)
    { killUnaryExpression((UnaryExpression) expressionxx); }
    if (expressionxx instanceof ConditionalExpression)
    { killConditionalExpression((ConditionalExpression) expressionxx); }
    if (expressionxx instanceof BasicExpression)
    { killBasicExpression((BasicExpression) expressionxx); }
    if (expressionxx instanceof BinaryExpression)
    { killBinaryExpression((BinaryExpression) expressionxx); }
    if (expressionxx instanceof CollectionExpression)
    { killCollectionExpression((CollectionExpression) expressionxx); }
  }

  public void killAbstractExpression(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Expression _e = (Expression) _l.get(_i);
      killAbstractExpression(_e);
    }
  }



  public void killAllUseCase(List usecasexx)
  { for (int _i = 0; _i < usecasexx.size(); _i++)
    { killUseCase((UseCase) usecasexx.get(_i)); }
  }

  public void killUseCase(UseCase usecasexx)
  { if (usecasexx == null) { return; }
   usecases.remove(usecasexx);
  killClassifier(usecasexx);
  }



  public void killAllCollectionType(List collectiontypexx)
  { for (int _i = 0; _i < collectiontypexx.size(); _i++)
    { killCollectionType((CollectionType) collectiontypexx.get(_i)); }
  }

  public void killCollectionType(CollectionType collectiontypexx)
  { if (collectiontypexx == null) { return; }
   collectiontypes.remove(collectiontypexx);
  killDataType(collectiontypexx);
  }



  public void killAllCStruct(List cstructxx)
  { for (int _i = 0; _i < cstructxx.size(); _i++)
    { killCStruct((CStruct) cstructxx.get(_i)); }
  }

  public void killCStruct(CStruct cstructxx)
  { if (cstructxx == null) { return; }
   cstructs.remove(cstructxx);
    Vector _1qrangeinterfacesCStruct = new Vector();
    _1qrangeinterfacesCStruct.addAll(cstructs);
    for (int _i = 0; _i < _1qrangeinterfacesCStruct.size(); _i++)
    { CStruct cstructx = (CStruct) _1qrangeinterfacesCStruct.get(_i);
      if (cstructx != null && cstructx.getinterfaces().contains(cstructxx))
      { removeinterfaces(cstructx,cstructxx); }
    }
    Vector _1qrangedefinersCOperation = new Vector();
    _1qrangedefinersCOperation.addAll(coperations);
    for (int _i = 0; _i < _1qrangedefinersCOperation.size(); _i++)
    { COperation coperationx = (COperation) _1qrangedefinersCOperation.get(_i);
      if (coperationx != null && coperationx.getdefiners().contains(cstructxx))
      { removedefiners(coperationx,cstructxx); }
    }
    Vector _1qrangestructsCProgram = new Vector();
    _1qrangestructsCProgram.addAll(cprograms);
    for (int _i = 0; _i < _1qrangestructsCProgram.size(); _i++)
    { CProgram cprogramx = (CProgram) _1qrangestructsCProgram.get(_i);
      if (cprogramx != null && cprogramx.getstructs().contains(cstructxx))
      { removestructs(cprogramx,cstructxx); }
    }
    cstructnameindex.remove(cstructxx.getname());
  killCType(cstructxx);
  }



  public void killAllCMember(List cmemberxx)
  { for (int _i = 0; _i < cmemberxx.size(); _i++)
    { killCMember((CMember) cmemberxx.get(_i)); }
  }

  public void killCMember(CMember cmemberxx)
  { if (cmemberxx == null) { return; }
   cmembers.remove(cmemberxx);
    Vector _1qrangemembersCStruct = new Vector();
    _1qrangemembersCStruct.addAll(cstructs);
    for (int _i = 0; _i < _1qrangemembersCStruct.size(); _i++)
    { CStruct cstructx = (CStruct) _1qrangemembersCStruct.get(_i);
      if (cstructx != null && cstructx.getmembers().contains(cmemberxx))
      { removemembers(cstructx,cmemberxx); }
    }
    Vector _1qrangeallMembersCStruct = new Vector();
    _1qrangeallMembersCStruct.addAll(cstructs);
    for (int _i = 0; _i < _1qrangeallMembersCStruct.size(); _i++)
    { CStruct cstructx = (CStruct) _1qrangeallMembersCStruct.get(_i);
      if (cstructx != null && cstructx.getallMembers().contains(cmemberxx))
      { removeallMembers(cstructx,cmemberxx); }
    }
  }



  public void killAllCType(List ctypexx)
  { for (int _i = 0; _i < ctypexx.size(); _i++)
    { killCType((CType) ctypexx.get(_i)); }
  }

  public void killCType(CType ctypexx)
  { if (ctypexx == null) { return; }
   ctypes.remove(ctypexx);
    Vector _1removedtypeCVariable = new Vector();
    Vector _1removedreturnTypeCOperation = new Vector();
    Vector _1removedelementTypeCOperation = new Vector();
    Vector _1removedcomponentTypeCArrayType = new Vector();
    Vector _1removedpointsToCPointerType = new Vector();
    Vector _1removeddomainTypeCFunctionPointerType = new Vector();
    Vector _1removedrangeTypeCFunctionPointerType = new Vector();
    Vector _1removedtypeCMember = new Vector();
    Vector _1qrangetypeCVariable = new Vector();
    _1qrangetypeCVariable.addAll(cvariables);
    for (int _i = 0; _i < _1qrangetypeCVariable.size(); _i++)
    { CVariable cvariablex = (CVariable) _1qrangetypeCVariable.get(_i);
      if (cvariablex != null && ctypexx.equals(cvariablex.gettype()))
      { _1removedtypeCVariable.add(cvariablex);
        cvariablex.settype(null);
      }
    }
    Vector _1qrangereturnTypeCOperation = new Vector();
    _1qrangereturnTypeCOperation.addAll(coperations);
    for (int _i = 0; _i < _1qrangereturnTypeCOperation.size(); _i++)
    { COperation coperationx = (COperation) _1qrangereturnTypeCOperation.get(_i);
      if (coperationx != null && ctypexx.equals(coperationx.getreturnType()))
      { _1removedreturnTypeCOperation.add(coperationx);
        coperationx.setreturnType(null);
      }
    }
    Vector _1qrangeelementTypeCOperation = new Vector();
    _1qrangeelementTypeCOperation.addAll(coperations);
    for (int _i = 0; _i < _1qrangeelementTypeCOperation.size(); _i++)
    { COperation coperationx = (COperation) _1qrangeelementTypeCOperation.get(_i);
      if (coperationx != null && ctypexx.equals(coperationx.getelementType()))
      { _1removedelementTypeCOperation.add(coperationx);
        coperationx.setelementType(null);
      }
    }
    Vector _1qrangecomponentTypeCArrayType = new Vector();
    _1qrangecomponentTypeCArrayType.addAll(carraytypes);
    for (int _i = 0; _i < _1qrangecomponentTypeCArrayType.size(); _i++)
    { CArrayType carraytypex = (CArrayType) _1qrangecomponentTypeCArrayType.get(_i);
      if (carraytypex != null && ctypexx.equals(carraytypex.getcomponentType()))
      { _1removedcomponentTypeCArrayType.add(carraytypex);
        carraytypex.setcomponentType(null);
      }
    }
    Vector _1qrangepointsToCPointerType = new Vector();
    _1qrangepointsToCPointerType.addAll(cpointertypes);
    for (int _i = 0; _i < _1qrangepointsToCPointerType.size(); _i++)
    { CPointerType cpointertypex = (CPointerType) _1qrangepointsToCPointerType.get(_i);
      if (cpointertypex != null && ctypexx.equals(cpointertypex.getpointsTo()))
      { _1removedpointsToCPointerType.add(cpointertypex);
        cpointertypex.setpointsTo(null);
      }
    }
    Vector _1qrangedomainTypeCFunctionPointerType = new Vector();
    _1qrangedomainTypeCFunctionPointerType.addAll(cfunctionpointertypes);
    for (int _i = 0; _i < _1qrangedomainTypeCFunctionPointerType.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) _1qrangedomainTypeCFunctionPointerType.get(_i);
      if (cfunctionpointertypex != null && ctypexx.equals(cfunctionpointertypex.getdomainType()))
      { _1removeddomainTypeCFunctionPointerType.add(cfunctionpointertypex);
        cfunctionpointertypex.setdomainType(null);
      }
    }
    Vector _1qrangerangeTypeCFunctionPointerType = new Vector();
    _1qrangerangeTypeCFunctionPointerType.addAll(cfunctionpointertypes);
    for (int _i = 0; _i < _1qrangerangeTypeCFunctionPointerType.size(); _i++)
    { CFunctionPointerType cfunctionpointertypex = (CFunctionPointerType) _1qrangerangeTypeCFunctionPointerType.get(_i);
      if (cfunctionpointertypex != null && ctypexx.equals(cfunctionpointertypex.getrangeType()))
      { _1removedrangeTypeCFunctionPointerType.add(cfunctionpointertypex);
        cfunctionpointertypex.setrangeType(null);
      }
    }
    Vector _1qrangetypeCMember = new Vector();
    _1qrangetypeCMember.addAll(cmembers);
    for (int _i = 0; _i < _1qrangetypeCMember.size(); _i++)
    { CMember cmemberx = (CMember) _1qrangetypeCMember.get(_i);
      if (cmemberx != null && ctypexx.equals(cmemberx.gettype()))
      { _1removedtypeCMember.add(cmemberx);
        cmemberx.settype(null);
      }
    }
    ctypectypeIdindex.remove(ctypexx.getctypeId());
    for (int _i = 0; _i < _1removedtypeCVariable.size(); _i++)
    { killCVariable((CVariable) _1removedtypeCVariable.get(_i)); }
    for (int _i = 0; _i < _1removedreturnTypeCOperation.size(); _i++)
    { killCOperation((COperation) _1removedreturnTypeCOperation.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeCOperation.size(); _i++)
    { killCOperation((COperation) _1removedelementTypeCOperation.get(_i)); }
    for (int _i = 0; _i < _1removedcomponentTypeCArrayType.size(); _i++)
    { killCArrayType((CArrayType) _1removedcomponentTypeCArrayType.get(_i)); }
    for (int _i = 0; _i < _1removedpointsToCPointerType.size(); _i++)
    { killCPointerType((CPointerType) _1removedpointsToCPointerType.get(_i)); }
    for (int _i = 0; _i < _1removeddomainTypeCFunctionPointerType.size(); _i++)
    { killCFunctionPointerType((CFunctionPointerType) _1removeddomainTypeCFunctionPointerType.get(_i)); }
    for (int _i = 0; _i < _1removedrangeTypeCFunctionPointerType.size(); _i++)
    { killCFunctionPointerType((CFunctionPointerType) _1removedrangeTypeCFunctionPointerType.get(_i)); }
    for (int _i = 0; _i < _1removedtypeCMember.size(); _i++)
    { killCMember((CMember) _1removedtypeCMember.get(_i)); }
  }

  public void killAbstractCType(CType ctypexx)
  {
    if (ctypexx instanceof CStruct)
    { killCStruct((CStruct) ctypexx); }
    if (ctypexx instanceof CPrimitiveType)
    { killCPrimitiveType((CPrimitiveType) ctypexx); }
    if (ctypexx instanceof CPointerType)
    { killCPointerType((CPointerType) ctypexx); }
    if (ctypexx instanceof CFunctionPointerType)
    { killCFunctionPointerType((CFunctionPointerType) ctypexx); }
    if (ctypexx instanceof CArrayType)
    { killCArrayType((CArrayType) ctypexx); }
    if (ctypexx instanceof CEnumeration)
    { killCEnumeration((CEnumeration) ctypexx); }
  }

  public void killAbstractCType(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { CType _e = (CType) _l.get(_i);
      killAbstractCType(_e);
    }
  }



  public void killAllCOperation(List coperationxx)
  { for (int _i = 0; _i < coperationxx.size(); _i++)
    { killCOperation((COperation) coperationxx.get(_i)); }
  }

  public void killCOperation(COperation coperationxx)
  { if (coperationxx == null) { return; }
   coperations.remove(coperationxx);
    Vector _1qrangeoperationsCProgram = new Vector();
    _1qrangeoperationsCProgram.addAll(cprograms);
    for (int _i = 0; _i < _1qrangeoperationsCProgram.size(); _i++)
    { CProgram cprogramx = (CProgram) _1qrangeoperationsCProgram.get(_i);
      if (cprogramx != null && cprogramx.getoperations().contains(coperationxx))
      { removeoperations(cprogramx,coperationxx); }
    }
    coperationopIdindex.remove(coperationxx.getopId());
  }



  public void killAllCVariable(List cvariablexx)
  { for (int _i = 0; _i < cvariablexx.size(); _i++)
    { killCVariable((CVariable) cvariablexx.get(_i)); }
  }

  public void killCVariable(CVariable cvariablexx)
  { if (cvariablexx == null) { return; }
   cvariables.remove(cvariablexx);
    Vector _1qrangeparametersCOperation = new Vector();
    _1qrangeparametersCOperation.addAll(coperations);
    for (int _i = 0; _i < _1qrangeparametersCOperation.size(); _i++)
    { COperation coperationx = (COperation) _1qrangeparametersCOperation.get(_i);
      if (coperationx != null && coperationx.getparameters().contains(cvariablexx))
      { removeparameters(coperationx,cvariablexx); }
    }
    Vector _1qrangevariablesCProgram = new Vector();
    _1qrangevariablesCProgram.addAll(cprograms);
    for (int _i = 0; _i < _1qrangevariablesCProgram.size(); _i++)
    { CProgram cprogramx = (CProgram) _1qrangevariablesCProgram.get(_i);
      if (cprogramx != null && cprogramx.getvariables().contains(cvariablexx))
      { removevariables(cprogramx,cvariablexx); }
    }
  }



  public void killAllCProgram(List cprogramxx)
  { for (int _i = 0; _i < cprogramxx.size(); _i++)
    { killCProgram((CProgram) cprogramxx.get(_i)); }
  }

  public void killCProgram(CProgram cprogramxx)
  { if (cprogramxx == null) { return; }
   cprograms.remove(cprogramxx);
  }



  public void killAllCPrimitiveType(List cprimitivetypexx)
  { for (int _i = 0; _i < cprimitivetypexx.size(); _i++)
    { killCPrimitiveType((CPrimitiveType) cprimitivetypexx.get(_i)); }
  }

  public void killCPrimitiveType(CPrimitiveType cprimitivetypexx)
  { if (cprimitivetypexx == null) { return; }
   cprimitivetypes.remove(cprimitivetypexx);
  killCType(cprimitivetypexx);
  }



  public void killAllCArrayType(List carraytypexx)
  { for (int _i = 0; _i < carraytypexx.size(); _i++)
    { killCArrayType((CArrayType) carraytypexx.get(_i)); }
  }

  public void killCArrayType(CArrayType carraytypexx)
  { if (carraytypexx == null) { return; }
   carraytypes.remove(carraytypexx);
  killCType(carraytypexx);
  }



  public void killAllCPointerType(List cpointertypexx)
  { for (int _i = 0; _i < cpointertypexx.size(); _i++)
    { killCPointerType((CPointerType) cpointertypexx.get(_i)); }
  }

  public void killCPointerType(CPointerType cpointertypexx)
  { if (cpointertypexx == null) { return; }
   cpointertypes.remove(cpointertypexx);
  killCType(cpointertypexx);
  }



  public void killAllCFunctionPointerType(List cfunctionpointertypexx)
  { for (int _i = 0; _i < cfunctionpointertypexx.size(); _i++)
    { killCFunctionPointerType((CFunctionPointerType) cfunctionpointertypexx.get(_i)); }
  }

  public void killCFunctionPointerType(CFunctionPointerType cfunctionpointertypexx)
  { if (cfunctionpointertypexx == null) { return; }
   cfunctionpointertypes.remove(cfunctionpointertypexx);
  killCType(cfunctionpointertypexx);
  }



  public void killAllCEnumeration(List cenumerationxx)
  { for (int _i = 0; _i < cenumerationxx.size(); _i++)
    { killCEnumeration((CEnumeration) cenumerationxx.get(_i)); }
  }

  public void killCEnumeration(CEnumeration cenumerationxx)
  { if (cenumerationxx == null) { return; }
   cenumerations.remove(cenumerationxx);
  killCType(cenumerationxx);
  }



  public void killAllCEnumerationLiteral(List cenumerationliteralxx)
  { for (int _i = 0; _i < cenumerationliteralxx.size(); _i++)
    { killCEnumerationLiteral((CEnumerationLiteral) cenumerationliteralxx.get(_i)); }
  }

  public void killCEnumerationLiteral(CEnumerationLiteral cenumerationliteralxx)
  { if (cenumerationliteralxx == null) { return; }
   cenumerationliterals.remove(cenumerationliteralxx);
    Vector _1qrangeownedLiteralCEnumeration = new Vector();
    _1qrangeownedLiteralCEnumeration.addAll(cenumerations);
    for (int _i = 0; _i < _1qrangeownedLiteralCEnumeration.size(); _i++)
    { CEnumeration cenumerationx = (CEnumeration) _1qrangeownedLiteralCEnumeration.get(_i);
      if (cenumerationx != null && cenumerationx.getownedLiteral().contains(cenumerationliteralxx))
      { removeownedLiteral(cenumerationx,cenumerationliteralxx); }
    }
  }



  public void killAllBinaryExpression(List binaryexpressionxx)
  { for (int _i = 0; _i < binaryexpressionxx.size(); _i++)
    { killBinaryExpression((BinaryExpression) binaryexpressionxx.get(_i)); }
  }

  public void killBinaryExpression(BinaryExpression binaryexpressionxx)
  { if (binaryexpressionxx == null) { return; }
   binaryexpressions.remove(binaryexpressionxx);
  killExpression(binaryexpressionxx);
  }



  public void killAllConditionalExpression(List conditionalexpressionxx)
  { for (int _i = 0; _i < conditionalexpressionxx.size(); _i++)
    { killConditionalExpression((ConditionalExpression) conditionalexpressionxx.get(_i)); }
  }

  public void killConditionalExpression(ConditionalExpression conditionalexpressionxx)
  { if (conditionalexpressionxx == null) { return; }
   conditionalexpressions.remove(conditionalexpressionxx);
  killExpression(conditionalexpressionxx);
  }



  public void killAllUnaryExpression(List unaryexpressionxx)
  { for (int _i = 0; _i < unaryexpressionxx.size(); _i++)
    { killUnaryExpression((UnaryExpression) unaryexpressionxx.get(_i)); }
  }

  public void killUnaryExpression(UnaryExpression unaryexpressionxx)
  { if (unaryexpressionxx == null) { return; }
   unaryexpressions.remove(unaryexpressionxx);
  killExpression(unaryexpressionxx);
  }



  public void killAllCollectionExpression(List collectionexpressionxx)
  { for (int _i = 0; _i < collectionexpressionxx.size(); _i++)
    { killCollectionExpression((CollectionExpression) collectionexpressionxx.get(_i)); }
  }

  public void killCollectionExpression(CollectionExpression collectionexpressionxx)
  { if (collectionexpressionxx == null) { return; }
   collectionexpressions.remove(collectionexpressionxx);
  killExpression(collectionexpressionxx);
  }



  public void killAllBasicExpression(List basicexpressionxx)
  { for (int _i = 0; _i < basicexpressionxx.size(); _i++)
    { killBasicExpression((BasicExpression) basicexpressionxx.get(_i)); }
  }

  public void killBasicExpression(BasicExpression basicexpressionxx)
  { if (basicexpressionxx == null) { return; }
   basicexpressions.remove(basicexpressionxx);
  killExpression(basicexpressionxx);
  }



  public void killAllPrintcode(List printcodexx)
  { for (int _i = 0; _i < printcodexx.size(); _i++)
    { killPrintcode((Printcode) printcodexx.get(_i)); }
  }

  public void killPrintcode(Printcode printcodexx)
  { if (printcodexx == null) { return; }
   printcodes.remove(printcodexx);
  }



  public void killAllTypes2C(List types2cxx)
  { for (int _i = 0; _i < types2cxx.size(); _i++)
    { killTypes2C((Types2C) types2cxx.get(_i)); }
  }

  public void killTypes2C(Types2C types2cxx)
  { if (types2cxx == null) { return; }
   types2cs.remove(types2cxx);
  }



  public void killAllProgram2C(List program2cxx)
  { for (int _i = 0; _i < program2cxx.size(); _i++)
    { killProgram2C((Program2C) program2cxx.get(_i)); }
  }

  public void killProgram2C(Program2C program2cxx)
  { if (program2cxx == null) { return; }
   program2cs.remove(program2cxx);
  }



  public void killAllStatement(List statementxx)
  { for (int _i = 0; _i < statementxx.size(); _i++)
    { killStatement((Statement) statementxx.get(_i)); }
  }

  public void killStatement(Statement statementxx)
  { if (statementxx == null) { return; }
   statements.remove(statementxx);
    Vector _1removedactivityBehaviouralFeature = new Vector();
    Vector _1removedclassifierBehaviourUseCase = new Vector();
    Vector _1removedifPartConditionalStatement = new Vector();
    Vector _1removedbodyLoopStatement = new Vector();
    Vector _1removedbodyTryStatement = new Vector();
    Vector _1removedactionCatchStatement = new Vector();
    Vector _1removedbodyFinalStatement = new Vector();
    Vector _1qrangeactivityBehaviouralFeature = new Vector();
    _1qrangeactivityBehaviouralFeature.addAll(behaviouralfeatures);
    for (int _i = 0; _i < _1qrangeactivityBehaviouralFeature.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) _1qrangeactivityBehaviouralFeature.get(_i);
      if (behaviouralfeaturex != null && statementxx.equals(behaviouralfeaturex.getactivity()))
      { _1removedactivityBehaviouralFeature.add(behaviouralfeaturex);
        behaviouralfeaturex.setactivity(null);
      }
    }
    Vector _1qrangeclassifierBehaviourUseCase = new Vector();
    _1qrangeclassifierBehaviourUseCase.addAll(usecases);
    for (int _i = 0; _i < _1qrangeclassifierBehaviourUseCase.size(); _i++)
    { UseCase usecasex = (UseCase) _1qrangeclassifierBehaviourUseCase.get(_i);
      if (usecasex != null && statementxx.equals(usecasex.getclassifierBehaviour()))
      { _1removedclassifierBehaviourUseCase.add(usecasex);
        usecasex.setclassifierBehaviour(null);
      }
    }
    Vector _1qrangeifPartConditionalStatement = new Vector();
    _1qrangeifPartConditionalStatement.addAll(conditionalstatements);
    for (int _i = 0; _i < _1qrangeifPartConditionalStatement.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) _1qrangeifPartConditionalStatement.get(_i);
      if (conditionalstatementx != null && statementxx.equals(conditionalstatementx.getifPart()))
      { _1removedifPartConditionalStatement.add(conditionalstatementx);
        conditionalstatementx.setifPart(null);
      }
    }
    Vector _1qrangeelsePartConditionalStatement = new Vector();
    _1qrangeelsePartConditionalStatement.addAll(conditionalstatements);
    for (int _i = 0; _i < _1qrangeelsePartConditionalStatement.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) _1qrangeelsePartConditionalStatement.get(_i);
      if (conditionalstatementx != null && conditionalstatementx.getelsePart().contains(statementxx))
      { removeelsePart(conditionalstatementx,statementxx); }
    }
    Vector _1qrangestatementsSequenceStatement = new Vector();
    _1qrangestatementsSequenceStatement.addAll(sequencestatements);
    for (int _i = 0; _i < _1qrangestatementsSequenceStatement.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) _1qrangestatementsSequenceStatement.get(_i);
      if (sequencestatementx != null && sequencestatementx.getstatements().contains(statementxx))
      { removestatements(sequencestatementx,statementxx); }
    }
    Vector _1qrangebodyLoopStatement = new Vector();
    _1qrangebodyLoopStatement.addAll(loopstatements);
    for (int _i = 0; _i < _1qrangebodyLoopStatement.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) _1qrangebodyLoopStatement.get(_i);
      if (loopstatementx != null && statementxx.equals(loopstatementx.getbody()))
      { _1removedbodyLoopStatement.add(loopstatementx);
        loopstatementx.setbody(null);
      }
    }
    Vector _1qrangecatchClausesTryStatement = new Vector();
    _1qrangecatchClausesTryStatement.addAll(trystatements);
    for (int _i = 0; _i < _1qrangecatchClausesTryStatement.size(); _i++)
    { TryStatement trystatementx = (TryStatement) _1qrangecatchClausesTryStatement.get(_i);
      if (trystatementx != null && trystatementx.getcatchClauses().contains(statementxx))
      { removecatchClauses(trystatementx,statementxx); }
    }
    Vector _1qrangebodyTryStatement = new Vector();
    _1qrangebodyTryStatement.addAll(trystatements);
    for (int _i = 0; _i < _1qrangebodyTryStatement.size(); _i++)
    { TryStatement trystatementx = (TryStatement) _1qrangebodyTryStatement.get(_i);
      if (trystatementx != null && statementxx.equals(trystatementx.getbody()))
      { _1removedbodyTryStatement.add(trystatementx);
        trystatementx.setbody(null);
      }
    }
    Vector _1qrangeendStatementTryStatement = new Vector();
    _1qrangeendStatementTryStatement.addAll(trystatements);
    for (int _i = 0; _i < _1qrangeendStatementTryStatement.size(); _i++)
    { TryStatement trystatementx = (TryStatement) _1qrangeendStatementTryStatement.get(_i);
      if (trystatementx != null && trystatementx.getendStatement().contains(statementxx))
      { removeendStatement(trystatementx,statementxx); }
    }
    Vector _1qrangeactionCatchStatement = new Vector();
    _1qrangeactionCatchStatement.addAll(catchstatements);
    for (int _i = 0; _i < _1qrangeactionCatchStatement.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) _1qrangeactionCatchStatement.get(_i);
      if (catchstatementx != null && statementxx.equals(catchstatementx.getaction()))
      { _1removedactionCatchStatement.add(catchstatementx);
        catchstatementx.setaction(null);
      }
    }
    Vector _1qrangebodyFinalStatement = new Vector();
    _1qrangebodyFinalStatement.addAll(finalstatements);
    for (int _i = 0; _i < _1qrangebodyFinalStatement.size(); _i++)
    { FinalStatement finalstatementx = (FinalStatement) _1qrangebodyFinalStatement.get(_i);
      if (finalstatementx != null && statementxx.equals(finalstatementx.getbody()))
      { _1removedbodyFinalStatement.add(finalstatementx);
        finalstatementx.setbody(null);
      }
    }
    statementstatIdindex.remove(statementxx.getstatId());
    for (int _i = 0; _i < _1removedactivityBehaviouralFeature.size(); _i++)
    { killAbstractBehaviouralFeature((BehaviouralFeature) _1removedactivityBehaviouralFeature.get(_i)); }
    for (int _i = 0; _i < _1removedclassifierBehaviourUseCase.size(); _i++)
    { killUseCase((UseCase) _1removedclassifierBehaviourUseCase.get(_i)); }
    for (int _i = 0; _i < _1removedifPartConditionalStatement.size(); _i++)
    { killConditionalStatement((ConditionalStatement) _1removedifPartConditionalStatement.get(_i)); }
    for (int _i = 0; _i < _1removedbodyLoopStatement.size(); _i++)
    { killAbstractLoopStatement((LoopStatement) _1removedbodyLoopStatement.get(_i)); }
    for (int _i = 0; _i < _1removedbodyTryStatement.size(); _i++)
    { killTryStatement((TryStatement) _1removedbodyTryStatement.get(_i)); }
    for (int _i = 0; _i < _1removedactionCatchStatement.size(); _i++)
    { killCatchStatement((CatchStatement) _1removedactionCatchStatement.get(_i)); }
    for (int _i = 0; _i < _1removedbodyFinalStatement.size(); _i++)
    { killFinalStatement((FinalStatement) _1removedbodyFinalStatement.get(_i)); }
  }

  public void killAbstractStatement(Statement statementxx)
  {
    if (statementxx instanceof ReturnStatement)
    { killReturnStatement((ReturnStatement) statementxx); }
    if (statementxx instanceof BreakStatement)
    { killBreakStatement((BreakStatement) statementxx); }
    if (statementxx instanceof OperationCallStatement)
    { killOperationCallStatement((OperationCallStatement) statementxx); }
    if (statementxx instanceof ImplicitCallStatement)
    { killImplicitCallStatement((ImplicitCallStatement) statementxx); }
    if (statementxx instanceof SequenceStatement)
    { killSequenceStatement((SequenceStatement) statementxx); }
    if (statementxx instanceof ConditionalStatement)
    { killConditionalStatement((ConditionalStatement) statementxx); }
    if (statementxx instanceof AssignStatement)
    { killAssignStatement((AssignStatement) statementxx); }
    if (statementxx instanceof CreationStatement)
    { killCreationStatement((CreationStatement) statementxx); }
    if (statementxx instanceof CatchStatement)
    { killCatchStatement((CatchStatement) statementxx); }
    if (statementxx instanceof FinalStatement)
    { killFinalStatement((FinalStatement) statementxx); }
    if (statementxx instanceof TryStatement)
    { killTryStatement((TryStatement) statementxx); }
    if (statementxx instanceof AssertStatement)
    { killAssertStatement((AssertStatement) statementxx); }
    if (statementxx instanceof ErrorStatement)
    { killErrorStatement((ErrorStatement) statementxx); }
    if (statementxx instanceof BoundedLoopStatement)
    { killBoundedLoopStatement((BoundedLoopStatement) statementxx); }
    if (statementxx instanceof UnboundedLoopStatement)
    { killUnboundedLoopStatement((UnboundedLoopStatement) statementxx); }
  }

  public void killAbstractStatement(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Statement _e = (Statement) _l.get(_i);
      killAbstractStatement(_e);
    }
  }



  public void killAllReturnStatement(List returnstatementxx)
  { for (int _i = 0; _i < returnstatementxx.size(); _i++)
    { killReturnStatement((ReturnStatement) returnstatementxx.get(_i)); }
  }

  public void killReturnStatement(ReturnStatement returnstatementxx)
  { if (returnstatementxx == null) { return; }
   returnstatements.remove(returnstatementxx);
  killStatement(returnstatementxx);
  }



  public void killAllBreakStatement(List breakstatementxx)
  { for (int _i = 0; _i < breakstatementxx.size(); _i++)
    { killBreakStatement((BreakStatement) breakstatementxx.get(_i)); }
  }

  public void killBreakStatement(BreakStatement breakstatementxx)
  { if (breakstatementxx == null) { return; }
   breakstatements.remove(breakstatementxx);
  killStatement(breakstatementxx);
  }



  public void killAllOperationCallStatement(List operationcallstatementxx)
  { for (int _i = 0; _i < operationcallstatementxx.size(); _i++)
    { killOperationCallStatement((OperationCallStatement) operationcallstatementxx.get(_i)); }
  }

  public void killOperationCallStatement(OperationCallStatement operationcallstatementxx)
  { if (operationcallstatementxx == null) { return; }
   operationcallstatements.remove(operationcallstatementxx);
  killStatement(operationcallstatementxx);
  }



  public void killAllImplicitCallStatement(List implicitcallstatementxx)
  { for (int _i = 0; _i < implicitcallstatementxx.size(); _i++)
    { killImplicitCallStatement((ImplicitCallStatement) implicitcallstatementxx.get(_i)); }
  }

  public void killImplicitCallStatement(ImplicitCallStatement implicitcallstatementxx)
  { if (implicitcallstatementxx == null) { return; }
   implicitcallstatements.remove(implicitcallstatementxx);
  killStatement(implicitcallstatementxx);
  }



  public void killAllLoopStatement(List loopstatementxx)
  { for (int _i = 0; _i < loopstatementxx.size(); _i++)
    { killLoopStatement((LoopStatement) loopstatementxx.get(_i)); }
  }

  public void killLoopStatement(LoopStatement loopstatementxx)
  { if (loopstatementxx == null) { return; }
   loopstatements.remove(loopstatementxx);
  killStatement(loopstatementxx);
  }

  public void killAbstractLoopStatement(LoopStatement loopstatementxx)
  {
    if (loopstatementxx instanceof BoundedLoopStatement)
    { killBoundedLoopStatement((BoundedLoopStatement) loopstatementxx); }
    if (loopstatementxx instanceof UnboundedLoopStatement)
    { killUnboundedLoopStatement((UnboundedLoopStatement) loopstatementxx); }
  }

  public void killAbstractLoopStatement(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { LoopStatement _e = (LoopStatement) _l.get(_i);
      killAbstractLoopStatement(_e);
    }
  }



  public void killAllBoundedLoopStatement(List boundedloopstatementxx)
  { for (int _i = 0; _i < boundedloopstatementxx.size(); _i++)
    { killBoundedLoopStatement((BoundedLoopStatement) boundedloopstatementxx.get(_i)); }
  }

  public void killBoundedLoopStatement(BoundedLoopStatement boundedloopstatementxx)
  { if (boundedloopstatementxx == null) { return; }
   boundedloopstatements.remove(boundedloopstatementxx);
  killLoopStatement(boundedloopstatementxx);
  }



  public void killAllUnboundedLoopStatement(List unboundedloopstatementxx)
  { for (int _i = 0; _i < unboundedloopstatementxx.size(); _i++)
    { killUnboundedLoopStatement((UnboundedLoopStatement) unboundedloopstatementxx.get(_i)); }
  }

  public void killUnboundedLoopStatement(UnboundedLoopStatement unboundedloopstatementxx)
  { if (unboundedloopstatementxx == null) { return; }
   unboundedloopstatements.remove(unboundedloopstatementxx);
  killLoopStatement(unboundedloopstatementxx);
  }



  public void killAllAssignStatement(List assignstatementxx)
  { for (int _i = 0; _i < assignstatementxx.size(); _i++)
    { killAssignStatement((AssignStatement) assignstatementxx.get(_i)); }
  }

  public void killAssignStatement(AssignStatement assignstatementxx)
  { if (assignstatementxx == null) { return; }
   assignstatements.remove(assignstatementxx);
  killStatement(assignstatementxx);
  }



  public void killAllSequenceStatement(List sequencestatementxx)
  { for (int _i = 0; _i < sequencestatementxx.size(); _i++)
    { killSequenceStatement((SequenceStatement) sequencestatementxx.get(_i)); }
  }

  public void killSequenceStatement(SequenceStatement sequencestatementxx)
  { if (sequencestatementxx == null) { return; }
   sequencestatements.remove(sequencestatementxx);
  killStatement(sequencestatementxx);
  }



  public void killAllConditionalStatement(List conditionalstatementxx)
  { for (int _i = 0; _i < conditionalstatementxx.size(); _i++)
    { killConditionalStatement((ConditionalStatement) conditionalstatementxx.get(_i)); }
  }

  public void killConditionalStatement(ConditionalStatement conditionalstatementxx)
  { if (conditionalstatementxx == null) { return; }
   conditionalstatements.remove(conditionalstatementxx);
  killStatement(conditionalstatementxx);
  }



  public void killAllCreationStatement(List creationstatementxx)
  { for (int _i = 0; _i < creationstatementxx.size(); _i++)
    { killCreationStatement((CreationStatement) creationstatementxx.get(_i)); }
  }

  public void killCreationStatement(CreationStatement creationstatementxx)
  { if (creationstatementxx == null) { return; }
   creationstatements.remove(creationstatementxx);
  killStatement(creationstatementxx);
  }



  public void killAllErrorStatement(List errorstatementxx)
  { for (int _i = 0; _i < errorstatementxx.size(); _i++)
    { killErrorStatement((ErrorStatement) errorstatementxx.get(_i)); }
  }

  public void killErrorStatement(ErrorStatement errorstatementxx)
  { if (errorstatementxx == null) { return; }
   errorstatements.remove(errorstatementxx);
  killStatement(errorstatementxx);
  }



  public void killAllCatchStatement(List catchstatementxx)
  { for (int _i = 0; _i < catchstatementxx.size(); _i++)
    { killCatchStatement((CatchStatement) catchstatementxx.get(_i)); }
  }

  public void killCatchStatement(CatchStatement catchstatementxx)
  { if (catchstatementxx == null) { return; }
   catchstatements.remove(catchstatementxx);
  killStatement(catchstatementxx);
  }



  public void killAllFinalStatement(List finalstatementxx)
  { for (int _i = 0; _i < finalstatementxx.size(); _i++)
    { killFinalStatement((FinalStatement) finalstatementxx.get(_i)); }
  }

  public void killFinalStatement(FinalStatement finalstatementxx)
  { if (finalstatementxx == null) { return; }
   finalstatements.remove(finalstatementxx);
  killStatement(finalstatementxx);
  }



  public void killAllTryStatement(List trystatementxx)
  { for (int _i = 0; _i < trystatementxx.size(); _i++)
    { killTryStatement((TryStatement) trystatementxx.get(_i)); }
  }

  public void killTryStatement(TryStatement trystatementxx)
  { if (trystatementxx == null) { return; }
   trystatements.remove(trystatementxx);
  killStatement(trystatementxx);
  }



  public void killAllAssertStatement(List assertstatementxx)
  { for (int _i = 0; _i < assertstatementxx.size(); _i++)
    { killAssertStatement((AssertStatement) assertstatementxx.get(_i)); }
  }

  public void killAssertStatement(AssertStatement assertstatementxx)
  { if (assertstatementxx == null) { return; }
   assertstatements.remove(assertstatementxx);
  killStatement(assertstatementxx);
  }




  
  
  public void printcode() 
  { 

    List _range106 = new Vector();
  _range106.addAll(Controller.inst().cstructs);
  for (int _i105 = 0; _i105 < _range106.size(); _i105++)
  { CStruct loopvar$0 = (CStruct) _range106.get(_i105);
        Controller.inst().printcode1(loopvar$0);
  }
    List _range108 = new Vector();
  _range108.addAll(Controller.inst().cstructs);
  for (int _i107 = 0; _i107 < _range108.size(); _i107++)
  { CStruct loopvar$1 = (CStruct) _range108.get(_i107);
        Controller.inst().printcode2(loopvar$1);
  }
    List _range110 = new Vector();
  _range110.addAll(Controller.inst().cenumerations);
  for (int _i109 = 0; _i109 < _range110.size(); _i109++)
  { CEnumeration loopvar$2 = (CEnumeration) _range110.get(_i109);
        Controller.inst().printcode3(loopvar$2);
  }
    List _range112 = new Vector();
  _range112.addAll(Controller.inst().cstructs);
  for (int _i111 = 0; _i111 < _range112.size(); _i111++)
  { CStruct loopvar$3 = (CStruct) _range112.get(_i111);
        Controller.inst().printcode4outer(loopvar$3);
  }
    List _range114 = new Vector();
  _range114.addAll(Controller.inst().cstructs);
  for (int _i113 = 0; _i113 < _range114.size(); _i113++)
  { CStruct loopvar$4 = (CStruct) _range114.get(_i113);
        Controller.inst().printcode5outer(loopvar$4);
  }
    List _range116 = new Vector();
  _range116.addAll(Controller.inst().cstructs);
  for (int _i115 = 0; _i115 < _range116.size(); _i115++)
  { CStruct loopvar$5 = (CStruct) _range116.get(_i115);
        Controller.inst().printcode6outer(loopvar$5);
  }
    List _range118 = new Vector();
  _range118.addAll(Controller.inst().cstructs);
  for (int _i117 = 0; _i117 < _range118.size(); _i117++)
  { CStruct loopvar$6 = (CStruct) _range118.get(_i117);
        Controller.inst().printcode7outer(loopvar$6);
  }
    List _range120 = new Vector();
  _range120.addAll(Controller.inst().cstructs);
  for (int _i119 = 0; _i119 < _range120.size(); _i119++)
  { CStruct loopvar$7 = (CStruct) _range120.get(_i119);
        Controller.inst().printcode8outer(loopvar$7);
  }
    List _range122 = new Vector();
  _range122.addAll(Controller.inst().cstructs);
  for (int _i121 = 0; _i121 < _range122.size(); _i121++)
  { CStruct loopvar$8 = (CStruct) _range122.get(_i121);
        Controller.inst().printcode9outer(loopvar$8);
  }
    List _range124 = new Vector();
  _range124.addAll(Controller.inst().cstructs);
  for (int _i123 = 0; _i123 < _range124.size(); _i123++)
  { CStruct loopvar$9 = (CStruct) _range124.get(_i123);
        Controller.inst().printcode10outer(loopvar$9);
  }
    List _range126 = new Vector();
  _range126.addAll(Controller.inst().cstructs);
  for (int _i125 = 0; _i125 < _range126.size(); _i125++)
  { CStruct loopvar$10 = (CStruct) _range126.get(_i125);
        Controller.inst().printcode11(loopvar$10);
  }
    List _range128 = new Vector();
  _range128.addAll(Controller.inst().cstructs);
  for (int _i127 = 0; _i127 < _range128.size(); _i127++)
  { CStruct loopvar$11 = (CStruct) _range128.get(_i127);
        Controller.inst().printcode12outer(loopvar$11);
  }
    List _range130 = new Vector();
  _range130.addAll(Controller.inst().cstructs);
  for (int _i129 = 0; _i129 < _range130.size(); _i129++)
  { CStruct loopvar$12 = (CStruct) _range130.get(_i129);
        Controller.inst().printcode13(loopvar$12);
  }
    List _range132 = new Vector();
  _range132.addAll(Controller.inst().cstructs);
  for (int _i131 = 0; _i131 < _range132.size(); _i131++)
  { CStruct loopvar$13 = (CStruct) _range132.get(_i131);
        Controller.inst().printcode14(loopvar$13);
  }
    List _range134 = new Vector();
  _range134.addAll(Controller.inst().cstructs);
  for (int _i133 = 0; _i133 < _range134.size(); _i133++)
  { CStruct loopvar$14 = (CStruct) _range134.get(_i133);
        Controller.inst().printcode15(loopvar$14);
  }
    List _range136 = new Vector();
  _range136.addAll(Controller.inst().cstructs);
  for (int _i135 = 0; _i135 < _range136.size(); _i135++)
  { CStruct loopvar$15 = (CStruct) _range136.get(_i135);
        Controller.inst().printcode16(loopvar$15);
  }
    List _range138 = new Vector();
  _range138.addAll(Controller.inst().cstructs);
  for (int _i137 = 0; _i137 < _range138.size(); _i137++)
  { CStruct loopvar$16 = (CStruct) _range138.get(_i137);
        Controller.inst().printcode17(loopvar$16);
  }
    List _range140 = new Vector();
  _range140.addAll(Controller.inst().cstructs);
  for (int _i139 = 0; _i139 < _range140.size(); _i139++)
  { CStruct loopvar$17 = (CStruct) _range140.get(_i139);
        Controller.inst().printcode18(loopvar$17);
  }
    List _range142 = new Vector();
  _range142.addAll(Controller.inst().cstructs);
  for (int _i141 = 0; _i141 < _range142.size(); _i141++)
  { CStruct loopvar$18 = (CStruct) _range142.get(_i141);
        Controller.inst().printcode19(loopvar$18);
  }
    List _range144 = new Vector();
  _range144.addAll(Controller.inst().cstructs);
  for (int _i143 = 0; _i143 < _range144.size(); _i143++)
  { CStruct loopvar$19 = (CStruct) _range144.get(_i143);
        Controller.inst().printcode20(loopvar$19);
  }
    List _range146 = new Vector();
  _range146.addAll(Controller.inst().cstructs);
  for (int _i145 = 0; _i145 < _range146.size(); _i145++)
  { CStruct loopvar$20 = (CStruct) _range146.get(_i145);
        Controller.inst().printcode21(loopvar$20);
  }
    List _range148 = new Vector();
  _range148.addAll(Controller.inst().cstructs);
  for (int _i147 = 0; _i147 < _range148.size(); _i147++)
  { CStruct loopvar$21 = (CStruct) _range148.get(_i147);
        Controller.inst().printcode22(loopvar$21);
  }
    List _range150 = new Vector();
  _range150.addAll(Controller.inst().cstructs);
  for (int _i149 = 0; _i149 < _range150.size(); _i149++)
  { CStruct loopvar$22 = (CStruct) _range150.get(_i149);
        Controller.inst().printcode23(loopvar$22);
  }
    List _range152 = new Vector();
  _range152.addAll(Controller.inst().cstructs);
  for (int _i151 = 0; _i151 < _range152.size(); _i151++)
  { CStruct loopvar$23 = (CStruct) _range152.get(_i151);
        Controller.inst().printcode24(loopvar$23);
  }
    List _range154 = new Vector();
  _range154.addAll(Controller.inst().cstructs);
  for (int _i153 = 0; _i153 < _range154.size(); _i153++)
  { CStruct loopvar$24 = (CStruct) _range154.get(_i153);
        Controller.inst().printcode25(loopvar$24);
  }
    List _range156 = new Vector();
  _range156.addAll(Controller.inst().cstructs);
  for (int _i155 = 0; _i155 < _range156.size(); _i155++)
  { CStruct loopvar$25 = (CStruct) _range156.get(_i155);
        Controller.inst().printcode26(loopvar$25);
  }
    List _range158 = new Vector();
  _range158.addAll(Controller.inst().cstructs);
  for (int _i157 = 0; _i157 < _range158.size(); _i157++)
  { CStruct loopvar$26 = (CStruct) _range158.get(_i157);
        Controller.inst().printcode27(loopvar$26);
  }
    List _range160 = new Vector();
  _range160.addAll(Controller.inst().cstructs);
  for (int _i159 = 0; _i159 < _range160.size(); _i159++)
  { CStruct loopvar$27 = (CStruct) _range160.get(_i159);
        Controller.inst().printcode28(loopvar$27);
  }
    List _range162 = new Vector();
  _range162.addAll(Controller.inst().cstructs);
  for (int _i161 = 0; _i161 < _range162.size(); _i161++)
  { CStruct loopvar$28 = (CStruct) _range162.get(_i161);
        Controller.inst().printcode29(loopvar$28);
  }
    List _range164 = new Vector();
  _range164.addAll(Controller.inst().cstructs);
  for (int _i163 = 0; _i163 < _range164.size(); _i163++)
  { CStruct loopvar$29 = (CStruct) _range164.get(_i163);
        Controller.inst().printcode30(loopvar$29);
  }
    List _range166 = new Vector();
  _range166.addAll(Controller.inst().cstructs);
  for (int _i165 = 0; _i165 < _range166.size(); _i165++)
  { CStruct loopvar$30 = (CStruct) _range166.get(_i165);
        Controller.inst().printcode31(loopvar$30);
  }
    List _range168 = new Vector();
  _range168.addAll(Controller.inst().cstructs);
  for (int _i167 = 0; _i167 < _range168.size(); _i167++)
  { CStruct loopvar$31 = (CStruct) _range168.get(_i167);
        Controller.inst().printcode32(loopvar$31);
  }
    List _range170 = new Vector();
  _range170.addAll(Controller.inst().cprograms);
  for (int _i169 = 0; _i169 < _range170.size(); _i169++)
  { CProgram loopvar$32 = (CProgram) _range170.get(_i169);
        Controller.inst().printcode33(loopvar$32);
  }
    List _range172 = new Vector();
  _range172.addAll(Controller.inst().usecases);
  for (int _i171 = 0; _i171 < _range172.size(); _i171++)
  { UseCase loopvar$33 = (UseCase) _range172.get(_i171);
        Controller.inst().printcode34(loopvar$33);
  }

  }



  
  public void types2C() 
  { 

    List _range174 = new Vector();
  _range174.addAll(Controller.inst().primitivetypes);
  for (int _i173 = 0; _i173 < _range174.size(); _i173++)
  { PrimitiveType loopvar$34 = (PrimitiveType) _range174.get(_i173);
        Controller.inst().types2c1(loopvar$34);
  }
    List _range176 = new Vector();
  _range176.addAll(Controller.inst().primitivetypes);
  for (int _i175 = 0; _i175 < _range176.size(); _i175++)
  { PrimitiveType loopvar$35 = (PrimitiveType) _range176.get(_i175);
        Controller.inst().types2c2(loopvar$35);
  }
    List _range178 = new Vector();
  _range178.addAll(Controller.inst().primitivetypes);
  for (int _i177 = 0; _i177 < _range178.size(); _i177++)
  { PrimitiveType loopvar$36 = (PrimitiveType) _range178.get(_i177);
        Controller.inst().types2c3(loopvar$36);
  }
    List _range180 = new Vector();
  _range180.addAll(Controller.inst().primitivetypes);
  for (int _i179 = 0; _i179 < _range180.size(); _i179++)
  { PrimitiveType loopvar$37 = (PrimitiveType) _range180.get(_i179);
        Controller.inst().types2c4(loopvar$37);
  }
    List _range182 = new Vector();
  _range182.addAll(Controller.inst().primitivetypes);
  for (int _i181 = 0; _i181 < _range182.size(); _i181++)
  { PrimitiveType loopvar$38 = (PrimitiveType) _range182.get(_i181);
        Controller.inst().types2c5(loopvar$38);
  }
    List _range184 = new Vector();
  _range184.addAll(Controller.inst().primitivetypes);
  for (int _i183 = 0; _i183 < _range184.size(); _i183++)
  { PrimitiveType loopvar$39 = (PrimitiveType) _range184.get(_i183);
        Controller.inst().types2c6(loopvar$39);
  }
    List _range186 = new Vector();
  _range186.addAll(Controller.inst().primitivetypes);
  for (int _i185 = 0; _i185 < _range186.size(); _i185++)
  { PrimitiveType loopvar$40 = (PrimitiveType) _range186.get(_i185);
        Controller.inst().types2c7(loopvar$40);
  }
    List _range188 = new Vector();
  _range188.addAll(Controller.inst().primitivetypes);
  for (int _i187 = 0; _i187 < _range188.size(); _i187++)
  { PrimitiveType loopvar$41 = (PrimitiveType) _range188.get(_i187);
        Controller.inst().types2c8(loopvar$41);
  }
    List _range190 = new Vector();
  _range190.addAll(Controller.inst().entitys);
  for (int _i189 = 0; _i189 < _range190.size(); _i189++)
  { Entity loopvar$42 = (Entity) _range190.get(_i189);
        Controller.inst().types2c9(loopvar$42);
  }
    List _range192 = new Vector();
  _range192.addAll(Controller.inst().enumerations);
  for (int _i191 = 0; _i191 < _range192.size(); _i191++)
  { Enumeration loopvar$43 = (Enumeration) _range192.get(_i191);
        Controller.inst().types2c10(loopvar$43);
  }
    List _range194 = new Vector();
  _range194.addAll(Controller.inst().collectiontypes);
  for (int _i193 = 0; _i193 < _range194.size(); _i193++)
  { CollectionType loopvar$44 = (CollectionType) _range194.get(_i193);
        Controller.inst().types2c11(loopvar$44);
  }
    List _range196 = new Vector();
  _range196.addAll(Controller.inst().collectiontypes);
  for (int _i195 = 0; _i195 < _range196.size(); _i195++)
  { CollectionType loopvar$45 = (CollectionType) _range196.get(_i195);
        Controller.inst().types2c12(loopvar$45);
  }
    List _range198 = new Vector();
  _range198.addAll(Controller.inst().collectiontypes);
  for (int _i197 = 0; _i197 < _range198.size(); _i197++)
  { CollectionType loopvar$46 = (CollectionType) _range198.get(_i197);
        Controller.inst().types2c13(loopvar$46);
  }
    List _range200 = new Vector();
  _range200.addAll(Controller.inst().collectiontypes);
  for (int _i199 = 0; _i199 < _range200.size(); _i199++)
  { CollectionType loopvar$47 = (CollectionType) _range200.get(_i199);
        Controller.inst().types2c14(loopvar$47);
  }
    List _range202 = new Vector();
  _range202.addAll(Controller.inst().collectiontypes);
  for (int _i201 = 0; _i201 < _range202.size(); _i201++)
  { CollectionType loopvar$48 = (CollectionType) _range202.get(_i201);
        Controller.inst().types2c15(loopvar$48);
  }
    List _range204 = new Vector();
  _range204.addAll(Controller.inst().collectiontypes);
  for (int _i203 = 0; _i203 < _range204.size(); _i203++)
  { CollectionType loopvar$49 = (CollectionType) _range204.get(_i203);
        Controller.inst().types2c16(loopvar$49);
  }
    List _range206 = new Vector();
  _range206.addAll(Controller.inst().collectiontypes);
  for (int _i205 = 0; _i205 < _range206.size(); _i205++)
  { CollectionType loopvar$50 = (CollectionType) _range206.get(_i205);
        Controller.inst().types2c17(loopvar$50);
  }

  }



  
  public void program2C() 
  { 

    List _range208 = new Vector();
  _range208.addAll(Controller.inst().entitys);
  for (int _i207 = 0; _i207 < _range208.size(); _i207++)
  { Entity loopvar$51 = (Entity) _range208.get(_i207);
        Controller.inst().program2c1outer(loopvar$51);
  }
    List _range210 = new Vector();
  _range210.addAll(Controller.inst().operations);
  for (int _i209 = 0; _i209 < _range210.size(); _i209++)
  { Operation loopvar$52 = (Operation) _range210.get(_i209);
        Controller.inst().program2c2(loopvar$52);
  }
    List _range212 = new Vector();
  _range212.addAll(Controller.inst().entitys);
  for (int _i211 = 0; _i211 < _range212.size(); _i211++)
  { Entity loopvar$53 = (Entity) _range212.get(_i211);
        Controller.inst().program2c3outer(loopvar$53);
  }
    List _range214 = new Vector();
  _range214.addAll(Controller.inst().operations);
  for (int _i213 = 0; _i213 < _range214.size(); _i213++)
  { Operation loopvar$54 = (Operation) _range214.get(_i213);
        Controller.inst().program2c4(loopvar$54);
  }
    List _range216 = new Vector();
  _range216.addAll(Controller.inst().generalizations);
  for (int _i215 = 0; _i215 < _range216.size(); _i215++)
  { Generalization loopvar$55 = (Generalization) _range216.get(_i215);
        Controller.inst().program2c5(loopvar$55);
  }
    List _range218 = new Vector();
  _range218.addAll(Controller.inst().generalizations);
  for (int _i217 = 0; _i217 < _range218.size(); _i217++)
  { Generalization loopvar$56 = (Generalization) _range218.get(_i217);
        Controller.inst().program2c6(loopvar$56);
  }
     CProgram p = new CProgram();
    Controller.inst().addCProgram(p);
    Controller.inst().setstructs(p,Controller.inst().cstructs);
    Controller.inst().setoperations(p,Controller.inst().coperations);
    List _range220 = new Vector();
  _range220.addAll(Controller.inst().entitys);
  for (int _i219 = 0; _i219 < _range220.size(); _i219++)
  { Entity loopvar$57 = (Entity) _range220.get(_i219);
        Controller.inst().program2c8outer(loopvar$57);
  }

  }

  public static void main(String[] args)
  { Controller c = Controller.inst();
    c.loadModel("model.txt");  
    c.checkCompleteness(); 
 
    // Date d1 = new Date(); 
    // long t1 = d1.getTime(); 
 
    c.types2C(); 
    c.program2C(); 
    c.printcode(); 

    /* Date d2 = new Date(); 
    long t2 = d2.getTime();
    System.out.println(t2 - t1); */ 
 
    c.saveModel("cmodel.txt"); 
	
	System.out.println("/* Header code written to app.h                          */"); 
	System.out.println("/* Please note that empty structs are not valid in C,    */"); 
	System.out.println("/* Add a field to any such structs.       */"); 
  }

 
}



