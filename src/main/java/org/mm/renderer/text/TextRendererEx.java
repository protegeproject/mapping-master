package org.mm.renderer.text;

import java.util.Optional;

import org.mm.core.ReferenceType;
import org.mm.parser.node.ReferenceNode;
import org.mm.renderer.ReferenceUtil;
import org.mm.renderer.RendererException;
import org.mm.rendering.text.TextReferenceRendering;
import org.mm.rendering.text.TextReferenceRenderingEx;
import org.mm.ss.SpreadSheetDataSource;

public class TextRendererEx extends TextRenderer
{
	public TextRendererEx(SpreadSheetDataSource dataSource)
	{
		super(dataSource);
	}

	@Override
	public Optional<TextReferenceRenderingEx> renderReference(ReferenceNode referenceNode) throws RendererException
	{
		TextReferenceRendering renderReference = super.renderReference(referenceNode).get();
		
		String rawValue = renderReference.getRawValue();
		ReferenceType referenceType = renderReference.getReferenceType();
		String comment = createComment(rawValue, referenceNode);
		
		return Optional.of(new TextReferenceRenderingEx(rawValue, comment, referenceType));
	}

	private String createComment(String rawValue, ReferenceNode referenceNode) throws RendererException
	{
		StringBuffer sb = new StringBuffer();
		sb.append(rawValue);
		sb.append(" ");
		sb.append("rendered from reference:");
		sb.append(" ");
		sb.append(referenceNode);
		sb.append(", ");
		sb.append("current location:");
		sb.append(" ");
		sb.append(ReferenceUtil.resolveLocation(getDataSource(), referenceNode));
		sb.append(", ");
		sb.append("location value:");
		sb.append(" ");
		sb.append("\"").append(rawValue).append("\"");
		sb.append(", ");
		sb.append(getEncoding(referenceNode));
		sb.append(".");
		return sb.toString();
	}

	private Object getEncoding(ReferenceNode referenceNode)
	{
		if (referenceNode.hasRDFSLabelValueEncoding()) {
			return "rdfs:label encoding";
		} else if (referenceNode.hasRDFIDValueEncoding()) {
			return "IRI encoding";
		} else if (referenceNode.hasLiteralValueEncoding()) {
			return "Literal encoding";
		} else {
			return "Unknown encoding";
		}
	}
}
