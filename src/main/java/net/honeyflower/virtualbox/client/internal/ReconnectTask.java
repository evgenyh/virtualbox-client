package net.honeyflower.virtualbox.client.internal;

import java.util.Timer;
import java.util.TimerTask;

import javax.xml.ws.WebServiceException;

import lombok.extern.slf4j.Slf4j;
import net.honeyflower.virtualbox.client.Client;

@Slf4j
public class ReconnectTask extends TimerTask {
	
	private Client client;
	private Timer timer;
	private int iteration;

	public ReconnectTask(Client client, Timer timer) {
		this(client, timer, 1);
	}
	
	private ReconnectTask(Client client, Timer timer, int iteration) {
		this.client = client;
		this.timer = timer;
		this.iteration=iteration;
	}

	@Override
	public void run() {
		if (client.isConnected()) {
			timer.cancel();
			return;
		}
		
		log.info("reconnection attempt {}", iteration);
		
		try {
			client.reconnect();
		} catch (Exception e) {
			log.warn("got error on reconnect : {}", e.getMessage());
			if (e.getCause()!=null) {
				if (e.getCause() instanceof WebServiceException) {
					log.warn("got {}, will schedule reconnection", e.getCause().getMessage());
				}
			}
		} finally {
			if (client.isConnected()) {
				log.info("connection established, cancelling tasks");
				timer.cancel();
			}
			else {
				iteration++;
				int delay =iteration*iteration;
				if (delay>60) delay=60;
				log.warn("connection unsuccesfull, scheduling reconnection in {} secs", delay);
				timer.schedule(this.cloned(), delay*1000);
			}
		}

	}
	
	/**
	 * since {@link TimerTask} is a once shot piece, we do clone it for each reschedule attempt
	 * @return
	 */
	private ReconnectTask cloned() {
		return new ReconnectTask(client, timer, iteration);
	}

	public void start() {
		this.timer.schedule(this, 1000);
		
	}

}
