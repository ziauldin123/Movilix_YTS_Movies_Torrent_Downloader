

package com.movilix.torrant.ui.detailtorrent;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MsgDetailTorrentViewModel extends ViewModel
{
    private BehaviorSubject<Boolean> fragmentInActionModeEvents = BehaviorSubject.create();

    public void fragmentInActionMode(boolean inActionMode)
    {
        fragmentInActionModeEvents.onNext(inActionMode);
    }

    public Observable<Boolean> observeFragmentInActionMode()
    {
        return fragmentInActionModeEvents;
    }
}
