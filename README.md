![Maven](https://github.com/jonas-lj/Ruffini/actions/workflows/maven.yml/badge.svg) [![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://jonas-lj.github.io/Ruffini/allclasses-index.html)

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
    <li><a href="#demos">Demos</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#referencing">Referencing</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## About The Project

<p>
The Ruffini library is developed to make it easy to algorithms in Java involving 
algebraic structures such as finite fields, polynomial rings, field extensions etc. The library includes 
an extensive library of already implemented algorithms such as the Euclidean algorithm, polynomial 
division etc. to make implementation even easier.
</p>
<p>
The project is named after the italian mathematician <a href="https://en.wikipedia.org/wiki/Paolo_Ruffini">Paolo Ruffini (1765-1822)</a> who, among other 
things, contributed to group theory and was the first to give a proof (although incomplete) that there is no 
general formula to solve quintic (and higher order) equations.
</p>

<p align="center">
    <img src="https://upload.wikimedia.org/wikipedia/commons/2/22/Ruffini_paolo.jpg">
</p>

## Built With

The project may be build using maven,

```
mvn clean install
```

and the documentation with

```
mvn javadoc:aggregate
```

<!-- USAGE EXAMPLES -->

## Usage

The Ruffini library is organized analogous of how abstract algebra is presented in mathematics. The base of the library
are a number of interfaces representing abstract algebraic structures. They are organized in an inheritance hierachy as
seen in figure below. Note that `E` is a generic class, representing the element of the given structure.

<p align="center">
  <img src="abstractions.svg" align="center" alt="Inheritance diagram for abstract algebraic structures">
</p>

As in abstract algebra, an algorithm may be defined for an abstract structure and then be used with any concrete
structure satisfying the definition of the abstract structure. As an example, consider the implementation of the
<a href="https://en.wikipedia.org/wiki/Gram–Schmidt_process">Gram-Schmidt process</a> definded over vectors of type `V`
and scalars of type `S`. Now, this code may be used with any
concrete implementations of a vector space over a field.

```java
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
The library also contains some concrete instantiations of algebraic structures such as prime fields, finite fields and 
certain elliptic curve constructions (Curve25519 and BLS12-381). 


## Demos

There are a few demo applications showing some capabilities of the library in the `demos` module.

These include an implementation of the [AKS primality testing algorithm](https://en.wikipedia.org/wiki/AKS_primality_test),
computing the [optimal Ate pairing over the BLS12-381 elliptic construction](https://hackmd.io/@benjaminion/bls12-381) and a 
demonstration of arbitrary precision arithmetic with real numbers inspired by [the work of Hans-J Boehm](https://www.hboehm.info/new_crcalc/CRCalc.html).

<!-- CONTRIBUTING -->

## Contributing

We welcome any help from the community, both if you want to help out developing new features for Ruffini or fix a bug
you've stumbled upon. Simply open a pull request with the suggested changes.

<!-- REFERENCING -->

## Referencing

If you are using the Ruffini library in a research project, please cite it as (setting the `date` field accordingly):

```
@software{Ruffini,
  author = {{Jonas Lindstrøm}},
  title = {{Ruffini} - Java library for computations over abstract algebraic structures},
  note = {\url{https://github.com/jonas-lj/Ruffini}},
  date = {},
}
```
