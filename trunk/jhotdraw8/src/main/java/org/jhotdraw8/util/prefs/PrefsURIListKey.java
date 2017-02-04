/* @(#)PrefsURIListKey.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */

package org.jhotdraw8.util.prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * PrefsURIListKey. The words are separated by tab character.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class PrefsURIListKey {
    private final String key;
    private final List<String> defaultValue;


    public PrefsURIListKey(String key, List<String>  defaultValue) {
        this.key = key;
        this.defaultValue = Collections.unmodifiableList(new ArrayList<>(defaultValue));

    }

    public List<String> get(Preferences prefs) {
        return defaultValue;
    }

    public void put(Preferences prefs, int newValue) {
    }
}
