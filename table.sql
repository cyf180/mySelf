CREATE TABLE `p_user_site` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '会员id',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机',
  `site` varchar(200) DEFAULT NULL COMMENT '地址',
  `isDefault` int(1) NOT NULL DEFAULT '0' COMMENT '是否默认(0:否 1:是)',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收货地址表';

CREATE TABLE `p_user_login` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `openId` varchar(100) NOT NULL COMMENT '微信OPENID',
  `headPath` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `nickName` varchar(100) DEFAULT NULL COMMENT '昵称',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户微信登录表';

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
  `bankName` varchar(100) DEFAULT NULL COMMENT '开户行',
  `bankBranch` varchar(100) DEFAULT NULL COMMENT '开户支行',
  `bankCard` varchar(100) DEFAULT NULL COMMENT '银行卡号',
  `idCard` varchar(100) DEFAULT NULL COMMENT '身份证',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `disableTime` datetime DEFAULT NULL COMMENT '禁用时间',
  `uncoilTime` datetime DEFAULT NULL COMMENT '解禁时间',
  `strawPath` varchar(255) DEFAULT NULL,
  `knotId` bigint(11) NOT NULL DEFAULT '0' COMMENT '结算利率id',
  `stayKnotAmount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '待结算金额',
  `knotAmount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '结算总金额',
  `teamOneNum` int(11) NOT NULL DEFAULT '0' COMMENT '团队单品数',
  `teamSuitNum` int(11) NOT NULL DEFAULT '0' COMMENT '团队套装数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE `p_knot_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `smallValue` int(6) NOT NULL DEFAULT '0' COMMENT '最小值',
  `bigValue` int(6) NOT NULL DEFAULT '0' COMMENT '最大值',
  `rate` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '利率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='结算区间配置表';

CREATE TABLE `p_goods` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `classId` bigint(11) NOT NULL DEFAULT '0' COMMENT '分类id',
  `goodsName` varchar(100) NOT NULL COMMENT '商品名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表';

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