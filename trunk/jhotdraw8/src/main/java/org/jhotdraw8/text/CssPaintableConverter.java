/* @(#)CssPaintConverter.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.text;

import java.io.IOException;
import java.nio.CharBuffer;
import java.text.ParseException;
import java.util.Locale;
import javafx.scene.paint.Color;
import org.jhotdraw8.draw.io.IdFactory;

/**
 * CssPaintableConverter.
 * <p>
 * Parses the following EBNF from the
 * <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html">JavaFX
 * CSS Reference Guide</a>.
 * </p>
 * <pre>
 * Paintable := (Color|LinearGradient|RadialGradient|ImagePattern RepeatingImagePattern) ;
 * </pre>
 * <p>
 * FIXME currently only parses the Color and the LinearGradient productions
 * </p>
 *
 * @author Werner Randelshofer
 */
public class CssPaintableConverter implements Converter<Paintable> {

    private CssColorConverter colorConverter = new CssColorConverter();
    private CssLinearGradientConverter linearGradientConverter = new CssLinearGradientConverter();
    private XmlNumberConverter doubleConverter = new XmlNumberConverter();

    public void toString(Appendable out, IdFactory idFactory, Paintable value) throws IOException {
        if (value == null) {
            out.append("none");
        } else if (Color.TRANSPARENT.equals(value)) {
            out.append("transparent");
        } else if (value instanceof CssColor) {
            CssColor c = (CssColor) value;
            colorConverter.toString(out, idFactory, c);
        } else if (value instanceof CssLinearGradient) {
            CssLinearGradient lg = (CssLinearGradient) value;
            linearGradientConverter.toString(out, idFactory, lg);
        } else {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    @Override
    public Paintable fromString(CharBuffer buf, IdFactory idFactory) throws ParseException, IOException {
        String str = buf.toString().trim().toLowerCase(Locale.ROOT);

        int pos=buf.position();
        ParseException pe=null;
        try {
            return colorConverter.fromString(buf, idFactory);
        } catch (ParseException e) {
            pe=e;
        }
        try {
            buf.position(pos);
            return linearGradientConverter.fromString(buf, idFactory);
        } catch (ParseException e) {
            if (e.getErrorOffset()>pe.getErrorOffset()) {
           pe=e;
            }
        }
        throw pe;
    }

    @Override
    public Paintable getDefaultValue() {
        return null;
    }
}
