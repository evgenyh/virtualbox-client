package net.honeyflower.virtualbox.client;

import org.virtualbox_6_0.IEvent;
import org.virtualbox_6_0.IMachineStateChangedEvent;
import org.virtualbox_6_0.VBoxEventType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class VMEventProcessor {
	
	public void processEvent(IEvent ev) {
        log.debug("got event: {} , of type {}", ev, ev.getType());
        VBoxEventType type = ev.getType();
        switch (type)
        {
            case OnMachineStateChanged:
            {
                IMachineStateChangedEvent mcse = IMachineStateChangedEvent.queryInterface(ev);
                if (mcse == null)
                    log.warn("Cannot query an interface");
                else
                    log.info("mid=" + mcse.getMachineId());
                break;
            }
        }
    }

}
