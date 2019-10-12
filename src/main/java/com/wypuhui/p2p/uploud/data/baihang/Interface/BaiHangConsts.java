package com.wypuhui.p2p.uploud.data.baihang.Interface;

/**
 * @Author: liuw
 * @Date: 2019/8/20 17:42
 * @Description:
 */
public class BaiHangConsts {

    public static String SUCCESS = "Y";

    public static String ERROR = "N";

    public enum QueryReason {       //查询原因

        CREDIT_APPROVAL(1),     //授信审批

        LOANING_MANAGER(2),     //贷中管理

        LOAN_AFTER_MANAGER(3),  //贷后管理

        PERSONAL_QUERY(4),      //个人查询

        ABNORMAL_DEAL(5),       //异常处理

        GUARANTEE_QUERY(6);     //担保查询

        private Integer type;

        private QueryReason(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }

    }

    public enum LoanGuaranteeType {     //贷款担保类型
        CREDIT(1),      //信用
        MORTGAGE(2),    //抵押
        PLEDGE(3),      //质押
        GUARANTEE(4),   //保证
        COMBINATION(5), //组合
        OTHER(6);      //其他

        private Integer type;

        LoanGuaranteeType(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }


    public enum CreditGuaranteeType {     //授信担保类型
        CREDIT(1),      //信用
        MORTGAGE(2),    //抵押
        PLEDGE(3),      //质押
        GUARANTEE(4);  //保证

        private Integer type;

        CreditGuaranteeType(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }

    public enum LoanPurpose {     //贷款用途
        NO_SCENES_LOAN(1),          //无场景贷款
        EDUCATION(2),               //教育
        MEDICAL_BEAUTY(3),          //医美
        RENTING_HOUSE(4),           //租房
        DIGITAL(5),                 //数码
        BUY_CAR(6),                 //买车
        DECORATION(7),              //装修
        TOURISM(8),                 //旅游
        AGRICULTURAL_PRODUCTION(9), //农业生产
        BUSINESS_MANAGEMENT(10),    //企业经营
        COMPREHENSIVE_USE(11),      //综合用款
        SHOPPING(12),               //购物
        DAILY_CONSUMPTION(13),      //日常消费
        OTHER(99);                  //其他

        private Integer type;

        LoanPurpose(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }

    public enum CustomType {     //客户类型
        STUDENTS(1),           //在校学生
        OFFICERS(2),           //在职人员
        SELF_EMPLOYED(3),      //自雇人员
        OTHER_PEOPLE(4),       //其他人士
        OTHER(5);              //未知

        private Integer type;

        CustomType(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }

    public enum TargerRepayDateType {     //账单日类型
        FIXED_CYCLE(1),            //固定周期类型
        FIXED_DATE(2);            //固定日期类型

        private Integer type;

        TargerRepayDateType(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }

    public enum DeviceType {     //终端类型
        PHONE(1),               //手机
        COMPUTER(2),            //电脑
        PAD(3),                 //ipad
        OTHER(99);              //其他

        private Integer type;

        DeviceType(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }

    public enum OsName {     //终端类型
        IOS(1),               //IOS
        ANDRIOD(2),           //andriod
        WIN_PHONE(3),         //winPhone
        BLACK_BERRY(4),       //blackBerry
        SYBIAN(5),            //sybian
        WINDOWS(6),           //windows
        MAC(7),               //mac
        OTHER(99);            //其他

        private Integer type;

        OsName(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }


    public enum TermStatus {    //本期还款状态
        NORMAL {
            public String getValue() {
                return "normal";
            }
        },//正常
        OVERDUE {
            public String getValue() {
                return "overdue";
            }
        };//逾期

        public abstract String getValue();
    }


    public enum OpCode {    //操作代码
        A {
            public String getValue() {
                return "A";
            }
        },//新增
        M {
            public String getValue() {
                return "M";
            }
        }, //修改
        D {
            public String getValue() {
                return "D";
            }
        };//删除

        public abstract String getValue();
    }


    public enum LoanStatus {     //本笔贷款状态
        NORMAL(1),             //正常
        OVERDUE(2),            //逾期
        SETTLE(3),             //结清
        CANCEL(4);             //撤销

        private Integer type;

        LoanStatus(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }

    public enum RevolvingBillingStatus {     //本期账单状态
        NORMAL(1),             //正常
        OVERDUE(2),            //逾期
        SETTLE(3);             //结清

        private Integer type;

        RevolvingBillingStatus(Integer type) {
            this.type = type;
        }

        public Integer val() {
            return type;
        }
    }

}
