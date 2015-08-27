package org.mm.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class MappingExpressionSetFactory
{
	public static MappingExpressionSet createEmptyMappingExpressionSet()
	{
		return new MappingExpressionSet();
	}

	public static MappingExpressionSet loadMapppingExpressionSetFromDocument(String location) throws FileNotFoundException
	{
		BufferedReader br = new BufferedReader(new FileReader(location));
		return new Gson().fromJson(br, MappingExpressionSet.class);
	}

	public static void saveMappingExpressionSetToDocument(String location, MappingExpressionSet mappings) throws IOException
	{
		String json = new Gson().toJson(mappings);
		FileWriter writer = new FileWriter(location);
		writer.write(json);
		writer.close();
	}
}
