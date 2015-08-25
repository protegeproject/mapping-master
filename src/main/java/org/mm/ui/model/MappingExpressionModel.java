package org.mm.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mm.core.MappingExpression;
import org.mm.core.MappingExpressionSet;

public class MappingExpressionModel implements MMModel
{
	private List<MappingExpression> cache = new ArrayList<MappingExpression>();

	public MappingExpressionModel()
	{
		this(new MappingExpressionSet());
	}

	public MappingExpressionModel(MappingExpressionSet mappings)
	{
		for (MappingExpression mapping : mappings) {
			cache.add(mapping);
		}
	}

	public MappingExpression getExpression(int index)
	{
		return cache.get(index);
	}

	public List<MappingExpression> getExpressions()
	{
		return Collections.unmodifiableList(cache);
	}

	public boolean isEmpty()
	{
		return cache.isEmpty();
	}

	public boolean contains(MappingExpression mapping)
	{
		return cache.contains(mapping);
	}

	public void addMappingExpression(MappingExpression mapping)
	{
		cache.add(mapping);
	}

	public void removeMappingExpression(MappingExpression mapping)
	{
		cache.remove(mapping);
	}

	public MappingExpressionSet getMappingExpressionSet()
	{
		MappingExpressionSet mappings = new MappingExpressionSet();
		for (MappingExpression mapping : cache) {
			mappings.add(mapping);
		}
		return mappings;
	}
}
