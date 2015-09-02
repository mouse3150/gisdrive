package cn.com.esrichina.gcloud.commons.web.request;

import cn.com.esrichina.genericdao.search.Search;

public class SearchRequest extends RestRequest {
	private Integer start = 0;
	private Integer count = 100;
	private String sortBy;
	private Boolean sortDesc;

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer newStart) {
		start = newStart;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer newCount) {
		count = newCount;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public Boolean getSortDesc() {
		return sortDesc;
	}

	public void setSortDesc(Boolean sortDesc) {
		this.sortDesc = sortDesc;
	}

	public Search getSearch(Class<?> t) {
		Search search = new Search(t);
		search.setFirstResult(start);
		search.setMaxResults(count);
		if (sortBy != null && sortDesc != null) {
			search.addSort(sortBy, sortDesc);
		} else {
			search.addSort("createDate", true);
		}
		return search;
	}
}