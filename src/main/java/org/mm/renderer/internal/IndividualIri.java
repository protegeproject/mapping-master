package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class IndividualIri extends IriValue {

   public IndividualIri(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static IndividualIri create(@Nonnull String value) {
      return new IndividualIri(value, false);
   }

   @Override
   public IndividualIri update(String newValue) {
      return new IndividualIri(newValue, isFromWorkbook());
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof IndividualIri)) {
         return false;
      }
      IndividualIri other = (IndividualIri) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}
