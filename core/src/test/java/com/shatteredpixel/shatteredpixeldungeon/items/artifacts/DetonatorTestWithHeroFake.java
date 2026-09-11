package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.fakes.HeroFake;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class DetonatorWithHeroFakeTest {

    private Detonator detonator;
    private HeroFake hero;

    @BeforeEach
    void setUp() {
        hero = new HeroFake();
        detonator = spy(new Detonator());
    }

    @Nested
    class Recharging {

        @Test
        void attaches_recharging_buff_to_hero() {
            detonator.activate(hero);

            assertTrue(hero.hasBuff(Detonator.DetonatorRecharge.class));

            Detonator.DetonatorRecharge buff = hero.getBuff(Detonator.DetonatorRecharge.class);

            assertNotNull(buff);
            assertEquals(1f, buff.getChargingFactor());
        }

        @Test
        void detaches_recharging_buff_when_unequipped() {
            detonator.activate(hero);
            detonator.doUnequip(hero, false, true);

            assertFalse(hero.hasBuff(Detonator.DetonatorRecharge.class));
            assertNull(detonator.passiveBuff);
        }
        
        @Test
        void attaches_recharging_buff_when_unequipped_and_hero_has_handy_detonator_talent() {
            hero.withTalent(Talent.HANDY_DETONATOR, 1);
            detonator.doUnequip(hero, false, true);

            assertTrue(hero.hasBuff(Detonator.DetonatorRecharge.class));

            Detonator.DetonatorRecharge buff = hero.getBuff(Detonator.DetonatorRecharge.class);
            assertNotNull(detonator.passiveBuff);
            assertNotNull(buff);
            assertSame(buff, detonator.passiveBuff);
            assertEquals(0.25f, buff.getChargingFactor());
        }
    }

    @Nested
    class Actions {

        private final List<String> commonActions = List.of(Item.AC_DROP, Item.AC_THROW);
        private final String equipAction = EquipableItem.AC_EQUIP;
        private final String unequipAction = EquipableItem.AC_UNEQUIP;
        private final List<String> detonatorActions = List.of(Detonator.AC_ACTIVATE, Detonator.AC_STORE_TRAP, Detonator.AC_SET_TRAP);

        @Test
        void has_all_actions_when_equipped() {
            doReturn(true).when(detonator).isEquipped(hero);

            List<String> actions = detonator.actions(hero);

            assertTrue(actions.containsAll(commonActions));
            assertTrue(actions.contains(unequipAction));
            assertTrue(actions.containsAll(detonatorActions));
        }
        
        @Test
        void has_equip_action_when_unequipped() {
            List<String> actions = detonator.actions(hero);

            assertTrue(actions.containsAll(commonActions));
            assertTrue(actions.contains(equipAction));
            assertTrue(Collections.disjoint(actions, detonatorActions));
        }
        
        @Test
        void has_usable_actions_when_unequipped_and_hero_has_handy_detonator_talent() {
            hero.withTalent(Talent.HANDY_DETONATOR, 1);

            List<String> actions = detonator.actions(hero);

            assertTrue(actions.containsAll(commonActions));
            assertTrue(actions.contains(equipAction));
            assertTrue(actions.containsAll(detonatorActions));
        }
    }
}