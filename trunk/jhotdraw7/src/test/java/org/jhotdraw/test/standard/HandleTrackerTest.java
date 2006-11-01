/*
 * @(#)Test.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	� by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.test.standard;

import java.awt.Point;

// JUnitDoclet begin import
import org.jhotdraw.draw.Handle;
import org.jhotdraw.draw.HandleTracker;
import org.jhotdraw.draw.NullHandle;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.RelativeLocator;
import org.jhotdraw.test.JHDTestCase;
// JUnitDoclet end import

/*
 * Generated by JUnitDoclet, a tool provided by
 * ObjectFab GmbH under LGPL.
 * Please see www.junitdoclet.org, www.gnu.org
 * and www.objectfab.de for informations about
 * the tool, the licence and the authors.
 */

// JUnitDoclet begin javadoc_class
/**
 * TestCase HandleTrackerTest is generated by
 * JUnitDoclet to hold the tests for HandleTracker.
 * @see org.jhotdraw.draw.HandleTracker
 */
// JUnitDoclet end javadoc_class
public class HandleTrackerTest
// JUnitDoclet begin extends_implements
extends JHDTestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private HandleTracker handletracker;
	// JUnitDoclet end class

	/**
	 * Constructor HandleTrackerTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public HandleTrackerTest(String name) {
		// JUnitDoclet begin method HandleTrackerTest
		super(name);
		// JUnitDoclet end method HandleTrackerTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public HandleTracker createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		Handle handle = new NullHandle(new RectangleFigure(new Point(10, 10), new Point(40, 40)), RelativeLocator.north());
		return new HandleTracker(getDrawingEditor(), handle);
		// JUnitDoclet end method testcase.createInstance
	}

	/**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
	protected void setUp() throws Exception {
		// JUnitDoclet begin method testcase.setUp
		super.setUp();
		handletracker = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		handletracker = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method mouseDown()
	/**
	 * Method testMouseDown is testing mouseDown
	 * @see org.jhotdraw.draw.HandleTracker#mouseDown(java.awt.event.MouseEvent, int, int)
	 */
	// JUnitDoclet end javadoc_method mouseDown()
	public void testMouseDown() throws Exception {
		// JUnitDoclet begin method mouseDown
		// JUnitDoclet end method mouseDown
	}

	// JUnitDoclet begin javadoc_method mouseDrag()
	/**
	 * Method testMouseDrag is testing mouseDrag
	 * @see org.jhotdraw.draw.HandleTracker#mouseDrag(java.awt.event.MouseEvent, int, int)
	 */
	// JUnitDoclet end javadoc_method mouseDrag()
	public void testMouseDrag() throws Exception {
		// JUnitDoclet begin method mouseDrag
		// JUnitDoclet end method mouseDrag
	}

	// JUnitDoclet begin javadoc_method mouseUp()
	/**
	 * Method testMouseUp is testing mouseUp
	 * @see org.jhotdraw.draw.HandleTracker#mouseUp(java.awt.event.MouseEvent, int, int)
	 */
	// JUnitDoclet end javadoc_method mouseUp()
	public void testMouseUp() throws Exception {
		// JUnitDoclet begin method mouseUp
		// JUnitDoclet end method mouseUp
	}

	// JUnitDoclet begin javadoc_method activate()
	/**
	 * Method testActivate is testing activate
	 * @see org.jhotdraw.draw.HandleTracker#activate()
	 */
	// JUnitDoclet end javadoc_method activate()
	public void testActivate() throws Exception {
		// JUnitDoclet begin method activate
		// JUnitDoclet end method activate
	}

	// JUnitDoclet begin javadoc_method testVault
	/**
	 * JUnitDoclet moves marker to this method, if there is not match
	 * for them in the regenerated code and if the marker is not empty.
	 * This way, no test gets lost when regenerating after renaming.
	 * <b>Method testVault is supposed to be empty.</b>
	 */
	// JUnitDoclet end javadoc_method testVault
	public void testVault() throws Exception {
		// JUnitDoclet begin method testcase.testVault
		// JUnitDoclet end method testcase.testVault
	}

}
