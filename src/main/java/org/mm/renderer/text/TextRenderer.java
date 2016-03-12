package org.mm.renderer.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLAnnotationPropertyNode;
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
import org.mm.parser.node.OWLPropertyAssertionNode;
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
import org.mm.parser.node.ValueEncodingDirectiveNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.renderer.ClassExpressionRenderer;
import org.mm.renderer.DeclarationRenderer;
import org.mm.renderer.EntityRenderer;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.LiteralRenderer;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.ReferenceRendererConfiguration;
import org.mm.renderer.ReferenceUtil;
import org.mm.renderer.Renderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.LiteralRendering;
import org.mm.rendering.ReferenceRendering;
import org.mm.rendering.text.TextLiteralRendering;
import org.mm.rendering.text.TextReferenceRendering;
import org.mm.rendering.text.TextRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;

// TODO Refactor - too long. Look at the OWLAPI renderer for example of decomposition.

/**
 * This renderer produces a text rendering of a Mapping Master expression with
 * reference values substituted inline.
 */
public class TextRenderer extends ReferenceRendererConfiguration implements Renderer, ReferenceRenderer,
      DeclarationRenderer, EntityRenderer, LiteralRenderer, ClassExpressionRenderer,
      MappingMasterParserConstants
{
   private SpreadSheetDataSource dataSource;

   private boolean isCommented = false;

   private final Map<SpreadsheetLocation, String> createdLabelsUsingLocation = new HashMap<>();

   private final static String INDENT = "   ";
   private final static String NEWLINE = "\n";

   public final static String COMMENT_HEADER = "   # ";

   public TextRenderer(SpreadSheetDataSource dataSource)
   {
      this.dataSource = dataSource;
   }

   public void setComment(boolean option)
   {
      isCommented = option;
   }

   @Override
   public SpreadSheetDataSource getDataSource()
   {
      return dataSource;
   }

   @Override
   public ReferenceRendererConfiguration getReferenceRendererConfiguration()
   {
      return this;
   }

   @Override
   public Optional<? extends TextRendering> render(MMExpressionNode node) throws RendererException
   {
      if (node.hasOWLClassDeclaration()) {
         return renderOWLClassDeclaration(node.getOWLClassDeclarationNode());
      } else if (node.hasOWLIndividualDeclaration()) {
         return renderOWLIndividualDeclaration(node.getOWLIndividualDeclarationNode());
      }
      throw new InternalRendererException("Unknown child for node " + node.getNodeName());
   }

   // TODO Refactor - too long
   @Override
   public Optional<? extends TextReferenceRendering> renderReference(ReferenceNode referenceNode) throws RendererException
   {
      ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
      if (referenceType.isUntyped()) {
         throw new RendererException("Untyped reference " + referenceNode);
      }

      SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
      if (sourceSpecificationNode.hasLiteral()) {
         String literalValue = sourceSpecificationNode.getLiteral();
         TextReferenceRendering rendering = new TextReferenceRendering(literalValue, referenceType);
         if (isCommented) rendering.addComment(createComment(literalValue, referenceNode));
         return Optional.of(rendering);

      } else if (sourceSpecificationNode.hasLocation()) {
         SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
         Optional<String> resolvedValue = ReferenceUtil.resolveReferenceValue(dataSource, referenceNode);
         
         if (!resolvedValue.isPresent()) {
            switch (referenceNode.getActualEmptyLocationDirective()) {
               case MM_PROCESS_IF_EMPTY_LOCATION:
                  resolvedValue = Optional.of("");
                  break;
               case MM_SKIP_IF_EMPTY_LOCATION:
                  break;
               case MM_WARNING_IF_EMPTY_LOCATION:
                  // TODO Warn in log files
                  break;
               case MM_ERROR_IF_EMPTY_LOCATION:
                  throw new RendererException("Empty cell values for " + referenceNode + " at location " + location);
            }
         }
         
         if (referenceNode.hasLiteralType()) { // Reference is an OWL literal
            String literal = processOWLLiteralReferenceValue(location, resolvedValue, referenceNode);
            if (literal.isEmpty()) {
               switch (referenceNode.getActualEmptyLiteralDirective()) {
                  case MM_SKIP_IF_EMPTY_LITERAL:
                     return Optional.empty();
                  case MM_WARNING_IF_EMPTY_LITERAL:
                     // TODO Warn in log file
                     return Optional.empty();
                  case MM_ERROR_IF_EMPTY_LITERAL:
                     throw new RendererException("Empty literal for " + referenceNode + " at location " + location);
               }
            }
            TextReferenceRendering rendering = new TextReferenceRendering(literal, referenceType);
            if (isCommented) rendering.addComment(createComment(literal, referenceNode));
            return Optional.of(rendering);
         } else if (referenceNode.hasEntityType()) { // Reference is an OWL entity
            // TODO If the rendering uses the ID then we should use it
            String entityName = getReferenceRDFID(resolvedValue, referenceNode);
            if (referenceNode.hasRDFIDValueEncoding() && entityName.isEmpty()) {
               switch (referenceNode.getActualEmptyRDFIDDirective()) {
                  case MM_SKIP_IF_EMPTY_ID:
                     return Optional.empty();
                  case MM_WARNING_IF_EMPTY_ID:
                     // TODO Warn in log file
                     return Optional.empty();
                  case MM_ERROR_IF_EMPTY_ID:
                     throw new RendererException("Empty rdf:ID for " + referenceNode + " at location " + location);
               }
            }
            String entityLabel = getReferenceRDFSLabel(resolvedValue, referenceNode);
            if (referenceNode.hasRDFSLabelValueEncoding() && entityLabel.isEmpty()) {
               switch (referenceNode.getActualEmptyRDFSLabelDirective()) {
                  case MM_SKIP_IF_EMPTY_LABEL:
                     return Optional.empty();
                  case MM_WARNING_IF_EMPTY_LABEL:
                     // TODO Warn in log file
                     return Optional.empty();
                  case MM_ERROR_IF_EMPTY_LABEL:
                     throw new RendererException("Empty rdfs:label for " + referenceNode + " at location " + location);
               }
            }
            TextReferenceRendering rendering = createTextReferenceRendering(entityName, entityLabel, referenceNode);
            if (isCommented) rendering.addComment(createComment(entityLabel, referenceNode));
            return Optional.of(rendering);
         } else if (referenceType.isIRI()) {
            String iri = resolvedValue.get();
            TextReferenceRendering rendering = new TextReferenceRendering(iri, referenceType);
            if (isCommented) rendering.addComment(createComment(iri, referenceNode));
            return Optional.of(rendering);
         }
         throw new InternalRendererException("Unknown type '" + referenceType + "' for reference node: " + referenceNode);
      }
      throw new InternalRendererException("Unknown definition for reference node: " + referenceNode);
   }

   private TextReferenceRendering createTextReferenceRendering(String entityName, String entityLabel, ReferenceNode referenceNode)
         throws RendererException
   {
      ReferenceType referenceType = getReferenceType(referenceNode);
      if (referenceNode.getReferenceDirectives().usesLocationEncoding()) {
         SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
         String label = getLabelUsingLocationEncoding(location);
         return new TextReferenceRendering(label, referenceType);
      } else {
         if (!entityName.isEmpty()) {
            return new TextReferenceRendering(entityLabel, referenceType);
         } else {
            return new TextReferenceRendering(entityLabel, referenceType);
         }
      }
   }

   private ReferenceType getReferenceType(ReferenceNode referenceNode) throws RendererException
   {
      ReferenceType referenceType = referenceNode.getReferenceType();
      if (referenceType.isUntyped()) {
         throw new RendererException("Untyped reference " + referenceNode);
      }
      return referenceType;
   }

   private String processOWLLiteralReferenceValue(SpreadsheetLocation location, Optional<String> resolvedValue,
         ReferenceNode referenceNode) throws RendererException
   {
      String literal = "";
      if (resolvedValue.isPresent()) {
         String value = resolvedValue.get();
         if (referenceNode.hasLiteralValueEncoding()) {
            if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding()) {
               literal = generateReferenceValue(value, referenceNode.getLiteralValueEncodingNode(), referenceNode);
            } else if (referenceNode.hasValueExtractionFunctionNode()) {
               ValueExtractionFunctionNode valueExtractionFunctionNode = referenceNode.getValueExtractionFunctionNode();
               literal = generateReferenceValue(value, valueExtractionFunctionNode);
            } else {
               literal = value;
            }
         }
      }
      if (referenceNode.hasExplicitlySpecifiedDefaultLiteral()) {
         literal = referenceNode.getActualDefaultLiteral();
      }
      return literal;
   }

   private String generateReferenceValue(String resolvedValue, ValueEncodingDirectiveNode valueEncoding,
         ReferenceNode referenceNode) throws RendererException
   {
      if (valueEncoding != null) {
         if (valueEncoding.hasValueSpecificationNode()) {
            return generateReferenceValue(resolvedValue, valueEncoding.getValueSpecificationNode(), referenceNode);
         } else {
            return resolvedValue;
         }
      } else {
         return resolvedValue;
      }
   }

   private String generateReferenceValue(String resolvedValue, ValueSpecificationNode valueSpecifications,
         ReferenceNode referenceNode) throws RendererException
   {
      String processedValue = "";
      for (ValueSpecificationItemNode specificationItem : valueSpecifications.getValueSpecificationItemNodes()) {
         if (specificationItem.hasStringLiteral()) {
            processedValue += specificationItem.getStringLiteral();
         } else if (specificationItem.hasReferenceNode()) {
            ReferenceNode specificationReferenceNode = specificationItem.getReferenceNode();
            specificationReferenceNode.setDefaultShiftSetting(referenceNode.getActualShiftDirective());
            Optional<? extends ReferenceRendering> referenceRenderingResult = renderReference(specificationReferenceNode);
            if (referenceRenderingResult.isPresent()) {
               ReferenceRendering referenceRendering = referenceRenderingResult.get();
               if (referenceRendering.isOWLLiteral()) {
                  processedValue += referenceRendering.getRawValue();
               } else {
                  throw new RendererException("Expecting OWL literal for value specification, got " + referenceRendering);
               }
            }
         } else if (specificationItem.hasValueExtractionFunctionNode()) {
            ValueExtractionFunctionNode valueExtractionFunction = specificationItem.getValueExtractionFunctionNode();
            processedValue += generateReferenceValue(resolvedValue, valueExtractionFunction);
         } else if (specificationItem.hasCapturingExpression() && resolvedValue != null) {
            String capturingExpression = specificationItem.getCapturingExpression();
            processedValue += ReferenceUtil.capture(resolvedValue, capturingExpression);
         }
      }
      return processedValue;
   }

   private String generateReferenceValue(String resolvedValue, ValueExtractionFunctionNode functionNode)
         throws RendererException
   {
      final List<String> arguments = new ArrayList<>();
      if (functionNode.hasArguments()) {
         for (ValueExtractionFunctionArgumentNode argumentNode : functionNode.getArgumentNodes()) {
            String argumentValue = generateValueExtractionFunctionArgument(argumentNode);
            arguments.add(argumentValue);
         }
      }
      return ReferenceUtil.evaluateReferenceValue(functionNode.getFunctionName(), functionNode.getFunctionID(),
            arguments, resolvedValue, functionNode.hasArguments());
   }

   private String getReferenceRDFID(Optional<String> resolvedValue, ReferenceNode referenceNode) throws RendererException
   {
      String id = "";
      if (resolvedValue.isPresent()) {
         String value = resolvedValue.get();
         if (referenceNode.hasRDFIDValueEncoding()) {
            if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding()) {
               id = generateReferenceValue(value, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
            } else if (referenceNode.hasValueExtractionFunctionNode()) {
               id = generateReferenceValue(value, referenceNode.getValueExtractionFunctionNode());
            } else {
               id = value;
            }
         }
      }
      if (referenceNode.hasExplicitlySpecifiedDefaultRDFID()) {
         id = referenceNode.getActualDefaultRDFID();
      } else if (referenceNode.hasExplicitlySpecifiedDefaultLocationValue()) {
         id = referenceNode.getActualDefaultLocationValue();
      }
      return id;
   }

   private String getReferenceRDFSLabel(Optional<String> resolvedValue, ReferenceNode referenceNode) throws RendererException
   {
      String label = "";
      if (resolvedValue.isPresent()) {
         String value = resolvedValue.get();
         if (referenceNode.hasRDFSLabelValueEncoding()) {
            if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding()) {
               label = generateReferenceValue(value, referenceNode.getRDFSLabelValueEncodingNode(), referenceNode);
            } else if (referenceNode.hasValueExtractionFunctionNode()) {
               label = generateReferenceValue(value, referenceNode.getValueExtractionFunctionNode());
            } else {
               label = value;
            }
         }
      }
      if (referenceNode.hasExplicitlySpecifiedDefaultRDFSLabel()) {
         label = referenceNode.getActualDefaultRDFSLabel();
      } else if (referenceNode.hasExplicitlySpecifiedDefaultLocationValue()) {
         label = referenceNode.getActualDefaultLocationValue();
      }
      return label;
   }

   private String getLabelUsingLocationEncoding(SpreadsheetLocation location) throws RendererException
   {
      String label = "";
      if (hasLabelBeenCreatedAtLocation(location)) {
         label = getLabelAtLocation(location);
      } else {
         label = ReferenceUtil.createNameUsingCellLocation(location);
         recordLabelAtLocation(location, label);
      }
      return label;
   }

   private boolean hasLabelBeenCreatedAtLocation(SpreadsheetLocation location)
   {
      return createdLabelsUsingLocation.containsKey(location);
   }

   private String getLabelAtLocation(SpreadsheetLocation location)
   {
      return createdLabelsUsingLocation.get(location);
   }

   private void recordLabelAtLocation(SpreadsheetLocation location, String label)
   {
      createdLabelsUsingLocation.put(location, label);
   }

   @Override
   public Optional<? extends TextRendering> renderOWLClassDeclaration(OWLClassDeclarationNode classDeclarationNode)
         throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder(NEWLINE);
      OWLClassNode declaredClassNode = classDeclarationNode.getOWLClassNode();
      Optional<? extends TextRendering> textRenderingResult = null;

      /*
       * Rendering OWL Class
       */
      textRenderingResult = renderOWLClass(declaredClassNode, true);
      if (!textRenderingResult.isPresent()) {
         return Optional.empty();
      }
      TextRendering declaredClassRendering = textRenderingResult.get();
      textRepresentation.append("Class: ");
      textRepresentation.append(declaredClassRendering.getRendering());
      if (declaredClassRendering.hasComment()) {
         textRepresentation.append(comment(declaredClassRendering.getComment()));
      }
      textRepresentation.append(NEWLINE);

      /*
       * Rendering OWL Class' SubClassOf
       */
      if (classDeclarationNode.hasOWLSubclassOfNodes()) {
         for (OWLSubclassOfNode subClassOfNode : classDeclarationNode.getOWLSubclassOfNodes()) {
            textRenderingResult = renderOWLSubClassOf(classDeclarationNode.getOWLClassNode(), subClassOfNode);
            if (textRenderingResult.isPresent()) {
               TextRendering subClassOfRendering = textRenderingResult.get();
               textRepresentation.append(subClassOfRendering.getRendering());
               textRepresentation.append(NEWLINE);
            }
         }
      }
      /*
       * Rendering OWL Class' EquivalentClasses
       */
      if (classDeclarationNode.hasOWLEquivalentClassesNode()) {
         for (OWLEquivalentClassesNode equivalentToNode : classDeclarationNode.getOWLEquivalentClassesNodes()) {
            textRenderingResult = renderOWLEquivalentClasses(classDeclarationNode.getOWLClassNode(), equivalentToNode);
            if (textRenderingResult.isPresent()) {
               TextRendering equivalentToRendering = textRenderingResult.get();
               textRepresentation.append(equivalentToRendering.getRendering());
               textRepresentation.append(NEWLINE);
            }
         }
      }
      /*
       * Rendering OWL Class' Annotation
       */
      boolean isFirst = true;
      if (classDeclarationNode.hasAnnotationFactNodes()) {
         for (AnnotationFactNode annotationFactNode : classDeclarationNode.getAnnotationFactNodes()) {
            textRenderingResult = renderAnnotationFact(annotationFactNode);
            if (textRenderingResult.isPresent()) {
               TextRendering annotationRendering = textRenderingResult.get();
               if (isFirst) {
                  textRepresentation.append(INDENT).append("Annotations: ");
               } else {
                  textRepresentation.append(",");
                  textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
               }
               textRepresentation.append(annotationRendering.getRendering());
               if (annotationRendering.hasComment()) {
                  textRepresentation.append(comment(annotationRendering.getComment()));
               }
               isFirst = false;
            }
         }
         textRepresentation.append(NEWLINE);
      }
      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   /**
    * Arguments to value extraction functions cannot be dropped if the reference
    * resolves to nothing.
    */
   private String generateValueExtractionFunctionArgument(ValueExtractionFunctionArgumentNode functionArgumentNode)
         throws RendererException
   {
      if (functionArgumentNode.isOWLLiteralNode()) {
         Optional<? extends LiteralRendering> literalRendering = renderOWLLiteral(
               functionArgumentNode.getOWLLiteralNode());
         if (literalRendering.isPresent()) {
            return literalRendering.get().getRawValue();
         } else {
            throw new RendererException("Empty literal for value extraction function argument");
         }
      } else if (functionArgumentNode.isReferenceNode()) {
         ReferenceNode referenceNode = functionArgumentNode.getReferenceNode();
         Optional<? extends ReferenceRendering> referenceRendering = renderReference(referenceNode);
         if (referenceRendering.isPresent()) {
            if (referenceRendering.get().isOWLLiteral()) {
               return referenceRendering.get().getRawValue();
            } else {
               throw new RendererException(
                     "Expecting literal reference for function argument, got " + functionArgumentNode);
            }
         } else {
            throw new RendererException("Empty reference " + referenceNode + " for function argument");
         }
      }
      throw new InternalRendererException("Unknown child for node " + functionArgumentNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLIndividualDeclaration(
         OWLIndividualDeclarationNode individualDeclarationNode) throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder(NEWLINE);
      Optional<? extends TextRendering> textRenderingResult = null;

      /*
       * Rendering OWL Individual.
       */
      textRenderingResult = renderOWLNamedIndividual(individualDeclarationNode.getOWLIndividualNode(), true);
      if (!textRenderingResult.isPresent()) {
         return Optional.empty();
      }
      TextRendering individualRendering = textRenderingResult.get();
      textRepresentation.append("Individual: ");
      textRepresentation.append(individualRendering.getRendering());
      if (individualRendering.hasComment()) {
         textRepresentation.append(comment(individualRendering.getComment()));
      }
      textRepresentation.append(NEWLINE);

      /*
       * Rendering OWL Individual's facts
       */
      boolean isFirst = true;
      if (individualDeclarationNode.hasFacts()) {
         for (FactNode factNode : individualDeclarationNode.getFactNodes()) {
            textRenderingResult = renderFact(factNode);
            if (textRenderingResult.isPresent()) {
               TextRendering factRendering = textRenderingResult.get();
               if (isFirst) {
                  textRepresentation.append(INDENT).append("Facts: ");
               } else {
                  textRepresentation.append(",");
                  textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
               }
               textRepresentation.append(factRendering.getRendering());
               if (factRendering.hasComment()) {
                  textRepresentation.append(comment(factRendering.getComment()));
               }
               isFirst = false;
            }
         }
         textRepresentation.append(NEWLINE);
      }
      /*
       * Rendering OWL Individual's types
       */
      if (individualDeclarationNode.hasTypes()) {
         textRenderingResult = renderTypes(individualDeclarationNode.getTypesNode());
         if (textRenderingResult.isPresent()) {
            TextRendering typesRendering = textRenderingResult.get();
            textRepresentation.append(INDENT).append("Types: ");
            textRepresentation.append(typesRendering.getRendering());
            if (typesRendering.hasComment()) {
               textRepresentation.append(comment(typesRendering.getComment()));
            }
            textRepresentation.append(NEWLINE);
         }
      }
      /*
       * Rendering OWL Individual's annotations
       */
      isFirst = true;
      if (individualDeclarationNode.hasAnnotations()) {
         for (AnnotationFactNode factNode : individualDeclarationNode.getAnnotationNodes()) {
            textRenderingResult = renderAnnotationFact(factNode);
            if (textRenderingResult.isPresent()) {
               if (isFirst) {
                  textRepresentation.append(INDENT).append("Annotations: ");
               } else {
                  textRepresentation.append(",");
                  textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
               }
               TextRendering annotationRendering = textRenderingResult.get();
               textRepresentation.append(annotationRendering.getRendering());
               if (annotationRendering.hasComment()) {
                  textRepresentation.append(comment(annotationRendering.getComment()));
               }
               isFirst = false;
            }
         }
         textRepresentation.append(NEWLINE);
      }
      /*
       * Rendering OWL Individual's hasSameAs axiom
       */
      if (individualDeclarationNode.hasSameAs()) {
         textRenderingResult = renderOWLSameAs(individualDeclarationNode.getOWLSameAsNode());
         if (textRenderingResult.isPresent()) {
            TextRendering sameAsRendering = textRenderingResult.get();
            textRepresentation.append(sameAsRendering.getRendering());
            textRepresentation.append(NEWLINE);
         }
      }
      /*
       * Rendering OWL Individuals' hasDifferentFrom axiom
       */
      if (individualDeclarationNode.hasDifferentFrom()) {
         textRenderingResult = renderOWLDifferentFrom(individualDeclarationNode.getOWLDifferentFromNode());
         if (textRenderingResult.isPresent()) {
            TextRendering differentFromRendering = textRenderingResult.get();
            textRepresentation.append(differentFromRendering.getRendering());
            textRepresentation.append(NEWLINE);
         }
      }
      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   @Override
   public Optional<? extends TextRendering> renderOWLClassExpression(OWLClassExpressionNode classExpressionNode)
         throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> expressionRenderingResult = null;

      if (classExpressionNode.hasOWLUnionClassNode()) {
         expressionRenderingResult = renderOWLUnionClass(classExpressionNode.getOWLUnionClassNode());
         if (expressionRenderingResult.isPresent()) {
            TextRendering unionClassRendering = expressionRenderingResult.get();
            textRepresentation.append(unionClassRendering.getRendering());
         }
      } else if (classExpressionNode.hasOWLRestrictionNode()) {
         expressionRenderingResult = renderOWLRestriction(classExpressionNode.getOWLRestrictionNode());
         if (expressionRenderingResult.isPresent()) {
            TextRendering restrictionRendering = expressionRenderingResult.get();
            textRepresentation.append(restrictionRendering.getRendering());
         }
      } else if (classExpressionNode.hasOWLClassNode()) {
         expressionRenderingResult = renderOWLClass(classExpressionNode.getOWLClassNode(), false);
         if (expressionRenderingResult.isPresent()) {
            TextRendering classRendering = expressionRenderingResult.get();
            textRepresentation.append(classRendering.getRendering());
         }
      } else {
         throw new RendererException("unexpected child for node " + classExpressionNode.getNodeName());
      }

      if (classExpressionNode.getIsNegated()) {
         textRepresentation.insert(0, "NOT ");
      }
      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   @Override
   public Optional<? extends TextRendering> renderOWLIntersectionClass(OWLIntersectionClassNode intersectionClassNode)
         throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> expressionRenderingResult = null;

      if (intersectionClassNode.getOWLClassExpressionNodes().size() == 1) {
         return renderOWLClassExpression(intersectionClassNode.getOWLClassExpressionNodes().get(0));
      }

      boolean isFirst = true;
      for (OWLClassExpressionNode classExpressionNode : intersectionClassNode.getOWLClassExpressionNodes()) {
         expressionRenderingResult = renderOWLClassExpression(classExpressionNode);
         if (expressionRenderingResult.isPresent()) {
            TextRendering classExpressionRendering = expressionRenderingResult.get();
            if (isFirst) {
               textRepresentation.append("(");
            } else {
               textRepresentation.append(" AND");
               textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
            }
            textRepresentation.append(classExpressionRendering.getRendering());
            isFirst = false;
         }
      }
      textRepresentation.append(")");

      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   @Override
   public Optional<? extends TextRendering> renderOWLEquivalentClasses(OWLClassNode classNode,
         OWLEquivalentClassesNode equivalentClassesNode) throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> expressionRenderingResult = null;

      boolean isFirst = true;
      for (OWLClassExpressionNode expressionNode : equivalentClassesNode.getClassExpressionNodes()) {
         expressionRenderingResult = renderOWLClassExpression(expressionNode);
         if (expressionRenderingResult.isPresent()) {
            TextRendering classExpressionRendering = expressionRenderingResult.get();
            if (isFirst) {
               textRepresentation.append(INDENT).append("EquivalentTo: ");
            } else {
               textRepresentation.append(",");
               textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
            }
            textRepresentation.append(classExpressionRendering.getRendering());
            if (classExpressionRendering.hasComment()) {
               textRepresentation.append(comment(classExpressionRendering.getComment()));
            }
            isFirst = false;
         }
      }
      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   @Override
   public Optional<? extends TextRendering> renderOWLUnionClass(OWLUnionClassNode unionNode) throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> expressionRenderingResult = null;

      if (unionNode.getOWLIntersectionClassNodes().size() == 1) {
         return renderOWLIntersectionClass(unionNode.getOWLIntersectionClassNodes().get(0));
      }

      boolean isFirst = true;
      for (OWLIntersectionClassNode intersectionClassNode : unionNode.getOWLIntersectionClassNodes()) {
         expressionRenderingResult = renderOWLIntersectionClass(intersectionClassNode);
         if (expressionRenderingResult.isPresent()) {
            TextRendering intersectionRendering = expressionRenderingResult.get();
            if (isFirst) {
               textRepresentation.append("(");
            } else {
               textRepresentation.append(" OR");
               textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
            }
            textRepresentation.append(intersectionRendering.getRendering());
            isFirst = false;
         }
      }
      textRepresentation.append(")");

      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   @Override
   public Optional<? extends TextRendering> renderOWLProperty(OWLPropertyNode propertyNode) throws RendererException
   {
      if (propertyNode.hasReferenceNode()) {
         return renderReference(propertyNode.getReferenceNode());
      } else if (propertyNode.hasNameNode()) {
         return renderName(propertyNode.getNameNode());
      }
      throw new InternalRendererException("Unknown child for node " + propertyNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLAnnotationProperty(OWLAnnotationPropertyNode propertyNode)
         throws RendererException
   {
      if (propertyNode.hasReferenceNode()) {
         return renderReference(propertyNode.getReferenceNode());
      } else if (propertyNode.hasNameNode()) {
         return renderName(propertyNode.getNameNode());
      }
      throw new InternalRendererException("Unknown child for node " + propertyNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLRestriction(OWLRestrictionNode restrictionNode)
         throws RendererException
   {
      Optional<? extends TextRendering> propertyRenderingResult = renderOWLProperty(
            restrictionNode.getOWLPropertyNode());

      Optional<? extends TextRendering> restrictionRenderingResult = null;
      if (restrictionNode.isOWLMinCardinality()) {
         restrictionRenderingResult = renderOWLMinCardinality(restrictionNode.getOWLPropertyNode(),
               restrictionNode.getOWLMinCardinalityNode());
      } else if (restrictionNode.isOWLMaxCardinality()) {
         restrictionRenderingResult = renderOWLMaxCardinality(restrictionNode.getOWLPropertyNode(),
               restrictionNode.getOWLMaxCardinalityNode());
      } else if (restrictionNode.isOWLExactCardinality()) {
         restrictionRenderingResult = renderOWLExactCardinality(restrictionNode.getOWLPropertyNode(),
               restrictionNode.getOWLExactCardinalityNode());
      } else if (restrictionNode.isOWLHasValue()) {
         restrictionRenderingResult = renderOWLHasValue(restrictionNode.getOWLPropertyNode(),
               restrictionNode.getOWLHasValueNode());
      } else if (restrictionNode.isOWLAllValuesFrom()) {
         restrictionRenderingResult = renderOWLAllValuesFrom(restrictionNode.getOWLPropertyNode(),
               restrictionNode.getOWLAllValuesFromNode());
      } else if (restrictionNode.isOWLSomeValuesFrom()) {
         restrictionRenderingResult = renderOWLSomeValuesFrom(restrictionNode.getOWLPropertyNode(),
               restrictionNode.getOWLSomeValuesFromNode());
      } else {
         throw new InternalRendererException("Unknown child for node " + restrictionNode.getNodeName());
      }

      if (propertyRenderingResult.isPresent() && restrictionRenderingResult.isPresent()) {
         TextRendering propertyRendering = propertyRenderingResult.get();
         TextRendering restrictionRendering = restrictionRenderingResult.get();

         String textRepresentation = "(" + propertyRendering.getRendering() + " " + restrictionRendering.getRendering()
               + ")";
         TextRendering rendering = new TextRendering(textRepresentation);
         rendering.addComment(propertyRendering.getComment());
         rendering.addComment(restrictionRendering.getComment());
         return Optional.of(rendering);
      }
      return Optional.empty();
   }

   @Override
   public Optional<? extends TextRendering> renderOWLObjectHasValue(OWLPropertyNode propertyNode,
         OWLHasValueNode hasValueNode) throws RendererException
   {
      if (hasValueNode.hasReferenceNode()) {
         return renderReference(hasValueNode.getReferenceNode());
      } else if (hasValueNode.hasNameNone()) {
         return renderName(hasValueNode.getNameNode());
      }
      throw new InternalRendererException("Unknown child for node " + hasValueNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLDataHasValue(OWLPropertyNode propertyNode,
         OWLHasValueNode dataHasValueNode) throws RendererException
   {
      if (dataHasValueNode.hasReferenceNode()) {
         return renderReference(dataHasValueNode.getReferenceNode());
      } else if (dataHasValueNode.hasLiteralNode()) {
         return renderOWLLiteral(dataHasValueNode.getOWLLiteralNode());
      }
      throw new InternalRendererException("Unknown child for node " + dataHasValueNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
         OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException
   {
      String datatypeName = dataAllValuesFromNode.getDatatypeName();
      if (!datatypeName.isEmpty()) {
         return Optional.of(new TextRendering("ONLY " + datatypeName));
      }
      return Optional.empty();
   }

   @Override
   public Optional<? extends TextRendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode propertyNode,
         OWLObjectSomeValuesFromNode objectSomeValuesFromNode) throws RendererException
   {
      if (objectSomeValuesFromNode.hasOWLClassNode()) {
         return renderOWLClass(objectSomeValuesFromNode.getOWLClassNode(), false);
      } else if (objectSomeValuesFromNode.hasOWLClassExpressionNode()) {
         return renderOWLClassExpression(objectSomeValuesFromNode.getOWLClassExpressionNode());
      }
      throw new InternalRendererException("Unknown child for node " + objectSomeValuesFromNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLDataSomeValuesFrom(OWLPropertyNode propertyNode,
         OWLDataSomeValuesFromNode dataSomeValuesFromNode) throws RendererException
   {
      String datatypeName = dataSomeValuesFromNode.getDatatypeName();
      if (!datatypeName.isEmpty()) {
         return Optional.of(new TextRendering(datatypeName));
      }
      return Optional.empty();
   }

   private Optional<? extends TextRendering> renderOWLSubClassOf(OWLClassNode classNode,
         OWLSubclassOfNode subClassOfNode) throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> classRenderingResult = null;

      boolean isFirst = true;
      for (OWLClassExpressionNode classExpressionNode : subClassOfNode.getClassExpressionNodes()) {
         classRenderingResult = renderOWLClassExpression(classExpressionNode);
         if (classRenderingResult.isPresent()) {
            TextRendering classExpressionRendering = classRenderingResult.get();
            if (isFirst) {
               textRepresentation.append(INDENT).append("SubClassOf: ");
            } else {
               textRepresentation.append(",");
               textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
            }
            textRepresentation.append(classExpressionRendering.getRendering());
            if (classExpressionRendering.hasComment()) {
               textRepresentation.append(comment(classExpressionRendering.getComment()));
            }
            isFirst = false;
         }
      }
      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   private Optional<? extends TextRendering> renderOWLSameAs(OWLSameAsNode OWLSameAsNode) throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> individualRenderingResult = null;

      boolean isFirst = true;
      for (OWLNamedIndividualNode owlNamedIndividualNode : OWLSameAsNode.getIndividualNodes()) {
         individualRenderingResult = renderOWLNamedIndividual(owlNamedIndividualNode, false);
         if (individualRenderingResult.isPresent()) {
            TextRendering individualRendering = individualRenderingResult.get();
            if (isFirst) {
               textRepresentation.append(INDENT).append("SameAs: ");
            } else {
               textRepresentation.append(",");
               textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
            }
            textRepresentation.append(individualRendering.getRendering());
            if (individualRendering.hasComment()) {
               textRepresentation.append(comment(individualRendering.getComment()));
            }
            isFirst = false;
         }
      }
      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   private Optional<? extends TextRendering> renderOWLDifferentFrom(OWLDifferentFromNode differentFromNode)
         throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> individualRenderingResult = null;

      boolean isFirst = true;
      for (OWLNamedIndividualNode namedIndividualNode : differentFromNode.getIndividualNodes()) {
         individualRenderingResult = renderOWLNamedIndividual(namedIndividualNode, false);
         if (individualRenderingResult.isPresent()) {
            TextRendering individualRendering = individualRenderingResult.get();
            if (isFirst) {
               textRepresentation.append(INDENT).append("DifferentFrom: ");
            } else {
               textRepresentation.append(", ");
               textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
            }
            textRepresentation.append(individualRendering.getRendering());
            if (individualRendering.hasComment()) {
               textRepresentation.append(comment(individualRendering.getComment()));
            }
            isFirst = false;
         }
      }
      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   @Override
   public Optional<? extends TextRendering> renderOWLPropertyAssertion(
         OWLPropertyAssertionNode propertyAssertionNode) throws RendererException
   {
      if (propertyAssertionNode.hasReferenceNode()) {
         ReferenceNode referenceNode = propertyAssertionNode.getReferenceNode();
         ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
         Optional<? extends TextReferenceRendering> referenceRenderingResult = renderReference(referenceNode);
         if (referenceRenderingResult.isPresent()) {
            TextReferenceRendering referenceRendering = referenceRenderingResult.get();
            if (referenceType.isQuotedOWLLiteral()) {
               TextRendering literalRendering = new TextRendering(quotes(referenceRendering.getRawValue()));
               literalRendering.addComment(referenceRendering.getComment());
               return Optional.of(literalRendering);
            } else {
               return referenceRenderingResult;
            }
         } else {
            return Optional.empty();
         }
      } else if (propertyAssertionNode.hasNameNode()) {
         return renderName(propertyAssertionNode.getNameNode());
      } else if (propertyAssertionNode.hasLiteralNode()) {
         Optional<? extends TextLiteralRendering> literalRenderingResult = renderOWLLiteral(
               propertyAssertionNode.getOWLLiteralNode());
         if (literalRenderingResult.isPresent()) {
            TextLiteralRendering literalRendering = literalRenderingResult.get();
            if (literalRendering.isQuoted()) {
               return Optional.of(new TextRendering(quotes(literalRendering.getRawValue())));
            } else {
               return literalRenderingResult;
            }
         } else {
            return Optional.empty();
         }
      }
      throw new InternalRendererException("Unknown property assertion node: " + propertyAssertionNode.getNodeName());
   }

   private Optional<? extends TextRendering> renderAnnotationFact(AnnotationFactNode annotationFactNode)
         throws RendererException
   {
      Optional<? extends TextRendering> annotationPropertyRenderingResult = renderOWLAnnotationProperty(
            annotationFactNode.getOWLAnnotationPropertyNode());
      Optional<? extends TextRendering> annotationValueRenderingResult = renderOWLAnnotationValue(
            annotationFactNode.getOWLAnnotationValueNode());

      if (annotationPropertyRenderingResult.isPresent() && annotationValueRenderingResult.isPresent()) {
         TextRendering annotationPropertyRendering = annotationPropertyRenderingResult.get();
         TextRendering annotationValueRendering = annotationValueRenderingResult.get();

         String textRepresentation = annotationPropertyRendering.getRendering() + " "
               + annotationValueRendering.getRendering();
         TextRendering annotationFactRendering = new TextRendering(textRepresentation);
         annotationFactRendering.addComment(annotationPropertyRendering.getComment());
         annotationFactRendering.addComment(annotationValueRendering.getComment());
         return Optional.of(annotationFactRendering);
      }
      return Optional.empty();
   }

   @Override
   public Optional<? extends TextRendering> renderOWLObjectOneOf(OWLObjectOneOfNode objectOneOfNode)
         throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> individualRenderingResult = null;

      boolean isFirst = true;
      for (OWLNamedIndividualNode owlNamedIndividualNode : objectOneOfNode.getOWLNamedIndividualNodes()) {
         individualRenderingResult = renderOWLNamedIndividual(owlNamedIndividualNode, false);
         if (individualRenderingResult.isPresent()) {
            TextRendering individualRendering = individualRenderingResult.get();
            if (isFirst) {
               textRepresentation.append("{");
            } else {
               textRepresentation.append(",");
               textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
            }
            textRepresentation.append(individualRendering.getRendering());
            if (individualRendering.hasComment()) {
               textRepresentation.append(comment(individualRendering.getComment()));
            }
            isFirst = false;
         }
      }
      textRepresentation.append("}");

      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   private Optional<? extends TextRendering> renderFact(FactNode factNode) throws RendererException
   {
      Optional<? extends TextRendering> propertyRenderingResult = renderOWLProperty(factNode.getOWLPropertyNode());
      Optional<? extends TextRendering> propertyValueRenderingResult = renderOWLPropertyAssertion(
            factNode.getOWLPropertyAssertionObjectNode());

      if (propertyRenderingResult.isPresent() && propertyValueRenderingResult.isPresent()) {
         TextRendering propertyRendering = propertyRenderingResult.get();
         TextRendering propertyValueRendering = propertyValueRenderingResult.get();

         String textRepresentation = propertyRendering.getRendering() + " " + propertyValueRendering.getRendering();
         TextRendering factRendering = new TextRendering(textRepresentation);
         factRendering.addComment(propertyRendering.getComment());
         factRendering.addComment(propertyValueRendering.getComment());

         return Optional.of(factRendering);
      }
      return Optional.empty();
   }

   @Override
   public Optional<? extends TextRendering> renderOWLAnnotationValue(OWLAnnotationValueNode annotationValueNode)
         throws RendererException
   {
      if (annotationValueNode.hasReferenceNode()) {
         ReferenceNode referenceNode = annotationValueNode.getReferenceNode();
         ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
         Optional<? extends TextReferenceRendering> referenceRendering = renderReference(referenceNode);
         if (referenceRendering.isPresent()) {
            if (referenceType.isQuotedOWLLiteral()) {
               return Optional.of(new TextRendering(quotes(referenceRendering.get().getRawValue())));
            } else {
               return referenceRendering;
            }
         } else {
            return Optional.empty();
         }
      } else if (annotationValueNode.hasNameNode()) {
         return renderName(annotationValueNode.getNameNode());
      } else if (annotationValueNode.hasLiteralNode()) {
         Optional<? extends TextLiteralRendering> literalRendering = renderOWLLiteral(
               annotationValueNode.getOWLLiteralNode());
         if (literalRendering.isPresent()) {
            if (literalRendering.get().isQuoted()) {
               return Optional.of(new TextRendering(quotes(literalRendering.get().getRawValue())));
            } else {
               return literalRendering;
            }
         } else {
            return Optional.empty();
         }
      }
      throw new InternalRendererException("Unknown annotation value node: " + annotationValueNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLObjectExactCardinality(OWLPropertyNode propertyNode,
         OWLExactCardinalityNode exactCardinalityNode) throws RendererException
   {
      return renderOWLExactCardinality(propertyNode, exactCardinalityNode);
   }

   @Override
   public Optional<? extends TextRendering> renderOWLDataExactCardinality(OWLPropertyNode propertyNode,
         OWLExactCardinalityNode exactCardinalityNode) throws RendererException
   {
      return renderOWLExactCardinality(propertyNode, exactCardinalityNode);
   }

   @Override
   public Optional<? extends TextRendering> renderOWLObjectMaxCardinality(OWLPropertyNode propertyNode,
         OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
   {
      return renderOWLMaxCardinality(propertyNode, maxCardinalityNode);
   }

   @Override
   public Optional<? extends TextRendering> renderOWLDataMaxCardinality(OWLPropertyNode propertyNode,
         OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
   {
      return renderOWLMaxCardinality(propertyNode, maxCardinalityNode);
   }

   @Override
   public Optional<? extends TextRendering> renderOWLObjectMinCardinality(OWLPropertyNode propertyNode,
         OWLMinCardinalityNode minCardinalityNode) throws RendererException
   {
      return renderOWLMinCardinality(propertyNode, minCardinalityNode);
   }

   @Override
   public Optional<? extends TextRendering> renderOWLDataMinCardinality(OWLPropertyNode propertyNode,
         OWLMinCardinalityNode minCardinalityNode) throws RendererException
   {
      return renderOWLMinCardinality(propertyNode, minCardinalityNode);
   }

   @Override
   public Optional<? extends TextRendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
         OWLObjectAllValuesFromNode objectAllValuesFromNode) throws RendererException
   {
      Optional<? extends TextRendering> classRendering = null;
      if (objectAllValuesFromNode.hasOWLClass()) {
         classRendering = renderOWLClass(objectAllValuesFromNode.getOWLClassNode(), false);
      } else if (objectAllValuesFromNode.hasOWLClassExpression()) {
         classRendering = renderOWLClassExpression(objectAllValuesFromNode.getOWLClassExpressionNode());
      } else if (objectAllValuesFromNode.hasOWLObjectOneOfNode()) {
         classRendering = renderOWLObjectOneOf(objectAllValuesFromNode.getOWLObjectOneOfNode());
      } else {
         throw new InternalRendererException("unknown child for node " + objectAllValuesFromNode.getNodeName());
      }

      if (classRendering.isPresent()) {
         return Optional.of(new TextRendering("ONLY " + classRendering.get().getRendering()));
      }
      return Optional.empty();
   }

   @Override
   public Optional<? extends TextRendering> renderOWLClass(OWLClassNode classNode, boolean isDeclaration) throws RendererException
   {
      if (classNode.hasReferenceNode()) {
         return renderReference(classNode.getReferenceNode());
      } else if (classNode.hasNameNode()) {
         return renderName(classNode.getNameNode());
      }
      throw new InternalRendererException("Unknown class node: " + classNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode)
         throws RendererException
   {
      if (propertyNode.hasReferenceNode()) {
         return renderReference(propertyNode.getReferenceNode());
      } else if (propertyNode.hasNameNode()) {
         return renderName(propertyNode.getNameNode());
      }
      throw new InternalRendererException("Unknown object property node: " + propertyNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLDataProperty(OWLPropertyNode propertyNode) throws RendererException
   {
      if (propertyNode.hasReferenceNode()) {
         return renderReference(propertyNode.getReferenceNode());
      } else if (propertyNode.hasNameNode()) {
         return renderName(propertyNode.getNameNode());
      }
      throw new InternalRendererException("Unknown data property node: " + propertyNode.getNodeName());
   }

   @Override
   public Optional<? extends TextRendering> renderOWLNamedIndividual(OWLNamedIndividualNode individualNode,
         boolean isDeclaration) throws RendererException
   {
      if (individualNode.hasReferenceNode()) {
         return renderReference(individualNode.getReferenceNode());
      } else if (individualNode.hasNameNode()) {
         return renderName(individualNode.getNameNode());
      }
      throw new InternalRendererException("Unknown individual node: " + individualNode.getNodeName());
   }

   @Override
   public Optional<? extends TextLiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
   {
      if (literalNode.isInt()) {
         return Optional.of(new TextLiteralRendering(literalNode.getIntLiteralNode().getValue()));
      } else if (literalNode.isFloat()) {
         return Optional.of(new TextLiteralRendering(literalNode.getFloatLiteralNode().getValue()));
      } else if (literalNode.isString()) {
         return Optional.of(new TextLiteralRendering(literalNode.getStringLiteralNode().getValue()));
      } else if (literalNode.isBoolean()) {
         return Optional.of(new TextLiteralRendering(literalNode.getBooleanLiteralNode().getValue()));
      }
      throw new InternalRendererException("Unknown literal node: " + literalNode.getNodeName());
   }

   private Optional<? extends TextRendering> renderOWLAllValuesFrom(OWLPropertyNode propertyNode,
         OWLAllValuesFromNode allValuesFromNode) throws RendererException
   {
      if (allValuesFromNode.hasOWLDataAllValuesFromNode()) {
         return renderOWLDataAllValuesFrom(propertyNode, allValuesFromNode.getOWLDataAllValuesFromNode());
      } else if (allValuesFromNode.hasOWLObjectAllValuesFromNode()) {
         return renderOWLObjectAllValuesFrom(propertyNode, allValuesFromNode.getObjectOWLAllValuesFromNode());
      }
      throw new InternalRendererException("Unknown all values node: " + allValuesFromNode.getNodeName());
   }

   private Optional<? extends TextRendering> renderOWLSomeValuesFrom(OWLPropertyNode propertyNode,
         OWLSomeValuesFromNode someValuesFromNode) throws RendererException
   {
      Optional<? extends TextRendering> valueRendering = null;
      if (someValuesFromNode.hasOWLDataSomeValuesFromNode()) {
         valueRendering = renderOWLDataSomeValuesFrom(propertyNode, someValuesFromNode.getOWLDataSomeValuesFromNode());
      } else if (someValuesFromNode.hasOWLObjectSomeValuesFrom()) {
         valueRendering = renderOWLObjectSomeValuesFrom(propertyNode,
               someValuesFromNode.getOWLObjectSomeValuesFromNode());
      } else {
         throw new InternalRendererException("Unknown some values node: " + someValuesFromNode.getNodeName());
      }

      if (valueRendering.isPresent()) {
         return Optional.of(new TextRendering("SOME " + valueRendering.get().getRendering()));
      }
      return Optional.empty();
   }

   private Optional<? extends TextRendering> renderOWLHasValue(OWLPropertyNode propertyNode,
         OWLHasValueNode hasValueNode) throws RendererException
   {
      Optional<? extends TextRendering> valueRendering = null;
      if (hasValueNode.hasReferenceNode()) {
         ReferenceNode referenceNode = hasValueNode.getReferenceNode();
         ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
         Optional<? extends TextReferenceRendering> referenceRendering = renderReference(referenceNode);
         if (referenceRendering.isPresent()) {
            if (referenceType.isQuotedOWLLiteral()) {
               return Optional.of(new TextRendering(quotes(referenceRendering.get().getRawValue())));
            } else {
               return Optional.of(new TextRendering(referenceRendering.get().getRawValue()));
            }
         } else {
            return Optional.empty();
         }
      } else if (hasValueNode.hasNameNone()) {
         valueRendering = renderName(hasValueNode.getNameNode());
      } else if (hasValueNode.hasLiteralNode()) {
         Optional<? extends TextLiteralRendering> literalRendering = renderOWLLiteral(hasValueNode.getOWLLiteralNode());
         if (literalRendering.isPresent()) {
            if (literalRendering.get().isQuoted()) {
               valueRendering = Optional.of(new TextLiteralRendering(quotes(literalRendering.get().getRendering())));
            } else {
               valueRendering = literalRendering;
            }
         } else {
            return Optional.empty();
         }
      } else {
         throw new InternalRendererException("Unknown child for node " + hasValueNode.getNodeName());
      }

      if (valueRendering.isPresent()) {
         return Optional.of(new TextRendering("VALUE " + valueRendering.get().getRendering()));
      }
      return Optional.empty();
   }

   private Optional<? extends TextRendering> renderOWLExactCardinality(OWLPropertyNode propertyNode,
         OWLExactCardinalityNode owlExactCardinalityNode) throws RendererException
   {
      String textRepresentation = "EXACTLY " + owlExactCardinalityNode.getCardinality();
      return Optional.of(new TextRendering(textRepresentation));
   }

   private Optional<? extends TextRendering> renderOWLMaxCardinality(OWLPropertyNode propertyNode,
         OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
   {
      String textRepresentation = "MAX " + maxCardinalityNode.getCardinality();
      return Optional.of(new TextRendering(textRepresentation));
   }

   private Optional<? extends TextRendering> renderOWLMinCardinality(OWLPropertyNode propertyNode,
         OWLMinCardinalityNode minCardinalityNode) throws RendererException
   {
      String textRepresentation = "MIN " + minCardinalityNode.getCardinality();
      return Optional.of(new TextRendering(textRepresentation));
   }

   private Optional<? extends TextRendering> renderName(NameNode nameNode) throws RendererException
   {
      String name = nameNode.isQuoted() ? "'" + nameNode.getName() + "'" : nameNode.getName();
      return Optional.of(new TextRendering(name));
   }

   private Optional<? extends TextRendering> renderType(TypeNode typeNode) throws RendererException
   {
      if (typeNode instanceof ReferenceNode) {
         return renderReference((ReferenceNode) typeNode);
      } else if (typeNode instanceof OWLClassExpressionNode) {
         return renderOWLClassExpression((OWLClassExpressionNode) typeNode);
      } else if (typeNode instanceof OWLClassNode) {
         return renderOWLClass((OWLClassNode) typeNode, false);
      }
      throw new InternalRendererException("Unknown type node: " + typeNode.getNodeName());
   }

   private Optional<? extends TextRendering> renderTypes(TypesNode types) throws RendererException
   {
      StringBuilder textRepresentation = new StringBuilder();
      Optional<? extends TextRendering> typeRenderingResult = null;

      boolean isFirst = true;
      for (TypeNode typeNode : types.getTypeNodes()) {
         typeRenderingResult = renderType(typeNode);
         if (typeRenderingResult.isPresent()) {
            TextRendering typeRendering = typeRenderingResult.get();
            if (!isFirst) {
               textRepresentation.append(",");
               textRepresentation.append(NEWLINE).append(INDENT).append(INDENT).append(INDENT);
            }
            textRepresentation.append(typeRendering.getRendering());
            if (typeRendering.hasComment()) {
               textRepresentation.append(comment(typeRendering.getComment()));
            }
            isFirst = false;
         }
      }
      return Optional.of(new TextRendering(textRepresentation.toString()));
   }

   private String createComment(String rawValue, ReferenceNode referenceNode) throws RendererException
   {
      StringBuffer sb = new StringBuffer();
      sb.append(rawValue);
      sb.append(" ");
      sb.append("was rendered from reference ");
      sb.append(referenceNode);
      sb.append(" ");
      sb.append("at location ");
      sb.append(ReferenceUtil.resolveLocation(getDataSource(), referenceNode));
      sb.append(" ");
      sb.append("with cell value ");
      sb.append("\"").append(rawValue).append("\"");
      sb.append(" ");
      sb.append("(");
      sb.append(getEncoding(referenceNode));
      sb.append(")");
      return sb.toString();
   }

   private Object getEncoding(ReferenceNode referenceNode)
   {
      if (referenceNode.hasRDFSLabelValueEncoding()) {
         return "rdfs:label encoding";
      } else if (referenceNode.hasRDFIDValueEncoding()) {
         return "IRI encoding";
      } else if (referenceNode.hasLiteralValueEncoding()) {
         return "Literal encoding";
      } else if (referenceNode.hasLocationValueEncoding()) {
         return "Location encoding";
      } else {
         return "Unknown encoding";
      }
   }

   private static String quotes(String text)
   {
      return "\"" + text + "\"";
   }

   private static String comment(String text)
   {
      return COMMENT_HEADER + text;
   }
}
