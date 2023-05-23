package ecs.systems;

import ecs.components.*;
import ecs.components.skill.BumerangProjectile;
import ecs.components.skill.ProjectileComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import starter.Game;
import tools.Point;

public class ProjectileSystem extends ECS_System {

    // private record to hold all data during streaming
    private record PSData(
            Entity e, ProjectileComponent prc, PositionComponent pc, VelocityComponent vc) {}

    /** sets the velocity and removes entities that reached their endpoint */
    @Override
    public void update() {
        Game.getEntities().stream()
                // Consider only entities that have a ProjectileComponent
                .flatMap(e -> e.getComponent(ProjectileComponent.class).stream())
                .map(prc -> buildDataObject((ProjectileComponent) prc))
                .map(this::setVelocity)
                // Filter all entities that have reached their endpoint
                .filter(
                        psd ->
                                hasReachedEndpoint(
                                        psd.prc.getStartPosition(),
                                        psd.prc.getGoalLocation(),
                                        psd.pc.getPosition()))
                // Remove all entities who reached their endpoint
                .forEach((psd)-> {
                    if(psd.e instanceof BumerangProjectile b) {
                        Hero h = (Hero) Game.getHero().get();
                        PositionComponent pcHero =
                            (PositionComponent) h.getComponent(PositionComponent.class).orElseThrow();

                        Entity projectile;
                        Point start = psd.pc.getPosition();

                        projectile = new BumerangProjectile(
                            3,
                            "character/knight/attack/bumerang",
                            5f,
                            0.3f,
                            start,
                            true);

                        ((BumerangProjectile) projectile).setupPositionComponent(psd.pc.getPosition());
                        ((BumerangProjectile) projectile).setupVelocityAndProjectileComponent(psd.pc, pcHero.getPosition());
                        ((BumerangProjectile) projectile).setupAnimationComponent();
                        ((BumerangProjectile) projectile).setupHitboxComponent();

                        this.removeEntitiesOnEndpoint(psd);
                        Game.addEntity(projectile);
                    } else {
                        this.removeEntitiesOnEndpoint(psd);
                    }
                });
    }

    private PSData buildDataObject(ProjectileComponent prc) {
        Entity e = prc.getEntity();

        PositionComponent pc =
                (PositionComponent)
                        e.getComponent(PositionComponent.class)
                                .orElseThrow(ProjectileSystem::missingAC);
        VelocityComponent vc =
                (VelocityComponent)
                        e.getComponent(VelocityComponent.class)
                                .orElseThrow(ProjectileSystem::missingAC);

        return new PSData(e, prc, pc, vc);
    }

    private PSData setVelocity(PSData data) {
        data.vc.setCurrentYVelocity(data.vc.getYVelocity());
        data.vc.setCurrentXVelocity(data.vc.getXVelocity());

        return data;
    }

    private void removeEntitiesOnEndpoint(PSData data) {
        Game.removeEntity(data.pc.getEntity());
    }

    /**
     * checks if the endpoint is reached
     *
     * @param start position to start the calculation
     * @param end point to check if projectile has reached its goal
     * @param current current position
     * @return true if the endpoint was reached or passed, else false
     */
    public boolean hasReachedEndpoint(Point start, Point end, Point current) {
        float dx = start.x - current.x;
        float dy = start.y - current.y;
        double distanceToStart = Math.sqrt(dx * dx + dy * dy);

        dx = start.x - end.x;
        dy = start.y - end.y;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        if (distanceToStart > totalDistance) {
            // The point has reached or passed the endpoint
            return true;
        } else {
            // The point has not yet reached the endpoint
            return false;
        }
    }

    private static MissingComponentException missingAC() {
        return new MissingComponentException("AnimationComponent");
    }
}
