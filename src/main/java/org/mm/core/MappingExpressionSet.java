package org.mm.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

public class MappingExpressionSet implements Iterable<MappingExpression>
{
	@SerializedName("Collections")
	private Set<MappingExpression> mappingExpressions;

	public MappingExpressionSet()
	{
		mappingExpressions = new HashSet<MappingExpression>();
	}

	public Set<MappingExpression> getMappingExpressions()
	{
		return Collections.unmodifiableSet(mappingExpressions);
	}

	public void setMappingExpressions(Set<MappingExpression> mappings)
	{
		mappingExpressions = mappings;
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

	public boolean contains(MappingExpression mapping)
	{
		return mappingExpressions.contains(mapping);
	}

	public int size()
	{
		return mappingExpressions.size();
	}

	@Override
	public Iterator<MappingExpression> iterator()
	{
		return mappingExpressions.iterator();
	}
}
