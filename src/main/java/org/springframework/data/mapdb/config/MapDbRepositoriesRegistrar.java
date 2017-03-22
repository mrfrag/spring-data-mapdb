package org.springframework.data.mapdb.config;

import java.lang.annotation.Annotation;

import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

class MapDbRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.keyvalue.repository.config.
	 * KeyValueRepositoriesRegistrar#getAnnotation()
	 */
	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableMapDbRepositories.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.config.
	 * RepositoryBeanDefinitionRegistrarSupport#getExtension()
	 */
	@Override
	protected RepositoryConfigurationExtension getExtension() {
		return new MapDbRepositoryConfigurationExtension();
	}

	private static class MapDbRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.springframework.data.keyvalue.repository.config.
		 * KeyValueRepositoryConfigurationExtension #getModuleName()
		 */
		@Override
		public String getModuleName() {
			return "MapDb";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.springframework.data.keyvalue.repository.config.
		 * KeyValueRepositoryConfigurationExtension #getModulePrefix()
		 */
		@Override
		protected String getModulePrefix() {
			return "mapdb";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.springframework.data.keyvalue.repository.config.
		 * KeyValueRepositoryConfigurationExtension
		 * #getDefaultKeyValueTemplateRef()
		 */
		@Override
		protected String getDefaultKeyValueTemplateRef() {
			return "keyValueTemplate";
		}
	}
}
