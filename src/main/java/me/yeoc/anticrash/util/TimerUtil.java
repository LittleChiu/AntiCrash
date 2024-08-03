package me.yeoc.anticrash.util;

public final class TimerUtil {
	private long time;
	private boolean active;

	public TimerUtil() {
		time = System.nanoTime() / 1000000L;
		active = true;
	}

	public boolean reach(final long time) {
		if (!active)
			return false;
		return time() >= time;
	}

	public void reset() {
		time = System.nanoTime() / 1000000L;
	}

	public boolean sleep(final long time) {
		if (!active)
			return false;
		if (time() >= time) {
			reset();
			return true;
		}
		return false;
	}

	public long time() {
		return System.nanoTime() / 1000000L - time;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}