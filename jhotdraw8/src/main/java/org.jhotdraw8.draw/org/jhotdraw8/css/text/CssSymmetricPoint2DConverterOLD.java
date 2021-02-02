/*
 * @(#)CssSymmetricPoint2DConverterOLD.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css.text;

import javafx.geometry.Point2D;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssToken;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.io.IdResolver;
import org.jhotdraw8.io.IdSupplier;

import java.io.IOException;
import java.text.ParseException;
import java.util.function.Consumer;

/**
 * Converts a {@code javafx.geometry.Point2D} into a {@code String} and vice
 * versa. If the X and the Y-value are identical, then only one value is output.
 *
 * @author Werner Randelshofer
 */
public class CssSymmetricPoint2DConverterOLD extends AbstractCssConverter<Point2D> {

    private final boolean withSpace;
    private boolean withComma;

    public CssSymmetricPoint2DConverterOLD(boolean nullable) {
        this(nullable, true, false);
    }

    public CssSymmetricPoint2DConverterOLD(boolean nullable, boolean withSpace, boolean withComma) {
        super(nullable);
        this.withSpace = withSpace;
        this.withComma = withComma || !withSpace;
    }

    @NonNull
    @Override
    public Point2D parseNonNull(@NonNull CssTokenizer tt, @Nullable IdResolver idResolver) throws ParseException, IOException {
        final double x, y;
        tt.requireNextToken(CssTokenType.TT_NUMBER, " ⟨SymmetricPoint2D⟩: ⟨x⟩ expected.");
        x = tt.currentNumberNonNull().doubleValue();
        tt.skipIfPresent(CssTokenType.TT_COMMA);
        if (tt.next() == CssTokenType.TT_NUMBER) {
            y = tt.currentNumberNonNull().doubleValue();
        } else {
            tt.pushBack();
            y = x;
        }
        return new Point2D(x, y);
    }

    @Override
    protected <TT extends Point2D> void produceTokensNonNull(@NonNull TT value, @Nullable IdSupplier idSupplier, @NonNull Consumer<CssToken> out) {
        out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getX()));
        if (value.getX() != value.getY()) {
            if (withComma) {
                out.accept(new CssToken(CssTokenType.TT_COMMA));
            }
            if (withSpace) {
                out.accept(new CssToken(CssTokenType.TT_S, " "));
            }
            out.accept(new CssToken(CssTokenType.TT_NUMBER, value.getY()));
        }
    }


    @NonNull
    @Override
    public Point2D getDefaultValue() {
        return new Point2D(0, 0);
    }

    @NonNull
    @Override
    public String getHelpText() {
        return "Format of ⟨SymmetricPoint2D⟩: ⟨xy⟩ ｜ ⟨x⟩ ⟨y⟩";
    }

}
