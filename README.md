<!-- PROJECT LOGO -->
<br />
<p align="center">
<!--
  <a href="https://github.com/jonas-lj/Ruffini">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>
-->

  <h3 align="center">Ruffini</h3>

  <p align="center">
    Computations over algebraic structures made easy
  </p>
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
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

The Ruffini library is developed to make it easy to implement algorithms in Java involving various algebraic structures such as finite field, polynomials, elliptic curves etc. The library includes an extensive library of already implemented algorithms such as the Euclidean algorithm, polynomial division etc. to make implementation even easier.


## Built With
The project is build using maven,
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

![Inhertince diagram of algebraic abstractions](abstractions.svg "Inheritance diagram for abstract algebraic structures")

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

There are a few demo applications showing some of the capabilities of the library. They are found in `src/main/java/dk/jonaslindstrom/math/demo`.

<!-- CONTRIBUTING -->
## Contributing

If you want to help out developing new features for Ruffini or fix a bug you've stumbleb upon, it may be done as follows:

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/MyFeature`)
3. Commit your Changes (`git commit -m 'Add MyFeature'`)
4. Push to the Branch (`git push origin feature/MyFeature`)
5. Open a Pull Request


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Jonas Lindstrøm - [@lindstrom_dk](https://twitter.com/lindstrom_dk) - mail@jonaslindstrom.dk

Project Link: [https://github.com/jonas-lj/Ruffini](https://github.com/jonas-lj/Ruffini)](https://github.com/jonas-lj/Ruffini)