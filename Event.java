/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Event implements Comparable<Event> {
    private double time;
    private Particle a, b;
    private int countA, countB;

    public Event(double t, Particle a, Particle b) {
        time = t;
        this.a = a;
        this.b = b;
        if (a != null) countA = a.count();
        else countA = -1;
        if (b != null) countB = b.count();
        else countB = -1;
    }

    public Particle getA() {
        return a;
    }

    public Particle getB() {
        return b;
    }

    public double getTime() {
        return time;
    }

    public int compareTo(Event that) {
        return (int) (this.time - that.time);
    }

    public boolean isValid() {
        if (a != null && a.count() != countA) return false;
        if (b != null && b.count() != countB) return false;
        return true;
    }
}
