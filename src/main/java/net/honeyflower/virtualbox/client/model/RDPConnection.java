package net.honeyflower.virtualbox.client.model;

import org.virtualbox_6_0.IVRDEServer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RDPConnection {
	private boolean enabled;

	public static RDPConnection fromVRDPServer(IVRDEServer vrdeServer) {
		RDPConnection rdpConnection = RDPConnection.builder()
		.enabled(vrdeServer.getEnabled())
		.build();
		
		return rdpConnection;
	}
	
}
