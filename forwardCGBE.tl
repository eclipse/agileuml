{ type = Integer } OclValue |--> ProgramExpression

{ type = Real } OclValue |--> ProgramExpression

{ type = Boolean } OclValue |--> ProgramExpression

{ type = String } OclValue |--> ProgramExpression

{ type = Object } OclValue |--> ProgramExpression


OclIdentifier |--> ProgramExpression


OclFieldAccess |--> ProgramExpression

OclArrayAccess |--> ProgramExpression

OclOperationCall0 |--> ProgramExpression


OclOperationCallN |--> ProgramExpression


OclConditionalExpression |--> ProgramExpression


OclUnaryPrefix |--> ProgramExpression

OclUnaryPostfix |--> ProgramExpression


OclInfixBinaryExpr |--> ProgramExpression

OclArrowOpBinExpr |--> ProgramExpression

OclIteratorExpression |--> ProgramExpression


OclVoidType |--> ProgramType

OclAnyType |--> ProgramType

OclBasicType |--> ProgramType

OclStringType |--> ProgramType

{ _1 = Class } OclClassifierType |--> ProgramType

OclSetType |--> ProgramType

OclSequenceType |--> ProgramType

OclFunctionType |--> ProgramType

OclMapType |--> ProgramType



OclLambdaExpr |--> ProgramExpression

EmptyCollectionExpr |--> ProgramExpression

NonEmptySetExpr |--> ProgramExpression

NonEmptySequenceExpr |--> ProgramExpression


OclBasicStatement |--> ProgramStatement

OclErrorStatement |--> ProgramStatement

OclReturnStatement |--> ProgramStatement

OclCallStatement |--> ProgramStatement

OclImplicitCallStatement |--> ProgramStatement

OclSkipStatement |--> ProgramStatement

OclAssignStatement |--> ProgramStatement

OclAssertStatement |--> ProgramStatement

OclConditionalStatement |--> ProgramStatement

OclSequenceStatement |--> ProgramStatement

OclBlock0 |--> ProgramStatement

OclBlockN |--> ProgramStatement

{ type = Real } OclCreationStatement |--> ProgramStatement

{ type = Integer } OclCreationStatement |--> ProgramStatement

{ type = Boolean } OclCreationStatement |--> ProgramStatement

{ type = String } OclCreationStatement |--> ProgramStatement

{ type = Sequence } OclCreationStatement |--> ProgramStatement

{ type = Set } OclCreationStatement |--> ProgramStatement

{ type = Object } OclCreationStatement |--> ProgramStatement

{ type = Map } OclCreationStatement |--> ProgramStatement

{ type = Function } OclCreationStatement |--> ProgramStatement

OclForStatement |--> ProgramStatement

OclWhileStatement |--> ProgramStatement


OclQuery0 |--> ProgramOperation

OclQueryN |--> ProgramOperation

OclUpdate0 |--> ProgramOperation

OclUpdateN |--> ProgramOperation

OclStaticOp0 |--> ProgramOperation

OclStaticOpN |--> ProgramOperation

{ type = Real } OclParameter |--> ProgramParameter

{ type = Integer } OclParameter |--> ProgramParameter

{ type = Boolean } OclParameter |--> ProgramParameter

{ type = String } OclParameter |--> ProgramParameter

{ type = Real } StaticAttribute |--> ProgramAttribute

{ type = Real } InstanceAttribute |--> ProgramAttribute

{ type = Integer } StaticAttribute |--> ProgramAttribute

{ type = Integer } InstanceAttribute |--> ProgramAttribute

{ type = Boolean } StaticAttribute |--> ProgramAttribute

{ type = Boolean } InstanceAttribute |--> ProgramAttribute

{ type = String } StaticAttribute |--> ProgramAttribute

{ type = String } InstanceAttribute |--> ProgramAttribute

{ type = Sequence } StaticAttribute |--> ProgramAttribute

{ type = Sequence } InstanceAttribute |--> ProgramAttribute

{ type = Set } StaticAttribute |--> ProgramAttribute

{ type = Set } InstanceAttribute |--> ProgramAttribute

{ type = Object } StaticAttribute |--> ProgramAttribute

{ type = Object } InstanceAttribute |--> ProgramAttribute

{ type = Map } StaticAttribute |--> ProgramAttribute

{ type = Map } InstanceAttribute |--> ProgramAttribute

{ type = Function } StaticAttribute |--> ProgramAttribute

{ type = Function } InstanceAttribute |--> ProgramAttribute


OclEmptyClass |--> ProgramClass

OclNonEmptyClass |--> ProgramClass

OclEnumeration |--> ProgramEnumeration


