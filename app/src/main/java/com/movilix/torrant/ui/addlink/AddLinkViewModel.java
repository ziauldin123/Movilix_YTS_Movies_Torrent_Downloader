

package com.movilix.torrant.ui.addlink;

import android.app.Application;

import com.movilix.torrant.core.exception.NormalizeUrlException;
import com.movilix.torrant.core.urlnormalizer.NormalizeUrl;
import com.movilix.torrant.core.utils.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

public class AddLinkViewModel extends AndroidViewModel
{
    public ObservableBoolean showClipboardButton = new ObservableBoolean();
    public ObservableField<String> link = new ObservableField<>();

    public AddLinkViewModel(@NonNull Application application)
    {
        super(application);
    }

    void initLinkFromClipboard()
    {
        List<CharSequence> clipboard = Utils.getClipboardText(getApplication());
        if (clipboard.isEmpty())
            return;

        String firstItem = clipboard.get(0).toString();
        String c = firstItem.toLowerCase();
        if (c.startsWith(Utils.MAGNET_PREFIX) ||
            c.startsWith(Utils.HTTP_PREFIX) ||
            Utils.isHash(firstItem))
        {
            link.set(firstItem);
        }
    }

    String normalizeUrl(@NonNull String link) throws NormalizeUrlException
    {
        if (Utils.isHash(link)) {
            link = Utils.normalizeMagnetHash(link);

        } else if (!link.toLowerCase().startsWith(Utils.MAGNET_PREFIX)) {
            NormalizeUrl.Options options = new NormalizeUrl.Options();
            options.decode = false;
            link = NormalizeUrl.normalize(link, options);
        }

        return link;
    }
}
