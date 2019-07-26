/*
 * @(#)CssLocatorConverter.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css.text;

import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.css.StreamCssTokenizer;
import org.jhotdraw8.draw.locator.BoundsLocator;
import org.jhotdraw8.draw.locator.Locator;
import org.jhotdraw8.io.CharBufferReader;
import org.jhotdraw8.io.IdFactory;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.xml.text.XmlNumberConverter;

import java.io.IOException;
import java.nio.CharBuffer;
import java.text.ParseException;

/**
 * CssLocatorConverter.
 * <p>
 * Currently converts relative locators only.
 * <p>
 * FIXME should implement CssConverter instead of Converter
 *
 * @author Werner Randelshofer
 */
public class CssLocatorConverter implements Converter<Locator> {

    private static final XmlNumberConverter numberConverter = new XmlNumberConverter();

    public CssLocatorConverter() {
    }

    @Override
    public Locator fromString(@Nullable CharBuffer buf, IdFactory idFactory) throws ParseException, IOException {
        Locator c;
        CssTokenizer tt = new StreamCssTokenizer(new CharBufferReader(buf));
        c = parseLocator(tt);

        if (!buf.toString().trim().isEmpty()) {
            throw new ParseException("Locator: End expected, found:" + buf.toString(), buf.position());
        }
        return c;
    }

    @Nullable
    @Override
    public Locator getDefaultValue() {
        return null;
    }

    @Nonnull
    @Override
    public String getHelpText() {
        return "Format of ⟨Locator⟩: relative(⟨x⟩%,⟨y⟩%)";
    }

    /**
     * Parses a Locator.
     *
     * @param tt the tokenizer
     * @return the parsed color
     * @throws ParseException if parsing fails
     * @throws IOException    if IO fails
     */
    @Nonnull
    public Locator parseLocator(@Nonnull CssTokenizer tt) throws ParseException, IOException {
        Locator color = null;

        switch (tt.next()) {
            case CssTokenType.TT_FUNCTION:
                if (!"relative".equals(tt.currentString())) {
                    throw new ParseException("Locator: function 'relative(' expected, found:" + tt.currentValue(), tt.getStartPosition());
                }
                break;
            default:
                throw new ParseException("Locator: function expected, found:" + tt.currentValue(), tt.getStartPosition());
        }
        double x, y;

        switch (tt.next()) {
            case CssTokenType.TT_NUMBER:
                x = tt.currentNumber().doubleValue();
                break;
            case CssTokenType.TT_PERCENTAGE:
                x = tt.currentNumber().doubleValue() / 100.0;
                break;
            default:
                throw new ParseException("BoundsLocator: x-value expected but found " + tt.currentValue(), tt.getStartPosition());
        }
        switch (tt.next()) {
            case ',':
                break;
            default:
                tt.pushBack();
                break;
        }
        switch (tt.next()) {
            case CssTokenType.TT_NUMBER:
                y = tt.currentNumber().doubleValue();
                break;
            case CssTokenType.TT_PERCENTAGE:
                y = tt.currentNumber().doubleValue() / 100.0;
                break;
            default:
                throw new ParseException("BoundsLocator: y-value expected but found " + tt.currentValue(), tt.getStartPosition());
        }
        if (tt.next() != ')') {
            throw new ParseException("BoundsLocator: ')' expected but found " + tt.currentValue(), tt.getStartPosition());
        }

        return new BoundsLocator(x, y);
    }


    @Override
    public void toString(@Nonnull Appendable out, IdFactory idFactory, Locator value) throws IOException {
        if (value instanceof BoundsLocator) {
            BoundsLocator rl = (BoundsLocator) value;
            out.append("relative(");
            out.append(numberConverter.toString(rl.getRelativeX() * 100));
            out.append("%,");
            out.append(numberConverter.toString(rl.getRelativeY() * 100));
            out.append("%)");
        } else {
            throw new UnsupportedOperationException("only BoundsLocator supported, value:" + value);
        }
    }
}
