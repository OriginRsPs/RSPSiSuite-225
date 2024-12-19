package com.rspsi.plugin.loader;

import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.val;
import org.apache.commons.compress.utils.Lists;

import com.jagex.cache.anim.Animation;
import com.jagex.cache.loader.anim.AnimationDefinitionLoader;
import com.jagex.io.Buffer;

public class AnimationDefinitionLoaderOSRS extends AnimationDefinitionLoader {


    private int count;
    private Animation[] animations;

    @Override
    public void init(Archive archive) {
        val highestId = Arrays.stream(archive.fileIds()).max().getAsInt();
        animations = new Animation[highestId + 1];
        for (File file : archive.files()) {
            if (file != null && file.getData() != null) {
                animations[file.getId()] = decode(new Buffer(file.getData()));
            }
        }

    }

    @Override
    public void init(byte[] data) {
        Buffer buffer = new Buffer(data);
        count = buffer.readUShort();

        if (animations == null) {
            animations = new Animation[count];
        }

        for (int id = 0; id < count; id++) {

            animations[id] = decode(buffer);
        }
    }

    boolean rev220FrameSounds = true;
    boolean rev226 = true;
    public Multimap<Integer, Sound> frameSounds = ArrayListMultimap.create();

    protected Animation decode(Buffer buffer) {
        while (true) {
            int opcode = buffer.readUByte();
            if (opcode == 0) {
                return null;
            }

            this.decodeNext(buffer, opcode);
        }
    }

    void decodeNext(Buffer buffer, int opcode) {
        Animation animation = new Animation();
        if (opcode == 1) { //sec
            int frameCount = buffer.readUShort();

            int[] delays = new int[frameCount];

            for (int index = 0; index < frameCount; index++) {
                delays[index] = buffer.readUShort();
            }

            int[] primaryFrameIds = new int[frameCount];
            int[] secondaryFrames = new int[frameCount];
            for (int index = 0; index < frameCount; index++) {
                primaryFrameIds[index] = buffer.readUShort();
                secondaryFrames[index] = -1;
            }

            for (int index = 0; index < frameCount; index++) {
                primaryFrameIds[index] += buffer.readUShort() << 16;
            }

            animation.setFrameCount(frameCount);
            animation.setPrimaryFrames(primaryFrameIds);
            animation.setSecondaryFrames(secondaryFrames);
            animation.setDurations(delays);
        } else if (opcode == 2) {
            animation.setLoopOffset(buffer.readUShort());
        } else if (opcode == 3) {
            int count = buffer.readUByte();
            int[] interleaveOrder = new int[count + 1];
            for (int index = 0; index < count; index++) {
                interleaveOrder[index] = buffer.readUByte();
            }

            interleaveOrder[count] = 9999999;
            animation.setInterleaveOrder(interleaveOrder);
        } else if (opcode == 4) {
            animation.setStretches(true);
        } else if (opcode == 5) {
            animation.setPriority(buffer.readUByte());
        } else if (opcode == 6) {
            animation.setPlayerOffhand(buffer.readUShort());
        } else if (opcode == 7) {
            animation.setPlayerMainhand(buffer.readUShort());
        } else if (opcode == 8) {
            animation.setMaximumLoops(buffer.readUByte());
        } else if (opcode == 9) {
            animation.setAnimatingPrecedence(buffer.readUByte());
        } else if (opcode == 10) {
            animation.setWalkingPrecedence(buffer.readUByte());
        } else if (opcode == 11) {
            animation.setReplayMode(buffer.readUByte());
        } else if (opcode == 12) {
            int len = buffer.readUByte();

            for (int i = 0; i < len; i++) {
                buffer.readUShort();
            }

            for (int i = 0; i < len; i++) {
                var frameIds = buffer.readUShort() << 16;
            }
        } else if (opcode == 13 && !rev226) {
            int len = buffer.readUByte();

            for (int i = 0; i < len; i++) {
                frameSounds.put(i, this.readFrameSound(buffer));
            }

        } else if (opcode == (rev226 ? 13 : 14)) {
            int skeletalId = buffer.readInt();
        } else if (opcode == (rev226 ? 14 : 15)) {
            int count = buffer.readUnsignedShort();
            for (int index = 0; index < count; index++) {
                int frame = buffer.readUnsignedShort();
                frameSounds.put(frame, this.readFrameSound(buffer));
            }
        } else if (opcode == (rev226 ? 15 : 16)) {
            int rangeBegin = buffer.readUnsignedShort();
            int rangeEnd = buffer.readUnsignedShort();
        } else if (opcode == 17) {
            boolean[] animayaMasks = new boolean[256];
            Arrays.fill(animayaMasks, false);
            int count = buffer.readUByte();
            for (int index = 0; index < count; index++) {
                animayaMasks[buffer.readUByte()] = true;
            }
        } else {
            System.err.println("Error unrecognised seq config code: " + opcode);
        }

        if (animation.getFrameCount() == 0) {
            animation.setFrameCount(1);
            int[] primaryFrames = new int[1];
            primaryFrames[0] = -1;
            int[] secondaryFrames = new int[1];
            secondaryFrames[0] = -1;
            int[] durations = new int[1];
            durations[0] = -1;
            animation.setPrimaryFrames(primaryFrames);
            animation.setSecondaryFrames(secondaryFrames);
            animation.setDurations(durations);
        }

        if (animation.getAnimatingPrecedence() == -1) {
            animation.setAnimatingPrecedence(animation.getInterleaveOrder() == null ? 0 : 2);
        }

        if (animation.getWalkingPrecedence() == -1) {
            animation.setWalkingPrecedence(animation.getInterleaveOrder() == null ? 0 : 2);
        }
    }

    private Sound readFrameSound(Buffer stream) {
        int id;
        int loops;
        int location;
        int retain;
        int weight = -1;
        if (!rev220FrameSounds) {
            int bits = stream.read24BitInt();
            location = bits & 15;
            id = bits >> 8;
            loops = bits >> 4 & 7;
            retain = 0;
        } else {
            id = stream.readUShort();
            if (rev226) {
                weight = stream.readUByte();
            }
            loops = stream.readUByte();
            location = stream.readUByte();
            retain = stream.readUByte();
        }

        if (id >= 1 && loops >= 1 && location >= 0 && retain >= 0) {
            return new AnimationDefinitionLoaderOSRS.Sound(id, loops, location, retain, weight);
        } else {
            return null;
        }
    }


    record Sound(int id, int loops, int location, int retain, int weight) {

    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public Animation forId(int id) {
        if (id < 0 || id > animations.length)
            id = 0;
        return animations[id];
    }


}
