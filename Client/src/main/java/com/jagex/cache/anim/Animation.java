package com.jagex.cache.anim;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jagex.cache.loader.anim.FrameLoader;

public class Animation {

	/**
	 * The animation precedence (will this animation 'override' other animations or
	 * will this one yield).
	 */
	public int moveStyle = -1;

	/**
	 * The duration of each frame in this Animation.
	 */
	public int[] delays;

	/**
	 * The amount of frames in this Animation.
	 */
	public int frameCount;

	public int[] interleaveOrder;

	/**
	 * The amount of frames subtracted to restart the loop.
	 */
	public int frameStep = -1;

	/**
	 * The maximum times this animation will loop.
	 */
	public int loopCount = 99;

	/**
	 * Indicates whether or not this player's shield will be displayed whilst this
	 * animation is played.
	 */
	public int leftHandItem = -1;

	/**
	 * Indicates whether or not this player's weapon will be displayed whilst this
	 * animation is played.
	 */
	public int rightHandItem = -1;

	/**
	 * The primary frame ids of this Animation.
	 */
	public int[] primaryFrames;
	public int[] masks;
	public boolean[] booleanMasks;
	public int rangeBegin;
	public int rangeEnd;
	public int[] chatFrameIds;
	public Multimap<Integer, Sound> frameSounds = ArrayListMultimap.create();
	public int skeletalId = -1;
	public int priority = 5;
	public int delayType = 2;

	/**
	 * The secondary frame ids of this Animation.
	 */
	public int[] secondaryFrames;

	public boolean stretches = false;

	/**
	 * The walking precedence (will the player be prevented from moving or can they
	 * continue).
	 */
	public int idleStyle = -1;


	public int duration(int frameId) {
		int duration = delays[frameId];
		if (duration == 0) {
			Frame frame = FrameLoader.lookup(primaryFrames[frameId]);

			if (frame != null) {
				duration = delays[frameId] = frame.getDuration();
			}
		}

		return duration == 0 ? 1 : duration;
	}

	/**
	 * Gets the animation precedence (will this animation 'override' other
	 * animations or will this one yield).
	 */
	public int getMoveStyle() {
		return moveStyle;
	}

	public int[] getDelays() {
		return delays;
	}

	/**
	 * Gets the amount of frames in this Animation.
	 * 
	 * @return The amount of frames.
	 */
	public int getFrameCount() {
		return frameCount;
	}

	public int[] getInterleaveOrder() {
		return interleaveOrder;
	}

	/**
	 * Gets the amount of frames subtracted to restart the loop.
	 * 
	 * @return The loop offset.
	 */
	public int getFrameStep() {
		return frameStep;
	}

	/**
	 * Gets the maximum times this animation will loop.
	 * 
	 * @return The maximum loop count.
	 */
	public int getLoopCount() {
		return loopCount;
	}

	/**
	 * Returns whether or not this player's shield will be displayed whilst this
	 * animation is played.
	 */
	public int getPlayerShieldDelta() {
		return leftHandItem;
	}

	/**
	 * Returns whether or not this player's weapon will be displayed whilst this
	 * animation is played.
	 */
	public int getPlayerWeaponDelta() {
		return rightHandItem;
	}

	/**
	 * Gets the primary frame ids of this Animation.
	 * 
	 * @return The primary frame ids.
	 */
	public int getPrimaryFrame(int index) {
		return primaryFrames[index];
	}

	/**
	 * Gets the priority of this Animation.
	 * 
	 * @return The priority.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Gets the replay mode of this Animation.
	 * 
	 * @return The replay mode.
	 */
	public int getDelayType() {
		return delayType;
	}

	/**
	 * Gets the secondary frame ids of this Animation.
	 * 
	 * @return The secondary frame ids.
	 */
	public int getSecondaryFrame(int index) {
		return secondaryFrames[index];
	}

	/**
	 * Gets the walking precedence (will the player be prevented from moving or can
	 * they continue).
	 * 
	 * @return The walking precedence.
	 */
	public int getIdleStyle() {
		return idleStyle;
	}

	public boolean stretches() {
		return stretches;
	}

	public void setMoveStyle(int moveStyle) {
		this.moveStyle = moveStyle;
	}

	public void setDelays(int[] delays) {
		this.delays = delays;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	public void setInterleaveOrder(int[] interleaveOrder) {
		this.interleaveOrder = interleaveOrder;
	}

	public void setFrameStep(int frameStep) {
		this.frameStep = frameStep;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}

	public void setLeftHandItem(int leftHandItem) {
		this.leftHandItem = leftHandItem;
	}

	public void setRightHandItem(int rightHandItem) {
		this.rightHandItem = rightHandItem;
	}

	public void setPrimaryFrames(int[] primaryFrames) {
		this.primaryFrames = primaryFrames;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setDelayType(int delayType) {
		this.delayType = delayType;
	}

	public void setSecondaryFrames(int[] secondaryFrames) {
		this.secondaryFrames = secondaryFrames;
	}

	public void setStretches(boolean stretches) {
		this.stretches = stretches;
	}

	public void setIdleStyle(int idleStyle) {
		this.idleStyle = idleStyle;
	}
	
	

}