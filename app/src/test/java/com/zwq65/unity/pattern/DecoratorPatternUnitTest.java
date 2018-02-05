package com.zwq65.unity.pattern;

import org.junit.Test;

/**
 * ================================================
 * 设计模式——结构型模式之装饰器模式
 * <p>
 * 装饰器模式（Decorator Pattern）允许向一个现有的对象添加新的功能，同时又不改变其结构。这种类型的设计模式属于结构型模式，它是作为现有的类的一个包装。
 * 这种模式创建了一个装饰类，用来包装原有的类，并在保持类方法签名完整性的前提下，提供了额外的功能。
 * <p>
 * 意图：动态地给一个对象添加一些额外的职责。就增加功能来说，装饰器模式相比生成子类更为灵活。
 * 主要解决：一般的，我们为了扩展一个类经常使用继承方式实现，由于继承为类引入静态特征，并且随着扩展功能的增多，子类会很膨胀。
 * <p>
 * Created by NIRVANA on 2017/11/7
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class DecoratorPatternUnitTest {

    @Test
    public void test() {
        //选择英雄
        Hero hero = new BlindMonk("李青");

        Skills skills = new Skills(hero);
        Skills r = new Skill_R(skills, "猛龙摆尾");
        Skills e = new Skill_E(r, "天雷破/摧筋断骨");
        Skills w = new Skill_W(e, "金钟罩/铁布衫");
        Skills q = new Skill_Q(w, "天音波/回音击");
        //学习技能
        q.learnSkills();
    }

    //Component 英雄接口
    public interface Hero {
        //学习技能
        void learnSkills();
    }

    //ConcreteComponent 具体英雄盲僧
    public class BlindMonk implements Hero {

        private String name;

        public BlindMonk(String name) {
            this.name = name;
        }

        @Override
        public void learnSkills() {
            System.out.println(name + "学习了以上技能！");
        }
    }

    //Decorator 技能栏
    public class Skills implements Hero {

        //持有一个英雄对象接口
        private Hero hero;

        public Skills(Hero hero) {
            this.hero = hero;
        }

        @Override
        public void learnSkills() {
            if (hero != null)
                hero.learnSkills();
        }
    }

    //ConreteDecorator 技能：Q
    public class Skill_Q extends Skills {

        private String skillName;

        public Skill_Q(Hero hero, String skillName) {
            super(hero);
            this.skillName = skillName;
        }

        @Override
        public void learnSkills() {
            System.out.println("学习了技能Q:" + skillName);
            super.learnSkills();
        }
    }

    //ConreteDecorator 技能：W
    public class Skill_W extends Skills {

        private String skillName;

        public Skill_W(Hero hero, String skillName) {
            super(hero);
            this.skillName = skillName;
        }

        @Override
        public void learnSkills() {
            System.out.println("学习了技能W:" + skillName);
            super.learnSkills();
        }
    }

    //ConreteDecorator 技能：E
    public class Skill_E extends Skills {

        private String skillName;

        public Skill_E(Hero hero, String skillName) {
            super(hero);
            this.skillName = skillName;
        }

        @Override
        public void learnSkills() {
            System.out.println("学习了技能E:" + skillName);
            super.learnSkills();
        }
    }

    //ConreteDecorator 技能：R
    public class Skill_R extends Skills {

        private String skillName;

        public Skill_R(Hero hero, String skillName) {
            super(hero);
            this.skillName = skillName;
        }

        @Override
        public void learnSkills() {
            System.out.println("学习了技能R:" + skillName);
            super.learnSkills();
        }
    }

}
