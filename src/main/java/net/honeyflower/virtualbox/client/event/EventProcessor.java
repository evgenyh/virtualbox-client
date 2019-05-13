package net.honeyflower.virtualbox.client.event;

import org.virtualbox_6_0.IEvent;

public interface EventProcessor {

	void processEvent(IEvent ev);

}