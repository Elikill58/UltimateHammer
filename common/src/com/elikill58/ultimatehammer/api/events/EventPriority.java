package com.elikill58.ultimatehammer.api.events;

public enum EventPriority {

	/**
	 * The first event called.
	 */
	PRE,
	/**
	 * Default value. Event called after {@link EventPriority#PRE} and before {@link EventPriority#POST}
	 */
	BASIC,
	/**
	 * Last event called.
	 */
	POST;
	
}
