

package com.movilix.torrant.core.model.filetree;

/*
 * The interface with basic functions for a file object.
 */

import java.io.Serializable;

import androidx.annotation.NonNull;

public interface FileNode<F> extends Comparable<F>
{
    class Type implements Serializable
    {
        public static int DIR = 0;
        public static int FILE = 1;
    }

    String getName();

    void setName(String name);

    int getType();

    void setType(int type);

    @Override
    int compareTo(@NonNull F another);
}
