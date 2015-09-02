package cn.com.esrichina.gcloud.commons.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.commons.domain.AlertMsg;
import cn.com.esrichina.gcloud.commons.domain.repository.AlertMsgRepository;
import cn.com.esrichina.genericdao.search.Search;
import cn.com.esrichina.genericdao.search.SearchResult;

@Service
public class AlertMsgService {

	@Resource
	private AlertMsgRepository alertMsgRepository;

	public SearchResult<AlertMsg> searchMsgs(Search search) {
		return alertMsgRepository.searchAndCount(search);
	}

	@Transactional
	public void pushAlertMsg(String targetType, String targetId, String targetName, String level, String msgKey) {
		String msgDetail = Messages.getMessage(msgKey);
		pushAlertMsg(targetType, targetId, targetName, level, msgKey, msgDetail);
	}

	@Transactional
	public void pushAlertMsg(String targetType, String targetId, String targetName, String level, String msgKey, String... params) {
		String msgDetail = Messages.getMessage(msgKey, params);
		pushAlertMsg(targetType, targetId, targetName, level, msgKey, msgDetail);
	}

	@Transactional
	private void pushAlertMsg(String targetType, String targetId, String targetName, String level, String msgKey, String msgDetail) {
		AlertMsg msg = getNewAlertMsg(targetType, targetId, msgKey);
		if (msg == null) {
			msg = new AlertMsg();
			msg.setCreateDate(new Date());
			msg.setModifyDate(new Date());
			msg.setLevel(level);
			msg.setMsgKey(msgKey);
			msg.setMsgDetail(msgDetail);
			msg.setStatus(AlertMsg.STATUS_NEW);
			msg.setTargetId(targetId);
			msg.setTargetName(targetName);
			msg.setTargetType(targetType);
			alertMsgRepository.save(msg);
		} else {
			msg.setTargetName(targetName);
			msg.setLevel(level);
			msg.setMsgDetail(msgDetail);
			msg.setModifyDate(new Date());
			alertMsgRepository.merge(msg);
		}
	}

	@Transactional
	public void resolveAlertMsg(String id) {
		AlertMsg msg = alertMsgRepository.find(id);
		msg.setStatus(AlertMsg.STATUS_RESOLVED);
		alertMsgRepository.merge(msg);
	}

	@Transactional
	public void resolveAlertMsg(String targetType, String targetId, String msgKey) {
		Search search = new Search(AlertMsg.class);
		search.addFilterEqual("targetType", targetType);
		search.addFilterEqual("targetId", targetId);
		search.addFilterEqual("msgKey", msgKey);
		search.addFilterEqual("status", AlertMsg.STATUS_NEW);
		List<AlertMsg> msgs = alertMsgRepository.search(search);
		for (AlertMsg alertMsg : msgs) {
			alertMsg.setStatus(AlertMsg.STATUS_RESOLVED);
			alertMsgRepository.merge(alertMsg);
		}
	}

	@Transactional
	public void removeAlertMsg(String targetType, String targetId, String msgKey) {
		Search search = new Search(AlertMsg.class);
		search.addFilterEqual("targetType", targetType);
		search.addFilterEqual("targetId", targetId);
		search.addFilterEqual("msgKey", msgKey);
		search.addFilterEqual("status", AlertMsg.STATUS_NEW);
		List<AlertMsg> msgs = alertMsgRepository.search(search);
		for (AlertMsg alertMsg : msgs) {
			alertMsgRepository.remove(alertMsg);
		}
	}

	public AlertMsg getNewAlertMsg(String targetType, String targetId, String msgKey) {
		Search search = new Search(AlertMsg.class);
		search.addFilterEqual("targetType", targetType);
		search.addFilterEqual("targetId", targetId);
		search.addFilterEqual("status", AlertMsg.STATUS_NEW);
		search.addSort("createDate", true);
		List<AlertMsg> msgs = alertMsgRepository.search(search);
		if (msgs == null || msgs.size() == 0) {
			return null;
		} else {
			return msgs.get(0);
		}
	}

	public Integer getInfoCount() {
		Search search = new Search(AlertMsg.class);
		search.addFilterEqual("status", AlertMsg.STATUS_NEW);
		search.addFilterEqual("level", AlertMsg.LEVEL_INFO);
		return alertMsgRepository.count(search);
	}

	public Integer getWarnCount() {
		Search search = new Search(AlertMsg.class);
		search.addFilterEqual("status", AlertMsg.STATUS_NEW);
		search.addFilterEqual("level", AlertMsg.LEVEL_WARN);
		return alertMsgRepository.count(search);
	}

	public Integer getErrorCount() {
		Search search = new Search(AlertMsg.class);
		search.addFilterEqual("status", AlertMsg.STATUS_NEW);
		search.addFilterEqual("level", AlertMsg.LEVEL_ERROR);
		return alertMsgRepository.count(search);
	}

	public Integer getResolvedCount() {
		Search search = new Search(AlertMsg.class);
		search.addFilterEqual("status", AlertMsg.STATUS_RESOLVED);
		return alertMsgRepository.count(search);
	}
}
