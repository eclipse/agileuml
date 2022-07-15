@ECHO OFF
SET TEST_CURRENT_DIR=%CLASSPATH:.;=%
if "%TEST_CURRENT_DIR%" == "%CLASSPATH%" ( SET CLASSPATH=.;%CLASSPATH% )
@ECHO ON
type %1 | java org.antlr.v4.gui.TestRig JavaScript program -tree >output/ast.txt
java -jar umlrsds.jar -js2python


