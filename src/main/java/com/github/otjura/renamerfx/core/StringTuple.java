/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package com.github.otjura.renamerfx.core;

/**
 * Tuple class to contain two strings.
 *
 * @param string1 String 1.
 * @param string2 String 2.
 */
public record StringTuple(String string1, String string2) {

	@Override
	public String toString() {
		return "Renamed " + string1 + " to " + string2;
	}
}
