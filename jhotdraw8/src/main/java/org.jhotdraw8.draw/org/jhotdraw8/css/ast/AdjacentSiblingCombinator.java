/* @(#)AdjacentSiblingCombinator.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css.ast;

import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.css.CssToken;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.SelectorModel;

import java.util.function.Consumer;

/**
 * An "adjacent sibling combinator" matches an element if its first selector
 * matches on the adjacent sibling of the element and if its second selector
 * matches the element.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class AdjacentSiblingCombinator extends Combinator {

    public AdjacentSiblingCombinator(SimpleSelector firstSelector, Selector secondSelector) {
        super(firstSelector, secondSelector);
    }

    @Nonnull
    @Override
    public String toString() {
        return firstSelector + " + " + secondSelector;
    }

    @Override
    public <T> T match(@Nonnull SelectorModel<T> model, T element) {
        T result = secondSelector.match(model, element);
        if (result != null) {
            result = firstSelector.match(model, model.getPreviousSibling(result));
        }
        return result;
    }

    @Override
    public int getSpecificity() {
        return firstSelector.getSpecificity() + secondSelector.getSpecificity();
    }

    @Override
    public void produceTokens(Consumer<CssToken> consumer) {
        firstSelector.produceTokens(consumer);
        consumer.accept(new CssToken(CssTokenType.TT_PLUS));
        secondSelector.produceTokens(consumer);
    }
}
