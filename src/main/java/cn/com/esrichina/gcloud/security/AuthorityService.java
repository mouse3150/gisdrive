package cn.com.esrichina.gcloud.security;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.esrichina.commons.exception.GeneralException;
import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.domain.repository.AuthorityRepository;
import cn.com.esrichina.genericdao.search.Search;

@Service
public class AuthorityService {
	@Resource
	private AuthorityRepository authorityRepository;

	public List<Authority> getAllAuthorty() {
		Search search = new Search(Authority.class);
		search.addFilterEqual("superAdminOnly", false);
		return authorityRepository.search(search);
	}

	public Authority getAuthorityById(String id) {
		return authorityRepository.find(id);
	}

	@Transactional
	public void addAuthority(Authority authority) throws GeneralException {
		Search search = new Search(Authority.class);
		search.addFilterEqual("name", authority.getName().trim());
		Integer count = authorityRepository.count(search);
		if (count > 0) {
			throw new GeneralException(Messages.getMessage("authority_name_exist", authority.getName()));
		}

		authority.setEditable(true);
		authority.setCreateDate(new Date());
		authority.setModifyDate(new Date());
		authorityRepository.save(authority);
	}

	@Transactional
	public void updateAuthority(Authority authority) throws GeneralException {

		Authority po = authorityRepository.find(authority.getId());
		if (po.getEditable() == false) {
			throw new GeneralException(Messages.getMessage("authority_cant_edit"));
		}
		if (!authority.getName().equals(po.getName())) {
			Search search = new Search(Authority.class);
			search.addFilterEqual("name", authority.getName().trim());
			Integer count = authorityRepository.count(search);
			if (count > 0) {
				throw new GeneralException(Messages.getMessage("authority_name_exist", authority.getName()));
			}
		}

		MyBeanUtils.copyProperties(authority, po);
		authorityRepository.merge(po);
	}

	@Transactional
	public void deleteAuthority(String id) throws GeneralException {
		Authority po = authorityRepository.find(id);
		if (po.getEditable() == false) {
			throw new GeneralException(Messages.getMessage("authority_cant_edit"));
		}
		authorityRepository.removeById(id);
	}
}
