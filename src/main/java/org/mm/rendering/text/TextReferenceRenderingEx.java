package org.mm.rendering.text;

import org.mm.core.ReferenceType;

public class TextReferenceRenderingEx extends TextReferenceRendering
{
	private String comment;

	public TextReferenceRenderingEx(String rawValue, String comment, ReferenceType referenceType)
	{
		super(String.format("%s // %s\n", rawValue, comment), referenceType);
		this.comment = comment;
	}

	public String getComment()
	{
		return comment;
	}
}
