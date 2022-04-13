import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.github.coolcrabs.brachyura.dependency.JavaJarDependency;
import io.github.coolcrabs.brachyura.maven.HttpMavenRepository;
import io.github.coolcrabs.brachyura.maven.LocalMavenRepository;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.maven.MavenResolver;
import io.github.coolcrabs.brachyura.project.DescriptiveBuildscriptName;
import io.github.coolcrabs.brachyura.project.java.SimpleJavaModule;
import io.github.coolcrabs.brachyura.project.java.SimpleJavaProject;

public class Buildscript extends SimpleJavaProject implements DescriptiveBuildscriptName {

    private final Collection<JavaJarDependency> dependencies = new ArrayList<>();

    public Buildscript() {
        MavenResolver resolver = new MavenResolver(getBuildDir().resolve("dependencies"))
                .addRepository(new LocalMavenRepository(MavenResolver.MAVEN_LOCAL))
                .addRepository(new HttpMavenRepository("https://papermc.io/repo/repository/maven-public/"))
                .addRepository(MavenResolver.MAVEN_CENTRAL_REPO);
        dependencies.addAll(resolver.getTransitiveDependencies(new MavenId("io.papermc.paper", "paper-api", "1.18.2-R0.1-SNAPSHOT")));
        dependencies.addAll(resolver.getTransitiveDependencies(new MavenId("net.kyori", "adventure-api", "4.10.1")));
        dependencies.add(new JavaJarDependency(getProjectDir().resolve("HolographicDisplays-2.3.3-SNAPSHOT.jar"), null, new MavenId("com.gmail.filoghost", "holographicdisplays", "2.3.3-SNAPSHOT")));
    }

    @Override
    @NotNull
    public String getBuildscriptName() {
        return "FarmCraftBuildscript";
    }

    @Override
    @NotNull
    public MavenId getId() {
        return new MavenId("de.geolykt", "farmcraft", "0.0.1-SNAPSHOT");
    }

    @Override
    public SimpleJavaModule createProjectModule() {
        return new SimpleJavaProjectModule() {

            @Override
            @NotNull
            protected List<JavaJarDependency> createDependencies() {
                List<JavaJarDependency> deps = new ArrayList<>(super.createDependencies());
                deps.addAll(Buildscript.this.dependencies);
                return deps;
            }
        };
    }

    @Override
    public int getJavaVersion() {
        return 17;
    }
}
