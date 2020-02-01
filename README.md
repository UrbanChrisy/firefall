# Firefall
### Objectify inspired Google Firestore interface wrapper

Firefall has a very similar api to Objectify, but not identical - yet?

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
Full documentation is available in the Wiki, which can be found here.

# Downloads
Firefall is released to Maven. To get the latest version, click [here](https://search.maven.org/artifact/nz.co.delacour/firefall-core).
