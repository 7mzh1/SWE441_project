/*
 * @(#)UriResolver.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.io;

import org.jhotdraw8.annotation.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

/**
 * Takes an URI and resolves it against the specified internal URI, then
 * relativizes it against the specified external URI.
 *
 * @author Werner Randelshofer
 */
public class UriResolver implements Function<URI, URI> {

    @Nullable
    private final URI internal;
    @Nullable
    private final URI external;

    /**
     * Creates a new instance.
     *
     * @param internal the internal URI
     * @param externl  the external URI
     */
    public UriResolver(@Nullable URI internal, @Nullable URI externl) {
        this.internal = internal;
        this.external = externl;
    }

    @Override
    public URI apply(URI uri) {
        URI resolved = uri;
        if (internal != null) {
            // Paths is better at resolving URIs than URI.relativize().
            if ("file".equals(internal.getScheme()) &&
                    ("file".equals(resolved.getScheme()) || resolved.getScheme() == null)) {
                resolved = Paths.get(internal).resolve(Paths.get(resolved.getPath())).normalize().toUri();
            } else {
                resolved = internal.resolve(resolved);
            }
        }
        if (external != null) {
            // Paths is better at relativizing URIs than URI.relativize().
            if ("file".equals(external.getScheme()) &&
                    ("file".equals(resolved.getScheme()) || resolved.getScheme() == null)) {
                Path resolvedPath = Paths.get(external).relativize(Paths.get(resolved.getPath()));
                if (resolvedPath.isAbsolute()) {
                    resolved = resolvedPath.toUri();
                } else {
                    try {
                        resolved = new URI(null, null, resolvedPath.toString()
                                , null, null);
                    } catch (URISyntaxException e) {
                        resolved = uri;// we tried hard, but we failed
                    }
                }
            } else {
                resolved = external.relativize(resolved);
            }
        }
        return resolved;
    }

}
