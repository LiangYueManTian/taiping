package com.taiping.biz.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.taiping.bean.user.UserGroupDto;
import com.taiping.biz.user.dao.MenuInfoDao;
import com.taiping.biz.user.dao.UserGroupDao;
import com.taiping.biz.user.dao.UserInfoDao;
import com.taiping.biz.user.service.IUserGroupManageService;
import com.taiping.constant.user.UserManageResultCode;
import com.taiping.constant.user.UserManageResultMsg;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.user.MenuInfo;
import com.taiping.entity.user.UserGroup;
import com.taiping.entity.user.UserGroupPermission;
import com.taiping.entity.user.UserInfo;
import com.taiping.exception.BizException;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 用户组管理逻辑层服务实现
 */
@Service
@Slf4j
public class UserGroupManageServiceImpl extends ServiceImpl<UserGroupDao,UserGroup> implements IUserGroupManageService {

    /**
     * 用户组管理持久层服务
     */
    @Autowired
    private UserGroupDao userGroupDao;

    /**
     * 用户信息管理持久层服务
     */
    @Autowired
    private UserInfoDao userInfoDao;

    /**
     * 菜单管理持久层服务
     */
    @Autowired
    private MenuInfoDao menuInfoDao;

    /**
     * 获取用户组列表
     *
     * @return 用户组列表
     */
    @Override
    public List<UserGroup> getUserGroupList() {
        return userGroupDao.getUserGroupList();
    }

    /**
     * 分页查询用户组列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    @Override
    public PageBean queryUserGroupByCondition(QueryCondition<UserGroup> queryCondition) {
        Page page = MpQueryHelper.myBatiesBuildPage(queryCondition);
        EntityWrapper entityWrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
        int count = userGroupDao.selectCount(entityWrapper);
        List<UserInfo> pageUserGroup = userGroupDao.selectPage(page, entityWrapper);
        return MpQueryHelper.myBatiesBuildPageBean(page,count,pageUserGroup);
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userGroupId 用户组id
     * @return 用户组信息
     */
    @Override
    public UserGroupDto getUserGroupById(String userGroupId) {
        //获取用户组信息
        UserGroup userGroup = userGroupDao.getUserGroupById(userGroupId);
        //获取用户组中用户信息
        List<UserInfo> userInfoList = userGroupDao.getUserListByUserGroupId(userGroupId);
        UserGroupDto userGroupDto = new UserGroupDto();
        userGroupDto.setUserGroup(userGroup);
        userGroupDto.setUserInfoList(userInfoList);
        //获取用户组菜单权限信息
        List<MenuInfo> menuInfoList =  menuInfoDao.getUserGroupMenuInfoByGrouopId(userGroupId);
        List<String> menuPermissionCodes = menuInfoList.stream().map(menuInfo -> menuInfo.getMenuCode()).collect(Collectors.toList());
        userGroupDto.setMenuPermissionCodes(menuPermissionCodes);
        return userGroupDto;
    }

    /**
     * 添加用户组
     *
     * @param userGroupDto 用户组信息
     * @return 添加结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addUserGroup(UserGroupDto userGroupDto) {
        UserGroup userGroup = userGroupDto.getUserGroup();
        //生成用户组ids
        String userGroupId = NineteenUUIDUtils.uuid();
        userGroup.setUserGroupId(userGroupId);
        try {
            //保存用户组信息
            userGroup.setIsDefaultGroup(0);
            userGroup.setIsDeleted(0);
            userGroup.setCreateTime(System.currentTimeMillis());
            userGroup.setUpdateTime(System.currentTimeMillis());
            userGroupDao.addUserGroup(userGroup);
            //保存用户组菜单权限信息
            List<String> menuPermissionCodes = userGroupDto.getMenuPermissionCodes();
            List<MenuInfo> menuInfoList = menuInfoDao.getMenuInfoList();
            List<MenuInfo> selectMenuList = menuInfoList.stream().filter(menuInfo -> menuPermissionCodes.
                    contains(menuInfo.getMenuCode())).collect(Collectors.toList());
            List<String> menuIds = selectMenuList.stream().map(menuInfo -> menuInfo.getMenuId()).collect(Collectors.toList());
            List<UserGroupPermission> permissionList = Lists.newArrayList();
            for (String menuId : menuIds) {
                UserGroupPermission permission = new UserGroupPermission();
                //生成权限id
                permission.setPermissionId(NineteenUUIDUtils.uuid());
                permission.setMenuId(menuId);
                permission.setUserGroupId(userGroupId);
                permissionList.add(permission);
            }
            userGroupDao.addUserGroupMenuPermission(permissionList);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("add userGroup error",e);
            throw new BizException(UserManageResultCode.DATABASE_OPERATION_FAIL,UserManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 批量删除用户组
     *
     * @param userGroupIds 用户组id列表
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteUserGroups(List<String> userGroupIds) {
        //验证是否选择默认用户组
        UserGroup defaultGroup = userGroupDao.getDefaultUserGroup();
        if(userGroupIds.contains(defaultGroup.getUserGroupId())) {
            return ResultUtils.warn(UserManageResultCode.DELETE_USER_GROUP_FAIL, UserManageResultMsg.EXISTED_DEFAULT_USERGROUP);
        }
        //验证用户组中是否存在用户
        List<UserInfo> userInfoList = userInfoDao.getUserInfoList();
        for (UserInfo userInfo: userInfoList) {
            if(userGroupIds.contains(userInfo.getUserGroupId())) {
                return ResultUtils.warn(UserManageResultCode.DELETE_USER_GROUP_FAIL, UserManageResultMsg.USERGROUP_HAVING_USER);
            }
        }
        try{
            //删除用户组信息
            userGroupDao.deleteUserGroups(userGroupIds);
            //删除用户组菜单权限信息
            userGroupDao.deleteUserGroupMenuPermission(userGroupIds);
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("delete userGroup error",e);
            throw new BizException(UserManageResultCode.DATABASE_OPERATION_FAIL,UserManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 更新用户组
     *
     * @param userGroupDto 用户信息组
     * @return 更新结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateUserGroup(UserGroupDto userGroupDto) {
        //验证用户组是否存在
        UserGroup userGroup = userGroupDto.getUserGroup();
        UserGroup group = userGroupDao.getUserGroupById(userGroup.getUserGroupId());
        if (ObjectUtils.isEmpty(group)) {
            return ResultUtils.warn(UserManageResultCode.USER_GROUP_EXISTED,UserManageResultMsg.USER_GROUP_EXISTED);
        }
        try{
            //更新用户组基本信息
            userGroup.setUpdateTime(System.currentTimeMillis());
            userGroupDao.updateUserGroup(userGroup);
            //更新用户组菜单权限信息
            userGroupDao.deleteUserGroupMenuPermission(Lists.newArrayList(userGroup.getUserGroupId()));
            List<String> menuPermissionCodes = userGroupDto.getMenuPermissionCodes();
            List<MenuInfo> menuInfoList = menuInfoDao.getMenuInfoList();
            List<MenuInfo> selectMenuList = menuInfoList.stream().filter(menuInfo -> menuPermissionCodes.
                    contains(menuInfo.getMenuCode())).collect(Collectors.toList());
            List<String> menuIds = selectMenuList.stream().map(menuInfo -> menuInfo.getMenuId()).collect(Collectors.toList());
            List<UserGroupPermission> permissionList = Lists.newArrayList();
            for (String menuId : menuIds) {
                UserGroupPermission permission = new UserGroupPermission();
                //生成权限id
                permission.setPermissionId(NineteenUUIDUtils.uuid());
                permission.setMenuId(menuId);
                permission.setUserGroupId(userGroup.getUserGroupId());
                permissionList.add(permission);
            }
            if (!ObjectUtils.isEmpty(permissionList.toArray())) {
                userGroupDao.addUserGroupMenuPermission(permissionList);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("modify userGroup error",e);
            throw new BizException(UserManageResultCode.DATABASE_OPERATION_FAIL,UserManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 验证用户组是否已存在
     *
     * @param userGroup 用户组信息
     * @return 验证结果
     */
    @Override
    public Result checkUserGroupExist(UserGroup userGroup) {
        UserGroup userGroupById = new UserGroup();
        if (!ObjectUtils.isEmpty(userGroup.getUserGroupId())) {
            userGroupById = userGroupDao.getUserGroupById(userGroup.getUserGroupId());
        }
        UserGroup checkUserGroup = userGroupDao.getUserGroupByName(userGroup.getUserGroupName());
        if(ObjectUtils.isEmpty(checkUserGroup) || checkUserGroup.getUserGroupId().equals(userGroupById.getUserGroupId())) {
            return ResultUtils.success();
        }
        return ResultUtils.warn(UserManageResultCode.USER_GROUP_EXISTED, UserManageResultMsg.USER_GROUP_EXISTED);
    }
}
