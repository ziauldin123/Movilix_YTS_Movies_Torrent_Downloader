

package com.movilix.torrant.ui.addtag;

import android.app.Application;

import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.model.data.entity.TagInfo;
import com.movilix.torrant.core.storage.TagRepository;
import com.movilix.torrant.core.utils.Utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.Completable;

public class AddTagViewModel extends AndroidViewModel {
    private final TagRepository tagRepo;
    public AddTagState state;

    public AddTagViewModel(@NonNull Application application) {
        super(application);

        state = new AddTagState();
        tagRepo = RepositoryHelper.getTagRepository(getApplication());
    }

    public void setInitValues(@NonNull TagInfo info) {
        state.setExistsTagId(info.id);
        state.setName(info.name);
        state.setColor(info.color);
    }

    public void setRandomColor() {
        state.setColor(Utils.getRandomColor());
    }

    Completable saveTag() {
        TagInfo info;
        Long existsTagId = state.getExistsTagId();
        if (existsTagId == null) {
            info = new TagInfo(state.getName(), state.getColor());
        } else {
            info = new TagInfo(existsTagId, state.getName(), state.getColor());
        }
        return Completable.fromCallable(() -> {
            if (existsTagId == null) {
                TagInfo oldTag = tagRepo.getByName(info.name);
                if (oldTag != null) {
                    throw new TagAlreadyExistsException();
                }
                tagRepo.insert(info);
            } else {
                tagRepo.update(info);
            }
            return null;
        });
    }

    public static class TagAlreadyExistsException extends Exception { }
}
