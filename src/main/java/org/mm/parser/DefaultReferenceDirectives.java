
package org.mm.parser;

import org.mm.core.ReferenceType;

public class DefaultReferenceDirectives
{
	private final ReferenceType defaultReferenceType;

	private final int defaultValueEncoding;

	private final String defaultLocationValue;
	private final String defaultDataValue;
	private final String defaultRDFID;
	private final String defaultRDFSLabel;
	private final String defaultLanguage;
	private final String defaultPrefix;
	private final String defaultNamespace;

	private final int defaultEmptyLocationDirective;
	private final int defaultEmptyDataValueDirective;
	private final int defaultEmptyRDFIDDirective;
	private final int defaultEmptyRDFSLabelDirective;
	private final int defaultIfExistsDirective;
	private final int defaultIfNotExistsDirective;

	private int defaultShiftDirective;

	public DefaultReferenceDirectives(int defaultReferenceType, int defaultValueEncoding, String defaultLocationValue, String defaultDataValue, String defaultRDFID,
			String defaultRDFSLabel, String defaultLanguage, String defaultPrefix, String defaultNamespace, int defaultShiftDirective,
			int defaultEmptyLocationDirective, int defaultEmptyDataValueDirective, int defaultEmptyRDFIDDirective, int defaultEmptyRDFSLabelDirective,
			int defaultIfExistsDirective, int defaultIfNotExistsDirective)
	{
		this.defaultReferenceType = new ReferenceType(defaultReferenceType);

		this.defaultValueEncoding = defaultValueEncoding;

		this.defaultLocationValue = defaultLocationValue;
		this.defaultDataValue = defaultDataValue;
		this.defaultRDFID = defaultRDFID;
		this.defaultRDFSLabel = defaultRDFSLabel;
		this.defaultLanguage = defaultLanguage;
		this.defaultPrefix = defaultPrefix;
		this.defaultNamespace = defaultNamespace;

		this.defaultShiftDirective = defaultShiftDirective;
		this.defaultEmptyLocationDirective = defaultEmptyLocationDirective;
		this.defaultEmptyDataValueDirective = defaultEmptyDataValueDirective;
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

	public String getDefaultDataValue()
	{
		return this.defaultDataValue;
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

	public int getDefaultEmptyDataValueDirective()
	{
		return this.defaultEmptyDataValueDirective;
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