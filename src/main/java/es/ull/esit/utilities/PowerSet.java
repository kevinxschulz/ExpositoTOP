package es.ull.esit.utilities;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @class PowerSet
 * @brief Utility class for generating all subsets (power set) of a given set.
 *
 * This generic class implements both Iterator and Iterable interfaces to enumerate all possible subsets
 * of a provided set. It uses a BitSet to efficiently track subset membership and supports iteration
 * over the power set. Useful for combinatorial algorithms and exhaustive search tasks.
 *
 * @tparam E Type of elements in the set.
 */
public class PowerSet<E> implements Iterator<Set<E>>, Iterable<Set<E>> {

    private E[] arr = null;
    private BitSet bset = null;

    @SuppressWarnings("unchecked")
    public PowerSet(Set<E> set) {
        this.arr = (E[]) set.toArray();
        this.bset = new BitSet(this.arr.length + 1);
    }

    @Override
    public boolean hasNext() {
        return !this.bset.get(this.arr.length);
    }

    @Override
    public Set<E> next() {
        Set<E> returnSet = new TreeSet<>();
        for (int i = 0; i < this.arr.length; i++) {
            if (this.bset.get(i)) {
                returnSet.add(this.arr[i]);
            }
        }
        for (int i = 0; i < this.bset.size(); i++) {
            if (!this.bset.get(i)) {
                this.bset.set(i);
                break;
            } else {
                this.bset.clear(i);
            }
        }
        return returnSet;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not Supported!");
    }

    @Override
    public Iterator<Set<E>> iterator() {
        return this;
    }
}