
package org.protege.owl.mm.parser;

import org.protege.owl.mm.OWLEntityType;

public class DefaultReferenceDirectives
{
	private OWLEntityType defaultEntityType;

	private int defaultValueEncoding;

	private String defaultLocationValue = null;
	private String defaultDataValue = null;
	private String defaultRDFID = null;
	private String defaultRDFSLabel = null;
	private String defaultLanguage = null;
	private String defaultPrefix = null;
	private String defaultNamespace = null;

	private int defaultShiftDirective;
	private int defaultEmptyLocationDirective;
	private int defaultEmptyDataValueDirective;
	private int defaultEmptyRDFIDDirective;
	private int defaultEmptyRDFSLabelDirective;
	private int defaultIfExistsDirective;
	private int defaultIfNotExistsDirective;

	public DefaultReferenceDirectives(int defaultEntityType, int defaultValueEncoding, String defaultLocationValue, String defaultDataValue, String defaultRDFID,
			String defaultRDFSLabel, String defaultLanguage, String defaultPrefix, String defaultNamespace, int defaultShiftDirective,
			int defaultEmptyLocationDirective, int defaultEmptyDataValueDirective, int defaultEmptyRDFIDDirective, int defaultEmptyRDFSLabelDirective,
			int defaultIfExistsDirective, int defaultIfNotExistsDirective)
	{
		this.defaultEntityType = new OWLEntityType(defaultEntityType);

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

	public OWLEntityType getDefaultEntityType()
	{
		return defaultEntityType;
	}

	public int getDefaultValueEncoding()
	{
		return defaultValueEncoding;
	}

	public String getDefaultLocationValue()
	{
		return defaultLocationValue;
	}

	public String getDefaultDataValue()
	{
		return defaultDataValue;
	}

	public String getDefaultRDFID()
	{
		return defaultRDFID;
	}

	public String getDefaultRDFSLabel()
	{
		return defaultRDFSLabel;
	}

	public String getDefaultLanguage()
	{
		return defaultLanguage;
	}

	public String getDefaultPrefix()
	{
		return defaultPrefix;
	}

	public String getDefaultNamespace()
	{
		return defaultNamespace;
	}

	public int getDefaultShiftDirective()
	{
		return defaultShiftDirective;
	}

	public int getDefaultIfExistsDirective()
	{
		return defaultIfExistsDirective;
	}

	public int getDefaultIfNotExistsDirective()
	{
		return defaultIfNotExistsDirective;
	}

	public int getDefaultEmptyLocationDirective()
	{
		return defaultEmptyLocationDirective;
	}

	public int getDefaultEmptyDataValueDirective()
	{
		return defaultEmptyDataValueDirective;
	}

	public int getDefaultEmptyRDFIDDirective()
	{
		return defaultEmptyRDFIDDirective;
	}

	public int getDefaultEmptyRDFSLabelDirective()
	{
		return defaultEmptyRDFSLabelDirective;
	}
}