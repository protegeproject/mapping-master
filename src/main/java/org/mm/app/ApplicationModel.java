package org.mm.app;

import java.util.Collection;

import org.mm.renderer.Renderer;
import org.mm.transformationrule.TransformationRule;
import org.mm.workbook.Workbook;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface ApplicationModel
{
   /**
    * Returns the workbook
    */
   Workbook getWorkbook();

   /**
    * Returns the default renderer.
    */
   Renderer getTransformationRenderer();

   /**
    * Returns the transformation rules.
    */
   Collection<TransformationRule> getTransformationRules();
}
