package org.mm.renderer;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.DefaultDataValueNode;
import org.mm.parser.node.DefaultIDNode;
import org.mm.parser.node.DefaultLabelNode;
import org.mm.parser.node.DefaultLocationValueNode;
import org.mm.parser.node.DifferentFromNode;
import org.mm.parser.node.EmptyDataValueSettingNode;
import org.mm.parser.node.EmptyLocationSettingNode;
import org.mm.parser.node.EmptyRDFIDSettingNode;
import org.mm.parser.node.EmptyRDFSLabelSettingNode;
import org.mm.parser.node.EntityTypeNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.IfExistsDirectiveNode;
import org.mm.parser.node.IfNotExistsDirectiveNode;
import org.mm.parser.node.LanguageNode;
import org.mm.parser.node.LiteralNode;
import org.mm.parser.node.MMDefaultDatatypePropertyValueTypeNode;
import org.mm.parser.node.MMDefaultEntityTypeNode;
import org.mm.parser.node.MMDefaultPropertyTypeNode;
import org.mm.parser.node.MMDefaultPropertyValueTypeNode;
import org.mm.parser.node.MMDefaultValueEncodingNode;
import org.mm.parser.node.MMDirectiveNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.NamespaceNode;
import org.mm.parser.node.OWLAllValuesFromClassNode;
import org.mm.parser.node.OWLAllValuesFromDataTypeNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLCardinalityNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassOrRestrictionNode;
import org.mm.parser.node.OWLEnumeratedClassNode;
import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIndividualNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedClassNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLPropertyValueNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSomeValuesFromClassNode;
import org.mm.parser.node.OWLSomeValuesFromDataTypeNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.PrefixNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SameAsNode;
import org.mm.parser.node.ShiftSettingNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.StringLiteralNode;
import org.mm.parser.node.StringOrReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.TypesNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;

/**
 * This renderer simply produces the standard presentation syntax rendering of the supplied entities. Subclasses will specialize and perform custom actions for
 * individual entities.
 */
public class DefaultRenderer implements Renderer, MappingMasterParserConstants
{
  public Rendering renderExpression(ExpressionNode expressionNode) throws RendererException
  {
    if (expressionNode.hasMMDirective())
      return renderMMDirective(expressionNode.getMMDirectiveNode());
    else if (expressionNode.hasMMExpression())
      return renderMMExpression(expressionNode.getMMExpressionNode());
    else
      throw new RendererException("unknown expression type " + expressionNode);
  }

  public Rendering renderMMDirective(MMDirectiveNode mmDirectiveNode) throws RendererException
  {
    if (mmDirectiveNode.hasDefaultValueEncoding())
      return renderMMDefaultValueEncoding(mmDirectiveNode.getDefaultValueEncodingNode());
    else if (mmDirectiveNode.hasDefaultEntityType())
      return renderMMDefaultEntityType(mmDirectiveNode.getDefaultEntityTypeNode());
    else if (mmDirectiveNode.hasDefaultPropertyValueType())
      return renderMMDefaultPropertyValueType(mmDirectiveNode.getDefaultPropertyValueTypeNode());
    else
      throw new RendererException("unknown directive " + mmDirectiveNode);
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

  public Rendering renderMMExpression(MMExpressionNode mmExpressionNode) throws RendererException
  {
    if (mmExpressionNode.hasOWLClassDeclaration())
      return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
    else if (mmExpressionNode.hasOWLIndividualDeclaration())
      return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
    else
      throw new RendererException("unknown expression: " + mmExpressionNode);
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

        rendering.addText(subclassOfRendering.getTextRendering());
        isFirst = false;
      }
    }

    isFirst = true;
    if (owlClassDeclarationNode.hasEquivalentTo()) {
      rendering.addText(" EquivalentTo: ");
      for (OWLClassEquivalentToNode equivalentTo : owlClassDeclarationNode.getEquivalentToNodes()) {
        Rendering equivalentToRendering = renderOWLEquivalentTo(equivalentTo);
        if (!equivalentToRendering.nothingRendered())
          continue;

        if (!isFirst)
          rendering.addText(", ");

        rendering.addText(equivalentToRendering.getTextRendering());
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
        rendering.addText(factRendering.getTextRendering());
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

    rendering.addText(individualRendering.getTextRendering());

    if (owlIndividualDeclarationNode.hasFacts()) {
      rendering.addText(" Facts: ");

      for (FactNode fact : owlIndividualDeclarationNode.getFactNodes()) {
        Rendering factRendering = renderFact(fact);

        if (factRendering.nothingRendered())
          continue;

        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(factRendering.getTextRendering());
        isFirst = false;
      }
    }

    if (owlIndividualDeclarationNode.hasTypes()) {
      Rendering typesRendering = renderTypes(owlIndividualDeclarationNode.getTypeNodes());

      if (!typesRendering.nothingRendered())
        rendering.addText(" Types: " + typesRendering.getTextRendering());
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
        rendering.addText(factRendering.getTextRendering());
        isFirst = false;
      }
    }

    if (owlIndividualDeclarationNode.hasSameAs()) {
      Rendering sameAsRendering = renderSameAs(owlIndividualDeclarationNode.getSameAsNode());
      if (!sameAsRendering.nothingRendered())
        rendering.addText(sameAsRendering.getTextRendering());
    }

    if (owlIndividualDeclarationNode.hasDifferentFrom()) {
      Rendering differentFromRendering = renderDifferentFrom(owlIndividualDeclarationNode.getDifferentFromNode());
      if (!differentFromRendering.nothingRendered())
        rendering.addText(differentFromRendering.getTextRendering());
    }

    return rendering;
  }

  public Rendering renderFact(FactNode factNode) throws RendererException
  {
    Rendering propertyRendering = renderOWLProperty(factNode.getOWLPropertyNode());
    Rendering propertyValueRendering = renderOWLPropertyValue(factNode.getOWLPropertyValueNode());
    Rendering rendering = new Rendering();

    if (!propertyRendering.nothingRendered() && !propertyValueRendering.nothingRendered())
      rendering.addText(propertyRendering.getTextRendering() + " " + propertyValueRendering.getTextRendering());

    return rendering;
  }

  public Rendering renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException
  {
    Rendering propertyRendering = renderOWLProperty(annotationFactNode.getOWLPropertyNode());
    Rendering propertyValueRendering = renderOWLPropertyValue(annotationFactNode.getOWLPropertyValueNode());
    Rendering rendering = new Rendering();

    if (!propertyRendering.nothingRendered() && !propertyValueRendering.nothingRendered())
      rendering.addText(propertyRendering.getTextRendering() + " " + propertyValueRendering.getTextRendering());

    return rendering;
  }

  public Rendering renderOWLPropertyValue(OWLPropertyValueNode owlPropertyValueNode) throws RendererException
  {
    if (owlPropertyValueNode.isReference())
      return renderReference(owlPropertyValueNode.getReferenceNode());
    else if (owlPropertyValueNode.isName())
      return renderName(owlPropertyValueNode.getNameNode());
    else if (owlPropertyValueNode.isLiteral())
      return renderLiteral(owlPropertyValueNode.getLiteralNode());
    else
      throw new RendererException("unknown property value node " + owlPropertyValueNode);
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
          rendering.addText(intersectionRendering.getTextRendering());
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
          rendering.addText(classesOrRestrictionsRendering.getTextRendering());
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
      rendering.addText(renderOWLEnumeratedClass(classOrRestrictionNode.getOWLEnumeratedClassNode()).getTextRendering());
    else if (classOrRestrictionNode.hasOWLUnionClass())
      rendering.addText(renderOWLUnionClass(classOrRestrictionNode.getOWLUnionClassNode()).getTextRendering());
    else if (classOrRestrictionNode.hasOWLRestriction())
      rendering.addText(renderOWLRestriction(classOrRestrictionNode.getOWLRestrictionNode()).getTextRendering());
    else if (classOrRestrictionNode.hasOWLNamedClass())
      rendering.addText(renderOWLNamedClass(classOrRestrictionNode.getOWLNamedClassNode()).getTextRendering());
    else
      throw new RendererException("unexpected OWLClassOrRestriction node " + classOrRestrictionNode);

    if (!rendering.nothingRendered())
      if (classOrRestrictionNode.getIsNegated())
        rendering.addText("NOT " + rendering.getTextRendering());

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

      rendering.addText(individualRendering.getTextRendering());
    } else {
      boolean isFirst = true;

      rendering.addText("{");
      for (OWLIndividualNode owlIndividualNode : owlEnumeratedClassNode.getOWLIndividualNodes()) {
        if (!isFirst)
          rendering.addText(" ");
        individualRendering = renderOWLIndividual(owlIndividualNode);

        if (individualRendering.nothingRendered())
          return rendering;

        rendering.addText(individualRendering.getTextRendering());
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
      rendering.addText("(" + propertyRendering.getTextRendering() + " " + restrictionRendering.getTextRendering() + ")");

    return rendering;
  }

  public Rendering renderOWLMaxCardinality(OWLMaxCardinalityNode owlMaxCardinalityNode) throws RendererException
  {
    Rendering cardinalityRendering = new Rendering("" + owlMaxCardinalityNode.getCardinality());
    Rendering rendering = new Rendering();

    if (!cardinalityRendering.nothingRendered())
      rendering.addText("MAX " + cardinalityRendering.getTextRendering());

    return rendering;
  }

  public Rendering renderOWLMinCardinality(OWLMinCardinalityNode owlMinCardinalityNode) throws RendererException
  {
    Rendering cardinalityRendering = new Rendering("" + owlMinCardinalityNode.getCardinality());
    Rendering rendering = new Rendering();

    if (!cardinalityRendering.nothingRendered())
      rendering.addText("MIN " + cardinalityRendering.getTextRendering());

    return rendering;
  }

  public Rendering renderOWLCardinality(OWLCardinalityNode owlCardinalityNode) throws RendererException
  {
    Rendering cardinalityRendering = new Rendering("" + owlCardinalityNode.getCardinality());
    Rendering rendering = new Rendering();

    if (!cardinalityRendering.nothingRendered())
      rendering.addText("EXACTLY " + cardinalityRendering.getTextRendering());

    return rendering;
  }

  public Rendering renderOWLHasValue(OWLHasValueNode owlHasValueNode) throws RendererException
  {
    Rendering propertyValueRendering = renderOWLPropertyValue(owlHasValueNode.getOWLPropertyValueNode());
    Rendering rendering = new Rendering();

    if (!propertyValueRendering.nothingRendered())
      rendering.addText("VALUE " + propertyValueRendering.getTextRendering());

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
    if (owlIndividualNode.isReference())
      return renderReference(owlIndividualNode.getReferenceNode());
    else if (owlIndividualNode.isName())
      return renderName(owlIndividualNode.getNameNode());
    else
      throw new RendererException("unknown OWLIndividual node " + owlIndividualNode);
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
      rendering.addText("ONLY " + classRendering.getTextRendering());

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
      rendering.addText("SOME " + classRendering.getTextRendering());

    return rendering;
  }

  public Rendering renderOWLEquivalentTo(OWLClassEquivalentToNode owlClassEquivalentToNode) throws RendererException
  {
    Rendering classExpressionRendering;
    Rendering rendering = new Rendering();

    rendering.addText(" EquivalentTo: ");

    if (owlClassEquivalentToNode.getClassExpressionNodes().size() == 1) {
      classExpressionRendering = renderOWLClassExpression(owlClassEquivalentToNode.getClassExpressionNodes().get(0));
      if (classExpressionRendering.nothingRendered())
        return rendering;
      rendering.addText(classExpressionRendering.getTextRendering());
    } else {
      boolean isFirst = true;

      for (OWLClassExpressionNode owlClassExpressionNode : owlClassEquivalentToNode.getClassExpressionNodes()) {
        classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
        if (classExpressionRendering.nothingRendered())
          continue; // Any empty class expression will generate an empty rendering
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(classExpressionRendering.getTextRendering());
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
      rendering.addText(classExpressionRendering.getTextRendering());
    } else {
      boolean isFirst = true;

      for (OWLClassExpressionNode owlClassExpressionNode : owlSubclassOfNode.getClassExpressionNodes()) {
        classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
        if (classExpressionRendering.nothingRendered())
          continue; // Any empty class expression will generate an empty rendering
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(classExpressionRendering.getTextRendering());
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
        rendering.addText(prefixRendering.getTextRendering());
      }
    }

    if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      Rendering namespaceRendering = renderNamespace(referenceNode.getNamespaceNode());
      if (!namespaceRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        else
          atLeastOneOptionProcessed = true;
        rendering.addText(namespaceRendering.getTextRendering());
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
        rendering.addText(valueExtractionFunctionRendering.getTextRendering());
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
          rendering.addText(valueEncodingRendering.getTextRendering());
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
        rendering.addText(defaultLocationValueRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedDefaultDataValue()) {
      Rendering defaultDataValueRendering = renderDefaultDataValue(referenceNode.getDefaultDataValueNode());
      if (!defaultDataValueRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(defaultDataValueRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedDefaultID()) {
      Rendering defaultIDRendering = renderDefaultID(referenceNode.getDefaultRDFIDNode());
      if (!defaultIDRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(defaultIDRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedDefaultLabel()) {
      Rendering defaultLabelRendering = renderDefaultLabel(referenceNode.getDefaultRDFSLabelNode());
      if (!defaultLabelRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(defaultLabelRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedLanguage()) {
      Rendering languageRendering = renderLanguage(referenceNode.getLanguageNode());
      if (!languageRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(languageRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedPrefix()) {
      if (atLeastOneOptionProcessed)
        rendering.addText(" ");
      rendering.addText(renderPrefix(referenceNode.getPrefixNode()).getTextRendering());
      atLeastOneOptionProcessed = true;
    }

    if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      Rendering namespaceRendering = renderNamespace(referenceNode.getNamespaceNode());
      if (!namespaceRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(namespaceRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedEmptyLocationDirective()) {
      Rendering emptyLocationSettingRendering = renderEmptyLocationSetting(referenceNode.getEmptyLocationSettingNode());
      if (!emptyLocationSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(emptyLocationSettingRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedEmptyDataValueDirective()) {
      Rendering emptyDataValueSettingRendering = renderEmptyDataValueSetting(
        referenceNode.getEmptyDataValueSettingNode());
      if (!emptyDataValueSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(emptyDataValueSettingRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedEmptyRDFIDDirective()) {
      Rendering emptyRDFIFSettingRendering = renderEmptyRDFIDSetting(referenceNode.getEmptyRDFIDSettingNode());
      if (!emptyRDFIFSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(emptyRDFIFSettingRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedEmptyRDFSLabelDirective()) {
      Rendering emptyRDFSLabelSettingRendering = renderEmptyRDFSLabelSetting(
        referenceNode.getEmptyRDFSLabelSettingNode());
      if (emptyRDFSLabelSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(emptyRDFSLabelSettingRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedShiftDirective()) {
      Rendering shiftSettingRendering = renderShiftSetting(referenceNode.getShiftSettingNode());
      if (!shiftSettingRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(shiftSettingRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedTypes()) {
      Rendering typesRendering = renderTypes(referenceNode.getTypesNode());
      if (!typesRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(typesRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedIfExistsDirective()) {
      Rendering ifExistsRendering = renderIfExistsDirective(referenceNode.getIfExistsDirectiveNode());
      if (!ifExistsRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(ifExistsRendering.getTextRendering());
        atLeastOneOptionProcessed = true;
      }
    }

    if (referenceNode.hasExplicitlySpecifiedIfNotExistsDirective()) {
      Rendering ifExistsRendering = renderIfNotExistsDirective(referenceNode.getIfNotExistsDirectiveNode());
      if (!ifExistsRendering.nothingRendered()) {
        if (atLeastOneOptionProcessed)
          rendering.addText(" ");
        rendering.addText(ifExistsRendering.getTextRendering());
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
        rendering.addText(valueSpecificationRendering.getTextRendering());
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
        rendering.addText(valueSpecificationItemRendering.getTextRendering());
        isFirst = false;
      }
    }

    if (valueSpecificationNode.getNumberOfValueSpecificationItems() > 1)
      rendering.addText(")");

    return rendering;
  }

  public Rendering renderDefaultLocationValue(DefaultLocationValueNode defaultLocationValueNode)
    throws RendererException
  {
    return new Rendering(defaultLocationValueNode.toString());
  }

  public Rendering renderDefaultDataValue(DefaultDataValueNode defaultDataValueNode) throws RendererException
  {
    return new Rendering(defaultDataValueNode.toString());
  }

  public Rendering renderDefaultID(DefaultIDNode defaultIDNode) throws RendererException
  {
    return new Rendering(defaultIDNode.toString());
  }

  public Rendering renderDefaultLabel(DefaultLabelNode defaultLabelNode) throws RendererException
  {
    return new Rendering(defaultLabelNode.toString());
  }

  public Rendering renderValueSpecificationItem(ValueSpecificationItemNode valueSpecificationItemNode)
    throws RendererException
  {
    if (valueSpecificationItemNode.hasStringLiteral())
      return new Rendering("\"" + valueSpecificationItemNode.getStringLiteral() + "\"");
    else if (valueSpecificationItemNode.hasReference())
      return renderReference(valueSpecificationItemNode.getReferenceNode());
    else if (valueSpecificationItemNode.hasValueExtractionFunction())
      return renderValueExtractionFunction(valueSpecificationItemNode.getValueExtractionFunctionNode());
    else if (valueSpecificationItemNode.hasCapturingExpression())
      return new Rendering("[\"" + valueSpecificationItemNode.getCapturingExpression() + "\"]");
    else
      throw new RendererException("unknown ValueSpecificationItem node " + valueSpecificationItemNode);
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
        rendering.addText(typeRendering.getTextRendering());
        isFirst = false;
      }
    }

    return rendering;
  }

  public Rendering renderType(TypeNode typeNode) throws RendererException
  {
    if (typeNode instanceof ReferenceNode)
      return renderReference((ReferenceNode)typeNode);
    else if (typeNode instanceof OWLClassExpressionNode)
      return renderOWLClassExpression((OWLClassExpressionNode)typeNode);
    else
      throw new RendererException("do not know how to render type node " + typeNode.getNodeName());
  }

  public Rendering renderSameAs(SameAsNode sameAsNode) throws RendererException
  {
    Rendering rendering = new Rendering(" SameAs: ");
    boolean isFirst = true;

    for (OWLIndividualNode owlIndividualNode : sameAsNode.getIndividualNodes()) {
      Rendering individualRendering = renderOWLIndividual(owlIndividualNode);

      if (!individualRendering.nothingRendered()) {
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(individualRendering.getTextRendering());
        isFirst = false;
      }
    }
    return rendering;
  }

  public Rendering renderDifferentFrom(DifferentFromNode differentFromNode) throws RendererException
  {
    Rendering rendering = new Rendering(" DifferentFrom: ");
    boolean isFirst = true;

    for (OWLIndividualNode owlIndividualNode : differentFromNode.getIndividualNodes()) {
      Rendering individualRendering = renderOWLIndividual(owlIndividualNode);

      if (!individualRendering.nothingRendered()) {
        if (!isFirst)
          rendering.addText(", ");
        rendering.addText(individualRendering.getTextRendering());
        isFirst = false;
      }
    }

    return rendering;
  }

  public Rendering renderValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode)
    throws RendererException
  {
    Rendering rendering = new Rendering(valueExtractionFunctionNode.getFunctionName());

    if (valueExtractionFunctionNode.hasArguments()) {
      boolean isFirst = true;
      rendering.addText("(");

      for (StringOrReferenceNode stringOrReferenceNode : valueExtractionFunctionNode.getArgumentNodes()) {
        Rendering stringOrReferenceRendering = renderStringOrReference(stringOrReferenceNode);
        if (!stringOrReferenceRendering.nothingRendered()) {
          if (!isFirst)
            rendering.addText(" ");
          rendering.addText(stringOrReferenceRendering.getTextRendering());
          isFirst = false;
        }
      }

      rendering.addText(")");
    }

    return rendering;
  }

  public Rendering renderSourceSpecification(SourceSpecificationNode sourceSpecificationNode) throws RendererException
  {
    Rendering rendering = new Rendering("@");

    if (sourceSpecificationNode.hasSource())
      rendering.addText("'" + sourceSpecificationNode.getSource() + "'!");

    if (sourceSpecificationNode.hasLocation())
      rendering.addText(sourceSpecificationNode.getLocation());
    else
      rendering.addText("\"" + sourceSpecificationNode.getLiteral() + "\""); // A literal

    return rendering;
  }

  public Rendering renderStringOrReference(StringOrReferenceNode stringOrReferenceNode) throws RendererException
  {
    if (stringOrReferenceNode.isString())
      return renderStringLiteral(stringOrReferenceNode.getStringLiteralNode());
    else if (stringOrReferenceNode.isReference())
      return renderReference(stringOrReferenceNode.getReferenceNode());
    else
      throw new RendererException("unknown StringOrReference node " + stringOrReferenceNode);
  }

  public Rendering renderStringLiteral(StringLiteralNode stringLiteralNode) throws RendererException
  {
    return new Rendering(stringLiteralNode.toString());
  }

  public Rendering renderShiftSetting(ShiftSettingNode shiftSettingNode) throws RendererException
  {
    return new Rendering(shiftSettingNode.toString());
  }

  public Rendering renderEmptyLocationSetting(EmptyLocationSettingNode emptyLocationSettingNode)
    throws RendererException
  {
    return new Rendering(emptyLocationSettingNode.toString());
  }

  public Rendering renderEmptyDataValueSetting(EmptyDataValueSettingNode emptyDataValueSettingNode)
    throws RendererException
  {
    return new Rendering(emptyDataValueSettingNode.toString());
  }

  public Rendering renderEmptyRDFIDSetting(EmptyRDFIDSettingNode emptyRDFIDSettingNode) throws RendererException
  {
    return new Rendering(emptyRDFIDSettingNode.toString());
  }

  public Rendering renderEmptyRDFSLabelSetting(EmptyRDFSLabelSettingNode emptyRDFSLabelSettingNode)
    throws RendererException
  {
    return new Rendering(emptyRDFSLabelSettingNode.toString());
  }

  public Rendering renderIfExistsDirective(IfExistsDirectiveNode ifExistsDirectiveNode) throws RendererException
  {
    return new Rendering(ifExistsDirectiveNode.toString());
  }

  public Rendering renderIfNotExistsDirective(IfNotExistsDirectiveNode ifNotExistsDirectiveNode)
    throws RendererException
  {
    return new Rendering(ifNotExistsDirectiveNode.toString());
  }
}
