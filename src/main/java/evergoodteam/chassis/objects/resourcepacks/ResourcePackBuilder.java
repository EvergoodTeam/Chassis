package evergoodteam.chassis.objects.resourcepacks;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.minecraft.SharedConstants;
import net.minecraft.resource.AbstractFileResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Log4j2
@ToString
public class ResourcePackBuilder extends AbstractFileResourcePack implements ResourcePack {

    private static final Pattern RESOURCEPACK_PATH = Pattern.compile("[a-z0-9-_]+");
    public static final List<ResourcePackBuilder> BUILT = new ArrayList<>();

    private String id;
    private ResourceType resourceType;
    private PackResourceMetadata packMetadata;
    private Path basePath;
    private String separator;
    private Set<String> namespaces;

    public ResourcePackBuilder(String id, ResourceType resourceType, Path basePath) {
        super(null);
        this.id = id;
        this.resourceType = resourceType;

        this.packMetadata = new PackResourceMetadata(new TranslatableText(id + ".metadata.description"), ResourceType.CLIENT_RESOURCES.getPackVersion(SharedConstants.getGameVersion()));

        this.basePath = basePath.resolve(id).resolve("resources").toAbsolutePath().normalize();
        this.separator = basePath.getFileSystem().getSeparator();

        BUILT.add(this);
    }

    private Path getPath(String filename) {
        Path childPath = basePath.resolve(filename.replace("/", separator));

        if (childPath.startsWith(basePath) && Files.exists(childPath)) return childPath;
        else return null;
    }

    @Override
    protected boolean containsFile(String filename) {

        if (PACK_METADATA_NAME.equals(filename)) {
            return true;
        }

        Path path = getPath(filename);
        return path != null && Files.isRegularFile(path);
    }

    @Override
    protected InputStream openFile(String fileName) throws IOException {
        Path path = getPath(fileName);

        if (path != null && Files.isRegularFile(path)) return Files.newInputStream(path);

        return InputStream.nullInputStream();
    }

    @Override
    public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix, int maxDepth, Predicate<String> pathFilter) {

        List<Identifier> ids = new ArrayList<>();
        String path = prefix.replace("/", separator);

        Path namespacePath = getPath(type.getDirectory() + "/" + namespace);

        if (namespacePath != null) {

            Path searchPath = namespacePath.resolve(path).toAbsolutePath().normalize();

            if (Files.exists(searchPath)) {
                try {
                    Files.walk(searchPath, maxDepth)
                            .filter(Files::isRegularFile)
                            .filter((p) -> {
                                String filename = p.getFileName().toString();
                                return !filename.endsWith(".mcmeta") && pathFilter.test(filename);
                            })
                            .map(namespacePath::relativize)
                            .map((p) -> p.toString().replace(separator, "/"))
                            .forEach((string) -> {

                                try {
                                    ids.add(new Identifier(namespace, string));
                                } catch (InvalidIdentifierException e) {
                                    log.error(e.getMessage(), e);
                                }
                            });
                } catch (IOException e) {
                    log.error("findResources at {} in namespace {} failed: {}", path, namespace, e);
                }
            }
        }

        return ids;
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {

        if (this.namespaces == null) {

            Path file = getPath(type.getDirectory());

            if(file == null) log.error("Invalid Path");

            if (!Files.isDirectory(file)) {
                return Collections.emptySet();
            }

            Set<String> namespaces = new HashSet<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(file, Files::isDirectory)) {
                for (Path path : stream) {
                    String s = path.getFileName().toString();

                    s = s.replace(separator, "");

                    if (RESOURCEPACK_PATH.matcher(s).matches()) {
                        namespaces.add(s);
                    } else {
                        log.error("Invalid namespace format at {}", path);
                    }
                }
            } catch (IOException e) {
                log.error("Could not get namespaces", e);
            }

            this.namespaces = namespaces;
        }

        return this.namespaces;
    }

    @Nullable
    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
        if (metaReader.getKey().equals("pack")) {
            return (T) packMetadata;
        } else {
            return null;
        }
    }

    @Override
    public String getName() {

        return StringUtils.capitalize(this.id);
    }

    @Override
    public void close() {
    }
}
