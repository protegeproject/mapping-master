package org.mm.rendering.text;

import org.mm.rendering.Rendering;

public class TextRendering implements Rendering
{
	private final String textRepresentation;

	public TextRendering(String textRepresentation)
	{
		this.textRepresentation = textRepresentation;
	}

	public String getTextRendering()
	{
		return this.textRepresentation;
	}

	@Override public String toString()
	{
		return getTextRendering();
	}
}
