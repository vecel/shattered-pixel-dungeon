package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import java.util.function.Supplier;

public enum TrapType {
    WORN_DART(WornDartTrap::new, 10),
    CHILLING(ChillingTrap::new, 10),
    SHOCKING(ShockingTrap::new, 10),
    TOXIC(ToxicTrap::new, 10),
    ALARM(AlarmTrap::new, 10),
    CONFUSION(ConfusionTrap::new, 10),
    FLOCK(FlockTrap::new, 10),
    TELEPORTATION(TeleportationTrap::new, 10),
    GATEWAY(GatewayTrap::new, 10),
    GEYSER(GeyserTrap::new, 10),
    SUMMONING(SummoningTrap::new, 20),
    OOZE(OozeTrap::new, 20),
    BURNING(BurningTrap::new, 20),
    POISON_DART(PoisonDartTrap::new, 20),
    GRIPPING(GrippingTrap::new, 20),
    FROST(FrostTrap::new, 20),
    STORM(StormTrap::new, 20),
    CORROSION(CorrosionTrap::new, 20),
    FLASHING(FlashingTrap::new, 20),
    WEAKENING(WeakeningTrap::new, 20),
    EXPLOSIVE(ExplosiveTrap::new, 20),
    GNOLL_ROCKFALL(GnollRockfallTrap::new, 20),
    CURSING(CursingTrap::new, 30),
    DISTORTION(DistortionTrap::new, 30),
    GRIM(GrimTrap::new, 30),
    ROCKFALL(RockfallTrap::new, 30),
    GUARDIAN(GuardianTrap::new, 30),
    WARPING(WarpingTrap::new, 30),
    PITFALL(PitfallTrap::new, 30),
    BLAZING(BlazingTrap::new, 30),
    DISARMING(DisarmingTrap::new, 30),
    DISINTEGRATION(DisintegrationTrap::new, 30),
    TENGU_DART(TenguDartTrap::new, 0);

    private final Supplier<Trap> factory;
    private final int danger;

    TrapType(Supplier<Trap> factory, int trapDanger) {
        this.factory = factory;
        this.danger = trapDanger;
    }

    public Trap create() {
        return factory.get();
    }

    public int getDanger() {
        return danger;
    }
}
