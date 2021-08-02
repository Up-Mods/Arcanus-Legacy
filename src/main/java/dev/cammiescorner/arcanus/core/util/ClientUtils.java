package dev.cammiescorner.arcanus.core.util;

import java.util.List;

public interface ClientUtils {
	List<Pattern> getPattern();

	void setTimer(int value);

	void setUnfinishedSpell(boolean value);
}
