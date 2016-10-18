package org.mm.app;

import org.mm.renderer.Renderer;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface ApplicationModel
{
   /**
    * Returns the data source model
    */
   DataSourceModel getDataSourceModel();

   /**
    * Returns the mapping expression model
    */
   TransformationRuleModel getTransformationRuleModel();

   /**
    * Returns the default renderer.
    */
   Renderer getDefaultRenderer();
}
