package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotationProperty;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTObjectProperty;
import org.mm.parser.node.ASTProperty;
import org.mm.parser.node.Node;
import org.mm.parser.node.SimpleNode;
import org.mm.renderer.CellCursor;
import org.mm.renderer.internal.AnnotationPropertyIri;
import org.mm.renderer.internal.AnnotationPropertyName;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.ClassIri;
import org.mm.renderer.internal.ClassName;
import org.mm.renderer.internal.DataPropertyIri;
import org.mm.renderer.internal.DataPropertyName;
import org.mm.renderer.internal.IndividualIri;
import org.mm.renderer.internal.IndividualName;
import org.mm.renderer.internal.ObjectPropertyIri;
import org.mm.renderer.internal.ObjectPropertyName;
import org.mm.renderer.internal.PropertyIri;
import org.mm.renderer.internal.PropertyName;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.UntypedIri;
import org.mm.renderer.internal.UntypedPrefixedName;
import org.mm.renderer.internal.Value;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class EntityNodeVisitor extends ValueNodeVisitor {

   protected final OwlFactory owlFactory;

   private OWLEntity entity;

   public EntityNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      super(referenceResolver, builtInFunctionHandler, cellCursor);
      this.owlFactory = checkNotNull(owlFactory);
   }

   @Nullable
   public OWLEntity getEntity() {
      return entity;
   }

   private Value getValue(SimpleNode node) {
      Node childNode = ParserUtils.getChild(node);
      childNode.accept(this);
      return getValue();
   }

   @Override
   public void visit(ASTClass classNode) {
      Value value = getValue(classNode);
      if (value instanceof ClassName) {
         entity = owlFactory.getOWLClass((ClassName) value);
      } else if (value instanceof ClassIri) {
         entity = owlFactory.getOWLClass((ClassIri) value);
      } else if (value instanceof UntypedPrefixedName) {
         entity = owlFactory.getOWLClass((UntypedPrefixedName) value);
      } else if (value instanceof UntypedIri) {
         entity = owlFactory.getOWLClass((UntypedIri) value);
      }
   }

   @Override
   public void visit(ASTProperty propertyNode) {
      Value value = getValue(propertyNode);
      if (value instanceof PropertyName) {
         entity = owlFactory.getOWLProperty((PropertyName) value);
      } else if (value instanceof PropertyIri) {
         entity = owlFactory.getOWLProperty((PropertyIri) value);
      } else if (value instanceof UntypedPrefixedName) {
         entity = owlFactory.getOWLProperty((UntypedPrefixedName) value);
      } else if (value instanceof UntypedIri) {
         entity = owlFactory.getOWLProperty((UntypedIri) value);
      }
   }

   @Override
   public void visit(ASTDataProperty propertyNode) {
      Value value = getValue(propertyNode);
      if (value instanceof DataPropertyName) {
         entity = owlFactory.getOWLDataProperty((DataPropertyName) value);
      } else if (value instanceof DataPropertyIri) {
         entity = owlFactory.getOWLDataProperty((DataPropertyIri) value);
      } else if (value instanceof UntypedPrefixedName) {
         entity = owlFactory.getOWLDataProperty((UntypedPrefixedName) value);
      } else if (value instanceof UntypedIri) {
         entity = owlFactory.getOWLDataProperty((UntypedIri) value);
      }
   }

   @Override
   public void visit(ASTObjectProperty propertyNode) {
      Value value = getValue(propertyNode);
      if (value instanceof ObjectPropertyName) {
         entity = owlFactory.getOWLObjectProperty((ObjectPropertyName) value);
      } else if (value instanceof ObjectPropertyIri) {
         entity = owlFactory.getOWLObjectProperty((ObjectPropertyIri) value);
      } else if (value instanceof UntypedPrefixedName) {
         entity = owlFactory.getOWLObjectProperty((UntypedPrefixedName) value);
      } else if (value instanceof UntypedIri) {
         entity = owlFactory.getOWLObjectProperty((UntypedIri) value);
      }
   }

   @Override
   public void visit(ASTAnnotationProperty propertyNode) {
      Value value = getValue(propertyNode);
      if (value instanceof AnnotationPropertyName) {
         entity = owlFactory.getOWLAnnotationProperty((AnnotationPropertyName) value);
      } else if (value instanceof AnnotationPropertyIri) {
         entity = owlFactory.getOWLAnnotationProperty((AnnotationPropertyIri) value);
      } else if (value instanceof UntypedPrefixedName) {
         entity = owlFactory.getOWLAnnotationProperty((UntypedPrefixedName) value);
      } else if (value instanceof UntypedIri) {
         entity = owlFactory.getOWLAnnotationProperty((UntypedIri) value);
      }
   }

   @Override
   public void visit(ASTNamedIndividual individualNode) {
      Value value = getValue(individualNode);
      if (value instanceof IndividualName) {
         entity = owlFactory.getOWLNamedIndividual((IndividualName) value);
      } else if (value instanceof IndividualIri) {
         entity = owlFactory.getOWLNamedIndividual((IndividualIri) value);
      } else if (value instanceof UntypedPrefixedName) {
         entity = owlFactory.getOWLNamedIndividual((UntypedPrefixedName) value);
      } else if (value instanceof UntypedIri) {
         entity = owlFactory.getOWLNamedIndividual((UntypedIri) value);
      }
   }
}
