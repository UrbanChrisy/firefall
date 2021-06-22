# Firefall
### [Objectify](https://github.com/objectify/objectify) inspired Google Firestore interface wrapper

Firefall has a very similar api to Objectify, but not identical - yet?

I currently use this in a product which is almost production ready, and I havn't encountered any problems as of yet. But that doesnt mean there are none. But I do need people to test it, so if your feeling adventurous give it a crack.


## Getting Started
* Firefall lets you persist, retrieve, delete, and query your own typed objects.

```java
@Entity
class Car extends HasId<Car> {

    public Car() {
        super(Car.class);
    }

    String color;

}

Card car = new Car();
car.setId("ids_are_always_strings");
car.setColor("Red");

fir().save().type(Car.class).entity(new Car()).now();
Car c = fir().load().type(Car.class).id("ids_are_always_strings").now();
fir().delete().type(Basic.class).entity(c).now();
```

# Documentation
Full documentation is available in the Wiki, which can be found [here](https://github.com/UrbanChrisy/firefall/wiki).

# Downloads
Firefall is released to Maven. Latest version can be found [here](https://search.maven.org/artifact/nz.co.delacour/firefall-core).

At the moment ive only deployed to maven as I do not use Gradle.
