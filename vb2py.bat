SET oldclasspath=%CLASSPATH%
SET CLASSPATH=.;.\antlr-4.8-complete.jar;%CLASSPATH%


type %1 | java org.antlr.v4.gui.TestRig VisualBasic6 module -tree >output/ast.txt
java -jar umlrsds.jar -vb2py

SET CLASSPATH=%oldclasspath%

