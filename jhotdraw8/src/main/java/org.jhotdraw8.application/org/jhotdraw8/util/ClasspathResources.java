/*
 * @(#)ClasspathResources.java
 * Copyright © 2020 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.util;

import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;


public class ClasspathResources extends ResourceBundle implements Serializable, Resources {
    private final static Logger LOG = Logger.getLogger(ClasspathResources.class.getName());
    private final static long serialVersionUID = 1L;

    /**
     * The base class
     */
    private Class<?> baseClass = getClass();
    /**
     * The base name of the resource bundle.
     */
    @NonNull
    private final String baseName;
    /**
     * The locale.
     */
    @NonNull
    private final Locale locale;

    /**
     * The parent resources object.
     */
    @Nullable
    private Resources parent;

    /**
     * The wrapped resource bundle.
     */
    private transient ResourceBundle resource;

    /**
     * Creates a new ClasspathResources object which wraps the provided resource bundle.
     *
     * @param baseName the base name
     * @param locale   the locale
     */
    public ClasspathResources(@NonNull String baseName, @NonNull Locale locale) {
        this.locale = locale;
        this.baseName = baseName;
        this.resource = ResourceBundle.getBundle(baseName, locale);

        ClasspathResources potentialParent = null;
        String moduleAndParentBaseName = null;
        try {
            moduleAndParentBaseName = this.resource.getString(Resources.PARENT_RESOURCE_KEY);
        } catch (MissingResourceException e) {

        }
        if (moduleAndParentBaseName != null) {
            String[] split = moduleAndParentBaseName.split("\\s+|\\s*,\\s*");
            String parentBaseName;
            String moduleName;
            switch (split.length) {
                case 1:
                    moduleName = "";
                    parentBaseName = split[0];
                    break;
                case 2:
                    moduleName = split[0];
                    parentBaseName = split[1];
                    break;
                default:
                    throw new IllegalArgumentException("Illegal " + PARENT_RESOURCE_KEY + " resource " + moduleAndParentBaseName);
            }
            try {
                potentialParent = new ClasspathResources(parentBaseName, locale);
            } catch (MissingResourceException e) {
                MissingResourceException ex = new MissingResourceException("Can't find parent bundle $parent=\"" + moduleAndParentBaseName + "\" specified in " + moduleName + "," + baseName + "," + locale, baseName, "");
                ex.initCause(e);
                throw ex;
            }
        }
        this.parent = potentialParent;
    }

    @Override
    public boolean containsKey(@Nullable String key) {
        Objects.requireNonNull(key, "key is null");
        if (resource.containsKey(key)) {
            return true;
        }
        if (parent != null) {
            return parent.containsKey(key);
        }
        LOG.warning("Can't find resource for bundle " + baseName + " key not found: " + key);
        return false;
    }


    @Override
    public Class<?> getBaseClass() {
        return baseClass;
    }

    @Override
    public Object getModule() {
        return null;
    }




    @Nullable
    @Override
    protected Object handleGetObject(@NonNull String key) {
        Object obj = handleGetObjectRecursively(key);
        if (obj == null) {
            obj = "";
            LOG.warning("Can't find resource for bundle " + baseName + ", key " + key);
        }

        if (obj instanceof String) {
            obj = substitutePlaceholders(key, (String) obj);
        }
        return obj;
    }

    @Nullable
    public Object handleGetObjectRecursively(@NonNull String key) {
        Object obj = null;
        try {
            obj = resource.getObject(key);
        } catch (MissingResourceException e) {
            if (parent != null) {
                return parent.handleGetObjectRecursively(key);
            }
        }
        return obj;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClasspathResources" + "[" + baseName + "]";
    }


    @NonNull
    @Override
    public String getBaseName() {
        return baseName;
    }

    /**
     * Get the appropriate ResourceBundle subclass.
     *
     * @param baseName the base name
     * @return the resource bundle
     * @see java.util.ResourceBundle
     */
    @NonNull
    static Resources getResources(@NonNull String baseName)
            throws MissingResourceException {
        return getResources(baseName, LocaleUtil.getDefault());
    }

    /**
     * Get the appropriate ResourceBundle subclass.
     *
     * @param baseName the base name
     * @param locale   the locale
     * @return the resource bundle
     * @see java.util.ResourceBundle
     */
    static Resources getResources(@NonNull String baseName, @NonNull Locale locale)
            throws MissingResourceException {
        Resources r;
        r = new ClasspathResources(baseName, locale);
        return r;
    }

    @NonNull
    @Override
    public ResourceBundle asResourceBundle() {
        return this;
    }

    @Override
    public Enumeration<String> getKeys() {
        Set<String> keys = new LinkedHashSet<>();

        for (String key : (Iterable<String>) () -> resource.getKeys().asIterator()) {
            keys.add(key);
        }
        if (parent != null) {
            for (String key : (Iterable<String>) () -> parent.getKeys().asIterator()) {
                keys.add(key);
            }
        }

        return Collections.enumeration(keys);
    }

    @Nullable
    @Override
    public Resources getParent() {
        return parent;
    }

    @Override
    public void setParent(Resources parent) {
        this.parent = parent;
    }
}
