package org.mm.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mm.core.MappingExpression;
import org.mm.core.MappingExpressionSet;

public class MMMappingExpressionModel implements MappingExpressionModel
{
	private MappingExpressionSet mappings;

	private List<MappingExpression> cache = new ArrayList<MappingExpression>();

	public MMMappingExpressionModel()
	{
		this(new MappingExpressionSet());
	}

	public MMMappingExpressionModel(MappingExpressionSet mappings)
	{
		if (mappings == null) {
			throw new ApplicationStartupException("Mapping expression set can't be null");
		}
		changeMappingExpressionSet(mappings);
	}

	@Override
	public List<MappingExpression> getExpressions()
	{
		return Collections.unmodifiableList(cache);
	}

	public void changeMappingExpressionSet(MappingExpressionSet mappings)
	{
		this.mappings = mappings;
		fireModelChanged();
	}

	private void fireModelChanged()
	{
		cache.clear(); // reset the cache
		for (MappingExpression mapping : mappings) {
			cache.add(mapping);
		}
	}

	public MappingExpression getExpression(int index)
	{
		return cache.get(index);
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
}
