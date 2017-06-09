package org.mm.renderer;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface Renderer<T> {

   T render(String transformationRule, Workbook workbook, RenderingContext context);
}
