package org.boomgames.boomclick;

import androidx.fragment.app.Fragment;

public interface FragmentActionListener {
    public void onFragmentShouldBeShown(Fragment fragment);
    public void onFragmentShouldBeHidden(boolean showScore);
}
