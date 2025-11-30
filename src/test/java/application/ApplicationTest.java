package application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {

    @Test
    void testApplicationClassExists() {
        LabsOopApplication app = new LabsOopApplication();
        assertNotNull(app);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(LabsOopApplication.class.getMethod("main", String[].class));
    }

    @Test
    void testApplicationAnnotations() {
        assertTrue(LabsOopApplication.class.isAnnotationPresent(SpringBootApplication.class));
    }

    @Test
    void testEntityScanAnnotation() {
        assertTrue(LabsOopApplication.class.isAnnotationPresent(EntityScan.class));
        EntityScan entityScan = LabsOopApplication.class.getAnnotation(EntityScan.class);
        assertEquals("entity", entityScan.value()[0]);
    }

    @Test
    void testEnableJpaRepositoriesAnnotation() {
        assertTrue(LabsOopApplication.class.isAnnotationPresent(EnableJpaRepositories.class));
        EnableJpaRepositories jpaRepos = LabsOopApplication.class.getAnnotation(EnableJpaRepositories.class);
        assertEquals("repository", jpaRepos.value()[0]);
    }

    @Test
    void testSpringBootApplicationScanPackages() {
        SpringBootApplication annotation = LabsOopApplication.class.getAnnotation(SpringBootApplication.class);
        String[] scanPackages = annotation.scanBasePackages();
        
        assertTrue(scanPackages.length > 0);
        
        boolean hasApplication = false;
        boolean hasController = false;
        boolean hasService = false;
        boolean hasRepository = false;
        
        for (String pkg : scanPackages) {
            if ("application".equals(pkg)) hasApplication = true;
            if ("controller".equals(pkg)) hasController = true;
            if ("service".equals(pkg)) hasService = true;
            if ("repository".equals(pkg)) hasRepository = true;
        }
        
        assertTrue(hasApplication);
        assertTrue(hasController);
        assertTrue(hasService);
        assertTrue(hasRepository);
    }

    @Test
    void testMainMethodIsStatic() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = LabsOopApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodIsPublic() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = LabsOopApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodReturnsVoid() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = LabsOopApplication.class.getMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void testMainMethodParameterTypes() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = LabsOopApplication.class.getMethod("main", String[].class);
        Class<?>[] paramTypes = mainMethod.getParameterTypes();
        assertEquals(1, paramTypes.length);
        assertEquals(String[].class, paramTypes[0]);
    }

    @Test
    void testAllScanPackagesPresent() {
        SpringBootApplication annotation = LabsOopApplication.class.getAnnotation(SpringBootApplication.class);
        String[] scanPackages = annotation.scanBasePackages();
        
        java.util.Set<String> packages = new java.util.HashSet<>(java.util.Arrays.asList(scanPackages));
        
        assertTrue(packages.contains("application"));
        assertTrue(packages.contains("config"));
        assertTrue(packages.contains("controller"));
        assertTrue(packages.contains("service"));
        assertTrue(packages.contains("repository"));
        assertTrue(packages.contains("entity"));
        assertTrue(packages.contains("dto"));
        assertTrue(packages.contains("search"));
        assertTrue(packages.contains("functions"));
        assertTrue(packages.contains("operations"));
        assertTrue(packages.contains("concurrent"));
        assertTrue(packages.contains("io"));
        assertTrue(packages.contains("exceptions"));
    }

    @Test
    void testMainMethodRuns() {
        ConfigurableApplicationContext context = SpringApplication.run(
            LabsOopApplication.class, 
            "--spring.main.web-application-type=none",
            "--spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
            "--spring.datasource.driver-class-name=org.h2.Driver",
            "--spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
            "--spring.jpa.hibernate.ddl-auto=create-drop",
            "--spring.liquibase.enabled=false"
        );
        assertNotNull(context);
        assertTrue(context.isRunning());
        context.close();
    }

    @Test
    void testMainMethodWithArgs() {
        LabsOopApplication.main(new String[]{
            "--spring.main.web-application-type=none",
            "--spring.datasource.url=jdbc:h2:mem:testdb2;DB_CLOSE_DELAY=-1",
            "--spring.datasource.driver-class-name=org.h2.Driver",
            "--spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
            "--spring.jpa.hibernate.ddl-auto=create-drop",
            "--spring.liquibase.enabled=false"
        });
    }
}

