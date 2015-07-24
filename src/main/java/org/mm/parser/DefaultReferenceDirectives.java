
package org.mm.parser;

import org.mm.core.ReferenceType;

public class DefaultReferenceDirectives
{
	private final ReferenceType defaultReferenceType;

	private final int defaultValueEncoding;

	private final String defaultLocationValue;
	private final String defaultLiteral;
	private final String defaultRDFID;
	private final String defaultRDFSLabel;
	private final String defaultLanguage;
	private final String defaultPrefix;
	private final String defaultNamespace;

	private final int defaultEmptyLocationDirective;
	private final int defaultEmptyLiteralDirective;
	private final int defaultEmptyRDFIDDirective;
	private final int defaultEmptyRDFSLabelDirective;
	private final int defaultIfExistsDirective;
	private final int defaultIfNotExistsDirective;

	private int defaultShiftDirective;

	public DefaultReferenceDirectives(int defaultReferenceType, int defaultValueEncoding, String defaultLocationValue, String defaultLiteral, String defaultRDFID,
			String defaultRDFSLabel, String defaultLanguage, String defaultPrefix, String defaultNamespace, int defaultShiftDirective,
			int defaultEmptyLocationDirective, int defaultEmptyLiteralDirective, int defaultEmptyRDFIDDirective, int defaultEmptyRDFSLabelDirective,
			int defaultIfExistsDirective, int defaultIfNotExistsDirective)
	{
		this.defaultReferenceType = new ReferenceType(defaultReferenceType);

		this.defaultValueEncoding = defaultValueEncoding;

		this.defaultLocationValue = defaultLocationValue;
		this.defaultLiteral = defaultLiteral;
		this.defaultRDFID = defaultRDFID;
		this.defaultRDFSLabel = defaultRDFSLabel;
		this.defaultLanguage = defaultLanguage;
		this.defaultPrefix = defaultPrefix;
		this.defaultNamespace = defaultNamespace;

		this.defaultShiftDirective = defaultShiftDirective;
		this.defaultEmptyLocationDirective = defaultEmptyLocationDirective;
		this.defaultEmptyLiteralDirective = defaultEmptyLiteralDirective;
		this.defaultEmptyRDFIDDirective = defaultEmptyRDFIDDirective;
		this.defaultEmptyRDFSLabelDirective = defaultEmptyRDFSLabelDirective;
		this.defaultIfExistsDirective = defaultIfExistsDirective;
		this.defaultIfNotExistsDirective = defaultIfNotExistsDirective;
	}

	public void setDefaultShiftDirective(int shiftDirective)
	{
		this.defaultShiftDirective = shiftDirective;
	}

	public ReferenceType getDefaultReferenceType()
	{
		return this.defaultReferenceType;
	}

	public int getDefaultValueEncoding()
	{
		return this.defaultValueEncoding;
	}

	public String getDefaultLocationValue()
	{
		return this.defaultLocationValue;
	}

	public String getDefaultLiteral()
	{
		return this.defaultLiteral;
	}

	public String getDefaultRDFID()
	{
		return this.defaultRDFID;
	}

	public String getDefaultRDFSLabel()
	{
		return this.defaultRDFSLabel;
	}

	public String getDefaultLanguage()
	{
		return this.defaultLanguage;
	}

	public String getDefaultPrefix()
	{
		return this.defaultPrefix;
	}

	public String getDefaultNamespace()
	{
		return this.defaultNamespace;
	}

	public int getDefaultShiftDirective()
	{
		return this.defaultShiftDirective;
	}

	public int getDefaultIfExistsDirective()
	{
		return this.defaultIfExistsDirective;
	}

	public int getDefaultIfNotExistsDirective()
	{
		return this.defaultIfNotExistsDirective;
	}

	public int getDefaultEmptyLocationDirective()
	{
		return this.defaultEmptyLocationDirective;
	}

	public int getDefaultEmptyLiteralDirective()
	{
		return this.defaultEmptyLiteralDirective;
	}

	public int getDefaultEmptyRDFIDDirective()
	{
		return this.defaultEmptyRDFIDDirective;
	}

	public int getDefaultEmptyRDFSLabelDirective()
	{
		return this.defaultEmptyRDFSLabelDirective;
	}
}