

package com.movilix.torrant.core.utils;

import com.movilix.torrant.core.model.data.metainfo.BencodeFileItem;
import com.movilix.torrant.core.model.filetree.FileNode;
import com.movilix.torrant.core.model.filetree.FileTree;
import com.movilix.torrant.core.model.filetree.TorrentContentFileTree;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.util.Pair;

/*
 * The static class for create and using TorrentContentFileTree objects.
 *
 * TODO: need refactor (with BencodeFileTreeUtils)
 */

public class TorrentContentFileTreeUtils
{
    /*
     * Returns tree and its files
     */

    public static Pair<TorrentContentFileTree, TorrentContentFileTree[]> buildFileTree(List<BencodeFileItem> files)
    {
        TorrentContentFileTree root = new TorrentContentFileTree(FileTree.ROOT, 0L, FileNode.Type.DIR);
        TorrentContentFileTree parentTree = root;
        TorrentContentFileTree[] leaves = new TorrentContentFileTree[files.size()];
        /* It allows reduce the number of iterations on the paths with equal beginnings */
        String prevPath = "";
        List<BencodeFileItem> filesCopy = new ArrayList<>(files);
        /* Sort reduces the returns number to root */
        Collections.sort(filesCopy);

        for (BencodeFileItem file : filesCopy) {
            String path;
            /*
             * Compare previous path with new path.
             * Example:
             * prev = dir1/dir2/
             * cur  = dir1/dir2/file1
             *        |________|
             *          equal
             *
             * prev = dir1/dir2/
             * cur  = dir3/file2
             *        |________|
             *         not equal
             */
            if (!prevPath.isEmpty() &&
                    file.getPath().regionMatches(true, 0, prevPath, 0, prevPath.length())) {
                /*
                 * If beginning paths are equal, remove previous path from the new path.
                 * Example:
                 * prev = dir1/dir2/
                 * cur  = dir1/dir2/file1
                 * new  = file1
                 */
                path = file.getPath().substring(prevPath.length());
            } else {
                /* If beginning paths are not equal, return to root */
                path = file.getPath();
                parentTree = root;
            }

            String[] nodes = path.split(File.separator);
            /*
             * Remove last node (file) from previous path.
             * Example:
             * cur = dir1/dir2/file1
             * new = dir1/dir2/
             */
            prevPath = file.getPath()
                    .substring(0, file.getPath().length() - nodes[nodes.length - 1].length());

            /* Iterates path nodes */
            for (int i = 0; i < nodes.length; i++) {
                if (!parentTree.contains(nodes[i])) {
                    TorrentContentFileTree leaf = makeObject(file.getIndex(), nodes[i],
                                                            file.getSize(), parentTree,
                                                            i == (nodes.length - 1));
                    leaves[file.getIndex()] = leaf;
                    /* The last leaf item is a file */
                    parentTree.addChild(leaf);
                }

                TorrentContentFileTree nextParent = parentTree.getChild(nodes[i]);
                /* Skipping leaf nodes */
                if (!nextParent.isFile())
                    parentTree = nextParent;
            }
        }

        return Pair.create(root, leaves);
    }

    private static TorrentContentFileTree makeObject(int index, String name,
                                              long size, TorrentContentFileTree parent,
                                              boolean isFile)
    {
        return (isFile ?
                new TorrentContentFileTree(index, name, size, FileNode.Type.FILE, parent) :
                new TorrentContentFileTree(name, 0L, FileNode.Type.DIR, parent));
    }
}
