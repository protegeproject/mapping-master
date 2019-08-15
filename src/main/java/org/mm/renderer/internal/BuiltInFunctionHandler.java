package org.mm.renderer.internal;

import java.util.List;
import org.mm.parser.MappingMasterParserConstants;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class BuiltInFunctionHandler implements MappingMasterParserConstants {

   public Value evaluate(BuiltInFunction function, Value inputValue) {
      int functionType = function.getFunctionType();
      switch (functionType) {
         case MM_TO_UPPER_CASE: return handleToUpperCase(inputValue);
         case MM_TO_LOWER_CASE: return handleToLowerCase(inputValue);
         case MM_REVERSE: return handleReverse(inputValue);
         case MM_PRINTF: return handlePrintf(inputValue, function.getArguments());
         default: return inputValue;
      }
   }

   private Value handleToUpperCase(Value inputValue) {
      String newString = inputValue.getString().toUpperCase();
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handleToLowerCase(Value inputValue) {
      String newString = inputValue.getString().toLowerCase();
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handleReverse(Value inputValue) {
      String inputString = inputValue.getString();
      String newString = new StringBuilder(inputString).reverse().toString();
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handlePrintf(Value inputValue, List<Argument> arguments) {
      String stringFormat = ((LiteralValue) arguments.get(0)).getString();
      String newString = String.format(stringFormat, inputValue.getString());
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }
}
