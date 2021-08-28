package com.taiping.biz.annex.dao;

import com.taiping.entity.annex.Annex;

import java.util.List;

/**
 * 附件持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-28
 */
public interface AnnexDao {

    /**
     * 新增附件
     * @param annex 附件
     * @return int
     */
    int addAnnex(Annex annex);

    /**
     * 删除附件
     * @param annexId 附件ID
     * @return int
     */
    int deleteAnnex(String annexId);

    /**
     * 查询附件
     * @param manageId 运维管理活动ID
     * @return 附件列表
     */
    List<Annex> selectAnnexList(String manageId);

    /**
     * 根据名称和运维管理活动ID查询附件
     * @param annex 附件
     * @return annex
     */
    Annex queryAnnexByNameAndId(Annex annex);
    /**
     * 根据名称查询附件数量
     * @param annex 附件名称
     * @return int
     */
    int queryAnnexByName(Annex annex);
    /**
     * 根据附件ID查询附件
     * @param annexId 附件ID
     * @return int
     */
    Annex findAnnexById(String annexId);

}
