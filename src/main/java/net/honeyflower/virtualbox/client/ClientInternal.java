package net.honeyflower.virtualbox.client;

import java.util.ArrayList;
import java.util.List;

import org.virtualbox_6_0.IConsole;
import org.virtualbox_6_0.IMachine;
import org.virtualbox_6_0.IProgress;
import org.virtualbox_6_0.ISession;
import org.virtualbox_6_0.ISnapshot;
import org.virtualbox_6_0.LockType;
import org.virtualbox_6_0.VirtualBoxManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientInternal {
	
	private VirtualBoxManager mgr;
	private ISession session;
	
	
	public ClientInternal(String username, String password, String url) {
		mgr = VirtualBoxManager.createInstance(null);
		mgr.connect(url, username, password);
		session = mgr.getSessionObject();
	}
	
	protected boolean startVM(IMachine vm) {
		IProgress p = vm.launchVMProcess(session, "headless", "");
		boolean result = watchProgress(mgr, p, 10000);
		session.unlockMachine();
		return result;
		//return 
	}
	
	protected boolean stopVM(IMachine vm) {
		boolean result = false;
		try {
			vm.lockMachine(session,  LockType.Shared);
			vm = session.getMachine();
			IProgress p = vm.saveState();
			result = watchProgress(mgr, p, 10000);
			session.unlockMachine();
		} catch (Exception e) {
			log.warn("error during saving state", e);
			
		}
		return result;
	}
	
	protected boolean createSnapshotVM(IMachine vm, String snapshotName, String desciption) {
		IProgress p = vm.takeSnapshot(snapshotName, desciption, true, null);
		return watchProgress(mgr, p, 10000);
	}
	
	protected List<String> getVMSnapshots(IMachine vm) {
		List<String> snaps = new ArrayList<String>(10);
		if (vm.getSnapshotCount() < 1) return snaps;
		
		ISnapshot root = vm.findSnapshot(null);
		traverseSnapshots(root, snaps);
		return snaps;
	}
	
	protected ISnapshot findSnapshot(IMachine vm, String snapshotName) {
		return vm.findSnapshot(snapshotName);
	}
	
	protected boolean restoreSnapshot(IMachine vm, ISnapshot snapshot) {
		log.info("starting snapshot restore for vm {}", vm.getName());
		shutdownVM(vm);
        log.debug("restoring snapshot {} for vm {}", snapshot.getName(), vm.getName());
		IProgress p = vm.restoreSnapshot(snapshot);
		return watchProgress(mgr, p, 10000);
	}
	
	protected boolean shutdownVM(IMachine vm) {
		boolean result = false;
		log.debug("shutting down vm {}", vm.getName());
        vm.lockMachine(session,  LockType.Shared);
        IConsole console = session.getConsole();
        result = watchProgress(mgr, console.powerDown(), 10000);
        session.unlockMachine();
        return result;
	}

	protected void diconnect() {
		mgr.cleanup();
	}
	
	private void traverseSnapshots(ISnapshot root, List<String> snaps) {
		snaps.add(root.getName());
		for (ISnapshot snapChild : root.getChildren()) {
			traverseSnapshots(snapChild, snaps);
        }
		
	}
	
	private boolean watchProgress(VirtualBoxManager mgr, IProgress p, long waitMillis) {
        long end = System.currentTimeMillis() + waitMillis;
        while (!p.getCompleted()) {
            // process system event queue
            mgr.waitForEvents(0);
            log.debug("processing task {} of {} operation, progress: {}", p.getId(), p.getDescription(), p.getPercent());
            // wait for completion of the task, but at most 200 msecs
            p.waitForCompletion(200);
            if (System.currentTimeMillis() >= end)
                return false;
        }
        log.info("finished processing task {}, result is: {}", p.getDescription(), p.getCompleted());
        return true;
    }

	protected IMachine findVM(String name) {
		return mgr.getVBox().findMachine(name);
	}

}
