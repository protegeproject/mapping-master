package org.mm.renderer.owlapi;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.BaseReferenceRenderer;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.OWLLiteralRendering;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
import org.mm.rendering.owlapi.OWLClassExpressionRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

public class OWLAPIReferenceRenderer extends BaseReferenceRenderer
  implements ReferenceRenderer, MappingMasterParserConstants
{
  private final OWLDataFactory owlDataFactory;
  private final OWLAPIObjectHandler owlObjectHandler;
  private final OWLAPIEntityRenderer entityRenderer;
  private final OWLAPILiteralRenderer literalRenderer;
  private final OWLAPIClassExpressionRenderer classExpressionRenderer;

  public OWLAPIReferenceRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource,
    OWLAPIEntityRenderer entityRenderer, OWLAPILiteralRenderer literalRenderer)
  {
    super(dataSource);
    this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
    this.owlObjectHandler = new OWLAPIObjectHandler(ontology);
    this.entityRenderer = entityRenderer;
    this.literalRenderer = literalRenderer;
    this.classExpressionRenderer = new OWLAPIClassExpressionRenderer(ontology, entityRenderer, this, literalRenderer);
  }

  @Override public Optional<OWLAPIReferenceRendering> renderReference(ReferenceNode referenceNode)
    throws RendererException
  {
    SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

    if (sourceSpecificationNode.hasLiteral()) {
      String literalValue = sourceSpecificationNode.getLiteral();
      OWLLiteral literal = this.literalRenderer.createOWLLiteral(literalValue);
      return Optional.of(new OWLAPIReferenceRendering(literal, referenceType));
    } else {
      SpreadsheetLocation location = resolveLocation(sourceSpecificationNode);
      String defaultNamespace = getReferenceNamespace(referenceNode);
      String language = getReferenceLanguage(referenceNode);
      String resolvedReferenceValue = resolveReferenceValue(location, referenceNode);

      if (referenceType.isUntyped())
        throw new RendererException("untyped reference " + referenceNode);

      if (resolvedReferenceValue.isEmpty()
        && referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION)
        return Optional.empty();

      if (referenceType.isOWLLiteral()) { // Reference is an OWL literal
        String literalReferenceValue = processOWLLiteralReferenceValue(location, resolvedReferenceValue, referenceNode);

        if (literalReferenceValue.isEmpty()
          && referenceNode.getActualEmptyLiteralDirective() == MM_SKIP_IF_EMPTY_LITERAL)
          return Optional.empty();

        OWLLiteral literal = this.literalRenderer.createOWLLiteral(literalReferenceValue, referenceType);

        return Optional.of(new OWLAPIReferenceRendering(literal, referenceType));
      } else if (referenceType.isOWLEntity()) { // Reference is an OWL entity
        String rdfID = getReferenceRDFID(resolvedReferenceValue, referenceNode);
        String rdfsLabel = getReferenceRDFSLabel(resolvedReferenceValue, referenceNode);

        OWLEntity owlEntity = this.owlObjectHandler
          .createOrResolveOWLEntity(location, resolvedReferenceValue, referenceType, rdfID, rdfsLabel, defaultNamespace,
            language, referenceNode.getReferenceDirectives());
        if (owlEntity == null) {
           return Optional.empty();
        }
        Set<OWLAxiom> axioms = addDefiningTypesFromReference(owlEntity, referenceNode);
        return Optional.of(new OWLAPIReferenceRendering(owlEntity, axioms, referenceType));
        
      } else
        throw new InternalRendererException(
          "unknown reference type " + referenceType + " for reference " + referenceNode);
    }
  }

  @Override public Optional<? extends OWLLiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode)
    throws RendererException
  {
    return this.literalRenderer.renderOWLLiteral(literalNode);
  }

  public Set<OWLAxiom> processTypesClause(OWLNamedIndividual declaredIndividual, List<TypeNode> typeNodes)
    throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    for (TypeNode typeNode : typeNodes) {
      Optional<OWLObject> typeRendering = renderType(typeNode);

      if (!typeRendering.isPresent()) {
        //logLine(
        //  "processReference: skipping OWL type declaration clause [" + typeNode + "] for individual "
        //    + individualDeclarationRendering + " because of missing type");
        continue;
      }

      OWLObject obj = typeRendering.get();
      if (obj instanceof OWLEntity) {
        OWLEntity entity = (OWLEntity) obj;
        if (entity.isOWLClass()) {
          OWLClass cls = entity.asOWLClass();
          OWLClassAssertionAxiom axiom = this.owlDataFactory.getOWLClassAssertionAxiom(cls, declaredIndividual);
          axioms.add(axiom);
        } else {
          throw new RendererException(
              "expecting OWL class as type for individual " + declaredIndividual.getIRI() + ", got " + entity.getIRI());
        }
      } else if (obj instanceof OWLClassExpression) {
        OWLClassExpression clsExp = (OWLClassExpression) obj;
        OWLClassAssertionAxiom axiom = this.owlDataFactory.getOWLClassAssertionAxiom(clsExp, declaredIndividual);
        axioms.add(axiom);
      } else {
        throw new RendererException("unknown type node " + typeNode.getNodeName() + " for individual " + declaredIndividual);
      }
    }
    return axioms;
  }

  private String getReferenceNamespace(ReferenceNode referenceNode) throws RendererException
  {
    // A reference will not have both a prefix and a namespace specified
    if (referenceNode.hasExplicitlySpecifiedPrefix()) {
      String prefix = referenceNode.getPrefixDirectiveNode().getPrefix();
      String namespace = this.owlObjectHandler.getNamespaceForPrefix(prefix);
      if (namespace == null)
        throw new RendererException("unknown prefix " + prefix + " specified in reference " + referenceNode);
      return namespace;
    } else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      return referenceNode.getNamespaceDirectiveNode().getNamespace();
    }
    // TODO Recheck this later because default namespace is always empty and the API doesn't allow to updating it.
//    else {
//      if (!hasDefaultNamespace())
//        throw new RendererException(
//          "ontology has no default namespace and no namespace specified by reference " + referenceNode);

      return getDefaultNamespace();
//    }
  }

  private Set<OWLAxiom> addDefiningTypesFromReference(OWLEntity entity, ReferenceNode referenceNode)
    throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
    
    if (referenceNode.hasExplicitlySpecifiedTypes()) {
      for (TypeNode typeNode : referenceNode.getTypesNode().getTypeNodes()) {
        Optional<OWLObject> definingType = renderType(typeNode);
        if (definingType.isPresent()) {
          if (definingType.get() instanceof OWLEntity) {
            OWLEntity definingTypeEntity = (OWLEntity) definingType.get();
            if (referenceType.isOWLClass()) {
              // TODO Make as a separate checking method
              if (!entity.isOWLClass()) {
                throw new RendererException(
                    "expecting class for type in reference " + referenceNode + " for " + entity + ", got " + entity
                        .getClass().getCanonicalName());
              }
              OWLClass cls = definingTypeEntity.asOWLClass();
              OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(cls, entity.asOWLClass());
              axioms.add(axiom);
            } else if (referenceType.isOWLNamedIndividual()) {
              if (!entity.isOWLNamedIndividual()) {
                throw new RendererException(
                    "expecting individual for type in reference " + referenceNode + " for " + entity + ", got " + entity
                      .getClass().getCanonicalName());
              }
              OWLClass cls = definingTypeEntity.asOWLClass();
              OWLClassAssertionAxiom axiom = this.owlDataFactory.getOWLClassAssertionAxiom(cls, entity.asOWLNamedIndividual());
              axioms.add(axiom);
            } else if (referenceType.isOWLObjectProperty()) {
              if (!entity.isOWLObjectProperty()) {
                throw new RendererException(
                    "expecting object property for type in reference " + referenceNode + " for " + entity);
              }
              OWLObjectProperty property = definingTypeEntity.asOWLObjectProperty();
              OWLSubObjectPropertyOfAxiom axiom = this.owlDataFactory.getOWLSubObjectPropertyOfAxiom(property, entity.asOWLObjectProperty());
              axioms.add(axiom);
            } else if (referenceType.isOWLDataProperty()) {
              if (!entity.isOWLDataProperty()) {
                throw new RendererException(
                    "expecting data property for type in reference " + referenceNode + " for " + entity);
              }
              OWLDataProperty property = definingTypeEntity.asOWLDataProperty();
              OWLSubDataPropertyOfAxiom axiom = this.owlDataFactory.getOWLSubDataPropertyOfAxiom(property, entity.asOWLDataProperty());
              axioms.add(axiom);
            } else
              throw new InternalRendererException("unknown entity type " + referenceType);
          } else {
            throw new RendererException("unsupported type node " + definingType + " for reference " + referenceNode);
          }
        }
      }
    }
    return axioms;
  }

  private Optional<OWLObject> renderType(TypeNode typeNode) throws RendererException
  {
    if (typeNode.isOWLClassNode()) {
      Optional<OWLClassRendering> classRendering = this.entityRenderer.renderOWLClass((OWLClassNode)typeNode);
      if (classRendering.isPresent()) {
        return Optional.of(classRendering.get().getOWLClass());
      }
    } else if (typeNode.isOWLClassExpressionNode()) {
        Optional<OWLClassExpressionRendering> classExpresssionRendering =
            this.classExpressionRenderer.renderOWLClassExpression((OWLClassExpressionNode)typeNode);
        if (classExpresssionRendering.isPresent()) {
          return Optional.of(classExpresssionRendering.get().getOWLClassExpression());
        }
    } else if (typeNode.isOWLPropertyNode()) {
      Optional<? extends OWLPropertyRendering> propertyRendering =
          this.entityRenderer.renderOWLProperty((OWLPropertyNode)typeNode);
      if (propertyRendering.isPresent()) {
        return Optional.of(propertyRendering.get().getOWLProperty());
      }
    } else if (typeNode.isReferenceNode()) {
      Optional<OWLAPIReferenceRendering> referenceRendering = renderReference((ReferenceNode)typeNode);
      if (referenceRendering.isPresent()) {
        if (referenceRendering.get().isOWLEntity()) {
          OWLEntity entity = referenceRendering.get().getOWLEntity().get();
          return Optional.of(entity);
        } else {
          throw new RendererException("expecting OWL entity for node " + typeNode.getNodeName());
        }
      }
    } else {
      throw new InternalRendererException("unknown type " + typeNode + " for node " + typeNode.getNodeName());
    }
    return Optional.empty();
  }
}
