package net.honeyflower.virtualbox.client.model;

import org.virtualbox_6_0.IVRDEServerInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RDPConnection {
	private boolean active;
	private int port;

	public static RDPConnection fromVRDPServer(IVRDEServerInfo info) {
		if (info==null) return null;
		RDPConnection rdpConnection = RDPConnection.builder()
		.active(info.getActive())
		.port(info.getPort())
		.build();
		
		return rdpConnection;
	}
	
}
