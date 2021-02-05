package org.wxw.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wxw
 */
@Getter
@Setter
@ToString
public class Product {


    /**公司名称：	中英人寿保险有限公司*/
    private String companyName;
    /**产品名称：	中英附加团体意外伤害保险（B款）*/
    private String productName;
    /**产品类别：	意外伤害保险-意外伤害保险*/
    private String productType;
    /**设计类型：	传统型产品*/
    private String designType;
    /**产品特殊属性：	无*/
    private String specialFlag;
    /**承保方式：	团体*/
    private String saleType;
    /**保险期间类型：	短期（一年及一年以下）*/
    private String insuranCecycle;
    /**产品交费方式：	分期交费一次性交费兼有*/
    private String  payintv;
    /**产品条款文字编码：	中英人寿[2014]意外伤害保险002号*/
    private String clauseTextCode;
    /**产品销售状态：	停用*/
    private String saleStatus;
    /**条款网页下载地址*/
    private String clauseWebUrl;
    /**条款本地存储地址*/
    private String clauseSavePath;


}
