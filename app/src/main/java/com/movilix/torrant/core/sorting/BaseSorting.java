

package com.movilix.torrant.core.sorting;

public class BaseSorting
{
    public enum Direction
    {
        ASC, DESC;

        public static Direction fromValue(String value)
        {
            for (Direction direction : Direction.class.getEnumConstants()) {
                if (direction.toString().equalsIgnoreCase(value)) {
                    return direction;
                }
            }

            return Direction.ASC;
        }
    }

    public interface SortingColumnsInterface<F>
    {
        int compare(F item1, F item2, Direction direction);

        String name();
    }

    private Direction direction;
    private String columnName;

    public BaseSorting(String columnName, Direction direction)
    {
        this.direction = direction;
        this.columnName = columnName;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public String getColumnName()
    {
        return columnName;
    }

    @Override
    public String toString()
    {
        return "BaseSorting{" +
                "direction=" + direction +
                ", columnName='" + columnName + '\'' +
                '}';
    }
}
