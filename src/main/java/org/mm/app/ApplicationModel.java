package org.mm.app;

import org.mm.renderer.Renderer;
import org.mm.ss.SpreadSheetDataSource;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface ApplicationModel
{
   /**
    * Returns the workbook
    */
   SpreadSheetDataSource getWorkbook();

   /**
    * Returns the default renderer.
    */
   Renderer getTransformationRenderer();

   /**
    * Returns the mapping expression model
    */
   TransformationRuleModel getTransformationRuleModel();
}
