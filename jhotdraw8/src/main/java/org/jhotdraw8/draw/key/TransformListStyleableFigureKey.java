/* @(#)TransformListStyleableFigureKey.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.key;

import java.util.function.Function;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.styleable.StyleablePropertyBean;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.text.StyleConverterAdapter;
import javafx.scene.transform.Transform;
import org.jhotdraw8.collection.ImmutableObservableList;
import org.jhotdraw8.text.CssTransformListConverter;
import org.jhotdraw8.styleable.WriteableStyleableMapAccessor;

/**
 * TransformListStyleableFigureKey.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class TransformListStyleableFigureKey extends SimpleFigureKey<ImmutableObservableList<Transform>> implements WriteableStyleableMapAccessor<ImmutableObservableList<Transform>> {

    private final static long serialVersionUID = 1L;

    private final CssMetaData<?, ImmutableObservableList<Transform>> cssMetaData;

    /**
     * Creates a new instance with the specified name and with null as the
     * default value.
     *
     * @param name The name of the key.
     */
    public TransformListStyleableFigureKey(String name) {
        this(name, null);
    }

    /**
     * Creates a new instance with the specified name and default value.
     *
     * @param name The name of the key.
     * @param defaultValue The default value.
     */
    public TransformListStyleableFigureKey(String name, ImmutableObservableList<Transform> defaultValue) {
        this(name, DirtyMask.of(DirtyBits.NODE), defaultValue);
    }

    /**
     * Creates a new instance with the specified name, mask and default value.
     *
     * @param name The name of the key.
     * @param mask The dirty mask.
     * @param defaultValue The default value.
     */
    public TransformListStyleableFigureKey(String name, DirtyMask mask, ImmutableObservableList<Transform> defaultValue) {
        super(name, ImmutableObservableList.class, new Class<?>[]{Transform.class}, mask, defaultValue);

        Function<Styleable, StyleableProperty<ImmutableObservableList<Transform>>> function = s -> {
            StyleablePropertyBean spb = (StyleablePropertyBean) s;
            return spb.getStyleableProperty(this);
        };
        boolean inherits = false;
        String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
        final StyleConverter<String, ImmutableObservableList<Transform>> converter
                = new StyleConverterAdapter<ImmutableObservableList<Transform>>(new CssTransformListConverter());
        CssMetaData<Styleable, ImmutableObservableList<Transform>> md
                = new SimpleCssMetaData<Styleable, ImmutableObservableList<Transform>>(property, function,
                        converter, defaultValue, inherits);
        cssMetaData = md;
    }

    @Override
    public CssMetaData<?, ImmutableObservableList<Transform>> getCssMetaData() {
        return cssMetaData;
    }

    private Converter<ImmutableObservableList<Transform>> converter;

    @Override
    public Converter<ImmutableObservableList<Transform>> getConverter() {
        if (converter == null) {
            converter = new CssTransformListConverter();
        }
        return converter;
    }

}
