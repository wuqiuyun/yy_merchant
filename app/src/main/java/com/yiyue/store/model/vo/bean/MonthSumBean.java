package com.yiyue.store.model.vo.bean;

import java.util.List;

/**
 * Created by jinyan on 2018/12/21.
 */
public class MonthSumBean {


    @Override
    public String toString() {
        return "MonthSumBean{" +
                "inSum=" + inSum +
                ", outSum=" + outSum +
                ", bits=" + bits +
                ", classifyIn=" + classifyIn +
                ", showfyMonth=" + showfyMonth +
                '}';
    }

    /**
     * inSum : 5498.73
     * outSum : 0
     * maxFly : null
     * classifyIn : [{"inType":1,"name":"注册奖金","inMoney":5000,"percent":0.9093},{"inType":2,"name":"推广佣金","inMoney":137.03,"percent":0.0249},{"inType":4,"name":"服务收入","inMoney":180.85,"percent":0.0328},{"inType":9,"name":"转可提现","inMoney":180.85,"percent":0.0328}]
     * bits : 19
     * showfyMonth : [{"date":"2018-12","sumMoney":5498.73}]
     */

    private double inSum;
    private double outSum;
    private int bits;
    private List<ClassifyInBean> classifyIn;
    private List<ClassifyOutBean> classifyOut;
    private List<ShowfyMonthBean> showfyMonth;
    private MaxFlyBean maxFly;

    public List<ClassifyOutBean> getClassifyOut() {
        return classifyOut;
    }

    public void setClassifyOut(List<ClassifyOutBean> classifyOut) {
        this.classifyOut = classifyOut;
    }

    public MaxFlyBean getMaxFly() {
        return maxFly;
    }

    public void setMaxFly(MaxFlyBean maxFly) {
        this.maxFly = maxFly;
    }

    public double getInSum() {
        return inSum;
    }

    public void setInSum(double inSum) {
        this.inSum = inSum;
    }

    public double getOutSum() {
        return outSum;
    }

    public void setOutSum(double outSum) {
        this.outSum = outSum;
    }



    public int getBits() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
    }

    public List<ClassifyInBean> getClassifyIn() {
        return classifyIn;
    }

    public void setClassifyIn(List<ClassifyInBean> classifyIn) {
        this.classifyIn = classifyIn;
    }

    public List<ShowfyMonthBean> getShowfyMonth() {
        return showfyMonth;
    }

    public void setShowfyMonth(List<ShowfyMonthBean> showfyMonth) {
        this.showfyMonth = showfyMonth;
    }



    public static class ClassifyOutBean {

        /**
         * outType : 6
         * name : 系统清算
         * outMoney : 75.25
         */

        private int outType;
        private String name;
        private double outMoney;

        public int getOutType() {
            return outType;
        }

        public void setOutType(int outType) {
            this.outType = outType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getOutMoney() {
            return outMoney;
        }

        public void setOutMoney(double outMoney) {
            this.outMoney = outMoney;
        }
    }
    public static class ClassifyInBean {
        @Override
        public String toString() {
            return "ClassifyInBean{" +
                    "inType=" + inType +
                    ", name='" + name + '\'' +
                    ", inMoney=" + inMoney +
                    ", percent=" + percent +
                    '}';
        }

        /**
         * inType : 1
         * name : 注册奖金
         * inMoney : 5000
         * percent : 0.9093
         */

        private int inType;
        private String name;
        private double inMoney;
        private double percent;
        private int color;
        private float angle;

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }

        public int getInType() {
            return inType;
        }

        public void setInType(int inType) {
            this.inType = inType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getInMoney() {
            return inMoney;
        }

        public void setInMoney(double inMoney) {
            this.inMoney = inMoney;
        }

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }
    }

    public static class ShowfyMonthBean {
        @Override
        public String toString() {
            return "ShowfyMonthBean{" +
                    "date='" + date + '\'' +
                    ", sumMoney=" + sumMoney +
                    '}';
        }

        /**
         * date : 2018-12
         * sumMoney : 5498.73
         */

        private String date;
        private float sumMoney;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public float getSumMoney() {
            return sumMoney;
        }

        public void setSumMoney(float sumMoney) {
            this.sumMoney = sumMoney;
        }
    }


    public static class MaxFlyBean {


        /**
         * inType : 4
         * name : 服务收入
         * inMoney : 180.85
         * percent : 0.0328
         */

        private int inType;
        private String name;
        private double inMoney;
        private double percent;

        public int getInType() {
            return inType;
        }

        public void setInType(int inType) {
            this.inType = inType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getInMoney() {
            return inMoney;
        }

        public void setInMoney(double inMoney) {
            this.inMoney = inMoney;
        }

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }
    }
}
