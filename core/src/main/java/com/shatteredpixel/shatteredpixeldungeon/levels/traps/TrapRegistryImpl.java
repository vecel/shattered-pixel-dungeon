package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import java.util.HashMap;
import java.util.Map;

public class TrapRegistryImpl implements TrapRegistry {

    private final Map<Class<? extends Trap>, Integer> danger = new HashMap<>();

    public TrapRegistryImpl() {
        danger.put(WornDartTrap.class, 10);
        danger.put(ChillingTrap.class, 10);
        danger.put(ShockingTrap.class, 10);
        danger.put(ToxicTrap.class, 10);
        danger.put(AlarmTrap.class, 10);
        danger.put(ConfusionTrap.class, 10);
        danger.put(FlockTrap.class, 10);
        danger.put(TeleportationTrap.class, 10);
        danger.put(GatewayTrap.class, 10);
        danger.put(GeyserTrap.class, 10);
        danger.put(SummoningTrap.class, 20);
        danger.put(OozeTrap.class, 20);
        danger.put(BurningTrap.class, 20);
        danger.put(PoisonDartTrap.class, 20);
        danger.put(GrippingTrap.class, 20);
        danger.put(FrostTrap.class, 20);
        danger.put(StormTrap.class, 20);
        danger.put(CorrosionTrap.class, 20);
        danger.put(FlashingTrap.class, 20);
        danger.put(WeakeningTrap.class, 20);
        danger.put(ExplosiveTrap.class, 20);
        danger.put(GnollRockfallTrap.class, 20);
        danger.put(CursingTrap.class, 30);
        danger.put(DistortionTrap.class, 30);
        danger.put(GrimTrap.class, 30);
        danger.put(RockfallTrap.class, 30);
        danger.put(GuardianTrap.class, 30);
        danger.put(WarpingTrap.class, 30);
        danger.put(PitfallTrap.class, 30);
        danger.put(BlazingTrap.class, 30);
        danger.put(DisarmingTrap.class, 30);
        danger.put(DisintegrationTrap.class, 30);

        danger.put(TenguDartTrap.class, 0);
    }

    @Override
    public int getDanger(Class<? extends Trap> trapClass) {
        if (!danger.containsKey(trapClass)) {
            throw new IllegalArgumentException("Trap danger is not registered for: " + trapClass.getName());
        }
        return danger.get(trapClass);
    }
}
