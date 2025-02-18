package uml2py3;


import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.*;
import java.util.StringTokenizer;

public class TestsGUI extends JFrame implements ActionListener
{ JPanel panel = new JPanel();
  JPanel tPanel = new JPanel();
  JPanel cPanel = new JPanel();
  Controller cont = Controller.inst();
    JButton loadModelButton = new JButton("loadModel");
    JButton saveModelButton = new JButton("saveModel");
    JButton loadXmiButton = new JButton("loadXmi");
    JButton loadCSVButton = new JButton("Execute Tests");
    JButton printcodeButton = new JButton("printcode");

 public TestsGUI()
  { super("Select use case to test");
    panel.setLayout(new BorderLayout());
    panel.add(tPanel, BorderLayout.NORTH);
    panel.add(cPanel, BorderLayout.CENTER);
    setContentPane(panel);
    addWindowListener(new WindowAdapter() 
    { public void windowClosing(WindowEvent e)
      { System.exit(0); } });
    tPanel.add(loadModelButton);
    loadModelButton.addActionListener(this);
    tPanel.add(saveModelButton);
    saveModelButton.addActionListener(this);
    tPanel.add(loadXmiButton);
    loadXmiButton.addActionListener(this);
    tPanel.add(loadCSVButton);
    loadCSVButton.addActionListener(this);
    cPanel.add(printcodeButton);
    printcodeButton.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e)
  { if (e == null) { return; }
    String cmd = e.getActionCommand();
    int[] intTestValues = {0, -1, 1, -1025, 1024};
    long[] longTestValues = {0, -1, 1, -999999, 100000};
    double[] doubleTestValues = {0, -1, 1, 3125.0891, 4.9E-324 };
    boolean[] booleanTestValues = {false, true};
    String[] stringTestValues = {"", " abc_XZ ", "#�$* &~@'"};
    int MAXOBJECTS = 500;

    if ("loadModel".equals(cmd))
    { Controller.loadModel("in.txt");
      cont.checkCompleteness();
      System.err.println("Model loaded");
      return; } 
    if ("saveModel".equals(cmd))
    { cont.saveModel("out.txt");  
      cont.saveXSI("xsi.txt"); 
      return;
    } 
    if ("loadXmi".equals(cmd))
    { cont.loadXSI();  
      cont.checkCompleteness();
      System.err.println("Model loaded");
      return;
    } 
    if ("Execute Tests".equals(cmd))
    { System.err.println("Execution tests");
      { int _associations_instances = Math.min(MAXOBJECTS, Controller.inst().associations.size());
      }

      { int _generalizations_instances = Math.min(MAXOBJECTS, Controller.inst().generalizations.size());
      }

      { int _enumerations_instances = Math.min(MAXOBJECTS, Controller.inst().enumerations.size());
      }

      { int _enumerationliterals_instances = Math.min(MAXOBJECTS, Controller.inst().enumerationliterals.size());
      }

      { int _primitivetypes_instances = Math.min(MAXOBJECTS, Controller.inst().primitivetypes.size());
        int[] primitivetypes_isPrimitiveType_counts = new int[100]; 
        int[] primitivetypes_isPrimitiveType_totals = new int[100]; 
        if (_primitivetypes_instances > 0)
        { PrimitiveType _ex = (PrimitiveType) Controller.inst().primitivetypes.get(0);
          MutationTest.isPrimitiveType_mutation_tests(_ex,primitivetypes_isPrimitiveType_counts, primitivetypes_isPrimitiveType_totals);
        }
        System.out.println();
        int[] primitivetypes_isPythonPrimitiveType_counts = new int[100]; 
        int[] primitivetypes_isPythonPrimitiveType_totals = new int[100]; 
        if (_primitivetypes_instances > 0)
        { PrimitiveType _ex = (PrimitiveType) Controller.inst().primitivetypes.get(0);
          MutationTest.isPythonPrimitiveType_mutation_tests(_ex,primitivetypes_isPythonPrimitiveType_counts, primitivetypes_isPythonPrimitiveType_totals);
        }
        System.out.println();
      }

      { int _entitys_instances = Math.min(MAXOBJECTS, Controller.inst().entitys.size());
        int[] entitys_isSingleValued_counts = new int[100]; 
        int[] entitys_isSingleValued_totals = new int[100]; 
        for (int _i = 0; _i < _entitys_instances; _i++)
        { Entity _ex = (Entity) Controller.inst().entitys.get(_i);
          try {
            MutationTest.isSingleValued_mutation_tests(_ex,entitys_isSingleValued_counts, entitys_isSingleValued_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] entitys_isSetValued_counts = new int[100]; 
        int[] entitys_isSetValued_totals = new int[100]; 
        for (int _i = 0; _i < _entitys_instances; _i++)
        { Entity _ex = (Entity) Controller.inst().entitys.get(_i);
          try {
            MutationTest.isSetValued_mutation_tests(_ex,entitys_isSetValued_counts, entitys_isSetValued_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] entitys_isSequenceValued_counts = new int[100]; 
        int[] entitys_isSequenceValued_totals = new int[100]; 
        for (int _i = 0; _i < _entitys_instances; _i++)
        { Entity _ex = (Entity) Controller.inst().entitys.get(_i);
          try {
            MutationTest.isSequenceValued_mutation_tests(_ex,entitys_isSequenceValued_counts, entitys_isSequenceValued_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] entitys_createPKOp_counts = new int[100]; 
        int[] entitys_createPKOp_totals = new int[100]; 
        for (int _i = 0; _i < _entitys_instances; _i++)
        { Entity _ex = (Entity) Controller.inst().entitys.get(_i);
          try {
            MutationTest.createPKOp_mutation_tests(_ex,entitys_createPKOp_counts, entitys_createPKOp_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] entitys_instancesOps_counts = new int[100]; 
        int[] entitys_instancesOps_totals = new int[100]; 
        if (_entitys_instances > 0)
        { Entity _ex = (Entity) Controller.inst().entitys.get(0);
          MutationTest.instancesOps_mutation_tests(_ex,entitys_instancesOps_counts, entitys_instancesOps_totals);
        }
        System.out.println();
      }

      { int _collectiontypes_instances = Math.min(MAXOBJECTS, Controller.inst().collectiontypes.size());
      }

      { int _binaryexpressions_instances = Math.min(MAXOBJECTS, Controller.inst().binaryexpressions.size());
        int[] binaryexpressions_isComparitor_counts = new int[100]; 
        int[] binaryexpressions_isComparitor_totals = new int[100]; 
        if (_binaryexpressions_instances > 0)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(0);
          MutationTest.isComparitor_mutation_tests(_ex,binaryexpressions_isComparitor_counts, binaryexpressions_isComparitor_totals);
        }
        System.out.println();
        int[] binaryexpressions_isInclusion_counts = new int[100]; 
        int[] binaryexpressions_isInclusion_totals = new int[100]; 
        if (_binaryexpressions_instances > 0)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(0);
          MutationTest.isInclusion_mutation_tests(_ex,binaryexpressions_isInclusion_counts, binaryexpressions_isInclusion_totals);
        }
        System.out.println();
        int[] binaryexpressions_isExclusion_counts = new int[100]; 
        int[] binaryexpressions_isExclusion_totals = new int[100]; 
        if (_binaryexpressions_instances > 0)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(0);
          MutationTest.isExclusion_mutation_tests(_ex,binaryexpressions_isExclusion_counts, binaryexpressions_isExclusion_totals);
        }
        System.out.println();
        int[] binaryexpressions_isBooleanOp_counts = new int[100]; 
        int[] binaryexpressions_isBooleanOp_totals = new int[100]; 
        if (_binaryexpressions_instances > 0)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(0);
          MutationTest.isBooleanOp_mutation_tests(_ex,binaryexpressions_isBooleanOp_counts, binaryexpressions_isBooleanOp_totals);
        }
        System.out.println();
        int[] binaryexpressions_isStringOp_counts = new int[100]; 
        int[] binaryexpressions_isStringOp_totals = new int[100]; 
        if (_binaryexpressions_instances > 0)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(0);
          MutationTest.isStringOp_mutation_tests(_ex,binaryexpressions_isStringOp_counts, binaryexpressions_isStringOp_totals);
        }
        System.out.println();
        int[] binaryexpressions_isCollectionOp_counts = new int[100]; 
        int[] binaryexpressions_isCollectionOp_totals = new int[100]; 
        if (_binaryexpressions_instances > 0)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(0);
          MutationTest.isCollectionOp_mutation_tests(_ex,binaryexpressions_isCollectionOp_counts, binaryexpressions_isCollectionOp_totals);
        }
        System.out.println();
        int[] binaryexpressions_isDistributedIteratorOp_counts = new int[100]; 
        int[] binaryexpressions_isDistributedIteratorOp_totals = new int[100]; 
        if (_binaryexpressions_instances > 0)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(0);
          MutationTest.isDistributedIteratorOp_mutation_tests(_ex,binaryexpressions_isDistributedIteratorOp_counts, binaryexpressions_isDistributedIteratorOp_totals);
        }
        System.out.println();
        int[] binaryexpressions_isIteratorOp_counts = new int[100]; 
        int[] binaryexpressions_isIteratorOp_totals = new int[100]; 
        if (_binaryexpressions_instances > 0)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(0);
          MutationTest.isIteratorOp_mutation_tests(_ex,binaryexpressions_isIteratorOp_counts, binaryexpressions_isIteratorOp_totals);
        }
        System.out.println();
        int[] binaryexpressions_mapDividesExpression_counts = new int[100]; 
        int[] binaryexpressions_mapDividesExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapDividesExpression_mutation_tests(_ex,binaryexpressions_mapDividesExpression_counts, binaryexpressions_mapDividesExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapNumericExpression_counts = new int[100]; 
        int[] binaryexpressions_mapNumericExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapNumericExpression_mutation_tests(_ex,binaryexpressions_mapNumericExpression_counts, binaryexpressions_mapNumericExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapComparitorExpression_counts = new int[100]; 
        int[] binaryexpressions_mapComparitorExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapComparitorExpression_mutation_tests(_ex,binaryexpressions_mapComparitorExpression_counts, binaryexpressions_mapComparitorExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapStringExpression_counts = new int[100]; 
        int[] binaryexpressions_mapStringExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapStringExpression_mutation_tests(_ex,binaryexpressions_mapStringExpression_counts, binaryexpressions_mapStringExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapStringPlus_counts = new int[100]; 
        int[] binaryexpressions_mapStringPlus_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapStringPlus_mutation_tests(_ex,binaryexpressions_mapStringPlus_counts, binaryexpressions_mapStringPlus_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapRefPlus_counts = new int[100]; 
        int[] binaryexpressions_mapRefPlus_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapRefPlus_mutation_tests(_ex,binaryexpressions_mapRefPlus_counts, binaryexpressions_mapRefPlus_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapBooleanExpression_counts = new int[100]; 
        int[] binaryexpressions_mapBooleanExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapBooleanExpression_mutation_tests(_ex,binaryexpressions_mapBooleanExpression_counts, binaryexpressions_mapBooleanExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapBinaryCollectionExpression_counts = new int[100]; 
        int[] binaryexpressions_mapBinaryCollectionExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapBinaryCollectionExpression_mutation_tests(_ex,binaryexpressions_mapBinaryCollectionExpression_counts, binaryexpressions_mapBinaryCollectionExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapBinaryMapExpression_counts = new int[100]; 
        int[] binaryexpressions_mapBinaryMapExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapBinaryMapExpression_mutation_tests(_ex,binaryexpressions_mapBinaryMapExpression_counts, binaryexpressions_mapBinaryMapExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapDistributedIteratorExpression_counts = new int[100]; 
        int[] binaryexpressions_mapDistributedIteratorExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapDistributedIteratorExpression_mutation_tests(_ex,binaryexpressions_mapDistributedIteratorExpression_counts, binaryexpressions_mapDistributedIteratorExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapMapIteratorExpression_counts = new int[100]; 
        int[] binaryexpressions_mapMapIteratorExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapMapIteratorExpression_mutation_tests(_ex,binaryexpressions_mapMapIteratorExpression_counts, binaryexpressions_mapMapIteratorExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapIteratorExpression_counts = new int[100]; 
        int[] binaryexpressions_mapIteratorExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapIteratorExpression_mutation_tests(_ex,binaryexpressions_mapIteratorExpression_counts, binaryexpressions_mapIteratorExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapTypeCastExpression_counts = new int[100]; 
        int[] binaryexpressions_mapTypeCastExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapTypeCastExpression_mutation_tests(_ex,binaryexpressions_mapTypeCastExpression_counts, binaryexpressions_mapTypeCastExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapCatchExpression_counts = new int[100]; 
        int[] binaryexpressions_mapCatchExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapCatchExpression_mutation_tests(_ex,binaryexpressions_mapCatchExpression_counts, binaryexpressions_mapCatchExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_mapBinaryExpression_counts = new int[100]; 
        int[] binaryexpressions_mapBinaryExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.mapBinaryExpression_mutation_tests(_ex,binaryexpressions_mapBinaryExpression_counts, binaryexpressions_mapBinaryExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] binaryexpressions_updateFormBinaryExpression_counts = new int[100]; 
        int[] binaryexpressions_updateFormBinaryExpression_totals = new int[100]; 
        for (int _i = 0; _i < _binaryexpressions_instances; _i++)
        { BinaryExpression _ex = (BinaryExpression) Controller.inst().binaryexpressions.get(_i);
          try {
            MutationTest.updateFormBinaryExpression_mutation_tests(_ex,binaryexpressions_updateFormBinaryExpression_counts, binaryexpressions_updateFormBinaryExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _conditionalexpressions_instances = Math.min(MAXOBJECTS, Controller.inst().conditionalexpressions.size());
        int[] conditionalexpressions_mapConditionalExpression_counts = new int[100]; 
        int[] conditionalexpressions_mapConditionalExpression_totals = new int[100]; 
        for (int _i = 0; _i < _conditionalexpressions_instances; _i++)
        { ConditionalExpression _ex = (ConditionalExpression) Controller.inst().conditionalexpressions.get(_i);
          try {
            MutationTest.mapConditionalExpression_mutation_tests(_ex,conditionalexpressions_mapConditionalExpression_counts, conditionalexpressions_mapConditionalExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] conditionalexpressions_updateFormConditionalExpression_counts = new int[100]; 
        int[] conditionalexpressions_updateFormConditionalExpression_totals = new int[100]; 
        for (int _i = 0; _i < _conditionalexpressions_instances; _i++)
        { ConditionalExpression _ex = (ConditionalExpression) Controller.inst().conditionalexpressions.get(_i);
          try {
            MutationTest.updateFormConditionalExpression_mutation_tests(_ex,conditionalexpressions_updateFormConditionalExpression_counts, conditionalexpressions_updateFormConditionalExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _unaryexpressions_instances = Math.min(MAXOBJECTS, Controller.inst().unaryexpressions.size());
        int[] unaryexpressions_isUnaryStringOp_counts = new int[100]; 
        int[] unaryexpressions_isUnaryStringOp_totals = new int[100]; 
        if (_unaryexpressions_instances > 0)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(0);
          MutationTest.isUnaryStringOp_mutation_tests(_ex,unaryexpressions_isUnaryStringOp_counts, unaryexpressions_isUnaryStringOp_totals);
        }
        System.out.println();
        int[] unaryexpressions_isReduceOp_counts = new int[100]; 
        int[] unaryexpressions_isReduceOp_totals = new int[100]; 
        if (_unaryexpressions_instances > 0)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(0);
          MutationTest.isReduceOp_mutation_tests(_ex,unaryexpressions_isReduceOp_counts, unaryexpressions_isReduceOp_totals);
        }
        System.out.println();
        int[] unaryexpressions_mapNumericExpression_counts = new int[100]; 
        int[] unaryexpressions_mapNumericExpression_totals = new int[100]; 
        for (int _i = 0; _i < _unaryexpressions_instances; _i++)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(_i);
          try {
            MutationTest.mapNumericExpression_mutation_tests(_ex,unaryexpressions_mapNumericExpression_counts, unaryexpressions_mapNumericExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] unaryexpressions_mapStringExpression_counts = new int[100]; 
        int[] unaryexpressions_mapStringExpression_totals = new int[100]; 
        for (int _i = 0; _i < _unaryexpressions_instances; _i++)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(_i);
          try {
            MutationTest.mapStringExpression_mutation_tests(_ex,unaryexpressions_mapStringExpression_counts, unaryexpressions_mapStringExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] unaryexpressions_mapReduceExpression_counts = new int[100]; 
        int[] unaryexpressions_mapReduceExpression_totals = new int[100]; 
        for (int _i = 0; _i < _unaryexpressions_instances; _i++)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(_i);
          try {
            MutationTest.mapReduceExpression_mutation_tests(_ex,unaryexpressions_mapReduceExpression_counts, unaryexpressions_mapReduceExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] unaryexpressions_mapUnaryCollectionExpression_counts = new int[100]; 
        int[] unaryexpressions_mapUnaryCollectionExpression_totals = new int[100]; 
        for (int _i = 0; _i < _unaryexpressions_instances; _i++)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(_i);
          try {
            MutationTest.mapUnaryCollectionExpression_mutation_tests(_ex,unaryexpressions_mapUnaryCollectionExpression_counts, unaryexpressions_mapUnaryCollectionExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] unaryexpressions_mapUnaryMapExpression_counts = new int[100]; 
        int[] unaryexpressions_mapUnaryMapExpression_totals = new int[100]; 
        for (int _i = 0; _i < _unaryexpressions_instances; _i++)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(_i);
          try {
            MutationTest.mapUnaryMapExpression_mutation_tests(_ex,unaryexpressions_mapUnaryMapExpression_counts, unaryexpressions_mapUnaryMapExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] unaryexpressions_mapUnaryExpression_counts = new int[100]; 
        int[] unaryexpressions_mapUnaryExpression_totals = new int[100]; 
        for (int _i = 0; _i < _unaryexpressions_instances; _i++)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(_i);
          try {
            MutationTest.mapUnaryExpression_mutation_tests(_ex,unaryexpressions_mapUnaryExpression_counts, unaryexpressions_mapUnaryExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] unaryexpressions_updateFormUnaryExpression_counts = new int[100]; 
        int[] unaryexpressions_updateFormUnaryExpression_totals = new int[100]; 
        for (int _i = 0; _i < _unaryexpressions_instances; _i++)
        { UnaryExpression _ex = (UnaryExpression) Controller.inst().unaryexpressions.get(_i);
          try {
            MutationTest.updateFormUnaryExpression_mutation_tests(_ex,unaryexpressions_updateFormUnaryExpression_counts, unaryexpressions_updateFormUnaryExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _collectionexpressions_instances = Math.min(MAXOBJECTS, Controller.inst().collectionexpressions.size());
        int[] collectionexpressions_mapCollectionExpression_counts = new int[100]; 
        int[] collectionexpressions_mapCollectionExpression_totals = new int[100]; 
        for (int _i = 0; _i < _collectionexpressions_instances; _i++)
        { CollectionExpression _ex = (CollectionExpression) Controller.inst().collectionexpressions.get(_i);
          try {
            MutationTest.mapCollectionExpression_mutation_tests(_ex,collectionexpressions_mapCollectionExpression_counts, collectionexpressions_mapCollectionExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] collectionexpressions_toLambdaList_counts = new int[100]; 
        int[] collectionexpressions_toLambdaList_totals = new int[100]; 
        for (int _i = 0; _i < _collectionexpressions_instances; _i++)
        { CollectionExpression _ex = (CollectionExpression) Controller.inst().collectionexpressions.get(_i);
          try {
            MutationTest.toLambdaList_mutation_tests(_ex,collectionexpressions_toLambdaList_counts, collectionexpressions_toLambdaList_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _basicexpressions_instances = Math.min(MAXOBJECTS, Controller.inst().basicexpressions.size());
        int[] basicexpressions_isMathFunction_counts = new int[100]; 
        int[] basicexpressions_isMathFunction_totals = new int[100]; 
        if (_basicexpressions_instances > 0)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(0);
          MutationTest.isMathFunction_mutation_tests(_ex,basicexpressions_isMathFunction_counts, basicexpressions_isMathFunction_totals);
        }
        System.out.println();
        int[] basicexpressions_noContextnoObject_counts = new int[100]; 
        int[] basicexpressions_noContextnoObject_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.noContextnoObject_mutation_tests(_ex,basicexpressions_noContextnoObject_counts, basicexpressions_noContextnoObject_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_contextAndObject_counts = new int[100]; 
        int[] basicexpressions_contextAndObject_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.contextAndObject_mutation_tests(_ex,basicexpressions_contextAndObject_counts, basicexpressions_contextAndObject_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_isOclExceptionCreation_counts = new int[100]; 
        int[] basicexpressions_isOclExceptionCreation_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.isOclExceptionCreation_mutation_tests(_ex,basicexpressions_isOclExceptionCreation_counts, basicexpressions_isOclExceptionCreation_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapTypeExpression_counts = new int[100]; 
        int[] basicexpressions_mapTypeExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapTypeExpression_mutation_tests(_ex,basicexpressions_mapTypeExpression_counts, basicexpressions_mapTypeExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapValueExpression_counts = new int[100]; 
        int[] basicexpressions_mapValueExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapValueExpression_mutation_tests(_ex,basicexpressions_mapValueExpression_counts, basicexpressions_mapValueExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapVariableExpression_counts = new int[100]; 
        int[] basicexpressions_mapVariableExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapVariableExpression_mutation_tests(_ex,basicexpressions_mapVariableExpression_counts, basicexpressions_mapVariableExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapStaticAttributeExpression_counts = new int[100]; 
        int[] basicexpressions_mapStaticAttributeExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapStaticAttributeExpression_mutation_tests(_ex,basicexpressions_mapStaticAttributeExpression_counts, basicexpressions_mapStaticAttributeExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapReferencedAttributeExpression_counts = new int[100]; 
        int[] basicexpressions_mapReferencedAttributeExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapReferencedAttributeExpression_mutation_tests(_ex,basicexpressions_mapReferencedAttributeExpression_counts, basicexpressions_mapReferencedAttributeExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapAttributeExpression_counts = new int[100]; 
        int[] basicexpressions_mapAttributeExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapAttributeExpression_mutation_tests(_ex,basicexpressions_mapAttributeExpression_counts, basicexpressions_mapAttributeExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapErrorCall_counts = new int[100]; 
        int[] basicexpressions_mapErrorCall_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapErrorCall_mutation_tests(_ex,basicexpressions_mapErrorCall_counts, basicexpressions_mapErrorCall_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapStaticOperationExpression_counts = new int[100]; 
        int[] basicexpressions_mapStaticOperationExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapStaticOperationExpression_mutation_tests(_ex,basicexpressions_mapStaticOperationExpression_counts, basicexpressions_mapStaticOperationExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapInstanceOperationExpression_counts = new int[100]; 
        int[] basicexpressions_mapInstanceOperationExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapInstanceOperationExpression_mutation_tests(_ex,basicexpressions_mapInstanceOperationExpression_counts, basicexpressions_mapInstanceOperationExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapOperationExpression_counts = new int[100]; 
        int[] basicexpressions_mapOperationExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapOperationExpression_mutation_tests(_ex,basicexpressions_mapOperationExpression_counts, basicexpressions_mapOperationExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapIntegerFunctionExpression_counts = new int[100]; 
        int[] basicexpressions_mapIntegerFunctionExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapIntegerFunctionExpression_mutation_tests(_ex,basicexpressions_mapIntegerFunctionExpression_counts, basicexpressions_mapIntegerFunctionExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapInsertAtFunctionExpression_counts = new int[100]; 
        int[] basicexpressions_mapInsertAtFunctionExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapInsertAtFunctionExpression_mutation_tests(_ex,basicexpressions_mapInsertAtFunctionExpression_counts, basicexpressions_mapInsertAtFunctionExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapSetAtFunctionExpression_counts = new int[100]; 
        int[] basicexpressions_mapSetAtFunctionExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapSetAtFunctionExpression_mutation_tests(_ex,basicexpressions_mapSetAtFunctionExpression_counts, basicexpressions_mapSetAtFunctionExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapSubrangeFunctionExpression_counts = new int[100]; 
        int[] basicexpressions_mapSubrangeFunctionExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapSubrangeFunctionExpression_mutation_tests(_ex,basicexpressions_mapSubrangeFunctionExpression_counts, basicexpressions_mapSubrangeFunctionExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapFunctionExpression_counts = new int[100]; 
        int[] basicexpressions_mapFunctionExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapFunctionExpression_mutation_tests(_ex,basicexpressions_mapFunctionExpression_counts, basicexpressions_mapFunctionExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapClassArrayExpression_counts = new int[100]; 
        int[] basicexpressions_mapClassArrayExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapClassArrayExpression_mutation_tests(_ex,basicexpressions_mapClassArrayExpression_counts, basicexpressions_mapClassArrayExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapClassExpression_counts = new int[100]; 
        int[] basicexpressions_mapClassExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapClassExpression_mutation_tests(_ex,basicexpressions_mapClassExpression_counts, basicexpressions_mapClassExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] basicexpressions_mapBasicExpression_counts = new int[100]; 
        int[] basicexpressions_mapBasicExpression_totals = new int[100]; 
        for (int _i = 0; _i < _basicexpressions_instances; _i++)
        { BasicExpression _ex = (BasicExpression) Controller.inst().basicexpressions.get(_i);
          try {
            MutationTest.mapBasicExpression_mutation_tests(_ex,basicexpressions_mapBasicExpression_counts, basicexpressions_mapBasicExpression_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _propertys_instances = Math.min(MAXOBJECTS, Controller.inst().propertys.size());
        int[] propertys_getPKOp_counts = new int[100]; 
        int[] propertys_getPKOp_totals = new int[100]; 
        for (int _i = 0; _i < _propertys_instances; _i++)
        { Property _ex = (Property) Controller.inst().propertys.get(_i);
          try {
            MutationTest.getPKOp_mutation_tests(_ex,propertys_getPKOp_counts, propertys_getPKOp_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] propertys_getPKOps_counts = new int[100]; 
        int[] propertys_getPKOps_totals = new int[100]; 
        for (int _i = 0; _i < _propertys_instances; _i++)
        { Property _ex = (Property) Controller.inst().propertys.get(_i);
          try {
            MutationTest.getPKOps_mutation_tests(_ex,propertys_getPKOps_counts, propertys_getPKOps_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _returnstatements_instances = Math.min(MAXOBJECTS, Controller.inst().returnstatements.size());
        int[] returnstatements_toPython_counts = new int[100]; 
        int[] returnstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _returnstatements_instances; _i++)
        { ReturnStatement _ex = (ReturnStatement) Controller.inst().returnstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,returnstatements_toPython_counts, returnstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _assertstatements_instances = Math.min(MAXOBJECTS, Controller.inst().assertstatements.size());
        int[] assertstatements_toPython_counts = new int[100]; 
        int[] assertstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _assertstatements_instances; _i++)
        { AssertStatement _ex = (AssertStatement) Controller.inst().assertstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,assertstatements_toPython_counts, assertstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _errorstatements_instances = Math.min(MAXOBJECTS, Controller.inst().errorstatements.size());
        int[] errorstatements_toPython_counts = new int[100]; 
        int[] errorstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _errorstatements_instances; _i++)
        { ErrorStatement _ex = (ErrorStatement) Controller.inst().errorstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,errorstatements_toPython_counts, errorstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _catchstatements_instances = Math.min(MAXOBJECTS, Controller.inst().catchstatements.size());
        int[] catchstatements_toPython_counts = new int[100]; 
        int[] catchstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _catchstatements_instances; _i++)
        { CatchStatement _ex = (CatchStatement) Controller.inst().catchstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,catchstatements_toPython_counts, catchstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _finalstatements_instances = Math.min(MAXOBJECTS, Controller.inst().finalstatements.size());
        int[] finalstatements_toPython_counts = new int[100]; 
        int[] finalstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _finalstatements_instances; _i++)
        { FinalStatement _ex = (FinalStatement) Controller.inst().finalstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,finalstatements_toPython_counts, finalstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _operations_instances = Math.min(MAXOBJECTS, Controller.inst().operations.size());
      }

      { int _usecases_instances = Math.min(MAXOBJECTS, Controller.inst().usecases.size());
      }

      { int _breakstatements_instances = Math.min(MAXOBJECTS, Controller.inst().breakstatements.size());
        int[] breakstatements_toPython_counts = new int[100]; 
        int[] breakstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _breakstatements_instances; _i++)
        { BreakStatement _ex = (BreakStatement) Controller.inst().breakstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,breakstatements_toPython_counts, breakstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _continuestatements_instances = Math.min(MAXOBJECTS, Controller.inst().continuestatements.size());
        int[] continuestatements_toPython_counts = new int[100]; 
        int[] continuestatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _continuestatements_instances; _i++)
        { ContinueStatement _ex = (ContinueStatement) Controller.inst().continuestatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,continuestatements_toPython_counts, continuestatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _operationcallstatements_instances = Math.min(MAXOBJECTS, Controller.inst().operationcallstatements.size());
        int[] operationcallstatements_toPython_counts = new int[100]; 
        int[] operationcallstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _operationcallstatements_instances; _i++)
        { OperationCallStatement _ex = (OperationCallStatement) Controller.inst().operationcallstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,operationcallstatements_toPython_counts, operationcallstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _implicitcallstatements_instances = Math.min(MAXOBJECTS, Controller.inst().implicitcallstatements.size());
        int[] implicitcallstatements_toPython_counts = new int[100]; 
        int[] implicitcallstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _implicitcallstatements_instances; _i++)
        { ImplicitCallStatement _ex = (ImplicitCallStatement) Controller.inst().implicitcallstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,implicitcallstatements_toPython_counts, implicitcallstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _boundedloopstatements_instances = Math.min(MAXOBJECTS, Controller.inst().boundedloopstatements.size());
        int[] boundedloopstatements_toPython_counts = new int[100]; 
        int[] boundedloopstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _boundedloopstatements_instances; _i++)
        { BoundedLoopStatement _ex = (BoundedLoopStatement) Controller.inst().boundedloopstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,boundedloopstatements_toPython_counts, boundedloopstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _unboundedloopstatements_instances = Math.min(MAXOBJECTS, Controller.inst().unboundedloopstatements.size());
        int[] unboundedloopstatements_toPython_counts = new int[100]; 
        int[] unboundedloopstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _unboundedloopstatements_instances; _i++)
        { UnboundedLoopStatement _ex = (UnboundedLoopStatement) Controller.inst().unboundedloopstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,unboundedloopstatements_toPython_counts, unboundedloopstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _assignstatements_instances = Math.min(MAXOBJECTS, Controller.inst().assignstatements.size());
        int[] assignstatements_toPython_counts = new int[100]; 
        int[] assignstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _assignstatements_instances; _i++)
        { AssignStatement _ex = (AssignStatement) Controller.inst().assignstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,assignstatements_toPython_counts, assignstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _sequencestatements_instances = Math.min(MAXOBJECTS, Controller.inst().sequencestatements.size());
        int[] sequencestatements_toPython_counts = new int[100]; 
        int[] sequencestatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _sequencestatements_instances; _i++)
        { SequenceStatement _ex = (SequenceStatement) Controller.inst().sequencestatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,sequencestatements_toPython_counts, sequencestatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _trystatements_instances = Math.min(MAXOBJECTS, Controller.inst().trystatements.size());
        int[] trystatements_toPython_counts = new int[100]; 
        int[] trystatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _trystatements_instances; _i++)
        { TryStatement _ex = (TryStatement) Controller.inst().trystatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,trystatements_toPython_counts, trystatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _conditionalstatements_instances = Math.min(MAXOBJECTS, Controller.inst().conditionalstatements.size());
        int[] conditionalstatements_elsecode_counts = new int[100]; 
        int[] conditionalstatements_elsecode_totals = new int[100]; 
        for (int _i = 0; _i < _conditionalstatements_instances; _i++)
        { ConditionalStatement _ex = (ConditionalStatement) Controller.inst().conditionalstatements.get(_i);
          try {
            MutationTest.elsecode_mutation_tests(_ex,conditionalstatements_elsecode_counts, conditionalstatements_elsecode_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
        int[] conditionalstatements_toPython_counts = new int[100]; 
        int[] conditionalstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _conditionalstatements_instances; _i++)
        { ConditionalStatement _ex = (ConditionalStatement) Controller.inst().conditionalstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,conditionalstatements_toPython_counts, conditionalstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      { int _creationstatements_instances = Math.min(MAXOBJECTS, Controller.inst().creationstatements.size());
        int[] creationstatements_toPython_counts = new int[100]; 
        int[] creationstatements_toPython_totals = new int[100]; 
        for (int _i = 0; _i < _creationstatements_instances; _i++)
        { CreationStatement _ex = (CreationStatement) Controller.inst().creationstatements.get(_i);
          try {
            MutationTest.toPython_mutation_tests(_ex,creationstatements_toPython_counts, creationstatements_toPython_totals);
          } catch (Throwable _thrw) { }
        }
        System.out.println();
      }

      return;
    } 
    if ("printcode".equals(cmd))
    {       System.out.println();
      System.out.print(">>> Test: " + "");
      try {  cont.printcode();
      }
      catch (Throwable _e) { System.err.println(" !! Exception occurred: test failed !! "); }

      System.out.println();
      System.out.println();

      return;
    } 
  }

  public static void main(String[] args)
  { TestsGUI gui = new TestsGUI();
    gui.setSize(550,400);
    gui.setVisible(true);
  }
 }
