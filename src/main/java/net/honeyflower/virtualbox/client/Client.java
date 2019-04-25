package net.honeyflower.virtualbox.client;

import java.util.ArrayList;
import java.util.List;

import org.virtualbox_6_0.IMachine;
import org.virtualbox_6_0.VBoxException;

import net.honeyflower.virtualbox.client.constants.SystemPropertyKey;
import net.honeyflower.virtualbox.client.constants.VMPropertyKey;
import net.honeyflower.virtualbox.client.model.NetworkCard;
import net.honeyflower.virtualbox.client.model.RDPConnection;
import net.honeyflower.virtualbox.client.model.Snapshot;
import net.honeyflower.virtualbox.client.model.VMInfo;

public class Client {
	
	private ClientInternal client;
	
	public Client(String username, String password, String url) {
		if (username == null || password==null || url == null) throw new IllegalArgumentException("all parameters should not be null");
		client = new ClientInternal(username, password, url);
	}
	
	protected String importVMByConfig(String configPath) {
		return client.importVMByConfig(configPath);
	}
	
	public String importVM(String name) {
		return client.importVM(name);
	}
	
	public boolean startVM(String name) throws VBoxException {
		IMachine vm = client.findVM(name);
		return client.startVM(vm);
	}
	
	public boolean stopVM(String name) throws VBoxException {
		return client.stopVM(client.findVM(name));
	}
	
	/**
	 * deletes vm defined by name and cleanups all non removable mediums
	 * @param name
	 * @return true if successfully deleted or false otherwise
	 */
	public boolean deleteVM(String name) {
		return client.deleteVM(client.findVM(name));
	}
	
	public boolean createSnapshotVM(String vmName, String snapshotName, String desciption) throws VBoxException {
		return client.createSnapshotVM(client.findVM(vmName), snapshotName, desciption);
	}
	
	public List<String> getVMSnapshots(String vmName) throws VBoxException {
		return client.getVMSnapshots(client.findVM(vmName));
	}
	
	public boolean restoreSnapshot(String vmName, String snapshotName) {
		IMachine vm = client.findVM(vmName);
		//if (!client.stopVM(vm)) return false;
		return client.restoreSnapshot(client.findVM(vmName), client.findSnapshot(vm, snapshotName));
	}
	
	public VMInfo getVMInfo(String vmName) {
		IMachine vm = client.findVM(vmName);
		
		int maxNics = (int) client.getVMProperty(VMPropertyKey.MAX_VM_NETWORK_ADAPTERS, vm, null);
		if (maxNics>10) maxNics=10;
		List<NetworkCard> cards = new ArrayList<NetworkCard>(maxNics);
		for (int x = 0; x< maxNics;x++) {
			NetworkCard card = NetworkCard.fromINetworkAdapter(vm.getNetworkAdapter((long)x));
			if (card==null) continue;
			card.setIp((String) client.getVMProperty(VMPropertyKey.GUEST_NIC_IP, vm, x));
			card.setMask((String) client.getVMProperty(VMPropertyKey.GUEST_NIC_MASK, vm, x));
			cards.add(card);
		}
		
		return VMInfo.builder()
				.id(vm.getId())
				.accessible(vm.getAccessible())
				.rdp(RDPConnection.fromVRDPServer(client.getRDPinfo(vm)))
				.currentSnapshot(Snapshot.fromISnapshot(vm.getCurrentSnapshot()))
				.autostartEnabled(vm.getAutostartEnabled())
				.memorySize(vm.getMemorySize())
				.name(vm.getName())
				.cpuCount(vm.getCPUCount())
				.state(vm.getState())
				.nics(cards)
				.build();
	}
	
	public boolean deleteSnapshot(String vmName, String snapshotName) {
		IMachine vm = client.findVM(vmName);
		//if (!client.stopVM(vm)) return false;
		return client.deleteSnapshot(client.findVM(vmName), client.findSnapshot(vm, snapshotName));
	}

	public void diconnect() {
		client.diconnect();
	}
	
	public String getProperty(SystemPropertyKey key) {
		return client.getProperty(key);
	}

}
