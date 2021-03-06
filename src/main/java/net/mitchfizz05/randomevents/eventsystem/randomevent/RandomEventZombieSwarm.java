package net.mitchfizz05.randomevents.eventsystem.randomevent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.mitchfizz05.randomevents.eventsystem.EventDifficulty;
import net.mitchfizz05.randomevents.eventsystem.ExecuteEventException;
import net.mitchfizz05.randomevents.eventsystem.component.CDifficulty;
import net.mitchfizz05.randomevents.eventsystem.component.CPlayerEvent;
import net.mitchfizz05.randomevents.eventsystem.component.CRandomPlayer;
import net.mitchfizz05.randomevents.eventsystem.component.CWorldTimer;
import net.mitchfizz05.randomevents.util.MobSpawner;
import net.mitchfizz05.randomevents.util.TimeHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A bunch of zombies spawn near the player, targeting them.
 */
public class RandomEventZombieSwarm extends RandomEvent implements MobSpawner.IMobSpawnEvent
{
    private List<Item> helmets = new ArrayList<Item>() {{
        add(Items.IRON_HELMET);
        add(Items.GOLDEN_HELMET);
        add(Items.DIAMOND_HELMET);
        add(null);
    }};

    public RandomEventZombieSwarm()
    {
        super("zombie_swarm");

        ((CDifficulty) getComponent(CDifficulty.class)).difficulty = EventDifficulty.VERY_BAD;

        addComponent(new CWorldTimer(this, TimeHelper.hrsToSecs(1), TimeHelper.hrsToSecs(3)));
        addComponent(new CRandomPlayer());
        addComponent(new CPlayerEvent());
    }

    @Override
    public void execute(@Nonnull World world, EntityPlayer player) throws ExecuteEventException
    {
        super.execute(world, player);

        MobSpawner.MobSpawnEventParameters parameters = new MobSpawner.MobSpawnEventParameters(12, 16, 36);
        MobSpawner.execute(this, parameters, world, player);
    }

    @Override
    public Entity getEntity(World world, EntityPlayer player)
    {
        EntityZombie zombie = new EntityZombie(world);
        zombie.replaceItemInInventory(103, new ItemStack(getHelmet(), 1));
        zombie.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20 * 60 * 15)); // 15 minutes of speed.
        zombie.setBreakDoorsAItask(true); // Make doors breakable
        zombie.setAttackTarget(player); // Target the player
        zombie.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(80f); // Extend follow distance

        return zombie;
    }

    protected Item getHelmet()
    {
        return helmets.get(ThreadLocalRandom.current().nextInt(0, helmets.size()));
    }
}
