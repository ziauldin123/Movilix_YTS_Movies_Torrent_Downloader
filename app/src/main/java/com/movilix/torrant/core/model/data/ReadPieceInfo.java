

package com.movilix.torrant.core.model.data;

public class ReadPieceInfo
{
    public int piece;
    public int size;
    public long bufferPtr;
    public Exception err;

    public ReadPieceInfo(int piece, int size, long bufferPtr, Exception err)
    {
        this.piece = piece;
        this.size = size;
        this.bufferPtr = bufferPtr;
        this.err = err;
    }

    @Override
    public String toString()
    {
        return "ReadPieceInfo{" +
                "piece=" + piece +
                ", size=" + size +
                ", bufferPtr=" + bufferPtr +
                ", err=" + err +
                '}';
    }
}
