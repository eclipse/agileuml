public abstract class Action extends ExecutableNode
{ Expression localPrecondition;  // not used
  Expression localPostcondition; // not used

  public Action(String nme) 
  { super(nme); } 
} 
