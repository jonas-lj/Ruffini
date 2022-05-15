<!-- PROJECT LOGO -->
<h1>Ruffini</h1>

<p>
    Computations over algebraic structures in Java made easy.
</p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#built-with">Built With</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<p>
The Ruffini library is developed to make it easy to implement algorithms in Java involving various 
algebraic structures such as finite fields, polynomial rings, field extensions etc. The library includes 
an extensive library of already implemented algorithms such as the Euclidean algorithm, polynomial 
division etc. to make implementation even easier.
</p>
<p>
The project is named after the italian mathematician Paolo Ruffini (1765-1822) who, among other 
things, contributed to group theory and was the first to give a proof (incomplete) that there is no 
general formula to solve quintic (and higher order) equations.
</p>

[<img src="https://upload.wikimedia.org/wikipedia/commons/2/22/Ruffini_paolo.jpg">](https://en.wikipedia.org/wiki/Paolo_Ruffini)

## Built With
The project may be build using maven,
```
mvn clean install
```
and the documentation with
```
mvn javadoc:javadoc
```

<!-- USAGE EXAMPLES -->
## Usage

The Ruffini library is organized analogous of how abstract algebra is presented in mathematics. The base of the library are a number of interfaces representing abstract algebraic structures. They are organized in an inheritance hierachy as seen in figure below. Note that `E` is a generic class, representing the element of the given structure.

<p align="center">
  <img src="abstractions.svg" align="center" alt="Inheritance diagram for abstract algebraic structures">
</p>

As in abstract algebra, an algorithm may be defined for an abstract structure and then be used with any concrete structure satisfying the definition of the abstract structure. As an example, consider the implementation of the Gram-Schmidt process definded over vectors of type `V` and scalars of type `S`. Now, this code may be used with any concrete implementations of a vector space over a field.

```
public class GramSchmidt<V, S, F extends Field<S>> implements Function<List<V>, List<V>> {

  private final VectorSpace<V, S, F> vectorSpace;
  private final BiFunction<V, V, S> innerProduct;

  public GramSchmidt(VectorSpace<V, S, F> V,
      BiFunction<V, V, S> innerProduct) {
    this.vectorSpace = V;
    this.innerProduct = innerProduct;
  }

  @Override
  public List<V> apply(List<V> vectors) {
    List<V> U = new ArrayList<>();

    Projection<V, S, F> proj = new Projection<>(vectorSpace, innerProduct);

    for (V v : vectors) {
      for (V u : U) {
        V p = proj.apply(v, u);
        v = vectorSpace.subtract(v, p);
      }
      U.add(v);
    }
    return U;
  }

}
```

There are a few demo applications showing some capabilities of the library. They are found in `src/main/java/dk/jonaslindstrom/math/demo`.

<!-- CONTRIBUTING -->
## Contributing

If you want to help out developing new features for Ruffini or fix a bug you've stumbled upon, it may be done as follows:

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/MyFeature`)
3. Commit your Changes (`git commit -m 'Add MyFeature'`)
4. Push to the Branch (`git push origin feature/MyFeature`)
5. Open a Pull Request


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.

