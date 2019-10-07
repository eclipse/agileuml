import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: QVT */ 

abstract public class QVTRule extends NamedElement
{ Vector domain = new Vector(); // of Domain
  RelationalTransformation transformation;
  boolean isAbstract = false; 
  // QVTRule overrides = null; 
  String soverrides = null; 
  QVTRule overrides = null; 

  public QVTRule(String nme, RelationalTransformation t)
  { super(nme);
    transformation = t; 
  }

  public void addDomain(Domain d)
  { domain.add(d); }

  public void setDomain(Vector domains)
  { domain = domains; }

  public void setIsAbstract(boolean b) 
  { isAbstract = b; } 

  public boolean isAbstract() 
  { return isAbstract; } 

  public boolean isConcrete() 
  { return isAbstract == false; }

  public boolean overrides()
  { return soverrides != null; }  

  public void setOverrides(String superRule) 
  { soverrides = superRule; } 

  public abstract boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env);

  public abstract void addTraceEntity(Vector entities, Vector assocs); 

  public abstract Vector targetEntities(); 

  public abstract Vector targetAttributes();

  public abstract Vector sourceAttributes();

  public abstract Expression checkTrace(Entity e, Attribute att);  

  public abstract Expression checkTraceEmpty(Entity e, Attribute att);  

  public abstract Vector toConstraints(UseCase uc, Vector entities, Vector types);

  /* public abstract Constraint toInverseConstraint(Vector entities, Vector types); */ 

  public abstract Vector toPreservationConstraints(UseCase uc, Vector entities, Vector types); 

  /* public abstract Vector toInverseConstraints(Vector entities, Vector types); */ 

  public abstract int complexity(); 

  public abstract int cyclomaticComplexity(); 

  public abstract int epl(); 

}
