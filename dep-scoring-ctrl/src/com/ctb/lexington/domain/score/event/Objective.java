package com.ctb.lexington.domain.score.event;

public class Objective {
    private final Long id;
    private final String name;
    private Long numberOfItems;
    private Long pointsPossible;

    public static final String PRIMARY = "primary";
    public static final String SECONDARY = "secondary";

    public Objective(final Long id, final String name) {
        if (null == id)
            throw new NullPointerException();
        if (null == name)
            throw new NullPointerException();

        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * @return Returns the numberOfItems.
     */
    public Long getNumberOfItems() {
        return numberOfItems;
    }

    /**
     * @param numberOfItems The numberOfItems to set.
     */
    public void setNumberOfItems(final Long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    /**
     * @return Returns the pointsPossible.
     */
    public Long getPointsPossible() {
        return pointsPossible;
    }

    /**
     * @param pointsPossible The pointsPossible to set.
     */
    public void setPointsPossible(final Long pointsPossible) {
        this.pointsPossible = pointsPossible;
    }

    public Long incNumberOfItems() {
        if (numberOfItems == null)
            numberOfItems = new Long(1);
        else
            numberOfItems = new Long(numberOfItems.longValue() + 1);

        return numberOfItems;
    }

    public Long incPointsPossible(final int increment) {
        if (pointsPossible == null)
            pointsPossible = new Long(increment);
        else
            pointsPossible = new Long(pointsPossible.longValue() + increment);

        return pointsPossible;
    }

    // Object

    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (! (o instanceof Objective))
            return false;

        final Objective objective = (Objective) o;

        if (!id.equals(objective.id))
            return false;

        return true;
    }

    public int hashCode() {
        return id.hashCode();
    }
}