package org.mm.renderer.internal;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
         case MM_TRIM: return handleTrim(inputValue);
         case MM_PRINTF: return handlePrintf(inputValue, function.getArguments());
         case MM_REPLACE: return handleReplace(inputValue, function.getArguments());
         case MM_REPLACE_FIRST: return handleReplaceFirst(inputValue, function.getArguments());
         case MM_REPLACE_ALL: return handleReplaceAll(inputValue, function.getArguments());
         case MM_DECIMAL_FORMAT: return handleDecimalFormat(inputValue, function.getArguments());
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

   private Value handleTrim(Value inputValue) {
      String inputString = inputValue.getString();
      String newString = inputString.trim();
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handlePrintf(Value inputValue, List<Argument> arguments) {
      String stringFormat = ((LiteralValue) arguments.get(0)).getString();
      String newString = String.format(stringFormat, inputValue.getString());
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handleReplace(Value inputValue, List<Argument> arguments) {
      String oldChar = ((LiteralValue) arguments.get(0)).getString();
      String newChar = ((LiteralValue) arguments.get(1)).getString();
      String newString = inputValue.getString().replace(oldChar, newChar);
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handleReplaceFirst(Value inputValue, List<Argument> arguments) {
      String regex = ((LiteralValue) arguments.get(0)).getString();
      String replacement = ((LiteralValue) arguments.get(1)).getString();
      String newString = inputValue.getString().replaceFirst(regex, replacement);
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handleReplaceAll(Value inputValue, List<Argument> arguments) {
      String regex = ((LiteralValue) arguments.get(0)).getString();
      String replacement = ((LiteralValue) arguments.get(1)).getString();
      String newString = inputValue.getString().replaceAll(regex, replacement);
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handleDecimalFormat(Value inputValue, List<Argument> arguments) {
      String decimalFormat = ((LiteralValue) arguments.get(0)).getString();
      DecimalFormat formatter = new DecimalFormat(decimalFormat);
      BigDecimal number = new BigDecimal(inputValue.getString());
      String newString = formatter.format(number);
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }
}
