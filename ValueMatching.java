public class ValueMatching
{ Expression src;
  Expression trg;

  public ValueMatching(Expression s, Expression t)
  { src = s;
    trg = t;
  }

  public String toString()
  { return src + " |--> " + trg; }

  public ValueMatching invert()
  { return new ValueMatching(trg,src); }
} 
