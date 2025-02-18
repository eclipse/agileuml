package uml2py3;


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
  protected Type type;
  protected Type elementType;

  public Feature(Type type,Type elementType)
  {
    this.type = type;
    this.elementType = elementType;

  }

  public Feature() { }



  public String toString()
  { String _res_ = "(Feature) ";
    return _res_ + super.toString();
  }



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
  protected boolean isSorted = false; // internal

  public Type()
  {
    this.typeId = "";
    this.isSorted = false;

  }



  public String toString()
  { String _res_ = "(Type) ";
    _res_ = _res_ + typeId + ",";
    _res_ = _res_ + isSorted;
    return _res_ + super.toString();
  }



  public void settypeId(String typeId_x) { typeId = typeId_x;  }


    public static void setAlltypeId(List types,String val)
  { for (int i = 0; i < types.size(); i++)
    { Type typex = (Type) types.get(i);
      Controller.inst().settypeId(typex,val); } }


  public void setisSorted(boolean isSorted_x) { isSorted = isSorted_x;  }


    public static void setAllisSorted(List types,boolean val)
  { for (int i = 0; i < types.size(); i++)
    { Type typex = (Type) types.get(i);
      Controller.inst().setisSorted(typex,val); } }


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

    public boolean getisSorted() { return isSorted; }

    public static List getAllisSorted(List types)
  { List result = new Vector();
    for (int i = 0; i < types.size(); i++)
    { Type typex = (Type) types.get(i);
      if (result.contains(new Boolean(typex.getisSorted()))) { }
      else { result.add(new Boolean(typex.getisSorted())); } }
    return result; }

    public static List getAllOrderedisSorted(List types)
  { List result = new Vector();
    for (int i = 0; i < types.size(); i++)
    { Type typex = (Type) types.get(i);
      result.add(new Boolean(typex.getisSorted())); } 
    return result; }

    public abstract String defaultInitialValue();



    public String toPython()
  {   String result = "";
 
  result = name;
    return result;
  }


    public boolean isStringType()
  {   boolean result = false;
 
  if (((String) name).equals("String")) 
  {   result = true;
 
  }    return result;
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

    public String defaultInitialValue()
  {   String result = "";
 
  result = name + "." + ((EnumerationLiteral) ownedLiteral.get(0)).getname();
    return result;
  }


    public String toPython()
  {   String result = "";
 
  result = name;
    return result;
  }


    public String literals()
  {   String result = "";
 
  result = Set.sumString(Set.collect_0(ownedLiteral,this));
    return result;
  }


    public void printcode2()
  {   System.out.println("" + ( "class " + this.getname() + "(Enum) :" ));

      System.out.println("" + this.literals());

      System.out.println("" + "\n");

  }


}


class EnumerationLiteral
  extends NamedElement
  implements SystemTypes
{

  public EnumerationLiteral()
  {

  }



  public String toString()
  { String _res_ = "(EnumerationLiteral) ";
    return _res_ + super.toString();
  }

  public static EnumerationLiteral parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    EnumerationLiteral enumerationliteralx = new EnumerationLiteral();
    return enumerationliteralx;
  }


  public void writeCSV(PrintWriter _out)
  { EnumerationLiteral enumerationliteralx = this;
    _out.println();
  }



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


    public static boolean isPrimitiveType(String s)
  {   boolean result = false;
 
  if (((String) s).equals("double")) 
  {   result = true;
 
  }  else
      {   if (((String) s).equals("long")) 
  {   result = true;
 
  }  else
      {   if (((String) s).equals("String")) 
  {   result = true;
 
  }  else
      {   if (((String) s).equals("boolean")) 
  {   result = true;
 
  }  else
      {   if (((String) s).equals("int")) 
  {   result = true;
 
  }  else
      {   if (((String) s).equals("OclType")) 
  {   result = true;
 
  }  else
      if (((String) s).equals("OclVoid")) 
  {   result = true;
 
  }   
   } 
   } 
   } 
   } 
   }     return result;
  }


    public static boolean isPythonPrimitiveType(String s)
  {   boolean result = false;
 
  if (((String) s).equals("float")) 
  {   result = true;
 
  }  else
      {   if (((String) s).equals("int")) 
  {   result = true;
 
  }  else
      {   if (((String) s).equals("str")) 
  {   result = true;
 
  }  else
      if (((String) s).equals("bool")) 
  {   result = true;
 
  }   
   } 
   }     return result;
  }


    public String defaultInitialValue()
  {   String result = "";
 
  if (((String) name).equals("double")) 
  {   result = "0.0";
 
  }  else
      {   if (((String) name).equals("String")) 
  {   result = "\"\"";
 
  }  else
      {   if (((String) name).equals("boolean")) 
  {   result = "False";
 
  }  else
      {   if (((String) name).equals("int")) 
  {   result = "0";
 
  }  else
      {   if (((String) name).equals("long")) 
  {   result = "0";
 
  }  else
      if (true) 
  {   result = "None";
 
  }   
   } 
   } 
   } 
   }     return result;
  }


    public String toPython()
  {   String result = "";
 
  if (((String) name).equals("double")) 
  {   result = "float";
 
  }  else
      {   if (((String) name).equals("long")) 
  {   result = "int";
 
  }  else
      {   if (((String) name).equals("String")) 
  {   result = "str";
 
  }  else
      {   if (((String) name).equals("boolean")) 
  {   result = "bool";
 
  }  else
      {   if (((String) name).equals("OclType")) 
  {   result = "type";
 
  }  else
      {   if (((String) name).equals("OclVoid")) 
  {   result = "None";
 
  }  else
      {   if (((String) name).equals("OclException")) 
  {   result = "BaseException";
 
  }  else
      if (true) 
  {   result = name;
 
  }   
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }



}


class Entity
  extends Classifier
  implements SystemTypes
{
  private boolean isAbstract = false; // internal
  private boolean isInterface = false; // internal
  private List stereotypes = new Vector(); // internal
  private List generalization = new Vector(); // of Generalization
  private List specialization = new Vector(); // of Generalization
  private List ownedOperation = new Vector(); // of Operation
  private List ownedAttribute = new Vector(); // of Property
  private List superclass = new Vector(); // of Entity
  private List subclasses = new Vector(); // of Entity

  public Entity()
  {
    this.isAbstract = false;
    this.isInterface = false;
    this.stereotypes = (new SystemTypes.Set()).getElements();

  }



  public String toString()
  { String _res_ = "(Entity) ";
    _res_ = _res_ + isAbstract + ",";
    _res_ = _res_ + isInterface + ",";
    _res_ = _res_ + stereotypes;
    return _res_ + super.toString();
  }

  public static Entity parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Entity entityx = new Entity();
    entityx.isAbstract = Boolean.parseBoolean((String) _line1vals.get(0));
    entityx.isInterface = Boolean.parseBoolean((String) _line1vals.get(1));
    for (int _i = 0; _i < _line1vals.size(); _i++)
    { entityx.stereotypes.add((String) _line1vals.get(_i)); }
;
    return entityx;
  }


  public void writeCSV(PrintWriter _out)
  { Entity entityx = this;
    _out.print("" + entityx.isAbstract);
    _out.print(" , ");
    _out.print("" + entityx.isInterface);
    _out.print(" , ");
    _out.print("" + entityx.stereotypes);
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


  public void setstereotypes(List stereotypes_x) { stereotypes = stereotypes_x;  }


  public void setstereotypes(int _ind, String stereotypes_x) { stereotypes.set(_ind, stereotypes_x);  }


    public void addstereotypes(String stereotypes_x)
  { stereotypes.add(stereotypes_x); }

  public void removestereotypes(String stereotypes_x)
  { Vector _removedstereotypes = new Vector();
    _removedstereotypes.add(stereotypes_x);
    stereotypes.removeAll(_removedstereotypes);
  }



  

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


  public void setownedAttribute(List ownedAttribute_xx) { ownedAttribute = ownedAttribute_xx;
    }
 
  public void addownedAttribute(Property ownedAttribute_xx) { if (ownedAttribute.contains(ownedAttribute_xx)) {} else { ownedAttribute.add(ownedAttribute_xx); }
    }
 
  public void removeownedAttribute(Property ownedAttribute_xx) { Vector _removedownedAttributeownedAttribute_xx = new Vector();
  _removedownedAttributeownedAttribute_xx.add(ownedAttribute_xx);
  ownedAttribute.removeAll(_removedownedAttributeownedAttribute_xx);
    }

  public static void setAllownedAttribute(List entitys, List _val)
  { for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx = (Entity) entitys.get(_i);
      Controller.inst().setownedAttribute(entityx, _val); } }

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

    public List getstereotypes() { return stereotypes; }

  

  

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
      result = Set.union(result, entityx.getownedAttribute()); }
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

    public List allLeafSubclasses()
  {   List result = new Vector();
 
  if (specialization.size() == 0) 
  {   result = (new SystemTypes.Set()).add(this).getElements();
 
  }  else
      if (specialization.size() > 0) 
  {   result = Set.unionAll(Set.collect_1(specialization));
 
  }       return result;
  }


    public List allProperties()
  {   List result = new Vector();
 
  if (generalization.size() == 0) 
  {   result = ownedAttribute;
 
  }  else
      if (generalization.size() > 0) 
  {   result = ( Set.union(ownedAttribute,  Controller.inst().AllEntityallProperties(Generalization.getAllgeneral(generalization))) );
 
  }       return result;
  }


    public List allOperations()
  {   List result = new Vector();
 
  final List oonames = Set.collect_2(this.getownedOperation()); 
     if (generalization.size() == 0) 
  {   result = this.getownedOperation();
 
  }  else
      if (generalization.size() > 0) 
  {   result = ( Set.concatenate(this.getownedOperation(), Set.select_3(  Controller.inst().AllEntityallOperations(Generalization.getAllgeneral(generalization)),oonames)) );
 
  }          return result;
  }


    public boolean isApplicationClass()
  {   boolean result = false;
 
  if (( !(stereotypes.contains("external")) && !(stereotypes.contains("component")) )) 
  {   result = true;
 
  }    return result;
  }


    public boolean isActiveClass()
  {   boolean result = false;
 
  if (stereotypes.contains("active")) 
  {   result = true;
 
  }    return result;
  }


    public boolean isSingleValued(String d)
  {   boolean result = false;
 
  if (Set.exists_4(this.allProperties(),d)) 
  {   result = true;
 
  }    return result;
  }


    public boolean isSetValued(String d)
  {   boolean result = false;
 
  if (Set.exists_5(this.allProperties(),d)) 
  {   result = true;
 
  }    return result;
  }


    public boolean isSequenceValued(String d)
  {   boolean result = false;
 
  if (Set.exists_6(this.allProperties(),d)) 
  {   result = true;
 
  }    return result;
  }


    public String defaultInitialValue()
  {   String result = "";
 
  result = "None";
    return result;
  }


    public String initialisations()
  {   String result = "";
 
  result = Set.sumString(Set.collect_8(Set.select_7(this.allProperties())));
    return result;
  }


    public String generalisationString()
  {   String result = "";
 
  if (generalization.size() == 0) 
  {   result = "";
 
  }  else
      if (generalization.size() > 0) 
  {   result = "(" + ((Generalization) Set.any(generalization)).getgeneral().getname() + ")";
 
  }       return result;
  }


    public String classHeader()
  {   String result = "";
 
  if (superclass.size() == 0) 
  {   result = "class " + name + " : ";
 
  }  else
      {   if (Set.exists_9(superclass)) 
  {   result = "class " + name + "(" + ((Entity) Set.any(superclass)).getname() + ") : ";
 
  }  else
      if (true) 
  {   result = "class " + name + " : ";
 
  }   
   }     return result;
  }


    public String allStaticCaches()
  {   String result = "";
 
  result = Set.sumString(Set.collect_11(Set.select_10(ownedOperation)));
    return result;
  }


    public String allInstanceCaches()
  {   String result = "";
 
  result = Set.sumString(Set.collect_13(Set.select_12(ownedOperation)));
    return result;
  }


    public String staticAttributes()
  {   String result = "";
 
  result = "  " + name.toLowerCase() + "_instances = []\n" + "  " + name.toLowerCase() + "_index = dict({})\n" + Set.sumString(Set.collect_15(Set.select_14(this.allProperties()))) + this.allStaticCaches();
    return result;
  }


    public String classConstructor()
  {   String result = "";
 
  result = "  def __init__(self):\n" + this.initialisations() + "    " + name + "." + name.toLowerCase() + "_instances.append(self)\n" + this.allInstanceCaches() + "\n";
    return result;
  }


    public String abstractClassConstructor()
  {   String result = "";
 
  result = "  def __init__(self):\n" + "    " + name + "." + name.toLowerCase() + "_instances.append(self)\n";
    return result;
  }


    public String callOp()
  {   String result = "";
 
  if (this.isActiveClass()) 
  {   result = "  def __call__(self):\n" + "    self.run()\n\n";
 
  }  else
      if (true) 
  {   result = "";
 
  }       return result;
  }


    public String createOp()
  {   String result = "";
 
  result = "def create" + name + "():\n" + "  " + name.toLowerCase() + " = " + name + "()\n" + "  return " + name.toLowerCase() + "\n";
    return result;
  }


    public String createPKOp(String key)
  {   String result = "";
 
  result = "def createByPK" + name + "(_value):\n" + "  result = get" + name + "ByPK(_value)\n" + "  if (result != None) : \n" + "    return result\n" + "  else :\n" + "    result = " + name + "()\n" + "    result." + key + " = _value\n" + "    " + name + "." + name.toLowerCase() + "_index[_value] = result\n" + "    return result\n";
    return result;
  }


    public static String deleteOp()
  {   String result = "";
 
  result = "def free(x):\n" + "  del x\n\n";
    return result;
  }


    public static String instancesOps(List leafs)
  {   String result = "";
 
  if (leafs.size() == 1) 
  {   result = "allInstances_" + ((Entity) Set.first(leafs)).getname() + "()";
 
  }  else
      if (leafs.size() > 1) 
  {   result = "ocl.union(allInstances_" + ((Entity) Set.first(leafs)).getname() + "(), " + Entity.instancesOps(Set.tail(leafs)) + ")";
 
  }       return result;
  }


    public String createOclTypeOp()
  {   String result = "";
 
  final String typename = name.toLowerCase() + "_OclType"; 
     result = typename + " = createByPKOclType(\"" + name + "\")\n" + typename + ".instance = create" + name + "()\n" + typename + ".actualMetatype = type(" + typename + ".instance)\n";
       return result;
  }


    public String allInstancesOp()
  {   String result = "";
 
  if (specialization.size() == 0) 
  {   result = "def allInstances_" + name + "():\n" + "  return " + name + "." + name.toLowerCase() + "_instances\n";
 
  }  else
      if (specialization.size() > 0) 
  {   result = "def allInstances_" + name + "():\n" + "  return " + Entity.instancesOps(this.allLeafSubclasses());
 
  }       return result;
  }


    public static String displayOps()
  {   String result = "";
 
  result = "def displayint(x):\n" + "  print(str(x))\n\n" + "def displaylong(x):\n" + "  print(str(x))\n\n" + "def displaydouble(x):\n" + "  print(str(x))\n\n" + "def displayboolean(x):\n" + "  print(str(x))\n\n" + "def displayString(x):\n" + "  print(x)\n\n" + "def displaySequence(x):\n" + "  print(x)\n\n" + "def displaySet(x):\n" + "  print(x)\n\n" + "def displayMap(x):\n" + "  print(x)\n\n";
    return result;
  }


    public void printcode3()
  {   if (this.getisInterface() == true && this.isApplicationClass()) 
  {   System.out.println("" + this.classHeader());

      System.out.println("" + this.staticAttributes());

      System.out.println("" + this.abstractClassConstructor());

        List _operation_list8 = new Vector();
    _operation_list8.addAll(this.allOperations());
    for (int _ind9 = 0; _ind9 < _operation_list8.size(); _ind9++)
    { Operation op = (Operation) _operation_list8.get(_ind9);
      Controller.inst().displayOperation(op,2);
    }
}
  }

    public void printcode4()
  {   if (this.getisInterface() == false && this.isApplicationClass()) 
  {   System.out.println("" + this.classHeader());

      System.out.println("" + this.staticAttributes());

      System.out.println("" + this.classConstructor());

      System.out.println("" + this.callOp());

        List _operation_list10 = new Vector();
    _operation_list10.addAll(this.allOperations());
    for (int _ind11 = 0; _ind11 < _operation_list10.size(); _ind11++)
    { Operation op = (Operation) _operation_list10.get(_ind11);
      Controller.inst().displayOperation(op,2);
    }
}
  }

    public void printcode5()
  {   if (this.getisInterface() == false && this.isApplicationClass()) 
  {   System.out.println("" + this.createOp());

      System.out.println("" + this.allInstancesOp());

      System.out.println("" + "");

      System.out.println("" + this.createOclTypeOp());

      System.out.println("" + "");
}
  }

    public void printcode6()
  {   if (this.getisInterface() == true && this.isApplicationClass()) 
  {   System.out.println("" + this.allInstancesOp());

      System.out.println("" + "");
}
  }

    public void printcode7(Property key)
  {   System.out.println("" + key.getPKOp(this.getname()));

      System.out.println("" + key.getPKOps(this.getname()));

      System.out.println("" + this.createPKOp(key.getname()));

  }

    public void printcode7outer()
  {  Entity entityx = this;
      if (entityx.isApplicationClass())
    {     if (Set.exists_16(entityx.allProperties()))
    {   Property key = ((Property) Set.any(Set.select_17(entityx.allProperties())));
      this.printcode7(key);
 }
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

    public String defaultInitialValue()
  {   String result = "";
 
  if (((String) name).equals("Sequence") && isSorted == false) 
  {   result = "[]";
 
  }  else
      {   if (((String) name).equals("Sequence") && isSorted == true) 
  {   result = "SortedList([])";
 
  }  else
      {   if (((String) name).equals("Set") && isSorted == true) 
  {   result = "SortedSet({})";
 
  }  else
      {   if (((String) name).equals("Set")) 
  {   result = "set({})";
 
  }  else
      {   if (((String) name).equals("Map") && isSorted == true) 
  {   result = "SortedDict({})";
 
  }  else
      {   if (((String) name).equals("Map")) 
  {   result = "dict({})";
 
  }  else
      {   if (((String) name).equals("Function")) 
  {   result = "None";
 
  }  else
      if (((String) name).equals("Ref")) 
  {   result = "[" + elementType.defaultInitialValue() + "]";
 
  }   
   } 
   } 
   } 
   } 
   } 
   }     return result;
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

    public static String tolist(List s)
  {   String result = "";
 
  if (s.size() == 0) 
  {   result = "";
 
  }  else
      {   if (s.size() == 1) 
  {   result = ((String) Set.first(s));
 
  }  else
      if (s.size() > 1) 
  {   result = ((String) Set.first(s)) + "," + Expression.tolist(Set.tail(s));
 
  }   
   }     return result;
  }


    public static String toLambdaList(String vbl,List s)
  {   String result = "";
 
  if (s.size() == 0) 
  {   result = "";
 
  }  else
      {   if (s.size() == 1) 
  {   result = "lambda " + vbl + " : " + ((String) Set.first(s));
 
  }  else
      if (s.size() > 1) 
  {   result = "lambda " + vbl + " : " + ((String) Set.first(s)) + ", " + Expression.toLambdaList(vbl,Set.tail(s));
 
  }   
   }     return result;
  }


    public static String maptolist(List s)
  {   String result = "";
 
  if (s.size() == 0) 
  {   result = "";
 
  }  else
      {   if (s.size() == 1) 
  {   result = ((String) Set.first(s));
 
  }  else
      if (s.size() > 1) 
  {   result = ((String) Set.first(s)) + "," + Expression.maptolist(Set.tail(s));
 
  }   
   }     return result;
  }


    public String typeName()
  {   String result = "";
 
  if ((type == null)) 
  {   result = "";
 
  }  else
      {   if (type.getisSorted() == true) 
  {   result = "Sorted" + type.getname();
 
  }  else
      if (type.getisSorted() == false) 
  {   result = type.getname();
 
  }   
   }     return result;
  }


    public static String indexstring(List ax,List aind)
  {   String result = "";
 
  if (aind.size() > 0 && ax.contains("String")) 
  {   result = "[" + ((String) Set.any(aind)) + "]";
 
  }  else
      {   if (aind.size() > 0 && !(ax.contains("String"))) 
  {   result = "[" + ((String) Set.any(aind)) + " -1]";
 
  }  else
      if (aind.size() == 0) 
  {   result = "";
 
  }   
   }     return result;
  }


    public static String parstring(List pars)
  {   String result = "";
 
  if (pars.size() == 1) 
  {   result = "(" + ((String) Set.first(pars)) + ")";
 
  }  else
      {   if (pars.size() > 1) 
  {   result = "(" + ((String) Set.first(pars)) + Set.sumString(Set.collect_18(Set.tail(pars))) + ")";
 
  }  else
      if (pars.size() == 0) 
  {   result = "()";
 
  }   
   }     return result;
  }


    public static String leftBracket(String tn)
  {   String result = "";
 
  if (((String) tn).equals("Set")) 
  {   result = "set({";
 
  }  else
      {   if (((String) tn).equals("SortedSet")) 
  {   result = "SortedSet({";
 
  }  else
      {   if (((String) tn).equals("Sequence")) 
  {   result = "[";
 
  }  else
      {   if (((String) tn).equals("Map")) 
  {   result = "dict({";
 
  }  else
      if (((String) tn).equals("SortedMap")) 
  {   result = "SortedDict({";
 
  }   
   } 
   } 
   }     return result;
  }


    public static String rightBracket(String tn)
  {   String result = "";
 
  if (((String) tn).equals("Set")) 
  {   result = "})";
 
  }  else
      {   if (((String) tn).equals("SortedSet")) 
  {   result = "})";
 
  }  else
      {   if (((String) tn).equals("Sequence")) 
  {   result = "]";
 
  }  else
      {   if (((String) tn).equals("Map")) 
  {   result = "})";
 
  }  else
      if (((String) tn).equals("SortedMap")) 
  {   result = "})";
 
  }   
   } 
   } 
   }     return result;
  }


    public abstract Expression addReference(BasicExpression x);



    public boolean isCollection()
  {   boolean result = false;
 
  if ((type == null)) 
  {   result = false;
 
  }  else
      {   if (((String) type.getname()).equals("Set")) 
  {   result = true;
 
  }  else
      if (((String) type.getname()).equals("Sequence")) 
  {   result = true;
 
  }   
   }     return result;
  }


    public boolean isSet()
  {   boolean result = false;
 
  if ((type == null)) 
  {   result = false;
 
  }  else
      if (((String) type.getname()).equals("Set")) 
  {   result = true;
 
  }       return result;
  }


    public boolean isMap()
  {   boolean result = false;
 
  if ((type == null)) 
  {   result = false;
 
  }  else
      if (((String) type.getname()).equals("Map")) 
  {   result = true;
 
  }       return result;
  }


    public boolean isNumeric()
  {   boolean result = false;
 
  if ((type == null)) 
  {   result = false;
 
  }  else
      if (((String) type.getname()).equals("int") || ((String) type.getname()).equals("long") || ((String) type.getname()).equals("double")) 
  {   result = true;
 
  }       return result;
  }


    public boolean isString()
  {   boolean result = false;
 
  if ((type == null)) 
  {   result = false;
 
  }  else
      if (((String) type.getname()).equals("String")) 
  {   result = true;
 
  }       return result;
  }


    public boolean isRef()
  {   boolean result = false;
 
  if ((type == null)) 
  {   result = false;
 
  }  else
      if (((String) type.getname()).equals("Ref")) 
  {   result = true;
 
  }       return result;
  }


    public boolean isEnumeration()
  {   boolean result = false;
 
  if ((type == null)) 
  {   result = false;
 
  }  else
      if (Set.collect_19(Controller.inst().enumerations).contains(type.getname())) 
  {   result = true;
 
  }       return result;
  }


    public static boolean isUnaryCollectionOp(String fname)
  {   boolean result = false;
 
  if (( ((String) fname).equals("->size") || ((String) fname).equals("->any") || ((String) fname).equals("->reverse") || ((String) fname).equals("->front") || ((String) fname).equals("->tail") || ((String) fname).equals("->first") || ((String) fname).equals("->last") || ((String) fname).equals("->sort") || ((String) fname).equals("->asSet") || ((String) fname).equals("->asSequence") || ((String) fname).equals("->asOrderedSet") || ((String) fname).equals("->asBag") )) 
  {   result = true;
 
  }    return result;
  }


    public abstract String toPython();



    public abstract String updateForm();




}


class BinaryExpression
  extends Expression
  implements SystemTypes
{
  private String operator = ""; // internal
  private String variable = ""; // internal
  private Expression left;
  private Expression right;
  private Property accumulator;

  public BinaryExpression(Expression left,Expression right,Property accumulator)
  {
    this.operator = "";
    this.variable = "";
    this.left = left;
    this.right = right;
    this.accumulator = accumulator;

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

  public void setaccumulator(Property accumulator_xx) { accumulator = accumulator_xx;
  }

  public static void setAllaccumulator(List binaryexpressions, Property _val)
  { for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      Controller.inst().setaccumulator(binaryexpressionx, _val); } }

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

  public Property getaccumulator() { return accumulator; }

  public static List getAllaccumulator(List binaryexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      if (result.contains(binaryexpressionx.getaccumulator())) {}
      else { result.add(binaryexpressionx.getaccumulator()); }
 }
    return result; }

  public static List getAllOrderedaccumulator(List binaryexpressions)
  { List result = new Vector();
    for (int _i = 0; _i < binaryexpressions.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressions.get(_i);
      if (result.contains(binaryexpressionx.getaccumulator())) {}
      else { result.add(binaryexpressionx.getaccumulator()); }
 }
    return result; }

    public static boolean isComparitor(String fname)
  {   boolean result = false;
 
  if (((String) fname).equals("=") || ((String) fname).equals("/=") || ((String) fname).equals("<>") || ((String) fname).equals("<") || ((String) fname).equals(">") || ((String) fname).equals("<=") || ((String) fname).equals(">=") || ((String) fname).equals("<>=")) 
  {   result = true;
 
  }    return result;
  }


    public static boolean isInclusion(String fname)
  {   boolean result = false;
 
  if (((String) fname).equals(":") || ((String) fname).equals("->includes") || ((String) fname).equals("<:") || ((String) fname).equals("->includesAll") || ((String) fname).equals("->includesKey") || ((String) fname).equals("->includesValue")) 
  {   result = true;
 
  }    return result;
  }


    public static boolean isExclusion(String fname)
  {   boolean result = false;
 
  if (((String) fname).equals("/:") || ((String) fname).equals("/<:") || ((String) fname).equals("->excludes") || ((String) fname).equals("->excludesAll") || ((String) fname).equals("->excludesKey") || ((String) fname).equals("->excludesValue")) 
  {   result = true;
 
  }    return result;
  }


    public static boolean isBooleanOp(String fname)
  {   boolean result = false;
 
  if (((String) fname).equals("&") || ((String) fname).equals("or") || ((String) fname).equals("=>") || ((String) fname).equals("->exists") || ((String) fname).equals("->forAll") || ((String) fname).equals("->exists1")) 
  {   result = true;
 
  }    return result;
  }


    public static boolean isStringOp(String fname)
  {   boolean result = false;
 
  if (((String) fname).equals("->indexOf") || ((String) fname).equals("->count") || ((String) fname).equals("->hasPrefix") || ((String) fname).equals("->hasSuffix") || ((String) fname).equals("->after") || ((String) fname).equals("->before") || ((String) fname).equals("->lastIndexOf") || ((String) fname).equals("->equalsIgnoreCase") || ((String) fname).equals("->split") || ((String) fname).equals("->isMatch") || ((String) fname).equals("->hasMatch") || ((String) fname).equals("->allMatches") || ((String) fname).equals("->firstMatch") || ((String) fname).equals("->excludingAt")) 
  {   result = true;
 
  }    return result;
  }


    public static boolean isCollectionOp(String fname)
  {   boolean result = false;
 
  if (( ((String) fname).equals("->including") || ((String) fname).equals("->excluding") || ((String) fname).equals("->excludingAt") || ((String) fname).equals("->excludingFirst") || ((String) fname).equals("->append") || ((String) fname).equals("->count") || ((String) fname).equals("->indexOf") || ((String) fname).equals("->lastIndexOf") || ((String) fname).equals("->union") || ((String) fname).equals("->intersection") || ((String) fname).equals("^") || ((String) fname).equals("->isUnique") || ((String) fname).equals("->at") )) 
  {   result = true;
 
  }    return result;
  }


    public static boolean isDistributedIteratorOp(String fname)
  {   boolean result = false;
 
  if (((String) fname).equals("->sortedBy") || ((String) fname).equals("->unionAll") || ((String) fname).equals("->concatenateAll") || ((String) fname).equals("->intersectAll") || ((String) fname).equals("->selectMinimals") || ((String) fname).equals("->selectMaximals")) 
  {   result = true;
 
  }    return result;
  }


    public static boolean isIteratorOp(String fname)
  {   boolean result = false;
 
  if (( ((String) fname).equals("->collect") || ((String) fname).equals("->select") || ((String) fname).equals("->reject") || ((String) fname).equals("->iterate") )) 
  {   result = true;
 
  }    return result;
  }


    public String mapDividesExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) type.getname()).equals("int") || ((String) type.getname()).equals("long")) 
  {   result = ls + "//" + rs;
 
  }  else
      if (true) 
  {   result = ls + "/" + rs;
 
  }       return result;
  }


    public String mapNumericExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) operator).equals("/")) 
  {   result = this.mapDividesExpression(ls,rs);
 
  }  else
      {   if (((String) operator).equals("mod")) 
  {   result = ls + " % " + rs;
 
  }  else
      {   if (((String) operator).equals("div")) 
  {   result = ls + " // " + rs;
 
  }  else
      {   if (((String) operator).equals("=")) 
  {   result = ls + " == " + rs;
 
  }  else
      {   if (((String) operator).equals("<>=")) 
  {   result = ls + " is " + rs;
 
  }  else
      {   if (((String) operator).equals("/=")) 
  {   result = ls + " != " + rs;
 
  }  else
      {   if (((String) operator).equals("->pow")) 
  {   result = "math.pow(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->gcd")) 
  {   result = "ocl.gcd(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->truncateTo") && ((String) left.gettype().getname()).equals("int")) 
  {   result = ls;
 
  }  else
      {   if (((String) operator).equals("->truncateTo")) 
  {   result = "ocl.truncateTo(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->roundTo") && ((String) left.gettype().getname()).equals("int")) 
  {   result = ls;
 
  }  else
      {   if (((String) operator).equals("->roundTo")) 
  {   result = "ocl.roundTo(" + ls + ", " + rs + ")";
 
  }  else
      if (true) 
  {   result = ls + " " + operator + " " + rs;
 
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


    public String mapComparitorExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) operator).equals("=")) 
  {   result = ls + " == " + rs;
 
  }  else
      {   if (((String) operator).equals("<>=")) 
  {   result = ls + " is " + rs;
 
  }  else
      {   if (((String) operator).equals("/=")) 
  {   result = ls + " != " + rs;
 
  }  else
      {   if (((String) operator).equals("<>")) 
  {   result = ls + " != " + rs;
 
  }  else
      if (true) 
  {   result = ls + " " + operator + " " + rs;
 
  }   
   } 
   } 
   }     return result;
  }


    public String mapStringExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) operator).equals("=")) 
  {   result = ls + " == " + rs;
 
  }  else
      {   if (((String) operator).equals("<>=")) 
  {   result = ls + " is " + rs;
 
  }  else
      {   if (((String) operator).equals("/=")) 
  {   result = ls + " != " + rs;
 
  }  else
      {   if (((String) operator).equals(":")) 
  {   result = ls + " in " + rs;
 
  }  else
      {   if (((String) operator).equals("/:")) 
  {   result = ls + " not in " + rs;
 
  }  else
      {   if (((String) operator).equals("->includes")) 
  {   result = rs + " in " + ls;
 
  }  else
      {   if (((String) operator).equals("->excludes")) 
  {   result = rs + " not in " + ls;
 
  }  else
      {   if (((String) operator).equals("-")) 
  {   result = "ocl.subtractString(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->hasPrefix")) 
  {   result = ls + ".startswith(" + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->hasSuffix")) 
  {   result = ls + ".endswith(" + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->equalsIgnoreCase")) 
  {   result = "ocl.equalsIgnoreCase(" + ls + "," + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->indexOf")) 
  {   result = "(" + ls + ".find(" + rs + ") + 1)";
 
  }  else
      {   if (((String) operator).equals("->lastIndexOf")) 
  {   result = "ocl.lastIndexOf(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->count")) 
  {   result = ls + ".count(" + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->isMatch")) 
  {   result = "ocl.isMatch(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->hasMatch")) 
  {   result = "ocl.hasMatch(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->allMatches")) 
  {   result = "ocl.allMatches(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->firstMatch")) 
  {   result = "ocl.firstMatch(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->before")) 
  {   result = "ocl.before(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->after")) 
  {   result = "ocl.after(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->split")) 
  {   result = "ocl.split(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->excludingAt")) 
  {   result = "ocl.removeAtString(" + ls + ", " + rs + ")";
 
  }  else
      if (true) 
  {   result = ls + " " + operator + " " + rs;
 
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


    public String mapStringPlus(String ls,String rs)
  {   String result = "";
 
  if (left.isString()) 
  {   result = ls + " + str(" + rs + ")";
 
  }  else
      if (right.isString()) 
  {   result = "str(" + ls + ") + " + rs;
 
  }       return result;
  }


    public String mapRefPlus(String ls,String rs)
  {   String result = "";
 
  result = "(" + ls + ")[" + rs + ":]";
    return result;
  }


    public String mapBooleanExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) operator).equals("&")) 
  {   result = ls + " and " + rs;
 
  }  else
      {   if (((String) operator).equals("=>")) 
  {   result = "((" + rs + ") if (" + ls + ") else (True))";
 
  }  else
      {   if (((String) operator).equals("or")) 
  {   result = ls + " or " + rs;
 
  }  else
      {   if (((String) operator).equals("->exists")) 
  {   result = "ocl.exists(" + ls + ", lambda " + variable + " : " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->forAll")) 
  {   result = "ocl.forAll(" + ls + ", lambda " + variable + " : " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->exists1")) 
  {   result = "ocl.exists1(" + ls + ", lambda " + variable + " : " + rs + ")";
 
  }  else
      if (true) 
  {   result = ls + " " + operator + " " + rs;
 
  }   
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String mapBinaryCollectionExpression(String ls,String rs,String lt,String rt)
  {   String result = "";
 
  if (((String) operator).equals(":")) 
  {   result = ls + " in " + rs;
 
  }  else
      {   if (((String) operator).equals("->includes")) 
  {   result = rs + " in " + ls;
 
  }  else
      {   if (((String) operator).equals("=")) 
  {   result = ls + " == " + rs;
 
  }  else
      {   if (((String) operator).equals("<>=")) 
  {   result = ls + " is " + rs;
 
  }  else
      {   if (((String) operator).equals("/=")) 
  {   result = ls + " != " + rs;
 
  }  else
      {   if (((String) operator).equals("/:")) 
  {   result = ls + " not in " + rs;
 
  }  else
      {   if (((String) operator).equals("->excludes")) 
  {   result = rs + " not in " + ls;
 
  }  else
      {   if (((String) operator).equals("<:")) 
  {   result = "ocl.includesAll(" + rs + ", " + ls + ")";
 
  }  else
      {   if (((String) operator).equals("/<:")) 
  {   result = "(not ocl.includesAll(" + rs + ", " + ls + "))";
 
  }  else
      {   if (((String) operator).equals("->includesAll")) 
  {   result = "ocl.includesAll(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->excludesAll")) 
  {   result = "ocl.excludesAll(" + rs + ", " + ls + ")";
 
  }  else
      {   if (((String) operator).equals("->including")) 
  {   result = "ocl.including" + lt + "(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->excluding")) 
  {   result = "ocl.excluding" + lt + "(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->excludingFirst")) 
  {   result = "ocl.excludingFirst(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->excludingAt")) 
  {   result = "ocl.removeAt(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->union")) 
  {   result = "ocl.union" + lt + "(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->intersection")) 
  {   result = "ocl.intersection" + lt + "(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("^")) 
  {   result = "ocl.concatenate(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->prepend")) 
  {   result = "ocl.prepend(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->append")) 
  {   result = "ocl.append(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("-")) 
  {   result = "ocl.excludeAll" + lt + "(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->indexOf") && ((String) right.gettype().getname()).equals("Sequence")) 
  {   result = "ocl.indexOfSubSequence(" + ls + "," + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->indexOf") && !(right.gettype().getname().equals("Sequence"))) 
  {   result = "ocl.indexOfSequence(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->lastIndexOf") && ((String) right.gettype().getname()).equals("Sequence")) 
  {   result = "ocl.lastIndexOfSubSequence(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->lastIndexOf") && !(right.gettype().getname().equals("Sequence"))) 
  {   result = "ocl.lastIndexOfSequence(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->count")) 
  {   result = "(" + ls + ").count(" + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->at")) 
  {   result = "(" + ls + ")[" + rs + " - 1]";
 
  }  else
      if (true) 
  {   result = ls + " " + operator + " " + rs;
 
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
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String mapBinaryMapExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) operator).equals(":")) 
  {   result = ls + " in " + rs;
 
  }  else
      {   if (((String) operator).equals("->includes")) 
  {   result = rs + " in " + ls;
 
  }  else
      {   if (((String) operator).equals("->includesKey")) 
  {   result = rs + " in " + ls;
 
  }  else
      {   if (((String) operator).equals("->includesValue")) 
  {   result = "ocl.includesValue(" + ls + "," + rs + ")";
 
  }  else
      {   if (((String) operator).equals("=")) 
  {   result = ls + " == " + rs;
 
  }  else
      {   if (((String) operator).equals("<>=")) 
  {   result = ls + " is " + rs;
 
  }  else
      {   if (((String) operator).equals("/=")) 
  {   result = ls + " != " + rs;
 
  }  else
      {   if (((String) operator).equals("/:")) 
  {   result = ls + " not in " + rs;
 
  }  else
      {   if (((String) operator).equals("->excludes")) 
  {   result = rs + " not in " + ls;
 
  }  else
      {   if (((String) operator).equals("->excludesKey")) 
  {   result = rs + " not in " + ls;
 
  }  else
      {   if (((String) operator).equals("->excludesValue")) 
  {   result = "ocl.excludesValue(" + ls + "," + rs + ")";
 
  }  else
      {   if (((String) operator).equals("<:")) 
  {   result = "ocl.includesAllMap(" + rs + ", " + ls + ")";
 
  }  else
      {   if (((String) operator).equals("/<:")) 
  {   result = "(not ocl.includesAllMap(" + rs + ", " + ls + "))";
 
  }  else
      {   if (((String) operator).equals("->includesAll")) 
  {   result = "ocl.includesAllMap(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->excludesAll")) 
  {   result = "ocl.excludesAllMap(" + rs + ", " + ls + ")";
 
  }  else
      {   if (((String) operator).equals("->including")) 
  {   result = "ocl.includingMap(" + ls + ", " + rs + ", " + variable + ")";
 
  }  else
      {   if (((String) operator).equals("->excluding")) 
  {   result = "ocl.excludingMapValue(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->excludingAt")) 
  {   result = "ocl.excludingAtMap(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->union")) 
  {   result = "ocl.unionMap(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->intersection")) 
  {   result = "ocl.intersectionMap(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->restrict")) 
  {   result = "ocl.restrict(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->antirestrict")) 
  {   result = "ocl.antirestrict(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("-")) 
  {   result = "ocl.excludeAllMap(" + ls + ", " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->at")) 
  {   result = "(" + ls + ")[" + rs + "]";
 
  }  else
      if (true) 
  {   result = ls + " " + operator + " " + rs;
 
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
   } 
   }     return result;
  }


    public String mapDistributedIteratorExpression(String ls,String rs,Expression rexp)
  {   String result = "";
 
  if (((String) operator).equals("->sortedBy") && (rexp instanceof CollectionExpression)) 
  {   result = "ocl.sortedBy(" + ls + ", [" + ((CollectionExpression) rexp).toLambdaList(variable) + "])";
 
  }  else
      {   if (((String) operator).equals("->sortedBy")) 
  {   result = "sorted(" + ls + ", key = lambda " + variable + " : " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->concatenateAll")) 
  {   result = "ocl.concatenateAll([" + rexp.toPython() + " for " + expId + " in " + ls + "])";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Sequence")) 
  {   result = "ocl.concatenateAll([" + rexp.toPython() + " for " + expId + " in " + ls + "])";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Set")) 
  {   result = "ocl.unionAll([" + rexp.toPython() + " for " + expId + " in " + ls + "])";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Map")) 
  {   result = "ocl.unionAllMap([" + rexp.toPython() + " for " + expId + " in " + ls + "])";
 
  }  else
      {   if (((String) operator).equals("->intersectAll")) 
  {   result = "ocl.intersectAll" + type.getname() + "([" + rexp.toPython() + " for " + expId + " in " + ls + "])";
 
  }  else
      {   if (((String) operator).equals("->selectMaximals")) 
  {   result = "ocl.selectMaximals" + left.gettype().getname() + "(" + ls + ", lambda " + expId + " : " + rexp.toPython() + ")";
 
  }  else
      if (((String) operator).equals("->selectMinimals")) 
  {   result = "ocl.selectMinimals" + left.gettype().getname() + "(" + ls + ", lambda " + expId + " : " + rexp.toPython() + ")";
 
  }   
   } 
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String mapMapIteratorExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) operator).equals("->select")) 
  {   result = "ocl.selectMap(" + ls + ", lambda " + variable + " : " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->reject")) 
  {   result = "ocl.rejectMap(" + ls + ", lambda " + variable + " : " + rs + ")";
 
  }  else
      if (((String) operator).equals("->collect")) 
  {   result = "ocl.collectMap(" + ls + ", lambda " + variable + " : " + rs + ")";
 
  }   
   }     return result;
  }


    public String mapIteratorExpression(String ls,String rs,String tn)
  {   String result = "";
 
  final String lbracket = Expression.leftBracket(tn); 
     final String rbracket = Expression.rightBracket(tn); 
     if (((String) tn).equals("Map")) 
  {   result = this.mapMapIteratorExpression(ls,rs);
 
  }  else
      {   if (((String) operator).equals("->iterate")) 
  {   result = "ocl.iterate(" + ls + "," + accumulator.getinitialValue().toPython() + ", lambda " + variable + ", " + accumulator.getname() + " : (" + rs + "))";
 
  }  else
      {   if (((String) operator).equals("->select")) 
  {   result = lbracket + variable + " for " + variable + " in " + ls + " if " + rs + rbracket;
 
  }  else
      {   if (((String) operator).equals("->reject")) 
  {   result = lbracket + variable + " for " + variable + " in " + ls + " if not " + rs + rbracket;
 
  }  else
      if (((String) operator).equals("->collect")) 
  {   result = "[" + rs + " for " + variable + " in " + ls + "]";
 
  }   
   } 
   } 
   }           return result;
  }


    public String mapTypeCastExpression(String ls,String rs)
  {   String result = "";
 
  if (PrimitiveType.isPythonPrimitiveType(rs)) 
  {   result = type.toPython() + "(" + ls + ")";
 
  }  else
      if (true) 
  {   result = ls;
 
  }       return result;
  }


    public String mapCatchExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) ":").equals(operator)) 
  {   result = ((BasicExpression) right).mapTypeExpression((new SystemTypes.Set()).getElements()) + " as " + ls;
 
  }  else
      if (true) 
  {   result = this.mapBinaryExpression(ls,rs);
 
  }       return result;
  }


    public boolean bothNumeric()
  {   boolean result = false;
 
  if (left.isNumeric() && right.isNumeric()) 
  {   result = true;
 
  }    return result;
  }


    public boolean bothString()
  {   boolean result = false;
 
  if (left.isString() && right.isString()) 
  {   result = true;
 
  }    return result;
  }


    public boolean isStringPlus()
  {   boolean result = false;
 
  if (((String) operator).equals("+") && ( left.isString() || right.isString() )) 
  {   result = true;
 
  }    return result;
  }


    public boolean isRefPlus()
  {   boolean result = false;
 
  if (((String) operator).equals("+") && left.isRef()) 
  {   result = true;
 
  }    return result;
  }


    public String mapBinaryExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) operator).equals(":")) 
  {   result = ls + " in " + rs;
 
  }  else
      {   if (((String) operator).equals("->apply")) 
  {   result = "(" + ls + ")(" + rs + ")";
 
  }  else
      {   if (((String) operator).equals("let")) 
  {   result = "(lambda " + variable + " : " + rs + ")(" + ls + ")";
 
  }  else
      {   if (((String) operator).equals("->oclAsType")) 
  {   result = this.mapTypeCastExpression(ls,rs);
 
  }  else
      {   if (((String) operator).equals("->oclIsTypeOf")) 
  {   result = "(type(" + ls + ") == " + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->oclIsKindOf")) 
  {   result = ls + " in " + rs;
 
  }  else
      {   if (((String) operator).equals("->compareTo")) 
  {   result = "ocl.compareTo(" + ls + "," + rs + ")";
 
  }  else
      {   if (((String) operator).equals("|->")) 
  {   result = ls + ":" + rs;
 
  }  else
      {   if (((String) operator).equals("->restrict")) 
  {   result = "ocl.restrict(" + ls + "," + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->antirestrict")) 
  {   result = "ocl.antirestrict(" + ls + "," + rs + ")";
 
  }  else
      {   if (((String) operator).equals("->append")) 
  {   result = "ocl.append(" + ls + ", " + rs + ")";
 
  }  else
      {   if (BinaryExpression.isComparitor(operator)) 
  {   result = this.mapComparitorExpression(ls,rs);
 
  }  else
      {   if (BinaryExpression.isDistributedIteratorOp(operator)) 
  {     if (Controller.inst().getBasicExpressionByPK(expId + "_variable") != null)
    { BasicExpression be = Controller.inst().getBasicExpressionByPK(expId + "_variable");
     be.setdata(expId);
    result = this.mapDistributedIteratorExpression(ls,rs,right.addReference(be));
  }
    else
    { BasicExpression be = new BasicExpression();
    Controller.inst().addBasicExpression(be);
    be.setdata(expId);
    be.setexpId(expId + "_variable");
    result = this.mapDistributedIteratorExpression(ls,rs,right.addReference(be)); }

   
  }  else
      {   if (this.bothNumeric()) 
  {   result = this.mapNumericExpression(ls,rs);
 
  }  else
      {   if (this.bothString()) 
  {   result = this.mapStringExpression(ls,rs);
 
  }  else
      {   if (left.isString() && ((String) operator).equals("->excludingAt")) 
  {   result = "ocl.removeAtString(" + ls + "," + rs + ")";
 
  }  else
      {   if (BinaryExpression.isIteratorOp(operator)) 
  {   result = this.mapIteratorExpression(ls,rs,left.typeName());
 
  }  else
      {   if (BinaryExpression.isBooleanOp(operator)) 
  {   result = this.mapBooleanExpression(ls,rs);
 
  }  else
      {   if (this.isStringPlus()) 
  {   result = this.mapStringPlus(ls,rs);
 
  }  else
      {   if (this.isRefPlus()) 
  {   result = this.mapRefPlus(ls,rs);
 
  }  else
      {   if (( left.isCollection() || right.isCollection() )) 
  {   result = this.mapBinaryCollectionExpression(ls,rs,left.typeName(),right.typeName());
 
  }  else
      {   if (( left.isMap() || right.isMap() )) 
  {   result = this.mapBinaryMapExpression(ls,rs);
 
  }  else
      {   if (((String) operator).equals("->at")) 
  {   result = "(" + ls + ")[" + rs + "]";
 
  }  else
      {   if (((String) operator).equals("->pow")) 
  {   result = "math.pow(" + ls + ", " + rs + ")";
 
  }  else
      if (true) 
  {   result = ls + " " + operator + " " + rs;
 
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
   } 
   }     return result;
  }


    public String toPython()
  {   String result = "";
 
  if (needsBracket) 
  {   result = "(" + this.mapBinaryExpression(left.toPython(),right.toPython()) + ")";
 
  }  else
      if (true) 
  {   result = this.mapBinaryExpression(left.toPython(),right.toPython());
 
  }       return result;
  }


    public String updateFormBinaryExpression(String ls,String rs)
  {   String result = "";
 
  if (((String) operator).equals(":") && right.isSet()) 
  {   result = rs + ".add(" + ls + ")";
 
  }  else
      {   if (((String) operator).equals(":") && right.isCollection()) 
  {   result = rs + ".append(" + ls + ")";
 
  }  else
      {   if (((String) operator).equals("/:") && right.isSet()) 
  {   result = rs + ".discard(" + ls + ")";
 
  }  else
      {   if (((String) operator).equals("/:") && right.isCollection()) 
  {   result = rs + ".remove(" + ls + ")";
 
  }  else
      {   if (((String) operator).equals("<:") && right.isSet()) 
  {   result = rs + ".update(" + ls + ")";
 
  }  else
      {   if (((String) operator).equals(":") && right.isCollection()) 
  {   result = rs + ".extend(" + ls + ")";
 
  }  else
      {   if (((String) operator).equals("/<:") && right.isSet()) 
  {   result = rs + ".difference_update(" + ls + ")";
 
  }  else
      if (true) 
  {   result = "pass";
 
  }   
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String updateForm()
  {   String result = "";
 
  result = this.updateFormBinaryExpression(left.toPython(),right.toPython());
    return result;
  }


    public Expression addReference(BasicExpression x)
  {   Expression result = null;
  //  if (((String) x.getdata()).equals(this.getvariable()))) { return result; } 
    if (Controller.inst().getBinaryExpressionByPK(this.getexpId() + "_" + x.getdata()) != null)
    { BinaryExpression e = Controller.inst().getBinaryExpressionByPK(this.getexpId() + "_" + x.getdata());
     Controller.inst().setoperator(e,this.getoperator());
    Controller.inst().setvariable(e,this.getvariable());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setleft(e,this.getleft().addReference(x));
    Controller.inst().setright(e,this.getright().addReference(x));
    result = e;
  }
    else
    { BinaryExpression e = new BinaryExpression();
    Controller.inst().addBinaryExpression(e);
    Controller.inst().setexpId(e,this.getexpId() + "_" + x.getdata());
    Controller.inst().setoperator(e,this.getoperator());
    Controller.inst().setvariable(e,this.getvariable());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setleft(e,this.getleft().addReference(x));
    Controller.inst().setright(e,this.getright().addReference(x));
    result = e; }

  return result;

  }


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

    public String mapConditionalExpression(String ts,String ls,String rs)
  {   String result = "";
 
  result = "(" + ls + " if " + ts + " else " + rs + ")";
    return result;
  }


    public String toPython()
  {   String result = "";
 
  result = this.mapConditionalExpression(test.toPython(),ifExp.toPython(),elseExp.toPython());
    return result;
  }


    public String updateFormConditionalExpression(String ts,String ls,String rs)
  {   String result = "";
 
  result = "  if " + ts + ":\n" + "    " + ls + "\n  else:\n    " + rs;
    return result;
  }


    public String updateForm()
  {   String result = "";
 
  result = this.updateFormConditionalExpression(test.toPython(),ifExp.updateForm(),elseExp.updateForm());
    return result;
  }


    public Expression addReference(BasicExpression x)
  {   Expression result = null;
  if (Controller.inst().getConditionalExpressionByPK(this.getexpId() + "_" + x.getdata()) != null)
    { ConditionalExpression e = Controller.inst().getConditionalExpressionByPK(this.getexpId() + "_" + x.getdata());
     Controller.inst().settest(e,this.gettest().addReference(x));
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setifExp(e,this.getifExp().addReference(x));
    Controller.inst().setelseExp(e,this.getelseExp().addReference(x));
    result = e;
  }
    else
    { ConditionalExpression e = new ConditionalExpression();
    Controller.inst().addConditionalExpression(e);
    Controller.inst().setexpId(e,this.getexpId() + "_" + x.getdata());
    Controller.inst().settest(e,this.gettest().addReference(x));
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setifExp(e,this.getifExp().addReference(x));
    Controller.inst().setelseExp(e,this.getelseExp().addReference(x));
    result = e; }

  return result;

  }


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

    public static boolean isUnaryStringOp(String fname)
  {   boolean result = false;
 
  if (((String) fname).equals("->size") || ((String) fname).equals("->first") || ((String) fname).equals("->last") || ((String) fname).equals("->front") || ((String) fname).equals("->tail") || ((String) fname).equals("->reverse") || ((String) fname).equals("->display") || ((String) fname).equals("->toUpperCase") || ((String) fname).equals("->toLowerCase") || ((String) fname).equals("->toInteger") || ((String) fname).equals("->toReal") || ((String) fname).equals("->toLong") || ((String) fname).equals("->toBoolean") || ((String) fname).equals("->trim")) 
  {   result = true;
 
  }    return result;
  }


    public static boolean isReduceOp(String fname)
  {   boolean result = false;
 
  if (( ((String) fname).equals("->min") || ((String) fname).equals("->max") || ((String) fname).equals("->sum") || ((String) fname).equals("->prd") )) 
  {   result = true;
 
  }    return result;
  }


    public String mapNumericExpression(String arg)
  {   String result = "";
 
  if (((String) operator).equals("->sqrt")) 
  {   result = "math.sqrt(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->sin")) 
  {   result = "math.sin(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->tan")) 
  {   result = "math.tan(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->cos")) 
  {   result = "math.cos(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->sqr")) 
  {   result = "ocl.sqr(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->floor")) 
  {   result = "int(math.floor(" + arg + "))";
 
  }  else
      {   if (((String) operator).equals("->ceil")) 
  {   result = "int(math.ceil(" + arg + "))";
 
  }  else
      {   if (((String) operator).equals("->round")) 
  {   result = "round(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->cbrt")) 
  {   result = "ocl.cbrt(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->display")) 
  {   result = "print(str(" + arg + "))";
 
  }  else
      {   if (((String) operator).equals("->abs")) 
  {   result = "math.fabs(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->exp")) 
  {   result = "math.exp(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->log")) 
  {   result = "math.log(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->log10")) 
  {   result = "math.log10(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->asin")) 
  {   result = "math.asin(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->acos")) 
  {   result = "math.acos(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->atan")) 
  {   result = "math.atan(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->sinh")) 
  {   result = "math.sinh(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->cosh")) 
  {   result = "math.cosh(" + arg + ")";
 
  }  else
      if (((String) operator).equals("->tanh")) 
  {   result = "math.tanh(" + arg + ")";
 
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
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String mapStringExpression(String arg)
  {   String result = "";
 
  if (((String) operator).equals("->size")) 
  {   result = "len(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->front")) 
  {   result = "(" + arg + ")[0:-1]";
 
  }  else
      {   if (((String) operator).equals("->tail")) 
  {   result = "(" + arg + ")[1:]";
 
  }  else
      {   if (((String) operator).equals("->first")) 
  {   result = "(" + arg + ")[0:1]";
 
  }  else
      {   if (((String) operator).equals("->last")) 
  {   result = "(" + arg + ")[-1:]";
 
  }  else
      {   if (((String) operator).equals("->toLowerCase")) 
  {   result = "ocl.toLowerCase(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->toUpperCase")) 
  {   result = "ocl.toUpperCase(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->characters")) 
  {   result = "ocl.characters(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->trim")) 
  {   result = "ocl.trim(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->reverse")) 
  {   result = "ocl.reverseString(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->display")) 
  {   result = "print(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isInteger")) 
  {   result = "(" + arg + ").isdigit()";
 
  }  else
      {   if (((String) operator).equals("->isLong")) 
  {   result = "(" + arg + ").isdigit()";
 
  }  else
      {   if (((String) operator).equals("->isReal")) 
  {   result = "ocl.isReal(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->toInteger")) 
  {   result = "ocl.toInteger(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->toLong")) 
  {   result = "ocl.toInteger(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->toReal")) 
  {   result = "float(" + arg + ")";
 
  }  else
      if (((String) operator).equals("->toBoolean")) 
  {   result = "ocl.toBoolean(" + arg + ")";
 
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
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String mapReduceExpression(String arg,String tn,Type et)
  {   String result = "";
 
  if (((String) operator).equals("->sum") && (et == null)) 
  {   result = "ocl.sum(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->sum") && et.isStringType()) 
  {   result = "ocl.sumString(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->sum") && !(et.isStringType())) 
  {   result = "ocl.sum(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->prd")) 
  {   result = "ocl.prd(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->max")) 
  {   result = "ocl.max" + tn + "(" + arg + ")";
 
  }  else
      if (((String) operator).equals("->min")) 
  {   result = "ocl.min" + tn + "(" + arg + ")";
 
  }   
   } 
   } 
   } 
   }     return result;
  }


    public String mapUnaryCollectionExpression(String arg,String tn)
  {   String result = "";
 
  if (((String) operator).equals("->size")) 
  {   result = "len(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isEmpty")) 
  {   result = "(len(" + arg + ") == 0)";
 
  }  else
      {   if (((String) operator).equals("->notEmpty")) 
  {   result = "(len(" + arg + ") > 0)";
 
  }  else
      {   if (((String) operator).equals("->asSet")) 
  {   result = "set(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->asSequence")) 
  {   result = "list(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->asOrderedSet")) 
  {   result = "ocl.asOrderedSet(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->asBag")) 
  {   result = "ocl.sortSequence(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Sequence")) 
  {   result = "ocl.concatenateAll(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Set")) 
  {   result = "ocl.unionAll(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Map")) 
  {   result = "ocl.unionAllMap(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->intersectAll")) 
  {   result = "ocl.intersectAll" + type.getname() + "(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->concatenateAll")) 
  {   result = "ocl.concatenateAll(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isUnique")) 
  {   result = "ocl.isUnique(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isDeleted")) 
  {   result = "del " + arg;
 
  }  else
      {   if (((String) operator).equals("->copy")) 
  {   result = "copy.copy(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->any")) 
  {   result = "ocl.any(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->reverse")) 
  {   result = "ocl.reverse" + tn + "(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->front")) 
  {   result = "(" + arg + ")[0:-1]";
 
  }  else
      {   if (((String) operator).equals("->tail")) 
  {   result = "(" + arg + ")[1:]";
 
  }  else
      {   if (((String) operator).equals("->first")) 
  {   result = "(" + arg + ")[0]";
 
  }  else
      {   if (((String) operator).equals("->last")) 
  {   result = "(" + arg + ")[-1]";
 
  }  else
      {   if (((String) operator).equals("->sort")) 
  {   result = "ocl.sort" + tn + "(" + arg + ")";
 
  }  else
      if (((String) operator).equals("->display")) 
  {   result = "print(str(" + arg + "))";
 
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


    public String mapUnaryMapExpression(String arg,String tn)
  {   String result = "";
 
  if (((String) operator).equals("->size")) 
  {   result = "len(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isEmpty")) 
  {   result = "(len(" + arg + ") == 0)";
 
  }  else
      {   if (((String) operator).equals("->notEmpty")) 
  {   result = "(len(" + arg + ") > 0)";
 
  }  else
      {   if (((String) operator).equals("->asSet")) 
  {   result = "set(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->asSequence")) 
  {   result = "list(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->asOrderedSet")) 
  {   result = "ocl.asOrderedSet(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->asBag")) 
  {   result = "ocl.sortSequence(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Sequence")) 
  {   result = "ocl.concatenateAll(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Set")) 
  {   result = "ocl.unionAll(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->unionAll") && ((String) type.getname()).equals("Map")) 
  {   result = "ocl.unionAllMap(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->intersectAll")) 
  {   result = "ocl.intersectAll" + type.getname() + "(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->concatenateAll")) 
  {   result = "ocl.concatenateAll(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isUnique")) 
  {   result = "ocl.isUnique(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isDeleted")) 
  {   result = "del " + arg;
 
  }  else
      {   if (((String) operator).equals("->copy")) 
  {   result = "copy.copy(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->any")) 
  {   result = "ocl.anyMap(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->reverse")) 
  {   result = "ocl.reverse" + tn + "(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->front")) 
  {   result = "(" + arg + ")[0:-1]";
 
  }  else
      {   if (((String) operator).equals("->tail")) 
  {   result = "(" + arg + ")[1:]";
 
  }  else
      {   if (((String) operator).equals("->first")) 
  {   result = "(" + arg + ")[0]";
 
  }  else
      {   if (((String) operator).equals("->last")) 
  {   result = "(" + arg + ")[-1]";
 
  }  else
      {   if (((String) operator).equals("->sort")) 
  {   result = "ocl.sort" + tn + "(" + arg + ")";
 
  }  else
      if (((String) operator).equals("->display")) 
  {   result = "print(str(" + arg + "))";
 
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


    public String mapUnaryExpression(String arg)
  {   String result = "";
 
  if (((String) operator).equals("-")) 
  {   result = "-" + arg;
 
  }  else
      {   if (((String) operator).equals("?")) 
  {   result = "[" + arg + "]";
 
  }  else
      {   if (((String) operator).equals("!")) 
  {   result = "(" + arg + ")[0]";
 
  }  else
      {   if (((String) operator).equals("not")) 
  {   result = "not " + arg;
 
  }  else
      {   if (((String) operator).equals("->isDeleted")) 
  {   result = "del " + arg;
 
  }  else
      {   if (((String) operator).equals("->copy")) 
  {   result = "copy.copy(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->oclIsUndefined")) 
  {   result = "(" + arg + " == None)";
 
  }  else
      {   if (((String) operator).equals("->oclIsInvalid")) 
  {   result = "math.isnan(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->oclType")) 
  {   result = "type(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("lambda")) 
  {   result = "lambda " + variable + " : " + arg;
 
  }  else
      {   if (((String) operator).equals("->byte2char")) 
  {   result = "chr(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->char2byte")) 
  {   result = "ord(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->keys")) 
  {   result = "ocl.keys(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->values")) 
  {   result = "ocl.values(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->exp")) 
  {   result = "math.exp(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->log")) 
  {   result = "math.log(" + arg + ")";
 
  }  else
      {   if (argument.isNumeric()) 
  {   result = this.mapNumericExpression(arg);
 
  }  else
      {   if (argument.isString()) 
  {   result = this.mapStringExpression(arg);
 
  }  else
      {   if (((String) operator).equals("->isInteger")) 
  {   result = "ocl.isInteger(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isLong")) 
  {   result = "ocl.isInteger(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->isReal")) 
  {   result = "ocl.isReal(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->toInteger")) 
  {   result = "ocl.toInteger(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->toLong")) 
  {   result = "ocl.toInteger(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->toReal")) 
  {   result = "ocl.toReal(" + arg + ")";
 
  }  else
      {   if (((String) operator).equals("->toBoolean")) 
  {   result = "ocl.toBoolean(" + arg + ")";
 
  }  else
      {   if (UnaryExpression.isReduceOp(operator)) 
  {   result = this.mapReduceExpression(arg,argument.gettype().getname(),argument.getelementType());
 
  }  else
      {   if (argument.isCollection()) 
  {   result = this.mapUnaryCollectionExpression(arg,argument.gettype().getname());
 
  }  else
      {   if (argument.isMap()) 
  {   result = this.mapUnaryMapExpression(arg,argument.gettype().getname());
 
  }  else
      {   if (((String) operator).equals("->display")) 
  {   result = "print(str(" + arg + "))";
 
  }  else
      {   if (((String) operator).equals("->toBoolean")) 
  {   result = "ocl.toBoolean(" + arg + ")";
 
  }  else
      if (true) 
  {   result = "ocl." + operator.substring(1,operator.length()).substring(1,operator.substring(1,operator.length()).length()) + "(" + arg + ")";
 
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
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String toPython()
  {   String result = "";
 
  if (needsBracket == true) 
  {   result = "(" + this.mapUnaryExpression(argument.toPython()) + ")";
 
  }  else
      if (true) 
  {   result = this.mapUnaryExpression(argument.toPython());
 
  }       return result;
  }


    public String updateFormUnaryExpression(String arg)
  {   String result = "";
 
  if (((String) operator).equals("->isDeleted")) 
  {   result = "del " + arg;
 
  }  else
      {   if (((String) operator).equals("->display")) 
  {   result = "print(" + arg + ")";
 
  }  else
      if (true) 
  {   result = "pass";
 
  }   
   }     return result;
  }


    public String updateForm()
  {   String result = "";
 
  result = this.updateFormUnaryExpression(argument.toPython());
    return result;
  }


    public Expression addReference(BasicExpression x)
  {   Expression result = null;
  if (Controller.inst().getUnaryExpressionByPK(this.getexpId() + "_" + x.getdata()) != null)
    { UnaryExpression e = Controller.inst().getUnaryExpressionByPK(this.getexpId() + "_" + x.getdata());
     Controller.inst().setoperator(e,this.getoperator());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setargument(e,this.getargument().addReference(x));
    result = e;
  }
    else
    { UnaryExpression e = new UnaryExpression();
    Controller.inst().addUnaryExpression(e);
    Controller.inst().setexpId(e,this.getexpId() + "_" + x.getdata());
    Controller.inst().setoperator(e,this.getoperator());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setargument(e,this.getargument().addReference(x));
    result = e; }

  return result;

  }


}


class CollectionExpression
  extends Expression
  implements SystemTypes
{
  private boolean isOrdered = false; // internal
  private boolean isSorted = false; // internal
  private List elements = new Vector(); // of Expression

  public CollectionExpression()
  {
    this.isOrdered = false;
    this.isSorted = false;

  }



  public String toString()
  { String _res_ = "(CollectionExpression) ";
    _res_ = _res_ + isOrdered + ",";
    _res_ = _res_ + isSorted;
    return _res_ + super.toString();
  }

  public static CollectionExpression parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    CollectionExpression collectionexpressionx = new CollectionExpression();
    collectionexpressionx.isOrdered = Boolean.parseBoolean((String) _line1vals.get(0));
    collectionexpressionx.isSorted = Boolean.parseBoolean((String) _line1vals.get(1));
    return collectionexpressionx;
  }


  public void writeCSV(PrintWriter _out)
  { CollectionExpression collectionexpressionx = this;
    _out.print("" + collectionexpressionx.isOrdered);
    _out.print(" , ");
    _out.print("" + collectionexpressionx.isSorted);
    _out.println();
  }


  public void setisOrdered(boolean isOrdered_x) { isOrdered = isOrdered_x;  }


    public static void setAllisOrdered(List collectionexpressions,boolean val)
  { for (int i = 0; i < collectionexpressions.size(); i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(i);
      Controller.inst().setisOrdered(collectionexpressionx,val); } }


  public void setisSorted(boolean isSorted_x) { isSorted = isSorted_x;  }


    public static void setAllisSorted(List collectionexpressions,boolean val)
  { for (int i = 0; i < collectionexpressions.size(); i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(i);
      Controller.inst().setisSorted(collectionexpressionx,val); } }


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

    public boolean getisSorted() { return isSorted; }

    public static List getAllisSorted(List collectionexpressions)
  { List result = new Vector();
    for (int i = 0; i < collectionexpressions.size(); i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(i);
      if (result.contains(new Boolean(collectionexpressionx.getisSorted()))) { }
      else { result.add(new Boolean(collectionexpressionx.getisSorted())); } }
    return result; }

    public static List getAllOrderedisSorted(List collectionexpressions)
  { List result = new Vector();
    for (int i = 0; i < collectionexpressions.size(); i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressions.get(i);
      result.add(new Boolean(collectionexpressionx.getisSorted())); } 
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

    public String mapCollectionExpression(List elems,String tn)
  {   String result = "";
 
  if (((String) tn).equals("Set") && isSorted == true) 
  {   result = "SortedSet({" + Expression.tolist(elems) + "})";
 
  }  else
      {   if (((String) tn).equals("Set")) 
  {   result = "set({" + Expression.tolist(elems) + "})";
 
  }  else
      {   if (((String) tn).equals("Sequence") && isSorted == true) 
  {   result = "SortedList([" + Expression.tolist(elems) + "])";
 
  }  else
      {   if (((String) tn).equals("Sequence")) 
  {   result = "[" + Expression.tolist(elems) + "]";
 
  }  else
      {   if (((String) tn).equals("Map") && isSorted == true) 
  {   result = "SortedDict({" + Expression.maptolist(elems) + "})";
 
  }  else
      {   if (((String) tn).equals("Map")) 
  {   result = "dict({" + Expression.maptolist(elems) + "})";
 
  }  else
      if (((String) tn).equals("Ref") && elems.size() > 0) 
  {   result = "[" + elementType.defaultInitialValue() + "]*" + elems.get(0);
 
  }   
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String toPython()
  {   String result = "";
 
  result = this.mapCollectionExpression(  Controller.inst().AllExpressiontoPython(elements),type.getname());
    return result;
  }


    public String updateForm()
  {   String result = "";
 
  result = "pass";
    return result;
  }


    public Expression addReference(BasicExpression x)
  {   Expression result = null;
  if (Controller.inst().getCollectionExpressionByPK(this.getexpId() + "_" + x.getdata()) != null)
    { CollectionExpression e = Controller.inst().getCollectionExpressionByPK(this.getexpId() + "_" + x.getdata());
     Controller.inst().setisOrdered(e,this.getisOrdered());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setelements(e,  Controller.inst().AllExpressionaddReference(this.getelements(),x));
    result = e;
  }
    else
    { CollectionExpression e = new CollectionExpression();
    Controller.inst().addCollectionExpression(e);
    Controller.inst().setexpId(e,this.getexpId() + "_" + x.getdata());
    Controller.inst().setisOrdered(e,this.getisOrdered());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setelements(e,  Controller.inst().AllExpressionaddReference(this.getelements(),x));
    result = e; }

  return result;

  }

    public String toLambdaList(String vbl)
  {   String result = "";
 
  result = Expression.toLambdaList(vbl,  Controller.inst().AllExpressiontoPython(elements));
    return result;
  }



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

    public static boolean isMathFunction(String fname)
  {   boolean result = false;
 
  if (((String) fname).equals("sqrt") || ((String) fname).equals("exp") || ((String) fname).equals("log") || ((String) fname).equals("sin") || ((String) fname).equals("cos") || ((String) fname).equals("tan") || ((String) fname).equals("pow") || ((String) fname).equals("log10") || ((String) fname).equals("cbrt") || ((String) fname).equals("tanh") || ((String) fname).equals("cosh") || ((String) fname).equals("sinh") || ((String) fname).equals("asin") || ((String) fname).equals("acos") || ((String) fname).equals("atan") || ((String) fname).equals("ceil") || ((String) fname).equals("round") || ((String) fname).equals("floor") || ((String) fname).equals("abs")) 
  {   result = true;
 
  }    return result;
  }


    public boolean noContextnoObject(List obs)
  {   boolean result = false;
 
  if (obs.size() == 0 && context.size() == 0) 
  {   result = true;
 
  }    return result;
  }


    public boolean contextAndObject(List obs)
  {   boolean result = false;
 
  if (context.size() > 0 && obs.size() > 0) 
  {   result = true;
 
  }    return result;
  }


    public boolean isOclExceptionCreation(List obs)
  {   boolean result = false;
 
  if (( ((String) data).equals("newOclException") || ((String) data).equals("newAssertionException") || ((String) data).equals("newProgramException") || ((String) data).equals("newSystemException") || ((String) data).equals("newIOException") || ((String) data).equals("newCastingException") || ((String) data).equals("newNullAccessException") || ((String) data).equals("newIndexingException") || ((String) data).equals("newArithmeticException") || ((String) data).equals("newIncorrectElementException") || ((String) data).equals("newAccessingException") || ((String) data).equals("newOclDate") )) 
  {   result = true;
 
  }    return result;
  }


    public String mapTypeExpression(List ainds)
  {   String result = "";
 
  if (((String) data).equals("long")) 
  {   result = "int";
 
  }  else
      {   if (((String) data).equals("double")) 
  {   result = "float";
 
  }  else
      {   if (((String) data).equals("boolean")) 
  {   result = "bool";
 
  }  else
      {   if (((String) data).equals("String")) 
  {   result = "str";
 
  }  else
      {   if (((String) data).equals("OclType") && ainds.size() > 0) 
  {   result = "getOclTypeByPK(" + ((String) Set.any(ainds)) + ")";
 
  }  else
      {   if (((String) data).equals("OclType") && ainds.size() == 0) 
  {   result = "type";
 
  }  else
      {   if (((String) data).equals("OclVoid")) 
  {   result = "None";
 
  }  else
      {   if (((String) data).equals("OclAny")) 
  {   result = "None";
 
  }  else
      {   if (((String) data).equals("OclException")) 
  {   result = "BaseException";
 
  }  else
      {   if (((String) data).equals("ProgramException")) 
  {   result = "Exception";
 
  }  else
      {   if (((String) data).equals("SystemException")) 
  {   result = "OSError";
 
  }  else
      {   if (((String) data).equals("IOException")) 
  {   result = "IOError";
 
  }  else
      {   if (((String) data).equals("CastingException")) 
  {   result = "TypeError";
 
  }  else
      {   if (((String) data).equals("NullAccessException")) 
  {   result = "AttributeError";
 
  }  else
      {   if (((String) data).equals("IndexingException")) 
  {   result = "LookupError";
 
  }  else
      {   if (((String) data).equals("ArithmeticException")) 
  {   result = "ArithmeticError";
 
  }  else
      {   if (((String) data).equals("IncorrectElementException")) 
  {   result = "ValueError";
 
  }  else
      {   if (((String) data).equals("AssertionException")) 
  {   result = "AssertionError";
 
  }  else
      {   if (((String) data).equals("AccessingException")) 
  {   result = "OSError";
 
  }  else
      if (true) 
  {   result = data;
 
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
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String mapValueExpression(List aind)
  {   String result = "";
 
  if (((String) data).equals("true")) 
  {   result = "True";
 
  }  else
      {   if (((String) data).equals("false")) 
  {   result = "False";
 
  }  else
      {   if (((String) data).equals("null")) 
  {   result = "None";
 
  }  else
      {   if (((String) data).equals("Math_NaN")) 
  {   result = "math.nan";
 
  }  else
      {   if (((String) data).equals("Math_PINFINITY")) 
  {   result = "math.inf";
 
  }  else
      {   if (((String) data).equals("Math_NINFINITY")) 
  {   result = "-math.inf";
 
  }  else
      {   if (aind.size() > 0) 
  {   result = data + "[" + ((String) Set.any(aind)) + "]";
 
  }  else
      {   if (this.isEnumeration()) 
  {   result = type.getname() + "." + data;
 
  }  else
      if (true) 
  {   result = data;
 
  }   
   } 
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String mapVariableExpression(List obs,List aind,List pars)
  {   String result = "";
 
  if (obs.size() == 0 && aind.size() == 0 && pars.size() == 0) 
  {   result = data;
 
  }  else
      {   if (obs.size() == 0 && aind.size() == 0 && pars.size() > 0) 
  {   result = data + Expression.parstring(pars);
 
  }  else
      {   if (obs.size() == 0 && aind.size() > 0 && Type.getAllname(Expression.getAlltype(arrayIndex)).contains("String")) 
  {   result = data + "[" + ((String) Set.any(aind)) + "]";
 
  }  else
      {   if (obs.size() == 0 && aind.size() > 0) 
  {   result = data + "[" + ((String) Set.any(aind)) + " -1]";
 
  }  else
      if (obs.size() > 0) 
  {   result = ((String) Set.any(obs)) + "." + this.mapVariableExpression((new SystemTypes.Set()).getElements(),aind,pars);
 
  }   
   } 
   } 
   }     return result;
  }


    public String mapStaticAttributeExpression(List obs,List aind,List pars)
  {   String result = "";
 
  if (obs.size() == 0) 
  {   result = data + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }  else
      if (this.contextAndObject(obs)) 
  {   result = ((BasicExpression) ((Expression) Set.any(objectRef))).getdata() + "." + data + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }       return result;
  }


    public String mapReferencedAttributeExpression(List obs,List aind,List pars)
  {   String result = "";
 
  if (!(((Expression) Set.any(objectRef)).gettype() instanceof CollectionType)) 
  {   result = ((String) Set.any(obs)) + "." + data + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }  else
      {   if (((String) type.getname()).equals("Sequence")) 
  {   result = "[ _x." + data + Expression.indexstring(Set.collect_20(arrayIndex),aind) + " for _x in " + ((String) Set.any(obs)) + "]";
 
  }  else
      if (((String) type.getname()).equals("Set")) 
  {   result = "set({ _x." + data + Expression.indexstring(Set.collect_20(arrayIndex),aind) + " for _x in " + ((String) Set.any(obs)) + "})";
 
  }   
   }     return result;
  }


    public String mapAttributeExpression(List obs,List aind,List pars)
  {   String result = "";
 
  if (isStatic == true) 
  {   result = this.mapStaticAttributeExpression(obs,aind,pars);
 
  }  else
      {   if (this.noContextnoObject(obs)) 
  {   result = data + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }  else
      {   if (obs.size() == 0) 
  {   result = "self." + data + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }  else
      {   if (this.contextAndObject(obs)) 
  {   result = this.mapReferencedAttributeExpression(obs,aind,pars);
 
  }  else
      if (obs.size() > 0) 
  {   result = ((String) Set.any(obs)) + "." + data + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }   
   } 
   } 
   }     return result;
  }


    public String mapErrorCall(List obs,List pars)
  {   String result = "";
 
  if (obs.size() > 0) 
  {   result = ((BasicExpression) ((Expression) Set.any(objectRef))).mapTypeExpression((new SystemTypes.Set()).getElements()) + Expression.parstring(pars);
 
  }  else
      if (obs.size() == 0) 
  {   result = data;
 
  }       return result;
  }


    public String mapStaticOperationExpression(List obs,List aind,List pars)
  {   String result = "";
 
  if (this.isOclExceptionCreation(obs)) 
  {   result = this.mapErrorCall(obs,pars);
 
  }  else
      {   if (obs.size() > 0) 
  {   result = ((BasicExpression) ((Expression) Set.any(objectRef))).getdata() + "." + data + Expression.parstring(pars) + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }  else
      if (obs.size() == 0) 
  {   result = data + Expression.parstring(pars) + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }   
   }     return result;
  }


    public String mapInstanceOperationExpression(List obs,List aind,List pars)
  {   String result = "";
 
  if (this.noContextnoObject(obs)) 
  {   result = data + Expression.parstring(pars) + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }  else
      {   if (this.contextAndObject(obs)) 
  {   result = ((String) Set.any(obs)) + "." + data + Expression.parstring(pars) + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }  else
      {   if (context.size() > 0) 
  {   result = "self." + data + Expression.parstring(pars) + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }  else
      if (obs.size() > 0) 
  {   result = ((String) Set.any(obs)) + "." + data + Expression.parstring(pars) + Expression.indexstring(Set.collect_20(arrayIndex),aind);
 
  }   
   } 
   }     return result;
  }


    public String mapOperationExpression(List obs,List aind,List pars)
  {   String result = "";
 
  if (isStatic == true) 
  {   result = this.mapStaticOperationExpression(obs,aind,pars);
 
  }  else
      if (isStatic == false) 
  {   result = this.mapInstanceOperationExpression(obs,aind,pars);
 
  }       return result;
  }


    public String mapIntegerFunctionExpression(List obs,List aind,List pars)
  {   String result = "";
    if (obs.size() <= 0) { return result; } 
   
  final String arg = ((String) Set.any(obs)); 
     if (((String) "subrange").equals(data) && pars.size() > 1) 
  {   result = "range(" + ((String) Set.first(pars)) + ", " + ((String) pars.get(2 - 1)) + " +1)";
 
  }  else
      {   if (((String) "Sum").equals(data)) 
  {   result = "ocl.sum([(" + pars.get(3) + ") for " + pars.get(2) + " in range(" + pars.get(0) + ", " + pars.get(1) + " + 1)])";
 
  }  else
      if (((String) "Prd").equals(data)) 
  {   result = "ocl.prd([(" + pars.get(3) + ") for " + pars.get(2) + " in range(" + pars.get(0) + ", " + pars.get(1) + " + 1)])";
 
  }   
   }        return result;
  }


    public String mapInsertAtFunctionExpression(List obs,List aind,List pars)
  {   String result = "";
    if (obs.size() <= 0) { return result; } 
   
  final String arg = ((String) Set.any(obs)); 
     if (((String) type.getname()).equals("String")) 
  {   result = "ocl.insertAtString(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }  else
      if (!(type.getname().equals("String"))) 
  {   result = "ocl.insertAt(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }          return result;
  }


    public String mapSetAtFunctionExpression(List obs,List aind,List pars)
  {   String result = "";
    if (obs.size() <= 0) { return result; } 
   
  final String arg = ((String) Set.any(obs)); 
     if (((String) type.getname()).equals("String")) 
  {   result = "ocl.setAtString(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }  else
      if (!(type.getname().equals("String"))) 
  {   result = "ocl.setAt(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }          return result;
  }


    public String mapSubrangeFunctionExpression(List obs,List aind,List pars)
  {   String result = "";
    if (obs.size() <= 0) { return result; } 
   
  final String arg = ((String) Set.any(obs)); 
     if ((pars).size() > 1) 
  {   result = arg + "[(" + pars.get(0) + "-1):" + pars.get(1) + "]";
 
  }  else
      if ((pars).size() <= 1) 
  {   result = arg + "[(" + pars.get(0) + "-1):]";
 
  }          return result;
  }


    public String mapFunctionExpression(List obs,List aind,List pars)
  {   String result = "";
    if (obs.size() <= 0) { return result; } 
   
  final String arg = ((String) Set.any(obs)); 
     if (((String) data).equals("allInstances")) 
  {   result = "allInstances_" + arg + "()";
 
  }  else
      {   if (((String) "Integer").equals(((BasicExpression) ((Expression) Set.any(objectRef))).getdata())) 
  {   result = this.mapIntegerFunctionExpression(obs,aind,pars);
 
  }  else
      {   if (((String) data).equals("ceil") || ((String) data).equals("floor")) 
  {   result = "int(math." + data + "(" + arg + "))";
 
  }  else
      {   if (((String) data).equals("sqr") || ((String) data).equals("cbrt")) 
  {   result = "ocl." + data + "(" + arg + ")";
 
  }  else
      {   if (BasicExpression.isMathFunction(data)) 
  {   result = "math." + data + "(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("replace")) 
  {   result = "ocl.replace(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }  else
      {   if (((String) data).equals("replaceFirstMatch")) 
  {   result = "ocl.replaceFirstMatch(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }  else
      {   if (((String) data).equals("replaceAllMatches")) 
  {   result = "ocl.replaceAllMatches(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }  else
      {   if (((String) data).equals("replaceAll")) 
  {   result = "ocl.replaceAll(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }  else
      {   if (((String) data).equals("insertAt")) 
  {   result = this.mapInsertAtFunctionExpression(obs,aind,pars);
 
  }  else
      {   if (((String) data).equals("insertInto")) 
  {   result = "ocl.insertInto(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }  else
      {   if (((String) data).equals("excludingSubrange")) 
  {   result = "ocl.excludingSubrange(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ")";
 
  }  else
      {   if (((String) data).equals("setSubrange")) 
  {   result = "ocl.setSubrange(" + arg + ", " + pars.get(0) + ", " + pars.get(1) + ", " + pars.get(2) + ")";
 
  }  else
      {   if (((String) data).equals("setAt")) 
  {   result = this.mapSetAtFunctionExpression(obs,aind,pars);
 
  }  else
      {   if (((String) data).equals("oclIsUndefined")) 
  {   result = "(" + arg + " == None)";
 
  }  else
      {   if (((String) data).equals("oclAsType")) 
  {   result = ((String) Set.first(pars)) + "(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("sum")) 
  {   result = "ocl.sum(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("prd")) 
  {   result = "ocl.prd(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("max")) 
  {   result = "ocl.max" + type.getname() + "(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("min")) 
  {   result = "ocl.min" + type.getname() + "(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("front")) 
  {   result = "(" + arg + ")[0:-1]";
 
  }  else
      {   if (((String) data).equals("tail")) 
  {   result = "(" + arg + ")[1:]";
 
  }  else
      {   if (((String) data).equals("first")) 
  {   result = "(" + arg + ")[0]";
 
  }  else
      {   if (((String) data).equals("last")) 
  {   result = "(" + arg + ")[-1]";
 
  }  else
      {   if (((String) data).equals("sort")) 
  {   result = "ocl.sort" + type.getname() + "(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("size")) 
  {   result = "len(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("toLowerCase")) 
  {   result = "ocl.toLowerCase(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("toUpperCase")) 
  {   result = "ocl.toUpperCase(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("reverse")) 
  {   result = "ocl.reverse" + type.getname() + "(" + arg + ")";
 
  }  else
      {   if (((String) data).equals("subrange")) 
  {   result = this.mapSubrangeFunctionExpression(obs,aind,pars);
 
  }  else
      if (true) 
  {   result = "ocl." + data + "(" + arg + ")";
 
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
   } 
   } 
   } 
   } 
   } 
   } 
   } 
   }        return result;
  }


    public String mapClassArrayExpression(List obs,List aind,List pars)
  {   String result = "";
    if (umlKind == value || umlKind == attribute || umlKind == role || umlKind == variable || umlKind == constant || umlKind == function || umlKind == queryop || umlKind == operation) { return result; } 
   
  if ((((Expression) Set.any(arrayIndex)).gettype() instanceof CollectionType)) 
  {   result = "get" + elementType.getname() + "ByPKs(" + ((String) Set.any(aind)) + ")";
 
  }  else
      if (!(((Expression) Set.any(arrayIndex)).gettype() instanceof CollectionType)) 
  {   result = "get" + elementType.getname() + "ByPK(" + ((String) Set.any(aind)) + ")";
 
  }       return result;
  }


    public String mapClassExpression(List obs,List aind,List pars)
  {   String result = "";
    if (umlKind == value || umlKind == attribute || umlKind == role || umlKind == variable || umlKind == constant || umlKind == function || umlKind == queryop || umlKind == operation) { return result; } 
   
  if (arrayIndex.size() > 0) 
  {   result = this.mapClassArrayExpression(obs,aind,pars);
 
  }  else
      if (arrayIndex.size() == 0) 
  {   result = "allInstances_" + data + "()";
 
  }       return result;
  }


    public String mapBasicExpression(List ob,List aind,List pars)
  {   String result = "";
 
  if (((String) data).equals("skip")) 
  {   result = "pass";
 
  }  else
      {   if (((String) data).equals("super")) 
  {   result = "super()";
 
  }  else
      {   if (PrimitiveType.isPrimitiveType(data)) 
  {   result = this.mapTypeExpression(aind);
 
  }  else
      {   if (umlKind == value) 
  {   result = this.mapValueExpression(aind);
 
  }  else
      {   if (umlKind == variable) 
  {   result = this.mapVariableExpression(ob,aind,pars);
 
  }  else
      {   if (umlKind == attribute || umlKind == role) 
  {   result = this.mapAttributeExpression(ob,aind,pars);
 
  }  else
      {   if (umlKind == operation) 
  {   result = this.mapOperationExpression(ob,aind,pars);
 
  }  else
      {   if (umlKind == function) 
  {   result = this.mapFunctionExpression(ob,aind,pars);
 
  }  else
      if (umlKind == classid) 
  {   result = this.mapClassExpression(ob,aind,pars);
 
  }   
   } 
   } 
   } 
   } 
   } 
   } 
   }     return result;
  }


    public String toPython()
  {   String result = "";
 
  result = this.mapBasicExpression(  Controller.inst().AllExpressiontoPython(objectRef),  Controller.inst().AllExpressiontoPython(arrayIndex),  Controller.inst().AllExpressiontoPython(parameters));
    return result;
  }


    public String updateForm()
  {   String result = "";
 
  result = "exec(\"" + this.toPython() + "\")";
    return result;
  }


    public Expression addClassIdReference(BasicExpression x)
  {   Expression result = null;
  if (Controller.inst().getBasicExpressionByPK(this.getexpId() + "_" + x.getdata()) != null)
    { BasicExpression e = Controller.inst().getBasicExpressionByPK(this.getexpId() + "_" + x.getdata());
     Controller.inst().setdata(e,this.getdata());
    Controller.inst().setprestate(e,this.getprestate());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setobjectRef(e,this.getobjectRef());
    Controller.inst().setarrayIndex(e,  Controller.inst().AllExpressionaddReference(this.getarrayIndex(),x));
    Controller.inst().setparameters(e,  Controller.inst().AllExpressionaddReference(this.getparameters(),x));
    result = e;
  }
    else
    { BasicExpression e = new BasicExpression();
    Controller.inst().addBasicExpression(e);
    Controller.inst().setexpId(e,this.getexpId() + "_" + x.getdata());
    Controller.inst().setdata(e,this.getdata());
    Controller.inst().setprestate(e,this.getprestate());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setobjectRef(e,this.getobjectRef());
    Controller.inst().setarrayIndex(e,  Controller.inst().AllExpressionaddReference(this.getarrayIndex(),x));
    Controller.inst().setparameters(e,  Controller.inst().AllExpressionaddReference(this.getparameters(),x));
    result = e; }

  return result;

  }

    public Expression addBEReference(BasicExpression x)
  {   Expression result = null;
  if (Controller.inst().getBasicExpressionByPK(this.getexpId() + "_" + x.getdata()) != null)
    { BasicExpression e = Controller.inst().getBasicExpressionByPK(this.getexpId() + "_" + x.getdata());
     Controller.inst().setdata(e,this.getdata());
    Controller.inst().setprestate(e,this.getprestate());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setobjectRef(e,(new SystemTypes.Set()).add(x).getElements());
    Controller.inst().setarrayIndex(e,  Controller.inst().AllExpressionaddReference(this.getarrayIndex(),x));
    Controller.inst().setparameters(e,  Controller.inst().AllExpressionaddReference(this.getparameters(),x));
    result = e;
  }
    else
    { BasicExpression e = new BasicExpression();
    Controller.inst().addBasicExpression(e);
    Controller.inst().setexpId(e,this.getexpId() + "_" + x.getdata());
    Controller.inst().setdata(e,this.getdata());
    Controller.inst().setprestate(e,this.getprestate());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setobjectRef(e,(new SystemTypes.Set()).add(x).getElements());
    Controller.inst().setarrayIndex(e,  Controller.inst().AllExpressionaddReference(this.getarrayIndex(),x));
    Controller.inst().setparameters(e,  Controller.inst().AllExpressionaddReference(this.getparameters(),x));
    result = e; }

  return result;

  }

    public Expression addSelfBEReference(BasicExpression x)
  {   Expression result = null;
  if (Controller.inst().getBasicExpressionByPK(this.getexpId() + "_" + x.getdata()) != null)
    { BasicExpression e = Controller.inst().getBasicExpressionByPK(this.getexpId() + "_" + x.getdata());
     Controller.inst().setdata(e,this.getdata());
    Controller.inst().setprestate(e,this.getprestate());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setobjectRef(e,(new SystemTypes.Set()).add(x).getElements());
    Controller.inst().setarrayIndex(e,  Controller.inst().AllExpressionaddReference(this.getarrayIndex(),x));
    Controller.inst().setparameters(e,  Controller.inst().AllExpressionaddReference(this.getparameters(),x));
    result = e;
  }
    else
    { BasicExpression e = new BasicExpression();
    Controller.inst().addBasicExpression(e);
    Controller.inst().setexpId(e,this.getexpId() + "_" + x.getdata());
    Controller.inst().setdata(e,this.getdata());
    Controller.inst().setprestate(e,this.getprestate());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setobjectRef(e,(new SystemTypes.Set()).add(x).getElements());
    Controller.inst().setarrayIndex(e,  Controller.inst().AllExpressionaddReference(this.getarrayIndex(),x));
    Controller.inst().setparameters(e,  Controller.inst().AllExpressionaddReference(this.getparameters(),x));
    result = e; }

  return result;

  }

    public Expression addObjectRefBEReference(BasicExpression x)
  {   Expression result = null;
  if (Controller.inst().getBasicExpressionByPK(this.getexpId() + "_" + x.getdata()) != null)
    { BasicExpression e = Controller.inst().getBasicExpressionByPK(this.getexpId() + "_" + x.getdata());
     Controller.inst().setdata(e,this.getdata());
    Controller.inst().setprestate(e,this.getprestate());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setobjectRef(e,  Controller.inst().AllExpressionaddReference(this.getobjectRef(),x));
    Controller.inst().setarrayIndex(e,  Controller.inst().AllExpressionaddReference(this.getarrayIndex(),x));
    Controller.inst().setparameters(e,  Controller.inst().AllExpressionaddReference(this.getparameters(),x));
    result = e;
  }
    else
    { BasicExpression e = new BasicExpression();
    Controller.inst().addBasicExpression(e);
    Controller.inst().setexpId(e,this.getexpId() + "_" + x.getdata());
    Controller.inst().setdata(e,this.getdata());
    Controller.inst().setprestate(e,this.getprestate());
    Controller.inst().setumlKind(e,this.getumlKind());
    Controller.inst().setobjectRef(e,  Controller.inst().AllExpressionaddReference(this.getobjectRef(),x));
    Controller.inst().setarrayIndex(e,  Controller.inst().AllExpressionaddReference(this.getarrayIndex(),x));
    Controller.inst().setparameters(e,  Controller.inst().AllExpressionaddReference(this.getparameters(),x));
    result = e; }

  return result;

  }

    public Expression addReference(BasicExpression x)
  {   Expression result = null;
result = ((( this.getumlKind() == classid )) ? (this.addClassIdReference(x)) : (((( this.getobjectRef().size() == 0 )) ? (this.addBEReference(x)) : (((( ((String) "self").equals(((Expression) Set.any(this.getobjectRef())) + "") )) ? (this.addSelfBEReference(x)) : (this.addObjectRefBEReference(x)))))));
  return result;

  }


}


class Property
  implements SystemTypes
{
  private String name = ""; // internal
  private int lower = 0; // internal
  private int upper = 0; // internal
  private boolean isOrdered = false; // internal
  private boolean isUnique = false; // internal
  private boolean isDerived = false; // internal
  private boolean isReadOnly = false; // internal
  private boolean isStatic = false; // internal
  private List qualifier = new Vector(); // of Property
  private Type type;
  private Expression initialValue;
  private Entity owner;

  public Property(Type type,Expression initialValue,Entity owner)
  {
    this.name = "";
    this.lower = 0;
    this.upper = 0;
    this.isOrdered = false;
    this.isUnique = false;
    this.isDerived = false;
    this.isReadOnly = false;
    this.isStatic = false;
    this.type = type;
    this.initialValue = initialValue;
    this.owner = owner;

  }

  public Property() { }



  public String toString()
  { String _res_ = "(Property) ";
    _res_ = _res_ + name + ",";
    _res_ = _res_ + lower + ",";
    _res_ = _res_ + upper + ",";
    _res_ = _res_ + isOrdered + ",";
    _res_ = _res_ + isUnique + ",";
    _res_ = _res_ + isDerived + ",";
    _res_ = _res_ + isReadOnly + ",";
    _res_ = _res_ + isStatic;
    return _res_;
  }

  public static Property parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Property propertyx = new Property();
    propertyx.name = (String) _line1vals.get(0);
    propertyx.lower = Integer.parseInt((String) _line1vals.get(1));
    propertyx.upper = Integer.parseInt((String) _line1vals.get(2));
    propertyx.isOrdered = Boolean.parseBoolean((String) _line1vals.get(3));
    propertyx.isUnique = Boolean.parseBoolean((String) _line1vals.get(4));
    propertyx.isDerived = Boolean.parseBoolean((String) _line1vals.get(5));
    propertyx.isReadOnly = Boolean.parseBoolean((String) _line1vals.get(6));
    propertyx.isStatic = Boolean.parseBoolean((String) _line1vals.get(7));
    return propertyx;
  }


  public void writeCSV(PrintWriter _out)
  { Property propertyx = this;
    _out.print("" + propertyx.name);
    _out.print(" , ");
    _out.print("" + propertyx.lower);
    _out.print(" , ");
    _out.print("" + propertyx.upper);
    _out.print(" , ");
    _out.print("" + propertyx.isOrdered);
    _out.print(" , ");
    _out.print("" + propertyx.isUnique);
    _out.print(" , ");
    _out.print("" + propertyx.isDerived);
    _out.print(" , ");
    _out.print("" + propertyx.isReadOnly);
    _out.print(" , ");
    _out.print("" + propertyx.isStatic);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List propertys,String val)
  { for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      Controller.inst().setname(propertyx,val); } }


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


  public void setisReadOnly(boolean isReadOnly_x) { isReadOnly = isReadOnly_x;  }


    public static void setAllisReadOnly(List propertys,boolean val)
  { for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      Controller.inst().setisReadOnly(propertyx,val); } }


  public void setisStatic(boolean isStatic_x) { isStatic = isStatic_x;  }


    public static void setAllisStatic(List propertys,boolean val)
  { for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      Controller.inst().setisStatic(propertyx,val); } }


  public void setqualifier(List qualifier_xx) { if (qualifier_xx.size() > 1) { return; } 
    qualifier = qualifier_xx;
  }
 
  public void addqualifier(Property qualifier_xx) { if (qualifier.size() > 0) { qualifier.clear(); } 
    if (qualifier.contains(qualifier_xx)) {} else { qualifier.add(qualifier_xx); }
    }
 
  public void removequalifier(Property qualifier_xx) { Vector _removedqualifierqualifier_xx = new Vector();
  _removedqualifierqualifier_xx.add(qualifier_xx);
  qualifier.removeAll(_removedqualifierqualifier_xx);
    }

  public static void setAllqualifier(List propertys, List _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().setqualifier(propertyx, _val); } }

  public static void addAllqualifier(List propertys, Property _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().addqualifier(propertyx,  _val); } }


  public static void removeAllqualifier(List propertys,Property _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().removequalifier(propertyx, _val); } }


  public static void unionAllqualifier(List propertys, List _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().unionqualifier(propertyx, _val); } }


  public static void subtractAllqualifier(List propertys,  List _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().subtractqualifier(propertyx,  _val); } }


  public void settype(Type type_xx) { type = type_xx;
  }

  public static void setAlltype(List propertys, Type _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().settype(propertyx, _val); } }

  public void setinitialValue(Expression initialValue_xx) { initialValue = initialValue_xx;
  }

  public static void setAllinitialValue(List propertys, Expression _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().setinitialValue(propertyx, _val); } }

  public void setowner(Entity owner_xx) { owner = owner_xx;
  }

  public static void setAllowner(List propertys, Entity _val)
  { for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      Controller.inst().setowner(propertyx, _val); } }

    public String getname() { return name; }

    public static List getAllname(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      if (result.contains(propertyx.getname())) { }
      else { result.add(propertyx.getname()); } }
    return result; }

    public static List getAllOrderedname(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      result.add(propertyx.getname()); } 
    return result; }

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

    public boolean getisReadOnly() { return isReadOnly; }

    public static List getAllisReadOnly(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      if (result.contains(new Boolean(propertyx.getisReadOnly()))) { }
      else { result.add(new Boolean(propertyx.getisReadOnly())); } }
    return result; }

    public static List getAllOrderedisReadOnly(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      result.add(new Boolean(propertyx.getisReadOnly())); } 
    return result; }

    public boolean getisStatic() { return isStatic; }

    public static List getAllisStatic(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      if (result.contains(new Boolean(propertyx.getisStatic()))) { }
      else { result.add(new Boolean(propertyx.getisStatic())); } }
    return result; }

    public static List getAllOrderedisStatic(List propertys)
  { List result = new Vector();
    for (int i = 0; i < propertys.size(); i++)
    { Property propertyx = (Property) propertys.get(i);
      result.add(new Boolean(propertyx.getisStatic())); } 
    return result; }

  public List getqualifier() { return (Vector) ((Vector) qualifier).clone(); }

  public static List getAllqualifier(List propertys)
  { List result = new Vector();
    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      result = Set.union(result, propertyx.getqualifier()); }
    return result; }

  public static List getAllOrderedqualifier(List propertys)
  { List result = new Vector();
    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      result = Set.union(result, propertyx.getqualifier()); }
    return result; }

  public Type gettype() { return type; }

  public static List getAlltype(List propertys)
  { List result = new Vector();
    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      if (result.contains(propertyx.gettype())) {}
      else { result.add(propertyx.gettype()); }
 }
    return result; }

  public static List getAllOrderedtype(List propertys)
  { List result = new Vector();
    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx = (Property) propertys.get(_i);
      if (result.contains(propertyx.gettype())) {}
      else { result.add(propertyx.gettype()); }
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

    public String initialisation()
  {   String result = "";
 
  if ((type == null)) 
  {   result = "    self." + name + " = None\n";
 
  }  else
      {   if (qualifier.size() > 0) 
  {   result = "    self." + name + " = dict({})\n";
 
  }  else
      {   if (qualifier.size() == 0 && (initialValue == null)) 
  {   result = "    self." + name + " = " + type.defaultInitialValue() + "\n";
 
  }  else
      if (qualifier.size() == 0) 
  {   result = "    self." + name + " = " + initialValue.toPython() + "\n";
 
  }   
   } 
   }     return result;
  }


    public String getPKOp(String ent)
  {   String result = "";
 
  final String e = ent.toLowerCase(); 
     result = "def get" + ent + "ByPK(_ex) :\n" + "  if (_ex in " + ent + "." + e + "_index) :\n" + "    return " + ent + "." + e + "_index[_ex]\n" + "  else :\n" + "    return None\n\n";
       return result;
  }


    public String getPKOps(String ent)
  {   String result = "";
 
  final String e = ent.toLowerCase(); 
     result = "def get" + ent + "ByPKs(_exs) :\n" + "  result = []\n" + "  for _ex in _exs :\n" + "    if (_ex in " + ent + "." + e + "_index) :\n" + "      result.append(" + ent + "." + e + "_index[_ex])\n" + "  return result\n\n";
       return result;
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

    public static String tab(int indent)
  {   String result = "";
 
  if (indent <= 0) 
  {   result = "";
 
  }  else
      if (indent > 0) 
  {   result = " " + Statement.tab(indent - 1);
 
  }       return result;
  }


    public abstract String toPython(int indent);




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

    public String toPython(int indent)
  {   String result = "";
 
  if (returnValue.size() == 0) 
  {   result = Statement.tab(indent) + "return" + "\n";
 
  }  else
      if (returnValue.size() > 0) 
  {   result = Statement.tab(indent) + "return " + ((Expression) Set.any(returnValue)).toPython() + "\n";
 
  }       return result;
  }



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

    public String toPython(int indent)
  {   String result = "";
 
  if (message.size() == 0) 
  {   result = Statement.tab(indent) + "assert " + condition.toPython() + "\n";
 
  }  else
      if (message.size() > 0) 
  {   result = Statement.tab(indent) + "assert " + condition.toPython() + ", " + ((Expression) Set.any(message)).toPython() + "\n";
 
  }       return result;
  }



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

    public String toPython(int indent)
  {   String result = "";
 
  List obs;
    obs = new Vector();
  obs.addAll(((BasicExpression) thrownObject).getobjectRef());

    List pars;
    pars = new Vector();
  pars.addAll(((BasicExpression) thrownObject).getparameters());

     result = Statement.tab(indent) + "raise " + ((BasicExpression) thrownObject).mapErrorCall(  Controller.inst().AllExpressiontoPython(obs),  Controller.inst().AllExpressiontoPython(pars)) + "\n";

    return result;
  }



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

    public String toPython(int indent)
  {   String result = "";
 
  final Expression cleft = ((BinaryExpression) caughtObject).getleft(); 
     final Expression cright = ((BinaryExpression) caughtObject).getright(); 
     result = Statement.tab(indent) + "except " + ((BinaryExpression) caughtObject).mapCatchExpression(cleft.toPython(),cright.toPython()) + ":\n" + action.toPython(indent + 2);
          return result;
  }



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

    public String toPython(int indent)
  {   String result = "";
 
  result = Statement.tab(indent) + "finally :\n" + body.toPython(indent + 2);
    return result;
  }



}


class Operation
  extends BehaviouralFeature
  implements SystemTypes
{
  private boolean isQuery = false; // internal
  private boolean isAbstract = false; // internal
  private boolean isCached = false; // internal
  private Entity owner;
  private List definers = new Vector(); // of Entity

  public Operation(Entity owner)
  {
    this.isQuery = false;
    this.isAbstract = false;
    this.isCached = false;
    this.owner = owner;

  }

  public Operation() { }



  public String toString()
  { String _res_ = "(Operation) ";
    _res_ = _res_ + isQuery + ",";
    _res_ = _res_ + isAbstract + ",";
    _res_ = _res_ + isCached;
    return _res_ + super.toString();
  }

  public static Operation parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    Operation operationx = new Operation();
    operationx.isQuery = Boolean.parseBoolean((String) _line1vals.get(0));
    operationx.isAbstract = Boolean.parseBoolean((String) _line1vals.get(1));
    operationx.isCached = Boolean.parseBoolean((String) _line1vals.get(2));
    return operationx;
  }


  public void writeCSV(PrintWriter _out)
  { Operation operationx = this;
    _out.print("" + operationx.isQuery);
    _out.print(" , ");
    _out.print("" + operationx.isAbstract);
    _out.print(" , ");
    _out.print("" + operationx.isCached);
    _out.println();
  }


  public void setisQuery(boolean isQuery_x) { isQuery = isQuery_x;  }


    public static void setAllisQuery(List operations,boolean val)
  { for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      Controller.inst().setisQuery(operationx,val); } }


  public void setisAbstract(boolean isAbstract_x) { isAbstract = isAbstract_x;  }


    public static void setAllisAbstract(List operations,boolean val)
  { for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      Controller.inst().setisAbstract(operationx,val); } }


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

    public boolean getisAbstract() { return isAbstract; }

    public static List getAllisAbstract(List operations)
  { List result = new Vector();
    for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      if (result.contains(new Boolean(operationx.getisAbstract()))) { }
      else { result.add(new Boolean(operationx.getisAbstract())); } }
    return result; }

    public static List getAllOrderedisAbstract(List operations)
  { List result = new Vector();
    for (int i = 0; i < operations.size(); i++)
    { Operation operationx = (Operation) operations.get(i);
      result.add(new Boolean(operationx.getisAbstract())); } 
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

    public void displayStaticCachedOperation(int indent,String p,String obj)
  {   System.out.println("" + ( Statement.tab(indent) + "def " + this.getname() + "(" + p + ") :" ));

      System.out.println("" + ( Statement.tab(indent + 2) + "if str(" + p + ") in " + obj + "." + this.getname() + "_cache :" ));

      System.out.println("" + ( Statement.tab(indent + 4) + "return " + obj + "." + this.getname() + "_cache[str(" + p + ")]" ));

      System.out.println("" + ( Statement.tab(indent + 2) + "result = " + obj + "." + this.getname() + "_uncached(" + p + ")" ));

      System.out.println("" + ( Statement.tab(indent + 2) + obj + "." + this.getname() + "_cache[str(" + p + ")] = result" ));

      System.out.println("" + ( Statement.tab(indent + 2) + "return result" ));

      System.out.println("" + "\n");

      System.out.println("" + ( Statement.tab(indent) + "def " + this.getname() + "_uncached(" + p + ") :" ));

      System.out.println("" + this.getactivity().toPython(indent + 2));

      System.out.println("" + "\n");

  }

    public void displayInstanceCachedOperation(int indent,String p,String obj)
  {   System.out.println("" + ( Statement.tab(indent) + "def " + this.getname() + "(self, " + p + ") :" ));

      System.out.println("" + ( Statement.tab(indent + 2) + "if str(" + p + ") in " + obj + "." + this.getname() + "_cache :" ));

      System.out.println("" + ( Statement.tab(indent + 4) + "return " + obj + "." + this.getname() + "_cache[str(" + p + ")]" ));

      System.out.println("" + ( Statement.tab(indent + 2) + "result = " + obj + "." + this.getname() + "_uncached(" + p + ")" ));

      System.out.println("" + ( Statement.tab(indent + 2) + obj + "." + this.getname() + "_cache[str(" + p + ")] = result" ));

      System.out.println("" + ( Statement.tab(indent + 2) + "return result" ));

      System.out.println("" + "\n");

      System.out.println("" + ( Statement.tab(indent) + "def " + this.getname() + "_uncached(self, " + p + ") :" ));

      System.out.println("" + this.getactivity().toPython(indent + 2));

      System.out.println("" + "\n");

  }

    public void displayStaticOperation(int indent)
  {   if (this.getisCached() == true && this.getparameters().size() == 1) 
  { this.displayStaticCachedOperation(indent,((Property) this.getparameters().get(0)).getname(),this.getowner().getname());}
      if (( this.getisCached() == false || this.getparameters().size() != 1 )) 
  {   System.out.println("" + ( Statement.tab(indent) + "def " + this.getname() + "(" + Expression.tolist(Property.getAllOrderedname(this.getparameters())) + ") :" ));

      System.out.println("" + this.getactivity().toPython(indent + 2));
}
  }

    public void displayInstanceOperation(int indent)
  {   if (this.getisCached() == true && this.getparameters().size() == 1) 
  { this.displayInstanceCachedOperation(indent,((Property) this.getparameters().get(0)).getname(),"self");}
      if (( this.getisCached() == false || this.getparameters().size() != 1 )) 
  {   System.out.println("" + ( Statement.tab(indent) + "def " + this.getname() + "(self" + Set.sumString(Set.collect_21(this.getparameters())) + ") :" ));

      System.out.println("" + this.getactivity().toPython(indent + 2));
}
  }

    public void displayOperation(int indent)
  {   if (this.getisStatic() == true) 
  { this.displayStaticOperation(indent);}
      if (this.getisStatic() == false) 
  { this.displayInstanceOperation(indent);}
  }

    public void printcode8()
  {   if (this.getisStatic() == true && (this.getowner() == null)) 
  { this.displayOperation(0);
      System.out.println("" + "");
}
  }


}


class UseCase
  implements SystemTypes
{
  private String name = ""; // internal
  private List parameters = new Vector(); // of Property
  private Type resultType;
  private Statement classifierBehaviour;

  public UseCase(Type resultType,Statement classifierBehaviour)
  {
    this.name = "";
    this.resultType = resultType;
    this.classifierBehaviour = classifierBehaviour;

  }

  public UseCase() { }



  public String toString()
  { String _res_ = "(UseCase) ";
    _res_ = _res_ + name;
    return _res_;
  }

  public static UseCase parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    UseCase usecasex = new UseCase();
    usecasex.name = (String) _line1vals.get(0);
    return usecasex;
  }


  public void writeCSV(PrintWriter _out)
  { UseCase usecasex = this;
    _out.print("" + usecasex.name);
    _out.println();
  }


  public void setname(String name_x) { name = name_x;  }


    public static void setAllname(List usecases,String val)
  { for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      Controller.inst().setname(usecasex,val); } }


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


  public void setresultType(Type resultType_xx) { resultType = resultType_xx;
  }

  public static void setAllresultType(List usecases, Type _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().setresultType(usecasex, _val); } }

  public void setclassifierBehaviour(Statement classifierBehaviour_xx) { classifierBehaviour = classifierBehaviour_xx;
  }

  public static void setAllclassifierBehaviour(List usecases, Statement _val)
  { for (int _i = 0; _i < usecases.size(); _i++)
    { UseCase usecasex = (UseCase) usecases.get(_i);
      Controller.inst().setclassifierBehaviour(usecasex, _val); } }

    public String getname() { return name; }

    public static List getAllname(List usecases)
  { List result = new Vector();
    for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      if (result.contains(usecasex.getname())) { }
      else { result.add(usecasex.getname()); } }
    return result; }

    public static List getAllOrderedname(List usecases)
  { List result = new Vector();
    for (int i = 0; i < usecases.size(); i++)
    { UseCase usecasex = (UseCase) usecases.get(i);
      result.add(usecasex.getname()); } 
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

    public String mapUseCase()
  {   String result = "";
 
  if ((classifierBehaviour == null) == false && !(resultType.getname().equals("void"))) 
  {   result = "def " + name + "(" + Expression.tolist(Property.getAllOrderedname(parameters)) + ") :\n" + classifierBehaviour.toPython(2) + "  return result\n";
 
  }  else
      if ((classifierBehaviour == null) == false && ((String) resultType.getname()).equals("void")) 
  {   result = "def " + name + "(" + Expression.tolist(Property.getAllOrderedname(parameters)) + ") :\n" + classifierBehaviour.toPython(2) + "\n";
 
  }       return result;
  }


    public void printcode9()
  {   System.out.println("" + this.mapUseCase());

      System.out.println("" + "");

      System.out.println("" + "");

      System.out.println("" + "");

  }


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


    public String toPython(int indent)
  {   String result = "";
 
  result = Statement.tab(indent) + "break" + "\n";
    return result;
  }



}


class ContinueStatement
  implements SystemTypes
{

  public ContinueStatement()
  {

  }



  public String toString()
  { String _res_ = "(ContinueStatement) ";
    return _res_;
  }

  public static ContinueStatement parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Set.tokeniseCSV(_line);
    ContinueStatement continuestatementx = new ContinueStatement();
    return continuestatementx;
  }


  public void writeCSV(PrintWriter _out)
  { ContinueStatement continuestatementx = this;
    _out.println();
  }


    public String toPython(int indent)
  {   String result = "";
 
  result = Statement.tab(indent) + "continue" + "\n";
    return result;
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

    public String toPython(int indent)
  {   String result = "";
 
  if ((callExp == null)) 
  {   result = Statement.tab(indent) + "pass\n";
 
  }  else
      if (true) 
  {   result = Statement.tab(indent) + callExp.toPython() + "\n";
 
  }       return result;
  }



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

    public String toPython(int indent)
  {   String result = "";
 
  result = Statement.tab(indent) + callExp.updateForm() + "\n";
    return result;
  }



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

    public String toPython(int indent)
  {   String result = "";
 
  result = Statement.tab(indent) + "for " + test.toPython() + " :\n" + body.toPython(indent + 2);
    return result;
  }



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


    public String toPython(int indent)
  {   String result = "";
 
  result = Statement.tab(indent) + "while " + test.toPython() + " :\n" + body.toPython(indent + 2);
    return result;
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

    public String toPython(int indent)
  {   String result = "";
 
  if (((String) left.gettype().getname()).equals("int") && ((String) right.gettype().getname()).equals("String")) 
  {   result = Statement.tab(indent) + left.toPython() + " = ocl.toInteger(" + right.toPython() + ")\n";
 
  }  else
      {   if (((String) left.gettype().getname()).equals("long") && ((String) right.gettype().getname()).equals("String")) 
  {   result = Statement.tab(indent) + left.toPython() + " = ocl.toInteger(" + right.toPython() + ")\n";
 
  }  else
      {   if (((String) left.gettype().getname()).equals("String") && !(right.gettype().getname().equals("String"))) 
  {   result = Statement.tab(indent) + left.toPython() + " = str(" + right.toPython() + ")\n";
 
  }  else
      if (true) 
  {   result = Statement.tab(indent) + left.toPython() + " = " + right.toPython() + "\n";
 
  }   
   } 
   }     return result;
  }



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

    public String toPython(int indent)
  {   String result = "";
 
  if (statements.size() == 0) 
  {   result = Statement.tab(indent) + "pass\n";
 
  }  else
      if (statements.size() > 0) 
  {   result = Set.sumString(Set.collect_22(statements,indent));
 
  }       return result;
  }



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

    public String toPython(int indent)
  {   String result = "";
 
  if (endStatement.size() == 0) 
  {   result = Statement.tab(indent) + "try :\n" + body.toPython(indent + 2) + Set.sumString(Set.collect_22(catchClauses,indent));
 
  }  else
      if (endStatement.size() > 0) 
  {   result = Statement.tab(indent) + "try :\n" + body.toPython(indent + 2) + Set.sumString(Set.collect_22(catchClauses,indent)) + ((Statement) Set.any(endStatement)).toPython(indent);
 
  }       return result;
  }



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

    public String elsecode(int indent)
  {   String result = "";
 
  if (elsePart.size() == 0) 
  {   result = "";
 
  }  else
      if (elsePart.size() > 0) 
  {   result = Statement.tab(indent) + "else :\n" + ((Statement) Set.any(elsePart)).toPython(indent + 2);
 
  }       return result;
  }


    public String toPython(int indent)
  {   String result = "";
 
  result = Statement.tab(indent) + "if " + test.toPython() + " :\n" + ifPart.toPython(indent + 2) + this.elsecode(indent);
    return result;
  }



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

  public void setinitialExpression(List initialExpression_xx) { initialExpression = initialExpression_xx;
    }
 
  public void addinitialExpression(Expression initialExpression_xx) { if (initialExpression.contains(initialExpression_xx)) {} else { initialExpression.add(initialExpression_xx); }
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

    public String toPython(int indent)
  {   String result = "";
 
  if ((type == null)) 
  {   result = Statement.tab(indent) + assignsTo + " = None\n";
 
  }  else
      {   if ((initialExpression).size() > 0) 
  {   result = Statement.tab(indent) + assignsTo + " = " + ((Expression) Set.any(initialExpression)).toPython() + "\n";
 
  }  else
      {   if ((type instanceof Entity)) 
  {   result = Statement.tab(indent) + assignsTo + " = None\n";
 
  }  else
      if (true) 
  {   result = Statement.tab(indent) + assignsTo + " = " + type.defaultInitialValue() + "\n";
 
  }   
   } 
   }     return result;
  }



}


abstract class BehaviouralFeature
  extends Feature
  implements SystemTypes
{
  protected boolean isStatic = false; // internal
  protected List parameters = new Vector(); // of Property
  protected Statement activity;

  public BehaviouralFeature(Statement activity)
  {
    this.isStatic = false;
    this.activity = activity;

  }

  public BehaviouralFeature() { }



  public String toString()
  { String _res_ = "(BehaviouralFeature) ";
    _res_ = _res_ + isStatic;
    return _res_ + super.toString();
  }



  public void setisStatic(boolean isStatic_x) { isStatic = isStatic_x;  }


    public static void setAllisStatic(List behaviouralfeatures,boolean val)
  { for (int i = 0; i < behaviouralfeatures.size(); i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(i);
      Controller.inst().setisStatic(behaviouralfeaturex,val); } }


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

    public boolean getisStatic() { return isStatic; }

    public static List getAllisStatic(List behaviouralfeatures)
  { List result = new Vector();
    for (int i = 0; i < behaviouralfeatures.size(); i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(i);
      if (result.contains(new Boolean(behaviouralfeaturex.getisStatic()))) { }
      else { result.add(new Boolean(behaviouralfeaturex.getisStatic())); } }
    return result; }

    public static List getAllOrderedisStatic(List behaviouralfeatures)
  { List result = new Vector();
    for (int i = 0; i < behaviouralfeatures.size(); i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) behaviouralfeatures.get(i);
      result.add(new Boolean(behaviouralfeaturex.getisStatic())); } 
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



public class Controller implements SystemTypes, ControllerInterface
{
  Vector namedelements = new Vector();
  Vector relationships = new Vector();
  Vector features = new Vector();
  Vector types = new Vector();
  Map typetypeIdindex = new HashMap(); // String --> Type

  Vector associations = new Vector();
  Vector generalizations = new Vector();
  Vector classifiers = new Vector();
  Vector datatypes = new Vector();
  Vector enumerations = new Vector();
  Vector enumerationliterals = new Vector();
  Vector primitivetypes = new Vector();
  Vector entitys = new Vector();
  Vector collectiontypes = new Vector();
  Vector expressions = new Vector();
  Map expressionexpIdindex = new HashMap(); // String --> Expression

  Vector binaryexpressions = new Vector();
  Vector conditionalexpressions = new Vector();
  Vector unaryexpressions = new Vector();
  Vector collectionexpressions = new Vector();
  Vector basicexpressions = new Vector();
  Vector propertys = new Vector();
  Vector statements = new Vector();
  Map statementstatIdindex = new HashMap(); // String --> Statement

  Vector returnstatements = new Vector();
  Vector assertstatements = new Vector();
  Vector errorstatements = new Vector();
  Vector catchstatements = new Vector();
  Vector finalstatements = new Vector();
  Vector operations = new Vector();
  Vector usecases = new Vector();
  Vector breakstatements = new Vector();
  Vector continuestatements = new Vector();
  Vector operationcallstatements = new Vector();
  Vector implicitcallstatements = new Vector();
  Vector loopstatements = new Vector();
  Vector boundedloopstatements = new Vector();
  Vector unboundedloopstatements = new Vector();
  Vector assignstatements = new Vector();
  Vector sequencestatements = new Vector();
  Vector trystatements = new Vector();
  Vector conditionalstatements = new Vector();
  Vector creationstatements = new Vector();
  Vector behaviouralfeatures = new Vector();
  Vector printcodes = new Vector();
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
      Class cont = Class.forName("uml2py3.Controller");
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
            try { cl = Class.forName("uml2py3." + right); }
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
    { File _continuestatement = new File("ContinueStatement.csv");
      __br = new BufferedReader(new FileReader(_continuestatement));
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
        { ContinueStatement continuestatementx = ContinueStatement.parseCSV(__s.trim());
          if (continuestatementx != null)
          { __cont.addContinueStatement(continuestatementx); }
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
  for (int _i = 0; _i < propertys.size(); _i++)
  { Property ownedAttribute_propertyx1 = (Property) propertys.get(_i);
    for (int _j = 0; _j < entitys.size(); _j++)
    { Entity owner_entityx2 = (Entity) entitys.get(_j);
      if (ownedAttribute_propertyx1.getowner() == owner_entityx2)
      { if (owner_entityx2.getownedAttribute().contains(ownedAttribute_propertyx1)) { }
        else { owner_entityx2.addownedAttribute(ownedAttribute_propertyx1); }
      }
      else if (owner_entityx2.getownedAttribute().contains(ownedAttribute_propertyx1))
      { ownedAttribute_propertyx1.setowner(owner_entityx2); } 
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
  }


  public void saveModel(String file)
  { File outfile = new File(file); 
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
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

  for (int _i = 0; _i < enumerations.size(); _i++)
  { Enumeration enumerationx_ = (Enumeration) enumerations.get(_i);
    out.println("enumerationx_" + _i + " : Enumeration");
    out.println("enumerationx_" + _i + ".typeId = \"" + enumerationx_.gettypeId() + "\"");
    out.println("enumerationx_" + _i + ".isSorted = " + enumerationx_.getisSorted());
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
    out.println("primitivetypex_" + _i + ".isSorted = " + primitivetypex_.getisSorted());
    out.println("primitivetypex_" + _i + ".name = \"" + primitivetypex_.getname() + "\"");
  }

  for (int _i = 0; _i < entitys.size(); _i++)
  { Entity entityx_ = (Entity) entitys.get(_i);
    out.println("entityx_" + _i + " : Entity");
    out.println("entityx_" + _i + ".isAbstract = " + entityx_.getisAbstract());
    out.println("entityx_" + _i + ".isInterface = " + entityx_.getisInterface());
    List entity_stereotypes = entityx_.getstereotypes();
    for (int _j = 0; _j < entity_stereotypes.size(); _j++)
    { out.println("\"" + entity_stereotypes.get(_j) + "\" : entityx_" + _i + ".stereotypes");
    }
    out.println("entityx_" + _i + ".typeId = \"" + entityx_.gettypeId() + "\"");
    out.println("entityx_" + _i + ".isSorted = " + entityx_.getisSorted());
    out.println("entityx_" + _i + ".name = \"" + entityx_.getname() + "\"");
  }

  for (int _i = 0; _i < collectiontypes.size(); _i++)
  { CollectionType collectiontypex_ = (CollectionType) collectiontypes.get(_i);
    out.println("collectiontypex_" + _i + " : CollectionType");
    out.println("collectiontypex_" + _i + ".typeId = \"" + collectiontypex_.gettypeId() + "\"");
    out.println("collectiontypex_" + _i + ".isSorted = " + collectiontypex_.getisSorted());
    out.println("collectiontypex_" + _i + ".name = \"" + collectiontypex_.getname() + "\"");
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
    out.println("collectionexpressionx_" + _i + ".isSorted = " + collectionexpressionx_.getisSorted());
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

  for (int _i = 0; _i < propertys.size(); _i++)
  { Property propertyx_ = (Property) propertys.get(_i);
    out.println("propertyx_" + _i + " : Property");
    out.println("propertyx_" + _i + ".name = \"" + propertyx_.getname() + "\"");
    out.println("propertyx_" + _i + ".lower = " + propertyx_.getlower());
    out.println("propertyx_" + _i + ".upper = " + propertyx_.getupper());
    out.println("propertyx_" + _i + ".isOrdered = " + propertyx_.getisOrdered());
    out.println("propertyx_" + _i + ".isUnique = " + propertyx_.getisUnique());
    out.println("propertyx_" + _i + ".isDerived = " + propertyx_.getisDerived());
    out.println("propertyx_" + _i + ".isReadOnly = " + propertyx_.getisReadOnly());
    out.println("propertyx_" + _i + ".isStatic = " + propertyx_.getisStatic());
  }

  for (int _i = 0; _i < returnstatements.size(); _i++)
  { ReturnStatement returnstatementx_ = (ReturnStatement) returnstatements.get(_i);
    out.println("returnstatementx_" + _i + " : ReturnStatement");
    out.println("returnstatementx_" + _i + ".statId = \"" + returnstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < assertstatements.size(); _i++)
  { AssertStatement assertstatementx_ = (AssertStatement) assertstatements.get(_i);
    out.println("assertstatementx_" + _i + " : AssertStatement");
    out.println("assertstatementx_" + _i + ".statId = \"" + assertstatementx_.getstatId() + "\"");
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

  for (int _i = 0; _i < operations.size(); _i++)
  { Operation operationx_ = (Operation) operations.get(_i);
    out.println("operationx_" + _i + " : Operation");
    out.println("operationx_" + _i + ".isQuery = " + operationx_.getisQuery());
    out.println("operationx_" + _i + ".isAbstract = " + operationx_.getisAbstract());
    out.println("operationx_" + _i + ".isCached = " + operationx_.getisCached());
    out.println("operationx_" + _i + ".isStatic = " + operationx_.getisStatic());
    out.println("operationx_" + _i + ".name = \"" + operationx_.getname() + "\"");
  }

  for (int _i = 0; _i < usecases.size(); _i++)
  { UseCase usecasex_ = (UseCase) usecases.get(_i);
    out.println("usecasex_" + _i + " : UseCase");
    out.println("usecasex_" + _i + ".name = \"" + usecasex_.getname() + "\"");
  }

  for (int _i = 0; _i < breakstatements.size(); _i++)
  { BreakStatement breakstatementx_ = (BreakStatement) breakstatements.get(_i);
    out.println("breakstatementx_" + _i + " : BreakStatement");
    out.println("breakstatementx_" + _i + ".statId = \"" + breakstatementx_.getstatId() + "\"");
  }

  for (int _i = 0; _i < continuestatements.size(); _i++)
  { ContinueStatement continuestatementx_ = (ContinueStatement) continuestatements.get(_i);
    out.println("continuestatementx_" + _i + " : ContinueStatement");
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

  for (int _i = 0; _i < trystatements.size(); _i++)
  { TryStatement trystatementx_ = (TryStatement) trystatements.get(_i);
    out.println("trystatementx_" + _i + " : TryStatement");
    out.println("trystatementx_" + _i + ".statId = \"" + trystatementx_.getstatId() + "\"");
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

  for (int _i = 0; _i < printcodes.size(); _i++)
  { Printcode printcodex_ = (Printcode) printcodes.get(_i);
    out.println("printcodex_" + _i + " : Printcode");
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
  for (int _i = 0; _i < enumerations.size(); _i++)
  { Enumeration enumerationx_ = (Enumeration) enumerations.get(_i);
    List enumeration_ownedLiteral_EnumerationLiteral = enumerationx_.getownedLiteral();
    for (int _j = 0; _j < enumeration_ownedLiteral_EnumerationLiteral.size(); _j++)
    { out.println("enumerationliteralx_" + enumerationliterals.indexOf(enumeration_ownedLiteral_EnumerationLiteral.get(_j)) + " : enumerationx_" + _i + ".ownedLiteral");
    }
  }
  for (int _i = 0; _i < entitys.size(); _i++)
  { Entity entityx_ = (Entity) entitys.get(_i);
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
  }
  for (int _i = 0; _i < collectiontypes.size(); _i++)
  { CollectionType collectiontypex_ = (CollectionType) collectiontypes.get(_i);
    if (collectiontypex_.getelementType() instanceof Entity)
    { out.println("collectiontypex_" + _i + ".elementType = entityx_" + entitys.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getelementType() instanceof Enumeration)
    { out.println("collectiontypex_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getelementType() instanceof PrimitiveType)
    { out.println("collectiontypex_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getelementType() instanceof CollectionType)
    { out.println("collectiontypex_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(collectiontypex_.getelementType())); } 
    if (collectiontypex_.getkeyType() instanceof Entity)
    { out.println("collectiontypex_" + _i + ".keyType = entityx_" + entitys.indexOf(collectiontypex_.getkeyType())); } 
    if (collectiontypex_.getkeyType() instanceof Enumeration)
    { out.println("collectiontypex_" + _i + ".keyType = enumerationx_" + enumerations.indexOf(collectiontypex_.getkeyType())); } 
    if (collectiontypex_.getkeyType() instanceof PrimitiveType)
    { out.println("collectiontypex_" + _i + ".keyType = primitivetypex_" + primitivetypes.indexOf(collectiontypex_.getkeyType())); } 
    if (collectiontypex_.getkeyType() instanceof CollectionType)
    { out.println("collectiontypex_" + _i + ".keyType = collectiontypex_" + collectiontypes.indexOf(collectiontypex_.getkeyType())); } 
  }
  for (int _i = 0; _i < binaryexpressions.size(); _i++)
  { BinaryExpression binaryexpressionx_ = (BinaryExpression) binaryexpressions.get(_i);
    if (binaryexpressionx_.getleft() instanceof UnaryExpression)
    { out.println("binaryexpressionx_" + _i + ".left = unaryexpressionx_" + unaryexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getleft() instanceof BasicExpression)
    { out.println("binaryexpressionx_" + _i + ".left = basicexpressionx_" + basicexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getleft() instanceof BinaryExpression)
    { out.println("binaryexpressionx_" + _i + ".left = binaryexpressionx_" + binaryexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getleft() instanceof ConditionalExpression)
    { out.println("binaryexpressionx_" + _i + ".left = conditionalexpressionx_" + conditionalexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getleft() instanceof CollectionExpression)
    { out.println("binaryexpressionx_" + _i + ".left = collectionexpressionx_" + collectionexpressions.indexOf(binaryexpressionx_.getleft())); } 
    if (binaryexpressionx_.getright() instanceof UnaryExpression)
    { out.println("binaryexpressionx_" + _i + ".right = unaryexpressionx_" + unaryexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.getright() instanceof BasicExpression)
    { out.println("binaryexpressionx_" + _i + ".right = basicexpressionx_" + basicexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.getright() instanceof BinaryExpression)
    { out.println("binaryexpressionx_" + _i + ".right = binaryexpressionx_" + binaryexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.getright() instanceof ConditionalExpression)
    { out.println("binaryexpressionx_" + _i + ".right = conditionalexpressionx_" + conditionalexpressions.indexOf(binaryexpressionx_.getright())); } 
    if (binaryexpressionx_.getright() instanceof CollectionExpression)
    { out.println("binaryexpressionx_" + _i + ".right = collectionexpressionx_" + collectionexpressions.indexOf(binaryexpressionx_.getright())); } 
    out.println("binaryexpressionx_" + _i + ".accumulator = propertyx_" + propertys.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getaccumulator()));
    if (binaryexpressionx_.gettype() instanceof Entity)
    { out.println("binaryexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.gettype() instanceof Enumeration)
    { out.println("binaryexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("binaryexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.gettype() instanceof CollectionType)
    { out.println("binaryexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(binaryexpressionx_.gettype())); } 
    if (binaryexpressionx_.getelementType() instanceof Entity)
    { out.println("binaryexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(binaryexpressionx_.getelementType())); } 
    if (binaryexpressionx_.getelementType() instanceof Enumeration)
    { out.println("binaryexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(binaryexpressionx_.getelementType())); } 
    if (binaryexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("binaryexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(binaryexpressionx_.getelementType())); } 
    if (binaryexpressionx_.getelementType() instanceof CollectionType)
    { out.println("binaryexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(binaryexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < conditionalexpressions.size(); _i++)
  { ConditionalExpression conditionalexpressionx_ = (ConditionalExpression) conditionalexpressions.get(_i);
    if (conditionalexpressionx_.gettest() instanceof UnaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = unaryexpressionx_" + unaryexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.gettest() instanceof BasicExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = basicexpressionx_" + basicexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.gettest() instanceof BinaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = binaryexpressionx_" + binaryexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.gettest() instanceof ConditionalExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = conditionalexpressionx_" + conditionalexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.gettest() instanceof CollectionExpression)
    { out.println("conditionalexpressionx_" + _i + ".test = collectionexpressionx_" + collectionexpressions.indexOf(conditionalexpressionx_.gettest())); } 
    if (conditionalexpressionx_.getifExp() instanceof UnaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = unaryexpressionx_" + unaryexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getifExp() instanceof BasicExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = basicexpressionx_" + basicexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getifExp() instanceof BinaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = binaryexpressionx_" + binaryexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getifExp() instanceof ConditionalExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = conditionalexpressionx_" + conditionalexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getifExp() instanceof CollectionExpression)
    { out.println("conditionalexpressionx_" + _i + ".ifExp = collectionexpressionx_" + collectionexpressions.indexOf(conditionalexpressionx_.getifExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof UnaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = unaryexpressionx_" + unaryexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof BasicExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = basicexpressionx_" + basicexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof BinaryExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = binaryexpressionx_" + binaryexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof ConditionalExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = conditionalexpressionx_" + conditionalexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.getelseExp() instanceof CollectionExpression)
    { out.println("conditionalexpressionx_" + _i + ".elseExp = collectionexpressionx_" + collectionexpressions.indexOf(conditionalexpressionx_.getelseExp())); } 
    if (conditionalexpressionx_.gettype() instanceof Entity)
    { out.println("conditionalexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.gettype() instanceof Enumeration)
    { out.println("conditionalexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("conditionalexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.gettype() instanceof CollectionType)
    { out.println("conditionalexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(conditionalexpressionx_.gettype())); } 
    if (conditionalexpressionx_.getelementType() instanceof Entity)
    { out.println("conditionalexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(conditionalexpressionx_.getelementType())); } 
    if (conditionalexpressionx_.getelementType() instanceof Enumeration)
    { out.println("conditionalexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(conditionalexpressionx_.getelementType())); } 
    if (conditionalexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("conditionalexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(conditionalexpressionx_.getelementType())); } 
    if (conditionalexpressionx_.getelementType() instanceof CollectionType)
    { out.println("conditionalexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(conditionalexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < unaryexpressions.size(); _i++)
  { UnaryExpression unaryexpressionx_ = (UnaryExpression) unaryexpressions.get(_i);
    if (unaryexpressionx_.getargument() instanceof UnaryExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = unaryexpressionx_" + unaryexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.getargument() instanceof BasicExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = basicexpressionx_" + basicexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.getargument() instanceof BinaryExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = binaryexpressionx_" + binaryexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.getargument() instanceof ConditionalExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = conditionalexpressionx_" + conditionalexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.getargument() instanceof CollectionExpression)
    { out.println("unaryexpressionx_" + _i + ".argument = collectionexpressionx_" + collectionexpressions.indexOf(unaryexpressionx_.getargument())); } 
    if (unaryexpressionx_.gettype() instanceof Entity)
    { out.println("unaryexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.gettype() instanceof Enumeration)
    { out.println("unaryexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("unaryexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.gettype() instanceof CollectionType)
    { out.println("unaryexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(unaryexpressionx_.gettype())); } 
    if (unaryexpressionx_.getelementType() instanceof Entity)
    { out.println("unaryexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(unaryexpressionx_.getelementType())); } 
    if (unaryexpressionx_.getelementType() instanceof Enumeration)
    { out.println("unaryexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(unaryexpressionx_.getelementType())); } 
    if (unaryexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("unaryexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(unaryexpressionx_.getelementType())); } 
    if (unaryexpressionx_.getelementType() instanceof CollectionType)
    { out.println("unaryexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(unaryexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < collectionexpressions.size(); _i++)
  { CollectionExpression collectionexpressionx_ = (CollectionExpression) collectionexpressions.get(_i);
    List collectionexpression_elements_Expression = collectionexpressionx_.getelements();
    for (int _k = 0; _k < collectionexpression_elements_Expression.size(); _k++)
    { if (collectionexpression_elements_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
 if (collectionexpression_elements_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
 if (collectionexpression_elements_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
 if (collectionexpression_elements_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
 if (collectionexpression_elements_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(collectionexpression_elements_Expression.get(_k)) + " : collectionexpressionx_" + _i + ".elements"); }
  }
    if (collectionexpressionx_.gettype() instanceof Entity)
    { out.println("collectionexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.gettype() instanceof Enumeration)
    { out.println("collectionexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("collectionexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.gettype() instanceof CollectionType)
    { out.println("collectionexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(collectionexpressionx_.gettype())); } 
    if (collectionexpressionx_.getelementType() instanceof Entity)
    { out.println("collectionexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(collectionexpressionx_.getelementType())); } 
    if (collectionexpressionx_.getelementType() instanceof Enumeration)
    { out.println("collectionexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(collectionexpressionx_.getelementType())); } 
    if (collectionexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("collectionexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(collectionexpressionx_.getelementType())); } 
    if (collectionexpressionx_.getelementType() instanceof CollectionType)
    { out.println("collectionexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(collectionexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < basicexpressions.size(); _i++)
  { BasicExpression basicexpressionx_ = (BasicExpression) basicexpressions.get(_i);
    List basicexpression_parameters_Expression = basicexpressionx_.getparameters();
    for (int _k = 0; _k < basicexpression_parameters_Expression.size(); _k++)
    { if (basicexpression_parameters_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
 if (basicexpression_parameters_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
 if (basicexpression_parameters_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
 if (basicexpression_parameters_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(basicexpression_parameters_Expression.get(_k)) + " : basicexpressionx_" + _i + ".parameters"); }
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
 if (basicexpression_arrayIndex_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
 if (basicexpression_arrayIndex_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
 if (basicexpression_arrayIndex_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
 if (basicexpression_arrayIndex_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(basicexpression_arrayIndex_Expression.get(_k)) + " : basicexpressionx_" + _i + ".arrayIndex"); }
  }
    List basicexpression_objectRef_Expression = basicexpressionx_.getobjectRef();
    for (int _k = 0; _k < basicexpression_objectRef_Expression.size(); _k++)
    { if (basicexpression_objectRef_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
 if (basicexpression_objectRef_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
 if (basicexpression_objectRef_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
 if (basicexpression_objectRef_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
 if (basicexpression_objectRef_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(basicexpression_objectRef_Expression.get(_k)) + " : basicexpressionx_" + _i + ".objectRef"); }
  }
    if (basicexpressionx_.gettype() instanceof Entity)
    { out.println("basicexpressionx_" + _i + ".type = entityx_" + entitys.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.gettype() instanceof Enumeration)
    { out.println("basicexpressionx_" + _i + ".type = enumerationx_" + enumerations.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.gettype() instanceof PrimitiveType)
    { out.println("basicexpressionx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.gettype() instanceof CollectionType)
    { out.println("basicexpressionx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(basicexpressionx_.gettype())); } 
    if (basicexpressionx_.getelementType() instanceof Entity)
    { out.println("basicexpressionx_" + _i + ".elementType = entityx_" + entitys.indexOf(basicexpressionx_.getelementType())); } 
    if (basicexpressionx_.getelementType() instanceof Enumeration)
    { out.println("basicexpressionx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(basicexpressionx_.getelementType())); } 
    if (basicexpressionx_.getelementType() instanceof PrimitiveType)
    { out.println("basicexpressionx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(basicexpressionx_.getelementType())); } 
    if (basicexpressionx_.getelementType() instanceof CollectionType)
    { out.println("basicexpressionx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(basicexpressionx_.getelementType())); } 
  }
  for (int _i = 0; _i < propertys.size(); _i++)
  { Property propertyx_ = (Property) propertys.get(_i);
    List property_qualifier_Property = propertyx_.getqualifier();
    for (int _j = 0; _j < property_qualifier_Property.size(); _j++)
    { out.println("propertyx_" + propertys.indexOf(property_qualifier_Property.get(_j)) + " : propertyx_" + _i + ".qualifier");
    }
    if (propertyx_.gettype() instanceof Entity)
    { out.println("propertyx_" + _i + ".type = entityx_" + entitys.indexOf(propertyx_.gettype())); } 
    if (propertyx_.gettype() instanceof Enumeration)
    { out.println("propertyx_" + _i + ".type = enumerationx_" + enumerations.indexOf(propertyx_.gettype())); } 
    if (propertyx_.gettype() instanceof PrimitiveType)
    { out.println("propertyx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(propertyx_.gettype())); } 
    if (propertyx_.gettype() instanceof CollectionType)
    { out.println("propertyx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(propertyx_.gettype())); } 
    if (propertyx_.getinitialValue() instanceof UnaryExpression)
    { out.println("propertyx_" + _i + ".initialValue = unaryexpressionx_" + unaryexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.getinitialValue() instanceof BasicExpression)
    { out.println("propertyx_" + _i + ".initialValue = basicexpressionx_" + basicexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.getinitialValue() instanceof BinaryExpression)
    { out.println("propertyx_" + _i + ".initialValue = binaryexpressionx_" + binaryexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.getinitialValue() instanceof ConditionalExpression)
    { out.println("propertyx_" + _i + ".initialValue = conditionalexpressionx_" + conditionalexpressions.indexOf(propertyx_.getinitialValue())); } 
    if (propertyx_.getinitialValue() instanceof CollectionExpression)
    { out.println("propertyx_" + _i + ".initialValue = collectionexpressionx_" + collectionexpressions.indexOf(propertyx_.getinitialValue())); } 
    out.println("propertyx_" + _i + ".owner = entityx_" + entitys.indexOf(((Property) propertys.get(_i)).getowner()));
  }
  for (int _i = 0; _i < returnstatements.size(); _i++)
  { ReturnStatement returnstatementx_ = (ReturnStatement) returnstatements.get(_i);
    List returnstatement_returnValue_Expression = returnstatementx_.getreturnValue();
    for (int _k = 0; _k < returnstatement_returnValue_Expression.size(); _k++)
    { if (returnstatement_returnValue_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
 if (returnstatement_returnValue_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
 if (returnstatement_returnValue_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
 if (returnstatement_returnValue_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
 if (returnstatement_returnValue_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(returnstatement_returnValue_Expression.get(_k)) + " : returnstatementx_" + _i + ".returnValue"); }
  }
  }
  for (int _i = 0; _i < assertstatements.size(); _i++)
  { AssertStatement assertstatementx_ = (AssertStatement) assertstatements.get(_i);
    if (assertstatementx_.getcondition() instanceof UnaryExpression)
    { out.println("assertstatementx_" + _i + ".condition = unaryexpressionx_" + unaryexpressions.indexOf(assertstatementx_.getcondition())); } 
    if (assertstatementx_.getcondition() instanceof BasicExpression)
    { out.println("assertstatementx_" + _i + ".condition = basicexpressionx_" + basicexpressions.indexOf(assertstatementx_.getcondition())); } 
    if (assertstatementx_.getcondition() instanceof BinaryExpression)
    { out.println("assertstatementx_" + _i + ".condition = binaryexpressionx_" + binaryexpressions.indexOf(assertstatementx_.getcondition())); } 
    if (assertstatementx_.getcondition() instanceof ConditionalExpression)
    { out.println("assertstatementx_" + _i + ".condition = conditionalexpressionx_" + conditionalexpressions.indexOf(assertstatementx_.getcondition())); } 
    if (assertstatementx_.getcondition() instanceof CollectionExpression)
    { out.println("assertstatementx_" + _i + ".condition = collectionexpressionx_" + collectionexpressions.indexOf(assertstatementx_.getcondition())); } 
    List assertstatement_message_Expression = assertstatementx_.getmessage();
    for (int _k = 0; _k < assertstatement_message_Expression.size(); _k++)
    { if (assertstatement_message_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
 if (assertstatement_message_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
 if (assertstatement_message_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
 if (assertstatement_message_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
 if (assertstatement_message_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(assertstatement_message_Expression.get(_k)) + " : assertstatementx_" + _i + ".message"); }
  }
  }
  for (int _i = 0; _i < errorstatements.size(); _i++)
  { ErrorStatement errorstatementx_ = (ErrorStatement) errorstatements.get(_i);
    if (errorstatementx_.getthrownObject() instanceof UnaryExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = unaryexpressionx_" + unaryexpressions.indexOf(errorstatementx_.getthrownObject())); } 
    if (errorstatementx_.getthrownObject() instanceof BasicExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = basicexpressionx_" + basicexpressions.indexOf(errorstatementx_.getthrownObject())); } 
    if (errorstatementx_.getthrownObject() instanceof BinaryExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = binaryexpressionx_" + binaryexpressions.indexOf(errorstatementx_.getthrownObject())); } 
    if (errorstatementx_.getthrownObject() instanceof ConditionalExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = conditionalexpressionx_" + conditionalexpressions.indexOf(errorstatementx_.getthrownObject())); } 
    if (errorstatementx_.getthrownObject() instanceof CollectionExpression)
    { out.println("errorstatementx_" + _i + ".thrownObject = collectionexpressionx_" + collectionexpressions.indexOf(errorstatementx_.getthrownObject())); } 
  }
  for (int _i = 0; _i < catchstatements.size(); _i++)
  { CatchStatement catchstatementx_ = (CatchStatement) catchstatements.get(_i);
    if (catchstatementx_.getcaughtObject() instanceof UnaryExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = unaryexpressionx_" + unaryexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getcaughtObject() instanceof BasicExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = basicexpressionx_" + basicexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getcaughtObject() instanceof BinaryExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = binaryexpressionx_" + binaryexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getcaughtObject() instanceof ConditionalExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = conditionalexpressionx_" + conditionalexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getcaughtObject() instanceof CollectionExpression)
    { out.println("catchstatementx_" + _i + ".caughtObject = collectionexpressionx_" + collectionexpressions.indexOf(catchstatementx_.getcaughtObject())); } 
    if (catchstatementx_.getaction() instanceof ReturnStatement)
    { out.println("catchstatementx_" + _i + ".action = returnstatementx_" + returnstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof AssertStatement)
    { out.println("catchstatementx_" + _i + ".action = assertstatementx_" + assertstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof ErrorStatement)
    { out.println("catchstatementx_" + _i + ".action = errorstatementx_" + errorstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof CatchStatement)
    { out.println("catchstatementx_" + _i + ".action = catchstatementx_" + catchstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof FinalStatement)
    { out.println("catchstatementx_" + _i + ".action = finalstatementx_" + finalstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof BreakStatement)
    { out.println("catchstatementx_" + _i + ".action = breakstatementx_" + breakstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof OperationCallStatement)
    { out.println("catchstatementx_" + _i + ".action = operationcallstatementx_" + operationcallstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof ImplicitCallStatement)
    { out.println("catchstatementx_" + _i + ".action = implicitcallstatementx_" + implicitcallstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof SequenceStatement)
    { out.println("catchstatementx_" + _i + ".action = sequencestatementx_" + sequencestatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof TryStatement)
    { out.println("catchstatementx_" + _i + ".action = trystatementx_" + trystatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof ConditionalStatement)
    { out.println("catchstatementx_" + _i + ".action = conditionalstatementx_" + conditionalstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof AssignStatement)
    { out.println("catchstatementx_" + _i + ".action = assignstatementx_" + assignstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof CreationStatement)
    { out.println("catchstatementx_" + _i + ".action = creationstatementx_" + creationstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof BoundedLoopStatement)
    { out.println("catchstatementx_" + _i + ".action = boundedloopstatementx_" + boundedloopstatements.indexOf(catchstatementx_.getaction())); } 
    if (catchstatementx_.getaction() instanceof UnboundedLoopStatement)
    { out.println("catchstatementx_" + _i + ".action = unboundedloopstatementx_" + unboundedloopstatements.indexOf(catchstatementx_.getaction())); } 
  }
  for (int _i = 0; _i < finalstatements.size(); _i++)
  { FinalStatement finalstatementx_ = (FinalStatement) finalstatements.get(_i);
    if (finalstatementx_.getbody() instanceof ReturnStatement)
    { out.println("finalstatementx_" + _i + ".body = returnstatementx_" + returnstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof AssertStatement)
    { out.println("finalstatementx_" + _i + ".body = assertstatementx_" + assertstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof ErrorStatement)
    { out.println("finalstatementx_" + _i + ".body = errorstatementx_" + errorstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof CatchStatement)
    { out.println("finalstatementx_" + _i + ".body = catchstatementx_" + catchstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof FinalStatement)
    { out.println("finalstatementx_" + _i + ".body = finalstatementx_" + finalstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof BreakStatement)
    { out.println("finalstatementx_" + _i + ".body = breakstatementx_" + breakstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof OperationCallStatement)
    { out.println("finalstatementx_" + _i + ".body = operationcallstatementx_" + operationcallstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof ImplicitCallStatement)
    { out.println("finalstatementx_" + _i + ".body = implicitcallstatementx_" + implicitcallstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof SequenceStatement)
    { out.println("finalstatementx_" + _i + ".body = sequencestatementx_" + sequencestatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof TryStatement)
    { out.println("finalstatementx_" + _i + ".body = trystatementx_" + trystatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof ConditionalStatement)
    { out.println("finalstatementx_" + _i + ".body = conditionalstatementx_" + conditionalstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof AssignStatement)
    { out.println("finalstatementx_" + _i + ".body = assignstatementx_" + assignstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof CreationStatement)
    { out.println("finalstatementx_" + _i + ".body = creationstatementx_" + creationstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof BoundedLoopStatement)
    { out.println("finalstatementx_" + _i + ".body = boundedloopstatementx_" + boundedloopstatements.indexOf(finalstatementx_.getbody())); } 
    if (finalstatementx_.getbody() instanceof UnboundedLoopStatement)
    { out.println("finalstatementx_" + _i + ".body = unboundedloopstatementx_" + unboundedloopstatements.indexOf(finalstatementx_.getbody())); } 
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
    if (operationx_.getactivity() instanceof AssertStatement)
    { out.println("operationx_" + _i + ".activity = assertstatementx_" + assertstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof ErrorStatement)
    { out.println("operationx_" + _i + ".activity = errorstatementx_" + errorstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof CatchStatement)
    { out.println("operationx_" + _i + ".activity = catchstatementx_" + catchstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof FinalStatement)
    { out.println("operationx_" + _i + ".activity = finalstatementx_" + finalstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof BreakStatement)
    { out.println("operationx_" + _i + ".activity = breakstatementx_" + breakstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof OperationCallStatement)
    { out.println("operationx_" + _i + ".activity = operationcallstatementx_" + operationcallstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof ImplicitCallStatement)
    { out.println("operationx_" + _i + ".activity = implicitcallstatementx_" + implicitcallstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof SequenceStatement)
    { out.println("operationx_" + _i + ".activity = sequencestatementx_" + sequencestatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof TryStatement)
    { out.println("operationx_" + _i + ".activity = trystatementx_" + trystatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof ConditionalStatement)
    { out.println("operationx_" + _i + ".activity = conditionalstatementx_" + conditionalstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof AssignStatement)
    { out.println("operationx_" + _i + ".activity = assignstatementx_" + assignstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof CreationStatement)
    { out.println("operationx_" + _i + ".activity = creationstatementx_" + creationstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof BoundedLoopStatement)
    { out.println("operationx_" + _i + ".activity = boundedloopstatementx_" + boundedloopstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.getactivity() instanceof UnboundedLoopStatement)
    { out.println("operationx_" + _i + ".activity = unboundedloopstatementx_" + unboundedloopstatements.indexOf(operationx_.getactivity())); } 
    if (operationx_.gettype() instanceof Entity)
    { out.println("operationx_" + _i + ".type = entityx_" + entitys.indexOf(operationx_.gettype())); } 
    if (operationx_.gettype() instanceof Enumeration)
    { out.println("operationx_" + _i + ".type = enumerationx_" + enumerations.indexOf(operationx_.gettype())); } 
    if (operationx_.gettype() instanceof PrimitiveType)
    { out.println("operationx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(operationx_.gettype())); } 
    if (operationx_.gettype() instanceof CollectionType)
    { out.println("operationx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(operationx_.gettype())); } 
    if (operationx_.getelementType() instanceof Entity)
    { out.println("operationx_" + _i + ".elementType = entityx_" + entitys.indexOf(operationx_.getelementType())); } 
    if (operationx_.getelementType() instanceof Enumeration)
    { out.println("operationx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(operationx_.getelementType())); } 
    if (operationx_.getelementType() instanceof PrimitiveType)
    { out.println("operationx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(operationx_.getelementType())); } 
    if (operationx_.getelementType() instanceof CollectionType)
    { out.println("operationx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(operationx_.getelementType())); } 
  }
  for (int _i = 0; _i < usecases.size(); _i++)
  { UseCase usecasex_ = (UseCase) usecases.get(_i);
    List usecase_parameters_Property = usecasex_.getparameters();
    for (int _j = 0; _j < usecase_parameters_Property.size(); _j++)
    { out.println("propertyx_" + propertys.indexOf(usecase_parameters_Property.get(_j)) + " : usecasex_" + _i + ".parameters");
    }
    if (usecasex_.getresultType() instanceof Entity)
    { out.println("usecasex_" + _i + ".resultType = entityx_" + entitys.indexOf(usecasex_.getresultType())); } 
    if (usecasex_.getresultType() instanceof Enumeration)
    { out.println("usecasex_" + _i + ".resultType = enumerationx_" + enumerations.indexOf(usecasex_.getresultType())); } 
    if (usecasex_.getresultType() instanceof PrimitiveType)
    { out.println("usecasex_" + _i + ".resultType = primitivetypex_" + primitivetypes.indexOf(usecasex_.getresultType())); } 
    if (usecasex_.getresultType() instanceof CollectionType)
    { out.println("usecasex_" + _i + ".resultType = collectiontypex_" + collectiontypes.indexOf(usecasex_.getresultType())); } 
    if (usecasex_.getclassifierBehaviour() instanceof ReturnStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = returnstatementx_" + returnstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof AssertStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = assertstatementx_" + assertstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof ErrorStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = errorstatementx_" + errorstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof CatchStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = catchstatementx_" + catchstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof FinalStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = finalstatementx_" + finalstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof BreakStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = breakstatementx_" + breakstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof OperationCallStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = operationcallstatementx_" + operationcallstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof ImplicitCallStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = implicitcallstatementx_" + implicitcallstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof SequenceStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = sequencestatementx_" + sequencestatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof TryStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = trystatementx_" + trystatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof ConditionalStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = conditionalstatementx_" + conditionalstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof AssignStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = assignstatementx_" + assignstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof CreationStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = creationstatementx_" + creationstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof BoundedLoopStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = boundedloopstatementx_" + boundedloopstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
    if (usecasex_.getclassifierBehaviour() instanceof UnboundedLoopStatement)
    { out.println("usecasex_" + _i + ".classifierBehaviour = unboundedloopstatementx_" + unboundedloopstatements.indexOf(usecasex_.getclassifierBehaviour())); } 
  }
  for (int _i = 0; _i < operationcallstatements.size(); _i++)
  { OperationCallStatement operationcallstatementx_ = (OperationCallStatement) operationcallstatements.get(_i);
    if (operationcallstatementx_.getcallExp() instanceof UnaryExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = unaryexpressionx_" + unaryexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
    if (operationcallstatementx_.getcallExp() instanceof BasicExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = basicexpressionx_" + basicexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
    if (operationcallstatementx_.getcallExp() instanceof BinaryExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = binaryexpressionx_" + binaryexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
    if (operationcallstatementx_.getcallExp() instanceof ConditionalExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = conditionalexpressionx_" + conditionalexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
    if (operationcallstatementx_.getcallExp() instanceof CollectionExpression)
    { out.println("operationcallstatementx_" + _i + ".callExp = collectionexpressionx_" + collectionexpressions.indexOf(operationcallstatementx_.getcallExp())); } 
  }
  for (int _i = 0; _i < implicitcallstatements.size(); _i++)
  { ImplicitCallStatement implicitcallstatementx_ = (ImplicitCallStatement) implicitcallstatements.get(_i);
    if (implicitcallstatementx_.getcallExp() instanceof UnaryExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = unaryexpressionx_" + unaryexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
    if (implicitcallstatementx_.getcallExp() instanceof BasicExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = basicexpressionx_" + basicexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
    if (implicitcallstatementx_.getcallExp() instanceof BinaryExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = binaryexpressionx_" + binaryexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
    if (implicitcallstatementx_.getcallExp() instanceof ConditionalExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = conditionalexpressionx_" + conditionalexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
    if (implicitcallstatementx_.getcallExp() instanceof CollectionExpression)
    { out.println("implicitcallstatementx_" + _i + ".callExp = collectionexpressionx_" + collectionexpressions.indexOf(implicitcallstatementx_.getcallExp())); } 
  }
  for (int _i = 0; _i < boundedloopstatements.size(); _i++)
  { BoundedLoopStatement boundedloopstatementx_ = (BoundedLoopStatement) boundedloopstatements.get(_i);
    if (boundedloopstatementx_.getloopRange() instanceof UnaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = unaryexpressionx_" + unaryexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopRange() instanceof BasicExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = basicexpressionx_" + basicexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopRange() instanceof BinaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = binaryexpressionx_" + binaryexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopRange() instanceof ConditionalExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = conditionalexpressionx_" + conditionalexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopRange() instanceof CollectionExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopRange = collectionexpressionx_" + collectionexpressions.indexOf(boundedloopstatementx_.getloopRange())); } 
    if (boundedloopstatementx_.getloopVar() instanceof UnaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = unaryexpressionx_" + unaryexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.getloopVar() instanceof BasicExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = basicexpressionx_" + basicexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.getloopVar() instanceof BinaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = binaryexpressionx_" + binaryexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.getloopVar() instanceof ConditionalExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = conditionalexpressionx_" + conditionalexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.getloopVar() instanceof CollectionExpression)
    { out.println("boundedloopstatementx_" + _i + ".loopVar = collectionexpressionx_" + collectionexpressions.indexOf(boundedloopstatementx_.getloopVar())); } 
    if (boundedloopstatementx_.gettest() instanceof UnaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = unaryexpressionx_" + unaryexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.gettest() instanceof BasicExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = basicexpressionx_" + basicexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.gettest() instanceof BinaryExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = binaryexpressionx_" + binaryexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.gettest() instanceof ConditionalExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = conditionalexpressionx_" + conditionalexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.gettest() instanceof CollectionExpression)
    { out.println("boundedloopstatementx_" + _i + ".test = collectionexpressionx_" + collectionexpressions.indexOf(boundedloopstatementx_.gettest())); } 
    if (boundedloopstatementx_.getbody() instanceof ReturnStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = returnstatementx_" + returnstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof AssertStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = assertstatementx_" + assertstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof ErrorStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = errorstatementx_" + errorstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof CatchStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = catchstatementx_" + catchstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof FinalStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = finalstatementx_" + finalstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof BreakStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = breakstatementx_" + breakstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof OperationCallStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = operationcallstatementx_" + operationcallstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof ImplicitCallStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = implicitcallstatementx_" + implicitcallstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof SequenceStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = sequencestatementx_" + sequencestatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof TryStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = trystatementx_" + trystatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof ConditionalStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = conditionalstatementx_" + conditionalstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof AssignStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = assignstatementx_" + assignstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof CreationStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = creationstatementx_" + creationstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof BoundedLoopStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = boundedloopstatementx_" + boundedloopstatements.indexOf(boundedloopstatementx_.getbody())); } 
    if (boundedloopstatementx_.getbody() instanceof UnboundedLoopStatement)
    { out.println("boundedloopstatementx_" + _i + ".body = unboundedloopstatementx_" + unboundedloopstatements.indexOf(boundedloopstatementx_.getbody())); } 
  }
  for (int _i = 0; _i < unboundedloopstatements.size(); _i++)
  { UnboundedLoopStatement unboundedloopstatementx_ = (UnboundedLoopStatement) unboundedloopstatements.get(_i);
    if (unboundedloopstatementx_.gettest() instanceof UnaryExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = unaryexpressionx_" + unaryexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.gettest() instanceof BasicExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = basicexpressionx_" + basicexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.gettest() instanceof BinaryExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = binaryexpressionx_" + binaryexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.gettest() instanceof ConditionalExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = conditionalexpressionx_" + conditionalexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.gettest() instanceof CollectionExpression)
    { out.println("unboundedloopstatementx_" + _i + ".test = collectionexpressionx_" + collectionexpressions.indexOf(unboundedloopstatementx_.gettest())); } 
    if (unboundedloopstatementx_.getbody() instanceof ReturnStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = returnstatementx_" + returnstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof AssertStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = assertstatementx_" + assertstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof ErrorStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = errorstatementx_" + errorstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof CatchStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = catchstatementx_" + catchstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof FinalStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = finalstatementx_" + finalstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof BreakStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = breakstatementx_" + breakstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof OperationCallStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = operationcallstatementx_" + operationcallstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof ImplicitCallStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = implicitcallstatementx_" + implicitcallstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof SequenceStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = sequencestatementx_" + sequencestatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof TryStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = trystatementx_" + trystatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof ConditionalStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = conditionalstatementx_" + conditionalstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof AssignStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = assignstatementx_" + assignstatements.indexOf(unboundedloopstatementx_.getbody())); } 
    if (unboundedloopstatementx_.getbody() instanceof CreationStatement)
    { out.println("unboundedloopstatementx_" + _i + ".body = creationstatementx_" + creationstatements.indexOf(unboundedloopstatementx_.getbody())); } 
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
 if (assignstatement_type_Type.get(_k) instanceof Enumeration)
      { out.println("enumerationx_" + enumerations.indexOf(assignstatement_type_Type.get(_k)) + " : assignstatementx_" + _i + ".type"); }
 if (assignstatement_type_Type.get(_k) instanceof PrimitiveType)
      { out.println("primitivetypex_" + primitivetypes.indexOf(assignstatement_type_Type.get(_k)) + " : assignstatementx_" + _i + ".type"); }
 if (assignstatement_type_Type.get(_k) instanceof CollectionType)
      { out.println("collectiontypex_" + collectiontypes.indexOf(assignstatement_type_Type.get(_k)) + " : assignstatementx_" + _i + ".type"); }
  }
    if (assignstatementx_.getleft() instanceof UnaryExpression)
    { out.println("assignstatementx_" + _i + ".left = unaryexpressionx_" + unaryexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getleft() instanceof BasicExpression)
    { out.println("assignstatementx_" + _i + ".left = basicexpressionx_" + basicexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getleft() instanceof BinaryExpression)
    { out.println("assignstatementx_" + _i + ".left = binaryexpressionx_" + binaryexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getleft() instanceof ConditionalExpression)
    { out.println("assignstatementx_" + _i + ".left = conditionalexpressionx_" + conditionalexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getleft() instanceof CollectionExpression)
    { out.println("assignstatementx_" + _i + ".left = collectionexpressionx_" + collectionexpressions.indexOf(assignstatementx_.getleft())); } 
    if (assignstatementx_.getright() instanceof UnaryExpression)
    { out.println("assignstatementx_" + _i + ".right = unaryexpressionx_" + unaryexpressions.indexOf(assignstatementx_.getright())); } 
    if (assignstatementx_.getright() instanceof BasicExpression)
    { out.println("assignstatementx_" + _i + ".right = basicexpressionx_" + basicexpressions.indexOf(assignstatementx_.getright())); } 
    if (assignstatementx_.getright() instanceof BinaryExpression)
    { out.println("assignstatementx_" + _i + ".right = binaryexpressionx_" + binaryexpressions.indexOf(assignstatementx_.getright())); } 
    if (assignstatementx_.getright() instanceof ConditionalExpression)
    { out.println("assignstatementx_" + _i + ".right = conditionalexpressionx_" + conditionalexpressions.indexOf(assignstatementx_.getright())); } 
    if (assignstatementx_.getright() instanceof CollectionExpression)
    { out.println("assignstatementx_" + _i + ".right = collectionexpressionx_" + collectionexpressions.indexOf(assignstatementx_.getright())); } 
  }
  for (int _i = 0; _i < sequencestatements.size(); _i++)
  { SequenceStatement sequencestatementx_ = (SequenceStatement) sequencestatements.get(_i);
    List sequencestatement_statements_Statement = sequencestatementx_.getstatements();
    for (int _k = 0; _k < sequencestatement_statements_Statement.size(); _k++)
    { if (sequencestatement_statements_Statement.get(_k) instanceof ReturnStatement)
      { out.println("returnstatementx_" + returnstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof AssertStatement)
      { out.println("assertstatementx_" + assertstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof ErrorStatement)
      { out.println("errorstatementx_" + errorstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof CatchStatement)
      { out.println("catchstatementx_" + catchstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof FinalStatement)
      { out.println("finalstatementx_" + finalstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof BreakStatement)
      { out.println("breakstatementx_" + breakstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof OperationCallStatement)
      { out.println("operationcallstatementx_" + operationcallstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof ImplicitCallStatement)
      { out.println("implicitcallstatementx_" + implicitcallstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof SequenceStatement)
      { out.println("sequencestatementx_" + sequencestatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof TryStatement)
      { out.println("trystatementx_" + trystatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof ConditionalStatement)
      { out.println("conditionalstatementx_" + conditionalstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof AssignStatement)
      { out.println("assignstatementx_" + assignstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof CreationStatement)
      { out.println("creationstatementx_" + creationstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof BoundedLoopStatement)
      { out.println("boundedloopstatementx_" + boundedloopstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
 if (sequencestatement_statements_Statement.get(_k) instanceof UnboundedLoopStatement)
      { out.println("unboundedloopstatementx_" + unboundedloopstatements.indexOf(sequencestatement_statements_Statement.get(_k)) + " : sequencestatementx_" + _i + ".statements"); }
  }
  }
  for (int _i = 0; _i < trystatements.size(); _i++)
  { TryStatement trystatementx_ = (TryStatement) trystatements.get(_i);
    List trystatement_catchClauses_Statement = trystatementx_.getcatchClauses();
    for (int _k = 0; _k < trystatement_catchClauses_Statement.size(); _k++)
    { if (trystatement_catchClauses_Statement.get(_k) instanceof ReturnStatement)
      { out.println("returnstatementx_" + returnstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof AssertStatement)
      { out.println("assertstatementx_" + assertstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof ErrorStatement)
      { out.println("errorstatementx_" + errorstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof CatchStatement)
      { out.println("catchstatementx_" + catchstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof FinalStatement)
      { out.println("finalstatementx_" + finalstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof BreakStatement)
      { out.println("breakstatementx_" + breakstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof OperationCallStatement)
      { out.println("operationcallstatementx_" + operationcallstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof ImplicitCallStatement)
      { out.println("implicitcallstatementx_" + implicitcallstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof SequenceStatement)
      { out.println("sequencestatementx_" + sequencestatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof TryStatement)
      { out.println("trystatementx_" + trystatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof ConditionalStatement)
      { out.println("conditionalstatementx_" + conditionalstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof AssignStatement)
      { out.println("assignstatementx_" + assignstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof CreationStatement)
      { out.println("creationstatementx_" + creationstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof BoundedLoopStatement)
      { out.println("boundedloopstatementx_" + boundedloopstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
 if (trystatement_catchClauses_Statement.get(_k) instanceof UnboundedLoopStatement)
      { out.println("unboundedloopstatementx_" + unboundedloopstatements.indexOf(trystatement_catchClauses_Statement.get(_k)) + " : trystatementx_" + _i + ".catchClauses"); }
  }
    if (trystatementx_.getbody() instanceof ReturnStatement)
    { out.println("trystatementx_" + _i + ".body = returnstatementx_" + returnstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof AssertStatement)
    { out.println("trystatementx_" + _i + ".body = assertstatementx_" + assertstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof ErrorStatement)
    { out.println("trystatementx_" + _i + ".body = errorstatementx_" + errorstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof CatchStatement)
    { out.println("trystatementx_" + _i + ".body = catchstatementx_" + catchstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof FinalStatement)
    { out.println("trystatementx_" + _i + ".body = finalstatementx_" + finalstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof BreakStatement)
    { out.println("trystatementx_" + _i + ".body = breakstatementx_" + breakstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof OperationCallStatement)
    { out.println("trystatementx_" + _i + ".body = operationcallstatementx_" + operationcallstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof ImplicitCallStatement)
    { out.println("trystatementx_" + _i + ".body = implicitcallstatementx_" + implicitcallstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof SequenceStatement)
    { out.println("trystatementx_" + _i + ".body = sequencestatementx_" + sequencestatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof TryStatement)
    { out.println("trystatementx_" + _i + ".body = trystatementx_" + trystatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof ConditionalStatement)
    { out.println("trystatementx_" + _i + ".body = conditionalstatementx_" + conditionalstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof AssignStatement)
    { out.println("trystatementx_" + _i + ".body = assignstatementx_" + assignstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof CreationStatement)
    { out.println("trystatementx_" + _i + ".body = creationstatementx_" + creationstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof BoundedLoopStatement)
    { out.println("trystatementx_" + _i + ".body = boundedloopstatementx_" + boundedloopstatements.indexOf(trystatementx_.getbody())); } 
    if (trystatementx_.getbody() instanceof UnboundedLoopStatement)
    { out.println("trystatementx_" + _i + ".body = unboundedloopstatementx_" + unboundedloopstatements.indexOf(trystatementx_.getbody())); } 
    List trystatement_endStatement_Statement = trystatementx_.getendStatement();
    for (int _k = 0; _k < trystatement_endStatement_Statement.size(); _k++)
    { if (trystatement_endStatement_Statement.get(_k) instanceof ReturnStatement)
      { out.println("returnstatementx_" + returnstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof AssertStatement)
      { out.println("assertstatementx_" + assertstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof ErrorStatement)
      { out.println("errorstatementx_" + errorstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof CatchStatement)
      { out.println("catchstatementx_" + catchstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof FinalStatement)
      { out.println("finalstatementx_" + finalstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof BreakStatement)
      { out.println("breakstatementx_" + breakstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof OperationCallStatement)
      { out.println("operationcallstatementx_" + operationcallstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof ImplicitCallStatement)
      { out.println("implicitcallstatementx_" + implicitcallstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof SequenceStatement)
      { out.println("sequencestatementx_" + sequencestatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof TryStatement)
      { out.println("trystatementx_" + trystatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof ConditionalStatement)
      { out.println("conditionalstatementx_" + conditionalstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof AssignStatement)
      { out.println("assignstatementx_" + assignstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof CreationStatement)
      { out.println("creationstatementx_" + creationstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof BoundedLoopStatement)
      { out.println("boundedloopstatementx_" + boundedloopstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
 if (trystatement_endStatement_Statement.get(_k) instanceof UnboundedLoopStatement)
      { out.println("unboundedloopstatementx_" + unboundedloopstatements.indexOf(trystatement_endStatement_Statement.get(_k)) + " : trystatementx_" + _i + ".endStatement"); }
  }
  }
  for (int _i = 0; _i < conditionalstatements.size(); _i++)
  { ConditionalStatement conditionalstatementx_ = (ConditionalStatement) conditionalstatements.get(_i);
    if (conditionalstatementx_.gettest() instanceof UnaryExpression)
    { out.println("conditionalstatementx_" + _i + ".test = unaryexpressionx_" + unaryexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.gettest() instanceof BasicExpression)
    { out.println("conditionalstatementx_" + _i + ".test = basicexpressionx_" + basicexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.gettest() instanceof BinaryExpression)
    { out.println("conditionalstatementx_" + _i + ".test = binaryexpressionx_" + binaryexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.gettest() instanceof ConditionalExpression)
    { out.println("conditionalstatementx_" + _i + ".test = conditionalexpressionx_" + conditionalexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.gettest() instanceof CollectionExpression)
    { out.println("conditionalstatementx_" + _i + ".test = collectionexpressionx_" + collectionexpressions.indexOf(conditionalstatementx_.gettest())); } 
    if (conditionalstatementx_.getifPart() instanceof ReturnStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = returnstatementx_" + returnstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof AssertStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = assertstatementx_" + assertstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof ErrorStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = errorstatementx_" + errorstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof CatchStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = catchstatementx_" + catchstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof FinalStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = finalstatementx_" + finalstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof BreakStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = breakstatementx_" + breakstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof OperationCallStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = operationcallstatementx_" + operationcallstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof ImplicitCallStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = implicitcallstatementx_" + implicitcallstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof SequenceStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = sequencestatementx_" + sequencestatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof TryStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = trystatementx_" + trystatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof ConditionalStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = conditionalstatementx_" + conditionalstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof AssignStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = assignstatementx_" + assignstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof CreationStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = creationstatementx_" + creationstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof BoundedLoopStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = boundedloopstatementx_" + boundedloopstatements.indexOf(conditionalstatementx_.getifPart())); } 
    if (conditionalstatementx_.getifPart() instanceof UnboundedLoopStatement)
    { out.println("conditionalstatementx_" + _i + ".ifPart = unboundedloopstatementx_" + unboundedloopstatements.indexOf(conditionalstatementx_.getifPart())); } 
    List conditionalstatement_elsePart_Statement = conditionalstatementx_.getelsePart();
    for (int _k = 0; _k < conditionalstatement_elsePart_Statement.size(); _k++)
    { if (conditionalstatement_elsePart_Statement.get(_k) instanceof ReturnStatement)
      { out.println("returnstatementx_" + returnstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof AssertStatement)
      { out.println("assertstatementx_" + assertstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof ErrorStatement)
      { out.println("errorstatementx_" + errorstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof CatchStatement)
      { out.println("catchstatementx_" + catchstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof FinalStatement)
      { out.println("finalstatementx_" + finalstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof BreakStatement)
      { out.println("breakstatementx_" + breakstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof OperationCallStatement)
      { out.println("operationcallstatementx_" + operationcallstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof ImplicitCallStatement)
      { out.println("implicitcallstatementx_" + implicitcallstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof SequenceStatement)
      { out.println("sequencestatementx_" + sequencestatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof TryStatement)
      { out.println("trystatementx_" + trystatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof ConditionalStatement)
      { out.println("conditionalstatementx_" + conditionalstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof AssignStatement)
      { out.println("assignstatementx_" + assignstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
 if (conditionalstatement_elsePart_Statement.get(_k) instanceof CreationStatement)
      { out.println("creationstatementx_" + creationstatements.indexOf(conditionalstatement_elsePart_Statement.get(_k)) + " : conditionalstatementx_" + _i + ".elsePart"); }
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
    if (creationstatementx_.gettype() instanceof Enumeration)
    { out.println("creationstatementx_" + _i + ".type = enumerationx_" + enumerations.indexOf(creationstatementx_.gettype())); } 
    if (creationstatementx_.gettype() instanceof PrimitiveType)
    { out.println("creationstatementx_" + _i + ".type = primitivetypex_" + primitivetypes.indexOf(creationstatementx_.gettype())); } 
    if (creationstatementx_.gettype() instanceof CollectionType)
    { out.println("creationstatementx_" + _i + ".type = collectiontypex_" + collectiontypes.indexOf(creationstatementx_.gettype())); } 
    if (creationstatementx_.getelementType() instanceof Entity)
    { out.println("creationstatementx_" + _i + ".elementType = entityx_" + entitys.indexOf(creationstatementx_.getelementType())); } 
    if (creationstatementx_.getelementType() instanceof Enumeration)
    { out.println("creationstatementx_" + _i + ".elementType = enumerationx_" + enumerations.indexOf(creationstatementx_.getelementType())); } 
    if (creationstatementx_.getelementType() instanceof PrimitiveType)
    { out.println("creationstatementx_" + _i + ".elementType = primitivetypex_" + primitivetypes.indexOf(creationstatementx_.getelementType())); } 
    if (creationstatementx_.getelementType() instanceof CollectionType)
    { out.println("creationstatementx_" + _i + ".elementType = collectiontypex_" + collectiontypes.indexOf(creationstatementx_.getelementType())); } 
    List creationstatement_initialExpression_Expression = creationstatementx_.getinitialExpression();
    for (int _k = 0; _k < creationstatement_initialExpression_Expression.size(); _k++)
    { if (creationstatement_initialExpression_Expression.get(_k) instanceof UnaryExpression)
      { out.println("unaryexpressionx_" + unaryexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
 if (creationstatement_initialExpression_Expression.get(_k) instanceof BasicExpression)
      { out.println("basicexpressionx_" + basicexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
 if (creationstatement_initialExpression_Expression.get(_k) instanceof BinaryExpression)
      { out.println("binaryexpressionx_" + binaryexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
 if (creationstatement_initialExpression_Expression.get(_k) instanceof ConditionalExpression)
      { out.println("conditionalexpressionx_" + conditionalexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
 if (creationstatement_initialExpression_Expression.get(_k) instanceof CollectionExpression)
      { out.println("collectionexpressionx_" + collectionexpressions.indexOf(creationstatement_initialExpression_Expression.get(_k)) + " : creationstatementx_" + _i + ".initialExpression"); }
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
    List res = convertXsiToVector(xmlstring);
    File outfile = new File("_in.txt");
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
    for (int i = 0; i < res.size(); i++)
    { 
      out.println(res.get(i));
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
    eallatts.add("isSorted");
    eallatts.add("name");
    allattsmap.put("Type", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("Type", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Type", eallatts);
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
    instancemap.put("classifiers", new Vector()); 
    instancemap.put("classifier",new Vector()); 
    entcodes.add("classifiers");
    entcodes.add("classifier");
    entmap.put("classifiers","Classifier");
    entmap.put("classifier","Classifier");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("isSorted");
    eallatts.add("name");
    allattsmap.put("Classifier", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("Classifier", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Classifier", eallatts);
    instancemap.put("datatypes", new Vector()); 
    instancemap.put("datatype",new Vector()); 
    entcodes.add("datatypes");
    entcodes.add("datatype");
    entmap.put("datatypes","DataType");
    entmap.put("datatype","DataType");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("isSorted");
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
    eallatts.add("isSorted");
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
    eallatts.add("isSorted");
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
    eallatts.add("stereotypes");
    eallatts.add("typeId");
    eallatts.add("isSorted");
    eallatts.add("name");
    allattsmap.put("Entity", eallatts);
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("name");
    stringattsmap.put("Entity", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Entity", eallatts);
    instancemap.put("collectiontypes", new Vector()); 
    instancemap.put("collectiontype",new Vector()); 
    entcodes.add("collectiontypes");
    entcodes.add("collectiontype");
    entmap.put("collectiontypes","CollectionType");
    entmap.put("collectiontype","CollectionType");
    eallatts = new Vector();
    eallatts.add("typeId");
    eallatts.add("isSorted");
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
    eallatts.add("accumulator");
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
    eallatts.add("isSorted");
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
    instancemap.put("propertys", new Vector()); 
    instancemap.put("property",new Vector()); 
    entcodes.add("propertys");
    entcodes.add("property");
    entmap.put("propertys","Property");
    entmap.put("property","Property");
    eallatts = new Vector();
    eallatts.add("name");
    eallatts.add("lower");
    eallatts.add("upper");
    eallatts.add("isOrdered");
    eallatts.add("isUnique");
    eallatts.add("isDerived");
    eallatts.add("isReadOnly");
    eallatts.add("isStatic");
    allattsmap.put("Property", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("Property", eallatts);
    eallatts = new Vector();
    eallatts.add("type");
    eallatts.add("initialValue");
    eallatts.add("owner");
    onerolesmap.put("Property", eallatts);
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
    instancemap.put("operations", new Vector()); 
    instancemap.put("operation",new Vector()); 
    entcodes.add("operations");
    entcodes.add("operation");
    entmap.put("operations","Operation");
    entmap.put("operation","Operation");
    eallatts = new Vector();
    eallatts.add("isQuery");
    eallatts.add("isAbstract");
    eallatts.add("isCached");
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
    instancemap.put("usecases", new Vector()); 
    instancemap.put("usecase",new Vector()); 
    entcodes.add("usecases");
    entcodes.add("usecase");
    entmap.put("usecases","UseCase");
    entmap.put("usecase","UseCase");
    eallatts = new Vector();
    eallatts.add("name");
    allattsmap.put("UseCase", eallatts);
    eallatts = new Vector();
    eallatts.add("name");
    stringattsmap.put("UseCase", eallatts);
    eallatts = new Vector();
    eallatts.add("resultType");
    eallatts.add("classifierBehaviour");
    onerolesmap.put("UseCase", eallatts);
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
    instancemap.put("continuestatements", new Vector()); 
    instancemap.put("continuestatement",new Vector()); 
    entcodes.add("continuestatements");
    entcodes.add("continuestatement");
    entmap.put("continuestatements","ContinueStatement");
    entmap.put("continuestatement","ContinueStatement");
    eallatts = new Vector();
    allattsmap.put("ContinueStatement", eallatts);
    eallatts = new Vector();
    stringattsmap.put("ContinueStatement", eallatts);
    eallatts = new Vector();
    onerolesmap.put("ContinueStatement", eallatts);
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
    instancemap.put("behaviouralfeatures", new Vector()); 
    instancemap.put("behaviouralfeature",new Vector()); 
    entcodes.add("behaviouralfeatures");
    entcodes.add("behaviouralfeature");
    entmap.put("behaviouralfeatures","BehaviouralFeature");
    entmap.put("behaviouralfeature","BehaviouralFeature");
    eallatts = new Vector();
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

    for (int _i = 0; _i < enumerations.size(); _i++)
    { Enumeration enumerationx_ = (Enumeration) enumerations.get(_i);
       out.print("<enumerations xsi:type=\"My:Enumeration\"");
    out.print(" typeId=\"" + enumerationx_.gettypeId() + "\" ");
    out.print(" isSorted=\"" + enumerationx_.getisSorted() + "\" ");
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
    out.print(" isSorted=\"" + primitivetypex_.getisSorted() + "\" ");
    out.print(" name=\"" + primitivetypex_.getname() + "\" ");
    out.println(" />");
  }

    for (int _i = 0; _i < entitys.size(); _i++)
    { Entity entityx_ = (Entity) entitys.get(_i);
       out.print("<entitys xsi:type=\"My:Entity\"");
    out.print(" isAbstract=\"" + entityx_.getisAbstract() + "\" ");
    out.print(" isInterface=\"" + entityx_.getisInterface() + "\" ");
    out.print(" typeId=\"" + entityx_.gettypeId() + "\" ");
    out.print(" isSorted=\"" + entityx_.getisSorted() + "\" ");
    out.print(" name=\"" + entityx_.getname() + "\" ");
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
    out.println(" />");
  }

    for (int _i = 0; _i < collectiontypes.size(); _i++)
    { CollectionType collectiontypex_ = (CollectionType) collectiontypes.get(_i);
       out.print("<collectiontypes xsi:type=\"My:CollectionType\"");
    out.print(" typeId=\"" + collectiontypex_.gettypeId() + "\" ");
    out.print(" isSorted=\"" + collectiontypex_.getisSorted() + "\" ");
    out.print(" name=\"" + collectiontypex_.getname() + "\" ");
    if (collectiontypex_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectiontypex_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectiontypex_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectiontypex_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CollectionType) collectiontypes.get(_i)).getelementType()));
    out.print("\""); }
    if (collectiontypex_.getkeyType() instanceof Entity)
    {   out.print(" keyType=\"");
    out.print("//@entitys." + entitys.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
 else    if (collectiontypex_.getkeyType() instanceof Enumeration)
    {   out.print(" keyType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
 else    if (collectiontypex_.getkeyType() instanceof PrimitiveType)
    {   out.print(" keyType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
 else    if (collectiontypex_.getkeyType() instanceof CollectionType)
    {   out.print(" keyType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CollectionType) collectiontypes.get(_i)).getkeyType()));
    out.print("\""); }
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
 else    if (binaryexpressionx_.getleft() instanceof BasicExpression)
    {   out.print(" left=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
 else    if (binaryexpressionx_.getleft() instanceof BinaryExpression)
    {   out.print(" left=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
 else    if (binaryexpressionx_.getleft() instanceof ConditionalExpression)
    {   out.print(" left=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
 else    if (binaryexpressionx_.getleft() instanceof CollectionExpression)
    {   out.print(" left=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getleft()));
    out.print("\""); }
    if (binaryexpressionx_.getright() instanceof UnaryExpression)
    {   out.print(" right=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
 else    if (binaryexpressionx_.getright() instanceof BasicExpression)
    {   out.print(" right=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
 else    if (binaryexpressionx_.getright() instanceof BinaryExpression)
    {   out.print(" right=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
 else    if (binaryexpressionx_.getright() instanceof ConditionalExpression)
    {   out.print(" right=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
 else    if (binaryexpressionx_.getright() instanceof CollectionExpression)
    {   out.print(" right=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getright()));
    out.print("\""); }
    out.print(" accumulator=\"");
    out.print("//@propertys." + propertys.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getaccumulator()));
    out.print("\"");
    if (binaryexpressionx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (binaryexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (binaryexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (binaryexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((BinaryExpression) binaryexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (binaryexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (binaryexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (binaryexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((BinaryExpression) binaryexpressions.get(_i)).getelementType()));
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
 else    if (conditionalexpressionx_.gettest() instanceof BasicExpression)
    {   out.print(" test=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettest() instanceof BinaryExpression)
    {   out.print(" test=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettest() instanceof ConditionalExpression)
    {   out.print(" test=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettest() instanceof CollectionExpression)
    {   out.print(" test=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettest()));
    out.print("\""); }
    if (conditionalexpressionx_.getifExp() instanceof UnaryExpression)
    {   out.print(" ifExp=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getifExp() instanceof BasicExpression)
    {   out.print(" ifExp=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getifExp() instanceof BinaryExpression)
    {   out.print(" ifExp=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getifExp() instanceof ConditionalExpression)
    {   out.print(" ifExp=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getifExp() instanceof CollectionExpression)
    {   out.print(" ifExp=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getifExp()));
    out.print("\""); }
    if (conditionalexpressionx_.getelseExp() instanceof UnaryExpression)
    {   out.print(" elseExp=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelseExp() instanceof BasicExpression)
    {   out.print(" elseExp=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelseExp() instanceof BinaryExpression)
    {   out.print(" elseExp=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelseExp() instanceof ConditionalExpression)
    {   out.print(" elseExp=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelseExp() instanceof CollectionExpression)
    {   out.print(" elseExp=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelseExp()));
    out.print("\""); }
    if (conditionalexpressionx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (conditionalexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (conditionalexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (conditionalexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((ConditionalExpression) conditionalexpressions.get(_i)).getelementType()));
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
 else    if (unaryexpressionx_.getargument() instanceof BasicExpression)
    {   out.print(" argument=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
 else    if (unaryexpressionx_.getargument() instanceof BinaryExpression)
    {   out.print(" argument=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
 else    if (unaryexpressionx_.getargument() instanceof ConditionalExpression)
    {   out.print(" argument=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
 else    if (unaryexpressionx_.getargument() instanceof CollectionExpression)
    {   out.print(" argument=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getargument()));
    out.print("\""); }
    if (unaryexpressionx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (unaryexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (unaryexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (unaryexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((UnaryExpression) unaryexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (unaryexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (unaryexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (unaryexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((UnaryExpression) unaryexpressions.get(_i)).getelementType()));
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
    out.print(" isSorted=\"" + collectionexpressionx_.getisSorted() + "\" ");
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
 else      if (collectionexpression_elements.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(collectionexpression_elements.get(_k)));
    }
 else      if (collectionexpression_elements.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(collectionexpression_elements.get(_k)));
    }
 else      if (collectionexpression_elements.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(collectionexpression_elements.get(_k)));
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
 else    if (collectionexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CollectionExpression) collectionexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (collectionexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CollectionExpression) collectionexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (collectionexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CollectionExpression) collectionexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (collectionexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((CollectionExpression) collectionexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectionexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CollectionExpression) collectionexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (collectionexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CollectionExpression) collectionexpressions.get(_i)).getelementType()));
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
 else      if (basicexpression_parameters.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(basicexpression_parameters.get(_k)));
    }
 else      if (basicexpression_parameters.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(basicexpression_parameters.get(_k)));
    }
 else      if (basicexpression_parameters.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(basicexpression_parameters.get(_k)));
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
 else      if (basicexpression_arrayIndex.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(basicexpression_arrayIndex.get(_k)));
    }
 else      if (basicexpression_arrayIndex.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(basicexpression_arrayIndex.get(_k)));
    }
 else      if (basicexpression_arrayIndex.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(basicexpression_arrayIndex.get(_k)));
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
 else      if (basicexpression_objectRef.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(basicexpression_objectRef.get(_k)));
    }
 else      if (basicexpression_objectRef.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(basicexpression_objectRef.get(_k)));
    }
 else      if (basicexpression_objectRef.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(basicexpression_objectRef.get(_k)));
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
 else    if (basicexpressionx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((BasicExpression) basicexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (basicexpressionx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((BasicExpression) basicexpressions.get(_i)).gettype()));
    out.print("\""); }
 else    if (basicexpressionx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((BasicExpression) basicexpressions.get(_i)).gettype()));
    out.print("\""); }
    if (basicexpressionx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (basicexpressionx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (basicexpressionx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
 else    if (basicexpressionx_.getelementType() instanceof CollectionType)
    {   out.print(" elementType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((BasicExpression) basicexpressions.get(_i)).getelementType()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < propertys.size(); _i++)
    { Property propertyx_ = (Property) propertys.get(_i);
       out.print("<propertys xsi:type=\"My:Property\"");
    out.print(" name=\"" + propertyx_.getname() + "\" ");
    out.print(" lower=\"" + propertyx_.getlower() + "\" ");
    out.print(" upper=\"" + propertyx_.getupper() + "\" ");
    out.print(" isOrdered=\"" + propertyx_.getisOrdered() + "\" ");
    out.print(" isUnique=\"" + propertyx_.getisUnique() + "\" ");
    out.print(" isDerived=\"" + propertyx_.getisDerived() + "\" ");
    out.print(" isReadOnly=\"" + propertyx_.getisReadOnly() + "\" ");
    out.print(" isStatic=\"" + propertyx_.getisStatic() + "\" ");
    out.print(" qualifier = \"");
    List property_qualifier = propertyx_.getqualifier();
    for (int _j = 0; _j < property_qualifier.size(); _j++)
    { out.print(" //@propertys." + propertys.indexOf(property_qualifier.get(_j)));
    }
    out.print("\"");
    if (propertyx_.gettype() instanceof Entity)
    {   out.print(" type=\"");
    out.print("//@entitys." + entitys.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
 else    if (propertyx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
 else    if (propertyx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
 else    if (propertyx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((Property) propertys.get(_i)).gettype()));
    out.print("\""); }
    if (propertyx_.getinitialValue() instanceof UnaryExpression)
    {   out.print(" initialValue=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
 else    if (propertyx_.getinitialValue() instanceof BasicExpression)
    {   out.print(" initialValue=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
 else    if (propertyx_.getinitialValue() instanceof BinaryExpression)
    {   out.print(" initialValue=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
 else    if (propertyx_.getinitialValue() instanceof ConditionalExpression)
    {   out.print(" initialValue=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
 else    if (propertyx_.getinitialValue() instanceof CollectionExpression)
    {   out.print(" initialValue=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((Property) propertys.get(_i)).getinitialValue()));
    out.print("\""); }
    out.print(" owner=\"");
    out.print("//@entitys." + entitys.indexOf(((Property) propertys.get(_i)).getowner()));
    out.print("\"");
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
 else      if (returnstatement_returnValue.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(returnstatement_returnValue.get(_k)));
    }
 else      if (returnstatement_returnValue.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(returnstatement_returnValue.get(_k)));
    }
 else      if (returnstatement_returnValue.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(returnstatement_returnValue.get(_k)));
    }
 else      if (returnstatement_returnValue.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(returnstatement_returnValue.get(_k)));
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
 else    if (assertstatementx_.getcondition() instanceof BasicExpression)
    {   out.print(" condition=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((AssertStatement) assertstatements.get(_i)).getcondition()));
    out.print("\""); }
 else    if (assertstatementx_.getcondition() instanceof BinaryExpression)
    {   out.print(" condition=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((AssertStatement) assertstatements.get(_i)).getcondition()));
    out.print("\""); }
 else    if (assertstatementx_.getcondition() instanceof ConditionalExpression)
    {   out.print(" condition=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((AssertStatement) assertstatements.get(_i)).getcondition()));
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
 else      if (assertstatement_message.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(assertstatement_message.get(_k)));
    }
 else      if (assertstatement_message.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(assertstatement_message.get(_k)));
    }
 else      if (assertstatement_message.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(assertstatement_message.get(_k)));
    }
 else      if (assertstatement_message.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(assertstatement_message.get(_k)));
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
 else    if (errorstatementx_.getthrownObject() instanceof BasicExpression)
    {   out.print(" thrownObject=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ErrorStatement) errorstatements.get(_i)).getthrownObject()));
    out.print("\""); }
 else    if (errorstatementx_.getthrownObject() instanceof BinaryExpression)
    {   out.print(" thrownObject=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ErrorStatement) errorstatements.get(_i)).getthrownObject()));
    out.print("\""); }
 else    if (errorstatementx_.getthrownObject() instanceof ConditionalExpression)
    {   out.print(" thrownObject=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ErrorStatement) errorstatements.get(_i)).getthrownObject()));
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
 else    if (catchstatementx_.getcaughtObject() instanceof BasicExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
 else    if (catchstatementx_.getcaughtObject() instanceof BinaryExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
 else    if (catchstatementx_.getcaughtObject() instanceof ConditionalExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
 else    if (catchstatementx_.getcaughtObject() instanceof CollectionExpression)
    {   out.print(" caughtObject=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((CatchStatement) catchstatements.get(_i)).getcaughtObject()));
    out.print("\""); }
    if (catchstatementx_.getaction() instanceof ReturnStatement)
    {   out.print(" action=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof AssertStatement)
    {   out.print(" action=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof ErrorStatement)
    {   out.print(" action=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof CatchStatement)
    {   out.print(" action=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
    out.print("\""); }
 else    if (catchstatementx_.getaction() instanceof FinalStatement)
    {   out.print(" action=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
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
 else    if (catchstatementx_.getaction() instanceof TryStatement)
    {   out.print(" action=\"");
    out.print("//@trystatements." + trystatements.indexOf(((CatchStatement) catchstatements.get(_i)).getaction()));
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
 else    if (finalstatementx_.getbody() instanceof AssertStatement)
    {   out.print(" body=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof ErrorStatement)
    {   out.print(" body=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof CatchStatement)
    {   out.print(" body=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (finalstatementx_.getbody() instanceof FinalStatement)
    {   out.print(" body=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
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
 else    if (finalstatementx_.getbody() instanceof TryStatement)
    {   out.print(" body=\"");
    out.print("//@trystatements." + trystatements.indexOf(((FinalStatement) finalstatements.get(_i)).getbody()));
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

    for (int _i = 0; _i < operations.size(); _i++)
    { Operation operationx_ = (Operation) operations.get(_i);
       out.print("<operations xsi:type=\"My:Operation\"");
    out.print(" isQuery=\"" + operationx_.getisQuery() + "\" ");
    out.print(" isAbstract=\"" + operationx_.getisAbstract() + "\" ");
    out.print(" isCached=\"" + operationx_.getisCached() + "\" ");
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
 else    if (operationx_.getactivity() instanceof AssertStatement)
    {   out.print(" activity=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof ErrorStatement)
    {   out.print(" activity=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof CatchStatement)
    {   out.print(" activity=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
    out.print("\""); }
 else    if (operationx_.getactivity() instanceof FinalStatement)
    {   out.print(" activity=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((Operation) operations.get(_i)).getactivity()));
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
 else    if (operationx_.getactivity() instanceof TryStatement)
    {   out.print(" activity=\"");
    out.print("//@trystatements." + trystatements.indexOf(((Operation) operations.get(_i)).getactivity()));
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
 else    if (operationx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((Operation) operations.get(_i)).gettype()));
    out.print("\""); }
 else    if (operationx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((Operation) operations.get(_i)).gettype()));
    out.print("\""); }
 else    if (operationx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((Operation) operations.get(_i)).gettype()));
    out.print("\""); }
    if (operationx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((Operation) operations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (operationx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((Operation) operations.get(_i)).getelementType()));
    out.print("\""); }
 else    if (operationx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((Operation) operations.get(_i)).getelementType()));
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
    out.print(" name=\"" + usecasex_.getname() + "\" ");
    out.print(" parameters = \"");
    List usecase_parameters = usecasex_.getparameters();
    for (int _j = 0; _j < usecase_parameters.size(); _j++)
    { out.print(" //@propertys." + propertys.indexOf(usecase_parameters.get(_j)));
    }
    out.print("\"");
    if (usecasex_.getresultType() instanceof Entity)
    {   out.print(" resultType=\"");
    out.print("//@entitys." + entitys.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
 else    if (usecasex_.getresultType() instanceof Enumeration)
    {   out.print(" resultType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
 else    if (usecasex_.getresultType() instanceof PrimitiveType)
    {   out.print(" resultType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
 else    if (usecasex_.getresultType() instanceof CollectionType)
    {   out.print(" resultType=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((UseCase) usecases.get(_i)).getresultType()));
    out.print("\""); }
    if (usecasex_.getclassifierBehaviour() instanceof ReturnStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof AssertStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof ErrorStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof CatchStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof FinalStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
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
 else    if (usecasex_.getclassifierBehaviour() instanceof TryStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@trystatements." + trystatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
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
 else    if (usecasex_.getclassifierBehaviour() instanceof BoundedLoopStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@boundedloopstatements." + boundedloopstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
 else    if (usecasex_.getclassifierBehaviour() instanceof UnboundedLoopStatement)
    {   out.print(" classifierBehaviour=\"");
    out.print("//@unboundedloopstatements." + unboundedloopstatements.indexOf(((UseCase) usecases.get(_i)).getclassifierBehaviour()));
    out.print("\""); }
    out.println(" />");
  }

    for (int _i = 0; _i < breakstatements.size(); _i++)
    { BreakStatement breakstatementx_ = (BreakStatement) breakstatements.get(_i);
       out.print("<breakstatements xsi:type=\"My:BreakStatement\"");
    out.print(" statId=\"" + breakstatementx_.getstatId() + "\" ");
    out.println(" />");
  }

    for (int _i = 0; _i < continuestatements.size(); _i++)
    { ContinueStatement continuestatementx_ = (ContinueStatement) continuestatements.get(_i);
       out.print("<continuestatements xsi:type=\"My:ContinueStatement\"");
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
 else    if (operationcallstatementx_.getcallExp() instanceof BasicExpression)
    {   out.print(" callExp=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((OperationCallStatement) operationcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (operationcallstatementx_.getcallExp() instanceof BinaryExpression)
    {   out.print(" callExp=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((OperationCallStatement) operationcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (operationcallstatementx_.getcallExp() instanceof ConditionalExpression)
    {   out.print(" callExp=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((OperationCallStatement) operationcallstatements.get(_i)).getcallExp()));
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
 else    if (implicitcallstatementx_.getcallExp() instanceof BasicExpression)
    {   out.print(" callExp=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ImplicitCallStatement) implicitcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (implicitcallstatementx_.getcallExp() instanceof BinaryExpression)
    {   out.print(" callExp=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ImplicitCallStatement) implicitcallstatements.get(_i)).getcallExp()));
    out.print("\""); }
 else    if (implicitcallstatementx_.getcallExp() instanceof ConditionalExpression)
    {   out.print(" callExp=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ImplicitCallStatement) implicitcallstatements.get(_i)).getcallExp()));
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
 else    if (boundedloopstatementx_.getloopRange() instanceof BasicExpression)
    {   out.print(" loopRange=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopRange() instanceof BinaryExpression)
    {   out.print(" loopRange=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopRange() instanceof ConditionalExpression)
    {   out.print(" loopRange=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopRange() instanceof CollectionExpression)
    {   out.print(" loopRange=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopRange()));
    out.print("\""); }
    if (boundedloopstatementx_.getloopVar() instanceof UnaryExpression)
    {   out.print(" loopVar=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopVar() instanceof BasicExpression)
    {   out.print(" loopVar=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopVar() instanceof BinaryExpression)
    {   out.print(" loopVar=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopVar() instanceof ConditionalExpression)
    {   out.print(" loopVar=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getloopVar() instanceof CollectionExpression)
    {   out.print(" loopVar=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getloopVar()));
    out.print("\""); }
    if (boundedloopstatementx_.gettest() instanceof UnaryExpression)
    {   out.print(" test=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (boundedloopstatementx_.gettest() instanceof BasicExpression)
    {   out.print(" test=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (boundedloopstatementx_.gettest() instanceof BinaryExpression)
    {   out.print(" test=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (boundedloopstatementx_.gettest() instanceof ConditionalExpression)
    {   out.print(" test=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (boundedloopstatementx_.gettest() instanceof CollectionExpression)
    {   out.print(" test=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
    if (boundedloopstatementx_.getbody() instanceof ReturnStatement)
    {   out.print(" body=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof AssertStatement)
    {   out.print(" body=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof ErrorStatement)
    {   out.print(" body=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof CatchStatement)
    {   out.print(" body=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (boundedloopstatementx_.getbody() instanceof FinalStatement)
    {   out.print(" body=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
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
 else    if (boundedloopstatementx_.getbody() instanceof TryStatement)
    {   out.print(" body=\"");
    out.print("//@trystatements." + trystatements.indexOf(((BoundedLoopStatement) boundedloopstatements.get(_i)).getbody()));
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
 else    if (unboundedloopstatementx_.gettest() instanceof BasicExpression)
    {   out.print(" test=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.gettest() instanceof BinaryExpression)
    {   out.print(" test=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.gettest() instanceof ConditionalExpression)
    {   out.print(" test=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.gettest() instanceof CollectionExpression)
    {   out.print(" test=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).gettest()));
    out.print("\""); }
    if (unboundedloopstatementx_.getbody() instanceof ReturnStatement)
    {   out.print(" body=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof AssertStatement)
    {   out.print(" body=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof ErrorStatement)
    {   out.print(" body=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof CatchStatement)
    {   out.print(" body=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (unboundedloopstatementx_.getbody() instanceof FinalStatement)
    {   out.print(" body=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
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
 else    if (unboundedloopstatementx_.getbody() instanceof TryStatement)
    {   out.print(" body=\"");
    out.print("//@trystatements." + trystatements.indexOf(((UnboundedLoopStatement) unboundedloopstatements.get(_i)).getbody()));
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
 else      if (assignstatement_type.get(_k) instanceof Enumeration)
      { out.print(" //@enumerations." + enumerations.indexOf(assignstatement_type.get(_k)));
    }
 else      if (assignstatement_type.get(_k) instanceof PrimitiveType)
      { out.print(" //@primitivetypes." + primitivetypes.indexOf(assignstatement_type.get(_k)));
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
 else    if (assignstatementx_.getleft() instanceof BasicExpression)
    {   out.print(" left=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
 else    if (assignstatementx_.getleft() instanceof BinaryExpression)
    {   out.print(" left=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
 else    if (assignstatementx_.getleft() instanceof ConditionalExpression)
    {   out.print(" left=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
 else    if (assignstatementx_.getleft() instanceof CollectionExpression)
    {   out.print(" left=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getleft()));
    out.print("\""); }
    if (assignstatementx_.getright() instanceof UnaryExpression)
    {   out.print(" right=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
    out.print("\""); }
 else    if (assignstatementx_.getright() instanceof BasicExpression)
    {   out.print(" right=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
    out.print("\""); }
 else    if (assignstatementx_.getright() instanceof BinaryExpression)
    {   out.print(" right=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
    out.print("\""); }
 else    if (assignstatementx_.getright() instanceof ConditionalExpression)
    {   out.print(" right=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((AssignStatement) assignstatements.get(_i)).getright()));
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
 else      if (sequencestatement_statements.get(_k) instanceof AssertStatement)
      { out.print(" //@assertstatements." + assertstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof ErrorStatement)
      { out.print(" //@errorstatements." + errorstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof CatchStatement)
      { out.print(" //@catchstatements." + catchstatements.indexOf(sequencestatement_statements.get(_k)));
    }
 else      if (sequencestatement_statements.get(_k) instanceof FinalStatement)
      { out.print(" //@finalstatements." + finalstatements.indexOf(sequencestatement_statements.get(_k)));
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
 else      if (sequencestatement_statements.get(_k) instanceof TryStatement)
      { out.print(" //@trystatements." + trystatements.indexOf(sequencestatement_statements.get(_k)));
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
 else      if (trystatement_catchClauses.get(_k) instanceof AssertStatement)
      { out.print(" //@assertstatements." + assertstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof ErrorStatement)
      { out.print(" //@errorstatements." + errorstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof CatchStatement)
      { out.print(" //@catchstatements." + catchstatements.indexOf(trystatement_catchClauses.get(_k)));
    }
 else      if (trystatement_catchClauses.get(_k) instanceof FinalStatement)
      { out.print(" //@finalstatements." + finalstatements.indexOf(trystatement_catchClauses.get(_k)));
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
 else      if (trystatement_catchClauses.get(_k) instanceof TryStatement)
      { out.print(" //@trystatements." + trystatements.indexOf(trystatement_catchClauses.get(_k)));
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
 else    if (trystatementx_.getbody() instanceof AssertStatement)
    {   out.print(" body=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof ErrorStatement)
    {   out.print(" body=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof CatchStatement)
    {   out.print(" body=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
    out.print("\""); }
 else    if (trystatementx_.getbody() instanceof FinalStatement)
    {   out.print(" body=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
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
 else    if (trystatementx_.getbody() instanceof TryStatement)
    {   out.print(" body=\"");
    out.print("//@trystatements." + trystatements.indexOf(((TryStatement) trystatements.get(_i)).getbody()));
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
 else      if (trystatement_endStatement.get(_k) instanceof AssertStatement)
      { out.print(" //@assertstatements." + assertstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof ErrorStatement)
      { out.print(" //@errorstatements." + errorstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof CatchStatement)
      { out.print(" //@catchstatements." + catchstatements.indexOf(trystatement_endStatement.get(_k)));
    }
 else      if (trystatement_endStatement.get(_k) instanceof FinalStatement)
      { out.print(" //@finalstatements." + finalstatements.indexOf(trystatement_endStatement.get(_k)));
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
 else      if (trystatement_endStatement.get(_k) instanceof TryStatement)
      { out.print(" //@trystatements." + trystatements.indexOf(trystatement_endStatement.get(_k)));
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

    for (int _i = 0; _i < conditionalstatements.size(); _i++)
    { ConditionalStatement conditionalstatementx_ = (ConditionalStatement) conditionalstatements.get(_i);
       out.print("<conditionalstatements xsi:type=\"My:ConditionalStatement\"");
    out.print(" statId=\"" + conditionalstatementx_.getstatId() + "\" ");
    if (conditionalstatementx_.gettest() instanceof UnaryExpression)
    {   out.print(" test=\"");
    out.print("//@unaryexpressions." + unaryexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalstatementx_.gettest() instanceof BasicExpression)
    {   out.print(" test=\"");
    out.print("//@basicexpressions." + basicexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalstatementx_.gettest() instanceof BinaryExpression)
    {   out.print(" test=\"");
    out.print("//@binaryexpressions." + binaryexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalstatementx_.gettest() instanceof ConditionalExpression)
    {   out.print(" test=\"");
    out.print("//@conditionalexpressions." + conditionalexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
 else    if (conditionalstatementx_.gettest() instanceof CollectionExpression)
    {   out.print(" test=\"");
    out.print("//@collectionexpressions." + collectionexpressions.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).gettest()));
    out.print("\""); }
    if (conditionalstatementx_.getifPart() instanceof ReturnStatement)
    {   out.print(" ifPart=\"");
    out.print("//@returnstatements." + returnstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof AssertStatement)
    {   out.print(" ifPart=\"");
    out.print("//@assertstatements." + assertstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof ErrorStatement)
    {   out.print(" ifPart=\"");
    out.print("//@errorstatements." + errorstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof CatchStatement)
    {   out.print(" ifPart=\"");
    out.print("//@catchstatements." + catchstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
    out.print("\""); }
 else    if (conditionalstatementx_.getifPart() instanceof FinalStatement)
    {   out.print(" ifPart=\"");
    out.print("//@finalstatements." + finalstatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
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
 else    if (conditionalstatementx_.getifPart() instanceof TryStatement)
    {   out.print(" ifPart=\"");
    out.print("//@trystatements." + trystatements.indexOf(((ConditionalStatement) conditionalstatements.get(_i)).getifPart()));
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
 else      if (conditionalstatement_elsePart.get(_k) instanceof AssertStatement)
      { out.print(" //@assertstatements." + assertstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof ErrorStatement)
      { out.print(" //@errorstatements." + errorstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof CatchStatement)
      { out.print(" //@catchstatements." + catchstatements.indexOf(conditionalstatement_elsePart.get(_k)));
    }
 else      if (conditionalstatement_elsePart.get(_k) instanceof FinalStatement)
      { out.print(" //@finalstatements." + finalstatements.indexOf(conditionalstatement_elsePart.get(_k)));
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
 else      if (conditionalstatement_elsePart.get(_k) instanceof TryStatement)
      { out.print(" //@trystatements." + trystatements.indexOf(conditionalstatement_elsePart.get(_k)));
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
 else    if (creationstatementx_.gettype() instanceof Enumeration)
    {   out.print(" type=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CreationStatement) creationstatements.get(_i)).gettype()));
    out.print("\""); }
 else    if (creationstatementx_.gettype() instanceof PrimitiveType)
    {   out.print(" type=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CreationStatement) creationstatements.get(_i)).gettype()));
    out.print("\""); }
 else    if (creationstatementx_.gettype() instanceof CollectionType)
    {   out.print(" type=\"");
    out.print("//@collectiontypes." + collectiontypes.indexOf(((CreationStatement) creationstatements.get(_i)).gettype()));
    out.print("\""); }
    if (creationstatementx_.getelementType() instanceof Entity)
    {   out.print(" elementType=\"");
    out.print("//@entitys." + entitys.indexOf(((CreationStatement) creationstatements.get(_i)).getelementType()));
    out.print("\""); }
 else    if (creationstatementx_.getelementType() instanceof Enumeration)
    {   out.print(" elementType=\"");
    out.print("//@enumerations." + enumerations.indexOf(((CreationStatement) creationstatements.get(_i)).getelementType()));
    out.print("\""); }
 else    if (creationstatementx_.getelementType() instanceof PrimitiveType)
    {   out.print(" elementType=\"");
    out.print("//@primitivetypes." + primitivetypes.indexOf(((CreationStatement) creationstatements.get(_i)).getelementType()));
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
 else      if (creationstatement_initialExpression.get(_k) instanceof BasicExpression)
      { out.print(" //@basicexpressions." + basicexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
 else      if (creationstatement_initialExpression.get(_k) instanceof BinaryExpression)
      { out.print(" //@binaryexpressions." + binaryexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
 else      if (creationstatement_initialExpression.get(_k) instanceof ConditionalExpression)
      { out.print(" //@conditionalexpressions." + conditionalexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
 else      if (creationstatement_initialExpression.get(_k) instanceof CollectionExpression)
      { out.print(" //@collectionexpressions." + collectionexpressions.indexOf(creationstatement_initialExpression.get(_k)));
    }
  }
    out.print("\"");
    out.println(" />");
  }

    for (int _i = 0; _i < printcodes.size(); _i++)
    { Printcode printcodex_ = (Printcode) printcodes.get(_i);
       out.print("<printcodes xsi:type=\"My:Printcode\"");
    out.println(" />");
  }

    out.println("</UMLRSDS:model>");
    out.close(); 
  }


  public void saveCSVModel()
  { try {
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
      File _collectiontype = new File("CollectionType.csv");
      PrintWriter _out_collectiontype = new PrintWriter(new BufferedWriter(new FileWriter(_collectiontype)));
      for (int __i = 0; __i < collectiontypes.size(); __i++)
      { CollectionType collectiontypex = (CollectionType) collectiontypes.get(__i);
        collectiontypex.writeCSV(_out_collectiontype);
      }
      _out_collectiontype.close();
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
      File _property = new File("Property.csv");
      PrintWriter _out_property = new PrintWriter(new BufferedWriter(new FileWriter(_property)));
      for (int __i = 0; __i < propertys.size(); __i++)
      { Property propertyx = (Property) propertys.get(__i);
        propertyx.writeCSV(_out_property);
      }
      _out_property.close();
      File _returnstatement = new File("ReturnStatement.csv");
      PrintWriter _out_returnstatement = new PrintWriter(new BufferedWriter(new FileWriter(_returnstatement)));
      for (int __i = 0; __i < returnstatements.size(); __i++)
      { ReturnStatement returnstatementx = (ReturnStatement) returnstatements.get(__i);
        returnstatementx.writeCSV(_out_returnstatement);
      }
      _out_returnstatement.close();
      File _assertstatement = new File("AssertStatement.csv");
      PrintWriter _out_assertstatement = new PrintWriter(new BufferedWriter(new FileWriter(_assertstatement)));
      for (int __i = 0; __i < assertstatements.size(); __i++)
      { AssertStatement assertstatementx = (AssertStatement) assertstatements.get(__i);
        assertstatementx.writeCSV(_out_assertstatement);
      }
      _out_assertstatement.close();
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
      File _breakstatement = new File("BreakStatement.csv");
      PrintWriter _out_breakstatement = new PrintWriter(new BufferedWriter(new FileWriter(_breakstatement)));
      for (int __i = 0; __i < breakstatements.size(); __i++)
      { BreakStatement breakstatementx = (BreakStatement) breakstatements.get(__i);
        breakstatementx.writeCSV(_out_breakstatement);
      }
      _out_breakstatement.close();
      File _continuestatement = new File("ContinueStatement.csv");
      PrintWriter _out_continuestatement = new PrintWriter(new BufferedWriter(new FileWriter(_continuestatement)));
      for (int __i = 0; __i < continuestatements.size(); __i++)
      { ContinueStatement continuestatementx = (ContinueStatement) continuestatements.get(__i);
        continuestatementx.writeCSV(_out_continuestatement);
      }
      _out_continuestatement.close();
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
      File _trystatement = new File("TryStatement.csv");
      PrintWriter _out_trystatement = new PrintWriter(new BufferedWriter(new FileWriter(_trystatement)));
      for (int __i = 0; __i < trystatements.size(); __i++)
      { TryStatement trystatementx = (TryStatement) trystatements.get(__i);
        trystatementx.writeCSV(_out_trystatement);
      }
      _out_trystatement.close();
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
      File _printcode = new File("Printcode.csv");
      PrintWriter _out_printcode = new PrintWriter(new BufferedWriter(new FileWriter(_printcode)));
      for (int __i = 0; __i < printcodes.size(); __i++)
      { Printcode printcodex = (Printcode) printcodes.get(__i);
        printcodex.writeCSV(_out_printcode);
      }
      _out_printcode.close();
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

  public void addAssociation(Association oo) { associations.add(oo); addRelationship(oo); }

  public void addGeneralization(Generalization oo) { generalizations.add(oo); addRelationship(oo); }

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

  public void addEnumerationLiteral(EnumerationLiteral oo) { enumerationliterals.add(oo); addNamedElement(oo); }

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

  public void addProperty(Property oo) { propertys.add(oo); }

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

  public void addOperation(Operation oo) { operations.add(oo); addBehaviouralFeature(oo); }

  public void addUseCase(UseCase oo) { usecases.add(oo); }

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

  public void addContinueStatement(ContinueStatement oo) { continuestatements.add(oo); }

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

  public void addBehaviouralFeature(BehaviouralFeature oo) { behaviouralfeatures.add(oo); addFeature(oo); }

  public void addPrintcode(Printcode oo) { printcodes.add(oo); }



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

  public CollectionType createCollectionType()
  { CollectionType collectiontypex = new CollectionType();
    addCollectionType(collectiontypex);
    return collectiontypex;
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

  public Property createProperty()
  { Property propertyx = new Property();
    addProperty(propertyx);
    return propertyx;
  }

  public ReturnStatement createReturnStatement()
  { ReturnStatement returnstatementx = new ReturnStatement();
    addReturnStatement(returnstatementx);
    return returnstatementx;
  }

  public AssertStatement createAssertStatement()
  { AssertStatement assertstatementx = new AssertStatement();
    addAssertStatement(assertstatementx);
    return assertstatementx;
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

  public BreakStatement createBreakStatement()
  { BreakStatement breakstatementx = new BreakStatement();
    addBreakStatement(breakstatementx);
    return breakstatementx;
  }

  public ContinueStatement createContinueStatement()
  { ContinueStatement continuestatementx = new ContinueStatement();
    addContinueStatement(continuestatementx);
    return continuestatementx;
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

  public TryStatement createTryStatement()
  { TryStatement trystatementx = new TryStatement();
    addTryStatement(trystatementx);
    return trystatementx;
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

  public Printcode createPrintcode()
  { Printcode printcodex = new Printcode();
    addPrintcode(printcodex);
    return printcodex;
  }


public void setname(NamedElement namedelementx, String name_x) 
  { namedelementx.setname(name_x);
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


public void setisSorted(Type typex, boolean isSorted_x) 
  { typex.setisSorted(isSorted_x);
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


public void setisAbstract(Entity entityx, boolean isAbstract_x) 
  { entityx.setisAbstract(isAbstract_x);
    }


public void setisInterface(Entity entityx, boolean isInterface_x) 
  { entityx.setisInterface(isInterface_x);
    }


public void setstereotypes(Entity entityx, int _ind, String stereotypes_x) 
  { entityx.setstereotypes(_ind, stereotypes_x); }

  public void setstereotypes(Entity entityx, List stereotypes_x) 
  { entityx.setstereotypes(stereotypes_x);
    }


  public void addstereotypes(Entity entityx, String stereotypes_x)
  { entityx.addstereotypes(stereotypes_x); }


  public void removestereotypes(Entity entityx, String stereotypes_x)
  { entityx.removestereotypes(stereotypes_x); }


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


  public void addownedAttribute(Entity entityx, Property ownedAttributexx) 
  { if (entityx.getownedAttribute().contains(ownedAttributexx)) { return; }
    if (ownedAttributexx.getowner() != null) { ownedAttributexx.getowner().removeownedAttribute(ownedAttributexx); }
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


  public void setelementType(CollectionType collectiontypex, Type elementTypexx) 
  {   if (collectiontypex.getelementType() == elementTypexx) { return; }
    collectiontypex.setelementType(elementTypexx);
      }


  public void setkeyType(CollectionType collectiontypex, Type keyTypexx) 
  {   if (collectiontypex.getkeyType() == keyTypexx) { return; }
    collectiontypex.setkeyType(keyTypexx);
      }


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


  public void setaccumulator(BinaryExpression binaryexpressionx, Property accumulatorxx) 
  {   if (binaryexpressionx.getaccumulator() == accumulatorxx) { return; }
    binaryexpressionx.setaccumulator(accumulatorxx);
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


public void setisSorted(CollectionExpression collectionexpressionx, boolean isSorted_x) 
  { collectionexpressionx.setisSorted(isSorted_x);
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


public void setname(Property propertyx, String name_x) 
  { propertyx.setname(name_x);
    }


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


public void setisReadOnly(Property propertyx, boolean isReadOnly_x) 
  { propertyx.setisReadOnly(isReadOnly_x);
    }


public void setisStatic(Property propertyx, boolean isStatic_x) 
  { propertyx.setisStatic(isStatic_x);
    }


  public void setqualifier(Property propertyx, List qualifierxx) 
  {   if (qualifierxx.size() > 1) { return; }
  List _oldqualifierxx = propertyx.getqualifier();
  for (int _i = 0; _i < qualifierxx.size(); _i++)
  { Property _xx = (Property) qualifierxx.get(_i);
    if (_oldqualifierxx.contains(_xx)) { }
    else { Property.removeAllqualifier(propertys, _xx); }
  }
    propertyx.setqualifier(qualifierxx);
      }


  public void addqualifier(Property propertyx, Property qualifierxx) 
  { if (propertyx.getqualifier().contains(qualifierxx)) { return; }
      if (propertyx.getqualifier().size() > 0)
    { Property property_xx = (Property) propertyx.getqualifier().get(0);
      propertyx.removequalifier(property_xx);
      }
    Property.removeAllqualifier(propertys,qualifierxx);
    propertyx.addqualifier(qualifierxx);
   }


  public void removequalifier(Property propertyx, Property qualifierxx) 
  { propertyx.removequalifier(qualifierxx);
    }


 public void unionqualifier(Property propertyx, List qualifierx)
  { for (int _i = 0; _i < qualifierx.size(); _i++)
    { Property propertyxqualifier = (Property) qualifierx.get(_i);
      addqualifier(propertyx, propertyxqualifier);
     } } 


 public void subtractqualifier(Property propertyx, List qualifierx)
  { for (int _i = 0; _i < qualifierx.size(); _i++)
    { Property propertyxqualifier = (Property) qualifierx.get(_i);
      removequalifier(propertyx, propertyxqualifier);
     } } 


  public void settype(Property propertyx, Type typexx) 
  {   if (propertyx.gettype() == typexx) { return; }
    propertyx.settype(typexx);
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


  public void setowner(Property propertyx, Entity ownerxx) 
  {   if (propertyx.getowner() == ownerxx) { return; }
    if (propertyx.getowner() != null)
    { Entity old_value = propertyx.getowner();
      old_value.removeownedAttribute(propertyx); } 
    if (ownerxx != null) { ownerxx.addownedAttribute(propertyx); }
    propertyx.setowner(ownerxx);
      }


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


public void setisQuery(Operation operationx, boolean isQuery_x) 
  { operationx.setisQuery(isQuery_x);
    }


public void setisAbstract(Operation operationx, boolean isAbstract_x) 
  { operationx.setisAbstract(isAbstract_x);
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


public void setname(UseCase usecasex, String name_x) 
  { usecasex.setname(name_x);
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


  public void setresultType(UseCase usecasex, Type resultTypexx) 
  {   if (usecasex.getresultType() == resultTypexx) { return; }
    usecasex.setresultType(resultTypexx);
      }


  public void setclassifierBehaviour(UseCase usecasex, Statement classifierBehaviourxx) 
  {   if (usecasex.getclassifierBehaviour() == classifierBehaviourxx) { return; }
    for (int _q = 0; _q < usecases.size(); _q++)
    { UseCase _q_E1x = (UseCase) usecases.get(_q);
      if (_q_E1x.getclassifierBehaviour() == classifierBehaviourxx)
      { _q_E1x.setclassifierBehaviour(null); }
    }
    usecasex.setclassifierBehaviour(classifierBehaviourxx);
      }


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
  {     creationstatementx.setinitialExpression(initialExpressionxx);
      }


  public void addinitialExpression(CreationStatement creationstatementx, Expression initialExpressionxx) 
  { if (creationstatementx.getinitialExpression().contains(initialExpressionxx)) { return; }
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


public void setisStatic(BehaviouralFeature behaviouralfeaturex, boolean isStatic_x) 
  { behaviouralfeaturex.setisStatic(isStatic_x);
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



  public  List AllTypedefaultInitialValue(List typexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < typexs.size(); _i++)
    { Type typex = (Type) typexs.get(_i);
      result.add(typex.defaultInitialValue());
    }
    return result; 
  }

  public  List AllTypetoPython(List typexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < typexs.size(); _i++)
    { Type typex = (Type) typexs.get(_i);
      result.add(typex.toPython());
    }
    return result; 
  }

  public  List AllTypeisStringType(List typexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < typexs.size(); _i++)
    { Type typex = (Type) typexs.get(_i);
      result.add(new Boolean(typex.isStringType()));
    }
    return result; 
  }

  public  List AllEnumerationdefaultInitialValue(List enumerationxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < enumerationxs.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerationxs.get(_i);
      result.add(enumerationx.defaultInitialValue());
    }
    return result; 
  }

  public  List AllEnumerationtoPython(List enumerationxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < enumerationxs.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerationxs.get(_i);
      result.add(enumerationx.toPython());
    }
    return result; 
  }

  public  List AllEnumerationliterals(List enumerationxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < enumerationxs.size(); _i++)
    { Enumeration enumerationx = (Enumeration) enumerationxs.get(_i);
      result.add(enumerationx.literals());
    }
    return result; 
  }

  public void printcode2(Enumeration enumerationx)
  {   enumerationx.printcode2();
   }

  public  List AllPrimitiveTypedefaultInitialValue(List primitivetypexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < primitivetypexs.size(); _i++)
    { PrimitiveType primitivetypex = (PrimitiveType) primitivetypexs.get(_i);
      result.add(primitivetypex.defaultInitialValue());
    }
    return result; 
  }

  public  List AllPrimitiveTypetoPython(List primitivetypexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < primitivetypexs.size(); _i++)
    { PrimitiveType primitivetypex = (PrimitiveType) primitivetypexs.get(_i);
      result.add(primitivetypex.toPython());
    }
    return result; 
  }

  public  List AllEntityallLeafSubclasses(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      if (entityx.allLeafSubclasses() != null)
      { result.addAll(entityx.allLeafSubclasses()); }
    }
    return result; 
  }

  public  List AllEntityallProperties(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      if (entityx.allProperties() != null)
      { result.addAll(entityx.allProperties()); }
    }
    return result; 
  }

  public  List AllEntityallOperations(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      if (entityx.allOperations() != null)
      { result.addAll(entityx.allOperations()); }
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

  public  List AllEntityisActiveClass(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(new Boolean(entityx.isActiveClass()));
    }
    return result; 
  }

  public  List AllEntityisSingleValued(List entityxs,String d)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(new Boolean(entityx.isSingleValued(d)));
    }
    return result; 
  }

  public  List AllEntityisSetValued(List entityxs,String d)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(new Boolean(entityx.isSetValued(d)));
    }
    return result; 
  }

  public  List AllEntityisSequenceValued(List entityxs,String d)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(new Boolean(entityx.isSequenceValued(d)));
    }
    return result; 
  }

  public  List AllEntitydefaultInitialValue(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.defaultInitialValue());
    }
    return result; 
  }

  public  List AllEntityinitialisations(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.initialisations());
    }
    return result; 
  }

  public  List AllEntitygeneralisationString(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.generalisationString());
    }
    return result; 
  }

  public  List AllEntityclassHeader(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.classHeader());
    }
    return result; 
  }

  public  List AllEntityallStaticCaches(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.allStaticCaches());
    }
    return result; 
  }

  public  List AllEntityallInstanceCaches(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.allInstanceCaches());
    }
    return result; 
  }

  public  List AllEntitystaticAttributes(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.staticAttributes());
    }
    return result; 
  }

  public  List AllEntityclassConstructor(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.classConstructor());
    }
    return result; 
  }

  public  List AllEntityabstractClassConstructor(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.abstractClassConstructor());
    }
    return result; 
  }

  public  List AllEntitycallOp(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.callOp());
    }
    return result; 
  }

  public  List AllEntitycreateOp(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.createOp());
    }
    return result; 
  }

  public  List AllEntitycreatePKOp(List entityxs,String key)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.createPKOp(key));
    }
    return result; 
  }

  public  List AllEntitycreateOclTypeOp(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.createOclTypeOp());
    }
    return result; 
  }

  public  List AllEntityallInstancesOp(List entityxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < entityxs.size(); _i++)
    { Entity entityx = (Entity) entityxs.get(_i);
      result.add(entityx.allInstancesOp());
    }
    return result; 
  }

  public void printcode3(Entity entityx)
  {   entityx.printcode3();
   }

  public void printcode4(Entity entityx)
  {   entityx.printcode4();
   }

  public void printcode5(Entity entityx)
  {   entityx.printcode5();
   }

  public void printcode6(Entity entityx)
  {   entityx.printcode6();
   }

  public void printcode7(Entity entityx,Property key)
  {   entityx.printcode7(key);
   }

  public void printcode7outer(Entity entityx)
  {   entityx.printcode7outer();
   }

  public  List AllCollectionTypedefaultInitialValue(List collectiontypexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < collectiontypexs.size(); _i++)
    { CollectionType collectiontypex = (CollectionType) collectiontypexs.get(_i);
      result.add(collectiontypex.defaultInitialValue());
    }
    return result; 
  }

  public  List AllExpressiontypeName(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(expressionx.typeName());
    }
    return result; 
  }

  public Expression addReference(Expression expressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   expressionx.addReference(x);
     return result;  }

  public  List AllExpressionaddReference(List expressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(addReference(expressionx,x));
    }
    return result; 
  }

  public  List AllExpressionisCollection(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(new Boolean(expressionx.isCollection()));
    }
    return result; 
  }

  public  List AllExpressionisSet(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(new Boolean(expressionx.isSet()));
    }
    return result; 
  }

  public  List AllExpressionisMap(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(new Boolean(expressionx.isMap()));
    }
    return result; 
  }

  public  List AllExpressionisNumeric(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(new Boolean(expressionx.isNumeric()));
    }
    return result; 
  }

  public  List AllExpressionisString(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(new Boolean(expressionx.isString()));
    }
    return result; 
  }

  public  List AllExpressionisRef(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(new Boolean(expressionx.isRef()));
    }
    return result; 
  }

  public  List AllExpressionisEnumeration(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(new Boolean(expressionx.isEnumeration()));
    }
    return result; 
  }

  public  List AllExpressiontoPython(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(expressionx.toPython());
    }
    return result; 
  }

  public  List AllExpressionupdateForm(List expressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < expressionxs.size(); _i++)
    { Expression expressionx = (Expression) expressionxs.get(_i);
      result.add(expressionx.updateForm());
    }
    return result; 
  }

  public  List AllBinaryExpressionmapDividesExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapDividesExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapNumericExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapNumericExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapComparitorExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapComparitorExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapStringExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapStringExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapStringPlus(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapStringPlus(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapRefPlus(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapRefPlus(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapBooleanExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapBooleanExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapBinaryCollectionExpression(List binaryexpressionxs,String ls,String rs,String lt,String rt)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapBinaryCollectionExpression(ls, rs, lt, rt));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapBinaryMapExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapBinaryMapExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapDistributedIteratorExpression(List binaryexpressionxs,String ls,String rs,Expression rexp)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapDistributedIteratorExpression(ls, rs, rexp));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapMapIteratorExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapMapIteratorExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapIteratorExpression(List binaryexpressionxs,String ls,String rs,String tn)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapIteratorExpression(ls, rs, tn));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapTypeCastExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapTypeCastExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapCatchExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapCatchExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionbothNumeric(List binaryexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(new Boolean(binaryexpressionx.bothNumeric()));
    }
    return result; 
  }

  public  List AllBinaryExpressionbothString(List binaryexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(new Boolean(binaryexpressionx.bothString()));
    }
    return result; 
  }

  public  List AllBinaryExpressionisStringPlus(List binaryexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(new Boolean(binaryexpressionx.isStringPlus()));
    }
    return result; 
  }

  public  List AllBinaryExpressionisRefPlus(List binaryexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(new Boolean(binaryexpressionx.isRefPlus()));
    }
    return result; 
  }

  public  List AllBinaryExpressionmapBinaryExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.mapBinaryExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressiontoPython(List binaryexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.toPython());
    }
    return result; 
  }

  public  List AllBinaryExpressionupdateFormBinaryExpression(List binaryexpressionxs,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.updateFormBinaryExpression(ls, rs));
    }
    return result; 
  }

  public  List AllBinaryExpressionupdateForm(List binaryexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(binaryexpressionx.updateForm());
    }
    return result; 
  }

  public Expression addReference(BinaryExpression binaryexpressionx,BasicExpression x)
  { 
   Expression result = null;
    //  if (!(x.getdata() != variable)) { return result; } 
   result =   binaryexpressionx.addReference(x);
     return result;  }

  public  List AllBinaryExpressionaddReference(List binaryexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < binaryexpressionxs.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) binaryexpressionxs.get(_i);
      result.add(addReference(binaryexpressionx,x));
    }
    return result; 
  }

  public  List AllConditionalExpressionmapConditionalExpression(List conditionalexpressionxs,String ts,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < conditionalexpressionxs.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressionxs.get(_i);
      result.add(conditionalexpressionx.mapConditionalExpression(ts, ls, rs));
    }
    return result; 
  }

  public  List AllConditionalExpressiontoPython(List conditionalexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < conditionalexpressionxs.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressionxs.get(_i);
      result.add(conditionalexpressionx.toPython());
    }
    return result; 
  }

  public  List AllConditionalExpressionupdateFormConditionalExpression(List conditionalexpressionxs,String ts,String ls,String rs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < conditionalexpressionxs.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressionxs.get(_i);
      result.add(conditionalexpressionx.updateFormConditionalExpression(ts, ls, rs));
    }
    return result; 
  }

  public  List AllConditionalExpressionupdateForm(List conditionalexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < conditionalexpressionxs.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressionxs.get(_i);
      result.add(conditionalexpressionx.updateForm());
    }
    return result; 
  }

  public Expression addReference(ConditionalExpression conditionalexpressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   conditionalexpressionx.addReference(x);
     return result;  }

  public  List AllConditionalExpressionaddReference(List conditionalexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < conditionalexpressionxs.size(); _i++)
    { ConditionalExpression conditionalexpressionx = (ConditionalExpression) conditionalexpressionxs.get(_i);
      result.add(addReference(conditionalexpressionx,x));
    }
    return result; 
  }

  public  List AllUnaryExpressionmapNumericExpression(List unaryexpressionxs,String arg)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.mapNumericExpression(arg));
    }
    return result; 
  }

  public  List AllUnaryExpressionmapStringExpression(List unaryexpressionxs,String arg)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.mapStringExpression(arg));
    }
    return result; 
  }

  public  List AllUnaryExpressionmapReduceExpression(List unaryexpressionxs,String arg,String tn,Type et)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.mapReduceExpression(arg, tn, et));
    }
    return result; 
  }

  public  List AllUnaryExpressionmapUnaryCollectionExpression(List unaryexpressionxs,String arg,String tn)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.mapUnaryCollectionExpression(arg, tn));
    }
    return result; 
  }

  public  List AllUnaryExpressionmapUnaryMapExpression(List unaryexpressionxs,String arg,String tn)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.mapUnaryMapExpression(arg, tn));
    }
    return result; 
  }

  public  List AllUnaryExpressionmapUnaryExpression(List unaryexpressionxs,String arg)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.mapUnaryExpression(arg));
    }
    return result; 
  }

  public  List AllUnaryExpressiontoPython(List unaryexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.toPython());
    }
    return result; 
  }

  public  List AllUnaryExpressionupdateFormUnaryExpression(List unaryexpressionxs,String arg)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.updateFormUnaryExpression(arg));
    }
    return result; 
  }

  public  List AllUnaryExpressionupdateForm(List unaryexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(unaryexpressionx.updateForm());
    }
    return result; 
  }

  public Expression addReference(UnaryExpression unaryexpressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   unaryexpressionx.addReference(x);
     return result;  }

  public  List AllUnaryExpressionaddReference(List unaryexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unaryexpressionxs.size(); _i++)
    { UnaryExpression unaryexpressionx = (UnaryExpression) unaryexpressionxs.get(_i);
      result.add(addReference(unaryexpressionx,x));
    }
    return result; 
  }

  public  List AllCollectionExpressionmapCollectionExpression(List collectionexpressionxs,List elems,String tn)
  { 
    List result = new Vector();
    for (int _i = 0; _i < collectionexpressionxs.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressionxs.get(_i);
      result.add(collectionexpressionx.mapCollectionExpression(elems, tn));
    }
    return result; 
  }

  public  List AllCollectionExpressiontoPython(List collectionexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < collectionexpressionxs.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressionxs.get(_i);
      result.add(collectionexpressionx.toPython());
    }
    return result; 
  }

  public  List AllCollectionExpressionupdateForm(List collectionexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < collectionexpressionxs.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressionxs.get(_i);
      result.add(collectionexpressionx.updateForm());
    }
    return result; 
  }

  public Expression addReference(CollectionExpression collectionexpressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   collectionexpressionx.addReference(x);
     return result;  }

  public  List AllCollectionExpressionaddReference(List collectionexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < collectionexpressionxs.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressionxs.get(_i);
      result.add(addReference(collectionexpressionx,x));
    }
    return result; 
  }

  public  List AllCollectionExpressiontoLambdaList(List collectionexpressionxs,String vbl)
  { 
    List result = new Vector();
    for (int _i = 0; _i < collectionexpressionxs.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) collectionexpressionxs.get(_i);
      result.add(collectionexpressionx.toLambdaList(vbl));
    }
    return result; 
  }

  public  List AllBasicExpressionnoContextnoObject(List basicexpressionxs,List obs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(new Boolean(basicexpressionx.noContextnoObject(obs)));
    }
    return result; 
  }

  public  List AllBasicExpressioncontextAndObject(List basicexpressionxs,List obs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(new Boolean(basicexpressionx.contextAndObject(obs)));
    }
    return result; 
  }

  public  List AllBasicExpressionisOclExceptionCreation(List basicexpressionxs,List obs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(new Boolean(basicexpressionx.isOclExceptionCreation(obs)));
    }
    return result; 
  }

  public  List AllBasicExpressionmapTypeExpression(List basicexpressionxs,List ainds)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapTypeExpression(ainds));
    }
    return result; 
  }

  public  List AllBasicExpressionmapValueExpression(List basicexpressionxs,List aind)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapValueExpression(aind));
    }
    return result; 
  }

  public  List AllBasicExpressionmapVariableExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapVariableExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapStaticAttributeExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapStaticAttributeExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapReferencedAttributeExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapReferencedAttributeExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapAttributeExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapAttributeExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapErrorCall(List basicexpressionxs,List obs,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapErrorCall(obs, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapStaticOperationExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapStaticOperationExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapInstanceOperationExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapInstanceOperationExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapOperationExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapOperationExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapIntegerFunctionExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapIntegerFunctionExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapInsertAtFunctionExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapInsertAtFunctionExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapSetAtFunctionExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapSetAtFunctionExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapSubrangeFunctionExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapSubrangeFunctionExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapFunctionExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapFunctionExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapClassArrayExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapClassArrayExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapClassExpression(List basicexpressionxs,List obs,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapClassExpression(obs, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressionmapBasicExpression(List basicexpressionxs,List ob,List aind,List pars)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.mapBasicExpression(ob, aind, pars));
    }
    return result; 
  }

  public  List AllBasicExpressiontoPython(List basicexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.toPython());
    }
    return result; 
  }

  public  List AllBasicExpressionupdateForm(List basicexpressionxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(basicexpressionx.updateForm());
    }
    return result; 
  }

  public Expression addClassIdReference(BasicExpression basicexpressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   basicexpressionx.addClassIdReference(x);
     return result;  }

  public  List AllBasicExpressionaddClassIdReference(List basicexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(addClassIdReference(basicexpressionx,x));
    }
    return result; 
  }

  public Expression addBEReference(BasicExpression basicexpressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   basicexpressionx.addBEReference(x);
     return result;  }

  public  List AllBasicExpressionaddBEReference(List basicexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(addBEReference(basicexpressionx,x));
    }
    return result; 
  }

  public Expression addSelfBEReference(BasicExpression basicexpressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   basicexpressionx.addSelfBEReference(x);
     return result;  }

  public  List AllBasicExpressionaddSelfBEReference(List basicexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(addSelfBEReference(basicexpressionx,x));
    }
    return result; 
  }

  public Expression addObjectRefBEReference(BasicExpression basicexpressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   basicexpressionx.addObjectRefBEReference(x);
     return result;  }

  public  List AllBasicExpressionaddObjectRefBEReference(List basicexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(addObjectRefBEReference(basicexpressionx,x));
    }
    return result; 
  }

  public Expression addReference(BasicExpression basicexpressionx,BasicExpression x)
  { 
   Expression result = null;
   result =   basicexpressionx.addReference(x);
     return result;  }

  public  List AllBasicExpressionaddReference(List basicexpressionxs,BasicExpression x)
  { 
    List result = new Vector();
    for (int _i = 0; _i < basicexpressionxs.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) basicexpressionxs.get(_i);
      result.add(addReference(basicexpressionx,x));
    }
    return result; 
  }

  public  List AllPropertyinitialisation(List propertyxs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < propertyxs.size(); _i++)
    { Property propertyx = (Property) propertyxs.get(_i);
      result.add(propertyx.initialisation());
    }
    return result; 
  }

  public  List AllPropertygetPKOp(List propertyxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < propertyxs.size(); _i++)
    { Property propertyx = (Property) propertyxs.get(_i);
      result.add(propertyx.getPKOp(ent));
    }
    return result; 
  }

  public  List AllPropertygetPKOps(List propertyxs,String ent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < propertyxs.size(); _i++)
    { Property propertyx = (Property) propertyxs.get(_i);
      result.add(propertyx.getPKOps(ent));
    }
    return result; 
  }

  public  List AllStatementtoPython(List statementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < statementxs.size(); _i++)
    { Statement statementx = (Statement) statementxs.get(_i);
      result.add(statementx.toPython(indent));
    }
    return result; 
  }

  public  List AllReturnStatementtoPython(List returnstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < returnstatementxs.size(); _i++)
    { ReturnStatement returnstatementx = (ReturnStatement) returnstatementxs.get(_i);
      result.add(returnstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllAssertStatementtoPython(List assertstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < assertstatementxs.size(); _i++)
    { AssertStatement assertstatementx = (AssertStatement) assertstatementxs.get(_i);
      result.add(assertstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllErrorStatementtoPython(List errorstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < errorstatementxs.size(); _i++)
    { ErrorStatement errorstatementx = (ErrorStatement) errorstatementxs.get(_i);
      result.add(errorstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllCatchStatementtoPython(List catchstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < catchstatementxs.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) catchstatementxs.get(_i);
      result.add(catchstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllFinalStatementtoPython(List finalstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < finalstatementxs.size(); _i++)
    { FinalStatement finalstatementx = (FinalStatement) finalstatementxs.get(_i);
      result.add(finalstatementx.toPython(indent));
    }
    return result; 
  }

  public void displayStaticCachedOperation(Operation operationx,int indent,String p,String obj)
  {   operationx.displayStaticCachedOperation(indent,p,obj);
   }

  public  List AllOperationdisplayStaticCachedOperation(List operationxs,int indent,String p,String obj)
  { 
    List result = new Vector();
    for (int _i = 0; _i < operationxs.size(); _i++)
    { Operation operationx = (Operation) operationxs.get(_i);
      displayStaticCachedOperation(operationx,indent, p, obj);
    }
    return result; 
  }

  public void displayInstanceCachedOperation(Operation operationx,int indent,String p,String obj)
  {   operationx.displayInstanceCachedOperation(indent,p,obj);
   }

  public  List AllOperationdisplayInstanceCachedOperation(List operationxs,int indent,String p,String obj)
  { 
    List result = new Vector();
    for (int _i = 0; _i < operationxs.size(); _i++)
    { Operation operationx = (Operation) operationxs.get(_i);
      displayInstanceCachedOperation(operationx,indent, p, obj);
    }
    return result; 
  }

  public void displayStaticOperation(Operation operationx,int indent)
  {   operationx.displayStaticOperation(indent);
   }

  public  List AllOperationdisplayStaticOperation(List operationxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < operationxs.size(); _i++)
    { Operation operationx = (Operation) operationxs.get(_i);
      displayStaticOperation(operationx,indent);
    }
    return result; 
  }

  public void displayInstanceOperation(Operation operationx,int indent)
  {   operationx.displayInstanceOperation(indent);
   }

  public  List AllOperationdisplayInstanceOperation(List operationxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < operationxs.size(); _i++)
    { Operation operationx = (Operation) operationxs.get(_i);
      displayInstanceOperation(operationx,indent);
    }
    return result; 
  }

  public void displayOperation(Operation operationx,int indent)
  {   operationx.displayOperation(indent);
   }

  public  List AllOperationdisplayOperation(List operationxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < operationxs.size(); _i++)
    { Operation operationx = (Operation) operationxs.get(_i);
      displayOperation(operationx,indent);
    }
    return result; 
  }

  public void printcode8(Operation operationx)
  {   operationx.printcode8();
   }

  public  List AllUseCasemapUseCase(List usecasexs)
  { 
    List result = new Vector();
    for (int _i = 0; _i < usecasexs.size(); _i++)
    { UseCase usecasex = (UseCase) usecasexs.get(_i);
      result.add(usecasex.mapUseCase());
    }
    return result; 
  }

  public void printcode9(UseCase usecasex)
  {   usecasex.printcode9();
   }

  public  List AllBreakStatementtoPython(List breakstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < breakstatementxs.size(); _i++)
    { BreakStatement breakstatementx = (BreakStatement) breakstatementxs.get(_i);
      result.add(breakstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllContinueStatementtoPython(List continuestatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < continuestatementxs.size(); _i++)
    { ContinueStatement continuestatementx = (ContinueStatement) continuestatementxs.get(_i);
      result.add(continuestatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllOperationCallStatementtoPython(List operationcallstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < operationcallstatementxs.size(); _i++)
    { OperationCallStatement operationcallstatementx = (OperationCallStatement) operationcallstatementxs.get(_i);
      result.add(operationcallstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllImplicitCallStatementtoPython(List implicitcallstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < implicitcallstatementxs.size(); _i++)
    { ImplicitCallStatement implicitcallstatementx = (ImplicitCallStatement) implicitcallstatementxs.get(_i);
      result.add(implicitcallstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllBoundedLoopStatementtoPython(List boundedloopstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < boundedloopstatementxs.size(); _i++)
    { BoundedLoopStatement boundedloopstatementx = (BoundedLoopStatement) boundedloopstatementxs.get(_i);
      result.add(boundedloopstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllUnboundedLoopStatementtoPython(List unboundedloopstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < unboundedloopstatementxs.size(); _i++)
    { UnboundedLoopStatement unboundedloopstatementx = (UnboundedLoopStatement) unboundedloopstatementxs.get(_i);
      result.add(unboundedloopstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllAssignStatementtoPython(List assignstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < assignstatementxs.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) assignstatementxs.get(_i);
      result.add(assignstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllSequenceStatementtoPython(List sequencestatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < sequencestatementxs.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) sequencestatementxs.get(_i);
      result.add(sequencestatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllTryStatementtoPython(List trystatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < trystatementxs.size(); _i++)
    { TryStatement trystatementx = (TryStatement) trystatementxs.get(_i);
      result.add(trystatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllConditionalStatementelsecode(List conditionalstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < conditionalstatementxs.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatementxs.get(_i);
      result.add(conditionalstatementx.elsecode(indent));
    }
    return result; 
  }

  public  List AllConditionalStatementtoPython(List conditionalstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < conditionalstatementxs.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) conditionalstatementxs.get(_i);
      result.add(conditionalstatementx.toPython(indent));
    }
    return result; 
  }

  public  List AllCreationStatementtoPython(List creationstatementxs,int indent)
  { 
    List result = new Vector();
    for (int _i = 0; _i < creationstatementxs.size(); _i++)
    { CreationStatement creationstatementx = (CreationStatement) creationstatementxs.get(_i);
      result.add(creationstatementx.toPython(indent));
    }
    return result; 
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
    if (namedelementxx instanceof EnumerationLiteral)
    { killEnumerationLiteral((EnumerationLiteral) namedelementxx); }
    if (namedelementxx instanceof Association)
    { killAssociation((Association) namedelementxx); }
    if (namedelementxx instanceof Generalization)
    { killGeneralization((Generalization) namedelementxx); }
    if (namedelementxx instanceof Entity)
    { killEntity((Entity) namedelementxx); }
    if (namedelementxx instanceof Enumeration)
    { killEnumeration((Enumeration) namedelementxx); }
    if (namedelementxx instanceof PrimitiveType)
    { killPrimitiveType((PrimitiveType) namedelementxx); }
    if (namedelementxx instanceof CollectionType)
    { killCollectionType((CollectionType) namedelementxx); }
    if (namedelementxx instanceof Operation)
    { killOperation((Operation) namedelementxx); }
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
    Vector _1removedtypeProperty = new Vector();
    Vector _1removedelementTypeCollectionType = new Vector();
    Vector _1removedkeyTypeCollectionType = new Vector();
    Vector _1removedtypeExpression = new Vector();
    Vector _1removedelementTypeExpression = new Vector();
    Vector _1removedresultTypeUseCase = new Vector();
    Vector _1removedtypeCreationStatement = new Vector();
    Vector _1removedelementTypeCreationStatement = new Vector();
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
    Vector _1qrangetypeProperty = new Vector();
    _1qrangetypeProperty.addAll(propertys);
    for (int _i = 0; _i < _1qrangetypeProperty.size(); _i++)
    { Property propertyx = (Property) _1qrangetypeProperty.get(_i);
      if (propertyx != null && typexx.equals(propertyx.gettype()))
      { _1removedtypeProperty.add(propertyx);
        propertyx.settype(null);
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
    Vector _1qrangeresultTypeUseCase = new Vector();
    _1qrangeresultTypeUseCase.addAll(usecases);
    for (int _i = 0; _i < _1qrangeresultTypeUseCase.size(); _i++)
    { UseCase usecasex = (UseCase) _1qrangeresultTypeUseCase.get(_i);
      if (usecasex != null && typexx.equals(usecasex.getresultType()))
      { _1removedresultTypeUseCase.add(usecasex);
        usecasex.setresultType(null);
      }
    }
    Vector _1qrangetypeAssignStatement = new Vector();
    _1qrangetypeAssignStatement.addAll(assignstatements);
    for (int _i = 0; _i < _1qrangetypeAssignStatement.size(); _i++)
    { AssignStatement assignstatementx = (AssignStatement) _1qrangetypeAssignStatement.get(_i);
      if (assignstatementx != null && assignstatementx.gettype().contains(typexx))
      { removetype(assignstatementx,typexx); }
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
    typetypeIdindex.remove(typexx.gettypeId());
    for (int _i = 0; _i < _1removedtypeFeature.size(); _i++)
    { killAbstractFeature((Feature) _1removedtypeFeature.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeFeature.size(); _i++)
    { killAbstractFeature((Feature) _1removedelementTypeFeature.get(_i)); }
    for (int _i = 0; _i < _1removedtypeProperty.size(); _i++)
    { killProperty((Property) _1removedtypeProperty.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeCollectionType.size(); _i++)
    { killCollectionType((CollectionType) _1removedelementTypeCollectionType.get(_i)); }
    for (int _i = 0; _i < _1removedkeyTypeCollectionType.size(); _i++)
    { killCollectionType((CollectionType) _1removedkeyTypeCollectionType.get(_i)); }
    for (int _i = 0; _i < _1removedtypeExpression.size(); _i++)
    { killAbstractExpression((Expression) _1removedtypeExpression.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeExpression.size(); _i++)
    { killAbstractExpression((Expression) _1removedelementTypeExpression.get(_i)); }
    for (int _i = 0; _i < _1removedresultTypeUseCase.size(); _i++)
    { killUseCase((UseCase) _1removedresultTypeUseCase.get(_i)); }
    for (int _i = 0; _i < _1removedtypeCreationStatement.size(); _i++)
    { killCreationStatement((CreationStatement) _1removedtypeCreationStatement.get(_i)); }
    for (int _i = 0; _i < _1removedelementTypeCreationStatement.size(); _i++)
    { killCreationStatement((CreationStatement) _1removedelementTypeCreationStatement.get(_i)); }
  killNamedElement(typexx);
  }

  public void killAbstractType(Type typexx)
  {
    if (typexx instanceof Entity)
    { killEntity((Entity) typexx); }
    if (typexx instanceof Enumeration)
    { killEnumeration((Enumeration) typexx); }
    if (typexx instanceof PrimitiveType)
    { killPrimitiveType((PrimitiveType) typexx); }
    if (typexx instanceof CollectionType)
    { killCollectionType((CollectionType) typexx); }
  }

  public void killAbstractType(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Type _e = (Type) _l.get(_i);
      killAbstractType(_e);
    }
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
    if (classifierxx instanceof Enumeration)
    { killEnumeration((Enumeration) classifierxx); }
    if (classifierxx instanceof PrimitiveType)
    { killPrimitiveType((PrimitiveType) classifierxx); }
    if (classifierxx instanceof CollectionType)
    { killCollectionType((CollectionType) classifierxx); }
  }

  public void killAbstractClassifier(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Classifier _e = (Classifier) _l.get(_i);
      killAbstractClassifier(_e);
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
    if (datatypexx instanceof Enumeration)
    { killEnumeration((Enumeration) datatypexx); }
    if (datatypexx instanceof PrimitiveType)
    { killPrimitiveType((PrimitiveType) datatypexx); }
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
  killNamedElement(enumerationliteralxx);
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
    Vector _2removedspecificGeneralization = new Vector();
    Vector _1removedgeneralGeneralization = new Vector();
    Vector _2removedownerOperation = new Vector();
    Vector _1removedownerProperty = new Vector();
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
    Vector _1qrangeownerProperty = new Vector();
    _1qrangeownerProperty.addAll(entityxx.getownedAttribute());
    for (int _i = 0; _i < _1qrangeownerProperty.size(); _i++)
    { Property propertyx = (Property) _1qrangeownerProperty.get(_i);
      if (propertyx != null && entityxx.equals(propertyx.getowner()))
      { _1removedownerProperty.add(propertyx);
        propertyx.setowner(null);
      }
    }
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
    for (int _i = 0; _i < _2removedspecificGeneralization.size(); _i++)
    { killGeneralization((Generalization) _2removedspecificGeneralization.get(_i)); }
    for (int _i = 0; _i < _1removedgeneralGeneralization.size(); _i++)
    { killGeneralization((Generalization) _1removedgeneralGeneralization.get(_i)); }
    for (int _i = 0; _i < _2removedownerOperation.size(); _i++)
    { killOperation((Operation) _2removedownerOperation.get(_i)); }
    for (int _i = 0; _i < _1removedownerProperty.size(); _i++)
    { killProperty((Property) _1removedownerProperty.get(_i)); }
  killClassifier(entityxx);
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
    Vector _1removedconditionAssertStatement = new Vector();
    Vector _1removedthrownObjectErrorStatement = new Vector();
    Vector _1removedcaughtObjectCatchStatement = new Vector();
    Vector _1removedcallExpOperationCallStatement = new Vector();
    Vector _1removedcallExpImplicitCallStatement = new Vector();
    Vector _1removedtestLoopStatement = new Vector();
    Vector _1removedleftAssignStatement = new Vector();
    Vector _1removedrightAssignStatement = new Vector();
    Vector _1removedtestConditionalStatement = new Vector();
    Vector _1removedloopRangeBoundedLoopStatement = new Vector();
    Vector _1removedloopVarBoundedLoopStatement = new Vector();
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
    Vector _1qrangecaughtObjectCatchStatement = new Vector();
    _1qrangecaughtObjectCatchStatement.addAll(catchstatements);
    for (int _i = 0; _i < _1qrangecaughtObjectCatchStatement.size(); _i++)
    { CatchStatement catchstatementx = (CatchStatement) _1qrangecaughtObjectCatchStatement.get(_i);
      if (catchstatementx != null && expressionxx.equals(catchstatementx.getcaughtObject()))
      { _1removedcaughtObjectCatchStatement.add(catchstatementx);
        catchstatementx.setcaughtObject(null);
      }
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
    for (int _i = 0; _i < _1removedconditionAssertStatement.size(); _i++)
    { killAssertStatement((AssertStatement) _1removedconditionAssertStatement.get(_i)); }
    for (int _i = 0; _i < _1removedthrownObjectErrorStatement.size(); _i++)
    { killErrorStatement((ErrorStatement) _1removedthrownObjectErrorStatement.get(_i)); }
    for (int _i = 0; _i < _1removedcaughtObjectCatchStatement.size(); _i++)
    { killCatchStatement((CatchStatement) _1removedcaughtObjectCatchStatement.get(_i)); }
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
  }

  public void killAbstractExpression(Expression expressionxx)
  {
    if (expressionxx instanceof UnaryExpression)
    { killUnaryExpression((UnaryExpression) expressionxx); }
    if (expressionxx instanceof BasicExpression)
    { killBasicExpression((BasicExpression) expressionxx); }
    if (expressionxx instanceof BinaryExpression)
    { killBinaryExpression((BinaryExpression) expressionxx); }
    if (expressionxx instanceof ConditionalExpression)
    { killConditionalExpression((ConditionalExpression) expressionxx); }
    if (expressionxx instanceof CollectionExpression)
    { killCollectionExpression((CollectionExpression) expressionxx); }
  }

  public void killAbstractExpression(List _l)
  { for (int _i = 0; _i < _l.size(); _i++)
    { Expression _e = (Expression) _l.get(_i);
      killAbstractExpression(_e);
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



  public void killAllProperty(List propertyxx)
  { for (int _i = 0; _i < propertyxx.size(); _i++)
    { killProperty((Property) propertyxx.get(_i)); }
  }

  public void killProperty(Property propertyxx)
  { if (propertyxx == null) { return; }
   propertys.remove(propertyxx);
    Vector _1removedaccumulatorBinaryExpression = new Vector();
    Vector _1qrangememberEndAssociation = new Vector();
    _1qrangememberEndAssociation.addAll(associations);
    for (int _i = 0; _i < _1qrangememberEndAssociation.size(); _i++)
    { Association associationx = (Association) _1qrangememberEndAssociation.get(_i);
      if (associationx != null && associationx.getmemberEnd().contains(propertyxx))
      { removememberEnd(associationx,propertyxx); }
    }
    Vector _1qrangequalifierProperty = new Vector();
    _1qrangequalifierProperty.addAll(propertys);
    for (int _i = 0; _i < _1qrangequalifierProperty.size(); _i++)
    { Property propertyx = (Property) _1qrangequalifierProperty.get(_i);
      if (propertyx != null && propertyx.getqualifier().contains(propertyxx))
      { removequalifier(propertyx,propertyxx); }
    }
    Vector _1qrangeparametersBehaviouralFeature = new Vector();
    _1qrangeparametersBehaviouralFeature.addAll(behaviouralfeatures);
    for (int _i = 0; _i < _1qrangeparametersBehaviouralFeature.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) _1qrangeparametersBehaviouralFeature.get(_i);
      if (behaviouralfeaturex != null && behaviouralfeaturex.getparameters().contains(propertyxx))
      { removeparameters(behaviouralfeaturex,propertyxx); }
    }
    Vector _1qrangeaccumulatorBinaryExpression = new Vector();
    _1qrangeaccumulatorBinaryExpression.addAll(binaryexpressions);
    for (int _i = 0; _i < _1qrangeaccumulatorBinaryExpression.size(); _i++)
    { BinaryExpression binaryexpressionx = (BinaryExpression) _1qrangeaccumulatorBinaryExpression.get(_i);
      if (binaryexpressionx != null && propertyxx.equals(binaryexpressionx.getaccumulator()))
      { _1removedaccumulatorBinaryExpression.add(binaryexpressionx);
        binaryexpressionx.setaccumulator(null);
      }
    }
    Vector _1qrangereferredPropertyBasicExpression = new Vector();
    _1qrangereferredPropertyBasicExpression.addAll(basicexpressions);
    for (int _i = 0; _i < _1qrangereferredPropertyBasicExpression.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _1qrangereferredPropertyBasicExpression.get(_i);
      if (basicexpressionx != null && basicexpressionx.getreferredProperty().contains(propertyxx))
      { removereferredProperty(basicexpressionx,propertyxx); }
    }
    Vector _2qrangeownedAttributeEntity = new Vector();
    _2qrangeownedAttributeEntity.add(propertyxx.getowner());
    for (int _i = 0; _i < _2qrangeownedAttributeEntity.size(); _i++)
    { Entity entityx = (Entity) _2qrangeownedAttributeEntity.get(_i);
      if (entityx != null && entityx.getownedAttribute().contains(propertyxx))
      { removeownedAttribute(entityx,propertyxx); }
    }
    Vector _1qrangeparametersUseCase = new Vector();
    _1qrangeparametersUseCase.addAll(usecases);
    for (int _i = 0; _i < _1qrangeparametersUseCase.size(); _i++)
    { UseCase usecasex = (UseCase) _1qrangeparametersUseCase.get(_i);
      if (usecasex != null && usecasex.getparameters().contains(propertyxx))
      { removeparameters(usecasex,propertyxx); }
    }
    for (int _i = 0; _i < _1removedaccumulatorBinaryExpression.size(); _i++)
    { killBinaryExpression((BinaryExpression) _1removedaccumulatorBinaryExpression.get(_i)); }
  }



  public void killAllStatement(List statementxx)
  { for (int _i = 0; _i < statementxx.size(); _i++)
    { killStatement((Statement) statementxx.get(_i)); }
  }

  public void killStatement(Statement statementxx)
  { if (statementxx == null) { return; }
   statements.remove(statementxx);
    Vector _1removedactivityBehaviouralFeature = new Vector();
    Vector _1removedactionCatchStatement = new Vector();
    Vector _1removedbodyFinalStatement = new Vector();
    Vector _1removedclassifierBehaviourUseCase = new Vector();
    Vector _1removedifPartConditionalStatement = new Vector();
    Vector _1removedbodyTryStatement = new Vector();
    Vector _1removedbodyLoopStatement = new Vector();
    Vector _1qrangeactivityBehaviouralFeature = new Vector();
    _1qrangeactivityBehaviouralFeature.addAll(behaviouralfeatures);
    for (int _i = 0; _i < _1qrangeactivityBehaviouralFeature.size(); _i++)
    { BehaviouralFeature behaviouralfeaturex = (BehaviouralFeature) _1qrangeactivityBehaviouralFeature.get(_i);
      if (behaviouralfeaturex != null && statementxx.equals(behaviouralfeaturex.getactivity()))
      { _1removedactivityBehaviouralFeature.add(behaviouralfeaturex);
        behaviouralfeaturex.setactivity(null);
      }
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
    Vector _1qrangebodyLoopStatement = new Vector();
    _1qrangebodyLoopStatement.addAll(loopstatements);
    for (int _i = 0; _i < _1qrangebodyLoopStatement.size(); _i++)
    { LoopStatement loopstatementx = (LoopStatement) _1qrangebodyLoopStatement.get(_i);
      if (loopstatementx != null && statementxx.equals(loopstatementx.getbody()))
      { _1removedbodyLoopStatement.add(loopstatementx);
        loopstatementx.setbody(null);
      }
    }
    statementstatIdindex.remove(statementxx.getstatId());
    for (int _i = 0; _i < _1removedactivityBehaviouralFeature.size(); _i++)
    { killAbstractBehaviouralFeature((BehaviouralFeature) _1removedactivityBehaviouralFeature.get(_i)); }
    for (int _i = 0; _i < _1removedactionCatchStatement.size(); _i++)
    { killCatchStatement((CatchStatement) _1removedactionCatchStatement.get(_i)); }
    for (int _i = 0; _i < _1removedbodyFinalStatement.size(); _i++)
    { killFinalStatement((FinalStatement) _1removedbodyFinalStatement.get(_i)); }
    for (int _i = 0; _i < _1removedclassifierBehaviourUseCase.size(); _i++)
    { killUseCase((UseCase) _1removedclassifierBehaviourUseCase.get(_i)); }
    for (int _i = 0; _i < _1removedifPartConditionalStatement.size(); _i++)
    { killConditionalStatement((ConditionalStatement) _1removedifPartConditionalStatement.get(_i)); }
    for (int _i = 0; _i < _1removedbodyTryStatement.size(); _i++)
    { killTryStatement((TryStatement) _1removedbodyTryStatement.get(_i)); }
    for (int _i = 0; _i < _1removedbodyLoopStatement.size(); _i++)
    { killAbstractLoopStatement((LoopStatement) _1removedbodyLoopStatement.get(_i)); }
  }

  public void killAbstractStatement(Statement statementxx)
  {
    if (statementxx instanceof ReturnStatement)
    { killReturnStatement((ReturnStatement) statementxx); }
    if (statementxx instanceof AssertStatement)
    { killAssertStatement((AssertStatement) statementxx); }
    if (statementxx instanceof ErrorStatement)
    { killErrorStatement((ErrorStatement) statementxx); }
    if (statementxx instanceof CatchStatement)
    { killCatchStatement((CatchStatement) statementxx); }
    if (statementxx instanceof FinalStatement)
    { killFinalStatement((FinalStatement) statementxx); }
    if (statementxx instanceof BreakStatement)
    { killBreakStatement((BreakStatement) statementxx); }
    if (statementxx instanceof OperationCallStatement)
    { killOperationCallStatement((OperationCallStatement) statementxx); }
    if (statementxx instanceof ImplicitCallStatement)
    { killImplicitCallStatement((ImplicitCallStatement) statementxx); }
    if (statementxx instanceof SequenceStatement)
    { killSequenceStatement((SequenceStatement) statementxx); }
    if (statementxx instanceof TryStatement)
    { killTryStatement((TryStatement) statementxx); }
    if (statementxx instanceof ConditionalStatement)
    { killConditionalStatement((ConditionalStatement) statementxx); }
    if (statementxx instanceof AssignStatement)
    { killAssignStatement((AssignStatement) statementxx); }
    if (statementxx instanceof CreationStatement)
    { killCreationStatement((CreationStatement) statementxx); }
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



  public void killAllAssertStatement(List assertstatementxx)
  { for (int _i = 0; _i < assertstatementxx.size(); _i++)
    { killAssertStatement((AssertStatement) assertstatementxx.get(_i)); }
  }

  public void killAssertStatement(AssertStatement assertstatementxx)
  { if (assertstatementxx == null) { return; }
   assertstatements.remove(assertstatementxx);
  killStatement(assertstatementxx);
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



  public void killAllUseCase(List usecasexx)
  { for (int _i = 0; _i < usecasexx.size(); _i++)
    { killUseCase((UseCase) usecasexx.get(_i)); }
  }

  public void killUseCase(UseCase usecasexx)
  { if (usecasexx == null) { return; }
   usecases.remove(usecasexx);
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



  public void killAllContinueStatement(List continuestatementxx)
  { for (int _i = 0; _i < continuestatementxx.size(); _i++)
    { killContinueStatement((ContinueStatement) continuestatementxx.get(_i)); }
  }

  public void killContinueStatement(ContinueStatement continuestatementxx)
  { if (continuestatementxx == null) { return; }
   continuestatements.remove(continuestatementxx);
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



  public void killAllTryStatement(List trystatementxx)
  { for (int _i = 0; _i < trystatementxx.size(); _i++)
    { killTryStatement((TryStatement) trystatementxx.get(_i)); }
  }

  public void killTryStatement(TryStatement trystatementxx)
  { if (trystatementxx == null) { return; }
   trystatements.remove(trystatementxx);
  killStatement(trystatementxx);
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



  public void killAllPrintcode(List printcodexx)
  { for (int _i = 0; _i < printcodexx.size(); _i++)
    { killPrintcode((Printcode) printcodexx.get(_i)); }
  }

  public void killPrintcode(Printcode printcodexx)
  { if (printcodexx == null) { return; }
   printcodes.remove(printcodexx);
  }




  
  
  public void printcode() 
  { 

       System.out.println("" + "import ocl");

      System.out.println("" + "import math");

      System.out.println("" + "import re");

      System.out.println("" + "import copy");

      System.out.println("" + "");

      System.out.println("" + "from mathlib import *");

      System.out.println("" + "from stringlib import *");

      System.out.println("" + "from oclfile import *");

      System.out.println("" + "from ocltype import *");

      System.out.println("" + "from ocldate import *");

      System.out.println("" + "from oclprocess import *");

      System.out.println("" + "from ocliterator import *");

      System.out.println("" + "from oclrandom import *");

      System.out.println("" + "from ocldatasource import *");

      System.out.println("" + "from sortedcontainers import *");

      System.out.println("" + "from enum import Enum");

      System.out.println("" + "");

      System.out.println("" + Entity.deleteOp());

      System.out.println("" + Entity.displayOps());

    List _range13 = new Vector();
  _range13.addAll(Controller.inst().enumerations);
  for (int _i12 = 0; _i12 < _range13.size(); _i12++)
  { Enumeration loopvar$0 = (Enumeration) _range13.get(_i12);
        Controller.inst().printcode2(loopvar$0);
  }
    List _range15 = new Vector();
  _range15.addAll(Controller.inst().entitys);
  for (int _i14 = 0; _i14 < _range15.size(); _i14++)
  { Entity loopvar$1 = (Entity) _range15.get(_i14);
        Controller.inst().printcode3(loopvar$1);
  }
    List _range17 = new Vector();
  _range17.addAll(Controller.inst().entitys);
  for (int _i16 = 0; _i16 < _range17.size(); _i16++)
  { Entity loopvar$2 = (Entity) _range17.get(_i16);
        Controller.inst().printcode4(loopvar$2);
  }
    List _range19 = new Vector();
  _range19.addAll(Controller.inst().entitys);
  for (int _i18 = 0; _i18 < _range19.size(); _i18++)
  { Entity loopvar$3 = (Entity) _range19.get(_i18);
        Controller.inst().printcode5(loopvar$3);
  }
    List _range21 = new Vector();
  _range21.addAll(Controller.inst().entitys);
  for (int _i20 = 0; _i20 < _range21.size(); _i20++)
  { Entity loopvar$4 = (Entity) _range21.get(_i20);
        Controller.inst().printcode6(loopvar$4);
  }
    List _range23 = new Vector();
  _range23.addAll(Controller.inst().entitys);
  for (int _i22 = 0; _i22 < _range23.size(); _i22++)
  { Entity loopvar$5 = (Entity) _range23.get(_i22);
        Controller.inst().printcode7outer(loopvar$5);
  }
    List _range25 = new Vector();
  _range25.addAll(Controller.inst().operations);
  for (int _i24 = 0; _i24 < _range25.size(); _i24++)
  { Operation loopvar$6 = (Operation) _range25.get(_i24);
        Controller.inst().printcode8(loopvar$6);
  }
    List _range27 = new Vector();
  _range27.addAll(Controller.inst().usecases);
  for (int _i26 = 0; _i26 < _range27.size(); _i26++)
  { UseCase loopvar$7 = (UseCase) _range27.get(_i26);
        Controller.inst().printcode9(loopvar$7);
  }

  }

  public static void main(String[] args)
  { Controller c = Controller.inst();

    java.util.Date d0 = new java.util.Date(); 
    long t0 = d0.getTime(); 

    c.loadModel("output/model.txt");  
    c.checkCompleteness(); 

    java.util.Date d1 = new java.util.Date(); 
    long t1 = d1.getTime(); 
  
    c.printcode(); 

    java.util.Date d2 = new java.util.Date(); 
    long t2 = d2.getTime();

    System.err.println(">>> Time for model load = " + (t1-t0) + " Time for transformation: " + (t2-t1));  
  }  


 
}



