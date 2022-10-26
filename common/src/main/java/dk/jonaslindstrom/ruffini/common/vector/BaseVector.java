package dk.jonaslindstrom.ruffini.common.vector;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class BaseVector<E> extends AbstractCollection<E> implements Vector<E> {

    @Override
    public String toString() {
        return asList().toString(); //TODO
    }

    @Override
    public boolean equals(Vector<E> other, BiPredicate<E, E> equality) {
        for (int i = 0; i < this.size(); i++) {
            if (!equality.test(this.get(i), other.get(i))) {
                return false;
            }
        }
        return true;
    }


    @Override
    public Iterator<E> iterator() {
        return stream().iterator();
    }

    @Override
    public Stream<E> stream() {
        return IntStream.range(0, this.size()).mapToObj(this::get);
    }

    @Override
    public <F> Vector<F> map(Function<E, F> map) {
        return new ConcreteVector<>(this.size(), i -> map.apply(get(i)));
    }

    @Override
    public <F> Vector<F> view(Function<E, F> map) {
        return new ConstructiveVector<>(this.size(), i -> map.apply(get(i)));
    }

    @Override
    public Vector<E> pad(int n, E padding) {
        return new ConstructiveVector<>(n, i -> {
            if (i < this.size()) {
                return get(i);
            } else {
                return padding;
            }
        });
    }

}
