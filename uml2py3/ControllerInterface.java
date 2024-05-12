package uml2py3;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public interface ControllerInterface
{
  public void addNamedElement(NamedElement oo);
    public void killNamedElement(NamedElement namedelementxx);
  public void killAbstractNamedElement(NamedElement namedelementxx);

  public void killAbstractNamedElement(List namedelementxx);

  public void addRelationship(Relationship oo);
    public void killRelationship(Relationship relationshipxx);
  public void killAbstractRelationship(Relationship relationshipxx);

  public void killAbstractRelationship(List relationshipxx);

  public void addFeature(Feature oo);
    public void killFeature(Feature featurexx);
  public void killAbstractFeature(Feature featurexx);

  public void killAbstractFeature(List featurexx);

 public void settype(Feature featurex, Type typexx);
 public void setelementType(Feature featurex, Type elementTypexx);
  public void addType(Type oo);
    public void killType(Type typexx);
  public void killAbstractType(Type typexx);

  public void killAbstractType(List typexx);

  public void addAssociation(Association oo);
    public void killAssociation(Association associationxx);
 public void setmemberEnd(Association associationx, List memberEndxx);
   public void addmemberEnd(Association associationx, Property memberEndxx);
   public void removememberEnd(Association associationx, Property memberEndxx);
    public void addGeneralization(Generalization oo);
    public void killGeneralization(Generalization generalizationxx);
 public void setspecific(Generalization generalizationx, Entity specificxx);
 public void setgeneral(Generalization generalizationx, Entity generalxx);
  public void addClassifier(Classifier oo);
    public void killClassifier(Classifier classifierxx);
  public void killAbstractClassifier(Classifier classifierxx);

  public void killAbstractClassifier(List classifierxx);

  public void addDataType(DataType oo);
    public void killDataType(DataType datatypexx);
  public void killAbstractDataType(DataType datatypexx);

  public void killAbstractDataType(List datatypexx);

  public void addEnumeration(Enumeration oo);
    public void killEnumeration(Enumeration enumerationxx);
 public void setownedLiteral(Enumeration enumerationx, List ownedLiteralxx);
   public void addownedLiteral(Enumeration enumerationx, EnumerationLiteral ownedLiteralxx);
   public void removeownedLiteral(Enumeration enumerationx, EnumerationLiteral ownedLiteralxx);
    public void addEnumerationLiteral(EnumerationLiteral oo);
    public void killEnumerationLiteral(EnumerationLiteral enumerationliteralxx);
  public void addPrimitiveType(PrimitiveType oo);
    public void killPrimitiveType(PrimitiveType primitivetypexx);
  public void addEntity(Entity oo);
    public void killEntity(Entity entityxx);
 public void setgeneralization(Entity entityx, List generalizationxx);
   public void addgeneralization(Entity entityx, Generalization generalizationxx);
   public void removegeneralization(Entity entityx, Generalization generalizationxx);
   public void setspecialization(Entity entityx, List specializationxx);
   public void addspecialization(Entity entityx, Generalization specializationxx);
   public void removespecialization(Entity entityx, Generalization specializationxx);
   public void setownedOperation(Entity entityx, List ownedOperationxx);
   public void addownedOperation(Entity entityx, Operation ownedOperationxx);
   public void removeownedOperation(Entity entityx, Operation ownedOperationxx);
   public void setownedAttribute(Entity entityx, List ownedAttributexx);
   public void addownedAttribute(Entity entityx, Property ownedAttributexx);
   public void removeownedAttribute(Entity entityx, Property ownedAttributexx);
   public void setsuperclass(Entity entityx, List superclassxx);
   public void addsuperclass(Entity entityx, Entity superclassxx);
   public void removesuperclass(Entity entityx, Entity superclassxx);
   public void setsubclasses(Entity entityx, List subclassesxx);
   public void addsubclasses(Entity entityx, Entity subclassesxx);
   public void removesubclasses(Entity entityx, Entity subclassesxx);
    public void addCollectionType(CollectionType oo);
    public void killCollectionType(CollectionType collectiontypexx);
 public void setelementType(CollectionType collectiontypex, Type elementTypexx);
 public void setkeyType(CollectionType collectiontypex, Type keyTypexx);
  public void addExpression(Expression oo);
    public void killExpression(Expression expressionxx);
  public void killAbstractExpression(Expression expressionxx);

  public void killAbstractExpression(List expressionxx);

 public void settype(Expression expressionx, Type typexx);
 public void setelementType(Expression expressionx, Type elementTypexx);
  public void addBinaryExpression(BinaryExpression oo);
    public void killBinaryExpression(BinaryExpression binaryexpressionxx);
 public void setleft(BinaryExpression binaryexpressionx, Expression leftxx);
 public void setright(BinaryExpression binaryexpressionx, Expression rightxx);
 public void setaccumulator(BinaryExpression binaryexpressionx, Property accumulatorxx);
  public void addConditionalExpression(ConditionalExpression oo);
    public void killConditionalExpression(ConditionalExpression conditionalexpressionxx);
 public void settest(ConditionalExpression conditionalexpressionx, Expression testxx);
 public void setifExp(ConditionalExpression conditionalexpressionx, Expression ifExpxx);
 public void setelseExp(ConditionalExpression conditionalexpressionx, Expression elseExpxx);
  public void addUnaryExpression(UnaryExpression oo);
    public void killUnaryExpression(UnaryExpression unaryexpressionxx);
 public void setargument(UnaryExpression unaryexpressionx, Expression argumentxx);
  public void addCollectionExpression(CollectionExpression oo);
    public void killCollectionExpression(CollectionExpression collectionexpressionxx);
 public void setelements(CollectionExpression collectionexpressionx, List elementsxx);
   public void addelements(CollectionExpression collectionexpressionx, Expression elementsxx);
   public void removeelements(CollectionExpression collectionexpressionx, Expression elementsxx);
    public void addBasicExpression(BasicExpression oo);
    public void killBasicExpression(BasicExpression basicexpressionxx);
 public void setparameters(BasicExpression basicexpressionx, List parametersxx);
   public void addparameters(BasicExpression basicexpressionx, Expression parametersxx);
   public void removeparameters(BasicExpression basicexpressionx, Expression parametersxx);
   public void setreferredProperty(BasicExpression basicexpressionx, List referredPropertyxx);
   public void addreferredProperty(BasicExpression basicexpressionx, Property referredPropertyxx);
   public void removereferredProperty(BasicExpression basicexpressionx, Property referredPropertyxx);
   public void setcontext(BasicExpression basicexpressionx, List contextxx);
   public void addcontext(BasicExpression basicexpressionx, Entity contextxx);
   public void removecontext(BasicExpression basicexpressionx, Entity contextxx);
   public void setarrayIndex(BasicExpression basicexpressionx, List arrayIndexxx);
   public void addarrayIndex(BasicExpression basicexpressionx, Expression arrayIndexxx);
   public void removearrayIndex(BasicExpression basicexpressionx, Expression arrayIndexxx);
   public void setobjectRef(BasicExpression basicexpressionx, List objectRefxx);
   public void addobjectRef(BasicExpression basicexpressionx, Expression objectRefxx);
   public void removeobjectRef(BasicExpression basicexpressionx, Expression objectRefxx);
    public void addProperty(Property oo);
    public void killProperty(Property propertyxx);
 public void setqualifier(Property propertyx, List qualifierxx);
   public void addqualifier(Property propertyx, Property qualifierxx);
   public void removequalifier(Property propertyx, Property qualifierxx);
   public void settype(Property propertyx, Type typexx);
 public void setinitialValue(Property propertyx, Expression initialValuexx);
 public void setowner(Property propertyx, Entity ownerxx);
  public void addStatement(Statement oo);
    public void killStatement(Statement statementxx);
  public void killAbstractStatement(Statement statementxx);

  public void killAbstractStatement(List statementxx);

  public void addReturnStatement(ReturnStatement oo);
    public void killReturnStatement(ReturnStatement returnstatementxx);
 public void setreturnValue(ReturnStatement returnstatementx, List returnValuexx);
   public void addreturnValue(ReturnStatement returnstatementx, Expression returnValuexx);
   public void removereturnValue(ReturnStatement returnstatementx, Expression returnValuexx);
    public void addAssertStatement(AssertStatement oo);
    public void killAssertStatement(AssertStatement assertstatementxx);
 public void setcondition(AssertStatement assertstatementx, Expression conditionxx);
 public void setmessage(AssertStatement assertstatementx, List messagexx);
   public void addmessage(AssertStatement assertstatementx, Expression messagexx);
   public void removemessage(AssertStatement assertstatementx, Expression messagexx);
    public void addErrorStatement(ErrorStatement oo);
    public void killErrorStatement(ErrorStatement errorstatementxx);
 public void setthrownObject(ErrorStatement errorstatementx, Expression thrownObjectxx);
  public void addCatchStatement(CatchStatement oo);
    public void killCatchStatement(CatchStatement catchstatementxx);
 public void setcaughtObject(CatchStatement catchstatementx, Expression caughtObjectxx);
 public void setaction(CatchStatement catchstatementx, Statement actionxx);
  public void addFinalStatement(FinalStatement oo);
    public void killFinalStatement(FinalStatement finalstatementxx);
 public void setbody(FinalStatement finalstatementx, Statement bodyxx);
  public void addOperation(Operation oo);
    public void killOperation(Operation operationxx);
 public void setowner(Operation operationx, Entity ownerxx);
 public void setdefiners(Operation operationx, List definersxx);
   public void adddefiners(Operation operationx, Entity definersxx);
   public void removedefiners(Operation operationx, Entity definersxx);
    public void addUseCase(UseCase oo);
    public void killUseCase(UseCase usecasexx);
 public void setparameters(UseCase usecasex, List parametersxx);
   public void addparameters(UseCase usecasex, Property parametersxx);
   public void removeparameters(UseCase usecasex, Property parametersxx);
   public void setresultType(UseCase usecasex, Type resultTypexx);
 public void setclassifierBehaviour(UseCase usecasex, Statement classifierBehaviourxx);
  public void addBreakStatement(BreakStatement oo);
    public void killBreakStatement(BreakStatement breakstatementxx);
  public void addContinueStatement(ContinueStatement oo);
    public void killContinueStatement(ContinueStatement continuestatementxx);
  public void addOperationCallStatement(OperationCallStatement oo);
    public void killOperationCallStatement(OperationCallStatement operationcallstatementxx);
 public void setcallExp(OperationCallStatement operationcallstatementx, Expression callExpxx);
  public void addImplicitCallStatement(ImplicitCallStatement oo);
    public void killImplicitCallStatement(ImplicitCallStatement implicitcallstatementxx);
 public void setcallExp(ImplicitCallStatement implicitcallstatementx, Expression callExpxx);
  public void addLoopStatement(LoopStatement oo);
    public void killLoopStatement(LoopStatement loopstatementxx);
  public void killAbstractLoopStatement(LoopStatement loopstatementxx);

  public void killAbstractLoopStatement(List loopstatementxx);

 public void settest(LoopStatement loopstatementx, Expression testxx);
 public void setbody(LoopStatement loopstatementx, Statement bodyxx);
  public void addBoundedLoopStatement(BoundedLoopStatement oo);
    public void killBoundedLoopStatement(BoundedLoopStatement boundedloopstatementxx);
 public void setloopRange(BoundedLoopStatement boundedloopstatementx, Expression loopRangexx);
 public void setloopVar(BoundedLoopStatement boundedloopstatementx, Expression loopVarxx);
  public void addUnboundedLoopStatement(UnboundedLoopStatement oo);
    public void killUnboundedLoopStatement(UnboundedLoopStatement unboundedloopstatementxx);
  public void addAssignStatement(AssignStatement oo);
    public void killAssignStatement(AssignStatement assignstatementxx);
 public void settype(AssignStatement assignstatementx, List typexx);
   public void addtype(AssignStatement assignstatementx, Type typexx);
   public void removetype(AssignStatement assignstatementx, Type typexx);
   public void setleft(AssignStatement assignstatementx, Expression leftxx);
 public void setright(AssignStatement assignstatementx, Expression rightxx);
  public void addSequenceStatement(SequenceStatement oo);
    public void killSequenceStatement(SequenceStatement sequencestatementxx);
 public void setstatements(SequenceStatement sequencestatementx, List statementsxx);
   public void addstatements(SequenceStatement sequencestatementx, Statement statementsxx);
   public void removestatements(SequenceStatement sequencestatementx, Statement statementsxx);
    public void addTryStatement(TryStatement oo);
    public void killTryStatement(TryStatement trystatementxx);
 public void setcatchClauses(TryStatement trystatementx, List catchClausesxx);
   public void addcatchClauses(TryStatement trystatementx, Statement catchClausesxx);
   public void removecatchClauses(TryStatement trystatementx, Statement catchClausesxx);
   public void setbody(TryStatement trystatementx, Statement bodyxx);
 public void setendStatement(TryStatement trystatementx, List endStatementxx);
   public void addendStatement(TryStatement trystatementx, Statement endStatementxx);
   public void removeendStatement(TryStatement trystatementx, Statement endStatementxx);
    public void addConditionalStatement(ConditionalStatement oo);
    public void killConditionalStatement(ConditionalStatement conditionalstatementxx);
 public void settest(ConditionalStatement conditionalstatementx, Expression testxx);
 public void setifPart(ConditionalStatement conditionalstatementx, Statement ifPartxx);
 public void setelsePart(ConditionalStatement conditionalstatementx, List elsePartxx);
   public void addelsePart(ConditionalStatement conditionalstatementx, Statement elsePartxx);
   public void removeelsePart(ConditionalStatement conditionalstatementx, Statement elsePartxx);
    public void addCreationStatement(CreationStatement oo);
    public void killCreationStatement(CreationStatement creationstatementxx);
 public void settype(CreationStatement creationstatementx, Type typexx);
 public void setelementType(CreationStatement creationstatementx, Type elementTypexx);
 public void setinitialExpression(CreationStatement creationstatementx, List initialExpressionxx);
   public void addinitialExpression(CreationStatement creationstatementx, Expression initialExpressionxx);
   public void removeinitialExpression(CreationStatement creationstatementx, Expression initialExpressionxx);
    public void addBehaviouralFeature(BehaviouralFeature oo);
    public void killBehaviouralFeature(BehaviouralFeature behaviouralfeaturexx);
  public void killAbstractBehaviouralFeature(BehaviouralFeature behaviouralfeaturexx);

  public void killAbstractBehaviouralFeature(List behaviouralfeaturexx);

 public void setparameters(BehaviouralFeature behaviouralfeaturex, List parametersxx);
   public void addparameters(BehaviouralFeature behaviouralfeaturex, Property parametersxx);
   public void removeparameters(BehaviouralFeature behaviouralfeaturex, Property parametersxx);
   public void setactivity(BehaviouralFeature behaviouralfeaturex, Statement activityxx);
  public void addPrintcode(Printcode oo);
    public void killPrintcode(Printcode printcodexx);
}

