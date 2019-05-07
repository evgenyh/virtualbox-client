package net.honeyflower.virtualbox.client.internal;

import java.net.ConnectException;
import java.util.Timer;
import java.util.TimerTask;

import lombok.extern.slf4j.Slf4j;
import net.honeyflower.virtualbox.client.Client;

@Slf4j
public class ReconnectTask extends TimerTask {
	
	private Client client;
	private Timer timer;
	private int iteration;

	public ReconnectTask(Client client, Timer timer) {
		this.client = client;
		this.timer = timer;
	}

	@Override
	public void run() {
		if (client.isConnected()) return;
		
		log.info("reconnection attempt {}", iteration);
		
		try {
			client.reconnect();
		} catch (Exception e) {
			log.warn("got error on reconnect : {}", e.getMessage());
			if (e.getCause()!=null) {
				if (e.getCause() instanceof ConnectException) {
					int delay =iteration*2000;
					if (delay>60000) delay=60000;
					log.warn("got {}, scheduling reconnection in {} secs", e.getMessage(), delay/1000);
					timer.schedule(this, delay);
				}
			}
		} finally {
			iteration++;
		}

	}

	public void start() {
		this.timer.schedule(this, 1000);
		
	}

}
