

package com.movilix.torrant.core.utils;

import android.content.Context;
import android.text.format.Formatter;
import android.widget.TextView;


import com.movilix.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

public class BindingAdapterUtils
{
    @BindingAdapter(value = {"fileSize", "formatFileSize"}, requireAll = false)
    public static void formatFileSize(@NonNull TextView view,
                                      long fileSize,
                                      @Nullable String formatFileSize)
    {
        Context context = view.getContext();
        String sizeStr = (fileSize >= 0 ?
                Formatter.formatFileSize(context, fileSize) :
                context.getString(R.string.not_available));
        view.setText((formatFileSize == null ? sizeStr : String.format(formatFileSize, sizeStr)));
    }

    @BindingAdapter({"formatDate"})
    public static void formatDate(@NonNull TextView view, long date)
    {
        view.setText(SimpleDateFormat.getDateTimeInstance()
                .format(new Date(date)));
    }

    public static String formatProgress(@NonNull Context context,
                                        long downloaded,
                                        long total,
                                        int progress)
    {
        return context.getString(R.string.download_counter_template,
                Formatter.formatFileSize(context, (progress == 100 ? total : downloaded)),
                Formatter.formatFileSize(context, total),
                progress);
    }

    public static String formatETA(@NonNull Context context,
                                   long ETA)
    {
        return (ETA <= 0 ? Utils.INFINITY_SYMBOL : DateUtils.formatElapsedTime(context, ETA));
    }

    @BindingAdapter(value = {"floatNum", "floatPrecision"}, requireAll = false)
    public static void formatFloat(@NonNull TextView view, double floatNum, int floatPrecision)
    {
        view.setText(String.format(Locale.getDefault(),
                "%,." + (floatPrecision <= 0 ? 3 : floatPrecision) + "f",
                floatNum));
    }

    public static String formatPieces(@NonNull Context context,
                                      int downloadedPieces,
                                      int numPieces,
                                      int pieceLength)
    {
        return context.getString(R.string.torrent_pieces_template,
                downloadedPieces,
                numPieces,
                Formatter.formatFileSize(context, pieceLength));
    }

    public static String formatSpeed(@NonNull Context context,
                                     long uploadSpeed,
                                     long downloadSpeed)
    {
        return context.getString(R.string.download_upload_speed_template,
                Formatter.formatFileSize(context, downloadSpeed),
                Formatter.formatFileSize(context, uploadSpeed));
    }

    public static String formatPiecesInfo(@NonNull Context context,
                                          int downloadedPieces,
                                          int allPiecesCount,
                                          int pieceLength)
    {
        String pieceLengthStr = Formatter.formatFileSize(context, pieceLength);

        return context.getString(R.string.torrent_pieces_template,
                downloadedPieces, allPiecesCount, pieceLengthStr);
    }
}
