package org.randomlima.wizardstaffs.utilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class ParticleUtil {
    public void line(Location loc1, Location loc2, Particle particle, int density) {
        double distance = loc1.distance(loc2);
        Vector vector = loc2.toVector().subtract(loc1.toVector()).normalize();

        int numParticles = (int) (distance * density);
        double increment = distance / numParticles;
        Vector step = vector.multiply(increment);

        for (int i = 0; i <= numParticles; i++) {
            Location point = loc1.clone().add(step.clone().multiply(i));
            loc1.getWorld().spawnParticle(particle, point, 0);
        }
    }
    public void circle(Location center, double radius, Particle particle, int density) {
        double angleIncrement = 2 * Math.PI / density;

        for (int i = 0; i < density; i++) {
            double angle = i * angleIncrement;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(center.getWorld(), x, center.getY(), z);
            center.getWorld().spawnParticle(particle, particleLocation, 0);
        }
    }
}
