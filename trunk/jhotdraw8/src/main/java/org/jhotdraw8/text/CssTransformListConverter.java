/* @(#)CssTransformListConverter.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.text;

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.draw.io.IdFactory;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import org.jhotdraw8.css.CssTokenizerInterface;

/**
 * CssTransformListConverter.
 * <p>
 * Parses a transform list given in the following EBNF:
 * <pre>
 * TransformList = [ Transform { S, Transform } ] ;
 * Transform     = ( Affine | Translate | Scale | Rotate | Shear ) ;
 *
 * Affine        = "affine(" , [S] ,
 *                  ( mxx ,  5 * ( C , m )
 *                  | mxx , 11 * ( C , m )
 *                  ) , [S], ")" ;
 * Translate     = "translate(" , [S] , tx , [ C , ty, [ C , tz ] ] , [S], ")" ;
 * Scale         = "scale(" , [S] ,
 *                 ( sx , [ C , sy, [ C , Pivot2D ] ]
 *                 | sx , C , sy, C , sz, [ C , Pivot ]
 *                 ) , [S], ")" ;
 * Rotate        = "rotate(" , [S] ,
 *                  rotate-angle , [ C , Pivot , [ C, Axis ] , [S], ")" ;
 *
 * Shear         = "shear(" , [S] , x , Sep , y , [ Sep , Pivot2D ], [S], ")" ;
 *
 * Axis          =  axisX, Sep , axisY, Sep , axisZ ;
 * Pivot         =  pivotX, Sep , pivotY, [ Sep , pivotZ ] ;
 * Pivot2D       =  pivotX, Sep , pivotY] ;
 *
 * C             = ( S , { S } | { S } , "," , { S } ) ;
 * S             = (* white space *)
 * </pre>
 * <p>
 * References:
 * <a href="http://www.w3.org/TR/2008/REC-SVGTiny12-20081222/coords.html#TransformAttribute">
 * SVG Tiny 1.2, The 'transform' attribute.
 * </a>
 *
 * @author Werner Randelshofer
 * @version $Id: CssTransformListConverter.java 1120 2016-01-15 17:37:49Z
 * rawcoder $
 */
public class CssTransformListConverter implements Converter<List<Transform>> {

    private final CssSizeConverter nb = new CssSizeConverter();

    @Override
    public void toString(Appendable buf, IdFactory idFactory, List<Transform> txs) throws IOException {
        boolean allTransformsAreIdentity = true;
        for (Transform tx : txs) {
            if (! tx.isIdentity()) {
                allTransformsAreIdentity = false;
                break;
            }
        }
        if (allTransformsAreIdentity) {
            buf.append("none");
            return;
        }

        boolean first = true;
        for (Transform tx : txs) {
            if (!tx.isIdentity()) {
                if (first) {
                    first = false;
                } else {
                    buf.append(' ');
                }
                if (tx instanceof Translate) {
                    Translate tr = (Translate) tx;
                    buf.append("translate(")
                            .append(nb.toString(tr.getTx()));
                    if (tr.getTy() != 0.0 || tr.getTz() != 0.0) {
                        buf.append(',')
                                .append(nb.toString(tr.getTy()));
                    }
                    if (tr.getTz() != 0.0) {
                        buf.append(',')
                                .append(nb.toString(tr.getTz()));
                    }
                    buf.append(')');
                } else if (tx instanceof Scale) {
                    Scale ts = (Scale) tx;
                    buf.append("scale(")
                            .append(nb.toString(ts.getX()));
                    if (ts.getY() != ts.getX() || ts.getZ() != 1 || ts.getPivotX() != 0 || ts.getPivotY() != 0) {
                        buf.append(' ')
                                .append(nb.toString(ts.getY()));
                    }
                    if (ts.getZ() != 1) {
                        buf.append(' ')
                                .append(nb.toString(ts.getZ()));
                        if (ts.getPivotX() != 0 || ts.getPivotY() != 0 || ts.getPivotZ() != 0) {
                            buf.append(", ")
                                    .append(nb.toString(ts.getPivotX()))
                                    .append(' ')
                                    .append(nb.toString(ts.getPivotY()))
                                    .append(' ')
                                    .append(nb.toString(ts.getPivotZ()));
                        }
                    } else if (ts.getPivotX() != 0 || ts.getPivotY() != 0) {
                        buf.append(", ")
                                .append(nb.toString(ts.getPivotX()))
                                .append(' ')
                                .append(nb.toString(ts.getPivotY()));
                    }
                    buf.append(')');

                } else if (tx instanceof Rotate) {
                    Rotate tr = (Rotate) tx;
                    buf.append("rotate(")
                            .append(nb.toString(tr.getAngle()));
                    if (tr.getPivotX() != 0.0 || tr.getPivotY() != 0.0 || tr.getPivotZ() != 0.0
                            || !tr.getAxis().equals(Rotate.Z_AXIS)) {
                        buf.append(' ')
                                .append(nb.toString(tr.getPivotX()))
                                .append(',')
                                .append(nb.toString(tr.getPivotY()));
                    }
                    if (tr.getPivotZ() != 0.0) {
                        buf.append(',')
                                .append(nb.toString(tr.getPivotZ()));
                    }
                    if (!tr.getAxis().equals(Rotate.Z_AXIS)) {
                        Point3D a = tr.getAxis();
                        buf.append(' ')
                                .append(nb.toString(a.getX()))
                                .append(',')
                                .append(nb.toString(a.getY()))
                                .append(',')
                                .append(nb.toString(a.getZ()));
                    }
                    buf.append(')');
                } else if (tx instanceof Shear) {
                    Shear tr = (Shear) tx;
                    buf.append("shear(")
                            .append(nb.toString(tr.getX()))
                            .append(',')
                            .append(nb.toString(tr.getY()));
                    if (tr.getPivotX() != 0.0 || tr.getPivotY() != 0.0) {
                        buf.append(' ')
                                .append(nb.toString(tr.getPivotX()))
                                .append(',')
                                .append(nb.toString(tr.getPivotY()));
                    }
                    buf.append(')');
                } else if (tx.isType2D()) {
                    buf.append("affine(")
                            .append(nb.toString(tx.getMxx()))
                            .append(',')
                            .append(nb.toString(tx.getMxy()))
                            .append(',')
                            .append(nb.toString(tx.getTx()))
                            .append(' ')
                            .append(nb.toString(tx.getMyx()))
                            .append(',')
                            .append(nb.toString(tx.getMyy()))
                            .append(',')
                            .append(nb.toString(tx.getTy()))
                            .append(')');
                } else {
                    buf.append("affine(")
                            .append(nb.toString(tx.getMxx()))
                            .append(',')
                            .append(nb.toString(tx.getMxy()))
                            .append(',')
                            .append(nb.toString(tx.getMzx()))
                            .append(',')
                            .append(nb.toString(tx.getTx()))
                            .append(' ')
                            .append(nb.toString(tx.getMyx()))
                            .append(',')
                            .append(nb.toString(tx.getMyy()))
                            .append(',')
                            .append(nb.toString(tx.getMyz()))
                            .append(',')
                            .append(nb.toString(tx.getTy()))
                            .append(' ')
                            .append(nb.toString(tx.getMzx()))
                            .append(',')
                            .append(nb.toString(tx.getMzy()))
                            .append(',')
                            .append(nb.toString(tx.getMzz()))
                            .append(',')
                            .append(nb.toString(tx.getTz()))
                            .append(')');
                }
            }
        }
    }

    @Override
    public List<Transform> fromString(CharBuffer in, IdFactory idFactory) throws ParseException, IOException {
        List<Transform> txs = new ArrayList<>();
        CssTokenizerInterface tt = new CssTokenizer(new StringReader(in.toString()));
tt.setSkipWhitespace(true);
        if (tt.nextToken() == CssTokenizer.TT_IDENT && tt.currentStringValue().equals("none")) {
            in.position(in.limit());
            return txs;
        } else {
            tt.pushBack();
        }

        while (tt.nextToken() != CssTokenizer.TT_EOF) {
            tt.pushBack();
            if (tt.nextToken() != CssTokenizer.TT_FUNCTION) {
                throw new ParseException("function expected: \"" + tt.currentStringValue() + "\"", tt.getPosition());
            }
            String func = tt.currentStringValue();
            int funcPos = tt.getPosition();
            List<Double> m = new ArrayList<>();
            if (tt.nextToken() != CssTokenizer.TT_NUMBER) {
                throw new ParseException("coefficient nb 1 expected: \"" + tt.currentStringValue() + "\"", tt.getPosition());
            }
            m.add(tt.currentNumericValue().doubleValue());
            while (tt.nextToken() != ')' && tt.currentToken() != CssTokenizer.TT_EOF) {
                if (tt.nextToken() != ',') {
                    tt.pushBack();
                }
                if (tt.nextToken() != CssTokenizer.TT_NUMBER) {
                    throw new ParseException("coefficient nb " + m.size() + " expected: \"" + tt.currentStringValue() + "\"", tt.getPosition());
                }
                m.add(tt.currentNumericValue().doubleValue());
            }
            if (tt.currentToken() != ')') {
                throw new ParseException("')' expected: \"" + tt.currentStringValue() + "\"", tt.getPosition());
            }
            switch (func) {
                case "affine": {
                    switch (m.size()) {
                        case 6:
                            txs.add(new Affine(//
                                    m.get(0), m.get(1), m.get(2),//
                                    m.get(3), m.get(4), m.get(5)//
                            ));
                            break;
                        case 12:
                            txs.add(new Affine(//
                                    m.get(0), m.get(1), m.get(2), m.get(3),//
                                    m.get(4), m.get(5), m.get(5), m.get(6),//
                                    m.get(7), m.get(8), m.get(9), m.get(10)//
                            ));
                            break;
                        default:
                            throw new ParseException("6 or 12 coefficients expected, but found " + m.size(), tt.getPosition());
                    }
                    break;
                }
                case "skew": 
                case "shear": {
                    switch (m.size()) {
                        case 2:
                            txs.add(Transform.shear(m.get(0), m.get(1)));
                            break;
                        case 4:
                            txs.add(Transform.shear(
                                    m.get(0), m.get(1), m.get(2), m.get(3)//
                            ));
                            break;
                        default:
                            throw new ParseException("2 or 4 coefficients expected, but found " + m.size(), tt.getPosition());
                    }
                    break;
                }
                case "translate": {
                    switch (m.size()) {
                        case 1:
                            txs.add(Transform.translate(
                                    m.get(0), 0//
                            ));
                            break;
                        case 2:
                            txs.add(Transform.translate(
                                    m.get(0), m.get(1)//
                            ));
                            break;
                        case 3:
                            txs.add(new Translate(
                                    m.get(0), m.get(1), m.get(2)//
                            ));
                            break;
                        default:
                            throw new ParseException("1, 2 or 3 coefficients expected, but found " + m.size(), tt.getPosition());
                    }
                    break;
                }
                case "scale": {
                    switch (m.size()) {
                        case 1:
                            txs.add(Transform.scale(//
                                    m.get(0), m.get(0)//
                            ));
                            break;
                        case 2:
                            txs.add(Transform.scale(
                                    m.get(0), m.get(1)//
                            ));
                            break;
                        case 3:
                            txs.add(new Scale(
                                    m.get(0), m.get(0), m.get(1), m.get(2)//
                            ));
                            break;
                        case 4:
                            txs.add(Transform.scale(
                                    m.get(0), m.get(1), m.get(2), m.get(3)//
                            ));
                            break;
                        case 6:
                            txs.add(new Scale(
                                    m.get(0), m.get(1), m.get(2),//
                                    m.get(3), m.get(4), m.get(5)//

                            ));
                            break;
                        default:
                            throw new ParseException("1, 2, 3, 4, or 6 coefficients expected, but found " + m.size(), tt.getPosition());
                    }
                    break;
                }
                case "rotate": {
                    switch (m.size()) {
                        case 1:
                            txs.add(Transform.rotate(//
                                    m.get(0),//
                                    0, 0//
                            ));
                            break;
                        case 3:
                            txs.add(Transform.rotate(//
                                    m.get(0),//
                                    m.get(1), m.get(2)//
                            ));
                            break;
                        case 4:
                            txs.add(new Rotate(//
                                    m.get(0),//
                                    m.get(1), m.get(2), m.get(3)//
                            ));
                            break;
                        case 6:
                            txs.add(new Rotate(//
                                    m.get(0),//
                                    m.get(1), m.get(2), 0,//
                                    new Point3D(m.get(3), m.get(4), m.get(5))//
                            ));
                            break;
                        case 7:
                            txs.add(new Rotate(//
                                    m.get(0),//
                                    m.get(1), m.get(2), m.get(3),//
                                    new Point3D(m.get(4), m.get(5), m.get(6))//
                            ));
                            break;
                        default:
                            throw new ParseException("1, 3, 4, 6 or 7 coefficients expected, but found " + m.size(), tt.getPosition());
                    }
                    break;
                }
                default:
                    throw new ParseException("unsupported function: \"" + func + "\"", funcPos);
            }
        }

        in.position(in.limit());
        return txs;
    }

    @Override
    public List<Transform> getDefaultValue() {
        return Collections.emptyList();
    }
}
