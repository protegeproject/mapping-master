package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OWLClassRendering extends OWLRendering implements OWLClassExpressionRendering {

   private final OWLClass cls;

   public OWLClassRendering(@Nonnull OWLClass cls) {
      super(cls);
      this.cls = checkNotNull(cls);
   }

   @Override
   @Nonnull
   public OWLClass getOWLObject() {
      return cls;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (obj == this) {
         return true;
     }
     if (!(obj instanceof OWLClassRendering)) {
         return false;
     }
     OWLClassRendering other = (OWLClassRendering) obj;
     return cls.equals(other.cls);
   }
}
