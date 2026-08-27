package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import java.util.HashMap;
import java.util.Map;

public class TrapRegistryImpl implements TrapRegistry {

    private final Map<Class<? extends Trap>, TrapDefinition> registry = new HashMap<>();

    public TrapRegistryImpl() {
        registry.put(WornDartTrap.class, new TrapDefinition(WornDartTrap::new, 10));
        registry.put(ChillingTrap.class, new TrapDefinition(ChillingTrap::new, 10));
        registry.put(ShockingTrap.class, new TrapDefinition(ShockingTrap::new, 10));
        registry.put(ToxicTrap.class, new TrapDefinition(ToxicTrap::new, 10));
        registry.put(AlarmTrap.class, new TrapDefinition(AlarmTrap::new, 10));
        registry.put(ConfusionTrap.class, new TrapDefinition(ConfusionTrap::new, 10));
        registry.put(FlockTrap.class, new TrapDefinition(FlockTrap::new, 10));
        registry.put(TeleportationTrap.class, new TrapDefinition(TeleportationTrap::new, 10));
        registry.put(GatewayTrap.class, new TrapDefinition(GatewayTrap::new, 10));
        registry.put(GeyserTrap.class, new TrapDefinition(GeyserTrap::new, 10));

        registry.put(SummoningTrap.class, new TrapDefinition(SummoningTrap::new, 20));
        registry.put(OozeTrap.class, new TrapDefinition(OozeTrap::new, 20));
        registry.put(BurningTrap.class, new TrapDefinition(BurningTrap::new, 20));
        registry.put(PoisonDartTrap.class, new TrapDefinition(PoisonDartTrap::new, 20));
        registry.put(GrippingTrap.class, new TrapDefinition(GrippingTrap::new, 20));
        registry.put(FrostTrap.class, new TrapDefinition(FrostTrap::new, 20));
        registry.put(StormTrap.class, new TrapDefinition(StormTrap::new, 20));
        registry.put(CorrosionTrap.class, new TrapDefinition(CorrosionTrap::new, 20));
        registry.put(FlashingTrap.class, new TrapDefinition(FlashingTrap::new, 20));
        registry.put(WeakeningTrap.class, new TrapDefinition(WeakeningTrap::new, 20));
        registry.put(ExplosiveTrap.class, new TrapDefinition(ExplosiveTrap::new, 20));
        registry.put(GnollRockfallTrap.class, new TrapDefinition(GnollRockfallTrap::new, 20));

        registry.put(CursingTrap.class, new TrapDefinition(CursingTrap::new, 30));
        registry.put(DistortionTrap.class, new TrapDefinition(DistortionTrap::new, 30));
        registry.put(GrimTrap.class, new TrapDefinition(GrimTrap::new, 30));
        registry.put(RockfallTrap.class, new TrapDefinition(RockfallTrap::new, 30));
        registry.put(GuardianTrap.class, new TrapDefinition(GuardianTrap::new, 30));
        registry.put(WarpingTrap.class, new TrapDefinition(WarpingTrap::new, 30));
        registry.put(PitfallTrap.class, new TrapDefinition(PitfallTrap::new, 30));
        registry.put(BlazingTrap.class, new TrapDefinition(BlazingTrap::new, 30));
        registry.put(DisarmingTrap.class, new TrapDefinition(DisarmingTrap::new, 30));
        registry.put(DisintegrationTrap.class, new TrapDefinition(DisintegrationTrap::new, 30));

        registry.put(TenguDartTrap.class, new TrapDefinition(TenguDartTrap::new, 0));
    }

    @Override
    public int getDanger(Class<? extends Trap> trapClass) {
        if (!registry.containsKey(trapClass)) {
            throw new IllegalArgumentException("Trap danger is not registered for: " + trapClass.getName());
        }
        return registry.get(trapClass).getDanger();
    }

    @Override
    public Trap create(Class<? extends Trap> trapClass) {
        if (!registry.containsKey(trapClass)) {
            throw new IllegalArgumentException("Trap factory is not registered for: " + trapClass.getName());
        }
        return registry.get(trapClass).getFactory().get();
    }
}
