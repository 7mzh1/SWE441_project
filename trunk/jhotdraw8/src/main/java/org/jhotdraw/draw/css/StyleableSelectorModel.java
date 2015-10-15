/*
 * @(#)StyleableSelectorModel.java
 * Copyright (c) 2014 Supercomputing Systems AG, Schweiz.
 * Alle Rechte vorbehalten. 
 */
package org.jhotdraw.draw.css;

import java.util.List;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import org.jhotdraw.xml.css.SelectorModel;

/**
 * StyleableSelectorModel.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class StyleableSelectorModel implements SelectorModel<Styleable> {

    @Override
    public boolean hasId(Styleable element, String id) {
        return id.equals(element.getId());
    }

    @Override
    public boolean hasType(Styleable element, String type) {
        return type.equals(element.getTypeSelector());
    }

    @Override
    public boolean hasStyleClass(Styleable element, String clazz) {
        return element.getStyleClass().contains(clazz);
    }

    @Override
    public boolean hasPseudoClass(Styleable element, String pseudoClass) {
        return element.getPseudoClassStates().contains(PseudoClass.getPseudoClass(pseudoClass));
    }

    @Override
    public Styleable getParent(Styleable element) {
        return element.getStyleableParent();
    }

    @Override
    public Styleable getPreviousSibling(Styleable element) {
        return null;
    }

    @Override
    public boolean hasAttribute(Styleable element, String attributeName) {
        // XXX linear time!
        List<CssMetaData<? extends Styleable, ?>> list = element.getCssMetaData();
        for (CssMetaData<? extends Styleable, ?> item : list) {
            if (attributeName.equals(item.getProperty())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getAttribute(Styleable element, String attributeName) {
        List<CssMetaData<? extends Styleable, ?>> list = element.getCssMetaData();
        // XXX linear time!
        for (CssMetaData<? extends Styleable, ?> i : list) {
            @SuppressWarnings("unchecked")
            CssMetaData<Styleable, ?> item = (CssMetaData<Styleable, ?>) i;
            if (attributeName.equals(item.getProperty())) {
                Object value = item.getStyleableProperty(element).getValue();

               // this is messy. we should be able to use the converter to 
                // convert the value from the object type to a CSS String.
                return value == null ? "" : value.toString();
            }
        }
        return null;
    }

}