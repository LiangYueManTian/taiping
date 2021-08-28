package com.taiping.biz.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.taiping.bean.user.ModifyPasswordVo;
import com.taiping.bean.user.UserInfoDto;
import com.taiping.bean.user.UserLoginVo;
import com.taiping.biz.user.dao.MenuInfoDao;
import com.taiping.biz.user.dao.UserGroupDao;
import com.taiping.biz.user.dao.UserInfoDao;
import com.taiping.biz.user.service.ITokenService;
import com.taiping.biz.user.service.IUserInfoManageService;
import com.taiping.constant.user.UserManageResultMsg;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.user.MenuInfo;
import com.taiping.entity.user.UserGroup;
import com.taiping.entity.user.UserInfo;
import com.taiping.constant.user.UserManageResultCode;
import com.taiping.exception.BizException;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author zhangliangyu
 * @since 2019/9/25
 * 用户信息管理逻辑层服务实现
 */
@Service
@Slf4j
public class UserManageServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements IUserInfoManageService {
    /**
     * 用户信息管理持久层
     */
    @Autowired
    private UserInfoDao userInfoDao;

    /**
     * 用户组管理持久层
     */
    @Autowired
    private UserGroupDao userGroupDao;

    /**
     *JWT 令牌相关服务
     */
    @Autowired
    private ITokenService tokenService;

    /**
     * 菜单权限管理持久层
     */
    @Autowired
    private MenuInfoDao menuInfoDao;


    /**
     * 用户登录
     *
     * @param user 登录用户
     * @return 登录结果
     */
    @Override
    public Result userLogin(UserInfo user) {
        UserInfo loginUser = userInfoDao.userLogin(user);
        if (ObjectUtils.isEmpty(loginUser)) {
            return ResultUtils.warn(UserManageResultCode.LOGIN_FAIL, UserManageResultMsg.LOGIN_FAIL);
        } else {
            String token = tokenService.getToken(loginUser);
            List<MenuInfo> menuPermissions = menuInfoDao.
                    getUserGroupMenuInfoByGrouopId(loginUser.getUserGroupId());
            UserLoginVo userLoginVo = new UserLoginVo();
            userLoginVo.setUserInfoBase(loginUser);
            userLoginVo.setMenuPermissions(menuPermissions);
            userLoginVo.setToken(token);
            return ResultUtils.success(userLoginVo);
        }
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @Override
    public List<UserInfo> getUserInfoList() {
        return userInfoDao.getUserInfoList();
    }

    /**
     * 分页查询用户列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
    @Override
     */
    @Override
    public PageBean queryUserInfoByCondition(QueryCondition<UserInfo> queryCondition) {
        //构建分页条件
        Page page = MpQueryHelper.myBatiesBuildPage(queryCondition);
        //构建查询条件
        EntityWrapper entityWrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
        //获取查询总数
        int count = userInfoDao.selectCount(entityWrapper);
        //获取分页查询数据
        List<UserInfo> pageUserInfo = userInfoDao.selectPage(page, entityWrapper);
        List<UserGroup> userGroupList = userGroupDao.getUserGroupList();
        List<UserInfoDto> userInfoDtoList = Lists.newArrayList();
        pageUserInfo.forEach(userInfo -> userGroupList.forEach(userGroup -> {
            if(userInfo.getUserGroupId().equals(userGroup.getUserGroupId())){
                UserInfoDto userInfoDto = new UserInfoDto();
                BeanUtils.copyProperties(userInfo,userInfoDto);
                userInfoDto.setUserGroupName(userGroup.getUserGroupName());
                userInfoDtoList.add(userInfoDto);
            }
        }));
        return MpQueryHelper.myBatiesBuildPageBean(page,count,userInfoDtoList);
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public UserInfoDto getUserById(String userId) {
        //获取用户信息
        UserInfo userInfo = userInfoDao.getUserInfoById(userId);
        //获取用户所属用户组信息
        UserGroup userGroup = userGroupDao.getUserGroupById(userInfo.getUserGroupId());
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo,userInfoDto);
        userInfoDto.setUserGroupName(userGroup.getUserGroupName());
        return userInfoDto;
    }

    /**
     * 添加用户
     *
     * @param userInfo 用户信息
     * @return 添加结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addUser(UserInfo userInfo) {
        //生成用户id
        userInfo.setIsSuperUser(0);
        userInfo.setIsDeleted(0);
        userInfo.setCreateTime(System.currentTimeMillis());
        userInfo.setUserId(NineteenUUIDUtils.uuid());
        try {
            userInfoDao.addUser(userInfo);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("add user error",e);
            throw new BizException(UserManageResultCode.DATABASE_OPERATION_FAIL,UserManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 批量删除用户
     *
     * @param userIds 用户id列表
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteUsers(List<String> userIds) {
        //验证是否选择了超级用户
        UserInfo superUser = userInfoDao.getSuperUser();
        if(userIds.contains(superUser.getUserId())) {
            return ResultUtils.warn(UserManageResultCode.DELETE_USER_FAIL, UserManageResultMsg.EXISTED_SUPER_USER);
        }
        try{
            userInfoDao.deleteUser(userIds);
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("delete userGroup error",e);
            throw new BizException(UserManageResultCode.DATABASE_OPERATION_FAIL,UserManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 更新用户
     *
     * @param userInfo 用户信息
     * @return 更新结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateUser(UserInfo userInfo) {
        //验证用户是否存在
        UserInfo user = userInfoDao.getUserInfoById(userInfo.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            return ResultUtils.warn(UserManageResultCode.UPDATE_USER_FAIL, UserManageResultMsg.USER_NOT_EXISTED);
        }
        userInfo.setUpdateTime(System.currentTimeMillis());
        try{
            userInfoDao.updateUser(userInfo);
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("modify userGroup error",e);
            throw new BizException(UserManageResultCode.DATABASE_OPERATION_FAIL,UserManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 修改密码
     *
     * @param modifyPasswordVo   修改密码对象
     * @return 修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result modifyPassword(ModifyPasswordVo modifyPasswordVo) {
        //验证旧密码是否正确
        UserInfo user = userInfoDao.getUserInfoById(modifyPasswordVo.getUserId());
        if (!modifyPasswordVo.getOldPassword().equals(user.getPassword())) {
            return ResultUtils.warn(UserManageResultCode.OLD_PASSWORD_ERROR, UserManageResultMsg.OLD_PASSWORD_ERROR);
        }
        try{
            userInfoDao.modifyPassword(modifyPasswordVo.getUserId(),modifyPasswordVo.getNewPassword());
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("modify password error",e);
            throw new BizException(UserManageResultCode.DATABASE_OPERATION_FAIL,UserManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 验证用户是否存在
     *
     * @param user 待验证用户
     * @return 验证结果
     */
    @Override
    public Result checkUserExist(UserInfo user) {
        UserInfo userInfo = new UserInfo();
        if (!ObjectUtils.isEmpty(user.getUserId())) {
            userInfo = userInfoDao.getUserInfoById(user.getUserId());
        }
        String userName = user.getUserName();
        String accountName = user.getAccountName();
        String userNumber = user.getUserNumber();
        UserInfo checkUser = new UserInfo();
        if(!ObjectUtils.isEmpty(userName)) {
            checkUser = userInfoDao.getUserInfoByUserName(userName);
        }
        if (!ObjectUtils.isEmpty(accountName)) {
            checkUser = userInfoDao.getUserInfoByAccountName(accountName);
        }
        if (!ObjectUtils.isEmpty(userNumber)) {
            checkUser = userInfoDao.getUserInfoByUserNumber(userNumber);
        }
        if(ObjectUtils.isEmpty(checkUser) || checkUser.getUserId().equals(userInfo.getUserId())) {
            return ResultUtils.success();
        }
        return ResultUtils.warn(UserManageResultCode.USER_EXISTED, UserManageResultMsg.USER_EXISTED);
    }
}
