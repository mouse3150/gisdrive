package cn.com.esrichina.gcloud.commons.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.esrichina.commons.exception.GeneralException;

@Service("SchedulerService")
public class SchedulerService {

//	@Resource
//	private CacheSettingService cacheSettingService;
//
//	@Resource
//	private ImageTypeService imageTypeService;
//
//	@Resource
//	private TaskManager taskManager;
//
//	public void checkCaches() {
//		List<CacheSetting> cacheSettings = cacheSettingService.getAllCacheSettings();
//		for (CacheSetting cacheSetting : cacheSettings) {
//			if (cacheSetting.getStatus().equals(CacheSetting.STATUS_NORMAL) && cacheSetting.getCacheNum() != cacheSetting.getCurrentNum()) {
//				// TODO 调用task处理吧
//				try {
//					CacheSetting targetCacheSetting = cacheSettingService.updateCacheSettingStatus(cacheSetting.getId(), CacheSetting.STATUS_PROCESSING);
//					taskManager.processCacheTask(targetCacheSetting);
//				} catch (GeneralException e) {
//					// TODO 理论上永远不会到这里
//					if (MyApp.debug) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
}
