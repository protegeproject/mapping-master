package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.MMExpressionNode;
import org.mm.rendering.Rendering;
import org.mm.workbook.Workbook;

public interface Renderer
{
   Optional<? extends Rendering> render(MMExpressionNode node) throws RendererException;

   public Workbook getDataSource(); // TODO: Rename to getWorkbook

   public ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
