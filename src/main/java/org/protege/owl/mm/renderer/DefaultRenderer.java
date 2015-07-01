package org.protege.owl.mm.renderer;

import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParserUtil;
import org.protege.owl.mm.parser.node.AnnotationFactNode;
import org.protege.owl.mm.parser.node.DefaultDataValueNode;
import org.protege.owl.mm.parser.node.DefaultIDNode;
import org.protege.owl.mm.parser.node.DefaultLabelNode;
import org.protege.owl.mm.parser.node.DefaultLocationValueNode;
import org.protege.owl.mm.parser.node.DifferentFromNode;
import org.protege.owl.mm.parser.node.EmptyDataValueSettingNode;
import org.protege.owl.mm.parser.node.EmptyLocationSettingNode;
import org.protege.owl.mm.parser.node.EmptyRDFIDSettingNode;
import org.protege.owl.mm.parser.node.EmptyRDFSLabelSettingNode;
import org.protege.owl.mm.parser.node.EntityTypeNode;
import org.protege.owl.mm.parser.node.ExpressionNode;
import org.protege.owl.mm.parser.node.FactNode;
import org.protege.owl.mm.parser.node.IfExistsDirectiveNode;
import org.protege.owl.mm.parser.node.IfNotExistsDirectiveNode;
import org.protege.owl.mm.parser.node.LanguageNode;
import org.protege.owl.mm.parser.node.LiteralNode;
import org.protege.owl.mm.parser.node.MMDefaultDatatypePropertyValueTypeNode;
import org.protege.owl.mm.parser.node.MMDefaultEntityTypeNode;
import org.protege.owl.mm.parser.node.MMDefaultPropertyTypeNode;
import org.protege.owl.mm.parser.node.MMDefaultPropertyValueTypeNode;
import org.protege.owl.mm.parser.node.MMDefaultValueEncodingNode;
import org.protege.owl.mm.parser.node.MMExpressionNode;
import org.protege.owl.mm.parser.node.NameNode;
import org.protege.owl.mm.parser.node.NamespaceNode;
import org.protege.owl.mm.parser.node.OWLAllValuesFromClassNode;
import org.protege.owl.mm.parser.node.OWLAllValuesFromDataTypeNode;
import org.protege.owl.mm.parser.node.OWLAllValuesFromNode;
import org.protege.owl.mm.parser.node.OWLCardinalityNode;
import org.protege.owl.mm.parser.node.OWLClassDeclarationNode;
import org.protege.owl.mm.parser.node.OWLClassExpressionNode;
import org.protege.owl.mm.parser.node.OWLClassOrRestrictionNode;
import org.protege.owl.mm.parser.node.OWLEnumeratedClassNode;
import org.protege.owl.mm.parser.node.OWLEquivalentToNode;
import org.protege.owl.mm.parser.node.OWLExpressionNode;
import org.protege.owl.mm.parser.node.OWLHasValueNode;
import org.protege.owl.mm.parser.node.OWLIndividualDeclarationNode;
import org.protege.owl.mm.parser.node.OWLIndividualNode;
import org.protege.owl.mm.parser.node.OWLIntersectionClassNode;
import org.protege.owl.mm.parser.node.OWLMaxCardinalityNode;
import org.protege.owl.mm.parser.node.OWLMinCardinalityNode;
import org.protege.owl.mm.parser.node.OWLNamedClassNode;
import org.protege.owl.mm.parser.node.OWLPropertyNode;
import org.protege.owl.mm.parser.node.OWLPropertyValueNode;
import org.protege.owl.mm.parser.node.OWLRestrictionNode;
import org.protege.owl.mm.parser.node.OWLSomeValuesFromClassNode;
import org.protege.owl.mm.parser.node.OWLSomeValuesFromDataTypeNode;
import org.protege.owl.mm.parser.node.OWLSomeValuesFromNode;
import org.protege.owl.mm.parser.node.OWLSubclassOfNode;
import org.protege.owl.mm.parser.node.OWLUnionClassNode;
import org.protege.owl.mm.parser.node.PrefixNode;
import org.protege.owl.mm.parser.node.ReferenceNode;
import org.protege.owl.mm.parser.node.SameAsNode;
import org.protege.owl.mm.parser.node.ShiftSettingNode;
import org.protege.owl.mm.parser.node.SourceSpecificationNode;
import org.protege.owl.mm.parser.node.StringLiteralNode;
import org.protege.owl.mm.parser.node.StringOrReferenceNode;
import org.protege.owl.mm.parser.node.TypeNode;
import org.protege.owl.mm.parser.node.TypesNode;
import org.protege.owl.mm.parser.node.ValueEncodingNode;
import org.protege.owl.mm.parser.node.ValueExtractionFunctionNode;
import org.protege.owl.mm.parser.node.ValueSpecificationItemNode;
import org.protege.owl.mm.parser.node.ValueSpecificationNode;

/**
 * This renderer simply produces the standard presentation syntax rendering of the supplied entities. Subclasses will specialize and perform custom actions for
 * individual entities.
 */
public class DefaultRenderer implements Renderer, MappingMasterParserConstants
{
  public Rendering renderExpression(ExpressionNode expressionNode) throws RendererException
  {
    Rendering rendering;

    if (expressionNode.hasMMExpression())
      rendering = renderMMExpression(expressionNode.getMMExpressionNode());
    else if (expressionNode.hasOWLExpression())
      rendering = renderOWLExpression(expressionNode.getOWLExpressionNode());
    else
      throw new RendererException("unknown expression type " + expressionNode);

    return rendering;
  }

  public Rendering renderMMExpression(MMExpressionNode mmExpressionNode) throws RendererException
  {
    Rendering rendering;

    if (mmExpressionNode.hasDefaultValueEncoding())
      rendering = renderMMDefaultValueEncoding(mmExpressionNode.getDefaultValueEncodingNode());
    else if (mmExpressionNode.hasDefaultEntityType())
      rendering = renderMMDefaultEntityType(mmExpressionNode.getDefaultEntityTypeNode());
    else if (mmExpressionNode.hasDefaultPropertyValueType())
      rendering = renderMMDefaultPropertyValueType(mmExpressionNode.getDefaultPropertyValueTypeNode());
    else
      throw new RendererException("unknown directive " + mmExpressionNode);

    return rendering;
  }

  public Rendering renderMMDefaultEntityType(MMDefaultEntityTypeNode mmDefaultEntityTypeNode) throws RendererException
  {
    return new Rendering(mmDefaultEntityTypeNode.toString());
  }

  public Rendering renderMMDefaultValueEncoding(MMDefaultValueEncodingNode mmDefaultValueEncodingNode)
    throws RendererException
  {
    return new Rendering(mmDefaultValueEncodingNode.toString());
  }

  public Rendering renderMMDefaultPropertyValueType(MMDefaultPropertyValueTypeNode mmDefaultPropertyValueTypeNode)
    throws RendererException
  {
    return new Rendering(mmDefaultPropertyValueTypeNode.toString());
  }

  public Rendering renderMMDefaultPropertyType(MMDefaultPropertyTypeNode mmDefaultPropertyTypeNode)
    throws RendererException
  {
    return new Rendering(mmDefaultPropertyTypeNode.toString());
  }

  public Rendering renderMMDefaultDatatypePropertyValueType(
    MMDefaultDatatypePropertyValueTypeNode mmDefaultDatatypePropertyValueTypeNode) throws RendererException
  {
    return new Rendering(mmDefaultDatatypePropertyValueTypeNode.toString());
  }

  public Rendering renderOWLExpression(OWLExpressionNode owlExpressionNode) throws RendererException
  {
    Rendering rendering;

    if (owlExpressionNode.hasOWLClassDeclaration())
      rendering = renderOWLClassDeclaration(owlExpressionNode.getOWLClassDeclarationNode());
    else if (owlExpressionNode.hasOWLIndividualDeclaration())
      rendering = renderOWLIndividualDeclaration(owlExpressionNode.getOWLIndividualDeclarationNode());
    else if (owlExpressionNode.hasOWLClassExpression())
      rendering = renderOWLClassExpression(owlExpressionNode.getOWLClassExpressionNode());
    else
      throw new RendererException("unknown expression: " + owlExpressionNode);

    return rendering;
  }

  public Rendering renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode) throws RendererException
  {
    Rendering rendering = new Rendering("Class: " + owlClassDeclarationNode.getOWLNamedClassNode().toString());
    boolean isFirst = true;

    if (owlClassDeclarationNode.hasSubclassOf()) {
      rendering.addText(" SubclassOf: ");
      for (OWLSubclassOfNode subclassOf : owlClassDeclarationNode.getSubclassOfNodes()) {
        Rendering subclassOfRendering = renderOWLSubclassOf(subclassOf);
        if (!subclassOfRendering.nothingRendered())
          continue;

        if (!isFirst)
          rendering.addText(", ");

        rendering.addText(subclassOfRendering.getFinalRendering());
        isFirst = false;
      }
    }

    isFirst = true;
    if (owlClassDeclarationNode.hasEquivalentTo()) {
      rendering.addText(" EquivalentTo: ");
      for (OWLEquivalentToNode equivalentTo : owlClassDeclarationNode.getEquivalentToNodes()) {
        Rendering equivalentToRendering = renderOWLEquivalentTo(equivalentTo);
        if (!equivalentToRendering.nothingRendered())
          continue;

        if (!isFirst)
          rendering.addText(", ");

        rendering.addText(equivalentToRendering.getFinalRendering());
        isFirst = false;
      }
    }

    isFirst = true;
    if (owlClassDeclarationNode.hasAnnotations()) {
      rendering.addText(" Annotations: ");
      for (AnnotationFactNode annotationFactNode : owlClassDeclarationNode.getAnnotationFactNodes()) {
        Rendering factRendering = renderAnnotationFact(annotationFactNode);

        if (factRendering.nothingRendered())
          continue;

        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(factRendering.getFinalRendering());
        isFirst = false;
      }
    }

    return rendering;
  }

  public Rendering renderOWLIndividualDeclaration(OWLIndividualDeclarationNode owlIndividualDeclarationNode)
    throws RendererException
  {
    Rendering rendering = new Rendering();
    boolean isFirst = true;
    Rendering individualRendering = renderOWLIndividual(owlIndividualDeclarationNode.getOWLIndividualNode());

    if (individualRendering.nothingRendered())
      return rendering;

    rendering.addText("Individual: ");

    rendering.addText(individualRendering.getFinalRendering());

    if (owlIndividualDeclarationNode.hasFacts()) {
      rendering.addText(" Facts: ");

      for (FactNode fact : owlIndividualDeclarationNode.getFactNodes()) {
        Rendering factRendering = renderFact(fact);

        if (factRendering.nothingRendered())
          continue;

        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(factRendering.getFinalRendering());
        isFirst = false;
      }
    }

    if (owlIndividualDeclarationNode.hasTypes()) {
      Rendering typesRendering = renderTypes(owlIndividualDeclarationNode.getTypeNodes());

      if (!typesRendering.nothingRendered())
        rendering.addText(" Types: " + typesRendering.getFinalRendering());
    }

    isFirst = true;
    if (owlIndividualDeclarationNode.hasAnnotations()) {
      rendering.addText(" Annotations:");

      for (AnnotationFactNode factNode : owlIndividualDeclarationNode.getAnnotationNodes()) {
        Rendering factRendering = renderAnnotationFact(factNode);

        if (factRendering.nothingRendered())
          continue;

        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(factRendering.getFinalRendering());
        isFirst = false;
      }
    }

    if (owlIndividualDeclarationNode.hasSameAs()) {
      Rendering sameAsRendering = renderSameAs(owlIndividualDeclarationNode.getSameAsNode());
      if (!sameAsRendering.nothingRendered())
        rendering.addText(sameAsRendering.getFinalRendering());
    }

    if (owlIndividualDeclarationNode.hasDifferentFrom()) {
      Rendering differentFromRendering = renderDifferentFrom(owlIndividualDeclarationNode.getDifferentFromNode());
      if (!differentFromRendering.nothingRendered())
        rendering.addText(differentFromRendering.getFinalRendering());
    }

    return rendering;
  }

  public Rendering renderFact(FactNode factNode) throws RendererException
  {
    Rendering propertyRendering = renderOWLProperty(factNode.getOWLPropertyNode());
    Rendering propertyValueRendering = renderOWLPropertyValue(factNode.getOWLPropertyValueNode());
    Rendering rendering = new Rendering();

    if (!propertyRendering.nothingRendered() && !propertyValueRendering.nothingRendered())
      rendering.addText(propertyRendering.getFinalRendering() + " " + propertyValueRendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException
  {
    Rendering propertyRendering = renderOWLProperty(annotationFactNode.getOWLPropertyNode());
    Rendering propertyValueRendering = renderOWLPropertyValue(annotationFactNode.getOWLPropertyValueNode());
    Rendering rendering = new Rendering();

    if (!propertyRendering.nothingRendered() && !propertyValueRendering.nothingRendered())
      rendering.addText(propertyRendering.getFinalRendering() + " " + propertyValueRendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderOWLPropertyValue(OWLPropertyValueNode owlPropertyValueNode) throws RendererException
  {
    Rendering rendering;

    if (owlPropertyValueNode.isReference())
      rendering = renderReference(owlPropertyValueNode.getReferenceNode());
    else if (owlPropertyValueNode.isName())
      rendering = renderName(owlPropertyValueNode.getNameNode());
    else if (owlPropertyValueNode.isLiteral())
      rendering = renderLiteral(owlPropertyValueNode.getLiteralNode());
    else
      throw new RendererException("unknown property value node " + owlPropertyValueNode);

    return rendering;
  }

  public Rendering renderOWLClassExpression(OWLClassExpressionNode owlClassExpressionNode) throws RendererException
  {
    return renderOWLUnionClass(owlClassExpressionNode.getOWLUnionClassNode());
  }

  public Rendering renderOWLUnionClass(OWLUnionClassNode owlUnionClassNode) throws RendererException
  {
    Rendering rendering = new Rendering();
    Rendering intersectionRendering;

    if (owlUnionClassNode.getOWLIntersectionClasseNodes().size() == 1) {
      intersectionRendering = renderOWLIntersectionClass(owlUnionClassNode.getOWLIntersectionClasseNodes().get(0));

      if (intersectionRendering.nothingRendered())
        return rendering;

      rendering = intersectionRendering;
    } else {
      boolean isFirst = true;

      for (OWLIntersectionClassNode owlIntersectionClassNode : owlUnionClassNode.getOWLIntersectionClasseNodes()) {
        intersectionRendering = renderOWLIntersectionClass(owlIntersectionClassNode);

        if (!intersectionRendering.nothingRendered()) {
          if (isFirst)
            rendering.addText("(");
          else
            rendering.addText(" OR ");
          rendering.addText(intersectionRendering.getFinalRendering());
          isFirst = false;
        }
      }
      if (!rendering.nothingRendered())
        rendering.addText(")");
    }

    return rendering;
  }

  public Rendering renderOWLIntersectionClass(OWLIntersectionClassNode owlIntersectionClassNode)
    throws RendererException
  {
    Rendering rendering = new Rendering();
    Rendering classesOrRestrictionsRendering;

    if (owlIntersectionClassNode.getOWLClassesOrRestrictionNodes().size() == 1) {
      classesOrRestrictionsRendering = renderOWLClassOrRestriction(
        owlIntersectionClassNode.getOWLClassesOrRestrictionNodes().get(0));

      if (classesOrRestrictionsRendering.nothingRendered())
        return rendering;

      rendering = classesOrRestrictionsRendering;
    } else {
      boolean isFirst = true;

      for (OWLClassOrRestrictionNode owlClassOrRestriction : owlIntersectionClassNode
        .getOWLClassesOrRestrictionNodes()) {
        classesOrRestrictionsRendering = renderOWLClassOrRestriction(owlClassOrRestriction);

        if (!classesOrRestrictionsRendering.nothingRendered()) {
          if (isFirst)
            rendering.addText("(");
          else
            rendering.addText(" AND ");
          rendering.addText(classesOrRestrictionsRendering.getFinalRendering());
          isFirst = false;
        }
      }
      if (!rendering.nothingRendered())
        rendering.addText(")");
    }

    return rendering;
  }

  public Rendering renderOWLClassOrRestriction(OWLClassOrRestrictionNode classOrRestrictionNode)
    throws RendererException
  {
    Rendering rendering = new Rendering();

    if (classOrRestrictionNode.hasOWLEnumeratedClass())
      rendering
        .addText(renderOWLEnumeratedClass(classOrRestrictionNode.getOWLEnumeratedClassNode()).getFinalRendering());
    else if (classOrRestrictionNode.hasOWLUnionClass())
      rendering.addText(renderOWLUnionClass(classOrRestrictionNode.getOWLUnionClassNode()).getFinalRendering());
    else if (classOrRestrictionNode.hasOWLRestriction())
      rendering.addText(renderOWLRestriction(classOrRestrictionNode.getOWLRestrictionNode()).getFinalRendering());
    else if (classOrRestrictionNode.hasOWLNamedClass())
      rendering.addText(renderOWLNamedClass(classOrRestrictionNode.getOWLNamedClassNode()).getFinalRendering());
    else
      throw new RendererException("unexpected OWLClassOrRestriction node " + classOrRestrictionNode);

    if (!rendering.nothingRendered())
      if (classOrRestrictionNode.getIsNegated())
        rendering.addText("NOT " + rendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderOWLEnumeratedClass(OWLEnumeratedClassNode owlEnumeratedClassNode) throws RendererException
  {
    Rendering rendering = new Rendering();
    Rendering individualRendering;

    if (owlEnumeratedClassNode.getOWLIndividualNodes().size() == 1) {
      individualRendering = renderOWLIndividual(owlEnumeratedClassNode.getOWLIndividualNodes().get(0));

      if (individualRendering.nothingRendered())
        return rendering;

      rendering.addText(individualRendering.getFinalRendering());
    } else {
      boolean isFirst = true;

      rendering.addText("{");
      for (OWLIndividualNode owlIndividualNode : owlEnumeratedClassNode.getOWLIndividualNodes()) {
        if (!isFirst)
          rendering.addText(" ");
        individualRendering = renderOWLIndividual(owlIndividualNode);

        if (individualRendering.nothingRendered())
          return rendering;

        rendering.addText(individualRendering.getFinalRendering());
        isFirst = false;
      }
      rendering.addText("}");
    }

    return rendering;
  }

  public Rendering renderOWLRestriction(OWLRestrictionNode owlRestrictionNode) throws RendererException
  {
    Rendering propertyRendering = renderOWLProperty(owlRestrictionNode.getOWLPropertyNode());
    Rendering rendering = new Rendering();
    Rendering restrictionRendering;

    if (owlRestrictionNode.hasOWLMinCardinality())
      restrictionRendering = renderOWLMinCardinality(owlRestrictionNode.getOWLMinCardinalityNode());
    else if (owlRestrictionNode.hasOWLMaxCardinality())
      restrictionRendering = renderOWLMaxCardinality(owlRestrictionNode.getOWLMaxCardinalityNode());
    else if (owlRestrictionNode.hasOWLCardinality())
      restrictionRendering = renderOWLCardinality(owlRestrictionNode.getOWLCardinalityNode());
    else if (owlRestrictionNode.hasOWLHasValue())
      restrictionRendering = renderOWLHasValue(owlRestrictionNode.getOWLHasValueNode());
    else if (owlRestrictionNode.hasOWLAllValuesFrom())
      restrictionRendering = renderOWLAllValuesFrom(owlRestrictionNode.getOWLAllValuesFromNode());
    else if (owlRestrictionNode.hasOWLSomeValuesFrom())
      restrictionRendering = renderOWLSomeValuesFrom(owlRestrictionNode.getOWLSomeValuesFromNode());
    else
      throw new RendererException("unkown OWLRestriction " + owlRestrictionNode);

    if (!propertyRendering.nothingRendered() && !restrictionRendering.nothingRendered())
      rendering
        .addText("(" + propertyRendering.getFinalRendering() + " " + restrictionRendering.getFinalRendering() + ")");

    return rendering;
  }

  public Rendering renderOWLMaxCardinality(OWLMaxCardinalityNode owlMaxCardinalityNode) throws RendererException
  {
    Rendering cardinalityRendering = new Rendering("" + owlMaxCardinalityNode.getCardinality());
    Rendering rendering = new Rendering();

    if (!cardinalityRendering.nothingRendered())
      rendering.addText("MAX " + cardinalityRendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderOWLMinCardinality(OWLMinCardinalityNode owlMinCardinalityNode) throws RendererException
  {
    Rendering cardinalityRendering = new Rendering("" + owlMinCardinalityNode.getCardinality());
    Rendering rendering = new Rendering();

    if (!cardinalityRendering.nothingRendered())
      rendering.addText("MIN " + cardinalityRendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderOWLCardinality(OWLCardinalityNode owlCardinalityNode) throws RendererException
  {
    Rendering cardinalityRendering = new Rendering("" + owlCardinalityNode.getCardinality());
    Rendering rendering = new Rendering();

    if (!cardinalityRendering.nothingRendered())
      rendering.addText("EXACTLY " + cardinalityRendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderOWLHasValue(OWLHasValueNode owlHasValueNode) throws RendererException
  {
    Rendering propertyValueRendering = renderOWLPropertyValue(owlHasValueNode.getOWLPropertyValueNode());
    Rendering rendering = new Rendering();

    if (!propertyValueRendering.nothingRendered())
      rendering.addText("VALUE " + propertyValueRendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderOWLAllValuesFrom(OWLAllValuesFromNode owlAllValuesFromNode) throws RendererException
  {
    Rendering rendering;

    if (owlAllValuesFromNode.hasOWLAllValuesFromDataType())
      rendering = renderOWLAllValuesFromDataType(owlAllValuesFromNode.getOWLAllValuesFromDataTypeNode());
    else if (owlAllValuesFromNode.hasOWLAllValuesFromClass())
      rendering = renderOWLAllValuesFromClass(owlAllValuesFromNode.getOWLAllValuesFromClassNode());
    else
      throw new RendererException("unknown OWLAllValuesFrom node " + owlAllValuesFromNode);

    return rendering;
  }

  public Rendering renderOWLSomeValuesFrom(OWLSomeValuesFromNode owlSomeValuesFromNode) throws RendererException
  {
    Rendering rendering;

    if (owlSomeValuesFromNode.hasOWLSomeValuesFromDataType())
      rendering = renderOWLSomeValuesFromDataType(owlSomeValuesFromNode.getOWLSomeValuesFromDataTypeNode());
    else if (owlSomeValuesFromNode.hasOWLSomeValuesFromClass())
      rendering = renderOWLSomeValuesFromClass(owlSomeValuesFromNode.getOWLSomeValuesFromClassNode());
    else
      throw new RendererException("unknown OWLSomeValuesFrom node " + owlSomeValuesFromNode);

    return rendering;
  }

  public Rendering renderOWLNamedClass(OWLNamedClassNode owlNamedClassNode) throws RendererException
  {
    Rendering rendering;

    if (owlNamedClassNode.isReference())
      rendering = renderReference(owlNamedClassNode.getReferenceNode());
    else if (owlNamedClassNode.isName())
      rendering = renderName(owlNamedClassNode.getNameNode());
    else
      throw new RendererException("unknown OWLNamedClass node " + owlNamedClassNode);

    return rendering;
  }

  public Rendering renderOWLProperty(OWLPropertyNode owlPropertyNode) throws RendererException
  {
    Rendering rendering;

    if (owlPropertyNode.isReference())
      rendering = renderReference(owlPropertyNode.getReferenceNode());
    else if (owlPropertyNode.isName())
      rendering = renderName(owlPropertyNode.getNameNode());
    else
      throw new RendererException("unknown OWLProperty node " + owlPropertyNode);

    return rendering;
  }

  public Rendering renderOWLIndividual(OWLIndividualNode owlIndividualNode) throws RendererException
  {
    Rendering rendering;

    if (owlIndividualNode.isReference())
      rendering = renderReference(owlIndividualNode.getReferenceNode());
    else if (owlIndividualNode.isName())
      rendering = renderName(owlIndividualNode.getNameNode());
    else
      throw new RendererException("unknown OWLIndividual node " + owlIndividualNode);

    return rendering;
  }

  public Rendering renderName(NameNode nameNode) throws RendererException
  {
    return new Rendering(nameNode.isQuoted() ? "'" + nameNode.getName() + "'" : nameNode.getName());
  }

  public Rendering renderPrefix(PrefixNode prefixNode) throws RendererException
  {
    return new Rendering(prefixNode.toString());
  }

  public Rendering renderLanguage(LanguageNode languageNode) throws RendererException
  {
    return new Rendering(languageNode.toString());
  }

  public Rendering renderNamespace(NamespaceNode namespaceNode) throws RendererException
  {
    return new Rendering(ParserUtil.getTokenName(MM_NAMESPACE) + "=\"" + namespaceNode.getNamespace() + "\"");
  }

  public Rendering renderOWLAllValuesFromDataType(OWLAllValuesFromDataTypeNode owlAllValuesFromDataTypeNode)
    throws RendererException
  {
    String datatypeName = owlAllValuesFromDataTypeNode.getDataTypeName();
    Rendering rendering = new Rendering();

    if (!datatypeName.equals(""))
      rendering.addText("ONLY " + datatypeName);

    return rendering;
  }

  public Rendering renderOWLAllValuesFromClass(OWLAllValuesFromClassNode owlAllValuesFromClassNode)
    throws RendererException
  {
    Rendering classRendering;
    Rendering rendering = new Rendering();

    if (owlAllValuesFromClassNode.hasOWLNamedClass())
      classRendering = renderOWLNamedClass(owlAllValuesFromClassNode.getOWLNamedClassNode());
    else if (owlAllValuesFromClassNode.hasOWLClassExpression())
      classRendering = renderOWLClassExpression(owlAllValuesFromClassNode.getOWLClassExpressionNode());
    else
      throw new RendererException("unknown OWLAllValuesFromClass node " + owlAllValuesFromClassNode);

    if (!classRendering.nothingRendered())
      rendering.addText("ONLY " + classRendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderOWLSomeValuesFromDataType(OWLSomeValuesFromDataTypeNode owlSomeValuesFromDataTypeNode)
    throws RendererException
  {
    String datatypeName = owlSomeValuesFromDataTypeNode.getDataTypeName();
    Rendering rendering = new Rendering();

    if (!datatypeName.equals(""))
      rendering.addText("SOME " + datatypeName);

    return rendering;
  }

  public Rendering renderOWLSomeValuesFromClass(OWLSomeValuesFromClassNode owlSomeValuesFromClassNode)
    throws RendererException
  {
    Rendering classRendering;
    Rendering rendering = new Rendering();

    if (owlSomeValuesFromClassNode.hasOWLNamedClass())
      classRendering = renderOWLNamedClass(owlSomeValuesFromClassNode.getOWLNamedClassNode());
    else if (owlSomeValuesFromClassNode.hasOWLClassExpression())
      classRendering = renderOWLClassExpression(owlSomeValuesFromClassNode.getOWLClassExpressionNode());
    else
      throw new RendererException("unknown OWLSomeValuesFromClass node " + owlSomeValuesFromClassNode);

    if (!classRendering.nothingRendered())
      rendering.addText("SOME " + classRendering.getFinalRendering());

    return rendering;
  }

  public Rendering renderOWLEquivalentTo(OWLEquivalentToNode owlEquivalentToNode) throws RendererException
  {
    Rendering classExpressionRendering;
    Rendering rendering = new Rendering();

    rendering.addText(" EquivalentTo: ");

    if (owlEquivalentToNode.getClassExpressionNodes().size() == 1) {
      classExpressionRendering = renderOWLClassExpression(owlEquivalentToNode.getClassExpressionNodes().get(0));
      if (classExpressionRendering.nothingRendered())
        return rendering;
      rendering.addText(classExpressionRendering.getFinalRendering());
    } else {
      boolean isFirst = true;

      for (OWLClassExpressionNode owlClassExpressionNode : owlEquivalentToNode.getClassExpressionNodes()) {
        classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
        if (classExpressionRendering.nothingRendered())
          continue; // Any empty class expression will generate an empty rendering
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(classExpressionRendering.getFinalRendering());
        isFirst = false;
      }
    }

    return rendering;
  }

  public Rendering renderOWLSubclassOf(OWLSubclassOfNode owlSubclassOfNode) throws RendererException
  {
    Rendering classExpressionRendering;
    Rendering rendering = new Rendering();

    rendering.addText(" SubClassOf: ");

    if (owlSubclassOfNode.getClassExpressionNodes().size() == 1) {
      classExpressionRendering = renderOWLClassExpression(owlSubclassOfNode.getClassExpressionNodes().get(0));
      if (classExpressionRendering.nothingRendered())
        return rendering;
      rendering.addText(classExpressionRendering.getFinalRendering());
    } else {
      boolean isFirst = true;

      for (OWLClassExpressionNode owlClassExpressionNode : owlSubclassOfNode.getClassExpressionNodes()) {
        classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
        if (classExpressionRendering.nothingRendered())
          continue; // Any empty class expression will generate an empty rendering
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(classExpressionRendering.getFinalRendering());
        isFirst = false;
      }
    }

    return rendering;
  }

  public Rendering renderLiteral(LiteralNode literalNode) throws RendererException
  {
    Rendering rendering;

    if (literalNode.isInteger())
      rendering = new Rendering(literalNode.getIntegerLiteralNode().toString());
    else if (literalNode.isFloat())
      rendering = new Rendering(literalNode.getFloatLiteralNode().toString());
    else if (literalNode.isString())
      rendering = new Rendering(literalNode.toString());
    else if (literalNode.isBoolean())
      rendering = new Rendering(literalNode.getBooleanLiteralNode().toString());
    else
      throw new RendererException("unknown Literal node " + literalNode);

    return rendering;
  }

  public Rendering renderReference(ReferenceNode referenceNode) throws RendererException
  {
    Rendering rendering = new Rendering();
    boolean hasExplicitOptions = referenceNode.hasExplicitOptions();
    boolean atLeastOneOptionProcessed = false;

    rendering.addText(referenceNode.getSourceSpecificationNode().toString());

    if (hasExplicitOptions)
      rendering.addText("(");

    if (referenceNode.hasExplicitlySpecifiedEntityType()) {
      rendering.addText(referenceNode.getEntityTypeNode().toString());
      atLeastOneOptionProcessed = true;
    }

    if (referenceNode.hasExplicitlySpecifiedPrefix()) {
      Rendering prefixRendering = renderPrefix(referenceNode.getPrefixNode());
      if (!prefixRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        else
          atLeastOneOptionProcessed = true;
        rendering.addText(prefixRendering.getFinalRendering());
      }
    }

    if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      Rendering namespaceRendering = renderNamespace(referenceNode.getNamespaceNode());
      if (!namespaceRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        else
          atLeastOneOptionProcessed = true;
        rendering.addText(namespaceRendering.getFinalRendering());
      }
    }

    if (referenceNode.hasValueExtractionFunction()) {
      Rendering valueExtractionFunctionRendering = renderValueExtractionFunction(
        referenceNode.getValueExtractionFunctionNode());
      if (!valueExtractionFunctionRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        else
          atLeastOneOptionProcessed = true;
        rendering.addText(valueExtractionFunctionRendering.getFinalRendering());
      }
    }

    if (referenceNode.hasExplicitlySpecifiedValueEncodings()) {
      boolean isFirst = true;
      if (atLeastOneOptionProcessed)
        rendering.addText(" ");
      for (ValueEncodingNode valueEncodingNode : referenceNode.getValueEncodingNodes()) {
        Rendering valueEncodingRendering = renderValueEncoding(valueEncodingNode);
        if (!valueEncodingRendering.nothingRendered()) {
          if (!isFirst)
            rendering.addText(" ");
          rendering.addText(valueEncodingRendering.getFinalRendering());
          isFirst = false;
        }
      }
      atLeastOneOptionProcessed = true;
    }

    if (referenceNode.hasExplicitlySpecifiedDefaultLocationValue()) {
      Rendering defaultLocationValueRendering = renderDefaultLocationValue(referenceNode.getDefaultLocationValueNode());
      if (!defaultLocationValueRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(defaultLocationValueRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedDefaultDataValue()) {
      Rendering defaultDataValueRendering = renderDefaultDataValue(referenceNode.getDefaultDataValueNode());
      if (!defaultDataValueRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(defaultDataValueRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedDefaultID()) {
      Rendering defaultIDRendering = renderDefaultID(referenceNode.getDefaultRDFIDNode());
      if (!defaultIDRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(defaultIDRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedDefaultLabel()) {
      Rendering defaultLabelRendering = renderDefaultLabel(referenceNode.getDefaultRDFSLabelNode());
      if (!defaultLabelRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(defaultLabelRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedLanguage()) {
      Rendering languageRendering = renderLanguage(referenceNode.getLanguageNode());
      if (!languageRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(languageRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedPrefix()) {
      if (atLeastOneOptionProcessed)
        rendering.addText(" ");
      rendering.addText(renderPrefix(referenceNode.getPrefixNode()).getFinalRendering());
      atLeastOneOptionProcessed = true;
    }

    if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      Rendering namespaceRendering = renderNamespace(referenceNode.getNamespaceNode());
      if (!namespaceRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(namespaceRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedEmptyLocationDirective()) {
      Rendering emptyLocationSettingRendering = renderEmptyLocationSetting(referenceNode.getEmptyLocationSettingNode());
      if (!emptyLocationSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(emptyLocationSettingRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedEmptyDataValueDirective()) {
      Rendering emptyDataValueSettingRendering = renderEmptyDataValueSetting(
        referenceNode.getEmptyDataValueSettingNode());
      if (!emptyDataValueSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(emptyDataValueSettingRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedEmptyRDFIDDirective()) {
      Rendering emptyRDFIFSettingRendering = renderEmptyRDFIDSetting(referenceNode.getEmptyRDFIDSettingNode());
      if (!emptyRDFIFSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(emptyRDFIFSettingRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedEmptyRDFSLabelDirective()) {
      Rendering emptyRDFSLabelSettingRendering = renderEmptyRDFSLabelSetting(
        referenceNode.getEmptyRDFSLabelSettingNode());
      if (emptyRDFSLabelSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(emptyRDFSLabelSettingRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedShiftDirective()) {
      Rendering shiftSettingRendering = renderShiftSetting(referenceNode.getShiftSettingNode());
      if (!shiftSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(shiftSettingRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedTypes()) {
      Rendering typesRendering = renderTypes(referenceNode.getTypesNode());
      if (!typesRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(typesRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedIfExistsDirective()) {
      Rendering ifExistsRendering = renderIfExistsDirective(referenceNode.getIfExistsDirectiveNode());
      if (!ifExistsRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(ifExistsRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedIfNotExistsDirective()) {
      Rendering ifExistsRendering = renderIfNotExistsDirective(referenceNode.getIfNotExistsDirectiveNode());
      if (!ifExistsRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(ifExistsRendering.getFinalRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (hasExplicitOptions)
      rendering.addText(")");

    return rendering;
  }

  public Rendering renderEntityType(EntityTypeNode entityTypeNode) throws RendererException
  {
    return new Rendering(entityTypeNode.getEntityType().getTypeName());
  }

  public Rendering renderValueEncoding(ValueEncodingNode valueEncodingNode) throws RendererException
  {
    Rendering rendering = new Rendering(valueEncodingNode.getEncodingTypeName());

    if (valueEncodingNode.hasValueSpecification()) {
      Rendering valueSpecificationRendering = renderValueSpecification(valueEncodingNode.getValueSpecification());
      if (!valueSpecificationRendering.nothingRendered())
        rendering.addText(valueSpecificationRendering.getFinalRendering());
    }

    return rendering;
  }

  public Rendering renderValueSpecification(ValueSpecificationNode valueSpecificationNode) throws RendererException
  {
    Rendering rendering = new Rendering();
    boolean isFirst = true;

    if (valueSpecificationNode.getNumberOfValueSpecificationItems() > 1)
      rendering.addText("(");

    for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
      .getValueSpecificationItemNodes()) {
      Rendering valueSpecificationItemRendering = renderValueSpecificationItem(valueSpecificationItemNode);

      if (!valueSpecificationItemRendering.nothingRendered()) {
        if (isFirst)
          rendering.addText("=");
        else
          rendering.addText(", ");
        rendering.addText(valueSpecificationItemRendering.getFinalRendering());
        isFirst = false;
      }
    }

    if (valueSpecificationNode.getNumberOfValueSpecificationItems() > 1)
      rendering.addText(")");

    return rendering;
  }

  public Rendering renderDefaultLocationValue(DefaultLocationValueNode defaultLocationValue) throws RendererException
  {
    return new Rendering(defaultLocationValue.toString());
  }

  public Rendering renderDefaultDataValue(DefaultDataValueNode defaultDataValue) throws RendererException
  {
    return new Rendering(defaultDataValue.toString());
  }

  public Rendering renderDefaultID(DefaultIDNode defaultID) throws RendererException
  {
    return new Rendering(defaultID.toString());
  }

  public Rendering renderDefaultLabel(DefaultLabelNode defaultLabel) throws RendererException
  {
    return new Rendering(defaultLabel.toString());
  }

  public Rendering renderValueSpecificationItem(ValueSpecificationItemNode valueSpecificationItem)
    throws RendererException
  {
    Rendering rendering;

    if (valueSpecificationItem.hasStringLiteral())
      rendering = new Rendering("\"" + valueSpecificationItem.getStringLiteral() + "\"");
    else if (valueSpecificationItem.hasReference())
      rendering = renderReference(valueSpecificationItem.getReferenceNode());
    else if (valueSpecificationItem.hasValueExtractionFunction())
      rendering = renderValueExtractionFunction(valueSpecificationItem.getValueExtractionFunctionNode());
    else if (valueSpecificationItem.hasCapturingExpression())
      rendering = new Rendering("[\"" + valueSpecificationItem.getCapturingExpression() + "\"]");
    else
      throw new RendererException("unknown ValueSpecificationItem node " + valueSpecificationItem);

    return rendering;
  }

  public Rendering renderTypes(TypesNode types) throws RendererException
  {
    Rendering rendering = new Rendering();
    boolean isFirst = true;

    for (TypeNode typeNode : types.getTypeNodes()) {
      Rendering typeRendering = renderType(typeNode);
      if (!typeRendering.nothingRendered()) {
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(typeRendering.getFinalRendering());
        isFirst = false;
      }
    }

    return rendering;
  }

  public Rendering renderType(TypeNode typeNode) throws RendererException
  {
    Rendering rendering;

    if (typeNode instanceof ReferenceNode)
      rendering = renderReference((ReferenceNode)typeNode);
    else if (typeNode instanceof OWLClassExpressionNode)
      rendering = renderOWLClassExpression((OWLClassExpressionNode)typeNode);
    else
      throw new RendererException("do not know how to render type node " + typeNode.getNodeName());

    return rendering;
  }

  public Rendering renderSameAs(SameAsNode sameAs) throws RendererException
  {
    Rendering rendering = new Rendering(" SameAs: ");
    boolean isFirst = true;

    for (OWLIndividualNode owlIndividualNode : sameAs.getIndividualNodes()) {
      Rendering individualRendering = renderOWLIndividual(owlIndividualNode);

      if (!individualRendering.nothingRendered()) {
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(individualRendering.getFinalRendering());
        isFirst = false;
      }
    }

    return rendering;
  }

  public Rendering renderDifferentFrom(DifferentFromNode differentFrom) throws RendererException
  {
    Rendering rendering = new Rendering(" DifferentFrom: ");
    boolean isFirst = true;

    for (OWLIndividualNode owlIndividualNode : differentFrom.getIndividualNodes()) {
      Rendering individualRendering = renderOWLIndividual(owlIndividualNode);

      if (!individualRendering.nothingRendered()) {
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(individualRendering.getFinalRendering());
        isFirst = false;
      }
    }

    return rendering;
  }

  public Rendering renderValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunction)
    throws RendererException
  {
    Rendering rendering = new Rendering(valueExtractionFunction.getFunctionName());

    if (valueExtractionFunction.hasArguments()) {
      boolean isFirst = true;
      rendering.addText("(");

      for (StringOrReferenceNode stringOrReferenceNode : valueExtractionFunction.getArgumentNodes()) {
        Rendering stringOrReferenceRendering = renderStringOrReference(stringOrReferenceNode);
        if (!stringOrReferenceRendering.nothingRendered()) {
          if (!isFirst)
            rendering.addText(" ");
          rendering.addText(stringOrReferenceRendering.getFinalRendering());
          isFirst = false;
        }
      }

      rendering.addText(")");
    }

    return rendering;
  }

  public Rendering renderSourceSpecification(SourceSpecificationNode sourceSpecification) throws RendererException
  {
    Rendering rendering = new Rendering("@");

    if (sourceSpecification.hasSource())
      rendering.addText("'" + sourceSpecification.getSource() + "'!");

    if (sourceSpecification.hasLocation())
      rendering.addText(sourceSpecification.getLocation());
    else
      // literal
      rendering.addText("\"" + sourceSpecification.getLiteral() + "\"");

    return rendering;
  }

  public Rendering renderStringOrReference(StringOrReferenceNode stringOrReference) throws RendererException
  {
    Rendering rendering;

    if (stringOrReference.isString())
      rendering = renderStringLiteral(stringOrReference.getStringLiteralNode());
    else if (stringOrReference.isReference())
      rendering = renderReference(stringOrReference.getReferenceNode());
    else
      throw new RendererException("unknown StringOrReference node" + stringOrReference);

    return rendering;
  }

  public Rendering renderStringLiteral(StringLiteralNode stringLiteral) throws RendererException
  {
    return new Rendering(stringLiteral.toString());
  }

  public Rendering renderShiftSetting(ShiftSettingNode shiftSetting) throws RendererException
  {
    return new Rendering(shiftSetting.toString());
  }

  public Rendering renderEmptyLocationSetting(EmptyLocationSettingNode emptyLocationSetting) throws RendererException
  {
    return new Rendering(emptyLocationSetting.toString());
  }

  public Rendering renderEmptyDataValueSetting(EmptyDataValueSettingNode emptyDataValueSetting) throws RendererException
  {
    return new Rendering(emptyDataValueSetting.toString());
  }

  public Rendering renderEmptyRDFIDSetting(EmptyRDFIDSettingNode emptyRDFIDSetting) throws RendererException
  {
    return new Rendering(emptyRDFIDSetting.toString());
  }

  public Rendering renderEmptyRDFSLabelSetting(EmptyRDFSLabelSettingNode emptyRDFSLabelSetting) throws RendererException
  {
    return new Rendering(emptyRDFSLabelSetting.toString());
  }

  public Rendering renderIfExistsDirective(IfExistsDirectiveNode ifExistsDirective) throws RendererException
  {
    return new Rendering(ifExistsDirective.toString());
  }

  public Rendering renderIfNotExistsDirective(IfNotExistsDirectiveNode ifNotExistsDirective) throws RendererException
  {
    return new Rendering(ifNotExistsDirective.toString());
  }
}
