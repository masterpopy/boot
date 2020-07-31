package personal.popy.localserver.servlet.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RestPath<T> {
    private static final String STRING_PATH_SEPARATOR = "/";

    private volatile T defaultHandler;
    private final SubstringMap<T> paths = new SubstringMap<>();
    private final HashMap<String, T> exactPathMatches = new HashMap<>();

    /**
     * lengths of all registered paths
     */
    private volatile int[] lengths = {};

    public RestPath(final T defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public RestPath() {
    }

    /**
     * Matches a path against the registered handlers.
     *
     * @param path The relative path to match
     * @return The match match. This will never be null, however if none matched its value field will be
     */
    public PathMatch<T> match(String path) {
        if (!exactPathMatches.isEmpty()) {
            T match = getExactPath(path);
            if (match != null) {

                return new PathMatch<>(path, "", match);
            }
        }

        int length = path.length();
        final int[] lengths = this.lengths;
        for (int i = 0; i < lengths.length; ++i) {
            int pathLength = lengths[i];
            if (pathLength == length) {
                SubstringMap.SubstringMatch<T> next = paths.get(path, length);
                if (next != null) {

                    return new PathMatch<>(path, "", next.getValue());
                }
            } else if (pathLength < length) {
                char c = path.charAt(pathLength);
                if (c == '/') {

                    //String part = path.substring(0, pathLength);
                    SubstringMap.SubstringMatch<T> next = paths.get(path, pathLength);
                    if (next != null) {

                        return new PathMatch<>(next.getKey(), path.substring(pathLength), next.getValue());
                    }
                }
            }
        }

        return new PathMatch<>("", path, defaultHandler);
    }

    /**
     * Adds a path prefix and a handler for that path. If the path does not start
     * with a / then one will be prepended.
     * <p>
     * The match is done on a prefix bases, so registering /foo will also match /bar. Exact
     * path matches are taken into account first.
     * <p>
     * If / is specified as the path then it will replace the default handler.
     *
     * @param path    The path
     * @param handler The handler
     */
    public synchronized RestPath<T> addPrefixPath(final String path, final T handler) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("path must not be empty");
        }

        final String normalizedPath = normalizeSlashes(path);

        if (STRING_PATH_SEPARATOR.equals(normalizedPath)) {
            this.defaultHandler = handler;
            return this;
        }

        paths.put(normalizedPath, handler);

        buildLengths();
        return this;
    }


    public synchronized RestPath<T> addExactPath(final String path, final T handler) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("path must not be empty");
        }
        exactPathMatches.put(normalizeSlashes(path), handler);
        return this;
    }

    public T getExactPath(final String path) {
        return exactPathMatches.get(normalizeSlashes(path));
    }

    public T getPrefixPath(final String path) {

        final String normalizedPath = normalizeSlashes(path);

        // enable the prefix path mechanism to return the default handler
        SubstringMap.SubstringMatch<T> match = paths.get(normalizedPath);
        if (STRING_PATH_SEPARATOR.equals(normalizedPath) && match == null) {
            return this.defaultHandler;
        }
        if (match == null) {
            return null;
        }

        // return the value for the given path
        return match.getValue();
    }

    private void buildLengths() {
        final Set<Integer> lengths = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return -o1.compareTo(o2);
            }
        });
        for (String p : paths.keys()) {
            lengths.add(p.length());
        }

        int[] lengthArray = new int[lengths.size()];
        int pos = 0;
        for (int i : lengths) {
            lengthArray[pos++] = i;
        }
        this.lengths = lengthArray;
    }


    public synchronized RestPath<T> removePrefixPath(final String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("path must not be empty");
        }

        final String normalizedPath = normalizeSlashes(path);

        if (STRING_PATH_SEPARATOR.equals(normalizedPath)) {
            defaultHandler = null;
            return this;
        }

        paths.remove(normalizedPath);

        buildLengths();
        return this;
    }

    public synchronized RestPath<T> removeExactPath(final String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("path must not be empty");
        }

        exactPathMatches.remove(normalizeSlashes(path));

        return this;
    }

    public synchronized RestPath<T> clearPaths() {
        paths.clear();
        exactPathMatches.clear();
        this.lengths = new int[0];
        defaultHandler = null;
        return this;
    }

    public Map<String, T> getPaths() {
        return paths.toMap();
    }

    public static final class PathMatch<T> {
        private final String matched;
        private final String remaining;
        private final T value;

        public PathMatch(String matched, String remaining, T value) {
            this.matched = matched;
            this.remaining = remaining;
            this.value = value;
        }

        public String getRemaining() {
            return remaining;
        }

        public String getMatched() {
            return matched;
        }

        public T getValue() {
            return value;
        }
    }

    //移除多余的/，并在开头添加/
    private static String normalizeSlashes(String path) {
        int prefix = 0;
        int length = path.length();
        for (int i = 0; i < length; i++) {
            if (path.charAt(i) == '/') {
                prefix++;
            } else {
                break;
            }
        }
        int suffix = 0;
        for (int i = length - 1; i > prefix; i--) {
            if (path.charAt(i) == '/') {
                suffix++;
            } else {
                break;
            }
        }
        if (prefix == 0) {
            path = STRING_PATH_SEPARATOR.concat(path);
            prefix += 1;
        }
        if (suffix > 0 || prefix > 1) {
            path = path.substring(prefix - 1, length - suffix);
        }
        return path;
    }
}
 