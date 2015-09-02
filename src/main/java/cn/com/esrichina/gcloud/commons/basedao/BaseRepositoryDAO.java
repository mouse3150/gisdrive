package cn.com.esrichina.gcloud.commons.basedao;

import java.util.List;

import cn.com.esrichina.genericdao.dao.jpa.GenericDAO;
import cn.com.esrichina.genericdao.search.IMutableSearch;
import cn.com.esrichina.genericdao.search.Page;

public interface BaseRepositoryDAO<T> extends GenericDAO<T, String> {
	/**
	 * 根据记录逻辑删除
	 * @param dataCenter
	 * @return
	 */
	//public boolean logicRemove(T entity);

	/**
	 * 根据记录批量逻辑删除
	 * @param centers
	 */
	//@SuppressWarnings("unchecked")
	//public void logicRemove(T... entities);

	/**
	 * 根据Id删除记录
	 * @param id
	 * @return
	 */
	//public boolean logicRemoveById(String id);

	/**
	 * 根据Id批量逻辑删除
	 * @param ids
	 */
	//public void logicRemoveByIds(String... ids);
	
	/**
	 * 查询所有有效数据（未删除）
	 */
	public List<T> findAllAvailableData();
	
	/**
	 * 根据条件查询所有有效数据（未删除）
	 */
	public List<T> findAvailableData(IMutableSearch search);
	
	/**
	 * 分页查询（只定义分页结构）
	 * @param format
	 * @return
	 */
	public Page<T> findPageAvailableData(Page<T> format);
	
	/**
	 * 根据查询条件分页查询
	 * @param format 分页格式，
	 * @param search 查询条件
	 * @return
	 */
	public Page<T> findPageAvailableData(Page<T> format, IMutableSearch search);
}
