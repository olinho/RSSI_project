package model;

import exceptions.WorldBuilderException;

/**
 * Created by sbk on 23.02.17.
 */
public class World {
    private static final double N = 2;
    private int height, width;
    private Robot robot;
    private Tower t1, t2,t3;

    private World(WorldBuilder wb){
        height = wb.height;
        width = wb.width;
        robot = wb.robot;
        t1 = wb.t1;
        t2 = wb.t2;
        t3 = wb.t3;
    }

    public static double getN() {
        return N;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Robot getRobot() {
        return robot;
    }

    public Tower getT1() {
        return t1;
    }

    public Tower getT2() {
        return t2;
    }

    public Tower getT3() {
        return t3;
    }

    public static class WorldBuilder{
        private int height, width;
        private Robot robot;
        private Tower t1, t2,t3;

        public WorldBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public WorldBuilder setWidth(int width) {
            this.width = width;
            return this;
        }

        public WorldBuilder setRobot(Robot robot) {
            this.robot = robot;
            return this;
        }

        public WorldBuilder setT1(Tower t1) {
            this.t1 = t1;
            return this;
        }

        public WorldBuilder setT2(Tower t2) {
            this.t2 = t2;
            return this;
        }

        public WorldBuilder setT3(Tower t3) {
            this.t3 = t3;
            return this;
        }

        public World build() throws WorldBuilderException {
            if(height == 0 || width == 0){
                throw new WorldBuilderException();
            }
            if(t1 == null | t2 == null || t3 == null){
                throw new WorldBuilderException();
            }
            if(robot == null){
                throw new WorldBuilderException();
            }

            return new World(this);
        }
    }
}
