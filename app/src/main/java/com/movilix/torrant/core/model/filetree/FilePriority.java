

package com.movilix.torrant.core.model.filetree;

import com.movilix.torrant.core.model.data.Priority;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class FilePriority implements Serializable
{
    private int priority;
    private Type type;

    public enum Type {MIXED, IGNORE, NORMAL, HIGH}

    public FilePriority(Priority priority)
    {
        this.priority = priority.value();
        this.type = typeFrom(priority);
    }

    public FilePriority(Type type)
    {
        this.priority = priorityFrom(type);
        this.type = type;
    }

    public Priority getPriority()
    {
        return Priority.fromValue(priority);
    }

    public Type getType()
    {
        return type;
    }

    public static Type typeFrom(Priority priority)
    {
        switch (priority) {
            case IGNORE:
                return Type.IGNORE;
            case LOW:
            case TWO:
            case THREE:
            case DEFAULT:
            case FIVE:
            case SIX:
                return Type.NORMAL;
            case TOP_PRIORITY:
                return Type.HIGH;
            default:
                return null;
        }
    }

    private static int priorityFrom(Type type)
    {
        switch (type) {
            case IGNORE:
                return Priority.IGNORE.value();
            case NORMAL:
                return Priority.DEFAULT.value();
            case HIGH:
                return Priority.TOP_PRIORITY.value();
            default:
                return -1;
        }
    }

    @Override
    public boolean equals(@Nullable Object o)
    {
        if (!(o instanceof FilePriority))
            return false;

        if (o == this)
            return true;

         FilePriority filePriority = (FilePriority)o;

         return priority == filePriority.priority && type.equals(filePriority.getType());
    }

    @Override
    public String toString()
    {
        return "FilePriority{" +
                "priority=" + priority +
                ", type=" + type +
                '}';
    }
}