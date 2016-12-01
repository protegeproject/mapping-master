package org.mm.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationAssertion;
import org.mm.parser.node.ASTAnnotationProperty;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTClassAssertion;
import org.mm.parser.node.ASTClassDeclaration;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTDifferentFrom;
import org.mm.parser.node.ASTEquivalentClasses;
import org.mm.parser.node.ASTFact;
import org.mm.parser.node.ASTIndividualDeclaration;
import org.mm.parser.node.ASTLiteralValue;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTObjectIntersection;
import org.mm.parser.node.ASTObjectProperty;
import org.mm.parser.node.ASTObjectValue;
import org.mm.parser.node.ASTProperty;
import org.mm.parser.node.ASTPropertyAssertion;
import org.mm.parser.node.ASTPropertyValue;
import org.mm.parser.node.ASTReferenceNotation;
import org.mm.parser.node.ASTRuleExpression;
import org.mm.parser.node.ASTSameAs;
import org.mm.parser.node.ASTSubclassOf;
import org.mm.parser.node.ASTValueCategory;
import org.mm.parser.node.Node;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class NodeType<T extends Node> {

   private final String name;
   private final Class<T> actualClass;

   private NodeType(@Nonnull String name, @Nonnull Class<T> actualClass) {
      this.name = checkNotNull(name);
      this.actualClass = checkNotNull(actualClass);
   }

   public String getName() {
      return name;
   }

   public Class<T> getActualClass() {
      return actualClass;
   }

   public static final NodeType<ASTAnnotation> ANNOTATION = new NodeType<>("AnnotationAssertion", ASTAnnotation.class);
   public static final NodeType<ASTAnnotationAssertion> ANNOTATION_ASSERTION = new NodeType<>("AnnotationAssertions", ASTAnnotationAssertion.class);
   public static final NodeType<ASTAnnotationProperty> ANNOTATION_PROPERTY = new NodeType<>("AnnotationProperty", ASTAnnotationProperty.class);
   public static final NodeType<ASTClass> CLASS = new NodeType<>("Class", ASTClass.class);
   public static final NodeType<ASTClassAssertion> CLASS_ASSERTION = new NodeType<>("ClassAssertion", ASTClassAssertion.class);
   public static final NodeType<ASTClassDeclaration> CLASS_DECLARATION = new NodeType<>("ClassDeclaration", ASTClassDeclaration.class);
   public static final NodeType<ASTClassExpressionCategory> CLASS_EXPRESSION = new NodeType<>("ClassExpressionCategory", ASTClassExpressionCategory.class);;
   public static final NodeType<ASTDataProperty> DATA_PROPERTY = new NodeType<>("DataProperty", ASTDataProperty.class);
   public static final NodeType<ASTDifferentFrom> DIFFERENT_FROM = new NodeType<>("DifferentFrom", ASTDifferentFrom.class);
   public static final NodeType<ASTEquivalentClasses> EQUIVALENT_CLASSES = new NodeType<>("EquivalentClasses", ASTEquivalentClasses.class);
   public static final NodeType<ASTFact> FACT = new NodeType<>("Fact", ASTFact.class);
   public static final NodeType<ASTIndividualDeclaration> INDIVIDUAL_DECLARATION = new NodeType<>("IndividualDeclaration", ASTIndividualDeclaration.class);
   public static final NodeType<ASTLiteralValue> LITERAL_VALUE = new NodeType<>("LiteralValue", ASTLiteralValue.class);
   public static final NodeType<ASTNamedIndividual> INDIVIDUAL = new NodeType<>("NamedIndividual", ASTNamedIndividual.class);
   public static final NodeType<ASTObjectIntersection> OBJECT_INTERSECTION = new NodeType<>("ObjectIntersection", ASTObjectIntersection.class);
   public static final NodeType<ASTObjectProperty> OBJECT_PROPERTY = new NodeType<>("ObjectProperty", ASTObjectProperty.class);
   public static final NodeType<ASTObjectValue> OBJECT_VALUE = new NodeType<>("ObjectValue", ASTObjectValue.class);
   public static final NodeType<ASTProperty> PROPERTY = new NodeType<>("Property", ASTProperty.class);
   public static final NodeType<ASTPropertyAssertion> PROPERTY_ASSERTION = new NodeType<>("PropertyAssertion", ASTPropertyAssertion.class);
   public static final NodeType<ASTPropertyValue> PROPERTY_VALUE = new NodeType<>("PropertyValue", ASTPropertyValue.class);
   public static final NodeType<ASTReferenceNotation> REFERENCE_NOTATION = new NodeType<>("ReferenceNotation", ASTReferenceNotation.class);
   public static final NodeType<ASTRuleExpression> RULE_EXPRESSION = new NodeType<>("RuleExpression", ASTRuleExpression.class);
   public static final NodeType<ASTSameAs> SAME_AS = new NodeType<>("SameAs", ASTSameAs.class);
   public static final NodeType<ASTSubclassOf> SUBCLASS_OF = new NodeType<>("SubclassOf", ASTSubclassOf.class);
   public static final NodeType<ASTValueCategory> VALUE = new NodeType<>("ValueCategory", ASTValueCategory.class);
}
