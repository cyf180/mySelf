/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50540
Source Host           : 127.0.0.1:3306
Source Database       : tdpro

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2019-12-10 17:20:15
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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='购物车';

-- ----------------------------
-- Records of p_cart
-- ----------------------------
INSERT INTO `p_cart` VALUES ('7', '1', '2', '11', '4', '1523.00', '加绒', '七匹狼男士冬季羽绒服', '1', '0', '2019-11-27 17:30:55');
INSERT INTO `p_cart` VALUES ('8', '1', '2', '11', '5', '1523.00', '特加绒', '七匹狼男士冬季羽绒服', '1', '0', '2019-11-27 17:30:55');
INSERT INTO `p_cart` VALUES ('9', '1', '1', '12', '0', '1200.00', null, '毛里男士外套', '1', '0', '2019-11-28 11:07:55');
INSERT INTO `p_cart` VALUES ('12', '1', '2', '16', '5', '1523.00', '特加绒', '七匹狼男士冬季羽绒服', '1', '0', '2019-11-28 11:58:06');
INSERT INTO `p_cart` VALUES ('13', '1', '1', '17', '0', '1200.00', null, '毛里男士外套', '2', '0', '2019-12-02 11:06:15');
INSERT INTO `p_cart` VALUES ('14', '1', '1', '18', '0', '462.00', null, '毛里男士外套', '1', '0', '2019-12-02 11:41:51');
INSERT INTO `p_cart` VALUES ('15', '1', '1', '19', '0', '462.00', null, '毛里男士外套', '1', '0', '2019-12-02 11:44:07');
INSERT INTO `p_cart` VALUES ('16', '1', '3', '20', '6', '1300.00', '白色', '床单四件套', '1', '0', '2019-12-05 11:20:21');
INSERT INTO `p_cart` VALUES ('17', '1', '3', '20', '7', '1300.00', '蓝色', '床单四件套', '1', '0', '2019-12-05 11:20:21');
INSERT INTO `p_cart` VALUES ('18', '1', '3', '21', '6', '1300.00', '白色', '床单四件套', '1', '0', '2019-12-05 11:40:32');
INSERT INTO `p_cart` VALUES ('19', '1', '3', '22', '6', '1300.00', '白色', '床单四件套', '1', '0', '2019-12-05 11:57:26');
INSERT INTO `p_cart` VALUES ('20', '1', '3', '23', '6', '1300.00', '白色', '床单四件套', '1', '0', '2019-12-06 11:06:27');
INSERT INTO `p_cart` VALUES ('21', '1', '3', '24', '6', '1300.00', '白色', '床单四件套', '1', '0', '2019-12-06 11:27:07');
INSERT INTO `p_cart` VALUES ('22', '1', '3', '25', '6', '1300.00', '白色', '床单四件套', '1', '0', '2019-12-06 11:52:38');
INSERT INTO `p_cart` VALUES ('23', '1', '3', '26', '6', '1300.00', '白色', '床单四件套', '1', '0', '2019-12-06 11:56:44');
INSERT INTO `p_cart` VALUES ('24', '5', '3', '27', '6', '1300.00', '白色', '床单四件套', '1', '0', '2019-12-06 15:20:37');

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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='交易记录表';

-- ----------------------------
-- Records of p_deal_log
-- ----------------------------
INSERT INTO `p_deal_log` VALUES ('1', '余额支付', '12', '1137.00', '863.00', '1', '0', '2019-11-28 11:24:09');
INSERT INTO `p_deal_log` VALUES ('3', '余额支付', '12', '1137.00', '863.00', '1', '0', '2019-12-02 10:38:41');
INSERT INTO `p_deal_log` VALUES ('4', '余额支付', '12', '1137.00', '1863.00', '1', '0', '2019-12-02 10:42:33');
INSERT INTO `p_deal_log` VALUES ('5', '余额支付', '12', '1137.00', '1863.00', '1', '0', '2019-12-02 10:47:51');
INSERT INTO `p_deal_log` VALUES ('6', '余额支付', '12', '1137.00', '1863.00', '1', '0', '2019-12-02 10:57:58');
INSERT INTO `p_deal_log` VALUES ('7', '余额支付', '17', '2400.00', '2600.00', '1', '0', '2019-12-02 11:09:04');
INSERT INTO `p_deal_log` VALUES ('8', '余额支付', '18', '462.00', '2138.00', '1', '0', '2019-12-02 11:42:22');
INSERT INTO `p_deal_log` VALUES ('9', '余额支付', '19', '462.00', '1676.00', '1', '0', '2019-12-02 11:44:14');
INSERT INTO `p_deal_log` VALUES ('10', '单品购买结算', '19', '300.00', '1200.00', '2', '1', '2019-12-02 11:44:30');
INSERT INTO `p_deal_log` VALUES ('15', '余额支付', '20', '2600.00', '2400.00', '1', '0', '2019-12-05 11:38:11');
INSERT INTO `p_deal_log` VALUES ('16', '积分转换', '20', '500.00', '1700.00', '2', '1', '2019-12-05 11:38:28');
INSERT INTO `p_deal_log` VALUES ('19', '余额支付', '21', '1300.00', '3700.00', '1', '0', '2019-12-05 11:49:27');
INSERT INTO `p_deal_log` VALUES ('21', '余额支付', '21', '1300.00', '3700.00', '1', '0', '2019-12-05 11:51:47');
INSERT INTO `p_deal_log` VALUES ('23', '余额支付', '21', '1300.00', '2400.00', '1', '0', '2019-12-05 11:54:02');
INSERT INTO `p_deal_log` VALUES ('24', '余额支付', '22', '1300.00', '1100.00', '1', '0', '2019-12-05 11:57:32');
INSERT INTO `p_deal_log` VALUES ('25', '积分转换', '22', '500.00', '2200.00', '2', '1', '2019-12-05 11:58:00');
INSERT INTO `p_deal_log` VALUES ('26', '余额支付', '27', '1300.00', '3700.00', '5', '0', '2019-12-06 15:29:39');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='商品表';

-- ----------------------------
-- Records of p_goods
-- ----------------------------
INSERT INTO `p_goods` VALUES ('1', '1', '毛里男士外套', '0', '0', '462.00', '毛里男士外套', '件', '257.jpg', '撒大大撒多驱蚊器二', '0', '2019-11-22 10:08:43', '0', '96', '8');
INSERT INTO `p_goods` VALUES ('2', '1', '七匹狼男士冬季羽绒服', '2', '1', '1523.00', '七匹狼男士冬季羽绒服', '件', '3554847.jpg', '韦尔奇二群翁', '0', '2019-11-22 10:10:18', '0', '99', '1');
INSERT INTO `p_goods` VALUES ('3', '1', '床单四件套', '0', '1', '1300.00', '沃尔沃无', '套', '3554847.jpg', '撒大大撒大大多', '0', '2019-12-05 11:17:24', '0', '86', '14');

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='商品套装配置表';

-- ----------------------------
-- Records of p_goods_suit
-- ----------------------------
INSERT INTO `p_goods_suit` VALUES ('1', '1', '黑白配', '2019-11-25 14:10:04');
INSERT INTO `p_goods_suit` VALUES ('2', '1', '红蓝配', '2019-11-25 14:10:22');
INSERT INTO `p_goods_suit` VALUES ('3', '1', '红黑配', '2019-11-25 14:10:46');
INSERT INTO `p_goods_suit` VALUES ('4', '2', '加绒', '2019-11-25 14:13:16');
INSERT INTO `p_goods_suit` VALUES ('5', '2', '特加绒', '2019-11-25 14:13:29');
INSERT INTO `p_goods_suit` VALUES ('6', '3', '白色', '2019-12-05 11:18:33');
INSERT INTO `p_goods_suit` VALUES ('7', '3', '蓝色', '2019-12-05 11:18:44');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='结算区间配置表';

-- ----------------------------
-- Records of p_knot_config
-- ----------------------------
INSERT INTO `p_knot_config` VALUES ('2', '1', '500', '0.03');
INSERT INTO `p_knot_config` VALUES ('3', '501', '1000', '0.05');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of p_log
-- ----------------------------
INSERT INTO `p_log` VALUES ('1', '18087760500', '0', '会员删除收货地址', '会员删除收货地址ID: 2', '1', '0', '2019-11-21 17:32:00');
INSERT INTO `p_log` VALUES ('2', '18087760500', '0', '订单任务测试', '定时任务测试', '1', '0', '2019-12-05 11:08:07');
INSERT INTO `p_log` VALUES ('3', '18087760500', '0', '订单任务测试', '定时任务测试', '1', '0', '2019-12-05 11:15:14');
INSERT INTO `p_log` VALUES ('4', '18087760502', '0', '订单任务测试', '定时任务测试', '1', '0', '2019-12-10 15:52:06');

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
  `isKnot` int(2) NOT NULL DEFAULT '0' COMMENT '是否结算(0:未 1:结算)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `orderNoIndexes` (`orderNo`) USING BTREE,
  KEY `orderUserIndexes` (`uid`) USING BTREE,
  KEY `orderGoodsIndexes` (`goodsId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Records of p_order
-- ----------------------------
INSERT INTO `p_order` VALUES ('11', '11574847055213', '1', '2', '2', '1', '3', '七匹狼男士冬季羽绒服', '3046.00', '3046.00', '2', '2', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-11-27 17:30:55', '2019-11-09 10:33:52', null, '0.00', null, null, '2019-10-25 15:42:36', null, null, '0');
INSERT INTO `p_order` VALUES ('12', '11574910475450', '1', '1', '0', '0', '3', '毛里男士外套', '1200.00', '1137.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-11-28 11:07:55', '2019-12-02 10:57:58', null, '0.00', null, null, '2019-11-25 15:43:41', '给我加辣', null, '1');
INSERT INTO `p_order` VALUES ('16', '11574913474958', '1', '2', '2', '1', '2', '七匹狼男士冬季羽绒服', '1523.00', '0.00', '1', '2', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-11-28 11:57:54', '2019-11-28 11:59:16', null, '0.00', null, null, '2019-12-10 15:44:44', '给我加辣', null, '0');
INSERT INTO `p_order` VALUES ('17', '11575255975960', '1', '1', '0', '0', '2', '毛里男士外套', '2400.00', '2400.00', '2', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-12-02 11:06:15', '2019-12-02 11:09:04', null, '0.00', null, null, '2019-12-10 15:47:47', '给我加辣', null, '1');
INSERT INTO `p_order` VALUES ('18', '11575258111688', '1', '1', '0', '0', '2', '毛里男士外套', '462.00', '462.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-12-02 11:41:51', '2019-12-02 11:42:22', null, '0.00', null, null, '2019-12-10 15:57:50', '给我加辣', null, '1');
INSERT INTO `p_order` VALUES ('19', '11575258247109', '1', '1', '0', '0', '1', '毛里男士外套', '462.00', '462.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-12-02 11:44:07', '2019-12-02 11:44:14', null, '0.00', null, null, null, '给我加辣', null, '1');
INSERT INTO `p_order` VALUES ('20', '11575516021753', '1', '3', '0', '1', '1', '床单四件套', '2600.00', '2600.00', '2', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-12-05 11:20:21', '2019-12-05 11:38:11', null, '0.00', null, null, null, '给我加辣', null, '1');
INSERT INTO `p_order` VALUES ('21', '11575517232087', '1', '3', '0', '1', '1', '床单四件套', '1300.00', '1300.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-12-05 11:40:32', '2019-12-05 11:54:02', null, '0.00', null, null, null, '给我加辣', null, '1');
INSERT INTO `p_order` VALUES ('22', '11575518246486', '3', '3', '0', '1', '1', '床单四件套', '1300.00', '1300.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-12-05 11:57:26', '2019-12-05 11:57:32', null, '0.00', null, null, null, '给我加辣', null, '1');
INSERT INTO `p_order` VALUES ('23', '11575601587657', '1', '3', '0', '1', '0', '床单四件套', '1300.00', '1300.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '-1', '2019-12-06 11:06:27', null, null, '0.00', null, null, null, '给我加辣', null, '0');
INSERT INTO `p_order` VALUES ('24', '11575602827480', '1', '3', '0', '1', '0', '床单四件套', '1300.00', '1300.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '-1', '2019-12-10 15:41:07', null, null, '0.00', null, null, null, '给我加辣', null, '0');
INSERT INTO `p_order` VALUES ('25', '11575604357907', '1', '3', '0', '1', '0', '床单四件套', '1300.00', '1300.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-12-10 15:47:37', null, null, '0.00', null, null, null, '给我加辣', null, '0');
INSERT INTO `p_order` VALUES ('26', '11575604604789', '1', '3', '0', '1', '0', '床单四件套', '1300.00', '1300.00', '1', '0', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', null, '0', '2019-12-10 15:48:44', null, null, '0.00', null, null, null, '给我加辣', null, '0');
INSERT INTO `p_order` VALUES ('27', '51575616837170', '4', '3', '0', '1', '1', '床单四件套', '1300.00', '1300.00', '1', '0', '与非', '18087760500', '云南省昆明市官渡区世纪城156号', null, '0', '2019-12-06 15:20:37', '2019-12-06 15:29:39', null, '0.00', null, null, null, '给我加辣', null, '1');

-- ----------------------------
-- Table structure for p_order_config
-- ----------------------------
DROP TABLE IF EXISTS `p_order_config`;
CREATE TABLE `p_order_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `type` int(2) NOT NULL DEFAULT '0' COMMENT '类型(0:未付款 1:为确认收货)',
  `time` int(5) NOT NULL DEFAULT '0' COMMENT '时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='订单配置表';

-- ----------------------------
-- Records of p_order_config
-- ----------------------------
INSERT INTO `p_order_config` VALUES ('1', '0', '15');
INSERT INTO `p_order_config` VALUES ('3', '1', '30');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

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
-- Table structure for p_sms
-- ----------------------------
DROP TABLE IF EXISTS `p_sms`;
CREATE TABLE `p_sms` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `smsName` varchar(50) NOT NULL COMMENT '短信登陆名',
  `smsPassword` varchar(50) NOT NULL COMMENT '短信接扣密码',
  `smsHttpUrl` varchar(100) NOT NULL COMMENT '短信接口请求地址',
  `smsSigna` varchar(100) DEFAULT NULL COMMENT '短信签名',
  `type` int(2) NOT NULL DEFAULT '0' COMMENT '短信类型（0：互亿，1：华信）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `aid` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='商户短信报备表';

-- ----------------------------
-- Records of p_sms
-- ----------------------------
INSERT INTO `p_sms` VALUES ('11', 'C05897078', '89522de5f2b832f5b65f35ea7fa85189', 'http://106.ihuyi.com/webservice/sms.php', '', '0');

-- ----------------------------
-- Table structure for p_sms_code
-- ----------------------------
DROP TABLE IF EXISTS `p_sms_code`;
CREATE TABLE `p_sms_code` (
  `id` bigint(4) unsigned NOT NULL AUTO_INCREMENT,
  `phone` varchar(50) NOT NULL DEFAULT '0' COMMENT '手机号',
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '会员uid',
  `code` varchar(50) NOT NULL COMMENT '验证码',
  `addTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='短信验证码表';

-- ----------------------------
-- Records of p_sms_code
-- ----------------------------
INSERT INTO `p_sms_code` VALUES ('1', '18087760500', '0', '1653', '2019-12-06 16:43:15', '2019-12-06 16:58:15');
INSERT INTO `p_sms_code` VALUES ('2', '18087760500', '0', '0808', '2019-12-06 16:44:24', '2019-12-06 16:59:24');
INSERT INTO `p_sms_code` VALUES ('3', '18087760500', '0', '3848', '2019-12-06 16:48:37', '2019-12-06 17:03:37');
INSERT INTO `p_sms_code` VALUES ('4', '18087760500', '0', '0859', '2019-12-06 17:09:14', '2019-12-06 17:24:14');

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
  `itemBuyAmount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `itemLeftAmount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `suitLevelNum` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of p_user
-- ----------------------------
INSERT INTO `p_user` VALUES ('1', '1', '18087760502', null, '0', '2', '剃刀', '云飞', '1178.00', '350.00', '350.00', '招商银行', '春城支行', '6666666666', '555555555555555', '2019-11-20 18:30:34', null, null, null, '2', '78.00', '10', '15', '4000.00', '461.00', '0');
INSERT INTO `p_user` VALUES ('2', '1', '18087760501', null, '0', '0', 'zz', 'zz', '2590.00', '0.00', '700.00', '建设银行', '春城支行', '8965656565656', '6666666', '2019-11-21 16:02:34', null, null, null, '3', '1590.00', '0', '4', '0.00', '0.00', '1');
INSERT INTO `p_user` VALUES ('3', '0', '18087760503', '71b596cb42ee254f7416043d184fc970', '0', '1', null, null, '0.00', '0.00', '0.00', null, null, null, null, '2019-12-06 15:06:42', null, null, null, '0', '0.00', '0', '0', '0.00', '0.00', '0');
INSERT INTO `p_user` VALUES ('4', '0', '18087760504', '71b596cb42ee254f7416043d184fc970', '0', '1', null, null, '0.00', '200.00', '200.00', null, null, null, null, '2019-12-06 15:11:28', null, null, null, '2', '0.00', '0', '1', '0.00', '0.00', '1');
INSERT INTO `p_user` VALUES ('5', '0', '18087760505', '71b596cb42ee254f7416043d184fc970', '0', '4', null, null, '3700.00', '0.00', '0.00', null, null, null, null, '2019-12-06 15:14:09', null, null, null, '0', '0.00', '0', '0', '0.00', '0.00', '0');
INSERT INTO `p_user` VALUES ('6', '0', '18087760500', '71b596cb42ee254f7416043d184fc970', '0', '5', null, null, '0.00', '0.00', '0.00', null, null, null, null, '2019-12-06 17:10:08', null, null, null, '0', '0.00', '0', '0', '0.00', '0.00', '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='会员结算表';

-- ----------------------------
-- Records of p_user_knot
-- ----------------------------
INSERT INTO `p_user_knot` VALUES ('1', '2', '1', '19', '0', '300.00', '462.00', '3', '2019-12-02 11:44:29');
INSERT INTO `p_user_knot` VALUES ('7', '2', '1', '20', '0', '200.00', '2600.00', '0', '2019-12-05 11:38:17');
INSERT INTO `p_user_knot` VALUES ('8', '2', '1', '20', '0', '300.00', '2600.00', '0', '2019-12-05 11:38:20');
INSERT INTO `p_user_knot` VALUES ('9', '2', '1', '20', '0', '500.00', '2600.00', '2', '2019-12-05 11:38:29');
INSERT INTO `p_user_knot` VALUES ('16', '2', '1', '21', '0', '200.00', '1300.00', '0', '2019-12-05 11:54:08');
INSERT INTO `p_user_knot` VALUES ('17', '2', '1', '22', '0', '300.00', '1300.00', '0', '2019-12-05 11:57:41');
INSERT INTO `p_user_knot` VALUES ('18', '2', '1', '22', '0', '500.00', '1300.00', '2', '2019-12-05 11:58:01');
INSERT INTO `p_user_knot` VALUES ('19', '4', '5', '27', '0', '200.00', '1300.00', '0', '2019-12-06 15:29:39');
INSERT INTO `p_user_knot` VALUES ('32', '1', '3', '22', '4', '39.00', '1300.00', '1', '2019-12-10 17:17:22');
INSERT INTO `p_user_knot` VALUES ('33', '1', '4', '27', '4', '39.00', '1300.00', '1', '2019-12-10 17:17:25');
INSERT INTO `p_user_knot` VALUES ('34', '2', '1', '20', '5', '130.00', '2600.00', '1', '2019-12-10 17:17:40');
INSERT INTO `p_user_knot` VALUES ('35', '2', '1', '21', '5', '65.00', '1300.00', '1', '2019-12-10 17:17:43');

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `openId` (`openId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户微信登录表';

-- ----------------------------
-- Records of p_user_login
-- ----------------------------
INSERT INTO `p_user_login` VALUES ('5', '6', 'oT-qp5bUQbu_L3MiR0-ldyYJyrr8', null, null, '2019-12-06 16:33:53');

-- ----------------------------
-- Table structure for p_user_month_knot
-- ----------------------------
DROP TABLE IF EXISTS `p_user_month_knot`;
CREATE TABLE `p_user_month_knot` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `knotPrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '结算金额',
  `year` int(10) NOT NULL DEFAULT '0' COMMENT '年',
  `month` int(10) NOT NULL DEFAULT '0' COMMENT '结算月份',
  `newOrderNum` int(10) NOT NULL DEFAULT '0' COMMENT '新增单数',
  `newOrderPrice` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '新增订单总金额',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '结算比列',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户月结记录表';

-- ----------------------------
-- Records of p_user_month_knot
-- ----------------------------
INSERT INTO `p_user_month_knot` VALUES ('4', '1', '78.00', '2019', '12', '2', '2600.00', '2019-12-10 17:17:29', '0.03');
INSERT INTO `p_user_month_knot` VALUES ('5', '2', '195.00', '2019', '12', '2', '3900.00', '2019-12-10 17:17:46', '0.05');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户收货地址表';

-- ----------------------------
-- Records of p_user_site
-- ----------------------------
INSERT INTO `p_user_site` VALUES ('1', '1', '萨达', '18087760500', '云南省昆明市官渡区世纪城15号', '0', '2019-11-21 16:36:04');
INSERT INTO `p_user_site` VALUES ('3', '1', '萨达', '18087760502', '云南省昭通市昭阳区180号', '1', '2019-11-21 17:03:41');
INSERT INTO `p_user_site` VALUES ('4', '5', '与非', '18087760500', '云南省昆明市官渡区世纪城156号', '1', '2019-12-06 15:20:15');

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
