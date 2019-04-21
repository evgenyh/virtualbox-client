package net.honeyflower.virtualbox.client.model;

import org.virtualbox_6_0.ISnapshot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Snapshot {
	private String name;
	private String id;
	private String description;
	private long timestamp;
	
	
	public static Snapshot fromISnapshot(ISnapshot iSnapshot) {
		if (iSnapshot==null) return null;
		Snapshot snapshot = Snapshot.builder()
		.description(iSnapshot.getDescription())
		.id(iSnapshot.getId())
		.name(iSnapshot.getName())
		.timestamp(iSnapshot.getTimeStamp())
		.build();
		
		return snapshot;
	}
}
