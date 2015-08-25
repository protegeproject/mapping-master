package org.mm.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MappingExpressionSet implements Iterable<MappingExpression>
{
	private Set<MappingExpression> mappingExpressions;

	public MappingExpressionSet()
	{
		mappingExpressions = new HashSet<MappingExpression>();
	}

	public void add(MappingExpression expression)
	{
		mappingExpressions.add(expression);
	}

	public boolean remove(MappingExpression expression)
	{
		return mappingExpressions.remove(expression);
	}

	public boolean isEmpty()
	{
		return mappingExpressions.isEmpty();
	}

	@Override
	public Iterator<MappingExpression> iterator()
	{
		return mappingExpressions.iterator();
	}

	public boolean contains(MappingExpression mapping)
	{
		return mappingExpressions.contains(mapping);
	}

	public int size()
	{
		return mappingExpressions.size();
	}
}
