SET oldclasspath=%CLASSPATH%
SET CLASSPATH=.;.\antlr-4.8-complete.jar;%CLASSPATH%


type %1 | java org.antlr.v4.gui.TestRig JavaScript program -tree >output/ast.txt
java -jar umlrsds.jar -js2python

SET CLASSPATH=%oldclasspath%

