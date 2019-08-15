package org.mm.renderer.internal;

import org.mm.parser.MappingMasterParserConstants;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class BuiltInFunctionHandler implements MappingMasterParserConstants {

   public Value evaluate(BuiltInFunction function, Value inputValue) {
      return null; // NO-OP
   }
}
