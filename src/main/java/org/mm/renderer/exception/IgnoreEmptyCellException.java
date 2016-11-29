package org.mm.renderer.exception;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

import javax.annotation.Nonnull;

import org.mm.renderer.internal.CellAddress;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class IgnoreEmptyCellException extends RendererException implements Locatable<CellAddress> {

   private static final long serialVersionUID = 1L;

   private CellAddress cellAddress;

   public IgnoreEmptyCellException() {
      super("Cell is empty");
   }

   @Override
   public void setErrorLocation(@Nonnull CellAddress location) {
      this.cellAddress = checkNotNull(cellAddress);
   }

   @Override
   public String getMessage() {
      String errorMessage = super.getMessage();
      if (cellAddress != null) {
         errorMessage = format("%s at %s", errorMessage, cellAddress);
      }
      return errorMessage;
   }
}
