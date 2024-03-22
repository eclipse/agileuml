Obtain the AgileUML tool from https://github.com/eclipse/agileuml

The executable is umlrsds.jar. 

A complete tool directory with plugins for code-generation, etc is in agileuml.zip


Run the tool from the command line as: 

java -jar umlrsds.jar

The main tool window is a class diagram editor. 

Build the executable with:

jar cvfm umlrsds.jar UMLRSDSManifest.txt *.class

then run umlrsds.jar as previously described.

Options on the File menu include: 

Recent -- load a class diagram file from output/mm.txt
Save -- save the class diagram to output/mm.txt


Options on the Create menu include: 

Class -- create a UML class
Type -- create a UML enumeration
Association -- create an association between classes
Inheritance -- create an inheritance between classes

Entity Statemachine -- create a statemachine for a class (eg., a life history of objects of a class)
Operation Statemachine -- create a statemachine showing the steps of an operation execution

Requirements -- create a SysML requirements goal decomposition model. 


Options on the Edit menu include: 

Move, Delete, Modify -- alter the visual elements

Edit KM3 -- edit the class diagram in text form (interactive text editor).



Options on the Synthesis menu include: 

Type-check -- check that types are correct
Generate tests -- generate test cases for classes & operations


Options on the Build menu include: 

Generate P -- for programming languages P being Java, Python, etc


The directory structure is: 

output -- contains most input and output files
output/tests -- generated test cases
cg -- code generator scripts for Java8 (Android) and Swift (iOS)
libraries -- libraries for different languages & services
zoo 
uml2py -- Python code generator
uml2Ca, uml2Cb -- C code generator



Supporting videos are: 

Introduction -- https://www.youtube.com/watch?v=IV2m1TofSyk
Class diagrams -- https://www.youtube.com/watch?v=v1r434xbFuc&t=5s
Generating tests & Python -- https://www.youtube.com/watch?v=NXq2L0fo7N4&t=190s
Generating Java -- https://www.youtube.com/watch?v=NKAK9eITDkw&t=39s




 




