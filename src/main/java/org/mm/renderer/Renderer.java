package org.mm.renderer;

import org.mm.ss.SpreadSheetDataSource;

public interface Renderer
{
	public void changeDataSource(SpreadSheetDataSource source);

	public ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
