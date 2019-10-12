package com.wypuhui.p2p.uploud.data.baihang.util;

import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAccountInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAfterInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanApplyInfoDto;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: liuw
 * @Date: 2019/8/22 16:50
 * @Description:
 */
public class ToBaiHangType {

    public static BigDecimal ofPrice(BigDecimal price) {
        return price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static String conversionToBHLoanPurposeType(String purpose) {
        if ("1".equals(purpose) || "10".equals(purpose)) {
            return "NO_SCENES_LOAN";
        } else if ("3".equals(purpose)) {
            return "DECORATION";
        } else if ("2".equals(purpose) || "5".equals(purpose)) {
            return "COMPREHENSIVE_USE";
        } else if ("6".equals(purpose)) {
            return "EDUCATION";
        } else if ("7".equals(purpose)) {
            return "BUY_CAR";
        } else if ("8".equals(purpose)) {
            return "BUSINESS_MANAGEMENT";
        } else if ("9".equals(purpose)) {
            return "MEDICAL_BEAUTY";
        } else {
            return "DAILY_CONSUMPTION";
        }
    }

    public static String conversionToBHLoanGuaranteeType(String tradeMark) {
        if ("1".equals(tradeMark) || "2".equals(tradeMark)) {
            return "MORTGAGE";
        } else if ("4".equals(tradeMark)) {
            return "CREDIT";
        } else {
            return "OTHER";
        }
    }

    public static void checkedApplyInfoBeanMustParamNull(LoanApplyInfoDto applyInfo) {
        if (StringUtils.isBlank(applyInfo.getName()) ||
                StringUtils.isBlank(applyInfo.getPid()) ||
                StringUtils.isBlank(applyInfo.getMobile()) ||
                applyInfo.getQueryReason() == null ||
                applyInfo.getLoanPurpose() == null ||
                applyInfo.getGuaranteeType() == null ||
                applyInfo.getCustomType() == null ||
                applyInfo.getApplyAmount() == null){
            throw new RuntimeException("loanApplyInfoDto property@notNull exist null");
        }
    }

    public static void checkedAfterInfoBeanMustParamNull(LoanAfterInfoDto loanAfterInfo) {
        if (StringUtils.isBlank(loanAfterInfo.getName()) ||
                StringUtils.isBlank(loanAfterInfo.getPid()) ||
                StringUtils.isBlank(loanAfterInfo.getMobile()) ||
                StringUtils.isBlank(loanAfterInfo.getLoanId()) ||
                StringUtils.isBlank(loanAfterInfo.getReqID()) ||
                StringUtils.isBlank(loanAfterInfo.getOpCode()) ||
                StringUtils.isBlank(loanAfterInfo.getUploadTs()) ||
                StringUtils.isBlank(loanAfterInfo.getTermStatus()) ||
                StringUtils.isBlank(loanAfterInfo.getTargetRepaymentDate()) ||
                StringUtils.isBlank(loanAfterInfo.getStatusConfirmAt()) ||
                loanAfterInfo.getTermNo() == null ||
                loanAfterInfo.getPlannedPayment() == null ||
                loanAfterInfo.getTargetRepayment() == null ||
                loanAfterInfo.getRealRepayment() == null ||
                loanAfterInfo.getOverdueAmount() == null ||
                loanAfterInfo.getRemainingAmount() == null ||
                loanAfterInfo.getLoanStatus() == null) {
            throw new RuntimeException("LoanAfterInfoDto property@notNull exist null");
        }
    }

    public static void checkedAccountInfoBeanMustParamNull(LoanAccountInfoDto accountInfoDto) {
        if (StringUtils.isBlank(accountInfoDto.getName()) ||
                StringUtils.isBlank(accountInfoDto.getPid()) ||
                StringUtils.isBlank(accountInfoDto.getMobile()) ||
                StringUtils.isBlank(accountInfoDto.getLoanId()) ||
                StringUtils.isBlank(accountInfoDto.getReqID()) ||
                StringUtils.isBlank(accountInfoDto.getOpCode()) ||
                StringUtils.isBlank(accountInfoDto.getUploadTs()) ||
                StringUtils.isBlank(accountInfoDto.getApplyDate()) ||
                StringUtils.isBlank(accountInfoDto.getAccountOpenDate()) ||
                StringUtils.isBlank(accountInfoDto.getIssueDate()) ||
                StringUtils.isBlank(accountInfoDto.getDueDate()) ||
                StringUtils.isBlank(accountInfoDto.getFirstRepaymentDate()) ||
                accountInfoDto.getGuaranteeType() == null ||
                accountInfoDto.getLoanPurpose() == null ||
                accountInfoDto.getLoanAmount() == null ||
                accountInfoDto.getTotalTerm() == null ||
                accountInfoDto.getTargetRepayDateType() == null ||
                accountInfoDto.getTermPeriod() == null ||
                accountInfoDto.getGracePeriod() == null) {
            throw new RuntimeException("LoanAccountInfoDto property@notNull exist null");
        }
    }
}
