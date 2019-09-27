package org.mm.renderer;

import org.mm.renderer.owl.OwlEntityResolver;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface Renderer<T> {

   T render(String transformationRule, Workbook workbook,
         RenderingContext context, OwlEntityResolver entityResolver);
}
