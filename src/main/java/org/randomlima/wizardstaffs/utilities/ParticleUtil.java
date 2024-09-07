package org.randomlima.wizardstaffs.utilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.randomlima.wizardstaffs.WizardStaffs;

public class ParticleUtil {
    private WizardStaffs plugin;
    public ParticleUtil(WizardStaffs plugin){
        this.plugin = plugin;
    }
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
    public void spiral(Location center, double radius, double height, Particle particle, int turns, double speed, long durationTicks) {
        // Schedule a repeating task for the animated spiral
        new BukkitRunnable() {
            double angle = 0;  // Angle starts at 0
            double y = 0;      // Height starts at 0
            double maxY = height; // The maximum height of the spiral

            @Override
            public void run() {
                // Increment the angle based on the speed
                angle += speed;
                if (angle >= 2 * Math.PI * turns) {
                    cancel();  // End the task after completing the desired number of turns
                    return;
                }

                // Calculate the x and z coordinates for the spiral
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);

                // Create the new particle location (based on the center)
                Location particleLocation = center.clone().add(new Vector(x, y, z));
                center.getWorld().spawnParticle(particle, particleLocation, 0);

                y += (maxY / (2 * Math.PI * turns * (1 / speed)));  // Smoothly raise the height

                // Stop the task when the desired height is reached
                if (y > maxY) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // Runs every tick (adjust to control speed)
    }
}
