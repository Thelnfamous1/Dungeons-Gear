package com.infamous.dungeons_gear.goals;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.passive.BeeEntity;

import java.util.EnumSet;

import static com.infamous.dungeons_gear.capabilities.summoning.SummoningHelper.getSummoner;
import static com.infamous.dungeons_gear.goals.GoalUtils.shouldAttackEntity;

public class BeeOwnerHurtByTargetGoal extends TargetGoal {
    private final BeeEntity beeEntity;
    private LivingEntity attacker;
    private int timestamp;

    public BeeOwnerHurtByTargetGoal(BeeEntity beeEntity) {
        super(beeEntity, false);
        this.beeEntity = beeEntity;
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean shouldExecute() {
        LivingEntity owner = getSummoner(this.beeEntity);
        if (owner == null) {
            return false;
        } else {
            this.attacker = owner.getRevengeTarget();
            int revengeTimer = owner.getRevengeTimer();
            return revengeTimer != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && shouldAttackEntity(this.attacker, owner);
        }
    }

    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacker);
        LivingEntity owner = getSummoner(this.beeEntity);
        if (owner != null) {
            this.timestamp = owner.getRevengeTimer();
        }

        super.startExecuting();
    }
}
