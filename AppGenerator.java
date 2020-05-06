import java.util.Vector; 
import java.io.*; 


public abstract class AppGenerator
{ 

  public abstract void modelFacade(Vector usecases, CGSpec cgs, Vector entities, Vector types, PrintWriter out); 

  public abstract void singlePageApp(UseCase uc, CGSpec cgs, PrintWriter out); 

  public abstract void listViewController(Entity e, PrintWriter out); 

}
