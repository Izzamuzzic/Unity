package com.zwq65.unity.pattern;

import org.junit.Test;

/**
 * ================================================
 * 设计模式——结构型模式之适配器模式
 * <p>
 * 适配器模式（Adapter Pattern）是作为两个不兼容的接口之间的桥梁。这种类型的设计模式属于结构型模式，它结合了两个独立接口的功能。
 * <p>
 * 意图：将一个类的接口转换成客户希望的另外一个接口。适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。
 * 主要解决：主要解决在软件系统中，常常要将一些"现存的对象"放到新的环境中，而新环境要求的接口是现对象不能满足的。
 * <p>
 * Created by NIRVANA on 2017/11/7
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class AdapterPatternUnitTest {

    @Test
    public void test() {
        AudioPlayer audioPlayer = new AudioPlayer();

        audioPlayer.play("mp3", "beyond the horizon.mp3");
        audioPlayer.play("mp4", "alone.mp4");
        audioPlayer.play("vlc", "far far away.vlc");
        audioPlayer.play("avi", "mind me.avi");
    }

    //媒体播放器
    public interface MediaPlayer {
        void play(String audioType, String fileName);
    }

    //更高级的媒体播放器
    public interface AdvancedMediaPlayer {
        void playVlc(String fileName);

        void playMp4(String fileName);
    }

    public class VlcPlayer implements AdvancedMediaPlayer {
        @Override
        public void playVlc(String fileName) {
            System.out.println("Playing vlc file. Name: " + fileName);
        }

        @Override
        public void playMp4(String fileName) {
            //什么也不做
        }
    }

    public class Mp4Player implements AdvancedMediaPlayer {

        @Override
        public void playVlc(String fileName) {
            //什么也不做
        }

        @Override
        public void playMp4(String fileName) {
            System.out.println("Playing mp4 file. Name: " + fileName);
        }
    }

    /**
     * 创建适配器类
     */
    public class MediaAdapter implements MediaPlayer {

        AdvancedMediaPlayer advancedMusicPlayer;

        MediaAdapter(String audioType) {
            if (audioType.equalsIgnoreCase("vlc")) {
                advancedMusicPlayer = new VlcPlayer();
            } else if (audioType.equalsIgnoreCase("mp4")) {
                advancedMusicPlayer = new Mp4Player();
            }
        }

        @Override
        public void play(String audioType, String fileName) {
            if (audioType.equalsIgnoreCase("vlc")) {
                advancedMusicPlayer.playVlc(fileName);
            } else if (audioType.equalsIgnoreCase("mp4")) {
                advancedMusicPlayer.playMp4(fileName);
            }
        }
    }

    public class AudioPlayer implements MediaPlayer {
        MediaAdapter mediaAdapter;

        @Override
        public void play(String audioType, String fileName) {

            //播放 mp3 音乐文件的内置支持
            if (audioType.equalsIgnoreCase("mp3")) {
                System.out.println("Playing mp3 file. Name: " + fileName);
            }
            //mediaAdapter 提供了播放其他文件格式的支持
            else if (audioType.equalsIgnoreCase("vlc")
                    || audioType.equalsIgnoreCase("mp4")) {
                mediaAdapter = new MediaAdapter(audioType);
                mediaAdapter.play(audioType, fileName);
            } else {
                System.out.println("Invalid media. " +
                        audioType + " format not supported");
            }
        }
    }
}
