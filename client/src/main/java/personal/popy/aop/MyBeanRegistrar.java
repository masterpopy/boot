package personal.popy.aop;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Repository;

import java.io.IOException;

public final class MyBeanRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

	Environment env;
	ResourceLoader resourceLoader;

	@Override
	public void setEnvironment(Environment environment) {
		environment.getProperty("");
		this.env = environment;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
	                                    BeanDefinitionRegistry registry) {
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(ControllerFactoryBean.class);
		ConstructorArgumentValues c = definition.getConstructorArgumentValues();
		/*c.addGenericArgumentValue(IndexController.class.getName());
		c.addGenericArgumentValue(IndexControllerImpl.class.getName());*/
		//registry.registerBeanDefinition("indexControllerImpl", definition);
	}

	private void registerHyRepository(BeanDefinitionRegistry beanDefinitionRegistry) {
		ClassPathBeanDefinitionScanner scanner = new MyScanner(beanDefinitionRegistry);
		scanner.setResourceLoader(resourceLoader);
		AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
		scanner.addIncludeFilter(new InterfaceTypeFilter(HyRepository.class));
		scanner.addIncludeFilter(new AnnotationTypeFilter(Repository.class));
		for (BeanDefinition b : scanner.findCandidateComponents("personal.popy.aop")) {
			b.getConstructorArgumentValues().addGenericArgumentValue(b.getBeanClassName());
			String beanname = beanNameGenerator.generateBeanName(b, beanDefinitionRegistry);
			b.setBeanClassName(HyRepositoryFactoryBean.class.getName());
			beanDefinitionRegistry.registerBeanDefinition(beanname, b);
		}
	}

	private static class InterfaceTypeFilter extends AssignableTypeFilter {
		public InterfaceTypeFilter(Class<?> targetType) {
			super(targetType);
		}

		public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
			ClassMetadata data = metadataReader.getClassMetadata();
			return data.isInterface() && super.match(metadataReader, metadataReaderFactory);
		}
	}

	private static class MyScanner extends ClassPathBeanDefinitionScanner {

		public MyScanner(BeanDefinitionRegistry registry) {
			super(registry, false);
		}

		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return true;
		}
	}

}
