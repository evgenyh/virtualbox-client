package net.honeyflower.virtualbox.client;

import org.virtualbox_6_0.IEvent;
import org.virtualbox_6_0.IEventListener;
import org.virtualbox_6_0.IEventSource;
import org.virtualbox_6_0.VBoxException;
import org.virtualbox_6_0.jaxws.InvalidObjectFaultMsg;
import org.virtualbox_6_0.jaxws.RuntimeFaultMsg;

import lombok.extern.slf4j.Slf4j;
import net.honeyflower.virtualbox.client.event.EventProcessor;
import net.honeyflower.virtualbox.client.event.NoOpEventProcessor;

@Slf4j
class VMEventListener extends Thread {
	
	private static final String _THREAD_NAME="vbox_event_processor";
	private IEventListener listener;
	private IEventSource source;
	private boolean running = true;
	private EventProcessor eventProcessor;
	
	public VMEventListener (IEventSource source, IEventListener listener) {
		this(source, listener, null);
	}
	
	public VMEventListener(IEventSource source, IEventListener listener, EventProcessor eventProcessor) {
		this.setName(_THREAD_NAME);
		this.listener=listener;
		this.source=source;
		setEventProcessor(eventProcessor);
	}

	@Override
	public void run() {
		while (running) {
			try {
				IEvent ev = source.getEvent(listener, 500);
	            if (ev != null) {
	                processEvent(ev);
	                source.eventProcessed(listener, ev);
	            }
			} catch (VBoxException e) {
				try {
					throw e.getCause();
				} catch (InvalidObjectFaultMsg ei) {
					log.warn("got: " + ei.getMessage(), ei);
					source.releaseRemote();
					
				} catch (RuntimeFaultMsg rf) {
					log.warn("got RuntimeFaultMsg: {}, will stop listener", rf.getMessage());
					running=false;
					
				} catch (Throwable th) {
					log.error("caught throwable, will stop listener", e);
					running=false;
				}
			}
			
			
		}
	}

	/*private void processEvent(IEvent ev) {
        log.debug("got event: {} , of type {}", ev, ev.getType());
        VBoxEventType type = ev.getType();
        switch (type) {
            case OnMachineStateChanged:
                IMachineStateChangedEvent mcse = IMachineStateChangedEvent.queryInterface(ev);
                if (mcse == null)
                    log.warn("Cannot query an interface");
                else
                    log.info("mid=" + mcse.getMachineId());
                break;
            default:
    			break;
        }
    }*/

	private void processEvent(IEvent ev) {
		eventProcessor.processEvent(ev);
		
	}

	public void setEventProcessor(EventProcessor eventProcessor) {
		if (eventProcessor==null) eventProcessor=new NoOpEventProcessor();
		this.eventProcessor = eventProcessor;
	}
	

}
