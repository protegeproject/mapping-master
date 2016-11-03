package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.MMExpressionNode;
import org.mm.rendering.Rendering;
import org.mm.workbook.SpreadSheetDataSource;

public interface Renderer
{
   Optional<? extends Rendering> render(MMExpressionNode node) throws RendererException;

   public SpreadSheetDataSource getDataSource();

   public ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
