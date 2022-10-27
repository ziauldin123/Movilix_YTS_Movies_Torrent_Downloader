

package com.movilix.torrant.ui.tag;

import android.app.Application;

import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.model.data.entity.TagInfo;
import com.movilix.torrant.core.storage.TagRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.Flowable;

public class SelectTagViewModel extends AndroidViewModel {
    private TagRepository tagRepo;
    private Set<Long> excludeTagsId;

    public SelectTagViewModel(@NonNull Application application) {
        super(application);

        tagRepo = RepositoryHelper.getTagRepository(application);
    }

    public void setExcludeTagsId(long[] tagsId) {
        if (tagsId == null) {
            excludeTagsId = null;
        } else {
            excludeTagsId = new HashSet<>();
            for (long tagId : tagsId) {
                excludeTagsId.add(tagId);
            }
        }
    }

    public boolean filterExcludeTags(@NonNull TagInfo info) {
        return excludeTagsId == null || !excludeTagsId.contains(info.id);
    }

    Flowable<List<TagInfo>> observeTags() {
        return tagRepo.observeAll();
    }
}
