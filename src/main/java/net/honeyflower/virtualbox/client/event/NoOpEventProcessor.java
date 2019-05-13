package net.honeyflower.virtualbox.client.event;

import org.virtualbox_6_0.IEvent;

/**
 * this implementation silently discards all events
 * @author Eugene
 *
 */
public class NoOpEventProcessor implements EventProcessor {

	@Override
	public void processEvent(IEvent ev) {
		// TODO Auto-generated method stub

	}

}
