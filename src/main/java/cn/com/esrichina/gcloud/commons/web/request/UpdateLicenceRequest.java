package cn.com.esrichina.gcloud.commons.web.request;

import org.springframework.web.multipart.MultipartFile;

public class UpdateLicenceRequest {
	private MultipartFile licenceFile;

	public MultipartFile getLicenceFile() {
		return licenceFile;
	}

	public void setLicenceFile(MultipartFile licenceFile) {
		this.licenceFile = licenceFile;
	}
}
