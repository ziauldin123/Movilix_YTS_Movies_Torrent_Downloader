package com.movilix.torrant.core.model.session;

import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.system.FileDescriptorWrapper;
import com.movilix.torrant.core.system.FileSystemFacade;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;

/*
 * Parser of blacklist IP addresses in DAT and P2P formats.
 */

class IPFilterParser
{
    private static final String TAG = IPFilterParser.class.getSimpleName();

    private static final int MAX_LOGGED_ERRORS = 5;

    private boolean logEnabled;

    public IPFilterParser()
    {
       logEnabled = true;
    }

    public IPFilterParser(boolean logEnabled)
    {
        this.logEnabled = logEnabled;
    }

    public int parseFile(@NonNull Uri path, @NonNull FileSystemFacade fs, @NonNull IPFilter filter)
    {
        int ruleCount = 0;
        try {
            if (!fs.fileExists(path))
                return ruleCount;
        } catch (UnknownUriException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return ruleCount;
        }

        Log.d(TAG, "Start parsing IP filter file");

        try (FileDescriptorWrapper w = fs.getFD(path);
             FileInputStream is = new FileInputStream(w.open("r"))) {

            String pathStr = path.toString().toLowerCase();
            if (pathStr.endsWith(".dat"))
                ruleCount = parseDAT(is, filter);
            else if (pathStr.endsWith(".p2p"))
                ruleCount = parseP2P(is, filter);

        } catch (IOException | UnknownUriException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return ruleCount;

        } finally {
            Log.d(TAG, "Completed parsing IP filter file, is success = " + ruleCount);
        }

        return ruleCount;
    }

    /*
     * Parser for eMule ip filter in DAT format
     */

    public int parseDAT(@NonNull InputStream is, @NonNull IPFilter filter)
    {
        int ruleCount = 0;
        long lineNum = 0;
        int parseErrorCount = 0;
        LineIterator it;

        try {
            it = IOUtils.lineIterator(is, "UTF-8");

        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));

            return ruleCount;
        }

        while (it.hasNext()) {
            lineNum++;

            String line = it.nextLine();
            line = (line == null ? null : line.trim());
            if (line == null || line.isEmpty())
                continue;

            /* Ignoring commented lines */
            if (line.startsWith("#") || line.startsWith("//"))
                continue;

            /* Line should be split by commas */
            String[] parts = line.split(",");
            /* Check if there is at least one item (ip range) */
            if (parts.length == 0)
                continue;

            /* Check if there is an access value (apparently not mandatory) */
            if (parts.length > 1) {
                int accessNum = Integer.parseInt(parts[1].trim());
                /* Ignoring this rule because access value is too high */
                if (accessNum > 127)
                    continue;
            }

            /* IP Range should be split by a dash */
            String[] ips = parts[0].split("-");
            if (ips.length != 2) {
                parseErrorCount++;
                errLog(parseErrorCount, "DAT", "line " + lineNum + " is malformed. Line was " + line);
                continue;
            }

            String startAddr = parseIpAddress(ips[0]);
            if (startAddr == null) {
                parseErrorCount++;
                errLog(parseErrorCount, "DAT", "line " + lineNum +
                        " is malformed. Start IP of the range is invalid: " + ips[0]);
                continue;
            }

            String endAddr = parseIpAddress(ips[1]);
            if (endAddr == null) {
                parseErrorCount++;
                errLog(parseErrorCount, "DAT", "line " + lineNum +
                        " is malformed. End IP of the range is invalid: " + ips[1]);
                continue;
            }

            try {
                filter.addRange(startAddr, endAddr);
                ruleCount++;

            } catch (Exception e) {
                parseErrorCount++;
                errLog(parseErrorCount, "DAT", "line " + lineNum +
                        " is malformed. Line was " + line + ": " + e.getMessage());
            }
        }

        return ruleCount;
    }

    /*
     * Parser for PeerGuardian ip filter in p2p format
     */

    public int parseP2P(@NonNull InputStream is, @NonNull IPFilter filter)
    {
        int ruleCount = 0;
        long lineNum = 0;
        int parseErrorCount = 0;
        LineIterator it;

        try {
            it = IOUtils.lineIterator(is, "UTF-8");

        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));

            return ruleCount;
        }

        while (it.hasNext()) {
            lineNum++;

            String line = it.nextLine();
            line = (line == null ? null : line.trim());
            if (line == null || line.isEmpty())
                continue;

            /* Ignoring commented lines */
            if (line.startsWith("#") || line.startsWith("//"))
                continue;

            /* Line should be split by ':' */
            String[] parts = line.split(":");
            if (parts.length < 2) {
                parseErrorCount++;
                errLog(parseErrorCount, "P2P", "line " + lineNum + " is malformed");
                continue;
            }

            /* IP Range should be split by a dash */
            String[] ips = parts[1].split("-");
            if (ips.length != 2) {
                parseErrorCount++;
                errLog(parseErrorCount, "P2P", "line " + lineNum + " is malformed. Line was" + line);
                continue;
            }

            String startAddr = parseIpAddress(ips[0]);
            if (startAddr == null) {
                parseErrorCount++;
                errLog(parseErrorCount, "P2P", "line " + lineNum +
                        " is malformed. Start IP of the range is invalid: " + ips[0]);
                continue;
            }

            String endAddr = parseIpAddress(ips[1]);
            if (endAddr == null) {
                parseErrorCount++;
                errLog(parseErrorCount, "P2P", "line " + lineNum +
                        " is malformed. End IP of the range is invalid: " + ips[1]);
                continue;
            }

            try {
                filter.addRange(startAddr, endAddr);
                ruleCount++;

            } catch (Exception e) {
                parseErrorCount++;
                errLog(parseErrorCount, "P2P", "line " + lineNum +
                        " is malformed. Line was " + line + ": " + e.getMessage());
            }
        }

        return ruleCount;
    }

    private String parseIpAddress(String ip)
    {
        if (ip == null || ip.isEmpty()) {
            return null;
        } else {
            return ip.trim();
        }
    }

    private void errLog(int parseErrorCount, String prefix, String msg)
    {
        if (!logEnabled || parseErrorCount > MAX_LOGGED_ERRORS)
            return;

        Log.e(TAG, prefix + ": " + msg);
    }
}
