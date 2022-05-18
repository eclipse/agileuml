@ECHO OFF
SET TEST_CURRENT_DIR=%CLASSPATH:.;=%
if "%TEST_CURRENT_DIR%" == "%CLASSPATH%" ( SET CLASSPATH=.;%CLASSPATH% )
@ECHO ON
java -jar umlrsds.jar -cgtl cg/mobile.cstl %1
java -jar umlrsds.jar -cgtl cg/mobileMain.cstl %1
java -jar umlrsds.jar -cgtl cg/mobileContentView.cstl %1
java -jar umlrsds.jar -cgtl cg/mobileModelFacade.cstl %1
java -jar umlrsds.jar -cgtl cg/mobileFirebaseDbi.cstl %1
java -jar umlrsds.jar -cgtl cg/mobileDbi.cstl %1
java -jar umlrsds.jar -cgtl cg/mobileDAOs.cstl %1
java -jar umlrsds.jar -cgtl cg/mobileValueObjects.cstl %1
java -jar umlrsds.jar -cgtl cg/mobileUseCaseScreens.cstl %1


