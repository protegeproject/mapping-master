package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.MMExpressionNode;
import org.mm.rendering.Rendering;
import org.mm.workbook.Workbook;

/**
 * Represents the transformation renderer that produces the output rendering
 * objects.
 *
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface Renderer
{
   Workbook getWorkbook();

   ReferenceRendererConfiguration getReferenceRendererConfiguration();

   Optional<? extends Rendering> render(MMExpressionNode node) throws RendererException;
}
