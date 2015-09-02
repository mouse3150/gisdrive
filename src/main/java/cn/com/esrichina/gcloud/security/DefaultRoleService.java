package cn.com.esrichina.gcloud.security;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.esrichina.commons.exception.GeneralException;
import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.domain.DefaultRole;
import cn.com.esrichina.gcloud.security.domain.repository.DefaultRoleRepository;
import cn.com.esrichina.genericdao.search.Search;

@Service
public class DefaultRoleService {

	@Resource
	private DefaultRoleRepository defaultRoleRepository;

	public List<DefaultRole> searchDefaultRoles(Search search) {
		return defaultRoleRepository.search(search);
	}

	public DefaultRole getDefauleRole(String id) {
		return defaultRoleRepository.find(id);
	}

	@Transactional
	public void addDefaultRole(DefaultRole defaultRole) throws GeneralException {
		if (StringUtils.isBlank(defaultRole.getName())) {
			throw new GeneralException(Messages.getMessage("defrole_name_cant_empty"));
		}

		Search search = new Search(DefaultRole.class);
		search.addFilterEqual("name", defaultRole.getName().trim());
		Integer count = defaultRoleRepository.count(search);

		if (count > 0) {
			throw new GeneralException(Messages.getMessage("defrole_name_exist", defaultRole.getName().trim()));
		}

		defaultRole.setName(defaultRole.getName().trim());
		defaultRole.setEditable(true);
		defaultRole.setModifyDate(new Date());
		defaultRole.setCreateDate(new Date());
		defaultRoleRepository.save(defaultRole);
	}

	@Transactional
	public void updateDefaultRole(DefaultRole defaultRole) throws GeneralException {
		DefaultRole po = defaultRoleRepository.find(defaultRole.getId());
		if (!po.getEditable()) {
			throw new GeneralException(Messages.getMessage("defrole_cant_edit"));
		}

		if (StringUtils.isNotBlank(defaultRole.getName()) && defaultRole.getName().equals(defaultRole)) {
			Search search = new Search(DefaultRole.class);
			search.addFilterEqual("name", defaultRole.getName().trim());
			Integer count = defaultRoleRepository.count(search);
			if (count > 0) {
				throw new GeneralException(Messages.getMessage("defrole_name_exist", defaultRole.getName().trim()));
			}
		}

		MyBeanUtils.copyProperties(defaultRole, po);
		po.setModifyDate(new Date());
		defaultRoleRepository.merge(po);
	}

	@Transactional
	public void deleteDefaultRole(String id) throws GeneralException {
		DefaultRole po = defaultRoleRepository.find(id);
		if (!po.getEditable()) {
			throw new GeneralException(Messages.getMessage("defrole_cant_edit"));
		}

		defaultRoleRepository.removeById(id);
	}

}
