package dk.jonaslindstrom.ruffini.common.vector;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ConstructiveVector<E> extends BaseVector<E> {

    private final int d;
    private final IntFunction<E> view;

    public ConstructiveVector(int d, IntFunction<E> view) {
        this.d = d;
        this.view = view;
    }

    @Override
    public int size() {
        return d;
    }

    @Override
    public E get(int i) {
        assert (i >= 0 && i < d);
        return view.apply(i);
    }

    @Override
    public <F> Vector<F> map(Function<E, F> map) {
        return new ConstructiveVector<>(d, i -> map.apply(view.apply(i)));
    }

    @Override
    public List<E> asList() {
        return new AbstractList<>() {

            @Override
            public int size() {
                return d;
            }

            @Override
            public E get(int i) {
                assert (i >= 0 && i < d);
                return view.apply(i);
            }
        };
    }

}
