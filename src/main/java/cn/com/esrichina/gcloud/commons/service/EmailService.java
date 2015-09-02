package cn.com.esrichina.gcloud.commons.service;

import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.com.esrichina.commons.utils.ConfigContext;
import cn.com.esrichina.gcloud.commons.domain.EmailHistory;
import cn.com.esrichina.gcloud.commons.domain.repository.EmailHistoryRepository;
import cn.com.esrichina.gcloud.commons.dto.EmailWrapper;
import cn.com.esrichina.gcloud.security.domain.User;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Resource
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Resource
	private EmailHistoryRepository emailHistoryRepository;

	private Boolean enable = ConfigContext.getInstance().getBoolean("mail.enable");

	@Resource(name = "siteCreateThreadPool")
	private ThreadPoolTaskExecutor createAGSSiteTaskExecutor;

	public String getParsedTemplate(String view, Object data) throws ServiceException {
		try {
			StringWriter writer = new StringWriter();
			freeMarkerConfigurer.getConfiguration().getTemplate(view).process(data, writer);
			writer.close();
			return writer.toString();
		} catch (Exception e) {
			throw new ServiceException("渲染模板出错，模板路径：" + view + "；数据为：" + data.toString(), e);
		}
	}

	public void sendAccountApplySuccessEmail() {

	}

	public void sendRegisterSuccessEmail(User user) {
		String title = getParsedTemplate("email/registerSuccess/subject.ftl", user);
		String content = getParsedTemplate("email/registerSuccess/content.ftl", user);

		sendEmail(user.getEmail(), title, content);
	}

	public void sendForgetPSWEmail(User user, Map<String, String> info) {
		String title = getParsedTemplate("email/psw/pswhead.ftl", info);
		String content = getParsedTemplate("email/psw/pswbody.ftl", info);

		sendEmail(user.getEmail(), title, content);
	}

	public void sendEmail(EmailWrapper emailWrapper) {
		String title = getParsedTemplate(emailWrapper.getTemplatePath() + "/subject.ftl", emailWrapper);
		String content = getParsedTemplate(emailWrapper.getTemplatePath() + "/content.ftl", emailWrapper);

		sendEmail(emailWrapper.getEmail(), title, content);
	}

	public void sendEmail(String to, String subject, String content) {
		if (enable) {
			createAGSSiteTaskExecutor.execute(new sendEmailTask(to, subject, content));
		}
	}

	class sendEmailTask implements Runnable {

		private String to;

		private String subject;

		private String content;

		public sendEmailTask(String to, String subject, String content) {
			super();
			this.to = to;
			this.subject = subject;
			this.content = content;
		}

		@Override
		public void run() {
			EmailHistory history = new EmailHistory();
			history.setTitle(subject);
			history.setCreateDate(new Date());
			try {
				HtmlEmail email = new HtmlEmail();
				email.setCharset("utf-8");
				email.addTo(to);
				email.setSubject(subject);
				email.setHtmlMsg(content);
				email.setFrom(ConfigContext.getInstance().getString("mail.from"));
				email.setHostName(ConfigContext.getInstance().getString("mail.hostname"));
				email.setSmtpPort(Integer.parseInt(ConfigContext.getInstance().getString("mail.smptPort")));
				email.setAuthenticator(new DefaultAuthenticator(ConfigContext.getInstance().getString("mail.username"), ConfigContext.getInstance().getString("mail.password")));
				email.send();
				history.setSuccess(true);
			} catch (EmailException e) {
				logger.error("发送邮件<" + subject + ">失败", e);
				history.setErrorMsg(e.getMessage());
				history.setSuccess(false);
			}
			emailHistoryRepository.save(history);
		}

	}
}
