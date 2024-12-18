import java.util.Vector; 
import java.io.*; 
import java.util.Collections; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: TransformationSynthesis */ 


class GAIndividual
{ Map mm; 
  double fitness = 0; 
  Map traitscores; 

  GAIndividual(Map mp) 
  { mm = mp; 
    traitscores = new Map(); 
  } 

  public String toString()
  { return mm + " (fitness = " + fitness + ")"; } 

  public boolean equals(Object obj)
  { if (obj instanceof GAIndividual) 
    { GAIndividual ind = (GAIndividual) obj; 
      if (ind.mm.equals(mm)) 
      { return true; } 
    } 
    return false; 
  } 

  static boolean isValid(Map mm)
  { for (int i = 0; i < mm.elements.size(); i++) 
    { Maplet mt = (Maplet) mm.elements.get(i); 
      Entity src = (Entity) mt.source; 
      Entity trg = (Entity) mt.dest; 
      if (src.isConcrete() && trg.isAbstract())
      { return false; } 
      else if (src.isAbstract())
      { Vector ssubs = src.getSubclasses(); 
        for (int j = 0; j < ssubs.size(); j++) 
        { Entity ssub = (Entity) ssubs.get(j); 
          Entity subt = (Entity) mm.get(ssub); 
          if (subt == null) 
          { /* if (trg.isConcrete())
            { mm.set(ssub,trg); } 
            else 
            { Vector trgsubs = new Vector(); 
              trgsubs.addAll(trg.getAllSubclasses()); 
              trgsubs.add(trg); 
              int sometarget = (int) Math.floor(Math.random()*trgsubs.size());
              Entity sometent = (Entity) trgsubs.get(sometarget); 
              if (sometent != null && !sometent.isSource()) 
              { mm.set(ssub,sometent); }  
            } */ 
          } 
          else if (trg == subt) { } 
          else if (Entity.isAncestor(trg,subt)) { } 
          else 
          { return false; } // mm does not preserve inheritance
        } 
      } 
    } 
    return true; 
  } 

  double computeFitness(Vector entities, Vector thesaurus)
  { // The bx score of the map mm
    double mapbxscore = 0; 

    Vector emaps = mm.getElements(); 
    for (int i = 0; i < emaps.size(); i++)
    { Maplet emap = (Maplet) emaps.get(i); 
      Entity src = (Entity) emap.source; 
      Entity trg = (Entity) emap.dest; 

      if (src.isConcrete() && trg.isAbstract()) 
      { return 0; } 
      else 
      { // double refscore = src.esimN(trg,mm,null,entities);
        // double abscore = src.esimAbsN(trg,mm,null,entities);
        // double traitscore = refscore*abscore;
        // traitscores.set(src,new Double(traitscore)); 
        double traitscore = src.compositeSimilarity(trg,mm,null,entities);
        double namesim = ModelElement.similarity(src.getName(), trg.getName()); 
        double nmssim = src.nmsSimilarity(trg,thesaurus); 

        mapbxscore = mapbxscore + traitscore*(namesim + nmssim - namesim*nmssim); 
      } 
    }
    fitness = mapbxscore;       
    // System.out.println(">>> Fitness of " + mm + " is: " + mapbxscore); 
    return fitness; 
  } 

  GAIndividual mutate(Vector sources, Vector targets, 
                      Vector unused, Vector emapsources, Vector emaptargets)
  { Vector mmsources = new Vector(); 
    Map mp = new Map(); 
    for (int i = 0; i < mm.elements.size(); i++) 
    { Maplet mt = (Maplet) mm.elements.get(i); 
      mp.elements.add(new Maplet(mt.source,mt.dest));
      mmsources.add(mt.source);  
    }
    GAIndividual res = new GAIndividual(mp); 

    Vector diff = new Vector(); 
    diff.addAll(sources); 
    diff.removeAll(mmsources); 
    if (diff.size() > 0) 
    { int drand = (int) Math.floor(Math.random()*diff.size()); 
      Entity newsrc = (Entity) diff.get(drand); 
      double rand1 = Math.random(); 
      int mpos = (int) Math.floor(rand1*targets.size()); 
      Entity newtrg = (Entity) targets.get(mpos); 
      if (newtrg != null && !newtrg.isSource())
      { mp.set(newsrc,newtrg); } 
      Map.extendDomainRange(mp,unused); 
      Map.extendDomainRange(mp,emapsources,emaptargets); 
      return res; 
    } 
      
    double rand = Math.random(); 
    int pos = (int) Math.floor(rand*(mp.size())); 
    double rand2 = Math.random(); 
    int mpos = (int) Math.floor(rand2*targets.size()); 
    Maplet elem = (Maplet) mp.elements.get(pos);
    Entity src = (Entity) elem.source;  
    Entity newtrg = (Entity) targets.get(mpos); 
    if (newtrg != null && !newtrg.isSource())
    { mp.set(src,newtrg); } 
    Map.extendDomainRange(mp,unused); 
    Map.extendDomainRange(mp,emapsources,emaptargets); 
    return res;  
  } // or extend it, if mm.elements.size() < allsources.size()

  GAIndividual crossover(GAIndividual other, Vector sources, Vector targets, 
                         Vector unused, Vector emapsources, Vector emaptargets)
  { Vector mmsources = new Vector(); 
    Map mp = new Map(); 
    for (int i = 0; i < mm.elements.size(); i++) 
    { Maplet mt = (Maplet) mm.elements.get(i); 
      mp.elements.add(new Maplet(mt.source,mt.dest));
      mmsources.add(mt.source);  
    }
    GAIndividual res = new GAIndividual(mp); 

    Map othermap = other.mm; 
      
    double rand = Math.random(); 
    int pos = (int) Math.floor(rand*(othermap.size()));
    for (int j = pos; j < othermap.size(); j++)
    { Maplet elem = (Maplet) othermap.elements.get(j);
      Entity src = (Entity) elem.source;  
      Entity newtrg = (Entity) elem.dest; 
      if (newtrg != null && !newtrg.isSource())
      { mp.set(src,newtrg); }
    }  
    Map.extendDomainRange(mp,unused); 
    Map.extendDomainRange(mp,emapsources,emaptargets); 
    return res;  
  } // or extend it, if mm.elements.size() < allsources.size()



} 

public class GeneticAlgorithm
{ Vector population = new Vector(); 
  double maxfitness = 0; 
  Vector best = new Vector(); 
  int maxpopulation = 100; 

  public GeneticAlgorithm(int n) 
  { maxpopulation = n; } 

  public double getMaxfitness()
  { return maxfitness; } 

  public Vector getBest()
  { Vector res = new Vector(); 
    for (int i = 0; i < best.size(); i++) 
    { GAIndividual ind = (GAIndividual) best.get(i); 
      res.add(new ModelMatching(ind.mm)); 
    } 
    return res; 
  }

  public void initialise(Vector maps) 
  { for (int i = 0; i < maps.size(); i++) 
    { Map mm = (Map) maps.get(i); 
      GAIndividual ind = new GAIndividual(mm); 
      population.add(ind); 
    } 
  } 

  public void computeFitness(Vector entities, Vector thesaurus)
  { maxfitness = 0; 
    for (int i = 0; i < population.size(); i++) 
    { GAIndividual ind = (GAIndividual) population.get(i); 
      double fit = ind.computeFitness(entities,thesaurus);
      if (fit > maxfitness)
      { maxfitness = fit; 
        best = new Vector(); 
        best.add(ind); 
      } 
      else if (fit == maxfitness) 
      { if (best.contains(ind)) { } 
        else 
        { best.add(ind); }
      }   
    } 
  } 


  public void nextGeneration(Vector sources, Vector targets, 
                             Vector unused, Vector emapsources, Vector emaptargets)
  { // preserve the best elements; mutate all elements
    Vector added = new Vector(); 

    for (int i = 0; i < population.size(); i++) 
    { GAIndividual ind = (GAIndividual) population.get(i); 
      GAIndividual indm = ind.mutate(sources,targets,unused,emapsources,emaptargets); 
      if (added.contains(indm)) { } 
      else if (population.contains(indm)) { } 
      else if (GAIndividual.isValid(indm.mm))
      { added.add(indm); 
        // System.out.println(">>> mutated " + ind + " to " + indm); 
      }  
    } 

    // crossover best elements
    for (int i = 0; i < best.size(); i++) 
    { GAIndividual b = (GAIndividual) best.get(i); 
      for (int j = 0; j < population.size(); j++) 
      { GAIndividual b2 = (GAIndividual) population.get(j);
        if (b.fitness > b2.fitness && b2.fitness > 0.7*maxfitness)
        {   
          GAIndividual newb = b.crossover(b2,sources,targets,unused,emapsources,emaptargets); 
          if (added.contains(newb)) { } 
          else if (population.contains(newb)) { } 
          else if (GAIndividual.isValid(newb.mm))
          { added.add(newb);
            // System.out.println(">>> Crossover of " + b + " and " + b2 + " to: " + newb); 
          }
        } 
      }   
    } 

    population.addAll(added); 
  }         

  public void removeUnfit(double ratio)
  { Vector removed = new Vector(); 
    for (int i = 0; i < population.size(); i++) 
    { GAIndividual ind = (GAIndividual) population.get(i); 
      if (ind.fitness < ratio*maxfitness)
      { removed.add(ind); } 
    } 
    population.removeAll(removed); 
  } 

  public void removeDuplicates()
  { Vector removed = new Vector(); 
    Vector preserved = new Vector(); 

    for (int i = 0; i < population.size(); i++) 
    { GAIndividual ind = (GAIndividual) population.get(i); 
      if (preserved.contains(ind.mm))
      { removed.add(ind); }
      else 
      { preserved.add(ind.mm); }  
    } 
    population.removeAll(removed); 
  } 

  public void iterate(int generations, Vector entities, Vector sources, Vector targets,
                      Vector unused, Vector emapsources, Vector emaptargets,
                      Vector thesaurus)
  { nextGeneration(sources,targets,unused,emapsources,emaptargets); 
    for (int i = 0; i < generations; i++) 
    { computeFitness(entities,thesaurus); 
      System.out.println("*** Population size = " + population.size()); 
      System.out.println("*** maxfitness = " + maxfitness); 
      System.out.println("*** For: " + best); 

      if (population.size() > 1000) 
      { removeUnfit(0.85); } 
      else
      { removeUnfit(0.6); } 
 
      removeDuplicates(); 
      System.out.println("*** Population reduced size = " + population.size()); 
      nextGeneration(sources,targets,unused,emapsources,emaptargets); 
    } 
    computeFitness(entities,thesaurus); 
  } 
} 



