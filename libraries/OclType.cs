using System;
using System.Collections;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Linq;
using System.Reflection;
using System.Diagnostics;
using System.Threading;
using System.Threading.Tasks;
using System.Xml.Serialization;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Data;
using System.Data.Common; 
using System.Data.SqlClient; 
using System.Net.Sockets;
    

   public class OclAttribute
    {
        string name = "";
        OclType type = null;

        public OclAttribute(string nme)
        { name = nme; }

        public void setType(OclType t)
        { type = t; }

        public string getName()
        { return name; }

        public OclType getType()
        { return type; }

        public override string ToString()
        {
            return name + " : " + type;
        }
    }

    public class OclOperation
    {
        string name = "";
        OclType type = null;
        ArrayList parameters = new ArrayList(); 

        public OclOperation(string nme)
        { name = nme; }

        public void setReturnType(OclType rt)
        { type = rt; }

        public void setParameters(ArrayList pars)
        { parameters = pars; }

        public string getName()
        { return name; }

        public OclType getType()
        { return type; }

        public OclType getReturnType()
        { return type; }

        public ArrayList getParameters()
        { return parameters; }

        public override string ToString()
        {
            string res = name + "("; 
            for (int i = 0; i < parameters.Count; i++)
            { OclAttribute par = (OclAttribute) parameters[i];
              res = res + par.getName() + " : " + par.getType(); 
              if (i < parameters.Count - 1)
              { res = res + ", "; }
            }
            return res + ") : " + type;
        }
    }

    public class OclType
    {
        string name = "";
        public Type actualMetatype = null;
        ArrayList attributes = new ArrayList();
        ArrayList operations = new ArrayList();
        ArrayList constructors = new ArrayList();
        ArrayList superclasses = new ArrayList(); 

        public static Hashtable ocltypenameindex = new Hashtable(); // String --> OclType

        static OclType intType = OclType.newOclType("int", 0.GetType());

        static OclType longType = OclType.newOclType("long", 0L.GetType());

        static OclType doubleType = OclType.newOclType("double", (0.0).GetType());

        static OclType booleanType = OclType.newOclType("boolean", typeof(bool));

        static OclType stringType = OclType.newOclType("String", "".GetType());

        static OclType voidType = OclType.newOclType("void", typeof(void));

        static OclType sequenceType = OclType.newOclType("Sequence", (new ArrayList()).GetType());

        static OclType setType = OclType.newOclType("Set", (new ArrayList()).GetType());

        static OclType mapType = OclType.newOclType("Map", (new Hashtable()).GetType());

        public static OclType newOclType(string nme)
        { return OclType.createOclType(nme); }

        public static OclType newOclType(string nme, Type typ)
        { OclType res = OclType.createOclType(nme);
          res.actualMetatype = typ;
          return res; 
        }


        public static OclType createOclType(string namex)
        {
            if (ocltypenameindex[namex] != null)
            { return (OclType)ocltypenameindex[namex]; }
            OclType ocltypex = new OclType();
            ocltypex.name = namex;
            ocltypenameindex[namex] = ocltypex;

            return ocltypex;
        }

        public static OclType getOclTypeByPK(string namex)
        { return (OclType)ocltypenameindex[namex]; }

        public static OclType getOclTypeByMetatype(Type t)
        {  foreach (string k in ocltypenameindex.Keys)
           { OclType tx = (OclType) ocltypenameindex[k]; 
             if (t.Equals(tx.actualMetatype))
             { return tx; }
           }
           return newOclType(t.Name, t); 
        }

        public void setMetatype(Type t)
        { actualMetatype = t; }

        public string getName()
        { return name; }

        public ArrayList getClasses()
        { return new ArrayList(); }

        public ArrayList getDeclaredClasses()
        { return new ArrayList(); }

        public OclType getComponentType()
        { Type[] etypes = actualMetatype.GenericTypeArguments; 
          if (etypes.Length > 0)
          { Type ctype = etypes[0];
            OclType etype = OclType.getOclTypeByMetatype(ctype);
            return etype;
          }
          return null; 
        }
        
        public ArrayList getFields()
        {
            if (actualMetatype != null)
            {
                attributes.Clear();
                FieldInfo[] fs =
                  actualMetatype.GetFields(BindingFlags.Public | BindingFlags.Static | BindingFlags.Instance | BindingFlags.NonPublic);
                for (int i = 0; i < fs.Length; i++)
                {
                    OclAttribute att = new OclAttribute(fs[i].Name);
                    Type t = fs[i].FieldType;
                    OclType ot = OclType.getOclTypeByMetatype(t);
                    att.setType(ot); 
                    attributes.Add(att);
                }
            }
            return attributes;
        }

        public ArrayList getDeclaredFields()
        { return getFields(); }

        public OclAttribute getField(string s)
        {
            if (actualMetatype != null)
            {
                FieldInfo[] fs =
                  actualMetatype.GetFields(BindingFlags.Public | BindingFlags.Static | BindingFlags.Instance | BindingFlags.NonPublic);
                for (int i = 0; i < fs.Length; i++)
                { if (s.Equals(fs[i].Name))
                  {
                    OclAttribute att = new OclAttribute(fs[i].Name);
                    Type t = fs[i].FieldType;
                    OclType ot = OclType.getOclTypeByMetatype(t);
                    att.setType(ot);
                    return att;
                  }
                }
            }
            return null;
        }

        public OclAttribute getDeclaredField(string s)
        { return getField(s); }

        public ArrayList getMethods()
        {
            if (actualMetatype != null)
            {
                operations.Clear();
                MethodInfo[] fs =
                  actualMetatype.GetMethods(BindingFlags.Public | BindingFlags.Static | BindingFlags.Instance | BindingFlags.NonPublic);
                for (int i = 0; i < fs.Length; i++)
                {
                    OclOperation opr = new OclOperation(fs[i].Name);
                    Type rt = fs[i].ReturnType;
                    OclType ort = OclType.getOclTypeByMetatype(rt);
                    opr.setReturnType(ort);
                    ParameterInfo[] parinf = fs[i].GetParameters();
                    ArrayList pars = new ArrayList();
                    for (int j = 0; j < parinf.Count(); j++)
                    {
                        string pname = parinf[j].Name;
                        Type ptype = parinf[j].ParameterType;
                        OclAttribute paratt = new OclAttribute(pname);
                        paratt.setType(OclType.getOclTypeByMetatype(ptype));
                        pars.Add(paratt);
                    }
                    opr.setParameters(pars); 
                    operations.Add(opr);
                }
            }
            return operations;
        }

        public ArrayList getDeclaredMethods()
        { return getMethods(); }

        public ArrayList getConstructors()
        {
            if (actualMetatype != null)
            {
                constructors.Clear();
                ConstructorInfo[] fs =
                  actualMetatype.GetConstructors(BindingFlags.Public | BindingFlags.Static | BindingFlags.Instance | BindingFlags.NonPublic);
                for (int i = 0; i < fs.Length; i++)
                {
                    OclOperation opr = new OclOperation(name);
                    opr.setReturnType(this);
                    ParameterInfo[] parinf = fs[i].GetParameters();
                    ArrayList pars = new ArrayList();
                    for (int j = 0; j < parinf.Count(); j++)
                    {
                        string pname = parinf[j].Name;
                        Type ptype = parinf[j].ParameterType;
                        OclAttribute paratt = new OclAttribute(pname);
                        paratt.setType(OclType.getOclTypeByMetatype(ptype));
                        pars.Add(paratt);
                    }
                    opr.setParameters(pars);
                    constructors.Add(opr);
                }
            }
            return constructors;
        }

        public OclType getSuperclass()
        {  if (actualMetatype != null && 
                actualMetatype.BaseType != null)
           { OclType sup = OclType.getOclTypeByMetatype(actualMetatype.BaseType);
             superclasses.Clear();
             superclasses.Add(sup);
             return sup; 
           }
           return null; 
        }

        public override string ToString()
        {
            if (actualMetatype != null)
            { return actualMetatype.ToString(); }
            return name;
        }

       public static bool hasAttribute(object obj, string att)
       { if (obj == null) 
         { return false; } 
         Type mt = obj.GetType(); 
         FieldInfo[] fs =
           mt.GetFields(BindingFlags.Public | 
                        BindingFlags.Static | 
                        BindingFlags.Instance | 
                        BindingFlags.NonPublic);
         for (int i = 0; i < fs.Length; i++)
         { if (att.Equals(fs[i].Name))
           { return true; }
         }
         return false; 
       } 

       public static object getAttributeValue(object obj, string att)
       { if (obj == null) 
         { return null; } 
         Type mt = obj.GetType(); 
         FieldInfo[] fs =
           mt.GetFields(BindingFlags.Public | 
                        BindingFlags.Static | 
                        BindingFlags.Instance | 
                        BindingFlags.NonPublic);
         for (int i = 0; i < fs.Length; i++)
         { if (att.Equals(fs[i].Name))
           { return fs[i].GetValue(obj); }
         }
         return null; 
       } 

       public static void setAttributeValue(
                object obj, string att, object value)
       { if (obj == null) 
         { return; } 
         Type mt = obj.GetType(); 
         FieldInfo[] fs =
           mt.GetFields(BindingFlags.Public | 
                        BindingFlags.Static | 
                        BindingFlags.Instance | 
                        BindingFlags.NonPublic);
         for (int i = 0; i < fs.Length; i++)
         { if (att.Equals(fs[i].Name))
           { fs[i].SetValue(obj,value); }
         }
       } 

       public bool isArray()
       { return name.Equals("Sequence"); }

       public bool isPrimitive() 
       { if (name.Equals("int") || 
             name.Equals("long") || 
             name.Equals("double") || 
             name.Equals("boolean"))
         { return true; } 
         return false; 
       }  

        public bool isInterface()
        {
            if (actualMetatype != null)
            { return actualMetatype.IsInterface; }
            return false;
        }

        public object newInstance()
        {
            if (actualMetatype != null)
            { MethodInfo mm = actualMetatype.GetMethod("new" + name);
                if (mm == null) { return null; }
                return mm.Invoke(null, null); 
            }
            return null;
        } 

        public bool isAssignableFrom(OclType c)
        {
            if (actualMetatype != null && c != null && c.actualMetatype != null)
            { return actualMetatype.IsAssignableFrom(c.actualMetatype); }
            return false;
        }

        public bool isInstance(object obj)
        {
            if (actualMetatype != null && obj != null)
            {
                return actualMetatype.IsInstanceOfType(obj);
            }
            else 
            { return false; }
        }

    }

