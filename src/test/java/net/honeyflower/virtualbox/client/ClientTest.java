package net.honeyflower.virtualbox.client;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class ClientTest {
	
	private Client client;
	private static final String testVMname = "windows 10";
	private static final String testVMSnapshotName = "test snap";

	@Before
	public void setUp() throws Exception {
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(Level.ALL);
		client = new Client("vbox", "vbox", "http://192.168.2.16:18083");
	}

	@After
	public void tearDown() throws Exception {
		client.diconnect();
	}

	@Test
	public void testStartVM() {
		client.startVM(testVMname);
	}

	@Test
	public void testStopVM() {
		client.stopVM(testVMname);
	}

	@Test
	public void testCreateSnapshotVM() {
		client.createSnapshotVM(testVMname, testVMSnapshotName, "test snap description");
	}

	@Test
	public void testGetVMSnapshots() {
		client.getVMSnapshots(testVMname);
	}

	@Test
	public void testRestoreSnapshot() {
		client.restoreSnapshot(testVMname, testVMSnapshotName);
	}

}
