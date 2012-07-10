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
	
	@Override
	public int hashCode() {
		if (!(this.getFirst() instanceof String) || !(this.getSecond() instanceof String))
		{
			return super.hashCode();
		}
		else
		{
			String result = null;
			String fst = (String)this.getFirst();
			String snd = (String)this.getSecond();
			if (fst.compareTo(snd) > 0)
				result = fst+snd;
			else
				result = snd+fst;
			return result.hashCode();
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (other.getClass() != getClass()) return false;
		
		UnorderedPair<K, V> rhs = (UnorderedPair<K,V>)other;
		if (this.getFirst().equals(rhs.getFirst()) || this.getFirst().equals(rhs.getSecond()) &&
			this.getSecond().equals(rhs.getFirst()) || this.getSecond().equals(rhs.getSecond()))
		{
			return true;
		}
		return false;
	}
}
