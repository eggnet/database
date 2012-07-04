package models;

public class UnorderedPair<K, V> extends Pair<K, V> implements Comparable<UnorderedPair<K, V>>
{
	public UnorderedPair(K k, V v)
	{
		super(k, v);
	}
	
	@Override
	public int compareTo(UnorderedPair<K,V> other)
	{
		if (this.getFirst().equals(other.getFirst()) || this.getFirst().equals(other.getSecond()) &&
			this.getSecond().equals(other.getFirst()) || this.getSecond().equals(other.getSecond()))
		{
			return 0;
		}
		return -1;
	}
}
