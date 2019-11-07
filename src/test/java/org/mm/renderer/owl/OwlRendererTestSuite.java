package org.mm.renderer.owl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   CellReferenceTest.class,
   EntityDeclarationTest.class,
   ClassExpressionTest.class,
   AssertionAxiomTest.class,
   EntityTypeDirectiveTest.class,
   IriEncodingDirectiveTest.class,
   EmptyCellDirectiveTest.class,
   AbsentEntityDirectiveTest.class,
   DatatypeDirectiveTest.class,
   LanguageDirectiveTest.class,
   NamespaceDirectiveTest.class,
   PrefixDirectiveTest.class,
   ShiftDirectiveTest.class,
   BuiltInFunctionsTest.class
})
public class OwlRendererTestSuite {
   // NO-OP
}
