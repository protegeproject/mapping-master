package org.mm.renderer;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLDataAllValuesFromNode;
import org.mm.parser.node.OWLDataSomeValuesFromNode;
import org.mm.parser.node.OWLDifferentFromNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLExactCardinalityNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLObjectAllValuesFromNode;
import org.mm.parser.node.OWLObjectOneOfNode;
import org.mm.parser.node.OWLObjectSomeValuesFromNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSameAsNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.TypesNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.rendering.TextReferenceRendering;
import org.mm.rendering.TextRendering;
import org.mm.ss.SpreadSheetDataSource;

import java.util.Optional;

/**
 * This renderer produces a text rendering of a Mapping Master expression with reference values
 * substituted inline.
 */
public class TextRenderer extends BaseReferenceRenderer
  implements CoreRenderer, OWLEntityRenderer, OWLLiteralRenderer, ReferenceRenderer, OWLClassExpressionRenderer,
  MappingMasterParserConstants
{
  private int defaultValueEncoding = RDFS_LABEL;
  private int defaultReferenceType = OWL_CLASS;
  private int defaultOWLPropertyType = OWL_OBJECT_PROPERTY;
  private int defaultOWLPropertyAssertionObjectType = XSD_STRING;
  private int defaultOWLDataPropertyValueType = XSD_STRING;

  public TextRenderer(SpreadSheetDataSource dataSource)
  {
    super(dataSource);
  }

  @Override public Optional<TextReferenceRendering> renderReference(ReferenceNode referenceNode)
    throws RendererException
  {
    // TODO
    return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderExpression(ExpressionNode expressionNode)
    throws RendererException
  {
    if (expressionNode.hasMMExpression())
      return renderMMExpression(expressionNode.getMMExpressionNode());
    else
      throw new RendererException("unknown expression node " + expressionNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderMMExpression(MMExpressionNode mmExpressionNode)
    throws RendererException
  {
    if (mmExpressionNode.hasOWLClassDeclaration())
      return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
    else if (mmExpressionNode.hasOWLIndividualDeclaration())
      return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
    else
      throw new RendererException("unknown Mapping Master expression node " + mmExpressionNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLClassDeclaration(
    OWLClassDeclarationNode classDeclarationNode) throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder("Class: " + classDeclarationNode.getOWLClassNode().toString());
    boolean isFirst = true;

    if (classDeclarationNode.hasOWLSubclassOfNodes()) {
      textRepresentation.append(" SubclassOf: ");
      for (OWLSubclassOfNode subclassOf : classDeclarationNode.getOWLSubclassOfNodes()) {
        Optional<? extends TextRendering> subclassOfRendering = renderOWLSubclassOf(
          classDeclarationNode.getOWLClassNode(), subclassOf);
        if (!subclassOfRendering.isPresent())
          continue;

        if (!isFirst)
          textRepresentation.append(", ");

        textRepresentation.append(subclassOfRendering.get().getTextRendering());
        isFirst = false;
      }
    }

    isFirst = true;
    if (classDeclarationNode.hasOWLEquivalentClassesNode()) {
      textRepresentation.append(" EquivalentTo: ");
      for (OWLEquivalentClassesNode equivalentTo : classDeclarationNode.getOWLEquivalentClassesNodes()) {
        Optional<? extends TextRendering> equivalentToRendering = renderOWLEquivalentClasses(
          classDeclarationNode.getOWLClassNode(), equivalentTo);
        if (!equivalentToRendering.isPresent())
          continue;

        if (!isFirst)
          textRepresentation.append(", ");

        textRepresentation.append(equivalentToRendering.get().getTextRendering());
        isFirst = false;
      }
    }

    isFirst = true;
    if (classDeclarationNode.hasAnnotationFactNodes()) {
      textRepresentation.append(" Annotations: ");
      for (AnnotationFactNode annotationFactNode : classDeclarationNode.getAnnotationFactNodes()) {
        Optional<? extends TextRendering> factRendering = renderAnnotationFact(annotationFactNode);

        if (factRendering.isPresent())
          continue;

        if (!isFirst)
          textRepresentation.append(", ");
        textRepresentation.append(factRendering.get().getTextRendering());
        isFirst = false;
      }
    }

    return textRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering(textRepresentation.toString()));
  }

  @Override public Optional<? extends TextRendering> renderOWLIndividualDeclaration(
    OWLIndividualDeclarationNode individualDeclarationNode) throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder();
    boolean isFirst = true;
    Optional<? extends TextRendering> individualRendering = renderOWLNamedIndividual(
      individualDeclarationNode.getOWLIndividualNode());

    if (!individualRendering.isPresent())
      return Optional.empty();

    textRepresentation.append("Individual: ");

    textRepresentation.append(individualRendering.get().getTextRendering());

    if (individualDeclarationNode.hasFacts()) {
      textRepresentation.append(" Facts: ");

      for (FactNode fact : individualDeclarationNode.getFactNodes()) {
        Optional<? extends TextRendering> factRendering = renderFact(fact);

        if (!factRendering.isPresent())
          continue;

        if (!isFirst)
          textRepresentation.append(", ");
        textRepresentation.append(factRendering.get().getTextRendering());
        isFirst = false;
      }
    }

    if (individualDeclarationNode.hasTypes()) {
      Optional<? extends TextRendering> typesRendering = renderTypes(individualDeclarationNode.getTypesNode());

      if (typesRendering.isPresent())
        textRepresentation.append(" Types: " + typesRendering.get().getTextRendering());
    }

    isFirst = true;
    if (individualDeclarationNode.hasAnnotations()) {
      textRepresentation.append(" Annotations:");

      for (AnnotationFactNode factNode : individualDeclarationNode.getAnnotationNodes()) {
        Optional<? extends TextRendering> factRendering = renderAnnotationFact(factNode);

        if (!factRendering.isPresent())
          continue;

        if (!isFirst)
          textRepresentation.append(", ");
        textRepresentation.append(factRendering.get().getTextRendering());
        isFirst = false;
      }
    }

    if (individualDeclarationNode.hasSameAs()) {
      Optional<? extends TextRendering> sameAsRendering = renderOWLSameAs(individualDeclarationNode.getOWLSameAsNode());
      if (sameAsRendering.isPresent())
        textRepresentation.append(sameAsRendering.get().getTextRendering());
    }

    if (individualDeclarationNode.hasDifferentFrom()) {
      Optional<? extends TextRendering> differentFromRendering = renderDifferentFrom(
        individualDeclarationNode.getOWLDifferentFromNode());
      if (differentFromRendering.isPresent())
        textRepresentation.append(differentFromRendering.get().getTextRendering());
    }

    return Optional.of(new TextRendering(textRepresentation.toString()));
  }

  @Override public Optional<? extends TextRendering> renderOWLClassExpression(
    OWLClassExpressionNode classExpressionNode) throws RendererException
  {
    StringBuilder textRendering = new StringBuilder();

    if (classExpressionNode.hasOWLObjectOneOfNode()) {
      Optional<? extends TextRendering> objectOneOfRendering = renderOWLObjectOneOf(
        classExpressionNode.getOWLObjectOneOfNode());
      if (objectOneOfRendering.isPresent())
        textRendering.append(objectOneOfRendering.get().getTextRendering());
    } else if (classExpressionNode.hasOWLUnionClassNode()) {
      Optional<? extends TextRendering> unionClassRendering = renderOWLUnionClass(
        classExpressionNode.getOWLUnionClassNode());
      if (unionClassRendering.isPresent())
        textRendering.append(unionClassRendering.get().getTextRendering());
    } else if (classExpressionNode.hasOWLRestrictionNode()) {
      Optional<? extends TextRendering> restrictionRendering = renderOWLRestriction(
        classExpressionNode.getOWLRestrictionNode());
      if (restrictionRendering.isPresent())
        textRendering.append(restrictionRendering.get().getTextRendering());
    } else if (classExpressionNode.hasOWLClassNode()) {
      Optional<? extends TextRendering> classRendering = renderOWLClass(classExpressionNode.getOWLClassNode());
      if (classRendering.isPresent())
        textRendering.append(classRendering.get().getTextRendering());
    } else
      throw new RendererException("unexpected child for node " + classExpressionNode.getNodeName());

    if (textRendering.length() == 0)
      if (classExpressionNode.getIsNegated())
        textRendering.append("NOT " + textRendering);

    return textRendering.length() != 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
  }

  @Override public Optional<? extends TextRendering> renderOWLIntersectionClass(
    OWLIntersectionClassNode intersectionClassNode) throws RendererException
  {
    if (intersectionClassNode.getOWLClassExpressionNodes().size() == 1) {
      Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(
        intersectionClassNode.getOWLClassExpressionNodes().get(0));

      return classExpressionRendering;
    } else {
      StringBuilder textRepresentation = new StringBuilder();
      boolean isFirst = true;

      for (OWLClassExpressionNode classExpressionNode : intersectionClassNode.getOWLClassExpressionNodes()) {
        Optional<? extends TextRendering> classesExpressionRendering = renderOWLClassExpression(classExpressionNode);

        if (classesExpressionRendering.isPresent()) {
          if (isFirst)
            textRepresentation.append("(");
          else
            textRepresentation.append(" AND ");
          textRepresentation.append(classesExpressionRendering.get().getTextRendering());
          isFirst = false;
        }
      }
      if (textRepresentation.length() != 0)
        textRepresentation.append(")");
      return Optional.of(new TextRendering(textRepresentation.toString()));
    }
  }

  @Override public Optional<? extends TextRendering> renderOWLEquivalentClasses(OWLClassNode declaredClassNode,
    OWLEquivalentClassesNode equivalentClassesNode) throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder();

    textRepresentation.append(" EquivalentTo: ");

    if (equivalentClassesNode.getClassExpressionNodes().size() == 1) {
      Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(
        equivalentClassesNode.getClassExpressionNodes().get(0));
      if (!classExpressionRendering.isPresent())
        return classExpressionRendering;
      else
        textRepresentation.append(classExpressionRendering.get().getTextRendering());
    } else {
      boolean isFirst = true;

      for (OWLClassExpressionNode owlClassExpressionNode : equivalentClassesNode.getClassExpressionNodes()) {
        Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
        if (!classExpressionRendering.isPresent())
          continue; // Any empty class expression will generate an empty rendering
        if (!isFirst)
          textRepresentation.append(", ");
        textRepresentation.append(classExpressionRendering.get().getTextRendering());
        isFirst = false;
      }
    }
    return textRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering(textRepresentation.toString()));
  }

  @Override public Optional<? extends TextRendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode)
    throws RendererException
  {
    if (unionClassNode.getOWLIntersectionClassNodes().size() == 1) {
      Optional<? extends TextRendering> intersectionRendering = renderOWLIntersectionClass(
        unionClassNode.getOWLIntersectionClassNodes().get(0));

      return intersectionRendering;
    } else {
      StringBuilder textRepresentation = new StringBuilder();
      boolean isFirst = true;

      for (OWLIntersectionClassNode intersectionClassNode : unionClassNode.getOWLIntersectionClassNodes()) {
        Optional<? extends TextRendering> intersectionRendering = renderOWLIntersectionClass(intersectionClassNode);

        if (intersectionRendering.isPresent()) {
          if (isFirst)
            textRepresentation.append("(");
          else
            textRepresentation.append(" OR ");
          textRepresentation.append(intersectionRendering.get().getTextRendering());
          isFirst = false;
        }
      }
      if (textRepresentation.length() != 0)
        textRepresentation.append(")");

      return textRepresentation.length() == 0 ?
        Optional.empty() :
        Optional.of(new TextRendering(textRepresentation.toString()));
    }
  }

  @Override public Optional<? extends TextRendering> renderOWLProperty(OWLPropertyNode propertyNode)
    throws RendererException
  {
    if (propertyNode.hasReferenceNode())
      return renderReference(propertyNode.getReferenceNode());
    else if (propertyNode.hasNameNode())
      return renderName(propertyNode.getNameNode());
    else
      throw new RendererException("unknown child for node " + propertyNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode)
    throws RendererException
  {
    if (propertyNode.hasReferenceNode())
      return renderReference(propertyNode.getReferenceNode());
    else if (propertyNode.hasNameNode())
      return renderName(propertyNode.getNameNode());
    else
      throw new RendererException("unknown child for node " + propertyNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLRestriction(OWLRestrictionNode restrictionNode)
    throws RendererException
  {
    Optional<? extends TextRendering> propertyRendering = renderOWLProperty(restrictionNode.getOWLPropertyNode());
    Optional<? extends TextRendering> restrictionRendering;

    if (restrictionNode.isOWLMinCardinality())
      restrictionRendering = renderOWLMinCardinality(restrictionNode.getOWLPropertyNode(),
        restrictionNode.getOWLMinCardinalityNode());
    else if (restrictionNode.isOWLMaxCardinality())
      restrictionRendering = renderOWLMaxCardinality(restrictionNode.getOWLPropertyNode(),
        restrictionNode.getOWLMaxCardinalityNode());
    else if (restrictionNode.isOWLExactCardinality())
      restrictionRendering = renderOWLExactCardinality(restrictionNode.getOWLPropertyNode(),
        restrictionNode.getOWLExactCardinalityNode());
    else if (restrictionNode.isOWLHasValue())
      restrictionRendering = renderOWLHasValue(restrictionNode.getOWLPropertyNode(),
        restrictionNode.getOWLHasValueNode());
    else if (restrictionNode.isOWLAllValuesFrom())
      restrictionRendering = renderOWLAllValuesFrom(restrictionNode.getOWLPropertyNode(),
        restrictionNode.getOWLAllValuesFromNode());
    else if (restrictionNode.isOWLSomeValuesFrom())
      restrictionRendering = renderOWLSomeValuesFrom(restrictionNode.getOWLPropertyNode(),
        restrictionNode.getOWLSomeValuesFromNode());
    else
      throw new RendererException("unknown child for node " + restrictionNode.getNodeName());

    if (propertyRendering.isPresent() && restrictionRendering.isPresent())
      return Optional.of(new TextRendering(
        "(" + propertyRendering.get().getTextRendering() + " " + restrictionRendering.get().getTextRendering() + ")"));
    else
      return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderOWLObjectHasValue(OWLPropertyNode propertyNode,
    OWLHasValueNode hasValueNode) throws RendererException
  {
    if (hasValueNode.hasReferenceNode())
      return renderReference(hasValueNode.getReferenceNode());
    else if (hasValueNode.hasNameNone())
      return renderName(hasValueNode.getNameNode());
    else
      throw new RendererException("unknown child for node " + hasValueNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLDataHasValue(OWLPropertyNode propertyNode,
    OWLHasValueNode dataHasValueNode) throws RendererException
  {
    if (dataHasValueNode.hasReferenceNode())
      return renderReference(dataHasValueNode.getReferenceNode());
    else if (dataHasValueNode.hasLiteralNode())
      return renderOWLLiteral(dataHasValueNode.getOWLLiteralNode());
    else
      throw new RendererException("unknown child for node " + dataHasValueNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
    OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException
  {
    String datatypeName = dataAllValuesFromNode.getDatatypeName();

    if (!datatypeName.equals(""))
      return Optional.of(new TextRendering("ONLY " + datatypeName));
    else
      return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderOWLDataSomeValuesFrom(OWLPropertyNode propertyNode,
    OWLDataSomeValuesFromNode dataSomeValuesFromNode) throws RendererException
  {
    String datatypeName = dataSomeValuesFromNode.getDatatypeName();

    if (!datatypeName.equals(""))
      return Optional.of(new TextRendering("SOME " + datatypeName));
    else
      return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode propertyNode,
    OWLObjectSomeValuesFromNode objectSomeValuesFromNode) throws RendererException
  {
    Optional<? extends TextRendering> classRendering;

    if (objectSomeValuesFromNode.hasOWLClassNode())
      classRendering = renderOWLClass(objectSomeValuesFromNode.getOWLClassNode());
    else if (objectSomeValuesFromNode.hasOWLClassExpressionNode())
      classRendering = renderOWLClassExpression(objectSomeValuesFromNode.getOWLClassExpressionNode());
    else
      throw new RendererException("unknown child for node " + objectSomeValuesFromNode.getNodeName());

    if (classRendering.isPresent())
      return Optional.of(new TextRendering("SOME " + classRendering.get().getTextRendering()));
    else
      return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderOWLSubclassOf(OWLClassNode declaredClassNode,
    OWLSubclassOfNode subclassOfNode) throws RendererException
  {
    StringBuilder subClassesRepresentation = new StringBuilder();

    if (subclassOfNode.getClassExpressionNodes().size() == 1) {
      Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(
        subclassOfNode.getClassExpressionNodes().get(0));
      if (!classExpressionRendering.isPresent())
        return Optional.empty();
      else
        subClassesRepresentation.append(classExpressionRendering.get().getTextRendering());
    } else {
      boolean isFirst = true;

      for (OWLClassExpressionNode classExpressionNode : subclassOfNode.getClassExpressionNodes()) {
        Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(classExpressionNode);
        if (!classExpressionRendering.isPresent())
          continue; // Any empty class expression will generate an empty rendering
        if (!isFirst)
          subClassesRepresentation.append(", ");
        subClassesRepresentation.append(classExpressionRendering.get().getTextRendering());
        isFirst = false;
      }
    }

    if (subClassesRepresentation.length() != 0)
      return Optional.of(new TextRendering(" SubClassOf: " + subClassesRepresentation));
    else
      return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderOWLSameAs(OWLSameAsNode OWLSameAsNode)
    throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder();
    boolean isFirst = true;

    for (OWLNamedIndividualNode owlNamedIndividualNode : OWLSameAsNode.getIndividualNodes()) {
      Optional<? extends TextRendering> namedIndividualRendering = renderOWLNamedIndividual(owlNamedIndividualNode);

      if (namedIndividualRendering.isPresent()) {
        if (!isFirst)
          textRepresentation.append(", ");
        textRepresentation.append(namedIndividualRendering.get().getTextRendering());
        isFirst = false;
      }
    }
    return textRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering(" SameAs: " + textRepresentation.toString()));
  }

  @Override public Optional<? extends TextRendering> renderOWLPropertyAssertionObject(
    OWLPropertyAssertionObjectNode propertyAssertionObjectNode) throws RendererException
  {
    if (propertyAssertionObjectNode.isReference())
      return renderReference(propertyAssertionObjectNode.getReferenceNode());
    else if (propertyAssertionObjectNode.isName())
      return renderName(propertyAssertionObjectNode.getNameNode());
    else if (propertyAssertionObjectNode.isLiteral())
      return renderOWLLiteral(propertyAssertionObjectNode.getOWLLiteralNode());
    else
      throw new RendererException("unknown property value node " + propertyAssertionObjectNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderAnnotationFact(AnnotationFactNode annotationFactNode)
    throws RendererException
  {
    Optional<? extends TextRendering> propertyRendering = renderOWLProperty(annotationFactNode.getOWLPropertyNode());
    Optional<? extends TextRendering> annotationValueRendering = renderOWLAnnotationValue(
      annotationFactNode.getOWLAnnotationValueNode());

    if (propertyRendering.isPresent() && annotationValueRendering.isPresent()) {
      String textRepresentation =
        propertyRendering.get().getTextRendering() + " " + annotationValueRendering.get().getTextRendering();
      return Optional.of(new TextRendering(textRepresentation));
    } else
      return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderOWLObjectOneOf(OWLObjectOneOfNode objectOneOfNode)
    throws RendererException
  {
    StringBuilder textRendering = new StringBuilder();

    if (objectOneOfNode.getOWLNamedIndividualNodes().size() == 1) {
      Optional<? extends TextRendering> individualRendering = renderOWLNamedIndividual(
        objectOneOfNode.getOWLNamedIndividualNodes().get(0));

      if (!individualRendering.isPresent())
        return Optional.empty();
      else
        textRendering.append(individualRendering.get().getTextRendering());
    } else {
      boolean isFirst = true;

      textRendering.append("{");
      for (OWLNamedIndividualNode owlNamedIndividualNode : objectOneOfNode.getOWLNamedIndividualNodes()) {
        if (!isFirst)
          textRendering.append(" ");
        Optional<? extends TextRendering> individualRendering = renderOWLNamedIndividual(owlNamedIndividualNode);

        if (!individualRendering.isPresent())
          return Optional.empty();
        else {
          textRendering.append(individualRendering.get().getTextRendering());
          isFirst = false;
        }
      }
      textRendering.append("}");
    }
    return textRendering.length() != 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
  }

  @Override public Optional<? extends TextRendering> renderFact(FactNode factNode) throws RendererException
  {
    Optional<? extends TextRendering> propertyRendering = renderOWLProperty(factNode.getOWLPropertyNode());
    Optional<? extends TextRendering> propertyValueRendering = renderOWLPropertyAssertionObject(
      factNode.getOWLPropertyAssertionObjectNode());

    if (propertyRendering.isPresent() && propertyValueRendering.isPresent()) {
      String textRendering =
        propertyRendering.get().getTextRendering() + " " + propertyValueRendering.get().getTextRendering();
      return Optional.of(new TextRendering(textRendering));
    } else
      return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderOWLAnnotationValue(
    OWLAnnotationValueNode annotationValueNode) throws RendererException
  {
    return Optional.empty(); // TODO
  }

  @Override public Optional<? extends TextRendering> renderOWLObjectExactCardinality(OWLPropertyNode propertyNode,
    OWLExactCardinalityNode exactCardinalityNode) throws RendererException
  {
    return renderOWLExactCardinality(propertyNode, exactCardinalityNode);
  }

  @Override public Optional<? extends TextRendering> renderOWLDataExactCardinality(OWLPropertyNode propertyNode,
    OWLExactCardinalityNode exactCardinalityNode) throws RendererException
  {
    return renderOWLExactCardinality(propertyNode, exactCardinalityNode);
  }

  @Override public Optional<? extends TextRendering> renderOWLObjectMaxCardinality(OWLPropertyNode propertyNode,
    OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
  {
    return renderOWLMaxCardinality(propertyNode, maxCardinalityNode);
  }

  @Override public Optional<? extends TextRendering> renderOWLDataMaxCardinality(OWLPropertyNode propertyNode,
    OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
  {
    return renderOWLMaxCardinality(propertyNode, maxCardinalityNode);
  }

  @Override public Optional<? extends TextRendering> renderOWLObjectMinCardinality(OWLPropertyNode propertyNode,
    OWLMinCardinalityNode minCardinalityNode) throws RendererException
  {
    return renderOWLMinCardinality(propertyNode, minCardinalityNode);
  }

  @Override public Optional<? extends TextRendering> renderOWLDataMinCardinality(OWLPropertyNode propertyNode,
    OWLMinCardinalityNode minCardinalityNode) throws RendererException
  {
    return renderOWLMinCardinality(propertyNode, minCardinalityNode);
  }

  @Override public Optional<? extends TextRendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
    OWLObjectAllValuesFromNode objectAllValuesFromNode) throws RendererException
  {
    Optional<? extends TextRendering> classRendering;

    if (objectAllValuesFromNode.hasOWLClass())
      classRendering = renderOWLClass(objectAllValuesFromNode.getOWLClassNode());
    else if (objectAllValuesFromNode.hasOWLClassExpression())
      classRendering = renderOWLClassExpression(objectAllValuesFromNode.getOWLClassExpressionNode());
    else
      throw new RendererException("unknown child for node " + objectAllValuesFromNode.getNodeName());

    if (classRendering.isPresent())
      return Optional.of(new TextRendering("ONLY " + classRendering.get().getTextRendering()));
    else
      return Optional.empty();
  }

  @Override public Optional<? extends TextRendering> renderOWLClass(OWLClassNode classNode) throws RendererException
  {
    if (classNode.hasReferenceNode())
      return renderReference(classNode.getReferenceNode());
    else if (classNode.hasNameNode())
      return renderName(classNode.getNameNode());
    else
      throw new RendererException("unknown child for node " + classNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode)
    throws RendererException
  {
    if (propertyNode.hasReferenceNode())
      return renderReference(propertyNode.getReferenceNode());
    else if (propertyNode.hasNameNode())
      return renderName(propertyNode.getNameNode());
    else
      throw new RendererException("unknown child for node " + propertyNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLDataProperty(OWLPropertyNode propertyNode)
    throws RendererException
  {
    if (propertyNode.hasReferenceNode())
      return renderReference(propertyNode.getReferenceNode());
    else if (propertyNode.hasNameNode())
      return renderName(propertyNode.getNameNode());
    else
      throw new RendererException("unknown child for node " + propertyNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLNamedIndividual(
    OWLNamedIndividualNode namedIndividualNode) throws RendererException
  {
    if (namedIndividualNode.hasReferenceNode())
      return renderReference(namedIndividualNode.getReferenceNode());
    else if (namedIndividualNode.hasNameNode())
      return renderName(namedIndividualNode.getNameNode());
    else
      throw new RendererException("unknown child for node " + namedIndividualNode.getNodeName());
  }

  @Override public Optional<? extends TextRendering> renderOWLLiteral(OWLLiteralNode literalNode)
    throws RendererException
  {
    if (literalNode.isInteger())
      return Optional.of(new TextRendering(literalNode.getIntLiteralNode().toString()));
    else if (literalNode.isFloat())
      return Optional.of(new TextRendering(literalNode.getFloatLiteralNode().toString()));
    else if (literalNode.isString())
      return Optional.of(new TextRendering(literalNode.toString()));
    else if (literalNode.isBoolean())
      return Optional.of(new TextRendering(literalNode.getBooleanLiteralNode().toString()));
    else
      throw new RendererException("unknown child for node " + literalNode.getNodeName());
  }

  public Optional<? extends TextRendering> renderDifferentFrom(OWLDifferentFromNode differentFromNode)
    throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder();
    boolean isFirst = true;

    for (OWLNamedIndividualNode namedIndividualNode : differentFromNode.getNamedIndividualNodes()) {
      Optional<? extends TextRendering> individualRendering = renderOWLNamedIndividual(namedIndividualNode);

      if (individualRendering.isPresent()) {
        if (!isFirst)
          textRepresentation.append(", ");
        textRepresentation.append(individualRendering.get().getTextRendering());
        isFirst = false;
      }
    }
    return textRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering(" DifferentFrom: " + textRepresentation.toString()));
  }

  @Override public int getDefaultValueEncoding()
  {
    return this.defaultValueEncoding;
  }

  @Override public int getDefaultReferenceType()
  {
    return this.defaultReferenceType;
  }

  @Override public int getDefaultOWLPropertyType()
  {
    return this.defaultOWLPropertyType;
  }

  @Override public int getDefaultOWLPropertyAssertionObjectType()
  {
    return this.defaultOWLPropertyAssertionObjectType;
  }

  @Override public int getDefaultOWLDataPropertyValueType()
  {
    return this.defaultOWLDataPropertyValueType;
  }

  @Override public void setDefaultValueEncoding(int defaultValueEncoding)
  {
    this.defaultValueEncoding = defaultValueEncoding;
  }

  @Override public void setDefaultReferenceType(int defaultReferenceType)
  {
    this.defaultReferenceType = defaultReferenceType;
  }

  @Override public void setDefaultOWLPropertyType(int defaultOWLPropertyType)
  {
    this.defaultOWLDataPropertyValueType = defaultOWLPropertyType;
  }

  @Override public void setDefaultOWLPropertyAssertionObjectType(int defaultOWLPropertyAssertionObjectType)
  {
    this.defaultOWLPropertyAssertionObjectType = defaultOWLPropertyAssertionObjectType;
  }

  @Override public void setDefaultOWLDataPropertyValueType(int defaultOWLDataPropertyValueType)
  {
    this.defaultOWLDataPropertyValueType = defaultOWLDataPropertyValueType;
  }

  @Override public OWLEntityRenderer getOWLEntityRenderer() { return this; }

  @Override public OWLClassExpressionRenderer getOWLClassExpressionRenderer()
  {
    return this;
  }

  @Override public ReferenceRenderer getReferenceRenderer()
  {
    return this;
  }

  @Override public OWLLiteralRenderer getOWLLiteralRenderer()
  {
    return this;
  }

  private Optional<? extends TextRendering> renderOWLAllValuesFrom(OWLPropertyNode propertyNode,
    OWLAllValuesFromNode allValuesFromNode) throws RendererException
  {
    if (allValuesFromNode.hasOWLDataAllValuesFromNode())
      return renderOWLDataAllValuesFrom(propertyNode, allValuesFromNode.getOWLDataAllValuesFromNode());
    else if (allValuesFromNode.hasOWLObjectAllValuesFromNode())
      return renderOWLObjectAllValuesFrom(propertyNode, allValuesFromNode.getObjectOWLAllValuesFromNode());
    else
      throw new RendererException("unknown child for node " + allValuesFromNode.getNodeName());
  }

  private Optional<? extends TextRendering> renderValueExtractionFunctionArgument(
    ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
  {
    if (valueExtractionFunctionArgumentNode.isOWLLiteralNode())
      return renderOWLLiteral(valueExtractionFunctionArgumentNode.getOWLLiteralNode());
    else if (valueExtractionFunctionArgumentNode.isReferenceNode())
      return renderReference(valueExtractionFunctionArgumentNode.getReferenceNode());
    else
      throw new RendererException("unknown child for node " + valueExtractionFunctionArgumentNode.getNodeName());
  }

  private Optional<? extends TextRendering> renderOWLSomeValuesFrom(OWLPropertyNode propertyNode,
    OWLSomeValuesFromNode someValuesFromNode) throws RendererException
  {
    if (someValuesFromNode.hasOWLDataSomeValuesFromNode())
      return renderOWLDataSomeValuesFrom(propertyNode, someValuesFromNode.getOWLDataSomeValuesFromNode());
    else if (someValuesFromNode.hasOWLObjectSomeValuesFrom())
      return renderOWLObjectSomeValuesFrom(propertyNode, someValuesFromNode.getOWLObjectSomeValuesFromNode());
    else
      throw new RendererException("unknown child for node " + someValuesFromNode.getNodeName());
  }

  private Optional<? extends TextRendering> renderOWLHasValue(OWLPropertyNode propertyNode,
    OWLHasValueNode hasValueNode) throws RendererException
  {
    if (hasValueNode.hasReferenceNode())
      return renderReference(hasValueNode.getReferenceNode());
    else if (hasValueNode.hasNameNone())
      return renderName(hasValueNode.getNameNode());
    else if (hasValueNode.hasLiteralNode())
      return renderOWLLiteral(hasValueNode.getOWLLiteralNode());
    else
      throw new RendererException("unknown child for node " + hasValueNode.getNodeName());
  }

  private Optional<? extends TextRendering> renderOWLExactCardinality(OWLPropertyNode propertyNode,
    OWLExactCardinalityNode owlExactCardinalityNode) throws RendererException
  {
    String textRendering = "" + owlExactCardinalityNode.getCardinality();

    if (textRendering.length() != 0)
      textRendering += "EXACTLY " + textRendering;

    return textRendering.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering));
  }

  private Optional<? extends TextRendering> renderOWLMaxCardinality(OWLPropertyNode propertyNode,
    OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
  {
    String textRendering = "" + maxCardinalityNode.getCardinality();

    if (textRendering.length() != 0)
      return Optional.of(new TextRendering("MAX " + textRendering));
    else
      return Optional.empty();
  }

  private Optional<? extends TextRendering> renderOWLMinCardinality(OWLPropertyNode propertyNode,
    OWLMinCardinalityNode minCardinalityNode) throws RendererException
  {
    String textRendering = "" + minCardinalityNode.getCardinality();

    if (textRendering.length() != 0)
      return Optional.of(new TextRendering("MIN " + textRendering));
    else
      return Optional.empty();
  }

  private Optional<? extends TextRendering> renderName(NameNode nameNode) throws RendererException
  {
    String name = nameNode.isQuoted() ? "'" + nameNode.getName() + "'" : nameNode.getName();

    return name.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(name));
  }

  private Optional<? extends TextRendering> renderSourceSpecification(SourceSpecificationNode sourceSpecificationNode)
    throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder();

    if (sourceSpecificationNode.hasSource())
      textRepresentation.append("'" + sourceSpecificationNode.getSource() + "'!");

    if (sourceSpecificationNode.hasLocation())
      textRepresentation.append(sourceSpecificationNode.getLocation());
    else
      textRepresentation.append("\"" + sourceSpecificationNode.getLiteral() + "\""); // A literal

    return textRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering("@" + textRepresentation.toString()));
  }

  private Optional<? extends TextRendering> renderType(TypeNode typeNode) throws RendererException
  {
    if (typeNode instanceof ReferenceNode)
      return renderReference((ReferenceNode)typeNode);
    else if (typeNode instanceof OWLClassExpressionNode)
      return renderOWLClassExpression((OWLClassExpressionNode)typeNode);
    else
      throw new RendererException("do not know how to render type node " + typeNode.getNodeName());
  }

  private Optional<? extends TextRendering> renderValueEncoding(ValueEncodingNode valueEncodingNode)
    throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder();

    textRepresentation.append(valueEncodingNode.getEncodingTypeName());

    if (valueEncodingNode.hasValueSpecificationNode()) {
      Optional<? extends TextRendering> valueSpecificationRendering = renderValueSpecification(
        valueEncodingNode.getValueSpecificationNode());
      if (valueSpecificationRendering.isPresent())
        textRepresentation.append(valueSpecificationRendering.get().getTextRendering());
    }
    return textRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering(textRepresentation.toString()));
  }

  private Optional<? extends TextRendering> renderTypes(TypesNode types) throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder();
    boolean isFirst = true;

    for (TypeNode typeNode : types.getTypeNodes()) {
      Optional<? extends TextRendering> typeRendering = renderType(typeNode);
      if (typeRendering.isPresent()) {
        if (!isFirst)
          textRepresentation.append(", ");
        textRepresentation.append(typeRendering.get().getTextRendering());
        isFirst = false;
      }
    }
    return textRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering(textRepresentation.toString()));
  }

  private Optional<? extends TextRendering> renderValueSpecification(
    ValueSpecificationNode valueSpecificationNode) throws RendererException
  {
    StringBuilder textRepresentation = new StringBuilder();
    boolean isFirst = true;

    if (valueSpecificationNode.getNumberOfValueSpecificationItems() > 1)
      textRepresentation.append("(");

    for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
      .getValueSpecificationItemNodes()) {
      Optional<? extends TextRendering> valueSpecificationItemRendering = renderValueSpecificationItem(
        valueSpecificationItemNode);

      if (valueSpecificationItemRendering.isPresent()) {
        if (isFirst)
          textRepresentation.append("=");
        else
          textRepresentation.append(", ");
        textRepresentation.append(valueSpecificationItemRendering.get().getTextRendering());
        isFirst = false;
      }
    }

    if (valueSpecificationNode.getNumberOfValueSpecificationItems() > 1)
      textRepresentation.append(")");

    return textRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering(textRepresentation.toString()));
  }

  private Optional<? extends TextRendering> renderValueSpecificationItem(
    ValueSpecificationItemNode valueSpecificationItemNode) throws RendererException
  {
    if (valueSpecificationItemNode.hasStringLiteral())
      return Optional.of(new TextRendering("\"" + valueSpecificationItemNode.getStringLiteral() + "\""));
    else if (valueSpecificationItemNode.hasReferenceNode())
      return renderReference(valueSpecificationItemNode.getReferenceNode());
    else if (valueSpecificationItemNode.hasValueExtractionFunctionNode())
      return renderValueExtractionFunction(valueSpecificationItemNode.getValueExtractionFunctionNode());
    else if (valueSpecificationItemNode.hasCapturingExpression())
      return Optional.of(new TextRendering("[\"" + valueSpecificationItemNode.getCapturingExpression() + "\"]"));
    else
      throw new RendererException("unknown ValueSpecificationItem node " + valueSpecificationItemNode);
  }

  private Optional<? extends TextRendering> renderValueExtractionFunction(
    ValueExtractionFunctionNode valueExtractionFunctionNode) throws RendererException
  {
    String valueExtractionFunctionName = valueExtractionFunctionNode.getFunctionName();
    StringBuilder functionArgumentsRepresentation = new StringBuilder();

    if (valueExtractionFunctionNode.hasArguments()) {
      boolean isFirst = true;
      functionArgumentsRepresentation.append("(");

      for (ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode : valueExtractionFunctionNode
        .getArgumentNodes()) {
        Optional<? extends TextRendering> valueExtractionFunctionArgumentRendering = renderValueExtractionFunctionArgument(
          valueExtractionFunctionArgumentNode);
        if (valueExtractionFunctionArgumentRendering.isPresent()) {
          if (!isFirst)
            functionArgumentsRepresentation.append(" ");
          functionArgumentsRepresentation.append(valueExtractionFunctionArgumentRendering.get().getTextRendering());
          isFirst = false;
        }
      }
      functionArgumentsRepresentation.append(")");
    }
    return functionArgumentsRepresentation.length() == 0 ?
      Optional.empty() :
      Optional.of(new TextRendering(valueExtractionFunctionName + functionArgumentsRepresentation.toString()));
  }
}
