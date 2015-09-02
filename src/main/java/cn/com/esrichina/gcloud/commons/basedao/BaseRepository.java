package cn.com.esrichina.gcloud.commons.basedao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.esrichina.genericdao.dao.jpa.GenericDAOImpl;
import cn.com.esrichina.genericdao.search.Filter;
import cn.com.esrichina.genericdao.search.IMutableSearch;
import cn.com.esrichina.genericdao.search.Page;
import cn.com.esrichina.genericdao.search.Search;
import cn.com.esrichina.genericdao.search.jpa.JPASearchProcessor;

@Repository
public class BaseRepository<E extends BaseEntity> extends GenericDAOImpl<E, String> implements BaseRepositoryDAO<E> {
	@Override
	@PersistenceContext(unitName="gcloud")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Override
	@Autowired
	public void setSearchProcessor(JPASearchProcessor searchProcessor) {
		super.setSearchProcessor(searchProcessor);
	}

	// @Override
	// public boolean logicRemove(E entity) {
	// if (entity != null) {
	// if (em().contains(entity)) {
	// entity.setIsDelete(true);
	// entity.setDeleteDate(new Date());
	// em().merge(entity);
	// return true;
	// } else {
	// return logicRemoveById(entity.getId());
	// }
	// }
	// return false;
	// }

//	@SuppressWarnings("unchecked")
//	@Override
//	public void logicRemove(E... entities) {
//		for (E e : entities) {
//			logicRemove(e);
//		}
//	}

	// @Override
	// public boolean logicRemoveById(String id) {
	// E entity = find(id);
	// if(entity != null) {
	// entity.setIsDelete(true);
	// entity.setDeleteDate(new Date());
	// em().merge(entity);
	// return true;
	// }
	// return false;
	// }

	// @Override
	// public void logicRemoveByIds(String... ids) {
	// for (String id : ids) {
	// logicRemoveById(id);
	// }
	// }

	@Override
	public List<E> findAllAvailableData() {
		Search search = new Search();
		search.addFilterEqual("isDelete", false);
		return search(search);
	}

	@Override
	public List<E> findAvailableData(IMutableSearch search) {
		if (search == null) {
			Search searchTool = new Search();
			searchTool.addFilterEqual("isDelete", false);
			return search(search);
		}

		Filter delFilter = new Filter();
		delFilter.setProperty("isDelete");
		delFilter.setOperator(Filter.OP_EQUAL);
		delFilter.setValue(false);

		List<Filter> filters = search.getFilters();
		filters.add(delFilter);

		return search(search);
	}

	@Override
	public Page<E> findPageAvailableData(Page<E> format) {
		return findPageAvailableData(format, null);
	}

	@Override
	public Page<E> findPageAvailableData(Page<E> format, IMutableSearch search) {

		/**
		 * Use default configuration when data is null.
		 */
		if (format == null) {
			format = new Page<E>();
		}
		if (search == null) {
			search = new Search();
		}

		// 添加删除标识过滤器
		Filter delFilter = new Filter();
		delFilter.setProperty("isDelete");
		delFilter.setOperator(Filter.OP_EQUAL);
		delFilter.setValue(false);

		List<Filter> filters = search.getFilters();
		filters.add(delFilter);

		int totalRecords = count(search);
		format.setTotalRecords(totalRecords);

		// 取得总页数
		int totalPages = format.getTotalPage();

		// 判断是否页号是否超过尾页
		if (format.getFrom() > totalPages) {
			format.setFrom(totalPages);
			// -1定义为尾页标识， 判断是否是尾页
		} else if (format.getFrom() == -1) {
			format.setFrom(totalPages);
			// 对0和负值，不合法请求处理成第一页
		} else if (format.getFrom() <= 0) {
			format.setFrom(1);
		}

		search.setFirstResult((format.getFrom() - 1) * format.getLimit());// 数据库查询索引从0开始
		search.setMaxResults(format.getLimit());

		List<E> results = search(search);

		format.setData(results);

		return format;
	}
}
