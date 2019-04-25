package net.honeyflower.virtualbox.client.model;

import org.virtualbox_6_0.INetworkAdapter;
import org.virtualbox_6_0.NetworkAdapterType;
import org.virtualbox_6_0.NetworkAttachmentType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NetworkCard {
	
	private Boolean cableConnected;
	private String mac;
	private NetworkAttachmentType attachmentType;
	private NetworkAdapterType type;
	private String ip;
	private String mask;
	
	public static NetworkCard fromINetworkAdapter(INetworkAdapter adapter) {
		if (adapter==null || !adapter.getEnabled()) return null;
		NetworkCard card = builder()
				.attachmentType(adapter.getAttachmentType())
				.cableConnected(adapter.getCableConnected())
				.mac(adapter.getMACAddress())
				.type(adapter.getAdapterType())
				.build();
		return card;
	}

	

}
