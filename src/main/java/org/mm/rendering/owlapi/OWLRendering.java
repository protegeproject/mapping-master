package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class OWLRendering implements OWLObjectRendering {

   private final OWLObject object;

   public OWLRendering(@Nonnull OWLObject object) {
      this.object = checkNotNull(object);
   }

   public abstract OWLObject getOWLObject();

   @Override
   public String getRendering() {
      return new ManchesterOWLSyntaxOWLObjectRendererImpl().render(object);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(object);
   }

   @Override
   public String toString() {
      return getRendering();
   }
}
