package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.Nonnull;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.renderer.RenderingContext;
import org.mm.renderer.Workbook;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class BuiltInFunctionHandler implements MappingMasterParserConstants {

   private final Workbook workbook;
   private final RenderingContext context;

   public BuiltInFunctionHandler(@Nonnull Workbook workbook, @Nonnull RenderingContext context) {
      this.workbook = checkNotNull(workbook);
      this.context = checkNotNull(context);
   }

   public String evaluate(BuiltInFunction function, String input) {
      int functionType = function.getFunctionType();
      if (functionType == MM_TO_UPPER_CASE) {
         handleToUpperCase(input, function.getArguments());
      }
      return "";
   }

   private void handleToUpperCase(String input, List<Argument> arguments) {
//      if (baseInput.isPresent()) {
//         output = baseInput.get().toUpperCase();
//      } else {
//         List<String> args = resolveArguments(arguments);
//         StringBuffer sb = new StringBuffer();
//         for (String arg : args) {
//            sb.append(arg.toUpperCase());
//         }
//         output = sb.toString();
//      }
   }

   private List<String> resolveArguments(List<Argument> arguments) {
//      for (Argument argument : arguments) {
//         if (argument instanceof ReferenceNotation) {
//            ReferenceNotation referenceNotation = (ReferenceNotation) argument;
//            context.getCellValue(something.toCellAddress(referenceNotation));
//         }
//      }
      return null;
   }
}
