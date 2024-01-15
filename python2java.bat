SET oldclasspath=%CLASSPATH%
SET CLASSPATH=.;.\antlr-4.8-complete.jar;%CLASSPATH%


type %1 | java org.antlr.v4.gui.TestRig Python file_input -tree >output/ast.txt
java -jar umlrsds.jar -python2java

SET CLASSPATH=%oldclasspath%

