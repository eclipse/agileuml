import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.lang.*;
import java.lang.reflect.*;
import java.util.StringTokenizer;
import java.io.*;

import java.util.function.Predicate;
import java.util.function.Function;


class MatrixLib_Aux 
{ 
  public static ArrayList<Integer> select_3(List<Integer> _l, ArrayList m)
  { // implements: s->select( i | 1 <= i & i <= m->size() )
    ArrayList<Integer> _results_3 = new ArrayList<Integer>();
    for (int _i = 0; _i < _l.size(); _i++)
    { int i = ((Integer) _l.get(_i)).intValue();
      if (1 <= i && i <= (m).size())
      { _results_3.add(i); }
    }
    return _results_3;
  }

  public static HashSet<Integer> select_3(HashSet<Integer> _l, ArrayList m)
  { // implements: s->select( i | 1 <= i & i <= m->size() )
    HashSet<Integer> _results_3 = new HashSet<Integer>();
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
      if (1 <= i && i <= (m).size())
      { _results_3.add(i); }
    }
    return _results_3;
  }

  public static TreeSet<Integer> select_3(TreeSet<Integer> _l, ArrayList m)
  { TreeSet<Integer> _results_3 = new TreeSet<Integer>();
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
      if (1 <= i && i <= (m).size())
      { _results_3.add(i); }
    }
    return _results_3;
  }

  public static HashSet<Integer> select_3(Set<Integer> _l, ArrayList m)
  { // implements: s->select( i | 1 <= i & i <= m->size() )
    HashSet<Integer> _results_3 = new HashSet<Integer>();
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
      if (1 <= i && i <= (m).size())
      { _results_3.add(i); }
    }
    return _results_3;
  }

  public static ArrayList<Integer> select_5(List<Integer> _l, ArrayList<ArrayList<Object>> m)
  { // implements: rows->select( i | 1 <= i & i <= m->size() )
    ArrayList<Integer> _results_5 = new ArrayList<Integer>();
    for (int _i = 0; _i < _l.size(); _i++)
    { int i = ((Integer) _l.get(_i)).intValue();
      if (1 <= i && i <= (m).size())
      { _results_5.add(i); }
    }
    return _results_5;
  }

  public static HashSet<Integer> select_5(HashSet<Integer> _l, ArrayList<ArrayList<Object>> m)
  { // implements: rows->select( i | 1 <= i & i <= m->size() )
    HashSet<Integer> _results_5 = new HashSet<Integer>();
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
      if (1 <= i && i <= (m).size())
      { _results_5.add(i); }
    }
    return _results_5;
  }

  public static TreeSet<Integer> select_5(TreeSet<Integer> _l, ArrayList<ArrayList<Object>> m)
  { TreeSet<Integer> _results_5 = new TreeSet<Integer>();
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
      if (1 <= i && i <= (m).size())
      { _results_5.add(i); }
    }
    return _results_5;
  }

  public static HashSet<Integer> select_5(Set<Integer> _l, ArrayList<ArrayList<Object>> m)
  { // implements: rows->select( i | 1 <= i & i <= m->size() )
    HashSet<Integer> _results_5 = new HashSet<Integer>();
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
      if (1 <= i && i <= (m).size())
      { _results_5.add(i); }
    }
    return _results_5;
  }

  public static ArrayList<Integer> select_7(List<Integer> _l, int col)
  { // implements: Integer.subrange(1,r->size())->select( j | j /= col )
    ArrayList<Integer> _results_7 = new ArrayList<Integer>();
    for (int _i = 0; _i < _l.size(); _i++)
    { int j = ((Integer) _l.get(_i)).intValue();
      if (j != col)
      { _results_7.add(j); }
    }
    return _results_7;
  }

  public static HashSet<Integer> select_7(HashSet<Integer> _l, int col)
  { // implements: Ocl.integerSubrange(1,(r).size())->select( j | j /= col )
    HashSet<Integer> _results_7 = new HashSet<Integer>();
    for (Integer _i : _l)
    { int j = ((Integer) _i).intValue();
      if (j != col)
      { _results_7.add(j); }
    }
    return _results_7;
  }

  public static TreeSet<Integer> select_7(TreeSet<Integer> _l, int col)
  { TreeSet<Integer> _results_7 = new TreeSet<Integer>();
    for (Integer _i : _l)
    { int j = ((Integer) _i).intValue();
      if (j != col)
      { _results_7.add(j); }
    }
    return _results_7;
  }

  public static HashSet<Integer> select_7(Set<Integer> _l, int col)
  { // implements: Ocl.integerSubrange(1,(r).size())->select( j | j /= col )
    HashSet<Integer> _results_7 = new HashSet<Integer>();
    for (Integer _i : _l)
    { int j = ((Integer) _i).intValue();
      if (j != col)
      { _results_7.add(j); }
    }
    return _results_7;
  }






  public static ArrayList<Double> collect_0(Collection<Integer> _l,ArrayList<Double> s,ArrayList<ArrayList<Double>> m,int i)
  { // Implements: Integer.subrange(1,m.size)->collect( k | s[k] * ( m[k]->at(i) ) )
   ArrayList<Double> _results_0 = new ArrayList<Double>();
    for (Integer _i : _l)
    { int k = ((Integer) _i).intValue();
     _results_0.add(new Double(s.get(k - 1) * m.get(k - 1).get(i - 1)));
    }
    return _results_0;
  }

  public static ArrayList<Double> collect_1(Collection<Integer> _l,ArrayList<ArrayList<Double>> m,ArrayList<Double> s)
  { // Implements: Integer.subrange(1,s.size)->collect( i | Integer.Sum(1,m.size,k,s[k] * ( m[k]->at(i) )) )
   ArrayList<Double> _results_1 = new ArrayList<Double>();
   ArrayList<Integer> rng = Ocl.integerSubrange(1,m.size()); 
   
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
     _results_1.add(new Double(Ocl.sumdouble(MatrixLib_Aux.collect_0(rng, s, m, i))));
    }
    return _results_1;
  }

  public static ArrayList<ArrayList<Double>> collect_2(Collection<ArrayList<Double>> _l,ArrayList<ArrayList<Double>> m2)
  { // Implements: m1->collect( row | MatrixLib.rowMult(row,m2) )
   ArrayList<ArrayList<Double>> _results_2 = new ArrayList<ArrayList<Double>>();
    for (ArrayList<Double> _i : _l)
    { ArrayList<Double> row = (ArrayList<Double>) _i;
     _results_2.add(MatrixLib.rowMult(row,m2));
    }
    return _results_2;
  }

  public static ArrayList<Object> collect_4(Collection<Integer> _l,ArrayList m)
  { // Implements: s->select( i | 1 <= i & i <= m->size() )->collect( j | m->at(j) )
   ArrayList<Object> _results_4 = new ArrayList<Object>();
    for (Integer _i : _l)
    { int j = ((Integer) _i).intValue();
     _results_4.add(((Object) m.get(j - 1)));
    }
    return _results_4;
  }

  public static ArrayList<ArrayList<Object>> collect_6(Collection<Integer> _l,ArrayList<ArrayList<Object>> m,ArrayList<Integer> cols)
  { // Implements: rows->select( i | 1 <= i & i <= m->size() )->collect( j | MatrixLib.subRows(m->at(j),cols) )
   ArrayList<ArrayList<Object>> _results_6 = new ArrayList<ArrayList<Object>>();
    for (Integer _i : _l)
    { int j = ((Integer) _i).intValue();
     _results_6.add(MatrixLib.subRows(((ArrayList) m.get(j - 1)),cols));
    }
    return _results_6;
  }

  public static ArrayList<Object> collect_8(Collection<Integer> _l,ArrayList r)
  { // Implements: Integer.subrange(1,r->size())->select( j | j /= col )->collect( j | r->at(j) )
   ArrayList<Object> _results_8 = new ArrayList<Object>();
    for (Integer _i : _l)
    { int j = ((Integer) _i).intValue();
     _results_8.add(((Object) r.get(j - 1)));
    }
    return _results_8;
  }

  public static ArrayList<Object> collect_9(Collection<Object> _l,int i)
  { // Implements: m->collect( r | r->oclAsType(Sequence)->at(i) )
   ArrayList<Object> _results_9 = new ArrayList<Object>();
    for (Object _i : _l)
    { Object r = (Object) _i;
     _results_9.add(((Object) ((ArrayList) r).get(i - 1)));
    }
    return _results_9;
  }

  public static ArrayList<Integer> collect_10(Collection<Object> _l)
  { // Implements: Sequence{}->collect( object_10_xx | 0 )
   ArrayList<Integer> _results_10 = new ArrayList<Integer>();
    for (Object _i : _l)
    { Object object_10_xx = (Object) _i;
     _results_10.add(new Integer(0));
    }
    return _results_10;
  }

  public static ArrayList<Object> collect_11(Collection<Integer> _l,Object x)
  { // Implements: Integer.subrange(1,sh->at(1))->collect( int_11_xx | x )
   ArrayList<Object> _results_11 = new ArrayList<Object>();
    for (Integer _i : _l)
    { int int_11_xx = ((Integer) _i).intValue();
     _results_11.add(x);
    }
    return _results_11;
  }

  public static ArrayList<ArrayList<Object>> collect_12(Collection<Integer> _l,ArrayList<Integer> sh,Object x)
  { // Implements: Integer.subrange(1,sh->at(1))->collect( int_12_xx | MatrixLib.singleValueMatrix(sh->tail(),x) )
   ArrayList<ArrayList<Object>> _results_12 = new ArrayList<ArrayList<Object>>();
    for (Integer _i : _l)
    { int int_12_xx = ((Integer) _i).intValue();
     _results_12.add(MatrixLib.singleValueMatrix(Ocl.tail(sh),x));
    }
    return _results_12;
  }

  public static ArrayList<Double> collect_13(Collection<Integer> _l,int i)
  { // Implements: Integer.subrange(1,n)->collect( j | if i = j then 1.0 else 0.0 endif )
   ArrayList<Double> _results_13 = new ArrayList<Double>();
    for (Integer _i : _l)
    { int j = ((Integer) _i).intValue();
     _results_13.add(new Double(((i == j) ? (1.0) : (0.0))));
    }
    return _results_13;
  }

  public static ArrayList<ArrayList<Double>> collect_14(Collection<Integer> _l,int n)
  { // Implements: Integer.subrange(1,n)->collect( i | Integer.subrange(1,n)->collect( j | if i = j then 1.0 else 0.0 endif ) )
   ArrayList<ArrayList<Double>> _results_14 = new ArrayList<ArrayList<Double>>();
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
     _results_14.add(MatrixLib_Aux.collect_13(Ocl.integerSubrange(1,n),i));
    }
    return _results_14;
  }

  public static ArrayList<ArrayList<Object>> collect_15(Collection<Object> _l,Function<Double, Double> f)
  { // Implements: m->collect( _r | MatrixLib.elementwiseApply(_r->oclAsType(Sequence),f) )
   ArrayList<ArrayList<Object>> _results_15 = new ArrayList<ArrayList<Object>>();
    for (Object _i : _l)
    { Object _r = (Object) _i;
     _results_15.add(MatrixLib.elementwiseApply(((ArrayList) _r),f));
    }
    return _results_15;
  }

  public static ArrayList<ArrayList<Object>> collect_selectElements(Collection<Object> _l,Function<Double, Boolean> f)
  { // Implements: m->collect( _r | MatrixLib.selectElements(_r->oclAsType(Sequence),f) )

   ArrayList<ArrayList<Object>> _results_15 = new ArrayList<ArrayList<Object>>();
    for (Object _i : _l)
    { Object _r = (Object) _i;
     _results_15.add(MatrixLib.selectElements(((ArrayList) _r),f));
    }
    return _results_15;
  }

  public static ArrayList<Double> collect_16(Collection<Object> _l)
  { // Implements: Sequence{}->collect( object_16_xx | 0.0 )
   ArrayList<Double> _results_16 = new ArrayList<Double>();
    for (Object _i : _l)
    { Object object_16_xx = (Object) _i;
     _results_16.add(new Double(0.0));
    }
    return _results_16;
  }

  public static ArrayList<ArrayList<Object>> collect_17(Collection<Object> _l,double x)
  { // Implements: m->collect( _r | MatrixLib.elementwiseMult(_r->oclAsType(Sequence),x) )
   ArrayList<ArrayList<Object>> _results_17 = new ArrayList<ArrayList<Object>>();
    for (Object _i : _l)
    { Object _r = (Object) _i;
     _results_17.add(MatrixLib.elementwiseMult(((ArrayList) _r),x));
    }
    return _results_17;
  }

  public static ArrayList<ArrayList<Object>> collect_18(Collection<Object> _l,double x)
  { // Implements: m->collect( _r | MatrixLib.elementwiseAdd(_r->oclAsType(Sequence),x) )
   ArrayList<ArrayList<Object>> _results_18 = new ArrayList<ArrayList<Object>>();
    for (Object _i : _l)
    { Object _r = (Object) _i;
     _results_18.add(MatrixLib.elementwiseAdd(((ArrayList) _r),x));
    }
    return _results_18;
  }

  public static ArrayList<ArrayList<Object>> collect_19(Collection<Object> _l,double x)
  { // Implements: m->collect( _r | MatrixLib.elementwiseDivide(_r->oclAsType(Sequence),x) )
   ArrayList<ArrayList<Object>> _results_19 = new ArrayList<ArrayList<Object>>();
    for (Object _i : _l)
    { Object _r = (Object) _i;
     _results_19.add(MatrixLib.elementwiseDivide(((ArrayList) _r),x));
    }
    return _results_19;
  }

  public static ArrayList<ArrayList<Object>> collect_20(Collection<Object> _l,double x)
  { // Implements: m->collect( _r | MatrixLib.elementwiseLess(_r->oclAsType(Sequence),x) )
   ArrayList<ArrayList<Object>> _results_20 = new ArrayList<ArrayList<Object>>();
    for (Object _i : _l)
    { Object _r = (Object) _i;
     _results_20.add(MatrixLib.elementwiseLess(((ArrayList) _r),x));
    }
    return _results_20;
  }

  public static ArrayList<Boolean> collect_21(Collection<Object> _l)
  { // Implements: Sequence{}->collect( object_21_xx | false )
   ArrayList<Boolean> _results_21 = new ArrayList<Boolean>();
    for (Object _i : _l)
    { Object object_21_xx = (Object) _i;
     _results_21.add(new Boolean(false));
    }
    return _results_21;
  }

  public static ArrayList<ArrayList<Object>> collect_22(Collection<Object> _l,double x)
  { // Implements: m->collect( _r | MatrixLib.elementwiseGreater(_r->oclAsType(Sequence),x) )
   ArrayList<ArrayList<Object>> _results_22 = new ArrayList<ArrayList<Object>>();
    for (Object _i : _l)
    { Object _r = (Object) _i;
     _results_22.add(MatrixLib.elementwiseGreater(((ArrayList) _r),x));
    }
    return _results_22;
  }

  public static ArrayList<ArrayList<Object>> collect_23(Collection<Object> _l,double x)
  { // Implements: m->collect( _r | MatrixLib.elementwiseEqual(_r->oclAsType(Sequence),x) )
   ArrayList<ArrayList<Object>> _results_23 = new ArrayList<ArrayList<Object>>();
    for (Object _i : _l)
    { Object _r = (Object) _i;
     _results_23.add(MatrixLib.elementwiseEqual(((ArrayList) _r),x));
    }
    return _results_23;
  }


}


class MatrixLib
{

  public MatrixLib()
  {

  }

  public Object clone()
  { MatrixLib result = new MatrixLib();
    return result;
  }



  public String toString()
  { String _res_ = "(MatrixLib) ";
    return _res_;
  }

    public static ArrayList<Double> rowMult(ArrayList<Double> s,ArrayList<ArrayList<Double>> m)
  {   ArrayList<Double> result = new ArrayList<Double>();
  
    result = MatrixLib_Aux.collect_1(Ocl.integerSubrange(1,s.size()),m,s);
  
    return result;
  }


    public static ArrayList<ArrayList<Double>> matrixMultiplication(ArrayList<ArrayList<Double>> m1,ArrayList<ArrayList<Double>> m2)
  {   ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
  
    result = MatrixLib_Aux.collect_2(m1,m2);
  
    return result;
  }


    public static ArrayList subRows(ArrayList m,ArrayList<Integer> s)
  {   ArrayList result = new ArrayList();
  
    result = MatrixLib_Aux.collect_4(MatrixLib_Aux.select_3(s,m),m);
  
    return result;
  }


    public static ArrayList subMatrix(ArrayList<ArrayList<Object>> m,ArrayList<Integer> rows,ArrayList<Integer> cols)
  {   ArrayList result = new ArrayList();
  
    result = MatrixLib_Aux.collect_6(MatrixLib_Aux.select_5(rows,m),m,cols);
  
    return result;
  }


    public static ArrayList matrixExcludingRowColumn(ArrayList m,int row,int col)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

    ArrayList<Integer> _range2 = new ArrayList<Integer>();
    _range2.addAll(Ocl.integerSubrange(1,(m).size()));
    for (int _i1 = 0; _i1 < _range2.size(); _i1++)
    { int i = ((Integer) _range2.get(_i1)).intValue();
    if (i != row)
{   ArrayList r = ((ArrayList<Object>) ((Object) m.get(i - 1)));

    ArrayList subrow = MatrixLib_Aux.collect_8(MatrixLib_Aux.select_7(Ocl.integerSubrange(1,(r).size()),col),r);

    res = Ocl.append(res,subrow);

 }
else {      }


  }
      return res;


  }


    public static ArrayList column(ArrayList m,int i)
  {   ArrayList result = new ArrayList();
  
  if ((((Object) m.get(1 - 1)) instanceof ArrayList)) 
  {     result = MatrixLib_Aux.collect_9(m,i);
 
  }  else
      if (true) 
  {     result = (ArrayList) (((Object) m.get(i - 1)));
 
  }     
    return result;
  }


    public static ArrayList<Integer> shape(Object x)
  { ArrayList<Integer> result;
  ArrayList<Integer> res = MatrixLib_Aux.collect_10((new ArrayList<Object>()));

  if ((x instanceof ArrayList))
{   ArrayList sq = ((ArrayList) x);

    res = Ocl.addSequence((new ArrayList<Integer>()), new Integer((sq).size()));
  if ((sq).size() > 0)
{   res = Ocl.union(res,MatrixLib.shape(((Object) sq.get(1 - 1)))); }
else {     return res; }


 }
else {     return res; }

      return res;


  }


    public static ArrayList singleValueMatrix(ArrayList<Integer> sh,Object x)
  { ArrayList result;
if ((sh).size() == 0)
{     return (new ArrayList<Object>()); }
else {      }

  if ((sh).size() == 1)
{     return MatrixLib_Aux.collect_11(Ocl.integerSubrange(1,sh.get(1 - 1)),x); }
else {      }

    ArrayList res = (new ArrayList<Object>());

    res = MatrixLib_Aux.collect_12(Ocl.integerSubrange(1,sh.get(1 - 1)),sh,x);
      return res;




  }


    public static ArrayList fillMatrixFrom(ArrayList<Double> sq,ArrayList<Integer> sh)
  { ArrayList result;
if ((sh).size() == 0)
{     return (new ArrayList<Object>()); }
else {      }

  if ((sh).size() == 1)
{     return Ocl.subrange(sq,1,sh.get(1 - 1)); }
else {      }

    ArrayList res = (new ArrayList<Object>());

    int prod = Ocl.prdint(Ocl.tail(sh));

    ArrayList<Integer> _range4 = new ArrayList<Integer>();
    _range4.addAll(Ocl.integerSubrange(1,sh.get(1 - 1)));
    for (int _i3 = 0; _i3 < _range4.size(); _i3++)
    { int i = ((Integer) _range4.get(_i3)).intValue();
      ArrayList rowi = MatrixLib.fillMatrixFrom(Ocl.subrange(sq,1 + prod * ( i - 1 ),prod * i),Ocl.tail(sh));

    res = Ocl.append(res,rowi);


  }
      return res;





  }


    public static ArrayList<ArrayList<Double>> identityMatrix(int n)
  {   ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
  
    result = MatrixLib_Aux.collect_14(Ocl.integerSubrange(1,n),n);
  
    return result;
  }


    public static ArrayList flattenMatrix(ArrayList m)
  { ArrayList result;
if ((m).size() == 0)
{     return (new ArrayList<Object>()); }
else {      }

  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{   ArrayList sq = ((ArrayList<Object>) ((Object) m.get(1 - 1)));

      return Ocl.union(MatrixLib.flattenMatrix(sq),MatrixLib.flattenMatrix(Ocl.tail(m)));

 }
else {      }

      return m;


  }


    public static double sumMatrix(ArrayList m)
  { double result;
if ((m).size() == 0)
{     return 0.0; }
else {      }

  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{   ArrayList sq = ((ArrayList<Object>) ((Object) m.get(1 - 1)));

      return MatrixLib.sumMatrix(sq) + MatrixLib.sumMatrix(Ocl.tail(m));

 }
else {      }

    ArrayList<Double> dmat = Ocl.concatenate(Ocl.addSequence((new ArrayList<Double>()), new Double(0.0)), m);

      return Ocl.sumdouble(dmat);



  }


    public static double prdMatrix(ArrayList m)
  { double result;
if ((m).size() == 0)
{     return 1.0; }
else {      }

  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{   ArrayList sq = ((ArrayList<Object>) ((Object) m.get(1 - 1)));

      return MatrixLib.prdMatrix(sq) * MatrixLib.prdMatrix(Ocl.tail(m));

 }

    ArrayList<Double> dmat = Ocl.concatenate(Ocl.addSequence((new ArrayList<Double>()), new Double(1.0)), m);

      return Ocl.prddouble(dmat);
    }



    public static ArrayList selectElements(ArrayList m,Function<Double, Boolean> f)
  { ArrayList result;
    if ((m).size() == 0)
    { return (new ArrayList<Double>()); }
    
    if ((((Object) m.get(1 - 1)) instanceof ArrayList))
    { return MatrixLib_Aux.collect_selectElements(m,f); }

    ArrayList<Double> dmat = new ArrayList<Double>();

    ArrayList<Object> _range7 = new ArrayList<Object>();
    _range7.addAll(m);
    for (int _i6 = 0; _i6 < _range7.size(); _i6++)
    { Object x = (Object) _range7.get(_i6);
      double y = (double) x;
      if (f.apply(y))
      { dmat = Ocl.append(dmat,y); }
    }
    return dmat;
  }


    public static ArrayList elementwiseApply(ArrayList m,Function<Double, Double> f)
  { ArrayList result;
    if ((m).size() == 0)
    { return (new ArrayList<Double>()); }

    if ((((Object) m.get(1 - 1)) instanceof ArrayList))
    { return MatrixLib_Aux.collect_15(m,f); }

    ArrayList<Double> dmat = MatrixLib_Aux.collect_16((new ArrayList<Object>()));

    ArrayList<Object> _range7 = new ArrayList<Object>();
    _range7.addAll(m);
    for (int _i6 = 0; _i6 < _range7.size(); _i6++)
    { Object x = (Object) _range7.get(_i6);
      double y = ((double) x);

      dmat = Ocl.append(dmat,(f).apply(y));
    }
    return dmat;
  }

    public static ArrayList elementwiseMult(ArrayList m,double x)
  { ArrayList result;
if ((m).size() == 0)
{     return (new ArrayList<Object>()); }


  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{     return MatrixLib_Aux.collect_17(m,x); }


    ArrayList<Double> dmat = MatrixLib_Aux.collect_16((new ArrayList<Object>()));

    ArrayList<Object> _range9 = new ArrayList<Object>();
    _range9.addAll(m);
    for (int _i8 = 0; _i8 < _range9.size(); _i8++)
    { Object z = (Object) _range9.get(_i8);
      double y = ((double) z);

    dmat = Ocl.append(dmat,y * x);


  }
      return dmat;




  }


    public static ArrayList elementwiseAdd(ArrayList m,double x)
  { ArrayList result;
if ((m).size() == 0)
{     return (new ArrayList<Object>()); }
else {      }

  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{     return MatrixLib_Aux.collect_18(m,x); }
else {      }

    ArrayList<Double> dmat = MatrixLib_Aux.collect_16((new ArrayList<Object>()));

    ArrayList<Object> _range11 = new ArrayList<Object>();
    _range11.addAll(m);
    for (int _i10 = 0; _i10 < _range11.size(); _i10++)
    { Object z = (Object) _range11.get(_i10);
      double y = ((double) z);

    dmat = Ocl.append(dmat,y + x);


  }
      return dmat;




  }


    public static ArrayList elementwiseDivide(ArrayList m,double x)
  { ArrayList result;
if ((m).size() == 0)
{     return (new ArrayList<Object>()); }
else {      }

  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{     return MatrixLib_Aux.collect_19(m,x); }
else {      }

    ArrayList<Double> dmat = MatrixLib_Aux.collect_16((new ArrayList<Object>()));

    ArrayList<Object> _range13 = new ArrayList<Object>();
    _range13.addAll(m);
    for (int _i12 = 0; _i12 < _range13.size(); _i12++)
    { Object z = (Object) _range13.get(_i12);
      double y = ((double) z);

    dmat = Ocl.append(dmat,y / x);


  }
      return dmat;




  }


    public static ArrayList elementwiseLess(ArrayList m,double x)
  { ArrayList result;
if ((m).size() == 0)
{     return (new ArrayList<Object>()); }
else {      }

  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{     return MatrixLib_Aux.collect_20(m,x); }
else {      }

    ArrayList<Boolean> dmat = MatrixLib_Aux.collect_21((new ArrayList<Object>()));

    ArrayList<Object> _range15 = new ArrayList<Object>();
    _range15.addAll(m);
    for (int _i14 = 0; _i14 < _range15.size(); _i14++)
    { Object z = (Object) _range15.get(_i14);
      double y = ((double) z);

    dmat = Ocl.append(dmat,((y < x) ? (true) : (false)));


  }
      return dmat;




  }


    public static ArrayList elementwiseGreater(ArrayList m,double x)
  { ArrayList result;
if ((m).size() == 0)
{     return (new ArrayList<Object>()); }
else {      }

  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{     return MatrixLib_Aux.collect_22(m,x); }
else {      }

    ArrayList<Boolean> dmat = MatrixLib_Aux.collect_21((new ArrayList<Object>()));

    ArrayList<Object> _range17 = new ArrayList<Object>();
    _range17.addAll(m);
    for (int _i16 = 0; _i16 < _range17.size(); _i16++)
    { Object z = (Object) _range17.get(_i16);
      double y = ((double) z);

    dmat = Ocl.append(dmat,((y > x) ? (true) : (false)));


  }
      return dmat;




  }


    public static ArrayList elementwiseEqual(ArrayList m,double x)
  { ArrayList result;
if ((m).size() == 0)
{     return (new ArrayList<Object>()); }
else {      }

  if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{     return MatrixLib_Aux.collect_23(m,x); }
else {      }

    ArrayList<Boolean> dmat = MatrixLib_Aux.collect_21((new ArrayList<Object>()));

    ArrayList<Object> _range19 = new ArrayList<Object>();
    _range19.addAll(m);
    for (int _i18 = 0; _i18 < _range19.size(); _i18++)
    { Object z = (Object) _range19.get(_i18);
      double y = ((double) z);

    dmat = Ocl.append(dmat,((x == y) ? (true) : (false)));


  }
      return dmat;




  }


    public static double detaux(double x1,double x2,double y1,double y2)
  {   double result = 0.0;
  
    result = x1 * y2 - x2 * y1;
  
    return result;
  }


    public static double determinant2(ArrayList<ArrayList<Object>> m)
  {   double result = 0.0;
    if ((m).size() != 2 || (m.get(0)).size() != 2) { return result; } 
    
    result = MatrixLib.detaux(((double) ((Object) m.get(0).get(1 - 1))),((double) ((Object) m.get(0).get(2 - 1))),((double) ((Object) m.get(1).get(1 - 1))),((double) ((Object) m.get(1).get(2 - 1))));
  
    return result;
  }


    public static double determinant3(ArrayList<ArrayList<Object>> m)
  { double result;
  ArrayList<ArrayList<Object>> subm1 = MatrixLib.subMatrix(m,Ocl.addSequence(Ocl.addSequence((new ArrayList<Integer>()), new Integer(2)), new Integer(3)),Ocl.addSequence(Ocl.addSequence((new ArrayList<Integer>()), new Integer(2)), new Integer(3)));

    ArrayList<ArrayList<Object>> subm2 = MatrixLib.subMatrix(m,Ocl.addSequence(Ocl.addSequence((new ArrayList<Integer>()), new Integer(2)), new Integer(3)),Ocl.addSequence(Ocl.addSequence((new ArrayList<Integer>()), new Integer(1)), new Integer(3)));

    ArrayList<ArrayList<Object>> subm3 = MatrixLib.subMatrix(m,Ocl.addSequence(Ocl.addSequence((new ArrayList<Integer>()), new Integer(2)), new Integer(3)),Ocl.addSequence(Ocl.addSequence((new ArrayList<Integer>()), new Integer(1)), new Integer(2)));

      return ((double) ((Object) m.get(0).get(1 - 1))) * MatrixLib.determinant2(subm1) - ((double) ((Object) m.get(0).get(2 - 1))) * MatrixLib.determinant2(subm2) + ((double) ((Object) m.get(0).get(3 - 1))) * MatrixLib.determinant2(subm3);



  }


    public static double determinant(ArrayList m)
  { double result;
  int n = (m).size();

  if (n == 1)
{     return ((double) ((Object) m.get(1 - 1))); }
else {      }

  if (n == 2)
{     return MatrixLib.determinant2(m); }
else {      }

  if (n == 3)
{     return MatrixLib.determinant3(m); }
else {      }

    double res = 0.0;

    ArrayList row = ((ArrayList<Object>) ((Object) m.get(1 - 1)));

    int factor = 1;

    ArrayList<Integer> _range21 = new ArrayList<Integer>();
    _range21.addAll(Ocl.integerSubrange(1,n));
    for (int _i20 = 0; _i20 < _range21.size(); _i20++)
    { int i = ((Integer) _range21.get(_i20)).intValue();
      ArrayList submat = MatrixLib.matrixExcludingRowColumn(m,1,i);

    double det = MatrixLib.determinant(submat);

    double rowi = ((double) ((Object) row.get(i - 1)));

    res = res + factor * rowi * det;
    factor = -factor;





  }
      return res;








  }


    public static ArrayList rowAddition(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

  if ((((Object) m1.get(1 - 1)) instanceof ArrayList))
{   ArrayList<Integer> _range23 = new ArrayList<Integer>();
    _range23.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i22 = 0; _i22 < _range23.size(); _i22++)
    { int i = ((Integer) _range23.get(_i22)).intValue();
      ArrayList sq1 = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList sq2 = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowAddition(sq1,sq2));



  }
      return res;
 }
else {      }

    ArrayList<Integer> _range25 = new ArrayList<Integer>();
    _range25.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i24 = 0; _i24 < _range25.size(); _i24++)
    { int j = ((Integer) _range25.get(_i24)).intValue();
      double m1j = ((double) ((Object) m1.get(j - 1)));

    double m2j = ((double) ((Object) m2.get(j - 1)));

    res = Ocl.append(res,m1j + m2j);



  }
      return res;



  }


    public static ArrayList matrixAddition(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

    ArrayList<Integer> _range27 = new ArrayList<Integer>();
    _range27.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i26 = 0; _i26 < _range27.size(); _i26++)
    { int i = ((Integer) _range27.get(_i26)).intValue();
      ArrayList sq1 = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList sq2 = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowAddition(sq1,sq2));



  }
      return res;


  }


    public static ArrayList rowSubtraction(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

  if ((((Object) m1.get(1 - 1)) instanceof ArrayList))
{   ArrayList<Integer> _range29 = new ArrayList<Integer>();
    _range29.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i28 = 0; _i28 < _range29.size(); _i28++)
    { int i = ((Integer) _range29.get(_i28)).intValue();
      ArrayList sq1 = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList sq2 = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowSubtraction(sq1,sq2));



  }
      return res;
 }
else {      }

    ArrayList<Integer> _range31 = new ArrayList<Integer>();
    _range31.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i30 = 0; _i30 < _range31.size(); _i30++)
    { int j = ((Integer) _range31.get(_i30)).intValue();
      double m1j = ((double) ((Object) m1.get(j - 1)));

    double m2j = ((double) ((Object) m2.get(j - 1)));

    res = Ocl.append(res,m1j - m2j);



  }
      return res;



  }


    public static ArrayList matrixSubtraction(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

    ArrayList<Integer> _range33 = new ArrayList<Integer>();
    _range33.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i32 = 0; _i32 < _range33.size(); _i32++)
    { int i = ((Integer) _range33.get(_i32)).intValue();
      ArrayList sq1 = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList sq2 = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowSubtraction(sq1,sq2));



  }
      return res;


  }


    public static ArrayList rowDotProduct(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

  if ((((Object) m1.get(1 - 1)) instanceof ArrayList))
{   ArrayList<Integer> _range35 = new ArrayList<Integer>();
    _range35.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i34 = 0; _i34 < _range35.size(); _i34++)
    { int i = ((Integer) _range35.get(_i34)).intValue();
      ArrayList sq1 = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList sq2 = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowDotProduct(sq1,sq2));



  }
      return res;
 }
else {      }

    ArrayList<Integer> _range37 = new ArrayList<Integer>();
    _range37.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i36 = 0; _i36 < _range37.size(); _i36++)
    { int j = ((Integer) _range37.get(_i36)).intValue();
      double m1j = ((double) ((Object) m1.get(j - 1)));

    double m2j = ((double) ((Object) m2.get(j - 1)));

    res = Ocl.append(res,m1j * m2j);



  }
      return res;



  }


    public static ArrayList dotProduct(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

    ArrayList<Integer> _range39 = new ArrayList<Integer>();
    _range39.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i38 = 0; _i38 < _range39.size(); _i38++)
    { int i = ((Integer) _range39.get(_i38)).intValue();
      ArrayList sq1 = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList sq2 = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowDotProduct(sq1,sq2));



  }
      return res;


  }


    public static ArrayList rowDotDivision(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

  if ((((Object) m1.get(1 - 1)) instanceof ArrayList))
{   ArrayList<Integer> _range41 = new ArrayList<Integer>();
    _range41.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i40 = 0; _i40 < _range41.size(); _i40++)
    { int i = ((Integer) _range41.get(_i40)).intValue();
      ArrayList m1i = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList m2i = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowDotDivision(m1i,m2i));



  }
      return res;
 }
else {      }

    ArrayList<Integer> _range43 = new ArrayList<Integer>();
    _range43.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i42 = 0; _i42 < _range43.size(); _i42++)
    { int j = ((Integer) _range43.get(_i42)).intValue();
      double m1j = ((double) ((Object) m1.get(j - 1)));

    double m2j = ((double) ((Object) m2.get(j - 1)));

    res = Ocl.append(res,m1j / m2j);



  }
      return res;



  }


    public static ArrayList dotDivision(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

    ArrayList<Integer> _range45 = new ArrayList<Integer>();
    _range45.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i44 = 0; _i44 < _range45.size(); _i44++)
    { int i = ((Integer) _range45.get(_i44)).intValue();
      ArrayList sq1 = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList sq2 = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowDotDivision(sq1,sq2));



  }
      return res;


  }


    public static ArrayList rowLess(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

  if ((((Object) m1.get(1 - 1)) instanceof ArrayList))
{   ArrayList<Integer> _range47 = new ArrayList<Integer>();
    _range47.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i46 = 0; _i46 < _range47.size(); _i46++)
    { int i = ((Integer) _range47.get(_i46)).intValue();
      ArrayList m1i = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList m2i = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowLess(m1i,m2i));



  }
      return res;
 }
else {      }

    ArrayList<Integer> _range49 = new ArrayList<Integer>();
    _range49.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i48 = 0; _i48 < _range49.size(); _i48++)
    { int j = ((Integer) _range49.get(_i48)).intValue();
      double m1j = ((double) ((Object) m1.get(j - 1)));

    double m2j = ((double) ((Object) m2.get(j - 1)));

    res = Ocl.append(res,((m1j < m2j) ? (true) : (false)));



  }
      return res;



  }


    public static ArrayList matrixLess(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

    ArrayList<Integer> _range51 = new ArrayList<Integer>();
    _range51.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i50 = 0; _i50 < _range51.size(); _i50++)
    { int i = ((Integer) _range51.get(_i50)).intValue();
      ArrayList m1i = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList m2i = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowLess(m1i,m2i));



  }
      return res;


  }


    public static ArrayList rowGreater(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

  if ((((Object) m1.get(1 - 1)) instanceof ArrayList))
{   ArrayList<Integer> _range53 = new ArrayList<Integer>();
    _range53.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i52 = 0; _i52 < _range53.size(); _i52++)
    { int i = ((Integer) _range53.get(_i52)).intValue();
      ArrayList m1i = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList m2i = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowGreater(m1i,m2i));



  }
      return res;
 }
else {      }

    ArrayList<Integer> _range55 = new ArrayList<Integer>();
    _range55.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i54 = 0; _i54 < _range55.size(); _i54++)
    { int j = ((Integer) _range55.get(_i54)).intValue();
      double m1j = ((double) ((Object) m1.get(j - 1)));

    double m2j = ((double) ((Object) m2.get(j - 1)));

    res = Ocl.append(res,((m1j > m2j) ? (true) : (false)));



  }
      return res;



  }


    public static ArrayList matrixGreater(ArrayList m1,ArrayList m2)
  { ArrayList result;
  ArrayList res = (new ArrayList<Object>());

    ArrayList<Integer> _range57 = new ArrayList<Integer>();
    _range57.addAll(Ocl.integerSubrange(1,(m1).size()));
    for (int _i56 = 0; _i56 < _range57.size(); _i56++)
    { int i = ((Integer) _range57.get(_i56)).intValue();
      ArrayList r1 = ((ArrayList<Object>) ((Object) m1.get(i - 1)));

    ArrayList r2 = ((ArrayList<Object>) ((Object) m2.get(i - 1)));

    res = Ocl.append(res,MatrixLib.rowGreater(r1,r2));



  }
      return res;


  }


    public static ArrayList transpose(ArrayList m)
  { ArrayList result;
if ((((Object) m.get(1 - 1)) instanceof ArrayList))
{      }
else {     return m; }

    ArrayList res = (new ArrayList<Object>());

    ArrayList<Integer> _range59 = new ArrayList<Integer>();
    _range59.addAll(Ocl.integerSubrange(1,(m).size()));
    for (int _i58 = 0; _i58 < _range59.size(); _i58++)
    { int i = ((Integer) _range59.get(_i58)).intValue();
      res = Ocl.append(res,MatrixLib.column(m,i));
  }
      return res;



  }


public static void main(String[] args)
 { ArrayList sh = new ArrayList(); 
   sh.add(2); sh.add(3); 
   ArrayList mtrx1 = MatrixLib.singleValueMatrix(sh, 5.0); 
   System.out.println("Single value matrix 5: " + mtrx1);
   
   ArrayList mtrx2 = MatrixLib.singleValueMatrix(sh, 3.5); 
   System.out.println("Single value matrix 3.5: " + mtrx2); 
   
   ArrayList gtr = MatrixLib.matrixGreater(mtrx1, mtrx2); 
   System.out.println("m1 > m2: " + gtr); 
   
   ArrayList lss = MatrixLib.matrixLess(mtrx1, mtrx2); 
   System.out.println("m1 < m2: " + lss); 
 
   
   ArrayList<Double> values = new ArrayList<Double>(); 
   values.add(1.0); values.add(2.0); values.add(3.0); 
   values.add(4.0); values.add(5.0); values.add(6.0); 
    
   ArrayList pp = MatrixLib.fillMatrixFrom(values, sh); 
   System.out.println("filled matrix: " + pp); 
   
   ArrayList rr = MatrixLib.elementwiseLess(pp,4.5); 
   System.out.println("filled matrix < 4.5: " + rr); 

   ArrayList ats = MatrixLib.matrixAddition(mtrx1, pp); 
   System.out.println("m1 + filled matrix: " + ats); 

   ArrayList sts = MatrixLib.matrixSubtraction(mtrx1, pp); 
   System.out.println("m1 - filled matrix: " + sts); 
   
   ArrayList mts = MatrixLib.dotProduct(mtrx1, pp); 
   System.out.println("m1 . filled matrix: " + mts); 

   ArrayList dts = MatrixLib.dotDivision(mtrx1, pp); 
   System.out.println("m1 / filled matrix: " + dts); 

   ArrayList sphe = MatrixLib.shape(mtrx1); 
   System.out.println("shape of m1: " + sphe); 

   ArrayList col1 = MatrixLib.column(pp, 1); 
   ArrayList col2 = MatrixLib.column(pp, 2); 
   ArrayList col3 = MatrixLib.column(pp, 3); 
   System.out.println("Columns: " + col1 + " " + col2 + " " + col3); 
   
   ArrayList rws = new ArrayList(); 
   rws.add(1); rws.add(2); 
   
   ArrayList submat = MatrixLib.subMatrix(pp, rws, rws); 
   System.out.println("Submatrix: " + submat); 
   
   ArrayList id = MatrixLib.identityMatrix(2); 
   System.out.println("Identity matrix " + id); 
   
   ArrayList<ArrayList<Double>> m1 = new ArrayList<ArrayList<Double>>(); 
   m1.addAll(submat); 
   
   ArrayList<ArrayList<Double>> m2 = new ArrayList<ArrayList<Double>>(); 
   m2.addAll(id);
   
   ArrayList mult1 = MatrixLib.matrixMultiplication(m1,m2); 
   System.out.println("Mult m1*id " + mult1);  
   
   ArrayList mult2 = MatrixLib.matrixMultiplication(m1,m1); 
   System.out.println("Mult m1*m1 " + mult2);  

   ArrayList mult3 = MatrixLib.matrixMultiplication(m2,m1); 
   System.out.println("Mult id*m1 " + mult3);  
   
   double dsum = MatrixLib.sumMatrix(pp); 
   System.out.println("Matrix sum " + dsum);  
   
   double dprd = MatrixLib.prdMatrix(pp); 
   System.out.println("Matrix prd " + dprd);  
   
   ArrayList rrxx = MatrixLib.elementwiseAdd(pp,4.5); 
   System.out.println("filled matrix + 4.5: " + rrxx); 

   ArrayList rryy = MatrixLib.elementwiseDivide(pp,4.5); 
   System.out.println("filled matrix / 4.5: " + rryy); 

   ArrayList rrzz = MatrixLib.elementwiseMult(pp,4.5); 
   System.out.println("filled matrix * 4.5: " + rrzz); 
   
   Function<Double,Double> df = (x) -> { return Math.sin(x); }; 
   
   ArrayList sins = MatrixLib.elementwiseApply(rryy, df); 
   System.out.println("Applied matrix: " + sins); 


   ArrayList ff = MatrixLib.flattenMatrix(pp); 
   System.out.println("Flattened matrix: " + ff); 

   double det = MatrixLib.determinant(submat); 
   System.out.println("Matrix determinant " + det);  

   ArrayList transp = MatrixLib.transpose(submat); 
   System.out.println("Matrix transpose " + transp);  

}

/* Expected results: 

Single value matrix 5: [[5.0, 5.0, 5.0], [5.0, 5.0, 5.0]]
Single value matrix 3.5: [[3.5, 3.5, 3.5], [3.5, 3.5, 3.5]]
m1 > m2: [[true, true, true], [true, true, true]]
m1 < m2: [[false, false, false], [false, false, false]]
filled matrix: [[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]]
filled matrix < 4.5: [[true, true, true], [true, false, false]]
m1 + filled matrix: [[6.0, 7.0, 8.0], [9.0, 10.0, 11.0]]
m1 - filled matrix: [[4.0, 3.0, 2.0], [1.0, 0.0, -1.0]]
m1 . filled matrix: [[5.0, 10.0, 15.0], [20.0, 25.0, 30.0]]
m1 / filled matrix: [[5.0, 2.5, 1.6666666666666667], [1.25, 1.0, 0.8333333333333334]]

shape of m1: [2, 3]

Columns: [1.0, 4.0] [2.0, 5.0] [3.0, 6.0]

Submatrix: [[1.0, 2.0], [4.0, 5.0]]

Identity matrix [[1.0, 0.0], [0.0, 1.0]]

Mult m1*id [[1.0, 2.0], [4.0, 5.0]]

Mult m1*m1 [[9.0, 12.0], [24.0, 33.0]]

Mult id*m1 [[1.0, 2.0], [4.0, 5.0]]

Matrix sum 21.0

Matrix prd 720.0

filled matrix + 4.5: [[5.5, 6.5, 7.5], [8.5, 9.5, 10.5]]

filled matrix / 4.5: [[0.2222222222222222, 0.4444444444444444, 0.6666666666666666], [0.8888888888888888, 1.1111111111111112, 1.3333333333333333]]

filled matrix * 4.5: [[4.5, 9.0, 13.5], [18.0, 22.5, 27.0]]

Applied matrix: [[0.22039774345612226, 0.42995636352835553, 0.618369803069737], [0.7763719213006605, 0.8961922010299563, 0.9719379013633127]]

Flattened matrix: [1.0, 2.0, 3.0, 4.0, 5.0, 6.0]

Matrix determinant -3.0

Matrix transpose [[1.0, 4.0], [2.0, 5.0]]
  */ 
     
}


