/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50540
Source Host           : 127.0.0.1:3306
Source Database       : tdpro

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2019-11-28 16:19:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for p_admin
-- ----------------------------
DROP TABLE IF EXISTS `p_admin`;
CREATE TABLE `p_admin` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(50) NOT NULL COMMENT '手机号(账号)',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `six` int(2) NOT NULL DEFAULT '0' COMMENT '性别(0：女 1：男)',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `rid` bigint(11) NOT NULL DEFAULT '0' COMMENT '权限id',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '状态(-1:禁用 0:正常)',
  `disableTime` datetime DEFAULT NULL COMMENT '禁用时间',
  `liftingTime` datetime DEFAULT NULL COMMENT '解禁时间',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_admin
-- ----------------------------

-- ----------------------------
-- Table structure for p_advert
-- ----------------------------
DROP TABLE IF EXISTS `p_advert`;
CREATE TABLE `p_advert` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '标题',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
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
-- Table structure for p_cart
-- ----------------------------
DROP TABLE IF EXISTS `p_cart`;
CREATE TABLE `p_cart` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '产品id',
  `orderId` bigint(11) NOT NULL DEFAULT '0' COMMENT '订单id',
  `suitId` bigint(11) NOT NULL DEFAULT '0' COMMENT '规格id',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `suitName` varchar(50) DEFAULT NULL COMMENT '规名称',
  `goodsName` varchar(50) NOT NULL COMMENT '产品名称',
  `number` int(10) NOT NULL DEFAULT '1' COMMENT '数量',
  `isKnot` int(2) NOT NULL DEFAULT '0' COMMENT '是否结算(0:未 1:结算)',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `goodsId` (`goodsId`) USING BTREE,
  KEY `uid` (`uid`) USING BTREE,
  KEY `orderId` (`orderId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='购物车';

-- ----------------------------
-- Records of p_cart
-- ----------------------------
INSERT INTO `p_cart` VALUES ('7', '1', '2', '11', '4', '1523.00', '加绒', '七匹狼男士冬季羽绒服', '1', '0', '2019-11-27 17:30:55');
INSERT INTO `p_cart` VALUES ('8', '1', '2', '11', '5', '1523.00', '特加绒', '七匹狼男士冬季羽绒服', '1', '0', '2019-11-27 17:30:55');
INSERT INTO `p_cart` VALUES ('9', '1', '1', '12', '0', '1200.00', null, '毛里男士外套', '1', '0', '2019-11-28 11:07:55');
INSERT INTO `p_cart` VALUES ('12', '1', '2', '16', '5', '1523.00', '特加绒', '七匹狼男士冬季羽绒服', '1', '0', '2019-11-28 11:58:06');

-- ----------------------------
-- Table structure for p_collect
-- ----------------------------
DROP TABLE IF EXISTS `p_collect`;
CREATE TABLE `p_collect` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '会员Id',
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '产品id',
  `createTime` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userGoodsIndexes` (`uid`,`goodsId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='收藏表';

-- ----------------------------
-- Records of p_collect
-- ----------------------------
INSERT INTO `p_collect` VALUES ('1', '1', '2', '2019-11-22 10:10:32');
INSERT INTO `p_collect` VALUES ('4', '1', '1', '2019-11-22 12:01:04');

-- ----------------------------
-- Table structure for p_deal_log
-- ----------------------------
DROP TABLE IF EXISTS `p_deal_log`;
CREATE TABLE `p_deal_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `dealName` varchar(50) NOT NULL COMMENT '交易名称',
  `orderId` bigint(11) NOT NULL DEFAULT '0' COMMENT '订单Id',
  `dealAmount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易金额',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '结算人id',
  `bUid` bigint(11) NOT NULL DEFAULT '0' COMMENT '操作人Id',
  `createTime` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='交易记录表';

-- ----------------------------
-- Records of p_deal_log
-- ----------------------------
INSERT INTO `p_deal_log` VALUES ('1', '余额支付', '12', '1137.00', '863.00', '1', '0', '2019-11-28 11:24:09');

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
  `sort` int(2) NOT NULL DEFAULT '0' COMMENT '排序',
  `repertory` int(11) NOT NULL DEFAULT '0' COMMENT '库存',
  `soldNum` int(11) NOT NULL DEFAULT '0' COMMENT '售出数量',
  PRIMARY KEY (`id`),
  KEY `classIndexes` (`classId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='商品表';

-- ----------------------------
-- Records of p_goods
-- ----------------------------
INSERT INTO `p_goods` VALUES ('1', '1', '毛里男士外套', '0', '0', '1200.00', '毛里男士外套', '件', '257.jpg', '撒大大撒多驱蚊器二', '0', '2019-11-22 10:08:43', '0', '2', '0');
INSERT INTO `p_goods` VALUES ('2', '1', '七匹狼男士冬季羽绒服', '2', '1', '1523.00', '七匹狼男士冬季羽绒服', '件', '3554847.jpg', '韦尔奇二群翁', '0', '2019-11-22 10:10:18', '0', '99', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='商品分类表';

-- ----------------------------
-- Records of p_goods_class
-- ----------------------------
INSERT INTO `p_goods_class` VALUES ('1', '男装', '0', '男孩子的衣服', null, '1', '0', '2019-11-22 10:06:43');

-- ----------------------------
-- Table structure for p_goods_exchange
-- ----------------------------
DROP TABLE IF EXISTS `p_goods_exchange`;
CREATE TABLE `p_goods_exchange` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '商品id',
  `voucherId` bigint(11) NOT NULL DEFAULT '0' COMMENT '券id',
  `number` int(4) NOT NULL DEFAULT '0' COMMENT '数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `goodsVoucherIndexes` (`goodsId`,`voucherId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='兑换商品配置表';

-- ----------------------------
-- Records of p_goods_exchange
-- ----------------------------
INSERT INTO `p_goods_exchange` VALUES ('1', '2', '1', '1');
INSERT INTO `p_goods_exchange` VALUES ('2', '2', '2', '1');
INSERT INTO `p_goods_exchange` VALUES ('3', '1', '1', '1');
INSERT INTO `p_goods_exchange` VALUES ('4', '1', '2', '2');

-- ----------------------------
-- Table structure for p_goods_img
-- ----------------------------
DROP TABLE IF EXISTS `p_goods_img`;
CREATE TABLE `p_goods_img` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `goodsId` bigint(11) NOT NULL DEFAULT '0' COMMENT '商品Id',
  `imgPath` varchar(50) DEFAULT NULL COMMENT '图片地址',
  PRIMARY KEY (`id`),
  KEY `goodsIndexes` (`goodsId`) USING BTREE
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
  PRIMARY KEY (`id`),
  KEY `suitGoodsIndexes` (`goodsId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='商品套装配置表';

-- ----------------------------
-- Records of p_goods_suit
-- ----------------------------
INSERT INTO `p_goods_suit` VALUES ('1', '1', '黑白配', '2019-11-25 14:10:04');
INSERT INTO `p_goods_suit` VALUES ('2', '1', '红蓝配', '2019-11-25 14:10:22');
INSERT INTO `p_goods_suit` VALUES ('3', '1', '红黑配', '2019-11-25 14:10:46');
INSERT INTO `p_goods_suit` VALUES ('4', '2', '加绒', '2019-11-25 14:13:16');
INSERT INTO `p_goods_suit` VALUES ('5', '2', '特加绒', '2019-11-25 14:13:29');

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
-- Table structure for p_log
-- ----------------------------
DROP TABLE IF EXISTS `p_log`;
CREATE TABLE `p_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `addName` varchar(50) DEFAULT NULL COMMENT '操作人名',
  `type` int(2) NOT NULL DEFAULT '0' COMMENT '0:用户端操作 1: 后台操作',
  `operation` varchar(100) NOT NULL COMMENT '操作名',
  `note` varchar(200) DEFAULT NULL COMMENT '操作说明',
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `adminId` bigint(11) NOT NULL DEFAULT '0' COMMENT '后台账号Id',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of p_log
-- ----------------------------
INSERT INTO `p_log` VALUES ('1', '18087760500', '0', '会员删除收货地址', '会员删除收货地址ID: 2', '1', '0', '2019-11-21 17:32:00');

-- ----------------------------
-- Table structure for p_menu
-- ----------------------------
DROP TABLE IF EXISTS `p_menu`;
CREATE TABLE `p_menu` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `mid` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属父级菜单',
  `menuName` varchar(50) NOT NULL COMMENT '菜单名',
  `menuUrl` varchar(100) DEFAULT NULL COMMENT '菜单url',
  `menuType` varchar(50) DEFAULT NULL COMMENT '类型说明',
  `menuApiPath` varchar(50) DEFAULT NULL COMMENT '接口地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理菜单表';

-- ----------------------------
-- Records of p_menu
-- ----------------------------

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
  PRIMARY KEY (`id`),
  KEY `orderUserIndexes` (`uid`) USING BTREE,
  KEY `orderGoodsIndexes` (`goodsId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Records of p_order
-- ----------------------------
INSERT INTO `p_order` VALUES ('11', '11574847055213', '1', '2', '2', '1', '1', '七匹狼男士冬季羽绒服', '3046.00', '3046.00', '2', '2', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-11-27 17:30:55', '2019-11-28 10:33:52', null, '0.00', null, null, null, null, null);
INSERT INTO `p_order` VALUES ('12', '11574910475450', '1', '1', '0', '0', '1', '毛里男士外套', '1200.00', '1137.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-11-28 11:07:55', '2019-11-28 11:24:20', null, '0.00', null, null, null, '给我加辣', null);
INSERT INTO `p_order` VALUES ('16', '11574913474958', '1', '2', '2', '1', '1', '七匹狼男士冬季羽绒服', '1523.00', '0.00', '1', '2', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-11-28 11:57:54', '2019-11-28 11:59:16', null, '0.00', null, null, null, '给我加辣', null);

-- ----------------------------
-- Table structure for p_order_voucher
-- ----------------------------
DROP TABLE IF EXISTS `p_order_voucher`;
CREATE TABLE `p_order_voucher` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `orderId` bigint(11) NOT NULL DEFAULT '0' COMMENT '订单id',
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '会员id',
  `userVoucherId` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户券Id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `orderVoucherOrderIndexes` (`orderId`,`uid`,`userVoucherId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='订单兑换券使用表';

-- ----------------------------
-- Records of p_order_voucher
-- ----------------------------
INSERT INTO `p_order_voucher` VALUES ('3', '11', '1', '2');
INSERT INTO `p_order_voucher` VALUES ('4', '11', '1', '3');
INSERT INTO `p_order_voucher` VALUES ('5', '12', '1', '1');
INSERT INTO `p_order_voucher` VALUES ('7', '16', '1', '4');
INSERT INTO `p_order_voucher` VALUES ('8', '16', '1', '5');

-- ----------------------------
-- Table structure for p_pay_config
-- ----------------------------
DROP TABLE IF EXISTS `p_pay_config`;
CREATE TABLE `p_pay_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '支付方式名称',
  `desc` varchar(50) DEFAULT NULL COMMENT '支付方式描述',
  `mchId` varchar(32) DEFAULT NULL COMMENT '支付商户号',
  `appId` varchar(32) DEFAULT NULL COMMENT '商户appId',
  `paySecret` varchar(32) DEFAULT NULL COMMENT '商户密钥',
  `channel` tinyint(2) NOT NULL DEFAULT '-1' COMMENT '支付渠道(0:小程序,1:app)',
  `payType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付类型(1:微信 2:支付宝)',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '开启状态(0:关闭 1: 开启)',
  `sort` tinyint(4) NOT NULL DEFAULT '0' COMMENT '排序',
  `certPath` varchar(200) DEFAULT NULL COMMENT '证书地址',
  `backPath` varchar(100) NOT NULL COMMENT '回调地址',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `channel` (`channel`,`payType`) USING BTREE,
  UNIQUE KEY `appId` (`appId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_pay_config
-- ----------------------------
INSERT INTO `p_pay_config` VALUES ('4', '小程序微信支付', '小程序微信支付', '1522552431', 'wx487abd95d03f9b6f', 'sJRjgGb2joHW1KcxXOV3zqeOv0Ouf20q', '0', '1', '1', '0', 'certs/1546409393224_apiclient_cert.p12', '', '2019-01-30 15:56:33');

-- ----------------------------
-- Table structure for p_role
-- ----------------------------
DROP TABLE IF EXISTS `p_role`;
CREATE TABLE `p_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleRank` varchar(50) NOT NULL COMMENT '权限标识',
  `roleName` varchar(50) DEFAULT NULL COMMENT '权限名',
  `roleTime` datetime DEFAULT NULL COMMENT '添加时间',
  `roleStatus` int(4) unsigned DEFAULT '0' COMMENT '状态  ',
  `roleType` enum('1','2') DEFAULT '1' COMMENT '角色类型1： 管理员  2: 门店角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理角色表';

-- ----------------------------
-- Records of p_role
-- ----------------------------

-- ----------------------------
-- Table structure for p_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `p_role_menu`;
CREATE TABLE `p_role_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `rid` int(11) unsigned NOT NULL COMMENT '权限id',
  `mid` int(11) unsigned NOT NULL COMMENT '目录id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of p_role_menu
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of p_user
-- ----------------------------
INSERT INTO `p_user` VALUES ('1', '1', '18087760500', null, '0', '0', '剃刀', '云飞', '863.00', '350.00', '350.00', '招商银行', '春城支行', '6666666666', '555555555555555', '2019-11-20 18:30:34', null, null, null, '2', '0.00', '10', '15');
INSERT INTO `p_user` VALUES ('2', '0', '18087760501', null, '0', '1', 'zz', 'zz', '0.00', '0.00', '0.00', '建设银行', '春城支行', '8965656565656', '6666666', '2019-11-21 16:02:34', null, null, null, '0', '0.00', '0', '0');

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
  PRIMARY KEY (`id`),
  KEY `userKnotUserIndexes` (`uid`) USING BTREE,
  KEY `userKnotPayUidIndexes` (`payUid`) USING BTREE
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户微信登录表';

-- ----------------------------
-- Records of p_user_login
-- ----------------------------
INSERT INTO `p_user_login` VALUES ('2', '1', 'oT-qp5bUQbu_L3MiR0-ldyYJyrr8', 'https://wx.qlogo.cn/mmopen/vi_32/KtGa6HIf2e69iajCS0XLtOIvH58e05ehdd5rbRntcgcQfIWD2Yk6bAUcE36d3ymHUD0LLdNjBq1errUQ1nFLMsA/132', '剃刀', '2019-11-20 18:31:09');
INSERT INTO `p_user_login` VALUES ('3', '2', 'qp5cfcwPHoNU4GonXaFW0e9aQ', 'https://wx.qlogo.cn/mmhead/9aajCPUNsPb2CWILmoCniabapJnzxg4rkWBJZ4icFg4ws/132', '丰', '2019-11-21 16:04:01');

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
  `name` varchar(50) DEFAULT NULL COMMENT '收件人',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机',
  `site` varchar(200) DEFAULT NULL COMMENT '地址',
  `isDefault` int(1) NOT NULL DEFAULT '0' COMMENT '是否默认(0:否 1:是)',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户收货地址表';

-- ----------------------------
-- Records of p_user_site
-- ----------------------------
INSERT INTO `p_user_site` VALUES ('1', '1', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', '0', '2019-11-21 16:36:04');
INSERT INTO `p_user_site` VALUES ('3', '1', '萨达', '18087760502', '云南省昭通市昭阳区180号', '1', '2019-11-21 17:03:41');

-- ----------------------------
-- Table structure for p_user_voucher
-- ----------------------------
DROP TABLE IF EXISTS `p_user_voucher`;
CREATE TABLE `p_user_voucher` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `voucherId` bigint(11) NOT NULL DEFAULT '0' COMMENT '券id',
  `useState` int(2) NOT NULL DEFAULT '0' COMMENT '使用状态(-1:使用 0:正常 1:绑定 )',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '状态(-1:过期 0:正常)',
  `startTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户券表';

-- ----------------------------
-- Records of p_user_voucher
-- ----------------------------
INSERT INTO `p_user_voucher` VALUES ('1', '1', '1', '-1', '0', null, null);
INSERT INTO `p_user_voucher` VALUES ('2', '1', '2', '-1', '0', null, null);
INSERT INTO `p_user_voucher` VALUES ('3', '1', '2', '-1', '0', '0000-00-00 00:00:00', '0000-00-00 00:00:00');
INSERT INTO `p_user_voucher` VALUES ('4', '1', '1', '-1', '0', null, null);
INSERT INTO `p_user_voucher` VALUES ('5', '1', '2', '-1', '0', null, null);

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
  `useExplain` varchar(200) DEFAULT NULL COMMENT '使用说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='券表';

-- ----------------------------
-- Records of p_voucher
-- ----------------------------
INSERT INTO `p_voucher` VALUES ('1', '68元抵用券', '63.00', '0.00', '0', '0.00', '0', '0', '0', null, null, '0', '0', '2019-11-21 15:11:04', '范德萨发生的');
INSERT INTO `p_voucher` VALUES ('2', '34元抵用券', '34.00', '0.00', '0', '0.00', '0', '0', '0', null, null, '0', '0', '2019-11-22 11:00:01', '客户客户客家话');
