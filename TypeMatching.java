import java.util.Vector;
import java.util.Set;
import java.util.HashSet;


public class TypeMatching
{ // Created from the enum-enum matchings etc, and
  // used to generate data-conversion functions in every MT language

  Type src;
  Type trg;
  Vector valueMappings = new Vector();

  public TypeMatching(Type s, Type t)
  { src = s;
    trg = t;
  }

  public void addValueMapping(Expression s, Expression t)
  { ValueMatching v = new ValueMatching(s,t);
    valueMappings.add(v);
  }

  public void addValueMapping(ValueMatching vm)
  { 
    valueMappings.add(vm);
  }

  public String toString()
  { String res = src + " |--> " + trg + "\n";
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      res = res + "  " + vm + "\n";
    }
    return res;
  }

  public boolean isBijective()
  { Set domain = new HashSet();
    Set range = new HashSet();
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      domain.add(vm.src + "");
      range.add(vm.trg + "");
    }
    if (domain.size() == range.size() &&
        domain.size() == valueMappings.size())
    { return true; }
    return false;
  }  

  public TypeMatching invert()
  { TypeMatching res = new TypeMatching(trg,src);
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      ValueMatching ivm = vm.invert();
      res.addValueMapping(ivm);
    }
    return res;
  }
}
