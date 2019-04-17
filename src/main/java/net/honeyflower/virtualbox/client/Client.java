package net.honeyflower.virtualbox.client;

import java.util.List;

import org.virtualbox_6_0.IMachine;
import org.virtualbox_6_0.VBoxException;

public class Client {
	
	private ClientInternal client;
	
	public Client(String username, String password, String url) {
		client = new ClientInternal(username, password, url);
	}
	
	public boolean startVM(String name) throws VBoxException {
		IMachine vm = client.findVM(name);
		return client.startVM(vm);
	}
	
	public boolean stopVM(String name) throws VBoxException {
		return client.stopVM(client.findVM(name));
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
	
	public boolean deleteSnapshot(String vmName, String snapshotName) {
		IMachine vm = client.findVM(vmName);
		//if (!client.stopVM(vm)) return false;
		return client.deleteSnapshot(client.findVM(vmName), client.findSnapshot(vm, snapshotName));
	}

	public void diconnect() {
		client.diconnect();
	}

}
