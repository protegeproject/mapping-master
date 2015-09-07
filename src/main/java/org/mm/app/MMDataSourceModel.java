package org.mm.app;

import java.util.ArrayList;
import java.util.List;

import org.mm.ss.SpreadSheetDataSource;

public class MMDataSourceModel implements DataSourceModel
{
	private SpreadSheetDataSource dataSource;

	public MMDataSourceModel()
	{
		this(new SpreadSheetDataSource());
	}

	public MMDataSourceModel(SpreadSheetDataSource dataSource)
	{
		if (dataSource == null) {
			throw new ApplicationStartupException("Data source can't be null");
		}
		this.dataSource = dataSource;
	}

	@Override
	public SpreadSheetDataSource getDataSource()
	{
		return dataSource;
	}

	public List<String> getSheetNames()
	{
		List<String> sheetNames = new ArrayList<String>();
		if (dataSource != null) {
			sheetNames.addAll(dataSource.getSheetNames());
		}
		return sheetNames;
	}
}
