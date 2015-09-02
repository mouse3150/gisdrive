package cn.com.esrichina.gcloud.commons.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.esrichina.commons.exception.GeneralException;
import cn.com.esrichina.gcloud.commons.ConfigRepository;
import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.commons.domain.Config;
import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.genericdao.search.Search;

@Service
public class ConfigService {

	@Resource
	private ConfigRepository configRepository;

	public Config getConfigByKey(String key) {

		Search search = new Search(Config.class);
		search.addFilterEqual("key", key);
		try {
			Config config = configRepository.searchUnique(search);

			if (config == null) {
				return null;
			}
			return config;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getConfigValueByKey(String key) {
		Config config = getConfigByKey(key);

		if (config == null) {
			return null;
		}

		return config.getValue();

	}

	@Transactional
	public void updateConfig(Config config) throws GeneralException {
		Config po = getConfigByKey(config.getKey());
		if (po == null) {
			throw new GeneralException(Messages.getMessage("config_not_exist", config.getKey()));
		}

		MyBeanUtils.copyProperties(config, po);
		configRepository.save(po);
	}

	@Transactional
	public void updateConfigs(List<Config> configs) throws GeneralException {
		for (Config config : configs) {
			updateConfig(config);
		}
	}

	public List<Config> getAllConfig() {
		return configRepository.findAll();
	}
}
