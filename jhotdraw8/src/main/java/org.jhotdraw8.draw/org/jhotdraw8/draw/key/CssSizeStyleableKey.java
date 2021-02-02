/*
 * @(#)CssSizeStyleableKey.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.key;

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.collection.NonNullMapAccessor;
import org.jhotdraw8.css.CssSize;
import org.jhotdraw8.css.text.CssSizeConverter;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.styleable.StyleablePropertyBean;
import org.jhotdraw8.styleable.WriteableStyleableMapAccessor;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.text.StyleConverterAdapter;

import java.util.function.Function;

/**
 * CssSizeStyleableKey.
 *
 * @author Werner Randelshofer
 */
public class CssSizeStyleableKey extends AbstractStyleableKey<@NonNull CssSize> implements WriteableStyleableMapAccessor<@NonNull CssSize>,
        NonNullMapAccessor<@NonNull CssSize> {

    final static long serialVersionUID = 1L;

    private final Converter<@NonNull CssSize> converter = new CssSizeConverter(false);
    @NonNull
    private final CssMetaData<@NonNull Styleable, @NonNull CssSize> cssMetaData;


    /**
     * Creates a new instance with the specified name, mask and default value.
     *  @param name         The name of the key.
     * @param defaultValue The default value.
     */
    public CssSizeStyleableKey(String name, @NonNull CssSize defaultValue) {
        super(name, CssSize.class, defaultValue);

        Function<Styleable, StyleableProperty<@NonNull CssSize>> function = s -> {
            StyleablePropertyBean spb = (StyleablePropertyBean) s;
            return spb.getStyleableProperty(this);
        };
        boolean inherits = false;
        String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
        final StyleConverter<String, @NonNull CssSize> cvrtr
                = new StyleConverterAdapter<>(converter);
        CssMetaData<@NonNull Styleable, @NonNull CssSize> md
                = new SimpleCssMetaData<>(property, function,
                cvrtr, defaultValue, inherits);
        cssMetaData = md;
    }


    @NonNull
    @Override
    public Converter<CssSize> getCssConverter() {
        return converter;
    }

    @Override
    public @NonNull CssMetaData<? extends @NonNull Styleable, CssSize> getCssMetaData() {
        return cssMetaData;

    }
}
