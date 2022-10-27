

package com.movilix.torrant.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.movilix.R;
import com.movilix.torrant.core.utils.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ClipboardDialog extends DialogFragment
{
    private static final String TAG = ClipboardDialog.class.getSimpleName();

    private AppCompatActivity activity;
    private ArrayAdapter<CharSequence> adapter;
    private SharedViewModel viewModel;

    public static class SharedViewModel extends androidx.lifecycle.ViewModel
    {
        private final PublishSubject<Item> selectedItemSubject = PublishSubject.create();

        public Observable<Item> observeSelectedItem()
        {
            return selectedItemSubject;
        }

        public void sendSelectedItem(Item item)
        {
            selectedItemSubject.onNext(item);
        }
    }

    public class Item
    {
        public String dialogTag;
        public String str;

        public Item(String dialogTag, String str)
        {
            this.dialogTag = dialogTag;
            this.str = str;
        }
    }

    public static ClipboardDialog newInstance()
    {
        ClipboardDialog frag = new ClipboardDialog();

        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            activity = (AppCompatActivity)context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (activity == null)
            activity = (AppCompatActivity)getActivity();

        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.clipboard)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        List<CharSequence> clipboardText = Utils.getClipboardText(activity.getApplicationContext());
        adapter = new ArrayAdapter<>(activity, R.layout.item_clipboard_list);
        adapter.addAll(clipboardText);

        dialogBuilder.setAdapter(adapter, (dialog, which) -> {
            CharSequence item = adapter.getItem(which);
            if (item != null)
                viewModel.sendSelectedItem(new Item(getTag(), item.toString()));
        });

        return dialogBuilder.create();
    }
}
