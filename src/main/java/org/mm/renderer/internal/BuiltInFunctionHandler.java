package org.mm.renderer.internal;

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
         default: return inputValue;
      }
   }

   private Value handleToUpperCase(Value inputValue) {
      String newString = inputValue.getString().toUpperCase();
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }
}
