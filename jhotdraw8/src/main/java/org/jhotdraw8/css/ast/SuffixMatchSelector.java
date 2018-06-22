/* @(#)SuffixMatchSelector.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css.ast;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jhotdraw8.css.SelectorModel;

/**
 * A "suffix match selector" {@code $=} matches an element if the element has an
 * attribute with the specified name and its value ends with the specified
 * substring.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SuffixMatchSelector extends AbstractAttributeSelector {

    private final String attributeName;
    private final String substring;

    public SuffixMatchSelector(String attributeName, String substring) {
        this.attributeName = attributeName;
        this.substring = substring;
    }

    @Nullable
    @Override
    protected <T> T match(@NonNull SelectorModel<T> model, T element) {
        return (model.attributeValueEndsWith(element, attributeName, substring))//
                ? element : null;
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + attributeName + "&=" + substring + ']';
    }

}
