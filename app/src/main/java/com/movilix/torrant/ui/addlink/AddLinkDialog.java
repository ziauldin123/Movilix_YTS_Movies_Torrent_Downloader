

package com.movilix.torrant.ui.addlink;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;


import com.movilix.R;
import com.movilix.databinding.DialogAddLinkBinding;
import com.movilix.torrant.core.exception.NormalizeUrlException;
import com.movilix.torrant.core.utils.Utils;

import com.movilix.torrant.ui.ClipboardDialog;
import com.movilix.torrant.ui.FragmentCallback;
import com.movilix.torrant.ui.addtorrent.AddTorrentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AddLinkDialog extends DialogFragment
{
    private static final String TAG = AddLinkDialog.class.getSimpleName();

    private static final String TAG_CLIPBOARD_DIALOG = "clipboard_dialog";

    private AlertDialog alert;
    private AppCompatActivity activity;
    private DialogAddLinkBinding binding;
    private com.movilix.torrant.ui.addlink.AddLinkViewModel viewModel;
    private ClipboardDialog clipboardDialog;
    private ClipboardDialog.SharedViewModel clipboardViewModel;
    private CompositeDisposable disposables = new CompositeDisposable();

    public static AddLinkDialog newInstance()
    {
        AddLinkDialog frag = new AddLinkDialog();

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

    @Override
    public void onResume()
    {
        super.onResume();

        /* Back button handle */
        getDialog().setOnKeyListener((DialogInterface dialog, int keyCode, KeyEvent event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return true;
                } else {
                    onBackPressed();
                    return true;
                }
            } else {
                return false;
            }
        });
    }

    @Override
    public void onStop()
    {
        super.onStop();

        unsubscribeClipboardManager();
        disposables.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        subscribeClipboardManager();
        subscribeAlertDialog();
    }

    private void subscribeAlertDialog()
    {
        Disposable d = clipboardViewModel.observeSelectedItem().subscribe((item) -> {
            if (TAG_CLIPBOARD_DIALOG.equals(item.dialogTag))
                handleUrlClipItem(item.str);
        });
        disposables.add(d);
    }

    private void handleUrlClipItem(String item)
    {
        if (TextUtils.isEmpty(item))
            return;

        viewModel.link.set(item);
        binding.link.postDelayed(this::addLink, 500);
    }

    private void subscribeClipboardManager() {
        ClipboardManager clipboard = (ClipboardManager)activity.getSystemService(Activity.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(clipListener);
    }

    private void unsubscribeClipboardManager() {
        ClipboardManager clipboard = (ClipboardManager)activity.getSystemService(Activity.CLIPBOARD_SERVICE);
        clipboard.removePrimaryClipChangedListener(clipListener);
    }

    private ClipboardManager.OnPrimaryClipChangedListener clipListener = this::switchClipboardButton;

    private final ViewTreeObserver.OnWindowFocusChangeListener onFocusChanged =
            (__) -> switchClipboardButton();

    private void switchClipboardButton()
    {
        ClipData clip = Utils.getClipData(activity.getApplicationContext());
        viewModel.showClipboardButton.set(clip != null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (activity == null)
            activity = (AppCompatActivity)getActivity();

        ViewModelProvider provider = new ViewModelProvider(activity);
        viewModel = provider.get(com.movilix.torrant.ui.addlink.AddLinkViewModel.class);
        clipboardViewModel = provider.get(ClipboardDialog.SharedViewModel.class);

        FragmentManager fm = getChildFragmentManager();
        clipboardDialog = (ClipboardDialog)fm.findFragmentByTag(TAG_CLIPBOARD_DIALOG);

        LayoutInflater i = LayoutInflater.from(activity);
        binding = DataBindingUtil.inflate(i, R.layout.dialog_add_link, null, false);
        binding.setViewModel(viewModel);

        initLayoutView();

        binding.getRoot().getViewTreeObserver().addOnWindowFocusChangeListener(onFocusChanged);

        return alert;
    }

    @Override
    public void onDestroyView()
    {
        binding.getRoot().getViewTreeObserver().removeOnWindowFocusChangeListener(onFocusChanged);

        super.onDestroyView();
    }

    private void initLayoutView()
    {
        /* Dismiss error label if user has changed the text */
        binding.link.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s)
            {
                binding.layoutLink.setErrorEnabled(false);
                binding.layoutLink.setError(null);
            }
        });

        binding.clipboardButton.setOnClickListener((v) -> showClipboardDialog());
        switchClipboardButton();
        viewModel.initLinkFromClipboard();

        initAlertDialog(binding.getRoot());
    }

    private void initAlertDialog(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(R.string.dialog_add_link_title)
                .setPositiveButton(R.string.add, null)
                .setNegativeButton(R.string.cancel, null)
                .setView(view);

        alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setOnShowListener((DialogInterface dialog) -> {
            Button addButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
            addButton.setOnClickListener((v) -> addLink());

            Button cancelButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
            cancelButton.setOnClickListener((v) ->
                    finish(new Intent(), FragmentCallback.ResultCode.CANCEL));
        });
    }

    private void showClipboardDialog()
    {
        if (!isAdded())
            return;

        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentByTag(TAG_CLIPBOARD_DIALOG) == null) {
            clipboardDialog = ClipboardDialog.newInstance();
            clipboardDialog.show(fm, TAG_CLIPBOARD_DIALOG);
        }
    }

    private void addLink()
    {
        String s = viewModel.link.get();
        if (TextUtils.isEmpty(s))
            return;
        if (!checkUrlField())
            return;

        try {
            s = viewModel.normalizeUrl(s);

        } catch (NormalizeUrlException e) {
            binding.layoutLink.setErrorEnabled(true);
            binding.layoutLink.setError(getString(R.string.invalid_url, e.getMessage()));
            binding.layoutLink.requestFocus();

            return;
        }

        Intent i = new Intent(activity, AddTorrentActivity.class);
        i.putExtra(AddTorrentActivity.TAG_URI, Uri.parse(s));
        startActivity(i);

        finish(new Intent(), FragmentCallback.ResultCode.OK);
    }

    private boolean checkUrlField()
    {
        if (TextUtils.isEmpty(binding.link.getText())) {
            binding.layoutLink.setErrorEnabled(true);
            binding.layoutLink.setError(getString(R.string.error_empty_link));
            binding.layoutLink.requestFocus();

            return false;
        }

        binding.layoutLink.setErrorEnabled(false);
        binding.layoutLink.setError(null);

        return true;
    }

    public void onBackPressed()
    {
        finish(new Intent(), FragmentCallback.ResultCode.BACK);
    }

    private void finish(Intent intent, FragmentCallback.ResultCode code)
    {
        alert.dismiss();
        ((FragmentCallback)activity).onFragmentFinished(this, intent, code);
    }
}
