/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50540
Source Host           : 127.0.0.1:3306
Source Database       : tdpro

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2019-11-21 15:15:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for p_advert
-- ----------------------------
DROP TABLE IF EXISTS `p_advert`;
CREATE TABLE `p_advert` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '标题',
  `ort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `imgPath` varchar(100) DEFAULT NULL COMMENT '图片地址',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  `isDel` int(2) NOT NULL DEFAULT '0' COMMENT '现实状态(-1:下架 0:正常)',
  `createTime` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='公告表';

-- ----------------------------
-- Records of p_advert
-- ----------------------------

-- ----------------------------
-- Table structure for p_collect
-- ----------------------------
DROP TABLE IF EXISTS `p_collect`;
CREATE TABLE `p_collect` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '会员Id',
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '产品id',
  `createTime` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏表';

-- ----------------------------
-- Records of p_collect
-- ----------------------------

-- ----------------------------
-- Table structure for p_goods
-- ----------------------------
DROP TABLE IF EXISTS `p_goods`;
CREATE TABLE `p_goods` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `classId` bigint(11) NOT NULL DEFAULT '0' COMMENT '分类id',
  `goodsName` varchar(100) NOT NULL COMMENT '商品名称',
  `zoneType` int(1) NOT NULL DEFAULT '0' COMMENT '专区类型(0:普通专区 1:会员专区 2:兑换专区)',
  `isSuit` int(1) NOT NULL DEFAULT '0' COMMENT '是否是套装(0:不是 1:是)',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品价格',
  `title` varchar(100) DEFAULT NULL COMMENT '简介',
  `specification` varchar(50) DEFAULT NULL COMMENT '规格',
  `hostImg` varchar(100) DEFAULT NULL COMMENT '主图地址',
  `details` varchar(250) DEFAULT NULL COMMENT '商品详情',
  `isDel` int(1) NOT NULL DEFAULT '0' COMMENT '-1：删除 0 正常',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表';

-- ----------------------------
-- Records of p_goods
-- ----------------------------

-- ----------------------------
-- Table structure for p_goods_class
-- ----------------------------
DROP TABLE IF EXISTS `p_goods_class`;
CREATE TABLE `p_goods_class` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `className` varchar(50) NOT NULL COMMENT '分类名',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `explain` varchar(200) DEFAULT NULL COMMENT '说明',
  `iconPath` varchar(200) DEFAULT NULL COMMENT '图标地址',
  `isShow` int(1) NOT NULL DEFAULT '0' COMMENT '是否显示(0:否 1:是)',
  `isDel` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除(-1:是 0:否)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品分类表';

-- ----------------------------
-- Records of p_goods_class
-- ----------------------------

-- ----------------------------
-- Table structure for p_goods_exchange
-- ----------------------------
DROP TABLE IF EXISTS `p_goods_exchange`;
CREATE TABLE `p_goods_exchange` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '商品id',
  `voucherId` bigint(11) NOT NULL DEFAULT '0' COMMENT '券id',
  `number` int(4) NOT NULL DEFAULT '0' COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='兑换商品配置表';

-- ----------------------------
-- Records of p_goods_exchange
-- ----------------------------

-- ----------------------------
-- Table structure for p_goods_img
-- ----------------------------
DROP TABLE IF EXISTS `p_goods_img`;
CREATE TABLE `p_goods_img` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '商品Id',
  `imgPath` varchar(50) DEFAULT NULL COMMENT '图片地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品图片地址';

-- ----------------------------
-- Records of p_goods_img
-- ----------------------------

-- ----------------------------
-- Table structure for p_goods_suit
-- ----------------------------
DROP TABLE IF EXISTS `p_goods_suit`;
CREATE TABLE `p_goods_suit` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '商品id',
  `explain` varchar(100) NOT NULL COMMENT '套装说明',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品套装配置表';

-- ----------------------------
-- Records of p_goods_suit
-- ----------------------------

-- ----------------------------
-- Table structure for p_knot_config
-- ----------------------------
DROP TABLE IF EXISTS `p_knot_config`;
CREATE TABLE `p_knot_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `smallValue` int(6) NOT NULL DEFAULT '0' COMMENT '最小值',
  `bigValue` int(6) NOT NULL DEFAULT '0' COMMENT '最大值',
  `rate` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '利率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='结算区间配置表';

-- ----------------------------
-- Records of p_knot_config
-- ----------------------------
INSERT INTO `p_knot_config` VALUES ('2', '1', '500', '0.03');

-- ----------------------------
-- Table structure for p_order
-- ----------------------------
DROP TABLE IF EXISTS `p_order`;
CREATE TABLE `p_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `orderNo` varchar(100) NOT NULL COMMENT '订单号',
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '产品Id',
  `zoneType` int(2) NOT NULL DEFAULT '0' COMMENT '专区类型(0:普通专区 1:会员专区 2:兑换专区)',
  `isSuit` int(2) NOT NULL DEFAULT '0' COMMENT '是否是套装(0:不是 1:是)',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '状态(0:为支付 1:已支付 2:已发货 3:完成)',
  `goodsName` varchar(100) NOT NULL COMMENT '产品名称',
  `totalPrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总价',
  `realPrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实付价格',
  `number` int(10) NOT NULL DEFAULT '1' COMMENT '数量',
  `payType` int(2) NOT NULL DEFAULT '0' COMMENT '支付类型(0:余额支付 1:微信支付)',
  `receiptName` varchar(50) NOT NULL COMMENT '收件人',
  `receiptPhone` varchar(50) NOT NULL COMMENT '收件电话',
  `receiptSite` varchar(100) NOT NULL COMMENT '收件地址',
  `suiteName` varchar(200) DEFAULT NULL COMMENT '套件名',
  `isDel` int(2) NOT NULL DEFAULT '0' COMMENT '删除状态(-1:删除 0:正常)',
  `createTime` datetime DEFAULT NULL COMMENT '下单时间',
  `payTime` datetime DEFAULT NULL COMMENT '支付时间',
  `wxOrderNo` varchar(100) DEFAULT NULL COMMENT '微信支付订单号',
  `callbackPrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付回调金额',
  `logisticsName` varchar(50) DEFAULT NULL COMMENT '物流名',
  `logisticsNo` varchar(100) DEFAULT NULL COMMENT '物流单号',
  `seendTime` datetime DEFAULT NULL COMMENT '发货时间',
  `userNote` varchar(200) DEFAULT NULL COMMENT '用户备注',
  `backNote` varchar(200) DEFAULT NULL COMMENT '后台备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Records of p_order
-- ----------------------------

-- ----------------------------
-- Table structure for p_order_voucher
-- ----------------------------
DROP TABLE IF EXISTS `p_order_voucher`;
CREATE TABLE `p_order_voucher` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `orderId` bigint(11) NOT NULL DEFAULT '0' COMMENT '订单id',
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '会员id',
  `userVoucherId` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户券Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单兑换券使用表';

-- ----------------------------
-- Records of p_order_voucher
-- ----------------------------

-- ----------------------------
-- Table structure for p_user
-- ----------------------------
DROP TABLE IF EXISTS `p_user`;
CREATE TABLE `p_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `isUser` int(1) NOT NULL DEFAULT '0' COMMENT '是否是会员(0:不是 1:是)',
  `phone` varchar(50) NOT NULL COMMENT '手机号',
  `payPassword` varchar(50) DEFAULT NULL COMMENT '支付密码',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '-1:禁用 0:正常',
  `strawUid` bigint(11) NOT NULL DEFAULT '0' COMMENT '推荐人uid',
  `nickName` varchar(50) DEFAULT NULL COMMENT '昵称',
  `name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `integral` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '积分',
  `totalIntegral` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总积分',
  `bankName` varchar(100) DEFAULT NULL COMMENT '开户行',
  `bankBranch` varchar(100) DEFAULT NULL COMMENT '开户支行',
  `bankCard` varchar(100) DEFAULT NULL COMMENT '银行卡号',
  `idCard` varchar(100) DEFAULT NULL COMMENT '身份证',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `disableTime` datetime DEFAULT NULL COMMENT '禁用时间',
  `uncoilTime` datetime DEFAULT NULL COMMENT '解禁时间',
  `strawPath` varchar(255) DEFAULT NULL,
  `knotId` bigint(11) NOT NULL DEFAULT '0' COMMENT '结算利率id',
  `knotAmount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '结算总金额',
  `teamOneNum` int(11) NOT NULL DEFAULT '0' COMMENT '团队单品数',
  `teamSuitNum` int(11) NOT NULL DEFAULT '0' COMMENT '团队套装数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of p_user
-- ----------------------------
INSERT INTO `p_user` VALUES ('1', '0', '18087760500', null, '0', '0', '剃刀', '剃刀', '100.00', '350.00', '350.00', '建设银行', '春城支行', '8965656565656', '4545454545455', '2019-11-20 18:30:34', null, null, null, '2', '0.00', '10', '15');

-- ----------------------------
-- Table structure for p_user_knot
-- ----------------------------
DROP TABLE IF EXISTS `p_user_knot`;
CREATE TABLE `p_user_knot` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '结算用户id',
  `payUid` bigint(11) NOT NULL DEFAULT '0' COMMENT '购买人id',
  `orderId` bigint(11) NOT NULL DEFAULT '0' COMMENT '订单id',
  `monthId` bigint(11) NOT NULL DEFAULT '0' COMMENT '月结Id',
  `knotPrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '结算金额',
  `payPrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额',
  `knotType` int(2) NOT NULL DEFAULT '0' COMMENT '结算类型(0:购买积分结算 1:提出结算 2:积分转换)',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员结算表';

-- ----------------------------
-- Records of p_user_knot
-- ----------------------------

-- ----------------------------
-- Table structure for p_user_login
-- ----------------------------
DROP TABLE IF EXISTS `p_user_login`;
CREATE TABLE `p_user_login` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `openId` varchar(100) NOT NULL COMMENT '微信OPENID',
  `headPath` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `nickName` varchar(100) DEFAULT NULL COMMENT '昵称',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户微信登录表';

-- ----------------------------
-- Records of p_user_login
-- ----------------------------
INSERT INTO `p_user_login` VALUES ('2', '1', 'asdasasdas', null, '剃刀', '2019-11-20 18:31:09');

-- ----------------------------
-- Table structure for p_user_month_knot
-- ----------------------------
DROP TABLE IF EXISTS `p_user_month_knot`;
CREATE TABLE `p_user_month_knot` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `knotPrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '结算金额',
  `knotOrderNum` int(11) NOT NULL DEFAULT '0' COMMENT '结算订单数量',
  `knotTime` datetime DEFAULT NULL COMMENT '结算月份',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户月结记录表';

-- ----------------------------
-- Records of p_user_month_knot
-- ----------------------------

-- ----------------------------
-- Table structure for p_user_site
-- ----------------------------
DROP TABLE IF EXISTS `p_user_site`;
CREATE TABLE `p_user_site` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '会员id',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机',
  `site` varchar(200) DEFAULT NULL COMMENT '地址',
  `isDefault` int(1) NOT NULL DEFAULT '0' COMMENT '是否默认(0:否 1:是)',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收货地址表';

-- ----------------------------
-- Records of p_user_site
-- ----------------------------

-- ----------------------------
-- Table structure for p_user_voucher
-- ----------------------------
DROP TABLE IF EXISTS `p_user_voucher`;
CREATE TABLE `p_user_voucher` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `voucherId` bigint(11) NOT NULL DEFAULT '0' COMMENT '券id',
  `useState` int(2) NOT NULL DEFAULT '0' COMMENT '使用状态(-1:使用 0:正常)',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '状态(-1:过期 0:正常)',
  `startTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户券表';

-- ----------------------------
-- Records of p_user_voucher
-- ----------------------------
INSERT INTO `p_user_voucher` VALUES ('1', '1', '1', '0', '0', null, null);

-- ----------------------------
-- Table structure for p_voucher
-- ----------------------------
DROP TABLE IF EXISTS `p_voucher`;
CREATE TABLE `p_voucher` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `voucherName` varchar(50) NOT NULL COMMENT '券名称',
  `faceValue` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '面额',
  `price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `isSill` int(1) NOT NULL DEFAULT '0' COMMENT '使用门槛(0:无 1:有)',
  `mainSill` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '最低门槛',
  `issueNum` int(10) NOT NULL DEFAULT '0' COMMENT '发信量',
  `useNum` int(10) NOT NULL DEFAULT '0' COMMENT '使用量',
  `timeType` int(1) NOT NULL DEFAULT '0' COMMENT '期限类型(0:无 1:固定期限 2:时间区间)',
  `startTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '到期时间',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '状态(-1:过期 0:正常)',
  `isDel` int(2) NOT NULL DEFAULT '0' COMMENT '删除状态(-1:删除 0:正常)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='券表';

-- ----------------------------
-- Records of p_voucher
-- ----------------------------
INSERT INTO `p_voucher` VALUES ('1', '68元抵用券', '63.00', '0.00', '0', '0.00', '0', '0', '0', null, null, '0', '0', '2019-11-21 15:11:04');
