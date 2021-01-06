/*
 * Copyright (c) 1994, 2004, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.lixh.rxhttp


import java.util.*

/**
 * This class represents an observable object, or "data"
 * in the com.angel.recycling.desewang.model-view paradigm. It can be subclassed to represent an
 * object that the application wants to have observed.
 *
 *
 * An observable object can have one or more observers. An observer
 * may be any object that implements interface <tt>Observer</tt>. After an
 * observable instance changes, an application calling the
 * `Observable`'s `notifyObservers` method
 * causes all of its observers to be notified of the change by a call
 * to their `update` method.
 *
 *
 * The order in which notifications will be delivered is unspecified.
 * The default implementation provided in the Observable class will
 * notify Observers in the order in which they registered interest, but
 * subclasses may change this order, use no guaranteed order, deliver
 * notifications on separate threads, or may guarantee that their
 * subclass follows this order, as they choose.
 *
 *
 * Note that this notification mechanism is has nothing to do with threads
 * and is completely separate from the <tt>wait</tt> and <tt>notify</tt>
 * mechanism of class <tt>Object</tt>.
 *
 *
 * When an observable object is newly created, its set of observers is
 * empty. Two observers are considered the same if and only if the
 * <tt>equals</tt> method returns true for them.
 *
 * @author Chris Warth
 * @see java.util.Observable.notifyObservers
 * @see java.util.Observable.notifyObservers
 * @see java.util.Observer
 *
 * @see java.util.Observer.update
 * @since JDK1.0
 */
open class Observable {
    private var changed = false
    private val obs: Vector<Observer<*>> = Vector()

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Synchronized
    fun  addObserver(o: Observer<*>?) {
        if (o == null)
            throw NullPointerException()
        if (!obs.contains(o)) {
            obs.addElement(o)
        }
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    @Synchronized
    fun deleteObserver(o: Observer<*>) {
        obs.removeElement(o)
    }

    /**
     * If this object has changed, as indicated by the
     * `hasChanged` method, then notify all of its observers
     * and then call the `clearChanged` method to
     * indicate that this object has no longer changed.
     *
     *
     * Each observer has its `update` method called with two
     * arguments: this observable object and `null`. In other
     * words, this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see Observable.clearChanged
     * @see Observable.hasChanged
     * @see Observer.update
     */
    fun notifyObservers() {
        notifyObservers(null)
    }

    /**
     * If this object has changed, as indicated by the
     * `hasChanged` method, then notify all of its observers
     * and then call the `clearChanged` method to indicate
     * that this object has no longer changed.
     *
     *
     * Each observer has its `update` method called with two
     * arguments: this observable object and the `arg` argument.
     *
     * @param arg any object.
     * @see Observable.clearChanged
     * @see Observable.hasChanged
     * @see Observer.update
     */
    open fun notifyObservers(arg: Any?) {
        val arrLocal: Array<Any>?
        synchronized(this) {
            if (!hasChanged())
                return
            arrLocal = obs.toTypedArray()
            clearChanged()
        }

        for (i in arrLocal!!.indices.reversed())
            (arrLocal[i] as Observer<*>).update(this, arg as Nothing)
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    @Synchronized
    fun deleteObservers() {
        obs.removeAllElements()
    }

    /**
     * Marks this <tt>Observable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     */
    @Synchronized
    protected fun setChanged() {
        changed = true
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the <tt>hasChanged</tt> method will now return <tt>false</tt>.
     * This method is called automatically by the
     * `notifyObservers` methods.
     *
     * @see Observable.notifyObservers
     * @see Observable.notifyObservers
     */
    @Synchronized
    protected fun clearChanged() {
        changed = false
    }

    /**
     * Tests if this object has changed.
     *
     * @return `true` if and only if the `setChanged`
     * method has been called more recently than the
     * `clearChanged` method on this object;
     * `false` otherwise.
     * @see Observable.clearChanged
     * @see Observable.setChanged
     */
    @Synchronized
    fun hasChanged(): Boolean {
        return changed
    }

    /**
     * Returns the number of observers of this <tt>Observable</tt> object.
     *
     * @return the number of observers of this object.
     */
    @Synchronized
    fun countObservers(): Int {
        return obs.size
    }
}
