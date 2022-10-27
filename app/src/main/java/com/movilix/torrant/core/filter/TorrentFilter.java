

package com.movilix.torrant.core.filter;

import com.movilix.torrant.core.model.data.TorrentInfo;

import io.reactivex.functions.Predicate;

public interface TorrentFilter extends Predicate<TorrentInfo> {}
