package com.taiping.biz.operation.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.operation.ChaiFa;
import com.taiping.entity.operation.DistHighCabinet;
import com.taiping.entity.operation.DistLowCabinet;
import com.taiping.entity.operation.Transformer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运行情况分析持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
public interface OperationDao {


    /**
     * 批量保存柴发sheet 数据
     *
     * @param chaiFas
     */
    void saveImportChaiFa(List<ChaiFa> chaiFas);

    /**
     * 柴发数据
     *
     * @return
     */
    List<ChaiFa> queryChaiFaData(@Param("month") Integer month,
                                 @Param("year") Integer year);

    /**
     * 删除柴发数据
     *
     * @param month
     * @param year
     */
    void deleteChaiFaByCondition(@Param("month") Integer month,
                                 @Param("year") Integer year);


    /**
     * 低压柜数据
     *
     * @return
     */
    List<DistLowCabinet> queryDistLowData(@Param("month") Integer month,
                                          @Param("year") Integer year);

    /**
     * 删除低压柜数据
     *
     * @param month
     * @param year
     */
    void deleteDistLowByCondition(@Param("month") Integer month,
                                  @Param("year") Integer year);

    /**
     * 批量保存导入低压柜数据
     *
     * @param lowCabinets
     */
    void saveImportDistLowCabinet(List<DistLowCabinet> lowCabinets);


    /**
     * 删除高压柜数据
     *
     * @param month
     * @param year
     */
    void deleteDistHighByCondition(@Param("month") Integer month,
                                   @Param("year") Integer year);

    /**
     * 批量保存导入高压柜数据
     *
     * @param highCabinets
     */
    void saveImportDistHighCabinet(List<DistHighCabinet> highCabinets);

    /**
     * 低压柜数据
     *
     * @return
     */
    List<DistHighCabinet> queryDistHighData(@Param("month") Integer month,
                                            @Param("year") Integer year);


    /**
     * 删除变压器数据
     *
     * @param month
     * @param year
     */
    void deleteTransByCondition(@Param("month") Integer month,
                                @Param("year") Integer year);

    /**
     * 变压器数据
     *
     * @return
     */
    List<Transformer> queryTransFormerData(@Param("month") Integer month,
                                           @Param("year") Integer year);

    /**
     * 批量保存导入变压器数据
     *
     * @param transformers
     */
    void saveImportTransformer(List<Transformer> transformers);

    /**
     * 检测导入Excel 是否已经导入过
     *
     * @param sheetType
     * @param importMonth
     * @param importYear
     */
    Integer checkImportExcel(@Param("sheetType") Integer sheetType,
                             @Param("importMonth") Integer importMonth,
                             @Param("importYear") Integer importYear);

    /**
     * 保存
     * @param tId
     * @param sheetType
     * @param importMonth
     * @param importYear
     */
    void saveImportExcel(@Param("tId") String tId,
                         @Param("sheetType") Integer sheetType,
                         @Param("importMonth") Integer importMonth,
                         @Param("importYear") Integer importYear);

    /**
     * 删除导入Sheet
     * @param sheetType
     * @param importMonth
     * @param importYear
     */
    void deleteImport(@Param("sheetType") Integer sheetType,
                      @Param("importMonth") Integer importMonth,
                      @Param("importYear") Integer importYear);


    Integer checkAllImportStatus( @Param("importMonth") Integer importMonth,
                                  @Param("importYear") Integer importYear);
}
