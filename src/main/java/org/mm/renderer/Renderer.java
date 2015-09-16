package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.ExpressionNode;
import org.mm.rendering.Rendering;
import org.mm.ss.SpreadSheetDataSource;

public interface Renderer
{
   Optional<? extends Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException;

   public SpreadSheetDataSource getDataSource();

   public ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
