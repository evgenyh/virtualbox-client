package net.honeyflower.virtualbox.client.model;

import org.virtualbox_6_0.MachineState;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class VMInfo {
	
	private String name;
    private long memorySize;
    private long cpuCount;
    private boolean accessible;
    private boolean autostartEnabled;
    private Snapshot currentSnapshot;
    private String ostypeId;
    private RDPConnection rdp;
    private MachineState state;
}
