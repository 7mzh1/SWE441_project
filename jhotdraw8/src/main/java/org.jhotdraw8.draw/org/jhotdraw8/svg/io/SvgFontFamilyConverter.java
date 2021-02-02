/*
 * @(#)SvgFontFamilyConverter.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */

package org.jhotdraw8.svg.io;

import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.collection.ImmutableList;
import org.jhotdraw8.collection.ImmutableLists;
import org.jhotdraw8.css.CssToken;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.css.text.CssConverter;
import org.jhotdraw8.io.IdResolver;
import org.jhotdraw8.io.IdSupplier;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Parses a font family.
 * <pre>
 * font-family = ( { family-name | generic-family , "," }
 *     	             family-name | generic-family )
 *     	       | "inherit" ;
 *
 *
 * family-name = STRING
 *             | IDENT, { IDENT } ;
 *
 * generic-family = 'serif' | 'sans-serif' | 'cursive' | 'fantasy' | 'monospace' ;
 * </pre>
 * <p>
 * References:
 * <dl>
 *     <dd>Font Family</dd><dt><a href="https://www.w3.org/TR/2008/REC-CSS2-20080411/fonts.html#propdef-font-family">CSS Fonts</a></dt>
 * </dl>
 */
public class SvgFontFamilyConverter implements CssConverter<ImmutableList<String>> {

    @Nullable
    @Override
    public ImmutableList<String> parse(@NonNull CssTokenizer tt, @Nullable IdResolver idResolver) throws ParseException, IOException {
        List<String> list = new ArrayList<>();
        StringBuffer buf = new StringBuffer();
        Loop:
        while (true) {
            switch (tt.next()) {
            case CssTokenType.TT_STRING:
                if (buf.length() != 0) {
                    throw tt.createParseException("<font-family>: Comma expected.");
                }
                list.add(tt.currentStringNonNull());
                break;
            case CssTokenType.TT_IDENT:
                if (buf.length() != 0) {
                    buf.append(' ');
                }
                buf.append(tt.currentStringNonNull());
                break;
            case CssTokenType.TT_COMMA:
                if (buf.length() != 0) {
                    list.add(buf.toString());
                    buf.setLength(0);
                }
                break;
            default:
                tt.pushBack();
                break Loop;
            }
        }
        if (list.isEmpty()) {
            throw tt.createParseException("<font-family>: <font-family> or <generic-family> expected.");
        }
        tt.requireNextToken(CssTokenType.TT_EOF, "<font-family>: EOF expected.");
        return ImmutableLists.ofCollection(list);
    }

    @Override
    public <TT extends ImmutableList<String>> void produceTokens(@Nullable TT value, @Nullable IdSupplier idSupplier, @NonNull Consumer<CssToken> out) throws IOException {
        boolean first = true;
        for (String s : value) {
            if (first) {
                first = false;
            } else {
                out.accept(new CssToken(CssTokenType.TT_COMMA));
                out.accept(new CssToken(CssTokenType.TT_S, " "));
            }
            switch (s) {
            case "inherit":
            case "serif":
            case "sans-serif":
            case "cursive":
            case "fantasy":
            case "monospace":
                out.accept(new CssToken(CssTokenType.TT_IDENT, s));
                break;
            default:
                out.accept(new CssToken(CssTokenType.TT_STRING, s));
                break;
            }
        }
    }

    @Nullable
    @Override
    public ImmutableList<String> getDefaultValue() {
        return ImmutableLists.of("serif");
    }

    @Override
    public @Nullable String getHelpText() {
        return "Format of ⟨font-family⟩: ｛⟨family-name｜generic-family⟩,｝⟨family-name｜generic-family⟩｜inherit"
                + "\n  with ⟨family-name⟩: ⟨string⟩"
                + "\n  with ⟨generic-family⟩: serif｜sans-serif｜cursive｜fantasy｜monospace"
                ;
    }

    @Override
    public boolean isNullable() {
        return false;
    }
}
