package net.honeyflower.virtualbox.client;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientTest {
	
	private Client client;
	private static final String testVMname = "windows 10";
	private static final String testVMSnapshotName = "test snap";

	@Before
	public void setUp() throws Exception {
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(Level.ALL);
		client = new Client("vbox", "vbox", "http://192.168.2.16:18083");
		client.connect();
	}

	@After
	public void tearDown() throws Exception {
		client.diconnect();
	}

	@Test
	public void stage1_testStartVM() {
		client.startVM(testVMname);
	}

	@Test
	public void stage2_testCreateSnapshotVM() {
		client.createSnapshotVM(testVMname, testVMSnapshotName, "test snap description");
	}

	@Test
	public void stage3_testGetVMSnapshots() {
		List<String> snaps = client.getVMSnapshots(testVMname);
		assertTrue(snaps!=null && ! snaps.isEmpty());
	}

	@Test
	public void stage4_testRestoreSnapshot() {
		client.restoreSnapshot(testVMname, testVMSnapshotName);
	}
	
	@Test
	public void stage5_testDeleteSnapshot() {
		client.deleteSnapshot(testVMname, testVMSnapshotName);
	}
	
	@Test
	public void stage6_testStopVM() {
		client.stopVM(testVMname);
	}

}
