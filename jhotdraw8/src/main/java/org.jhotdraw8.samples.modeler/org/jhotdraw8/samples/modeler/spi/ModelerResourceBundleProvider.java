/*
 * @(#)ModelerResourceBundleProvider.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.samples.modeler.spi;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.spi.AbstractResourceBundleProvider;

public class ModelerResourceBundleProvider extends AbstractResourceBundleProvider {

    @Override
    public ResourceBundle getBundle(String baseName, Locale locale) {
        return ResourceBundle.getBundle(baseName, locale);
    }
}
