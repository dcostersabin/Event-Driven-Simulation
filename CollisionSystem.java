/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;

public class CollisionSystem {
    private static final double refresh = 0.2;
    private MinPQ<Event> pq;
    private double t = 0.0;
    private Particle[] particles;

    public CollisionSystem(Particle[] particles) {
        this.particles = particles.clone();
    }

    private void predict(Particle a, double limit) {
        if (a == null) return;
        for (int i = 0; i < particles.length; i++) {
            double dt = a.timeToHit(particles[i]);
            if (t + dt <= limit) {
                pq.insert(new Event(t + dt, a, particles[i]));
            }
        }
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (t + dtX <= limit) pq.insert(new Event(t + dtX, a, null));
        if (t + dtY <= limit) pq.insert(new Event(t + dtY, null, a));
    }

    public void simulate(double limit) {
        StdDraw.setCanvasSize(800, 800);
        pq = new MinPQ<Event>();
        for (int i = 0; i < particles.length; i++) predict(particles[i], limit);
        pq.insert(new Event(0, null, null));

        while (!pq.isEmpty()) {
            Event event = pq.delMin();
            if (!event.isValid()) continue;
            Particle a = event.getA();
            Particle b = event.getB();

            for (int i = 0; i < particles.length; i++)
                particles[i].move(event.getTime() - t);
            t = event.getTime();

            if (a != null && b != null) a.bounceOff(b);
            else if (a != null && b == null) a.bounceOffVerticalWall();
            else if (a == null && b != null) b.bounceOffHorizontalWall();
            else if (a == null && b == null) redraw(limit);

            predict(a, limit);
            predict(b, limit);
        }
    }

    private void redraw(double limit) {
        StdDraw.clear();
        for (int i = 0; i < particles.length; i++) {
            particles[i].draw();
        }
        StdDraw.show();
        StdDraw.pause(20);
        if (t < limit) {
            pq.insert(new Event(t + 1.0 / refresh, null, null));
        }
    }

    public static void main(String[] args) {
        Particle[] particles = new Particle[15];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
        }
        CollisionSystem s = new CollisionSystem(particles);
        s.simulate(100000);
    }
}
