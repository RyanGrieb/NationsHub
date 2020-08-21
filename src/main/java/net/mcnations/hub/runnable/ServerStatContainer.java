package net.mcnations.hub.runnable;

public class ServerStatContainer {

	private boolean online;
	private int playerCount;

	public ServerStatContainer(boolean online, int playerCount) {
		this.online = online;
		this.playerCount = playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public boolean isOnline() {
		return online;
	}
}
