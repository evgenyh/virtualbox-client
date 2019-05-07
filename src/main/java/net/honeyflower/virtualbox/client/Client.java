package net.honeyflower.virtualbox.client;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.stream.Collectors;

import org.virtualbox_6_0.IMachine;
import org.virtualbox_6_0.VBoxException;

import lombok.extern.slf4j.Slf4j;
import net.honeyflower.virtualbox.client.constants.SystemPropertyKey;
import net.honeyflower.virtualbox.client.internal.ReconnectTask;
import net.honeyflower.virtualbox.client.model.VMInfo;

@Slf4j
public class Client {
	
	private ClientInternal client;
	
	public Client(String username, String password, String url) {
		this(username, password, url, false);
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param url
	 * @param reconnect will try to reconnect if connection was not successful
	 */
	public Client(String username, String password, String url, boolean reconnect) {
		if (username == null || password==null || url == null) throw new IllegalArgumentException("all parameters should not be null");
		client = new ClientInternal(username, password, url);
		try {
			client.init();
		} catch (Exception e) {
			if (reconnect) {
				log.warn("connection attempt was unsuccesfull, we got : {},  scheduling reconnection", e.getMessage());
				new ReconnectTask(this, new Timer("vbox_reconnector_" + System.currentTimeMillis())).start();
			} else {
				throw e;
			}
		}
		
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
		
		return client.getVMInfo(vm);
	}
	
	
	public List<VMInfo> getVMs() {
		
		return client.getVMs()
				.stream()
				.filter(Objects::nonNull)
				.map(client::getVMInfo)
				.collect(Collectors.toList());
	}
	
	public boolean deleteSnapshot(String vmName, String snapshotName) {
		IMachine vm = client.findVM(vmName);
		//if (!client.stopVM(vm)) return false;
		return client.deleteSnapshot(client.findVM(vmName), client.findSnapshot(vm, snapshotName));
	}

	public void diconnect() {
		client.diconnect();
	}
	
	public void connect() {
		reconnect();
	}
	
	public void reconnect() {
		client.init();
	}
	
	public String getProperty(SystemPropertyKey key) {
		return client.getProperty(key);
	}

	public boolean isConnected() {
		return client.isConnected();
	}

}
