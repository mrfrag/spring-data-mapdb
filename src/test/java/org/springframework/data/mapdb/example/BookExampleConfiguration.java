package org.springframework.data.mapdb.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.mapdb.MapDbKeyValueAdapter;
import org.springframework.data.mapdb.config.EnableMapDbRepositories;

@Configuration
@EnableMapDbRepositories(basePackages = "org.springframework.data.mapdb.example")
public class BookExampleConfiguration {

	@Bean
	public KeyValueTemplate keyValueTemplate() {
		return new KeyValueTemplate(new MapDbKeyValueAdapter());
	}

}
