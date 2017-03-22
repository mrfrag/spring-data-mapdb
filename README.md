# Spring Data MapDB

This is [Spring Data](http://projects.spring.io/spring-data/) module for a [MapDB: database engine](https://github.com/jankotek/mapdb)
Many thanks to [Spring Data Hazelcast](https://github.com/hazelcast/spring-data-hazelcast) for an inspiration.

## Installation

Clone the repo and build the project.

## Usage

Configuration sample.

```java
@Configuration
@EnableMapDbRepositories(basePackages = "your.fancy.package")
public class RepositoryConfiguration {

	@Bean
	public KeyValueTemplate keyValueTemplate() {
		return new KeyValueTemplate(new MapDbKeyValueAdapter());
	}

}
```

or 

```java
@Configuration
@EnableMapDbRepositories(basePackageClasses = { YourFancyRepository.class})
public class RepositoryConfiguration {

	@Bean
	public KeyValueTemplate keyValueTemplate() {
		DB db = DBMaker.fileDB("/wherever/it/is/foo.db").make();
		return new KeyValueTemplate(new MapDbKeyValueAdapter(db));
	}

}
```

for detailed example check `src/test/java/org/springframework/data/mapdb/example/`

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

##License

MIT License
