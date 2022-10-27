

package com.movilix.torrant.core.model.session;

import org.libtorrent4j.swig.address;
import org.libtorrent4j.swig.error_code;
import org.libtorrent4j.swig.ip_filter;
import com.movilix.torrant.core.exception.IPFilterException;

import androidx.annotation.NonNull;

class IPFilterImpl implements IPFilter
{
    private ip_filter filter;

    public IPFilterImpl()
    {
        filter = new ip_filter();
    }

    @Override
    public void addRange(@NonNull String first, @NonNull String last) throws IPFilterException
    {
        error_code ec = new error_code();
        address firstAddr = address.from_string(first, ec);
        if (ec.value() > 0)
            throw new IPFilterException("Invalid first IP in range: " + first);
        ec.clear();

        address lastAddr;
        if (first.equals(last)) {
            lastAddr = firstAddr;

        } else {
            lastAddr = address.from_string(last, ec);
            if (ec.value() > 0)
                throw new IPFilterException("Invalid last IP in range: " + last);

            if (firstAddr.is_v4() != lastAddr.is_v4() ||
                firstAddr.is_v6() != lastAddr.is_v6())
                throw new IPFilterException("IP range is malformed. One IP is IPv6 and the other is IPv4!");
        }

        filter.add_rule(firstAddr, lastAddr, ip_filter.access_flags.blocked.swigValue());
    }

    public ip_filter getFilter()
    {
        return filter;
    }
}
