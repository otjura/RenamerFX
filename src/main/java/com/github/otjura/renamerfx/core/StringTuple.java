package com.github.otjura.renamerfx.core;

import lombok.Getter;

/**
 * Tuple class to contain two strings.
 */
public class StringTuple
{
	@Getter
	private final String string1;

	@Getter
	private final String string2;

	/**
	 * Constructor.
	 *
	 * @param string1
	 * 	String 1.
	 * @param string2
	 * 	String 2.
	 */
	public StringTuple(String string1, String string2)
	{
		this.string1 = string1;
		this.string2 = string2;
	}

	@Override
	public String toString()
	{
		return "Renamed " + string1 + " to " + string2;
	}
}
