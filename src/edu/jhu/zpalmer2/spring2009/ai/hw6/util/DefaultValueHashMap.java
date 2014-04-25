package edu.jhu.zpalmer2.spring2009.ai.hw6.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultValueHashMap<K,V> implements Map<K,V>, Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** The backing data structure. */
	private Map<K,V> backingMap;
	/** The default value. */
	private V defaultValue;
	
	/**
	 * General constructor.
	 * @param defaultValue The default value for this map.
	 */
	public DefaultValueHashMap(V defaultValue)
	{
		super();
		this.backingMap = new HashMap<K,V>();
		this.defaultValue = defaultValue;
	}

	@Override
	public void clear()
	{
		this.backingMap.clear();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return this.backingMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		if ((this.defaultValue==null) && (value==null)) return true;
		return ((this.defaultValue!=null) && (this.defaultValue.equals(value))) || this.backingMap.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return this.backingMap.entrySet();
	}

	@Override
	public V get(Object key)
	{
		V v = this.backingMap.get(key);
		if (v==null) v = this.defaultValue;
		return v;
	}

	@Override
	public boolean isEmpty()
	{
		return this.backingMap.isEmpty();
	}

	@Override
	public Set<K> keySet()
	{
		return this.backingMap.keySet();
	}

	@Override
	public V put(K key, V value)
	{
		if (((value==null) && (defaultValue==null)) || ((defaultValue!=null) && (defaultValue.equals(value))))
		{
			return this.backingMap.remove(key);
		} else
		{
			return this.backingMap.put(key, value);
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
		{
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key)
	{
		return this.backingMap.remove(key);
	}

	@Override
	public int size()
	{
		return this.backingMap.size();
	}

	@Override
	public Collection<V> values()
	{
		return this.backingMap.values();
	}
	
	
}
