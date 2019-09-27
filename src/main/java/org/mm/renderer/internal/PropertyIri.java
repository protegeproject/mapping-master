package org.mm.renderer.internal;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class PropertyIri extends IriValue {

   public PropertyIri(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public abstract boolean isDataProperty();
   public abstract boolean isObjectProperty();
   public abstract boolean isAnnotationProperty();
}
