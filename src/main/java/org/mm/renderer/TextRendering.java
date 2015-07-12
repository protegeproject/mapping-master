package org.mm.renderer;

public class TextRendering implements Rendering
{
	private String rendering;
	private StringBuffer loggingText;

	public TextRendering()
	{
		this.rendering = "";
		this.loggingText = new StringBuffer();
	}

	public TextRendering(String initialTextRendering)
	{
		rendering = initialTextRendering;
	}

	public boolean nothingRendered()
	{
		return rendering.equals("") || rendering.equals("\"\"");
	} // TODO: last clause is hack

	// TODO Remove addText
	@Override public void addText(String text)
	{
		this.rendering = rendering.concat(text);
	}

	// TODO Use real logger

	@Override public void log(String loggingText)
	{
		this.loggingText.append(loggingText);
		System.err.print(loggingText);
	}

	@Override public void logLine(String loggingText)
	{
		log(loggingText + "\n");
	}

	@Override public String getTextRendering()
	{
		stripDoubleQuotesIfNecessary(); // TODO: hack

		return this.rendering;
	}

	@Override public void addLoggingTextNewLine()
	{
		log("\n");
	}

	@Override public String getLoggingText()
	{
		return loggingText.toString();
	}

	@Override public String toString()
	{
		return getTextRendering();
	}

	private void stripDoubleQuotesIfNecessary()
	{
		if (rendering.startsWith("\""))
			rendering = rendering.substring(1, rendering.length() - 1);
	}
}
