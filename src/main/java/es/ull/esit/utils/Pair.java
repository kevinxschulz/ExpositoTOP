package es.ull.esit.utils;
import java.util.Objects;

/**
 * @class Pair
 * @brief Generic utility class for storing a pair of objects.
 *
 * This class represents a tuple containing two elements, referred to as 'first' and 'second'.
 * It provides methods for equality comparison, hash code generation, and static creation of pairs.
 * The class is useful for grouping related objects and is commonly used in data structures and algorithms.
 *
 * @tparam F Type of the first element.
 * @tparam S Type of the second element.
 */
public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second);
    }

    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    public static <A, B> Pair <A, B> create(A a, B b) {
        return new Pair<A, B>(a, b);
    }
}
