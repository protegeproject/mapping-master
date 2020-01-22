package org.mm.renderer.internal;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
         case MM_CAPTURING: return handleCapturing(inputValue, function.getArguments());
         default: return inputValue;
      }
   }

   private Value handleToUpperCase(Value inputValue) {
      Value outputValue;
      String inputString = inputValue.getString();
      if (inputValue instanceof PrefixedValue) {
         PrefixedValue prefixedValue = (PrefixedValue) inputValue;
         String newString = prefixedValue.getPrefix() + ":" + prefixedValue.getLocalName().toUpperCase();
         outputValue = inputValue.update(newString);
      } else {
         String newString = inputString.toUpperCase();
         outputValue = inputValue.update(newString);
      }
      return outputValue;
   }

   private Value handleToLowerCase(Value inputValue) {
      Value outputValue;
      String inputString = inputValue.getString();
      if (inputValue instanceof PrefixedValue) {
         PrefixedValue prefixedValue = (PrefixedValue) inputValue;
         String newString = prefixedValue.getPrefix() + ":" + prefixedValue.getLocalName().toLowerCase();
         outputValue = inputValue.update(newString);
      } else {
         String newString = inputString.toLowerCase();
         outputValue = inputValue.update(newString);
      }
      return outputValue;
   }

   private Value handleReverse(Value inputValue) {
      Value outputValue;
      String inputString = inputValue.getString();
      if (inputValue instanceof PrefixedValue) {
         PrefixedValue prefixedValue = (PrefixedValue) inputValue;
         String reversedString = new StringBuilder(prefixedValue.getLocalName()).reverse().toString();
         String newString = prefixedValue.getPrefix() + ":" + reversedString;
         outputValue = inputValue.update(newString);
      } else {
         String newString = new StringBuilder(inputString).reverse().toString();
         outputValue = inputValue.update(newString);
      }
      return outputValue;
   }

   private Value handleTrim(Value inputValue) {
      String inputString = inputValue.getString();
      String newString = inputString.trim();
      Value outputValue = inputValue.update(newString);
      return outputValue;
   }

   private Value handlePrintf(Value inputValue, List<Argument> arguments) {
      Value outputValue;
      String inputString = inputValue.getString();
      String stringFormat = ((LiteralValue) arguments.get(0)).getString();
      if (inputValue instanceof PrefixedValue) {
         PrefixedValue prefixedValue = (PrefixedValue) inputValue;
         String formattedString = String.format(stringFormat, prefixedValue.getLocalName());
         String newString = prefixedValue.getPrefix() + ":" + formattedString;
         outputValue = inputValue.update(newString);
      } else {
         String newString = String.format(stringFormat, inputString);
         outputValue = inputValue.update(newString);
      }
      return outputValue;
   }

   private Value handleReplace(Value inputValue, List<Argument> arguments) {
      Value outputValue;
      String inputString = inputValue.getString();
      String oldChar = ((LiteralValue) arguments.get(0)).getString();
      String newChar = ((LiteralValue) arguments.get(1)).getString();
      if (inputValue instanceof PrefixedValue) {
         PrefixedValue prefixedValue = (PrefixedValue) inputValue;
         String replacedString = prefixedValue.getLocalName().replace(oldChar, newChar);
         String newString = prefixedValue.getPrefix() + ":" + replacedString;
         outputValue = inputValue.update(newString);
      } else {
         String newString = inputString.replace(oldChar, newChar);
         outputValue = inputValue.update(newString);
      }
      return outputValue;
   }

   private Value handleReplaceFirst(Value inputValue, List<Argument> arguments) {
      Value outputValue;
      String inputString = inputValue.getString();
      String oldChar = ((LiteralValue) arguments.get(0)).getString();
      String newChar = ((LiteralValue) arguments.get(1)).getString();
      if (inputValue instanceof PrefixedValue) {
         PrefixedValue prefixedValue = (PrefixedValue) inputValue;
         String replacedString = prefixedValue.getLocalName().replaceFirst(oldChar, newChar);
         String newString = prefixedValue.getPrefix() + ":" + replacedString;
         outputValue = inputValue.update(newString);
      } else {
         String newString = inputString.replaceFirst(oldChar, newChar);
         outputValue = inputValue.update(newString);
      }
      return outputValue;
   }

   private Value handleReplaceAll(Value inputValue, List<Argument> arguments) {
      Value outputValue;
      String inputString = inputValue.getString();
      String oldChar = ((LiteralValue) arguments.get(0)).getString();
      String newChar = ((LiteralValue) arguments.get(1)).getString();
      if (inputValue instanceof PrefixedValue) {
         PrefixedValue prefixedValue = (PrefixedValue) inputValue;
         String replacedString = prefixedValue.getLocalName().replaceAll(oldChar, newChar);
         String newString = prefixedValue.getPrefix() + ":" + replacedString;
         outputValue = inputValue.update(newString);
      } else {
         String newString = inputString.replaceAll(oldChar, newChar);
         outputValue = inputValue.update(newString);
      }
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

   private Value handleCapturing(Value inputValue, List<Argument> arguments) {
      Value outputValue;
      String inputString = inputValue.getString();
      String regex = ((LiteralValue) arguments.get(0)).getString();
      Pattern p = Pattern.compile(regex);
      if (inputValue instanceof PrefixedValue) {
         PrefixedValue prefixedValue = (PrefixedValue) inputValue;
         Matcher m = p.matcher(prefixedValue.getLocalName());
         String capturedString = "";
         if (m.find()) {
            for (int groupIndex = 1; groupIndex <= m.groupCount(); groupIndex++) {
               capturedString += m.group(groupIndex);
            }
         }
         String newString = prefixedValue.getPrefix() + ":" + capturedString;
         outputValue = inputValue.update(newString);
      } else {
         Matcher m = p.matcher(inputString);
         String newString = "";
         if (m.find()) {
           for (int groupIndex = 1; groupIndex <= m.groupCount(); groupIndex++)
              newString += m.group(groupIndex);
         }
         outputValue = inputValue.update(newString);
      }
      return outputValue;
   }
}
