package net.honeyflower.virtualbox.client.model;

import org.virtualbox_6_0.IVRDEServer;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VMInfo {
	
	private String name;
    private long ram;
    private boolean hwvirtEnabled;
    private String os;
    private IVRDEServer rdp;

}
