package org.mm.app;

import org.mm.renderer.Renderer;

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
